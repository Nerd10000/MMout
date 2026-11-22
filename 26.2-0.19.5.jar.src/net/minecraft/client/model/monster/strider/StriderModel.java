/*    */ package net.minecraft.client.model.monster.strider;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.StriderRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class StriderModel extends EntityModel<StriderRenderState> {
/*    */   protected static final float SPEED = 1.5F;
/*    */   
/*    */   protected final ModelPart rightLeg;
/*    */   
/*    */   protected final ModelPart leftLeg;
/*    */   
/*    */   protected final ModelPart body;
/*    */   
/*    */   public StriderModel(ModelPart root) {
/* 24 */     super(root);
/* 25 */     this.rightLeg = root.getChild("right_leg");
/* 26 */     this.leftLeg = root.getChild("left_leg");
/* 27 */     this.body = root.getChild("body");
/*    */   }
/*    */   
/*    */   public void setupAnim(StriderRenderState state) {
/* 32 */     super.setupAnim(state);
/* 34 */     float animationPos = state.walkAnimationPos;
/* 35 */     float animationSpeed = Math.min(state.walkAnimationSpeed, 0.25F);
/* 37 */     if (!state.isRidden) {
/* 38 */       this.body.xRot = state.xRot * 0.017453292F;
/* 39 */       this.body.yRot = state.yRot * 0.017453292F;
/*    */     } else {
/* 41 */       this.body.xRot = 0.0F;
/* 42 */       this.body.yRot = 0.0F;
/*    */     } 
/* 45 */     this.body.zRot = 0.1F * Mth.sin((animationPos * 1.5F)) * 4.0F * animationSpeed;
/* 47 */     this.leftLeg.xRot = Mth.sin((animationPos * 1.5F * 0.5F)) * 2.0F * animationSpeed;
/* 48 */     this.rightLeg.xRot = Mth.sin((animationPos * 1.5F * 0.5F + 3.1415927F)) * 2.0F * animationSpeed;
/* 50 */     this.leftLeg.zRot = 0.17453292F * Mth.cos((animationPos * 1.5F * 0.5F)) * animationSpeed;
/* 51 */     this.rightLeg.zRot = 0.17453292F * Mth.cos((animationPos * 1.5F * 0.5F + 3.1415927F)) * animationSpeed;
/* 53 */     customAnimations(animationPos, animationSpeed, state.ageInTicks);
/*    */   }
/*    */   
/*    */   public void animateBristle(float ageInTicks, float bristleFlow, ModelPart firstBristle, ModelPart secondBristle, ModelPart thirdBristle, BiConsumer<ModelPart, Float> addRotationFunction) {
/* 59 */     addRotationFunction.accept(firstBristle, Float.valueOf(bristleFlow * 0.6F));
/* 60 */     addRotationFunction.accept(secondBristle, Float.valueOf(bristleFlow * 1.2F));
/* 61 */     addRotationFunction.accept(thirdBristle, Float.valueOf(bristleFlow * 1.3F));
/* 63 */     addRotationFunction.accept(firstBristle, Float.valueOf(0.1F * Mth.sin((ageInTicks * 0.4F))));
/* 64 */     addRotationFunction.accept(secondBristle, Float.valueOf(0.1F * Mth.sin((ageInTicks * 0.2F))));
/* 65 */     addRotationFunction.accept(thirdBristle, Float.valueOf(0.05F * Mth.sin((ageInTicks * -0.4F))));
/*    */   }
/*    */   
/*    */   protected abstract void customAnimations(float paramFloat1, float paramFloat2, float paramFloat3);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\strider\StriderModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */