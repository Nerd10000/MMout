/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.FenceGateBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WalkNodeEvaluator extends NodeEvaluator {
/*     */   public static final double SPACE_BETWEEN_WALL_POSTS = 0.5D;
/*     */   
/*     */   private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125D;
/*     */   
/*  38 */   private final Long2ObjectMap<PathType> pathTypesByPosCacheByMob = (Long2ObjectMap<PathType>)new Long2ObjectOpenHashMap();
/*     */   
/*  39 */   private final Object2BooleanMap<AABB> collisionCache = (Object2BooleanMap<AABB>)new Object2BooleanOpenHashMap();
/*     */   
/*  40 */   private final Node[] reusableNeighbors = new Node[Direction.Plane.HORIZONTAL.length()];
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  47 */     super.prepare(level, entity);
/*  48 */     entity.onPathfindingStart();
/*     */   }
/*     */   
/*     */   public void done() {
/*  53 */     this.mob.onPathfindingDone();
/*  55 */     this.pathTypesByPosCacheByMob.clear();
/*  56 */     this.collisionCache.clear();
/*  58 */     super.done();
/*     */   }
/*     */   
/*     */   public Node getStart() {
/*  64 */     BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
/*  65 */     int startY = this.mob.getBlockY();
/*  66 */     BlockState blockState = this.currentContext.getBlockState((BlockPos)reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*  68 */     if (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  69 */       while (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  70 */         startY++;
/*  71 */         blockState = this.currentContext.getBlockState((BlockPos)reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*     */       } 
/*  73 */       startY--;
/*  74 */     } else if (canFloat() && this.mob.isInWater()) {
/*  75 */       while (blockState.is(Blocks.WATER) || blockState.getFluidState() == Fluids.WATER.getSource(false)) {
/*  76 */         startY++;
/*  77 */         blockState = this.currentContext.getBlockState((BlockPos)reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*     */       } 
/*  79 */       startY--;
/*  81 */     } else if (this.mob.onGround()) {
/*  82 */       startY = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } else {
/*  84 */       reusablePos.set(this.mob.getX(), this.mob.getY() + 1.0D, this.mob.getZ());
/*  85 */       while (reusablePos.getY() > this.currentContext.level().getMinY()) {
/*  86 */         startY = reusablePos.getY();
/*  87 */         reusablePos.setY(reusablePos.getY() - 1);
/*  88 */         BlockState belowBlockState = this.currentContext.getBlockState((BlockPos)reusablePos);
/*  89 */         if (!belowBlockState.isAir() && !belowBlockState.isPathfindable(PathComputationType.LAND))
/*     */           break; 
/*     */       } 
/*     */     } 
/*  96 */     BlockPos startPos = this.mob.blockPosition();
/*  97 */     if (!canStartAt((BlockPos)reusablePos.set(startPos.getX(), startY, startPos.getZ()))) {
/*  98 */       AABB mobBB = this.mob.getBoundingBox();
/* 100 */       if (canStartAt((BlockPos)reusablePos.set(mobBB.minX, startY, mobBB.minZ)) || 
/* 101 */         canStartAt((BlockPos)reusablePos.set(mobBB.minX, startY, mobBB.maxZ)) || 
/* 102 */         canStartAt((BlockPos)reusablePos.set(mobBB.maxX, startY, mobBB.minZ)) || 
/* 103 */         canStartAt((BlockPos)reusablePos.set(mobBB.maxX, startY, mobBB.maxZ)))
/* 105 */         return getStartNode((BlockPos)reusablePos); 
/*     */     } 
/* 108 */     return getStartNode(new BlockPos(startPos.getX(), startY, startPos.getZ()));
/*     */   }
/*     */   
/*     */   protected Node getStartNode(BlockPos pos) {
/* 114 */     Node node = getNode(pos);
/* 115 */     node.type = getCachedPathType(node.x, node.y, node.z);
/* 116 */     node.costMalus = this.mob.getPathfindingMalus(node.type);
/* 117 */     return node;
/*     */   }
/*     */   
/*     */   protected boolean canStartAt(BlockPos pos) {
/* 121 */     PathType blockPathType = getCachedPathType(pos.getX(), pos.getY(), pos.getZ());
/* 122 */     return (blockPathType != PathType.OPEN && this.mob.getPathfindingMalus(blockPathType) >= 0.0F);
/*     */   }
/*     */   
/*     */   public Target getTarget(double x, double y, double z) {
/* 127 */     return getTargetNodeAt(x, y, z);
/*     */   }
/*     */   
/*     */   public int getNeighbors(Node[] neighbors, Node pos) {
/* 132 */     int p = 0;
/* 133 */     int jumpSize = 0;
/* 134 */     PathType blockPathTypeAbove = getCachedPathType(pos.x, pos.y + 1, pos.z);
/* 135 */     PathType blockPathTypeCurrent = getCachedPathType(pos.x, pos.y, pos.z);
/* 137 */     if (this.mob.getPathfindingMalus(blockPathTypeAbove) >= 0.0F && blockPathTypeCurrent != PathType.STICKY_HONEY)
/* 138 */       jumpSize = Mth.floor(Math.max(1.0F, this.mob.maxUpStep())); 
/* 141 */     double posHeight = getFloorLevel(new BlockPos(pos.x, pos.y, pos.z));
/* 142 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 143 */       Node node = findAcceptedNode(pos.x + direction.getStepX(), pos.y, pos.z + direction.getStepZ(), jumpSize, posHeight, direction, blockPathTypeCurrent);
/* 144 */       this.reusableNeighbors[direction.get2DDataValue()] = node;
/* 145 */       if (isNeighborValid(node, pos))
/* 146 */         neighbors[p++] = node; 
/*     */     } 
/* 150 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 151 */       Direction secondDirection = direction.getClockWise();
/* 152 */       if (isDiagonalValid(pos, this.reusableNeighbors[direction.get2DDataValue()], this.reusableNeighbors[secondDirection.get2DDataValue()])) {
/* 153 */         Node diagonalNode = findAcceptedNode(pos.x + direction.getStepX() + secondDirection.getStepX(), pos.y, pos.z + direction.getStepZ() + secondDirection.getStepZ(), jumpSize, posHeight, direction, blockPathTypeCurrent);
/* 154 */         if (isDiagonalValid(diagonalNode))
/* 155 */           neighbors[p++] = diagonalNode; 
/*     */       } 
/*     */     } 
/* 159 */     return p;
/*     */   }
/*     */   
/*     */   protected boolean isNeighborValid(Node neighbor, Node current) {
/* 163 */     return (neighbor != null && !neighbor.closed && (neighbor.costMalus >= 0.0F || current.costMalus < 0.0F));
/*     */   }
/*     */   
/*     */   protected boolean isDiagonalValid(Node pos, Node ew, Node ns) {
/* 167 */     if (ns == null || ew == null || ns.y > pos.y || ew.y > pos.y)
/* 168 */       return false; 
/* 170 */     if (ew.type == PathType.WALKABLE_DOOR || ns.type == PathType.WALKABLE_DOOR)
/* 172 */       return false; 
/* 174 */     if (this.mob.getBbWidth() > 1.0F && (ew.costMalus > 0.0F || ns.costMalus > 0.0F))
/* 175 */       return false; 
/* 178 */     boolean canPassBetweenPosts = (ns.type == PathType.FENCE && ew.type == PathType.FENCE && this.mob.getBbWidth() < 0.5D);
/* 179 */     return ((ns.y < pos.y || ns.costMalus >= 0.0F || canPassBetweenPosts) && (ew.y < pos.y || ew.costMalus >= 0.0F || canPassBetweenPosts));
/*     */   }
/*     */   
/*     */   protected boolean isDiagonalValid(Node diagonal) {
/* 183 */     if (diagonal == null || diagonal.closed)
/* 184 */       return false; 
/* 186 */     if (diagonal.type == PathType.WALKABLE_DOOR)
/* 188 */       return false; 
/* 190 */     return (diagonal.costMalus >= 0.0F);
/*     */   }
/*     */   
/*     */   private static boolean doesBlockHavePartialCollision(PathType type) {
/* 194 */     return (type == PathType.FENCE || type == PathType.DOOR_WOOD_CLOSED || type == PathType.DOOR_IRON_CLOSED);
/*     */   }
/*     */   
/*     */   private boolean canReachWithoutCollision(Node posTo) {
/* 200 */     AABB bb = this.mob.getBoundingBox();
/* 204 */     Vec3 delta = new Vec3(posTo.x - this.mob.getX() + bb.getXsize() / 2.0D, posTo.y - this.mob.getY() + bb.getYsize() / 2.0D, posTo.z - this.mob.getZ() + bb.getZsize() / 2.0D);
/* 206 */     int steps = Mth.ceil(delta.length() / bb.getSize());
/* 207 */     delta = delta.scale((1.0F / steps));
/* 208 */     for (int i = 1; i <= steps; i++) {
/* 209 */       bb = bb.move(delta);
/* 210 */       if (hasCollisions(bb))
/* 211 */         return false; 
/*     */     } 
/* 214 */     return true;
/*     */   }
/*     */   
/*     */   protected double getFloorLevel(BlockPos pos) {
/* 218 */     CollisionGetter collisionGetter = this.currentContext.level();
/* 219 */     if ((canFloat() || isAmphibious()) && collisionGetter.getFluidState(pos).is(FluidTags.WATER))
/* 220 */       return pos.getY() + 0.5D; 
/* 222 */     return getFloorLevel((BlockGetter)collisionGetter, pos);
/*     */   }
/*     */   
/*     */   public static double getFloorLevel(BlockGetter level, BlockPos pos) {
/* 226 */     BlockPos target = pos.below();
/* 227 */     VoxelShape shape = level.getBlockState(target).getCollisionShape(level, target);
/* 228 */     return target.getY() + (shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y));
/*     */   }
/*     */   
/*     */   protected boolean isAmphibious() {
/* 232 */     return false;
/*     */   }
/*     */   
/*     */   protected Node findAcceptedNode(int x, int y, int z, int jumpSize, double nodeHeight, Direction travelDirection, PathType blockPathTypeCurrent) {
/* 236 */     Node best = null;
/* 237 */     BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
/* 239 */     double maxYTarget = getFloorLevel((BlockPos)reusablePos.set(x, y, z));
/* 241 */     if (maxYTarget - nodeHeight > getMobJumpHeight())
/* 242 */       return null; 
/* 245 */     PathType pathType = getCachedPathType(x, y, z);
/* 246 */     float pathCost = this.mob.getPathfindingMalus(pathType);
/* 248 */     if (pathCost >= 0.0F)
/* 249 */       best = getNodeAndUpdateCostToMax(x, y, z, pathType, pathCost); 
/* 253 */     if (doesBlockHavePartialCollision(blockPathTypeCurrent) && best != null && best.costMalus >= 0.0F && !canReachWithoutCollision(best))
/* 254 */       best = null; 
/* 257 */     if (pathType == PathType.WALKABLE || (isAmphibious() && pathType == PathType.WATER))
/* 258 */       return best; 
/* 261 */     if ((best == null || best.costMalus < 0.0F) && jumpSize > 0 && (pathType != PathType.FENCE || canWalkOverFences()) && pathType != PathType.UNPASSABLE_RAIL && pathType != PathType.TRAPDOOR && pathType != PathType.POWDER_SNOW) {
/* 262 */       best = tryJumpOn(x, y, z, jumpSize, nodeHeight, travelDirection, blockPathTypeCurrent, reusablePos);
/* 263 */     } else if (!isAmphibious() && pathType == PathType.WATER && !canFloat()) {
/* 264 */       best = tryFindFirstNonWaterBelow(x, y, z, best);
/* 265 */     } else if (pathType == PathType.OPEN) {
/* 266 */       best = tryFindFirstGroundNodeBelow(x, y, z);
/* 267 */     } else if (doesBlockHavePartialCollision(pathType) && best == null) {
/* 268 */       best = getClosedNode(x, y, z, pathType);
/*     */     } 
/* 270 */     return best;
/*     */   }
/*     */   
/*     */   private double getMobJumpHeight() {
/* 274 */     return Math.max(1.125D, this.mob.maxUpStep());
/*     */   }
/*     */   
/*     */   private Node getNodeAndUpdateCostToMax(int x, int y, int z, PathType pathType, float cost) {
/* 278 */     Node node = getNode(x, y, z);
/* 279 */     node.type = pathType;
/* 280 */     node.costMalus = Math.max(node.costMalus, cost);
/* 281 */     return node;
/*     */   }
/*     */   
/*     */   private Node getBlockedNode(int x, int y, int z) {
/* 285 */     Node node = getNode(x, y, z);
/* 286 */     node.type = PathType.BLOCKED;
/* 287 */     node.costMalus = -1.0F;
/* 288 */     return node;
/*     */   }
/*     */   
/*     */   private Node getClosedNode(int x, int y, int z, PathType pathType) {
/* 292 */     Node node = getNode(x, y, z);
/* 293 */     node.closed = true;
/* 294 */     node.type = pathType;
/* 295 */     node.costMalus = pathType.getMalus();
/* 296 */     return node;
/*     */   }
/*     */   
/*     */   private Node tryJumpOn(int x, int y, int z, int jumpSize, double nodeHeight, Direction travelDirection, PathType blockPathTypeCurrent, BlockPos.MutableBlockPos reusablePos) {
/* 300 */     Node nodeAbove = findAcceptedNode(x, y + 1, z, jumpSize - 1, nodeHeight, travelDirection, blockPathTypeCurrent);
/* 302 */     if (nodeAbove == null)
/* 303 */       return null; 
/* 306 */     if (this.mob.getBbWidth() >= 1.0F)
/* 307 */       return nodeAbove; 
/* 310 */     if (nodeAbove.type != PathType.OPEN && nodeAbove.type != PathType.WALKABLE)
/* 311 */       return nodeAbove; 
/* 317 */     double centerX = (x - travelDirection.getStepX()) + 0.5D;
/* 318 */     double centerZ = (z - travelDirection.getStepZ()) + 0.5D;
/* 319 */     double halfWidth = this.mob.getBbWidth() / 2.0D;
/* 325 */     AABB grow = new AABB(centerX - halfWidth, getFloorLevel((BlockPos)reusablePos.set(centerX, (y + 1), centerZ)) + 0.001D, centerZ - halfWidth, centerX + halfWidth, this.mob.getBbHeight() + getFloorLevel((BlockPos)reusablePos.set(nodeAbove.x, nodeAbove.y, nodeAbove.z)) - 0.002D, centerZ + halfWidth);
/* 328 */     return hasCollisions(grow) ? null : nodeAbove;
/*     */   }
/*     */   
/*     */   private Node tryFindFirstNonWaterBelow(int x, int y, int z, Node best) {
/* 333 */     for (; --y > this.mob.level().getMinY(); y--) {
/* 334 */       PathType pathTypeLocal = getCachedPathType(x, y, z);
/* 336 */       if (pathTypeLocal == PathType.WATER) {
/* 337 */         best = getNodeAndUpdateCostToMax(x, y, z, pathTypeLocal, this.mob.getPathfindingMalus(pathTypeLocal));
/*     */       } else {
/* 339 */         return best;
/*     */       } 
/*     */     } 
/* 342 */     return best;
/*     */   }
/*     */   
/*     */   private Node tryFindFirstGroundNodeBelow(int x, int y, int z) {
/* 348 */     for (int currentY = y - 1; currentY >= this.mob.level().getMinY(); currentY--) {
/* 349 */       if (y - currentY > this.mob.getMaxFallDistance())
/* 350 */         return getBlockedNode(x, currentY, z); 
/* 353 */       PathType pathType = getCachedPathType(x, currentY, z);
/* 354 */       float pathCost = this.mob.getPathfindingMalus(pathType);
/* 356 */       if (pathType != PathType.OPEN) {
/* 357 */         if (pathCost >= 0.0F)
/* 358 */           return getNodeAndUpdateCostToMax(x, currentY, z, pathType, pathCost); 
/* 360 */         return getBlockedNode(x, currentY, z);
/*     */       } 
/*     */     } 
/* 365 */     return getBlockedNode(x, y, z);
/*     */   }
/*     */   
/*     */   private boolean hasCollisions(AABB aabb) {
/* 369 */     return this.collisionCache.computeIfAbsent(aabb, bb -> !this.currentContext.level().noCollision((Entity)this.mob, aabb));
/*     */   }
/*     */   
/*     */   protected PathType getCachedPathType(int x, int y, int z) {
/* 373 */     return (PathType)this.pathTypesByPosCacheByMob.computeIfAbsent(BlockPos.asLong(x, y, z), k -> getPathTypeOfMob(this.currentContext, x, y, z, this.mob));
/*     */   }
/*     */   
/*     */   public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
/* 379 */     Set<PathType> blockTypes = getPathTypeWithinMobBB(context, x, y, z);
/* 381 */     if (blockTypes.size() == 1)
/* 382 */       return blockTypes.iterator().next(); 
/* 385 */     if (blockTypes.contains(PathType.FENCE))
/* 386 */       return PathType.FENCE; 
/* 389 */     if (blockTypes.contains(PathType.UNPASSABLE_RAIL))
/* 390 */       return PathType.UNPASSABLE_RAIL; 
/* 393 */     PathType highestMalusPathTypeWithinBB = PathType.BLOCKED;
/* 394 */     float highestMalusWithinBB = mob.getPathfindingMalus(highestMalusPathTypeWithinBB);
/* 396 */     for (PathType pathType : blockTypes) {
/* 398 */       float malusForPathType = mob.getPathfindingMalus(pathType);
/* 399 */       if (malusForPathType < 0.0F)
/* 400 */         return pathType; 
/* 404 */       if (malusForPathType >= highestMalusWithinBB) {
/* 405 */         highestMalusWithinBB = malusForPathType;
/* 406 */         highestMalusPathTypeWithinBB = pathType;
/*     */       } 
/*     */     } 
/* 410 */     PathType currentNodePathType = getPathType(context, x, y, z);
/* 411 */     boolean isLargeMob = (this.entityWidth > 1);
/* 413 */     if (isLargeMob) {
/* 415 */       boolean isCurrentNodeCheaper = (mob.getPathfindingMalus(currentNodePathType) < highestMalusWithinBB);
/* 416 */       boolean capMalusDueToCheapNode = (isCurrentNodeCheaper && mob.getPathfindingMalus(PathType.BIG_MOBS_CLOSE_TO_DANGER) < highestMalusWithinBB);
/* 418 */       if (capMalusDueToCheapNode)
/* 419 */         return PathType.BIG_MOBS_CLOSE_TO_DANGER; 
/* 422 */       return highestMalusPathTypeWithinBB;
/*     */     } 
/* 426 */     if (currentNodePathType == PathType.OPEN && highestMalusPathTypeWithinBB != PathType.OPEN && highestMalusWithinBB == 0.0F)
/* 427 */       return PathType.OPEN; 
/* 430 */     return highestMalusPathTypeWithinBB;
/*     */   }
/*     */   
/*     */   public Set<PathType> getPathTypeWithinMobBB(PathfindingContext context, int x, int y, int z) {
/* 434 */     EnumSet<PathType> blockTypes = EnumSet.noneOf(PathType.class);
/* 435 */     for (int dx = 0; dx < this.entityWidth; dx++) {
/* 436 */       for (int dy = 0; dy < this.entityHeight; dy++) {
/* 437 */         for (int dz = 0; dz < this.entityDepth; dz++) {
/* 438 */           int xx = dx + x;
/* 439 */           int yy = dy + y;
/* 440 */           int zz = dz + z;
/* 442 */           PathType blockType = getPathType(context, xx, yy, zz);
/* 444 */           BlockPos mobPosition = this.mob.blockPosition();
/* 445 */           boolean canPassDoors = canPassDoors();
/* 446 */           if (blockType == PathType.DOOR_WOOD_CLOSED && canOpenDoors() && canPassDoors)
/* 447 */             blockType = PathType.WALKABLE_DOOR; 
/* 449 */           if (blockType == PathType.DOOR_OPEN && !canPassDoors)
/* 450 */             blockType = PathType.BLOCKED; 
/* 452 */           if (blockType == PathType.RAIL && getPathType(context, mobPosition.getX(), mobPosition.getY(), mobPosition.getZ()) != PathType.RAIL && getPathType(context, mobPosition.getX(), mobPosition.getY() - 1, mobPosition.getZ()) != PathType.RAIL)
/* 453 */             blockType = PathType.UNPASSABLE_RAIL; 
/* 456 */           blockTypes.add(blockType);
/*     */         } 
/*     */       } 
/*     */     } 
/* 460 */     return blockTypes;
/*     */   }
/*     */   
/*     */   public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 465 */     return getPathTypeStatic(context, new BlockPos.MutableBlockPos(x, y, z));
/*     */   }
/*     */   
/*     */   public static PathType getPathTypeStatic(Mob mob, BlockPos pos) {
/* 469 */     return getPathTypeStatic(new PathfindingContext((CollisionGetter)mob.level(), mob), pos.mutable());
/*     */   }
/*     */   
/*     */   public static PathType getPathTypeStatic(PathfindingContext context, BlockPos.MutableBlockPos pos) {
/* 478 */     int x = pos.getX();
/* 479 */     int y = pos.getY();
/* 480 */     int z = pos.getZ();
/* 482 */     PathType blockPathType = context.getPathTypeFromState(x, y, z);
/* 483 */     if (blockPathType != PathType.OPEN || y < context.level().getMinY() + 1)
/* 484 */       return blockPathType; 
/* 487 */     switch (context.getPathTypeFromState(x, y - 1, z)) {
/*     */       case OPEN:
/*     */       case WATER:
/*     */       case LAVA:
/*     */       case WALKABLE:
/*     */       
/*     */       case FIRE:
/*     */       
/*     */       case DAMAGING:
/*     */       
/*     */       case STICKY_HONEY:
/*     */       
/*     */       case POWDER_SNOW:
/*     */       
/*     */       case DAMAGE_CAUTIOUS:
/*     */       
/*     */       case TRAPDOOR:
/*     */       
/*     */     } 
/* 487 */     return 
/*     */       
/* 497 */       checkNeighbourBlocks(context, x, y, z, PathType.WALKABLE);
/*     */   }
/*     */   
/*     */   public static PathType checkNeighbourBlocks(PathfindingContext context, int x, int y, int z, PathType blockPathType) {
/* 502 */     for (int dx = -1; dx <= 1; dx++) {
/* 503 */       for (int dy = -1; dy <= 1; dy++) {
/* 504 */         for (int dz = -1; dz <= 1; dz++) {
/* 505 */           if (dx != 0 || dz != 0) {
/* 506 */             PathType pathType = context.getPathTypeFromState(x + dx, y + dy, z + dz);
/* 508 */             if (pathType == PathType.DAMAGING)
/* 509 */               return PathType.DAMAGING_IN_NEIGHBOR; 
/* 510 */             if (pathType == PathType.FIRE || pathType == PathType.LAVA)
/* 511 */               return PathType.FIRE_IN_NEIGHBOR; 
/* 512 */             if (pathType == PathType.WATER)
/* 513 */               return PathType.WATER_BORDER; 
/* 514 */             if (pathType == PathType.DAMAGE_CAUTIOUS)
/* 515 */               return PathType.DAMAGE_CAUTIOUS; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 521 */     return blockPathType;
/*     */   }
/*     */   
/*     */   protected static PathType getPathTypeFromState(BlockGetter level, BlockPos pos) {
/* 525 */     BlockState blockState = level.getBlockState(pos);
/* 526 */     Block block = blockState.getBlock();
/* 528 */     if (blockState.isAir())
/* 529 */       return PathType.OPEN; 
/* 532 */     if (blockState.is(BlockTags.TRAPDOORS) || blockState.is(Blocks.LILY_PAD) || blockState.is(Blocks.BIG_DRIPLEAF))
/* 533 */       return PathType.TRAPDOOR; 
/* 536 */     if (blockState.is(Blocks.POWDER_SNOW))
/* 537 */       return PathType.POWDER_SNOW; 
/* 540 */     if (blockState.is(Blocks.CACTUS) || blockState.is(Blocks.SWEET_BERRY_BUSH))
/* 541 */       return PathType.DAMAGING; 
/* 544 */     if (blockState.is(Blocks.HONEY_BLOCK))
/* 545 */       return PathType.STICKY_HONEY; 
/* 548 */     if (blockState.is(Blocks.COCOA))
/* 549 */       return PathType.COCOA; 
/* 552 */     if (blockState.is(Blocks.WITHER_ROSE) || blockState.is(BlockTags.SPELEOTHEMS))
/* 553 */       return PathType.DAMAGE_CAUTIOUS; 
/* 556 */     FluidState fluidState = blockState.getFluidState();
/* 557 */     if (fluidState.is(FluidTags.LAVA))
/* 558 */       return PathType.LAVA; 
/* 561 */     if (isBurningBlock(blockState))
/* 562 */       return PathType.FIRE; 
/* 565 */     if (block instanceof DoorBlock) {
/* 565 */       DoorBlock door = (DoorBlock)block;
/* 566 */       if (((Boolean)blockState.getValue((Property)DoorBlock.OPEN)).booleanValue())
/* 567 */         return PathType.DOOR_OPEN; 
/* 569 */       return door.type().canOpenByHand() ? PathType.DOOR_WOOD_CLOSED : PathType.DOOR_IRON_CLOSED;
/*     */     } 
/* 572 */     if (block instanceof net.minecraft.world.level.block.BaseRailBlock)
/* 573 */       return PathType.RAIL; 
/* 576 */     if (block instanceof net.minecraft.world.level.block.LeavesBlock)
/* 577 */       return PathType.LEAVES; 
/* 580 */     if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS) || (block instanceof FenceGateBlock && !((Boolean)blockState.getValue((Property)FenceGateBlock.OPEN)).booleanValue()))
/* 581 */       return PathType.FENCE; 
/* 585 */     if (!blockState.isPathfindable(PathComputationType.LAND))
/* 586 */       return PathType.BLOCKED; 
/* 589 */     if (fluidState.is(FluidTags.WATER))
/* 590 */       return PathType.WATER; 
/* 593 */     return PathType.OPEN;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\pathfinder\WalkNodeEvaluator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */