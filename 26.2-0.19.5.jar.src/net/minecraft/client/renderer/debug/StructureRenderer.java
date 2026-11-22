/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugStructureInfo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class StructureRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 15 */     debugValues.forEachChunk(DebugSubscriptions.STRUCTURES, (chunkPos, structures) -> {
/*    */           for (DebugStructureInfo structure : structures) {
/*    */             Gizmos.cuboid(AABB.of(structure.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F)));
/*    */             for (DebugStructureInfo.Piece piece : structure.pieces()) {
/*    */               if (piece.isStart()) {
/*    */                 Gizmos.cuboid(AABB.of(piece.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.0F, 1.0F, 0.0F)));
/*    */                 continue;
/*    */               } 
/*    */               Gizmos.cuboid(AABB.of(piece.boundingBox()), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.0F, 0.0F, 1.0F)));
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\StructureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */