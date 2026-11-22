/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ 
/*     */ class CreakingPathNavigation extends GroundPathNavigation {
/*     */   public CreakingPathNavigation(Creaking mob, Level level) {
/* 603 */     super((Mob)mob, level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 608 */     if (Creaking.this.canMove())
/* 609 */       super.tick(); 
/*     */   }
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 615 */     this.nodeEvaluator = (NodeEvaluator)new Creaking.HomeNodeEvaluator(Creaking.this);
/* 616 */     this.nodeEvaluator.setCanPassDoors(true);
/* 617 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$CreakingPathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */