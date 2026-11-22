/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ 
/*     */ public final class Sampler extends Record {
/*     */   private final DensityFunction temperature;
/*     */   
/*     */   private final DensityFunction humidity;
/*     */   
/*     */   private final DensityFunction continentalness;
/*     */   
/*     */   private final DensityFunction erosion;
/*     */   
/*     */   private final DensityFunction depth;
/*     */   
/*     */   private final DensityFunction weirdness;
/*     */   
/*     */   private final List<Climate.ParameterPoint> spawnTarget;
/*     */   
/*     */   public Sampler(DensityFunction temperature, DensityFunction humidity, DensityFunction continentalness, DensityFunction erosion, DensityFunction depth, DensityFunction weirdness, List<Climate.ParameterPoint> spawnTarget) {
/* 446 */     this.temperature = temperature;
/* 446 */     this.humidity = humidity;
/* 446 */     this.continentalness = continentalness;
/* 446 */     this.erosion = erosion;
/* 446 */     this.depth = depth;
/* 446 */     this.weirdness = weirdness;
/* 446 */     this.spawnTarget = spawnTarget;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$Sampler;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #446	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Sampler;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #446	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Sampler;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #446	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Sampler;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DensityFunction temperature() {
/* 446 */     return this.temperature;
/*     */   }
/*     */   
/*     */   public DensityFunction humidity() {
/* 446 */     return this.humidity;
/*     */   }
/*     */   
/*     */   public DensityFunction continentalness() {
/* 446 */     return this.continentalness;
/*     */   }
/*     */   
/*     */   public DensityFunction erosion() {
/* 446 */     return this.erosion;
/*     */   }
/*     */   
/*     */   public DensityFunction depth() {
/* 446 */     return this.depth;
/*     */   }
/*     */   
/*     */   public DensityFunction weirdness() {
/* 446 */     return this.weirdness;
/*     */   }
/*     */   
/*     */   public List<Climate.ParameterPoint> spawnTarget() {
/* 446 */     return this.spawnTarget;
/*     */   }
/*     */   
/*     */   public Climate.TargetPoint sample(int quartX, int quartY, int quartZ) {
/* 456 */     int blockX = QuartPos.toBlock(quartX);
/* 457 */     int blockY = QuartPos.toBlock(quartY);
/* 458 */     int blockZ = QuartPos.toBlock(quartZ);
/* 460 */     DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(blockX, blockY, blockZ);
/* 462 */     return Climate.target(
/* 463 */         (float)this.temperature.compute((DensityFunction.FunctionContext)context), 
/* 464 */         (float)this.humidity.compute((DensityFunction.FunctionContext)context), 
/* 465 */         (float)this.continentalness.compute((DensityFunction.FunctionContext)context), 
/* 466 */         (float)this.erosion.compute((DensityFunction.FunctionContext)context), 
/* 467 */         (float)this.depth.compute((DensityFunction.FunctionContext)context), 
/* 468 */         (float)this.weirdness.compute((DensityFunction.FunctionContext)context));
/*     */   }
/*     */   
/*     */   public BlockPos findSpawnPosition() {
/* 473 */     if (this.spawnTarget.isEmpty())
/* 474 */       return BlockPos.ZERO; 
/* 476 */     return Climate.findSpawnPosition(this.spawnTarget, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$Sampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */