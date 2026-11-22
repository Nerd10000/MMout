/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ 
/*      */ class PandaLieOnBackGoal extends Goal {
/*      */   private final Panda panda;
/*      */   
/*      */   private int cooldown;
/*      */   
/*      */   public PandaLieOnBackGoal(Panda panda) {
/* 1110 */     this.panda = panda;
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1115 */     return (this.cooldown < this.panda.tickCount && this.panda.isLazy() && this.panda.canPerformAction() && Panda.access$800(this.panda).nextInt(reducedTickDelay(400)) == 1);
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1120 */     if (this.panda.isInWater() || (!this.panda.isLazy() && Panda.access$900(this.panda).nextInt(reducedTickDelay(600)) == 1))
/* 1121 */       return false; 
/* 1124 */     if (Panda.access$1000(this.panda).nextInt(reducedTickDelay(2000)) == 1)
/* 1125 */       return false; 
/* 1128 */     return true;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1133 */     this.panda.setOnBack(true);
/* 1134 */     this.cooldown = 0;
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1139 */     this.panda.setOnBack(false);
/* 1140 */     this.cooldown = this.panda.tickCount + 200;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaLieOnBackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */