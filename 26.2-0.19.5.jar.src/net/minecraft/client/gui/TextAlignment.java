/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public enum TextAlignment {
/*  6 */   LEFT {
/*    */     public int calculateLeft(int anchor, int width) {
/*  9 */       return anchor;
/*    */     }
/*    */     
/*    */     public int calculateLeft(int anchor, Font font, FormattedCharSequence text) {
/* 15 */       return anchor;
/*    */     }
/*    */   },
/* 18 */   CENTER {
/*    */     public int calculateLeft(int anchor, int width) {
/* 21 */       return anchor - width / 2;
/*    */     }
/*    */   },
/* 24 */   RIGHT {
/*    */     public int calculateLeft(int anchor, int width) {
/* 27 */       return anchor - width;
/*    */     }
/*    */   };
/*    */   
/*    */   public int calculateLeft(int anchor, Font font, FormattedCharSequence text) {
/* 35 */     return calculateLeft(anchor, font.width(text));
/*    */   }
/*    */   
/*    */   public abstract int calculateLeft(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\TextAlignment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */