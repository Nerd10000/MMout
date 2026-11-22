/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/* 447 */   Interpolated("interpolated"),
/* 448 */   FlatCache("flat_cache"),
/* 449 */   Cache2D("cache_2d"),
/* 450 */   CacheOnce("cache_once"),
/* 451 */   CacheAllInCell("cache_all_in_cell"),
/* 452 */   BlendDensity("blend_density");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*     */   
/*     */   Type(String name) {
/* 457 */     this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/* 460 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 465 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Marker$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */