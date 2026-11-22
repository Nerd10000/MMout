/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*    */ 
/*    */ class null extends MeleeAttackGoal {
/*    */   null(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
/* 61 */     super(mob, speedModifier, followingTargetEvenIfNotSeen);
/*    */   }
/*    */   
/*    */   public void stop() {
/* 64 */     super.stop();
/* 65 */     AbstractSkeleton.this.setAggressive(false);
/*    */   }
/*    */   
/*    */   public void start() {
/* 70 */     super.start();
/* 71 */     AbstractSkeleton.this.setAggressive(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\skeleton\AbstractSkeleton$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */