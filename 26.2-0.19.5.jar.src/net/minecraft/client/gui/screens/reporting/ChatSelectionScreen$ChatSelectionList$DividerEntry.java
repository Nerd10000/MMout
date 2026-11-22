/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ 
/*     */ public class DividerEntry extends ChatSelectionScreen.ChatSelectionList.Entry {
/*     */   private final Component text;
/*     */   
/*     */   public DividerEntry(Component text) {
/* 417 */     this.text = text;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 422 */     int centerY = getContentYMiddle();
/* 423 */     int rowRight = getContentRight() - 8;
/* 425 */     int textWidth = ChatSelectionScreen.access$1100(this.this$1.this$0).width((FormattedText)this.text);
/* 426 */     int textLeft = (getContentX() + rowRight - textWidth) / 2;
/* 427 */     Objects.requireNonNull(ChatSelectionScreen.access$1200(this.this$1.this$0));
/* 427 */     int textTop = centerY - 9 / 2;
/* 429 */     graphics.text(ChatSelectionScreen.access$1300(this.this$1.this$0), this.text, textLeft, textTop, -6250336);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 434 */     return this.text;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList$DividerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */