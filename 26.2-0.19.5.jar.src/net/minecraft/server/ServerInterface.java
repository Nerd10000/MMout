package net.minecraft.server;

import net.minecraft.server.dedicated.DedicatedServerProperties;

public interface ServerInterface extends ServerInfo {
  DedicatedServerProperties getProperties();
  
  String getServerIp();
  
  int getServerPort();
  
  String getServerName();
  
  String[] getPlayerNames();
  
  String getLevelIdName();
  
  String getPluginNames();
  
  String runCommand(String paramString);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ServerInterface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */