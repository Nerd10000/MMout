/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ @Deprecated
/*    */ public class CountOnEveryLayerPlacement extends PlacementModifier {
/*    */   public static final MapCodec<CountOnEveryLayerPlacement> CODEC;
/*    */   
/*    */   private final IntProvider count;
/*    */   
/*    */   static {
/* 20 */     CODEC = IntProviders.codec(0, 256).fieldOf("count").xmap(CountOnEveryLayerPlacement::new, c -> c.count);
/*    */   }
/*    */   
/*    */   private CountOnEveryLayerPlacement(IntProvider count) {
/* 25 */     this.count = count;
/*    */   }
/*    */   
/*    */   public static CountOnEveryLayerPlacement of(IntProvider count) {
/* 29 */     return new CountOnEveryLayerPlacement(count);
/*    */   }
/*    */   
/*    */   public static CountOnEveryLayerPlacement of(int count) {
/* 33 */     return of((IntProvider)ConstantInt.of(count));
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/*    */     boolean foundAny;
/* 38 */     Stream.Builder<BlockPos> positions = Stream.builder();
/* 40 */     int layer = 0;
/*    */     do {
/* 42 */       foundAny = false;
/* 44 */       for (int i = 0; i < this.count.sample(random); i++) {
/* 45 */         int x = random.nextInt(16) + origin.getX();
/* 46 */         int z = random.nextInt(16) + origin.getZ();
/* 47 */         int startY = context.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
/* 48 */         int y = findOnGroundYPosition(context, x, startY, z, layer);
/* 49 */         if (y != Integer.MAX_VALUE) {
/* 50 */           positions.add(new BlockPos(x, y, z));
/* 51 */           foundAny = true;
/*    */         } 
/*    */       } 
/* 54 */       layer++;
/* 55 */     } while (foundAny);
/* 57 */     return positions.build();
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 62 */     return PlacementModifierType.COUNT_ON_EVERY_LAYER;
/*    */   }
/*    */   
/*    */   private static int findOnGroundYPosition(PlacementContext context, int xStart, int yStart, int zStart, int layerToPlaceOn) {
/* 67 */     BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos(xStart, yStart, zStart);
/* 69 */     int currentLayer = 0;
/* 70 */     BlockState currentBlock = context.getBlockState((BlockPos)currentPos);
/* 71 */     for (int y = yStart; y >= context.getMinY() + 1; y--) {
/* 72 */       currentPos.setY(y - 1);
/* 73 */       BlockState belowBlock = context.getBlockState((BlockPos)currentPos);
/* 74 */       if (!isEmpty(belowBlock) && isEmpty(currentBlock) && !belowBlock.is(Blocks.BEDROCK)) {
/* 75 */         if (currentLayer == layerToPlaceOn)
/* 76 */           return currentPos.getY() + 1; 
/* 78 */         currentLayer++;
/*    */       } 
/* 80 */       currentBlock = belowBlock;
/*    */     } 
/* 82 */     return Integer.MAX_VALUE;
/*    */   }
/*    */   
/*    */   private static boolean isEmpty(BlockState blockState) {
/* 86 */     return (blockState.isAir() || blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\CountOnEveryLayerPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */