/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ class Silent implements ParseState<S> {
/* 184 */   private final ErrorCollector<S> silentCollector = new ErrorCollector.Nop<>();
/*     */   
/*     */   public ErrorCollector<S> errorCollector() {
/* 188 */     return this.silentCollector;
/*     */   }
/*     */   
/*     */   public Scope scope() {
/* 193 */     return CachedParseState.this.scope();
/*     */   }
/*     */   
/*     */   public <T> T parse(NamedRule<S, T> rule) {
/* 198 */     return CachedParseState.this.parse(rule);
/*     */   }
/*     */   
/*     */   public S input() {
/* 203 */     return CachedParseState.this.input();
/*     */   }
/*     */   
/*     */   public int mark() {
/* 208 */     return CachedParseState.this.mark();
/*     */   }
/*     */   
/*     */   public void restore(int mark) {
/* 213 */     CachedParseState.this.restore(mark);
/*     */   }
/*     */   
/*     */   public Control acquireControl() {
/* 218 */     return CachedParseState.this.acquireControl();
/*     */   }
/*     */   
/*     */   public void releaseControl() {
/* 223 */     CachedParseState.this.releaseControl();
/*     */   }
/*     */   
/*     */   public ParseState<S> silent() {
/* 228 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\CachedParseState$Silent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */