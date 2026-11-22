/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ class StriderPathNavigation extends GroundPathNavigation {
/*     */   public StriderPathNavigation(Strider mob, Level level) {
/* 494 */     super((Mob)mob, level);
/*     */   }
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 500 */     this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/* 501 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos pos) {
/* 506 */     return (this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Strider$StriderPathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */