/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ public class RenderBuffers implements AutoCloseable {
/*    */   private final SectionBufferBuilderPack fixedBufferPack;
/*    */   
/*    */   private final SectionBufferBuilderPool sectionBufferPool;
/*    */   
/*    */   private final StagedVertexBuffer stagedVertexBuffer;
/*    */   
/*    */   public RenderBuffers(int maxSectionBuilders) {
/* 12 */     this.fixedBufferPack = new SectionBufferBuilderPack();
/* 13 */     this.sectionBufferPool = SectionBufferBuilderPool.allocate(maxSectionBuilders);
/* 15 */     this.stagedVertexBuffer = new StagedVertexBuffer(() -> "Shared Buffer", 4194304);
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPack fixedBufferPack() {
/* 19 */     return this.fixedBufferPack;
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPool sectionBufferPool() {
/* 23 */     return this.sectionBufferPool;
/*    */   }
/*    */   
/*    */   public StagedVertexBuffer stagedVertexBuffer() {
/* 27 */     return this.stagedVertexBuffer;
/*    */   }
/*    */   
/*    */   public void endFrame() {
/* 31 */     this.stagedVertexBuffer.endFrame();
/*    */   }
/*    */   
/*    */   public void close() {
/* 36 */     this.sectionBufferPool.close();
/* 37 */     this.stagedVertexBuffer.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\RenderBuffers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */