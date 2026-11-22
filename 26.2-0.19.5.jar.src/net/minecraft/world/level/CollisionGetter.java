/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public interface CollisionGetter extends BlockGetter {
/*     */   default boolean isUnobstructed(Entity source, VoxelShape shape) {
/*  28 */     return true;
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(BlockState state, BlockPos pos, CollisionContext context) {
/*  32 */     VoxelShape shape = state.getCollisionShape(this, pos, context);
/*  33 */     return (shape.isEmpty() || isUnobstructed(null, shape.move((Vec3i)pos)));
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(Entity ignore) {
/*  37 */     return isUnobstructed(ignore, Shapes.create(ignore.getBoundingBox()));
/*     */   }
/*     */   
/*     */   default boolean noCollision(AABB aabb) {
/*  41 */     return noCollision(null, aabb);
/*     */   }
/*     */   
/*     */   default boolean noCollision(Entity source) {
/*  45 */     return noCollision(source, source.getBoundingBox());
/*     */   }
/*     */   
/*     */   default boolean noCollision(Entity entity, AABB aabb) {
/*  49 */     return noCollision(entity, aabb, false);
/*     */   }
/*     */   
/*     */   default boolean noCollision(Entity entity, AABB aabb, boolean alwaysCollideWithFluids) {
/*  54 */     return (noBlockCollision(entity, aabb, alwaysCollideWithFluids) && noEntityCollision(entity, aabb) && noBorderCollision(entity, aabb));
/*     */   }
/*     */   
/*     */   default boolean noBlockCollision(Entity entity, AABB aabb) {
/*  58 */     return noBlockCollision(entity, aabb, false);
/*     */   }
/*     */   
/*     */   default boolean noBlockCollision(Entity entity, AABB aabb, boolean alwaysCollideWithFluids) {
/*  62 */     Iterable<VoxelShape> blockCollisions = alwaysCollideWithFluids ? getBlockAndLiquidCollisions(entity, aabb) : getBlockCollisions(entity, aabb);
/*  63 */     for (VoxelShape blockCollision : blockCollisions) {
/*  64 */       if (!blockCollision.isEmpty())
/*  65 */         return false; 
/*     */     } 
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   default boolean noEntityCollision(Entity entity, AABB aabb) {
/*  72 */     return getEntityCollisions(entity, aabb).isEmpty();
/*     */   }
/*     */   
/*     */   default boolean noBorderCollision(Entity entity, AABB aabb) {
/*  76 */     if (entity != null) {
/*  77 */       VoxelShape borderShape = borderCollision(entity, aabb);
/*  78 */       return (borderShape == null || !Shapes.joinIsNotEmpty(borderShape, Shapes.create(aabb), BooleanOp.AND));
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getCollisions(Entity source, AABB box) {
/*  86 */     List<VoxelShape> entityCollisions = getEntityCollisions(source, box);
/*  87 */     Iterable<VoxelShape> blockCollisions = getBlockCollisions(source, box);
/*  88 */     return entityCollisions.isEmpty() ? blockCollisions : Iterables.concat(entityCollisions, blockCollisions);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getPreMoveCollisions(Entity source, AABB box, Vec3 oldPos) {
/*  92 */     List<VoxelShape> entityCollisions = getEntityCollisions(source, box);
/*  93 */     Iterable<VoxelShape> blockCollisions = getBlockCollisionsFromContext(CollisionContext.withPosition(source, oldPos.y), box);
/*  94 */     return entityCollisions.isEmpty() ? blockCollisions : Iterables.concat(entityCollisions, blockCollisions);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getBlockCollisions(Entity source, AABB box) {
/*  98 */     return getBlockCollisionsFromContext((source == null) ? CollisionContext.empty() : CollisionContext.of(source), box);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity source, AABB box) {
/* 102 */     return getBlockCollisionsFromContext((source == null) ? CollisionContext.emptyWithFluidCollisions() : CollisionContext.of(source, true), box);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getBlockCollisionsFromContext(CollisionContext source, AABB box) {
/* 106 */     return () -> new BlockCollisions(this, source, box, false, ());
/*     */   }
/*     */   
/*     */   private VoxelShape borderCollision(Entity source, AABB box) {
/* 110 */     WorldBorder worldBorder = getWorldBorder();
/* 111 */     return worldBorder.isInsideCloseToBorder(source, box) ? worldBorder.getCollisionShape() : null;
/*     */   }
/*     */   
/*     */   default BlockHitResult clipIncludingBorder(ClipContext c) {
/* 115 */     BlockHitResult hitResult = clip(c);
/* 116 */     WorldBorder worldBorder = getWorldBorder();
/* 117 */     if (worldBorder.isWithinBounds(c.getFrom()) && !worldBorder.isWithinBounds(hitResult.getLocation())) {
/* 118 */       Vec3 delta = hitResult.getLocation().subtract(c.getFrom());
/* 119 */       Direction deltaDirection = Direction.getApproximateNearest(delta.x, delta.y, delta.z);
/* 120 */       Vec3 hit = worldBorder.clampVec3ToBound(hitResult.getLocation());
/* 121 */       return new BlockHitResult(hit, deltaDirection, BlockPos.containing((Position)hit), false, true);
/*     */     } 
/* 123 */     return hitResult;
/*     */   }
/*     */   
/*     */   default boolean collidesWithSuffocatingBlock(Entity source, AABB box) {
/* 127 */     BlockCollisions<VoxelShape> blockCollisions = new BlockCollisions<>(this, source, box, true, (p, shape) -> shape);
/* 128 */     while (blockCollisions.hasNext()) {
/* 129 */       if (!((VoxelShape)blockCollisions.next()).isEmpty())
/* 130 */         return true; 
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   default Optional<BlockPos> findSupportingBlock(Entity source, AABB box) {
/* 137 */     BlockPos mainSupport = null;
/* 138 */     double mainSupportDistance = Double.MAX_VALUE;
/* 139 */     BlockCollisions<BlockPos> blockCollisions = new BlockCollisions<>(this, source, box, false, (pos, shape) -> pos);
/* 140 */     while (blockCollisions.hasNext()) {
/* 141 */       BlockPos pos = (BlockPos)blockCollisions.next();
/* 142 */       double distance = pos.distToCenterSqr((Position)source.position());
/* 143 */       if (distance < mainSupportDistance || (distance == mainSupportDistance && (mainSupport == null || mainSupport.compareTo((Vec3i)pos) < 0))) {
/* 144 */         mainSupport = pos.immutable();
/* 145 */         mainSupportDistance = distance;
/*     */       } 
/*     */     } 
/* 148 */     return Optional.ofNullable(mainSupport);
/*     */   }
/*     */   
/*     */   default Optional<Vec3> findFreePosition(Entity source, VoxelShape allowedCenters, Vec3 preferredCenter, double sizeX, double sizeY, double sizeZ) {
/* 155 */     if (allowedCenters.isEmpty())
/* 156 */       return Optional.empty(); 
/* 159 */     AABB searchArea = allowedCenters.bounds().inflate(sizeX, sizeY, sizeZ);
/* 165 */     VoxelShape expandedCollisions = StreamSupport.stream(getBlockCollisions(source, searchArea).spliterator(), false).filter(shape -> (getWorldBorder() == null || getWorldBorder().isWithinBounds(shape.bounds()))).flatMap(shape -> shape.toAabbs().stream()).map(aabb -> aabb.inflate(sizeX / 2.0D, sizeY / 2.0D, sizeZ / 2.0D)).map(Shapes::create).reduce(Shapes.empty(), Shapes::or);
/* 168 */     VoxelShape freeSpots = Shapes.join(allowedCenters, expandedCollisions, BooleanOp.ONLY_FIRST);
/* 170 */     return freeSpots.closestPointTo(preferredCenter);
/*     */   }
/*     */   
/*     */   WorldBorder getWorldBorder();
/*     */   
/*     */   BlockGetter getChunkForCollisions(int paramInt1, int paramInt2);
/*     */   
/*     */   List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\CollisionGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */