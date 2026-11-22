/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ public final class LookAhead<S> extends Record implements Term<S> {
/*     */   private final Term<S> term;
/*     */   
/*     */   private final boolean positive;
/*     */   
/*     */   public LookAhead(Term<S> term, boolean positive) {
/* 197 */     this.term = term;
/* 197 */     this.positive = positive;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #197	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #197	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #197	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */   }
/*     */   
/*     */   public Term<S> term() {
/* 197 */     return this.term;
/*     */   }
/*     */   
/*     */   public boolean positive() {
/* 197 */     return this.positive;
/*     */   }
/*     */   
/*     */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 203 */     int mark = state.mark();
/* 205 */     boolean result = this.term.parse(state.silent(), scope, control);
/* 206 */     state.restore(mark);
/* 207 */     return (this.positive == result);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$LookAhead.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */