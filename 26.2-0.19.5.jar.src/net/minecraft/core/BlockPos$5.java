/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos> {
/* 609 */   private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private int index;
/*     */   
/*     */   protected BlockPos computeNext() {
/* 614 */     if (this.index == stepCount)
/* 615 */       return (BlockPos)endOfData(); 
/* 618 */     int y = this.index % height;
/* 619 */     int stepIndex = this.index / height;
/* 620 */     Vec3i step = steps[stepIndex];
/* 621 */     this.index++;
/* 622 */     return this.cursor.set(startX + step.getX(), startY + y * yDirection, startZ + step.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */