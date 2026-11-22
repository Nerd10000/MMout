/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ final class Leaf implements Octree.Node {
/*     */   private final SectionRenderDispatcher.RenderSection section;
/*     */   
/*     */   private Leaf(SectionRenderDispatcher.RenderSection section) {
/* 198 */     this.section = section;
/*     */   }
/*     */   
/*     */   public void visitNodes(Octree.OctreeVisitor visitor, boolean skipFrustumCheck, Frustum frustum, int depth, int closeDistance, boolean isClose) {
/* 203 */     AABB boundingBox = this.section.getBoundingBox();
/* 204 */     if (skipFrustumCheck || frustum.isVisible(getSection().getBoundingBox())) {
/* 205 */       isClose = (isClose && Octree.this.isClose(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, closeDistance));
/* 206 */       visitor.visit(this, skipFrustumCheck, depth, isClose);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SectionRenderDispatcher.RenderSection getSection() {
/* 212 */     return this.section;
/*     */   }
/*     */   
/*     */   public AABB getAABB() {
/* 217 */     return this.section.getBoundingBox();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Octree$Leaf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */