/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ final class BakedEntry<T> extends Record {
/*     */   private final Holder.Reference<T> element;
/*     */   
/*     */   private final DataComponentMap components;
/*     */   
/*     */   private BakedEntry(Holder.Reference<T> element, DataComponentMap components) {
/* 104 */     this.element = element;
/* 104 */     this.components = components;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> element() {
/* 104 */     return this.element;
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/* 104 */     return this.components;
/*     */   }
/*     */   
/*     */   public void apply() {
/* 109 */     this.element.bindComponents(this.components);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$BakedEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */