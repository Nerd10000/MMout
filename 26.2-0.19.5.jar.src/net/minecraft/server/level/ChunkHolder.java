/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ public class ChunkHolder extends GenerationChunkHolder {
/*  33 */   public static final ChunkResult<LevelChunk> UNLOADED_LEVEL_CHUNK = ChunkResult.error("Unloaded level chunk");
/*     */   
/*  34 */   private static final CompletableFuture<ChunkResult<LevelChunk>> UNLOADED_LEVEL_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_LEVEL_CHUNK);
/*     */   
/*     */   private final LevelHeightAccessor levelHeightAccessor;
/*     */   
/*  38 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */   
/*  39 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */   
/*  40 */   private volatile CompletableFuture<ChunkResult<LevelChunk>> entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */   
/*     */   private int oldTicketLevel;
/*     */   
/*     */   private int ticketLevel;
/*     */   
/*     */   private int queueLevel;
/*     */   
/*     */   private boolean hasChangedSections;
/*     */   
/*     */   private final ShortSet[] changedBlocksPerSection;
/*     */   
/*  49 */   private final BitSet blockChangedLightSectionFilter = new BitSet();
/*     */   
/*  50 */   private final BitSet skyChangedLightSectionFilter = new BitSet();
/*     */   
/*     */   private final LevelLightEngine lightEngine;
/*     */   
/*     */   private final LevelChangeListener onLevelChange;
/*     */   
/*     */   private final PlayerProvider playerProvider;
/*     */   
/*     */   private boolean wasAccessibleSinceLastSave;
/*     */   
/*  56 */   private CompletableFuture<?> pendingFullStateConfirmation = CompletableFuture.completedFuture(null);
/*     */   
/*  57 */   private CompletableFuture<?> sendSync = CompletableFuture.completedFuture(null);
/*     */   
/*  58 */   private CompletableFuture<?> saveSync = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public ChunkHolder(ChunkPos pos, int ticketLevel, LevelHeightAccessor levelHeightAccessor, LevelLightEngine lightEngine, LevelChangeListener onLevelChange, PlayerProvider playerProvider) {
/*  61 */     super(pos);
/*  62 */     this.levelHeightAccessor = levelHeightAccessor;
/*  63 */     this.lightEngine = lightEngine;
/*  64 */     this.onLevelChange = onLevelChange;
/*  65 */     this.playerProvider = playerProvider;
/*  66 */     this.oldTicketLevel = ChunkLevel.MAX_LEVEL + 1;
/*  67 */     this.ticketLevel = this.oldTicketLevel;
/*  68 */     this.queueLevel = this.oldTicketLevel;
/*  69 */     setTicketLevel(ticketLevel);
/*  70 */     this.changedBlocksPerSection = new ShortSet[levelHeightAccessor.getSectionsCount()];
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getTickingChunkFuture() {
/*  75 */     return this.tickingChunkFuture;
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getEntityTickingChunkFuture() {
/*  80 */     return this.entityTickingChunkFuture;
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkResult<LevelChunk>> getFullChunkFuture() {
/*  85 */     return this.fullChunkFuture;
/*     */   }
/*     */   
/*     */   public LevelChunk getTickingChunk() {
/*  90 */     return ((ChunkResult<LevelChunk>)getTickingChunkFuture().getNow(UNLOADED_LEVEL_CHUNK)).orElse(null);
/*     */   }
/*     */   
/*     */   public LevelChunk getChunkToSend() {
/*  94 */     if (!this.sendSync.isDone())
/*  95 */       return null; 
/*  97 */     return getTickingChunk();
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> getSendSyncFuture() {
/* 101 */     return this.sendSync;
/*     */   }
/*     */   
/*     */   public void addSendDependency(CompletableFuture<?> sync) {
/* 105 */     if (this.sendSync.isDone()) {
/* 106 */       this.sendSync = sync;
/*     */     } else {
/* 108 */       this.sendSync = this.sendSync.thenCombine(sync, (a, b) -> null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> getSaveSyncFuture() {
/* 113 */     return this.saveSync;
/*     */   }
/*     */   
/*     */   public boolean isReadyForSaving() {
/* 117 */     return this.saveSync.isDone();
/*     */   }
/*     */   
/*     */   protected void addSaveDependency(CompletableFuture<?> sync) {
/* 122 */     if (this.saveSync.isDone()) {
/* 123 */       this.saveSync = sync;
/*     */     } else {
/* 125 */       this.saveSync = this.saveSync.thenCombine(sync, (a, b) -> null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean blockChanged(BlockPos pos) {
/*     */     ShortOpenHashSet shortOpenHashSet;
/* 132 */     LevelChunk chunk = getTickingChunk();
/* 133 */     if (chunk == null)
/* 134 */       return false; 
/* 137 */     boolean hadChangedSections = this.hasChangedSections;
/* 138 */     int sectionIndex = this.levelHeightAccessor.getSectionIndex(pos.getY());
/* 139 */     ShortSet changedBlocksInSection = this.changedBlocksPerSection[sectionIndex];
/* 140 */     if (changedBlocksInSection == null) {
/* 141 */       this.hasChangedSections = true;
/* 142 */       shortOpenHashSet = new ShortOpenHashSet();
/* 143 */       this.changedBlocksPerSection[sectionIndex] = (ShortSet)shortOpenHashSet;
/*     */     } 
/* 145 */     shortOpenHashSet.add(SectionPos.sectionRelativePos(pos));
/* 146 */     return !hadChangedSections;
/*     */   }
/*     */   
/*     */   public boolean sectionLightChanged(LightLayer layer, int chunkY) {
/* 150 */     ChunkAccess chunk = getChunkIfPresent(ChunkStatus.INITIALIZE_LIGHT);
/* 151 */     if (chunk == null)
/* 152 */       return false; 
/* 155 */     chunk.markUnsaved();
/* 157 */     LevelChunk tickingChunk = getTickingChunk();
/* 158 */     if (tickingChunk == null)
/* 159 */       return false; 
/* 162 */     int minLightSection = this.lightEngine.getMinLightSection();
/* 163 */     int maxLightSection = this.lightEngine.getMaxLightSection();
/* 164 */     if (chunkY < minLightSection || chunkY > maxLightSection)
/* 165 */       return false; 
/* 168 */     BitSet filter = (layer == LightLayer.SKY) ? this.skyChangedLightSectionFilter : this.blockChangedLightSectionFilter;
/* 169 */     int index = chunkY - minLightSection;
/* 170 */     if (!filter.get(index)) {
/* 171 */       filter.set(index);
/* 172 */       return true;
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasChangesToBroadcast() {
/* 178 */     return (this.hasChangedSections || !this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty());
/*     */   }
/*     */   
/*     */   public void broadcastChanges(LevelChunk chunk) {
/* 182 */     if (!hasChangesToBroadcast())
/*     */       return; 
/* 186 */     Level level = chunk.getLevel();
/* 188 */     if (!this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty()) {
/* 189 */       List<ServerPlayer> borderPlayers = this.playerProvider.getPlayers(this.pos, true);
/* 190 */       if (!borderPlayers.isEmpty()) {
/* 191 */         ClientboundLightUpdatePacket lightPacket = new ClientboundLightUpdatePacket(chunk.getPos(), this.lightEngine, this.skyChangedLightSectionFilter, this.blockChangedLightSectionFilter);
/* 192 */         broadcast(borderPlayers, (Packet<?>)lightPacket);
/*     */       } 
/* 194 */       this.skyChangedLightSectionFilter.clear();
/* 195 */       this.blockChangedLightSectionFilter.clear();
/*     */     } 
/* 198 */     if (!this.hasChangedSections)
/*     */       return; 
/* 202 */     List<ServerPlayer> players = this.playerProvider.getPlayers(this.pos, false);
/* 203 */     for (int sectionIndex = 0; sectionIndex < this.changedBlocksPerSection.length; sectionIndex++) {
/* 204 */       ShortSet changedBlocks = this.changedBlocksPerSection[sectionIndex];
/* 205 */       if (changedBlocks != null) {
/* 208 */         this.changedBlocksPerSection[sectionIndex] = null;
/* 210 */         if (!players.isEmpty()) {
/* 213 */           int sectionY = this.levelHeightAccessor.getSectionYFromSectionIndex(sectionIndex);
/* 214 */           SectionPos sectionPos = SectionPos.of(chunk.getPos(), sectionY);
/* 216 */           if (changedBlocks.size() == 1) {
/* 217 */             BlockPos pos = sectionPos.relativeToBlockPos(changedBlocks.iterator().nextShort());
/* 218 */             BlockState state = level.getBlockState(pos);
/* 220 */             broadcast(players, (Packet<?>)new ClientboundBlockUpdatePacket(pos, state));
/* 221 */             broadcastBlockEntityIfNeeded(players, level, pos, state);
/*     */           } else {
/* 223 */             LevelChunkSection section = chunk.getSection(sectionIndex);
/* 224 */             ClientboundSectionBlocksUpdatePacket packet = new ClientboundSectionBlocksUpdatePacket(sectionPos, changedBlocks, section);
/* 226 */             broadcast(players, (Packet<?>)packet);
/* 227 */             packet.runUpdates((pos, state) -> broadcastBlockEntityIfNeeded(players, level, pos, state));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 230 */     this.hasChangedSections = false;
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntityIfNeeded(List<ServerPlayer> players, Level level, BlockPos pos, BlockState state) {
/* 234 */     if (state.hasBlockEntity())
/* 235 */       broadcastBlockEntity(players, level, pos); 
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntity(List<ServerPlayer> players, Level level, BlockPos blockPos) {
/* 240 */     BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 241 */     if (blockEntity != null) {
/* 242 */       Packet<?> packet = blockEntity.getUpdatePacket();
/* 243 */       if (packet != null)
/* 244 */         broadcast(players, packet); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(List<ServerPlayer> players, Packet<?> packet) {
/* 250 */     players.forEach(player -> player.connection.send(packet));
/*     */   }
/*     */   
/*     */   public int getTicketLevel() {
/* 255 */     return this.ticketLevel;
/*     */   }
/*     */   
/*     */   public int getQueueLevel() {
/* 260 */     return this.queueLevel;
/*     */   }
/*     */   
/*     */   private void setQueueLevel(int queueLevel) {
/* 264 */     this.queueLevel = queueLevel;
/*     */   }
/*     */   
/*     */   public void setTicketLevel(int ticketLevel) {
/* 268 */     this.ticketLevel = ticketLevel;
/*     */   }
/*     */   
/*     */   private void scheduleFullChunkPromotion(ChunkMap scheduler, CompletableFuture<ChunkResult<LevelChunk>> task, Executor mainThreadExecutor, FullChunkStatus status) {
/* 272 */     this.pendingFullStateConfirmation.cancel(false);
/* 273 */     CompletableFuture<Void> confirmation = new CompletableFuture<>();
/* 274 */     confirmation.thenRunAsync(() -> scheduler.onFullChunkStatusChange(this.pos, status), mainThreadExecutor);
/* 275 */     this.pendingFullStateConfirmation = confirmation;
/* 276 */     task.thenAccept(r -> r.ifSuccess(()));
/*     */   }
/*     */   
/*     */   private void demoteFullChunk(ChunkMap scheduler, FullChunkStatus status) {
/* 280 */     this.pendingFullStateConfirmation.cancel(false);
/* 281 */     scheduler.onFullChunkStatusChange(this.pos, status);
/*     */   }
/*     */   
/*     */   protected void updateFutures(ChunkMap scheduler, Executor mainThreadExecutor) {
/* 286 */     FullChunkStatus oldFullStatus = ChunkLevel.fullStatus(this.oldTicketLevel);
/* 287 */     FullChunkStatus newFullStatus = ChunkLevel.fullStatus(this.ticketLevel);
/* 289 */     boolean wasAccessible = oldFullStatus.isOrAfter(FullChunkStatus.FULL);
/* 290 */     boolean isAccessible = newFullStatus.isOrAfter(FullChunkStatus.FULL);
/* 291 */     this.wasAccessibleSinceLastSave |= isAccessible;
/* 293 */     if (!wasAccessible && isAccessible) {
/* 294 */       this.fullChunkFuture = scheduler.prepareAccessibleChunk(this);
/* 295 */       scheduleFullChunkPromotion(scheduler, this.fullChunkFuture, mainThreadExecutor, FullChunkStatus.FULL);
/* 296 */       addSaveDependency(this.fullChunkFuture);
/*     */     } 
/* 298 */     if (wasAccessible && !isAccessible) {
/* 299 */       this.fullChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 300 */       this.fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/* 303 */     boolean wasTicking = oldFullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/* 304 */     boolean isTicking = newFullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/* 306 */     if (!wasTicking && isTicking) {
/* 307 */       this.tickingChunkFuture = scheduler.prepareTickingChunk(this);
/* 308 */       scheduleFullChunkPromotion(scheduler, this.tickingChunkFuture, mainThreadExecutor, FullChunkStatus.BLOCK_TICKING);
/* 309 */       addSaveDependency(this.tickingChunkFuture);
/*     */     } 
/* 311 */     if (wasTicking && !isTicking) {
/* 312 */       this.tickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 313 */       this.tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/* 316 */     boolean wasEntityTicking = oldFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/* 317 */     boolean isEntityTicking = newFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/* 319 */     if (!wasEntityTicking && isEntityTicking) {
/* 320 */       if (this.entityTickingChunkFuture != UNLOADED_LEVEL_CHUNK_FUTURE)
/* 321 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException()); 
/* 323 */       this.entityTickingChunkFuture = scheduler.prepareEntityTickingChunk(this);
/* 324 */       scheduleFullChunkPromotion(scheduler, this.entityTickingChunkFuture, mainThreadExecutor, FullChunkStatus.ENTITY_TICKING);
/* 325 */       addSaveDependency(this.entityTickingChunkFuture);
/*     */     } 
/* 327 */     if (wasEntityTicking && !isEntityTicking) {
/* 328 */       this.entityTickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 329 */       this.entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/* 332 */     if (!newFullStatus.isOrAfter(oldFullStatus))
/* 333 */       demoteFullChunk(scheduler, newFullStatus); 
/* 336 */     this.onLevelChange.onLevelChange(this.pos, this::getQueueLevel, this.ticketLevel, this::setQueueLevel);
/* 337 */     this.oldTicketLevel = this.ticketLevel;
/*     */   }
/*     */   
/*     */   public boolean wasAccessibleSinceLastSave() {
/* 341 */     return this.wasAccessibleSinceLastSave;
/*     */   }
/*     */   
/*     */   public void refreshAccessibility() {
/* 345 */     this.wasAccessibleSinceLastSave = ChunkLevel.fullStatus(this.ticketLevel).isOrAfter(FullChunkStatus.FULL);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LevelChangeListener {
/*     */     void onLevelChange(ChunkPos param1ChunkPos, IntSupplier param1IntSupplier, int param1Int, IntConsumer param1IntConsumer);
/*     */   }
/*     */   
/*     */   public static interface PlayerProvider {
/*     */     List<ServerPlayer> getPlayers(ChunkPos param1ChunkPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */