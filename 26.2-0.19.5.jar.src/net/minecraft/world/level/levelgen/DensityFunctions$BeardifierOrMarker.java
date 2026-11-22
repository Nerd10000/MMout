/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ public interface BeardifierOrMarker extends DensityFunction.SimpleFunction {
/* 357 */   public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(DensityFunctions.BeardifierMarker.INSTANCE));
/*     */   
/*     */   default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 361 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$BeardifierOrMarker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */