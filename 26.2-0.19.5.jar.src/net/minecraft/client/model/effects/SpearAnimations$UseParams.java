/*    */ package net.minecraft.client.model.effects;
/*    */ 
/*    */ import net.minecraft.util.Ease;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.component.KineticWeapon;
/*    */ 
/*    */ final class UseParams extends Record {
/*    */   private final float raiseProgress;
/*    */   
/*    */   private final float raiseProgressStart;
/*    */   
/*    */   private final float raiseProgressMiddle;
/*    */   
/*    */   private final float raiseProgressEnd;
/*    */   
/*    */   private final float swayProgress;
/*    */   
/*    */   private final float lowerProgress;
/*    */   
/*    */   private final float raiseBackProgress;
/*    */   
/*    */   private final float swayIntensity;
/*    */   
/*    */   private final float swayScaleSlow;
/*    */   
/*    */   private final float swayScaleFast;
/*    */   
/*    */   private UseParams(float raiseProgress, float raiseProgressStart, float raiseProgressMiddle, float raiseProgressEnd, float swayProgress, float lowerProgress, float raiseBackProgress, float swayIntensity, float swayScaleSlow, float swayScaleFast) {
/* 23 */     this.raiseProgress = raiseProgress;
/* 23 */     this.raiseProgressStart = raiseProgressStart;
/* 23 */     this.raiseProgressMiddle = raiseProgressMiddle;
/* 23 */     this.raiseProgressEnd = raiseProgressEnd;
/* 23 */     this.swayProgress = swayProgress;
/* 23 */     this.lowerProgress = lowerProgress;
/* 23 */     this.raiseBackProgress = raiseBackProgress;
/* 23 */     this.swayIntensity = swayIntensity;
/* 23 */     this.swayScaleSlow = swayScaleSlow;
/* 23 */     this.swayScaleFast = swayScaleFast;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float raiseProgress() {
/* 23 */     return this.raiseProgress;
/*    */   }
/*    */   
/*    */   public float raiseProgressStart() {
/* 23 */     return this.raiseProgressStart;
/*    */   }
/*    */   
/*    */   public float raiseProgressMiddle() {
/* 23 */     return this.raiseProgressMiddle;
/*    */   }
/*    */   
/*    */   public float raiseProgressEnd() {
/* 23 */     return this.raiseProgressEnd;
/*    */   }
/*    */   
/*    */   public float swayProgress() {
/* 23 */     return this.swayProgress;
/*    */   }
/*    */   
/*    */   public float lowerProgress() {
/* 23 */     return this.lowerProgress;
/*    */   }
/*    */   
/*    */   public float raiseBackProgress() {
/* 23 */     return this.raiseBackProgress;
/*    */   }
/*    */   
/*    */   public float swayIntensity() {
/* 23 */     return this.swayIntensity;
/*    */   }
/*    */   
/*    */   public float swayScaleSlow() {
/* 23 */     return this.swayScaleSlow;
/*    */   }
/*    */   
/*    */   public float swayScaleFast() {
/* 23 */     return this.swayScaleFast;
/*    */   }
/*    */   
/*    */   public static UseParams fromKineticWeapon(KineticWeapon kineticWeapon, float time) {
/* 28 */     int finishRaisingTick = kineticWeapon.delayTicks();
/* 29 */     int finishSwayingTick = ((Integer)kineticWeapon.dismountConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/* 30 */     int startSwayingTick = finishSwayingTick - 20;
/* 31 */     int finishLoweringTick = ((Integer)kineticWeapon.knockbackConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/* 32 */     int startLoweringTick = finishLoweringTick - 40;
/* 33 */     int finishRaisingBackTick = ((Integer)kineticWeapon.damageConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue() + finishRaisingTick;
/* 35 */     float raiseProgress = SpearAnimations.progress(time, 0.0F, finishRaisingTick);
/* 36 */     float raiseProgressStart = SpearAnimations.progress(raiseProgress, 0.0F, 0.5F);
/* 37 */     float raiseProgressMiddle = SpearAnimations.progress(raiseProgress, 0.5F, 0.8F);
/* 38 */     float raiseProgressEnd = SpearAnimations.progress(raiseProgress, 0.8F, 1.0F);
/* 40 */     float swayProgress = SpearAnimations.progress(time, startSwayingTick, finishSwayingTick);
/* 41 */     float lowerProgress = Ease.outCubic(Ease.inOutElastic(SpearAnimations.progress(time - 20.0F, startLoweringTick, finishLoweringTick)));
/* 42 */     float raiseBackProgress = SpearAnimations.progress(time, (finishRaisingBackTick - 5), finishRaisingBackTick);
/* 44 */     float swayIntensity = 2.0F * Ease.outCirc(swayProgress) - 2.0F * Ease.inCirc(raiseBackProgress);
/* 46 */     float swayScaleSlow = Mth.sin((time * 19.0F * 0.017453292F)) * swayIntensity;
/* 47 */     float swayScaleFast = Mth.sin((time * 30.0F * 0.017453292F)) * swayIntensity;
/* 49 */     return new UseParams(raiseProgress, raiseProgressStart, raiseProgressMiddle, raiseProgressEnd, swayProgress, lowerProgress, raiseBackProgress, swayIntensity, swayScaleSlow, swayScaleFast);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\effects\SpearAnimations$UseParams.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */