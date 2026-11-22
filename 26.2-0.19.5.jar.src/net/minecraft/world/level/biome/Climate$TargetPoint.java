/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ 
/*     */ public final class TargetPoint extends Record {
/*     */   private final long temperature;
/*     */   
/*     */   private final long humidity;
/*     */   
/*     */   private final long continentalness;
/*     */   
/*     */   private final long erosion;
/*     */   
/*     */   private final long depth;
/*     */   
/*     */   private final long weirdness;
/*     */   
/*     */   public TargetPoint(long temperature, long humidity, long continentalness, long erosion, long depth, long weirdness) {
/* 334 */     this.temperature = temperature;
/* 334 */     this.humidity = humidity;
/* 334 */     this.continentalness = continentalness;
/* 334 */     this.erosion = erosion;
/* 334 */     this.depth = depth;
/* 334 */     this.weirdness = weirdness;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #334	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #334	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$TargetPoint;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #334	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$TargetPoint;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long temperature() {
/* 334 */     return this.temperature;
/*     */   }
/*     */   
/*     */   public long humidity() {
/* 334 */     return this.humidity;
/*     */   }
/*     */   
/*     */   public long continentalness() {
/* 334 */     return this.continentalness;
/*     */   }
/*     */   
/*     */   public long erosion() {
/* 334 */     return this.erosion;
/*     */   }
/*     */   
/*     */   public long depth() {
/* 334 */     return this.depth;
/*     */   }
/*     */   
/*     */   public long weirdness() {
/* 334 */     return this.weirdness;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   long[] toParameterArray() {
/* 338 */     return new long[] { this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, 0L };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$TargetPoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */