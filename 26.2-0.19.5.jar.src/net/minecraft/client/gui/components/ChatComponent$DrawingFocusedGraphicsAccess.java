/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector2f;
/*     */ 
/*     */ class DrawingFocusedGraphicsAccess implements ChatComponent.ChatGraphicsAccess, Consumer<Style> {
/*     */   private final GuiGraphicsExtractor graphics;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final ActiveTextCollector textRenderer;
/*     */   
/*     */   private ActiveTextCollector.Parameters parameters;
/*     */   
/*     */   private final int globalMouseX;
/*     */   
/*     */   private final int globalMouseY;
/*     */   
/* 657 */   private final Vector2f localMousePos = new Vector2f();
/*     */   
/*     */   private Style hoveredStyle;
/*     */   
/*     */   private final boolean changeCursorOnInsertions;
/*     */   
/*     */   public DrawingFocusedGraphicsAccess(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY, boolean changeCursorOnInsertions) {
/* 663 */     this.graphics = graphics;
/* 664 */     this.font = font;
/* 665 */     this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR, this);
/* 666 */     this.globalMouseX = mouseX;
/* 667 */     this.globalMouseY = mouseY;
/* 668 */     this.changeCursorOnInsertions = changeCursorOnInsertions;
/* 669 */     this.parameters = this.textRenderer.defaultParameters();
/* 670 */     updateLocalMousePos();
/*     */   }
/*     */   
/*     */   private void updateLocalMousePos() {
/* 674 */     this.graphics.pose().invert(new Matrix3x2f()).transformPosition(this.globalMouseX, this.globalMouseY, this.localMousePos);
/*     */   }
/*     */   
/*     */   public void updatePose(Consumer<Matrix3x2f> updater) {
/* 679 */     updater.accept(this.graphics.pose());
/* 680 */     this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/* 681 */     updateLocalMousePos();
/*     */   }
/*     */   
/*     */   public void fill(int x0, int y0, int x1, int y1, int color) {
/* 686 */     this.graphics.fill(x0, y0, x1, y1, color);
/*     */   }
/*     */   
/*     */   public void accept(Style style) {
/* 691 */     this.hoveredStyle = style;
/*     */   }
/*     */   
/*     */   public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 696 */     this.hoveredStyle = null;
/* 697 */     this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 698 */     if (this.changeCursorOnInsertions && this.hoveredStyle != null && this.hoveredStyle.getInsertion() != null)
/* 699 */       this.graphics.requestCursor(CursorTypes.POINTING_HAND); 
/* 701 */     return (this.hoveredStyle != null);
/*     */   }
/*     */   
/*     */   private boolean isMouseOver(int left, int top, int right, int bottom) {
/* 705 */     return ActiveTextCollector.isPointInRectangle(this.localMousePos.x, this.localMousePos.y, left, top, right, bottom);
/*     */   }
/*     */   
/*     */   public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 710 */     int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 711 */     this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/* 712 */     if (isMouseOver(x0, y0, x1, y1))
/* 713 */       showTooltip(tag); 
/*     */   }
/*     */   
/*     */   public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {
/* 719 */     int top = bottom - icon.height - 1;
/* 720 */     int right = left + icon.width;
/* 721 */     boolean isMouseOver = isMouseOver(left, top, right, bottom);
/* 722 */     if (isMouseOver)
/* 723 */       showTooltip(tag); 
/* 726 */     if (forceVisible || isMouseOver)
/* 727 */       icon.extractRenderState(this.graphics, left, top); 
/*     */   }
/*     */   
/*     */   private void showTooltip(GuiMessageTag tag) {
/* 732 */     if (tag.text() != null)
/* 733 */       this.graphics.setTooltipForNextFrame(this.font, this.font.split((FormattedText)tag.text(), 210), this.globalMouseX, this.globalMouseY); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$DrawingFocusedGraphicsAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */