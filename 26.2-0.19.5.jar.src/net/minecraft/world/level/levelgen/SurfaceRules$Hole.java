/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ 
/*     */ enum Hole implements SurfaceRules.ConditionSource {
/* 580 */   INSTANCE;
/*     */   
/*     */   private static final MapCodec<Hole> CODEC;
/*     */   
/*     */   static {
/* 581 */     CODEC = MapCodec.unit(INSTANCE);
/*     */   }
/*     */   
/*     */   public MapCodec<Hole> codec() {
/* 585 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 590 */     return context.hole;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Hole.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */