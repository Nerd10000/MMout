/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class DualNoiseProvider extends NoiseProvider {
/*    */   public static final MapCodec<DualNoiseProvider> CODEC;
/*    */   
/*    */   private final InclusiveRange<Integer> variety;
/*    */   
/*    */   private final NormalNoise.NoiseParameters slowNoiseParameters;
/*    */   
/*    */   private final float slowScale;
/*    */   
/*    */   private final NormalNoise slowNoise;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)InclusiveRange.codec((Codec)Codec.INT, Integer.valueOf(1), Integer.valueOf(64)).fieldOf("variety").forGetter(()), (App)NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("slow_noise").forGetter(()), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter(())).and(noiseProviderCodec(i)).apply((Applicative)i, DualNoiseProvider::new));
/*    */   }
/*    */   
/*    */   public DualNoiseProvider(InclusiveRange<Integer> variety, NormalNoise.NoiseParameters slowNoiseParameters, float slowScale, long seed, NormalNoise.NoiseParameters parameters, float scale, List<BlockState> states) {
/* 41 */     super(seed, parameters, scale, states);
/* 42 */     this.variety = variety;
/* 43 */     this.slowNoiseParameters = slowNoiseParameters;
/* 44 */     this.slowScale = slowScale;
/* 45 */     this.slowNoise = NormalNoise.create((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(seed)), slowNoiseParameters);
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 50 */     return BlockStateProviderType.DUAL_NOISE_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 56 */     double varietyNoise = getSlowNoiseValue(pos);
/* 57 */     int localVariety = (int)Mth.clampedMap(varietyNoise, -1.0D, 1.0D, ((Integer)this.variety.minInclusive()).intValue(), (((Integer)this.variety.maxInclusive()).intValue() + 1));
/* 62 */     List<BlockState> possibleStates = Lists.newArrayListWithCapacity(localVariety);
/* 63 */     for (int i = 0; i < localVariety; i++)
/* 65 */       possibleStates.add(getRandomState(this.states, getSlowNoiseValue(pos.offset(i * 54545, 0, i * 34234)))); 
/* 68 */     return getRandomState(possibleStates, pos, this.scale);
/*    */   }
/*    */   
/*    */   protected double getSlowNoiseValue(BlockPos pos) {
/* 72 */     return this.slowNoise.getValue((pos.getX() * this.slowScale), (pos.getY() * this.slowScale), (pos.getZ() * this.slowScale));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\DualNoiseProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */