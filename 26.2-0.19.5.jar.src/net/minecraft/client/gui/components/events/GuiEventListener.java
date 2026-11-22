/*    */ package net.minecraft.client.gui.components.events;
/*    */ 
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.components.TabOrderedElement;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.input.CharacterEvent;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.input.PreeditEvent;
/*    */ 
/*    */ public interface GuiEventListener extends TabOrderedElement {
/*    */   default void mouseMoved(double x, double y) {}
/*    */   
/*    */   default boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseReleased(MouseButtonEvent event) {
/* 26 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 34 */     return false;
/*    */   }
/*    */   
/*    */   default boolean keyPressed(KeyEvent event) {
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   default boolean keyReleased(KeyEvent event) {
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   default boolean charTyped(CharacterEvent event) {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   default boolean preeditUpdated(PreeditEvent event) {
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   default ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   default boolean isMouseOver(double mouseX, double mouseY) {
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   void setFocused(boolean paramBoolean);
/*    */   
/*    */   boolean isFocused();
/*    */   
/*    */   default boolean shouldTakeFocusAfterInteraction() {
/* 70 */     return true;
/*    */   }
/*    */   
/*    */   default ComponentPath getCurrentFocusPath() {
/* 74 */     if (isFocused())
/* 75 */       return ComponentPath.leaf(this); 
/* 77 */     return null;
/*    */   }
/*    */   
/*    */   default ScreenRectangle getRectangle() {
/* 81 */     return ScreenRectangle.empty();
/*    */   }
/*    */   
/*    */   default ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 85 */     return getRectangle().getBorder(opposite);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\events\GuiEventListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */