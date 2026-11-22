/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ 
/*     */ class FrogPathNavigation extends AmphibiousPathNavigation {
/*     */   public FrogPathNavigation(Frog mob, Level level) {
/* 367 */     super((Mob)mob, level);
/*     */   }
/*     */   
/*     */   public boolean canCutCorner(PathType pathType) {
/* 372 */     return (pathType != PathType.WATER_BORDER && super.canCutCorner(pathType));
/*     */   }
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 377 */     this.nodeEvaluator = (NodeEvaluator)new Frog.FrogNodeEvaluator(true);
/* 378 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\Frog$FrogPathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */