/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ public class PathFinder {
/*     */   private static final float FUDGING = 1.5F;
/*     */   
/*  26 */   private final Node[] neighbors = new Node[32];
/*     */   
/*     */   private int maxVisitedNodes;
/*     */   
/*     */   private final NodeEvaluator nodeEvaluator;
/*     */   
/*  30 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   private BooleanSupplier captureDebug = () -> false;
/*     */   
/*     */   public PathFinder(NodeEvaluator nodeEvaluator, int maxVisitedNodes) {
/*  34 */     this.nodeEvaluator = nodeEvaluator;
/*  35 */     this.maxVisitedNodes = maxVisitedNodes;
/*     */   }
/*     */   
/*     */   public void setCaptureDebug(BooleanSupplier captureDebug) {
/*  39 */     this.captureDebug = captureDebug;
/*     */   }
/*     */   
/*     */   public void setMaxVisitedNodes(int maxVisitedNodes) {
/*  43 */     this.maxVisitedNodes = maxVisitedNodes;
/*     */   }
/*     */   
/*     */   public Path findPath(PathNavigationRegion level, Mob entity, Set<BlockPos> targets, float maxPathLength, int reachRange, float maxVisitedNodesMultiplier) {
/*  47 */     this.openSet.clear();
/*  48 */     this.nodeEvaluator.prepare(level, entity);
/*  49 */     Node from = this.nodeEvaluator.getStart();
/*  50 */     if (from == null)
/*  51 */       return null; 
/*  55 */     Map<Target, BlockPos> tos = (Map<Target, BlockPos>)targets.stream().collect(Collectors.toMap(pos -> this.nodeEvaluator.getTarget(pos.getX(), pos.getY(), pos.getZ()), Function.identity()));
/*  56 */     Path path = findPath(from, tos, maxPathLength, reachRange, maxVisitedNodesMultiplier);
/*  58 */     this.nodeEvaluator.done();
/*  59 */     return path;
/*     */   }
/*     */   
/*     */   private Path findPath(Node from, Map<Target, BlockPos> targetMap, float maxPathLength, int reachRange, float maxVisitedNodesMultiplier) {
/*  68 */     ProfilerFiller profiler = Profiler.get();
/*  69 */     profiler.push("find_path");
/*  70 */     profiler.markForCharting(MetricCategory.PATH_FINDING);
/*  71 */     Set<Target> targets = targetMap.keySet();
/*  73 */     from.g = 0.0F;
/*  74 */     from.h = getBestH(from, targets);
/*  75 */     from.f = from.h;
/*  77 */     this.openSet.clear();
/*  78 */     this.openSet.insert(from);
/*  80 */     boolean captureDebug = this.captureDebug.getAsBoolean();
/*  81 */     Set<Node> closedSet = captureDebug ? new HashSet<>() : Set.<Node>of();
/*  84 */     int count = 0;
/*  86 */     Set<Target> reachedTargets = Sets.newHashSetWithExpectedSize(targets.size());
/*  88 */     int maxVisitedNodesAdjusted = (int)(this.maxVisitedNodes * maxVisitedNodesMultiplier);
/*  89 */     while (!this.openSet.isEmpty() && ++count < maxVisitedNodesAdjusted) {
/*  90 */       Node current = this.openSet.pop();
/*  91 */       current.closed = true;
/*  94 */       for (Target target : targets) {
/*  95 */         if (current.distanceManhattan(target) <= reachRange) {
/*  96 */           target.setReached();
/*  97 */           reachedTargets.add(target);
/*     */         } 
/*     */       } 
/* 101 */       if (!reachedTargets.isEmpty())
/*     */         break; 
/* 105 */       if (captureDebug)
/* 106 */         closedSet.add(current); 
/* 109 */       if (current.distanceTo(from) >= maxPathLength)
/*     */         continue; 
/* 113 */       int neighborCount = this.nodeEvaluator.getNeighbors(this.neighbors, current);
/* 114 */       for (int i = 0; i < neighborCount; i++) {
/* 115 */         Node neighbor = this.neighbors[i];
/* 117 */         float distance = distance(current, neighbor);
/* 118 */         current.walkedDistance += distance;
/* 120 */         float tentativeGScore = current.g + distance + neighbor.costMalus;
/* 121 */         if (neighbor.walkedDistance < maxPathLength && (!neighbor.inOpenSet() || tentativeGScore < neighbor.g)) {
/* 122 */           neighbor.cameFrom = current;
/* 123 */           neighbor.g = tentativeGScore;
/* 124 */           neighbor.h = getBestH(neighbor, targets) * 1.5F;
/* 126 */           if (neighbor.inOpenSet()) {
/* 127 */             this.openSet.changeCost(neighbor, neighbor.g + neighbor.h);
/*     */           } else {
/* 129 */             neighbor.f = neighbor.g + neighbor.h;
/* 130 */             this.openSet.insert(neighbor);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 142 */     Optional<Path> optPath = !reachedTargets.isEmpty() ? reachedTargets.stream().map(target -> reconstructPath(target.getBestNode(), (BlockPos)targetMap.get(target), true)).min(Comparator.comparingInt(Path::getNodeCount)) : targets.stream().map(target -> reconstructPath(target.getBestNode(), (BlockPos)targetMap.get(target), false)).min(Comparator.<Path>comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
/* 144 */     profiler.pop();
/* 145 */     if (optPath.isEmpty())
/* 146 */       return null; 
/* 148 */     Path path = optPath.get();
/* 150 */     if (captureDebug)
/* 151 */       path.setDebug(this.openSet.getHeap(), (Node[])closedSet.toArray(x$0 -> new Node[x$0]), targets); 
/* 153 */     return path;
/*     */   }
/*     */   
/*     */   protected float distance(Node from, Node to) {
/* 157 */     return from.distanceTo(to);
/*     */   }
/*     */   
/*     */   private float getBestH(Node from, Set<Target> targets) {
/* 162 */     float bestH = Float.MAX_VALUE;
/* 163 */     for (Target target : targets) {
/* 164 */       float h = from.distanceTo(target);
/* 165 */       target.updateBest(h, from);
/* 166 */       bestH = Math.min(h, bestH);
/*     */     } 
/* 168 */     return bestH;
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node closest, BlockPos target, boolean reached) {
/* 172 */     List<Node> nodes = Lists.newArrayList();
/* 173 */     Node node = closest;
/* 174 */     nodes.add(0, node);
/* 175 */     while (node.cameFrom != null) {
/* 176 */       node = node.cameFrom;
/* 177 */       nodes.add(0, node);
/*     */     } 
/* 179 */     return new Path(nodes, target, reached);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\pathfinder\PathFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */