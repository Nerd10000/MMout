/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class Interpolations {
/*    */   public static final AnimationChannel.Interpolation LINEAR;
/*    */   
/*    */   public static final AnimationChannel.Interpolation CATMULLROM;
/*    */   
/*    */   static {
/* 28 */     LINEAR = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */         Vector3fc point0 = keyframes[prev].postTarget();
/*    */         Vector3fc point1 = keyframes[next].preTarget();
/*    */         return point0.lerp(point1, alpha, vector).mul(targetScale);
/*    */       });
/* 35 */     CATMULLROM = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */         Vector3fc point0 = keyframes[Math.max(0, prev - 1)].postTarget();
/*    */         Vector3fc point1 = keyframes[prev].postTarget();
/*    */         Vector3fc point2 = keyframes[next].postTarget();
/*    */         Vector3fc point3 = keyframes[Math.min(keyframes.length - 1, next + 1)].postTarget();
/*    */         vector.set(Mth.catmullrom(alpha, point0.x(), point1.x(), point2.x(), point3.x()) * targetScale, Mth.catmullrom(alpha, point0.y(), point1.y(), point2.y(), point3.y()) * targetScale, Mth.catmullrom(alpha, point0.z(), point1.z(), point2.z(), point3.z()) * targetScale);
/*    */         return vector;
/*    */       });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\AnimationChannel$Interpolations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */