/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ 
/*     */ class SectionToNodeMap {
/*     */   private final SectionOcclusionGraph.Node[] nodes;
/*     */   
/*     */   private SectionToNodeMap(int sectionCount) {
/* 434 */     this.nodes = new SectionOcclusionGraph.Node[sectionCount];
/*     */   }
/*     */   
/*     */   public void put(SectionRenderDispatcher.RenderSection renderSection, SectionOcclusionGraph.Node node) {
/* 438 */     this.nodes[renderSection.index] = node;
/*     */   }
/*     */   
/*     */   public SectionOcclusionGraph.Node get(SectionRenderDispatcher.RenderSection renderSection) {
/* 442 */     int index = renderSection.index;
/* 443 */     if (index < 0 || index >= this.nodes.length)
/* 444 */       return null; 
/* 446 */     return this.nodes[index];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionOcclusionGraph$SectionToNodeMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */