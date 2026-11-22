/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ class SimpleControl implements Control {
/*     */   private boolean hasCut;
/*     */   
/*     */   public void cut() {
/* 237 */     this.hasCut = true;
/*     */   }
/*     */   
/*     */   public boolean hasCut() {
/* 242 */     return this.hasCut;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 246 */     this.hasCut = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\CachedParseState$SimpleControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */