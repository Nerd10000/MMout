/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.server.network.ServerPlayerConnection;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ class TrackedEntity implements ServerEntity.Synchronizer {
/*      */   private final ServerEntity serverEntity;
/*      */   
/*      */   private final Entity entity;
/*      */   
/*      */   private final int range;
/*      */   
/*      */   private SectionPos lastSectionPos;
/*      */   
/* 1371 */   private final Set<ServerPlayerConnection> seenBy = Sets.newIdentityHashSet();
/*      */   
/*      */   public TrackedEntity(Entity entity, int range, int updateInterval, boolean trackDelta) {
/* 1374 */     this.serverEntity = new ServerEntity(paramChunkMap.level, entity, updateInterval, trackDelta, this);
/* 1375 */     this.entity = entity;
/* 1376 */     this.range = range;
/* 1377 */     this.lastSectionPos = SectionPos.of((EntityAccess)entity);
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1382 */     if (obj instanceof TrackedEntity) {
/* 1382 */       TrackedEntity trackedEntity = (TrackedEntity)obj;
/* 1383 */       return (trackedEntity.entity.getId() == this.entity.getId());
/*      */     } 
/* 1386 */     return false;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/* 1391 */     return this.entity.getId();
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> packet) {
/* 1396 */     for (ServerPlayerConnection connection : this.seenBy)
/* 1397 */       connection.send(packet); 
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> packet) {
/* 1403 */     sendToTrackingPlayers(packet);
/* 1404 */     Entity entity = this.entity;
/* 1404 */     if (entity instanceof ServerPlayer) {
/* 1404 */       ServerPlayer player = (ServerPlayer)entity;
/* 1405 */       player.connection.send(packet);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
/* 1411 */     for (ServerPlayerConnection connection : this.seenBy) {
/* 1412 */       if (targetPredicate.test(connection.getPlayer()))
/* 1413 */         connection.send(packet); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void broadcastRemoved() {
/* 1419 */     for (ServerPlayerConnection connection : this.seenBy)
/* 1420 */       this.serverEntity.removePairing(connection.getPlayer()); 
/*      */   }
/*      */   
/*      */   public void removePlayer(ServerPlayer player) {
/* 1425 */     if (this.seenBy.remove(player.connection)) {
/* 1426 */       this.serverEntity.removePairing(player);
/* 1427 */       if (this.seenBy.isEmpty())
/* 1428 */         ChunkMap.this.level.debugSynchronizers().dropEntity(this.entity); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updatePlayer(ServerPlayer player) {
/* 1434 */     if (player == this.entity)
/*      */       return; 
/* 1438 */     Vec3 deltaToPlayer = player.position().subtract(this.entity.position());
/* 1439 */     int playerViewDistance = ChunkMap.this.getPlayerViewDistance(player);
/* 1440 */     double visibleRange = Math.min(getEffectiveRange(), playerViewDistance * 16);
/* 1441 */     double distanceSquared = deltaToPlayer.x * deltaToPlayer.x + deltaToPlayer.z * deltaToPlayer.z;
/* 1442 */     double rangeSquared = visibleRange * visibleRange;
/* 1444 */     boolean visibleToPlayer = (distanceSquared <= rangeSquared && this.entity.broadcastToPlayer(player) && ChunkMap.this.isChunkTracked(player, this.entity.chunkPosition().x(), this.entity.chunkPosition().z()));
/* 1446 */     if (visibleToPlayer) {
/* 1447 */       if (this.seenBy.add(player.connection)) {
/* 1448 */         this.serverEntity.addPairing(player);
/* 1449 */         if (this.seenBy.size() == 1)
/* 1450 */           ChunkMap.this.level.debugSynchronizers().registerEntity(this.entity); 
/* 1452 */         ChunkMap.this.level.debugSynchronizers().startTrackingEntity(player, this.entity);
/*      */       } 
/*      */     } else {
/* 1455 */       removePlayer(player);
/*      */     } 
/*      */   }
/*      */   
/*      */   private int scaledRange(int range) {
/* 1460 */     return ChunkMap.this.level.getServer().getScaledTrackingDistance(range);
/*      */   }
/*      */   
/*      */   private int getEffectiveRange() {
/* 1464 */     int effectiveRange = this.range;
/* 1465 */     for (Entity passenger : this.entity.getIndirectPassengers()) {
/* 1466 */       int passengerRange = passenger.getType().clientTrackingRange() * 16;
/* 1467 */       if (passengerRange > effectiveRange)
/* 1468 */         effectiveRange = passengerRange; 
/*      */     } 
/* 1471 */     return scaledRange(effectiveRange);
/*      */   }
/*      */   
/*      */   public void updatePlayers(List<ServerPlayer> players) {
/* 1475 */     for (ServerPlayer player : players)
/* 1476 */       updatePlayer(player); 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkMap$TrackedEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */