/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ 
/*    */ import net.minecraft.client.model.AnimationUtils;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ 
/*    */ public abstract class AbstractZombieModel<S extends ZombieRenderState> extends HumanoidModel<S> {
/*    */   protected AbstractZombieModel(ModelPart root) {
/* 10 */     super(root);
/*    */   }
/*    */   
/*    */   public void setupAnim(S state) {
/* 15 */     super.setupAnim((HumanoidRenderState)state);
/* 17 */     AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, ((ZombieRenderState)state).isAggressive, (UndeadRenderState)state);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\zombie\AbstractZombieModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */