package net.minecraft.world.waypoints;

public interface Connection {
  void connect();
  
  void disconnect();
  
  void update();
  
  boolean isBroken();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter$Connection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */