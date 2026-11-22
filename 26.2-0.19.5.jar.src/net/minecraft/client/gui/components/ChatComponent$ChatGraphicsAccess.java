package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;

public interface ChatGraphicsAccess {
  void updatePose(Consumer<Matrix3x2f> paramConsumer);
  
  void fill(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  boolean handleMessage(int paramInt, float paramFloat, FormattedCharSequence paramFormattedCharSequence);
  
  void handleTag(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat, GuiMessageTag paramGuiMessageTag);
  
  void handleTagIcon(int paramInt1, int paramInt2, boolean paramBoolean, GuiMessageTag paramGuiMessageTag, GuiMessageTag.Icon paramIcon);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$ChatGraphicsAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */