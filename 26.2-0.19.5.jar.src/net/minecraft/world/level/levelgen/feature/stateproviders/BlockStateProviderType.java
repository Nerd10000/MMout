/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class BlockStateProviderType<P extends BlockStateProvider> {
/*  8 */   public static final BlockStateProviderType<SimpleStateProvider> SIMPLE_STATE_PROVIDER = register("simple_state_provider", SimpleStateProvider.CODEC);
/*    */   
/*  9 */   public static final BlockStateProviderType<WeightedStateProvider> WEIGHTED_STATE_PROVIDER = register("weighted_state_provider", WeightedStateProvider.CODEC);
/*    */   
/* 10 */   public static final BlockStateProviderType<NoiseThresholdProvider> NOISE_THRESHOLD_PROVIDER = register("noise_threshold_provider", NoiseThresholdProvider.CODEC);
/*    */   
/* 11 */   public static final BlockStateProviderType<NoiseProvider> NOISE_PROVIDER = register("noise_provider", NoiseProvider.CODEC);
/*    */   
/* 12 */   public static final BlockStateProviderType<DualNoiseProvider> DUAL_NOISE_PROVIDER = register("dual_noise_provider", DualNoiseProvider.CODEC);
/*    */   
/* 13 */   public static final BlockStateProviderType<RotatedBlockProvider> ROTATED_BLOCK_PROVIDER = register("rotated_block_provider", RotatedBlockProvider.CODEC);
/*    */   
/* 14 */   public static final BlockStateProviderType<RandomizedIntStateProvider> RANDOMIZED_INT_STATE_PROVIDER = register("randomized_int_state_provider", RandomizedIntStateProvider.CODEC);
/*    */   
/* 15 */   public static final BlockStateProviderType<RuleBasedStateProvider> RULE_BASED_STATE_PROVIDER = register("rule_based_state_provider", RuleBasedStateProvider.CODEC);
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private static <P extends BlockStateProvider> BlockStateProviderType<P> register(String name, MapCodec<P> codec) {
/* 18 */     return (BlockStateProviderType<P>)Registry.register(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, name, new BlockStateProviderType<>(codec));
/*    */   }
/*    */   
/*    */   private BlockStateProviderType(MapCodec<P> codec) {
/* 24 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 28 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProviderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */