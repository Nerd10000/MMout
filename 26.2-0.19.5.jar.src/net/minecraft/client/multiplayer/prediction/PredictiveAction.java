package net.minecraft.client.multiplayer.prediction;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

@FunctionalInterface
public interface PredictiveAction {
  Packet<ServerGamePacketListener> predict(int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\prediction\PredictiveAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */