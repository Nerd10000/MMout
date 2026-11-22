package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteListEntry;

public interface MinecraftAllowListService {
  Collection<UserWhiteListEntry> getEntries();
  
  boolean add(UserWhiteListEntry paramUserWhiteListEntry, ClientInfo paramClientInfo);
  
  void clear(ClientInfo paramClientInfo);
  
  void remove(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void kickUnlistedPlayers(ClientInfo paramClientInfo);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftAllowListService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */