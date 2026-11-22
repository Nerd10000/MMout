package com.mojang.blaze3d.font;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;

public interface Stitcher {
  BakedGlyph stitch(GlyphInfo paramGlyphInfo, GlyphBitmap paramGlyphBitmap);
  
  BakedGlyph getMissing();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\UnbakedGlyph$Stitcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */