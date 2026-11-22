package net.minecraft.nbt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public interface Styling {
  Style keyStyle();
  
  Style stringStyle();
  
  Style numberStyle();
  
  Component token(TextComponentTagVisitor.Token paramToken);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TextComponentTagVisitor$Styling.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */