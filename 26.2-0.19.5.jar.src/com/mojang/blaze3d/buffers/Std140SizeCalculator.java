/*    */ package com.mojang.blaze3d.buffers;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class Std140SizeCalculator {
/*    */   private int size;
/*    */   
/*    */   public int get() {
/* 12 */     return this.size;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator align(int alignment) {
/* 16 */     this.size = Mth.roundToward(this.size, alignment);
/* 17 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putFloat() {
/* 21 */     align(4);
/* 22 */     this.size += 4;
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putInt() {
/* 27 */     align(4);
/* 28 */     this.size += 4;
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec2() {
/* 33 */     align(8);
/* 34 */     this.size += 8;
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec2() {
/* 39 */     align(8);
/* 40 */     this.size += 8;
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec3() {
/* 45 */     align(16);
/* 46 */     this.size += 16;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec3() {
/* 51 */     align(16);
/* 52 */     this.size += 16;
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec4() {
/* 57 */     align(16);
/* 58 */     this.size += 16;
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec4() {
/* 63 */     align(16);
/* 64 */     this.size += 16;
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putMat4f() {
/* 69 */     align(16);
/* 70 */     this.size += 64;
/* 71 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\buffers\Std140SizeCalculator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */