/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import it.unimi.dsi.fastutil.ints.IntConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public final class AutoStorageIndexBuffer implements AutoCloseable {
/*     */   private final int vertexStride;
/*     */   
/*     */   private final int indexStride;
/*     */   
/*     */   private final IndexGenerator generator;
/*     */   
/*     */   private GpuBuffer buffer;
/*     */   
/* 317 */   private IndexType type = IndexType.SHORT;
/*     */   
/*     */   private int indexCount;
/*     */   
/*     */   private AutoStorageIndexBuffer(int vertexStride, int indexStride, IndexGenerator generator) {
/* 321 */     this.vertexStride = vertexStride;
/* 322 */     this.indexStride = indexStride;
/* 323 */     this.generator = generator;
/*     */   }
/*     */   
/*     */   public void close() {
/* 328 */     if (this.buffer != null)
/* 329 */       this.buffer.close(); 
/*     */   }
/*     */   
/*     */   public boolean hasStorage(int indexCount) {
/* 334 */     return (indexCount <= this.indexCount);
/*     */   }
/*     */   
/*     */   public GpuBuffer getBuffer(int indexCount) {
/* 338 */     ensureStorage(indexCount);
/* 339 */     return this.buffer;
/*     */   }
/*     */   
/*     */   private void ensureStorage(int indexCount) {
/* 343 */     if (hasStorage(indexCount))
/*     */       return; 
/* 347 */     indexCount = Mth.roundToward(indexCount * 2, this.indexStride);
/* 348 */     RenderSystem.LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", Integer.valueOf(this.indexCount), Integer.valueOf(indexCount));
/* 350 */     int primitiveCount = indexCount / this.indexStride;
/* 351 */     int vertexCount = primitiveCount * this.vertexStride;
/* 353 */     IndexType type = IndexType.least(vertexCount);
/* 354 */     int bufferSize = Mth.roundToward(indexCount * type.bytes, 4);
/* 356 */     ByteBuffer data = MemoryUtil.memAlloc(bufferSize);
/*     */     try {
/* 359 */       this.type = type;
/* 361 */       IntConsumer intConsumer = intConsumer(data);
/*     */       int ii;
/* 362 */       for (ii = 0; ii < indexCount; ii += this.indexStride)
/* 363 */         this.generator.accept(intConsumer, ii * this.vertexStride / this.indexStride); 
/* 365 */       data.flip();
/* 367 */       if (this.buffer != null)
/* 368 */         this.buffer.close(); 
/* 370 */       this.buffer = RenderSystem.getDevice().createBuffer(() -> "Auto Storage index buffer", 64, data);
/*     */     } finally {
/* 372 */       MemoryUtil.memFree(data);
/*     */     } 
/* 375 */     this.indexCount = indexCount;
/*     */   }
/*     */   
/*     */   private IntConsumer intConsumer(ByteBuffer buffer) {
/* 379 */     switch (RenderSystem.null.$SwitchMap$com$mojang$blaze3d$IndexType[this.type.ordinal()]) {
/*     */       case 1:
/* 381 */         return value -> buffer.putShort((short)value);
/*     */     } 
/* 384 */     Objects.requireNonNull(buffer);
/* 384 */     return buffer::putInt;
/*     */   }
/*     */   
/*     */   public IndexType type() {
/* 389 */     return this.type;
/*     */   }
/*     */   
/*     */   private static interface IndexGenerator {
/*     */     void accept(IntConsumer param2IntConsumer, int param2Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderSystem$AutoStorageIndexBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */