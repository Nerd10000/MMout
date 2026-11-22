/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import java.util.EnumSet;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ 
/*      */ class PerchAndSearchGoal extends Fox.FoxBehaviorGoal {
/*      */   private double relX;
/*      */   
/*      */   private double relZ;
/*      */   
/*      */   private int lookTime;
/*      */   
/*      */   private int looksRemaining;
/*      */   
/*      */   public PerchAndSearchGoal() {
/* 1170 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1175 */     return (Fox.this.getLastHurtByMob() == null && Fox.this.getRandom().nextFloat() < 0.02F && !Fox.this.isSleeping() && Fox.this.getTarget() == null && Fox.this.getNavigation().isDone() && !alertable() && !Fox.this.isPouncing() && !Fox.this.isCrouching());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1180 */     return (this.looksRemaining > 0);
/*      */   }
/*      */   
/*      */   public void start() {
/* 1185 */     resetLook();
/* 1186 */     this.looksRemaining = 2 + Fox.this.getRandom().nextInt(3);
/* 1187 */     Fox.this.setSitting(true);
/* 1188 */     Fox.this.getNavigation().stop();
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1193 */     Fox.this.setSitting(false);
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1198 */     this.lookTime--;
/* 1199 */     if (this.lookTime <= 0) {
/* 1200 */       this.looksRemaining--;
/* 1201 */       resetLook();
/*      */     } 
/* 1203 */     Fox.this.getLookControl().setLookAt(Fox.this.getX() + this.relX, Fox.this.getEyeY(), Fox.this.getZ() + this.relZ, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */   }
/*      */   
/*      */   private void resetLook() {
/* 1207 */     double rnd = 6.283185307179586D * Fox.this.getRandom().nextDouble();
/* 1208 */     this.relX = Math.cos(rnd);
/* 1209 */     this.relZ = Math.sin(rnd);
/* 1210 */     this.lookTime = adjustedTickDelay(80 + Fox.this.getRandom().nextInt(20));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$PerchAndSearchGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */