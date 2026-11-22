/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.input.PreeditEvent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class IMEPreeditOverlay implements Renderable {
/* 15 */   private static final Identifier BACKGROUND = Identifier.withDefaultNamespace("widget/preedit");
/*    */   
/* 18 */   private static final Style FOCUSED_STYLE = Style.EMPTY.withUnderlined(Boolean.valueOf(true));
/*    */   
/*    */   private static final int SEPARATION_FROM_INPUT = 4;
/*    */   
/*    */   private static final int BORDER_MARGIN = 4;
/*    */   
/*    */   private static final int BORDER_WIDTH = 1;
/*    */   
/*    */   private static final int BORDER_OFFSET = 5;
/*    */   
/*    */   private static final int TEXT_COLOR = -16777216;
/*    */   
/*    */   private static final int HOT_AREA_MARGIN = 2;
/*    */   
/*    */   private final Font font;
/*    */   
/*    */   private int inputLeft;
/*    */   
/*    */   private int inputTop;
/*    */   
/*    */   private final int inputHeight;
/*    */   
/*    */   private final long initTimeMs;
/*    */   
/*    */   private final Component preEditText;
/*    */   
/*    */   private final int preEditTextWidth;
/*    */   
/*    */   private final int caretPos;
/*    */   
/*    */   public IMEPreeditOverlay(PreeditEvent contents, Font font, int inputHeight) {
/* 36 */     this.font = font;
/* 37 */     this.inputHeight = inputHeight;
/* 38 */     this.initTimeMs = Util.getMillis();
/* 39 */     this.preEditText = (Component)contents.toFormattedText(FOCUSED_STYLE).withColor(-16777216);
/* 40 */     this.preEditTextWidth = font.width((FormattedText)this.preEditText);
/* 42 */     String textBeforeCaret = contents.fullText().substring(0, contents.caretPosition());
/* 43 */     this.caretPos = font.width(textBeforeCaret);
/*    */   }
/*    */   
/*    */   public void updateInputPosition(int inputLeft, int inputTop) {
/* 47 */     this.inputLeft = inputLeft;
/* 48 */     this.inputTop = inputTop;
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 54 */     int preeditLeft = this.inputLeft;
/* 55 */     int preeditRight = preeditLeft + this.preEditTextWidth;
/* 56 */     if (preeditRight > graphics.guiWidth()) {
/* 57 */       preeditLeft = graphics.guiWidth() - this.preEditTextWidth;
/* 58 */       preeditRight = preeditLeft + this.preEditTextWidth;
/*    */     } 
/* 62 */     int inputBottom = this.inputTop + this.inputHeight;
/* 63 */     Objects.requireNonNull(this.font);
/* 63 */     int preeditBottom = inputBottom + 4 + 9;
/* 65 */     if (preeditBottom > graphics.guiHeight()) {
/* 67 */       Objects.requireNonNull(this.font);
/* 67 */       preeditBottom = this.inputTop - 4 - 9;
/*    */     } 
/* 69 */     Objects.requireNonNull(this.font);
/* 69 */     int preeditTop = preeditBottom - 9;
/* 72 */     Minecraft.getInstance().textInputManager().setTextInputArea(
/* 73 */         Math.min(preeditLeft, this.inputLeft) - 2, 
/* 74 */         Math.min(preeditTop, this.inputTop) - 2, preeditRight + 2, 
/*    */         
/* 76 */         Math.max(preeditBottom, inputBottom) + 2);
/* 79 */     int backgroundWidth = preeditRight - preeditLeft + 10;
/* 80 */     int backgroundHeight = preeditBottom - preeditTop + 10;
/* 81 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, preeditLeft - 5, preeditTop - 5, backgroundWidth, backgroundHeight);
/* 83 */     graphics.text(this.font, this.preEditText, preeditLeft, preeditTop, -16777216, false);
/* 85 */     if (TextCursorUtils.isCursorVisible(Util.getMillis() - this.initTimeMs)) {
/* 87 */       Objects.requireNonNull(this.font);
/* 87 */       TextCursorUtils.extractInsertCursor(graphics, preeditLeft + this.caretPos, preeditTop, -16777216, 9 + 1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\IMEPreeditOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */