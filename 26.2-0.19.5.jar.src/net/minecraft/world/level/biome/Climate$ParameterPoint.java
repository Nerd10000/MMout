/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class ParameterPoint extends Record {
/*     */   private final Climate.Parameter temperature;
/*     */   
/*     */   private final Climate.Parameter humidity;
/*     */   
/*     */   private final Climate.Parameter continentalness;
/*     */   
/*     */   private final Climate.Parameter erosion;
/*     */   
/*     */   private final Climate.Parameter depth;
/*     */   
/*     */   private final Climate.Parameter weirdness;
/*     */   
/*     */   private final long offset;
/*     */   
/*     */   public static final Codec<ParameterPoint> CODEC;
/*     */   
/*     */   public ParameterPoint(Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, Climate.Parameter weirdness, long offset) {
/* 342 */     this.temperature = temperature;
/* 342 */     this.humidity = humidity;
/* 342 */     this.continentalness = continentalness;
/* 342 */     this.erosion = erosion;
/* 342 */     this.depth = depth;
/* 342 */     this.weirdness = weirdness;
/* 342 */     this.offset = offset;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$ParameterPoint;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #342	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$ParameterPoint;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Climate.Parameter temperature() {
/* 342 */     return this.temperature;
/*     */   }
/*     */   
/*     */   public Climate.Parameter humidity() {
/* 342 */     return this.humidity;
/*     */   }
/*     */   
/*     */   public Climate.Parameter continentalness() {
/* 342 */     return this.continentalness;
/*     */   }
/*     */   
/*     */   public Climate.Parameter erosion() {
/* 342 */     return this.erosion;
/*     */   }
/*     */   
/*     */   public Climate.Parameter depth() {
/* 342 */     return this.depth;
/*     */   }
/*     */   
/*     */   public Climate.Parameter weirdness() {
/* 342 */     return this.weirdness;
/*     */   }
/*     */   
/*     */   public long offset() {
/* 342 */     return this.offset;
/*     */   }
/*     */   
/*     */   static {
/* 351 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Climate.Parameter.CODEC.fieldOf("temperature").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("humidity").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("continentalness").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("erosion").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("depth").forGetter(()), (App)Climate.Parameter.CODEC.fieldOf("weirdness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("offset").xmap(Climate::quantizeCoord, Climate::unquantizeCoord).forGetter(())).apply((Applicative)i, ParameterPoint::new));
/*     */   }
/*     */   
/*     */   private long fitness(Climate.TargetPoint target) {
/* 362 */     return Mth.square(this.temperature.distance(target.temperature)) + 
/* 363 */       Mth.square(this.humidity.distance(target.humidity)) + 
/* 364 */       Mth.square(this.continentalness.distance(target.continentalness)) + 
/* 365 */       Mth.square(this.erosion.distance(target.erosion)) + 
/* 366 */       Mth.square(this.depth.distance(target.depth)) + 
/* 367 */       Mth.square(this.weirdness.distance(target.weirdness)) + 
/* 368 */       Mth.square(this.offset);
/*     */   }
/*     */   
/*     */   List<Climate.Parameter> parameterSpace() {
/* 374 */     return (List<Climate.Parameter>)ImmutableList.of(this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, new Climate.Parameter(this.offset, this.offset));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$ParameterPoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */