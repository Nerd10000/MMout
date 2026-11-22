/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class EditBox extends AbstractWidget {
/*  33 */   private static final WidgetSprites SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("widget/text_field"), Identifier.withDefaultNamespace("widget/text_field_highlighted"));
/*     */   
/*     */   public static final int BACKWARDS = -1;
/*     */   
/*     */   public static final int FORWARDS = 1;
/*     */   
/*     */   public static final int DEFAULT_TEXT_COLOR = -2039584;
/*     */   
/*  38 */   public static final Style DEFAULT_HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
/*     */   
/*  39 */   public static final Style SEARCH_HINT_STYLE = Style.EMPTY.applyFormats(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */   
/*     */   private final Font font;
/*     */   
/*  42 */   private String value = "";
/*     */   
/*  43 */   private int maxLength = 32;
/*     */   
/*     */   private boolean bordered = true;
/*     */   
/*     */   private boolean canLoseFocus = true;
/*     */   
/*     */   private boolean isEditable = true;
/*     */   
/*     */   private boolean centered = false;
/*     */   
/*     */   private boolean textShadow = true;
/*     */   
/*     */   private boolean invertHighlightedTextColor = true;
/*     */   
/*     */   private int displayPos;
/*     */   
/*     */   private int cursorPos;
/*     */   
/*     */   private int highlightPos;
/*     */   
/*  53 */   private int textColor = -2039584;
/*     */   
/*  54 */   private int textColorUneditable = -9408400;
/*     */   
/*     */   private String suggestion;
/*     */   
/*     */   private Consumer<String> responder;
/*     */   
/*  57 */   private final List<TextFormatter> formatters = new ArrayList<>();
/*     */   
/*     */   private Component hint;
/*     */   
/*     */   private IMEPreeditOverlay preeditOverlay;
/*     */   
/*  61 */   private long focusedTime = Util.getMillis();
/*     */   
/*     */   private int textX;
/*     */   
/*     */   private int textY;
/*     */   
/*     */   public EditBox(Font font, Component narration) {
/*  66 */     this(font, 150, 20, narration);
/*     */   }
/*     */   
/*     */   public EditBox(Font font, int width, int height, Component narration) {
/*  70 */     this(font, 0, 0, width, height, narration);
/*     */   }
/*     */   
/*     */   public EditBox(Font font, int x, int y, int width, int height, Component narration) {
/*  74 */     this(font, x, y, width, height, (EditBox)null, narration);
/*     */   }
/*     */   
/*     */   public EditBox(Font font, int x, int y, int width, int height, EditBox oldBox, Component narration) {
/*  78 */     super(x, y, width, height, narration);
/*  79 */     this.font = font;
/*  80 */     if (oldBox != null)
/*  81 */       setValue(oldBox.getValue()); 
/*  83 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setResponder(Consumer<String> responder) {
/*  87 */     this.responder = responder;
/*     */   }
/*     */   
/*     */   public void addFormatter(TextFormatter formatter) {
/*  91 */     this.formatters.add(formatter);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/*  96 */     Component message = getMessage();
/*  97 */     return Component.translatable("gui.narrate.editBox", new Object[] { message, this.value });
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 101 */     if (value.length() > this.maxLength) {
/* 102 */       this.value = value.substring(0, this.maxLength);
/*     */     } else {
/* 104 */       this.value = value;
/*     */     } 
/* 107 */     moveCursorToEnd(false);
/* 108 */     setHighlightPos(this.cursorPos);
/* 109 */     onValueChange(value);
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 113 */     return this.value;
/*     */   }
/*     */   
/*     */   public String getHighlighted() {
/* 117 */     int start = Math.min(this.cursorPos, this.highlightPos);
/* 118 */     int end = Math.max(this.cursorPos, this.highlightPos);
/* 120 */     return this.value.substring(start, end);
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 125 */     super.setX(x);
/* 126 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 131 */     super.setY(y);
/* 132 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void insertText(String input) {
/* 136 */     int start = Math.min(this.cursorPos, this.highlightPos);
/* 137 */     int end = Math.max(this.cursorPos, this.highlightPos);
/* 138 */     int maxInsertionLength = this.maxLength - this.value.length() - start - end;
/* 139 */     if (maxInsertionLength <= 0)
/*     */       return; 
/* 143 */     String text = StringUtil.filterText(input);
/* 144 */     int insertionLength = text.length();
/* 145 */     if (maxInsertionLength < insertionLength) {
/* 146 */       if (Character.isHighSurrogate(text.charAt(maxInsertionLength - 1)))
/* 147 */         maxInsertionLength--; 
/* 149 */       text = text.substring(0, maxInsertionLength);
/* 150 */       insertionLength = maxInsertionLength;
/*     */     } 
/* 153 */     this.value = (new StringBuilder(this.value)).replace(start, end, text).toString();
/* 154 */     setCursorPosition(start + insertionLength);
/* 155 */     setHighlightPos(this.cursorPos);
/* 157 */     onValueChange(this.value);
/*     */   }
/*     */   
/*     */   private void onValueChange(String value) {
/* 161 */     if (this.responder != null)
/* 162 */       this.responder.accept(value); 
/* 164 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   private void deleteText(int dir, boolean wholeWord) {
/* 168 */     if (wholeWord) {
/* 169 */       deleteWords(dir);
/*     */     } else {
/* 171 */       deleteChars(dir);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteWords(int dir) {
/* 176 */     if (this.value.isEmpty())
/*     */       return; 
/* 180 */     if (this.highlightPos != this.cursorPos) {
/* 181 */       insertText("");
/*     */       return;
/*     */     } 
/* 185 */     deleteCharsToPos(getWordPosition(dir));
/*     */   }
/*     */   
/*     */   public void deleteChars(int dir) {
/* 189 */     deleteCharsToPos(getCursorPos(dir));
/*     */   }
/*     */   
/*     */   public void deleteCharsToPos(int pos) {
/* 193 */     if (this.value.isEmpty())
/*     */       return; 
/* 197 */     if (this.highlightPos != this.cursorPos) {
/* 198 */       insertText("");
/*     */       return;
/*     */     } 
/* 202 */     int start = Math.min(pos, this.cursorPos);
/* 203 */     int end = Math.max(pos, this.cursorPos);
/* 204 */     if (start == end)
/*     */       return; 
/* 208 */     this.value = (new StringBuilder(this.value)).delete(start, end).toString();
/* 209 */     setCursorPosition(start);
/* 210 */     onValueChange(this.value);
/* 211 */     moveCursorTo(start, false);
/*     */   }
/*     */   
/*     */   public int getWordPosition(int dir) {
/* 215 */     return getWordPosition(dir, getCursorPosition());
/*     */   }
/*     */   
/*     */   private int getWordPosition(int dir, int from) {
/* 219 */     return getWordPosition(dir, from, true);
/*     */   }
/*     */   
/*     */   private int getWordPosition(int dir, int from, boolean stripSpaces) {
/* 223 */     int result = from;
/* 224 */     boolean reverse = (dir < 0);
/* 225 */     int abs = Math.abs(dir);
/* 227 */     for (int i = 0; i < abs; i++) {
/* 228 */       if (reverse) {
/* 229 */         while (stripSpaces && result > 0 && this.value.charAt(result - 1) == ' ')
/* 230 */           result--; 
/* 232 */         while (result > 0 && this.value.charAt(result - 1) != ' ')
/* 233 */           result--; 
/*     */       } else {
/* 236 */         int length = this.value.length();
/* 238 */         result = this.value.indexOf(' ', result);
/* 239 */         if (result == -1) {
/* 240 */           result = length;
/*     */         } else {
/* 242 */           while (stripSpaces && result < length && this.value.charAt(result) == ' ')
/* 243 */             result++; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 249 */     return result;
/*     */   }
/*     */   
/*     */   public void moveCursor(int dir, boolean hasShiftDown) {
/* 253 */     moveCursorTo(getCursorPos(dir), hasShiftDown);
/*     */   }
/*     */   
/*     */   private int getCursorPos(int dir) {
/* 257 */     return Util.offsetByCodepoints(this.value, this.cursorPos, dir);
/*     */   }
/*     */   
/*     */   public void moveCursorTo(int dir, boolean extendSelection) {
/* 261 */     setCursorPosition(dir);
/* 263 */     if (!extendSelection)
/* 264 */       setHighlightPos(this.cursorPos); 
/* 267 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int pos) {
/* 271 */     this.cursorPos = Mth.clamp(pos, 0, this.value.length());
/* 272 */     scrollTo(this.cursorPos);
/*     */   }
/*     */   
/*     */   public void moveCursorToStart(boolean hasShiftDown) {
/* 276 */     moveCursorTo(0, hasShiftDown);
/*     */   }
/*     */   
/*     */   public void moveCursorToEnd(boolean hasShiftDown) {
/* 280 */     moveCursorTo(this.value.length(), hasShiftDown);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 285 */     if (!isActive() || !isFocused())
/* 286 */       return false; 
/* 289 */     switch (event.key()) {
/*     */       case 263:
/* 291 */         if (event.hasControlDownWithQuirk()) {
/* 292 */           moveCursorTo(getWordPosition(-1), event.hasShiftDown());
/*     */         } else {
/* 294 */           moveCursor(-1, event.hasShiftDown());
/*     */         } 
/* 297 */         return true;
/*     */       case 262:
/* 300 */         if (event.hasControlDownWithQuirk()) {
/* 301 */           moveCursorTo(getWordPosition(1), event.hasShiftDown());
/*     */         } else {
/* 303 */           moveCursor(1, event.hasShiftDown());
/*     */         } 
/* 306 */         return true;
/*     */       case 259:
/* 309 */         if (this.isEditable)
/* 310 */           deleteText(-1, event.hasControlDownWithQuirk()); 
/* 313 */         return true;
/*     */       case 261:
/* 316 */         if (this.isEditable)
/* 317 */           deleteText(1, event.hasControlDownWithQuirk()); 
/* 320 */         return true;
/*     */       case 268:
/* 323 */         moveCursorToStart(event.hasShiftDown());
/* 325 */         return true;
/*     */       case 269:
/* 328 */         moveCursorToEnd(event.hasShiftDown());
/* 330 */         return true;
/*     */     } 
/* 334 */     if (event.isSelectAll()) {
/* 335 */       moveCursorToEnd(false);
/* 336 */       setHighlightPos(0);
/* 337 */       return true;
/*     */     } 
/* 338 */     if (event.isCopy()) {
/* 339 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getHighlighted());
/* 340 */       return true;
/*     */     } 
/* 341 */     if (event.isPaste()) {
/* 342 */       if (isEditable())
/* 343 */         insertText((Minecraft.getInstance()).keyboardHandler.getClipboard()); 
/* 345 */       return true;
/*     */     } 
/* 346 */     if (event.isCut()) {
/* 347 */       (Minecraft.getInstance()).keyboardHandler.setClipboard(getHighlighted());
/* 348 */       if (isEditable())
/* 349 */         insertText(""); 
/* 351 */       return true;
/*     */     } 
/* 354 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canConsumeInput() {
/* 358 */     return (isActive() && isFocused() && isEditable());
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 363 */     if (!canConsumeInput())
/* 364 */       return false; 
/* 366 */     if (event.isAllowedChatCharacter()) {
/* 367 */       if (this.isEditable)
/* 368 */         insertText(event.codepointAsString()); 
/* 371 */       return true;
/*     */     } 
/* 374 */     return false;
/*     */   }
/*     */   
/*     */   public boolean preeditUpdated(PreeditEvent event) {
/* 379 */     Objects.requireNonNull(this.font);
/* 379 */     this.preeditOverlay = (event != null) ? new IMEPreeditOverlay(event, this.font, 9 + 1) : null;
/* 380 */     return true;
/*     */   }
/*     */   
/*     */   private int findClickedPositionInText(MouseButtonEvent event) {
/* 384 */     int positionInText = Math.min(Mth.floor(event.x()) - this.textX, getInnerWidth());
/* 385 */     String displayed = this.value.substring(this.displayPos);
/* 386 */     return this.displayPos + this.font.plainSubstrByWidth(displayed, positionInText).length();
/*     */   }
/*     */   
/*     */   private void selectWord(MouseButtonEvent event) {
/* 390 */     int clickedPosition = findClickedPositionInText(event);
/* 391 */     int wordStart = getWordPosition(-1, clickedPosition);
/* 392 */     int wordEnd = getWordPosition(1, clickedPosition);
/* 393 */     moveCursorTo(wordStart, false);
/* 394 */     moveCursorTo(wordEnd, true);
/*     */   }
/*     */   
/*     */   public void onClick(MouseButtonEvent event, boolean doubleClick) {
/* 399 */     if (doubleClick) {
/* 400 */       selectWord(event);
/*     */     } else {
/* 402 */       moveCursorTo(findClickedPositionInText(event), event.hasShiftDown());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/* 408 */     moveCursorTo(findClickedPositionInText(event), true);
/*     */   }
/*     */   
/*     */   public void playDownSound(SoundManager soundManager) {}
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 417 */     if (!isVisible())
/*     */       return; 
/* 421 */     if (isBordered()) {
/* 422 */       Identifier sprite = SPRITES.get(isActive(), isFocused());
/* 423 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), getWidth(), getHeight());
/*     */     } 
/* 426 */     int color = this.isEditable ? this.textColor : this.textColorUneditable;
/* 427 */     int relCursorPos = this.cursorPos - this.displayPos;
/* 428 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), getInnerWidth());
/* 429 */     boolean cursorOnScreen = (relCursorPos >= 0 && relCursorPos <= displayed.length());
/* 430 */     boolean showCursor = (isFocused() && TextCursorUtils.isCursorVisible(Util.getMillis() - this.focusedTime) && cursorOnScreen);
/* 431 */     int drawX = this.textX;
/* 433 */     int relHighlightPos = Mth.clamp(this.highlightPos - this.displayPos, 0, displayed.length());
/* 435 */     if (!displayed.isEmpty()) {
/* 436 */       String half = cursorOnScreen ? displayed.substring(0, relCursorPos) : displayed;
/* 437 */       FormattedCharSequence charSequence = applyFormat(half, this.displayPos);
/* 438 */       graphics.text(this.font, charSequence, drawX, this.textY, color, this.textShadow);
/* 439 */       drawX += this.font.width(charSequence) + 1;
/*     */     } 
/* 442 */     boolean insert = (this.cursorPos < this.value.length() || this.value.length() >= getMaxLength());
/* 443 */     int cursorX = drawX;
/* 445 */     if (!cursorOnScreen) {
/* 446 */       cursorX = (relCursorPos > 0) ? (this.textX + this.width) : this.textX;
/* 447 */     } else if (insert) {
/* 448 */       cursorX--;
/* 449 */       drawX--;
/*     */     } 
/* 452 */     if (!displayed.isEmpty() && cursorOnScreen && relCursorPos < displayed.length())
/* 453 */       graphics.text(this.font, applyFormat(displayed.substring(relCursorPos), this.cursorPos), drawX, this.textY, color, this.textShadow); 
/* 456 */     if (this.hint != null && displayed.isEmpty() && !isFocused())
/* 457 */       graphics.text(this.font, this.hint, drawX, this.textY, color); 
/* 460 */     if (!insert && this.suggestion != null)
/* 461 */       graphics.text(this.font, this.suggestion, cursorX - 1, this.textY, -8355712, this.textShadow); 
/* 464 */     if (relHighlightPos != relCursorPos) {
/* 465 */       int highlightX = this.textX + this.font.width(displayed.substring(0, relHighlightPos));
/* 470 */       Objects.requireNonNull(this.font);
/* 470 */       graphics.textHighlight(Math.min(cursorX, getX() + this.width), this.textY - 1, Math.min(highlightX - 1, getX() + this.width), this.textY + 1 + 9, this.invertHighlightedTextColor);
/*     */     } 
/* 475 */     if (showCursor)
/* 476 */       if (insert) {
/* 477 */         Objects.requireNonNull(this.font);
/* 477 */         TextCursorUtils.extractInsertCursor(graphics, cursorX, this.textY, color, 9 + 1);
/*     */       } else {
/* 479 */         TextCursorUtils.extractAppendCursor(graphics, this.font, cursorX, this.textY, color, this.textShadow);
/*     */       }  
/* 483 */     if (isHovered())
/* 484 */       graphics.requestCursor(isEditable() ? CursorTypes.IBEAM : CursorTypes.NOT_ALLOWED); 
/* 487 */     if (this.preeditOverlay != null) {
/* 488 */       this.preeditOverlay.updateInputPosition(cursorX, this.textY);
/* 489 */       graphics.setPreeditOverlay(this.preeditOverlay);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FormattedCharSequence applyFormat(String text, int offset) {
/* 494 */     for (TextFormatter formatter : this.formatters) {
/* 495 */       FormattedCharSequence formattedCharSequence = formatter.format(text, offset);
/* 497 */       if (formattedCharSequence != null)
/* 498 */         return formattedCharSequence; 
/*     */     } 
/* 501 */     return FormattedCharSequence.forward(text, Style.EMPTY);
/*     */   }
/*     */   
/*     */   private void updateTextPosition() {
/* 506 */     if (this.font == null)
/*     */       return; 
/* 509 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), getInnerWidth());
/* 510 */     this.textX = getX() + (isCentered() ? ((getWidth() - this.font.width(displayed)) / 2) : (this.bordered ? 4 : 0));
/* 511 */     this.textY = this.bordered ? (getY() + (this.height - 8) / 2) : getY();
/*     */   }
/*     */   
/*     */   public void setMaxLength(int maxLength) {
/* 515 */     this.maxLength = maxLength;
/* 517 */     if (this.value.length() > maxLength) {
/* 518 */       this.value = this.value.substring(0, maxLength);
/* 519 */       onValueChange(this.value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getMaxLength() {
/* 524 */     return this.maxLength;
/*     */   }
/*     */   
/*     */   public int getCursorPosition() {
/* 528 */     return this.cursorPos;
/*     */   }
/*     */   
/*     */   public boolean isBordered() {
/* 532 */     return this.bordered;
/*     */   }
/*     */   
/*     */   public void setBordered(boolean bordered) {
/* 536 */     this.bordered = bordered;
/* 537 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setTextColor(int textColor) {
/* 541 */     this.textColor = textColor;
/*     */   }
/*     */   
/*     */   public void setTextColorUneditable(int textColorUneditable) {
/* 545 */     this.textColorUneditable = textColorUneditable;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 550 */     if (!this.canLoseFocus && !focused)
/*     */       return; 
/* 553 */     super.setFocused(focused);
/* 554 */     if (focused)
/* 555 */       this.focusedTime = Util.getMillis(); 
/* 557 */     if (isEditable())
/* 558 */       Minecraft.getInstance().onTextInputFocusChange(this, focused); 
/*     */   }
/*     */   
/*     */   private boolean isEditable() {
/* 563 */     return this.isEditable;
/*     */   }
/*     */   
/*     */   public void setEditable(boolean isEditable) {
/* 567 */     if (isFocused())
/* 568 */       Minecraft.getInstance().onTextInputFocusChange(this, isEditable); 
/* 570 */     this.isEditable = isEditable;
/*     */   }
/*     */   
/*     */   private boolean isCentered() {
/* 574 */     return this.centered;
/*     */   }
/*     */   
/*     */   public void setCentered(boolean centered) {
/* 578 */     this.centered = centered;
/* 579 */     updateTextPosition();
/*     */   }
/*     */   
/*     */   public void setTextShadow(boolean textShadow) {
/* 583 */     this.textShadow = textShadow;
/*     */   }
/*     */   
/*     */   public void setInvertHighlightedTextColor(boolean invertHighlightedTextColor) {
/* 587 */     this.invertHighlightedTextColor = invertHighlightedTextColor;
/*     */   }
/*     */   
/*     */   public int getInnerWidth() {
/* 591 */     return isBordered() ? (this.width - 8) : this.width;
/*     */   }
/*     */   
/*     */   public void setHighlightPos(int pos) {
/* 595 */     this.highlightPos = Mth.clamp(pos, 0, this.value.length());
/* 596 */     scrollTo(this.highlightPos);
/*     */   }
/*     */   
/*     */   private void scrollTo(int pos) {
/* 601 */     if (this.font == null)
/*     */       return; 
/* 605 */     this.displayPos = Math.min(this.displayPos, this.value.length());
/* 607 */     int innerWidth = getInnerWidth();
/* 608 */     String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), innerWidth);
/* 609 */     int lastPos = displayed.length() + this.displayPos;
/* 611 */     if (pos == this.displayPos)
/* 612 */       this.displayPos -= this.font.plainSubstrByWidth(this.value, innerWidth, true).length(); 
/* 614 */     if (pos > lastPos) {
/* 615 */       this.displayPos += pos - lastPos;
/* 616 */     } else if (pos <= this.displayPos) {
/* 617 */       this.displayPos -= this.displayPos - pos;
/*     */     } 
/* 620 */     this.displayPos = Mth.clamp(this.displayPos, 0, this.value.length());
/*     */   }
/*     */   
/*     */   public void setCanLoseFocus(boolean canLoseFocus) {
/* 624 */     this.canLoseFocus = canLoseFocus;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 628 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 632 */     this.visible = visible;
/*     */   }
/*     */   
/*     */   public void setSuggestion(String suggestion) {
/* 636 */     this.suggestion = suggestion;
/*     */   }
/*     */   
/*     */   public int getScreenX(int charIndex) {
/* 640 */     if (charIndex > this.value.length())
/* 641 */       return getX(); 
/* 643 */     return getX() + this.font.width(this.value.substring(0, charIndex));
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 648 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/*     */   }
/*     */   
/*     */   public void setHint(Component hint) {
/* 652 */     boolean hasNoStyle = hint.getStyle().equals(Style.EMPTY);
/* 653 */     this.hint = hasNoStyle ? (Component)hint.copy().withStyle(DEFAULT_HINT_STYLE) : hint;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TextFormatter {
/*     */     FormattedCharSequence format(String param1String, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\EditBox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */