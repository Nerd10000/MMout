/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos> {
/* 439 */   private final BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/*     */   
/* 440 */   private int counter = limit;
/*     */   
/*     */   protected BlockPos computeNext() {
/* 444 */     if (this.counter <= 0)
/* 445 */       return (BlockPos)endOfData(); 
/* 448 */     BlockPos next = this.nextPos.set(minX + random
/* 449 */         .nextInt(width), minY + random
/* 450 */         .nextInt(height), minZ + random
/* 451 */         .nextInt(depth));
/* 453 */     this.counter--;
/* 454 */     return next;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */