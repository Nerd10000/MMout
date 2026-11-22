/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.StagingBuffer;
/*     */ import com.mojang.blaze3d.vertex.TlsfAllocator;
/*     */ import com.mojang.blaze3d.vertex.UberGpuBuffer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.TracingExecutor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.RotatingSectionStorage;
/*     */ import net.minecraft.client.renderer.RenderBuffers;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPool;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SectionRenderDispatcher {
/*     */   public static final int NEARBY_SECTION_DISTANCE_IN_BLOCKS = 32;
/*     */   
/*  42 */   private final SectionTaskDynamicQueue queue = new SectionTaskDynamicQueue();
/*     */   
/*     */   private final SectionBufferBuilderPack fixedBuffers;
/*     */   
/*     */   private final SectionBufferBuilderPool bufferPool;
/*     */   
/*     */   private volatile boolean closed;
/*     */   
/*     */   private final TracingExecutor executor;
/*     */   
/*     */   private final Consumer<RenderSection> onSectionMeshUpdate;
/*     */   
/*  50 */   private final AtomicReference<Vec3> cameraPosition = new AtomicReference<>(Vec3.ZERO);
/*     */   
/*     */   private volatile SectionCompiler sectionCompiler;
/*     */   
/*     */   private final StagingBuffer stagingBuffer;
/*     */   
/*     */   private final Map<ChunkSectionLayer, SectionUberBuffers> chunkUberBuffers;
/*     */   
/*  56 */   private final ReentrantLock copyLock = new ReentrantLock();
/*     */   
/*     */   public SectionRenderDispatcher(TracingExecutor executor, RenderBuffers renderBuffers, SectionCompiler sectionCompiler, Consumer<RenderSection> onSectionMeshUpdate) {
/*  59 */     this.onSectionMeshUpdate = onSectionMeshUpdate;
/*  60 */     this.fixedBuffers = renderBuffers.fixedBufferPack();
/*  61 */     this.bufferPool = renderBuffers.sectionBufferPool();
/*  62 */     this.executor = executor;
/*  64 */     this.sectionCompiler = sectionCompiler;
/*  66 */     int vertexBufferHeapSize = 134217728;
/*  67 */     int indexBufferHeapSize = 33554432;
/*  68 */     int stagingBufferSize = 102760448;
/*  70 */     GpuDevice gpuDevice = RenderSystem.getDevice();
/*  72 */     this.stagingBuffer = StagingBuffer.create("Chunk", gpuDevice, 102760448);
/*  74 */     this.chunkUberBuffers = Util.makeEnumMap(ChunkSectionLayer.class, layer -> {
/*     */           VertexFormat vertexFormat = layer.pipeline().getVertexFormatBinding(0);
/*     */           UberGpuBuffer<SectionMesh> vertexUberBuffer = new UberGpuBuffer(layer.label(), 32, 134217728, vertexFormat.getVertexSize(), this.stagingBuffer);
/*     */           UberGpuBuffer<SectionMesh> indexUberBuffer = new UberGpuBuffer(layer.label(), 64, 33554432, 8, this.stagingBuffer);
/*     */           return new SectionUberBuffers(vertexUberBuffer, indexUberBuffer);
/*     */         });
/*     */   }
/*     */   
/*     */   public void setCompiler(SectionCompiler sectionCompiler) {
/*  84 */     this.sectionCompiler = sectionCompiler;
/*     */   }
/*     */   
/*     */   private void runTask() {
/*  89 */     if (this.closed)
/*     */       return; 
/*  93 */     RenderSection.SectionTask task = this.queue.poll(this.cameraPosition.get());
/*  94 */     if (task == null || task.isCompleted.get() || task.isCancelled.get())
/*     */       return; 
/*     */     try {
/*  99 */       SectionBufferBuilderPack buffer = Objects.<SectionBufferBuilderPack>requireNonNull(this.bufferPool.acquire());
/* 100 */       RenderSection.SectionTask.SectionTaskResult result = task.doTask(buffer);
/* 101 */       task.isCompleted.set(true);
/* 102 */       if (result == RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL) {
/* 103 */         buffer.clearAll();
/*     */       } else {
/* 105 */         buffer.discardAll();
/*     */       } 
/* 107 */       this.bufferPool.release(buffer);
/* 112 */       this.executor.execute(this::runTask);
/* 113 */     } catch (NullPointerException e) {
/* 115 */       this.queue.add(task);
/* 116 */     } catch (Exception e) {
/* 117 */       Minecraft.getInstance().delayCrash(CrashReport.forThrowable(e, "Batching sections"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCameraPosition(Vec3 cameraPosition) {
/* 122 */     this.cameraPosition.set(cameraPosition);
/*     */   }
/*     */   
/*     */   public static final class RenderSectionBufferSlice extends Record {
/*     */     private final GpuBuffer vertexBuffer;
/*     */     
/*     */     private final long vertexBufferOffset;
/*     */     
/*     */     private final GpuBuffer indexBuffer;
/*     */     
/*     */     private final long indexBufferOffset;
/*     */     
/*     */     public RenderSectionBufferSlice(GpuBuffer vertexBuffer, long vertexBufferOffset, GpuBuffer indexBuffer, long indexBufferOffset) {
/* 125 */       this.vertexBuffer = vertexBuffer;
/* 125 */       this.vertexBufferOffset = vertexBufferOffset;
/* 125 */       this.indexBuffer = indexBuffer;
/* 125 */       this.indexBufferOffset = indexBufferOffset;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSectionBufferSlice;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public GpuBuffer vertexBuffer() {
/* 125 */       return this.vertexBuffer;
/*     */     }
/*     */     
/*     */     public long vertexBufferOffset() {
/* 125 */       return this.vertexBufferOffset;
/*     */     }
/*     */     
/*     */     public GpuBuffer indexBuffer() {
/* 125 */       return this.indexBuffer;
/*     */     }
/*     */     
/*     */     public long indexBufferOffset() {
/* 125 */       return this.indexBufferOffset;
/*     */     }
/*     */   }
/*     */   
/*     */   public RenderSectionBufferSlice getRenderSectionSlice(SectionMesh sectionMesh, ChunkSectionLayer layer) {
/* 128 */     SectionUberBuffers uberBuffers = this.chunkUberBuffers.get(layer);
/* 130 */     TlsfAllocator.Allocation vertexSlice = uberBuffers.vertexBuffer.getAllocation(sectionMesh);
/* 131 */     if (vertexSlice == null)
/* 132 */       return null; 
/* 134 */     long vertexBufferOffset = vertexSlice.getOffsetFromHeap();
/* 135 */     TlsfAllocator.Allocation indexSlice = uberBuffers.indexBuffer.getAllocation(sectionMesh);
/* 136 */     long indexBufferOffset = 0L;
/* 137 */     GpuBuffer indexBuffer = null;
/* 138 */     if (indexSlice != null) {
/* 139 */       indexBufferOffset = indexSlice.getOffsetFromHeap();
/* 140 */       indexBuffer = uberBuffers.indexBuffer.getGpuBuffer(indexSlice);
/*     */     } 
/* 143 */     return new RenderSectionBufferSlice(uberBuffers.vertexBuffer.getGpuBuffer(vertexSlice), vertexBufferOffset, indexBuffer, indexBufferOffset);
/*     */   }
/*     */   
/*     */   public void lock() {
/* 147 */     this.copyLock.lock();
/*     */   }
/*     */   
/*     */   public void unlock() {
/* 151 */     this.copyLock.unlock();
/*     */   }
/*     */   
/*     */   public void uploadTerrainBuffersToGpu() {
/* 155 */     GpuDevice device = RenderSystem.getDevice();
/* 156 */     StagingBuffer.Uploader uploader = this.stagingBuffer.startUploading(device.createCommandEncoder());
/*     */     try {
/* 161 */       for (SectionUberBuffers buffers : this.chunkUberBuffers.values()) {
/* 162 */         boolean performedBufferResize = buffers.vertexBuffer.uploadStagedAllocations(device, uploader);
/* 163 */         buffers.indexBuffer.uploadStagedAllocations(device, uploader);
/* 167 */         if (performedBufferResize)
/*     */           break; 
/*     */       } 
/* 171 */       if (uploader != null)
/* 171 */         uploader.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (uploader != null)
/*     */         try {
/*     */           uploader.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void schedule(RenderSection.SectionTask task) {
/* 175 */     if (this.closed)
/*     */       return; 
/* 179 */     this.queue.add(task);
/* 180 */     this.executor.execute(this::runTask);
/*     */   }
/*     */   
/*     */   public void clearCompileQueue() {
/* 184 */     this.queue.clear();
/*     */   }
/*     */   
/*     */   public boolean isQueueEmpty() {
/* 189 */     return (this.queue.size() == 0);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 193 */     this.closed = true;
/* 194 */     clearCompileQueue();
/* 195 */     this.copyLock.lock();
/*     */     try {
/* 197 */       for (SectionUberBuffers buffers : this.chunkUberBuffers.values()) {
/* 198 */         buffers.vertexBuffer.close();
/* 199 */         buffers.indexBuffer.close();
/*     */       } 
/* 201 */       this.stagingBuffer.close();
/*     */     } finally {
/* 203 */       this.copyLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String getStats() {
/* 209 */     return String.format(Locale.ROOT, "pC: %03d, aB: %02d", new Object[] { Integer.valueOf(this.queue.size()), Integer.valueOf(this.bufferPool.getFreeBufferCount()) });
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getCompileQueueSize() {
/* 214 */     return this.queue.size();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getFreeBufferCount() {
/* 219 */     return this.bufferPool.getFreeBufferCount();
/*     */   }
/*     */   
/*     */   public class RenderSection implements RotatingSectionStorage.Value {
/*     */     public final int index;
/*     */     
/* 224 */     public final AtomicReference<SectionMesh> sectionMesh = new AtomicReference<>(CompiledSectionMesh.UNCOMPILED);
/*     */     
/*     */     private CompileTask lastCompileTask;
/*     */     
/*     */     private ResortTransparencyTask lastResortTransparencyTask;
/*     */     
/*     */     private AABB bb;
/*     */     
/* 230 */     private volatile long sectionNode = SectionPos.asLong(-1, -1, -1);
/*     */     
/* 231 */     private final BlockPos.MutableBlockPos renderOrigin = new BlockPos.MutableBlockPos(-1, -1, -1);
/*     */     
/*     */     private long uploadedTime;
/*     */     
/*     */     private long fadeDuration;
/*     */     
/*     */     private boolean wasPreviouslyEmpty;
/*     */     
/*     */     public RenderSection(int index, long sectionNode) {
/* 238 */       this.index = index;
/* 239 */       setSectionNode(sectionNode);
/*     */     }
/*     */     
/*     */     public float getVisibility(long now) {
/* 243 */       long elapsed = now - this.uploadedTime;
/* 244 */       if (elapsed >= this.fadeDuration)
/* 245 */         return 1.0F; 
/* 247 */       return (float)elapsed / (float)this.fadeDuration;
/*     */     }
/*     */     
/*     */     public void setFadeDuration(long fadeDuration) {
/* 251 */       this.fadeDuration = fadeDuration;
/*     */     }
/*     */     
/*     */     public void setWasPreviouslyEmpty(boolean wasPreviouslyEmpty) {
/* 255 */       this.wasPreviouslyEmpty = wasPreviouslyEmpty;
/*     */     }
/*     */     
/*     */     public boolean wasPreviouslyEmpty() {
/* 259 */       return this.wasPreviouslyEmpty;
/*     */     }
/*     */     
/*     */     public AABB getBoundingBox() {
/* 263 */       return this.bb;
/*     */     }
/*     */     
/*     */     public void setSectionNode(long sectionNode) {
/* 268 */       reset();
/* 269 */       this.sectionNode = sectionNode;
/* 270 */       int x = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
/* 271 */       int y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 272 */       int z = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
/* 273 */       this.renderOrigin.set(x, y, z);
/* 274 */       this.bb = new AABB(x, y, z, (x + 16), (y + 16), (z + 16));
/*     */     }
/*     */     
/*     */     public SectionMesh getSectionMesh() {
/* 278 */       return this.sectionMesh.get();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 282 */       cancelTasks();
/* 283 */       SectionMesh mesh = this.sectionMesh.getAndSet(CompiledSectionMesh.UNCOMPILED);
/* 284 */       SectionRenderDispatcher.this.copyLock.lock();
/*     */       try {
/* 286 */         releaseSectionMesh(mesh);
/*     */       } finally {
/* 288 */         SectionRenderDispatcher.this.copyLock.unlock();
/*     */       } 
/* 290 */       this.uploadedTime = 0L;
/* 291 */       this.wasPreviouslyEmpty = false;
/*     */     }
/*     */     
/*     */     public BlockPos getRenderOrigin() {
/* 296 */       return (BlockPos)this.renderOrigin;
/*     */     }
/*     */     
/*     */     public long getSectionNode() {
/* 301 */       return this.sectionNode;
/*     */     }
/*     */     
/*     */     public long getNeighborSectionNode(Direction direction) {
/* 305 */       return SectionPos.offset(this.sectionNode, direction);
/*     */     }
/*     */     
/*     */     public void resortTransparency() {
/* 309 */       SectionMesh sectionMesh = getSectionMesh();
/* 309 */       if (sectionMesh instanceof CompiledSectionMesh) {
/* 309 */         CompiledSectionMesh mesh = (CompiledSectionMesh)sectionMesh;
/* 310 */         this.lastResortTransparencyTask = new ResortTransparencyTask(mesh);
/* 311 */         SectionRenderDispatcher.this.schedule(this.lastResortTransparencyTask);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasTranslucentGeometry() {
/* 316 */       return getSectionMesh().hasTranslucentGeometry();
/*     */     }
/*     */     
/*     */     public boolean transparencyResortingScheduled() {
/* 320 */       return (this.lastResortTransparencyTask != null && !this.lastResortTransparencyTask.isCompleted.get());
/*     */     }
/*     */     
/*     */     private void cancelTasks() {
/* 324 */       if (this.lastCompileTask != null) {
/* 325 */         this.lastCompileTask.cancel();
/* 326 */         this.lastCompileTask = null;
/*     */       } 
/* 328 */       if (this.lastResortTransparencyTask != null) {
/* 329 */         this.lastResortTransparencyTask.cancel();
/* 330 */         this.lastResortTransparencyTask = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private SectionTask createCompileTask(RenderSectionRegion region) {
/* 335 */       cancelTasks();
/* 336 */       boolean isRecompile = (this.sectionMesh.get() != CompiledSectionMesh.UNCOMPILED);
/* 337 */       this.lastCompileTask = new CompileTask(region, isRecompile);
/* 338 */       return this.lastCompileTask;
/*     */     }
/*     */     
/*     */     public void compileAsync(RenderSectionRegion region) {
/* 342 */       SectionTask task = createCompileTask(region);
/* 343 */       SectionRenderDispatcher.this.schedule(task);
/*     */     }
/*     */     
/*     */     public void compileSync(RenderSectionRegion region) {
/* 347 */       SectionTask task = createCompileTask(region);
/* 348 */       task.doTask(SectionRenderDispatcher.this.fixedBuffers);
/*     */     }
/*     */     
/*     */     private SectionMesh setSectionMesh(SectionMesh sectionMesh) {
/* 352 */       SectionMesh oldMesh = this.sectionMesh.getAndSet(sectionMesh);
/* 354 */       SectionRenderDispatcher.this.onSectionMeshUpdate.accept(this);
/* 355 */       if (this.uploadedTime == 0L)
/* 356 */         this.uploadedTime = Util.getMillis(); 
/* 359 */       return oldMesh;
/*     */     }
/*     */     
/*     */     private void releaseSectionMesh(SectionMesh oldMesh) {
/* 363 */       oldMesh.close();
/* 364 */       for (SectionRenderDispatcher.SectionUberBuffers buffers : SectionRenderDispatcher.this.chunkUberBuffers.values()) {
/* 365 */         buffers.vertexBuffer.removeAllocation(oldMesh);
/* 366 */         buffers.indexBuffer.removeAllocation(oldMesh);
/*     */       } 
/*     */     }
/*     */     
/*     */     private VertexSorting createVertexSorting(SectionPos sectionPos, Vec3 cameraPos) {
/* 371 */       return VertexSorting.byDistance(
/* 372 */           (float)(cameraPos.x - sectionPos.minBlockX()), 
/* 373 */           (float)(cameraPos.y - sectionPos.minBlockY()), 
/* 374 */           (float)(cameraPos.z - sectionPos.minBlockZ()));
/*     */     }
/*     */     
/*     */     private void checkSectionMesh(CompiledSectionMesh compiledSectionMesh) {
/* 380 */       boolean allBuffersUpdated = true;
/* 381 */       for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
/* 382 */         SectionMesh.SectionDraw draw = compiledSectionMesh.getSectionDraw(layer);
/* 383 */         if (draw != null) {
/* 384 */           allBuffersUpdated &= compiledSectionMesh.isIndexBufferUploaded(layer);
/* 385 */           allBuffersUpdated &= compiledSectionMesh.isVertexBufferUploaded(layer);
/*     */         } 
/*     */       } 
/* 389 */       if (allBuffersUpdated && this.sectionMesh.get() != compiledSectionMesh) {
/* 390 */         SectionMesh oldMesh = setSectionMesh(compiledSectionMesh);
/* 393 */         releaseSectionMesh(oldMesh);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void vertexBufferUploadCallback(CompiledSectionMesh sectionMesh, ChunkSectionLayer layer) {
/* 398 */       sectionMesh.setVertexBufferUploaded(layer);
/* 399 */       checkSectionMesh(sectionMesh);
/*     */     }
/*     */     
/*     */     private void indexBufferUploadCallback(CompiledSectionMesh sectionMesh, ChunkSectionLayer layer, boolean sortedIndexBuffer) {
/* 403 */       sectionMesh.setIndexBufferUploaded(layer);
/* 404 */       if (!sortedIndexBuffer)
/* 405 */         checkSectionMesh(sectionMesh); 
/*     */     }
/*     */     
/*     */     private boolean addSectionBuffersToUberBuffer(ChunkSectionLayer layer, CompiledSectionMesh key, ByteBuffer vertexBuffer, ByteBuffer indexBuffer) {
/* 410 */       boolean success = true;
/* 411 */       SectionRenderDispatcher.this.copyLock.lock();
/*     */       try {
/* 413 */         SectionMesh.SectionDraw draw = key.getSectionDraw(layer);
/* 414 */         if (draw != null) {
/* 415 */           SectionRenderDispatcher.SectionUberBuffers sectionBuffers = SectionRenderDispatcher.this.chunkUberBuffers.get(layer);
/* 416 */           assert sectionBuffers != null;
/* 418 */           if (vertexBuffer != null) {
/* 419 */             UberGpuBuffer.UploadCallback<CompiledSectionMesh> callback = mesh -> vertexBufferUploadCallback(mesh, layer);
/* 420 */             success &= sectionBuffers.vertexBuffer.addAllocation(key, callback, vertexBuffer);
/*     */           } 
/* 423 */           if (indexBuffer != null) {
/* 424 */             boolean sortedIndexBuffer = (vertexBuffer == null);
/* 425 */             UberGpuBuffer.UploadCallback<CompiledSectionMesh> callback = mesh -> indexBufferUploadCallback(mesh, layer, sortedIndexBuffer);
/* 426 */             success &= sectionBuffers.indexBuffer.addAllocation(key, callback, indexBuffer);
/*     */           } else {
/* 429 */             key.setIndexBufferUploaded(layer);
/*     */           } 
/*     */         } 
/* 433 */         if (!success && RenderSystem.isOnRenderThread())
/* 437 */           SectionRenderDispatcher.this.uploadTerrainBuffersToGpu(); 
/*     */       } finally {
/* 440 */         SectionRenderDispatcher.this.copyLock.unlock();
/*     */       } 
/* 443 */       return success;
/*     */     }
/*     */     
/*     */     private class CompileTask extends SectionTask {
/*     */       private final RenderSectionRegion region;
/*     */       
/*     */       public CompileTask(RenderSectionRegion region, boolean isRecompile) {
/* 450 */         super(isRecompile);
/* 451 */         this.region = region;
/*     */       }
/*     */       
/*     */       public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/*     */         SectionCompiler.Results results;
/* 456 */         if (this.isCancelled.get())
/* 457 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 460 */         long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 461 */         SectionPos sectionPos = SectionPos.of(sectionNode);
/* 465 */         if (this.isCancelled.get())
/* 466 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 469 */         Vec3 cameraPos = SectionRenderDispatcher.this.cameraPosition.get();
/* 471 */         Zone ignored = Profiler.get().zone("Compile Section");
/*     */         try {
/* 472 */           results = SectionRenderDispatcher.this.sectionCompiler.compile(sectionPos, this.region, SectionRenderDispatcher.RenderSection.this.createVertexSorting(sectionPos, cameraPos), buffers);
/* 473 */           if (ignored != null)
/* 473 */             ignored.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (ignored != null)
/*     */             try {
/*     */               ignored.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 475 */         TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 476 */         CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results);
/* 479 */         if (results.renderedLayers.isEmpty()) {
/* 480 */           SectionMesh oldMesh = SectionRenderDispatcher.RenderSection.this.setSectionMesh(compiledSectionMesh);
/* 481 */           SectionRenderDispatcher.this.copyLock.lock();
/*     */           try {
/* 483 */             SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(oldMesh);
/*     */           } finally {
/* 485 */             SectionRenderDispatcher.this.copyLock.unlock();
/*     */           } 
/* 487 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */         } 
/* 490 */         for (Map.Entry<ChunkSectionLayer, MeshData> entry : results.renderedLayers.entrySet()) {
/* 491 */           MeshData meshData = entry.getValue();
/* 492 */           boolean success = false;
/* 493 */           while (!success) {
/* 494 */             if (this.isCancelled.get()) {
/* 495 */               results.release();
/* 496 */               SectionRenderDispatcher.this.copyLock.lock();
/*     */               try {
/* 498 */                 SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(compiledSectionMesh);
/*     */               } finally {
/* 500 */                 SectionRenderDispatcher.this.copyLock.unlock();
/*     */               } 
/* 502 */               return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */             } 
/* 504 */             success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(entry.getKey(), compiledSectionMesh, meshData.vertexBuffer(), meshData.indexBuffer());
/* 508 */             if (!success && !RenderSystem.isOnRenderThread())
/* 509 */               Thread.onSpinWait(); 
/*     */           } 
/* 512 */           meshData.close();
/*     */         } 
/* 515 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */       }
/*     */       
/*     */       public void cancel() {
/* 520 */         this.isCancelled.compareAndSet(false, true);
/*     */       }
/*     */     }
/*     */     
/*     */     private class ResortTransparencyTask extends SectionTask {
/*     */       private final CompiledSectionMesh compiledSectionMesh;
/*     */       
/*     */       public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) {
/* 528 */         super(true);
/* 529 */         this.compiledSectionMesh = compiledSectionMesh;
/*     */       }
/*     */       
/*     */       public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/* 534 */         if (this.isCancelled.get())
/* 535 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 538 */         MeshData.SortState state = this.compiledSectionMesh.getTransparencyState();
/* 539 */         if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT))
/* 540 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 543 */         Vec3 cameraPos = SectionRenderDispatcher.this.cameraPosition.get();
/* 544 */         long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 545 */         VertexSorting vertexSorting = SectionRenderDispatcher.RenderSection.this.createVertexSorting(SectionPos.of(sectionNode), cameraPos);
/* 546 */         TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 548 */         if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned())
/* 549 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 552 */         ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
/* 553 */         if (indexBuffer == null)
/* 554 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 557 */         boolean success = false;
/* 558 */         while (!success) {
/* 559 */           if (this.isCancelled.get()) {
/* 560 */             indexBuffer.close();
/* 561 */             return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */           } 
/* 563 */           success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(ChunkSectionLayer.TRANSLUCENT, this.compiledSectionMesh, null, indexBuffer.byteBuffer());
/* 567 */           if (!success && !RenderSystem.isOnRenderThread())
/* 568 */             Thread.onSpinWait(); 
/*     */         } 
/* 572 */         indexBuffer.close();
/* 574 */         this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
/* 575 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */       }
/*     */       
/*     */       public void cancel() {
/* 580 */         this.isCancelled.set(true);
/*     */       }
/*     */     }
/*     */     
/*     */     public abstract class SectionTask {
/*     */       public enum SectionTaskResult {
/* 586 */         SUCCESSFUL, CANCELLED;
/*     */       }
/*     */       
/* 590 */       protected final AtomicBoolean isCancelled = new AtomicBoolean(false);
/*     */       
/* 591 */       protected final AtomicBoolean isCompleted = new AtomicBoolean(false);
/*     */       
/*     */       private final boolean isRecompile;
/*     */       
/*     */       public SectionTask(boolean isRecompile) {
/* 595 */         this.isRecompile = isRecompile;
/*     */       }
/*     */       
/*     */       public abstract SectionTaskResult doTask(SectionBufferBuilderPack param2SectionBufferBuilderPack);
/*     */       
/*     */       public abstract void cancel();
/*     */       
/*     */       public boolean isRecompile() {
/* 603 */         return this.isRecompile;
/*     */       }
/*     */       
/*     */       public BlockPos getRenderOrigin() {
/* 608 */         return (BlockPos)SectionRenderDispatcher.RenderSection.this.renderOrigin;
/*     */       }
/*     */     }
/*     */     
/*     */     public enum SectionTaskResult {
/*     */       SUCCESSFUL, CANCELLED;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CompileTask extends RenderSection.SectionTask {
/*     */     private final RenderSectionRegion region;
/*     */     
/*     */     public CompileTask(RenderSectionRegion region, boolean isRecompile) {
/*     */       super((SectionRenderDispatcher.RenderSection)SectionRenderDispatcher.this, isRecompile);
/*     */       this.region = region;
/*     */     }
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/*     */       SectionCompiler.Results results;
/*     */       if (this.isCancelled.get())
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       long sectionNode = this.this$1.sectionNode;
/*     */       SectionPos sectionPos = SectionPos.of(sectionNode);
/*     */       if (this.isCancelled.get())
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       Vec3 cameraPos = SectionRenderDispatcher.this.cameraPosition.get();
/*     */       Zone ignored = Profiler.get().zone("Compile Section");
/*     */       try {
/*     */         results = SectionRenderDispatcher.this.sectionCompiler.compile(sectionPos, this.region, this.this$1.createVertexSorting(sectionPos, cameraPos), buffers);
/*     */         if (ignored != null)
/*     */           ignored.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */       TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/*     */       CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results);
/*     */       if (results.renderedLayers.isEmpty()) {
/*     */         SectionMesh oldMesh = this.this$1.setSectionMesh(compiledSectionMesh);
/*     */         SectionRenderDispatcher.this.copyLock.lock();
/*     */         try {
/*     */           this.this$1.releaseSectionMesh(oldMesh);
/*     */         } finally {
/*     */           SectionRenderDispatcher.this.copyLock.unlock();
/*     */         } 
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */       } 
/*     */       for (Map.Entry<ChunkSectionLayer, MeshData> entry : results.renderedLayers.entrySet()) {
/*     */         MeshData meshData = entry.getValue();
/*     */         boolean success = false;
/*     */         while (!success) {
/*     */           if (this.isCancelled.get()) {
/*     */             results.release();
/*     */             SectionRenderDispatcher.this.copyLock.lock();
/*     */             try {
/*     */               this.this$1.releaseSectionMesh(compiledSectionMesh);
/*     */             } finally {
/*     */               SectionRenderDispatcher.this.copyLock.unlock();
/*     */             } 
/*     */             return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */           } 
/*     */           success = this.this$1.addSectionBuffersToUberBuffer(entry.getKey(), compiledSectionMesh, meshData.vertexBuffer(), meshData.indexBuffer());
/*     */           if (!success && !RenderSystem.isOnRenderThread())
/*     */             Thread.onSpinWait(); 
/*     */         } 
/*     */         meshData.close();
/*     */       } 
/*     */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */     }
/*     */     
/*     */     public void cancel() {
/*     */       this.isCancelled.compareAndSet(false, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResortTransparencyTask extends RenderSection.SectionTask {
/*     */     private final CompiledSectionMesh compiledSectionMesh;
/*     */     
/*     */     public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) {
/*     */       super((SectionRenderDispatcher.RenderSection)SectionRenderDispatcher.this, true);
/*     */       this.compiledSectionMesh = compiledSectionMesh;
/*     */     }
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/*     */       if (this.isCancelled.get())
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       MeshData.SortState state = this.compiledSectionMesh.getTransparencyState();
/*     */       if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT))
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       Vec3 cameraPos = SectionRenderDispatcher.this.cameraPosition.get();
/*     */       long sectionNode = this.this$1.sectionNode;
/*     */       VertexSorting vertexSorting = this.this$1.createVertexSorting(SectionPos.of(sectionNode), cameraPos);
/*     */       TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/*     */       if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned())
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
/*     */       if (indexBuffer == null)
/*     */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/*     */       boolean success = false;
/*     */       while (!success) {
/*     */         if (this.isCancelled.get()) {
/*     */           indexBuffer.close();
/*     */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */         } 
/*     */         success = this.this$1.addSectionBuffersToUberBuffer(ChunkSectionLayer.TRANSLUCENT, this.compiledSectionMesh, null, indexBuffer.byteBuffer());
/*     */         if (!success && !RenderSystem.isOnRenderThread())
/*     */           Thread.onSpinWait(); 
/*     */       } 
/*     */       indexBuffer.close();
/*     */       this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
/*     */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */     }
/*     */     
/*     */     public void cancel() {
/*     */       this.isCancelled.set(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class SectionTask {
/*     */     public enum SectionTaskResult {
/*     */       SUCCESSFUL, CANCELLED;
/*     */     }
/*     */     
/*     */     protected final AtomicBoolean isCancelled = new AtomicBoolean(false);
/*     */     
/*     */     protected final AtomicBoolean isCompleted = new AtomicBoolean(false);
/*     */     
/*     */     private final boolean isRecompile;
/*     */     
/*     */     public SectionTask(boolean isRecompile) {
/*     */       this.isRecompile = isRecompile;
/*     */     }
/*     */     
/*     */     public abstract SectionTaskResult doTask(SectionBufferBuilderPack param1SectionBufferBuilderPack);
/*     */     
/*     */     public abstract void cancel();
/*     */     
/*     */     public boolean isRecompile() {
/*     */       return this.isRecompile;
/*     */     }
/*     */     
/*     */     public BlockPos getRenderOrigin() {
/* 608 */       return (BlockPos)SectionRenderDispatcher.RenderSection.this.renderOrigin;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SectionTaskResult {
/*     */     SUCCESSFUL, CANCELLED;
/*     */   }
/*     */   
/*     */   private static final class SectionUberBuffers extends Record {
/*     */     private final UberGpuBuffer<SectionMesh> vertexBuffer;
/*     */     
/*     */     private final UberGpuBuffer<SectionMesh> indexBuffer;
/*     */     
/*     */     private SectionUberBuffers(UberGpuBuffer<SectionMesh> vertexBuffer, UberGpuBuffer<SectionMesh> indexBuffer) {
/* 613 */       this.vertexBuffer = vertexBuffer;
/* 613 */       this.indexBuffer = indexBuffer;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #613	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #613	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #613	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$SectionUberBuffers;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UberGpuBuffer<SectionMesh> vertexBuffer() {
/* 613 */       return this.vertexBuffer;
/*     */     }
/*     */     
/*     */     public UberGpuBuffer<SectionMesh> indexBuffer() {
/* 613 */       return this.indexBuffer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */