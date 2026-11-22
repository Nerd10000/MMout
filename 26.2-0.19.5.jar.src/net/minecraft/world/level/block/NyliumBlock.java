/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.features.NetherFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.lighting.LightEngine;
/*    */ 
/*    */ public class NyliumBlock extends Block implements BonemealableBlock {
/* 20 */   public static final MapCodec<NyliumBlock> CODEC = simpleCodec(NyliumBlock::new);
/*    */   
/*    */   public MapCodec<NyliumBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected NyliumBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(properties);
/*    */   }
/*    */   
/*    */   private static boolean canBeNylium(BlockState state, LevelReader level, BlockPos pos) {
/* 32 */     BlockPos above = pos.above();
/* 33 */     BlockState aboveState = level.getBlockState(above);
/* 36 */     int lightDampeningTopFace = LightEngine.getLightDampeningInto(state, aboveState, Direction.UP, aboveState.getLightDampening());
/* 37 */     return (lightDampeningTopFace < 15);
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 42 */     if (!canBeNylium(state, (LevelReader)level, pos))
/* 43 */       level.setBlockAndUpdate(pos, Blocks.NETHERRACK.defaultBlockState()); 
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 49 */     return (level.getBlockState(pos.above()).isAir() && level.isInsideBuildHeight(pos.above()));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 54 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 59 */     BlockState blockState = level.getBlockState(pos);
/* 60 */     BlockPos abovePos = pos.above();
/* 61 */     ChunkGenerator generator = level.getChunkSource().getGenerator();
/* 62 */     Registry<ConfiguredFeature<?, ?>> configuredFeatures = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
/* 63 */     if (blockState.is(Blocks.CRIMSON_NYLIUM)) {
/* 64 */       place(configuredFeatures, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, level, generator, random, abovePos);
/* 65 */     } else if (blockState.is(Blocks.WARPED_NYLIUM)) {
/* 66 */       place(configuredFeatures, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL, level, generator, random, abovePos);
/* 67 */       place(configuredFeatures, NetherFeatures.NETHER_SPROUTS_BONEMEAL, level, generator, random, abovePos);
/* 68 */       if (random.nextInt(8) == 0)
/* 69 */         place(configuredFeatures, NetherFeatures.TWISTING_VINES_BONEMEAL, level, generator, random, abovePos); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void place(Registry<ConfiguredFeature<?, ?>> configuredFeatures, ResourceKey<ConfiguredFeature<?, ?>> id, ServerLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos) {
/* 75 */     if (level.isInsideBuildHeight(pos))
/* 76 */       configuredFeatures.get(id).ifPresent(h -> ((ConfiguredFeature)h.value()).place((WorldGenLevel)level, generator, random, pos)); 
/*    */   }
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 82 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\NyliumBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */