/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.effects.SpearAnimations;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public enum ArmPose {
/* 63 */   EMPTY(false, false),
/* 64 */   ITEM(false, false),
/* 65 */   BLOCK(false, false),
/* 66 */   BOW_AND_ARROW(true, true),
/* 67 */   THROW_TRIDENT(false, true),
/* 68 */   CROSSBOW_CHARGE(true, true),
/* 69 */   CROSSBOW_HOLD(true, true),
/* 70 */   SPYGLASS(false, false),
/* 71 */   TOOT_HORN(false, false),
/* 72 */   BRUSH(false, false),
/* 73 */   SPEAR(false, true) {
/*    */     public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {
/* 76 */       SpearAnimations.thirdPersonUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*    */     }
/*    */   };
/*    */   
/*    */   private final boolean twoHanded;
/*    */   
/*    */   private final boolean affectsOffhandPose;
/*    */   
/*    */   ArmPose(boolean twoHanded, boolean affectsOffhandPose) {
/* 85 */     this.twoHanded = twoHanded;
/* 86 */     this.affectsOffhandPose = affectsOffhandPose;
/*    */   }
/*    */   
/*    */   public boolean isTwoHanded() {
/* 90 */     return this.twoHanded;
/*    */   }
/*    */   
/*    */   public boolean affectsOffhandPose() {
/* 94 */     return this.affectsOffhandPose;
/*    */   }
/*    */   
/*    */   public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\HumanoidModel$ArmPose.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */