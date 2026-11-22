/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.Octree;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ public class OctreeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public OctreeDebugRenderer(Minecraft minecraft) {
/* 23 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 28 */     Octree octree = this.minecraft.levelRenderer.sectionOcclusionGraph().getOctree();
/* 29 */     MutableInt count = new MutableInt(0);
/* 30 */     octree.visitNodes((node, fullyVisible, depth, isClose) -> renderNode(node, depth, fullyVisible, count, isClose), frustum, 32);
/*    */   }
/*    */   
/*    */   private void renderNode(Octree.Node node, int depth, boolean fullyVisible, MutableInt count, boolean isClose) {
/* 34 */     AABB aabb = node.getAABB();
/* 35 */     double xSize = aabb.getXsize();
/* 36 */     long size = Math.round(xSize / 16.0D);
/* 37 */     if (size == 1L) {
/* 38 */       count.add(1);
/* 39 */       int color = isClose ? -16711936 : -1;
/* 40 */       Gizmos.billboardText(String.valueOf(count.intValue()), aabb.getCenter(), TextGizmo.Style.forColorAndCentered(color).withScale(4.8F));
/*    */     } 
/* 42 */     long colorNum = size + 5L;
/* 43 */     Gizmos.cuboid(aabb.deflate(0.1D * depth), GizmoStyle.stroke(ARGB.colorFromFloat(fullyVisible ? 0.4F : 1.0F, getColorComponent(colorNum, 0.3F), getColorComponent(colorNum, 0.8F), getColorComponent(colorNum, 0.5F))));
/*    */   }
/*    */   
/*    */   private static float getColorComponent(long size, float multiplier) {
/* 47 */     float minColor = 0.1F;
/* 48 */     return Mth.frac(multiplier * (float)size) * 0.9F + 0.1F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\OctreeDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */