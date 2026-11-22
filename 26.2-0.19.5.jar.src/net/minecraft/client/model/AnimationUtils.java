/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ 
/*     */ public class AnimationUtils {
/*     */   public static void animateCrossbowHold(ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean holdingInRightArm) {
/*  14 */     ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
/*  15 */     ModelPart shootingArm = holdingInRightArm ? leftArm : rightArm;
/*  17 */     holdingArm.yRot = (holdingInRightArm ? -0.3F : 0.3F) + head.yRot;
/*  18 */     shootingArm.yRot = (holdingInRightArm ? 0.6F : -0.6F) + head.yRot;
/*  19 */     holdingArm.xRot = -1.5707964F + head.xRot + 0.1F;
/*  20 */     shootingArm.xRot = -1.5F + head.xRot;
/*     */   }
/*     */   
/*     */   public static void animateCrossbowCharge(ModelPart rightArm, ModelPart leftArm, float maxCrossbowChargeDuration, float ticksUsingItem, boolean holdingInRightArm) {
/*  25 */     ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
/*  26 */     ModelPart pullingArm = holdingInRightArm ? leftArm : rightArm;
/*  28 */     holdingArm.yRot = holdingInRightArm ? -0.8F : 0.8F;
/*  29 */     holdingArm.xRot = -0.97079635F;
/*  30 */     pullingArm.xRot = holdingArm.xRot;
/*  33 */     float useTicks = Mth.clamp(ticksUsingItem, 0.0F, maxCrossbowChargeDuration);
/*  34 */     float lerpAlpha = useTicks / maxCrossbowChargeDuration;
/*  35 */     pullingArm.yRot = Mth.lerp(lerpAlpha, 0.4F, 0.85F) * (holdingInRightArm ? true : -1);
/*  36 */     pullingArm.xRot = Mth.lerp(lerpAlpha, pullingArm.xRot, -1.5707964F);
/*     */   }
/*     */   
/*     */   public static void swingWeaponDown(ModelPart rightArm, ModelPart leftArm, HumanoidArm mainArm, float attackTime, float ageInTicks) {
/*  40 */     float attack2 = Mth.sin((attackTime * 3.1415927F));
/*  41 */     float attack = Mth.sin(((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.1415927F));
/*  42 */     rightArm.zRot = 0.0F;
/*  43 */     leftArm.zRot = 0.0F;
/*  44 */     rightArm.yRot = 0.15707964F;
/*  45 */     leftArm.yRot = -0.15707964F;
/*  47 */     if (mainArm == HumanoidArm.RIGHT) {
/*  48 */       rightArm.xRot = -1.8849558F + Mth.cos((ageInTicks * 0.09F)) * 0.15F;
/*  49 */       leftArm.xRot = -0.0F + Mth.cos((ageInTicks * 0.19F)) * 0.5F;
/*  51 */       rightArm.xRot += attack2 * 2.2F - attack * 0.4F;
/*  52 */       leftArm.xRot += attack2 * 1.2F - attack * 0.4F;
/*     */     } else {
/*  54 */       rightArm.xRot = -0.0F + Mth.cos((ageInTicks * 0.19F)) * 0.5F;
/*  55 */       leftArm.xRot = -1.8849558F + Mth.cos((ageInTicks * 0.09F)) * 0.15F;
/*  57 */       rightArm.xRot += attack2 * 1.2F - attack * 0.4F;
/*  58 */       leftArm.xRot += attack2 * 2.2F - attack * 0.4F;
/*     */     } 
/*  61 */     bobArms(rightArm, leftArm, ageInTicks);
/*     */   }
/*     */   
/*     */   public static void bobModelPart(ModelPart modelPart, float ageInTicks, float scale) {
/*  65 */     modelPart.zRot += scale * (Mth.cos((ageInTicks * 0.09F)) * 0.05F + 0.05F);
/*  66 */     modelPart.xRot += scale * Mth.sin((ageInTicks * 0.067F)) * 0.05F;
/*     */   }
/*     */   
/*     */   public static void bobArms(ModelPart rightArm, ModelPart leftArm, float ageInTicks) {
/*  70 */     bobModelPart(rightArm, ageInTicks, 1.0F);
/*  71 */     bobModelPart(leftArm, ageInTicks, -1.0F);
/*     */   }
/*     */   
/*     */   public static <T extends UndeadRenderState> void animateZombieArms(ModelPart leftArm, ModelPart rightArm, boolean aggressive, T state) {
/*  76 */     boolean animateAttack = (((UndeadRenderState)state).swingAnimationType != SwingAnimationType.STAB);
/*  77 */     if (animateAttack) {
/*  79 */       boolean raiseArms = (!((UndeadRenderState)state).isBaby || state.getMainHandItemStack() == ItemStack.EMPTY);
/*  80 */       float armDrop = raiseArms ? (-3.1415927F / (aggressive ? 1.5F : 2.25F)) : 0.0F;
/*  81 */       animateAttackArms(leftArm, rightArm, ((UndeadRenderState)state).attackTime, raiseArms, armDrop);
/*     */     } 
/*  84 */     bobArms(rightArm, leftArm, ((UndeadRenderState)state).ageInTicks);
/*     */   }
/*     */   
/*     */   private static void animateAttackArms(ModelPart leftArm, ModelPart rightArm, float attackTime, boolean negateArmRotation, float armDrop) {
/*  90 */     float attackYRotModifier = (negateArmRotation ? 1.0F : -1.0F) * Mth.sin((attackTime * 3.1415927F));
/*  91 */     float attackXRotModifier = Mth.sin(((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.1415927F));
/*  92 */     float xRot = armDrop + attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
/*  93 */     float yRot = 0.1F - attackYRotModifier * 0.6F;
/*  95 */     rightArm.xRot = xRot;
/*  96 */     rightArm.yRot = negateArmRotation ? -yRot : yRot;
/*  97 */     rightArm.zRot = 0.0F;
/*  99 */     leftArm.xRot = xRot;
/* 100 */     leftArm.yRot = negateArmRotation ? yRot : -yRot;
/* 101 */     leftArm.zRot = 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\AnimationUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */