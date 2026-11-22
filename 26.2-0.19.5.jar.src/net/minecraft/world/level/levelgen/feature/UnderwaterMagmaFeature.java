/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Column;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class UnderwaterMagmaFeature extends Feature<UnderwaterMagmaConfiguration> {
/*    */   public UnderwaterMagmaFeature(Codec<UnderwaterMagmaConfiguration> codec) {
/* 27 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<UnderwaterMagmaConfiguration> context) {
/* 32 */     WorldGenLevel level = context.level();
/* 33 */     BlockPos origin = context.origin();
/* 34 */     UnderwaterMagmaConfiguration config = context.config();
/* 35 */     RandomSource random = context.random();
/* 37 */     OptionalInt floorY = getFloorY(level, origin, config);
/* 38 */     if (floorY.isEmpty())
/* 39 */       return false; 
/* 41 */     BlockPos floorPos = origin.atY(floorY.getAsInt());
/* 43 */     Vec3i radius = new Vec3i(config.placementRadiusAroundFloor, config.placementRadiusAroundFloor, config.placementRadiusAroundFloor);
/* 44 */     BoundingBox bounds = BoundingBox.fromCorners((Vec3i)floorPos.subtract(radius), (Vec3i)floorPos.offset(radius));
/* 45 */     return 
/*    */       
/* 51 */       (BlockPos.betweenClosedStream(bounds).filter(pos -> (random.nextFloat() < config.placementProbabilityPerValidPosition)).filter(pos -> isValidPlacement(level, pos)).mapToInt(pos -> {
/*    */           level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 2);
/*    */           return 1;
/* 51 */         }).sum() > 0);
/*    */   }
/*    */   
/*    */   private static OptionalInt getFloorY(WorldGenLevel level, BlockPos origin, UnderwaterMagmaConfiguration config) {
/* 55 */     Predicate<BlockState> insideColumn = state -> state.is(Blocks.WATER);
/* 56 */     Predicate<BlockState> validEdge = state -> !state.is(Blocks.WATER);
/* 57 */     Optional<Column> waterColumn = Column.scan((LevelSimulatedReader)level, origin, config.floorSearchRange, insideColumn, validEdge);
/* 58 */     return waterColumn.<OptionalInt>map(Column::getFloor).orElseGet(OptionalInt::empty);
/*    */   }
/*    */   
/*    */   private boolean isValidPlacement(WorldGenLevel level, BlockPos pos) {
/* 63 */     if (isWaterOrAir(level.getBlockState(pos)) || isVisibleFromOutside((LevelAccessor)level, pos.below(), Direction.UP))
/* 64 */       return false; 
/* 66 */     for (Direction neighbourDir : Direction.Plane.HORIZONTAL) {
/* 67 */       if (isVisibleFromOutside((LevelAccessor)level, pos.relative(neighbourDir), neighbourDir.getOpposite()))
/* 68 */         return false; 
/*    */     } 
/* 71 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isWaterOrAir(BlockState state) {
/* 75 */     return (state.is(Blocks.WATER) || state.isAir());
/*    */   }
/*    */   
/*    */   private boolean isVisibleFromOutside(LevelAccessor level, BlockPos pos, Direction coveredDirection) {
/* 79 */     BlockState state = level.getBlockState(pos);
/* 80 */     VoxelShape faceOcclusionShape = state.getFaceOcclusionShape(coveredDirection);
/* 81 */     return (faceOcclusionShape == Shapes.empty() || !Block.isShapeFullBlock(faceOcclusionShape));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\UnderwaterMagmaFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */