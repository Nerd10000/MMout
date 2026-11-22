/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ class null extends GroundPathNavigation {
/*     */   null(Warden this$0, Mob mob, Level level) {
/* 604 */     super(mob, level);
/*     */   }
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 607 */     this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/* 608 */     return new PathFinder(this, this.nodeEvaluator, maxVisitedNodes) {
/*     */         protected float distance(Node from, Node to) {
/* 614 */           return from.distanceToXZ(to);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\Warden$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */