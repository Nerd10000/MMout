/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class IntRange extends Record implements OptionInstance.IntRangeBase {
/*     */   private final int minInclusive;
/*     */   
/*     */   private final int maxInclusive;
/*     */   
/*     */   private final boolean applyValueImmediately;
/*     */   
/*     */   public IntRange(int minInclusive, int maxInclusive, boolean applyValueImmediately) {
/* 455 */     this.minInclusive = minInclusive;
/* 455 */     this.maxInclusive = maxInclusive;
/* 455 */     this.applyValueImmediately = applyValueImmediately;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$IntRange;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #455	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$IntRange;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #455	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$IntRange;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #455	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int minInclusive() {
/* 455 */     return this.minInclusive;
/*     */   }
/*     */   
/*     */   public int maxInclusive() {
/* 455 */     return this.maxInclusive;
/*     */   }
/*     */   
/*     */   public boolean applyValueImmediately() {
/* 455 */     return this.applyValueImmediately;
/*     */   }
/*     */   
/*     */   public IntRange(int minInclusive, int maxInclusive) {
/* 457 */     this(minInclusive, maxInclusive, true);
/*     */   }
/*     */   
/*     */   public Optional<Integer> validateValue(Integer value) {
/* 462 */     return (value.compareTo(Integer.valueOf(minInclusive())) >= 0 && value.compareTo(Integer.valueOf(maxInclusive())) <= 0) ? Optional.<Integer>of(value) : Optional.<Integer>empty();
/*     */   }
/*     */   
/*     */   public Codec<Integer> codec() {
/* 467 */     return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$IntRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */