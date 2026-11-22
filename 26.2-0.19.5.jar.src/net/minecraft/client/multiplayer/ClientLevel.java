/*      */ package net.minecraft.client.multiplayer;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.Camera;
/*      */ import net.minecraft.client.ClientClockManager;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.color.block.BlockTintCache;
/*      */ import net.minecraft.client.color.block.BlockTintSource;
/*      */ import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
/*      */ import net.minecraft.client.particle.FireworkParticles;
/*      */ import net.minecraft.client.particle.Particle;
/*      */ import net.minecraft.client.particle.TerrainParticle;
/*      */ import net.minecraft.client.player.AbstractClientPlayer;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.renderer.BiomeColors;
/*      */ import net.minecraft.client.renderer.EndFlashState;
/*      */ import net.minecraft.client.renderer.LevelEventHandler;
/*      */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*      */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
/*      */ import net.minecraft.client.renderer.extract.LevelExtractor;
/*      */ import net.minecraft.client.resources.sounds.DirectionalSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Cursor3D;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.particles.SimpleParticleType;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.BlockDestructionProgress;
/*      */ import net.minecraft.server.level.ParticleStatus;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.Zone;
/*      */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.AmbientParticle;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.clock.ClockManager;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.component.FireworkExplosion;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.CardinalLighting;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ColorResolver;
/*      */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.Biomes;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CampfireBlock;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.level.entity.EntityTickList;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*      */ import net.minecraft.world.level.entity.TransientEntitySectionManager;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.ticks.BlackholeTickAccess;
/*      */ import net.minecraft.world.ticks.LevelTickAccess;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ClientLevel extends Level implements BlockAndTintGetter, CacheSlot.Cleaner<ClientLevel> {
/*  142 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  144 */   public static final Component DEFAULT_QUIT_MESSAGE = (Component)Component.translatable("multiplayer.status.quitting");
/*      */   
/*      */   private static final double FLUID_PARTICLE_SPAWN_OFFSET = 0.05D;
/*      */   
/*      */   private static final int NORMAL_LIGHT_UPDATES_PER_FRAME = 10;
/*      */   
/*      */   private static final int LIGHT_UPDATE_QUEUE_SIZE_THRESHOLD = 1000;
/*      */   
/*      */   private static final float RAIN_PARTICLES_PER_BLOCK = 0.225F;
/*      */   
/*      */   private static final int RAIN_RADIUS = 10;
/*      */   
/*  151 */   private final EntityTickList tickingEntities = new EntityTickList();
/*      */   
/*  152 */   private final TransientEntitySectionManager<Entity> entityStorage = new TransientEntitySectionManager(Entity.class, new EntityCallbacks());
/*      */   
/*      */   private final ClientPacketListener connection;
/*      */   
/*      */   private final LevelExtractor levelExtractor;
/*      */   
/*      */   private final LevelEventHandler levelEventHandler;
/*      */   
/*      */   private final ClientLevelData clientLevelData;
/*      */   
/*      */   private final TickRateManager tickRateManager;
/*      */   
/*      */   private final EndFlashState endFlashState;
/*      */   
/*  161 */   private final Minecraft minecraft = Minecraft.getInstance();
/*      */   
/*  162 */   private final List<AbstractClientPlayer> players = Lists.newArrayList();
/*      */   
/*  163 */   private final List<EnderDragonPart> dragonParts = Lists.newArrayList();
/*      */   
/*  164 */   private final Map<MapId, MapItemSavedData> mapData = Maps.newHashMap();
/*      */   
/*      */   private int skyFlashTime;
/*      */   
/*      */   private int rainSoundTime;
/*      */   
/*      */   private final Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;
/*      */   
/*      */   private final ClientChunkCache chunkSource;
/*      */   
/*      */   private final Deque<Runnable> lightUpdateQueue;
/*      */   
/*      */   private int serverSimulationDistance;
/*      */   
/*      */   private final BlockStatePredictionHandler blockStatePredictionHandler;
/*      */   
/*      */   private final Set<BlockEntity> globallyRenderedBlockEntities;
/*      */   
/*      */   private final ClientExplosionTracker explosionTracker;
/*      */   
/*      */   private final WorldBorder worldBorder;
/*      */   
/*      */   private final EnvironmentAttributeSystem environmentAttributes;
/*      */   
/*      */   private final Int2ObjectMap<BlockDestructionProgress> destroyingBlocks;
/*      */   
/*      */   private final Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;
/*      */   
/*      */   private final int seaLevel;
/*      */   
/*      */   public void handleBlockChangedAck(int sequence) {
/*  187 */     if (SharedConstants.DEBUG_BLOCK_BREAK)
/*  188 */       LOGGER.debug("ACK {}", Integer.valueOf(sequence)); 
/*  191 */     this.blockStatePredictionHandler.endPredictionsUpTo(sequence, this);
/*      */   }
/*      */   
/*      */   public void onBlockEntityAdded(BlockEntity blockEntity) {
/*  196 */     BlockEntityRenderer<BlockEntity, ?> renderer = this.minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
/*  197 */     if (renderer != null && renderer.shouldRenderOffScreen())
/*  198 */       this.globallyRenderedBlockEntities.add(blockEntity); 
/*      */   }
/*      */   
/*      */   public Set<BlockEntity> getGloballyRenderedBlockEntities() {
/*  203 */     return this.globallyRenderedBlockEntities;
/*      */   }
/*      */   
/*      */   public void setServerVerifiedBlockState(BlockPos pos, BlockState blockState, int updateFlag) {
/*  207 */     if (!this.blockStatePredictionHandler.updateKnownServerState(pos, blockState))
/*  208 */       super.setBlock(pos, blockState, updateFlag, 512); 
/*      */   }
/*      */   
/*      */   public void syncBlockState(BlockPos pos, BlockState state, Vec3 playerPos) {
/*  213 */     BlockState oldState = getBlockState(pos);
/*  214 */     if (oldState != state) {
/*  215 */       setBlock(pos, state, 19);
/*  219 */       LocalPlayer localPlayer = this.minecraft.player;
/*  220 */       if (playerPos != null && this == localPlayer.level() && localPlayer.isColliding(pos, state))
/*  221 */         localPlayer.absSnapTo(playerPos.x, playerPos.y, playerPos.z); 
/*      */     } 
/*      */   }
/*      */   
/*      */   BlockStatePredictionHandler getBlockStatePredictionHandler() {
/*  228 */     return this.blockStatePredictionHandler;
/*      */   }
/*      */   
/*      */   public boolean setBlock(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit) {
/*  233 */     if (this.blockStatePredictionHandler.isPredicting()) {
/*  234 */       BlockState oldState = getBlockState(pos);
/*  235 */       boolean success = super.setBlock(pos, blockState, updateFlags, updateLimit);
/*  236 */       if (success)
/*  237 */         this.blockStatePredictionHandler.retainKnownServerState(pos, oldState, this.minecraft.player); 
/*  239 */       return success;
/*      */     } 
/*  241 */     return super.setBlock(pos, blockState, updateFlags, updateLimit);
/*      */   }
/*      */   
/*  244 */   private static final Set<Item> MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT);
/*      */   
/*      */   public ClientLevel(ClientPacketListener connection, ClientLevelData levelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionType, int serverChunkRadius, int serverSimulationDistance, LevelExtractor levelExtractor, boolean isDebug, long biomeZoomSeed, int seaLevel) {
/*  247 */     super(levelData, dimension, (RegistryAccess)connection.registryAccess(), dimensionType, true, isDebug, biomeZoomSeed, 1000000);
/*      */     this.tintCaches = (Object2ObjectArrayMap<ColorResolver, BlockTintCache>)Util.make(new Object2ObjectArrayMap(3), cache -> {
/*      */           cache.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache(()));
/*      */           cache.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache(()));
/*      */           cache.put(BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER, new BlockTintCache(()));
/*      */           cache.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache(()));
/*      */         });
/*      */     this.lightUpdateQueue = Queues.newArrayDeque();
/*      */     this.blockStatePredictionHandler = new BlockStatePredictionHandler();
/*      */     this.globallyRenderedBlockEntities = (Set<BlockEntity>)new ReferenceOpenHashSet();
/*      */     this.explosionTracker = new ClientExplosionTracker();
/*      */     this.worldBorder = new WorldBorder();
/*      */     this.destroyingBlocks = (Int2ObjectMap<BlockDestructionProgress>)new Int2ObjectOpenHashMap();
/*      */     this.destructionProgress = (Long2ObjectMap<SortedSet<BlockDestructionProgress>>)new Long2ObjectOpenHashMap();
/*  248 */     this.connection = connection;
/*  249 */     this.chunkSource = new ClientChunkCache(this, serverChunkRadius);
/*  250 */     this.tickRateManager = new TickRateManager();
/*  251 */     this.clientLevelData = levelData;
/*  252 */     this.levelExtractor = levelExtractor;
/*  253 */     this.seaLevel = seaLevel;
/*  254 */     this.levelEventHandler = new LevelEventHandler(this.minecraft, this);
/*  255 */     this.endFlashState = ((DimensionType)dimensionType.value()).hasEndFlashes() ? new EndFlashState() : null;
/*  256 */     setRespawnData(LevelData.RespawnData.of(dimension, new BlockPos(8, 64, 8), 0.0F, 0.0F));
/*  258 */     this.serverSimulationDistance = serverSimulationDistance;
/*  260 */     this.environmentAttributes = addEnvironmentAttributeLayers(EnvironmentAttributeSystem.builder()).build();
/*  262 */     updateSkyBrightness();
/*      */   }
/*      */   
/*      */   private EnvironmentAttributeSystem.Builder addEnvironmentAttributeLayers(EnvironmentAttributeSystem.Builder environmentAttributes) {
/*  266 */     environmentAttributes.addDefaultLayers(this);
/*  268 */     int flashColor = ARGB.color(204, 204, 255);
/*  269 */     environmentAttributes.addTimeBasedLayer(EnvironmentAttributes.SKY_COLOR, (skyColor, cacheTickId) -> (getSkyFlashTime() > 0) ? Integer.valueOf(ARGB.srgbLerp(0.22F, skyColor.intValue(), flashColor)) : skyColor);
/*  275 */     environmentAttributes.addTimeBasedLayer(EnvironmentAttributes.SKY_LIGHT_FACTOR, (skyFactor, cacheTickId) -> Float.valueOf((getSkyFlashTime() > 0) ? 1.0F : skyFactor.floatValue()));
/*  279 */     return environmentAttributes;
/*      */   }
/*      */   
/*      */   public void queueLightUpdate(Runnable update) {
/*  283 */     this.lightUpdateQueue.add(update);
/*      */   }
/*      */   
/*      */   public void pollLightUpdates() {
/*  287 */     int size = this.lightUpdateQueue.size();
/*  288 */     int lightUpdatesPerFrame = (size < 1000) ? Math.max(10, size / 10) : size;
/*  289 */     for (int i = 0; i < lightUpdatesPerFrame; ) {
/*  290 */       Runnable update = this.lightUpdateQueue.poll();
/*  291 */       if (update != null) {
/*  292 */         update.run();
/*      */         i++;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public EndFlashState endFlashState() {
/*  300 */     return this.endFlashState;
/*      */   }
/*      */   
/*      */   public void tick(BooleanSupplier haveTime) {
/*  304 */     updateSkyBrightness();
/*  305 */     if (tickRateManager().runsNormally()) {
/*  306 */       getWorldBorder().tick();
/*  307 */       tickTime();
/*  308 */       tickWeatherEffects();
/*  309 */       removeBlockBreakingProgress();
/*      */     } 
/*  312 */     if (this.skyFlashTime > 0)
/*  313 */       setSkyFlashTime(this.skyFlashTime - 1); 
/*  316 */     if (this.endFlashState != null) {
/*  317 */       this.endFlashState.tick(getDefaultClockTime());
/*  318 */       if (this.endFlashState.flashStartedThisTick() && !(this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.WinScreen))
/*  319 */         this.minecraft.getSoundManager().playDelayed((SoundInstance)new DirectionalSoundInstance(SoundEvents.WEATHER_END_FLASH, SoundSource.WEATHER, this.random, this.minecraft.gameRenderer.mainCamera(), this.endFlashState.getXAngle(), this.endFlashState.getYAngle()), 30); 
/*      */     } 
/*  323 */     this.explosionTracker.tick(this);
/*  325 */     Zone ignored = Profiler.get().zone("blocks");
/*      */     try {
/*  326 */       this.chunkSource.tick(haveTime, true);
/*  327 */       if (ignored != null)
/*  327 */         ignored.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (ignored != null)
/*      */         try {
/*      */           ignored.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*  329 */     JvmProfiler.INSTANCE.onClientTick(this.minecraft.getFps());
/*  330 */     environmentAttributes().invalidateTickCache();
/*      */   }
/*      */   
/*      */   public void tickWeatherEffects() {
/*  334 */     ParticleStatus particleStatus = (ParticleStatus)this.minecraft.options.particles().get();
/*  335 */     int weatherRadius = ((Integer)this.minecraft.options.weatherRadius().get()).intValue();
/*  336 */     float rainLevel = getRainLevel(1.0F);
/*  337 */     if (rainLevel <= 0.0F)
/*      */       return; 
/*  341 */     RandomSource random = RandomSource.createThreadLocalInstance(getGameTime() * 312987231L);
/*  342 */     BlockPos cameraPosition = BlockPos.containing((Position)this.minecraft.gameRenderer.mainCamera().position());
/*  344 */     BlockPos rainParticlePosition = null;
/*  346 */     int weatherDiameter = 2 * weatherRadius + 1;
/*  347 */     int weatherArea = weatherDiameter * weatherDiameter;
/*  348 */     int rainParticles = (int)(0.225F * weatherArea * rainLevel * rainLevel) / ((particleStatus == ParticleStatus.DECREASED) ? 2 : 1);
/*  349 */     for (int ii = 0; ii < rainParticles; ii++) {
/*  350 */       int x = random.nextInt(weatherDiameter) - weatherRadius;
/*  351 */       int z = random.nextInt(weatherDiameter) - weatherRadius;
/*  354 */       BlockPos heightmapPosition = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPosition.offset(x, 0, z));
/*  355 */       if (heightmapPosition.getY() > getMinY() && heightmapPosition.getY() <= cameraPosition.getY() + 10 && heightmapPosition.getY() >= cameraPosition.getY() - 10)
/*  359 */         if (getPrecipitationAt(heightmapPosition) == Biome.Precipitation.RAIN) {
/*  360 */           rainParticlePosition = heightmapPosition.below();
/*  362 */           if (particleStatus == ParticleStatus.MINIMAL)
/*      */             break; 
/*  367 */           double blockX = random.nextDouble();
/*  368 */           double blockZ = random.nextDouble();
/*  370 */           BlockState block = getBlockState(rainParticlePosition);
/*  371 */           FluidState fluid = getFluidState(rainParticlePosition);
/*  372 */           VoxelShape blockShape = block.getCollisionShape((BlockGetter)this, rainParticlePosition);
/*  374 */           double blockTop = blockShape.max(Direction.Axis.Y, blockX, blockZ);
/*  375 */           double fluidTop = fluid.getHeight((BlockGetter)this, rainParticlePosition);
/*  376 */           double particleY = Math.max(blockTop, fluidTop);
/*  378 */           SimpleParticleType simpleParticleType = (fluid.is(FluidTags.LAVA) || block.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(block)) ? ParticleTypes.SMOKE : ParticleTypes.RAIN;
/*  379 */           addParticle((ParticleOptions)simpleParticleType, rainParticlePosition.getX() + blockX, rainParticlePosition.getY() + particleY, rainParticlePosition.getZ() + blockZ, 0.0D, 0.0D, 0.0D);
/*      */         }  
/*      */     } 
/*  383 */     if (rainParticlePosition != null && random.nextInt(3) < this.rainSoundTime++) {
/*  384 */       this.rainSoundTime = 0;
/*  385 */       if (rainParticlePosition.getY() > cameraPosition.getY() + 1 && getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, cameraPosition).getY() > Mth.floor(cameraPosition.getY())) {
/*  387 */         playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
/*      */       } else {
/*  389 */         playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Biome.Precipitation getPrecipitationAt(BlockPos pos) {
/*  395 */     if (!this.chunkSource.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
/*  396 */       return Biome.Precipitation.NONE; 
/*  398 */     Biome biome = (Biome)getBiome(pos).value();
/*  399 */     return biome.getPrecipitationAt(pos, getSeaLevel());
/*      */   }
/*      */   
/*      */   private void removeBlockBreakingProgress() {
/*  403 */     long gameTime = getGameTime();
/*  404 */     if (gameTime % 20L != 0L)
/*      */       return; 
/*  408 */     ObjectIterator<BlockDestructionProgress> objectIterator = this.destroyingBlocks.values().iterator();
/*  409 */     while (objectIterator.hasNext()) {
/*  410 */       BlockDestructionProgress block = objectIterator.next();
/*  412 */       long updatedRenderTick = block.getUpdatedRenderTick();
/*  414 */       if (gameTime - updatedRenderTick > 400L) {
/*  415 */         objectIterator.remove();
/*  416 */         removeProgress(block);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void removeProgress(BlockDestructionProgress block) {
/*  422 */     long pos = block.getPos().asLong();
/*  423 */     Set<BlockDestructionProgress> progresses = (Set<BlockDestructionProgress>)this.destructionProgress.get(pos);
/*  424 */     progresses.remove(block);
/*  425 */     if (progresses.isEmpty())
/*  426 */       this.destructionProgress.remove(pos); 
/*      */   }
/*      */   
/*      */   public Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress() {
/*  431 */     return this.destructionProgress;
/*      */   }
/*      */   
/*      */   private void tickTime() {
/*  435 */     long gameTime = this.clientLevelData.getGameTime() + 1L;
/*  436 */     this.clientLevelData.setGameTime(gameTime);
/*  437 */     clockManager().tick(gameTime);
/*      */   }
/*      */   
/*      */   public void setTimeFromServer(long gameTime) {
/*  441 */     this.clientLevelData.setGameTime(gameTime);
/*      */   }
/*      */   
/*      */   public Iterable<Entity> entitiesForRendering() {
/*  445 */     return getEntities().getAll();
/*      */   }
/*      */   
/*      */   public void tickEntities() {
/*  449 */     this.tickingEntities.forEach(entity -> {
/*      */           if (entity.isRemoved() || entity.isPassenger() || this.tickRateManager.isEntityFrozen(entity))
/*      */             return; 
/*      */           guardEntityTick(this::tickNonPassenger, entity);
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean isTickingEntity(Entity entity) {
/*  458 */     return this.tickingEntities.contains(entity);
/*      */   }
/*      */   
/*      */   public boolean shouldTickDeath(Entity entity) {
/*  463 */     return (entity.chunkPosition().getChessboardDistance(this.minecraft.player.chunkPosition()) <= this.serverSimulationDistance);
/*      */   }
/*      */   
/*      */   public void tickNonPassenger(Entity entity) {
/*  467 */     entity.setOldPosAndRot();
/*  469 */     entity.tickCount++;
/*  470 */     Objects.requireNonNull(entity.typeHolder());
/*  470 */     Profiler.get().push(entity.typeHolder()::getRegisteredName);
/*  471 */     entity.tick();
/*  472 */     Profiler.get().pop();
/*  474 */     for (Entity passenger : entity.getPassengers())
/*  475 */       tickPassenger(entity, passenger); 
/*      */   }
/*      */   
/*      */   private void tickPassenger(Entity vehicle, Entity entity) {
/*  480 */     if (entity.isRemoved() || entity.getVehicle() != vehicle) {
/*  481 */       entity.stopRiding();
/*      */       return;
/*      */     } 
/*  485 */     if (!(entity instanceof net.minecraft.world.entity.player.Player) && !this.tickingEntities.contains(entity))
/*      */       return; 
/*  489 */     entity.setOldPosAndRot();
/*  491 */     entity.tickCount++;
/*  492 */     entity.rideTick();
/*  494 */     for (Entity passenger : entity.getPassengers())
/*  495 */       tickPassenger(entity, passenger); 
/*      */   }
/*      */   
/*      */   public void update() {
/*  500 */     ProfilerFiller profiler = Profiler.get();
/*  502 */     profiler.push("populateLightUpdates");
/*  503 */     pollLightUpdates();
/*  504 */     profiler.popPush("runLightUpdates");
/*  505 */     getChunkSource().getLightEngine().runLightUpdates();
/*  506 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public void unload(LevelChunk levelChunk) {
/*  510 */     levelChunk.clearAllBlockEntities();
/*  511 */     this.chunkSource.getLightEngine().setLightEnabled(levelChunk.getPos(), false);
/*  512 */     this.entityStorage.stopTicking(levelChunk.getPos());
/*      */   }
/*      */   
/*      */   public void onChunkLoaded(ChunkPos pos) {
/*  516 */     this.tintCaches.forEach((resolver, cache) -> cache.invalidateForChunk(pos.x(), pos.z()));
/*  517 */     this.entityStorage.startTicking(pos);
/*      */   }
/*      */   
/*      */   public void clearTintCaches() {
/*  521 */     this.tintCaches.forEach((resolver, cache) -> cache.invalidateAll());
/*      */   }
/*      */   
/*      */   public boolean hasChunk(int chunkX, int chunkZ) {
/*  526 */     return true;
/*      */   }
/*      */   
/*      */   public int getEntityCount() {
/*  530 */     return this.entityStorage.count();
/*      */   }
/*      */   
/*      */   public void addEntity(Entity entity) {
/*  534 */     removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
/*  535 */     this.entityStorage.addEntity((EntityAccess)entity);
/*      */   }
/*      */   
/*      */   public void removeEntity(int id, Entity.RemovalReason reason) {
/*  539 */     Entity entity = (Entity)getEntities().get(id);
/*  540 */     if (entity != null) {
/*  541 */       entity.setRemoved(reason);
/*  542 */       entity.onClientRemoval();
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<Entity> getPushableEntities(Entity pusher, AABB boundingBox) {
/*  549 */     LocalPlayer player = this.minecraft.player;
/*  550 */     if (player != null && player != pusher && player
/*      */       
/*  552 */       .getBoundingBox().intersects(boundingBox) && 
/*  553 */       EntitySelector.pushableBy(pusher).test(player))
/*  555 */       return (List)List.of(player); 
/*  557 */     return List.of();
/*      */   }
/*      */   
/*      */   public Entity getEntity(int id) {
/*  562 */     return (Entity)getEntities().get(id);
/*      */   }
/*      */   
/*      */   public void disconnect(Component message) {
/*  566 */     this.connection.getConnection().disconnect(message);
/*  567 */     this.minecraft.getPlayerSocialManager().getPresenceHandler().tryUpdatePresence();
/*      */   }
/*      */   
/*      */   public void animateTick(int xt, int yt, int zt) {
/*  571 */     int r = 32;
/*  572 */     RandomSource animateRandom = RandomSource.createThreadLocalInstance();
/*  574 */     Block markerParticleTarget = getMarkerParticleTarget();
/*  575 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  576 */     for (int i = 0; i < 667; i++) {
/*  577 */       doAnimateTick(xt, yt, zt, 16, animateRandom, markerParticleTarget, pos);
/*  578 */       doAnimateTick(xt, yt, zt, 32, animateRandom, markerParticleTarget, pos);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Block getMarkerParticleTarget() {
/*  583 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
/*  584 */       ItemStack carriedItemStack = this.minecraft.player.getMainHandItem();
/*  585 */       Item carriedItem = carriedItemStack.getItem();
/*  586 */       if (MARKER_PARTICLE_ITEMS.contains(carriedItem) && carriedItem instanceof BlockItem) {
/*  586 */         BlockItem blockItem = (BlockItem)carriedItem;
/*  587 */         return blockItem.getBlock();
/*      */       } 
/*      */     } 
/*  591 */     return null;
/*      */   }
/*      */   
/*      */   public void doAnimateTick(int xt, int yt, int zt, int r, RandomSource animateRandom, Block markerParticleTarget, BlockPos.MutableBlockPos pos) {
/*  595 */     int x = xt + this.random.nextInt(r) - this.random.nextInt(r);
/*  596 */     int y = yt + this.random.nextInt(r) - this.random.nextInt(r);
/*  597 */     int z = zt + this.random.nextInt(r) - this.random.nextInt(r);
/*  599 */     pos.set(x, y, z);
/*  600 */     BlockState state = getBlockState((BlockPos)pos);
/*  601 */     state.getBlock().animateTick(state, this, (BlockPos)pos, animateRandom);
/*  603 */     FluidState fluidState = getFluidState((BlockPos)pos);
/*  605 */     if (!fluidState.isEmpty()) {
/*  606 */       fluidState.animateTick(this, (BlockPos)pos, animateRandom);
/*  608 */       ParticleOptions dripParticle = fluidState.getDripParticle();
/*  609 */       if (dripParticle != null && this.random.nextInt(10) == 0) {
/*  610 */         boolean hasWatertightBottom = state.isFaceSturdy((BlockGetter)this, (BlockPos)pos, Direction.DOWN);
/*  611 */         BlockPos below = pos.below();
/*  612 */         trySpawnDripParticles(below, getBlockState(below), dripParticle, hasWatertightBottom);
/*      */       } 
/*      */     } 
/*  616 */     if (markerParticleTarget == state.getBlock())
/*  617 */       addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK_MARKER, state), x + 0.5D, y + 0.5D, z + 0.5D, 0.0D, 0.0D, 0.0D); 
/*  620 */     if (!state.isCollisionShapeFullBlock((BlockGetter)this, (BlockPos)pos))
/*  621 */       for (AmbientParticle particle : environmentAttributes().getValue(EnvironmentAttributes.AMBIENT_PARTICLES, (BlockPos)pos)) {
/*  622 */         if (particle.canSpawn(this.random))
/*  623 */           addParticle(particle.particle(), pos.getX() + this.random.nextDouble(), pos.getY() + this.random.nextDouble(), pos.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*      */       }  
/*      */   }
/*      */   
/*      */   private void trySpawnDripParticles(BlockPos pos, BlockState state, ParticleOptions dripParticle, boolean isTopSolid) {
/*  630 */     if (!state.getFluidState().isEmpty())
/*      */       return; 
/*  634 */     VoxelShape collisionShape = state.getCollisionShape((BlockGetter)this, pos);
/*  635 */     double topSideHeight = collisionShape.max(Direction.Axis.Y);
/*  636 */     if (topSideHeight < 1.0D) {
/*  637 */       if (isTopSolid)
/*  638 */         spawnFluidParticle(pos.getX(), (pos.getX() + 1), pos.getZ(), (pos.getZ() + 1), (pos.getY() + 1) - 0.05D, dripParticle); 
/*  640 */     } else if (!state.is(BlockTags.IMPERMEABLE)) {
/*  641 */       double bottomSideHeight = collisionShape.min(Direction.Axis.Y);
/*  642 */       if (bottomSideHeight > 0.0D) {
/*  643 */         spawnParticle(pos, dripParticle, collisionShape, pos.getY() + bottomSideHeight - 0.05D);
/*      */       } else {
/*  645 */         BlockPos below = pos.below();
/*  646 */         BlockState belowState = getBlockState(below);
/*  647 */         VoxelShape belowShape = belowState.getCollisionShape((BlockGetter)this, below);
/*  648 */         double belowTopSideHeight = belowShape.max(Direction.Axis.Y);
/*  649 */         if (belowTopSideHeight < 1.0D && belowState.getFluidState().isEmpty())
/*  650 */           spawnParticle(pos, dripParticle, collisionShape, pos.getY() - 0.05D); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void spawnParticle(BlockPos pos, ParticleOptions dripParticle, VoxelShape dripShape, double height) {
/*  657 */     spawnFluidParticle(pos.getX() + dripShape.min(Direction.Axis.X), pos
/*  658 */         .getX() + dripShape.max(Direction.Axis.X), pos
/*  659 */         .getZ() + dripShape.min(Direction.Axis.Z), pos
/*  660 */         .getZ() + dripShape.max(Direction.Axis.Z), height, dripParticle);
/*      */   }
/*      */   
/*      */   private void spawnFluidParticle(double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) {
/*  666 */     addParticle(dripParticle, Mth.lerp(this.random.nextDouble(), x1, x2), y, Mth.lerp(this.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
/*      */   }
/*      */   
/*      */   public CrashReportCategory fillReportDetails(CrashReport report) {
/*  671 */     CrashReportCategory category = super.fillReportDetails(report);
/*  673 */     category.setDetail("Server brand", () -> this.minecraft.player.connection.serverBrand());
/*  674 */     category.setDetail("Server type", () -> (this.minecraft.getSingleplayerServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server");
/*  675 */     category.setDetail("Tracked entity count", () -> String.valueOf(getEntityCount()));
/*  676 */     category.setDetail("Client weather", () -> String.format(Locale.ROOT, "Raining: %b, thundering: %b", new Object[] { Boolean.valueOf(isRaining()), Boolean.valueOf(isThundering()) }));
/*  678 */     return category;
/*      */   }
/*      */   
/*      */   public void playSeededSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/*  683 */     if (except == this.minecraft.player)
/*  684 */       playSound(x, y, z, (SoundEvent)sound.value(), source, volume, pitch, false, seed); 
/*      */   }
/*      */   
/*      */   public void playSeededSound(Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/*  690 */     if (except == this.minecraft.player)
/*  691 */       this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)sound.value(), source, volume, pitch, sourceEntity, seed)); 
/*      */   }
/*      */   
/*      */   public void playLocalSound(Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {
/*  697 */     this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance(sound, source, volume, pitch, sourceEntity, this.random.nextLong()));
/*      */   }
/*      */   
/*      */   public void playPlayerSound(SoundEvent sound, SoundSource source, float volume, float pitch) {
/*  702 */     if (this.minecraft.player != null)
/*  703 */       this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance(sound, source, volume, pitch, (Entity)this.minecraft.player, this.random.nextLong())); 
/*      */   }
/*      */   
/*      */   public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
/*  709 */     playSound(x, y, z, sound, source, volume, pitch, distanceDelay, this.random.nextLong());
/*      */   }
/*      */   
/*      */   private void playSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay, long seed) {
/*  713 */     double distanceToSqr = this.minecraft.gameRenderer.mainCamera().position().distanceToSqr(x, y, z);
/*  714 */     SimpleSoundInstance instance = new SimpleSoundInstance(sound, source, volume, pitch, RandomSource.create(seed), x, y, z);
/*  716 */     if (distanceDelay && distanceToSqr > 100.0D) {
/*  718 */       double delayInSeconds = Math.sqrt(distanceToSqr) / 40.0D;
/*  719 */       this.minecraft.getSoundManager().playDelayed((SoundInstance)instance, (int)(delayInSeconds * 20.0D));
/*      */     } else {
/*  721 */       this.minecraft.getSoundManager().play((SoundInstance)instance);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void createFireworks(double x, double y, double z, double xd, double yd, double zd, List<FireworkExplosion> explosions) {
/*  727 */     if (explosions.isEmpty()) {
/*  729 */       for (int i = 0; i < this.random.nextInt(3) + 2; i++)
/*  730 */         addParticle((ParticleOptions)ParticleTypes.POOF, x, y, z, this.random.nextGaussian() * 0.05D, 0.005D, this.random.nextGaussian() * 0.05D); 
/*      */     } else {
/*  733 */       this.minecraft.particleEngine.add((Particle)new FireworkParticles.Starter(this, x, y, z, xd, yd, zd, this.minecraft.particleEngine, explosions));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendPacketToServer(Packet<?> packet) {
/*  739 */     this.connection.send(packet);
/*      */   }
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/*  744 */     return this.worldBorder;
/*      */   }
/*      */   
/*      */   public RecipeAccess recipeAccess() {
/*  749 */     return this.connection.recipes();
/*      */   }
/*      */   
/*      */   public TickRateManager tickRateManager() {
/*  754 */     return this.tickRateManager;
/*      */   }
/*      */   
/*      */   public ClientClockManager clockManager() {
/*  759 */     return this.connection.clockManager();
/*      */   }
/*      */   
/*      */   public EnvironmentAttributeSystem environmentAttributes() {
/*  764 */     return this.environmentAttributes;
/*      */   }
/*      */   
/*      */   public LevelTickAccess<Block> getBlockTicks() {
/*  769 */     return BlackholeTickAccess.emptyLevelList();
/*      */   }
/*      */   
/*      */   public LevelTickAccess<Fluid> getFluidTicks() {
/*  774 */     return BlackholeTickAccess.emptyLevelList();
/*      */   }
/*      */   
/*      */   public ClientChunkCache getChunkSource() {
/*  779 */     return this.chunkSource;
/*      */   }
/*      */   
/*      */   public MapItemSavedData getMapData(MapId id) {
/*  784 */     return this.mapData.get(id);
/*      */   }
/*      */   
/*      */   public void overrideMapData(MapId id, MapItemSavedData data) {
/*  788 */     this.mapData.put(id, data);
/*      */   }
/*      */   
/*      */   public Scoreboard getScoreboard() {
/*  793 */     return this.connection.scoreboard();
/*      */   }
/*      */   
/*      */   public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, int updateFlags) {
/*  798 */     this.levelExtractor.blockChanged(pos, updateFlags);
/*      */   }
/*      */   
/*      */   public void setBlocksDirty(BlockPos pos, BlockState oldState, BlockState newState) {
/*  803 */     this.levelExtractor.setBlockDirty(pos, oldState, newState);
/*      */   }
/*      */   
/*      */   public void setSectionDirtyWithNeighbors(int chunkX, int chunkY, int chunkZ) {
/*  807 */     this.levelExtractor.setSectionDirtyWithNeighbors(chunkX, chunkY, chunkZ);
/*      */   }
/*      */   
/*      */   public void setSectionRangeDirty(int minSectionX, int minSectionY, int minSectionZ, int maxSectionX, int maxSectionY, int maxSectionZ) {
/*  811 */     this.levelExtractor.setSectionRangeDirty(minSectionX, minSectionY, minSectionZ, maxSectionX, maxSectionY, maxSectionZ);
/*      */   }
/*      */   
/*      */   public void destroyBlockProgress(int id, BlockPos pos, int progress) {
/*  816 */     if (progress < 0 || progress >= 10) {
/*  817 */       BlockDestructionProgress removed = (BlockDestructionProgress)this.destroyingBlocks.remove(id);
/*  818 */       if (removed != null)
/*  819 */         removeProgress(removed); 
/*      */     } else {
/*  822 */       BlockDestructionProgress entry = (BlockDestructionProgress)this.destroyingBlocks.get(id);
/*  823 */       if (entry != null)
/*  824 */         removeProgress(entry); 
/*  827 */       if (entry == null || entry.getPos().getX() != pos.getX() || entry.getPos().getY() != pos.getY() || entry.getPos().getZ() != pos.getZ()) {
/*  828 */         entry = new BlockDestructionProgress(id, pos);
/*  829 */         this.destroyingBlocks.put(id, entry);
/*      */       } 
/*  832 */       entry.setProgress(progress);
/*  833 */       entry.updateTick(getGameTime());
/*  834 */       ((SortedSet<BlockDestructionProgress>)this.destructionProgress.computeIfAbsent(entry.getPos().asLong(), k -> Sets.newTreeSet())).add(entry);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void globalLevelEvent(int type, BlockPos pos, int data) {
/*  840 */     this.levelEventHandler.globalLevelEvent(type, pos, data);
/*      */   }
/*      */   
/*      */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {
/*      */     try {
/*  846 */       this.levelEventHandler.levelEvent(type, pos, data);
/*  847 */     } catch (Throwable t) {
/*  848 */       CrashReport report = CrashReport.forThrowable(t, "Playing level event");
/*  849 */       CrashReportCategory category = report.addCategory("Level event being played");
/*  851 */       category.setDetail("Block coordinates", CrashReportCategory.formatLocation((LevelHeightAccessor)this, pos));
/*  852 */       category.setDetail("Event source", source);
/*  853 */       category.setDetail("Event type", Integer.valueOf(type));
/*  854 */       category.setDetail("Event data", Integer.valueOf(data));
/*  856 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
/*  862 */     doAddParticle(particle, particle.getType().getOverrideLimiter(), false, x, y, z, xd, yd, zd);
/*      */   }
/*      */   
/*      */   public void addParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, double xd, double yd, double zd) {
/*  867 */     doAddParticle(particle, (particle.getType().getOverrideLimiter() || overrideLimiter), alwaysShow, x, y, z, xd, yd, zd);
/*      */   }
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
/*  872 */     doAddParticle(particle, false, true, x, y, z, xd, yd, zd);
/*      */   }
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, double xd, double yd, double zd) {
/*  877 */     doAddParticle(particle, (particle.getType().getOverrideLimiter() || overrideLimiter), true, x, y, z, xd, yd, zd);
/*      */   }
/*      */   
/*      */   private void doAddParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShowParticles, double x, double y, double z, double xd, double yd, double zd) {
/*      */     try {
/*  882 */       Camera camera = this.minecraft.gameRenderer.mainCamera();
/*  883 */       ParticleStatus particleLevel = calculateParticleLevel(alwaysShowParticles);
/*  885 */       if (overrideLimiter) {
/*  886 */         this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
/*      */         return;
/*      */       } 
/*  890 */       if (camera.position().distanceToSqr(x, y, z) > 1024.0D)
/*      */         return; 
/*  894 */       if (particleLevel == ParticleStatus.MINIMAL)
/*      */         return; 
/*  899 */       this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
/*  900 */     } catch (Throwable t) {
/*  901 */       CrashReport report = CrashReport.forThrowable(t, "Exception while adding particle");
/*  902 */       CrashReportCategory category = report.addCategory("Particle being added");
/*  904 */       category.setDetail("ID", BuiltInRegistries.PARTICLE_TYPE.getKey(particle.getType()));
/*  905 */       category.setDetail("Parameters", () -> ParticleTypes.CODEC.encodeStart((DynamicOps)registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE), particle).toString());
/*  906 */       category.setDetail("Position", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/*  908 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ParticleStatus calculateParticleLevel(boolean alwaysShowParticles) {
/*  913 */     ParticleStatus particleLevel = (ParticleStatus)this.minecraft.options.particles().get();
/*  915 */     if (alwaysShowParticles && particleLevel == ParticleStatus.MINIMAL)
/*  917 */       if (this.random.nextInt(10) == 0)
/*  918 */         particleLevel = ParticleStatus.DECREASED;  
/*  922 */     if (particleLevel == ParticleStatus.DECREASED)
/*  924 */       if (this.random.nextInt(3) == 0)
/*  925 */         particleLevel = ParticleStatus.MINIMAL;  
/*  929 */     return particleLevel;
/*      */   }
/*      */   
/*      */   public List<AbstractClientPlayer> players() {
/*  934 */     return this.players;
/*      */   }
/*      */   
/*      */   public List<EnderDragonPart> dragonParts() {
/*  939 */     return this.dragonParts;
/*      */   }
/*      */   
/*      */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
/*  944 */     return (Holder<Biome>)registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
/*      */   }
/*      */   
/*      */   private int getSkyFlashTime() {
/*  948 */     return ((Boolean)this.minecraft.options.hideLightningFlash().get()).booleanValue() ? 0 : this.skyFlashTime;
/*      */   }
/*      */   
/*      */   public void setSkyFlashTime(int skyFlashTime) {
/*  953 */     this.skyFlashTime = skyFlashTime;
/*      */   }
/*      */   
/*      */   public CardinalLighting cardinalLighting() {
/*  958 */     return dimensionType().cardinalLightType().get();
/*      */   }
/*      */   
/*      */   public int getBlockTint(BlockPos pos, ColorResolver resolver) {
/*  963 */     BlockTintCache cache = (BlockTintCache)this.tintCaches.get(resolver);
/*  964 */     return cache.getColor(pos);
/*      */   }
/*      */   
/*      */   public int calculateBlockTint(BlockPos pos, ColorResolver colorResolver) {
/*  968 */     int dist = ((Integer)(Minecraft.getInstance()).options.biomeBlendRadius().get()).intValue();
/*  969 */     if (dist == 0)
/*  970 */       return colorResolver.getColor((Biome)getBiome(pos).value(), pos.getX(), pos.getZ()); 
/*  973 */     int count = (dist * 2 + 1) * (dist * 2 + 1);
/*  974 */     int totalRed = 0;
/*  975 */     int totalGreen = 0;
/*  976 */     int totalBlue = 0;
/*  978 */     Cursor3D cursor = new Cursor3D(pos.getX() - dist, pos.getY(), pos.getZ() - dist, pos.getX() + dist, pos.getY(), pos.getZ() + dist);
/*  979 */     BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/*  980 */     while (cursor.advance()) {
/*  981 */       nextPos.set(cursor.nextX(), cursor.nextY(), cursor.nextZ());
/*  982 */       int color = colorResolver.getColor((Biome)getBiome((BlockPos)nextPos).value(), nextPos.getX(), nextPos.getZ());
/*  983 */       totalRed += ARGB.red(color);
/*  984 */       totalGreen += ARGB.green(color);
/*  985 */       totalBlue += ARGB.blue(color);
/*      */     } 
/*  988 */     return ARGB.color(totalRed / count, totalGreen / count, totalBlue / count);
/*      */   }
/*      */   
/*      */   public void setRespawnData(LevelData.RespawnData respawnData) {
/*  993 */     this.levelData.setSpawn(getWorldBorderAdjustedRespawnData(respawnData));
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/*  998 */     return this.levelData.getRespawnData();
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1003 */     return "ClientLevel";
/*      */   }
/*      */   
/*      */   public ClientLevelData getLevelData() {
/* 1008 */     return this.clientLevelData;
/*      */   }
/*      */   
/*      */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context) {}
/*      */   
/*      */   protected Map<MapId, MapItemSavedData> getAllMapData() {
/* 1017 */     return (Map<MapId, MapItemSavedData>)ImmutableMap.copyOf(this.mapData);
/*      */   }
/*      */   
/*      */   protected void addMapData(Map<MapId, MapItemSavedData> mapData) {
/* 1021 */     this.mapData.putAll(mapData);
/*      */   }
/*      */   
/*      */   public static class ClientLevelData implements WritableLevelData {
/*      */     private final boolean hardcore;
/*      */     
/*      */     private final boolean isFlat;
/*      */     
/*      */     private LevelData.RespawnData respawnData;
/*      */     
/*      */     private long gameTime;
/*      */     
/*      */     private Difficulty difficulty;
/*      */     
/*      */     private boolean difficultyLocked;
/*      */     
/*      */     public ClientLevelData(Difficulty difficulty, boolean hardcore, boolean isFlat) {
/* 1035 */       this.difficulty = difficulty;
/* 1036 */       this.hardcore = hardcore;
/* 1037 */       this.isFlat = isFlat;
/*      */     }
/*      */     
/*      */     public LevelData.RespawnData getRespawnData() {
/* 1042 */       return this.respawnData;
/*      */     }
/*      */     
/*      */     public long getGameTime() {
/* 1047 */       return this.gameTime;
/*      */     }
/*      */     
/*      */     public void setGameTime(long time) {
/* 1051 */       this.gameTime = time;
/*      */     }
/*      */     
/*      */     public void setSpawn(LevelData.RespawnData respawnData) {
/* 1056 */       this.respawnData = respawnData;
/*      */     }
/*      */     
/*      */     public boolean isHardcore() {
/* 1061 */       return this.hardcore;
/*      */     }
/*      */     
/*      */     public Difficulty getDifficulty() {
/* 1066 */       return this.difficulty;
/*      */     }
/*      */     
/*      */     public boolean isDifficultyLocked() {
/* 1071 */       return this.difficultyLocked;
/*      */     }
/*      */     
/*      */     public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 1076 */       super.fillCrashReportCategory(category, levelHeightAccessor);
/*      */     }
/*      */     
/*      */     public void setDifficulty(Difficulty difficulty) {
/* 1080 */       this.difficulty = difficulty;
/*      */     }
/*      */     
/*      */     public void setDifficultyLocked(boolean locked) {
/* 1084 */       this.difficultyLocked = locked;
/*      */     }
/*      */     
/*      */     public double getHorizonHeight(LevelHeightAccessor level) {
/* 1088 */       if (this.isFlat)
/* 1089 */         return level.getMinY(); 
/* 1092 */       return 63.0D;
/*      */     }
/*      */     
/*      */     public float voidDarknessOnsetRange() {
/* 1096 */       if (this.isFlat)
/* 1097 */         return 1.0F; 
/* 1099 */       return 32.0F;
/*      */     }
/*      */   }
/*      */   
/*      */   protected LevelEntityGetter<Entity> getEntities() {
/* 1105 */     return this.entityStorage.getEntityGetter();
/*      */   }
/*      */   
/*      */   private final class EntityCallbacks implements LevelCallback<Entity> {
/*      */     public void onCreated(Entity entity) {}
/*      */     
/*      */     public void onDestroyed(Entity entity) {}
/*      */     
/*      */     public void onTickingStart(Entity entity) {
/* 1119 */       ClientLevel.this.tickingEntities.add(entity);
/*      */     }
/*      */     
/*      */     public void onTickingEnd(Entity entity) {
/* 1124 */       ClientLevel.this.tickingEntities.remove(entity);
/*      */     }
/*      */     
/*      */     public void onTrackingStart(Entity entity) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: dup
/*      */       //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */       //   5: pop
/*      */       //   6: astore_2
/*      */       //   7: iconst_0
/*      */       //   8: istore_3
/*      */       //   9: aload_2
/*      */       //   10: iload_3
/*      */       //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*      */       //   16: lookupswitch default -> 98, 0 -> 44, 1 -> 68
/*      */       //   44: aload_2
/*      */       //   45: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */       //   48: astore #4
/*      */       //   50: aload_0
/*      */       //   51: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   54: getfield players : Ljava/util/List;
/*      */       //   57: aload #4
/*      */       //   59: invokeinterface add : (Ljava/lang/Object;)Z
/*      */       //   64: pop
/*      */       //   65: goto -> 98
/*      */       //   68: aload_2
/*      */       //   69: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */       //   72: astore #5
/*      */       //   74: aload_0
/*      */       //   75: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   78: getfield dragonParts : Ljava/util/List;
/*      */       //   81: aload #5
/*      */       //   83: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */       //   86: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */       //   89: invokeinterface addAll : (Ljava/util/Collection;)Z
/*      */       //   94: pop
/*      */       //   95: goto -> 98
/*      */       //   98: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1129	-> 0
/*      */       //   #1130	-> 44
/*      */       //   #1131	-> 68
/*      */       //   #1135	-> 98
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   50	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */       //   74	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */       //   0	99	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */       //   0	99	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */     }
/*      */     
/*      */     public void onTrackingEnd(Entity entity) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: invokevirtual unRide : ()V
/*      */       //   4: aload_1
/*      */       //   5: dup
/*      */       //   6: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */       //   9: pop
/*      */       //   10: astore_2
/*      */       //   11: iconst_0
/*      */       //   12: istore_3
/*      */       //   13: aload_2
/*      */       //   14: iload_3
/*      */       //   15: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*      */       //   20: lookupswitch default -> 102, 0 -> 48, 1 -> 72
/*      */       //   48: aload_2
/*      */       //   49: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */       //   52: astore #4
/*      */       //   54: aload_0
/*      */       //   55: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   58: getfield players : Ljava/util/List;
/*      */       //   61: aload #4
/*      */       //   63: invokeinterface remove : (Ljava/lang/Object;)Z
/*      */       //   68: pop
/*      */       //   69: goto -> 102
/*      */       //   72: aload_2
/*      */       //   73: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */       //   76: astore #5
/*      */       //   78: aload_0
/*      */       //   79: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   82: getfield dragonParts : Ljava/util/List;
/*      */       //   85: aload #5
/*      */       //   87: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */       //   90: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */       //   93: invokeinterface removeAll : (Ljava/util/Collection;)Z
/*      */       //   98: pop
/*      */       //   99: goto -> 102
/*      */       //   102: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1139	-> 0
/*      */       //   #1140	-> 4
/*      */       //   #1141	-> 48
/*      */       //   #1142	-> 72
/*      */       //   #1146	-> 102
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   54	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */       //   78	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */       //   0	103	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */       //   0	103	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */     }
/*      */     
/*      */     public void onSectionChange(Entity entity) {}
/*      */   }
/*      */   
/*      */   public String gatherChunkSourceStats() {
/* 1155 */     return "Chunks[C] W: " + this.chunkSource.gatherStats() + " E: " + this.entityStorage.gatherStats();
/*      */   }
/*      */   
/*      */   public void addDestroyBlockEffect(BlockPos pos, BlockState blockState) {
/* 1160 */     if (blockState.isAir() || !blockState.shouldSpawnTerrainParticles())
/*      */       return; 
/* 1164 */     VoxelShape shape = blockState.getShape((BlockGetter)this, pos);
/* 1166 */     double density = 0.25D;
/* 1169 */     shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
/*      */           double widthX = Math.min(1.0D, x2 - x1);
/*      */           double widthY = Math.min(1.0D, y2 - y1);
/*      */           double widthZ = Math.min(1.0D, z2 - z1);
/*      */           int countX = Math.max(2, Mth.ceil(widthX / 0.25D));
/*      */           int countY = Math.max(2, Mth.ceil(widthY / 0.25D));
/*      */           int countZ = Math.max(2, Mth.ceil(widthZ / 0.25D));
/*      */           for (int xx = 0; xx < countX; xx++) {
/*      */             for (int yy = 0; yy < countY; yy++) {
/*      */               for (int zz = 0; zz < countZ; zz++) {
/*      */                 double relX = (xx + 0.5D) / countX;
/*      */                 double relY = (yy + 0.5D) / countY;
/*      */                 double relZ = (zz + 0.5D) / countZ;
/*      */                 double x = relX * widthX + x1;
/*      */                 double y = relY * widthY + y1;
/*      */                 double z = relZ * widthZ + z1;
/*      */                 this.minecraft.particleEngine.add((Particle)new TerrainParticle(this, pos.getX() + x, pos.getY() + y, pos.getZ() + z, relX - 0.5D, relY - 0.5D, relZ - 0.5D, blockState, pos));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public void addBreakingBlockEffect(BlockPos pos, Direction direction) {
/* 1197 */     BlockState blockState = getBlockState(pos);
/* 1198 */     if (blockState.getRenderShape() == RenderShape.INVISIBLE || !blockState.shouldSpawnTerrainParticles())
/*      */       return; 
/* 1202 */     int x = pos.getX();
/* 1203 */     int y = pos.getY();
/* 1204 */     int z = pos.getZ();
/* 1206 */     float r = 0.1F;
/* 1208 */     AABB shape = blockState.getShape((BlockGetter)this, pos).bounds();
/* 1209 */     double xp = x + this.random.nextDouble() * (shape.maxX - shape.minX - 0.20000000298023224D) + 0.10000000149011612D + shape.minX;
/* 1210 */     double yp = y + this.random.nextDouble() * (shape.maxY - shape.minY - 0.20000000298023224D) + 0.10000000149011612D + shape.minY;
/* 1211 */     double zp = z + this.random.nextDouble() * (shape.maxZ - shape.minZ - 0.20000000298023224D) + 0.10000000149011612D + shape.minZ;
/* 1214 */     if (direction == Direction.DOWN)
/* 1215 */       yp = y + shape.minY - 0.10000000149011612D; 
/* 1217 */     if (direction == Direction.UP)
/* 1218 */       yp = y + shape.maxY + 0.10000000149011612D; 
/* 1220 */     if (direction == Direction.NORTH)
/* 1221 */       zp = z + shape.minZ - 0.10000000149011612D; 
/* 1223 */     if (direction == Direction.SOUTH)
/* 1224 */       zp = z + shape.maxZ + 0.10000000149011612D; 
/* 1226 */     if (direction == Direction.WEST)
/* 1227 */       xp = x + shape.minX - 0.10000000149011612D; 
/* 1229 */     if (direction == Direction.EAST)
/* 1230 */       xp = x + shape.maxX + 0.10000000149011612D; 
/* 1233 */     this.minecraft.particleEngine.add((new TerrainParticle(this, xp, yp, zp, 0.0D, 0.0D, 0.0D, blockState, pos)).setPower(0.2F).scale(0.6F));
/*      */   }
/*      */   
/*      */   public void setServerSimulationDistance(int serverSimulationDistance) {
/* 1237 */     this.serverSimulationDistance = serverSimulationDistance;
/*      */   }
/*      */   
/*      */   public int getServerSimulationDistance() {
/* 1241 */     return this.serverSimulationDistance;
/*      */   }
/*      */   
/*      */   public FeatureFlagSet enabledFeatures() {
/* 1246 */     return this.connection.enabledFeatures();
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 1251 */     return this.connection.potionBrewing();
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 1256 */     return this.connection.fuelValues();
/*      */   }
/*      */   
/*      */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, Level.ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> secondaryParticles, Holder<SoundEvent> explosionSound) {}
/*      */   
/*      */   public int getSeaLevel() {
/* 1265 */     return this.seaLevel;
/*      */   }
/*      */   
/*      */   public int getClientLeafTintColor(BlockPos pos) {
/* 1270 */     BlockState state = getBlockState(pos);
/* 1271 */     BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(state, 0);
/* 1272 */     return (tintSource != null) ? tintSource.colorInWorld(state, this, pos) : -1;
/*      */   }
/*      */   
/*      */   public void registerForCleaning(CacheSlot<ClientLevel, ?> slot) {
/* 1277 */     this.connection.registerForCleaning(slot);
/*      */   }
/*      */   
/*      */   public void trackExplosionEffects(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 1281 */     this.explosionTracker.track(center, radius, blockCount, blockParticles);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */