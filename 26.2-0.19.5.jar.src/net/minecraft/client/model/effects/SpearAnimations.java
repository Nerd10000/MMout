/*     */ package net.minecraft.client.model.effects;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.Ease;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class SpearAnimations {
/*     */   private static float progress(float time, float start, float end) {
/*  20 */     return Mth.clamp(Mth.inverseLerp(time, start, end), 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private static final class UseParams extends Record {
/*     */     private final float raiseProgress;
/*     */     
/*     */     private final float raiseProgressStart;
/*     */     
/*     */     private final float raiseProgressMiddle;
/*     */     
/*     */     private final float raiseProgressEnd;
/*     */     
/*     */     private final float swayProgress;
/*     */     
/*     */     private final float lowerProgress;
/*     */     
/*     */     private final float raiseBackProgress;
/*     */     
/*     */     private final float swayIntensity;
/*     */     
/*     */     private final float swayScaleSlow;
/*     */     
/*     */     private final float swayScaleFast;
/*     */     
/*     */     private UseParams(float raiseProgress, float raiseProgressStart, float raiseProgressMiddle, float raiseProgressEnd, float swayProgress, float lowerProgress, float raiseBackProgress, float swayIntensity, float swayScaleSlow, float swayScaleFast) {
/*  23 */       this.raiseProgress = raiseProgress;
/*  23 */       this.raiseProgressStart = raiseProgressStart;
/*  23 */       this.raiseProgressMiddle = raiseProgressMiddle;
/*  23 */       this.raiseProgressEnd = raiseProgressEnd;
/*  23 */       this.swayProgress = swayProgress;
/*  23 */       this.lowerProgress = lowerProgress;
/*  23 */       this.raiseBackProgress = raiseBackProgress;
/*  23 */       this.swayIntensity = swayIntensity;
/*  23 */       this.swayScaleSlow = swayScaleSlow;
/*  23 */       this.swayScaleFast = swayScaleFast;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float raiseProgress() {
/*  23 */       return this.raiseProgress;
/*     */     }
/*     */     
/*     */     public float raiseProgressStart() {
/*  23 */       return this.raiseProgressStart;
/*     */     }
/*     */     
/*     */     public float raiseProgressMiddle() {
/*  23 */       return this.raiseProgressMiddle;
/*     */     }
/*     */     
/*     */     public float raiseProgressEnd() {
/*  23 */       return this.raiseProgressEnd;
/*     */     }
/*     */     
/*     */     public float swayProgress() {
/*  23 */       return this.swayProgress;
/*     */     }
/*     */     
/*     */     public float lowerProgress() {
/*  23 */       return this.lowerProgress;
/*     */     }
/*     */     
/*     */     public float raiseBackProgress() {
/*  23 */       return this.raiseBackProgress;
/*     */     }
/*     */     
/*     */     public float swayIntensity() {
/*  23 */       return this.swayIntensity;
/*     */     }
/*     */     
/*     */     public float swayScaleSlow() {
/*  23 */       return this.swayScaleSlow;
/*     */     }
/*     */     
/*     */     public float swayScaleFast() {
/*  23 */       return this.swayScaleFast;
/*     */     }
/*     */     
/*     */     public static UseParams fromKineticWeapon(KineticWeapon kineticWeapon, float time) {
/*  28 */       int finishRaisingTick = kineticWeapon.delayTicks();
/*  29 */       int finishSwayingTick = ((Integer)kineticWeapon.dismountConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/*  30 */       int startSwayingTick = finishSwayingTick - 20;
/*  31 */       int finishLoweringTick = ((Integer)kineticWeapon.knockbackConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/*  32 */       int startLoweringTick = finishLoweringTick - 40;
/*  33 */       int finishRaisingBackTick = ((Integer)kineticWeapon.damageConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/*  35 */       float raiseProgress = SpearAnimations.progress(time, 0.0F, finishRaisingTick);
/*  36 */       float raiseProgressStart = SpearAnimations.progress(raiseProgress, 0.0F, 0.5F);
/*  37 */       float raiseProgressMiddle = SpearAnimations.progress(raiseProgress, 0.5F, 0.8F);
/*  38 */       float raiseProgressEnd = SpearAnimations.progress(raiseProgress, 0.8F, 1.0F);
/*  40 */       float swayProgress = SpearAnimations.progress(time, startSwayingTick, finishSwayingTick);
/*  41 */       float lowerProgress = Ease.outCubic(Ease.inOutElastic(SpearAnimations.progress(time - 20.0F, startLoweringTick, finishLoweringTick)));
/*  42 */       float raiseBackProgress = SpearAnimations.progress(time, (finishRaisingBackTick - 5), finishRaisingBackTick);
/*  44 */       float swayIntensity = 2.0F * Ease.outCirc(swayProgress) - 2.0F * Ease.inCirc(raiseBackProgress);
/*  46 */       float swayScaleSlow = Mth.sin((time * 19.0F * 0.017453292F)) * swayIntensity;
/*  47 */       float swayScaleFast = Mth.sin((time * 30.0F * 0.017453292F)) * swayIntensity;
/*  49 */       return new UseParams(raiseProgress, raiseProgressStart, raiseProgressMiddle, raiseProgressEnd, swayProgress, lowerProgress, raiseBackProgress, swayIntensity, swayScaleSlow, swayScaleFast);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T extends HumanoidRenderState> void thirdPersonHandUse(ModelPart arm, ModelPart head, boolean holdingInRightArm, ItemStack item, T state) {
/*  55 */     int invert = holdingInRightArm ? 1 : -1;
/*  57 */     arm.yRot = -0.1F * invert + head.yRot;
/*  58 */     arm.xRot = -1.5707964F + head.xRot + 0.8F;
/*  60 */     if (((HumanoidRenderState)state).isFallFlying || ((HumanoidRenderState)state).swimAmount > 0.0F)
/*  61 */       arm.xRot -= 0.9599311F; 
/*  64 */     arm.yRot = 0.017453292F * Math.clamp(57.295776F * arm.yRot, -60.0F, 60.0F);
/*  65 */     arm.xRot = 0.017453292F * Math.clamp(57.295776F * arm.xRot, -120.0F, 30.0F);
/*  68 */     if (((HumanoidRenderState)state).ticksUsingItem <= 0.0F || (((HumanoidRenderState)state).isUsingItem && ((HumanoidRenderState)state).useItemHand != (holdingInRightArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)))
/*     */       return; 
/*  72 */     KineticWeapon kineticWeapon = (KineticWeapon)item.get(DataComponents.KINETIC_WEAPON);
/*  73 */     if (kineticWeapon == null)
/*     */       return; 
/*  76 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, ((HumanoidRenderState)state).ticksUsingItem);
/*  78 */     arm.yRot += -invert * params.swayScaleFast() * 0.017453292F * params.swayIntensity() * 1.0F;
/*  79 */     arm.zRot += -invert * params.swayScaleSlow() * 0.017453292F * params.swayIntensity() * 0.5F;
/*  80 */     arm.xRot += 0.017453292F * (-40.0F * params
/*  81 */       .raiseProgressStart() + 30.0F * params.raiseProgressMiddle() + -20.0F * params.raiseProgressEnd() + 20.0F * params.lowerProgress() + 10.0F * params.raiseBackProgress() + 0.6F * params
/*  82 */       .swayScaleSlow() * params.swayIntensity());
/*     */   }
/*     */   
/*     */   public static <S extends ArmedEntityRenderState> void thirdPersonUseItem(S state, PoseStack poseStack, float timeHeld, HumanoidArm arm, ItemStack actualItem) {
/*  87 */     KineticWeapon kineticWeapon = (KineticWeapon)actualItem.get(DataComponents.KINETIC_WEAPON);
/*  88 */     if (kineticWeapon == null || timeHeld == 0.0F)
/*     */       return; 
/*  93 */     float attack = Ease.inQuad(progress(((ArmedEntityRenderState)state).attackTime, 0.05F, 0.2F));
/*  94 */     float retract = Ease.inOutExpo(progress(((ArmedEntityRenderState)state).attackTime, 0.4F, 1.0F));
/*  96 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
/*  97 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/*  99 */     float raiseProgressModified = 1.0F - Ease.outBack(1.0F - params.raiseProgress());
/* 100 */     float itemInHandDepth = 0.125F;
/* 101 */     float hitFeedback = hitFeedbackAmount(((ArmedEntityRenderState)state).ticksSinceKineticHitFeedback);
/* 102 */     poseStack.translate(0.0D, -hitFeedback * 0.4D, (-kineticWeapon.forwardMovement() * (raiseProgressModified - params.raiseBackProgress()) + hitFeedback));
/* 104 */     poseStack.rotateAround((Quaternionfc)Axis.XN.rotationDegrees(70.0F * (params.raiseProgress() - params.raiseBackProgress()) - 40.0F * (attack - retract)), 0.0F, -0.03125F, 0.125F);
/* 106 */     poseStack.rotateAround((Quaternionfc)Axis.YP.rotationDegrees((invert * 90) * (params.raiseProgress() - params.swayProgress() + 3.0F * retract + attack)), 0.0F, 0.0F, 0.125F);
/*     */   }
/*     */   
/*     */   public static <T extends HumanoidRenderState> void thirdPersonAttackHand(HumanoidModel<T> model, T state) {
/* 110 */     float attackTime = ((HumanoidRenderState)state).attackTime;
/* 111 */     HumanoidArm arm = ((HumanoidRenderState)state).attackArm;
/* 112 */     model.rightArm.yRot -= model.body.yRot;
/* 113 */     model.leftArm.yRot -= model.body.yRot;
/* 114 */     model.leftArm.xRot -= model.body.yRot;
/* 116 */     float prepare = Ease.inOutSine(progress(attackTime, 0.0F, 0.05F));
/* 117 */     float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
/* 118 */     float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
/* 119 */     (model.getArm(arm)).xRot += (90.0F * prepare - 120.0F * attack + 30.0F * retract) * 0.017453292F;
/*     */   }
/*     */   
/*     */   public static <S extends ArmedEntityRenderState> void thirdPersonAttackItem(S state, PoseStack poseStack) {
/* 123 */     if (((ArmedEntityRenderState)state).attackTime <= 0.0F)
/*     */       return; 
/* 126 */     KineticWeapon kineticWeapon = (KineticWeapon)state.getMainHandItemStack().get(DataComponents.KINETIC_WEAPON);
/* 127 */     float jetForward = (kineticWeapon != null) ? kineticWeapon.forwardMovement() : 0.0F;
/* 129 */     float itemInHandDepth = 0.125F;
/* 130 */     float attackTime = ((ArmedEntityRenderState)state).attackTime;
/* 133 */     float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
/* 134 */     float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
/* 136 */     poseStack.rotateAround((Quaternionfc)Axis.XN.rotationDegrees(70.0F * (attack - retract)), 0.0F, -0.125F, 0.125F);
/* 137 */     poseStack.translate(0.0F, jetForward * (attack - retract), 0.0F);
/*     */   }
/*     */   
/*     */   private static float hitFeedbackAmount(float ticksSinceFeedbackStart) {
/* 141 */     return 0.4F * (Ease.outQuart(progress(ticksSinceFeedbackStart, 1.0F, 3.0F)) - Ease.inOutSine(progress(ticksSinceFeedbackStart, 3.0F, 10.0F)));
/*     */   }
/*     */   
/*     */   public static void firstPersonUse(float ticksSinceKineticHitFeedback, PoseStack poseStack, float timeHeld, HumanoidArm arm, ItemStack itemStack) {
/* 145 */     KineticWeapon kineticWeapon = (KineticWeapon)itemStack.get(DataComponents.KINETIC_WEAPON);
/* 146 */     if (kineticWeapon == null)
/*     */       return; 
/* 149 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
/* 150 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 152 */     poseStack.translate((invert * (params.raiseProgress() * 0.15F + params.raiseProgressEnd() * -0.05F + params.swayProgress() * -0.1F + params.swayScaleSlow() * 0.005F)), (params
/* 153 */         .raiseProgress() * -0.075F + params.raiseProgressMiddle() * 0.075F + params.swayScaleFast() * 0.01F), params
/* 154 */         .raiseProgressStart() * 0.05D + params.raiseProgressEnd() * -0.05D + (params.swayScaleSlow() * 0.005F));
/* 156 */     poseStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(-65.0F * 
/* 157 */           Ease.inOutBack(params.raiseProgress()) - 35.0F * params.lowerProgress() + 100.0F * params.raiseBackProgress() + -0.5F * params.swayScaleFast()), 0.0F, 0.1F, 0.0F);
/* 160 */     poseStack.rotateAround((Quaternionfc)Axis.YN.rotationDegrees(invert * (-90.0F * 
/* 161 */           progress(params.raiseProgress(), 0.5F, 0.55F) + 90.0F * params.swayProgress() + 2.0F * params.swayScaleSlow())), invert * 0.15F, 0.0F, 0.0F);
/* 164 */     poseStack.translate(0.0F, -hitFeedbackAmount(ticksSinceKineticHitFeedback), 0.0F);
/*     */   }
/*     */   
/*     */   public static void firstPersonAttack(float attack, PoseStack poseStack, int invert, HumanoidArm arm) {
/* 168 */     float startingAmount = Ease.inOutSine(progress(attack, 0.0F, 0.05F));
/* 169 */     float middleAmount = Ease.outBack(progress(attack, 0.05F, 0.2F));
/* 170 */     float endingAmount = Ease.inOutExpo(progress(attack, 0.4F, 1.0F));
/* 172 */     poseStack.translate(invert * 0.1F * (startingAmount - middleAmount), -0.075F * (startingAmount - endingAmount), 0.65F * (startingAmount - middleAmount));
/* 175 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-70.0F * (startingAmount - endingAmount)));
/* 176 */     poseStack.translate(0.0D, 0.0D, -0.25D * (endingAmount - middleAmount));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\effects\SpearAnimations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */