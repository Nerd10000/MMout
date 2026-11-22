/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public class ChatSelectionList extends ObjectSelectionList<ChatSelectionScreen.ChatSelectionList.Entry> implements ChatSelectionLogFiller.Output {
/*     */   public static final int ITEM_HEIGHT = 16;
/*     */   
/*     */   private Heading previousHeading;
/*     */   
/*     */   public ChatSelectionList(Minecraft minecraft, int upperMargin) {
/* 136 */     super(minecraft, ChatSelectionScreen.this.width, ChatSelectionScreen.this.height - upperMargin - 80, 40, 16);
/*     */   }
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/* 141 */     double prevScrollAmount = scrollAmount();
/* 142 */     super.setScrollAmount(scrollAmount);
/* 143 */     if (maxScrollAmount() > 1.0E-5F && scrollAmount <= 9.999999747378752E-6D && !Mth.equal(scrollAmount, prevScrollAmount))
/* 144 */       ChatSelectionScreen.this.onReachedScrollTop(); 
/*     */   }
/*     */   
/*     */   public void acceptMessage(int id, LoggedChatMessage.Player message) {
/* 150 */     boolean canReport = message.canReport(ChatSelectionScreen.this.report.reportedProfileId());
/* 151 */     ChatTrustLevel trustLevel = message.trustLevel();
/* 152 */     GuiMessageTag tag = trustLevel.createTag(message.message());
/* 153 */     Entry entry = new MessageEntry(id, message.toContentComponent(), message.toNarrationComponent(), tag, canReport, true);
/* 154 */     addEntryToTop((AbstractSelectionList.Entry)entry);
/* 155 */     updateHeading(message, canReport);
/*     */   }
/*     */   
/*     */   private void updateHeading(LoggedChatMessage.Player message, boolean canReport) {
/* 159 */     Entry entry = new MessageHeadingEntry(message.profile(), message.toHeadingComponent(), canReport);
/* 160 */     addEntryToTop((AbstractSelectionList.Entry)entry);
/* 162 */     Heading heading = new Heading(message.profileId(), entry);
/* 163 */     if (this.previousHeading != null && this.previousHeading.canCombine(heading))
/* 164 */       removeEntryFromTop((AbstractSelectionList.Entry)this.previousHeading.entry()); 
/* 166 */     this.previousHeading = heading;
/*     */   }
/*     */   
/*     */   public void acceptDivider(Component text) {
/* 171 */     addEntryToTop((AbstractSelectionList.Entry)new PaddingEntry());
/* 172 */     addEntryToTop((AbstractSelectionList.Entry)new DividerEntry(text));
/* 173 */     addEntryToTop((AbstractSelectionList.Entry)new PaddingEntry());
/* 174 */     this.previousHeading = null;
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 179 */     return Math.min(350, this.width - 50);
/*     */   }
/*     */   
/*     */   public int getMaxVisibleEntries() {
/* 183 */     return Mth.positiveCeilDiv(this.height, 16);
/*     */   }
/*     */   
/*     */   protected void extractItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, Entry entry) {
/* 188 */     if (shouldHighlightEntry(entry)) {
/* 189 */       boolean selected = (getSelected() == entry);
/* 190 */       int outlineColor = (isFocused() && selected) ? -1 : -8355712;
/* 191 */       extractSelection(graphics, (AbstractSelectionList.Entry)entry, outlineColor);
/*     */     } 
/* 194 */     entry.extractContent(graphics, mouseX, mouseY, (getHovered() == entry), a);
/*     */   }
/*     */   
/*     */   private boolean shouldHighlightEntry(Entry entry) {
/* 198 */     if (entry.canSelect()) {
/* 199 */       boolean entrySelected = (getSelected() == entry);
/* 200 */       boolean nothingSelected = (getSelected() == null);
/* 201 */       boolean entryHovered = (getHovered() == entry);
/* 202 */       return (entrySelected || (nothingSelected && entryHovered && entry.canReport()));
/*     */     } 
/* 204 */     return false;
/*     */   }
/*     */   
/*     */   protected Entry nextEntry(ScreenDirection dir) {
/* 209 */     return (Entry)nextEntry(dir, Entry::canSelect);
/*     */   }
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 214 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 215 */     Entry entry = nextEntry(ScreenDirection.UP);
/* 216 */     if (entry == null)
/* 217 */       ChatSelectionScreen.this.onReachedScrollTop(); 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 223 */     Entry selected = (Entry)getSelected();
/* 224 */     if (selected != null && selected.keyPressed(event))
/* 225 */       return true; 
/* 227 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public int getFooterTop() {
/* 231 */     Objects.requireNonNull(ChatSelectionScreen.access$000(ChatSelectionScreen.this));
/* 231 */     return getBottom() + 9;
/*     */   }
/*     */   
/*     */   private static final class Heading extends Record {
/*     */     private final UUID sender;
/*     */     
/*     */     private final ChatSelectionScreen.ChatSelectionList.Entry entry;
/*     */     
/*     */     private Heading(UUID sender, ChatSelectionScreen.ChatSelectionList.Entry entry) {
/* 234 */       this.sender = sender;
/* 234 */       this.entry = entry;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UUID sender() {
/* 234 */       return this.sender;
/*     */     }
/*     */     
/*     */     public ChatSelectionScreen.ChatSelectionList.Entry entry() {
/* 234 */       return this.entry;
/*     */     }
/*     */     
/*     */     public boolean canCombine(Heading other) {
/* 236 */       return other.sender.equals(this.sender);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     public Component getNarration() {
/* 243 */       return CommonComponents.EMPTY;
/*     */     }
/*     */     
/*     */     public boolean isSelected() {
/* 247 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canSelect() {
/* 251 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canReport() {
/* 255 */       return canSelect();
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 260 */       return canSelect();
/*     */     }
/*     */   }
/*     */   
/*     */   public class MessageEntry extends Entry {
/*     */     private static final int CHECKMARK_WIDTH = 9;
/*     */     
/*     */     private static final int CHECKMARK_HEIGHT = 8;
/*     */     
/*     */     private static final int INDENT_AMOUNT = 11;
/*     */     
/*     */     private static final int TAG_MARGIN_LEFT = 4;
/*     */     
/*     */     private final int chatId;
/*     */     
/*     */     private final FormattedText text;
/*     */     
/*     */     private final Component narration;
/*     */     
/*     */     private final List<FormattedCharSequence> hoverText;
/*     */     
/*     */     private final GuiMessageTag.Icon tagIcon;
/*     */     
/*     */     private final List<FormattedCharSequence> tagHoverText;
/*     */     
/*     */     private final boolean canReport;
/*     */     
/*     */     private final boolean playerMessage;
/*     */     
/*     */     public MessageEntry(int chatId, Component text, Component narration, GuiMessageTag tag, boolean canReport, boolean playerMessage) {
/* 283 */       this.chatId = chatId;
/* 284 */       this.tagIcon = (GuiMessageTag.Icon)Optionull.map(tag, GuiMessageTag::icon);
/* 285 */       this.tagHoverText = (tag != null && tag.text() != null) ? ChatSelectionScreen.access$100(this$1.this$0).split((FormattedText)tag.text(), ChatSelectionScreen.ChatSelectionList.this.getRowWidth()) : null;
/* 287 */       this.canReport = canReport;
/* 288 */       this.playerMessage = playerMessage;
/* 289 */       FormattedText shortText = ChatSelectionScreen.access$300(this$1.this$0).substrByWidth((FormattedText)text, getMaximumTextWidth() - ChatSelectionScreen.access$200(this$1.this$0).width((FormattedText)CommonComponents.ELLIPSIS));
/* 290 */       if (text != shortText) {
/* 291 */         this.text = FormattedText.composite(new FormattedText[] { shortText, (FormattedText)CommonComponents.ELLIPSIS });
/* 292 */         this.hoverText = ChatSelectionScreen.access$400(this$1.this$0).split((FormattedText)text, ChatSelectionScreen.ChatSelectionList.this.getRowWidth());
/*     */       } else {
/* 294 */         this.text = (FormattedText)text;
/* 295 */         this.hoverText = null;
/*     */       } 
/* 297 */       this.narration = narration;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 302 */       if (isSelected() && this.canReport)
/* 303 */         extractSelectedCheckmark(graphics, getContentY(), getContentX(), getContentHeight()); 
/* 306 */       int textX = getContentX() + getTextIndent();
/* 307 */       Objects.requireNonNull(ChatSelectionScreen.access$500(this.this$1.this$0));
/* 307 */       int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 308 */       graphics.text(ChatSelectionScreen.access$600(this.this$1.this$0), Language.getInstance().getVisualOrder(this.text), textX, textY, this.canReport ? -1 : -1593835521);
/* 310 */       if (this.hoverText != null && hovered)
/* 311 */         graphics.setTooltipForNextFrame(this.hoverText, mouseX, mouseY); 
/* 314 */       int textWidth = ChatSelectionScreen.access$700(this.this$1.this$0).width(this.text);
/* 315 */       extractTag(graphics, textX + textWidth + 4, getContentY(), getContentHeight(), mouseX, mouseY);
/*     */     }
/*     */     
/*     */     private void extractTag(GuiGraphicsExtractor graphics, int iconLeft, int rowTop, int rowHeight, int mouseX, int mouseY) {
/* 319 */       if (this.tagIcon != null) {
/* 320 */         int iconTop = rowTop + (rowHeight - this.tagIcon.height) / 2;
/* 321 */         this.tagIcon.extractRenderState(graphics, iconLeft, iconTop);
/* 323 */         if (this.tagHoverText != null && mouseX >= iconLeft && mouseX <= iconLeft + this.tagIcon.width && mouseY >= iconTop && mouseY <= iconTop + this.tagIcon.height)
/* 324 */           graphics.setTooltipForNextFrame(this.tagHoverText, mouseX, mouseY); 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void extractSelectedCheckmark(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowHeight) {
/* 330 */       int left = rowLeft;
/* 331 */       int top = rowTop + (rowHeight - 8) / 2;
/* 332 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ChatSelectionScreen.CHECKMARK_SPRITE, left, top, 9, 8);
/*     */     }
/*     */     
/*     */     private int getMaximumTextWidth() {
/* 336 */       int tagMargin = (this.tagIcon != null) ? (this.tagIcon.width + 4) : 0;
/* 337 */       return ChatSelectionScreen.ChatSelectionList.this.getRowWidth() - getTextIndent() - 4 - tagMargin;
/*     */     }
/*     */     
/*     */     private int getTextIndent() {
/* 341 */       return this.playerMessage ? 11 : 0;
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 346 */       return isSelected() ? (Component)Component.translatable("narrator.select", new Object[] { this.narration }) : this.narration;
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 351 */       ChatSelectionScreen.ChatSelectionList.this.setSelected((ChatSelectionScreen.ChatSelectionList.Entry)null);
/* 352 */       return toggleReport();
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 357 */       if (event.isSelection())
/* 358 */         return toggleReport(); 
/* 360 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isSelected() {
/* 365 */       return this.this$1.this$0.report.isReported(this.chatId);
/*     */     }
/*     */     
/*     */     public boolean canSelect() {
/* 370 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canReport() {
/* 375 */       return this.canReport;
/*     */     }
/*     */     
/*     */     private boolean toggleReport() {
/* 379 */       if (this.canReport) {
/* 380 */         this.this$1.this$0.report.toggleReported(this.chatId);
/* 381 */         this.this$1.this$0.updateConfirmSelectedButton();
/* 382 */         return true;
/*     */       } 
/* 384 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public class MessageHeadingEntry extends Entry {
/*     */     private static final int FACE_SIZE = 12;
/*     */     
/*     */     private static final int PADDING = 4;
/*     */     
/*     */     private final Component heading;
/*     */     
/*     */     private final Supplier<PlayerSkin> skin;
/*     */     
/*     */     private final boolean canReport;
/*     */     
/*     */     public MessageHeadingEntry(GameProfile profile, Component heading, boolean canReport) {
/* 397 */       this.heading = heading;
/* 398 */       this.canReport = canReport;
/* 399 */       this.skin = ChatSelectionScreen.ChatSelectionList.this.minecraft.getSkinManager().createLookup(profile, true);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 404 */       int faceX = getContentX() - 12 + 4;
/* 405 */       int faceY = getContentY() + (getContentHeight() - 12) / 2;
/* 406 */       PlayerFaceExtractor.extractRenderState(graphics, this.skin.get(), faceX, faceY, 12);
/* 408 */       Objects.requireNonNull(ChatSelectionScreen.access$900(this.this$1.this$0));
/* 408 */       int textY = getContentY() + 1 + (getContentHeight() - 9) / 2;
/* 409 */       graphics.text(ChatSelectionScreen.access$1000(this.this$1.this$0), this.heading, faceX + 12 + 4, textY, this.canReport ? -1 : -1593835521);
/*     */     }
/*     */   }
/*     */   
/*     */   public class DividerEntry extends Entry {
/*     */     private final Component text;
/*     */     
/*     */     public DividerEntry(Component text) {
/* 417 */       this.text = text;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 422 */       int centerY = getContentYMiddle();
/* 423 */       int rowRight = getContentRight() - 8;
/* 425 */       int textWidth = ChatSelectionScreen.access$1100(this.this$1.this$0).width((FormattedText)this.text);
/* 426 */       int textLeft = (getContentX() + rowRight - textWidth) / 2;
/* 427 */       Objects.requireNonNull(ChatSelectionScreen.access$1200(this.this$1.this$0));
/* 427 */       int textTop = centerY - 9 / 2;
/* 429 */       graphics.text(ChatSelectionScreen.access$1300(this.this$1.this$0), this.text, textLeft, textTop, -6250336);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 434 */       return this.text;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PaddingEntry extends Entry {
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */