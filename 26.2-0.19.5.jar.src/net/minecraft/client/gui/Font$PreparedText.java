package net.minecraft.client.gui;

import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface PreparedText {
  void visit(Font.GlyphVisitor paramGlyphVisitor);
  
  ScreenRectangle bounds();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Font$PreparedText.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */