/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public class MessageEntry extends ChatSelectionScreen.ChatSelectionList.Entry {
/*     */   private static final int CHECKMARK_WIDTH = 9;
/*     */   
/*     */   private static final int CHECKMARK_HEIGHT = 8;
/*     */   
/*     */   private static final int INDENT_AMOUNT = 11;
/*     */   
/*     */   private static final int TAG_MARGIN_LEFT = 4;
/*     */   
/*     */   private final int chatId;
/*     */   
/*     */   private final FormattedText text;
/*     */   
/*     */   private final Component narration;
/*     */   
/*     */   private final List<FormattedCharSequence> hoverText;
/*     */   
/*     */   private final GuiMessageTag.Icon tagIcon;
/*     */   
/*     */   private final List<FormattedCharSequence> tagHoverText;
/*     */   
/*     */   private final boolean canReport;
/*     */   
/*     */   private final boolean playerMessage;
/*     */   
/*     */   public MessageEntry(int chatId, Component text, Component narration, GuiMessageTag tag, boolean canReport, boolean playerMessage) {
/* 283 */     this.chatId = chatId;
/* 284 */     this.tagIcon = (GuiMessageTag.Icon)Optionull.map(tag, GuiMessageTag::icon);
/* 285 */     this.tagHoverText = (tag != null && tag.text() != null) ? ChatSelectionScreen.access$100(this$1.this$0).split((FormattedText)tag.text(), ChatSelectionScreen.ChatSelectionList.this.getRowWidth()) : null;
/* 287 */     this.canReport = canReport;
/* 288 */     this.playerMessage = playerMessage;
/* 289 */     FormattedText shortText = ChatSelectionScreen.access$300(this$1.this$0).substrByWidth((FormattedText)text, getMaximumTextWidth() - ChatSelectionScreen.access$200(this$1.this$0).width((FormattedText)CommonComponents.ELLIPSIS));
/* 290 */     if (text != shortText) {
/* 291 */       this.text = FormattedText.composite(new FormattedText[] { shortText, (FormattedText)CommonComponents.ELLIPSIS });
/* 292 */       this.hoverText = ChatSelectionScreen.access$400(this$1.this$0).split((FormattedText)text, ChatSelectionScreen.ChatSelectionList.this.getRowWidth());
/*     */     } else {
/* 294 */       this.text = (FormattedText)text;
/* 295 */       this.hoverText = null;
/*     */     } 
/* 297 */     this.narration = narration;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 302 */     if (isSelected() && this.canReport)
/* 303 */       extractSelectedCheckmark(graphics, getContentY(), getContentX(), getContentHeight()); 
/* 306 */     int textX = getContentX() + getTextIndent();
/* 307 */     Objects.requireNonNull(ChatSelectionScreen.access$500(this.this$1.this$0));
/* 307 */     int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 308 */     graphics.text(ChatSelectionScreen.access$600(this.this$1.this$0), Language.getInstance().getVisualOrder(this.text), textX, textY, this.canReport ? -1 : -1593835521);
/* 310 */     if (this.hoverText != null && hovered)
/* 311 */       graphics.setTooltipForNextFrame(this.hoverText, mouseX, mouseY); 
/* 314 */     int textWidth = ChatSelectionScreen.access$700(this.this$1.this$0).width(this.text);
/* 315 */     extractTag(graphics, textX + textWidth + 4, getContentY(), getContentHeight(), mouseX, mouseY);
/*     */   }
/*     */   
/*     */   private void extractTag(GuiGraphicsExtractor graphics, int iconLeft, int rowTop, int rowHeight, int mouseX, int mouseY) {
/* 319 */     if (this.tagIcon != null) {
/* 320 */       int iconTop = rowTop + (rowHeight - this.tagIcon.height) / 2;
/* 321 */       this.tagIcon.extractRenderState(graphics, iconLeft, iconTop);
/* 323 */       if (this.tagHoverText != null && mouseX >= iconLeft && mouseX <= iconLeft + this.tagIcon.width && mouseY >= iconTop && mouseY <= iconTop + this.tagIcon.height)
/* 324 */         graphics.setTooltipForNextFrame(this.tagHoverText, mouseX, mouseY); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractSelectedCheckmark(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowHeight) {
/* 330 */     int left = rowLeft;
/* 331 */     int top = rowTop + (rowHeight - 8) / 2;
/* 332 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ChatSelectionScreen.CHECKMARK_SPRITE, left, top, 9, 8);
/*     */   }
/*     */   
/*     */   private int getMaximumTextWidth() {
/* 336 */     int tagMargin = (this.tagIcon != null) ? (this.tagIcon.width + 4) : 0;
/* 337 */     return ChatSelectionScreen.ChatSelectionList.this.getRowWidth() - getTextIndent() - 4 - tagMargin;
/*     */   }
/*     */   
/*     */   private int getTextIndent() {
/* 341 */     return this.playerMessage ? 11 : 0;
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 346 */     return isSelected() ? (Component)Component.translatable("narrator.select", new Object[] { this.narration }) : this.narration;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 351 */     ChatSelectionScreen.ChatSelectionList.this.setSelected((ChatSelectionScreen.ChatSelectionList.Entry)null);
/* 352 */     return toggleReport();
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 357 */     if (event.isSelection())
/* 358 */       return toggleReport(); 
/* 360 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSelected() {
/* 365 */     return this.this$1.this$0.report.isReported(this.chatId);
/*     */   }
/*     */   
/*     */   public boolean canSelect() {
/* 370 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canReport() {
/* 375 */     return this.canReport;
/*     */   }
/*     */   
/*     */   private boolean toggleReport() {
/* 379 */     if (this.canReport) {
/* 380 */       this.this$1.this$0.report.toggleReported(this.chatId);
/* 381 */       this.this$1.this$0.updateConfirmSelectedButton();
/* 382 */       return true;
/*     */     } 
/* 384 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList$MessageEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */