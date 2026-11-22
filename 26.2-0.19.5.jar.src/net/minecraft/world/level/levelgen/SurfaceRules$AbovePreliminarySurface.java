/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ 
/*     */ enum AbovePreliminarySurface implements SurfaceRules.ConditionSource {
/* 565 */   INSTANCE;
/*     */   
/*     */   private static final MapCodec<AbovePreliminarySurface> CODEC;
/*     */   
/*     */   static {
/* 566 */     CODEC = MapCodec.unit(INSTANCE);
/*     */   }
/*     */   
/*     */   public MapCodec<AbovePreliminarySurface> codec() {
/* 570 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 575 */     return context.abovePreliminarySurface;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$AbovePreliminarySurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */