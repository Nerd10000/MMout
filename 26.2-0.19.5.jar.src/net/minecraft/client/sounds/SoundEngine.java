/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.audio.Channel;
/*     */ import com.mojang.blaze3d.audio.DeviceList;
/*     */ import com.mojang.blaze3d.audio.DeviceTracker;
/*     */ import com.mojang.blaze3d.audio.Library;
/*     */ import com.mojang.blaze3d.audio.Listener;
/*     */ import com.mojang.blaze3d.audio.ListenerTransform;
/*     */ import com.mojang.blaze3d.audio.SoundBuffer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.client.resources.sounds.TickableSoundInstance;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.MarkerFactory;
/*     */ 
/*     */ public class SoundEngine {
/*  45 */   private static final Marker MARKER = MarkerFactory.getMarker("SOUNDS");
/*     */   
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final float PITCH_MIN = 0.5F;
/*     */   
/*     */   private static final float PITCH_MAX = 2.0F;
/*     */   
/*     */   private static final float VOLUME_MIN = 0.0F;
/*     */   
/*     */   private static final float VOLUME_MAX = 1.0F;
/*     */   
/*     */   private static final int MIN_SOURCE_LIFETIME = 20;
/*     */   
/*  52 */   private static final Set<Identifier> ONLY_WARN_ONCE = Sets.newHashSet();
/*     */   
/*     */   public static final String MISSING_SOUND = "FOR THE DEBUG!";
/*     */   
/*     */   public static final String OPEN_AL_SOFT_PREFIX = "OpenAL Soft on ";
/*     */   
/*  57 */   public static final int OPEN_AL_SOFT_PREFIX_LENGTH = "OpenAL Soft on ".length();
/*     */   
/*     */   private final SoundManager soundManager;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private boolean loaded;
/*     */   
/*  64 */   private final Library library = new Library();
/*     */   
/*  65 */   private final Listener listener = this.library.getListener();
/*     */   
/*     */   private final SoundBufferLibrary soundBuffers;
/*     */   
/*  68 */   private final SoundEngineExecutor executor = new SoundEngineExecutor();
/*     */   
/*  70 */   private final ChannelAccess channelAccess = new ChannelAccess(this.library, (Executor)this.executor);
/*     */   
/*     */   private int tickCount;
/*     */   
/*     */   private DeviceList lastSeenDevices;
/*     */   
/*  75 */   private final DeviceTracker deviceTracker = Library.createDeviceTracker();
/*     */   
/*  77 */   private final Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = Maps.newHashMap();
/*     */   
/*  78 */   private final Multimap<SoundSource, SoundInstance> instanceBySource = (Multimap<SoundSource, SoundInstance>)HashMultimap.create();
/*     */   
/*     */   private final Object2FloatMap<SoundSource> gainBySource;
/*     */   
/*     */   private final List<TickableSoundInstance> tickingSounds;
/*     */   
/*     */   private final Map<SoundInstance, Integer> queuedSounds;
/*     */   
/*     */   private final Map<SoundInstance, Integer> soundDeleteTime;
/*     */   
/*     */   private final List<SoundEventListener> listeners;
/*     */   
/*     */   private final List<TickableSoundInstance> queuedTickableSounds;
/*     */   
/*     */   private final List<Sound> preloadQueue;
/*     */   
/*     */   public SoundEngine(SoundManager soundManager, Options options, ResourceProvider resourceProvider) {
/*  79 */     this.gainBySource = (Object2FloatMap<SoundSource>)Util.make(new Object2FloatOpenHashMap(), map -> map.defaultReturnValue(1.0F));
/*  81 */     this.tickingSounds = Lists.newArrayList();
/*  82 */     this.queuedSounds = Maps.newHashMap();
/*  83 */     this.soundDeleteTime = Maps.newHashMap();
/*  84 */     this.listeners = Lists.newArrayList();
/*  85 */     this.queuedTickableSounds = Lists.newArrayList();
/*  87 */     this.preloadQueue = Lists.newArrayList();
/*  90 */     this.soundManager = soundManager;
/*  91 */     this.options = options;
/*  92 */     this.soundBuffers = new SoundBufferLibrary(resourceProvider);
/*  94 */     this.lastSeenDevices = this.deviceTracker.currentDevices();
/*     */   }
/*     */   
/*     */   public void reload() {
/*  98 */     ONLY_WARN_ONCE.clear();
/*  99 */     for (SoundEvent sound : BuiltInRegistries.SOUND_EVENT) {
/* 100 */       if (sound != SoundEvents.EMPTY) {
/* 101 */         Identifier location = sound.location();
/* 102 */         if (this.soundManager.getSoundEvent(location) == null) {
/* 103 */           LOGGER.warn("Missing sound for event: {}", BuiltInRegistries.SOUND_EVENT.getKey(sound));
/* 104 */           ONLY_WARN_ONCE.add(location);
/*     */         } 
/*     */       } 
/*     */     } 
/* 108 */     destroy();
/* 109 */     loadLibrary();
/*     */   }
/*     */   
/*     */   private synchronized void loadLibrary() {
/* 113 */     if (this.loaded)
/*     */       return; 
/*     */     try {
/* 118 */       String soundDevice = (String)this.options.soundDevice().get();
/* 119 */       DeviceList currentDevices = this.deviceTracker.currentDevices();
/* 120 */       this.library.init(Options.isSoundDeviceDefault(soundDevice) ? null : soundDevice, currentDevices, ((Boolean)this.options.directionalAudio().get()).booleanValue());
/* 121 */       this.listener.reset();
/* 122 */       Objects.requireNonNull(this.preloadQueue);
/* 122 */       this.soundBuffers.preload(this.preloadQueue).thenRun(this.preloadQueue::clear);
/* 123 */       this.loaded = true;
/* 124 */       LOGGER.info(MARKER, "Sound engine started");
/* 125 */     } catch (RuntimeException e) {
/* 126 */       LOGGER.error(MARKER, "Error starting SoundSystem. Turning off sounds & music", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshCategoryVolume(SoundSource source) {
/* 131 */     if (!this.loaded)
/*     */       return; 
/* 135 */     this.instanceToChannel.forEach((soundInstance, channelHandle) -> {
/*     */           if (source == soundInstance.getSource() || source == SoundSource.MASTER) {
/*     */             float newVolume = calculateVolume(soundInstance);
/*     */             channelHandle.execute(());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 144 */     if (this.loaded) {
/* 145 */       stopAll();
/* 146 */       this.soundBuffers.clear();
/* 147 */       this.library.cleanup();
/* 148 */       this.loaded = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void emergencyShutdown() {
/* 153 */     if (this.loaded)
/* 154 */       this.library.cleanup(); 
/*     */   }
/*     */   
/*     */   public void stop(SoundInstance soundInstance) {
/* 159 */     if (this.loaded) {
/* 160 */       ChannelAccess.ChannelHandle handle = this.instanceToChannel.get(soundInstance);
/* 161 */       if (handle != null)
/* 162 */         handle.execute(Channel::stop); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateCategoryVolume(SoundSource source, float gain) {
/* 168 */     this.gainBySource.put(source, Mth.clamp(gain, 0.0F, 1.0F));
/* 169 */     refreshCategoryVolume(source);
/*     */   }
/*     */   
/*     */   public void stopAll() {
/* 173 */     if (this.loaded) {
/* 174 */       this.executor.shutDown();
/* 175 */       this.instanceToChannel.clear();
/* 176 */       this.channelAccess.clear();
/* 177 */       this.queuedSounds.clear();
/* 178 */       this.tickingSounds.clear();
/* 179 */       this.instanceBySource.clear();
/* 180 */       this.soundDeleteTime.clear();
/* 181 */       this.queuedTickableSounds.clear();
/* 182 */       this.gainBySource.clear();
/* 183 */       this.executor.startUp();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addEventListener(SoundEventListener listener) {
/* 188 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeEventListener(SoundEventListener listener) {
/* 192 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   private boolean shouldChangeDevice() {
/* 201 */     if (this.library.isCurrentDeviceDisconnected()) {
/* 202 */       LOGGER.info("Audio device was lost!");
/* 204 */       this.deviceTracker.forceRefresh();
/* 205 */       return true;
/*     */     } 
/* 208 */     this.deviceTracker.tick();
/* 210 */     boolean shouldChangeDevice = false;
/* 211 */     DeviceList currentDevices = this.deviceTracker.currentDevices();
/* 212 */     if (!currentDevices.equals(this.lastSeenDevices)) {
/* 213 */       String currentDeviceName = this.library.currentDeviceName();
/* 216 */       if (!currentDevices.allDevices().contains(currentDeviceName)) {
/* 217 */         LOGGER.info("Current audio device has disapeared!");
/* 218 */         shouldChangeDevice = true;
/*     */       } 
/* 221 */       String userSelectedDevice = (String)this.options.soundDevice().get();
/* 222 */       if (Options.isSoundDeviceDefault(userSelectedDevice)) {
/* 223 */         String newDefault = currentDevices.defaultDevice();
/* 225 */         if (!Objects.equals(currentDeviceName, newDefault)) {
/* 226 */           LOGGER.info("System default audio device has changed!");
/* 227 */           shouldChangeDevice = true;
/*     */         } 
/* 231 */       } else if (!Objects.equals(currentDeviceName, userSelectedDevice) && currentDevices.allDevices().contains(userSelectedDevice)) {
/* 232 */         LOGGER.info("Preferred audio device has become available!");
/* 233 */         shouldChangeDevice = true;
/*     */       } 
/* 236 */       this.lastSeenDevices = currentDevices;
/*     */     } 
/* 239 */     return shouldChangeDevice;
/*     */   }
/*     */   
/*     */   public void tick(boolean paused) {
/* 243 */     if (shouldChangeDevice())
/* 244 */       reload(); 
/* 246 */     if (!paused) {
/* 247 */       tickInGameSound();
/*     */     } else {
/* 249 */       tickMusicWhenPaused();
/*     */     } 
/* 251 */     this.channelAccess.scheduleTick();
/*     */   }
/*     */   
/*     */   private void tickInGameSound() {
/* 255 */     this.tickCount++;
/* 257 */     this.queuedTickableSounds.stream().filter(SoundInstance::canPlaySound).forEach(this::play);
/* 258 */     this.queuedTickableSounds.clear();
/* 261 */     for (Iterator<TickableSoundInstance> iterator1 = this.tickingSounds.iterator(); iterator1.hasNext(); ) {
/* 261 */       TickableSoundInstance instance = iterator1.next();
/* 262 */       if (!instance.canPlaySound())
/* 263 */         stop((SoundInstance)instance); 
/* 265 */       instance.tick();
/* 267 */       if (instance.isStopped()) {
/* 268 */         stop((SoundInstance)instance);
/*     */         continue;
/*     */       } 
/* 270 */       float volume = calculateVolume((SoundInstance)instance);
/* 271 */       float pitch = calculatePitch((SoundInstance)instance);
/* 272 */       Vec3 position = new Vec3(instance.getX(), instance.getY(), instance.getZ());
/* 273 */       ChannelAccess.ChannelHandle handle = this.instanceToChannel.get(instance);
/* 274 */       if (handle != null)
/* 275 */         handle.execute(channel -> {
/*     */               channel.setVolume(volume);
/*     */               channel.setPitch(pitch);
/*     */               channel.setSelfPosition(position);
/*     */             }); 
/*     */     } 
/* 285 */     Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator = this.instanceToChannel.entrySet().iterator();
/* 286 */     while (iterator.hasNext()) {
/* 287 */       Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry = iterator.next();
/* 289 */       ChannelAccess.ChannelHandle handle = entry.getValue();
/* 290 */       SoundInstance instance = entry.getKey();
/* 292 */       if (handle.isStopped()) {
/* 293 */         int minDeleteTime = ((Integer)this.soundDeleteTime.get(instance)).intValue();
/* 294 */         if (minDeleteTime <= this.tickCount) {
/* 295 */           if (shouldLoopManually(instance))
/* 296 */             this.queuedSounds.put(instance, Integer.valueOf(this.tickCount + instance.getDelay())); 
/* 298 */           iterator.remove();
/* 299 */           LOGGER.debug(MARKER, "Removed channel {} because it's not playing anymore", handle);
/* 300 */           this.soundDeleteTime.remove(instance);
/*     */           try {
/* 303 */             this.instanceBySource.remove(instance.getSource(), instance);
/* 304 */           } catch (RuntimeException runtimeException) {}
/* 308 */           if (instance instanceof TickableSoundInstance)
/* 309 */             this.tickingSounds.remove(instance); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 315 */     Iterator<Map.Entry<SoundInstance, Integer>> queueIterator = this.queuedSounds.entrySet().iterator();
/* 316 */     while (queueIterator.hasNext()) {
/* 317 */       Map.Entry<SoundInstance, Integer> next = queueIterator.next();
/* 319 */       if (this.tickCount >= ((Integer)next.getValue()).intValue()) {
/* 320 */         SoundInstance instance = next.getKey();
/* 323 */         if (instance instanceof TickableSoundInstance) {
/* 323 */           TickableSoundInstance tickableSoundInstance = (TickableSoundInstance)instance;
/* 324 */           tickableSoundInstance.tick();
/*     */         } 
/* 327 */         play(instance);
/* 328 */         queueIterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickMusicWhenPaused() {
/* 335 */     Iterator<Map.Entry<SoundInstance, ChannelAccess.ChannelHandle>> iterator = this.instanceToChannel.entrySet().iterator();
/* 336 */     while (iterator.hasNext()) {
/* 337 */       Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry = iterator.next();
/* 338 */       ChannelAccess.ChannelHandle handle = entry.getValue();
/* 339 */       SoundInstance instance = entry.getKey();
/* 340 */       if (instance.getSource() == SoundSource.MUSIC && handle.isStopped()) {
/* 341 */         iterator.remove();
/* 342 */         LOGGER.debug(MARKER, "Removed channel {} because it's not playing anymore", handle);
/* 343 */         this.soundDeleteTime.remove(instance);
/* 344 */         this.instanceBySource.remove(instance.getSource(), instance);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean requiresManualLooping(SoundInstance instance) {
/* 350 */     return (instance.getDelay() > 0);
/*     */   }
/*     */   
/*     */   private static boolean shouldLoopManually(SoundInstance instance) {
/* 354 */     return (instance.isLooping() && requiresManualLooping(instance));
/*     */   }
/*     */   
/*     */   private static boolean shouldLoopAutomatically(SoundInstance instance) {
/* 358 */     return (instance.isLooping() && !requiresManualLooping(instance));
/*     */   }
/*     */   
/*     */   public boolean isActive(SoundInstance instance) {
/* 362 */     if (!this.loaded)
/* 363 */       return false; 
/* 366 */     if (this.soundDeleteTime.containsKey(instance) && ((Integer)this.soundDeleteTime.get(instance)).intValue() <= this.tickCount)
/* 367 */       return true; 
/* 370 */     return this.instanceToChannel.containsKey(instance);
/*     */   }
/*     */   
/*     */   public PlayResult play(SoundInstance instance) {
/* 374 */     if (!this.loaded)
/* 375 */       return PlayResult.NOT_STARTED; 
/* 378 */     if (!instance.canPlaySound())
/* 379 */       return PlayResult.NOT_STARTED; 
/* 382 */     WeighedSoundEvents soundEvent = instance.resolve(this.soundManager);
/* 383 */     Identifier eventLocation = instance.getIdentifier();
/* 384 */     if (soundEvent == null) {
/* 385 */       if (ONLY_WARN_ONCE.add(eventLocation))
/* 386 */         LOGGER.warn(MARKER, "Unable to play unknown soundEvent: {}", eventLocation); 
/* 388 */       if (!SharedConstants.DEBUG_SUBTITLES)
/* 389 */         return PlayResult.NOT_STARTED; 
/* 392 */       soundEvent = new WeighedSoundEvents(eventLocation, "FOR THE DEBUG!");
/*     */     } 
/* 395 */     Sound sound = instance.getSound();
/* 397 */     if (sound == SoundManager.INTENTIONALLY_EMPTY_SOUND)
/* 398 */       return PlayResult.NOT_STARTED; 
/* 401 */     if (sound == SoundManager.EMPTY_SOUND) {
/* 402 */       if (ONLY_WARN_ONCE.add(eventLocation))
/* 403 */         LOGGER.warn(MARKER, "Unable to play empty soundEvent: {}", eventLocation); 
/* 405 */       return PlayResult.NOT_STARTED;
/*     */     } 
/* 408 */     float instanceVolume = instance.getVolume();
/* 409 */     float attenuationDistance = Math.max(instanceVolume, 1.0F) * sound.getAttenuationDistance();
/* 411 */     SoundSource soundSource = instance.getSource();
/* 412 */     float volume = calculateVolume(instanceVolume, soundSource);
/* 413 */     float pitch = calculatePitch(instance);
/* 415 */     SoundInstance.Attenuation attenuation = instance.getAttenuation();
/* 416 */     boolean isRelative = instance.isRelative();
/* 418 */     if (!this.listeners.isEmpty()) {
/* 419 */       float range = (isRelative || attenuation == SoundInstance.Attenuation.NONE) ? Float.POSITIVE_INFINITY : attenuationDistance;
/* 420 */       for (SoundEventListener listener : this.listeners)
/* 421 */         listener.onPlaySound(instance, soundEvent, range); 
/*     */     } 
/* 425 */     boolean startedSilently = false;
/* 426 */     if (volume == 0.0F)
/* 427 */       if (instance.canStartSilent() || soundSource == SoundSource.MUSIC) {
/* 428 */         startedSilently = true;
/*     */       } else {
/* 430 */         LOGGER.debug(MARKER, "Skipped playing sound {}, volume was zero.", sound.getLocation());
/* 431 */         return PlayResult.NOT_STARTED;
/*     */       }  
/* 435 */     Vec3 position = new Vec3(instance.getX(), instance.getY(), instance.getZ());
/* 437 */     boolean isLooping = shouldLoopAutomatically(instance);
/* 438 */     boolean isStreaming = sound.shouldStream();
/* 440 */     CompletableFuture<ChannelAccess.ChannelHandle> handleFuture = this.channelAccess.createHandle(sound.shouldStream() ? Library.Pool.STREAMING : Library.Pool.STATIC);
/* 441 */     ChannelAccess.ChannelHandle handle = handleFuture.join();
/* 442 */     if (handle == null) {
/* 443 */       if (SharedConstants.IS_RUNNING_IN_IDE)
/* 444 */         LOGGER.warn("Failed to create new sound handle"); 
/* 446 */       return PlayResult.NOT_STARTED;
/*     */     } 
/* 449 */     LOGGER.debug(MARKER, "Playing sound {} for event {}", sound.getLocation(), eventLocation);
/* 451 */     this.soundDeleteTime.put(instance, Integer.valueOf(this.tickCount + 20));
/* 452 */     this.instanceToChannel.put(instance, handle);
/* 453 */     this.instanceBySource.put(soundSource, instance);
/* 455 */     handle.execute(channel -> {
/*     */           channel.setPitch(pitch);
/*     */           channel.setVolume(volume);
/*     */           if (attenuation == SoundInstance.Attenuation.LINEAR) {
/*     */             channel.linearAttenuation(attenuationDistance);
/*     */           } else {
/*     */             channel.disableAttenuation();
/*     */           } 
/* 464 */           channel.setLooping((isLooping && !isStreaming));
/*     */           channel.setSelfPosition(position);
/*     */           channel.setRelative(isRelative);
/*     */         });
/* 469 */     if (!isStreaming) {
/* 470 */       this.soundBuffers.getCompleteBuffer(sound.getPath())
/* 471 */         .thenAccept(soundBuffer -> handle.execute(()));
/*     */     } else {
/* 477 */       this.soundBuffers.getStream(sound.getPath(), isLooping)
/* 478 */         .thenAccept(stream -> handle.execute(()));
/*     */     } 
/* 485 */     if (instance instanceof TickableSoundInstance) {
/* 485 */       TickableSoundInstance tickableSoundInstance = (TickableSoundInstance)instance;
/* 486 */       this.tickingSounds.add(tickableSoundInstance);
/*     */     } 
/* 488 */     if (startedSilently)
/* 489 */       return PlayResult.STARTED_SILENTLY; 
/* 491 */     return PlayResult.STARTED;
/*     */   }
/*     */   
/*     */   public void queueTickingSound(TickableSoundInstance tickableSoundInstance) {
/* 495 */     this.queuedTickableSounds.add(tickableSoundInstance);
/*     */   }
/*     */   
/*     */   public void requestPreload(Sound sound) {
/* 499 */     this.preloadQueue.add(sound);
/*     */   }
/*     */   
/*     */   private float calculatePitch(SoundInstance instance) {
/* 503 */     return Mth.clamp(instance.getPitch(), 0.5F, 2.0F);
/*     */   }
/*     */   
/*     */   private float calculateVolume(SoundInstance instance) {
/* 507 */     return calculateVolume(instance.getVolume(), instance.getSource());
/*     */   }
/*     */   
/*     */   private float calculateVolume(float volume, SoundSource source) {
/* 511 */     return Mth.clamp(volume, 0.0F, 1.0F) * Mth.clamp(this.options.getFinalSoundSourceVolume(source), 0.0F, 1.0F) * this.gainBySource.getFloat(source);
/*     */   }
/*     */   
/*     */   public void pauseAllExcept(SoundSource... ignoredSources) {
/* 515 */     if (!this.loaded)
/*     */       return; 
/* 519 */     for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> instance : this.instanceToChannel.entrySet()) {
/* 520 */       if (!List.<SoundSource>of(ignoredSources).contains(((SoundInstance)instance.getKey()).getSource()))
/* 521 */         ((ChannelAccess.ChannelHandle)instance.getValue()).execute(Channel::pause); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resume() {
/* 527 */     if (this.loaded)
/* 528 */       this.channelAccess.executeOnChannels(channels -> channels.forEach(Channel::unpause)); 
/*     */   }
/*     */   
/*     */   public void playDelayed(SoundInstance instance, int delay) {
/* 533 */     this.queuedSounds.put(instance, Integer.valueOf(this.tickCount + delay));
/*     */   }
/*     */   
/*     */   public void updateSource(Camera camera) {
/* 537 */     if (!this.loaded || !camera.isInitialized())
/*     */       return; 
/* 541 */     ListenerTransform transform = new ListenerTransform(camera.position(), new Vec3(camera.forwardVector()), new Vec3(camera.upVector()));
/* 542 */     this.executor.execute(() -> this.listener.setTransform(transform));
/*     */   }
/*     */   
/*     */   public void stop(Identifier sound, SoundSource source) {
/* 546 */     if (source != null) {
/* 547 */       for (SoundInstance instance : this.instanceBySource.get(source)) {
/* 548 */         if (sound == null || instance.getIdentifier().equals(sound))
/* 549 */           stop(instance); 
/*     */       } 
/* 553 */     } else if (sound == null) {
/* 554 */       stopAll();
/*     */     } else {
/* 556 */       for (SoundInstance instance : this.instanceToChannel.keySet()) {
/* 557 */         if (instance.getIdentifier().equals(sound))
/* 558 */           stop(instance); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getChannelDebugString() {
/* 566 */     return this.library.getChannelDebugString();
/*     */   }
/*     */   
/*     */   public void getSoundCacheDebugStats(SoundBufferLibrary.DebugOutput output) {
/* 570 */     this.soundBuffers.enumerate(output);
/*     */   }
/*     */   
/*     */   public List<String> getAvailableSoundDevices() {
/* 574 */     return this.deviceTracker.currentDevices().allDevices();
/*     */   }
/*     */   
/*     */   public ListenerTransform getListenerTransform() {
/* 578 */     return this.listener.getTransform();
/*     */   }
/*     */   
/*     */   public enum PlayResult {
/* 582 */     STARTED, STARTED_SILENTLY, NOT_STARTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundEngine.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */