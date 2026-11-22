/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
/*     */   public ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
/* 596 */     super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
/*     */   }
/*     */   
/*     */   public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
/* 601 */     level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.access$000(Zombie.this).nextFloat() * 0.2F);
/*     */   }
/*     */   
/*     */   public void playBreakSound(Level level, BlockPos pos) {
/* 606 */     level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
/*     */   }
/*     */   
/*     */   public double acceptedDistance() {
/* 611 */     return 1.14D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Zombie$ZombieAttackTurtleEggGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */