/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class KeyframeAnimations {
/*    */   public static Vector3f posVec(float x, float y, float z) {
/*  9 */     return new Vector3f(x, -y, z);
/*    */   }
/*    */   
/*    */   public static Vector3f degreeVec(float x, float y, float z) {
/* 13 */     return new Vector3f(x * 0.017453292F, y * 0.017453292F, z * 0.017453292F);
/*    */   }
/*    */   
/*    */   public static Vector3f scaleVec(double x, double y, double z) {
/* 24 */     return new Vector3f((float)(x - 1.0D), (float)(y - 1.0D), (float)(z - 1.0D));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\KeyframeAnimations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */