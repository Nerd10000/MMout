/*    */ package net.minecraft.client;
/*    */ 
/*    */ import org.joml.Vector2i;
/*    */ 
/*    */ public class ScrollWheelHandler {
/*    */   private double accumulatedScrollX;
/*    */   
/*    */   private double accumulatedScrollY;
/*    */   
/*    */   public Vector2i onMouseScroll(double scaledXScrollOffset, double scaledYScrollOffset) {
/* 11 */     if (this.accumulatedScrollX != 0.0D && Math.signum(scaledXScrollOffset) != Math.signum(this.accumulatedScrollX))
/* 12 */       this.accumulatedScrollX = 0.0D; 
/* 14 */     if (this.accumulatedScrollY != 0.0D && Math.signum(scaledYScrollOffset) != Math.signum(this.accumulatedScrollY))
/* 15 */       this.accumulatedScrollY = 0.0D; 
/* 17 */     this.accumulatedScrollX += scaledXScrollOffset;
/* 18 */     this.accumulatedScrollY += scaledYScrollOffset;
/* 20 */     int wheelX = (int)this.accumulatedScrollX;
/* 21 */     int wheelY = (int)this.accumulatedScrollY;
/* 22 */     if (wheelX == 0 && wheelY == 0)
/* 23 */       return new Vector2i(0, 0); 
/* 26 */     this.accumulatedScrollX -= wheelX;
/* 27 */     this.accumulatedScrollY -= wheelY;
/* 29 */     return new Vector2i(wheelX, wheelY);
/*    */   }
/*    */   
/*    */   public static int getNextScrollWheelSelection(double wheel, int currentSelected, int limit) {
/* 33 */     int step = (int)Math.signum(wheel);
/* 34 */     currentSelected -= step;
/* 35 */     currentSelected = Math.max(-1, currentSelected);
/* 37 */     while (currentSelected < 0)
/* 38 */       currentSelected += limit; 
/* 40 */     while (currentSelected >= limit)
/* 41 */       currentSelected -= limit; 
/* 43 */     return currentSelected;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\ScrollWheelHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */