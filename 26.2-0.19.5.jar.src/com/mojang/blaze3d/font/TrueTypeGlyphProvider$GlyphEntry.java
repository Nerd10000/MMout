/*     */ package com.mojang.blaze3d.font;
/*     */ 
/*     */ class GlyphEntry {
/*     */   private final int index;
/*     */   
/*     */   private volatile UnbakedGlyph glyph;
/*     */   
/*     */   private GlyphEntry(int index) {
/* 146 */     this.index = index;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\TrueTypeGlyphProvider$GlyphEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */