package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;

public interface Handler {
  default void add(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
  
  default void remove(UUID id) {}
  
  default void updateProgress(UUID id, float progress) {}
  
  default void updateName(UUID id, Component name) {}
  
  default void updateStyle(UUID id, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {}
  
  default void updateProperties(UUID id, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Handler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */