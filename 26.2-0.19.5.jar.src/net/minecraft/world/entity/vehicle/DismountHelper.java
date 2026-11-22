/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.block.TrapDoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DismountHelper {
/*     */   public static int[][] offsetsForDirection(Direction forward) {
/*  26 */     Direction right = forward.getClockWise();
/*  27 */     Direction left = right.getOpposite();
/*  28 */     Direction back = forward.getOpposite();
/*  30 */     return new int[][] { { right
/*  31 */           .getStepX(), right.getStepZ() }, { left
/*  32 */           .getStepX(), left.getStepZ() }, { back
/*  33 */           .getStepX() + right.getStepX(), back.getStepZ() + right.getStepZ() }, { back
/*  34 */           .getStepX() + left.getStepX(), back.getStepZ() + left.getStepZ() }, { forward
/*  35 */           .getStepX() + right.getStepX(), forward.getStepZ() + right.getStepZ() }, { forward
/*  36 */           .getStepX() + left.getStepX(), forward.getStepZ() + left.getStepZ() }, { back
/*  37 */           .getStepX(), back.getStepZ() }, { forward
/*  38 */           .getStepX(), forward.getStepZ() } };
/*     */   }
/*     */   
/*     */   public static boolean isBlockFloorValid(double blockFloorHeight) {
/*  43 */     return (!Double.isInfinite(blockFloorHeight) && blockFloorHeight < 1.0D);
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter level, LivingEntity passenger, AABB box) {
/*  47 */     Iterable<VoxelShape> blockCollisions = level.getBlockCollisions((Entity)passenger, box);
/*  48 */     for (VoxelShape collision : blockCollisions) {
/*  49 */       if (!collision.isEmpty())
/*  50 */         return false; 
/*     */     } 
/*  54 */     if (!level.getWorldBorder().isWithinBounds(box))
/*  55 */       return false; 
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter level, Vec3 location, LivingEntity passenger, Pose dismountPose) {
/*  62 */     return canDismountTo(level, passenger, passenger.getLocalBoundsForPose(dismountPose).move(location));
/*     */   }
/*     */   
/*     */   public static VoxelShape nonClimbableShape(BlockGetter level, BlockPos pos) {
/*  66 */     BlockState blockState = level.getBlockState(pos);
/*  67 */     if (blockState.is(BlockTags.CLIMBABLE) || (blockState.getBlock() instanceof TrapDoorBlock && ((Boolean)blockState.getValue((Property)TrapDoorBlock.OPEN)).booleanValue()))
/*  68 */       return Shapes.empty(); 
/*  70 */     return blockState.getCollisionShape(level, pos);
/*     */   }
/*     */   
/*     */   public static double findCeilingFrom(BlockPos pos, int blocks, Function<BlockPos, VoxelShape> shapeGetter) {
/*  74 */     BlockPos.MutableBlockPos cursor = pos.mutable();
/*  75 */     int y = 0;
/*  76 */     while (y < blocks) {
/*  77 */       VoxelShape collisionShape = shapeGetter.apply(cursor);
/*  78 */       if (!collisionShape.isEmpty())
/*  79 */         return (pos.getY() + y) + collisionShape.min(Direction.Axis.Y); 
/*  81 */       y++;
/*  82 */       cursor.move(Direction.UP);
/*     */     } 
/*  84 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public static Vec3 findSafeDismountLocation(EntityType<?> type, CollisionGetter level, BlockPos blockPos, boolean checkDangerous) {
/*  88 */     if (checkDangerous && type.isBlockDangerous(level.getBlockState(blockPos)))
/*  89 */       return null; 
/*  92 */     double floorHeight = level.getBlockFloorHeight(nonClimbableShape((BlockGetter)level, blockPos), () -> nonClimbableShape((BlockGetter)level, blockPos.below()));
/*  93 */     if (!isBlockFloorValid(floorHeight))
/*  94 */       return null; 
/*  97 */     if (checkDangerous && floorHeight <= 0.0D && type.isBlockDangerous(level.getBlockState(blockPos.below())))
/*  98 */       return null; 
/* 101 */     Vec3 position = Vec3.upFromBottomCenterOf((Vec3i)blockPos, floorHeight);
/* 102 */     AABB aabb = type.getDimensions().makeBoundingBox(position);
/* 103 */     Iterable<VoxelShape> worldCollisions = level.getBlockCollisions(null, aabb);
/* 104 */     for (VoxelShape shape : worldCollisions) {
/* 105 */       if (!shape.isEmpty())
/* 106 */         return null; 
/*     */     } 
/* 110 */     if (type == EntityTypes.PLAYER)
/* 112 */       if (level.getBlockState(blockPos).is(BlockTags.INVALID_SPAWN_INSIDE) || level.getBlockState(blockPos.above()).is(BlockTags.INVALID_SPAWN_INSIDE))
/* 113 */         return null;  
/* 117 */     if (!level.getWorldBorder().isWithinBounds(aabb))
/* 118 */       return null; 
/* 121 */     return position;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\DismountHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */