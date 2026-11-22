/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.suggestion.Suggestion;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.Rect2i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ public class SuggestionsList {
/*     */   private final Rect2i rect;
/*     */   
/*     */   private final String originalContents;
/*     */   
/*     */   private final List<Suggestion> suggestionList;
/*     */   
/*     */   private int offset;
/*     */   
/*     */   private int current;
/*     */   
/* 482 */   private Vec2 lastMouse = Vec2.ZERO;
/*     */   
/*     */   private boolean tabCycles;
/*     */   
/*     */   private int lastNarratedEntry;
/*     */   
/*     */   private SuggestionsList(int x, int y, int width, List<Suggestion> suggestionList, boolean immediateNarration) {
/* 487 */     int listX = x - (CommandSuggestions.this.input.isBordered() ? 0 : 1);
/* 488 */     int listY = CommandSuggestions.this.anchorToBottom ? (y - 3 - Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12) : (y - (CommandSuggestions.this.input.isBordered() ? 1 : 0));
/* 489 */     this.rect = new Rect2i(listX, listY, width + 1, Math.min(suggestionList.size(), CommandSuggestions.this.suggestionLineLimit) * 12);
/* 490 */     this.originalContents = CommandSuggestions.this.input.getValue();
/* 491 */     this.lastNarratedEntry = immediateNarration ? -1 : 0;
/* 492 */     this.suggestionList = suggestionList;
/* 493 */     select(0);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 497 */     int limit = Math.min(this.suggestionList.size(), CommandSuggestions.this.suggestionLineLimit);
/* 498 */     int unselectedColor = -5592406;
/* 499 */     boolean hasPrevious = (this.offset > 0);
/* 500 */     boolean hasNext = (this.suggestionList.size() > this.offset + limit);
/* 501 */     boolean limited = (hasPrevious || hasNext);
/* 502 */     boolean mouseMoved = (this.lastMouse.x != mouseX || this.lastMouse.y != mouseY);
/* 504 */     if (mouseMoved)
/* 505 */       this.lastMouse = new Vec2(mouseX, mouseY); 
/* 508 */     if (limited) {
/* 509 */       graphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), CommandSuggestions.this.fillColor);
/* 510 */       graphics.fill(this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, CommandSuggestions.this.fillColor);
/* 511 */       if (hasPrevious)
/* 512 */         for (int x = 0; x < this.rect.getWidth(); x++) {
/* 513 */           if (x % 2 == 0)
/* 514 */             graphics.fill(this.rect.getX() + x, this.rect.getY() - 1, this.rect.getX() + x + 1, this.rect.getY(), -1); 
/*     */         }  
/* 518 */       if (hasNext)
/* 519 */         for (int x = 0; x < this.rect.getWidth(); x++) {
/* 520 */           if (x % 2 == 0)
/* 521 */             graphics.fill(this.rect.getX() + x, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + x + 1, this.rect.getY() + this.rect.getHeight() + 1, -1); 
/*     */         }  
/*     */     } 
/* 527 */     boolean hovered = false;
/* 528 */     for (int i = 0; i < limit; i++) {
/* 529 */       Suggestion suggestion = this.suggestionList.get(i + this.offset);
/* 530 */       graphics.fill(this.rect.getX(), this.rect.getY() + 12 * i, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * i + 12, CommandSuggestions.this.fillColor);
/* 531 */       if (mouseX > this.rect.getX() && mouseX < this.rect.getX() + this.rect.getWidth() && mouseY > this.rect.getY() + 12 * i && mouseY < this.rect.getY() + 12 * i + 12) {
/* 532 */         if (mouseMoved)
/* 533 */           select(i + this.offset); 
/* 535 */         hovered = true;
/*     */       } 
/* 537 */       graphics.text(CommandSuggestions.this.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * i, (i + this.offset == this.current) ? -256 : -5592406);
/*     */     } 
/* 540 */     if (hovered) {
/* 541 */       Message tooltip = ((Suggestion)this.suggestionList.get(this.current)).getTooltip();
/* 542 */       if (tooltip != null)
/* 543 */         graphics.setTooltipForNextFrame(CommandSuggestions.this.font, ComponentUtils.fromMessage(tooltip), mouseX, mouseY); 
/*     */     } 
/* 547 */     if (this.rect.contains(mouseX, mouseY))
/* 548 */       graphics.requestCursor(CursorTypes.POINTING_HAND); 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(int x, int y) {
/* 553 */     if (!this.rect.contains(x, y))
/* 554 */       return false; 
/* 557 */     int line = (y - this.rect.getY()) / 12 + this.offset;
/* 558 */     if (line >= 0 && line < this.suggestionList.size()) {
/* 559 */       select(line);
/* 560 */       useSuggestion();
/*     */     } 
/* 563 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double scroll) {
/* 567 */     int mouseX = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledXPos(CommandSuggestions.this.minecraft.getWindow());
/* 568 */     int mouseY = (int)CommandSuggestions.this.minecraft.mouseHandler.getScaledYPos(CommandSuggestions.this.minecraft.getWindow());
/* 570 */     if (this.rect.contains(mouseX, mouseY)) {
/* 571 */       this.offset = Mth.clamp((int)(this.offset - scroll), 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 572 */       return true;
/*     */     } 
/* 575 */     return false;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 579 */     if (event.isUp()) {
/* 580 */       cycle(-1);
/* 581 */       this.tabCycles = false;
/* 582 */       return true;
/*     */     } 
/* 583 */     if (event.isDown()) {
/* 584 */       cycle(1);
/* 585 */       this.tabCycles = false;
/* 586 */       return true;
/*     */     } 
/* 587 */     if (event.isCycleFocus()) {
/* 588 */       if (this.tabCycles)
/* 589 */         cycle(event.hasShiftDown() ? -1 : 1); 
/* 591 */       useSuggestion();
/* 592 */       return true;
/*     */     } 
/* 593 */     if (event.isEscape()) {
/* 594 */       CommandSuggestions.this.hide();
/* 595 */       CommandSuggestions.this.input.setSuggestion(null);
/* 597 */       return true;
/*     */     } 
/* 600 */     return false;
/*     */   }
/*     */   
/*     */   public void cycle(int direction) {
/* 604 */     select(this.current + direction);
/* 605 */     int first = this.offset;
/* 606 */     int last = this.offset + CommandSuggestions.this.suggestionLineLimit - 1;
/* 607 */     if (this.current < first) {
/* 608 */       this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/* 609 */     } else if (this.current > last) {
/* 610 */       this.offset = Mth.clamp(this.current + CommandSuggestions.this.lineStartOffset - CommandSuggestions.this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void select(int index) {
/* 615 */     this.current = index;
/* 617 */     if (this.current < 0)
/* 618 */       this.current += this.suggestionList.size(); 
/* 620 */     if (this.current >= this.suggestionList.size())
/* 621 */       this.current -= this.suggestionList.size(); 
/* 624 */     Suggestion suggestion = this.suggestionList.get(this.current);
/* 625 */     CommandSuggestions.this.input.setSuggestion(CommandSuggestions.calculateSuggestionSuffix(CommandSuggestions.this.input.getValue(), suggestion.apply(this.originalContents)));
/* 627 */     if (this.lastNarratedEntry != this.current)
/* 628 */       CommandSuggestions.this.minecraft.getNarrator().saySystemNow(getNarrationMessage()); 
/*     */   }
/*     */   
/*     */   public void useSuggestion() {
/* 633 */     Suggestion suggestion = this.suggestionList.get(this.current);
/* 634 */     CommandSuggestions.this.keepSuggestions = true;
/* 635 */     CommandSuggestions.this.input.setValue(suggestion.apply(this.originalContents));
/* 636 */     int end = suggestion.getRange().getStart() + suggestion.getText().length();
/* 637 */     CommandSuggestions.this.input.setCursorPosition(end);
/* 638 */     CommandSuggestions.this.input.setHighlightPos(end);
/* 639 */     select(this.current);
/* 640 */     CommandSuggestions.this.keepSuggestions = false;
/* 641 */     this.tabCycles = true;
/*     */   }
/*     */   
/*     */   private Component getNarrationMessage() {
/* 645 */     this.lastNarratedEntry = this.current;
/* 646 */     Suggestion suggestion = this.suggestionList.get(this.current);
/* 647 */     Message tooltip = suggestion.getTooltip();
/* 648 */     if (tooltip != null)
/* 649 */       return (Component)Component.translatable("narration.suggestion.tooltip", new Object[] { Integer.valueOf(this.current + 1), Integer.valueOf(this.suggestionList.size()), suggestion.getText(), Component.translationArg(tooltip) }); 
/* 651 */     return (Component)Component.translatable("narration.suggestion", new Object[] { Integer.valueOf(this.current + 1), Integer.valueOf(this.suggestionList.size()), suggestion.getText() });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CommandSuggestions$SuggestionsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */