/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class MultiLineEditBox extends AbstractTextAreaWidget {
/*     */   private static final int CURSOR_COLOR = -3092272;
/*     */   
/*  24 */   private static final int PLACEHOLDER_TEXT_COLOR = ARGB.color(204, -2039584);
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final Component placeholder;
/*     */   
/*     */   private final MultilineTextField textField;
/*     */   
/*     */   private final int textColor;
/*     */   
/*     */   private final boolean textShadow;
/*     */   
/*     */   private final int cursorColor;
/*     */   
/*     */   private IMEPreeditOverlay preeditOverlay;
/*     */   
/*  34 */   private long focusedTime = Util.getMillis();
/*     */   
/*     */   private MultiLineEditBox(Font font, int x, int y, int width, int height, Component placeholder, Component narration, int textColor, boolean textShadow, int cursorColor, boolean showBackground, boolean showDecorations) {
/*  37 */     super(x, y, width, height, narration, AbstractScrollArea.defaultSettings((int)(9.0D / 2.0D)), showBackground, showDecorations);
/*  38 */     this.font = font;
/*  39 */     this.textShadow = textShadow;
/*  40 */     this.textColor = textColor;
/*  41 */     this.cursorColor = cursorColor;
/*  42 */     this.placeholder = placeholder;
/*  43 */     this.textField = new MultilineTextField(font, width - totalInnerPadding());
/*  44 */     this.textField.setCursorListener(this::scrollToCursor);
/*     */   }
/*     */   
/*     */   public void setCharacterLimit(int characterLimit) {
/*  48 */     this.textField.setCharacterLimit(characterLimit);
/*     */   }
/*     */   
/*     */   public void setLineLimit(int lineLimit) {
/*  52 */     this.textField.setLineLimit(lineLimit);
/*     */   }
/*     */   
/*     */   public void setValueListener(Consumer<String> valueListener) {
/*  56 */     this.textField.setValueListener(valueListener);
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  60 */     setValue(value, false);
/*     */   }
/*     */   
/*     */   public void setValue(String value, boolean allowOverflowLineLimit) {
/*  64 */     this.textField.setValue(value, allowOverflowLineLimit);
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  68 */     return this.textField.value();
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/*  73 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("gui.narrate.editBox", new Object[] { getMessage(), getValue() }));
/*     */   }
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/*  78 */     if (doubleClick) {
/*  79 */       this.textField.selectWordAtCursor();
/*     */     } else {
/*  81 */       this.textField.setSelecting(event.hasShiftDown());
/*  82 */       seekCursorScreen(event.x(), event.y());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/*  88 */     this.textField.setSelecting(true);
/*  89 */     seekCursorScreen(event.x(), event.y());
/*  90 */     this.textField.setSelecting(event.hasShiftDown());
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  95 */     return this.textField.keyPressed(event);
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 100 */     if (!this.visible || !isFocused() || !event.isAllowedChatCharacter())
/* 101 */       return false; 
/* 104 */     this.textField.insertText(event.codepointAsString());
/* 105 */     return true;
/*     */   }
/*     */   
/*     */   public boolean preeditUpdated(PreeditEvent event) {
/* 110 */     Objects.requireNonNull(this.font);
/* 110 */     this.preeditOverlay = (event != null) ? new IMEPreeditOverlay(event, this.font, 9 + 1) : null;
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 116 */     String value = this.textField.value();
/* 117 */     if (value.isEmpty() && !isFocused()) {
/* 118 */       graphics.textWithWordWrap(this.font, (FormattedText)this.placeholder, getInnerLeft(), getInnerTop(), this.width - totalInnerPadding(), PLACEHOLDER_TEXT_COLOR);
/*     */       return;
/*     */     } 
/* 122 */     int cursor = this.textField.cursor();
/* 123 */     boolean showCursor = (isFocused() && TextCursorUtils.isCursorVisible(Util.getMillis() - this.focusedTime));
/* 124 */     boolean needsValidCursorPos = (this.preeditOverlay != null);
/* 125 */     boolean insertCursor = (cursor < value.length());
/* 127 */     int cursorX = 0;
/* 128 */     int cursorY = 0;
/* 129 */     int drawTop = getInnerTop();
/* 130 */     int innerLeft = getInnerLeft();
/* 133 */     boolean hasDrawnCursor = false;
/* 134 */     for (MultilineTextField.StringView lineView : this.textField.iterateLines()) {
/* 135 */       Objects.requireNonNull(this.font);
/* 135 */       boolean lineWithinVisibleBounds = withinContentAreaTopBottom(drawTop, drawTop + 9);
/* 137 */       if (!hasDrawnCursor && (needsValidCursorPos || showCursor) && insertCursor && cursor >= lineView.beginIndex() && cursor <= lineView.endIndex()) {
/* 139 */         if (lineWithinVisibleBounds) {
/* 140 */           String textBeforeCursor = value.substring(lineView.beginIndex(), cursor);
/* 141 */           int textBeforeCursorPosRight = innerLeft + this.font.width(textBeforeCursor);
/* 142 */           String textAfterCursor = value.substring(cursor, lineView.endIndex());
/* 144 */           graphics.text(this.font, textBeforeCursor, innerLeft, drawTop, this.textColor, this.textShadow);
/* 145 */           graphics.text(this.font, textAfterCursor, textBeforeCursorPosRight, drawTop, this.textColor, this.textShadow);
/* 147 */           cursorX = textBeforeCursorPosRight;
/* 148 */           cursorY = drawTop;
/* 149 */           if (showCursor) {
/* 150 */             Objects.requireNonNull(this.font);
/* 150 */             TextCursorUtils.extractInsertCursor(graphics, cursorX, cursorY, this.cursorColor, 9 + 1);
/*     */           } 
/* 152 */           hasDrawnCursor = true;
/*     */         } 
/* 156 */       } else if (lineWithinVisibleBounds) {
/* 157 */         String substring = value.substring(lineView.beginIndex(), lineView.endIndex());
/* 158 */         graphics.text(this.font, substring, innerLeft, drawTop, this.textColor, this.textShadow);
/* 159 */         if ((needsValidCursorPos || showCursor) && !insertCursor) {
/* 161 */           cursorX = innerLeft + this.font.width(substring);
/* 162 */           cursorY = drawTop;
/*     */         } 
/*     */       } 
/* 167 */       Objects.requireNonNull(this.font);
/* 167 */       drawTop += 9;
/*     */     } 
/* 170 */     Objects.requireNonNull(this.font);
/* 170 */     if (showCursor && !insertCursor && withinContentAreaTopBottom(cursorY, cursorY + 9))
/* 171 */       TextCursorUtils.extractAppendCursor(graphics, this.font, cursorX, cursorY, this.cursorColor, this.textShadow); 
/* 174 */     if (this.textField.hasSelection()) {
/* 175 */       MultilineTextField.StringView selection = this.textField.getSelected();
/* 177 */       int drawX = getInnerLeft();
/* 178 */       drawTop = getInnerTop();
/* 179 */       for (MultilineTextField.StringView lineView : this.textField.iterateLines()) {
/* 180 */         if (selection.beginIndex() > lineView.endIndex()) {
/* 181 */           Objects.requireNonNull(this.font);
/* 181 */           drawTop += 9;
/*     */           continue;
/*     */         } 
/* 183 */         if (lineView.beginIndex() > selection.endIndex())
/*     */           break; 
/* 187 */         Objects.requireNonNull(this.font);
/* 187 */         if (withinContentAreaTopBottom(drawTop, drawTop + 9)) {
/* 188 */           int drawEnd, drawBegin = this.font.width(value.substring(lineView.beginIndex(), Math.max(selection.beginIndex(), lineView.beginIndex())));
/* 190 */           if (selection.endIndex() > lineView.endIndex()) {
/* 191 */             drawEnd = this.width - innerPadding();
/*     */           } else {
/* 193 */             drawEnd = this.font.width(value.substring(lineView.beginIndex(), selection.endIndex()));
/*     */           } 
/* 195 */           Objects.requireNonNull(this.font);
/* 195 */           graphics.textHighlight(drawX + drawBegin, drawTop, drawX + drawEnd, drawTop + 9, true);
/*     */         } 
/* 198 */         Objects.requireNonNull(this.font);
/* 198 */         drawTop += 9;
/*     */       } 
/*     */     } 
/* 202 */     if (isHovered())
/* 203 */       graphics.requestCursor(CursorTypes.IBEAM); 
/* 206 */     if (this.preeditOverlay != null) {
/* 207 */       this.preeditOverlay.updateInputPosition(cursorX, cursorY);
/* 208 */       graphics.setPreeditOverlay(this.preeditOverlay);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractDecorations(GuiGraphicsExtractor graphics) {
/* 214 */     super.extractDecorations(graphics);
/* 216 */     if (this.textField.hasCharacterLimit()) {
/* 217 */       int characterLimit = this.textField.characterLimit();
/* 218 */       MutableComponent mutableComponent = Component.translatable("gui.multiLineEditBox.character_limit", new Object[] { Integer.valueOf(this.textField.value().length()), Integer.valueOf(characterLimit) });
/* 219 */       graphics.text(this.font, (Component)mutableComponent, getX() + this.width - this.font.width((FormattedText)mutableComponent), getY() + this.height + 4, -6250336);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getInnerHeight() {
/* 225 */     Objects.requireNonNull(this.font);
/* 225 */     return 9 * this.textField.getLineCount();
/*     */   }
/*     */   
/*     */   private void scrollToCursor() {
/* 229 */     double scrollAmount = scrollAmount();
/* 231 */     Objects.requireNonNull(this.font);
/* 231 */     MultilineTextField.StringView firstFullyVisibleLine = this.textField.getLineView((int)(scrollAmount / 9.0D));
/* 233 */     if (this.textField.cursor() <= firstFullyVisibleLine.beginIndex()) {
/* 234 */       Objects.requireNonNull(this.font);
/* 234 */       scrollAmount = (this.textField.getLineAtCursor() * 9);
/*     */     } else {
/* 236 */       Objects.requireNonNull(this.font);
/* 236 */       MultilineTextField.StringView lastFullyVisibleLine = this.textField.getLineView((int)((scrollAmount + this.height) / 9.0D) - 1);
/* 238 */       if (this.textField.cursor() > lastFullyVisibleLine.endIndex()) {
/* 239 */         Objects.requireNonNull(this.font);
/* 239 */         Objects.requireNonNull(this.font);
/* 239 */         scrollAmount = (this.textField.getLineAtCursor() * 9 - this.height + 9 + totalInnerPadding());
/*     */       } 
/*     */     } 
/* 243 */     setScrollAmount(scrollAmount);
/*     */   }
/*     */   
/*     */   private void seekCursorScreen(double x, double y) {
/* 247 */     double mouseX = x - getX() - innerPadding();
/* 248 */     double mouseY = y - getY() - innerPadding() + scrollAmount();
/* 250 */     this.textField.seekCursorToPoint(mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 255 */     super.setFocused(focused);
/* 256 */     if (focused)
/* 257 */       this.focusedTime = Util.getMillis(); 
/* 259 */     Minecraft.getInstance().onTextInputFocusChange(this, focused);
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 263 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private int x;
/*     */     
/*     */     private int y;
/*     */     
/* 269 */     private Component placeholder = CommonComponents.EMPTY;
/*     */     
/* 270 */     private int textColor = -2039584;
/*     */     
/*     */     private boolean textShadow = true;
/*     */     
/* 272 */     private int cursorColor = -3092272;
/*     */     
/*     */     private boolean showBackground = true;
/*     */     
/*     */     private boolean showDecorations = true;
/*     */     
/*     */     public Builder setX(int x) {
/* 280 */       this.x = x;
/* 281 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setY(int y) {
/* 285 */       this.y = y;
/* 286 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPlaceholder(Component placeholder) {
/* 290 */       this.placeholder = placeholder;
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTextColor(int textColor) {
/* 295 */       this.textColor = textColor;
/* 296 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTextShadow(boolean textShadow) {
/* 300 */       this.textShadow = textShadow;
/* 301 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCursorColor(int cursorColor) {
/* 305 */       this.cursorColor = cursorColor;
/* 306 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShowBackground(boolean showBackground) {
/* 310 */       this.showBackground = showBackground;
/* 311 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShowDecorations(boolean showDecorations) {
/* 315 */       this.showDecorations = showDecorations;
/* 316 */       return this;
/*     */     }
/*     */     
/*     */     public MultiLineEditBox build(Font font, int width, int height, Component narration) {
/* 320 */       return new MultiLineEditBox(font, this.x, this.y, width, height, this.placeholder, narration, this.textColor, this.textShadow, this.cursorColor, this.showBackground, this.showDecorations);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultiLineEditBox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */