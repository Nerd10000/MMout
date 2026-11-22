/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ 
/*     */ class TurtlePanicGoal extends PanicGoal {
/*     */   public TurtlePanicGoal(Turtle turtle, double speedModifier) {
/* 325 */     super((PathfinderMob)turtle, speedModifier);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 330 */     if (!shouldPanic())
/* 331 */       return false; 
/* 334 */     BlockPos blockPos = lookForWater((BlockGetter)this.mob.level(), (Entity)this.mob, 7);
/* 335 */     if (blockPos != null) {
/* 336 */       this.posX = blockPos.getX();
/* 337 */       this.posY = blockPos.getY();
/* 338 */       this.posZ = blockPos.getZ();
/* 340 */       return true;
/*     */     } 
/* 343 */     return findRandomPosition();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtlePanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */