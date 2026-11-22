/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ 
/*    */ public interface Layout extends LayoutElement {
/*    */   default void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
/* 12 */     visitChildren(child -> child.visitWidgets(widgetVisitor));
/*    */   }
/*    */   
/*    */   default void arrangeElements() {
/* 21 */     visitChildren(child -> {
/*    */           if (child instanceof Layout) {
/*    */             Layout layout = (Layout)child;
/*    */             layout.arrangeElements();
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   void visitChildren(Consumer<LayoutElement> paramConsumer);
/*    */   
/*    */   void removeChildren();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\Layout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */