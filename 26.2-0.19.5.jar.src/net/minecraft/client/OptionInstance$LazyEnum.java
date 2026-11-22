/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ public final class LazyEnum<T> extends Record implements OptionInstance.CycleableValueSet<T> {
/*     */   private final Supplier<List<T>> values;
/*     */   
/*     */   private final Function<T, Optional<T>> validateValue;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   public LazyEnum(Supplier<List<T>> values, Function<T, Optional<T>> validateValue, Codec<T> codec) {
/* 263 */     this.values = values;
/* 263 */     this.validateValue = validateValue;
/* 263 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$LazyEnum;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #263	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$LazyEnum;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #263	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$LazyEnum;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #263	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */   }
/*     */   
/*     */   public Supplier<List<T>> values() {
/* 263 */     return this.values;
/*     */   }
/*     */   
/*     */   public Function<T, Optional<T>> validateValue() {
/* 263 */     return this.validateValue;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 263 */     return this.codec;
/*     */   }
/*     */   
/*     */   public Optional<T> validateValue(T value) {
/* 266 */     return this.validateValue.apply(value);
/*     */   }
/*     */   
/*     */   public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 271 */     return CycleButton.ValueListSupplier.create(this.values.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$LazyEnum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */