/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ enum BlendAlpha implements DensityFunction.SimpleFunction {
/* 296 */   INSTANCE;
/*     */   
/*     */   public static final KeyDispatchDataCodec<DensityFunction> CODEC;
/*     */   
/*     */   static {
/* 297 */     CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 301 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 306 */     Arrays.fill(output, 1.0D);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 311 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 316 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 321 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$BlendAlpha.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */