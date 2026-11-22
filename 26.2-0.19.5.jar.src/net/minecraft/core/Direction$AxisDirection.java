/*     */ package net.minecraft.core;
/*     */ 
/*     */ public enum AxisDirection {
/* 543 */   POSITIVE(1, "Towards positive"),
/* 544 */   NEGATIVE(-1, "Towards negative");
/*     */   
/*     */   private final int step;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   AxisDirection(int step, String name) {
/* 551 */     this.step = step;
/* 552 */     this.name = name;
/*     */   }
/*     */   
/*     */   public int getStep() {
/* 556 */     return this.step;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 560 */     return this.name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 565 */     return this.name;
/*     */   }
/*     */   
/*     */   public AxisDirection opposite() {
/* 569 */     return (this == POSITIVE) ? NEGATIVE : POSITIVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Direction$AxisDirection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */