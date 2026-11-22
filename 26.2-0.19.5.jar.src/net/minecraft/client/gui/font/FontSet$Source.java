/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class Source implements GlyphSource {
/*     */   private final boolean filterFishyGlyphs;
/*     */   
/*     */   public Source(boolean filterFishyGlyphs) {
/* 225 */     this.filterFishyGlyphs = filterFishyGlyphs;
/*     */   }
/*     */   
/*     */   public BakedGlyph getGlyph(int codepoint) {
/* 230 */     return FontSet.this.getGlyph(codepoint).select(this.filterFishyGlyphs).get();
/*     */   }
/*     */   
/*     */   public BakedGlyph getRandomGlyph(RandomSource random, int width) {
/* 235 */     return FontSet.this.getRandomGlyph(random, width);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontSet$Source.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */