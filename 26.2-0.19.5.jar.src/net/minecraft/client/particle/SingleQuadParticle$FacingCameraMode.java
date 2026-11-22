/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public interface FacingCameraMode {
/*    */   public static final FacingCameraMode LOOKAT_XYZ;
/*    */   
/*    */   public static final FacingCameraMode LOOKAT_Y;
/*    */   
/*    */   static {
/* 19 */     LOOKAT_XYZ = ((target, camera, partialTickTime) -> target.set((Quaternionfc)camera.rotation()));
/* 21 */     LOOKAT_Y = ((target, camera, partialTickTime) -> target.set(0.0F, (camera.rotation()).y, 0.0F, (camera.rotation()).w));
/*    */   }
/*    */   
/*    */   void setRotation(Quaternionf paramQuaternionf, Camera paramCamera, float paramFloat);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SingleQuadParticle$FacingCameraMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */