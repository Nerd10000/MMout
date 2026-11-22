/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.UberGpuBuffer;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.client.RotatingSectionStorage;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RenderSection implements RotatingSectionStorage.Value {
/*     */   public final int index;
/*     */   
/* 224 */   public final AtomicReference<SectionMesh> sectionMesh = new AtomicReference<>(CompiledSectionMesh.UNCOMPILED);
/*     */   
/*     */   private CompileTask lastCompileTask;
/*     */   
/*     */   private ResortTransparencyTask lastResortTransparencyTask;
/*     */   
/*     */   private AABB bb;
/*     */   
/* 230 */   private volatile long sectionNode = SectionPos.asLong(-1, -1, -1);
/*     */   
/* 231 */   private final BlockPos.MutableBlockPos renderOrigin = new BlockPos.MutableBlockPos(-1, -1, -1);
/*     */   
/*     */   private long uploadedTime;
/*     */   
/*     */   private long fadeDuration;
/*     */   
/*     */   private boolean wasPreviouslyEmpty;
/*     */   
/*     */   public RenderSection(int index, long sectionNode) {
/* 238 */     this.index = index;
/* 239 */     setSectionNode(sectionNode);
/*     */   }
/*     */   
/*     */   public float getVisibility(long now) {
/* 243 */     long elapsed = now - this.uploadedTime;
/* 244 */     if (elapsed >= this.fadeDuration)
/* 245 */       return 1.0F; 
/* 247 */     return (float)elapsed / (float)this.fadeDuration;
/*     */   }
/*     */   
/*     */   public void setFadeDuration(long fadeDuration) {
/* 251 */     this.fadeDuration = fadeDuration;
/*     */   }
/*     */   
/*     */   public void setWasPreviouslyEmpty(boolean wasPreviouslyEmpty) {
/* 255 */     this.wasPreviouslyEmpty = wasPreviouslyEmpty;
/*     */   }
/*     */   
/*     */   public boolean wasPreviouslyEmpty() {
/* 259 */     return this.wasPreviouslyEmpty;
/*     */   }
/*     */   
/*     */   public AABB getBoundingBox() {
/* 263 */     return this.bb;
/*     */   }
/*     */   
/*     */   public void setSectionNode(long sectionNode) {
/* 268 */     reset();
/* 269 */     this.sectionNode = sectionNode;
/* 270 */     int x = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
/* 271 */     int y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 272 */     int z = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
/* 273 */     this.renderOrigin.set(x, y, z);
/* 274 */     this.bb = new AABB(x, y, z, (x + 16), (y + 16), (z + 16));
/*     */   }
/*     */   
/*     */   public SectionMesh getSectionMesh() {
/* 278 */     return this.sectionMesh.get();
/*     */   }
/*     */   
/*     */   public void reset() {
/* 282 */     cancelTasks();
/* 283 */     SectionMesh mesh = this.sectionMesh.getAndSet(CompiledSectionMesh.UNCOMPILED);
/* 284 */     SectionRenderDispatcher.this.copyLock.lock();
/*     */     try {
/* 286 */       releaseSectionMesh(mesh);
/*     */     } finally {
/* 288 */       SectionRenderDispatcher.this.copyLock.unlock();
/*     */     } 
/* 290 */     this.uploadedTime = 0L;
/* 291 */     this.wasPreviouslyEmpty = false;
/*     */   }
/*     */   
/*     */   public BlockPos getRenderOrigin() {
/* 296 */     return (BlockPos)this.renderOrigin;
/*     */   }
/*     */   
/*     */   public long getSectionNode() {
/* 301 */     return this.sectionNode;
/*     */   }
/*     */   
/*     */   public long getNeighborSectionNode(Direction direction) {
/* 305 */     return SectionPos.offset(this.sectionNode, direction);
/*     */   }
/*     */   
/*     */   public void resortTransparency() {
/* 309 */     SectionMesh sectionMesh = getSectionMesh();
/* 309 */     if (sectionMesh instanceof CompiledSectionMesh) {
/* 309 */       CompiledSectionMesh mesh = (CompiledSectionMesh)sectionMesh;
/* 310 */       this.lastResortTransparencyTask = new ResortTransparencyTask(mesh);
/* 311 */       SectionRenderDispatcher.this.schedule(this.lastResortTransparencyTask);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasTranslucentGeometry() {
/* 316 */     return getSectionMesh().hasTranslucentGeometry();
/*     */   }
/*     */   
/*     */   public boolean transparencyResortingScheduled() {
/* 320 */     return (this.lastResortTransparencyTask != null && !this.lastResortTransparencyTask.isCompleted.get());
/*     */   }
/*     */   
/*     */   private void cancelTasks() {
/* 324 */     if (this.lastCompileTask != null) {
/* 325 */       this.lastCompileTask.cancel();
/* 326 */       this.lastCompileTask = null;
/*     */     } 
/* 328 */     if (this.lastResortTransparencyTask != null) {
/* 329 */       this.lastResortTransparencyTask.cancel();
/* 330 */       this.lastResortTransparencyTask = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private SectionTask createCompileTask(RenderSectionRegion region) {
/* 335 */     cancelTasks();
/* 336 */     boolean isRecompile = (this.sectionMesh.get() != CompiledSectionMesh.UNCOMPILED);
/* 337 */     this.lastCompileTask = new CompileTask(region, isRecompile);
/* 338 */     return this.lastCompileTask;
/*     */   }
/*     */   
/*     */   public void compileAsync(RenderSectionRegion region) {
/* 342 */     SectionTask task = createCompileTask(region);
/* 343 */     SectionRenderDispatcher.this.schedule(task);
/*     */   }
/*     */   
/*     */   public void compileSync(RenderSectionRegion region) {
/* 347 */     SectionTask task = createCompileTask(region);
/* 348 */     task.doTask(SectionRenderDispatcher.this.fixedBuffers);
/*     */   }
/*     */   
/*     */   private SectionMesh setSectionMesh(SectionMesh sectionMesh) {
/* 352 */     SectionMesh oldMesh = this.sectionMesh.getAndSet(sectionMesh);
/* 354 */     SectionRenderDispatcher.this.onSectionMeshUpdate.accept(this);
/* 355 */     if (this.uploadedTime == 0L)
/* 356 */       this.uploadedTime = Util.getMillis(); 
/* 359 */     return oldMesh;
/*     */   }
/*     */   
/*     */   private void releaseSectionMesh(SectionMesh oldMesh) {
/* 363 */     oldMesh.close();
/* 364 */     for (SectionRenderDispatcher.SectionUberBuffers buffers : SectionRenderDispatcher.this.chunkUberBuffers.values()) {
/* 365 */       buffers.vertexBuffer.removeAllocation(oldMesh);
/* 366 */       buffers.indexBuffer.removeAllocation(oldMesh);
/*     */     } 
/*     */   }
/*     */   
/*     */   private VertexSorting createVertexSorting(SectionPos sectionPos, Vec3 cameraPos) {
/* 371 */     return VertexSorting.byDistance(
/* 372 */         (float)(cameraPos.x - sectionPos.minBlockX()), 
/* 373 */         (float)(cameraPos.y - sectionPos.minBlockY()), 
/* 374 */         (float)(cameraPos.z - sectionPos.minBlockZ()));
/*     */   }
/*     */   
/*     */   private void checkSectionMesh(CompiledSectionMesh compiledSectionMesh) {
/* 380 */     boolean allBuffersUpdated = true;
/* 381 */     for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
/* 382 */       SectionMesh.SectionDraw draw = compiledSectionMesh.getSectionDraw(layer);
/* 383 */       if (draw != null) {
/* 384 */         allBuffersUpdated &= compiledSectionMesh.isIndexBufferUploaded(layer);
/* 385 */         allBuffersUpdated &= compiledSectionMesh.isVertexBufferUploaded(layer);
/*     */       } 
/*     */     } 
/* 389 */     if (allBuffersUpdated && this.sectionMesh.get() != compiledSectionMesh) {
/* 390 */       SectionMesh oldMesh = setSectionMesh(compiledSectionMesh);
/* 393 */       releaseSectionMesh(oldMesh);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void vertexBufferUploadCallback(CompiledSectionMesh sectionMesh, ChunkSectionLayer layer) {
/* 398 */     sectionMesh.setVertexBufferUploaded(layer);
/* 399 */     checkSectionMesh(sectionMesh);
/*     */   }
/*     */   
/*     */   private void indexBufferUploadCallback(CompiledSectionMesh sectionMesh, ChunkSectionLayer layer, boolean sortedIndexBuffer) {
/* 403 */     sectionMesh.setIndexBufferUploaded(layer);
/* 404 */     if (!sortedIndexBuffer)
/* 405 */       checkSectionMesh(sectionMesh); 
/*     */   }
/*     */   
/*     */   private boolean addSectionBuffersToUberBuffer(ChunkSectionLayer layer, CompiledSectionMesh key, ByteBuffer vertexBuffer, ByteBuffer indexBuffer) {
/* 410 */     boolean success = true;
/* 411 */     SectionRenderDispatcher.this.copyLock.lock();
/*     */     try {
/* 413 */       SectionMesh.SectionDraw draw = key.getSectionDraw(layer);
/* 414 */       if (draw != null) {
/* 415 */         SectionRenderDispatcher.SectionUberBuffers sectionBuffers = SectionRenderDispatcher.this.chunkUberBuffers.get(layer);
/* 416 */         assert sectionBuffers != null;
/* 418 */         if (vertexBuffer != null) {
/* 419 */           UberGpuBuffer.UploadCallback<CompiledSectionMesh> callback = mesh -> vertexBufferUploadCallback(mesh, layer);
/* 420 */           success &= sectionBuffers.vertexBuffer.addAllocation(key, callback, vertexBuffer);
/*     */         } 
/* 423 */         if (indexBuffer != null) {
/* 424 */           boolean sortedIndexBuffer = (vertexBuffer == null);
/* 425 */           UberGpuBuffer.UploadCallback<CompiledSectionMesh> callback = mesh -> indexBufferUploadCallback(mesh, layer, sortedIndexBuffer);
/* 426 */           success &= sectionBuffers.indexBuffer.addAllocation(key, callback, indexBuffer);
/*     */         } else {
/* 429 */           key.setIndexBufferUploaded(layer);
/*     */         } 
/*     */       } 
/* 433 */       if (!success && RenderSystem.isOnRenderThread())
/* 437 */         SectionRenderDispatcher.this.uploadTerrainBuffersToGpu(); 
/*     */     } finally {
/* 440 */       SectionRenderDispatcher.this.copyLock.unlock();
/*     */     } 
/* 443 */     return success;
/*     */   }
/*     */   
/*     */   private class CompileTask extends SectionTask {
/*     */     private final RenderSectionRegion region;
/*     */     
/*     */     public CompileTask(RenderSectionRegion region, boolean isRecompile) {
/* 450 */       super(isRecompile);
/* 451 */       this.region = region;
/*     */     }
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/*     */       SectionCompiler.Results results;
/* 456 */       if (this.isCancelled.get())
/* 457 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 460 */       long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 461 */       SectionPos sectionPos = SectionPos.of(sectionNode);
/* 465 */       if (this.isCancelled.get())
/* 466 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 469 */       Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();
/* 471 */       Zone ignored = Profiler.get().zone("Compile Section");
/*     */       try {
/* 472 */         results = this.this$1.this$0.sectionCompiler.compile(sectionPos, this.region, SectionRenderDispatcher.RenderSection.this.createVertexSorting(sectionPos, cameraPos), buffers);
/* 473 */         if (ignored != null)
/* 473 */           ignored.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 475 */       TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 476 */       CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results);
/* 479 */       if (results.renderedLayers.isEmpty()) {
/* 480 */         SectionMesh oldMesh = SectionRenderDispatcher.RenderSection.this.setSectionMesh(compiledSectionMesh);
/* 481 */         this.this$1.this$0.copyLock.lock();
/*     */         try {
/* 483 */           SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(oldMesh);
/*     */         } finally {
/* 485 */           this.this$1.this$0.copyLock.unlock();
/*     */         } 
/* 487 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */       } 
/* 490 */       for (Map.Entry<ChunkSectionLayer, MeshData> entry : results.renderedLayers.entrySet()) {
/* 491 */         MeshData meshData = entry.getValue();
/* 492 */         boolean success = false;
/* 493 */         while (!success) {
/* 494 */           if (this.isCancelled.get()) {
/* 495 */             results.release();
/* 496 */             this.this$1.this$0.copyLock.lock();
/*     */             try {
/* 498 */               SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(compiledSectionMesh);
/*     */             } finally {
/* 500 */               this.this$1.this$0.copyLock.unlock();
/*     */             } 
/* 502 */             return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */           } 
/* 504 */           success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(entry.getKey(), compiledSectionMesh, meshData.vertexBuffer(), meshData.indexBuffer());
/* 508 */           if (!success && !RenderSystem.isOnRenderThread())
/* 509 */             Thread.onSpinWait(); 
/*     */         } 
/* 512 */         meshData.close();
/*     */       } 
/* 515 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 520 */       this.isCancelled.compareAndSet(false, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResortTransparencyTask extends SectionTask {
/*     */     private final CompiledSectionMesh compiledSectionMesh;
/*     */     
/*     */     public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) {
/* 528 */       super(true);
/* 529 */       this.compiledSectionMesh = compiledSectionMesh;
/*     */     }
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/* 534 */       if (this.isCancelled.get())
/* 535 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 538 */       MeshData.SortState state = this.compiledSectionMesh.getTransparencyState();
/* 539 */       if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT))
/* 540 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 543 */       Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();
/* 544 */       long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 545 */       VertexSorting vertexSorting = SectionRenderDispatcher.RenderSection.this.createVertexSorting(SectionPos.of(sectionNode), cameraPos);
/* 546 */       TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 548 */       if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned())
/* 549 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 552 */       ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
/* 553 */       if (indexBuffer == null)
/* 554 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 557 */       boolean success = false;
/* 558 */       while (!success) {
/* 559 */         if (this.isCancelled.get()) {
/* 560 */           indexBuffer.close();
/* 561 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */         } 
/* 563 */         success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(ChunkSectionLayer.TRANSLUCENT, this.compiledSectionMesh, null, indexBuffer.byteBuffer());
/* 567 */         if (!success && !RenderSystem.isOnRenderThread())
/* 568 */           Thread.onSpinWait(); 
/*     */       } 
/* 572 */       indexBuffer.close();
/* 574 */       this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
/* 575 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */     }
/*     */     
/*     */     public void cancel() {
/* 580 */       this.isCancelled.set(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class SectionTask {
/*     */     public enum SectionTaskResult {
/* 586 */       SUCCESSFUL, CANCELLED;
/*     */     }
/*     */     
/* 590 */     protected final AtomicBoolean isCancelled = new AtomicBoolean(false);
/*     */     
/* 591 */     protected final AtomicBoolean isCompleted = new AtomicBoolean(false);
/*     */     
/*     */     private final boolean isRecompile;
/*     */     
/*     */     public SectionTask(boolean isRecompile) {
/* 595 */       this.isRecompile = isRecompile;
/*     */     }
/*     */     
/*     */     public abstract SectionTaskResult doTask(SectionBufferBuilderPack param2SectionBufferBuilderPack);
/*     */     
/*     */     public abstract void cancel();
/*     */     
/*     */     public boolean isRecompile() {
/* 603 */       return this.isRecompile;
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
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$RenderSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */