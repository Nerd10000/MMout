/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class HeaderEntry extends TransferableSelectionList.Entry {
/*     */   private final Font font;
/*     */   
/*     */   private final Component text;
/*     */   
/*     */   public HeaderEntry(TransferableSelectionList this$0, Font font, Component text) {
/* 290 */     super(this$0);
/* 291 */     this.font = font;
/* 292 */     this.text = text;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 297 */     Objects.requireNonNull(this.font);
/* 297 */     graphics.centeredText(this.font, this.text, getX() + getWidth() / 2, getContentYMiddle() - 9 / 2, -1);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 302 */     return this.text;
/*     */   }
/*     */   
/*     */   public String getPackId() {
/* 307 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\packs\TransferableSelectionList$HeaderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */