/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public interface Rule<S, T> {
/*    */   T parse(ParseState<S> paramParseState);
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface SimpleRuleAction<S, T> extends RuleAction<S, T> {
/*    */     T run(Scope param1Scope);
/*    */     
/*    */     default T run(ParseState<S> state) {
/* 21 */       return run(state.scope());
/*    */     }
/*    */   }
/*    */   
/*    */   static <S, T> Rule<S, T> fromTerm(Term<S> child, RuleAction<S, T> action) {
/* 26 */     return new WrappedTerm<>(action, child);
/*    */   }
/*    */   
/*    */   static <S, T> Rule<S, T> fromTerm(Term<S> child, SimpleRuleAction<S, T> action) {
/* 30 */     return new WrappedTerm<>(action, child);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface RuleAction<S, T> {
/*    */     T run(ParseState<S> param1ParseState);
/*    */   }
/*    */   
/*    */   public static final class WrappedTerm<S, T> extends Record implements Rule<S, T> {
/*    */     private final Rule.RuleAction<S, T> action;
/*    */     
/*    */     private final Term<S> child;
/*    */     
/*    */     public WrappedTerm(Rule.RuleAction<S, T> action, Term<S> child) {
/* 33 */       this.action = action;
/* 33 */       this.child = child;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>;
/*    */     }
/*    */     
/*    */     public Rule.RuleAction<S, T> action() {
/* 33 */       return this.action;
/*    */     }
/*    */     
/*    */     public Term<S> child() {
/* 33 */       return this.child;
/*    */     }
/*    */     
/*    */     public T parse(ParseState<S> state) {
/* 36 */       Scope scope = state.scope();
/* 37 */       scope.pushFrame();
/*    */       try {
/* 39 */         if (this.child.parse(state, scope, Control.UNBOUND))
/* 42 */           return this.action.run(state); 
/* 44 */         return null;
/*    */       } finally {
/* 47 */         scope.popFrame();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Rule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */