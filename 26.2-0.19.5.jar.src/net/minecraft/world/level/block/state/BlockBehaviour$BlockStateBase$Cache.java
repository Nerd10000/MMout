/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ final class Cache {
/* 1313 */   private static final Direction[] DIRECTIONS = Direction.values();
/*      */   
/* 1314 */   private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */   
/*      */   public final VoxelShape collisionShape;
/*      */   
/*      */   public final boolean largeCollisionShape;
/*      */   
/*      */   private final boolean[] faceSturdy;
/*      */   
/*      */   public final boolean isCollisionShapeFullBlock;
/*      */   
/*      */   private Cache(BlockState state) {
/* 1321 */     Block block = state.getBlock();
/* 1323 */     this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1324 */     if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/* 1325 */       throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/* 1327 */     this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1328 */     this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1329 */     for (Direction direction : DIRECTIONS) {
/* 1330 */       for (SupportType type : SupportType.values())
/* 1331 */         this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */     } 
/* 1334 */     this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */   }
/*      */   
/*      */   public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/* 1338 */     return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */   }
/*      */   
/*      */   private static int getFaceSupportIndex(Direction direction, SupportType supportType) {
/* 1342 */     return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockBehaviour$BlockStateBase$Cache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */