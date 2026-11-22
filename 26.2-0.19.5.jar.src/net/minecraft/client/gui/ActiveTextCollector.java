/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Vector2f;
/*     */ 
/*     */ public interface ActiveTextCollector {
/*     */   public static final double PERIOD_PER_SCROLLED_PIXEL = 0.5D;
/*     */   
/*     */   public static final double MIN_SCROLL_PERIOD = 3.0D;
/*     */   
/*     */   Parameters defaultParameters();
/*     */   
/*     */   void defaultParameters(Parameters paramParameters);
/*     */   
/*     */   default void accept(int x, int y, FormattedCharSequence text) {
/*  32 */     accept(TextAlignment.LEFT, x, y, defaultParameters(), text);
/*     */   }
/*     */   
/*     */   default void accept(int x, int y, Component text) {
/*  36 */     accept(TextAlignment.LEFT, x, y, defaultParameters(), text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, Parameters parameters, Component text) {
/*  40 */     accept(alignment, anchorX, y, parameters, text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   void accept(TextAlignment paramTextAlignment, int paramInt1, int paramInt2, Parameters paramParameters, FormattedCharSequence paramFormattedCharSequence);
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, Component text) {
/*  46 */     accept(alignment, anchorX, y, text.getVisualOrderText());
/*     */   }
/*     */   
/*     */   default void accept(TextAlignment alignment, int anchorX, int y, FormattedCharSequence text) {
/*  50 */     accept(alignment, anchorX, y, defaultParameters(), text);
/*     */   }
/*     */   
/*     */   void acceptScrolling(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Parameters paramParameters);
/*     */   
/*     */   default void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom) {
/*  57 */     acceptScrolling(message, centerX, left, right, top, bottom, defaultParameters());
/*     */   }
/*     */   
/*     */   default void acceptScrollingWithDefaultCenter(Component message, int left, int right, int top, int bottom) {
/*  61 */     acceptScrolling(message, (left + right) / 2, left, right, top, bottom);
/*     */   }
/*     */   
/*     */   default void defaultScrollingHelper(Component message, int centerX, int left, int right, int top, int bottom, int lineWidth, int lineHeight, Parameters parameters) {
/*  65 */     int textTop = (top + bottom - lineHeight) / 2 + 1;
/*  66 */     int availableMessageWidth = right - left;
/*  67 */     if (lineWidth > availableMessageWidth) {
/*  68 */       int maxPosition = lineWidth - availableMessageWidth;
/*  69 */       double time = Util.getMillis() / 1000.0D;
/*  70 */       double period = Math.max(maxPosition * 0.5D, 3.0D);
/*  71 */       double alpha = Math.sin(1.5707963267948966D * Math.cos(6.283185307179586D * time / period)) / 2.0D + 0.5D;
/*  72 */       double pos = Mth.lerp(alpha, 0.0D, maxPosition);
/*  73 */       Parameters localParameters = parameters.withScissor(left, right, top, bottom);
/*  74 */       accept(TextAlignment.LEFT, left - (int)pos, textTop, localParameters, message.getVisualOrderText());
/*     */     } else {
/*  76 */       int textX = Mth.clamp(centerX, left + lineWidth / 2, right - lineWidth / 2);
/*  77 */       accept(TextAlignment.CENTER, textX, textTop, message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Parameters extends Record {
/*     */     private final Matrix3x2fc pose;
/*     */     
/*     */     private final float opacity;
/*     */     
/*     */     private final ScreenRectangle scissor;
/*     */     
/*     */     public Parameters(Matrix3x2fc pose, float opacity, ScreenRectangle scissor) {
/*  83 */       this.pose = pose;
/*  83 */       this.opacity = opacity;
/*  83 */       this.scissor = scissor;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/ActiveTextCollector$Parameters;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Matrix3x2fc pose() {
/*  83 */       return this.pose;
/*     */     }
/*     */     
/*     */     public float opacity() {
/*  83 */       return this.opacity;
/*     */     }
/*     */     
/*     */     public ScreenRectangle scissor() {
/*  83 */       return this.scissor;
/*     */     }
/*     */     
/*     */     public Parameters(Matrix3x2fc pose) {
/*  89 */       this(pose, 1.0F, null);
/*     */     }
/*     */     
/*     */     public Parameters withPose(Matrix3x2fc pose) {
/*  93 */       return new Parameters(pose, this.opacity, this.scissor);
/*     */     }
/*     */     
/*     */     public Parameters withScale(float scale) {
/*  98 */       return withPose((Matrix3x2fc)this.pose.scale(scale, scale, new Matrix3x2f()));
/*     */     }
/*     */     
/*     */     public Parameters withOpacity(float opacity) {
/* 102 */       if (this.opacity == opacity)
/* 103 */         return this; 
/* 105 */       return new Parameters(this.pose, opacity, this.scissor);
/*     */     }
/*     */     
/*     */     public Parameters withScissor(ScreenRectangle scissor) {
/* 109 */       if (scissor.equals(this.scissor))
/* 110 */         return this; 
/* 112 */       return new Parameters(this.pose, this.opacity, scissor);
/*     */     }
/*     */     
/*     */     public Parameters withScissor(int left, int right, int top, int bottom) {
/* 116 */       ScreenRectangle newScissor = (new ScreenRectangle(left, top, right - left, bottom - top)).transformAxisAligned(this.pose);
/* 117 */       if (this.scissor != null)
/* 118 */         newScissor = Objects.<ScreenRectangle>requireNonNullElse(this.scissor.intersection(newScissor), ScreenRectangle.empty()); 
/* 120 */       return withScissor(newScissor);
/*     */     }
/*     */   }
/*     */   
/*     */   static void findElementUnderCursor(GuiTextRenderState text, float testX, float testY, final Consumer<Style> output) {
/* 127 */     ScreenRectangle bounds = text.bounds();
/* 128 */     if (bounds == null || !bounds.containsPoint((int)testX, (int)testY))
/*     */       return; 
/* 132 */     Vector2f vector2f = text.pose.invert(new Matrix3x2f()).transformPosition(new Vector2f(testX, testY));
/* 133 */     final float localMouseX = vector2f.x();
/* 134 */     final float localMouseY = vector2f.y();
/* 135 */     text.ensurePrepared().visit(new Font.GlyphVisitor() {
/*     */           public void acceptGlyph(TextRenderable.Styled glyph) {
/* 138 */             acceptActiveArea((ActiveArea)glyph);
/*     */           }
/*     */           
/*     */           public void acceptEmptyArea(EmptyArea empty) {
/* 143 */             acceptActiveArea((ActiveArea)empty);
/*     */           }
/*     */           
/*     */           private void acceptActiveArea(ActiveArea glyph) {
/* 147 */             if (ActiveTextCollector.isPointInRectangle(localMouseX, localMouseY, glyph.activeLeft(), glyph.activeTop(), glyph.activeRight(), glyph.activeBottom()))
/* 148 */               output.accept(glyph.style()); 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   static boolean isPointInRectangle(float x, float y, float left, float top, float right, float bottom) {
/* 155 */     return (x >= left && x < right && y >= top && y < bottom);
/*     */   }
/*     */   
/*     */   public static class ClickableStyleFinder implements ActiveTextCollector {
/* 159 */     private static final ActiveTextCollector.Parameters INITIAL = new ActiveTextCollector.Parameters((Matrix3x2fc)new Matrix3x2f());
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     private final int testX;
/*     */     
/*     */     private final int testY;
/*     */     
/* 165 */     private ActiveTextCollector.Parameters defaultParameters = INITIAL;
/*     */     
/*     */     private boolean includeInsertions;
/*     */     
/*     */     private Style result;
/*     */     
/*     */     private final Consumer<Style> styleScanner;
/*     */     
/*     */     public ClickableStyleFinder(Font font, int testX, int testY) {
/* 171 */       this.styleScanner = (style -> {
/*     */           if (style.getClickEvent() != null || (this.includeInsertions && style.getInsertion() != null))
/*     */             this.result = style; 
/*     */         });
/* 178 */       this.font = font;
/* 179 */       this.testX = testX;
/* 180 */       this.testY = testY;
/*     */     }
/*     */     
/*     */     public ActiveTextCollector.Parameters defaultParameters() {
/* 185 */       return this.defaultParameters;
/*     */     }
/*     */     
/*     */     public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 190 */       this.defaultParameters = newParameters;
/*     */     }
/*     */     
/*     */     public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 195 */       int leftX = alignment.calculateLeft(anchorX, this.font, text);
/* 196 */       GuiTextRenderState renderState = new GuiTextRenderState(this.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, true, parameters.scissor());
/* 197 */       ActiveTextCollector.findElementUnderCursor(renderState, this.testX, this.testY, this.styleScanner);
/*     */     }
/*     */     
/*     */     public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 202 */       int lineWidth = this.font.width((FormattedText)message);
/* 203 */       Objects.requireNonNull(this.font);
/* 203 */       int lineHeight = 9;
/* 204 */       defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */     }
/*     */     
/*     */     public ClickableStyleFinder includeInsertions(boolean flag) {
/* 208 */       this.includeInsertions = flag;
/* 209 */       return this;
/*     */     }
/*     */     
/*     */     public Style result() {
/* 213 */       return this.result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ActiveTextCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */