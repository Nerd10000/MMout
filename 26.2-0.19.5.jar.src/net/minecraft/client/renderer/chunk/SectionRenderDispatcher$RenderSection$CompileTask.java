/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class CompileTask extends SectionRenderDispatcher.RenderSection.SectionTask {
/*     */   private final RenderSectionRegion region;
/*     */   
/*     */   public CompileTask(RenderSectionRegion region, boolean isRecompile) {
/* 450 */     super(isRecompile);
/* 451 */     this.region = region;
/*     */   }
/*     */   
/*     */   public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/*     */     SectionCompiler.Results results;
/* 456 */     if (this.isCancelled.get())
/* 457 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 460 */     long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 461 */     SectionPos sectionPos = SectionPos.of(sectionNode);
/* 465 */     if (this.isCancelled.get())
/* 466 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 469 */     Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();
/* 471 */     Zone ignored = Profiler.get().zone("Compile Section");
/*     */     try {
/* 472 */       results = this.this$1.this$0.sectionCompiler.compile(sectionPos, this.region, SectionRenderDispatcher.RenderSection.this.createVertexSorting(sectionPos, cameraPos), buffers);
/* 473 */       if (ignored != null)
/* 473 */         ignored.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 475 */     TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 476 */     CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results);
/* 479 */     if (results.renderedLayers.isEmpty()) {
/* 480 */       SectionMesh oldMesh = SectionRenderDispatcher.RenderSection.this.setSectionMesh(compiledSectionMesh);
/* 481 */       this.this$1.this$0.copyLock.lock();
/*     */       try {
/* 483 */         SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(oldMesh);
/*     */       } finally {
/* 485 */         this.this$1.this$0.copyLock.unlock();
/*     */       } 
/* 487 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */     } 
/* 490 */     for (Map.Entry<ChunkSectionLayer, MeshData> entry : results.renderedLayers.entrySet()) {
/* 491 */       MeshData meshData = entry.getValue();
/* 492 */       boolean success = false;
/* 493 */       while (!success) {
/* 494 */         if (this.isCancelled.get()) {
/* 495 */           results.release();
/* 496 */           this.this$1.this$0.copyLock.lock();
/*     */           try {
/* 498 */             SectionRenderDispatcher.RenderSection.this.releaseSectionMesh(compiledSectionMesh);
/*     */           } finally {
/* 500 */             this.this$1.this$0.copyLock.unlock();
/*     */           } 
/* 502 */           return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */         } 
/* 504 */         success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(entry.getKey(), compiledSectionMesh, meshData.vertexBuffer(), meshData.indexBuffer());
/* 508 */         if (!success && !RenderSystem.isOnRenderThread())
/* 509 */           Thread.onSpinWait(); 
/*     */       } 
/* 512 */       meshData.close();
/*     */     } 
/* 515 */     return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 520 */     this.isCancelled.compareAndSet(false, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$RenderSection$CompileTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */