/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ public class InstantaneousMobEffect extends MobEffect {
/*    */   public InstantaneousMobEffect(MobEffectCategory category, int color) {
/*  5 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean isInstantaneous() {
/* 10 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 15 */     return (remainingDuration >= 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\InstantaneousMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */