package net.minecraft.client.gui.components.toasts;

import java.util.UUID;
import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface SkinToastEmitter {
  void emit(Minecraft paramMinecraft, String paramString, UUID paramUUID);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\FriendToast$SkinToastEmitter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */