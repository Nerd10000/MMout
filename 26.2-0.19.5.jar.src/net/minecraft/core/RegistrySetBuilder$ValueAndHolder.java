/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Optional;
/*     */ 
/*     */ final class ValueAndHolder<T> extends Record {
/*     */   private final RegistrySetBuilder.RegisteredValue<T> value;
/*     */   
/*     */   private final Optional<Holder.Reference<T>> holder;
/*     */   
/*     */   private ValueAndHolder(RegistrySetBuilder.RegisteredValue<T> value, Optional<Holder.Reference<T>> holder) {
/* 194 */     this.value = value;
/* 194 */     this.holder = holder;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #194	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #194	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #194	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$ValueAndHolder<TT;>;
/*     */   }
/*     */   
/*     */   public RegistrySetBuilder.RegisteredValue<T> value() {
/* 194 */     return this.value;
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> holder() {
/* 194 */     return this.holder;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$ValueAndHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */