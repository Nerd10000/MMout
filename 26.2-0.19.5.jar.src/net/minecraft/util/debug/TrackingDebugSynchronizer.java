/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public abstract class TrackingDebugSynchronizer<T> {
/*     */   protected final DebugSubscription<T> subscription;
/*     */   
/*  34 */   private final Set<UUID> subscribedPlayers = (Set<UUID>)new ObjectOpenHashSet();
/*     */   
/*     */   public TrackingDebugSynchronizer(DebugSubscription<T> subscription) {
/*  37 */     this.subscription = subscription;
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel level) {
/*  41 */     for (ServerPlayer player : level.players()) {
/*  42 */       boolean wasSubscribed = this.subscribedPlayers.contains(player.getUUID());
/*  43 */       boolean isSubscribed = player.debugSubscriptions().contains(this.subscription);
/*  44 */       if (isSubscribed == wasSubscribed)
/*     */         continue; 
/*  47 */       if (isSubscribed) {
/*  48 */         addSubscriber(player);
/*     */         continue;
/*     */       } 
/*  50 */       this.subscribedPlayers.remove(player.getUUID());
/*     */     } 
/*  53 */     this.subscribedPlayers.removeIf(id -> (level.getPlayerByUUID(id) == null));
/*  56 */     if (!this.subscribedPlayers.isEmpty())
/*  57 */       pollAndSendUpdates(level); 
/*     */   }
/*     */   
/*     */   private void addSubscriber(ServerPlayer player) {
/*  62 */     this.subscribedPlayers.add(player.getUUID());
/*  64 */     player.getChunkTrackingView().forEach(chunkPos -> {
/*     */           if (!player.connection.chunkSender.isPending(chunkPos.pack()))
/*     */             startTrackingChunk(player, chunkPos); 
/*     */         });
/*  70 */     (player.level().getChunkSource()).chunkMap.forEachEntityTrackedBy(player, entity -> startTrackingEntity(player, entity));
/*     */   }
/*     */   
/*     */   protected final void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos trackedChunk, Packet<? super ClientGamePacketListener> packet) {
/*  76 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  77 */     for (UUID playerId : this.subscribedPlayers) {
/*  78 */       Player player = level.getPlayerByUUID(playerId);
/*  78 */       if (player instanceof ServerPlayer) {
/*  78 */         ServerPlayer serverPlayer = (ServerPlayer)player;
/*  78 */         if (chunkMap.isChunkTracked(serverPlayer, trackedChunk.x(), trackedChunk.z()))
/*  79 */           serverPlayer.connection.send(packet); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void sendToPlayersTrackingEntity(ServerLevel level, Entity trackedEntity, Packet<? super ClientGamePacketListener> packet) {
/*  85 */     ChunkMap chunkMap = (level.getChunkSource()).chunkMap;
/*  86 */     chunkMap.sendToTrackingPlayersFiltered(trackedEntity, packet, player -> this.subscribedPlayers.contains(player.getUUID()));
/*     */   }
/*     */   
/*     */   public final void startTrackingChunk(ServerPlayer player, ChunkPos chunkPos) {
/*  94 */     if (this.subscribedPlayers.contains(player.getUUID()))
/*  95 */       sendInitialChunk(player, chunkPos); 
/*     */   }
/*     */   
/*     */   public final void startTrackingEntity(ServerPlayer player, Entity entity) {
/* 100 */     if (this.subscribedPlayers.contains(player.getUUID()))
/* 101 */       sendInitialEntity(player, entity); 
/*     */   }
/*     */   
/*     */   protected void clear() {}
/*     */   
/*     */   protected void pollAndSendUpdates(ServerLevel level) {}
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {}
/*     */   
/*     */   protected void sendInitialEntity(ServerPlayer player, Entity entity) {}
/*     */   
/*     */   public static class SourceSynchronizer<T> extends TrackingDebugSynchronizer<T> {
/* 118 */     private final Map<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> chunkSources = new HashMap<>();
/*     */     
/* 119 */     private final Map<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> blockEntitySources = new HashMap<>();
/*     */     
/* 120 */     private final Map<UUID, TrackingDebugSynchronizer.ValueSource<T>> entitySources = new HashMap<>();
/*     */     
/*     */     public SourceSynchronizer(DebugSubscription<T> subscription) {
/* 123 */       super(subscription);
/*     */     }
/*     */     
/*     */     protected void clear() {
/* 128 */       this.chunkSources.clear();
/* 129 */       this.blockEntitySources.clear();
/* 130 */       this.entitySources.clear();
/*     */     }
/*     */     
/*     */     protected void pollAndSendUpdates(ServerLevel level) {
/* 135 */       for (Map.Entry<ChunkPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.chunkSources.entrySet()) {
/* 136 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 137 */         if (update != null) {
/* 138 */           ChunkPos chunkPos = entry.getKey();
/* 139 */           sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugChunkValuePacket(chunkPos, update));
/*     */         } 
/*     */       } 
/* 143 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 144 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 145 */         if (update != null) {
/* 146 */           BlockPos blockPos = entry.getKey();
/* 147 */           ChunkPos chunkPos = ChunkPos.containing(blockPos);
/* 148 */           sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, update));
/*     */         } 
/*     */       } 
/* 152 */       for (Map.Entry<UUID, TrackingDebugSynchronizer.ValueSource<T>> entry : this.entitySources.entrySet()) {
/* 153 */         DebugSubscription.Update<T> update = ((TrackingDebugSynchronizer.ValueSource<T>)entry.getValue()).pollUpdate(this.subscription);
/* 154 */         if (update != null) {
/* 155 */           Entity entity = Objects.<Entity>requireNonNull(level.getEntity(entry.getKey()));
/* 156 */           sendToPlayersTrackingEntity(level, entity, (Packet<? super ClientGamePacketListener>)new ClientboundDebugEntityValuePacket(entity.getId(), update));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void registerChunk(ChunkPos chunkPos, DebugValueSource.ValueGetter<T> getter) {
/* 162 */       this.chunkSources.put(chunkPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void registerBlockEntity(BlockPos blockPos, DebugValueSource.ValueGetter<T> getter) {
/* 166 */       this.blockEntitySources.put(blockPos, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void registerEntity(UUID entityId, DebugValueSource.ValueGetter<T> getter) {
/* 170 */       this.entitySources.put(entityId, new TrackingDebugSynchronizer.ValueSource<>(getter));
/*     */     }
/*     */     
/*     */     public void dropChunk(ChunkPos chunkPos) {
/* 174 */       this.chunkSources.remove(chunkPos);
/* 177 */       Objects.requireNonNull(chunkPos);
/* 177 */       this.blockEntitySources.keySet().removeIf(chunkPos::contains);
/*     */     }
/*     */     
/*     */     public void dropBlockEntity(ServerLevel level, BlockPos blockPos) {
/* 181 */       TrackingDebugSynchronizer.ValueSource<T> source = this.blockEntitySources.remove(blockPos);
/* 182 */       if (source != null) {
/* 183 */         ChunkPos chunkPos = ChunkPos.containing(blockPos);
/* 184 */         sendToPlayersTrackingChunk(level, chunkPos, (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.emptyUpdate()));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void dropEntity(Entity entity) {
/* 189 */       this.entitySources.remove(entity.getUUID());
/*     */     }
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 194 */       TrackingDebugSynchronizer.ValueSource<T> chunkSource = this.chunkSources.get(chunkPos);
/* 195 */       if (chunkSource != null && chunkSource.lastSyncedValue != null)
/* 196 */         player.connection.send((Packet)new ClientboundDebugChunkValuePacket(chunkPos, this.subscription.packUpdate(chunkSource.lastSyncedValue))); 
/* 199 */       for (Map.Entry<BlockPos, TrackingDebugSynchronizer.ValueSource<T>> entry : this.blockEntitySources.entrySet()) {
/* 200 */         T lastValue = ((TrackingDebugSynchronizer.ValueSource)entry.getValue()).lastSyncedValue;
/* 201 */         if (lastValue == null)
/*     */           continue; 
/* 204 */         BlockPos blockPos = entry.getKey();
/* 205 */         if (chunkPos.contains(blockPos))
/* 206 */           player.connection.send((Packet)new ClientboundDebugBlockValuePacket(blockPos, this.subscription.packUpdate(lastValue))); 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void sendInitialEntity(ServerPlayer player, Entity entity) {
/* 213 */       TrackingDebugSynchronizer.ValueSource<T> source = this.entitySources.get(entity.getUUID());
/* 214 */       if (source != null && source.lastSyncedValue != null)
/* 215 */         player.connection.send((Packet)new ClientboundDebugEntityValuePacket(entity.getId(), this.subscription.packUpdate(source.lastSyncedValue))); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ValueSource<T> {
/*     */     private final DebugValueSource.ValueGetter<T> getter;
/*     */     
/*     */     private T lastSyncedValue;
/*     */     
/*     */     private ValueSource(DebugValueSource.ValueGetter<T> getter) {
/* 225 */       this.getter = getter;
/*     */     }
/*     */     
/*     */     public DebugSubscription.Update<T> pollUpdate(DebugSubscription<T> subscription) {
/* 229 */       T newValue = this.getter.get();
/* 230 */       if (!Objects.equals(newValue, this.lastSyncedValue)) {
/* 231 */         this.lastSyncedValue = newValue;
/* 232 */         return subscription.packUpdate(newValue);
/*     */       } 
/* 234 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PoiSynchronizer extends TrackingDebugSynchronizer<DebugPoiInfo> {
/*     */     public PoiSynchronizer() {
/* 240 */       super(DebugSubscriptions.POIS);
/*     */     }
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 245 */       ServerLevel level = player.level();
/* 246 */       PoiManager poiManager = level.getPoiManager();
/* 247 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> player.connection.send((Packet)new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))));
/*     */     }
/*     */     
/*     */     public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 255 */       sendToPlayersTrackingChunk(level, ChunkPos.containing(record.getPos()), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record))));
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 259 */       sendToPlayersTrackingChunk(level, ChunkPos.containing(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate()));
/*     */     }
/*     */     
/*     */     public void onPoiTicketCountChanged(ServerLevel level, BlockPos poiPos) {
/* 263 */       sendToPlayersTrackingChunk(level, ChunkPos.containing(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos))));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VillageSectionSynchronizer extends TrackingDebugSynchronizer<Unit> {
/*     */     public VillageSectionSynchronizer() {
/* 269 */       super(DebugSubscriptions.VILLAGE_SECTIONS);
/*     */     }
/*     */     
/*     */     protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 274 */       ServerLevel level = player.level();
/* 275 */       PoiManager poiManager = level.getPoiManager();
/* 276 */       poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> {
/*     */             SectionPos centerSection = SectionPos.of(record.getPos());
/*     */             forEachVillageSectionUpdate(level, centerSection, ());
/*     */           });
/*     */     }
/*     */     
/*     */     public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 288 */       sendVillageSectionsPacket(level, record.getPos());
/*     */     }
/*     */     
/*     */     public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 292 */       sendVillageSectionsPacket(level, poiPos);
/*     */     }
/*     */     
/*     */     private void sendVillageSectionsPacket(ServerLevel level, BlockPos poiPos) {
/* 296 */       forEachVillageSectionUpdate(level, SectionPos.of(poiPos), (sectionPos, isVillage) -> {
/*     */             BlockPos sectionBlockPos = sectionPos.center();
/*     */             if (isVillage.booleanValue()) {
/*     */               sendToPlayersTrackingChunk(level, ChunkPos.containing(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */             } else {
/*     */               sendToPlayersTrackingChunk(level, ChunkPos.containing(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate()));
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/*     */     private static void forEachVillageSectionUpdate(ServerLevel level, SectionPos centerSection, BiConsumer<SectionPos, Boolean> consumer) {
/* 307 */       for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 308 */         for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 309 */           for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 310 */             SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
/* 311 */             if (level.isVillage(sectionPos.center())) {
/* 312 */               consumer.accept(sectionPos, Boolean.valueOf(true));
/*     */             } else {
/* 314 */               consumer.accept(sectionPos, Boolean.valueOf(false));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */