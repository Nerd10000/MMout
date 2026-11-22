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
/*     */ 
/*     */ class RenderingTextCollector implements ActiveTextCollector, Consumer<Style> {
/*     */   private ActiveTextCollector.Parameters defaultParameters;
/*     */   
/*     */   private final GuiGraphicsExtractor.HoveredTextEffects hoveredTextEffects;
/*     */   
/*     */   private final Consumer<Style> additionalConsumer;
/*     */   
/*     */   private RenderingTextCollector(ActiveTextCollector.Parameters initialParameters, GuiGraphicsExtractor.HoveredTextEffects hoveredTextEffects, Consumer<Style> additonalConsumer) {
/* 884 */     this.defaultParameters = initialParameters;
/* 885 */     this.hoveredTextEffects = hoveredTextEffects;
/* 886 */     this.additionalConsumer = additonalConsumer;
/*     */   }
/*     */   
/*     */   public ActiveTextCollector.Parameters defaultParameters() {
/* 891 */     return this.defaultParameters;
/*     */   }
/*     */   
/*     */   public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 896 */     this.defaultParameters = newParameters;
/*     */   }
/*     */   
/*     */   public void accept(Style style) {
/* 901 */     if (this.hoveredTextEffects.allowTooltip && style.getHoverEvent() != null)
/* 902 */       GuiGraphicsExtractor.this.hoveredTextStyle = style; 
/* 905 */     if (this.hoveredTextEffects.allowCursorChanges && style.getClickEvent() != null)
/* 906 */       GuiGraphicsExtractor.this.clickableTextStyle = style; 
/* 909 */     if (this.additionalConsumer != null)
/* 910 */       this.additionalConsumer.accept(style); 
/*     */   }
/*     */   
/*     */   public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 916 */     boolean needsFullStyleScan = (this.hoveredTextEffects.allowCursorChanges || this.hoveredTextEffects.allowTooltip || this.additionalConsumer != null);
/* 917 */     int leftX = alignment.calculateLeft(anchorX, GuiGraphicsExtractor.this.minecraft.font, text);
/* 918 */     GuiTextRenderState renderState = new GuiTextRenderState(GuiGraphicsExtractor.this.minecraft.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, needsFullStyleScan, parameters.scissor());
/* 920 */     if (ARGB.as8BitChannel(parameters.opacity()) != 0)
/* 921 */       GuiGraphicsExtractor.this.guiRenderState.addText(renderState); 
/* 924 */     if (needsFullStyleScan)
/* 925 */       ActiveTextCollector.findElementUnderCursor(renderState, GuiGraphicsExtractor.this.mouseX, GuiGraphicsExtractor.this.mouseY, this); 
/*     */   }
/*     */   
/*     */   public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 931 */     int lineWidth = GuiGraphicsExtractor.this.minecraft.font.width((FormattedText)message);
/* 932 */     Objects.requireNonNull(GuiGraphicsExtractor.this.minecraft.font);
/* 932 */     int lineHeight = 9;
/* 933 */     defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\GuiGraphicsExtractor$RenderingTextCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */