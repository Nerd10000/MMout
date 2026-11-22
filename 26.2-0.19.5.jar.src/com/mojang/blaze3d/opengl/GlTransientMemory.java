/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.TransientMemory;
/*     */ import com.mojang.blaze3d.util.TransientBlockAllocator;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.opengl.ARBBufferStorage;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public abstract class GlTransientMemory implements TransientMemory, AutoCloseable {
/*     */   private static final long BLOCK_SIZE = 524288L;
/*     */   
/*     */   private static final long MAX_CPU_ALIGNMENT = 16L;
/*     */   
/*  28 */   private static final long MAX_GPU_ALIGNMENT = Long.highestOneBit(Long.MAX_VALUE);
/*     */   
/*     */   final GlCommandEncoder encoder;
/*     */   
/*     */   protected final DirectStateAccess dsa;
/*     */   
/*     */   protected final BufferStorage bufferStorage;
/*     */   
/*     */   protected final GlDebugLabel debugLabels;
/*     */   
/*  36 */   private final TransientBlockAllocator<Long> cpuBlockAllocator = new TransientBlockAllocator(524288L, 16L, TransientBlockAllocator.Allocator.create(MemoryUtil::nmemAlloc, MemoryUtil::nmemFree));
/*     */   
/*     */   GlTransientMemory(GlDevice device, GlCommandEncoder encoder) {
/*  40 */     this.encoder = encoder;
/*  41 */     this.dsa = device.directStateAccess();
/*  42 */     this.bufferStorage = device.getBufferStorage();
/*  43 */     this.debugLabels = device.debugLabels();
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public ByteBuffer allocateCpu(long size, long alignment, long minimumAllocation, long elementSize) {
/*  52 */     assert size <= 2147483647L;
/*  53 */     TransientBlockAllocator.Allocation<Long> alloc = this.cpuBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/*  54 */     return MemoryUtil.memByteBuffer(((Long)alloc.block()).longValue() + alloc.offset(), (int)alloc.size());
/*     */   }
/*     */   
/*     */   public void rotate() {
/*  58 */     this.cpuBlockAllocator.rotate().run();
/*     */   }
/*     */   
/*     */   static class Fallback extends GlTransientMemory {
/*     */     private final TransientBlockAllocator<GlAllocation> blockAllocator;
/*     */     
/*     */     private static final class GlAllocation extends Record {
/*     */       private final GlBuffer glBuffer;
/*     */       
/*     */       private final long hostBuffer;
/*     */       
/*     */       private GlAllocation(GlBuffer glBuffer, long hostBuffer) {
/*  64 */         this.glBuffer = glBuffer;
/*  64 */         this.hostBuffer = hostBuffer;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #64	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #64	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #64	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$Fallback$GlAllocation;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public GlBuffer glBuffer() {
/*  64 */         return this.glBuffer;
/*     */       }
/*     */       
/*     */       public long hostBuffer() {
/*  64 */         return this.hostBuffer;
/*     */       }
/*     */     }
/*     */     
/*     */     Fallback(GlDevice device, GlCommandEncoder encoder) {
/*  71 */       super(device, encoder);
/*  72 */       this.blockAllocator = new TransientBlockAllocator(524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(this::allocateGlBlock, this::freeGlBlock));
/*     */     }
/*     */     
/*     */     public void close() {
/*  77 */       rotate();
/*     */     }
/*     */     
/*     */     public void rotate() {
/*  82 */       this.blockAllocator.rotate().run();
/*     */     }
/*     */     
/*     */     private GlAllocation allocateGlBlock(long size) {
/*  89 */       GlBuffer buffer = this.bufferStorage.createBuffer(this.dsa, 40, size);
/*  90 */       this.debugLabels.applyLabel(buffer, () -> "OpenGL Transient Buffer");
/*  91 */       long hostPtr = MemoryUtil.nmemAlloc(size);
/*  92 */       return new GlAllocation(buffer, hostPtr);
/*     */     }
/*     */     
/*     */     private void freeGlBlock(GlAllocation allocation) {
/*  96 */       allocation.glBuffer.close();
/*  97 */       MemoryUtil.nmemFree(allocation.hostBuffer);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView allocateStaging(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 104 */       TransientBlockAllocator.Allocation<GlAllocation> allocation = this.blockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 105 */       GlTransientMemory.TransientGpuBuffer transientBuffer = new GlTransientMemory.TransientGpuBuffer(((GlAllocation)allocation.block()).glBuffer().handle(), usage, ((GlAllocation)allocation.block()).glBuffer().size());
/* 106 */       GpuBufferSlice slice = new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
/* 107 */       ByteBuffer hostBuffer = MemoryUtil.memByteBuffer(((GlAllocation)allocation.block()).hostBuffer + allocation.offset(), (int)allocation.size());
/* 108 */       return new GpuBufferSlice.MappedView(slice, hostBuffer, () -> this.dsa.bufferSubData(transientBuffer.handle(), slice.offset(), MemoryUtil.memByteBuffer(((GlAllocation)allocation.block()).hostBuffer + allocation.offset(), (int)allocation.size()), usage));
/*     */     }
/*     */     
/*     */     public GpuBufferSlice allocateGpu(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 115 */       TransientBlockAllocator.Allocation<GlAllocation> allocation = this.blockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 116 */       GlTransientMemory.TransientGpuBuffer transientBuffer = new GlTransientMemory.TransientGpuBuffer(((GlAllocation)allocation.block()).glBuffer().handle(), usage, ((GlAllocation)allocation.block()).glBuffer().size());
/* 117 */       return new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView allocateGpuMapped(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 122 */       return allocateStaging(size, alignment, usage, minimumAllocation, elementSize);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice uploadStaging(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 128 */       return uploadGpu(data, alignment, usage, minimumAllocation, elementSize);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice uploadGpu(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 133 */       long totalSize = 0L;
/* 134 */       for (int i = 0; i < data.size(); i++) {
/* 135 */         totalSize += ((ByteBuffer)data.get(i)).remaining();
/* 136 */         totalSize = Mth.roundToward(totalSize, alignment);
/*     */       } 
/* 139 */       GpuBufferSlice bufferSlice = allocateGpu(totalSize, alignment, usage);
/* 141 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 142 */       GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
/* 143 */       long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), totalSize, 38);
/* 144 */       long offset = 0L;
/* 145 */       for (int j = 0; j < data.size(); j++) {
/* 146 */         ByteBuffer buffer = data.get(j);
/* 147 */         MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), ptr + offset, buffer.remaining());
/* 149 */         offset += buffer.remaining();
/* 150 */         offset = Mth.roundToward(offset, alignment);
/*     */       } 
/* 152 */       GL33C.glUnmapBuffer(target);
/* 153 */       GlStateManager._glBindBuffer(target, 0);
/* 155 */       return bufferSlice;
/*     */     }
/*     */     
/*     */     public List<GpuBufferSlice> multiUploadStaging(List<ByteBuffer> data, long alignment, int usage) {
/* 161 */       return multiUploadGpu(data, alignment, usage);
/*     */     }
/*     */     
/*     */     public List<GpuBufferSlice> multiUploadGpu(List<ByteBuffer> data, long alignment, int usage) {
/* 166 */       ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
/* 167 */       uploadedBuffers.size(data.size());
/* 169 */       IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
/* 170 */       sortedDataIndices.sort(IntComparator.comparing(index -> Integer.valueOf(((ByteBuffer)data.get(index)).remaining())));
/* 172 */       int target = GlUtil.selectBufferBindTarget(usage);
/* 173 */       while (!sortedDataIndices.isEmpty()) {
/* 175 */         boolean allocatedAnything = false;
/* 177 */         for (int i = sortedDataIndices.size() - 1; i >= 0; ) {
/* 178 */           int j = sortedDataIndices.getInt(i);
/* 179 */           ByteBuffer byteBuffer = data.get(j);
/* 180 */           if (!this.blockAllocator.canAllocateInCurrentBlock(byteBuffer.remaining(), alignment)) {
/*     */             i--;
/*     */             continue;
/*     */           } 
/* 183 */           sortedDataIndices.removeInt(i);
/* 184 */           GpuBufferSlice gpuBufferSlice = allocateGpu(byteBuffer.remaining(), alignment, usage);
/* 185 */           uploadedBuffers.set(j, gpuBufferSlice);
/* 187 */           GlStateManager._glBindBuffer(target, ((GlBuffer)gpuBufferSlice.buffer()).handle());
/* 188 */           long l = GL33C.nglMapBufferRange(target, gpuBufferSlice.offset(), gpuBufferSlice.length(), 38);
/* 189 */           MemoryUtil.memCopy(MemoryUtil.memAddress(byteBuffer), l, gpuBufferSlice.length());
/* 190 */           GL33C.glUnmapBuffer(target);
/* 191 */           allocatedAnything = true;
/*     */         } 
/* 194 */         if (allocatedAnything)
/*     */           continue; 
/* 199 */         int bufferIndex = sortedDataIndices.popInt();
/* 200 */         ByteBuffer currentBuffer = data.get(bufferIndex);
/* 201 */         GpuBufferSlice bufferSlice = allocateGpu(currentBuffer.remaining(), alignment, usage);
/* 202 */         uploadedBuffers.set(bufferIndex, bufferSlice);
/* 204 */         GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
/* 205 */         long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), bufferSlice.length(), 38);
/* 206 */         MemoryUtil.memCopy(MemoryUtil.memAddress(currentBuffer), ptr, bufferSlice.length());
/* 207 */         GL33C.glUnmapBuffer(target);
/*     */       } 
/* 209 */       GlStateManager._glBindBuffer(target, 0);
/* 211 */       return (List<GpuBufferSlice>)uploadedBuffers;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GlAllocation extends Record {
/*     */     private final GlBuffer glBuffer;
/*     */     
/*     */     private final long hostBuffer;
/*     */     
/*     */     private GlAllocation(GlBuffer glBuffer, long hostBuffer) {
/*     */       this.glBuffer = glBuffer;
/*     */       this.hostBuffer = hostBuffer;
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
/*     */       return this.glBuffer;
/*     */     }
/*     */     
/*     */     public long hostBuffer() {
/*     */       return this.hostBuffer;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GlAllocation extends Record {
/*     */     private final int glBuffer;
/*     */     
/*     */     private final long hostPtr;
/*     */     
/*     */     private final long size;
/*     */     
/*     */     private GlAllocation(int glBuffer, long hostPtr, long size) {
/* 218 */       this.glBuffer = glBuffer;
/* 218 */       this.hostPtr = hostPtr;
/* 218 */       this.size = size;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int glBuffer() {
/* 218 */       return this.glBuffer;
/*     */     }
/*     */     
/*     */     public long hostPtr() {
/* 218 */       return this.hostPtr;
/*     */     }
/*     */     
/*     */     public long size() {
/* 218 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*     */   static class PersistentMapping extends GlTransientMemory {
/*     */     private final TransientBlockAllocator<GlAllocation> stagingBlockAllocator;
/*     */     
/*     */     private final TransientBlockAllocator<GlAllocation> gpuBlockAllocator;
/*     */     
/*     */     private final TransientBlockAllocator<GlAllocation> gpuMappedBlockAllocator;
/*     */     
/*     */     private static final class GlAllocation extends Record {
/*     */       private final int glBuffer;
/*     */       
/*     */       private final long hostPtr;
/*     */       
/*     */       private final long size;
/*     */       
/*     */       private GlAllocation(int glBuffer, long hostPtr, long size) {
/* 218 */         this.glBuffer = glBuffer;
/* 218 */         this.hostPtr = hostPtr;
/* 218 */         this.size = size;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #218	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #218	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #218	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$GlAllocation;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public int glBuffer() {
/* 218 */         return this.glBuffer;
/*     */       }
/*     */       
/*     */       public long hostPtr() {
/* 218 */         return this.hostPtr;
/*     */       }
/*     */       
/*     */       public long size() {
/* 218 */         return this.size;
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class Rotation extends Record implements Runnable {
/*     */       private final Runnable staging;
/*     */       
/*     */       private final Runnable gpu;
/*     */       
/*     */       private final Runnable gpuMapped;
/*     */       
/*     */       private Rotation(Runnable staging, Runnable gpu, Runnable gpuMapped) {
/* 225 */         this.staging = staging;
/* 225 */         this.gpu = gpu;
/* 225 */         this.gpuMapped = gpuMapped;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #225	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #225	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #225	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public Runnable staging() {
/* 225 */         return this.staging;
/*     */       }
/*     */       
/*     */       public Runnable gpu() {
/* 225 */         return this.gpu;
/*     */       }
/*     */       
/*     */       public Runnable gpuMapped() {
/* 225 */         return this.gpuMapped;
/*     */       }
/*     */       
/*     */       public void run() {
/* 229 */         this.staging.run();
/* 230 */         this.gpu.run();
/* 231 */         this.gpuMapped.run();
/*     */       }
/*     */     }
/*     */     
/* 236 */     private final Rotation[] rotations = new Rotation[2];
/*     */     
/*     */     PersistentMapping(GlDevice device, GlCommandEncoder encoder) {
/* 240 */       super(device, encoder);
/* 241 */       this.stagingBlockAllocator = new TransientBlockAllocator(524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(size -> allocateGlBlock(size, true, true), this::freeGlBlock));
/* 242 */       this.gpuBlockAllocator = new TransientBlockAllocator(524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(size -> allocateGlBlock(size, false, false), this::freeGlBlock));
/* 243 */       this.gpuMappedBlockAllocator = new TransientBlockAllocator(524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator.Allocator.create(size -> allocateGlBlock(size, false, true), this::freeGlBlock));
/*     */     }
/*     */     
/*     */     public void close() {
/* 248 */       GL33C.glFinish();
/* 249 */       for (Rotation rotation : this.rotations) {
/* 250 */         if (rotation != null)
/* 251 */           rotation.run(); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void rotate() {
/* 258 */       Rotation previousRotation = this.rotations[this.encoder.currentSubmitSlot()];
/* 259 */       if (previousRotation != null)
/* 260 */         previousRotation.run(); 
/* 263 */       this.rotations[this.encoder.currentSubmitSlot()] = new Rotation(this.stagingBlockAllocator
/* 264 */           .rotate(), this.gpuBlockAllocator
/* 265 */           .rotate(), this.gpuMappedBlockAllocator
/* 266 */           .rotate());
/* 269 */       super.rotate();
/*     */     }
/*     */     
/*     */     private GlAllocation allocateGlBlock(long size, boolean host, boolean mapped) {
/*     */       long hostPtr;
/* 273 */       int glBuffer = this.dsa.createBuffer();
/* 275 */       int usageFlags = 0;
/* 276 */       if (host)
/* 277 */         usageFlags |= 0x200; 
/* 279 */       if (mapped) {
/* 280 */         usageFlags |= 0x2;
/* 281 */         usageFlags |= 0x40;
/* 282 */         usageFlags |= 0x80;
/*     */       } 
/* 284 */       GlStateManager._glBindBuffer(34962, glBuffer);
/* 285 */       ARBBufferStorage.glBufferStorage(34962, size, usageFlags);
/* 287 */       if (mapped) {
/* 288 */         hostPtr = GL33C.nglMapBufferRange(34962, 0L, size, 226);
/*     */       } else {
/* 290 */         hostPtr = 0L;
/*     */       } 
/* 292 */       GlStateManager._glBindBuffer(34962, 0);
/* 294 */       this.debugLabels.applyLabel(new GlTransientMemory.TransientGpuBuffer(glBuffer, 0, size), () -> "OpenGL Transient Buffer");
/* 296 */       return new GlAllocation(glBuffer, hostPtr, size);
/*     */     }
/*     */     
/*     */     private void freeGlBlock(GlAllocation allocation) {
/* 300 */       GlStateManager._glDeleteBuffers(allocation.glBuffer);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView allocateStaging(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 305 */       assert size <= 2147483647L;
/* 306 */       TransientBlockAllocator.Allocation<GlAllocation> alloc = this.stagingBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 307 */       GlTransientMemory.TransientGpuBuffer apiBuffer = new GlTransientMemory.TransientGpuBuffer(((GlAllocation)alloc.block()).glBuffer, usage, (int)((GlAllocation)alloc.block()).size);
/* 308 */       ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(((GlAllocation)alloc.block()).hostPtr + alloc.offset(), (int)alloc.size());
/* 309 */       return new GpuBufferSlice.MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {
/*     */           
/*     */           });
/*     */     }
/*     */     
/*     */     public GpuBufferSlice allocateGpu(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 314 */       assert size <= 2147483647L;
/* 315 */       TransientBlockAllocator.Allocation<GlAllocation> alloc = this.gpuBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 316 */       GlTransientMemory.TransientGpuBuffer apiBuffer = new GlTransientMemory.TransientGpuBuffer(((GlAllocation)alloc.block()).glBuffer, usage, (int)((GlAllocation)alloc.block()).size);
/* 317 */       return new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size());
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView allocateGpuMapped(long size, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 322 */       assert size <= 2147483647L;
/* 323 */       TransientBlockAllocator.Allocation<GlAllocation> alloc = this.gpuMappedBlockAllocator.allocate(size, alignment, minimumAllocation, elementSize);
/* 324 */       GlTransientMemory.TransientGpuBuffer apiBuffer = new GlTransientMemory.TransientGpuBuffer(((GlAllocation)alloc.block()).glBuffer, usage, (int)((GlAllocation)alloc.block()).size);
/* 325 */       ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(((GlAllocation)alloc.block()).hostPtr + alloc.offset(), (int)alloc.size());
/* 326 */       return new GpuBufferSlice.MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {
/*     */           
/*     */           });
/*     */     }
/*     */     
/*     */     public GpuBufferSlice uploadStaging(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 331 */       return upload(data, alignment, usage, minimumAllocation, elementSize, true);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice uploadGpu(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 336 */       return upload(data, alignment, usage, minimumAllocation, elementSize, false);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice upload(List<ByteBuffer> data, long alignment, int usage, long minimumAllocation, long elementSize, boolean staging) {
/* 340 */       long totalSize = 0L;
/* 341 */       for (ByteBuffer buffer : data) {
/* 342 */         totalSize += buffer.remaining();
/* 343 */         totalSize = Mth.roundToward(totalSize, alignment);
/*     */       } 
/* 349 */       GpuBufferSlice.MappedView mapped = staging ? allocateStaging(totalSize, alignment, usage, minimumAllocation, elementSize) : allocateGpuMapped(totalSize, alignment, usage, minimumAllocation, elementSize);
/*     */       try {
/* 351 */         long mappedPtr = MemoryUtil.memAddress(mapped.data());
/* 352 */         long offset = 0L;
/* 353 */         for (ByteBuffer buffer : data) {
/* 354 */           MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), mappedPtr + offset, Math.min(mapped.slice().length() - offset, buffer.remaining()));
/* 355 */           offset += buffer.remaining();
/* 356 */           offset = Mth.roundToward(offset, alignment);
/* 357 */           if (offset >= mapped.slice().length())
/*     */             break; 
/*     */         } 
/* 361 */         GpuBufferSlice gpuBufferSlice = mapped.slice();
/* 362 */         if (mapped != null)
/* 362 */           mapped.close(); 
/*     */         return gpuBufferSlice;
/*     */       } catch (Throwable throwable) {
/*     */         if (mapped != null)
/*     */           try {
/*     */             mapped.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<GpuBufferSlice> multiUploadStaging(List<ByteBuffer> data, long alignment, int usage) {
/* 367 */       return multiUpload(data, alignment, usage, true);
/*     */     }
/*     */     
/*     */     public List<GpuBufferSlice> multiUploadGpu(List<ByteBuffer> data, long alignment, int usage) {
/* 372 */       return multiUpload(data, alignment, usage, false);
/*     */     }
/*     */     
/*     */     public List<GpuBufferSlice> multiUpload(List<ByteBuffer> data, long alignment, int usage, boolean staging) {
/* 376 */       ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
/* 377 */       uploadedBuffers.size(data.size());
/* 379 */       TransientBlockAllocator<?> allocatorInUse = staging ? this.stagingBlockAllocator : this.gpuMappedBlockAllocator;
/* 381 */       IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
/* 382 */       sortedDataIndices.sort(IntComparator.comparing(index -> Integer.valueOf(((ByteBuffer)data.get(index)).remaining())));
/* 383 */       while (!sortedDataIndices.isEmpty()) {
/* 385 */         boolean allocatedAnything = false;
/* 387 */         for (int i = sortedDataIndices.size() - 1; i >= 0; ) {
/* 388 */           int j = sortedDataIndices.getInt(i);
/* 389 */           ByteBuffer byteBuffer = data.get(j);
/* 390 */           if (!allocatorInUse.canAllocateInCurrentBlock(byteBuffer.remaining(), alignment)) {
/*     */             i--;
/*     */             continue;
/*     */           } 
/* 393 */           sortedDataIndices.removeInt(i);
/* 398 */           GpuBufferSlice.MappedView mappedView = staging ? allocateStaging(byteBuffer.remaining(), alignment, usage) : allocateGpuMapped(byteBuffer.remaining(), alignment, usage);
/*     */           try {
/* 400 */             MemoryUtil.memCopy(byteBuffer, mappedView.data());
/* 401 */             uploadedBuffers.set(j, mappedView.slice());
/* 402 */             if (mappedView != null)
/* 402 */               mappedView.close(); 
/*     */           } catch (Throwable throwable) {
/*     */             if (mappedView != null)
/*     */               try {
/*     */                 mappedView.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               }  
/*     */             throw throwable;
/*     */           } 
/* 403 */           allocatedAnything = true;
/*     */         } 
/* 406 */         if (allocatedAnything)
/*     */           continue; 
/* 411 */         int bufferIndex = sortedDataIndices.popInt();
/* 412 */         ByteBuffer currentBuffer = data.get(bufferIndex);
/* 413 */         GpuBufferSlice.MappedView view = allocateGpuMapped(currentBuffer.remaining(), alignment, usage);
/*     */         try {
/* 414 */           MemoryUtil.memCopy(currentBuffer, view.data());
/* 415 */           uploadedBuffers.set(bufferIndex, view.slice());
/* 416 */           if (view != null)
/* 416 */             view.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (view != null)
/*     */             try {
/*     */               view.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } 
/* 418 */       return (List<GpuBufferSlice>)uploadedBuffers;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Rotation extends Record implements Runnable {
/*     */     private final Runnable staging;
/*     */     
/*     */     private final Runnable gpu;
/*     */     
/*     */     private final Runnable gpuMapped;
/*     */     
/*     */     private Rotation(Runnable staging, Runnable gpu, Runnable gpuMapped) {
/*     */       this.staging = staging;
/*     */       this.gpu = gpu;
/*     */       this.gpuMapped = gpuMapped;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Runnable staging() {
/*     */       return this.staging;
/*     */     }
/*     */     
/*     */     public Runnable gpu() {
/*     */       return this.gpu;
/*     */     }
/*     */     
/*     */     public Runnable gpuMapped() {
/*     */       return this.gpuMapped;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       this.staging.run();
/*     */       this.gpu.run();
/*     */       this.gpuMapped.run();
/*     */     }
/*     */   }
/*     */   
/*     */   private class TransientGpuBuffer extends GlBuffer {
/*     */     private boolean closed = false;
/*     */     
/*     */     private final long bufferSubmitIndex;
/*     */     
/*     */     protected TransientGpuBuffer(int handle, int usage, long size) {
/* 427 */       super(usage, size, handle, true);
/* 428 */       this.bufferSubmitIndex = GlTransientMemory.this.encoder.currentSubmitIndex();
/*     */     }
/*     */     
/*     */     public boolean isClosed() {
/* 433 */       if (this.closed)
/* 434 */         return true; 
/* 436 */       this.closed = (this.bufferSubmitIndex < GlTransientMemory.this.encoder.currentSubmitIndex());
/* 437 */       return this.closed;
/*     */     }
/*     */     
/*     */     public void close() {
/* 442 */       this.closed = true;
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/* 447 */       throw new IllegalStateException("Cannot map Transient buffer");
/*     */     }
/*     */     
/*     */     public GpuBufferSlice slice(long offset, long length) {
/* 452 */       throw new IllegalStateException("Cannot slice transient buffer");
/*     */     }
/*     */     
/*     */     public GpuBufferSlice slice() {
/* 457 */       throw new IllegalStateException("Cannot slice transient buffer");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */