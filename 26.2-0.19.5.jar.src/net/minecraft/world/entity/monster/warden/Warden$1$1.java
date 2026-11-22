/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ 
/*     */ class null extends PathFinder {
/*     */   protected float distance(Node from, Node to) {
/* 614 */     return from.distanceToXZ(to);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\Warden$1$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */