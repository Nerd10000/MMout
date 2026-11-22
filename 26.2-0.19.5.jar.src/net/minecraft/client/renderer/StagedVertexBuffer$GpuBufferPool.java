/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class GpuBufferPool implements AutoCloseable {
/*     */   private static final int BUFFER_SIZE_INCREMENT = 262144;
/*     */   
/*     */   private static final int MAX_REUSE_SIZE_FACTOR = 4;
/*     */   
/*     */   private final Supplier<String> label;
/*     */   
/*     */   private final int usage;
/*     */   
/* 241 */   private final List<GpuBuffer> available = new ArrayList<>();
/*     */   
/* 242 */   private final List<GpuBuffer> usedThisFrame = new ArrayList<>();
/*     */   
/* 243 */   private final List<PendingRecycle> pendingRecycle = new ArrayList<>();
/*     */   
/*     */   private GpuBufferPool(Supplier<String> label, int usage) {
/* 246 */     this.label = label;
/* 247 */     this.usage = usage;
/*     */   }
/*     */   
/*     */   private void tryRecycleBuffers() {
/* 251 */     this.pendingRecycle.removeIf(buffer -> {
/*     */           List<GpuBuffer> recycled = buffer.tryRecycle();
/*     */           if (recycled != null) {
/*     */             this.available.addAll(recycled);
/*     */             return true;
/*     */           } 
/*     */           return false;
/*     */         });
/*     */   }
/*     */   
/*     */   public GpuBuffer acquire(GpuDevice device, int minSize) {
/* 262 */     tryRecycleBuffers();
/* 263 */     int roundedMinSize = Mth.roundToward(minSize, 262144);
/* 264 */     GpuBuffer buffer = takeBestAvailable(roundedMinSize, roundedMinSize * 4);
/* 265 */     if (buffer == null)
/* 266 */       buffer = device.createBuffer(this.label, this.usage, roundedMinSize); 
/* 268 */     this.usedThisFrame.add(buffer);
/* 269 */     return buffer;
/*     */   }
/*     */   
/*     */   private GpuBuffer takeBestAvailable(int minSize, int maxSize) {
/* 273 */     int bestIndex = -1;
/* 274 */     long bestSize = (maxSize + 1);
/* 275 */     for (int i = 0; i < this.available.size(); i++) {
/* 276 */       long size = ((GpuBuffer)this.available.get(i)).size();
/* 277 */       if (size == minSize)
/* 278 */         return this.available.remove(i); 
/* 280 */       if (size > minSize && size < bestSize) {
/* 281 */         bestIndex = i;
/* 282 */         bestSize = size;
/*     */       } 
/*     */     } 
/* 285 */     if (bestIndex == -1)
/* 286 */       return null; 
/* 288 */     return this.available.remove(bestIndex);
/*     */   }
/*     */   
/*     */   public void endFrame(GpuDevice device) {
/* 292 */     if (!this.usedThisFrame.isEmpty()) {
/* 293 */       GpuFence fence = device.createCommandEncoder().createFence();
/* 294 */       this.pendingRecycle.add(new PendingRecycle(List.copyOf(this.usedThisFrame), fence));
/* 295 */       this.usedThisFrame.clear();
/*     */     } 
/* 300 */     if (!this.available.isEmpty()) {
/* 301 */       this.available.forEach(GpuBuffer::close);
/* 302 */       this.available.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 308 */     this.available.forEach(GpuBuffer::close);
/* 309 */     this.usedThisFrame.forEach(GpuBuffer::close);
/* 310 */     this.pendingRecycle.forEach(PendingRecycle::close);
/* 311 */     this.available.clear();
/* 312 */     this.usedThisFrame.clear();
/* 313 */     this.pendingRecycle.clear();
/*     */   }
/*     */   
/*     */   private static final class PendingRecycle extends Record implements AutoCloseable {
/*     */     private final List<GpuBuffer> buffers;
/*     */     
/*     */     private final GpuFence fence;
/*     */     
/*     */     private PendingRecycle(List<GpuBuffer> buffers, GpuFence fence) {
/* 316 */       this.buffers = buffers;
/* 316 */       this.fence = fence;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<GpuBuffer> buffers() {
/* 316 */       return this.buffers;
/*     */     }
/*     */     
/*     */     public GpuFence fence() {
/* 316 */       return this.fence;
/*     */     }
/*     */     
/*     */     public List<GpuBuffer> tryRecycle() {
/* 318 */       if (this.fence.awaitCompletion(0L)) {
/* 319 */         this.fence.close();
/* 320 */         return this.buffers;
/*     */       } 
/* 322 */       return null;
/*     */     }
/*     */     
/*     */     public void close() {
/* 327 */       this.buffers.forEach(GpuBuffer::close);
/* 328 */       this.fence.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\StagedVertexBuffer$GpuBufferPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */