/*     */ package net.minecraft.world.entity.animal.parrot;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
/*     */   public ParrotWanderGoal(PathfinderMob mob, double speedModifier) {
/* 520 */     super(mob, speedModifier);
/*     */   }
/*     */   
/*     */   protected Vec3 getPosition() {
/* 525 */     Vec3 pos = null;
/* 526 */     if (this.mob.isInWater())
/* 527 */       pos = LandRandomPos.getPos(this.mob, 15, 15); 
/* 529 */     if (this.mob.getRandom().nextFloat() >= this.probability)
/* 530 */       pos = getTreePos(); 
/* 532 */     return (pos == null) ? super.getPosition() : pos;
/*     */   }
/*     */   
/*     */   private Vec3 getTreePos() {
/* 536 */     BlockPos mobPos = this.mob.blockPosition();
/* 538 */     BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
/* 539 */     BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/* 540 */     Iterable<BlockPos> between = BlockPos.betweenClosed(
/* 541 */         Mth.floor(this.mob.getX() - 3.0D), 
/* 542 */         Mth.floor(this.mob.getY() - 6.0D), 
/* 543 */         Mth.floor(this.mob.getZ() - 3.0D), 
/* 544 */         Mth.floor(this.mob.getX() + 3.0D), 
/* 545 */         Mth.floor(this.mob.getY() + 6.0D), 
/* 546 */         Mth.floor(this.mob.getZ() + 3.0D));
/* 549 */     for (BlockPos pos : between) {
/* 550 */       if (mobPos.equals(pos))
/*     */         continue; 
/* 554 */       BlockState state = this.mob.level().getBlockState((BlockPos)belowPos.setWithOffset((Vec3i)pos, Direction.DOWN));
/* 555 */       boolean canSitOn = (state.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || state.is(BlockTags.LOGS));
/* 556 */       if (canSitOn && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock((BlockPos)abovePos.setWithOffset((Vec3i)pos, Direction.UP)))
/* 557 */         return Vec3.atBottomCenterOf((Vec3i)pos); 
/*     */     } 
/* 561 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\parrot\Parrot$ParrotWanderGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */