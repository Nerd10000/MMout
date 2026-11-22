/*     */ package net.minecraft.util;
/*     */ 
/*     */ class CountedAppendable implements Appendable {
/*     */   private int totalCount;
/*     */   
/*     */   private final int limit;
/*     */   
/*     */   public CountedAppendable(int limit) {
/* 636 */     this.limit = limit;
/*     */   }
/*     */   
/*     */   private Appendable accountChars(int count) {
/* 640 */     this.totalCount += count;
/* 641 */     if (this.totalCount > this.limit)
/* 642 */       throw new IllegalStateException("Character count over limit: " + this.totalCount + " > " + this.limit); 
/* 644 */     return this;
/*     */   }
/*     */   
/*     */   public Appendable append(CharSequence csq) {
/* 649 */     return accountChars(csq.length());
/*     */   }
/*     */   
/*     */   public Appendable append(CharSequence csq, int start, int end) {
/* 654 */     return accountChars(end - start);
/*     */   }
/*     */   
/*     */   public Appendable append(char c) {
/* 659 */     return accountChars(1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\GsonHelper$CountedAppendable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */