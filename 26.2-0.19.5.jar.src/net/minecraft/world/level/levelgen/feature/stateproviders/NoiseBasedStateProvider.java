/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public abstract class NoiseBasedStateProvider extends BlockStateProvider {
/*    */   protected final long seed;
/*    */   
/*    */   protected final NormalNoise.NoiseParameters parameters;
/*    */   
/*    */   protected final float scale;
/*    */   
/*    */   protected final NormalNoise noise;
/*    */   
/*    */   protected static <P extends NoiseBasedStateProvider> Products.P3<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float> noiseCodec(RecordCodecBuilder.Instance<P> instance) {
/* 14 */     return instance.group((App)Codec.LONG
/* 15 */         .fieldOf("seed").forGetter(p -> Long.valueOf(p.seed)), (App)NormalNoise.NoiseParameters.DIRECT_CODEC
/* 16 */         .fieldOf("noise").forGetter(p -> p.parameters), (App)ExtraCodecs.POSITIVE_FLOAT
/* 17 */         .fieldOf("scale").forGetter(p -> Float.valueOf(p.scale)));
/*    */   }
/*    */   
/*    */   protected NoiseBasedStateProvider(long seed, NormalNoise.NoiseParameters parameters, float scale) {
/* 27 */     this.seed = seed;
/* 28 */     this.parameters = parameters;
/* 29 */     this.scale = scale;
/* 30 */     this.noise = NormalNoise.create((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(seed)), parameters);
/*    */   }
/*    */   
/*    */   protected double getNoiseValue(BlockPos pos, double scale) {
/* 34 */     return this.noise.getValue(pos.getX() * scale, pos.getY() * scale, pos.getZ() * scale);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseBasedStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */