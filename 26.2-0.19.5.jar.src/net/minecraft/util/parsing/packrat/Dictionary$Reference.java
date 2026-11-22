/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ final class Reference<S, T> extends Record implements Term<S> {
/*    */   private final Dictionary.Entry<S, T> ruleToParse;
/*    */   
/*    */   private final Atom<T> nameToStore;
/*    */   
/*    */   private Reference(Dictionary.Entry<S, T> ruleToParse, Atom<T> nameToStore) {
/* 62 */     this.ruleToParse = ruleToParse;
/* 62 */     this.nameToStore = nameToStore;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */   }
/*    */   
/*    */   public Dictionary.Entry<S, T> ruleToParse() {
/* 62 */     return this.ruleToParse;
/*    */   }
/*    */   
/*    */   public Atom<T> nameToStore() {
/* 62 */     return this.nameToStore;
/*    */   }
/*    */   
/*    */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 65 */     T result = state.parse(this.ruleToParse);
/* 66 */     if (result == null)
/* 67 */       return false; 
/* 69 */     scope.put(this.nameToStore, result);
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Dictionary$Reference.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */