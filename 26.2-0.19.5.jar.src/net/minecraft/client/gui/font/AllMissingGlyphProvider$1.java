/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
/*    */ 
/*    */ class null implements UnbakedGlyph {
/*    */   public GlyphInfo info() {
/* 16 */     return (GlyphInfo)SpecialGlyphs.MISSING;
/*    */   }
/*    */   
/*    */   public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 21 */     return stitcher.getMissing();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\AllMissingGlyphProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */