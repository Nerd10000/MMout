/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ 
/*     */ enum Bandlands implements SurfaceRules.RuleSource {
/* 870 */   INSTANCE;
/*     */   
/*     */   private static final MapCodec<Bandlands> CODEC;
/*     */   
/*     */   static {
/* 871 */     CODEC = MapCodec.unit(INSTANCE);
/*     */   }
/*     */   
/*     */   public MapCodec<Bandlands> codec() {
/* 875 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 880 */     Objects.requireNonNull(context.system);
/* 880 */     return context.system::getBand;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Bandlands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */