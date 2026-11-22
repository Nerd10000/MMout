/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ 
/*     */ public final class Enum<T> extends Record implements OptionInstance.CycleableValueSet<T> {
/*     */   private final List<T> values;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   public Enum(List<T> values, Codec<T> codec) {
/* 251 */     this.values = values;
/* 251 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$Enum;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #251	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$Enum;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #251	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$Enum;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #251	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */   }
/*     */   
/*     */   public List<T> values() {
/* 251 */     return this.values;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 251 */     return this.codec;
/*     */   }
/*     */   
/*     */   public Optional<T> validateValue(T value) {
/* 254 */     return this.values.contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */   }
/*     */   
/*     */   public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 259 */     return CycleButton.ValueListSupplier.create(this.values);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$Enum.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */