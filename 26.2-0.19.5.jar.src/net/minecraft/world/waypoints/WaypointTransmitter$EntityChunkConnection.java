/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class EntityChunkConnection implements WaypointTransmitter.ChunkConnection {
/*     */   private final LivingEntity source;
/*     */   
/*     */   private final Waypoint.Icon icon;
/*     */   
/*     */   private final ServerPlayer receiver;
/*     */   
/*     */   private ChunkPos lastPosition;
/*     */   
/*     */   public EntityChunkConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 123 */     this.source = source;
/* 124 */     this.icon = icon;
/* 125 */     this.receiver = receiver;
/* 126 */     this.lastPosition = source.chunkPosition();
/*     */   }
/*     */   
/*     */   public int distanceChessboard() {
/* 131 */     return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
/*     */   }
/*     */   
/*     */   public void connect() {
/* 136 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
/*     */   }
/*     */   
/*     */   public void disconnect() {
/* 141 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */   
/*     */   public void update() {
/* 146 */     ChunkPos currentPosition = this.source.chunkPosition();
/* 147 */     if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
/* 148 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition));
/* 149 */       this.lastPosition = currentPosition;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBroken() {
/* 155 */     if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver))
/* 156 */       return true; 
/* 160 */     return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityChunkConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */