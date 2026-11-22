/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface SimpleRuleAction<S, T> extends Rule.RuleAction<S, T> {
/*    */   T run(Scope paramScope);
/*    */   
/*    */   default T run(ParseState<S> state) {
/* 21 */     return run(state.scope());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Rule$SimpleRuleAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */