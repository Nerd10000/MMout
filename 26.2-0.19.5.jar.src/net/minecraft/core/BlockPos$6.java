/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos.MutableBlockPos> {
/* 631 */   private final Direction[] directions = new Direction[] { this.val$firstDirection, this.val$secondDirection, this.val$firstDirection
/*     */       
/* 634 */       .getOpposite(), this.val$secondDirection
/* 635 */       .getOpposite() };
/*     */   
/* 637 */   private final BlockPos.MutableBlockPos cursor = center.mutable().move(secondDirection);
/*     */   
/* 638 */   private final int legs = 4 * radius;
/*     */   
/* 639 */   private int leg = -1;
/*     */   
/*     */   private int legSize;
/*     */   
/*     */   private int legIndex;
/*     */   
/* 643 */   private int lastX = this.cursor.getX();
/*     */   
/* 644 */   private int lastY = this.cursor.getY();
/*     */   
/* 645 */   private int lastZ = this.cursor.getZ();
/*     */   
/*     */   protected BlockPos.MutableBlockPos computeNext() {
/* 649 */     this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
/* 651 */     this.lastX = this.cursor.getX();
/* 652 */     this.lastY = this.cursor.getY();
/* 653 */     this.lastZ = this.cursor.getZ();
/* 655 */     if (this.legIndex >= this.legSize) {
/* 656 */       if (this.leg >= this.legs)
/* 657 */         return (BlockPos.MutableBlockPos)endOfData(); 
/* 659 */       this.leg++;
/* 660 */       this.legIndex = 0;
/* 661 */       this.legSize = this.leg / 2 + 1;
/*     */     } 
/* 663 */     this.legIndex++;
/* 665 */     return this.cursor;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */