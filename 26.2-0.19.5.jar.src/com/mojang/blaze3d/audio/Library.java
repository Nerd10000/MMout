/*     */ package com.mojang.blaze3d.audio;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.HexFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.openal.AL;
/*     */ import org.lwjgl.openal.AL10;
/*     */ import org.lwjgl.openal.ALC;
/*     */ import org.lwjgl.openal.ALC10;
/*     */ import org.lwjgl.openal.ALC11;
/*     */ import org.lwjgl.openal.ALCCapabilities;
/*     */ import org.lwjgl.openal.ALCapabilities;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Library {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int NO_DEVICE = 0;
/*     */   
/*     */   private static final String NO_DEVICE_NAME = "(None)";
/*     */   
/*     */   private static final int DEFAULT_CHANNEL_COUNT = 30;
/*     */   
/*     */   private long currentDevice;
/*     */   
/*     */   public enum Pool {
/*  34 */     STATIC, STREAMING;
/*     */   }
/*     */   
/*  46 */   private String currentDeviceName = "(None)";
/*     */   
/*     */   private long context;
/*     */   
/*     */   private boolean supportsDisconnections;
/*     */   
/*  62 */   private static final ChannelPool EMPTY = new ChannelPool() {
/*     */       public Channel acquire() {
/*  65 */         return null;
/*     */       }
/*     */       
/*     */       public boolean release(Channel channel) {
/*  70 */         return false;
/*     */       }
/*     */       
/*     */       public void cleanup() {}
/*     */       
/*     */       public int getMaxCount() {
/*  79 */         return 0;
/*     */       }
/*     */       
/*     */       public int getUsedCount() {
/*  84 */         return 0;
/*     */       }
/*     */     };
/*     */   
/*     */   private static class CountingChannelPool implements ChannelPool {
/*     */     private final int limit;
/*     */     
/*  90 */     private final Set<Channel> activeChannels = Sets.newIdentityHashSet();
/*     */     
/*     */     public CountingChannelPool(int limit) {
/*  93 */       this.limit = limit;
/*     */     }
/*     */     
/*     */     public Channel acquire() {
/*  98 */       if (this.activeChannels.size() >= this.limit) {
/*  99 */         if (SharedConstants.IS_RUNNING_IN_IDE)
/* 100 */           Library.LOGGER.warn("Maximum sound pool size {} reached", Integer.valueOf(this.limit)); 
/* 102 */         return null;
/*     */       } 
/* 105 */       Channel channel = Channel.create();
/* 106 */       if (channel != null)
/* 107 */         this.activeChannels.add(channel); 
/* 110 */       return channel;
/*     */     }
/*     */     
/*     */     public boolean release(Channel channel) {
/* 115 */       if (!this.activeChannels.remove(channel))
/* 116 */         return false; 
/* 118 */       channel.destroy();
/* 119 */       return true;
/*     */     }
/*     */     
/*     */     public void cleanup() {
/* 124 */       this.activeChannels.forEach(Channel::destroy);
/* 125 */       this.activeChannels.clear();
/*     */     }
/*     */     
/*     */     public int getMaxCount() {
/* 130 */       return this.limit;
/*     */     }
/*     */     
/*     */     public int getUsedCount() {
/* 135 */       return this.activeChannels.size();
/*     */     }
/*     */   }
/*     */   
/* 139 */   private ChannelPool staticChannels = EMPTY;
/*     */   
/* 140 */   private ChannelPool streamingChannels = EMPTY;
/*     */   
/* 142 */   private final Listener listener = new Listener();
/*     */   
/*     */   public void init(String preferredDevice, DeviceList currentDevices, boolean useHrtf) {
/* 148 */     this.currentDeviceName = "(None)";
/* 150 */     this.currentDevice = openDeviceOrFallback(preferredDevice, currentDevices.defaultDevice());
/* 151 */     this.currentDeviceName = queryDeviceName(this.currentDevice);
/* 152 */     this.supportsDisconnections = false;
/* 154 */     ALCCapabilities alcCapabilities = ALC.createCapabilities(this.currentDevice);
/* 155 */     if (OpenAlUtil.checkALCError(this.currentDevice, "Get capabilities"))
/* 156 */       throw new IllegalStateException("Failed to get OpenAL capabilities"); 
/* 159 */     if (!alcCapabilities.OpenALC11)
/* 160 */       throw new IllegalStateException("OpenAL 1.1 not supported"); 
/* 163 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 164 */       IntBuffer attr = createAttributes(stack, (alcCapabilities.ALC_SOFT_HRTF && useHrtf));
/* 165 */       this.context = ALC10.alcCreateContext(this.currentDevice, attr);
/* 166 */       if (stack != null)
/* 166 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 168 */     if (OpenAlUtil.checkALCError(this.currentDevice, "Create context"))
/* 169 */       throw new IllegalStateException("Unable to create OpenAL context"); 
/* 172 */     ALC10.alcMakeContextCurrent(this.context);
/* 174 */     int totalChannelCount = getChannelCount();
/* 175 */     int streamingChannelCount = Mth.clamp((int)Mth.sqrt(totalChannelCount), 2, 8);
/* 176 */     int staticChannelCount = Mth.clamp(totalChannelCount - streamingChannelCount, 8, 255);
/* 178 */     this.staticChannels = new CountingChannelPool(staticChannelCount);
/* 179 */     this.streamingChannels = new CountingChannelPool(streamingChannelCount);
/* 181 */     ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
/* 182 */     OpenAlUtil.checkALError("Initialization");
/* 184 */     if (!alCapabilities.AL_EXT_source_distance_model)
/* 186 */       throw new IllegalStateException("AL_EXT_source_distance_model is not supported"); 
/* 188 */     AL10.alEnable(512);
/* 190 */     if (!alCapabilities.AL_EXT_LINEAR_DISTANCE)
/* 191 */       throw new IllegalStateException("AL_EXT_LINEAR_DISTANCE is not supported"); 
/* 193 */     OpenAlUtil.checkALError("Enable per-source distance models");
/* 194 */     LOGGER.info("OpenAL initialized on device {}", this.currentDeviceName);
/* 198 */     this.supportsDisconnections = ALC10.alcIsExtensionPresent(this.currentDevice, "ALC_EXT_disconnect");
/*     */   }
/*     */   
/*     */   private IntBuffer createAttributes(MemoryStack stack, boolean enableHrtf) {
/* 202 */     int maxAttributes = 5;
/* 203 */     IntBuffer attr = stack.callocInt(11);
/* 205 */     int numHrtf = ALC10.alcGetInteger(this.currentDevice, 6548);
/* 206 */     if (numHrtf > 0) {
/* 207 */       attr.put(6546).put(enableHrtf ? 1 : 0);
/* 209 */       attr.put(6550).put(0);
/*     */     } 
/* 214 */     attr.put(6554).put(1);
/* 216 */     return attr.put(0).flip();
/*     */   }
/*     */   
/*     */   private int getChannelCount() {
/* 220 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 221 */       int size = ALC10.alcGetInteger(this.currentDevice, 4098);
/* 222 */       if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes size"))
/* 223 */         throw new IllegalStateException("Failed to get OpenAL attributes"); 
/* 226 */       IntBuffer attributes = stack.mallocInt(size);
/* 227 */       ALC10.alcGetIntegerv(this.currentDevice, 4099, attributes);
/* 228 */       if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes"))
/* 229 */         throw new IllegalStateException("Failed to get OpenAL attributes"); 
/* 232 */       int pos = 0;
/* 233 */       while (pos < size) {
/* 234 */         int attribute = attributes.get(pos++);
/* 235 */         if (attribute == 0)
/*     */           break; 
/* 238 */         int attributeValue = attributes.get(pos++);
/* 240 */         if (attribute == 4112) {
/* 241 */           int i = attributeValue;
/* 244 */           if (stack != null)
/* 244 */             stack.close(); 
/*     */           return i;
/*     */         } 
/*     */       } 
/* 244 */       if (stack != null)
/* 244 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 245 */     return 30;
/*     */   }
/*     */   
/*     */   public String currentDeviceName() {
/* 249 */     return this.currentDeviceName;
/*     */   }
/*     */   
/*     */   private static String queryDeviceName(long deviceId) {
/* 253 */     String name = ALC10.alcGetString(deviceId, 4115);
/* 254 */     if (name == null)
/* 255 */       name = ALC10.alcGetString(deviceId, 4101); 
/* 257 */     if (name == null)
/* 258 */       name = "Unknown (0x" + HexFormat.of().toHexDigits(deviceId) + ")"; 
/* 260 */     return name;
/*     */   }
/*     */   
/*     */   private static long openDeviceOrFallback(String preferredDevice, String systemDefaultDevice) {
/* 264 */     OptionalLong device = OptionalLong.empty();
/* 265 */     if (preferredDevice != null)
/* 266 */       device = tryOpenDevice(preferredDevice); 
/* 268 */     if (device.isEmpty() && systemDefaultDevice != null)
/* 269 */       device = tryOpenDevice(systemDefaultDevice); 
/* 271 */     if (device.isEmpty())
/* 272 */       device = tryOpenDevice(null); 
/* 274 */     if (device.isEmpty())
/* 275 */       throw new IllegalStateException("Failed to open OpenAL device"); 
/* 278 */     return device.getAsLong();
/*     */   }
/*     */   
/*     */   private static OptionalLong tryOpenDevice(String name) {
/* 282 */     long device = ALC10.alcOpenDevice(name);
/* 284 */     if (device != 0L && !OpenAlUtil.checkALCError(device, "Open device"))
/* 285 */       return OptionalLong.of(device); 
/* 288 */     return OptionalLong.empty();
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 292 */     this.staticChannels.cleanup();
/* 293 */     this.streamingChannels.cleanup();
/* 295 */     ALC10.alcDestroyContext(this.context);
/* 296 */     if (this.currentDevice != 0L)
/* 297 */       ALC10.alcCloseDevice(this.currentDevice); 
/*     */   }
/*     */   
/*     */   public Listener getListener() {
/* 302 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Channel acquireChannel(Pool pool) {
/* 306 */     return ((pool == Pool.STREAMING) ? this.streamingChannels : this.staticChannels).acquire();
/*     */   }
/*     */   
/*     */   public void releaseChannel(Channel channel) {
/* 310 */     if (!this.staticChannels.release(channel) && !this.streamingChannels.release(channel))
/* 311 */       throw new IllegalStateException("Tried to release unknown channel"); 
/*     */   }
/*     */   
/*     */   public String getChannelDebugString() {
/* 316 */     return String.format(Locale.ROOT, "Sounds: %d/%d + %d/%d", new Object[] { Integer.valueOf(this.staticChannels.getUsedCount()), Integer.valueOf(this.staticChannels.getMaxCount()), Integer.valueOf(this.streamingChannels.getUsedCount()), Integer.valueOf(this.streamingChannels.getMaxCount()) });
/*     */   }
/*     */   
/*     */   public boolean isCurrentDeviceDisconnected() {
/* 320 */     return (this.supportsDisconnections && ALC11.alcGetInteger(this.currentDevice, 787) == 0);
/*     */   }
/*     */   
/*     */   public static DeviceTracker createDeviceTracker() {
/* 327 */     DeviceList deviceList = DeviceList.query();
/* 328 */     if (CallbackDeviceTracker.isSupported()) {
/* 329 */       LOGGER.debug("Using SOFT_system_events callback for tracking audio device changes");
/* 330 */       return CallbackDeviceTracker.createAndInstall(deviceList);
/*     */     } 
/* 332 */     LOGGER.debug("Using polling for tracking audio device changes");
/* 333 */     return new PollingDeviceTracker(deviceList);
/*     */   }
/*     */   
/*     */   private static interface ChannelPool {
/*     */     Channel acquire();
/*     */     
/*     */     boolean release(Channel param1Channel);
/*     */     
/*     */     void cleanup();
/*     */     
/*     */     int getMaxCount();
/*     */     
/*     */     int getUsedCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\Library.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */