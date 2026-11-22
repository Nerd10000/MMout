/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public final class Alternative<S> extends Record implements Term<S> {
/*    */   private final Term<S>[] elements;
/*    */   
/*    */   public Alternative(Term<S>[] elements) {
/* 45 */     this.elements = elements;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*    */   }
/*    */   
/*    */   public Term<S>[] elements() {
/* 45 */     return this.elements;
/*    */   }
/*    */   
/*    */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 48 */     Control controlForThis = state.acquireControl();
/*    */     try {
/* 50 */       int mark = state.mark();
/* 51 */       scope.splitFrame();
/* 52 */       for (Term<S> element : this.elements) {
/* 53 */         if (element.parse(state, scope, controlForThis)) {
/* 54 */           scope.mergeFrame();
/* 55 */           return true;
/*    */         } 
/* 57 */         scope.clearFrameValues();
/* 58 */         state.restore(mark);
/* 60 */         if (controlForThis.hasCut())
/*    */           break; 
/*    */       } 
/* 64 */       scope.popFrame();
/* 65 */       return false;
/*    */     } finally {
/* 67 */       state.releaseControl();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$Alternative.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */