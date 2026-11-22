/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ class null implements DensityFunction.Visitor {
/*  89 */   private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
/*     */   
/*     */   null(RandomState this$0) {}
/*     */   
/*     */   private DensityFunction wrapNew(DensityFunction function) {
/*  92 */     if (function instanceof DensityFunctions.HolderHolder) {
/*  92 */       DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)function;
/*  93 */       return (DensityFunction)holder.function().value();
/*     */     } 
/*  95 */     if (function instanceof DensityFunctions.Marker) {
/*  95 */       DensityFunctions.Marker marker = (DensityFunctions.Marker)function;
/*  96 */       return marker.wrapped();
/*     */     } 
/*  98 */     return function;
/*     */   }
/*     */   
/*     */   public DensityFunction apply(DensityFunction input) {
/* 103 */     return this.wrapped.computeIfAbsent(input, this::wrapNew);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\RandomState$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */