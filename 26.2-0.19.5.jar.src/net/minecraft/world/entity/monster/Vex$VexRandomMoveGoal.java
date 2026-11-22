/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class VexRandomMoveGoal extends Goal {
/*     */   public VexRandomMoveGoal() {
/* 331 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 336 */     return (!Vex.this.getMoveControl().hasWanted() && Vex.access$300(Vex.this).nextInt(reducedTickDelay(7)) == 0);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 341 */     return false;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 346 */     BlockPos boundOrigin = Vex.this.getBoundOrigin();
/* 347 */     if (boundOrigin == null)
/* 349 */       boundOrigin = Vex.this.blockPosition(); 
/* 352 */     for (int attempts = 0; attempts < 3; attempts++) {
/* 353 */       BlockPos testPos = boundOrigin.offset(Vex.access$400(Vex.this).nextInt(15) - 7, Vex.access$500(Vex.this).nextInt(11) - 5, Vex.access$600(Vex.this).nextInt(15) - 7);
/* 354 */       if (Vex.this.level().isEmptyBlock(testPos)) {
/* 355 */         Vex.access$700(Vex.this).setWantedPosition(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 0.25D);
/* 356 */         if (Vex.this.getTarget() == null)
/* 357 */           Vex.this.getLookControl().setLookAt(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 180.0F, 20.0F); 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Vex$VexRandomMoveGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */