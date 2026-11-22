/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RaidDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private static final int MAX_RENDER_DIST = 160;
/*    */   
/*    */   private static final float TEXT_SCALE = 0.64F;
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public RaidDebugRenderer(Minecraft minecraft) {
/* 24 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 29 */     BlockPos playerPos = getCamera().blockPosition();
/* 31 */     debugValues.forEachChunk(DebugSubscriptions.RAIDS, (chunkPos, raidCenters) -> {
/*    */           for (BlockPos raidCenter : raidCenters) {
/*    */             if (playerPos.closerThan((Vec3i)raidCenter, 160.0D))
/*    */               highlightRaidCenter(raidCenter); 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void highlightRaidCenter(BlockPos raidCenter) {
/* 41 */     Gizmos.cuboid(raidCenter, GizmoStyle.fill(ARGB.colorFromFloat(0.15F, 1.0F, 0.0F, 0.0F)));
/* 42 */     renderTextOverBlock("Raid center", raidCenter, -65536);
/*    */   }
/*    */   
/*    */   private static void renderTextOverBlock(String text, BlockPos pos, int color) {
/* 46 */     Gizmos.billboardText(text, Vec3.atLowerCornerWithOffset((Vec3i)pos, 0.5D, 1.3D, 0.5D), TextGizmo.Style.forColor(color).withScale(0.64F)).setAlwaysOnTop();
/*    */   }
/*    */   
/*    */   private Camera getCamera() {
/* 50 */     return this.minecraft.gameRenderer.mainCamera();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\RaidDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */