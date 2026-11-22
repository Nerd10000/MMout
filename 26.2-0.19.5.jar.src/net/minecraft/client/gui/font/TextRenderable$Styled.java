/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ public interface Styled extends TextRenderable, ActiveArea {
/*    */   default float activeLeft() {
/* 30 */     return left();
/*    */   }
/*    */   
/*    */   default float activeTop() {
/* 35 */     return top();
/*    */   }
/*    */   
/*    */   default float activeRight() {
/* 40 */     return right();
/*    */   }
/*    */   
/*    */   default float activeBottom() {
/* 45 */     return bottom();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\TextRenderable$Styled.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */