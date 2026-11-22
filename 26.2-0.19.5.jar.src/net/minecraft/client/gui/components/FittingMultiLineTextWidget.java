/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FittingMultiLineTextWidget extends AbstractTextAreaWidget {
/*    */   private final MultiLineTextWidget multilineWidget;
/*    */   
/*    */   public FittingMultiLineTextWidget(int x, int y, int width, int height, Component message, Font font) {
/* 13 */     super(x, y, width, height, message, AbstractScrollArea.defaultSettings(9));
/* 14 */     this.multilineWidget = (new MultiLineTextWidget(message, font)).setMaxWidth(getWidth() - totalInnerPadding());
/*    */   }
/*    */   
/*    */   public void setWidth(int width) {
/* 19 */     super.setWidth(width);
/* 20 */     this.multilineWidget.setMaxWidth(getWidth() - totalInnerPadding());
/*    */   }
/*    */   
/*    */   protected int getInnerHeight() {
/* 25 */     return this.multilineWidget.getHeight();
/*    */   }
/*    */   
/*    */   public void minimizeHeight() {
/* 29 */     if (!showingScrollBar())
/* 30 */       setHeight(getInnerHeight() + totalInnerPadding()); 
/*    */   }
/*    */   
/*    */   protected void extractBackground(GuiGraphicsExtractor graphics) {
/* 36 */     super.extractBackground(graphics);
/*    */   }
/*    */   
/*    */   public boolean showingScrollBar() {
/* 40 */     return scrollable();
/*    */   }
/*    */   
/*    */   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 45 */     graphics.pose().pushMatrix();
/* 46 */     graphics.pose().translate(getInnerLeft(), getInnerTop());
/* 47 */     this.multilineWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 48 */     graphics.pose().popMatrix();
/*    */   }
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 53 */     output.add(NarratedElementType.TITLE, getMessage());
/*    */   }
/*    */   
/*    */   public void setMessage(Component message) {
/* 58 */     super.setMessage(message);
/* 59 */     this.multilineWidget.setMessage(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\FittingMultiLineTextWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */