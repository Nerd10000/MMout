/*    */ package net.minecraft.world.level.border;
/*    */ 
/*    */ public enum BorderStatus {
/*  4 */   GROWING(4259712),
/*  5 */   SHRINKING(16724016),
/*  6 */   STATIONARY(2138367);
/*    */   
/*    */   private final int color;
/*    */   
/*    */   BorderStatus(int color) {
/* 12 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int getColor() {
/* 16 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\BorderStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */