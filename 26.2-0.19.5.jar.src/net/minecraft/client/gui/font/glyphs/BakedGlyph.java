package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.network.chat.Style;

public interface BakedGlyph {
  GlyphInfo info();
  
  TextRenderable.Styled createGlyph(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, Style paramStyle, float paramFloat3, float paramFloat4);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\glyphs\BakedGlyph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */