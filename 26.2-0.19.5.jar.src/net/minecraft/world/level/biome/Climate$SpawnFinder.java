/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class SpawnFinder {
/*     */   private static final long MAX_RADIUS = 2048L;
/*     */   
/*     */   private Result result;
/*     */   
/*     */   private static final class Result extends Record {
/*     */     private final BlockPos location;
/*     */     
/*     */     private final long fitness;
/*     */     
/*     */     private Result(BlockPos location, long fitness) {
/* 483 */       this.location = location;
/* 483 */       this.fitness = fitness;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #483	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$SpawnFinder$Result;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos location() {
/* 483 */       return this.location;
/*     */     }
/*     */     
/*     */     public long fitness() {
/* 483 */       return this.fitness;
/*     */     }
/*     */   }
/*     */   
/*     */   private SpawnFinder(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler) {
/* 489 */     this.result = getSpawnPositionAndFitness(targetClimates, sampler, 0, 0);
/* 492 */     radialSearch(targetClimates, sampler, 2048.0F, 512.0F);
/* 494 */     radialSearch(targetClimates, sampler, 512.0F, 32.0F);
/*     */   }
/*     */   
/*     */   private void radialSearch(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, float maxRadius, float radiusIncrement) {
/* 498 */     float angle = 0.0F;
/* 499 */     float radius = radiusIncrement;
/* 500 */     BlockPos searchOrigin = this.result.location();
/* 501 */     while (radius <= maxRadius) {
/* 502 */       int x = searchOrigin.getX() + (int)(Math.sin(angle) * radius);
/* 503 */       int z = searchOrigin.getZ() + (int)(Math.cos(angle) * radius);
/* 504 */       Result candidate = getSpawnPositionAndFitness(targetClimates, sampler, x, z);
/* 505 */       if (candidate.fitness() < this.result.fitness())
/* 506 */         this.result = candidate; 
/* 509 */       angle += radiusIncrement / radius;
/* 510 */       if (angle > 6.283185307179586D) {
/* 511 */         angle = 0.0F;
/* 512 */         radius += radiusIncrement;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Result getSpawnPositionAndFitness(List<Climate.ParameterPoint> targetClimates, Climate.Sampler sampler, int blockX, int blockZ) {
/* 518 */     Climate.TargetPoint targetPoint = sampler.sample(QuartPos.fromBlock(blockX), 0, QuartPos.fromBlock(blockZ));
/* 525 */     Climate.TargetPoint zeroDepthTargetPoint = new Climate.TargetPoint(targetPoint.temperature(), targetPoint.humidity(), targetPoint.continentalness(), targetPoint.erosion(), 0L, targetPoint.weirdness());
/* 528 */     long minFitness = Long.MAX_VALUE;
/* 529 */     for (Climate.ParameterPoint point : targetClimates)
/* 530 */       minFitness = Math.min(minFitness, point.fitness(zeroDepthTargetPoint)); 
/* 533 */     long distanceBiasToWorldOrigin = Mth.square(blockX) + Mth.square(blockZ);
/* 535 */     long fitnessWithDistance = minFitness * Mth.square(2048L) + distanceBiasToWorldOrigin;
/* 536 */     return new Result(new BlockPos(blockX, 0, blockZ), fitnessWithDistance);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$SpawnFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */