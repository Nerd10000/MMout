/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessageTag;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ class DrawingBackgroundGraphicsAccess implements ChatComponent.ChatGraphicsAccess {
/*     */   private final GuiGraphicsExtractor graphics;
/*     */   
/*     */   private final ActiveTextCollector textRenderer;
/*     */   
/*     */   private ActiveTextCollector.Parameters parameters;
/*     */   
/*     */   public DrawingBackgroundGraphicsAccess(GuiGraphicsExtractor graphics) {
/* 616 */     this.graphics = graphics;
/* 617 */     this.textRenderer = graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.NONE, null);
/* 618 */     this.parameters = this.textRenderer.defaultParameters();
/*     */   }
/*     */   
/*     */   public void updatePose(Consumer<Matrix3x2f> updater) {
/* 623 */     updater.accept(this.graphics.pose());
/* 624 */     this.parameters = this.parameters.withPose((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.graphics.pose()));
/*     */   }
/*     */   
/*     */   public void fill(int x0, int y0, int x1, int y1, int color) {
/* 629 */     this.graphics.fill(x0, y0, x1, y1, color);
/*     */   }
/*     */   
/*     */   public boolean handleMessage(int textTop, float opacity, FormattedCharSequence message) {
/* 634 */     this.textRenderer.accept(TextAlignment.LEFT, 0, textTop, this.parameters.withOpacity(opacity), message);
/* 635 */     return false;
/*     */   }
/*     */   
/*     */   public void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag) {
/* 640 */     int indicatorColor = ARGB.color(opacity, tag.indicatorColor());
/* 641 */     this.graphics.fill(x0, y0, x1, y1, indicatorColor);
/*     */   }
/*     */   
/*     */   public void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag.Icon icon) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$DrawingBackgroundGraphicsAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */