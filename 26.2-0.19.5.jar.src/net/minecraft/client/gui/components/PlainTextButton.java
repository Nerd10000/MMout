/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class PlainTextButton extends Button {
/*    */   private final Font font;
/*    */   
/*    */   private final Component message;
/*    */   
/*    */   private final Component underlinedMessage;
/*    */   
/*    */   public PlainTextButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, Font font) {
/* 16 */     super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
/* 17 */     this.font = font;
/* 18 */     this.message = message;
/* 19 */     this.underlinedMessage = ComponentUtils.mergeStyles(message, Style.EMPTY.withUnderlined(Boolean.valueOf(true)));
/*    */   }
/*    */   
/*    */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 24 */     Component messageToRender = isHoveredOrFocused() ? this.underlinedMessage : this.message;
/* 25 */     graphics.text(this.font, messageToRender, getX(), getY(), 0xFFFFFF | Mth.ceil(this.alpha * 255.0F) << 24);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlainTextButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */