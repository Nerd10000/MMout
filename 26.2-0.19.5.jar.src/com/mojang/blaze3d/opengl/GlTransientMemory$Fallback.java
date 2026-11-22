/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.util.TransientBlockAllocator;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ class Fallback extends GlTransientMemory {
/*     */   private final TransientBlockAllocator<GlAllocation> blockAllocator;
/*     */   
/*     */   private static final class GlAllocation extends Record {
/*     */     private final GlBuffer glBuffer;
/*     */     
/*     */     private final long hostBuffer;
/*     */     
/*     */     private GlAllocation(GlBuffer glBuffer, long hostBuffer) {
/*  64 */       this.glBuffer = glBuffer;
/*  64 */       this.hostBuffer = hostBuffer;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GlBuffer glBuffer() {
/*  64 */       return this.glBuffer;
/*     */     }
/*     */     
/*     */     public long hostBuffer() {
/*  64 */       return this.hostBuffer;
/*     */     }
/*     */   }
/*     */   
/*     */   Fallback(GlDevice device, GlCommandEncoder encoder) {
/*  71 */     super(device, encoder);
/*  72 */     this.blockAllocator = new TransientBlockAllocator(524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(this::allocateGlBlock, this::freeGlBlock));
/*     */   }
/*     */   
/*     */   public void close() {
/*  77 */     rotate();
/*     */   }
/*     */   
/*     */   public void rotate() {
/*  82 */     this.blockAllocator.rotate().run();
/*     */   }
/*     */   
/*     */   private GlAllocation allocateGlBlock(long size) {
/*  89 */     GlBuffer buffer = this.bufferStorage.createBuffer(this.dsa, 40, size);
/*  90 */     this.debugLabels.applyLabel(buffer, () -> "OpenGL Transient Buffer");
/*  91 */     long hostPtr = MemoryUtil.nmemAlloc(size);
/*  92 */     return new GlAllocation(buffer, hostPtr);
/*     */   }
/*     */   
/*     */   private void freeGlBlock(GlAllocation allocation) {
/*  96 */     allocation.glBuffer.close();
/*  97 */     MemoryUtil.nmemFree(allocation.hostBuffer);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView allocateStaging(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 104 */     TransientBlockAllocator.Allocation<GlAllocation> allocation = this.blockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 105 */     GlTransientMemory.TransientGpuBuffer transientBuffer = new GlTransientMemory.TransientGpuBuffer(this, ((GlAllocation)allocation.block()).glBuffer().handle(), usage, ((GlAllocation)allocation.block()).glBuffer().size());
/* 106 */     GpuBufferSlice slice = new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
/* 107 */     ByteBuffer hostBuffer = MemoryUtil.memByteBuffer(((GlAllocation)allocation.block()).hostBuffer + allocation.offset(), (int)allocation.size());
/* 108 */     return new GpuBufferSlice.MappedView(slice, hostBuffer, () -> this.dsa.bufferSubData(transientBuffer.handle(), slice.offset(), MemoryUtil.memByteBuffer(((GlAllocation)allocation.block()).hostBuffer + allocation.offset(), (int)allocation.size()), usage));
/*     */   }
/*     */   
/*     */   public GpuBufferSlice allocateGpu(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 115 */     TransientBlockAllocator.Allocation<GlAllocation> allocation = this.blockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 116 */     GlTransientMemory.TransientGpuBuffer transientBuffer = new GlTransientMemory.TransientGpuBuffer(this, ((GlAllocation)allocation.block()).glBuffer().handle(), usage, ((GlAllocation)allocation.block()).glBuffer().size());
/* 117 */     return new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView allocateGpuMapped(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 122 */     return allocateStaging(size, alignment, usage, minimumAllocation, elementSize);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice uploadStaging(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 128 */     return uploadGpu(data, alignment, usage, minimumAllocation, elementSize);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice uploadGpu(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 133 */     long totalSize = 0L;
/* 134 */     for (int i = 0; i < data.size(); i++) {
/* 135 */       totalSize += ((ByteBuffer)data.get(i)).remaining();
/* 136 */       totalSize = Mth.roundToward(totalSize, alignment);
/*     */     } 
/* 139 */     GpuBufferSlice bufferSlice = allocateGpu(totalSize, alignment, usage);
/* 141 */     int target = GlUtil.selectBufferBindTarget(usage);
/* 142 */     GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
/* 143 */     long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), totalSize, 38);
/* 144 */     long offset = 0L;
/* 145 */     for (int j = 0; j < data.size(); j++) {
/* 146 */       ByteBuffer buffer = data.get(j);
/* 147 */       MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), ptr + offset, buffer.remaining());
/* 149 */       offset += buffer.remaining();
/* 150 */       offset = Mth.roundToward(offset, alignment);
/*     */     } 
/* 152 */     GL33C.glUnmapBuffer(target);
/* 153 */     GlStateManager._glBindBuffer(target, 0);
/* 155 */     return bufferSlice;
/*     */   }
/*     */   
/*     */   public List<GpuBufferSlice> multiUploadStaging(List<ByteBuffer> data, long alignment, int usage) {
/* 161 */     return multiUploadGpu(data, alignment, usage);
/*     */   }
/*     */   
/*     */   public List<GpuBufferSlice> multiUploadGpu(List<ByteBuffer> data, long alignment, int usage) {
/* 166 */     ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
/* 167 */     uploadedBuffers.size(data.size());
/* 169 */     IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
/* 170 */     sortedDataIndices.sort(IntComparator.comparing(index -> Integer.valueOf(((ByteBuffer)data.get(index)).remaining())));
/* 172 */     int target = GlUtil.selectBufferBindTarget(usage);
/* 173 */     while (!sortedDataIndices.isEmpty()) {
/* 175 */       boolean allocatedAnything = false;
/* 177 */       for (int i = sortedDataIndices.size() - 1; i >= 0; ) {
/* 178 */         int j = sortedDataIndices.getInt(i);
/* 179 */         ByteBuffer byteBuffer = data.get(j);
/* 180 */         if (!this.blockAllocator.canAllocateInCurrentBlock(byteBuffer.remaining(), alignment)) {
/*     */           i--;
/*     */           continue;
/*     */         } 
/* 183 */         sortedDataIndices.removeInt(i);
/* 184 */         GpuBufferSlice gpuBufferSlice = allocateGpu(byteBuffer.remaining(), alignment, usage);
/* 185 */         uploadedBuffers.set(j, gpuBufferSlice);
/* 187 */         GlStateManager._glBindBuffer(target, ((GlBuffer)gpuBufferSlice.buffer()).handle());
/* 188 */         long l = GL33C.nglMapBufferRange(target, gpuBufferSlice.offset(), gpuBufferSlice.length(), 38);
/* 189 */         MemoryUtil.memCopy(MemoryUtil.memAddress(byteBuffer), l, gpuBufferSlice.length());
/* 190 */         GL33C.glUnmapBuffer(target);
/* 191 */         allocatedAnything = true;
/*     */       } 
/* 194 */       if (allocatedAnything)
/*     */         continue; 
/* 199 */       int bufferIndex = sortedDataIndices.popInt();
/* 200 */       ByteBuffer currentBuffer = data.get(bufferIndex);
/* 201 */       GpuBufferSlice bufferSlice = allocateGpu(currentBuffer.remaining(), alignment, usage);
/* 202 */       uploadedBuffers.set(bufferIndex, bufferSlice);
/* 204 */       GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
/* 205 */       long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), bufferSlice.length(), 38);
/* 206 */       MemoryUtil.memCopy(MemoryUtil.memAddress(currentBuffer), ptr, bufferSlice.length());
/* 207 */       GL33C.glUnmapBuffer(target);
/*     */     } 
/* 209 */     GlStateManager._glBindBuffer(target, 0);
/* 211 */     return (List<GpuBufferSlice>)uploadedBuffers;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory$Fallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */