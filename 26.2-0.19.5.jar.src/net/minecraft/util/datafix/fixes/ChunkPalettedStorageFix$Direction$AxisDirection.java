/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ public enum AxisDirection {
/* 878 */   POSITIVE(1),
/* 879 */   NEGATIVE(-1);
/*     */   
/*     */   private final int step;
/*     */   
/*     */   AxisDirection(int step) {
/* 885 */     this.step = step;
/*     */   }
/*     */   
/*     */   public int getStep() {
/* 889 */     return this.step;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$Direction$AxisDirection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */