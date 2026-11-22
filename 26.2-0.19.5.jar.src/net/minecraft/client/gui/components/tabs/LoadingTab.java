/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LoadingTab implements Tab {
/*    */   private final Component title;
/*    */   
/*    */   private final Component loadingTitle;
/*    */   
/* 17 */   protected final LinearLayout layout = LinearLayout.vertical();
/*    */   
/*    */   public LoadingTab(Font font, Component title, Component loadingTitle) {
/* 20 */     this.title = title;
/* 21 */     this.loadingTitle = loadingTitle;
/* 23 */     LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(font, loadingTitle);
/* 24 */     this.layout.defaultCellSetting()
/* 25 */       .alignVerticallyMiddle()
/* 26 */       .alignHorizontallyCenter();
/* 27 */     this.layout.addChild((LayoutElement)loadingDotsWidget, layoutSettings -> layoutSettings.paddingBottom(30));
/*    */   }
/*    */   
/*    */   public Component getTabTitle() {
/* 32 */     return this.title;
/*    */   }
/*    */   
/*    */   public Component getTabExtraNarration() {
/* 37 */     return this.loadingTitle;
/*    */   }
/*    */   
/*    */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/* 42 */     this.layout.visitWidgets(childrenConsumer);
/*    */   }
/*    */   
/*    */   public void doLayout(ScreenRectangle screenRectangle) {
/* 47 */     this.layout.arrangeElements();
/* 48 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.5F);
/*    */   }
/*    */   
/*    */   public Layout getLayout() {
/* 53 */     return (Layout)this.layout;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\LoadingTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */