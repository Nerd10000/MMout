/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphBitmap;
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ 
/*    */ class null implements UnbakedGlyph.Stitcher {
/*    */   public BakedGlyph stitch(GlyphInfo glyphInfo, GlyphBitmap glyphBitmap) {
/* 51 */     return (BakedGlyph)Objects.requireNonNullElse(FontSet.this.stitcher.stitch(glyphInfo, glyphBitmap), FontSet.this.missingGlyph);
/*    */   }
/*    */   
/*    */   public BakedGlyph getMissing() {
/* 56 */     return FontSet.this.missingGlyph;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontSet$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */