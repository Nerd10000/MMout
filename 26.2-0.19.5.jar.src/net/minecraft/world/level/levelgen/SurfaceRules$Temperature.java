/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ 
/*     */ enum Temperature implements SurfaceRules.ConditionSource {
/* 783 */   INSTANCE;
/*     */   
/*     */   private static final MapCodec<Temperature> CODEC;
/*     */   
/*     */   static {
/* 784 */     CODEC = MapCodec.unit(INSTANCE);
/*     */   }
/*     */   
/*     */   public MapCodec<Temperature> codec() {
/* 788 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 793 */     return context.temperature;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Temperature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */