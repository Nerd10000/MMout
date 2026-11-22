/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public final class BiomeSpecialEffects extends Record {
/*     */   private final int waterColor;
/*     */   
/*     */   private final Optional<Integer> foliageColorOverride;
/*     */   
/*     */   private final Optional<Integer> dryFoliageColorOverride;
/*     */   
/*     */   private final Optional<Integer> grassColorOverride;
/*     */   
/*     */   private final GrassColorModifier grassColorModifier;
/*     */   
/*     */   public static final Codec<BiomeSpecialEffects> CODEC;
/*     */   
/*     */   public BiomeSpecialEffects(int waterColor, Optional<Integer> foliageColorOverride, Optional<Integer> dryFoliageColorOverride, Optional<Integer> grassColorOverride, GrassColorModifier grassColorModifier) {
/*  12 */     this.waterColor = waterColor;
/*  12 */     this.foliageColorOverride = foliageColorOverride;
/*  12 */     this.dryFoliageColorOverride = dryFoliageColorOverride;
/*  12 */     this.grassColorOverride = grassColorOverride;
/*  12 */     this.grassColorModifier = grassColorModifier;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #12	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int waterColor() {
/*  12 */     return this.waterColor;
/*     */   }
/*     */   
/*     */   public Optional<Integer> foliageColorOverride() {
/*  12 */     return this.foliageColorOverride;
/*     */   }
/*     */   
/*     */   public Optional<Integer> dryFoliageColorOverride() {
/*  12 */     return this.dryFoliageColorOverride;
/*     */   }
/*     */   
/*     */   public Optional<Integer> grassColorOverride() {
/*  12 */     return this.grassColorOverride;
/*     */   }
/*     */   
/*     */   public GrassColorModifier grassColorModifier() {
/*  12 */     return this.grassColorModifier;
/*     */   }
/*     */   
/*     */   static {
/*  19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.STRING_RGB_COLOR.fieldOf("water_color").forGetter(BiomeSpecialEffects::waterColor), (App)ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("foliage_color").forGetter(BiomeSpecialEffects::foliageColorOverride), (App)ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("dry_foliage_color").forGetter(BiomeSpecialEffects::dryFoliageColorOverride), (App)ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("grass_color").forGetter(BiomeSpecialEffects::grassColorOverride), (App)GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(BiomeSpecialEffects::grassColorModifier)).apply((Applicative)i, BiomeSpecialEffects::new));
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  28 */     private OptionalInt waterColor = OptionalInt.empty();
/*     */     
/*  29 */     private Optional<Integer> foliageColorOverride = Optional.empty();
/*     */     
/*  30 */     private Optional<Integer> dryFoliageColorOverride = Optional.empty();
/*     */     
/*  31 */     private Optional<Integer> grassColorOverride = Optional.empty();
/*     */     
/*  32 */     private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*     */     
/*     */     public Builder waterColor(int waterColor) {
/*  35 */       this.waterColor = OptionalInt.of(waterColor);
/*  36 */       return this;
/*     */     }
/*     */     
/*     */     public Builder foliageColorOverride(int foliageColor) {
/*  40 */       this.foliageColorOverride = Optional.of(Integer.valueOf(foliageColor));
/*  41 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dryFoliageColorOverride(int dryFoliageColor) {
/*  45 */       this.dryFoliageColorOverride = Optional.of(Integer.valueOf(dryFoliageColor));
/*  46 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorOverride(int grassColor) {
/*  50 */       this.grassColorOverride = Optional.of(Integer.valueOf(grassColor));
/*  51 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassModifier) {
/*  55 */       this.grassColorModifier = grassModifier;
/*  56 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeSpecialEffects build() {
/*  60 */       return new BiomeSpecialEffects(this.waterColor
/*  61 */           .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum GrassColorModifier implements StringRepresentable {
/*  71 */     NONE("none") {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  74 */         return baseColor;
/*     */       }
/*     */     },
/*  77 */     DARK_FOREST("dark_forest") {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  80 */         return ARGB.opaque((baseColor & 0xFEFEFE) + 2634762 >> 1);
/*     */       }
/*     */     },
/*  83 */     SWAMP("swamp") {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  86 */         double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*  87 */         if (groundValue < -0.1D)
/*  88 */           return -11766212; 
/*  90 */         return -9801671;
/*     */       }
/*     */     };
/*     */     
/*     */     private final String name;
/*     */     
/* 102 */     public static final Codec<GrassColorModifier> CODEC = (Codec<GrassColorModifier>)StringRepresentable.fromEnum(GrassColorModifier::values);
/*     */     
/*     */     GrassColorModifier(String name) {
/*     */       this.name = name;
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public String getName() {
/* 105 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 110 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract int modifyColor(double param1Double1, double param1Double2, int param1Int);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       return baseColor;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       return ARGB.opaque((baseColor & 0xFEFEFE) + 2634762 >> 1);
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*     */       if (groundValue < -0.1D)
/*     */         return -11766212; 
/*     */       return -9801671;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */