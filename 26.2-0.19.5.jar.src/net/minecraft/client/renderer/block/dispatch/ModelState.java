/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public interface ModelState {
/* 10 */   public static final Matrix4fc NO_TRANSFORM = (Matrix4fc)new Matrix4f();
/*    */   
/*    */   default Transformation transformation() {
/* 63 */     return Transformation.IDENTITY;
/*    */   }
/*    */   
/*    */   default Matrix4fc faceTransformation(Direction face) {
/* 68 */     return NO_TRANSFORM;
/*    */   }
/*    */   
/*    */   default Matrix4fc inverseFaceTransformation(Direction face) {
/* 73 */     return NO_TRANSFORM;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\ModelState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */