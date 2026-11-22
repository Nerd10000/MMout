/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos> {
/* 576 */   private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private int index;
/*     */   
/*     */   protected BlockPos computeNext() {
/* 581 */     if (this.index == end)
/* 582 */       return (BlockPos)endOfData(); 
/* 585 */     int x = this.index % width;
/* 586 */     int slice = this.index / width;
/* 587 */     int y = slice % height;
/* 588 */     int z = slice / height;
/* 590 */     this.index++;
/* 591 */     return this.cursor.set(minX + x, minY + y, minZ + z);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */