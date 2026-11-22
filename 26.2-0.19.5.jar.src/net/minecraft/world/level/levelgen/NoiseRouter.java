/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function15;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class NoiseRouter extends Record {
/*    */   private final DensityFunction barrierNoise;
/*    */   
/*    */   private final DensityFunction fluidLevelFloodednessNoise;
/*    */   
/*    */   private final DensityFunction fluidLevelSpreadNoise;
/*    */   
/*    */   private final DensityFunction lavaNoise;
/*    */   
/*    */   private final DensityFunction temperature;
/*    */   
/*    */   private final DensityFunction vegetation;
/*    */   
/*    */   private final DensityFunction continents;
/*    */   
/*    */   private final DensityFunction erosion;
/*    */   
/*    */   private final DensityFunction depth;
/*    */   
/*    */   private final DensityFunction ridges;
/*    */   
/*    */   private final DensityFunction preliminarySurfaceLevel;
/*    */   
/*    */   private final DensityFunction finalDensity;
/*    */   
/*    */   private final DensityFunction veinToggle;
/*    */   
/*    */   private final DensityFunction veinRidged;
/*    */   
/*    */   private final DensityFunction veinGap;
/*    */   
/*    */   public static final Codec<NoiseRouter> CODEC;
/*    */   
/*    */   public NoiseRouter(DensityFunction barrierNoise, DensityFunction fluidLevelFloodednessNoise, DensityFunction fluidLevelSpreadNoise, DensityFunction lavaNoise, DensityFunction temperature, DensityFunction vegetation, DensityFunction continents, DensityFunction erosion, DensityFunction depth, DensityFunction ridges, DensityFunction preliminarySurfaceLevel, DensityFunction finalDensity, DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap) {
/*  9 */     this.barrierNoise = barrierNoise;
/*  9 */     this.fluidLevelFloodednessNoise = fluidLevelFloodednessNoise;
/*  9 */     this.fluidLevelSpreadNoise = fluidLevelSpreadNoise;
/*  9 */     this.lavaNoise = lavaNoise;
/*  9 */     this.temperature = temperature;
/*  9 */     this.vegetation = vegetation;
/*  9 */     this.continents = continents;
/*  9 */     this.erosion = erosion;
/*  9 */     this.depth = depth;
/*  9 */     this.ridges = ridges;
/*  9 */     this.preliminarySurfaceLevel = preliminarySurfaceLevel;
/*  9 */     this.finalDensity = finalDensity;
/*  9 */     this.veinToggle = veinToggle;
/*  9 */     this.veinRidged = veinRidged;
/*  9 */     this.veinGap = veinGap;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseRouter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DensityFunction barrierNoise() {
/*  9 */     return this.barrierNoise;
/*    */   }
/*    */   
/*    */   public DensityFunction fluidLevelFloodednessNoise() {
/*  9 */     return this.fluidLevelFloodednessNoise;
/*    */   }
/*    */   
/*    */   public DensityFunction fluidLevelSpreadNoise() {
/*  9 */     return this.fluidLevelSpreadNoise;
/*    */   }
/*    */   
/*    */   public DensityFunction lavaNoise() {
/*  9 */     return this.lavaNoise;
/*    */   }
/*    */   
/*    */   public DensityFunction temperature() {
/*  9 */     return this.temperature;
/*    */   }
/*    */   
/*    */   public DensityFunction vegetation() {
/*  9 */     return this.vegetation;
/*    */   }
/*    */   
/*    */   public DensityFunction continents() {
/*  9 */     return this.continents;
/*    */   }
/*    */   
/*    */   public DensityFunction erosion() {
/*  9 */     return this.erosion;
/*    */   }
/*    */   
/*    */   public DensityFunction depth() {
/*  9 */     return this.depth;
/*    */   }
/*    */   
/*    */   public DensityFunction ridges() {
/*  9 */     return this.ridges;
/*    */   }
/*    */   
/*    */   public DensityFunction preliminarySurfaceLevel() {
/*  9 */     return this.preliminarySurfaceLevel;
/*    */   }
/*    */   
/*    */   public DensityFunction finalDensity() {
/*  9 */     return this.finalDensity;
/*    */   }
/*    */   
/*    */   public DensityFunction veinToggle() {
/*  9 */     return this.veinToggle;
/*    */   }
/*    */   
/*    */   public DensityFunction veinRidged() {
/*  9 */     return this.veinRidged;
/*    */   }
/*    */   
/*    */   public DensityFunction veinGap() {
/*  9 */     return this.veinGap;
/*    */   }
/*    */   
/*    */   private static RecordCodecBuilder<NoiseRouter, DensityFunction> field(String name, Function<NoiseRouter, DensityFunction> getter) {
/* 36 */     return DensityFunction.CODEC.fieldOf(name).forGetter(getter);
/*    */   }
/*    */   
/*    */   static {
/* 39 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)field("barrier", NoiseRouter::barrierNoise), (App)field("fluid_level_floodedness", NoiseRouter::fluidLevelFloodednessNoise), (App)field("fluid_level_spread", NoiseRouter::fluidLevelSpreadNoise), (App)field("lava", NoiseRouter::lavaNoise), (App)field("temperature", NoiseRouter::temperature), (App)field("vegetation", NoiseRouter::vegetation), (App)field("continents", NoiseRouter::continents), (App)field("erosion", NoiseRouter::erosion), (App)field("depth", NoiseRouter::depth), (App)field("ridges", NoiseRouter::ridges), (App)field("preliminary_surface_level", NoiseRouter::preliminarySurfaceLevel), (App)field("final_density", NoiseRouter::finalDensity), (App)field("vein_toggle", NoiseRouter::veinToggle), (App)field("vein_ridged", NoiseRouter::veinRidged), (App)field("vein_gap", NoiseRouter::veinGap)).apply((Applicative)i, NoiseRouter::new));
/*    */   }
/*    */   
/*    */   public NoiseRouter mapAll(DensityFunction.Visitor visitor) {
/* 61 */     return new NoiseRouter(this.barrierNoise
/* 62 */         .mapAll(visitor), this.fluidLevelFloodednessNoise
/* 63 */         .mapAll(visitor), this.fluidLevelSpreadNoise
/* 64 */         .mapAll(visitor), this.lavaNoise
/* 65 */         .mapAll(visitor), this.temperature
/* 66 */         .mapAll(visitor), this.vegetation
/* 67 */         .mapAll(visitor), this.continents
/* 68 */         .mapAll(visitor), this.erosion
/* 69 */         .mapAll(visitor), this.depth
/* 70 */         .mapAll(visitor), this.ridges
/* 71 */         .mapAll(visitor), this.preliminarySurfaceLevel
/* 72 */         .mapAll(visitor), this.finalDensity
/* 73 */         .mapAll(visitor), this.veinToggle
/* 74 */         .mapAll(visitor), this.veinRidged
/* 75 */         .mapAll(visitor), this.veinGap
/* 76 */         .mapAll(visitor));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseRouter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */