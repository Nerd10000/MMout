/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ClipBlockStateContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public interface VibrationSystem {
/*  52 */   public static final List<ResourceKey<GameEvent>> RESONANCE_EVENTS = List.of((ResourceKey<GameEvent>[])new ResourceKey[] { 
/*  52 */         GameEvent.RESONATE_1
/*  53 */         .key(), GameEvent.RESONATE_2.key(), GameEvent.RESONATE_3.key(), GameEvent.RESONATE_4.key(), GameEvent.RESONATE_5.key(), GameEvent.RESONATE_6
/*  54 */         .key(), GameEvent.RESONATE_7.key(), GameEvent.RESONATE_8.key(), GameEvent.RESONATE_9.key(), GameEvent.RESONATE_10.key(), 
/*  54 */         GameEvent.RESONATE_11
/*  55 */         .key(), GameEvent.RESONATE_12.key(), GameEvent.RESONATE_13.key(), GameEvent.RESONATE_14.key(), GameEvent.RESONATE_15.key() });
/*     */   
/*     */   public static final int NO_VIBRATION_FREQUENCY = 0;
/*     */   
/*     */   public static final ToIntFunction<ResourceKey<GameEvent>> VIBRATION_FREQUENCY_FOR_EVENT;
/*     */   
/*     */   static {
/*  60 */     VIBRATION_FREQUENCY_FOR_EVENT = (ToIntFunction<ResourceKey<GameEvent>>)Util.make(new Reference2IntOpenHashMap(), map -> {
/*     */           map.defaultReturnValue(0);
/*     */           map.put(GameEvent.STEP.key(), 1);
/*     */           map.put(GameEvent.SWIM.key(), 1);
/*     */           map.put(GameEvent.FLAP.key(), 1);
/*     */           map.put(GameEvent.PROJECTILE_LAND.key(), 2);
/*     */           map.put(GameEvent.HIT_GROUND.key(), 2);
/*     */           map.put(GameEvent.SPLASH.key(), 2);
/*     */           map.put(GameEvent.BOUNCE.key(), 2);
/*     */           map.put(GameEvent.ITEM_INTERACT_FINISH.key(), 3);
/*     */           map.put(GameEvent.PROJECTILE_SHOOT.key(), 3);
/*     */           map.put(GameEvent.INSTRUMENT_PLAY.key(), 3);
/*     */           map.put(GameEvent.ENTITY_ACTION.key(), 4);
/*     */           map.put(GameEvent.ELYTRA_GLIDE.key(), 4);
/*     */           map.put(GameEvent.UNEQUIP.key(), 4);
/*     */           map.put(GameEvent.ENTITY_DISMOUNT.key(), 5);
/*     */           map.put(GameEvent.EQUIP.key(), 5);
/*     */           map.put(GameEvent.ENTITY_INTERACT.key(), 6);
/*     */           map.put(GameEvent.SHEAR.key(), 6);
/*     */           map.put(GameEvent.ENTITY_MOUNT.key(), 6);
/*     */           map.put(GameEvent.ENTITY_DAMAGE.key(), 7);
/*     */           map.put(GameEvent.DRINK.key(), 8);
/*     */           map.put(GameEvent.EAT.key(), 8);
/*     */           map.put(GameEvent.CONTAINER_CLOSE.key(), 9);
/*     */           map.put(GameEvent.BLOCK_CLOSE.key(), 9);
/*     */           map.put(GameEvent.BLOCK_DEACTIVATE.key(), 9);
/*     */           map.put(GameEvent.BLOCK_DETACH.key(), 9);
/*     */           map.put(GameEvent.CONTAINER_OPEN.key(), 10);
/*     */           map.put(GameEvent.BLOCK_OPEN.key(), 10);
/*     */           map.put(GameEvent.BLOCK_ACTIVATE.key(), 10);
/*     */           map.put(GameEvent.BLOCK_ATTACH.key(), 10);
/*     */           map.put(GameEvent.PRIME_FUSE.key(), 10);
/*     */           map.put(GameEvent.NOTE_BLOCK_PLAY.key(), 10);
/*     */           map.put(GameEvent.BLOCK_CHANGE.key(), 11);
/*     */           map.put(GameEvent.BLOCK_DESTROY.key(), 12);
/*     */           map.put(GameEvent.FLUID_PICKUP.key(), 12);
/*     */           map.put(GameEvent.BLOCK_PLACE.key(), 13);
/*     */           map.put(GameEvent.FLUID_PLACE.key(), 13);
/*     */           map.put(GameEvent.ENTITY_PLACE.key(), 14);
/*     */           map.put(GameEvent.LIGHTNING_STRIKE.key(), 14);
/*     */           map.put(GameEvent.TELEPORT.key(), 14);
/*     */           map.put(GameEvent.ENTITY_DIE.key(), 15);
/*     */           map.put(GameEvent.EXPLODE.key(), 15);
/*     */           for (int i = 1; i <= 15; i++)
/*     */             map.put(getResonanceEventByFrequency(i), i); 
/*     */         });
/*     */   }
/*     */   
/*     */   static int getGameEventFrequency(Holder<GameEvent> event) {
/* 140 */     return ((Integer)event.unwrapKey().map(VibrationSystem::getGameEventFrequency).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */   
/*     */   static int getGameEventFrequency(ResourceKey<GameEvent> event) {
/* 145 */     return VIBRATION_FREQUENCY_FOR_EVENT.applyAsInt(event);
/*     */   }
/*     */   
/*     */   static ResourceKey<GameEvent> getResonanceEventByFrequency(int vibrationFrequency) {
/* 149 */     return RESONANCE_EVENTS.get(vibrationFrequency - 1);
/*     */   }
/*     */   
/*     */   static int getRedstoneStrengthForDistance(float distance, int listenerRadius) {
/* 153 */     double powerScale = 15.0D / listenerRadius;
/* 154 */     return Math.max(1, 15 - Mth.floor(powerScale * distance));
/*     */   }
/*     */   
/*     */   Data getVibrationData();
/*     */   
/*     */   User getVibrationUser();
/*     */   
/*     */   public static final class Data {
/*     */     public static final Codec<Data> CODEC;
/*     */     
/*     */     public static final String NBT_TAG_KEY = "listener";
/*     */     
/*     */     private VibrationInfo currentVibration;
/*     */     
/*     */     private int travelTimeInTicks;
/*     */     
/*     */     private final VibrationSelector selectionStrategy;
/*     */     
/*     */     private boolean reloadVibrationParticle;
/*     */     
/*     */     static {
/* 161 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)VibrationSelector.CODEC.fieldOf("selector").forGetter(Data::getSelectionStrategy), (App)ExtraCodecs.optionalAlwaysPresentFieldOf(ExtraCodecs.NON_NEGATIVE_INT, "event_delay", Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks)).apply((Applicative)i, ()));
/*     */     }
/*     */     
/*     */     private Data(VibrationInfo currentVibration, VibrationSelector selectionStrategy, int travelTimeInTicks, boolean reloadVibrationParticle) {
/* 175 */       this.currentVibration = currentVibration;
/* 176 */       this.travelTimeInTicks = travelTimeInTicks;
/* 177 */       this.selectionStrategy = selectionStrategy;
/* 178 */       this.reloadVibrationParticle = reloadVibrationParticle;
/*     */     }
/*     */     
/*     */     public Data() {
/* 182 */       this(null, new VibrationSelector(), 0, false);
/*     */     }
/*     */     
/*     */     public VibrationSelector getSelectionStrategy() {
/* 186 */       return this.selectionStrategy;
/*     */     }
/*     */     
/*     */     public VibrationInfo getCurrentVibration() {
/* 190 */       return this.currentVibration;
/*     */     }
/*     */     
/*     */     public void setCurrentVibration(VibrationInfo currentVibration) {
/* 194 */       this.currentVibration = currentVibration;
/*     */     }
/*     */     
/*     */     public int getTravelTimeInTicks() {
/* 198 */       return this.travelTimeInTicks;
/*     */     }
/*     */     
/*     */     public void setTravelTimeInTicks(int travelTimeInTicks) {
/* 202 */       this.travelTimeInTicks = travelTimeInTicks;
/*     */     }
/*     */     
/*     */     public void decrementTravelTime() {
/* 206 */       this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
/*     */     }
/*     */     
/*     */     public boolean shouldReloadVibrationParticle() {
/* 210 */       return this.reloadVibrationParticle;
/*     */     }
/*     */     
/*     */     public void setReloadVibrationParticle(boolean reloadVibrationParticle) {
/* 214 */       this.reloadVibrationParticle = reloadVibrationParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Listener implements GameEventListener {
/*     */     private final VibrationSystem system;
/*     */     
/*     */     public Listener(VibrationSystem system) {
/* 225 */       this.system = system;
/*     */     }
/*     */     
/*     */     public PositionSource getListenerSource() {
/* 230 */       return this.system.getVibrationUser().getPositionSource();
/*     */     }
/*     */     
/*     */     public int getListenerRadius() {
/* 235 */       return this.system.getVibrationUser().getListenerRadius();
/*     */     }
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 240 */       VibrationSystem.Data data = this.system.getVibrationData();
/* 241 */       VibrationSystem.User user = this.system.getVibrationUser();
/* 244 */       if (data.getCurrentVibration() != null)
/* 245 */         return false; 
/* 248 */       if (!user.isValidVibration(event, context))
/* 249 */         return false; 
/* 252 */       Optional<Vec3> listenerSourcePos = user.getPositionSource().getPosition((Level)level);
/* 254 */       if (listenerSourcePos.isEmpty())
/* 255 */         return false; 
/* 258 */       Vec3 destination = listenerSourcePos.get();
/* 261 */       if (!user.canReceiveVibration(level, BlockPos.containing((Position)sourcePosition), event, context))
/* 262 */         return false; 
/* 265 */       if (isOccluded((Level)level, sourcePosition, destination))
/* 266 */         return false; 
/* 269 */       scheduleVibration(level, data, event, context, sourcePosition, destination);
/* 271 */       return true;
/*     */     }
/*     */     
/*     */     public void forceScheduleVibration(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin) {
/* 275 */       this.system.getVibrationUser().getPositionSource().getPosition((Level)level).ifPresent(p -> scheduleVibration(level, this.system.getVibrationData(), event, context, origin, p));
/*     */     }
/*     */     
/*     */     private void scheduleVibration(ServerLevel level, VibrationSystem.Data data, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin, Vec3 dest) {
/* 279 */       data.selectionStrategy.addCandidate(new VibrationInfo(event, (float)origin.distanceTo(dest), origin, context.sourceEntity()), level.getGameTime());
/*     */     }
/*     */     
/*     */     public static float distanceBetweenInBlocks(BlockPos origin, BlockPos dest) {
/* 296 */       return (float)Math.sqrt(origin.distSqr((Vec3i)dest));
/*     */     }
/*     */     
/*     */     private static boolean isOccluded(Level level, Vec3 origin, Vec3 dest) {
/* 305 */       Vec3 from = new Vec3(Mth.floor(origin.x) + 0.5D, Mth.floor(origin.y) + 0.5D, Mth.floor(origin.z) + 0.5D);
/* 310 */       Vec3 to = new Vec3(Mth.floor(dest.x) + 0.5D, Mth.floor(dest.y) + 0.5D, Mth.floor(dest.z) + 0.5D);
/* 313 */       for (Direction direction : Direction.values()) {
/* 314 */         Vec3 nudgedSource = from.relative(direction, 9.999999747378752E-6D);
/* 315 */         if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, state -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK)
/* 316 */           return false; 
/*     */       } 
/* 319 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Ticker {
/*     */     static void tick(Level level, VibrationSystem.Data data, VibrationSystem.User user) {
/*     */       ServerLevel serverLevel;
/* 332 */       if (level instanceof ServerLevel) {
/* 332 */         serverLevel = (ServerLevel)level;
/*     */       } else {
/*     */         return;
/*     */       } 
/* 336 */       if (data.currentVibration == null)
/* 337 */         trySelectAndScheduleVibration(serverLevel, data, user); 
/* 340 */       if (data.currentVibration == null)
/*     */         return; 
/* 344 */       boolean hasChanged = (data.getTravelTimeInTicks() > 0);
/* 345 */       tryReloadVibrationParticle(serverLevel, data, user);
/* 346 */       data.decrementTravelTime();
/* 348 */       if (data.getTravelTimeInTicks() <= 0)
/* 349 */         hasChanged = receiveVibration(serverLevel, data, user, data.currentVibration); 
/* 352 */       if (hasChanged)
/* 353 */         user.onDataChanged(); 
/*     */     }
/*     */     
/*     */     private static void trySelectAndScheduleVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user) {
/* 358 */       data.getSelectionStrategy().chosenCandidate(serverLevel.getGameTime()).ifPresent(context -> {
/*     */             data.setCurrentVibration(context);
/*     */             Vec3 origin = context.pos();
/*     */             data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(context.distance()));
/*     */             serverLevel.sendParticles((ParticleOptions)new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()), origin.x, origin.y, origin.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */             user.onDataChanged();
/*     */             data.getSelectionStrategy().startOver();
/*     */           });
/*     */     }
/*     */     
/*     */     private static void tryReloadVibrationParticle(ServerLevel level, VibrationSystem.Data data, VibrationSystem.User user) {
/* 369 */       if (!data.shouldReloadVibrationParticle())
/*     */         return; 
/* 373 */       if (data.currentVibration == null) {
/* 374 */         data.setReloadVibrationParticle(false);
/*     */         return;
/*     */       } 
/* 378 */       Vec3 origin = data.currentVibration.pos();
/* 379 */       PositionSource positionSource = user.getPositionSource();
/* 380 */       Vec3 destination = positionSource.getPosition((Level)level).orElse(origin);
/* 381 */       int travelTimeInTicks = data.getTravelTimeInTicks();
/* 383 */       int initialTravelTime = user.calculateTravelTimeInTicks(data.currentVibration.distance());
/* 384 */       double alpha = 1.0D - travelTimeInTicks / initialTravelTime;
/* 386 */       double newInitialX = Mth.lerp(alpha, origin.x, destination.x);
/* 387 */       double newInitialY = Mth.lerp(alpha, origin.y, destination.y);
/* 388 */       double newInitialZ = Mth.lerp(alpha, origin.z, destination.z);
/* 390 */       boolean particleWasSent = (level.sendParticles((ParticleOptions)new VibrationParticleOption(positionSource, travelTimeInTicks), newInitialX, newInitialY, newInitialZ, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0);
/* 392 */       if (particleWasSent)
/* 393 */         data.setReloadVibrationParticle(false); 
/*     */     }
/*     */     
/*     */     private static boolean receiveVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, VibrationInfo currentVibration) {
/* 398 */       BlockPos origin = BlockPos.containing((Position)currentVibration.pos());
/* 399 */       BlockPos destination = user.getPositionSource().getPosition((Level)serverLevel).map(BlockPos::containing).orElse(origin);
/* 403 */       if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking((Level)serverLevel, destination))
/* 404 */         return false; 
/* 407 */       user.onReceiveVibration(serverLevel, origin, currentVibration
/*     */           
/* 410 */           .gameEvent(), currentVibration
/* 411 */           .getEntity(serverLevel).orElse(null), currentVibration
/* 412 */           .getProjectileOwner(serverLevel).orElse(null), 
/* 413 */           VibrationSystem.Listener.distanceBetweenInBlocks(origin, destination));
/* 417 */       data.setCurrentVibration(null);
/* 418 */       return true;
/*     */     }
/*     */     
/*     */     private static boolean areAdjacentChunksTicking(Level level, BlockPos listenerPos) {
/* 422 */       ChunkPos listenerChunkPos = ChunkPos.containing(listenerPos);
/* 424 */       for (int x = listenerChunkPos.x() - 1; x <= listenerChunkPos.x() + 1; x++) {
/* 425 */         for (int z = listenerChunkPos.z() - 1; z <= listenerChunkPos.z() + 1; z++) {
/* 426 */           if (!level.shouldTickBlocksAt(ChunkPos.pack(x, z)) || level.getChunkSource().getChunkNow(x, z) == null)
/* 427 */             return false; 
/*     */         } 
/*     */       } 
/* 432 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface User {
/*     */     int getListenerRadius();
/*     */     
/*     */     PositionSource getPositionSource();
/*     */     
/*     */     boolean canReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, GameEvent.Context param1Context);
/*     */     
/*     */     void onReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, Entity param1Entity1, Entity param1Entity2, float param1Float);
/*     */     
/*     */     default TagKey<GameEvent> getListenableEvents() {
/* 460 */       return GameEventTags.VIBRATIONS;
/*     */     }
/*     */     
/*     */     default boolean canTriggerAvoidVibration() {
/* 464 */       return false;
/*     */     }
/*     */     
/*     */     default boolean requiresAdjacentChunksToBeTicking() {
/* 468 */       return false;
/*     */     }
/*     */     
/*     */     default int calculateTravelTimeInTicks(float distanceToDestination) {
/* 472 */       return Mth.floor(distanceToDestination);
/*     */     }
/*     */     
/*     */     default boolean isValidVibration(Holder<GameEvent> event, GameEvent.Context context) {
/* 487 */       if (!event.is(getListenableEvents()))
/* 488 */         return false; 
/* 491 */       Entity sourceEntity = context.sourceEntity();
/* 493 */       if (sourceEntity != null) {
/* 494 */         if (sourceEntity.isSpectator())
/* 495 */           return false; 
/* 498 */         if (sourceEntity.isSteppingCarefully() && event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
/* 499 */           if (canTriggerAvoidVibration() && sourceEntity instanceof ServerPlayer) {
/* 499 */             ServerPlayer player = (ServerPlayer)sourceEntity;
/* 500 */             CriteriaTriggers.AVOID_VIBRATION.trigger(player);
/*     */           } 
/* 503 */           return false;
/*     */         } 
/* 506 */         if (sourceEntity.dampensVibrations())
/* 507 */           return false; 
/*     */       } 
/* 511 */       if (context.affectedState() != null)
/* 512 */         return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS); 
/* 515 */       return true;
/*     */     }
/*     */     
/*     */     default void onDataChanged() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */