package net.minecraft.network.protocol.login.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

public interface CustomQueryPayload {
  Identifier id();
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\custom\CustomQueryPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */