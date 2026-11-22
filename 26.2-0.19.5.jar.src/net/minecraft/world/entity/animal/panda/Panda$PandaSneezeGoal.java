/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class PandaSneezeGoal extends Goal {
/*     */   private final Panda panda;
/*     */   
/*     */   public PandaSneezeGoal(Panda panda) {
/* 939 */     this.panda = panda;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 944 */     if (!this.panda.isBaby() || !this.panda.canPerformAction())
/* 945 */       return false; 
/* 948 */     if (this.panda.isWeak() && Panda.access$200(this.panda).nextInt(reducedTickDelay(500)) == 1)
/* 949 */       return true; 
/* 952 */     return (Panda.access$300(this.panda).nextInt(reducedTickDelay(6000)) == 1);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 957 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 962 */     this.panda.sneeze(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaSneezeGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */