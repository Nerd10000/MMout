/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public final class QuantizedSpaghettiRarity {
/*     */   public static DensityFunction wrapRarity2d(DensityFunction input, Holder<NormalNoise.NoiseParameters> noise) {
/* 698 */     return DensityFunctions.intervalSelect(input, 
/* 699 */         DoubleList.of(new double[] { -0.75D, -0.5D, 0.5D, 0.75D }, ), List.of(
/* 701 */           noiseFunctionForRarity(noise, 0.5D), 
/* 702 */           noiseFunctionForRarity(noise, 0.75D), 
/* 703 */           noiseFunctionForRarity(noise, 1.0D), 
/* 704 */           noiseFunctionForRarity(noise, 2.0D), 
/* 705 */           noiseFunctionForRarity(noise, 3.0D)))
/*     */       
/* 707 */       .abs();
/*     */   }
/*     */   
/*     */   public static DensityFunction wrapRarity3d(DensityFunction input, Holder<NormalNoise.NoiseParameters> noise) {
/* 711 */     return DensityFunctions.intervalSelect(input, 
/* 712 */         DoubleList.of(-0.5D, 0.0D, 0.5D), 
/* 713 */         List.of(
/* 714 */           noiseFunctionForRarity(noise, 0.75D), 
/* 715 */           noiseFunctionForRarity(noise, 1.0D), 
/* 716 */           noiseFunctionForRarity(noise, 1.5D), 
/* 717 */           noiseFunctionForRarity(noise, 2.0D)))
/*     */       
/* 719 */       .abs();
/*     */   }
/*     */   
/*     */   private static DensityFunction noiseFunctionForRarity(Holder<NormalNoise.NoiseParameters> noise, double rarity) {
/* 723 */     return DensityFunctions.mul(DensityFunctions.constant(rarity), DensityFunctions.noise(noise, 1.0D / rarity, 1.0D / rarity));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseRouterData$QuantizedSpaghettiRarity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */