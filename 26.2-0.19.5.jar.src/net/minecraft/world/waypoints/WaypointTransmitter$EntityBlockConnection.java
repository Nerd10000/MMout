/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class EntityBlockConnection implements WaypointTransmitter.BlockConnection {
/*     */   private final LivingEntity source;
/*     */   
/*     */   private final Waypoint.Icon icon;
/*     */   
/*     */   private final ServerPlayer receiver;
/*     */   
/*     */   private BlockPos lastPosition;
/*     */   
/*     */   public EntityBlockConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/*  71 */     this.source = source;
/*  72 */     this.receiver = receiver;
/*  73 */     this.icon = icon;
/*  74 */     this.lastPosition = source.blockPosition();
/*     */   }
/*     */   
/*     */   public void connect() {
/*  79 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)this.lastPosition));
/*     */   }
/*     */   
/*     */   public void disconnect() {
/*  84 */     this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */   }
/*     */   
/*     */   public void update() {
/*  89 */     BlockPos currentPosition = this.source.blockPosition();
/*  90 */     if (currentPosition.distManhattan((Vec3i)this.lastPosition) > 0) {
/*  91 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)currentPosition));
/*  92 */       this.lastPosition = currentPosition;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int distanceManhattan() {
/*  98 */     return this.lastPosition.distManhattan((Vec3i)this.source.blockPosition());
/*     */   }
/*     */   
/*     */   public boolean isBroken() {
/* 103 */     return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\WaypointTransmitter$EntityBlockConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */