/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class PathNavigation {
/*     */   private static final int MAX_TIME_RECOMPUTE = 20;
/*     */   
/*     */   private static final int STUCK_CHECK_INTERVAL = 100;
/*     */   
/*     */   private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;
/*     */   
/*     */   protected final Mob mob;
/*     */   
/*     */   protected final Level level;
/*     */   
/*     */   protected Path path;
/*     */   
/*     */   protected double speedModifier;
/*     */   
/*     */   protected int tick;
/*     */   
/*     */   protected int lastStuckCheck;
/*     */   
/*  56 */   protected Vec3 lastStuckCheckPos = Vec3.ZERO;
/*     */   
/*  57 */   protected Vec3i timeoutCachedNode = Vec3i.ZERO;
/*     */   
/*     */   protected long timeoutTimer;
/*     */   
/*     */   protected long lastTimeoutCheck;
/*     */   
/*     */   protected double timeoutLimit;
/*     */   
/*  61 */   protected float maxDistanceToWaypoint = 0.5F;
/*     */   
/*     */   protected boolean hasDelayedRecomputation;
/*     */   
/*     */   protected long timeLastRecompute;
/*     */   
/*     */   protected NodeEvaluator nodeEvaluator;
/*     */   
/*     */   private BlockPos targetPos;
/*     */   
/*     */   private int reachRange;
/*     */   
/*  70 */   private float maxVisitedNodesMultiplier = 1.0F;
/*     */   
/*     */   private final PathFinder pathFinder;
/*     */   
/*     */   private boolean isStuck;
/*     */   
/*  80 */   private float requiredPathLength = 16.0F;
/*     */   
/*     */   public PathNavigation(Mob mob, Level level) {
/*  83 */     this.mob = mob;
/*  84 */     this.level = level;
/*  85 */     this.pathFinder = createPathFinder(Mth.floor(mob.getAttributeBaseValue(Attributes.FOLLOW_RANGE) * 16.0D));
/*  86 */     if (level instanceof ServerLevel) {
/*  86 */       ServerLevel serverLevel = (ServerLevel)level;
/*  87 */       ServerDebugSubscribers subscribers = serverLevel.getServer().debugSubscribers();
/*  88 */       this.pathFinder.setCaptureDebug(() -> subscribers.hasAnySubscriberFor(DebugSubscriptions.ENTITY_PATHS));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updatePathfinderMaxVisitedNodes() {
/*  95 */     int maxVisitedNodes = Mth.floor(getMaxPathLength() * 16.0F);
/*  96 */     this.pathFinder.setMaxVisitedNodes(maxVisitedNodes);
/*     */   }
/*     */   
/*     */   public void setRequiredPathLength(float length) {
/* 102 */     this.requiredPathLength = length;
/* 104 */     updatePathfinderMaxVisitedNodes();
/*     */   }
/*     */   
/*     */   private float getMaxPathLength() {
/* 109 */     return Math.max((float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE), this.requiredPathLength);
/*     */   }
/*     */   
/*     */   public void resetMaxVisitedNodesMultiplier() {
/* 113 */     this.maxVisitedNodesMultiplier = 1.0F;
/*     */   }
/*     */   
/*     */   public void setMaxVisitedNodesMultiplier(float maxVisitedNodesMultiplier) {
/* 118 */     this.maxVisitedNodesMultiplier = maxVisitedNodesMultiplier;
/*     */   }
/*     */   
/*     */   public BlockPos getTargetPos() {
/* 122 */     return this.targetPos;
/*     */   }
/*     */   
/*     */   public void setSpeedModifier(double speedModifier) {
/* 128 */     this.speedModifier = speedModifier;
/*     */   }
/*     */   
/*     */   public void recomputePath() {
/* 132 */     if (this.level.getGameTime() - this.timeLastRecompute > 20L && canUpdatePath()) {
/* 133 */       if (this.targetPos != null) {
/* 134 */         this.path = null;
/* 135 */         this.path = createPath(this.targetPos, this.reachRange);
/* 136 */         this.timeLastRecompute = this.level.getGameTime();
/* 137 */         this.hasDelayedRecomputation = false;
/*     */       } 
/*     */     } else {
/* 140 */       this.hasDelayedRecomputation = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public final Path createPath(double x, double y, double z, int reachRange) {
/* 145 */     return createPath(BlockPos.containing(x, y, z), reachRange);
/*     */   }
/*     */   
/*     */   public Path createPath(Stream<BlockPos> positions, int reachRange) {
/* 155 */     return createPath(positions.collect((Collector)Collectors.toSet()), 8, false, reachRange);
/*     */   }
/*     */   
/*     */   public Path createPath(Set<BlockPos> positions, int reachRange) {
/* 159 */     return createPath(positions, 8, false, reachRange);
/*     */   }
/*     */   
/*     */   public Path createPath(BlockPos pos, int reachRange) {
/* 169 */     return createPath((Set<BlockPos>)ImmutableSet.of(pos), 8, false, reachRange);
/*     */   }
/*     */   
/*     */   public Path createPath(BlockPos pos, int reachRange, int maxPathLength) {
/* 175 */     return createPath((Set<BlockPos>)ImmutableSet.of(pos), 8, false, reachRange, maxPathLength);
/*     */   }
/*     */   
/*     */   public Path createPath(Entity target, int reachRange) {
/* 185 */     return createPath((Set<BlockPos>)ImmutableSet.of(target.blockPosition()), 16, true, reachRange);
/*     */   }
/*     */   
/*     */   protected Path createPath(Set<BlockPos> targets, int radiusOffset, boolean above, int reachRange) {
/* 189 */     return createPath(targets, radiusOffset, above, reachRange, getMaxPathLength());
/*     */   }
/*     */   
/*     */   protected Path createPath(Set<BlockPos> targets, int radiusOffset, boolean above, int reachRange, float maxPathLength) {
/* 193 */     if (targets.isEmpty())
/* 194 */       return null; 
/* 197 */     if (this.mob.getY() < this.level.getMinY())
/* 198 */       return null; 
/* 201 */     if (!canUpdatePath())
/* 202 */       return null; 
/* 206 */     if (this.path != null && !this.path.isDone() && targets.contains(this.targetPos))
/* 207 */       return this.path; 
/* 210 */     ProfilerFiller profiler = Profiler.get();
/* 211 */     profiler.push("pathfind");
/* 212 */     BlockPos fromPos = above ? this.mob.blockPosition().above() : this.mob.blockPosition();
/* 213 */     int radius = (int)(maxPathLength + radiusOffset);
/* 216 */     PathNavigationRegion region = new PathNavigationRegion(this.level, fromPos.offset(-radius, -radius, -radius), fromPos.offset(radius, radius, radius));
/* 217 */     Path path = this.pathFinder.findPath(region, this.mob, targets, maxPathLength, reachRange, this.maxVisitedNodesMultiplier);
/* 218 */     profiler.pop();
/* 220 */     if (path != null && path.getTarget() != null) {
/* 224 */       this.targetPos = path.getTarget();
/* 225 */       this.reachRange = reachRange;
/* 226 */       resetStuckTimeout();
/*     */     } 
/* 229 */     return path;
/*     */   }
/*     */   
/*     */   public boolean moveTo(double x, double y, double z, double speedModifier) {
/* 236 */     return moveTo(createPath(x, y, z, 1), speedModifier);
/*     */   }
/*     */   
/*     */   public boolean moveTo(double x, double y, double z, int reachRange, double speedModifier) {
/* 240 */     return moveTo(createPath(x, y, z, reachRange), speedModifier);
/*     */   }
/*     */   
/*     */   public boolean moveTo(Entity target, double speedModifier) {
/* 247 */     Path newPath = createPath(target, 1);
/* 248 */     return (newPath != null && moveTo(newPath, speedModifier));
/*     */   }
/*     */   
/*     */   public boolean moveTo(Path newPath, double speedModifier) {
/* 254 */     if (newPath == null) {
/* 255 */       this.path = null;
/* 256 */       return false;
/*     */     } 
/* 258 */     if (!newPath.sameAs(this.path))
/* 259 */       this.path = newPath; 
/* 261 */     if (isDone())
/* 262 */       return false; 
/* 264 */     trimPath();
/* 265 */     if (this.path.getNodeCount() <= 0)
/* 266 */       return false; 
/* 269 */     this.speedModifier = speedModifier;
/* 270 */     Vec3 mobPos = getTempMobPos();
/* 271 */     this.lastStuckCheck = this.tick;
/* 272 */     this.lastStuckCheckPos = mobPos;
/* 273 */     return true;
/*     */   }
/*     */   
/*     */   public Path getPath() {
/* 277 */     return this.path;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 281 */     this.tick++;
/* 283 */     if (this.hasDelayedRecomputation)
/* 284 */       recomputePath(); 
/* 287 */     if (isDone())
/*     */       return; 
/* 291 */     if (canUpdatePath()) {
/* 292 */       followThePath();
/* 293 */     } else if (this.path != null && !this.path.isDone()) {
/* 294 */       Vec3 mobPos = getTempMobPos();
/* 295 */       Vec3 pos = this.path.getNextEntityPos((Entity)this.mob);
/* 296 */       if (mobPos.y > pos.y && !this.mob.onGround() && Mth.floor(mobPos.x) == Mth.floor(pos.x) && Mth.floor(mobPos.z) == Mth.floor(pos.z))
/* 297 */         this.path.advance(); 
/*     */     } 
/* 301 */     if (isDone())
/*     */       return; 
/* 306 */     Vec3 target = this.path.getNextEntityPos((Entity)this.mob);
/* 307 */     this.mob.getMoveControl().setWantedPosition(target.x, getGroundY(target), target.z, this.speedModifier);
/*     */   }
/*     */   
/*     */   protected double getGroundY(Vec3 target) {
/* 312 */     BlockPos blockPos = BlockPos.containing((Position)target);
/* 313 */     return this.level.getBlockState(blockPos.below()).isAir() ? target.y : WalkNodeEvaluator.getFloorLevel((BlockGetter)this.level, blockPos);
/*     */   }
/*     */   
/*     */   protected void followThePath() {
/* 318 */     Vec3 mobPos = getTempMobPos();
/* 320 */     this.maxDistanceToWaypoint = (this.mob.getBbWidth() > 0.75F) ? (this.mob.getBbWidth() / 2.0F) : (0.75F - this.mob.getBbWidth() / 2.0F);
/* 321 */     BlockPos blockPos = this.path.getNextNodePos();
/* 322 */     double xDistance = Math.abs(this.mob.getX() - blockPos.getX() + 0.5D);
/* 323 */     double yDistance = Math.abs(this.mob.getY() - blockPos.getY());
/* 324 */     double zDistance = Math.abs(this.mob.getZ() - blockPos.getZ() + 0.5D);
/* 325 */     boolean isCloseEnoughToCurrentNode = (xDistance < this.maxDistanceToWaypoint && zDistance < this.maxDistanceToWaypoint && yDistance < getMaxVerticalDistanceToWaypoint());
/* 329 */     if (isCloseEnoughToCurrentNode || (canCutCorner((this.path.getNextNode()).type) && shouldTargetNextNodeInDirection(mobPos)))
/* 330 */       this.path.advance(); 
/* 332 */     doStuckDetection(mobPos);
/*     */   }
/*     */   
/*     */   private boolean shouldTargetNextNodeInDirection(Vec3 mobPosition) {
/* 336 */     if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount())
/* 337 */       return false; 
/* 340 */     Vec3 currentNode = Vec3.atBottomCenterOf((Vec3i)this.path.getNextNodePos());
/* 341 */     if (!mobPosition.closerThan((Position)currentNode, 2.0D))
/* 344 */       return false; 
/* 347 */     if (canMoveDirectly(mobPosition, this.path.getNextEntityPos((Entity)this.mob)))
/* 348 */       return true; 
/* 351 */     Vec3 nextNode = Vec3.atBottomCenterOf((Vec3i)this.path.getNodePos(this.path.getNextNodeIndex() + 1));
/* 354 */     Vec3 mobToCurrent = currentNode.subtract(mobPosition);
/* 355 */     Vec3 mobToNext = nextNode.subtract(mobPosition);
/* 356 */     double mobToCurrentSqr = mobToCurrent.lengthSqr();
/* 357 */     double mobToNextSqr = mobToNext.lengthSqr();
/* 358 */     boolean closerToNextThanCurrent = (mobToNextSqr < mobToCurrentSqr);
/* 359 */     boolean withinCurrentBlock = (mobToCurrentSqr < 0.5D);
/* 360 */     if (closerToNextThanCurrent || withinCurrentBlock) {
/* 361 */       Vec3 mobDirection = mobToCurrent.normalize();
/* 362 */       Vec3 pathDirection = mobToNext.normalize();
/* 363 */       return (pathDirection.dot(mobDirection) < 0.0D);
/*     */     } 
/* 366 */     return false;
/*     */   }
/*     */   
/*     */   protected void doStuckDetection(Vec3 mobPos) {
/* 371 */     if (this.tick - this.lastStuckCheck > 100) {
/* 373 */       float effectiveSpeed = (this.mob.getSpeed() >= 1.0F) ? this.mob.getSpeed() : (this.mob.getSpeed() * this.mob.getSpeed());
/* 374 */       float thresholdDistance = effectiveSpeed * 100.0F * 0.25F;
/* 375 */       if (mobPos.distanceToSqr(this.lastStuckCheckPos) < (thresholdDistance * thresholdDistance)) {
/* 376 */         this.isStuck = true;
/* 377 */         stop();
/*     */       } else {
/* 379 */         this.isStuck = false;
/*     */       } 
/* 381 */       this.lastStuckCheck = this.tick;
/* 382 */       this.lastStuckCheckPos = mobPos;
/*     */     } 
/* 385 */     if (this.path != null && !this.path.isDone()) {
/* 386 */       BlockPos blockPos = this.path.getNextNodePos();
/* 388 */       long time = this.level.getGameTime();
/* 389 */       if (blockPos.equals(this.timeoutCachedNode)) {
/* 390 */         this.timeoutTimer += time - this.lastTimeoutCheck;
/*     */       } else {
/* 392 */         this.timeoutCachedNode = (Vec3i)blockPos;
/* 398 */         double distToNode = mobPos.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
/* 399 */         this.timeoutLimit = (this.mob.getSpeed() > 0.0F) ? (distToNode / this.mob.getSpeed() * 20.0D) : 0.0D;
/*     */       } 
/* 402 */       if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 3.0D)
/* 403 */         timeoutPath(); 
/* 405 */       this.lastTimeoutCheck = time;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void timeoutPath() {
/* 410 */     resetStuckTimeout();
/* 411 */     stop();
/*     */   }
/*     */   
/*     */   private void resetStuckTimeout() {
/* 415 */     this.timeoutCachedNode = Vec3i.ZERO;
/* 416 */     this.timeoutTimer = 0L;
/* 417 */     this.timeoutLimit = 0.0D;
/* 418 */     this.isStuck = false;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 423 */     return (this.path == null || this.path.isDone());
/*     */   }
/*     */   
/*     */   public boolean isInProgress() {
/* 428 */     return !isDone();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 432 */     this.path = null;
/*     */   }
/*     */   
/*     */   protected void trimPath() {
/* 442 */     if (this.path == null)
/*     */       return; 
/* 446 */     for (int i = 0; i < this.path.getNodeCount(); i++) {
/* 447 */       Node node = this.path.getNode(i);
/* 448 */       Node nextNode = (i + 1 < this.path.getNodeCount()) ? this.path.getNode(i + 1) : null;
/* 450 */       BlockState state = this.level.getBlockState(new BlockPos(node.x, node.y, node.z));
/* 452 */       if (state.is(BlockTags.CAULDRONS)) {
/* 453 */         this.path.replaceNode(i, node.cloneAndMove(node.x, node.y + 1, node.z));
/* 454 */         if (nextNode != null && node.y >= nextNode.y)
/* 455 */           this.path.replaceNode(i + 1, node.cloneAndMove(nextNode.x, node.y + 1, nextNode.z)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 462 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canCutCorner(PathType pathType) {
/* 470 */     return (pathType != PathType.FIRE_IN_NEIGHBOR && pathType != PathType.DAMAGING_IN_NEIGHBOR && pathType != PathType.WALKABLE_DOOR);
/*     */   }
/*     */   
/*     */   protected static boolean isClearForMovementBetween(Mob mob, Vec3 startPos, Vec3 stopPos, boolean blockedByFluids) {
/* 476 */     Vec3 to = new Vec3(stopPos.x, stopPos.y + mob.getBbHeight() * 0.5D, stopPos.z);
/* 477 */     return (mob.level().clip(new ClipContext(startPos, to, ClipContext.Block.COLLIDER, blockedByFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, (Entity)mob)).getType() == HitResult.Type.MISS);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos pos) {
/* 481 */     BlockPos below = pos.below();
/* 482 */     return this.level.getBlockState(below).isSolidRender();
/*     */   }
/*     */   
/*     */   public NodeEvaluator getNodeEvaluator() {
/* 486 */     return this.nodeEvaluator;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean canFloat) {
/* 490 */     this.nodeEvaluator.setCanFloat(canFloat);
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/* 494 */     return this.nodeEvaluator.canFloat();
/*     */   }
/*     */   
/*     */   public boolean shouldRecomputePath(BlockPos pos) {
/* 498 */     if (this.hasDelayedRecomputation)
/* 499 */       return false; 
/* 502 */     if (this.path == null || this.path.isDone() || this.path.getNodeCount() == 0)
/* 503 */       return false; 
/* 506 */     Node target = this.path.getEndNode();
/* 511 */     Vec3 middlePos = new Vec3((target.x + this.mob.getX()) / 2.0D, (target.y + this.mob.getY()) / 2.0D, (target.z + this.mob.getZ()) / 2.0D);
/* 514 */     return pos.closerToCenterThan((Position)middlePos, (this.path.getNodeCount() - this.path.getNextNodeIndex()));
/*     */   }
/*     */   
/*     */   public float getMaxDistanceToWaypoint() {
/* 518 */     return this.maxDistanceToWaypoint;
/*     */   }
/*     */   
/*     */   public float getMaxVerticalDistanceToWaypoint() {
/* 522 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public boolean isStuck() {
/* 526 */     return this.isStuck;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean canOpenDoors) {
/* 532 */     this.nodeEvaluator.setCanOpenDoors(canOpenDoors);
/*     */   }
/*     */   
/*     */   protected abstract PathFinder createPathFinder(int paramInt);
/*     */   
/*     */   protected abstract Vec3 getTempMobPos();
/*     */   
/*     */   protected abstract boolean canUpdatePath();
/*     */   
/*     */   public abstract boolean canNavigateGround();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\navigation\PathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */