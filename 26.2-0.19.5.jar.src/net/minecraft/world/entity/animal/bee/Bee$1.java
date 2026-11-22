/*     */ package net.minecraft.world.entity.animal.bee;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null extends FlyingPathNavigation {
/*     */   null(Mob mob, Level level) {
/* 604 */     super(mob, level);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos pos) {
/* 607 */     return !this.level.getBlockState(pos.below()).isAir();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 612 */     if (Bee.this.beePollinateGoal.isPollinating())
/*     */       return; 
/* 616 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */