/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ final class SubTree<T> extends Climate.RTree.Node<T> {
/*     */   private final Climate.RTree.Node<T>[] children;
/*     */   
/*     */   public SubTree(List<? extends Climate.RTree.Node<T>> children) {
/* 117 */     this(Climate.RTree.buildParameterSpace(children), children);
/*     */   }
/*     */   
/*     */   public SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 121 */     super(parameterSpace);
/* 122 */     this.children = children.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */   }
/*     */   
/*     */   protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 127 */     long minDistance = (candidate == null) ? Long.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 128 */     Climate.RTree.Leaf<T> closestLeaf = candidate;
/* 130 */     for (Climate.RTree.Node<T> child : this.children) {
/* 131 */       long childDistance = distanceMetric.distance(child, target);
/* 132 */       if (minDistance > childDistance) {
/* 134 */         Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 135 */         long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 136 */         if (minDistance > leafDistance) {
/* 137 */           minDistance = leafDistance;
/* 138 */           closestLeaf = leaf;
/*     */         } 
/*     */       } 
/*     */     } 
/* 142 */     return closestLeaf;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$RTree$SubTree.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */