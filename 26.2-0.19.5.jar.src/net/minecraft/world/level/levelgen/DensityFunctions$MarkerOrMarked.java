/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ public interface MarkerOrMarked extends DensityFunction {
/*     */   DensityFunctions.Marker.Type type();
/*     */   
/*     */   DensityFunction wrapped();
/*     */   
/*     */   default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 435 */     return (KeyDispatchDataCodec)(type()).codec;
/*     */   }
/*     */   
/*     */   default DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 440 */     return new DensityFunctions.Marker(type(), visitor.apply(wrapped()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$MarkerOrMarked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */