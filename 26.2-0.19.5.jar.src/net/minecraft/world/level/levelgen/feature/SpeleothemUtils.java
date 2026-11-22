/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PointedDripstoneBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
/*     */ 
/*     */ public class SpeleothemUtils {
/*     */   protected static double getSpeleothemHeight(double xzDistanceFromCenter, double speleothemRadius, double scale, double bluntness) {
/*  27 */     if (xzDistanceFromCenter < bluntness)
/*  28 */       xzDistanceFromCenter = bluntness; 
/*  32 */     double cutoff = 0.384D;
/*  33 */     double r = xzDistanceFromCenter / speleothemRadius * 0.384D;
/*  38 */     double part1 = 0.75D * Math.pow(r, 1.3333333333333333D);
/*  39 */     double part2 = Math.pow(r, 0.6666666666666666D);
/*  40 */     double part3 = 0.3333333333333333D * Math.log(r);
/*  41 */     double heightRelativeToMaxRadius = scale * (part1 - part2 - part3);
/*  43 */     heightRelativeToMaxRadius = Math.max(heightRelativeToMaxRadius, 0.0D);
/*  44 */     return heightRelativeToMaxRadius / 0.384D * speleothemRadius;
/*     */   }
/*     */   
/*     */   protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, BlockPos center, int xzRadius) {
/*  55 */     if (isEmptyOrWaterOrLava((LevelAccessor)level, center))
/*  56 */       return false; 
/*  60 */     float arcLength = 6.0F;
/*  61 */     float angleIncrement = 6.0F / xzRadius;
/*     */     float angle;
/*  62 */     for (angle = 0.0F; angle < 6.2831855F; angle += angleIncrement) {
/*  63 */       int dx = (int)(Mth.cos(angle) * xzRadius);
/*  64 */       int dz = (int)(Mth.sin(angle) * xzRadius);
/*  65 */       if (isEmptyOrWaterOrLava((LevelAccessor)level, center.offset(dx, 0, dz)))
/*  66 */         return false; 
/*     */     } 
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) {
/*  73 */     return level.isStateAtPosition(pos, SpeleothemUtils::isEmptyOrWater);
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyOrWaterOrLava(LevelAccessor level, BlockPos pos) {
/*  77 */     return level.isStateAtPosition(pos, SpeleothemUtils::isEmptyOrWaterOrLava);
/*     */   }
/*     */   
/*     */   protected static void buildBaseToTipColumn(Direction direction, int totalLength, boolean mergedTip, Consumer<BlockState> consumer, Block pointedBlock) {
/*  84 */     if (totalLength >= 3) {
/*  85 */       consumer.accept(createPointedBlock(direction, SpeleothemThickness.BASE, pointedBlock));
/*  86 */       for (int i = 0; i < totalLength - 3; i++)
/*  87 */         consumer.accept(createPointedBlock(direction, SpeleothemThickness.MIDDLE, pointedBlock)); 
/*     */     } 
/*  90 */     if (totalLength >= 2)
/*  91 */       consumer.accept(createPointedBlock(direction, SpeleothemThickness.FRUSTUM, pointedBlock)); 
/*  93 */     if (totalLength >= 1)
/*  94 */       consumer.accept(createPointedBlock(direction, mergedTip ? SpeleothemThickness.TIP_MERGE : SpeleothemThickness.TIP, pointedBlock)); 
/*     */   }
/*     */   
/*     */   protected static void growSpeleothem(LevelAccessor level, BlockPos startPos, Direction tipDirection, int height, boolean mergedTip, Block baseBlock, Block pointedBlock, HolderSet<Block> replaceableBlocks) {
/*  99 */     if (!isBase(level.getBlockState(startPos.relative(tipDirection.getOpposite())), baseBlock, replaceableBlocks))
/*     */       return; 
/* 103 */     BlockPos.MutableBlockPos pos = startPos.mutable();
/* 104 */     buildBaseToTipColumn(tipDirection, height, mergedTip, state -> {
/*     */           if (state.is(pointedBlock))
/*     */             state = (BlockState)state.setValue((Property)PointedDripstoneBlock.WATERLOGGED, Boolean.valueOf(level.isWaterAt((BlockPos)pos))); 
/*     */           level.setBlock((BlockPos)pos, state, 2);
/*     */           pos.move(tipDirection);
/* 104 */         }pointedBlock);
/*     */   }
/*     */   
/*     */   protected static boolean placeBaseBlockIfPossible(LevelAccessor level, BlockPos pos, Block baseBlock, HolderSet<Block> replaceableBlocks) {
/* 114 */     BlockState state = level.getBlockState(pos);
/* 115 */     if (state.is(replaceableBlocks)) {
/* 116 */       level.setBlock(pos, baseBlock.defaultBlockState(), 2);
/* 117 */       return true;
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockState createPointedBlock(Direction direction, SpeleothemThickness thickness, Block pointedBlock) {
/* 123 */     return (BlockState)((BlockState)pointedBlock.defaultBlockState()
/* 124 */       .setValue((Property)PointedDripstoneBlock.TIP_DIRECTION, (Comparable)direction))
/* 125 */       .setValue((Property)PointedDripstoneBlock.THICKNESS, (Comparable)thickness);
/*     */   }
/*     */   
/*     */   public static boolean isBaseOrLava(BlockState state, Block baseBlock, HolderSet<Block> replaceableBlocks) {
/* 129 */     return (isBase(state, baseBlock, replaceableBlocks) || state.is(Blocks.LAVA));
/*     */   }
/*     */   
/*     */   public static boolean isBase(BlockState state, Block baseBlock, HolderSet<Block> replaceableBlocks) {
/* 134 */     return (state.is(baseBlock) || state.is(replaceableBlocks));
/*     */   }
/*     */   
/*     */   public static boolean isEmptyOrWater(BlockState state) {
/* 138 */     return (state.isAir() || state.is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public static boolean isNeitherEmptyNorWater(BlockState state) {
/* 142 */     return (!state.isAir() && !state.is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public static boolean isEmptyOrWaterOrLava(BlockState state) {
/* 146 */     return (state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SpeleothemUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */