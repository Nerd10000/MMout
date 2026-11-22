/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ class TurtlePathNavigation extends AmphibiousPathNavigation {
/*     */   public TurtlePathNavigation(Turtle mob, Level level) {
/* 707 */     super((Mob)mob, level);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos pos) {
/* 712 */     Mob mob = this.mob;
/* 712 */     if (mob instanceof Turtle) {
/* 712 */       Turtle turtle = (Turtle)mob;
/* 713 */       if (turtle.travelPos != null)
/* 714 */         return this.level.getBlockState(pos).is(Blocks.WATER); 
/*     */     } 
/* 718 */     return !this.level.getBlockState(pos.below()).isAir();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtlePathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */