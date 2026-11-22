/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ 
/*     */ enum Steep implements SurfaceRules.ConditionSource {
/* 798 */   INSTANCE;
/*     */   
/*     */   private static final MapCodec<Steep> CODEC;
/*     */   
/*     */   static {
/* 799 */     CODEC = MapCodec.unit(INSTANCE);
/*     */   }
/*     */   
/*     */   public MapCodec<Steep> codec() {
/* 803 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 808 */     return context.steep;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Steep.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */