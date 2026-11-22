/*   */ package net.minecraft.core.component.predicates;
/*   */ 
/*   */ import net.minecraft.core.component.DataComponentGetter;
/*   */ import net.minecraft.core.component.DataComponentType;
/*   */ 
/*   */ public final class AnyValue extends Record implements DataComponentPredicate {
/*   */   private final DataComponentType<?> type;
/*   */   
/*   */   public AnyValue(DataComponentType<?> type) {
/* 6 */     this.type = type;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/AnyValue;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AnyValue;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/AnyValue;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/AnyValue;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/AnyValue;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/AnyValue;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public DataComponentType<?> type() {
/* 6 */     return this.type;
/*   */   }
/*   */   
/*   */   public boolean matches(DataComponentGetter components) {
/* 9 */     return (components.get(this.type) != null);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\AnyValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */