/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.audio.SoundBuffer;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import net.minecraft.client.resources.sounds.Sound;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceProvider;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class SoundBufferLibrary {
/*    */   private final ResourceProvider resourceManager;
/*    */   
/* 22 */   private final Map<Identifier, CompletableFuture<SoundBuffer>> cache = Maps.newHashMap();
/*    */   
/*    */   public SoundBufferLibrary(ResourceProvider resourceProvider) {
/* 25 */     this.resourceManager = resourceProvider;
/*    */   }
/*    */   
/*    */   public CompletableFuture<SoundBuffer> getCompleteBuffer(Identifier location) {
/* 29 */     return this.cache.computeIfAbsent(location, l -> CompletableFuture.supplyAsync((), (Executor)Util.nonCriticalIoPool()));
/*    */   }
/*    */   
/*    */   public CompletableFuture<AudioStream> getStream(Identifier location, boolean looping) {
/* 42 */     return CompletableFuture.supplyAsync(() -> {
/*    */           try {
/*    */             InputStream is = this.resourceManager.open(location);
/*    */             return looping ? new LoopingAudioStream(JOrbisAudioStream::new, is) : new JOrbisAudioStream(is);
/* 46 */           } catch (IOException e) {
/*    */             throw new CompletionException(e);
/*    */           } 
/* 49 */         }(Executor)Util.nonCriticalIoPool());
/*    */   }
/*    */   
/*    */   public void clear() {
/* 53 */     this.cache.values().forEach(future -> future.thenAccept(SoundBuffer::discardAlBuffer));
/* 54 */     this.cache.clear();
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> preload(Collection<Sound> sounds) {
/* 58 */     return CompletableFuture.allOf((CompletableFuture<?>[])sounds.stream().map(sound -> getCompleteBuffer(sound.getPath())).toArray(x$0 -> new CompletableFuture[x$0]));
/*    */   }
/*    */   
/*    */   public void enumerate(DebugOutput debugOutput) {
/* 62 */     this.cache.forEach((id, bufferFuture) -> {
/*    */           SoundBuffer buffer = bufferFuture.getNow(null);
/*    */           if (buffer != null && buffer.isValid())
/*    */             debugOutput.accountBuffer(id, buffer.size(), buffer.format()); 
/*    */         });
/*    */   }
/*    */   
/*    */   public static interface DebugOutput {
/*    */     void accountBuffer(Identifier param1Identifier, int param1Int, AudioFormat param1AudioFormat);
/*    */     
/*    */     public static class Counter implements DebugOutput {
/*    */       private int totalCount;
/*    */       
/*    */       private long totalSize;
/*    */       
/*    */       public void accountBuffer(Identifier id, int size, AudioFormat format) {
/* 79 */         this.totalCount++;
/* 80 */         this.totalSize += size;
/*    */       }
/*    */       
/*    */       public int totalCount() {
/* 84 */         return this.totalCount;
/*    */       }
/*    */       
/*    */       public long totalSize() {
/* 88 */         return this.totalSize;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Counter implements DebugOutput {
/*    */     private int totalCount;
/*    */     
/*    */     private long totalSize;
/*    */     
/*    */     public void accountBuffer(Identifier id, int size, AudioFormat format) {
/*    */       this.totalCount++;
/*    */       this.totalSize += size;
/*    */     }
/*    */     
/*    */     public int totalCount() {
/*    */       return this.totalCount;
/*    */     }
/*    */     
/*    */     public long totalSize() {
/* 88 */       return this.totalSize;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundBufferLibrary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */