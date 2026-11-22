/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ public class ClickableStyleFinder implements ActiveTextCollector {
/* 159 */   private static final ActiveTextCollector.Parameters INITIAL = new ActiveTextCollector.Parameters((Matrix3x2fc)new Matrix3x2f());
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final int testX;
/*     */   
/*     */   private final int testY;
/*     */   
/* 165 */   private ActiveTextCollector.Parameters defaultParameters = INITIAL;
/*     */   
/*     */   private boolean includeInsertions;
/*     */   
/*     */   private Style result;
/*     */   
/*     */   private final Consumer<Style> styleScanner;
/*     */   
/*     */   public ClickableStyleFinder(Font font, int testX, int testY) {
/* 171 */     this.styleScanner = (style -> {
/*     */         if (style.getClickEvent() != null || (this.includeInsertions && style.getInsertion() != null))
/*     */           this.result = style; 
/*     */       });
/* 178 */     this.font = font;
/* 179 */     this.testX = testX;
/* 180 */     this.testY = testY;
/*     */   }
/*     */   
/*     */   public ActiveTextCollector.Parameters defaultParameters() {
/* 185 */     return this.defaultParameters;
/*     */   }
/*     */   
/*     */   public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 190 */     this.defaultParameters = newParameters;
/*     */   }
/*     */   
/*     */   public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 195 */     int leftX = alignment.calculateLeft(anchorX, this.font, text);
/* 196 */     GuiTextRenderState renderState = new GuiTextRenderState(this.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, true, parameters.scissor());
/* 197 */     ActiveTextCollector.findElementUnderCursor(renderState, this.testX, this.testY, this.styleScanner);
/*     */   }
/*     */   
/*     */   public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 202 */     int lineWidth = this.font.width((FormattedText)message);
/* 203 */     Objects.requireNonNull(this.font);
/* 203 */     int lineHeight = 9;
/* 204 */     defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */   }
/*     */   
/*     */   public ClickableStyleFinder includeInsertions(boolean flag) {
/* 208 */     this.includeInsertions = flag;
/* 209 */     return this;
/*     */   }
/*     */   
/*     */   public Style result() {
/* 213 */     return this.result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ActiveTextCollector$ClickableStyleFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */