package net.minecraft.world.waypoints;

public interface WaypointManager<T extends Waypoint> {
  void trackWaypoint(T paramT);
  
  void updateWaypoint(T paramT);
  
  void untrackWaypoint(T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */