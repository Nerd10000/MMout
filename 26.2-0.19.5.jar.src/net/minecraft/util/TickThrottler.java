/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class TickThrottler {
/*    */   private final int incrementStep;
/*    */   
/*    */   private final int threshold;
/*    */   
/*    */   private int count;
/*    */   
/*    */   public TickThrottler(int incrementStep, int threshold) {
/*  9 */     this.incrementStep = incrementStep;
/* 10 */     this.threshold = threshold;
/*    */   }
/*    */   
/*    */   public void increment() {
/* 14 */     this.count += this.incrementStep;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.count > 0)
/* 19 */       this.count--; 
/*    */   }
/*    */   
/*    */   public boolean isUnderThreshold() {
/* 24 */     return (this.threshold <= 0 || this.count < this.threshold);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\TickThrottler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */