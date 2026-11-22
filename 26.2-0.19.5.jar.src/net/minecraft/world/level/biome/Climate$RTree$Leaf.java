/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ final class Leaf<T> extends Climate.RTree.Node<T> {
/*     */   private final T value;
/*     */   
/*     */   private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/* 103 */     super(parameterPoint.parameterSpace());
/* 104 */     this.value = value;
/*     */   }
/*     */   
/*     */   protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 109 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$RTree$Leaf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */