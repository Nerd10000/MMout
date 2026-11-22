/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*    */ import net.minecraft.references.BlockItemIds;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class GrassBlock extends SpreadingSnowyBlock implements BonemealableBlock {
/* 22 */   public static final MapCodec<GrassBlock> CODEC = simpleCodec(GrassBlock::new);
/*    */   
/*    */   public MapCodec<GrassBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/*    */   public GrassBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties, BlockItemIds.DIRT.block());
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 35 */     return (level.getBlockState(pos.above()).isAir() && level.isInsideBuildHeight(pos.above()));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 45 */     BlockPos above = pos.above();
/* 47 */     BlockState grass = Blocks.SHORT_GRASS.defaultBlockState();
/* 49 */     Optional<Holder.Reference<PlacedFeature>> grassFeature = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);
/*    */     int j;
/* 52 */     label34: for (j = 0; j < 128; j++) {
/* 53 */       BlockPos testPos = above;
/* 54 */       for (int i = 0; i < j / 16; ) {
/* 55 */         testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
/* 56 */         if (level.getBlockState(testPos.below()).is(this)) {
/* 56 */           if (level.getBlockState(testPos).isCollisionShapeFullBlock((BlockGetter)level, testPos))
/*    */             continue label34; 
/*    */           i++;
/*    */         } 
/*    */         continue label34;
/*    */       } 
/* 62 */       BlockState testState = level.getBlockState(testPos);
/* 63 */       if (testState.is(grass.getBlock()) && random.nextInt(10) == 0) {
/* 64 */         BonemealableBlock bonemealableBlock = (BonemealableBlock)grass.getBlock();
/* 65 */         if (bonemealableBlock.isValidBonemealTarget((LevelReader)level, testPos, testState))
/* 66 */           bonemealableBlock.performBonemeal(level, random, testPos, testState); 
/*    */       } 
/* 70 */       if (testState.isAir() && !level.isOutsideBuildHeight(testPos))
/* 74 */         if (random.nextInt(8) == 0) {
/* 75 */           List<ConfiguredFeature<?, ?>> features = ((Biome)level.getBiome(testPos).value()).getGenerationSettings().getBoneMealFeatures();
/* 76 */           if (!features.isEmpty()) {
/* 79 */             ConfiguredFeature<?, ?> placementFeature = (ConfiguredFeature<?, ?>)Util.getRandom(features, random);
/* 80 */             placementFeature.place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, testPos);
/*    */           } 
/* 81 */         } else if (grassFeature.isPresent()) {
/* 82 */           ((PlacedFeature)((Holder.Reference)grassFeature.get()).value()).place((WorldGenLevel)level, level.getChunkSource().getGenerator(), random, testPos);
/*    */         }  
/*    */     } 
/*    */   }
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 89 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\GrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */