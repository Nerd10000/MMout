/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class ClampingLazyMaxIntRange extends Record implements OptionInstance.IntRangeBase, OptionInstance.SliderableOrCyclableValueSet<Integer> {
/*     */   private final int minInclusive;
/*     */   
/*     */   private final IntSupplier maxSupplier;
/*     */   
/*     */   private final int encodableMaxInclusive;
/*     */   
/*     */   public ClampingLazyMaxIntRange(int minInclusive, IntSupplier maxSupplier, int encodableMaxInclusive) {
/* 471 */     this.minInclusive = minInclusive;
/* 471 */     this.maxSupplier = maxSupplier;
/* 471 */     this.encodableMaxInclusive = encodableMaxInclusive;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #471	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #471	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #471	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int minInclusive() {
/* 471 */     return this.minInclusive;
/*     */   }
/*     */   
/*     */   public IntSupplier maxSupplier() {
/* 471 */     return this.maxSupplier;
/*     */   }
/*     */   
/*     */   public int encodableMaxInclusive() {
/* 471 */     return this.encodableMaxInclusive;
/*     */   }
/*     */   
/*     */   public Optional<Integer> validateValue(Integer value) {
/* 474 */     return Optional.of(Integer.valueOf(Mth.clamp(value.intValue(), minInclusive(), maxInclusive())));
/*     */   }
/*     */   
/*     */   public int maxInclusive() {
/* 479 */     return this.maxSupplier.getAsInt();
/*     */   }
/*     */   
/*     */   public Codec<Integer> codec() {
/* 485 */     return Codec.INT.validate(value -> {
/*     */           int maxExclusive = this.encodableMaxInclusive + 1;
/* 487 */           return (value.compareTo(Integer.valueOf(this.minInclusive)) >= 0 && value.compareTo(Integer.valueOf(maxExclusive)) <= 0) ? DataResult.success(value) : DataResult.error((), value);
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean createCycleButton() {
/* 496 */     return true;
/*     */   }
/*     */   
/*     */   public CycleButton.ValueListSupplier<Integer> valueListSupplier() {
/* 501 */     return CycleButton.ValueListSupplier.create(IntStream.range(this.minInclusive, maxInclusive() + 1).boxed().toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$ClampingLazyMaxIntRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */