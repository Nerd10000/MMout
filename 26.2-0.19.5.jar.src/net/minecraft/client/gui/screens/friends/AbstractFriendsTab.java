/*    */ package net.minecraft.client.gui.screens.friends;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.tabs.Tab;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class AbstractFriendsTab implements Tab {
/*    */   static final int SPACING = 8;
/*    */   
/*    */   static final int SCROLLBAR_SPACING = 2;
/*    */   
/*    */   static final int LIST_MARGIN = 8;
/*    */   
/*    */   protected final int width;
/*    */   
/*    */   protected int height;
/*    */   
/*    */   public AbstractFriendsTab(int width, int height) {
/* 23 */     this.width = width;
/* 24 */     this.height = height;
/*    */   }
/*    */   
/*    */   protected int getListContentWidth() {
/* 28 */     return this.width - 16;
/*    */   }
/*    */   
/*    */   final void disable() {
/* 38 */     entriesContainer().visitWidgets(widget -> {
/*    */           if (widget instanceof AbstractFriendsEntryContainerWidget) {
/*    */             AbstractFriendsEntryContainerWidget entry = (AbstractFriendsEntryContainerWidget)widget;
/*    */             entry.disable();
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   void setHeight(int height) {
/* 47 */     this.height = height;
/* 48 */     rearrangeElements();
/*    */   }
/*    */   
/*    */   FrameLayout createCenteredFrame(LayoutElement child, int frameWidth, int frameHeight) {
/* 53 */     FrameLayout frameLayout = new FrameLayout(frameWidth, frameHeight);
/* 54 */     frameLayout.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/* 55 */     frameLayout.addChild(child);
/* 56 */     return frameLayout;
/*    */   }
/*    */   
/*    */   FocusableTextWidget createText(Component message, Font font, int maxWidth) {
/* 61 */     return FocusableTextWidget.builder(message, font)
/* 62 */       .maxWidth(maxWidth)
/* 63 */       .alwaysShowBorder(false)
/* 64 */       .backgroundFill(FocusableTextWidget.BackgroundFill.NEVER)
/* 65 */       .build();
/*    */   }
/*    */   
/*    */   MultiLineTextWidget createCenteredText(Component message, Font font, int maxWidth) {
/* 70 */     return createText(message, font, maxWidth).setCentered(true);
/*    */   }
/*    */   
/*    */   abstract void rearrangeElements();
/*    */   
/*    */   protected abstract Layout entriesContainer();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\AbstractFriendsTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */