/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.OptionalInt;
/*     */ 
/*     */ public final class Range extends Column {
/*     */   private final int floor;
/*     */   
/*     */   private final int ceiling;
/*     */   
/*     */   public Range(int floor, int ceiling) {
/* 119 */     this.floor = floor;
/* 120 */     this.ceiling = ceiling;
/* 121 */     if (height() < 0)
/* 122 */       throw new IllegalArgumentException("Column of negative height: " + String.valueOf(this)); 
/*     */   }
/*     */   
/*     */   public OptionalInt getCeiling() {
/* 128 */     return OptionalInt.of(this.ceiling);
/*     */   }
/*     */   
/*     */   public OptionalInt getFloor() {
/* 133 */     return OptionalInt.of(this.floor);
/*     */   }
/*     */   
/*     */   public OptionalInt getHeight() {
/* 138 */     return OptionalInt.of(height());
/*     */   }
/*     */   
/*     */   public int ceiling() {
/* 142 */     return this.ceiling;
/*     */   }
/*     */   
/*     */   public int floor() {
/* 146 */     return this.floor;
/*     */   }
/*     */   
/*     */   public int height() {
/* 150 */     return this.ceiling - this.floor - 1;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 155 */     return "C(" + this.ceiling + "-" + this.floor + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Column$Range.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */