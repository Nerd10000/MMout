/*    */ package net.minecraft.client.gui.components.events;
/*    */ 
/*    */ public abstract class AbstractContainerEventHandler implements ContainerEventHandler {
/*    */   private GuiEventListener focused;
/*    */   
/*    */   private boolean isDragging;
/*    */   
/*    */   public final boolean isDragging() {
/* 16 */     return this.isDragging;
/*    */   }
/*    */   
/*    */   public final void setDragging(boolean dragging) {
/* 21 */     this.isDragging = dragging;
/*    */   }
/*    */   
/*    */   public GuiEventListener getFocused() {
/* 26 */     return this.focused;
/*    */   }
/*    */   
/*    */   public void setFocused(GuiEventListener focused) {
/* 31 */     if (this.focused == focused)
/*    */       return; 
/* 34 */     if (this.focused != null)
/* 35 */       this.focused.setFocused(false); 
/* 37 */     if (focused != null)
/* 38 */       focused.setFocused(true); 
/* 40 */     this.focused = focused;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\events\AbstractContainerEventHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */