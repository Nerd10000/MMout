/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SourceSynchronizer<T> extends TrackingDebugSynchronizer<T> {
/* 118 */   private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap<>();
/*     */   
/* 119 */   private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap<>();
/*     */   
/* 120 */   private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap<>();
/*     */   
/*     */   public SourceSynchronizer(DebugSubscription<T> subscription) {
/* 123 */     super(subscription);
/*     */   }
/*     */   
/*     */   protected void clear() {
/* 128 */     this.chunkSources.clear();
/* 129 */     this.blockEntitySources.clear();
/* 130 */     this.entitySources.clear();
/*     */   }
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel level) {
/* 135 */     for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 136 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 137 */       if (update != null) {
/* 138 */         ChunkPos chunkPos = entry.getKey();
/* 139 */         sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */       } 
/*     */     } 
/* 143 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 144 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 145 */       if (update != null) {
/* 146 */         BlockPos blockPos = entry.getKey();
/* 147 */         ChunkPos chunkPos = ChunkPos.containing(blockPos);
/* 148 */         sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */       } 
/*     */     } 
/* 152 */     for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 153 */       DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 154 */       if (update != null) {
/* 155 */         Entity entity = Objects.<Entity>requireNonNull(level.getEntity(entry.getKey()));
/* 156 */         sendToPlayersTrackingEntity(level, entity, (Packet<? super ClientGamePacketListener>)new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerChunk(ChunkPos chunkPos, DebugValueSource.ValueGetter<T> getter) {
/* 162 */     this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */   }
/*     */   
/*     */   public void registerBlockEntity(BlockPos blockPos, DebugValueSource.ValueGetter<T> getter) {
/* 166 */     this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */   }
/*     */   
/*     */   public void registerEntity(UUID entityId, DebugValueSource.ValueGetter<T> getter) {
/* 170 */     this.entitySources.put(entityId, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */   }
/*     */   
/*     */   public void dropChunk(ChunkPos chunkPos) {
/* 174 */     this.chunkSources.remove(chunkPos);
/* 177 */     Objects.requireNonNull(chunkPos);
/* 177 */     this.blockEntitySources.keySet().removeIf(chunkPos::contains);
/*     */   }
/*     */   
/*     */   public void dropBlockEntity(ServerLevel level, BlockPos blockPos) {
/* 181 */     TrackingDebugSynchronizer.ValueSource<T> source = this.blockEntitySources.remove(blockPos);
/* 182 */     if (source != null) {
/* 183 */       ChunkPos chunkPos = ChunkPos.containing(blockPos);
/* 184 */       sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dropEntity(Entity entity) {
/* 189 */     this.entitySources.remove(entity.getUUID());
/*     */   }
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 194 */     TrackingDebugSynchronizer.ValueSource<T> chunkSource = this.chunkSources.get(chunkPos);
/* 195 */     if (chunkSource != null && chunkSource.lastSyncedValue != null)
/* 196 */       player.connection.send((Packet)new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue))); 
/* 199 */     for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 200 */       T lastValue = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 201 */       if (lastValue == null)
/*     */         continue; 
/* 204 */       BlockPos blockPos = entry.getKey();
/* 205 */       if (chunkPos.contains(blockPos))
/* 206 */         player.connection.send((Packet)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue))); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer player, Entity entity) {
/* 213 */     TrackingDebugSynchronizer.ValueSource<T> source = this.entitySources.get(entity.getUUID());
/* 214 */     if (source != null && source.lastSyncedValue != null)
/* 215 */       player.connection.send((Packet)new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue))); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$SourceSynchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */