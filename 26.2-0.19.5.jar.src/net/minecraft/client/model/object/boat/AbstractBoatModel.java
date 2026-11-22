/*    */ package net.minecraft.client.model.object.boat;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class AbstractBoatModel extends EntityModel<BoatRenderState> {
/*    */   private final ModelPart leftPaddle;
/*    */   
/*    */   private final ModelPart rightPaddle;
/*    */   
/*    */   public AbstractBoatModel(ModelPart root) {
/* 15 */     super(root);
/* 16 */     this.leftPaddle = root.getChild("left_paddle");
/* 17 */     this.rightPaddle = root.getChild("right_paddle");
/*    */   }
/*    */   
/*    */   public void setupAnim(BoatRenderState state) {
/* 22 */     super.setupAnim(state);
/* 24 */     animatePaddle(state.rowingTimeLeft, 0, this.leftPaddle);
/* 25 */     animatePaddle(state.rowingTimeRight, 1, this.rightPaddle);
/*    */   }
/*    */   
/*    */   private static void animatePaddle(float time, int side, ModelPart paddle) {
/* 29 */     paddle.xRot = Mth.clampedLerp((Mth.sin(-time) + 1.0F) / 2.0F, -1.0471976F, -0.2617994F);
/* 30 */     paddle.yRot = Mth.clampedLerp((Mth.sin((-time + 1.0F)) + 1.0F) / 2.0F, -0.7853982F, 0.7853982F);
/* 32 */     if (side == 1)
/* 33 */       paddle.yRot = 3.1415927F - paddle.yRot; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\boat\AbstractBoatModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */