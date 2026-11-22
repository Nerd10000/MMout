/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ 
/*     */ public interface GlyphVisitor {
/*     */   default void acceptGlyph(TextRenderable.Styled glyph) {
/* 377 */     acceptRenderable((TextRenderable)glyph);
/*     */   }
/*     */   
/*     */   default void acceptEffect(TextRenderable effect) {
/* 381 */     acceptRenderable(effect);
/*     */   }
/*     */   
/*     */   default void acceptRenderable(TextRenderable renderable) {}
/*     */   
/*     */   default void acceptEmptyArea(EmptyArea empty) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Font$GlyphVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */