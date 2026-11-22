/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class PandaRollGoal extends Goal {
/*     */   private final Panda panda;
/*     */   
/*     */   public PandaRollGoal(Panda panda) {
/* 887 */     this.panda = panda;
/* 888 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 893 */     if ((!this.panda.isBaby() && !this.panda.isPlayful()) || !this.panda.onGround())
/* 894 */       return false; 
/* 897 */     if (!this.panda.canPerformAction())
/* 898 */       return false; 
/* 901 */     float angle = this.panda.getYRot() * 0.017453292F;
/* 902 */     float xDir = -Mth.sin(angle);
/* 903 */     float zDir = Mth.cos(angle);
/* 904 */     int xStep = (Math.abs(xDir) > 0.5D) ? Mth.sign(xDir) : 0;
/* 905 */     int zStep = (Math.abs(zDir) > 0.5D) ? Mth.sign(zDir) : 0;
/* 907 */     if (this.panda.level().getBlockState(this.panda.blockPosition().offset(xStep, -1, zStep)).isAir())
/* 908 */       return true; 
/* 911 */     if (this.panda.isPlayful() && Panda.access$000(this.panda).nextInt(reducedTickDelay(60)) == 1)
/* 912 */       return true; 
/* 915 */     return (Panda.access$100(this.panda).nextInt(reducedTickDelay(500)) == 1);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 920 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 925 */     this.panda.roll(true);
/*     */   }
/*     */   
/*     */   public boolean isInterruptable() {
/* 930 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaRollGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */