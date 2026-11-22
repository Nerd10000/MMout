/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class ClientTextTooltip implements ClientTooltipComponent {
/*    */   private final FormattedCharSequence text;
/*    */   
/*    */   public ClientTextTooltip(FormattedCharSequence text) {
/* 12 */     this.text = text;
/*    */   }
/*    */   
/*    */   public int getWidth(Font font) {
/* 17 */     return font.width(this.text);
/*    */   }
/*    */   
/*    */   public int getHeight(Font font) {
/* 22 */     return 10;
/*    */   }
/*    */   
/*    */   public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
/* 27 */     graphics.text(font, this.text, x, y, -1, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\tooltip\ClientTextTooltip.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */