/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class SliderableEnum<T> extends Record implements OptionInstance.SliderableValueSet<T> {
/*     */   private final List<T> values;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   public SliderableEnum(List<T> values, Codec<T> codec) {
/* 505 */     this.values = values;
/* 505 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #505	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #505	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$SliderableEnum;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #505	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */   }
/*     */   
/*     */   public List<T> values() {
/* 505 */     return this.values;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 505 */     return this.codec;
/*     */   }
/*     */   
/*     */   public double toSliderValue(T value) {
/* 510 */     if (value == this.values.getFirst())
/* 511 */       return 0.0D; 
/* 512 */     if (value == this.values.getLast())
/* 513 */       return 1.0D; 
/* 515 */     return Mth.map(this.values.indexOf(value), 0.0D, (this.values.size() - 1), 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public Optional<T> next(T current) {
/* 520 */     int currentIntex = this.values.indexOf(current);
/* 521 */     int nextIndex = Mth.clamp(currentIntex + 1, 0, this.values.size() - 1);
/* 522 */     return Optional.of(this.values.get(nextIndex));
/*     */   }
/*     */   
/*     */   public Optional<T> previous(T current) {
/* 527 */     int currentIntex = this.values.indexOf(current);
/* 528 */     int previousIndex = Mth.clamp(currentIntex - 1, 0, this.values.size() - 1);
/* 529 */     return Optional.of(this.values.get(previousIndex));
/*     */   }
/*     */   
/*     */   public T fromSliderValue(double slider) {
/* 534 */     if (slider >= 1.0D)
/* 535 */       slider = 0.9999899864196777D; 
/* 537 */     int index = Mth.floor(Mth.map(slider, 0.0D, 1.0D, 0.0D, this.values.size()));
/* 538 */     return this.values.get(Mth.clamp(index, 0, this.values.size() - 1));
/*     */   }
/*     */   
/*     */   public Optional<T> validateValue(T value) {
/* 543 */     int index = this.values.indexOf(value);
/* 544 */     return (index > -1) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$SliderableEnum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */