/*     */ package net.minecraft.world.level.dimension.end;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ContiguousSet;
/*     */ import com.google.common.collect.DiscreteDomain;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Range;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.features.EndFeatures;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockPredicate;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnderDragonFight extends SavedData {
/*  75 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TICKS_BEFORE_DRAGON_RESPAWN = 1200;
/*     */   
/*     */   private static final int TIME_BETWEEN_CRYSTAL_SCANS = 100;
/*     */   
/*     */   public static final int TIME_BETWEEN_PLAYER_SCANS = 20;
/*     */   
/*     */   private static final int ARENA_SIZE_CHUNKS = 8;
/*     */   
/*     */   public static final int ARENA_TICKET_LEVEL = 9;
/*     */   
/*     */   private static final int GATEWAY_COUNT = 20;
/*     */   
/*     */   private static final int GATEWAY_DISTANCE = 96;
/*     */   
/*     */   public static final int DRAGON_SPAWN_Y = 128;
/*     */   
/*  84 */   private static final Component EVENT_DISPLAY_NAME = (Component)Component.translatable("entity.minecraft.ender_dragon");
/*     */   
/*     */   private Predicate<Entity> validPlayer;
/*     */   
/*     */   private ServerBossEvent dragonEvent;
/*     */   
/*     */   private ServerLevel level;
/*     */   
/*     */   private BlockPos origin;
/*     */   
/*     */   private final List<Integer> gateways;
/*     */   
/*  91 */   private final BlockPattern exitPortalPattern = BlockPatternBuilder.start()
/*  92 */     .aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  " }).aisle(new String[] { "       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       " }).where('#', BlockInWorld.hasState((Predicate)BlockPredicate.forBlock(Blocks.BEDROCK)))
/* 138 */     .build();
/*     */   
/*     */   private int ticksSinceDragonSeen;
/*     */   
/*     */   private int aliveCrystals;
/*     */   
/*     */   private int ticksSinceCrystalsScanned;
/*     */   
/* 142 */   private int ticksSinceLastPlayerScan = 21;
/*     */   
/*     */   private boolean dragonKilled;
/*     */   
/*     */   private boolean hasPreviouslyKilledDragon;
/*     */   
/*     */   private boolean skipArenaLoadedCheck = false;
/*     */   
/*     */   private UUID dragonUUID;
/*     */   
/*     */   private boolean needsStateScanning;
/*     */   
/*     */   private BlockPos exitPortalLocation;
/*     */   
/*     */   private DragonRespawnStage respawnStage;
/*     */   
/*     */   private int respawnTime;
/*     */   
/*     */   private List<EntityReference<EndCrystal>> respawnCrystals;
/*     */   
/*     */   public static final Codec<EnderDragonFight> CODEC;
/*     */   
/*     */   static {
/* 154 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "needs_state_scanning", Boolean.valueOf(true)).forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "dragon_killed", Boolean.valueOf(false)).forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "previously_killed", Boolean.valueOf(false)).forGetter(()), (App)DragonRespawnStage.CODEC.optionalFieldOf("respawn_stage").forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.INT, "respawn_time", Integer.valueOf(0)).forGetter(()), (App)UUIDUtil.CODEC.lenientOptionalFieldOf("dragon_uuid").forGetter(()), (App)BlockPos.CODEC.lenientOptionalFieldOf("exit_portal_location").forGetter(()), (App)Codec.list((Codec)Codec.INT).lenientOptionalFieldOf("gateways", new ArrayList()).forGetter(()), (App)Codec.list(EntityReference.codec()).optionalFieldOf("respawn_crystals", List.of()).forGetter(())).apply((Applicative)i, EnderDragonFight::new));
/*     */   }
/*     */   
/* 165 */   public static final SavedDataType<EnderDragonFight> TYPE = new SavedDataType(Identifier.withDefaultNamespace("ender_dragon_fight"), EnderDragonFight::createDefault, CODEC, DataFixTypes.SAVED_DATA_ENDER_DRAGON_FIGHT);
/*     */   
/*     */   public static EnderDragonFight createDefault() {
/* 168 */     return new EnderDragonFight(true, false, false, Optional.empty(), 0, Optional.empty(), Optional.empty(), (List<Integer>)new ObjectArrayList(), List.of());
/*     */   }
/*     */   
/*     */   public EnderDragonFight(boolean needsStateScanning, boolean dragonKilled, boolean previouslyKilled, Optional<DragonRespawnStage> respawnStage, int respawnTime, Optional<UUID> dragonUUID, Optional<BlockPos> exitPortalLocation, List<Integer> gateways, List<EntityReference<EndCrystal>> respawnCrystals) {
/* 172 */     this.needsStateScanning = needsStateScanning;
/* 173 */     this.dragonUUID = dragonUUID.orElse(null);
/* 174 */     this.dragonKilled = dragonKilled;
/* 175 */     this.hasPreviouslyKilledDragon = previouslyKilled;
/* 176 */     this.respawnStage = respawnStage.orElse(null);
/* 177 */     this.respawnTime = respawnTime;
/* 178 */     this.exitPortalLocation = exitPortalLocation.orElse(null);
/* 179 */     this.gateways = (List<Integer>)new ObjectArrayList(gateways);
/* 180 */     this.respawnCrystals = respawnCrystals;
/*     */   }
/*     */   
/*     */   public void init(ServerLevel level, long seed, BlockPos origin) {
/* 184 */     this.level = level;
/* 185 */     this.origin = origin;
/* 187 */     this.dragonEvent = new ServerBossEvent(Mth.createInsecureUUID(level.getRandom()), EVENT_DISPLAY_NAME, BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS);
/* 188 */     this.dragonEvent.setPlayBossMusic(true).setCreateWorldFog(true);
/* 190 */     this.validPlayer = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance(origin.getX(), (128 + origin.getY()), origin.getZ(), 192.0D));
/* 191 */     if (this.gateways.isEmpty()) {
/* 192 */       ObjectArrayList<Integer> newGateways = new ObjectArrayList((Collection)ContiguousSet.create(Range.closedOpen(Integer.valueOf(0), Integer.valueOf(20)), DiscreteDomain.integers()));
/* 193 */       Util.shuffle((List)newGateways, RandomSource.createThreadLocalInstance(seed));
/* 194 */       this.gateways.addAll((Collection<? extends Integer>)newGateways);
/* 195 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   public void skipArenaLoadedCheck() {
/* 206 */     this.skipArenaLoadedCheck = true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 210 */     this.dragonEvent.setVisible(!this.dragonKilled);
/* 212 */     if (++this.ticksSinceLastPlayerScan >= 20) {
/* 213 */       updatePlayers();
/* 214 */       this.ticksSinceLastPlayerScan = 0;
/*     */     } 
/* 217 */     if (!this.dragonEvent.getPlayers().isEmpty()) {
/* 218 */       this.level.getChunkSource().addTicketWithRadius(TicketType.DRAGON, new ChunkPos(0, 0), 9);
/* 220 */       if (!isArenaLoaded())
/*     */         return; 
/* 224 */       if (this.needsStateScanning) {
/* 225 */         scanState();
/* 226 */         this.needsStateScanning = false;
/* 227 */         setDirty();
/*     */       } 
/* 230 */       if (this.respawnStage != null) {
/* 231 */         List<EndCrystal> respawnCrystals = this.respawnCrystals.stream().map(e -> (EndCrystal)e.getEntity((Level)this.level, EndCrystal.class)).filter(Objects::nonNull).toList();
/* 232 */         if (respawnCrystals.isEmpty()) {
/* 233 */           abortRespawnSequence();
/*     */           return;
/*     */         } 
/* 236 */         this.respawnStage.tick(this.level, this, respawnCrystals, this.respawnTime++);
/* 237 */         setDirty();
/*     */       } 
/* 240 */       if (!this.dragonKilled) {
/* 241 */         if (this.dragonUUID == null || ++this.ticksSinceDragonSeen >= 1200) {
/* 242 */           findOrCreateDragon();
/* 243 */           this.ticksSinceDragonSeen = 0;
/*     */         } 
/* 246 */         if (++this.ticksSinceCrystalsScanned >= 100) {
/* 247 */           updateCrystalCount();
/* 248 */           this.ticksSinceCrystalsScanned = 0;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 252 */       this.level.getChunkSource().removeTicketWithRadius(TicketType.DRAGON, new ChunkPos(0, 0), 9);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanState() {
/* 257 */     LOGGER.info("Scanning for legacy world dragon fight...");
/* 258 */     boolean activePortalExists = hasActiveExitPortal();
/* 259 */     if (activePortalExists) {
/* 260 */       LOGGER.info("Found that the dragon has been killed in this world already.");
/* 261 */       this.hasPreviouslyKilledDragon = true;
/*     */     } else {
/* 263 */       LOGGER.info("Found that the dragon has not yet been killed in this world.");
/* 264 */       this.hasPreviouslyKilledDragon = false;
/* 265 */       if (findExitPortal() == null)
/* 266 */         spawnExitPortal(false); 
/*     */     } 
/* 270 */     List<? extends EnderDragon> entities = this.level.getDragons();
/* 271 */     if (entities.isEmpty()) {
/* 272 */       this.dragonKilled = true;
/*     */     } else {
/* 274 */       EnderDragon dragon = entities.get(0);
/* 275 */       this.dragonUUID = dragon.getUUID();
/* 276 */       LOGGER.info("Found that there's a dragon still alive ({})", dragon);
/* 277 */       this.dragonKilled = false;
/* 279 */       if (!activePortalExists) {
/* 280 */         LOGGER.info("But we didn't have a portal, let's remove it.");
/* 281 */         dragon.discard();
/* 282 */         this.dragonUUID = null;
/*     */       } 
/*     */     } 
/* 286 */     if (!this.hasPreviouslyKilledDragon && this.dragonKilled)
/* 288 */       this.dragonKilled = false; 
/* 290 */     setDirty();
/*     */   }
/*     */   
/*     */   private void findOrCreateDragon() {
/* 294 */     List<? extends EnderDragon> entities = this.level.getDragons();
/* 295 */     if (entities.isEmpty()) {
/* 296 */       LOGGER.debug("Haven't seen the dragon, respawning it");
/* 297 */       createNewDragon();
/*     */     } else {
/* 299 */       LOGGER.debug("Haven't seen our dragon, but found another one to use.");
/* 300 */       this.dragonUUID = ((EnderDragon)entities.get(0)).getUUID();
/* 301 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setRespawnStage(DragonRespawnStage stage) {
/* 306 */     if (this.respawnStage == null)
/* 307 */       throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation."); 
/* 310 */     this.respawnTime = 0;
/* 311 */     if (stage == DragonRespawnStage.END) {
/* 312 */       this.respawnStage = null;
/* 313 */       this.dragonKilled = false;
/* 314 */       EnderDragon dragon = createNewDragon();
/* 316 */       if (dragon != null)
/* 317 */         for (ServerPlayer player : this.dragonEvent.getPlayers())
/* 318 */           CriteriaTriggers.SUMMONED_ENTITY.trigger(player, (Entity)dragon);  
/*     */     } else {
/* 322 */       this.respawnStage = stage;
/*     */     } 
/* 324 */     setDirty();
/*     */   }
/*     */   
/*     */   private boolean hasActiveExitPortal() {
/* 328 */     for (int x = -8; x <= 8; x++) {
/* 329 */       for (int z = -8; z <= 8; z++) {
/* 330 */         LevelChunk chunk = this.level.getChunk(x, z);
/* 331 */         for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
/* 332 */           if (blockEntity instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity)
/* 333 */             return true; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     return false;
/*     */   }
/*     */   
/*     */   private BlockPattern.BlockPatternMatch findExitPortal() {
/* 343 */     ChunkPos chunkOrigin = ChunkPos.containing(this.origin);
/* 344 */     for (int x = -8 + chunkOrigin.x(); x <= 8 + chunkOrigin.x(); x++) {
/* 345 */       for (int z = -8 + chunkOrigin.z(); z <= 8 + chunkOrigin.z(); z++) {
/* 346 */         LevelChunk chunk = this.level.getChunk(x, z);
/* 347 */         for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
/* 348 */           if (blockEntity instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity) {
/* 349 */             BlockPattern.BlockPatternMatch match = this.exitPortalPattern.find((LevelReader)this.level, blockEntity.getBlockPos());
/* 350 */             if (match != null) {
/* 351 */               BlockPos posInWorld = match.getBlock(3, 3, 3).getPos();
/* 352 */               if (this.exitPortalLocation == null) {
/* 353 */                 this.exitPortalLocation = posInWorld;
/* 354 */                 setDirty();
/*     */               } 
/* 356 */               return match;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 363 */     BlockPos endPodiumLocation = EndPodiumFeature.getLocation(this.origin);
/* 364 */     int maxY = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, endPodiumLocation).getY();
/* 366 */     for (int y = maxY; y >= this.level.getMinY(); y--) {
/* 367 */       BlockPattern.BlockPatternMatch match = this.exitPortalPattern.find((LevelReader)this.level, new BlockPos(endPodiumLocation.getX(), y, endPodiumLocation.getZ()));
/* 368 */       if (match != null) {
/* 369 */         if (this.exitPortalLocation == null) {
/* 370 */           this.exitPortalLocation = match.getBlock(3, 3, 3).getPos();
/* 371 */           setDirty();
/*     */         } 
/* 373 */         return match;
/*     */       } 
/*     */     } 
/* 377 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isArenaLoaded() {
/* 381 */     if (this.skipArenaLoadedCheck)
/* 382 */       return true; 
/* 384 */     ChunkPos chunkOrigin = ChunkPos.containing(this.origin);
/* 385 */     for (int x = -8 + chunkOrigin.x(); x <= 8 + chunkOrigin.x(); x++) {
/* 386 */       for (int z = 8 + chunkOrigin.z(); z <= 8 + chunkOrigin.z(); z++) {
/*     */         LevelChunk levelChunk;
/* 387 */         ChunkAccess chunk = this.level.getChunk(x, z, ChunkStatus.FULL, false);
/* 388 */         if (chunk instanceof LevelChunk) {
/* 388 */           levelChunk = (LevelChunk)chunk;
/*     */         } else {
/* 389 */           return false;
/*     */         } 
/* 391 */         FullChunkStatus status = levelChunk.getFullStatus();
/* 392 */         if (!status.isOrAfter(FullChunkStatus.BLOCK_TICKING))
/* 393 */           return false; 
/*     */       } 
/*     */     } 
/* 397 */     if (!ChunkPos.rangeClosed(chunkOrigin, 1).allMatch(pos -> this.level.areEntitiesLoaded(pos.pack())))
/* 398 */       return false; 
/* 400 */     return true;
/*     */   }
/*     */   
/*     */   private void updatePlayers() {
/* 404 */     Set<ServerPlayer> newPlayers = Sets.newHashSet();
/* 405 */     for (ServerPlayer player : this.level.getPlayers(this.validPlayer)) {
/* 406 */       this.dragonEvent.addPlayer(player);
/* 407 */       newPlayers.add(player);
/*     */     } 
/* 409 */     Set<ServerPlayer> toRemove = Sets.newHashSet(this.dragonEvent.getPlayers());
/* 410 */     toRemove.removeAll(newPlayers);
/* 411 */     for (ServerPlayer player : toRemove)
/* 412 */       this.dragonEvent.removePlayer(player); 
/*     */   }
/*     */   
/*     */   private void updateCrystalCount() {
/* 417 */     this.ticksSinceCrystalsScanned = 0;
/* 418 */     this.aliveCrystals = 0;
/* 420 */     for (EndSpikeFeature.EndSpike spike : EndSpikeFeature.getSpikesForLevel((WorldGenLevel)this.level))
/* 421 */       this.aliveCrystals += this.level.getEntitiesOfClass(EndCrystal.class, spike.getTopBoundingBox()).size(); 
/* 424 */     LOGGER.debug("Found {} end crystals still alive", Integer.valueOf(this.aliveCrystals));
/*     */   }
/*     */   
/*     */   public void setDragonKilled(EnderDragon dragon) {
/* 428 */     if (dragon.getUUID().equals(this.dragonUUID)) {
/* 429 */       this.dragonEvent.setProgress(0.0F);
/* 430 */       this.dragonEvent.setVisible(false);
/* 431 */       spawnExitPortal(true);
/* 432 */       spawnNewGateway();
/* 434 */       if (!this.hasPreviouslyKilledDragon)
/* 435 */         this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.origin)), Blocks.DRAGON_EGG.defaultBlockState()); 
/* 438 */       this.hasPreviouslyKilledDragon = true;
/* 439 */       this.dragonKilled = true;
/* 440 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   public void removeAllGateways() {
/* 448 */     this.gateways.clear();
/* 449 */     setDirty();
/*     */   }
/*     */   
/*     */   private void spawnNewGateway() {
/* 453 */     if (this.gateways.isEmpty())
/*     */       return; 
/* 457 */     int gateway = ((Integer)this.gateways.remove(this.gateways.size() - 1)).intValue();
/* 458 */     int x = Mth.floor(96.0D * Math.cos(2.0D * (-3.141592653589793D + 0.15707963267948966D * gateway)));
/* 459 */     int z = Mth.floor(96.0D * Math.sin(2.0D * (-3.141592653589793D + 0.15707963267948966D * gateway)));
/* 460 */     spawnNewGateway(new BlockPos(x, 75, z));
/* 461 */     setDirty();
/*     */   }
/*     */   
/*     */   private void spawnNewGateway(BlockPos pos) {
/* 465 */     this.level.levelEvent(3000, pos, 0);
/* 466 */     this.level.registryAccess().lookup(Registries.CONFIGURED_FEATURE)
/* 467 */       .flatMap(registry -> registry.get(EndFeatures.END_GATEWAY_DELAYED))
/* 468 */       .ifPresent(endGateway -> ((ConfiguredFeature)endGateway.value()).place((WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), pos));
/*     */   }
/*     */   
/*     */   private void spawnExitPortal(boolean activated) {
/* 473 */     EndPodiumFeature feature = new EndPodiumFeature(activated);
/* 475 */     if (this.exitPortalLocation == null) {
/* 476 */       this.exitPortalLocation = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.origin)).below();
/* 477 */       while (this.level.getBlockState(this.exitPortalLocation).is(Blocks.BEDROCK) && this.exitPortalLocation.getY() > 63)
/* 478 */         this.exitPortalLocation = this.exitPortalLocation.below(); 
/* 480 */       this.exitPortalLocation = this.exitPortalLocation.atY(Math.max(this.level.getMinY() + 1, this.exitPortalLocation.getY()));
/* 481 */       setDirty();
/*     */     } 
/* 484 */     if (feature.place((FeatureConfiguration)FeatureConfiguration.NONE, (WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), this.exitPortalLocation)) {
/* 487 */       int chunkRadius = Mth.positiveCeilDiv(4, 16);
/* 488 */       (this.level.getChunkSource()).chunkMap.waitForLightBeforeSending(ChunkPos.containing(this.exitPortalLocation), chunkRadius);
/*     */     } 
/*     */   }
/*     */   
/*     */   private EnderDragon createNewDragon() {
/* 493 */     this.level.getChunkAt(new BlockPos(this.origin.getX(), 128 + this.origin.getY(), this.origin.getZ()));
/* 494 */     EnderDragon dragon = (EnderDragon)EntityTypes.ENDER_DRAGON.create((Level)this.level, EntitySpawnReason.EVENT);
/* 495 */     if (dragon != null) {
/* 496 */       dragon.setDragonFight(this);
/* 497 */       dragon.setFightOrigin(this.origin);
/* 498 */       dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/* 499 */       dragon.snapTo(this.origin.getX(), (128 + this.origin.getY()), this.origin.getZ(), this.level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 500 */       this.level.addFreshEntity((Entity)dragon);
/* 501 */       this.dragonUUID = dragon.getUUID();
/* 502 */       setDirty();
/*     */     } 
/* 504 */     return dragon;
/*     */   }
/*     */   
/*     */   public void updateDragon(EnderDragon dragon) {
/* 508 */     if (dragon.getUUID().equals(this.dragonUUID)) {
/* 509 */       this.dragonEvent.setProgress(dragon.getHealth() / dragon.getMaxHealth());
/* 510 */       this.ticksSinceDragonSeen = 0;
/* 511 */       if (dragon.hasCustomName())
/* 512 */         this.dragonEvent.setName(dragon.getDisplayName()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int aliveCrystals() {
/* 518 */     return this.aliveCrystals;
/*     */   }
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal crystal, DamageSource source) {
/* 522 */     if (this.respawnStage != null && this.respawnCrystals.stream().anyMatch(ref -> ref.matches((UniquelyIdentifyable)crystal))) {
/* 523 */       abortRespawnSequence();
/*     */     } else {
/* 525 */       updateCrystalCount();
/* 526 */       Entity dragon = this.level.getEntity(this.dragonUUID);
/* 527 */       if (dragon instanceof EnderDragon) {
/* 527 */         EnderDragon actuallyDragon = (EnderDragon)dragon;
/* 528 */         actuallyDragon.onCrystalDestroyed(this.level, crystal, crystal.blockPosition(), source);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void abortRespawnSequence() {
/* 534 */     LOGGER.debug("Aborting respawn sequence");
/* 535 */     this.respawnStage = null;
/* 536 */     this.respawnTime = 0;
/* 537 */     resetSpikeCrystals();
/* 538 */     spawnExitPortal(true);
/* 539 */     setDirty();
/*     */   }
/*     */   
/*     */   public boolean hasPreviouslyKilledDragon() {
/* 543 */     return this.hasPreviouslyKilledDragon;
/*     */   }
/*     */   
/*     */   public void tryRespawn() {
/* 547 */     if (this.dragonKilled && this.respawnStage == null) {
/* 548 */       BlockPos location = this.exitPortalLocation;
/* 549 */       if (location == null) {
/* 550 */         LOGGER.debug("Tried to respawn, but need to find the portal first.");
/* 551 */         BlockPattern.BlockPatternMatch match = findExitPortal();
/* 552 */         if (match == null) {
/* 553 */           LOGGER.debug("Couldn't find a portal, so we made one.");
/* 554 */           spawnExitPortal(true);
/*     */         } else {
/* 556 */           LOGGER.debug("Found the exit portal & saved its location for next time.");
/*     */         } 
/* 558 */         location = this.exitPortalLocation;
/*     */       } 
/* 561 */       List<EndCrystal> crystals = Lists.newArrayList();
/* 562 */       BlockPos center = location.above(1);
/* 563 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 564 */         List<EndCrystal> found = this.level.getEntitiesOfClass(EndCrystal.class, new AABB(center.relative(direction, 3)));
/* 565 */         if (found.isEmpty())
/*     */           return; 
/* 568 */         crystals.addAll(found);
/*     */       } 
/* 571 */       LOGGER.debug("Found all crystals, respawning dragon.");
/* 572 */       respawnDragon(crystals);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void respawnDragon(List<EndCrystal> crystals) {
/* 577 */     if (this.dragonKilled && this.respawnStage == null) {
/* 578 */       BlockPattern.BlockPatternMatch portal = findExitPortal();
/* 579 */       while (portal != null) {
/* 580 */         for (int x = 0; x < this.exitPortalPattern.getWidth(); x++) {
/* 581 */           for (int y = 0; y < this.exitPortalPattern.getHeight(); y++) {
/* 582 */             for (int z = 0; z < this.exitPortalPattern.getDepth(); z++) {
/* 583 */               BlockInWorld block = portal.getBlock(x, y, z);
/* 584 */               if (block.getState().is(Blocks.BEDROCK) || block.getState().is(Blocks.END_PORTAL))
/* 585 */                 this.level.setBlockAndUpdate(block.getPos(), Blocks.END_STONE.defaultBlockState()); 
/*     */             } 
/*     */           } 
/*     */         } 
/* 590 */         portal = findExitPortal();
/*     */       } 
/* 593 */       this.respawnStage = DragonRespawnStage.START;
/* 594 */       this.respawnTime = 0;
/* 595 */       spawnExitPortal(false);
/* 596 */       this.respawnCrystals = crystals.stream().map(EntityReference::of).toList();
/* 597 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetSpikeCrystals() {
/* 602 */     for (EndSpikeFeature.EndSpike spike : EndSpikeFeature.getSpikesForLevel((WorldGenLevel)this.level)) {
/* 603 */       List<EndCrystal> spikeCrystals = this.level.getEntitiesOfClass(EndCrystal.class, spike.getTopBoundingBox());
/* 604 */       for (EndCrystal crystal : spikeCrystals) {
/* 605 */         crystal.setInvulnerable(false);
/* 606 */         crystal.setBeamTarget(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public UUID dragonUUID() {
/* 612 */     return this.dragonUUID;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\end\EnderDragonFight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */