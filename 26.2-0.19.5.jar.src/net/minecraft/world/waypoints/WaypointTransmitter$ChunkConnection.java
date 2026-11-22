/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ public interface ChunkConnection extends WaypointTransmitter.Connection {
/*     */   int distanceChessboard();
/*     */   
/*     */   default boolean isBroken() {
/* 112 */     return (distanceChessboard() > 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter$ChunkConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */