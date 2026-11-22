/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ public class NonTameRandomTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*    */   private final TamableAnimal tamableMob;
/*    */   
/*    */   public NonTameRandomTargetGoal(TamableAnimal mob, Class<T> targetType, boolean mustSee, TargetingConditions.Selector subselector) {
/* 12 */     super((Mob)mob, targetType, 10, mustSee, false, subselector);
/* 13 */     this.tamableMob = mob;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 18 */     return (!this.tamableMob.isTame() && super.canUse());
/*    */   }
/*    */   
/*    */   public boolean canContinueToUse() {
/* 23 */     if (this.targetConditions != null)
/* 24 */       return this.targetConditions.test(getServerLevel((Entity)this.mob), (LivingEntity)this.mob, this.target); 
/* 26 */     return super.canContinueToUse();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\target\NonTameRandomTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */