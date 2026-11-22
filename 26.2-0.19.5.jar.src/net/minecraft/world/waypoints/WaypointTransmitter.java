/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public interface WaypointTransmitter extends Waypoint {
/*     */   public static final int REALLY_FAR_DISTANCE = 332;
/*     */   
/*     */   boolean isTransmittingWaypoint();
/*     */   
/*     */   Optional<Connection> makeWaypointConnectionWith(ServerPlayer paramServerPlayer);
/*     */   
/*     */   Waypoint.Icon waypointIcon();
/*     */   
/*     */   static boolean doesSourceIgnoreReceiver(LivingEntity source, ServerPlayer receiver) {
/*  34 */     if (receiver.isSpectator())
/*  35 */       return false; 
/*  37 */     if (source.isSpectator() || source.hasIndirectPassenger((Entity)receiver))
/*  38 */       return true; 
/*  40 */     double broadcastRange = Math.min(source
/*  41 */         .getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE), receiver
/*  42 */         .getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE));
/*  44 */     return (source.distanceTo((Entity)receiver) >= broadcastRange);
/*     */   }
/*     */   
/*     */   static boolean isChunkVisible(ChunkPos chunkPos, ServerPlayer receiver) {
/*  48 */     return receiver.getChunkTrackingView().isInViewDistance(chunkPos.x(), chunkPos.z());
/*     */   }
/*     */   
/*     */   static boolean isReallyFar(LivingEntity source, ServerPlayer receiver) {
/*  52 */     return (source.distanceTo((Entity)receiver) > 332.0F);
/*     */   }
/*     */   
/*     */   public static interface Connection {
/*     */     void connect();
/*     */     
/*     */     void disconnect();
/*     */     
/*     */     void update();
/*     */     
/*     */     boolean isBroken();
/*     */   }
/*     */   
/*     */   public static interface BlockConnection extends Connection {
/*     */     int distanceManhattan();
/*     */     
/*     */     default boolean isBroken() {
/*  60 */       return (distanceManhattan() > 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityBlockConnection implements BlockConnection {
/*     */     private final LivingEntity source;
/*     */     
/*     */     private final Waypoint.Icon icon;
/*     */     
/*     */     private final ServerPlayer receiver;
/*     */     
/*     */     private BlockPos lastPosition;
/*     */     
/*     */     public EntityBlockConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/*  71 */       this.source = source;
/*  72 */       this.receiver = receiver;
/*  73 */       this.icon = icon;
/*  74 */       this.lastPosition = source.blockPosition();
/*     */     }
/*     */     
/*     */     public void connect() {
/*  79 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)this.lastPosition));
/*     */     }
/*     */     
/*     */     public void disconnect() {
/*  84 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */     
/*     */     public void update() {
/*  89 */       BlockPos currentPosition = this.source.blockPosition();
/*  90 */       if (currentPosition.distManhattan((Vec3i)this.lastPosition) > 0) {
/*  91 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)currentPosition));
/*  92 */         this.lastPosition = currentPosition;
/*     */       } 
/*     */     }
/*     */     
/*     */     public int distanceManhattan() {
/*  98 */       return this.lastPosition.distManhattan((Vec3i)this.source.blockPosition());
/*     */     }
/*     */     
/*     */     public boolean isBroken() {
/* 103 */       return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ChunkConnection extends Connection {
/*     */     int distanceChessboard();
/*     */     
/*     */     default boolean isBroken() {
/* 112 */       return (distanceChessboard() > 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityChunkConnection implements ChunkConnection {
/*     */     private final LivingEntity source;
/*     */     
/*     */     private final Waypoint.Icon icon;
/*     */     
/*     */     private final ServerPlayer receiver;
/*     */     
/*     */     private ChunkPos lastPosition;
/*     */     
/*     */     public EntityChunkConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 123 */       this.source = source;
/* 124 */       this.icon = icon;
/* 125 */       this.receiver = receiver;
/* 126 */       this.lastPosition = source.chunkPosition();
/*     */     }
/*     */     
/*     */     public int distanceChessboard() {
/* 131 */       return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
/*     */     }
/*     */     
/*     */     public void connect() {
/* 136 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
/*     */     }
/*     */     
/*     */     public void disconnect() {
/* 141 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */     
/*     */     public void update() {
/* 146 */       ChunkPos currentPosition = this.source.chunkPosition();
/* 147 */       if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
/* 148 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition));
/* 149 */         this.lastPosition = currentPosition;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isBroken() {
/* 155 */       if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver))
/* 156 */         return true; 
/* 160 */       return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityAzimuthConnection implements Connection {
/*     */     private final LivingEntity source;
/*     */     
/*     */     private final Waypoint.Icon icon;
/*     */     
/*     */     private final ServerPlayer receiver;
/*     */     
/*     */     private float lastAngle;
/*     */     
/*     */     public EntityAzimuthConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 171 */       this.source = source;
/* 172 */       this.icon = icon;
/* 173 */       this.receiver = receiver;
/* 175 */       Vec3 direction = receiver.position().subtract(source.position()).rotateClockwise90();
/* 176 */       this.lastAngle = (float)Mth.atan2(direction.z(), direction.x());
/*     */     }
/*     */     
/*     */     public boolean isBroken() {
/* 181 */       return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver));
/*     */     }
/*     */     
/*     */     public void connect() {
/* 186 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
/*     */     }
/*     */     
/*     */     public void disconnect() {
/* 191 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */     
/*     */     public void update() {
/* 196 */       Vec3 direction = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 197 */       float currentAngle = (float)Mth.atan2(direction.z(), direction.x());
/* 198 */       if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
/* 199 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle));
/* 200 */         this.lastAngle = currentAngle;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */