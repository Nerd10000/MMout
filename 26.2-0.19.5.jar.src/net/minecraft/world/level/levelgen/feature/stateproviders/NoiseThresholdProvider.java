/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class NoiseThresholdProvider extends NoiseBasedStateProvider {
/*    */   public static final MapCodec<NoiseThresholdProvider> CODEC;
/*    */   
/*    */   private final float threshold;
/*    */   
/*    */   private final float highChance;
/*    */   
/*    */   private final BlockState defaultState;
/*    */   
/*    */   private final List<BlockState> lowStates;
/*    */   
/*    */   private final List<BlockState> highStates;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> noiseCodec(i).and(i.group((App)Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("high_chance").forGetter(()), (App)BlockState.CODEC.fieldOf("default_state").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("low_states").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("high_states").forGetter(()))).apply((Applicative)i, NoiseThresholdProvider::new));
/*    */   }
/*    */   
/*    */   public NoiseThresholdProvider(long seed, NormalNoise.NoiseParameters parameters, float scale, float threshold, float highChance, BlockState defaultState, List<BlockState> lowStates, List<BlockState> highStates) {
/* 41 */     super(seed, parameters, scale);
/* 42 */     this.threshold = threshold;
/* 43 */     this.highChance = highChance;
/* 44 */     this.defaultState = defaultState;
/* 45 */     this.lowStates = lowStates;
/* 46 */     this.highStates = highStates;
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 51 */     return BlockStateProviderType.NOISE_THRESHOLD_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 57 */     double localValue = getNoiseValue(pos, this.scale);
/* 58 */     if (localValue < this.threshold)
/* 59 */       return (BlockState)Util.getRandom(this.lowStates, random); 
/* 62 */     if (random.nextFloat() < this.highChance)
/* 63 */       return (BlockState)Util.getRandom(this.highStates, random); 
/* 66 */     return this.defaultState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseThresholdProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */