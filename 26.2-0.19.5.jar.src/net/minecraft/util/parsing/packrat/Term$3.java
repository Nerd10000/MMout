/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ class null implements Term<S> {
/*     */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 245 */     state.errorCollector().store(state.mark(), message);
/* 246 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 251 */     return "fail";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */