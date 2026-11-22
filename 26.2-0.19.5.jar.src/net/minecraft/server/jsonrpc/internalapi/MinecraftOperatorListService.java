package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import java.util.Optional;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;

public interface MinecraftOperatorListService {
  Collection<ServerOpListEntry> getEntries();
  
  void op(NameAndId paramNameAndId, Optional<PermissionLevel> paramOptional, Optional<Boolean> paramOptional1, ClientInfo paramClientInfo);
  
  void op(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void deop(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void clear(ClientInfo paramClientInfo);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftOperatorListService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */