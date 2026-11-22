package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanListEntry;

public interface MinecraftBanListService {
  void addUserBan(UserBanListEntry paramUserBanListEntry, ClientInfo paramClientInfo);
  
  void removeUserBan(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  Collection<UserBanListEntry> getUserBanEntries();
  
  Collection<IpBanListEntry> getIpBanEntries();
  
  void addIpBan(IpBanListEntry paramIpBanListEntry, ClientInfo paramClientInfo);
  
  void clearIpBans(ClientInfo paramClientInfo);
  
  void removeIpBan(String paramString, ClientInfo paramClientInfo);
  
  void clearUserBans(ClientInfo paramClientInfo);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftBanListService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */