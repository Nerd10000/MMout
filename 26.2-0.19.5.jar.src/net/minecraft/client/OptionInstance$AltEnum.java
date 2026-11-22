/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ public final class AltEnum<T> extends Record implements OptionInstance.CycleableValueSet<T> {
/*     */   private final List<T> values;
/*     */   
/*     */   private final List<T> altValues;
/*     */   
/*     */   private final BooleanSupplier altCondition;
/*     */   
/*     */   private final OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   public AltEnum(List<T> values, List<T> altValues, BooleanSupplier altCondition, OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter, Codec<T> codec) {
/* 239 */     this.values = values;
/* 239 */     this.altValues = altValues;
/* 239 */     this.altCondition = altCondition;
/* 239 */     this.valueSetter = valueSetter;
/* 239 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$AltEnum;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$AltEnum;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$AltEnum;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */   }
/*     */   
/*     */   public List<T> values() {
/* 239 */     return this.values;
/*     */   }
/*     */   
/*     */   public List<T> altValues() {
/* 239 */     return this.altValues;
/*     */   }
/*     */   
/*     */   public BooleanSupplier altCondition() {
/* 239 */     return this.altCondition;
/*     */   }
/*     */   
/*     */   public OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter() {
/* 239 */     return this.valueSetter;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 239 */     return this.codec;
/*     */   }
/*     */   
/*     */   public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 242 */     return CycleButton.ValueListSupplier.create(this.altCondition, this.values, this.altValues);
/*     */   }
/*     */   
/*     */   public Optional<T> validateValue(T value) {
/* 247 */     return (this.altCondition.getAsBoolean() ? this.altValues : this.values).contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$AltEnum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */