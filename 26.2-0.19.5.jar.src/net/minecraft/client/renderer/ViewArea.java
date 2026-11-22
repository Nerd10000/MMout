/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.RotatingSectionStorage;
/*    */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ public class ViewArea {
/*    */   private final SectionOcclusionGraph sectionOcclusionGraph;
/*    */   
/*    */   private final RotatingSectionStorage<SectionRenderDispatcher.RenderSection> sections;
/*    */   
/*    */   private final int minY;
/*    */   
/*    */   private final int maxY;
/*    */   
/*    */   public ViewArea(SectionRenderDispatcher sectionRenderDispatcher, int minY, int maxY, int minSectionY, int maxSectionY, int renderDistance, SectionOcclusionGraph sectionOcclusionGraph) {
/* 18 */     this.sectionOcclusionGraph = sectionOcclusionGraph;
/* 19 */     this.minY = minY;
/* 20 */     this.maxY = maxY;
/* 22 */     if (!Minecraft.getInstance().isSameThread())
/* 23 */       throw new IllegalStateException("createSections called from wrong thread: " + Thread.currentThread().getName()); 
/* 25 */     this.sections = new RotatingSectionStorage(renderDistance, minSectionY, maxSectionY, (index, sectionNode) -> {
/*    */           Objects.requireNonNull(sectionRenderDispatcher);
/*    */           return new SectionRenderDispatcher.RenderSection(sectionRenderDispatcher, index, sectionNode);
/*    */         });
/*    */   }
/*    */   
/*    */   public void releaseAllBuffers() {
/* 29 */     for (SectionRenderDispatcher.RenderSection section : this.sections)
/* 30 */       section.reset(); 
/*    */   }
/*    */   
/*    */   public int size() {
/* 35 */     return this.sections.size();
/*    */   }
/*    */   
/*    */   public int minY() {
/* 39 */     return this.minY;
/*    */   }
/*    */   
/*    */   public int maxY() {
/* 43 */     return this.maxY;
/*    */   }
/*    */   
/*    */   public int minSectionY() {
/* 47 */     return this.sections.minY();
/*    */   }
/*    */   
/*    */   public int maxSectionY() {
/* 51 */     return this.sections.maxY();
/*    */   }
/*    */   
/*    */   public int sectionCount() {
/* 55 */     return this.sections.height();
/*    */   }
/*    */   
/*    */   public int getViewDistance() {
/* 59 */     return this.sections.radius();
/*    */   }
/*    */   
/*    */   public boolean repositionCamera(SectionPos cameraSectionPos) {
/* 63 */     boolean result = this.sections.repositionCenter(cameraSectionPos);
/* 64 */     if (result)
/* 65 */       this.sectionOcclusionGraph.invalidate(); 
/* 67 */     return result;
/*    */   }
/*    */   
/*    */   public SectionPos getCameraSectionPos() {
/* 71 */     return this.sections.centerSectionPos();
/*    */   }
/*    */   
/*    */   public SectionRenderDispatcher.RenderSection getRenderSectionAt(BlockPos pos) {
/* 75 */     return (SectionRenderDispatcher.RenderSection)this.sections.getValueAt(pos);
/*    */   }
/*    */   
/*    */   protected SectionRenderDispatcher.RenderSection getRenderSection(long sectionNode) {
/* 79 */     return (SectionRenderDispatcher.RenderSection)this.sections.getValue(sectionNode);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ViewArea.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */