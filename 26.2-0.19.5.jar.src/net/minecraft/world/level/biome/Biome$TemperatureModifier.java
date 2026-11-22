/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum TemperatureModifier implements StringRepresentable {
/*  90 */   NONE("none") {
/*     */     public float modifyTemperature(BlockPos pos, float baseTemperature) {
/*  93 */       return baseTemperature;
/*     */     }
/*     */   },
/*  96 */   FROZEN("frozen") {
/*     */     public float modifyTemperature(BlockPos pos, float baseTemperature) {
/*  99 */       double groundValueLargeVariation = Biome.FROZEN_TEMPERATURE_NOISE.getValue(pos.getX() * 0.05D, pos.getZ() * 0.05D, false) * 7.0D;
/* 100 */       double groundValueEdgeVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.2D, pos.getZ() * 0.2D, false);
/* 101 */       double icePatches = groundValueLargeVariation + groundValueEdgeVariation;
/* 102 */       if (icePatches < 0.3D) {
/* 103 */         double groundValueSmallVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.09D, pos.getZ() * 0.09D, false);
/* 104 */         if (groundValueSmallVariation < 0.8D)
/* 105 */           return 0.2F; 
/*     */       } 
/* 109 */       return baseTemperature;
/*     */     }
/*     */   };
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public static final Codec<TemperatureModifier> CODEC;
/*     */   
/*     */   TemperatureModifier(String name) {
/* 118 */     this.name = name;
/*     */   }
/*     */   
/*     */   static {
/* 121 */     CODEC = (Codec<TemperatureModifier>)StringRepresentable.fromEnum(TemperatureModifier::values);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 124 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 129 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract float modifyTemperature(BlockPos paramBlockPos, float paramFloat);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Biome$TemperatureModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */