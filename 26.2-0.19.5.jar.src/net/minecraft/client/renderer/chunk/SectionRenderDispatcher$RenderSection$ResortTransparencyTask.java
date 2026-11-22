/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class ResortTransparencyTask extends SectionRenderDispatcher.RenderSection.SectionTask {
/*     */   private final CompiledSectionMesh compiledSectionMesh;
/*     */   
/*     */   public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) {
/* 528 */     super(true);
/* 529 */     this.compiledSectionMesh = compiledSectionMesh;
/*     */   }
/*     */   
/*     */   public SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult doTask(SectionBufferBuilderPack buffers) {
/* 534 */     if (this.isCancelled.get())
/* 535 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 538 */     MeshData.SortState state = this.compiledSectionMesh.getTransparencyState();
/* 539 */     if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT))
/* 540 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 543 */     Vec3 cameraPos = this.this$1.this$0.cameraPosition.get();
/* 544 */     long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 545 */     VertexSorting vertexSorting = SectionRenderDispatcher.RenderSection.this.createVertexSorting(SectionPos.of(sectionNode), cameraPos);
/* 546 */     TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(cameraPos, sectionNode);
/* 548 */     if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned())
/* 549 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 552 */     ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
/* 553 */     if (indexBuffer == null)
/* 554 */       return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED; 
/* 557 */     boolean success = false;
/* 558 */     while (!success) {
/* 559 */       if (this.isCancelled.get()) {
/* 560 */         indexBuffer.close();
/* 561 */         return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.CANCELLED;
/*     */       } 
/* 563 */       success = SectionRenderDispatcher.RenderSection.this.addSectionBuffersToUberBuffer(ChunkSectionLayer.TRANSLUCENT, this.compiledSectionMesh, null, indexBuffer.byteBuffer());
/* 567 */       if (!success && !RenderSystem.isOnRenderThread())
/* 568 */         Thread.onSpinWait(); 
/*     */     } 
/* 572 */     indexBuffer.close();
/* 574 */     this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
/* 575 */     return SectionRenderDispatcher.RenderSection.SectionTask.SectionTaskResult.SUCCESSFUL;
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 580 */     this.isCancelled.set(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$RenderSection$ResortTransparencyTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */