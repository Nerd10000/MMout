/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.DensityFunctions;
/*     */ 
/*     */ public class Climate {
/*     */   private static final boolean DEBUG_SLOW_BIOME_SEARCH = false;
/*     */   
/*     */   private static final float QUANTIZATION_FACTOR = 10000.0F;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static final int PARAMETER_COUNT = 7;
/*     */   
/*     */   public static TargetPoint target(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness) {
/*  34 */     return new TargetPoint(quantizeCoord(temperature), quantizeCoord(humidity), quantizeCoord(continentalness), quantizeCoord(erosion), quantizeCoord(depth), quantizeCoord(weirdness));
/*     */   }
/*     */   
/*     */   public static ParameterPoint parameters(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness, float offset) {
/*  38 */     return new ParameterPoint(Parameter.point(temperature), Parameter.point(humidity), Parameter.point(continentalness), Parameter.point(erosion), Parameter.point(depth), Parameter.point(weirdness), quantizeCoord(offset));
/*     */   }
/*     */   
/*     */   public static ParameterPoint parameters(Parameter temperature, Parameter humidity, Parameter continentalness, Parameter erosion, Parameter depth, Parameter weirdness, float offset) {
/*  42 */     return new ParameterPoint(temperature, humidity, continentalness, erosion, depth, weirdness, quantizeCoord(offset));
/*     */   }
/*     */   
/*     */   public static long quantizeCoord(float coord) {
/*  46 */     return (long)(coord * 10000.0F);
/*     */   }
/*     */   
/*     */   public static float unquantizeCoord(long coord) {
/*  50 */     return (float)coord / 10000.0F;
/*     */   }
/*     */   
/*     */   protected static final class RTree<T> {
/*     */     private static final int CHILDREN_PER_NODE = 6;
/*     */     
/*     */     private final Node<T> root;
/*     */     
/*  68 */     private final ThreadLocal<Leaf<T>> lastResult = new ThreadLocal<>();
/*     */     
/*     */     private RTree(Node<T> root) {
/*  71 */       this.root = root;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static abstract class Node<T> {
/*     */       protected final Climate.Parameter[] parameterSpace;
/*     */       
/*     */       protected Node(List<Climate.Parameter> parameterSpace) {
/*  80 */         this.parameterSpace = parameterSpace.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*     */       }
/*     */       
/*     */       protected abstract Climate.RTree.Leaf<T> search(long[] param2ArrayOflong, Climate.RTree.Leaf<T> param2Leaf, Climate.DistanceMetric<T> param2DistanceMetric);
/*     */       
/*     */       protected long distance(long[] target) {
/*  86 */         long distance = 0L;
/*  87 */         for (int i = 0; i < 7; i++)
/*  88 */           distance += Mth.square(this.parameterSpace[i].distance(target[i])); 
/*  90 */         return distance;
/*     */       }
/*     */       
/*     */       public String toString() {
/*  95 */         return Arrays.toString((Object[])this.parameterSpace);
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class Leaf<T> extends Node<T> {
/*     */       private final T value;
/*     */       
/*     */       private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/* 103 */         super(parameterPoint.parameterSpace());
/* 104 */         this.value = value;
/*     */       }
/*     */       
/*     */       protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 109 */         return this;
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class SubTree<T> extends Node<T> {
/*     */       private final Climate.RTree.Node<T>[] children;
/*     */       
/*     */       public SubTree(List<? extends Climate.RTree.Node<T>> children) {
/* 117 */         this(Climate.RTree.buildParameterSpace(children), children);
/*     */       }
/*     */       
/*     */       public SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/* 121 */         super(parameterSpace);
/* 122 */         this.children = children.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */       }
/*     */       
/*     */       protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/* 127 */         long minDistance = (candidate == null) ? Long.MAX_VALUE : distanceMetric.distance(candidate, target);
/* 128 */         Climate.RTree.Leaf<T> closestLeaf = candidate;
/* 130 */         for (Climate.RTree.Node<T> child : this.children) {
/* 131 */           long childDistance = distanceMetric.distance(child, target);
/* 132 */           if (minDistance > childDistance) {
/* 134 */             Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/* 135 */             long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/* 136 */             if (minDistance > leafDistance) {
/* 137 */               minDistance = leafDistance;
/* 138 */               closestLeaf = leaf;
/*     */             } 
/*     */           } 
/*     */         } 
/* 142 */         return closestLeaf;
/*     */       }
/*     */     }
/*     */     
/*     */     public static <T> RTree<T> create(List<Pair<Climate.ParameterPoint, T>> values) {
/* 147 */       if (values.isEmpty())
/* 148 */         throw new IllegalArgumentException("Need at least one value to build the search tree."); 
/* 150 */       int dimensions = ((Climate.ParameterPoint)((Pair)values.get(0)).getFirst()).parameterSpace().size();
/* 151 */       if (dimensions != 7)
/* 152 */         throw new IllegalStateException("Expecting parameter space to be 7, got " + dimensions); 
/* 155 */       List<Leaf<T>> leaves = (List<Leaf<T>>)values.stream().map(p -> new Leaf((Climate.ParameterPoint)p.getFirst(), p.getSecond())).collect(Collectors.toCollection(java.util.ArrayList::new));
/* 157 */       return new RTree<>(build(dimensions, (List)leaves));
/*     */     }
/*     */     
/*     */     private static <T> Node<T> build(int dimensions, List<? extends Node<T>> children) {
/* 161 */       if (children.isEmpty())
/* 162 */         throw new IllegalStateException("Need at least one child to build a node"); 
/* 164 */       if (children.size() == 1)
/* 165 */         return children.get(0); 
/* 167 */       if (children.size() <= 6) {
/* 168 */         children.sort(Comparator.comparingLong(leaf -> {
/*     */                 long totalMagnitude = 0L;
/*     */                 for (int d = 0; d < dimensions; d++) {
/*     */                   Climate.Parameter parameter = leaf.parameterSpace[d];
/*     */                   totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
/*     */                 } 
/*     */                 return totalMagnitude;
/*     */               }));
/* 176 */         return new SubTree<>(children);
/*     */       } 
/* 183 */       long minCost = Long.MAX_VALUE;
/* 184 */       int minDimension = -1;
/* 185 */       List<SubTree<T>> minBuckets = null;
/* 187 */       for (int d = 0; d < dimensions; d++) {
/* 188 */         sort(children, dimensions, d, false);
/* 189 */         List<SubTree<T>> buckets = bucketize(children);
/* 191 */         long totalCost = 0L;
/* 192 */         for (SubTree<T> bucket : buckets)
/* 193 */           totalCost += cost(bucket.parameterSpace); 
/* 196 */         if (minCost > totalCost) {
/* 197 */           minCost = totalCost;
/* 198 */           minDimension = d;
/* 199 */           minBuckets = buckets;
/*     */         } 
/*     */       } 
/* 203 */       sort((List)minBuckets, dimensions, minDimension, true);
/* 205 */       return new SubTree<>((List<? extends Node<T>>)minBuckets.stream().map(b -> build(dimensions, Arrays.asList((Node<?>[])b.children))).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     private static <T> void sort(List<? extends Node<T>> children, int dimensions, int dimension, boolean absolute) {
/* 209 */       Comparator<Node<T>> comparator = comparator(dimension, absolute);
/* 210 */       for (int d = 1; d < dimensions; d++)
/* 211 */         comparator = comparator.thenComparing((Comparator)comparator((dimension + d) % dimensions, absolute)); 
/* 213 */       children.sort(comparator);
/*     */     }
/*     */     
/*     */     private static <T> Comparator<Node<T>> comparator(int dimension, boolean absolute) {
/* 217 */       return Comparator.comparingLong(leaf -> {
/*     */             Climate.Parameter parameter = leaf.parameterSpace[dimension];
/*     */             long center = (parameter.min() + parameter.max()) / 2L;
/*     */             return absolute ? Math.abs(center) : center;
/*     */           });
/*     */     }
/*     */     
/*     */     private static <T> List<SubTree<T>> bucketize(List<? extends Node<T>> nodes) {
/* 226 */       List<SubTree<T>> buckets = Lists.newArrayList();
/* 228 */       List<Node<T>> children = Lists.newArrayList();
/* 229 */       int expectedChildrenCount = (int)Math.pow(6.0D, Math.floor(Math.log(nodes.size() - 0.01D) / Math.log(6.0D)));
/* 230 */       for (Node<T> child : nodes) {
/* 231 */         children.add(child);
/* 232 */         if (children.size() >= expectedChildrenCount) {
/* 233 */           buckets.add(new SubTree<>(children));
/* 234 */           children = Lists.newArrayList();
/*     */         } 
/*     */       } 
/* 237 */       if (!children.isEmpty())
/* 238 */         buckets.add(new SubTree<>(children)); 
/* 240 */       return buckets;
/*     */     }
/*     */     
/*     */     private static long cost(Climate.Parameter[] parameterSpace) {
/* 244 */       long result = 0L;
/* 245 */       for (Climate.Parameter parameter : parameterSpace)
/* 246 */         result += Math.abs(parameter.max() - parameter.min()); 
/* 248 */       return result;
/*     */     }
/*     */     
/*     */     private static <T> List<Climate.Parameter> buildParameterSpace(List<? extends Node<T>> children) {
/* 252 */       if (children.isEmpty())
/* 253 */         throw new IllegalArgumentException("SubTree needs at least one child"); 
/* 255 */       int dimensions = 7;
/* 256 */       List<Climate.Parameter> bounds = Lists.newArrayList();
/* 257 */       for (int d = 0; d < 7; d++)
/* 258 */         bounds.add((Climate.Parameter)null); 
/* 260 */       for (Node<T> child : children) {
/* 261 */         for (int i = 0; i < 7; i++)
/* 262 */           bounds.set(i, child.parameterSpace[i].span(bounds.get(i))); 
/*     */       } 
/* 265 */       return bounds;
/*     */     }
/*     */     
/*     */     public T search(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 269 */       long[] targetArray = target.toParameterArray();
/* 270 */       Leaf<T> leaf = this.root.search(targetArray, this.lastResult.get(), distanceMetric);
/* 271 */       this.lastResult.set(leaf);
/* 272 */       return leaf.value;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static abstract class Node<T> {
/*     */     protected final Climate.Parameter[] parameterSpace;
/*     */     
/*     */     protected Node(List<Climate.Parameter> parameterSpace) {
/*     */       this.parameterSpace = parameterSpace.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*     */     }
/*     */     
/*     */     protected abstract Climate.RTree.Leaf<T> search(long[] param1ArrayOflong, Climate.RTree.Leaf<T> param1Leaf, Climate.DistanceMetric<T> param1DistanceMetric);
/*     */     
/*     */     protected long distance(long[] target) {
/*     */       long distance = 0L;
/*     */       for (int i = 0; i < 7; i++)
/*     */         distance += Mth.square(this.parameterSpace[i].distance(target[i])); 
/*     */       return distance;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return Arrays.toString((Object[])this.parameterSpace);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Leaf<T> extends RTree.Node<T> {
/*     */     private final T value;
/*     */     
/*     */     private Leaf(Climate.ParameterPoint parameterPoint, T value) {
/*     */       super(parameterPoint.parameterSpace());
/*     */       this.value = value;
/*     */     }
/*     */     
/*     */     protected Leaf<T> search(long[] target, Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/*     */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SubTree<T> extends RTree.Node<T> {
/*     */     private final Climate.RTree.Node<T>[] children;
/*     */     
/*     */     public SubTree(List<? extends Climate.RTree.Node<T>> children) {
/*     */       this(Climate.RTree.buildParameterSpace(children), children);
/*     */     }
/*     */     
/*     */     public SubTree(List<Climate.Parameter> parameterSpace, List<? extends Climate.RTree.Node<T>> children) {
/*     */       super(parameterSpace);
/*     */       this.children = children.<Climate.RTree.Node<T>>toArray((Climate.RTree.Node<T>[])new Climate.RTree.Node[0]);
/*     */     }
/*     */     
/*     */     protected Climate.RTree.Leaf<T> search(long[] target, Climate.RTree.Leaf<T> candidate, Climate.DistanceMetric<T> distanceMetric) {
/*     */       long minDistance = (candidate == null) ? Long.MAX_VALUE : distanceMetric.distance(candidate, target);
/*     */       Climate.RTree.Leaf<T> closestLeaf = candidate;
/*     */       for (Climate.RTree.Node<T> child : this.children) {
/*     */         long childDistance = distanceMetric.distance(child, target);
/*     */         if (minDistance > childDistance) {
/*     */           Climate.RTree.Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
/*     */           long leafDistance = (child == leaf) ? childDistance : distanceMetric.distance(leaf, target);
/*     */           if (minDistance > leafDistance) {
/*     */             minDistance = leafDistance;
/*     */             closestLeaf = leaf;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       return closestLeaf;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ParameterList<T> {
/*     */     private final List<Pair<Climate.ParameterPoint, T>> values;
/*     */     
/*     */     private final Climate.RTree<T> index;
/*     */     
/*     */     public static <T> Codec<ParameterList<T>> codec(MapCodec<T> valueCodec) {
/* 281 */       return ExtraCodecs.nonEmptyList(RecordCodecBuilder.create(i -> i.group((App)Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), (App)valueCodec.forGetter(Pair::getSecond)).apply((Applicative)i, Pair::of))
/*     */           
/* 284 */           .listOf()).xmap(ParameterList::new, ParameterList::values);
/*     */     }
/*     */     
/*     */     public ParameterList(List<Pair<Climate.ParameterPoint, T>> values) {
/* 288 */       this.values = values;
/* 289 */       this.index = Climate.RTree.create(values);
/*     */     }
/*     */     
/*     */     public List<Pair<Climate.ParameterPoint, T>> values() {
/* 293 */       return this.values;
/*     */     }
/*     */     
/*     */     public T findValue(Climate.TargetPoint target) {
/* 300 */       return findValueIndex(target);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     public T findValueBruteForce(Climate.TargetPoint target) {
/* 305 */       Iterator<Pair<Climate.ParameterPoint, T>> iterator = values().iterator();
/* 308 */       Pair<Climate.ParameterPoint, T> first = iterator.next();
/* 309 */       long bestFitness = ((Climate.ParameterPoint)first.getFirst()).fitness(target);
/* 310 */       T best = (T)first.getSecond();
/* 312 */       while (iterator.hasNext()) {
/* 313 */         Pair<Climate.ParameterPoint, T> parameter = iterator.next();
/* 314 */         long fitness = ((Climate.ParameterPoint)parameter.getFirst()).fitness(target);
/* 315 */         if (fitness < bestFitness) {
/* 316 */           bestFitness = fitness;
/* 317 */           best = (T)parameter.getSecond();
/*     */         } 
/*     */       } 
/* 320 */       return best;
/*     */     }
/*     */     
/*     */     public T findValueIndex(Climate.TargetPoint target) {
/* 324 */       return findValueIndex(target, Climate.RTree.Node::distance);
/*     */     }
/*     */     
/*     */     protected T findValueIndex(Climate.TargetPoint target, Climate.DistanceMetric<T> distanceMetric) {
/* 328 */       return this.index.search(target, distanceMetric);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TargetPoint extends Record {
/*     */     private final long temperature;
/*     */     
/*     */     private final long humidity;
/*     */     
/*     */     private final long continentalness;
/*     */     
/*     */     private final long erosion;
/*     */     
/*     */     private final long depth;
/*     */     
/*     */     private final long weirdness;
/*     */     
/*     */     public TargetPoint(long temperature, long humidity, long continentalness, long erosion, long depth, long weirdness) {
/* 334 */       this.temperature = temperature;
/* 334 */       this.humidity = humidity;
/* 334 */       this.continentalness = continentalness;
/* 334 */       this.erosion = erosion;
/* 334 */       this.depth = depth;
/* 334 */       this.weirdness = weirdness;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #334	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #334	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #334	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long temperature() {
/* 334 */       return this.temperature;
/*     */     }
/*     */     
/*     */     public long humidity() {
/* 334 */       return this.humidity;
/*     */     }
/*     */     
/*     */     public long continentalness() {
/* 334 */       return this.continentalness;
/*     */     }
/*     */     
/*     */     public long erosion() {
/* 334 */       return this.erosion;
/*     */     }
/*     */     
/*     */     public long depth() {
/* 334 */       return this.depth;
/*     */     }
/*     */     
/*     */     public long weirdness() {
/* 334 */       return this.weirdness;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     long[] toParameterArray() {
/* 338 */       return new long[] { this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, 0L };
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ParameterPoint extends Record {
/*     */     private final Climate.Parameter temperature;
/*     */     
/*     */     private final Climate.Parameter humidity;
/*     */     
/*     */     private final Climate.Parameter continentalness;
/*     */     
/*     */     private final Climate.Parameter erosion;
/*     */     
/*     */     private final Climate.Parameter depth;
/*     */     
/*     */     private final Climate.Parameter weirdness;
/*     */     
/*     */     private final long offset;
/*     */     
/*     */     public static final Codec<ParameterPoint> CODEC;
/*     */     
/*     */     public ParameterPoint(Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, Climate.Parameter weirdness, long offset) {
/* 342 */       this.temperature = temperature;
/* 342 */       this.humidity = humidity;
/* 342 */       this.continentalness = continentalness;
/* 342 */       this.erosion = erosion;
/* 342 */       this.depth = depth;
/* 342 */       this.weirdness = weirdness;
/* 342 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #342	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Climate.Parameter temperature() {
/* 342 */       return this.temperature;
/*     */     }
/*     */     
/*     */     public Climate.Parameter humidity() {
/* 342 */       return this.humidity;
/*     */     }
/*     */     
/*     */     public Climate.Parameter continentalness() {
/* 342 */       return this.continentalness;
/*     */     }
/*     */     
/*     */     public Climate.Parameter erosion() {
/* 342 */       return this.erosion;
/*     */     }
/*     */     
/*     */     public Climate.Parameter depth() {
/* 342 */       return this.depth;
/*     */     }
/*     */     
/*     */     public Climate.Parameter weirdness() {
/* 342 */       return this.weirdness;
/*     */     }
/*     */     
/*     */     public long offset() {
/* 342 */       return this.offset;
/*     */     }
/*     */     
/*     */     static {
/* 351 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Climate.Parameter.CODEC.fieldOf("temperature").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("humidity").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("continentalness").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("erosion").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("depth").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("weirdness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("offset").xmap(Climate::quantizeCoord, Climate::unquantizeCoord).forGetter(())).apply((Applicative)i, ParameterPoint::new));
/*     */     }
/*     */     
/*     */     private long fitness(Climate.TargetPoint target) {
/* 362 */       return Mth.square(this.temperature.distance(target.temperature)) + 
/* 363 */         Mth.square(this.humidity.distance(target.humidity)) + 
/* 364 */         Mth.square(this.continentalness.distance(target.continentalness)) + 
/* 365 */         Mth.square(this.erosion.distance(target.erosion)) + 
/* 366 */         Mth.square(this.depth.distance(target.depth)) + 
/* 367 */         Mth.square(this.weirdness.distance(target.weirdness)) + 
/* 368 */         Mth.square(this.offset);
/*     */     }
/*     */     
/*     */     List<Climate.Parameter> parameterSpace() {
/* 374 */       return (List<Climate.Parameter>)ImmutableList.of(this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, new Climate.Parameter(this.offset, this.offset));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Parameter extends Record {
/*     */     private final long min;
/*     */     
/*     */     private final long max;
/*     */     
/*     */     public static final Codec<Parameter> CODEC;
/*     */     
/*     */     public Parameter(long min, long max) {
/* 386 */       this.min = min;
/* 386 */       this.max = max;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Parameter;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #386	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Parameter;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #386	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long min() {
/* 386 */       return this.min;
/*     */     }
/*     */     
/*     */     public long max() {
/* 386 */       return this.max;
/*     */     }
/*     */     
/*     */     static {
/* 387 */       CODEC = ExtraCodecs.intervalCodec(Codec.floatRange(-2.0F, 2.0F), "min", "max", (min, max) -> (min.compareTo(max) > 0) ? DataResult.error(()) : DataResult.success(new Parameter(Climate.quantizeCoord(min.floatValue()), Climate.quantizeCoord(max.floatValue()))), p -> Float.valueOf(Climate.unquantizeCoord(p.min())), p -> Float.valueOf(Climate.unquantizeCoord(p.max())));
/*     */     }
/*     */     
/*     */     public static Parameter point(float min) {
/* 395 */       return span(min, min);
/*     */     }
/*     */     
/*     */     public static Parameter span(float min, float max) {
/* 399 */       if (min > max)
/* 400 */         throw new IllegalArgumentException("min > max: " + min + " " + max); 
/* 402 */       return new Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
/*     */     }
/*     */     
/*     */     public static Parameter span(Parameter min, Parameter max) {
/* 406 */       if (min.min() > max.max())
/* 407 */         throw new IllegalArgumentException("min > max: " + String.valueOf(min) + " " + String.valueOf(max)); 
/* 409 */       return new Parameter(min.min(), max.max());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 414 */       return (this.min == this.max) ? String.format(Locale.ROOT, "%d", new Object[] { Long.valueOf(this.min) }) : String.format(Locale.ROOT, "[%d-%d]", new Object[] { Long.valueOf(this.min), Long.valueOf(this.max) });
/*     */     }
/*     */     
/*     */     public long distance(long target) {
/* 418 */       long above = target - this.max;
/* 419 */       long below = this.min - target;
/* 420 */       if (above > 0L)
/* 421 */         return above; 
/* 423 */       return Math.max(below, 0L);
/*     */     }
/*     */     
/*     */     public long distance(Parameter target) {
/* 427 */       long above = target.min() - this.max;
/* 428 */       long below = this.min - target.max();
/* 430 */       if (above > 0L)
/* 431 */         return above; 
/* 433 */       return Math.max(below, 0L);
/*     */     }
/*     */     
/*     */     public Parameter span(Parameter other) {
/* 437 */       return (other == null) ? this : new Parameter(Math.min(this.min, other.min()), Math.max(this.max, other.max()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Sampler empty() {
/* 442 */     DensityFunction zero = DensityFunctions.zero();
/* 443 */     return new Sampler(zero, zero, zero, zero, zero, zero, List.of());
/*     */   }
/*     */   
/*     */   public static final class Sampler extends Record {
/*     */     private final DensityFunction temperature;
/*     */     
/*     */     private final DensityFunction humidity;
/*     */     
/*     */     private final DensityFunction continentalness;
/*     */     
/*     */     private final DensityFunction erosion;
/*     */     
/*     */     private final DensityFunction depth;
/*     */     
/*     */     private final DensityFunction weirdness;
/*     */     
/*     */     private final List<Climate.ParameterPoint> spawnTarget;
/*     */     
/*     */     public Sampler(DensityFunction temperature, DensityFunction humidity, DensityFunction continentalness, DensityFunction erosion, DensityFunction depth, DensityFunction weirdness, List<Climate.ParameterPoint> spawnTarget) {
/* 446 */       this.temperature = temperature;
/* 446 */       this.humidity = humidity;
/* 446 */       this.continentalness = continentalness;
/* 446 */       this.erosion = erosion;
/* 446 */       this.depth = depth;
/* 446 */       this.weirdness = weirdness;
/* 446 */       this.spawnTarget = spawnTarget;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$Sampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #446	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Sampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #446	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Sampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #446	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DensityFunction temperature() {
/* 446 */       return this.temperature;
/*     */     }
/*     */     
/*     */     public DensityFunction humidity() {
/* 446 */       return this.humidity;
/*     */     }
/*     */     
/*     */     public DensityFunction continentalness() {
/* 446 */       return this.continentalness;
/*     */     }
/*     */     
/*     */     public DensityFunction erosion() {
/* 446 */       return this.erosion;
/*     */     }
/*     */     
/*     */     public DensityFunction depth() {
/* 446 */       return this.depth;
/*     */     }
/*     */     
/*     */     public DensityFunction weirdness() {
/* 446 */       return this.weirdness;
/*     */     }
/*     */     
/*     */     public List<Climate.ParameterPoint> spawnTarget() {
/* 446 */       return this.spawnTarget;
/*     */     }
/*     */     
/*     */     public Climate.TargetPoint sample(int quartX, int quartY, int quartZ) {
/* 456 */       int blockX = QuartPos.toBlock(quartX);
/* 457 */       int blockY = QuartPos.toBlock(quartY);
/* 458 */       int blockZ = QuartPos.toBlock(quartZ);
/* 460 */       DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(blockX, blockY, blockZ);
/* 462 */       return Climate.target(
/* 463 */           (float)this.temperature.compute((DensityFunction.FunctionContext)context), 
/* 464 */           (float)this.humidity.compute((DensityFunction.FunctionContext)context), 
/* 465 */           (float)this.continentalness.compute((DensityFunction.FunctionContext)context), 
/* 466 */           (float)this.erosion.compute((DensityFunction.FunctionContext)context), 
/* 467 */           (float)this.depth.compute((DensityFunction.FunctionContext)context), 
/* 468 */           (float)this.weirdness.compute((DensityFunction.FunctionContext)context));
/*     */     }
/*     */     
/*     */     public BlockPos findSpawnPosition() {
/* 473 */       if (this.spawnTarget.isEmpty())
/* 474 */         return BlockPos.ZERO; 
/* 476 */       return Climate.findSpawnPosition(this.spawnTarget, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpawnFinder {
/*     */     private static final long MAX_RADIUS = 2048L;
/*     */     
/*     */     private Result result;
/*     */     
/*     */     private static final class Result extends Record {
/*     */       private final BlockPos location;
/*     */       
/*     */       private final long fitness;
/*     */       
/*     */       private Result(BlockPos location, long fitness) {
/* 483 */         this.location = location;
/* 483 */         this.fitness = fitness;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #483	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #483	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #483	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public BlockPos location() {
/* 483 */         return this.location;
/*     */       }
/*     */       
/*     */       public long fitness() {
/* 483 */         return this.fitness;
/*     */       }
/*     */     }
/*     */     
/*     */     private SpawnFinder(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler) {
/* 489 */       this.result = getSpawnPositionAndFitness(targetClimates, sampler, 0, 0);
/* 492 */       radialSearch(targetClimates, sampler, 2048.0F, 512.0F);
/* 494 */       radialSearch(targetClimates, sampler, 512.0F, 32.0F);
/*     */     }
/*     */     
/*     */     private void radialSearch(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, float maxRadius, float radiusIncrement) {
/* 498 */       float angle = 0.0F;
/* 499 */       float radius = radiusIncrement;
/* 500 */       BlockPos searchOrigin = this.result.location();
/* 501 */       while (radius <= maxRadius) {
/* 502 */         int x = searchOrigin.getX() + (int)(Math.sin(angle) * radius);
/* 503 */         int z = searchOrigin.getZ() + (int)(Math.cos(angle) * radius);
/* 504 */         Result candidate = getSpawnPositionAndFitness(targetClimates, sampler, x, z);
/* 505 */         if (candidate.fitness() < this.result.fitness())
/* 506 */           this.result = candidate; 
/* 509 */         angle += radiusIncrement / radius;
/* 510 */         if (angle > 6.283185307179586D) {
/* 511 */           angle = 0.0F;
/* 512 */           radius += radiusIncrement;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Result getSpawnPositionAndFitness(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, int blockX, int blockZ) {
/* 518 */       Climate.TargetPoint targetPoint = sampler.sample(QuartPos.fromBlock(blockX), 0, QuartPos.fromBlock(blockZ));
/* 525 */       Climate.TargetPoint zeroDepthTargetPoint = new Climate.TargetPoint(targetPoint.temperature(), targetPoint.humidity(), targetPoint.continentalness(), targetPoint.erosion(), 0L, targetPoint.weirdness());
/* 528 */       long minFitness = Long.MAX_VALUE;
/* 529 */       for (Climate.ParameterPoint point : targetClimates)
/* 530 */         minFitness = Math.min(minFitness, point.fitness(zeroDepthTargetPoint)); 
/* 533 */       long distanceBiasToWorldOrigin = Mth.square(blockX) + Mth.square(blockZ);
/* 535 */       long fitnessWithDistance = minFitness * Mth.square(2048L) + distanceBiasToWorldOrigin;
/* 536 */       return new Result(new BlockPos(blockX, 0, blockZ), fitnessWithDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Result extends Record {
/*     */     private final BlockPos location;
/*     */     
/*     */     private final long fitness;
/*     */     
/*     */     private Result(BlockPos location, long fitness) {
/*     */       this.location = location;
/*     */       this.fitness = fitness;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos location() {
/*     */       return this.location;
/*     */     }
/*     */     
/*     */     public long fitness() {
/*     */       return this.fitness;
/*     */     }
/*     */   }
/*     */   
/*     */   public static BlockPos findSpawnPosition(List<ParameterPoint> targetClimates, Sampler sampler) {
/* 541 */     return (new SpawnFinder(targetClimates, sampler)).result.location();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface DistanceMetric<T> {
/*     */     long distance(Climate.RTree.Node<T> param1Node, long[] param1ArrayOflong);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */