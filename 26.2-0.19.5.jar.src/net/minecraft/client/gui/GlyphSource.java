package net.minecraft.client.gui;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.RandomSource;

public interface GlyphSource {
  BakedGlyph getGlyph(int paramInt);
  
  BakedGlyph getRandomGlyph(RandomSource paramRandomSource, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\GlyphSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */