/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class RTree<T> {
/*     */   private static final int CHILDREN_PER_NODE = 6;
/*     */   
/*     */   private final Node<T> root;
/*     */   
/*  68 */   private final ThreadLocal<Leaf<T>> lastResult = new ThreadLocal<>();
/*     */   
/*     */   private RTree(Node<T> root) {
/*  71 */     this.root = root;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static abstract class Node<T> {
/*     */     protected final Climate.Parameter[] parameterSpace;
/*     */     
/*     */     protected Node(List<Climate.Parameter> parameterSpace) {
/*  80 */       this.parameterSpace = parameterSpace.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*     */     }
/*     */     
/*     */     protected abstract Climate.RTree.Leaf<T> search(long[] param2ArrayOflong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric);
/*     */     
/*     */     protected long distance(long[] target) {
/*  86 */       long distance = 0L;
/*  87 */       for (int i = 0; i < 7; i++)
/*  88 */         distance += Mth.square(this.parameterSpace[i].distance(target[i])); 
/*  90 */       return distance;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  95 */       return Arrays.toString((Object[])this.parameterSpace);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Leaf<T> extends Node<T> {
/*     */     private final T value;
/*     */     
/*     */     private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/* 103 */       super(parameterPoint.parameterSpace());
/* 104 */       this.value = value;
/*     */     }
/*     */     
/*     */     protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 109 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SubTree<T> extends Node<T> {
/*     */     private final Climate.RTree.Node<T>[] children;
/*     */     
/*     */     public SubTree(List<? extends Climate.RTree.Node<T>> children) {
/* 117 */       this(Climate.RTree.buildParameterSpace(children), children);
/*     */     }
/*     */     
/*     */     public SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 121 */       super(parameterSpace);
/* 122 */       this.children = children.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */     }
/*     */     
/*     */     protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 127 */       long minDistance = (candidate == null) ? Long.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 128 */       Climate.RTree.Leaf<T> closestLeaf = candidate;
/* 130 */       for (Climate.RTree.Node<T> child : this.children) {
/* 131 */         long childDistance = distanceMetric.distance(child, target);
/* 132 */         if (minDistance > childDistance) {
/* 134 */           Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 135 */           long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 136 */           if (minDistance > leafDistance) {
/* 137 */             minDistance = leafDistance;
/* 138 */             closestLeaf = leaf;
/*     */           } 
/*     */         } 
/*     */       } 
/* 142 */       return closestLeaf;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> RTree<T> create(List<Pair<Climate.ParameterPoint, T>> values) {
/* 147 */     if (values.isEmpty())
/* 148 */       throw new IllegalArgumentException("Need at least one value to build the search tree."); 
/* 150 */     int dimensions = ((Climate.ParameterPoint)((Pair)values.get(0)).getFirst()).parameterSpace().size();
/* 151 */     if (dimensions != 7)
/* 152 */       throw new IllegalStateException("Expecting parameter space to be 7, got " + dimensions); 
/* 155 */     List<Leaf<T>> leaves = (List<Leaf<T>>)values.stream().map(p -> new Leaf((Climate.ParameterPoint)p.getFirst(), p.getSecond())).collect(Collectors.toCollection(java.util.ArrayList::new));
/* 157 */     return new RTree<>(build(dimensions, (List)leaves));
/*     */   }
/*     */   
/*     */   private static <T> Node<T> build(int dimensions, List<? extends Node<T>> children) {
/* 161 */     if (children.isEmpty())
/* 162 */       throw new IllegalStateException("Need at least one child to build a node"); 
/* 164 */     if (children.size() == 1)
/* 165 */       return children.get(0); 
/* 167 */     if (children.size() <= 6) {
/* 168 */       children.sort(Comparator.comparingLong(leaf -> {
/*     */               long totalMagnitude = 0L;
/*     */               for (int d = 0; d < dimensions; d++) {
/*     */                 Climate.Parameter parameter = leaf.parameterSpace[d];
/*     */                 totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
/*     */               } 
/*     */               return totalMagnitude;
/*     */             }));
/* 176 */       return new SubTree<>(children);
/*     */     } 
/* 183 */     long minCost = Long.MAX_VALUE;
/* 184 */     int minDimension = -1;
/* 185 */     List<SubTree<T>> minBuckets = null;
/* 187 */     for (int d = 0; d < dimensions; d++) {
/* 188 */       sort(children, dimensions, d, false);
/* 189 */       List<SubTree<T>> buckets = bucketize(children);
/* 191 */       long totalCost = 0L;
/* 192 */       for (SubTree<T> bucket : buckets)
/* 193 */         totalCost += cost(bucket.parameterSpace); 
/* 196 */       if (minCost > totalCost) {
/* 197 */         minCost = totalCost;
/* 198 */         minDimension = d;
/* 199 */         minBuckets = buckets;
/*     */       } 
/*     */     } 
/* 203 */     sort((List)minBuckets, dimensions, minDimension, true);
/* 205 */     return new SubTree<>((List<? extends Node<T>>)minBuckets.stream().map(b -> build(dimensions, Arrays.asList((Node<?>[])b.children))).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   private static <T> void sort(List<? extends Node<T>> children, int dimensions, int dimension, boolean absolute) {
/* 209 */     Comparator<Node<T>> comparator = comparator(dimension, absolute);
/* 210 */     for (int d = 1; d < dimensions; d++)
/* 211 */       comparator = comparator.thenComparing((Comparator)comparator((dimension + d) % dimensions, absolute)); 
/* 213 */     children.sort(comparator);
/*     */   }
/*     */   
/*     */   private static <T> Comparator<Node<T>> comparator(int dimension, boolean absolute) {
/* 217 */     return Comparator.comparingLong(leaf -> {
/*     */           Climate.Parameter parameter = leaf.parameterSpace[dimension];
/*     */           long center = (parameter.min() + parameter.max()) / 2L;
/*     */           return absolute ? Math.abs(center) : center;
/*     */         });
/*     */   }
/*     */   
/*     */   private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> nodes) {
/* 226 */     List<SubTree<T>> buckets = Lists.newArrayList();
/* 228 */     List<Node<T>> children = Lists.newArrayList();
/* 229 */     int expectedChildrenCount = (int)Math.pow(6.0D, Math.floor(Math.log(nodes.size() - 0.01D) / Math.log(6.0D)));
/* 230 */     for (Node<T> child : nodes) {
/* 231 */       children.add(child);
/* 232 */       if (children.size() >= expectedChildrenCount) {
/* 233 */         buckets.add(new SubTree<>(children));
/* 234 */         children = Lists.newArrayList();
/*     */       } 
/*     */     } 
/* 237 */     if (!children.isEmpty())
/* 238 */       buckets.add(new SubTree<>(children)); 
/* 240 */     return buckets;
/*     */   }
/*     */   
/*     */   private static long cost(Climate.Parameter[] parameterSpace) {
/* 244 */     long result = 0L;
/* 245 */     for (Climate.Parameter parameter : parameterSpace)
/* 246 */       result += Math.abs(parameter.max() - parameter.min()); 
/* 248 */     return result;
/*     */   }
/*     */   
/*     */   private static <T> List<Climate.Parameter> buildParameterSpace(List<? extends Node<T>> children) {
/* 252 */     if (children.isEmpty())
/* 253 */       throw new IllegalArgumentException("SubTree needs at least one child"); 
/* 255 */     int dimensions = 7;
/* 256 */     List<Climate.Parameter> bounds = Lists.newArrayList();
/* 257 */     for (int d = 0; d < 7; d++)
/* 258 */       bounds.add((Climate.Parameter)null); 
/* 260 */     for (Node<T> child : children) {
/* 261 */       for (int i = 0; i < 7; i++)
/* 262 */         bounds.set(i, child.parameterSpace[i].span(bounds.get(i))); 
/*     */     } 
/* 265 */     return bounds;
/*     */   }
/*     */   
/*     */   public T search(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 269 */     long[] targetArray = target.toParameterArray();
/* 270 */     Leaf<T> leaf = this.root.search(targetArray, this.lastResult.get(), distanceMetric);
/* 271 */     this.lastResult.set(leaf);
/* 272 */     return leaf.value;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$RTree.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */