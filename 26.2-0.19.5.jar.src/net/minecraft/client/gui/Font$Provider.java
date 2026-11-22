package net.minecraft.client.gui;

import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;

public interface Provider {
  GlyphSource glyphs(FontDescription paramFontDescription);
  
  EffectGlyph effect();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Font$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */