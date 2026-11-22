/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ enum BlendOffset implements DensityFunction.SimpleFunction {
/* 327 */   INSTANCE;
/*     */   
/*     */   public static final KeyDispatchDataCodec<DensityFunction> CODEC;
/*     */   
/*     */   static {
/* 328 */     CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 332 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 337 */     Arrays.fill(output, 0.0D);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 342 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 347 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 352 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$BlendOffset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */