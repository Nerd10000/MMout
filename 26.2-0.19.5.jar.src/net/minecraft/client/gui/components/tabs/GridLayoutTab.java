/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.GridLayout;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GridLayoutTab implements Tab {
/*    */   private final Component title;
/*    */   
/* 14 */   protected final GridLayout layout = new GridLayout();
/*    */   
/*    */   public GridLayoutTab(Component title) {
/* 17 */     this.title = title;
/*    */   }
/*    */   
/*    */   public Component getTabTitle() {
/* 22 */     return this.title;
/*    */   }
/*    */   
/*    */   public Component getTabExtraNarration() {
/* 27 */     return (Component)Component.empty();
/*    */   }
/*    */   
/*    */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/* 32 */     this.layout.visitWidgets(childrenConsumer);
/*    */   }
/*    */   
/*    */   public void doLayout(ScreenRectangle screenRectangle) {
/* 37 */     this.layout.arrangeElements();
/* 38 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.16666667F);
/*    */   }
/*    */   
/*    */   public Layout getLayout() {
/* 43 */     return (Layout)this.layout;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\GridLayoutTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */