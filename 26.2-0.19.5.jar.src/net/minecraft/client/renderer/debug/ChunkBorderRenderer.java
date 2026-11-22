/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ChunkBorderRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private static final float THICK_WIDTH = 4.0F;
/*    */   
/*    */   private static final float THIN_WIDTH = 1.0F;
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/* 20 */   private static final int CELL_BORDER = ARGB.color(255, 0, 155, 155);
/*    */   
/* 21 */   private static final int YELLOW = ARGB.color(255, 255, 255, 0);
/*    */   
/* 22 */   private static final int MAJOR_LINES = ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 1.0F);
/*    */   
/*    */   public ChunkBorderRenderer(Minecraft minecraft) {
/* 25 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 30 */     Entity cameraEntity = this.minecraft.getCameraEntity();
/* 32 */     float ymin = this.minecraft.level.getMinY();
/* 33 */     float ymax = (this.minecraft.level.getMaxY() + 1);
/* 35 */     SectionPos cameraPos = SectionPos.of(cameraEntity.blockPosition());
/* 36 */     double xstart = cameraPos.minBlockX();
/* 37 */     double zstart = cameraPos.minBlockZ();
/*    */     int j;
/* 40 */     for (j = -16; j <= 32; j += 16) {
/* 41 */       for (int k = -16; k <= 32; k += 16)
/* 42 */         Gizmos.line(new Vec3(xstart + j, ymin, zstart + k), new Vec3(xstart + j, ymax, zstart + k), ARGB.colorFromFloat(0.5F, 1.0F, 0.0F, 0.0F), 4.0F); 
/*    */     } 
/* 47 */     for (j = 2; j < 16; j += 2) {
/* 48 */       int color = (j % 4 == 0) ? CELL_BORDER : YELLOW;
/* 49 */       Gizmos.line(new Vec3(xstart + j, ymin, zstart), new Vec3(xstart + j, ymax, zstart), color, 1.0F);
/* 50 */       Gizmos.line(new Vec3(xstart + j, ymin, zstart + 16.0D), new Vec3(xstart + j, ymax, zstart + 16.0D), color, 1.0F);
/*    */     } 
/* 54 */     for (int z = 2; z < 16; z += 2) {
/* 55 */       int color = (z % 4 == 0) ? CELL_BORDER : YELLOW;
/* 56 */       Gizmos.line(new Vec3(xstart, ymin, zstart + z), new Vec3(xstart, ymax, zstart + z), color, 1.0F);
/* 58 */       Gizmos.line(new Vec3(xstart + 16.0D, ymin, zstart + z), new Vec3(xstart + 16.0D, ymax, zstart + z), color, 1.0F);
/*    */     } 
/* 62 */     for (int i = this.minecraft.level.getMinY(); i <= this.minecraft.level.getMaxY() + 1; i += 2) {
/* 63 */       float yline = i;
/* 64 */       int color = (i % 8 == 0) ? CELL_BORDER : YELLOW;
/* 65 */       Gizmos.line(new Vec3(xstart, yline, zstart), new Vec3(xstart, yline, zstart + 16.0D), color, 1.0F);
/* 66 */       Gizmos.line(new Vec3(xstart, yline, zstart + 16.0D), new Vec3(xstart + 16.0D, yline, zstart + 16.0D), color, 1.0F);
/* 67 */       Gizmos.line(new Vec3(xstart + 16.0D, yline, zstart + 16.0D), new Vec3(xstart + 16.0D, yline, zstart), color, 1.0F);
/* 68 */       Gizmos.line(new Vec3(xstart + 16.0D, yline, zstart), new Vec3(xstart, yline, zstart), color, 1.0F);
/*    */     } 
/* 72 */     for (int x = 0; x <= 16; x += 16) {
/* 73 */       for (int k = 0; k <= 16; k += 16)
/* 74 */         Gizmos.line(new Vec3(xstart + x, ymin, zstart + k), new Vec3(xstart + x, ymax, zstart + k), MAJOR_LINES, 4.0F); 
/*    */     } 
/* 79 */     Gizmos.cuboid(new AABB(cameraPos
/*    */           
/* 81 */           .minBlockX(), cameraPos
/* 82 */           .minBlockY(), cameraPos
/* 83 */           .minBlockZ(), (cameraPos
/* 84 */           .maxBlockX() + 1), (cameraPos
/* 85 */           .maxBlockY() + 1), (cameraPos
/* 86 */           .maxBlockZ() + 1)), 
/*    */         
/* 88 */         GizmoStyle.stroke(MAJOR_LINES, 1.0F))
/* 89 */       .setAlwaysOnTop();
/* 92 */     for (int y = this.minecraft.level.getMinY(); y <= this.minecraft.level.getMaxY() + 1; y += 16) {
/* 93 */       Gizmos.line(new Vec3(xstart, y, zstart), new Vec3(xstart, y, zstart + 16.0D), MAJOR_LINES, 4.0F);
/* 94 */       Gizmos.line(new Vec3(xstart, y, zstart + 16.0D), new Vec3(xstart + 16.0D, y, zstart + 16.0D), MAJOR_LINES, 4.0F);
/* 95 */       Gizmos.line(new Vec3(xstart + 16.0D, y, zstart + 16.0D), new Vec3(xstart + 16.0D, y, zstart), MAJOR_LINES, 4.0F);
/* 96 */       Gizmos.line(new Vec3(xstart + 16.0D, y, zstart), new Vec3(xstart, y, zstart), MAJOR_LINES, 4.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\ChunkBorderRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */