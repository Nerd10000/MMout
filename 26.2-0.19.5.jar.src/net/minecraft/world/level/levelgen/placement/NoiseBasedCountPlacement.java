/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class NoiseBasedCountPlacement extends RepeatingPlacement {
/*    */   public static final MapCodec<NoiseBasedCountPlacement> CODEC;
/*    */   
/*    */   private final int noiseToCountRatio;
/*    */   
/*    */   private final double noiseFactor;
/*    */   
/*    */   private final double noiseOffset;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(()), (App)Codec.DOUBLE.optionalFieldOf("noise_offset", Double.valueOf(0.0D)).forGetter(())).apply((Applicative)i, NoiseBasedCountPlacement::new));
/*    */   }
/*    */   
/*    */   private NoiseBasedCountPlacement(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
/* 27 */     this.noiseToCountRatio = noiseToCountRatio;
/* 28 */     this.noiseFactor = noiseFactor;
/* 29 */     this.noiseOffset = noiseOffset;
/*    */   }
/*    */   
/*    */   public static NoiseBasedCountPlacement of(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
/* 33 */     return new NoiseBasedCountPlacement(noiseToCountRatio, noiseFactor, noiseOffset);
/*    */   }
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 38 */     double flowerNoise = Biome.BIOME_INFO_NOISE.getValue(origin.getX() / this.noiseFactor, origin.getZ() / this.noiseFactor, false);
/* 39 */     return (int)Math.ceil((flowerNoise + this.noiseOffset) * this.noiseToCountRatio);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 44 */     return PlacementModifierType.NOISE_BASED_COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\NoiseBasedCountPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */