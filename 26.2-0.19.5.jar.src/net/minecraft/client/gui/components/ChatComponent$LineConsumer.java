package net.minecraft.client.gui.components;

import net.minecraft.client.multiplayer.chat.GuiMessage;

@FunctionalInterface
interface LineConsumer {
  void accept(GuiMessage.Line paramLine, int paramInt, float paramFloat);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$LineConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */