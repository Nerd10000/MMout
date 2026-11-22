/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.CompactVectorArray;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class StagedVertexBuffer implements AutoCloseable {
/*     */   private final ByteBufferBuilder stagingBuffer;
/*     */   
/*     */   private final GpuBufferPool stagingGpuBufferPool;
/*     */   
/*  31 */   private final List<Draw> draws = new ArrayList<>();
/*     */   
/*     */   private Draw lastBuildingDraw;
/*     */   
/*     */   private BufferBuilder lastVertexBuilder;
/*     */   
/*     */   private final GpuBufferPool vertexBufferPool;
/*     */   
/*     */   private final GpuBufferPool indexBufferPool;
/*     */   
/*     */   private GpuBuffer currentVertexBuffer;
/*     */   
/*     */   private GpuBuffer currentIndexBuffer;
/*     */   
/*     */   public StagedVertexBuffer(Supplier<String> label, int initialCapacity) {
/*  41 */     this.stagingBuffer = new ByteBufferBuilder(initialCapacity);
/*  43 */     this.stagingGpuBufferPool = new GpuBufferPool(() -> (String)label.get() + " - Staging", 22);
/*  44 */     this.vertexBufferPool = new GpuBufferPool(() -> (String)label.get() + " - Vertex", 40);
/*  45 */     this.indexBufferPool = new GpuBufferPool(() -> (String)label.get() + " - Index", 72);
/*     */   }
/*     */   
/*     */   public Draw appendDraw(VertexFormat format, PrimitiveTopology primitiveTopology) {
/*  49 */     return appendDraw(format, primitiveTopology, null);
/*     */   }
/*     */   
/*     */   public Draw appendDraw(VertexFormat format, PrimitiveTopology primitiveTopology, VertexSorting quadSorting) {
/*  53 */     if (this.currentVertexBuffer != null)
/*  54 */       throw new IllegalStateException("Cannot append draw after upload"); 
/*  56 */     if (quadSorting != null && primitiveTopology != PrimitiveTopology.QUADS)
/*  57 */       throw new IllegalArgumentException("Cannot sort draw with " + String.valueOf(primitiveTopology)); 
/*  59 */     Draw draw = new Draw(format, primitiveTopology, quadSorting);
/*  60 */     this.draws.add(draw);
/*  61 */     return draw;
/*     */   }
/*     */   
/*     */   public VertexConsumer getVertexBuilder(Draw draw) {
/*  65 */     if (this.currentVertexBuffer != null)
/*  66 */       throw new IllegalStateException("Cannot append draw after upload"); 
/*  68 */     if (this.lastBuildingDraw == draw)
/*  69 */       return (VertexConsumer)Objects.requireNonNull(this.lastVertexBuilder); 
/*  71 */     finishLastVertexBuilder();
/*  72 */     this.lastBuildingDraw = draw;
/*  73 */     this.lastVertexBuilder = new BufferBuilder(this.stagingBuffer, draw.primitiveTopology, draw.format);
/*  74 */     return (VertexConsumer)this.lastVertexBuilder;
/*     */   }
/*     */   
/*     */   private void finishLastVertexBuilder() {
/*  78 */     if (this.lastVertexBuilder == null)
/*     */       return; 
/*  81 */     MeshData mesh = this.lastVertexBuilder.build();
/*  82 */     if (mesh != null)
/*  83 */       ((Draw)Objects.<Draw>requireNonNull(this.lastBuildingDraw)).append(mesh); 
/*  85 */     this.lastVertexBuilder = null;
/*  86 */     this.lastBuildingDraw = null;
/*     */   }
/*     */   
/*     */   public void upload() {
/*  90 */     if (this.currentVertexBuffer != null)
/*  91 */       throw new IllegalStateException("Already uploaded"); 
/*  92 */     if (this.draws.isEmpty())
/*     */       return; 
/*  96 */     finishLastVertexBuilder();
/*  98 */     int nextVertexOffset = 0;
/*  99 */     int nextIndexOffset = 0;
/* 101 */     for (Draw draw : this.draws) {
/* 102 */       if (draw.isEmpty())
/*     */         continue; 
/* 107 */       draw.vertexOffset = Mth.roundToward(nextVertexOffset, draw.format.getVertexSize());
/* 108 */       nextVertexOffset = draw.vertexOffset + draw.vertexBufferSize;
/* 110 */       if (draw.quadSorting != null) {
/* 111 */         IndexType indexType = draw.indexType();
/* 113 */         draw.indexOffset = Mth.roundToward(nextIndexOffset, indexType.bytes);
/* 114 */         nextIndexOffset = draw.indexOffset + draw.indexCount * indexType.bytes;
/*     */         continue;
/*     */       } 
/* 117 */       RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(draw.primitiveTopology);
/* 118 */       autoIndices.getBuffer(draw.indexCount);
/*     */     } 
/* 122 */     int vertexBufferSize = nextVertexOffset;
/* 123 */     int indexBufferSize = nextIndexOffset;
/* 124 */     if (vertexBufferSize == 0)
/*     */       return; 
/* 128 */     GpuDevice device = RenderSystem.getDevice();
/* 129 */     this.currentVertexBuffer = this.vertexBufferPool.acquire(device, vertexBufferSize);
/* 130 */     this.currentIndexBuffer = (indexBufferSize > 0) ? this.indexBufferPool.acquire(device, indexBufferSize) : null;
/* 132 */     uploadDrawsToBuffers(device, this.draws, this.currentVertexBuffer, this.currentIndexBuffer, vertexBufferSize, indexBufferSize);
/*     */   }
/*     */   
/*     */   private void uploadDrawsToBuffers(GpuDevice device, List<Draw> draws, GpuBuffer vertexGpuBuffer, GpuBuffer indexGpuBuffer, int vertexBufferSize, int indexBufferSize) {
/* 143 */     CommandEncoder commandEncoder = device.createCommandEncoder();
/* 146 */     int stagingBufferSize = vertexBufferSize + indexBufferSize;
/* 147 */     GpuBuffer stagingBuffer = this.stagingGpuBufferPool.acquire(device, stagingBufferSize);
/* 148 */     GpuBufferSlice.MappedView view = stagingBuffer.slice(0L, stagingBufferSize).map(false, true);
/*     */     try {
/* 149 */       ByteBuffer buffer = view.data();
/* 150 */       for (Draw draw : draws) {
/* 151 */         if (draw.isEmpty())
/*     */           continue; 
/* 154 */         buffer.position(draw.vertexOffset);
/* 155 */         for (ByteBufferBuilder.Result slice : draw.vertexBufferSlices)
/* 156 */           buffer.put(slice.byteBuffer()); 
/*     */       } 
/* 159 */       for (Draw draw : draws) {
/* 160 */         if (draw.isEmpty())
/*     */           continue; 
/* 163 */         if (indexGpuBuffer != null && draw.quadSorting != null) {
/* 164 */           MeshData.SortState sortState = new MeshData.SortState(decodeSortingPoints(draw), draw.indexType());
/* 165 */           buffer.position(vertexBufferSize + draw.indexOffset);
/* 166 */           sortState.writeSortedIndexBuffer(buffer, draw.quadSorting);
/*     */         } 
/* 168 */         draw.freeVertexData();
/*     */       } 
/* 170 */       if (view != null)
/* 170 */         view.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (view != null)
/*     */         try {
/*     */           view.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 172 */     commandEncoder.copyToBuffer(stagingBuffer.slice(0L, vertexBufferSize), vertexGpuBuffer.slice(0L, vertexBufferSize));
/* 173 */     if (indexGpuBuffer != null)
/* 174 */       commandEncoder.copyToBuffer(stagingBuffer.slice(vertexBufferSize, indexBufferSize), indexGpuBuffer.slice(0L, indexBufferSize)); 
/*     */   }
/*     */   
/*     */   private static CompactVectorArray decodeSortingPoints(Draw draw) {
/* 179 */     VertexFormat format = draw.format;
/* 180 */     CompactVectorArray points = new CompactVectorArray(draw.vertexCount / 4);
/* 181 */     int offset = 0;
/* 182 */     for (ByteBufferBuilder.Result vertexBuffer : draw.vertexBufferSlices) {
/* 183 */       int vertexCount = vertexBuffer.size() / format.getVertexSize();
/* 184 */       MeshData.decodeQuadCentroids(vertexBuffer.byteBuffer(), vertexCount, format, points, offset);
/* 185 */       offset += vertexCount / 4;
/*     */     } 
/* 187 */     return points;
/*     */   }
/*     */   
/*     */   public ExecuteInfo getExecuteInfo(Draw draw) {
/* 191 */     if (draw.isEmpty())
/* 192 */       return null; 
/* 194 */     if (this.currentVertexBuffer == null)
/* 195 */       throw new IllegalStateException("Cannot execute before upload"); 
/* 197 */     int baseVertex = draw.vertexOffset / draw.format.getVertexSize();
/* 198 */     if (this.currentIndexBuffer != null && draw.quadSorting != null) {
/* 199 */       IndexType indexType = draw.indexType();
/* 200 */       int firstIndex = draw.indexOffset / indexType.bytes;
/* 201 */       return new ExecuteInfo(this.currentVertexBuffer, this.currentIndexBuffer, indexType, baseVertex, firstIndex, draw.indexCount);
/*     */     } 
/* 203 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(draw.primitiveTopology);
/* 204 */     GpuBuffer indexBuffer = autoIndices.getBuffer(draw.indexCount);
/* 205 */     return new ExecuteInfo(this.currentVertexBuffer, indexBuffer, autoIndices.type(), baseVertex, 0, draw.indexCount);
/*     */   }
/*     */   
/*     */   public void endDraw() {
/* 210 */     this.draws.clear();
/* 211 */     this.currentVertexBuffer = null;
/* 212 */     this.currentIndexBuffer = null;
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 216 */     endDraw();
/* 217 */     GpuDevice device = RenderSystem.getDevice();
/* 218 */     this.stagingGpuBufferPool.endFrame(device);
/* 219 */     this.vertexBufferPool.endFrame(device);
/* 220 */     this.indexBufferPool.endFrame(device);
/*     */   }
/*     */   
/*     */   public void close() {
/* 225 */     this.stagingBuffer.close();
/* 226 */     this.stagingGpuBufferPool.close();
/* 227 */     this.vertexBufferPool.close();
/* 228 */     this.indexBufferPool.close();
/*     */   }
/*     */   
/*     */   private static class GpuBufferPool implements AutoCloseable {
/*     */     private static final int BUFFER_SIZE_INCREMENT = 262144;
/*     */     
/*     */     private static final int MAX_REUSE_SIZE_FACTOR = 4;
/*     */     
/*     */     private final Supplier<String> label;
/*     */     
/*     */     private final int usage;
/*     */     
/* 241 */     private final List<GpuBuffer> available = new ArrayList<>();
/*     */     
/* 242 */     private final List<GpuBuffer> usedThisFrame = new ArrayList<>();
/*     */     
/* 243 */     private final List<PendingRecycle> pendingRecycle = new ArrayList<>();
/*     */     
/*     */     private GpuBufferPool(Supplier<String> label, int usage) {
/* 246 */       this.label = label;
/* 247 */       this.usage = usage;
/*     */     }
/*     */     
/*     */     private void tryRecycleBuffers() {
/* 251 */       this.pendingRecycle.removeIf(buffer -> {
/*     */             List<GpuBuffer> recycled = buffer.tryRecycle();
/*     */             if (recycled != null) {
/*     */               this.available.addAll(recycled);
/*     */               return true;
/*     */             } 
/*     */             return false;
/*     */           });
/*     */     }
/*     */     
/*     */     public GpuBuffer acquire(GpuDevice device, int minSize) {
/* 262 */       tryRecycleBuffers();
/* 263 */       int roundedMinSize = Mth.roundToward(minSize, 262144);
/* 264 */       GpuBuffer buffer = takeBestAvailable(roundedMinSize, roundedMinSize * 4);
/* 265 */       if (buffer == null)
/* 266 */         buffer = device.createBuffer(this.label, this.usage, roundedMinSize); 
/* 268 */       this.usedThisFrame.add(buffer);
/* 269 */       return buffer;
/*     */     }
/*     */     
/*     */     private GpuBuffer takeBestAvailable(int minSize, int maxSize) {
/* 273 */       int bestIndex = -1;
/* 274 */       long bestSize = (maxSize + 1);
/* 275 */       for (int i = 0; i < this.available.size(); i++) {
/* 276 */         long size = ((GpuBuffer)this.available.get(i)).size();
/* 277 */         if (size == minSize)
/* 278 */           return this.available.remove(i); 
/* 280 */         if (size > minSize && size < bestSize) {
/* 281 */           bestIndex = i;
/* 282 */           bestSize = size;
/*     */         } 
/*     */       } 
/* 285 */       if (bestIndex == -1)
/* 286 */         return null; 
/* 288 */       return this.available.remove(bestIndex);
/*     */     }
/*     */     
/*     */     public void endFrame(GpuDevice device) {
/* 292 */       if (!this.usedThisFrame.isEmpty()) {
/* 293 */         GpuFence fence = device.createCommandEncoder().createFence();
/* 294 */         this.pendingRecycle.add(new PendingRecycle(List.copyOf(this.usedThisFrame), fence));
/* 295 */         this.usedThisFrame.clear();
/*     */       } 
/* 300 */       if (!this.available.isEmpty()) {
/* 301 */         this.available.forEach(GpuBuffer::close);
/* 302 */         this.available.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void close() {
/* 308 */       this.available.forEach(GpuBuffer::close);
/* 309 */       this.usedThisFrame.forEach(GpuBuffer::close);
/* 310 */       this.pendingRecycle.forEach(PendingRecycle::close);
/* 311 */       this.available.clear();
/* 312 */       this.usedThisFrame.clear();
/* 313 */       this.pendingRecycle.clear();
/*     */     }
/*     */     
/*     */     private static final class PendingRecycle extends Record implements AutoCloseable {
/*     */       private final List<GpuBuffer> buffers;
/*     */       
/*     */       private final GpuFence fence;
/*     */       
/*     */       private PendingRecycle(List<GpuBuffer> buffers, GpuFence fence) {
/* 316 */         this.buffers = buffers;
/* 316 */         this.fence = fence;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #316	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #316	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #316	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$GpuBufferPool$PendingRecycle;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public List<GpuBuffer> buffers() {
/* 316 */         return this.buffers;
/*     */       }
/*     */       
/*     */       public GpuFence fence() {
/* 316 */         return this.fence;
/*     */       }
/*     */       
/*     */       public List<GpuBuffer> tryRecycle() {
/* 318 */         if (this.fence.awaitCompletion(0L)) {
/* 319 */           this.fence.close();
/* 320 */           return this.buffers;
/*     */         } 
/* 322 */         return null;
/*     */       }
/*     */       
/*     */       public void close() {
/* 327 */         this.buffers.forEach(GpuBuffer::close);
/* 328 */         this.fence.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PendingRecycle extends Record implements AutoCloseable {
/*     */     private final List<GpuBuffer> buffers;
/*     */     
/*     */     private final GpuFence fence;
/*     */     
/*     */     private PendingRecycle(List<GpuBuffer> buffers, GpuFence fence) {
/*     */       this.buffers = buffers;
/*     */       this.fence = fence;
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
/*     */       return this.buffers;
/*     */     }
/*     */     
/*     */     public GpuFence fence() {
/*     */       return this.fence;
/*     */     }
/*     */     
/*     */     public List<GpuBuffer> tryRecycle() {
/*     */       if (this.fence.awaitCompletion(0L)) {
/*     */         this.fence.close();
/*     */         return this.buffers;
/*     */       } 
/*     */       return null;
/*     */     }
/*     */     
/*     */     public void close() {
/*     */       this.buffers.forEach(GpuBuffer::close);
/* 328 */       this.fence.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Draw {
/*     */     private final VertexFormat format;
/*     */     
/*     */     private final PrimitiveTopology primitiveTopology;
/*     */     
/*     */     private final VertexSorting quadSorting;
/*     */     
/* 338 */     private final List<ByteBufferBuilder.Result> vertexBufferSlices = new ArrayList<>();
/*     */     
/*     */     private int vertexBufferSize;
/*     */     
/*     */     private int vertexCount;
/*     */     
/*     */     private int indexCount;
/*     */     
/*     */     private int vertexOffset;
/*     */     
/*     */     private int indexOffset;
/*     */     
/*     */     private Draw(VertexFormat format, PrimitiveTopology primitiveTopology, VertexSorting quadSorting) {
/* 348 */       this.format = format;
/* 349 */       this.primitiveTopology = primitiveTopology;
/* 350 */       this.quadSorting = quadSorting;
/*     */     }
/*     */     
/*     */     private void append(MeshData mesh) {
/* 354 */       assert mesh.indexBuffer() == null;
/* 355 */       this.vertexBufferSlices.add(mesh.vertexBufferSlice());
/* 356 */       this.vertexBufferSize += mesh.vertexBuffer().remaining();
/* 357 */       this.vertexCount += mesh.drawState().vertexCount();
/* 358 */       this.indexCount += mesh.drawState().indexCount();
/*     */     }
/*     */     
/*     */     private IndexType indexType() {
/* 362 */       return IndexType.least(this.vertexCount);
/*     */     }
/*     */     
/*     */     private void freeVertexData() {
/* 366 */       this.vertexBufferSlices.forEach(ByteBufferBuilder.Result::close);
/* 367 */       this.vertexBufferSlices.clear();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 371 */       return (this.vertexCount == 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ExecuteInfo extends Record {
/*     */     private final GpuBuffer vertexBuffer;
/*     */     
/*     */     private final GpuBuffer indexBuffer;
/*     */     
/*     */     private final IndexType indexType;
/*     */     
/*     */     private final int baseVertex;
/*     */     
/*     */     private final int firstIndex;
/*     */     
/*     */     private final int indexCount;
/*     */     
/*     */     public ExecuteInfo(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int baseVertex, int firstIndex, int indexCount) {
/* 375 */       this.vertexBuffer = vertexBuffer;
/* 375 */       this.indexBuffer = indexBuffer;
/* 375 */       this.indexType = indexType;
/* 375 */       this.baseVertex = baseVertex;
/* 375 */       this.firstIndex = firstIndex;
/* 375 */       this.indexCount = indexCount;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #375	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #375	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #375	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/StagedVertexBuffer$ExecuteInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GpuBuffer vertexBuffer() {
/* 375 */       return this.vertexBuffer;
/*     */     }
/*     */     
/*     */     public GpuBuffer indexBuffer() {
/* 375 */       return this.indexBuffer;
/*     */     }
/*     */     
/*     */     public IndexType indexType() {
/* 375 */       return this.indexType;
/*     */     }
/*     */     
/*     */     public int baseVertex() {
/* 375 */       return this.baseVertex;
/*     */     }
/*     */     
/*     */     public int firstIndex() {
/* 375 */       return this.firstIndex;
/*     */     }
/*     */     
/*     */     public int indexCount() {
/* 375 */       return this.indexCount;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\StagedVertexBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */