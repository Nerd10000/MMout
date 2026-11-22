/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.debug.DebugEntityBlockIntersection;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ 
/*    */ public class EntityBlockIntersectionDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private static final float PADDING = 0.02F;
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 14 */     debugValues.forEachBlock(DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS, (pos, type) -> Gizmos.cuboid(pos, 0.02F, GizmoStyle.fill(type.color())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\EntityBlockIntersectionDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */