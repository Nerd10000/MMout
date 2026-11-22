/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class EndermanLeaveBlockGoal extends Goal {
/*     */   private final EnderMan enderman;
/*     */   
/*     */   public EndermanLeaveBlockGoal(EnderMan enderman) {
/* 535 */     this.enderman = enderman;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 540 */     if (this.enderman.getCarriedBlock() == null)
/* 541 */       return false; 
/* 543 */     if (!((Boolean)getServerLevel((Entity)this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 544 */       return false; 
/* 546 */     return (this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 551 */     RandomSource random = this.enderman.getRandom();
/* 552 */     Level level = this.enderman.level();
/* 554 */     int xt = Mth.floor(this.enderman.getX() - 1.0D + random.nextDouble() * 2.0D);
/* 555 */     int yt = Mth.floor(this.enderman.getY() + random.nextDouble() * 2.0D);
/* 556 */     int zt = Mth.floor(this.enderman.getZ() - 1.0D + random.nextDouble() * 2.0D);
/* 557 */     BlockPos pos = new BlockPos(xt, yt, zt);
/* 558 */     BlockState targetState = level.getBlockState(pos);
/* 559 */     BlockPos below = pos.below();
/* 560 */     BlockState belowState = level.getBlockState(below);
/* 562 */     BlockState carried = this.enderman.getCarriedBlock();
/* 563 */     if (carried == null)
/*     */       return; 
/* 567 */     carried = Block.updateFromNeighbourShapes(carried, (LevelAccessor)this.enderman.level(), pos);
/* 568 */     if (canPlaceBlock(level, pos, carried, targetState, belowState, below)) {
/* 569 */       level.setBlock(pos, carried, 3);
/* 570 */       level.gameEvent((Holder)GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of((Entity)this.enderman, carried));
/* 571 */       this.enderman.setCarriedBlock((BlockState)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canPlaceBlock(Level level, BlockPos pos, BlockState carried, BlockState targetState, BlockState belowState, BlockPos below) {
/* 576 */     return (targetState.isAir() && !belowState.isAir() && !belowState.is(Blocks.BEDROCK) && belowState.isCollisionShapeFullBlock((BlockGetter)level, below) && carried.canSurvive((LevelReader)level, pos) && level
/* 577 */       .getEntities((Entity)this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf((Vec3i)pos))).isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\EnderMan$EndermanLeaveBlockGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */