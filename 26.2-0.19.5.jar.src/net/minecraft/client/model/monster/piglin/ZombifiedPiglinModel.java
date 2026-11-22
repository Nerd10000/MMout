/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.AnimationUtils;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
/*    */ 
/*    */ public abstract class ZombifiedPiglinModel extends AbstractPiglinModel<ZombifiedPiglinRenderState> {
/*    */   public ZombifiedPiglinModel(ModelPart root) {
/* 10 */     super(root);
/*    */   }
/*    */   
/*    */   public void setupAnim(ZombifiedPiglinRenderState state) {
/* 15 */     super.setupAnim(state);
/* 16 */     AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, (UndeadRenderState)state);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\ZombifiedPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */