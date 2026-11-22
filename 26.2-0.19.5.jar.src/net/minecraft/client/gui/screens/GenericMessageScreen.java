/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ 
/*    */ public class GenericMessageScreen extends Screen {
/*    */   private FocusableTextWidget textWidget;
/*    */   
/*    */   public GenericMessageScreen(Component title) {
/* 13 */     super(title);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 18 */     this.textWidget = addRenderableWidget(FocusableTextWidget.builder(this.title, this.font, 12).textWidth(this.font.width((FormattedText)this.title)).build());
/* 19 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void repositionElements() {
/* 24 */     if (this.textWidget != null) {
/* 25 */       Objects.requireNonNull(this.font);
/* 25 */       this.textWidget.setPosition(this.width / 2 - this.textWidget.getWidth() / 2, this.height / 2 - 9 / 2);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean shouldNarrateNavigation() {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 42 */     extractPanorama(graphics, a);
/* 43 */     extractBlurredBackground(graphics);
/* 44 */     extractMenuBackground(graphics);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\GenericMessageScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */