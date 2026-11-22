/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Parameter extends Record {
/*     */   private final long min;
/*     */   
/*     */   private final long max;
/*     */   
/*     */   public static final Codec<Parameter> CODEC;
/*     */   
/*     */   public Parameter(long min, long max) {
/* 386 */     this.min = min;
/* 386 */     this.max = max;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Climate$Parameter;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #386	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Climate$Parameter;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #386	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/Climate$Parameter;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long min() {
/* 386 */     return this.min;
/*     */   }
/*     */   
/*     */   public long max() {
/* 386 */     return this.max;
/*     */   }
/*     */   
/*     */   static {
/* 387 */     CODEC = ExtraCodecs.intervalCodec(Codec.floatRange(-2.0F, 2.0F), "min", "max", (min, max) -> (min.compareTo(max) > 0) ? DataResult.error(()) : DataResult.success(new Parameter(Climate.quantizeCoord(min.floatValue()), Climate.quantizeCoord(max.floatValue()))), p -> Float.valueOf(Climate.unquantizeCoord(p.min())), p -> Float.valueOf(Climate.unquantizeCoord(p.max())));
/*     */   }
/*     */   
/*     */   public static Parameter point(float min) {
/* 395 */     return span(min, min);
/*     */   }
/*     */   
/*     */   public static Parameter span(float min, float max) {
/* 399 */     if (min > max)
/* 400 */       throw new IllegalArgumentException("min > max: " + min + " " + max); 
/* 402 */     return new Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
/*     */   }
/*     */   
/*     */   public static Parameter span(Parameter min, Parameter max) {
/* 406 */     if (min.min() > max.max())
/* 407 */       throw new IllegalArgumentException("min > max: " + String.valueOf(min) + " " + String.valueOf(max)); 
/* 409 */     return new Parameter(min.min(), max.max());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 414 */     return (this.min == this.max) ? String.format(Locale.ROOT, "%d", new Object[] { Long.valueOf(this.min) }) : String.format(Locale.ROOT, "[%d-%d]", new Object[] { Long.valueOf(this.min), Long.valueOf(this.max) });
/*     */   }
/*     */   
/*     */   public long distance(long target) {
/* 418 */     long above = target - this.max;
/* 419 */     long below = this.min - target;
/* 420 */     if (above > 0L)
/* 421 */       return above; 
/* 423 */     return Math.max(below, 0L);
/*     */   }
/*     */   
/*     */   public long distance(Parameter target) {
/* 427 */     long above = target.min() - this.max;
/* 428 */     long below = this.min - target.max();
/* 430 */     if (above > 0L)
/* 431 */       return above; 
/* 433 */     return Math.max(below, 0L);
/*     */   }
/*     */   
/*     */   public Parameter span(Parameter other) {
/* 437 */     return (other == null) ? this : new Parameter(Math.min(this.min, other.min()), Math.max(this.max, other.max()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$Parameter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */