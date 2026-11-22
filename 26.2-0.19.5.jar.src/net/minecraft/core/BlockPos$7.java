/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos> {
/* 770 */   private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private int firstIndex;
/*     */   
/*     */   private int secondIndex;
/*     */   
/*     */   private int thirdIndex;
/*     */   
/*     */   private boolean end;
/*     */   
/* 776 */   private final int firstDirX = firstVisitDir.getStepX();
/*     */   
/* 777 */   private final int firstDirY = firstVisitDir.getStepY();
/*     */   
/* 778 */   private final int firstDirZ = firstVisitDir.getStepZ();
/*     */   
/* 780 */   private final int secondDirX = secondVisitDir.getStepX();
/*     */   
/* 781 */   private final int secondDirY = secondVisitDir.getStepY();
/*     */   
/* 782 */   private final int secondDirZ = secondVisitDir.getStepZ();
/*     */   
/* 784 */   private final int thirdDirX = thirdVisitDir.getStepX();
/*     */   
/* 785 */   private final int thirdDirY = thirdVisitDir.getStepY();
/*     */   
/* 786 */   private final int thirdDirZ = thirdVisitDir.getStepZ();
/*     */   
/*     */   protected BlockPos computeNext() {
/* 790 */     if (this.end)
/* 791 */       return (BlockPos)endOfData(); 
/* 794 */     this.cursor.set(startCornerX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex, startCornerY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex, startCornerZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex);
/* 800 */     if (this.thirdIndex < thirdMax) {
/* 801 */       this.thirdIndex++;
/* 802 */     } else if (this.secondIndex < secondMax) {
/* 803 */       this.secondIndex++;
/* 804 */       this.thirdIndex = 0;
/* 805 */     } else if (this.firstIndex < firstMax) {
/* 806 */       this.firstIndex++;
/* 807 */       this.thirdIndex = 0;
/* 808 */       this.secondIndex = 0;
/*     */     } else {
/* 810 */       this.end = true;
/*     */     } 
/* 813 */     return this.cursor;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */