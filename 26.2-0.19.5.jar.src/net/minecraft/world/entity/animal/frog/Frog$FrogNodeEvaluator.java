/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ 
/*     */ class FrogNodeEvaluator extends AmphibiousNodeEvaluator {
/* 383 */   private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   public FrogNodeEvaluator(boolean prefersShallowSwimming) {
/* 386 */     super(prefersShallowSwimming);
/*     */   }
/*     */   
/*     */   public Node getStart() {
/* 391 */     if (!this.mob.isInWater())
/* 392 */       return super.getStart(); 
/* 394 */     return getStartNode(new BlockPos(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY), Mth.floor((this.mob.getBoundingBox()).minZ)));
/*     */   }
/*     */   
/*     */   public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 399 */     this.belowPos.set(x, y - 1, z);
/* 401 */     BlockState belowState = context.getBlockState((BlockPos)this.belowPos);
/* 402 */     if (belowState.is(BlockTags.FROG_PREFER_JUMP_TO))
/* 403 */       return PathType.OPEN; 
/* 406 */     return super.getPathType(context, x, y, z);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\Frog$FrogNodeEvaluator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */