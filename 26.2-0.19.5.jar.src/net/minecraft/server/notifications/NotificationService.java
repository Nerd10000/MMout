package net.minecraft.server.notifications;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.level.gamerules.GameRule;

public interface NotificationService {
  void playerJoined(ServerPlayer paramServerPlayer);
  
  void playerLeft(ServerPlayer paramServerPlayer);
  
  void serverStarted();
  
  void serverShuttingDown();
  
  void serverSaveStarted();
  
  void serverSaveCompleted();
  
  void serverActivityOccured();
  
  void playerOped(ServerOpListEntry paramServerOpListEntry);
  
  void playerDeoped(ServerOpListEntry paramServerOpListEntry);
  
  void playerAddedToAllowlist(NameAndId paramNameAndId);
  
  void playerRemovedFromAllowlist(NameAndId paramNameAndId);
  
  void ipBanned(IpBanListEntry paramIpBanListEntry);
  
  void ipUnbanned(String paramString);
  
  void playerBanned(UserBanListEntry paramUserBanListEntry);
  
  void playerUnbanned(NameAndId paramNameAndId);
  
  <T> void onGameRuleChanged(GameRule<T> paramGameRule, T paramT);
  
  void statusHeartbeat();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\notifications\NotificationService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */