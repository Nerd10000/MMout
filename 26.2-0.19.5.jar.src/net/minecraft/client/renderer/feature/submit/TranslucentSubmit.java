/*    */ package net.minecraft.client.renderer.feature.submit;
/*    */ 
/*    */ import net.minecraft.client.renderer.feature.FeatureRendererType;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public interface TranslucentSubmit extends SubmitNode {
/*    */   float distanceToCameraSq();
/*    */   
/*    */   FeatureRendererType<? extends TranslucentSubmit> featureType();
/*    */   
/*    */   static float computeDistanceToCameraSq(Matrix4fc pose) {
/* 15 */     return Mth.lengthSquared(pose.m30(), pose.m31(), pose.m32());
/*    */   }
/*    */   
/*    */   static float computeDistanceToCameraSq(Matrix4fc pose, float originX, float originY, float originZ) {
/* 19 */     return pose.transformPosition(originX, originY, originZ, new Vector3f()).lengthSquared();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\submit\TranslucentSubmit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */