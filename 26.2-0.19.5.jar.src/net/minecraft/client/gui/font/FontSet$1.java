/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ class null implements BakedGlyph {
/*    */   public GlyphInfo info() {
/* 37 */     return (GlyphInfo)SpecialGlyphs.MISSING;
/*    */   }
/*    */   
/*    */   public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/* 42 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontSet$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */