/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestHealableRaiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*    */   private static final int DEFAULT_COOLDOWN = 200;
/*    */   
/*    */   private int cooldown;
/*    */   
/*    */   public NearestHealableRaiderTargetGoal(Raider raider, Class<T> targetType, boolean mustSee, TargetingConditions.Selector subselector) {
/* 14 */     super((Mob)raider, targetType, 500, mustSee, false, subselector);
/* 15 */     this.cooldown = 0;
/*    */   }
/*    */   
/*    */   public int getCooldown() {
/* 19 */     return this.cooldown;
/*    */   }
/*    */   
/*    */   public void decrementCooldown() {
/* 23 */     this.cooldown--;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 28 */     if (this.cooldown > 0 || !this.mob.getRandom().nextBoolean())
/* 29 */       return false; 
/* 31 */     if (!((Raider)this.mob).hasActiveRaid())
/* 32 */       return false; 
/* 35 */     findTarget();
/* 36 */     return (this.target != null);
/*    */   }
/*    */   
/*    */   public void start() {
/* 41 */     this.cooldown = reducedTickDelay(200);
/* 42 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\target\NearestHealableRaiderTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */