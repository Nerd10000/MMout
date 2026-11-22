/*     */ package net.minecraft.util.profiling.jfr.parse;
/*     */ 
/*     */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*     */ 
/*     */ public final class MutableCountAndSize {
/*     */   private long count;
/*     */   
/*     */   private long totalSize;
/*     */   
/*     */   public void increment(int bytes) {
/* 181 */     this.totalSize += bytes;
/* 182 */     this.count++;
/*     */   }
/*     */   
/*     */   public IoSummary.CountAndSize toCountAndSize() {
/* 186 */     return new IoSummary.CountAndSize(this.count, this.totalSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsParser$MutableCountAndSize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */