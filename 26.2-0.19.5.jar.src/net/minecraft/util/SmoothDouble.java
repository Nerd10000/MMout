/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class SmoothDouble {
/*    */   private double targetValue;
/*    */   
/*    */   private double remainingValue;
/*    */   
/*    */   private double lastAmount;
/*    */   
/*    */   public double getNewDeltaValue(double targetDelta, double time) {
/*  9 */     this.targetValue += targetDelta;
/* 11 */     double delta = this.targetValue - this.remainingValue;
/* 13 */     double newLastAmount = Mth.lerp(0.5D, this.lastAmount, delta);
/* 16 */     double deltaSign = Math.signum(delta);
/* 17 */     if (deltaSign * delta > deltaSign * this.lastAmount)
/* 18 */       delta = newLastAmount; 
/* 21 */     this.lastAmount = newLastAmount;
/* 22 */     this.remainingValue += delta * time;
/* 24 */     return delta * time;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 28 */     this.targetValue = 0.0D;
/* 29 */     this.remainingValue = 0.0D;
/* 30 */     this.lastAmount = 0.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SmoothDouble.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */