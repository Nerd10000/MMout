/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public final class BuiltinKey extends Record {
/*    */   private final String id;
/*    */   
/*    */   private final int argCount;
/*    */   
/*    */   public BuiltinKey(String id, int argCount) {
/* 91 */     this.id = id;
/* 91 */     this.argCount = argCount;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/nbt/SnbtOperations$BuiltinKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String id() {
/* 91 */     return this.id;
/*    */   }
/*    */   
/*    */   public int argCount() {
/* 91 */     return this.argCount;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 94 */     return this.id + "/" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtOperations$BuiltinKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */