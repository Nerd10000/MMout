/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ public enum ScreenAxis {
/*  4 */   HORIZONTAL, VERTICAL;
/*    */   
/*    */   public ScreenAxis orthogonal() {
/*  9 */     switch (ordinal()) {
/*    */       default:
/*  9 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/*  9 */     return 
/*    */       
/* 11 */       HORIZONTAL;
/*    */   }
/*    */   
/*    */   public ScreenDirection getPositive() {
/* 16 */     switch (ordinal()) {
/*    */       default:
/* 16 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/* 16 */     return 
/*    */       
/* 18 */       ScreenDirection.DOWN;
/*    */   }
/*    */   
/*    */   public ScreenDirection getNegative() {
/* 23 */     switch (ordinal()) {
/*    */       default:
/* 23 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/* 23 */     return 
/*    */       
/* 25 */       ScreenDirection.UP;
/*    */   }
/*    */   
/*    */   public ScreenDirection getDirection(boolean positive) {
/* 30 */     return positive ? getPositive() : getNegative();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\navigation\ScreenAxis.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */