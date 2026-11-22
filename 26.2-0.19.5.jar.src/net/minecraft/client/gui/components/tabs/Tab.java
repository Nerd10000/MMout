package net.minecraft.client.gui.components.tabs;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

public interface Tab {
  Component getTabTitle();
  
  Component getTabExtraNarration();
  
  void visitChildren(Consumer<AbstractWidget> paramConsumer);
  
  void doLayout(ScreenRectangle paramScreenRectangle);
  
  Layout getLayout();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\Tab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */