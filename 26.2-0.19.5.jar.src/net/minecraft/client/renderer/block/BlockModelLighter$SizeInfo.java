/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ enum SizeInfo {
/* 382 */   DOWN(0),
/* 383 */   UP(1),
/* 384 */   NORTH(2),
/* 385 */   SOUTH(3),
/* 386 */   WEST(4),
/* 387 */   EAST(5),
/* 388 */   FLIP_DOWN(6),
/* 389 */   FLIP_UP(7),
/* 390 */   FLIP_NORTH(8),
/* 391 */   FLIP_SOUTH(9),
/* 392 */   FLIP_WEST(10),
/* 393 */   FLIP_EAST(11);
/*     */   
/*     */   public static final int COUNT;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   static {
/* 395 */     COUNT = (values()).length;
/*     */   }
/*     */   
/*     */   SizeInfo(int index) {
/* 400 */     this.index = index;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelLighter$SizeInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */