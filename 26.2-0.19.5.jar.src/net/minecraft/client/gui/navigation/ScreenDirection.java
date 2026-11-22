/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*    */ 
/*    */ public enum ScreenDirection {
/*  6 */   UP, DOWN, LEFT, RIGHT;
/*    */   
/*    */   ScreenDirection() {
/* 12 */     this.coordinateValueComparator = ((k1, k2) -> (k1 == k2) ? 0 : (isBefore(k1, k2) ? -1 : 1));
/*    */   }
/*    */   
/*    */   private final IntComparator coordinateValueComparator;
/*    */   
/*    */   public ScreenAxis getAxis() {
/* 15 */     switch (ordinal()) {
/*    */       default:
/* 15 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 15 */     return 
/*    */       
/* 17 */       ScreenAxis.HORIZONTAL;
/*    */   }
/*    */   
/*    */   public ScreenDirection getOpposite() {
/* 22 */     switch (ordinal()) {
/*    */       default:
/* 22 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 22 */     return 
/*    */       
/* 26 */       LEFT;
/*    */   }
/*    */   
/*    */   public boolean isPositive() {
/* 31 */     switch (ordinal()) {
/*    */       default:
/* 31 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 2:
/*    */       
/*    */       case 1:
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isAfter(int a, int b) {
/* 42 */     if (isPositive())
/* 43 */       return (a > b); 
/* 45 */     return (b > a);
/*    */   }
/*    */   
/*    */   public boolean isBefore(int a, int b) {
/* 54 */     if (isPositive())
/* 55 */       return (a < b); 
/* 57 */     return (b < a);
/*    */   }
/*    */   
/*    */   public IntComparator coordinateValueComparator() {
/* 62 */     return this.coordinateValueComparator;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\navigation\ScreenDirection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */