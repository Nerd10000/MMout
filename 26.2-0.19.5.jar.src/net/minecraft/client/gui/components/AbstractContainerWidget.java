/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class AbstractContainerWidget extends AbstractScrollArea implements ContainerEventHandler {
/*    */   private GuiEventListener focused;
/*    */   
/*    */   private boolean isDragging;
/*    */   
/*    */   public AbstractContainerWidget(int x, int y, int width, int height, Component message) {
/* 17 */     super(x, y, width, height, message, AbstractScrollArea.ScrollbarSettings.NO_SCROLL);
/*    */   }
/*    */   
/*    */   public AbstractContainerWidget(int x, int y, int width, int height, Component message, AbstractScrollArea.ScrollbarSettings scrollbarSettings) {
/* 21 */     super(x, y, width, height, message, scrollbarSettings);
/*    */   }
/*    */   
/*    */   public final boolean isDragging() {
/* 26 */     return this.isDragging;
/*    */   }
/*    */   
/*    */   public final void setDragging(boolean dragging) {
/* 31 */     this.isDragging = dragging;
/*    */   }
/*    */   
/*    */   public GuiEventListener getFocused() {
/* 36 */     return this.focused;
/*    */   }
/*    */   
/*    */   public void setFocused(GuiEventListener focused) {
/* 41 */     if (this.focused != null)
/* 42 */       this.focused.setFocused(false); 
/* 44 */     if (focused != null)
/* 45 */       focused.setFocused(true); 
/* 47 */     this.focused = focused;
/*    */   }
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 52 */     return super.nextFocusPath(navigationEvent);
/*    */   }
/*    */   
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 57 */     boolean scrolling = updateScrolling(event);
/* 58 */     return (super.mouseClicked(event, doubleClick) || scrolling);
/*    */   }
/*    */   
/*    */   public boolean mouseReleased(MouseButtonEvent event) {
/* 63 */     super.mouseReleased(event);
/* 64 */     return super.mouseReleased(event);
/*    */   }
/*    */   
/*    */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 69 */     super.mouseDragged(event, dx, dy);
/* 70 */     return super.mouseDragged(event, dx, dy);
/*    */   }
/*    */   
/*    */   public boolean isFocused() {
/* 75 */     return super.isFocused();
/*    */   }
/*    */   
/*    */   public void setFocused(boolean focused) {
/* 80 */     super.setFocused(focused);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractContainerWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */