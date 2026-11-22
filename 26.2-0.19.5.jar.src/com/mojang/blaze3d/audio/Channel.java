/*     */ package com.mojang.blaze3d.audio;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import net.minecraft.client.sounds.AudioStream;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.lwjgl.openal.AL10;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Channel {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int QUEUED_BUFFER_COUNT = 4;
/*     */   
/*     */   public static final int BUFFER_DURATION_SECONDS = 1;
/*     */   
/*     */   private final int source;
/*     */   
/*  23 */   private final AtomicBoolean initialized = new AtomicBoolean(true);
/*     */   
/*  25 */   private int streamingBufferSize = 16384;
/*     */   
/*     */   private AudioStream stream;
/*     */   
/*     */   static Channel create() {
/*  31 */     int[] newId = new int[1];
/*  33 */     AL10.alGenSources(newId);
/*  34 */     if (OpenAlUtil.checkALError("Allocate new source"))
/*  35 */       return null; 
/*  37 */     return new Channel(newId[0]);
/*     */   }
/*     */   
/*     */   private Channel(int src) {
/*  41 */     this.source = src;
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  45 */     if (this.initialized.compareAndSet(true, false)) {
/*  46 */       AL10.alSourceStop(this.source);
/*  47 */       OpenAlUtil.checkALError("Stop");
/*  48 */       if (this.stream != null) {
/*     */         try {
/*  50 */           this.stream.close();
/*  51 */         } catch (IOException e) {
/*  52 */           LOGGER.error("Failed to close audio stream", e);
/*     */         } 
/*  54 */         removeProcessedBuffers();
/*  55 */         this.stream = null;
/*     */       } 
/*  58 */       AL10.alDeleteSources(new int[] { this.source });
/*  59 */       OpenAlUtil.checkALError("Cleanup");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void play() {
/*  64 */     AL10.alSourcePlay(this.source);
/*     */   }
/*     */   
/*     */   private int getState() {
/*  68 */     if (!this.initialized.get())
/*  69 */       return 4116; 
/*  71 */     return AL10.alGetSourcei(this.source, 4112);
/*     */   }
/*     */   
/*     */   public void pause() {
/*  75 */     if (getState() == 4114)
/*  76 */       AL10.alSourcePause(this.source); 
/*     */   }
/*     */   
/*     */   public void unpause() {
/*  81 */     if (getState() == 4115)
/*  82 */       AL10.alSourcePlay(this.source); 
/*     */   }
/*     */   
/*     */   public void stop() {
/*  87 */     if (this.initialized.get()) {
/*  88 */       AL10.alSourceStop(this.source);
/*  89 */       OpenAlUtil.checkALError("Stop");
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean playing() {
/*  94 */     return (getState() == 4114);
/*     */   }
/*     */   
/*     */   public boolean stopped() {
/*  98 */     return (getState() == 4116);
/*     */   }
/*     */   
/*     */   public void setSelfPosition(Vec3 newPosition) {
/* 102 */     AL10.alSourcefv(this.source, 4100, new float[] { (float)newPosition.x, (float)newPosition.y, (float)newPosition.z });
/*     */   }
/*     */   
/*     */   public void setPitch(float pitch) {
/* 106 */     AL10.alSourcef(this.source, 4099, pitch);
/*     */   }
/*     */   
/*     */   public void setLooping(boolean looping) {
/* 110 */     AL10.alSourcei(this.source, 4103, looping ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void setVolume(float volume) {
/* 114 */     AL10.alSourcef(this.source, 4106, volume);
/*     */   }
/*     */   
/*     */   public void disableAttenuation() {
/* 118 */     AL10.alSourcei(this.source, 53248, 0);
/*     */   }
/*     */   
/*     */   public void linearAttenuation(float maxDistance) {
/* 122 */     AL10.alSourcei(this.source, 53248, 53251);
/* 123 */     AL10.alSourcef(this.source, 4131, maxDistance);
/* 124 */     AL10.alSourcef(this.source, 4129, 1.0F);
/* 125 */     AL10.alSourcef(this.source, 4128, 0.0F);
/*     */   }
/*     */   
/*     */   public void setRelative(boolean relative) {
/* 129 */     AL10.alSourcei(this.source, 514, relative ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void attachStaticBuffer(SoundBuffer buffer) {
/* 133 */     buffer.getAlBuffer().ifPresent(bufferId -> AL10.alSourcei(this.source, 4105, bufferId));
/*     */   }
/*     */   
/*     */   public void attachBufferStream(AudioStream stream) {
/* 137 */     this.stream = stream;
/* 138 */     AudioFormat format = stream.getFormat();
/* 139 */     this.streamingBufferSize = calculateBufferSize(format, 1);
/* 140 */     pumpBuffers(4);
/*     */   }
/*     */   
/*     */   private static int calculateBufferSize(AudioFormat format, int seconds) {
/* 144 */     return (int)((seconds * format.getSampleSizeInBits()) / 8.0F * format.getChannels() * format.getSampleRate());
/*     */   }
/*     */   
/*     */   private void pumpBuffers(int size) {
/* 148 */     if (this.stream != null)
/*     */       try {
/* 150 */         for (int i = 0; i < size; i++) {
/* 151 */           ByteBuffer buffer = this.stream.read(this.streamingBufferSize);
/* 152 */           if (buffer != null)
/* 154 */             (new SoundBuffer(buffer, this.stream.getFormat())).releaseAlBuffer().ifPresent(bufferId -> AL10.alSourceQueueBuffers(this.source, new int[] { bufferId })); 
/*     */         } 
/* 157 */       } catch (IOException e) {
/* 158 */         LOGGER.error("Failed to read from audio stream", e);
/*     */       }  
/*     */   }
/*     */   
/*     */   public void updateStream() {
/* 164 */     if (this.stream != null) {
/* 165 */       int processedBuffers = removeProcessedBuffers();
/* 166 */       pumpBuffers(processedBuffers);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int removeProcessedBuffers() {
/* 171 */     int processed = AL10.alGetSourcei(this.source, 4118);
/* 173 */     if (processed > 0) {
/* 174 */       int[] ids = new int[processed];
/* 175 */       AL10.alSourceUnqueueBuffers(this.source, ids);
/* 176 */       OpenAlUtil.checkALError("Unqueue buffers");
/* 177 */       AL10.alDeleteBuffers(ids);
/* 178 */       OpenAlUtil.checkALError("Remove processed buffers");
/*     */     } 
/* 181 */     return processed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\Channel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */