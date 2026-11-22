/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntityAzimuthConnection implements WaypointTransmitter.Connection {
/*     */   private final LivingEntity source;
/*     */   
/*     */   private final Waypoint.Icon icon;
/*     */   
/*     */   private final ServerPlayer receiver;
/*     */   
/*     */   private float lastAngle;
/*     */   
/*     */   public EntityAzimuthConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 171 */     this.source = source;
/* 172 */     this.icon = icon;
/* 173 */     this.receiver = receiver;
/* 175 */     Vec3 direction = receiver.position().subtract(source.position()).rotateClockwise90();
/* 176 */     this.lastAngle = (float)Mth.atan2(direction.z(), direction.x());
/*     */   }
/*     */   
/*     */   public boolean isBroken() {
/* 181 */     return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver));
/*     */   }
/*     */   
/*     */   public void connect() {
/* 186 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
/*     */   }
/*     */   
/*     */   public void disconnect() {
/* 191 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */   
/*     */   public void update() {
/* 196 */     Vec3 direction = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 197 */     float currentAngle = (float)Mth.atan2(direction.z(), direction.x());
/* 198 */     if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
/* 199 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle));
/* 200 */       this.lastAngle = currentAngle;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityAzimuthConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */