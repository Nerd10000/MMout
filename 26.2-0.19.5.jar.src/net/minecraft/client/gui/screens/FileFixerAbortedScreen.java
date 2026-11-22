/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ 
/*    */ public class FileFixerAbortedScreen extends Screen {
/* 15 */   protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
/*    */   
/*    */   private final Component message;
/*    */   
/*    */   protected Button backButton;
/*    */   
/*    */   protected Button reportBugButton;
/*    */   
/*    */   protected final Runnable callback;
/*    */   
/*    */   public FileFixerAbortedScreen(Runnable callback, Component message) {
/* 22 */     super((Component)Component.translatable("upgradeWorld.aborted.title"));
/* 23 */     this.callback = callback;
/* 24 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Component getNarrationMessage() {
/* 29 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.message });
/*    */   }
/*    */   
/*    */   protected void init() {
/* 34 */     super.init();
/* 35 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/* 37 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/* 39 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(this.message, this.font)).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true));
/* 41 */     LinearLayout buttonLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/* 42 */     buttonLayout.defaultCellSetting().paddingTop(16);
/* 43 */     addButtons(buttonLayout);
/* 45 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 46 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void repositionElements() {
/* 51 */     this.layout.arrangeElements();
/* 52 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */   
/*    */   protected void addButtons(LinearLayout buttonLayout) {
/* 56 */     this.backButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, paramButton -> this.callback.run()).build());
/* 57 */     this.reportBugButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder((Component)Component.translatable("upgradeWorld.aborted.reportBug"), ConfirmLinkScreen.confirmLink(this, CommonLinks.SNAPSHOT_BUGS_FEEDBACK, true)).build());
/*    */   }
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 67 */     if (event.isEscape()) {
/* 68 */       this.callback.run();
/* 69 */       return true;
/*    */     } 
/* 71 */     return super.keyPressed(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\FileFixerAbortedScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */