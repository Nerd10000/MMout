/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SectionBufferBuilderPool implements AutoCloseable {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ArrayBlockingQueue<SectionBufferBuilderPack> freeBuffers;
/*    */   
/*    */   private SectionBufferBuilderPool(List<SectionBufferBuilderPack> buffers) {
/* 18 */     this.freeBuffers = Queues.newArrayBlockingQueue(buffers.size());
/* 19 */     this.freeBuffers.addAll(buffers);
/*    */   }
/*    */   
/*    */   public static SectionBufferBuilderPool allocate(int maxWorkers) {
/* 25 */     int maxBuffers = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3D) / SectionBufferBuilderPack.TOTAL_BUFFERS_SIZE);
/* 26 */     int targetBufferCount = Math.max(1, Math.min(maxWorkers, maxBuffers));
/* 28 */     List<SectionBufferBuilderPack> buffers = new ArrayList<>(targetBufferCount);
/*    */     try {
/* 30 */       for (int i = 0; i < targetBufferCount; i++)
/* 31 */         buffers.add(new SectionBufferBuilderPack()); 
/* 33 */     } catch (OutOfMemoryError e) {
/* 34 */       LOGGER.warn("Allocated only {}/{} buffers", Integer.valueOf(buffers.size()), Integer.valueOf(targetBufferCount));
/* 36 */       int buffersToDrop = Math.min(buffers.size() * 2 / 3, buffers.size() - 1);
/* 37 */       for (int i = 0; i < buffersToDrop; i++)
/* 38 */         ((SectionBufferBuilderPack)buffers.remove(buffers.size() - 1)).close(); 
/*    */     } 
/* 42 */     return new SectionBufferBuilderPool(buffers);
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPack acquire() {
/* 46 */     return this.freeBuffers.poll();
/*    */   }
/*    */   
/*    */   public void release(SectionBufferBuilderPack buffer) {
/* 50 */     this.freeBuffers.offer(buffer);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 54 */     return this.freeBuffers.isEmpty();
/*    */   }
/*    */   
/*    */   public int getFreeBufferCount() {
/* 58 */     return this.freeBuffers.size();
/*    */   }
/*    */   
/*    */   public void close() {
/* 63 */     this.freeBuffers.forEach(SectionBufferBuilderPack::close);
/* 64 */     this.freeBuffers.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionBufferBuilderPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */