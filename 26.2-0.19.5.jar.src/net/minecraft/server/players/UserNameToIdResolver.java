package net.minecraft.server.players;

import java.util.Optional;
import java.util.UUID;

public interface UserNameToIdResolver {
  void add(NameAndId paramNameAndId);
  
  Optional<NameAndId> get(String paramString);
  
  Optional<NameAndId> get(UUID paramUUID);
  
  void resolveOfflineUsers(boolean paramBoolean);
  
  void save();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\UserNameToIdResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */