/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum GrassColorModifier implements StringRepresentable {
/*  71 */   NONE("none") {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*  74 */       return baseColor;
/*     */     }
/*     */   },
/*  77 */   DARK_FOREST("dark_forest") {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*  80 */       return ARGB.opaque((baseColor & 0xFEFEFE) + 2634762 >> 1);
/*     */     }
/*     */   },
/*  83 */   SWAMP("swamp") {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*  86 */       double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*  87 */       if (groundValue < -0.1D)
/*  88 */         return -11766212; 
/*  90 */       return -9801671;
/*     */     }
/*     */   };
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public static final Codec<GrassColorModifier> CODEC;
/*     */   
/*     */   GrassColorModifier(String name) {
/*  99 */     this.name = name;
/*     */   }
/*     */   
/*     */   static {
/* 102 */     CODEC = (Codec<GrassColorModifier>)StringRepresentable.fromEnum(GrassColorModifier::values);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 105 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 110 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract int modifyColor(double paramDouble1, double paramDouble2, int paramInt);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$GrassColorModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */