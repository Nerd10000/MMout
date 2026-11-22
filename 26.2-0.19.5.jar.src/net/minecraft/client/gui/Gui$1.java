/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ class null implements Font.GlyphVisitor {
/*     */   private int index;
/*     */   
/*     */   public void acceptGlyph(TextRenderable.Styled glyph) {
/* 437 */     renderDebugMarkers((ActiveArea)glyph, false);
/*     */   }
/*     */   
/*     */   public void acceptEmptyArea(EmptyArea empty) {
/* 442 */     renderDebugMarkers((ActiveArea)empty, true);
/*     */   }
/*     */   
/*     */   private void renderDebugMarkers(ActiveArea glyph, boolean isEmpty) {
/* 446 */     int intensity = (isEmpty ? 128 : 255) - (this.index++ & 0x1) * 64;
/* 448 */     Style style = glyph.style();
/* 449 */     int red = (style.getClickEvent() != null) ? intensity : 0;
/* 450 */     int green = (style.getHoverEvent() != null) ? intensity : 0;
/* 451 */     int blue = (red == 0 || green == 0) ? intensity : 0;
/* 452 */     int color = ARGB.color(128, red, green, blue);
/* 454 */     Gui.this.guiRenderState.addGuiElement((GuiElementRenderState)new ColoredRectangleRenderState(RenderPipelines.GUI, 
/*     */           
/* 456 */           TextureSetup.noTexture(), text.pose, 
/*     */           
/* 458 */           (int)glyph.activeLeft(), 
/* 459 */           (int)glyph.activeTop(), 
/* 460 */           (int)glyph.activeRight(), 
/* 461 */           (int)glyph.activeBottom(), color, color, text.scissor));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Gui$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */