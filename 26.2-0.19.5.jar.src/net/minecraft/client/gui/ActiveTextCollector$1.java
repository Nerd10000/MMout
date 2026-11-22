/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ 
/*     */ class null implements Font.GlyphVisitor {
/*     */   public void acceptGlyph(TextRenderable.Styled glyph) {
/* 138 */     acceptActiveArea((ActiveArea)glyph);
/*     */   }
/*     */   
/*     */   public void acceptEmptyArea(EmptyArea empty) {
/* 143 */     acceptActiveArea((ActiveArea)empty);
/*     */   }
/*     */   
/*     */   private void acceptActiveArea(ActiveArea glyph) {
/* 147 */     if (ActiveTextCollector.isPointInRectangle(localMouseX, localMouseY, glyph.activeLeft(), glyph.activeTop(), glyph.activeRight(), glyph.activeBottom()))
/* 148 */       output.accept(glyph.style()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ActiveTextCollector$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */