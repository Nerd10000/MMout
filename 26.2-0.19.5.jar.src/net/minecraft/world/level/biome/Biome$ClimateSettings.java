/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ final class ClimateSettings extends Record {
/*     */   private final boolean hasPrecipitation;
/*     */   
/*     */   private final float temperature;
/*     */   
/*     */   private final Biome.TemperatureModifier temperatureModifier;
/*     */   
/*     */   private final float downfall;
/*     */   
/*     */   public static final MapCodec<ClimateSettings> CODEC;
/*     */   
/*     */   private ClimateSettings(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) {
/* 419 */     this.hasPrecipitation = hasPrecipitation;
/* 419 */     this.temperature = temperature;
/* 419 */     this.temperatureModifier = temperatureModifier;
/* 419 */     this.downfall = downfall;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #419	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public boolean hasPrecipitation() {
/* 419 */     return this.hasPrecipitation;
/*     */   }
/*     */   
/*     */   public float temperature() {
/* 419 */     return this.temperature;
/*     */   }
/*     */   
/*     */   public Biome.TemperatureModifier temperatureModifier() {
/* 419 */     return this.temperatureModifier;
/*     */   }
/*     */   
/*     */   public float downfall() {
/* 419 */     return this.downfall;
/*     */   }
/*     */   
/*     */   static {
/* 420 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("has_precipitation").forGetter(()), (App)Codec.FLOAT.fieldOf("temperature").forGetter(()), (App)Biome.TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", Biome.TemperatureModifier.NONE).forGetter(()), (App)Codec.FLOAT.fieldOf("downfall").forGetter(())).apply((Applicative)i, ClimateSettings::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Biome$ClimateSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */