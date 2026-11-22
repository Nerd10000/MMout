/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ 
/*     */ class null extends AbstractIterator<BlockPos> {
/* 466 */   private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private int currentDepth;
/*     */   
/*     */   private int maxX;
/*     */   
/*     */   private int maxY;
/*     */   
/*     */   private int x;
/*     */   
/*     */   private int y;
/*     */   
/*     */   private boolean zMirror;
/*     */   
/*     */   protected BlockPos computeNext() {
/* 479 */     if (this.zMirror) {
/* 480 */       this.zMirror = false;
/* 481 */       this.cursor.setZ(originZ - this.cursor.getZ() - originZ);
/* 482 */       return this.cursor;
/*     */     } 
/* 485 */     BlockPos found = null;
/* 486 */     while (found == null) {
/* 487 */       if (this.y > this.maxY) {
/* 488 */         this.x++;
/* 489 */         if (this.x > this.maxX) {
/* 490 */           this.currentDepth++;
/* 491 */           if (this.currentDepth > maxDepth)
/* 492 */             return (BlockPos)endOfData(); 
/* 494 */           this.maxX = Math.min(reachX, this.currentDepth);
/* 495 */           this.x = -this.maxX;
/*     */         } 
/* 497 */         this.maxY = Math.min(reachY, this.currentDepth - Math.abs(this.x));
/* 498 */         this.y = -this.maxY;
/*     */       } 
/* 501 */       int xx = this.x;
/* 502 */       int yy = this.y;
/* 503 */       int zz = this.currentDepth - Math.abs(xx) - Math.abs(yy);
/* 504 */       if (zz <= reachZ) {
/* 505 */         this.zMirror = (zz != 0);
/* 506 */         found = this.cursor.set(originX + xx, originY + yy, originZ + zz);
/*     */       } 
/* 508 */       this.y++;
/*     */     } 
/* 510 */     return found;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */