/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ public class CubeDeformation {
/*  6 */   public static final CubeDeformation NONE = new CubeDeformation(0.0F);
/*    */   
/*    */   final float growX;
/*    */   
/*    */   final float growY;
/*    */   
/*    */   final float growZ;
/*    */   
/*    */   public CubeDeformation(float growX, float growY, float growZ) {
/* 16 */     this.growX = growX;
/* 17 */     this.growY = growY;
/* 18 */     this.growZ = growZ;
/*    */   }
/*    */   
/*    */   public CubeDeformation(float grow) {
/* 22 */     this(grow, grow, grow);
/*    */   }
/*    */   
/*    */   public CubeDeformation extend(float factor) {
/* 26 */     return new CubeDeformation(this.growX + factor, this.growY + factor, this.growZ + factor);
/*    */   }
/*    */   
/*    */   public CubeDeformation extend(float factorX, float factorY, float factorZ) {
/* 30 */     return new CubeDeformation(this.growX + factorX, this.growY + factorY, this.growZ + factorZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\builders\CubeDeformation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */