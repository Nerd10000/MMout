/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ 
/*    */ public interface LayoutElement {
/*    */   void setX(int paramInt);
/*    */   
/*    */   void setY(int paramInt);
/*    */   
/*    */   int getX();
/*    */   
/*    */   int getY();
/*    */   
/*    */   int getWidth();
/*    */   
/*    */   int getHeight();
/*    */   
/*    */   default ScreenRectangle getRectangle() {
/* 22 */     return new ScreenRectangle(getX(), getY(), getWidth(), getHeight());
/*    */   }
/*    */   
/*    */   default void setPosition(int x, int y) {
/* 26 */     setX(x);
/* 27 */     setY(y);
/*    */   }
/*    */   
/*    */   void visitWidgets(Consumer<AbstractWidget> paramConsumer);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\LayoutElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */