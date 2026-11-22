package net.minecraft.server.notifications;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.level.gamerules.GameRule;

public class EmptyNotificationService implements NotificationService {
  public void playerJoined(ServerPlayer player) {}
  
  public void playerLeft(ServerPlayer player) {}
  
  public void serverStarted() {}
  
  public void serverShuttingDown() {}
  
  public void serverSaveStarted() {}
  
  public void serverSaveCompleted() {}
  
  public void serverActivityOccured() {}
  
  public void playerOped(ServerOpListEntry operator) {}
  
  public void playerDeoped(ServerOpListEntry operator) {}
  
  public void playerAddedToAllowlist(NameAndId player) {}
  
  public void playerRemovedFromAllowlist(NameAndId player) {}
  
  public void ipBanned(IpBanListEntry ban) {}
  
  public void ipUnbanned(String ip) {}
  
  public void playerBanned(UserBanListEntry ban) {}
  
  public void playerUnbanned(NameAndId player) {}
  
  public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {}
  
  public void statusHeartbeat() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\notifications\EmptyNotificationService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */