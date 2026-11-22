/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.cache.CacheBuilder;
/*      */ import com.google.common.cache.CacheLoader;
/*      */ import com.google.common.cache.LoadingCache;
/*      */ import com.google.common.hash.HashCode;
/*      */ import com.google.common.net.InetAddresses;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.UUIDUtil;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.PacketSendListener;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.OutgoingChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.ResolutionContext;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMountScreenOpenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
/*      */ import net.minecraft.network.protocol.status.ServerStatus;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.PlayerAdvancements;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*      */ import net.minecraft.server.network.TextFilter;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.stats.ServerRecipeBook;
/*      */ import net.minecraft.stats.ServerStatsCounter;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.HashOps;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.DebugSubscription;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.attribute.BedRule;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageTypes;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntitySpawnRequest;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Input;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ContainerListener;
/*      */ import net.minecraft.world.inventory.ContainerSynchronizer;
/*      */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*      */ import net.minecraft.world.inventory.NautilusInventoryMenu;
/*      */ import net.minecraft.world.inventory.RemoteSlot;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.MapItem;
/*      */ import net.minecraft.world.item.ServerItemCooldowns;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*      */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.portal.TeleportTransition;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.scores.TeamColor;
/*      */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ServerPlayer extends Player {
/*  216 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_XZ = 32;
/*      */   
/*      */   private static final int NEUTRAL_MOB_DEATH_NOTIFICATION_RADII_Y = 10;
/*      */   
/*      */   private static final int FLY_STAT_RECORDING_SPEED = 25;
/*      */   
/*      */   public static final double BLOCK_INTERACTION_DISTANCE_VERIFICATION_BUFFER = 1.0D;
/*      */   
/*      */   public static final double ENTITY_INTERACTION_DISTANCE_VERIFICATION_BUFFER = 3.0D;
/*      */   
/*      */   public static final int ENDER_PEARL_TICKET_RADIUS = 2;
/*      */   
/*      */   public static final String ENDER_PEARLS_TAG = "ender_pearls";
/*      */   
/*      */   public static final String ENDER_PEARL_DIMENSION_TAG = "ender_pearl_dimension";
/*      */   
/*      */   public static final String TAG_DIMENSION = "Dimension";
/*      */   
/*  227 */   private static final AttributeModifier CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER = new AttributeModifier(
/*  228 */       Identifier.withDefaultNamespace("creative_mode_block_range"), 0.5D, AttributeModifier.Operation.ADD_VALUE);
/*      */   
/*  232 */   private static final AttributeModifier CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER = new AttributeModifier(
/*  233 */       Identifier.withDefaultNamespace("creative_mode_entity_range"), 2.0D, AttributeModifier.Operation.ADD_VALUE);
/*      */   
/*  237 */   private static final Component SPAWN_SET_MESSAGE = (Component)Component.translatable("block.minecraft.set_spawn");
/*      */   
/*  238 */   private static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER = new AttributeModifier(
/*  239 */       Identifier.withDefaultNamespace("waypoint_transmit_range_crouch"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*      */   
/*      */   private static final boolean DEFAULT_SEEN_CREDITS = false;
/*      */   
/*      */   private static final boolean DEFAULT_SPAWN_EXTRA_PARTICLES_ON_FALL = false;
/*      */   
/*      */   public ServerGamePacketListenerImpl connection;
/*      */   
/*      */   private final MinecraftServer server;
/*      */   
/*      */   public final ServerPlayerGameMode gameMode;
/*      */   
/*      */   private final PlayerAdvancements advancements;
/*      */   
/*      */   private final ServerStatsCounter stats;
/*      */   
/*  252 */   private float lastRecordedHealthAndAbsorption = Float.MIN_VALUE;
/*      */   
/*  253 */   private int lastRecordedFoodLevel = Integer.MIN_VALUE;
/*      */   
/*  254 */   private int lastRecordedAirLevel = Integer.MIN_VALUE;
/*      */   
/*  255 */   private int lastRecordedArmor = Integer.MIN_VALUE;
/*      */   
/*  256 */   private int lastRecordedLevel = Integer.MIN_VALUE;
/*      */   
/*  257 */   private int lastRecordedExperience = Integer.MIN_VALUE;
/*      */   
/*  258 */   private float lastSentHealth = -1.0E8F;
/*      */   
/*  259 */   private int lastSentFood = -99999999;
/*      */   
/*      */   private boolean lastFoodSaturationZero = true;
/*      */   
/*  261 */   private int lastSentExp = -99999999;
/*      */   
/*  262 */   private ChatVisiblity chatVisibility = ChatVisiblity.FULL;
/*      */   
/*  263 */   private ParticleStatus particleStatus = ParticleStatus.ALL;
/*      */   
/*      */   private boolean canChatColor = true;
/*      */   
/*  265 */   private long lastActionTime = Util.getMillis();
/*      */   
/*      */   private Entity camera;
/*      */   
/*      */   private boolean isChangingDimension;
/*      */   
/*      */   public boolean seenCredits = false;
/*      */   
/*      */   private final ServerRecipeBook recipeBook;
/*      */   
/*      */   private Vec3 levitationStartPos;
/*      */   
/*      */   private int levitationStartTime;
/*      */   
/*      */   private boolean disconnected;
/*      */   
/*  273 */   private int requestedViewDistance = 2;
/*      */   
/*  274 */   private String language = "en_us";
/*      */   
/*      */   private Vec3 startingToFallPosition;
/*      */   
/*      */   private Vec3 enteredNetherPosition;
/*      */   
/*      */   private Vec3 enteredLavaOnVehiclePosition;
/*      */   
/*      */   private Vec3 currentExplosionImpactPos;
/*      */   
/*      */   private Entity currentExplosionCause;
/*      */   
/*  282 */   private SectionPos lastSectionPos = SectionPos.of(0, 0, 0);
/*      */   
/*  283 */   private ChunkTrackingView chunkTrackingView = ChunkTrackingView.EMPTY;
/*      */   
/*      */   private RespawnConfig respawnConfig;
/*      */   
/*      */   private final TextFilter textFilter;
/*      */   
/*      */   private boolean textFilteringEnabled;
/*      */   
/*      */   private boolean allowsListing;
/*      */   
/*      */   private boolean spawnExtraParticlesOnFall = false;
/*      */   
/*  292 */   private WardenSpawnTracker wardenSpawnTracker = new WardenSpawnTracker();
/*      */   
/*      */   private BlockPos raidOmenPosition;
/*      */   
/*  294 */   private Vec3 lastKnownClientMovement = Vec3.ZERO;
/*      */   
/*  295 */   private Input lastClientInput = Input.EMPTY;
/*      */   
/*  296 */   private final Set<ThrownEnderpearl> enderPearls = new HashSet<>();
/*      */   
/*      */   private long timeEntitySatOnShoulder;
/*      */   
/*  299 */   private CompoundTag shoulderEntityLeft = new CompoundTag();
/*      */   
/*  300 */   private CompoundTag shoulderEntityRight = new CompoundTag();
/*      */   
/*  302 */   private final ContainerSynchronizer containerSynchronizer = new ContainerSynchronizer() {
/*  305 */       private final LoadingCache<TypedDataComponent<?>, Integer> cache = CacheBuilder.newBuilder()
/*  306 */         .maximumSize(256L)
/*  307 */         .build(new CacheLoader<TypedDataComponent<?>, Integer>() {
/*  308 */             private final DynamicOps<HashCode> registryHashOps = (DynamicOps<HashCode>)ServerPlayer.this.registryAccess().createSerializationContext((DynamicOps)HashOps.CRC32C_INSTANCE);
/*      */             
/*      */             public Integer load(TypedDataComponent<?> component) {
/*  312 */               return Integer.valueOf(((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + String.valueOf(component) + ": " + msg))).asInt());
/*      */             }
/*      */           });
/*      */       
/*      */       public void sendInitialData(AbstractContainerMenu container, List<ItemStack> slotItems, ItemStack carriedItem, int[] dataSlots) {
/*  318 */         ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetContentPacket(container.containerId, container.incrementStateId(), slotItems, carriedItem));
/*  319 */         for (int slot = 0; slot < dataSlots.length; slot++)
/*  320 */           broadcastDataValue(container, slot, dataSlots[slot]); 
/*      */       }
/*      */       
/*      */       public void sendSlotChange(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/*  326 */         ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), slotIndex, itemStack));
/*      */       }
/*      */       
/*      */       public void sendCarriedChange(AbstractContainerMenu container, ItemStack itemStack) {
/*  331 */         ServerPlayer.this.connection.send((Packet)new ClientboundSetCursorItemPacket(itemStack));
/*      */       }
/*      */       
/*      */       public void sendDataChange(AbstractContainerMenu container, int id, int value) {
/*  336 */         broadcastDataValue(container, id, value);
/*      */       }
/*      */       
/*      */       private void broadcastDataValue(AbstractContainerMenu container, int id, int value) {
/*  340 */         ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetDataPacket(container.containerId, id, value));
/*      */       }
/*      */       
/*      */       public RemoteSlot createSlot() {
/*  345 */         Objects.requireNonNull(this.cache);
/*  345 */         return (RemoteSlot)new RemoteSlot.Synchronized(this.cache::getUnchecked);
/*      */       }
/*      */     };
/*      */   
/*  349 */   private final ContainerListener containerListener = new ContainerListener() {
/*      */       public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack changedItem) {
/*  352 */         Slot slot = container.getSlot(slotIndex);
/*  353 */         if (slot instanceof net.minecraft.world.inventory.ResultSlot)
/*      */           return; 
/*  357 */         if (slot.container == ServerPlayer.this.getInventory())
/*  359 */           CriteriaTriggers.INVENTORY_CHANGED.trigger(ServerPlayer.this, ServerPlayer.this.getInventory(), changedItem); 
/*      */       }
/*      */       
/*      */       public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*      */     };
/*      */   
/*      */   private RemoteChatSession chatSession;
/*      */   
/*      */   public final Object object;
/*      */   
/*  373 */   private final CommandSource commandSource = new CommandSource() {
/*      */       public boolean acceptsSuccess() {
/*  376 */         return ((Boolean)ServerPlayer.this.level().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue();
/*      */       }
/*      */       
/*      */       public boolean acceptsFailure() {
/*  381 */         return true;
/*      */       }
/*      */       
/*      */       public boolean shouldInformAdmins() {
/*  386 */         return true;
/*      */       }
/*      */       
/*      */       public void sendSystemMessage(Component message) {
/*  391 */         ServerPlayer.this.sendSystemMessage(message);
/*      */       }
/*      */     };
/*      */   
/*  395 */   private Set<DebugSubscription<?>> requestedDebugSubscriptions = Set.of();
/*      */   
/*      */   private int containerCounter;
/*      */   
/*      */   public boolean wonGame;
/*      */   
/*      */   public ServerPlayer(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
/*  398 */     super(level, gameProfile);
/*  399 */     this.server = server;
/*  401 */     this.textFilter = server.createTextFilterForPlayer(this);
/*  402 */     this.gameMode = server.createGameModeForPlayer(this);
/*  403 */     this.gameMode.setGameModeForPlayer(calculateGameModeForNewPlayer((GameType)null), null);
/*  405 */     this.recipeBook = new ServerRecipeBook((id, output) -> server.getRecipeManager().listDisplaysForRecipe(id, output));
/*  407 */     this.stats = server.getPlayerList().getPlayerStats(this);
/*  408 */     this.advancements = server.getPlayerList().getPlayerAdvancements(this);
/*  410 */     updateOptions(clientInformation);
/*  412 */     this.object = null;
/*      */   }
/*      */   
/*      */   public BlockPos adjustSpawnLocation(ServerLevel level, BlockPos spawnSuggestion) {
/*  418 */     CompletableFuture<Vec3> future = PlayerSpawnFinder.findSpawn(level, spawnSuggestion);
/*  419 */     Objects.requireNonNull(future);
/*  419 */     this.server.managedBlock(future::isDone);
/*  420 */     return BlockPos.containing((Position)future.join());
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  425 */     super.readAdditionalSaveData(input);
/*  427 */     this.wardenSpawnTracker = input.read("warden_spawn_tracker", WardenSpawnTracker.CODEC).orElseGet(WardenSpawnTracker::new);
/*  429 */     this.enteredNetherPosition = input.read("entered_nether_pos", Vec3.CODEC).orElse(null);
/*  431 */     this.currentExplosionImpactPos = input.read("last_explosion_impact_pos", Vec3.CODEC).orElse(null);
/*  433 */     this.seenCredits = input.getBooleanOr("seenCredits", false);
/*  435 */     input.read("recipeBook", ServerRecipeBook.Packed.CODEC)
/*  436 */       .ifPresent(p -> this.recipeBook.loadUntrusted(p, ()));
/*  438 */     if (isSleeping())
/*  439 */       stopSleeping(); 
/*  442 */     this.respawnConfig = input.read("respawn", RespawnConfig.CODEC).orElse(null);
/*  444 */     this.spawnExtraParticlesOnFall = input.getBooleanOr("spawn_extra_particles_on_fall", false);
/*  446 */     this.raidOmenPosition = input.read("raid_omen_position", BlockPos.CODEC).orElse(null);
/*  448 */     this.gameMode.setGameModeForPlayer(
/*  449 */         calculateGameModeForNewPlayer(readPlayerMode(input, "playerGameType")), 
/*  450 */         readPlayerMode(input, "previousPlayerGameType"));
/*  453 */     setShoulderEntityLeft(input.read("ShoulderEntityLeft", CompoundTag.CODEC).orElseGet(CompoundTag::new));
/*  454 */     setShoulderEntityRight(input.read("ShoulderEntityRight", CompoundTag.CODEC).orElseGet(CompoundTag::new));
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  459 */     super.addAdditionalSaveData(output);
/*  461 */     output.store("warden_spawn_tracker", WardenSpawnTracker.CODEC, this.wardenSpawnTracker);
/*  463 */     storeGameTypes(output);
/*  464 */     output.putBoolean("seenCredits", this.seenCredits);
/*  466 */     output.storeNullable("entered_nether_pos", Vec3.CODEC, this.enteredNetherPosition);
/*  468 */     output.storeNullable("last_explosion_impact_pos", Vec3.CODEC, this.currentExplosionImpactPos);
/*  470 */     saveParentVehicle(output);
/*  472 */     output.store("recipeBook", ServerRecipeBook.Packed.CODEC, this.recipeBook.pack());
/*  474 */     output.putString("Dimension", level().dimension().identifier().toString());
/*  476 */     output.storeNullable("respawn", RespawnConfig.CODEC, this.respawnConfig);
/*  478 */     output.putBoolean("spawn_extra_particles_on_fall", this.spawnExtraParticlesOnFall);
/*  480 */     output.storeNullable("raid_omen_position", BlockPos.CODEC, this.raidOmenPosition);
/*  482 */     saveEnderPearls(output);
/*  484 */     if (!getShoulderEntityLeft().isEmpty())
/*  485 */       output.store("ShoulderEntityLeft", CompoundTag.CODEC, getShoulderEntityLeft()); 
/*  487 */     if (!getShoulderEntityRight().isEmpty())
/*  488 */       output.store("ShoulderEntityRight", CompoundTag.CODEC, getShoulderEntityRight()); 
/*      */   }
/*      */   
/*      */   private void saveParentVehicle(ValueOutput playerOutput) {
/*  493 */     Entity rootVehicle = getRootVehicle();
/*  494 */     Entity vehicle = getVehicle();
/*  495 */     if (vehicle != null && rootVehicle != this && rootVehicle.hasExactlyOnePlayerPassenger()) {
/*  496 */       ValueOutput vehicleWrapper = playerOutput.child("RootVehicle");
/*  497 */       vehicleWrapper.store("Attach", UUIDUtil.CODEC, vehicle.getUUID());
/*  499 */       rootVehicle.save(vehicleWrapper.child("Entity"));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void loadAndSpawnParentVehicle(ValueInput playerInput) {
/*  504 */     Optional<ValueInput> rootTag = playerInput.child("RootVehicle");
/*  505 */     if (rootTag.isEmpty())
/*      */       return; 
/*  509 */     ServerLevel serverLevel = level();
/*  510 */     Entity vehicle = EntityType.loadEntityRecursive(((ValueInput)rootTag.get()).childOrEmpty("Entity"), serverLevel, EntitySpawnReason.LOAD, e -> !serverLevel.addWithUUID(e) ? null : e);
/*  517 */     if (vehicle == null)
/*      */       return; 
/*  521 */     UUID attachTo = ((ValueInput)rootTag.get()).read("Attach", UUIDUtil.CODEC).orElse(null);
/*  523 */     if (vehicle.getUUID().equals(attachTo)) {
/*  524 */       startRiding(vehicle, true, false);
/*      */     } else {
/*  526 */       for (Entity entity : vehicle.getIndirectPassengers()) {
/*  527 */         if (entity.getUUID().equals(attachTo)) {
/*  528 */           startRiding(entity, true, false);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  533 */     if (!isPassenger()) {
/*  534 */       LOGGER.warn("Couldn't reattach entity to player");
/*  535 */       vehicle.discard();
/*  536 */       for (Entity entity : vehicle.getIndirectPassengers())
/*  537 */         entity.discard(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void saveEnderPearls(ValueOutput playerOutput) {
/*  543 */     if (!this.enderPearls.isEmpty()) {
/*  544 */       ValueOutput.ValueOutputList pearlsOutput = playerOutput.childrenList("ender_pearls");
/*  545 */       for (ThrownEnderpearl enderPearl : this.enderPearls) {
/*  546 */         if (enderPearl.isRemoved()) {
/*  547 */           LOGGER.warn("Trying to save removed ender pearl, skipping");
/*      */           continue;
/*      */         } 
/*  550 */         ValueOutput pearlTag = pearlsOutput.addChild();
/*  551 */         enderPearl.save(pearlTag);
/*  552 */         pearlTag.store("ender_pearl_dimension", Level.RESOURCE_KEY_CODEC, enderPearl.level().dimension());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void loadAndSpawnEnderPearls(ValueInput playerInput) {
/*  558 */     playerInput.childrenListOrEmpty("ender_pearls").forEach(this::loadAndSpawnEnderPearl);
/*      */   }
/*      */   
/*      */   private void loadAndSpawnEnderPearl(ValueInput pearlInput) {
/*  562 */     Optional<ResourceKey<Level>> pearlLevelKey = pearlInput.read("ender_pearl_dimension", Level.RESOURCE_KEY_CODEC);
/*  563 */     if (pearlLevelKey.isEmpty())
/*      */       return; 
/*  566 */     ServerLevel pearlLevel = level().getServer().getLevel(pearlLevelKey.get());
/*  567 */     if (pearlLevel != null) {
/*  568 */       Entity pearl = EntityType.loadEntityRecursive(pearlInput, pearlLevel, EntitySpawnReason.LOAD, entity -> !pearlLevel.addWithUUID(entity) ? null : entity);
/*  574 */       if (pearl != null) {
/*  575 */         placeEnderPearlTicket(pearlLevel, pearl.chunkPosition());
/*      */       } else {
/*  577 */         LOGGER.warn("Failed to spawn player ender pearl in level ({}), skipping", pearlLevelKey.get());
/*      */       } 
/*      */     } else {
/*  580 */       LOGGER.warn("Trying to load ender pearl without level ({}) being loaded, skipping", pearlLevelKey.get());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setExperiencePoints(int amount) {
/*  585 */     float limit = getXpNeededForNextLevel();
/*  586 */     float max = (limit - 1.0F) / limit;
/*  587 */     float experiencePointsToSet = Mth.clamp(amount / limit, 0.0F, max);
/*  588 */     if (experiencePointsToSet == this.experienceProgress)
/*      */       return; 
/*  591 */     this.experienceProgress = experiencePointsToSet;
/*  592 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void setExperienceLevels(int amount) {
/*  596 */     if (amount == this.experienceLevel)
/*      */       return; 
/*  599 */     this.experienceLevel = amount;
/*  600 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void giveExperienceLevels(int amount) {
/*  605 */     if (amount == 0)
/*      */       return; 
/*  608 */     super.giveExperienceLevels(amount);
/*  609 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack itemStack, int enchantmentCost) {
/*  614 */     super.onEnchantmentPerformed(itemStack, enchantmentCost);
/*  615 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   private void initMenu(AbstractContainerMenu container) {
/*  619 */     container.addSlotListener(this.containerListener);
/*  620 */     container.setSynchronizer(this.containerSynchronizer);
/*      */   }
/*      */   
/*      */   public void initInventoryMenu() {
/*  624 */     initMenu((AbstractContainerMenu)this.inventoryMenu);
/*      */   }
/*      */   
/*      */   public void onEnterCombat() {
/*  629 */     super.onEnterCombat();
/*  631 */     this.connection.send((Packet)ClientboundPlayerCombatEnterPacket.INSTANCE);
/*      */   }
/*      */   
/*      */   public void onLeaveCombat() {
/*  636 */     super.onLeaveCombat();
/*  638 */     this.connection.send((Packet)new ClientboundPlayerCombatEndPacket(getCombatTracker()));
/*      */   }
/*      */   
/*      */   public void onInsideBlock(BlockState state) {
/*  643 */     CriteriaTriggers.ENTER_BLOCK.trigger(this, state);
/*      */   }
/*      */   
/*      */   protected ItemCooldowns createItemCooldowns() {
/*  648 */     return (ItemCooldowns)new ServerItemCooldowns(this);
/*      */   }
/*      */   
/*      */   public void tick() {
/*  653 */     this.connection.tickClientLoadTimeout();
/*  655 */     this.gameMode.tick();
/*  657 */     this.wardenSpawnTracker.tick();
/*  659 */     if (this.invulnerableTime > 0)
/*  660 */       this.invulnerableTime--; 
/*  662 */     this.containerMenu.broadcastChanges();
/*  664 */     if (!this.containerMenu.stillValid(this)) {
/*  665 */       closeContainer();
/*  666 */       this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */     } 
/*  669 */     Entity camera = getCamera();
/*  670 */     if (camera != this)
/*  671 */       if (camera.isAlive()) {
/*  673 */         absSnapTo(camera.getX(), camera.getY(), camera.getZ(), camera.getYRot(), camera.getXRot());
/*  674 */         level().getChunkSource().move(this);
/*  675 */         if (wantsToStopRiding())
/*  677 */           setCamera((Entity)this); 
/*      */       } else {
/*  680 */         setCamera((Entity)this);
/*      */       }  
/*  684 */     CriteriaTriggers.TICK.trigger(this);
/*  685 */     if (this.levitationStartPos != null)
/*  686 */       CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.tickCount - this.levitationStartTime); 
/*  689 */     trackStartFallingPosition();
/*  690 */     trackEnteredOrExitedLavaOnVehicle();
/*  691 */     updatePlayerAttributes();
/*  693 */     this.advancements.flushDirty(this, true);
/*      */   }
/*      */   
/*      */   private void updatePlayerAttributes() {
/*  697 */     AttributeInstance blockInteractionRange = getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
/*  698 */     if (blockInteractionRange != null)
/*  699 */       if (isCreative()) {
/*  700 */         blockInteractionRange.addOrUpdateTransientModifier(CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER);
/*      */       } else {
/*  702 */         blockInteractionRange.removeModifier(CREATIVE_BLOCK_INTERACTION_RANGE_MODIFIER);
/*      */       }  
/*  705 */     AttributeInstance entityInteractionRange = getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
/*  706 */     if (entityInteractionRange != null)
/*  707 */       if (isCreative()) {
/*  708 */         entityInteractionRange.addOrUpdateTransientModifier(CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER);
/*      */       } else {
/*  710 */         entityInteractionRange.removeModifier(CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER);
/*      */       }  
/*  713 */     AttributeInstance waypointTransmitRange = getAttribute(Attributes.WAYPOINT_TRANSMIT_RANGE);
/*  714 */     if (waypointTransmitRange != null)
/*  715 */       if (isCrouching()) {
/*  716 */         waypointTransmitRange.addOrUpdateTransientModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
/*      */       } else {
/*  718 */         waypointTransmitRange.removeModifier(WAYPOINT_TRANSMIT_RANGE_CROUCH_MODIFIER);
/*      */       }  
/*      */   }
/*      */   
/*      */   public void doTick() {
/*      */     try {
/*  725 */       if (!isSpectator() || !touchingUnloadedChunk()) {
/*  726 */         super.tick();
/*  727 */         if (!this.containerMenu.stillValid(this)) {
/*  728 */           closeContainer();
/*  729 */           this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */         } 
/*  732 */         this.foodData.tick(this);
/*  733 */         awardStat(Stats.PLAY_TIME);
/*  734 */         awardStat(Stats.TOTAL_WORLD_TIME);
/*  735 */         if (isAlive())
/*  736 */           awardStat(Stats.TIME_SINCE_DEATH); 
/*  738 */         if (isDiscrete())
/*  739 */           awardStat(Stats.CROUCH_TIME); 
/*  741 */         if (!isSleeping())
/*  742 */           awardStat(Stats.TIME_SINCE_REST); 
/*      */       } 
/*  746 */       for (int i = 0; i < getInventory().getContainerSize(); i++) {
/*  747 */         ItemStack itemStack = getInventory().getItem(i);
/*  748 */         if (!itemStack.isEmpty())
/*  749 */           synchronizeSpecialItemUpdates(itemStack); 
/*      */       } 
/*  753 */       if (getHealth() != this.lastSentHealth || this.lastSentFood != this.foodData.getFoodLevel() || ((this.foodData.getSaturationLevel() == 0.0F)) != this.lastFoodSaturationZero) {
/*  754 */         this.connection.send((Packet)new ClientboundSetHealthPacket(getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
/*  755 */         this.lastSentHealth = getHealth();
/*  756 */         this.lastSentFood = this.foodData.getFoodLevel();
/*  757 */         this.lastFoodSaturationZero = (this.foodData.getSaturationLevel() == 0.0F);
/*      */       } 
/*  760 */       if (getHealth() + getAbsorptionAmount() != this.lastRecordedHealthAndAbsorption) {
/*  761 */         this.lastRecordedHealthAndAbsorption = getHealth() + getAbsorptionAmount();
/*  762 */         updateScoreForCriteria(ObjectiveCriteria.HEALTH, Mth.ceil(this.lastRecordedHealthAndAbsorption));
/*      */       } 
/*  765 */       if (this.foodData.getFoodLevel() != this.lastRecordedFoodLevel) {
/*  766 */         this.lastRecordedFoodLevel = this.foodData.getFoodLevel();
/*  767 */         updateScoreForCriteria(ObjectiveCriteria.FOOD, Mth.ceil(this.lastRecordedFoodLevel));
/*      */       } 
/*  770 */       if (getAirSupply() != this.lastRecordedAirLevel) {
/*  771 */         this.lastRecordedAirLevel = getAirSupply();
/*  772 */         updateScoreForCriteria(ObjectiveCriteria.AIR, Mth.ceil(this.lastRecordedAirLevel));
/*      */       } 
/*  775 */       if (getArmorValue() != this.lastRecordedArmor) {
/*  776 */         this.lastRecordedArmor = getArmorValue();
/*  777 */         updateScoreForCriteria(ObjectiveCriteria.ARMOR, Mth.ceil(this.lastRecordedArmor));
/*      */       } 
/*  780 */       if (this.totalExperience != this.lastRecordedExperience) {
/*  781 */         this.lastRecordedExperience = this.totalExperience;
/*  782 */         updateScoreForCriteria(ObjectiveCriteria.EXPERIENCE, Mth.ceil(this.lastRecordedExperience));
/*      */       } 
/*  785 */       if (this.experienceLevel != this.lastRecordedLevel) {
/*  786 */         this.lastRecordedLevel = this.experienceLevel;
/*  787 */         updateScoreForCriteria(ObjectiveCriteria.LEVEL, Mth.ceil(this.lastRecordedLevel));
/*      */       } 
/*  790 */       if (this.totalExperience != this.lastSentExp) {
/*  791 */         this.lastSentExp = this.totalExperience;
/*  792 */         this.connection.send((Packet)new ClientboundSetExperiencePacket(this.experienceProgress, this.totalExperience, this.experienceLevel));
/*      */       } 
/*  795 */       if (this.tickCount % 20 == 0)
/*  796 */         CriteriaTriggers.LOCATION.trigger(this); 
/*  798 */     } catch (Throwable t) {
/*  799 */       CrashReport report = CrashReport.forThrowable(t, "Ticking player");
/*  800 */       CrashReportCategory category = report.addCategory("Player being ticked");
/*  802 */       fillCrashReportCategory(category);
/*  804 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void synchronizeSpecialItemUpdates(ItemStack itemStack) {
/*  809 */     MapId mapId = (MapId)itemStack.get(DataComponents.MAP_ID);
/*  810 */     MapItemSavedData data = MapItem.getSavedData(mapId, level());
/*  811 */     if (data != null) {
/*  812 */       Packet<?> packet = data.getUpdatePacket(mapId, this);
/*  813 */       if (packet != null)
/*  814 */         this.connection.send(packet); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void tickRegeneration() {
/*  821 */     if (level().getDifficulty() == Difficulty.PEACEFUL && ((Boolean)level().getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION)).booleanValue()) {
/*  822 */       if (this.tickCount % 20 == 0) {
/*  823 */         if (getHealth() < getMaxHealth())
/*  824 */           heal(1.0F); 
/*  827 */         float saturation = this.foodData.getSaturationLevel();
/*  828 */         if (saturation < 20.0F)
/*  829 */           this.foodData.setSaturation(saturation + 1.0F); 
/*      */       } 
/*  832 */       if (this.tickCount % 10 == 0 && 
/*  833 */         this.foodData.needsFood())
/*  834 */         this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleShoulderEntities() {
/*  842 */     playShoulderEntityAmbientSound(getShoulderEntityLeft());
/*  843 */     playShoulderEntityAmbientSound(getShoulderEntityRight());
/*  844 */     if (this.fallDistance > 0.5D || isInWater() || (getAbilities()).flying || isSleeping() || this.isInPowderSnow)
/*  845 */       removeEntitiesOnShoulder(); 
/*      */   }
/*      */   
/*      */   private void playShoulderEntityAmbientSound(CompoundTag shoulderEntityTag) {
/*  850 */     if (shoulderEntityTag.isEmpty() || shoulderEntityTag.getBooleanOr("Silent", false))
/*      */       return; 
/*  854 */     if (this.random.nextInt(200) == 0) {
/*  855 */       EntityType<?> entityType = shoulderEntityTag.read("id", EntityType.CODEC).orElse(null);
/*  856 */       if (entityType == EntityTypes.PARROT && 
/*  857 */         !Parrot.imitateNearbyMobs(level(), (Entity)this))
/*  858 */         level().playSound(null, getX(), getY(), getZ(), Parrot.getAmbient(level(), this.random), getSoundSource(), 1.0F, Parrot.getPitch(this.random)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean setEntityOnShoulder(CompoundTag entityTag) {
/*  865 */     if (isPassenger() || !onGround() || isInWater() || this.isInPowderSnow)
/*  866 */       return false; 
/*  870 */     if (getShoulderEntityLeft().isEmpty()) {
/*  871 */       setShoulderEntityLeft(entityTag);
/*  872 */       this.timeEntitySatOnShoulder = level().getGameTime();
/*  873 */       return true;
/*      */     } 
/*  874 */     if (getShoulderEntityRight().isEmpty()) {
/*  875 */       setShoulderEntityRight(entityTag);
/*  876 */       this.timeEntitySatOnShoulder = level().getGameTime();
/*  877 */       return true;
/*      */     } 
/*  880 */     return false;
/*      */   }
/*      */   
/*      */   protected void removeEntitiesOnShoulder() {
/*  885 */     if (this.timeEntitySatOnShoulder + 20L < level().getGameTime()) {
/*  886 */       respawnEntityOnShoulder(getShoulderEntityLeft());
/*  887 */       setShoulderEntityLeft(new CompoundTag());
/*  888 */       respawnEntityOnShoulder(getShoulderEntityRight());
/*  889 */       setShoulderEntityRight(new CompoundTag());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void respawnEntityOnShoulder(CompoundTag tag) {
/*  894 */     ServerLevel serverLevel = level();
/*  895 */     if (!tag.isEmpty()) {
/*  896 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*      */       try {
/*  897 */         EntityType.create(TagValueInput.create(reporter.forChild(() -> ".shoulder"), (HolderLookup.Provider)serverLevel.registryAccess(), tag), serverLevel, new EntitySpawnRequest(EntitySpawnReason.LOAD, false))
/*  898 */           .ifPresent(entity -> {
/*      */               if (entity instanceof TamableAnimal) {
/*      */                 TamableAnimal tamed = (TamableAnimal)entity;
/*      */                 tamed.setOwner((LivingEntity)this);
/*      */               } 
/*      */               entity.setPos(getX(), getY() + 0.699999988079071D, getZ());
/*      */               serverLevel.addWithUUID(entity);
/*      */             });
/*  907 */         reporter.close();
/*      */       } catch (Throwable throwable) {
/*      */         try {
/*      */           reporter.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         } 
/*      */         throw throwable;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resetFallDistance() {
/*  913 */     if (getHealth() > 0.0F && this.startingToFallPosition != null)
/*  914 */       CriteriaTriggers.FALL_FROM_HEIGHT.trigger(this, this.startingToFallPosition); 
/*  916 */     this.startingToFallPosition = null;
/*  917 */     super.resetFallDistance();
/*      */   }
/*      */   
/*      */   public void trackStartFallingPosition() {
/*  921 */     if (this.fallDistance > 0.0D && this.startingToFallPosition == null) {
/*  922 */       this.startingToFallPosition = position();
/*  924 */       if (this.currentExplosionImpactPos != null && this.currentExplosionImpactPos.y <= this.startingToFallPosition.y)
/*  925 */         CriteriaTriggers.FALL_AFTER_EXPLOSION.trigger(this, this.currentExplosionImpactPos, this.currentExplosionCause); 
/*  927 */       this.currentExplosionImpactPos = null;
/*  928 */       this.currentExplosionCause = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void trackEnteredOrExitedLavaOnVehicle() {
/*  933 */     if (getVehicle() != null && getVehicle().isInLava())
/*  934 */       if (this.enteredLavaOnVehiclePosition == null) {
/*  935 */         this.enteredLavaOnVehiclePosition = position();
/*      */       } else {
/*  937 */         CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.trigger(this, this.enteredLavaOnVehiclePosition);
/*      */       }  
/*  940 */     if (this.enteredLavaOnVehiclePosition != null && (getVehicle() == null || !getVehicle().isInLava()))
/*  941 */       this.enteredLavaOnVehiclePosition = null; 
/*      */   }
/*      */   
/*      */   private void updateScoreForCriteria(ObjectiveCriteria criteria, int value) {
/*  946 */     level().getScoreboard().forAllObjectives(criteria, (ScoreHolder)this, score -> score.set(value));
/*      */   }
/*      */   
/*      */   public void die(DamageSource source) {
/*  951 */     gameEvent((Holder)GameEvent.ENTITY_DIE);
/*  952 */     boolean showDeathMessage = ((Boolean)level().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)).booleanValue();
/*  954 */     if (showDeathMessage) {
/*  955 */       Component deathMessage = getCombatTracker().getDeathMessage();
/*  956 */       this.connection.send((Packet)new ClientboundPlayerCombatKillPacket(getId(), deathMessage), 
/*  957 */           PacketSendListener.exceptionallySend(() -> {
/*      */               int truncatedMessageSize = 256;
/*      */               String truncatedDeathMessage = deathMessage.getString(256);
/*      */               MutableComponent mutableComponent1 = Component.translatable("death.attack.message_too_long", new Object[] { Component.literal(truncatedDeathMessage).withStyle(ChatFormatting.YELLOW) });
/*      */               MutableComponent mutableComponent2 = Component.translatable("death.attack.even_more_magic", new Object[] { getDisplayName() }).withStyle(());
/*      */               return (Packet)new ClientboundPlayerCombatKillPacket(getId(), (Component)mutableComponent2);
/*      */             }));
/*  965 */       PlayerTeam playerTeam = getTeam();
/*  966 */       if (playerTeam == null || playerTeam.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
/*  967 */         this.server.getPlayerList().broadcastSystemMessage(deathMessage, false);
/*  968 */       } else if (playerTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
/*  969 */         this.server.getPlayerList().broadcastSystemToTeam(this, deathMessage);
/*  970 */       } else if (playerTeam.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
/*  971 */         this.server.getPlayerList().broadcastSystemToAllExceptTeam(this, deathMessage);
/*      */       } 
/*      */     } else {
/*  974 */       this.connection.send((Packet)new ClientboundPlayerCombatKillPacket(getId(), CommonComponents.EMPTY));
/*      */     } 
/*  976 */     removeEntitiesOnShoulder();
/*  977 */     if (((Boolean)level().getGameRules().get(GameRules.FORGIVE_DEAD_PLAYERS)).booleanValue())
/*  978 */       tellNeutralMobsThatIDied(); 
/*  981 */     if (!isSpectator())
/*  982 */       dropAllDeathLoot(level(), source); 
/*  985 */     level().getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, (ScoreHolder)this, ScoreAccess::increment);
/*  987 */     LivingEntity killer = getKillCredit();
/*  988 */     if (killer != null) {
/*  989 */       awardStat(Stats.ENTITY_KILLED_BY.get(killer.getType()));
/*  990 */       killer.awardKillScore((Entity)this, source);
/*  992 */       createWitherRose(killer);
/*      */     } 
/*  995 */     level().broadcastEntityEvent((Entity)this, (byte)3);
/*  997 */     awardStat(Stats.DEATHS);
/*  998 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  999 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/* 1000 */     clearFire();
/* 1001 */     setTicksFrozen(0);
/* 1002 */     setSharedFlagOnFire(false);
/* 1003 */     getCombatTracker().recheckStatus();
/* 1004 */     setLastDeathLocation(Optional.of(GlobalPos.of(level().dimension(), blockPosition())));
/* 1005 */     this.connection.markClientUnloadedAfterDeath();
/*      */   }
/*      */   
/*      */   private void tellNeutralMobsThatIDied() {
/* 1009 */     AABB aabb = (new AABB(blockPosition())).inflate(32.0D, 10.0D, 32.0D);
/* 1010 */     level().getEntitiesOfClass(Mob.class, aabb, EntitySelector.NO_SPECTATORS).stream()
/* 1011 */       .filter(mob -> mob instanceof NeutralMob)
/* 1012 */       .forEach(mob -> ((NeutralMob)mob).playerDied(level(), this));
/*      */   }
/*      */   
/*      */   public void awardKillScore(Entity victim, DamageSource killingBlow) {
/* 1017 */     if (victim == this)
/*      */       return; 
/* 1020 */     super.awardKillScore(victim, killingBlow);
/* 1022 */     ServerScoreboard serverScoreboard = level().getScoreboard();
/* 1023 */     serverScoreboard.forAllObjectives(ObjectiveCriteria.KILL_COUNT_ALL, (ScoreHolder)this, ScoreAccess::increment);
/* 1025 */     if (victim instanceof Player) {
/* 1026 */       awardStat(Stats.PLAYER_KILLS);
/* 1027 */       serverScoreboard.forAllObjectives(ObjectiveCriteria.KILL_COUNT_PLAYERS, (ScoreHolder)this, ScoreAccess::increment);
/*      */     } else {
/* 1029 */       awardStat(Stats.MOB_KILLS);
/*      */     } 
/* 1032 */     handleTeamKill((ScoreHolder)this, (ScoreHolder)victim, ObjectiveCriteria.TEAM_KILL);
/* 1033 */     handleTeamKill((ScoreHolder)victim, (ScoreHolder)this, ObjectiveCriteria.KILLED_BY_TEAM);
/* 1035 */     CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, victim, killingBlow);
/*      */   }
/*      */   
/*      */   private void handleTeamKill(ScoreHolder source, ScoreHolder target, Map<TeamColor, ObjectiveCriteria> criteriaByTeam) {
/* 1039 */     ServerScoreboard serverScoreboard = level().getScoreboard();
/* 1040 */     PlayerTeam ownTeam = serverScoreboard.getPlayersTeam(target.getScoreboardName());
/* 1041 */     if (ownTeam != null && ownTeam.getColor().isPresent()) {
/* 1042 */       ObjectiveCriteria criteria = criteriaByTeam.get(ownTeam.getColor().get());
/* 1043 */       if (criteria != null)
/* 1044 */         serverScoreboard.forAllObjectives(criteria, source, ScoreAccess::increment); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 1051 */     if (isInvulnerableTo(level, source))
/* 1052 */       return false; 
/* 1054 */     Entity entity = source.getEntity();
/* 1055 */     if (entity instanceof Player) {
/* 1055 */       Player player = (Player)entity;
/* 1055 */       if (!canHarmPlayer(player))
/* 1056 */         return false; 
/*      */     } 
/* 1058 */     if (entity instanceof AbstractArrow) {
/* 1058 */       AbstractArrow arrow = (AbstractArrow)entity;
/* 1059 */       Entity currentOwner = arrow.getOwner();
/* 1060 */       if (currentOwner instanceof Player) {
/* 1060 */         Player player = (Player)currentOwner;
/* 1060 */         if (!canHarmPlayer(player))
/* 1061 */           return false; 
/*      */       } 
/*      */     } 
/* 1064 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */   
/*      */   public boolean canHarmPlayer(Player target) {
/* 1069 */     if (!isPvpAllowed())
/* 1070 */       return false; 
/* 1072 */     return super.canHarmPlayer(target);
/*      */   }
/*      */   
/*      */   private boolean isPvpAllowed() {
/* 1076 */     return level().isPvpAllowed();
/*      */   }
/*      */   
/*      */   public TeleportTransition findRespawnPositionAndUseSpawnBlock(boolean consumeSpawnBlock, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/* 1080 */     RespawnConfig respawnConfig = getRespawnConfig();
/* 1081 */     ServerLevel respawnLevel = this.server.getLevel(RespawnConfig.getDimensionOrDefault(respawnConfig));
/* 1083 */     if (respawnLevel != null && respawnConfig != null) {
/* 1084 */       Optional<RespawnPosAngle> respawn = findRespawnAndUseSpawnBlock(respawnLevel, respawnConfig, consumeSpawnBlock);
/* 1085 */       if (respawn.isPresent()) {
/* 1086 */         RespawnPosAngle respawnPosAngle = respawn.get();
/* 1087 */         return new TeleportTransition(respawnLevel, respawnPosAngle.position(), Vec3.ZERO, respawnPosAngle.yaw(), respawnPosAngle.pitch(), postTeleportTransition);
/*      */       } 
/* 1089 */       return TeleportTransition.missingRespawnBlock(this, postTeleportTransition);
/*      */     } 
/* 1091 */     return TeleportTransition.createDefault(this, postTeleportTransition);
/*      */   }
/*      */   
/*      */   public boolean isReceivingWaypoints() {
/* 1096 */     return (getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE) > 0.0D);
/*      */   }
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1101 */     if (attribute.is(Attributes.WAYPOINT_RECEIVE_RANGE)) {
/* 1102 */       ServerWaypointManager waypointManager = level().getWaypointManager();
/* 1103 */       if (getAttributes().getValue(attribute) > 0.0D) {
/* 1104 */         waypointManager.addPlayer(this);
/*      */       } else {
/* 1106 */         waypointManager.removePlayer(this);
/*      */       } 
/*      */     } 
/* 1110 */     super.onAttributeUpdated(attribute);
/*      */   }
/*      */   
/*      */   private static final class RespawnPosAngle extends Record {
/*      */     private final Vec3 position;
/*      */     
/*      */     private final float yaw;
/*      */     
/*      */     private final float pitch;
/*      */     
/*      */     private RespawnPosAngle(Vec3 position, float yaw, float pitch) {
/* 1113 */       this.position = position;
/* 1113 */       this.yaw = yaw;
/* 1113 */       this.pitch = pitch;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1113	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1113	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1113	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnPosAngle;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Vec3 position() {
/* 1113 */       return this.position;
/*      */     }
/*      */     
/*      */     public float yaw() {
/* 1113 */       return this.yaw;
/*      */     }
/*      */     
/*      */     public float pitch() {
/* 1113 */       return this.pitch;
/*      */     }
/*      */     
/*      */     public static RespawnPosAngle of(Vec3 position, BlockPos lookAtBlockPos, float pitch) {
/* 1115 */       return new RespawnPosAngle(position, calculateLookAtYaw(position, lookAtBlockPos), pitch);
/*      */     }
/*      */     
/*      */     private static float calculateLookAtYaw(Vec3 position, BlockPos lookAtBlockPos) {
/* 1119 */       Vec3 lookDirection = Vec3.atBottomCenterOf((Vec3i)lookAtBlockPos).subtract(position).normalize();
/* 1120 */       return (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 57.2957763671875D - 90.0D);
/*      */     }
/*      */   }
/*      */   
/*      */   private static Optional<RespawnPosAngle> findRespawnAndUseSpawnBlock(ServerLevel level, RespawnConfig respawnConfig, boolean consumeSpawnBlock) {
/* 1125 */     LevelData.RespawnData respawnData = respawnConfig.respawnData;
/* 1126 */     BlockPos pos = respawnData.pos();
/* 1127 */     float yaw = respawnData.yaw();
/* 1128 */     float pitch = respawnData.pitch();
/* 1129 */     boolean forced = respawnConfig.forced;
/* 1131 */     BlockState blockState = level.getBlockState(pos);
/* 1132 */     Block block = blockState.getBlock();
/* 1134 */     if (block instanceof RespawnAnchorBlock && (forced || ((Integer)blockState.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() > 0) && RespawnAnchorBlock.canSetSpawn(level, pos)) {
/* 1135 */       Optional<Vec3> standUpPosition = RespawnAnchorBlock.findStandUpPosition(EntityTypes.PLAYER, (CollisionGetter)level, pos);
/* 1136 */       if (!forced && consumeSpawnBlock && standUpPosition.isPresent())
/* 1137 */         level.setBlock(pos, (BlockState)blockState.setValue((Property)RespawnAnchorBlock.CHARGE, Integer.valueOf(((Integer)blockState.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() - 1)), 3); 
/* 1139 */       return standUpPosition.map(p -> RespawnPosAngle.of(p, pos, 0.0F));
/*      */     } 
/* 1140 */     if (block instanceof BedBlock && ((BedRule)level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos)).canSetSpawn(level))
/* 1142 */       return 
/* 1143 */         BedBlock.findStandUpPosition(EntityTypes.PLAYER, (CollisionGetter)level, pos, (Direction)blockState.getValue((Property)BedBlock.FACING), yaw)
/* 1144 */         .map(p -> RespawnPosAngle.of(p, pos, 0.0F)); 
/* 1147 */     if (!forced)
/* 1148 */       return Optional.empty(); 
/* 1151 */     boolean freeBottom = block.isPossibleToRespawnInThis(blockState);
/* 1152 */     BlockState topState = level.getBlockState(pos.above());
/* 1153 */     boolean freeTop = topState.getBlock().isPossibleToRespawnInThis(topState);
/* 1155 */     if (freeBottom && freeTop)
/* 1156 */       return Optional.of(new RespawnPosAngle(new Vec3(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D), yaw, pitch)); 
/* 1159 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public void showEndCredits() {
/* 1164 */     unRide();
/* 1165 */     level().removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
/* 1166 */     if (!this.wonGame) {
/* 1167 */       this.wonGame = true;
/* 1168 */       this.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0.0F));
/* 1169 */       this.seenCredits = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public ServerPlayer teleport(TeleportTransition transition) {
/* 1175 */     if (isRemoved())
/* 1176 */       return null; 
/* 1179 */     if (transition.missingRespawnBlock())
/* 1180 */       this.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F)); 
/* 1183 */     ServerLevel newLevel = transition.newLevel();
/* 1184 */     ServerLevel oldLevel = level();
/* 1185 */     ResourceKey<Level> lastDimension = oldLevel.dimension();
/* 1187 */     if (!transition.asPassenger())
/* 1188 */       removeVehicle(); 
/* 1191 */     if (newLevel.dimension() == lastDimension) {
/* 1192 */       this.connection.teleport(PositionMoveRotation.of(transition), transition.relatives());
/* 1193 */       this.connection.resetPosition();
/* 1194 */       transition.postTeleportTransition().onTransition((Entity)this);
/* 1195 */       return this;
/*      */     } 
/* 1198 */     this.isChangingDimension = true;
/* 1199 */     LevelData levelData = newLevel.getLevelData();
/* 1201 */     this.connection.send((Packet)new ClientboundRespawnPacket(
/* 1202 */           createCommonSpawnInfo(newLevel), (byte)3));
/* 1205 */     this.connection.send((Packet)new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/* 1206 */     PlayerList playerList = this.server.getPlayerList();
/* 1208 */     playerList.sendPlayerPermissionLevel(this);
/* 1210 */     oldLevel.removePlayerImmediately(this, Entity.RemovalReason.CHANGED_DIMENSION);
/* 1212 */     unsetRemoved();
/* 1214 */     ProfilerFiller profiler = Profiler.get();
/* 1216 */     profiler.push("moving");
/* 1217 */     if (lastDimension == Level.OVERWORLD && newLevel.dimension() == Level.NETHER)
/* 1218 */       this.enteredNetherPosition = position(); 
/* 1221 */     profiler.pop();
/* 1223 */     profiler.push("placing");
/* 1225 */     setServerLevel(newLevel);
/* 1227 */     this.connection.teleport(PositionMoveRotation.of(transition), transition.relatives());
/* 1228 */     this.connection.resetPosition();
/* 1230 */     newLevel.addDuringTeleport((Entity)this);
/* 1232 */     profiler.pop();
/* 1234 */     triggerDimensionChangeTriggers(oldLevel);
/* 1236 */     stopUsingItem();
/* 1238 */     this.connection.send((Packet)new ClientboundPlayerAbilitiesPacket(getAbilities()));
/* 1239 */     playerList.sendLevelInfo(this, newLevel);
/* 1240 */     playerList.sendAllPlayerInfo(this);
/* 1241 */     playerList.sendActivePlayerEffects(this);
/* 1242 */     transition.postTeleportTransition().onTransition((Entity)this);
/* 1244 */     this.lastSentExp = -1;
/* 1245 */     this.lastSentHealth = -1.0F;
/* 1246 */     this.lastSentFood = -1;
/* 1248 */     teleportSpectators(transition, oldLevel);
/* 1249 */     return this;
/*      */   }
/*      */   
/*      */   public void forceSetRotation(float yRot, boolean relativeY, float xRot, boolean relativeX) {
/* 1254 */     super.forceSetRotation(yRot, relativeY, xRot, relativeX);
/* 1255 */     this.connection.send((Packet)new ClientboundPlayerRotationPacket(yRot, relativeY, xRot, relativeX));
/*      */   }
/*      */   
/*      */   private void triggerDimensionChangeTriggers(ServerLevel oldLevel) {
/* 1259 */     ResourceKey<Level> oldKey = oldLevel.dimension();
/* 1260 */     ResourceKey<Level> newKey = level().dimension();
/* 1261 */     CriteriaTriggers.CHANGED_DIMENSION.trigger(this, oldKey, newKey);
/* 1263 */     if (oldKey == Level.NETHER && newKey == Level.OVERWORLD && this.enteredNetherPosition != null)
/* 1264 */       CriteriaTriggers.NETHER_TRAVEL.trigger(this, this.enteredNetherPosition); 
/* 1266 */     if (newKey != Level.NETHER)
/* 1267 */       this.enteredNetherPosition = null; 
/*      */   }
/*      */   
/*      */   public boolean broadcastToPlayer(ServerPlayer player) {
/* 1273 */     if (player.isSpectator())
/* 1274 */       return (getCamera() == this); 
/* 1277 */     if (isSpectator())
/* 1278 */       return false; 
/* 1281 */     return super.broadcastToPlayer(player);
/*      */   }
/*      */   
/*      */   public void take(Entity entity, int orgCount) {
/* 1286 */     super.take(entity, orgCount);
/* 1287 */     this.containerMenu.broadcastChanges();
/*      */   }
/*      */   
/*      */   public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos pos) {
/* 1292 */     Direction direction = (Direction)level().getBlockState(pos).getValue((Property)HorizontalDirectionalBlock.FACING);
/* 1293 */     if (isSleeping() || !isAlive())
/* 1294 */       return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM); 
/* 1297 */     BedRule rule = (BedRule)level().environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);
/* 1298 */     boolean canSleep = rule.canSleep(level());
/* 1299 */     boolean canSetSpawn = rule.canSetSpawn(level());
/* 1301 */     if (!canSetSpawn && !canSleep)
/* 1302 */       return Either.left(rule.asProblem()); 
/* 1305 */     if (!bedInRange(pos, direction))
/* 1306 */       return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY); 
/* 1309 */     if (bedBlocked(pos, direction))
/* 1310 */       return Either.left(Player.BedSleepingProblem.OBSTRUCTED); 
/* 1314 */     if (canSetSpawn)
/* 1315 */       setRespawnPosition(new RespawnConfig(LevelData.RespawnData.of(level().dimension(), pos, getYRot(), getXRot()), false), true); 
/* 1318 */     if (!canSleep)
/* 1319 */       return Either.left(rule.asProblem()); 
/* 1322 */     if (!isCreative()) {
/* 1323 */       double hRange = 8.0D;
/* 1324 */       double vRange = 5.0D;
/* 1325 */       Vec3 bedCenter = Vec3.atBottomCenterOf((Vec3i)pos);
/* 1326 */       List<Monster> monsters = level().getEntitiesOfClass(Monster.class, new AABB(bedCenter.x() - 8.0D, bedCenter.y() - 5.0D, bedCenter.z() - 8.0D, bedCenter.x() + 8.0D, bedCenter.y() + 5.0D, bedCenter.z() + 8.0D), monster -> monster.isPreventingPlayerRest(level(), this));
/* 1327 */       if (!monsters.isEmpty())
/* 1328 */         return Either.left(Player.BedSleepingProblem.NOT_SAFE); 
/*      */     } 
/* 1332 */     Either<Player.BedSleepingProblem, Unit> result = super.startSleepInBed(pos).ifRight(unit -> {
/*      */           awardStat(Stats.SLEEP_IN_BED);
/*      */           CriteriaTriggers.SLEPT_IN_BED.trigger(this);
/*      */         });
/* 1336 */     if (!level().canSleepThroughNights())
/* 1337 */       sendOverlayMessage((Component)Component.translatable("sleep.not_possible")); 
/* 1340 */     level().updateSleepingPlayerList();
/* 1341 */     return result;
/*      */   }
/*      */   
/*      */   public void startSleeping(BlockPos bedPosition) {
/* 1346 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/* 1347 */     super.startSleeping(bedPosition);
/*      */   }
/*      */   
/*      */   private boolean bedInRange(BlockPos pos, Direction direction) {
/* 1351 */     return (isReachableBedBlock(pos) || isReachableBedBlock(pos.relative(direction.getOpposite())));
/*      */   }
/*      */   
/*      */   private boolean isReachableBedBlock(BlockPos bedBlockPos) {
/* 1355 */     Vec3 bedBlockCenter = Vec3.atBottomCenterOf((Vec3i)bedBlockPos);
/* 1356 */     return (Math.abs(getX() - bedBlockCenter.x()) <= 3.0D && Math.abs(getY() - bedBlockCenter.y()) <= 2.0D && Math.abs(getZ() - bedBlockCenter.z()) <= 3.0D);
/*      */   }
/*      */   
/*      */   private boolean bedBlocked(BlockPos pos, Direction direction) {
/* 1360 */     BlockPos above = pos.above();
/* 1361 */     return (!freeAt(above) || !freeAt(above.relative(direction.getOpposite())));
/*      */   }
/*      */   
/*      */   public void stopSleepInBed(boolean forcefulWakeUp, boolean updateLevelList) {
/* 1366 */     if (isSleeping())
/* 1367 */       level().getChunkSource().sendToTrackingPlayersAndSelf((Entity)this, (Packet<? super ClientGamePacketListener>)new ClientboundAnimatePacket((Entity)this, 2)); 
/* 1369 */     super.stopSleepInBed(forcefulWakeUp, updateLevelList);
/* 1370 */     if (this.connection != null)
/* 1371 */       this.connection.teleport(getX(), getY(), getZ(), getYRot(), getXRot()); 
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 1377 */     return (super.isInvulnerableTo(level, source) || (isChangingDimension() && !source.is(DamageTypes.ENDER_PEARL)) || !this.connection.hasClientLoaded());
/*      */   }
/*      */   
/*      */   protected void onChangedBlock(ServerLevel level, BlockPos pos) {
/* 1382 */     if (!isSpectator())
/* 1383 */       super.onChangedBlock(level, pos); 
/*      */   }
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 1389 */     if (this.spawnExtraParticlesOnFall && onGround && this.fallDistance > 0.0D) {
/* 1390 */       Vec3 centered = Vec3.atCenterOf((Vec3i)pos).add(0.0D, 0.5D, 0.0D);
/* 1391 */       int particles = (int)Mth.clamp(50.0D * this.fallDistance, 0.0D, 200.0D);
/* 1393 */       level().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, onState), centered.x, centered.y, centered.z, particles, 0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D, 0.15000000596046448D);
/* 1394 */       this.spawnExtraParticlesOnFall = false;
/*      */     } 
/* 1397 */     super.checkFallDamage(ya, onGround, onState, pos);
/*      */   }
/*      */   
/*      */   public void onExplosionHit(Entity explosionCausedBy) {
/* 1402 */     super.onExplosionHit(explosionCausedBy);
/* 1403 */     this.currentExplosionImpactPos = position();
/* 1404 */     this.currentExplosionCause = explosionCausedBy;
/* 1405 */     setIgnoreFallDamageFromCurrentImpulse((explosionCausedBy != null && explosionCausedBy.is(EntityTypes.WIND_CHARGE)), position());
/*      */   }
/*      */   
/*      */   protected void pushEntities() {
/* 1411 */     if (level().tickRateManager().runsNormally())
/* 1412 */       super.pushEntities(); 
/*      */   }
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {
/* 1419 */     this.connection.send((Packet)new ClientboundBlockUpdatePacket((BlockGetter)level(), sign.getBlockPos()));
/* 1420 */     this.connection.send((Packet)new ClientboundOpenSignEditorPacket(sign.getBlockPos(), isFrontText));
/*      */   }
/*      */   
/*      */   public void openDialog(Holder<Dialog> dialog) {
/* 1425 */     this.connection.send((Packet)new ClientboundShowDialogPacket(dialog));
/*      */   }
/*      */   
/*      */   private void nextContainerCounter() {
/* 1432 */     this.containerCounter = this.containerCounter % 100 + 1;
/*      */   }
/*      */   
/*      */   public OptionalInt openMenu(MenuProvider provider) {
/* 1437 */     if (provider == null)
/* 1438 */       return OptionalInt.empty(); 
/* 1441 */     if (this.containerMenu != this.inventoryMenu)
/* 1442 */       closeContainer(); 
/* 1445 */     nextContainerCounter();
/* 1447 */     AbstractContainerMenu menu = provider.createMenu(this.containerCounter, getInventory(), this);
/* 1448 */     if (menu == null) {
/* 1449 */       if (isSpectator())
/* 1450 */         sendOverlayMessage((Component)Component.translatable("container.spectatorCantOpen").withStyle(ChatFormatting.RED)); 
/* 1452 */       return OptionalInt.empty();
/*      */     } 
/* 1454 */     this.connection.send((Packet)new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), provider.getDisplayName()));
/* 1455 */     initMenu(menu);
/* 1456 */     this.containerMenu = menu;
/* 1457 */     return OptionalInt.of(this.containerCounter);
/*      */   }
/*      */   
/*      */   public void sendMerchantOffers(int containerId, MerchantOffers offers, int merchantLevel, int merchantXp, boolean showProgressBar, boolean canRestock) {
/* 1462 */     this.connection.send((Packet)new ClientboundMerchantOffersPacket(containerId, offers, merchantLevel, merchantXp, showProgressBar, canRestock));
/*      */   }
/*      */   
/*      */   public void openHorseInventory(AbstractHorse horse, Container container) {
/* 1467 */     if (this.containerMenu != this.inventoryMenu)
/* 1468 */       closeContainer(); 
/* 1470 */     nextContainerCounter();
/* 1471 */     int inventoryColumns = horse.getInventoryColumns();
/* 1472 */     this.connection.send((Packet)new ClientboundMountScreenOpenPacket(this.containerCounter, inventoryColumns, horse.getId()));
/* 1473 */     this.containerMenu = (AbstractContainerMenu)new HorseInventoryMenu(this.containerCounter, getInventory(), container, horse, inventoryColumns);
/* 1474 */     initMenu(this.containerMenu);
/*      */   }
/*      */   
/*      */   public void openNautilusInventory(AbstractNautilus nautilus, Container container) {
/* 1479 */     if (this.containerMenu != this.inventoryMenu)
/* 1480 */       closeContainer(); 
/* 1482 */     nextContainerCounter();
/* 1483 */     int inventoryColumns = nautilus.getInventoryColumns();
/* 1484 */     this.connection.send((Packet)new ClientboundMountScreenOpenPacket(this.containerCounter, inventoryColumns, nautilus.getId()));
/* 1485 */     this.containerMenu = (AbstractContainerMenu)new NautilusInventoryMenu(this.containerCounter, getInventory(), container, nautilus, inventoryColumns);
/* 1486 */     initMenu(this.containerMenu);
/*      */   }
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {
/* 1491 */     if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/* 1494 */       if (WrittenBookContent.resolveForItem(itemStack, ResolutionContext.create(createCommandSourceStack()), (HolderLookup.Provider)registryAccess()))
/* 1495 */         this.containerMenu.broadcastChanges(); 
/* 1498 */       this.connection.send((Packet)new ClientboundOpenBookPacket(hand));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity commandBlock) {
/* 1504 */     this.connection.send((Packet)ClientboundBlockEntityDataPacket.create((BlockEntity)commandBlock, BlockEntity::saveCustomOnly));
/*      */   }
/*      */   
/*      */   public void closeContainer() {
/* 1509 */     this.connection.send((Packet)new ClientboundContainerClosePacket(this.containerMenu.containerId));
/* 1510 */     doCloseContainer();
/*      */   }
/*      */   
/*      */   public void doCloseContainer() {
/* 1515 */     this.containerMenu.removed(this);
/* 1517 */     this.inventoryMenu.transferState(this.containerMenu);
/* 1518 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */   }
/*      */   
/*      */   public void rideTick() {
/* 1523 */     double preX = getX();
/* 1524 */     double preY = getY();
/* 1525 */     double preZ = getZ();
/* 1526 */     super.rideTick();
/* 1527 */     checkRidingStatistics(getX() - preX, getY() - preY, getZ() - preZ);
/*      */   }
/*      */   
/*      */   public void checkMovementStatistics(double dx, double dy, double dz) {
/* 1531 */     if (isPassenger() || didNotMove(dx, dy, dz))
/*      */       return; 
/* 1535 */     if (isSwimming()) {
/* 1536 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1537 */       if (distance > 0) {
/* 1538 */         awardStat(Stats.SWIM_ONE_CM, distance);
/* 1539 */         causeFoodExhaustion(0.01F * distance * 0.01F);
/*      */       } 
/* 1541 */     } else if (isEyeInFluid(FluidTags.WATER)) {
/* 1542 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1543 */       if (distance > 0) {
/* 1544 */         awardStat(Stats.WALK_UNDER_WATER_ONE_CM, distance);
/* 1545 */         causeFoodExhaustion(0.01F * distance * 0.01F);
/*      */       } 
/* 1547 */     } else if (isInWater()) {
/* 1548 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1549 */       if (horizontalDistance > 0) {
/* 1550 */         awardStat(Stats.WALK_ON_WATER_ONE_CM, horizontalDistance);
/* 1551 */         causeFoodExhaustion(0.01F * horizontalDistance * 0.01F);
/*      */       } 
/* 1553 */     } else if (onClimbable()) {
/* 1554 */       if (dy > 0.0D)
/* 1555 */         awardStat(Stats.CLIMB_ONE_CM, (int)Math.round(dy * 100.0D)); 
/* 1557 */     } else if (onGround()) {
/* 1558 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1559 */       if (horizontalDistance > 0)
/* 1560 */         if (isSprinting()) {
/* 1561 */           awardStat(Stats.SPRINT_ONE_CM, horizontalDistance);
/* 1562 */           causeFoodExhaustion(0.1F * horizontalDistance * 0.01F);
/* 1563 */         } else if (isCrouching()) {
/* 1564 */           awardStat(Stats.CROUCH_ONE_CM, horizontalDistance);
/* 1565 */           causeFoodExhaustion(0.0F * horizontalDistance * 0.01F);
/*      */         } else {
/* 1567 */           awardStat(Stats.WALK_ONE_CM, horizontalDistance);
/* 1568 */           causeFoodExhaustion(0.0F * horizontalDistance * 0.01F);
/*      */         }  
/* 1571 */     } else if (isFallFlying()) {
/* 1572 */       int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1573 */       awardStat(Stats.AVIATE_ONE_CM, distance);
/*      */     } else {
/* 1575 */       int horizontalDistance = Math.round((float)Math.sqrt(dx * dx + dz * dz) * 100.0F);
/* 1576 */       if (horizontalDistance > 25)
/* 1577 */         awardStat(Stats.FLY_ONE_CM, horizontalDistance); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkRidingStatistics(double dx, double dy, double dz) {
/* 1583 */     if (!isPassenger() || didNotMove(dx, dy, dz))
/*      */       return; 
/* 1587 */     int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
/* 1588 */     Entity vehicle = getVehicle();
/* 1589 */     if (vehicle instanceof net.minecraft.world.entity.vehicle.minecart.AbstractMinecart) {
/* 1590 */       awardStat(Stats.MINECART_ONE_CM, distance);
/* 1591 */     } else if (vehicle instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 1592 */       awardStat(Stats.BOAT_ONE_CM, distance);
/* 1593 */     } else if (vehicle instanceof net.minecraft.world.entity.animal.pig.Pig) {
/* 1594 */       awardStat(Stats.PIG_ONE_CM, distance);
/* 1595 */     } else if (vehicle instanceof AbstractHorse) {
/* 1596 */       awardStat(Stats.HORSE_ONE_CM, distance);
/* 1597 */     } else if (vehicle instanceof net.minecraft.world.entity.monster.Strider) {
/* 1598 */       awardStat(Stats.STRIDER_ONE_CM, distance);
/* 1599 */     } else if (vehicle instanceof net.minecraft.world.entity.animal.happyghast.HappyGhast) {
/* 1600 */       awardStat(Stats.HAPPY_GHAST_ONE_CM, distance);
/* 1601 */     } else if (vehicle instanceof AbstractNautilus) {
/* 1602 */       awardStat(Stats.NAUTILUS_ONE_CM, distance);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean didNotMove(double dx, double dy, double dz) {
/* 1607 */     return (dx == 0.0D && dy == 0.0D && dz == 0.0D);
/*      */   }
/*      */   
/*      */   public void awardStat(Stat<?> stat, int count) {
/* 1612 */     this.stats.increment(this, stat, count);
/* 1613 */     level().getScoreboard().forAllObjectives((ObjectiveCriteria)stat, (ScoreHolder)this, score -> score.add(count));
/*      */   }
/*      */   
/*      */   public void resetStat(Stat<?> stat) {
/* 1618 */     this.stats.setValue(this, stat, 0);
/* 1619 */     level().getScoreboard().forAllObjectives((ObjectiveCriteria)stat, (ScoreHolder)this, ScoreAccess::reset);
/*      */   }
/*      */   
/*      */   public int awardRecipes(Collection<RecipeHolder<?>> recipes) {
/* 1624 */     return this.recipeBook.addRecipes(recipes, this);
/*      */   }
/*      */   
/*      */   public void triggerRecipeCrafted(RecipeHolder<?> recipe, List<ItemStack> itemStacks) {
/* 1629 */     CriteriaTriggers.RECIPE_CRAFTED.trigger(this, recipe.id(), itemStacks);
/*      */   }
/*      */   
/*      */   public void awardRecipesByKey(List<ResourceKey<Recipe<?>>> recipeIds) {
/* 1636 */     List<RecipeHolder<?>> recipes = (List<RecipeHolder<?>>)recipeIds.stream().flatMap(id -> this.server.getRecipeManager().byKey(id).stream()).collect(Collectors.toList());
/* 1637 */     awardRecipes(recipes);
/*      */   }
/*      */   
/*      */   public int resetRecipes(Collection<RecipeHolder<?>> recipe) {
/* 1642 */     return this.recipeBook.removeRecipes(recipe, this);
/*      */   }
/*      */   
/*      */   public void jumpFromGround() {
/* 1647 */     super.jumpFromGround();
/* 1649 */     awardStat(Stats.JUMP);
/* 1650 */     if (isSprinting()) {
/* 1651 */       causeFoodExhaustion(0.2F);
/*      */     } else {
/* 1653 */       causeFoodExhaustion(0.05F);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void giveExperiencePoints(int i) {
/* 1659 */     if (i == 0)
/*      */       return; 
/* 1662 */     super.giveExperiencePoints(i);
/* 1663 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void disconnect() {
/* 1667 */     this.disconnected = true;
/* 1668 */     ejectPassengers();
/* 1669 */     if (isSleeping())
/* 1670 */       stopSleepInBed(true, false); 
/*      */   }
/*      */   
/*      */   public boolean hasDisconnected() {
/* 1675 */     return this.disconnected;
/*      */   }
/*      */   
/*      */   public void resetSentInfo() {
/* 1679 */     this.lastSentHealth = -1.0E8F;
/*      */   }
/*      */   
/*      */   protected void completeUsingItem() {
/* 1684 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 1685 */       this.connection.send((Packet)new ClientboundEntityEventPacket((Entity)this, (byte)9));
/* 1686 */       super.completeUsingItem();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 1692 */     super.lookAt(anchor, pos);
/* 1693 */     this.connection.send((Packet)new ClientboundPlayerLookAtPacket(anchor, pos.x, pos.y, pos.z));
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor fromAnchor, Entity entity, EntityAnchorArgument.Anchor toAnchor) {
/* 1697 */     Vec3 pos = toAnchor.apply(entity);
/* 1698 */     super.lookAt(fromAnchor, pos);
/* 1699 */     this.connection.send((Packet)new ClientboundPlayerLookAtPacket(fromAnchor, entity, toAnchor));
/*      */   }
/*      */   
/*      */   public void restoreFrom(ServerPlayer oldPlayer, boolean restoreAll) {
/* 1703 */     this.wardenSpawnTracker = oldPlayer.wardenSpawnTracker;
/* 1704 */     this.chatSession = oldPlayer.chatSession;
/* 1707 */     this.gameMode.setGameModeForPlayer(oldPlayer.gameMode
/* 1708 */         .getGameModeForPlayer(), oldPlayer.gameMode
/* 1709 */         .getPreviousGameModeForPlayer());
/* 1711 */     onUpdateAbilities();
/* 1712 */     getAttributes().assignBaseValues(oldPlayer.getAttributes());
/* 1714 */     if (restoreAll) {
/* 1715 */       getAttributes().assignPermanentModifiers(oldPlayer.getAttributes());
/* 1716 */       setHealth(oldPlayer.getHealth());
/* 1717 */       this.foodData = oldPlayer.foodData;
/* 1719 */       for (MobEffectInstance effect : oldPlayer.getActiveEffects())
/* 1720 */         addEffect(new MobEffectInstance(effect)); 
/* 1723 */       transferInventoryXpAndScore(oldPlayer);
/* 1724 */       this.portalProcess = oldPlayer.portalProcess;
/*      */     } else {
/* 1726 */       setHealth(getMaxHealth());
/* 1728 */       if (((Boolean)level().getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue() || oldPlayer.isSpectator())
/* 1729 */         transferInventoryXpAndScore(oldPlayer); 
/*      */     } 
/* 1733 */     this.enchantmentSeed = oldPlayer.enchantmentSeed;
/* 1734 */     this.enderChestInventory = oldPlayer.enderChestInventory;
/* 1735 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, oldPlayer.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 1736 */     this.lastSentExp = -1;
/* 1737 */     this.lastSentHealth = -1.0F;
/* 1738 */     this.lastSentFood = -1;
/* 1739 */     this.recipeBook.copyOverData(oldPlayer.recipeBook);
/* 1740 */     this.seenCredits = oldPlayer.seenCredits;
/* 1741 */     this.enteredNetherPosition = oldPlayer.enteredNetherPosition;
/* 1742 */     this.currentExplosionImpactPos = oldPlayer.currentExplosionImpactPos;
/* 1743 */     this.currentExplosionCause = oldPlayer.currentExplosionCause;
/* 1744 */     this.chunkTrackingView = oldPlayer.chunkTrackingView;
/* 1745 */     this.requestedDebugSubscriptions = oldPlayer.requestedDebugSubscriptions;
/* 1747 */     setShoulderEntityLeft(oldPlayer.getShoulderEntityLeft());
/* 1748 */     setShoulderEntityRight(oldPlayer.getShoulderEntityRight());
/* 1749 */     setLastDeathLocation(oldPlayer.getLastDeathLocation());
/* 1750 */     waypointIcon().copyFrom(oldPlayer.waypointIcon());
/*      */   }
/*      */   
/*      */   private void transferInventoryXpAndScore(Player oldPlayer) {
/* 1754 */     getInventory().replaceWith(oldPlayer.getInventory());
/* 1755 */     this.experienceLevel = oldPlayer.experienceLevel;
/* 1756 */     this.totalExperience = oldPlayer.totalExperience;
/* 1757 */     this.experienceProgress = oldPlayer.experienceProgress;
/* 1758 */     setScore(oldPlayer.getScore());
/*      */   }
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance effect, Entity source) {
/* 1763 */     super.onEffectAdded(effect, source);
/* 1764 */     this.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), effect, true));
/* 1766 */     if (effect.is(MobEffects.LEVITATION)) {
/* 1767 */       this.levitationStartTime = this.tickCount;
/* 1768 */       this.levitationStartPos = position();
/*      */     } 
/* 1771 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, source);
/*      */   }
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance effect, boolean doRefreshAttributes, Entity source) {
/* 1776 */     super.onEffectUpdated(effect, doRefreshAttributes, source);
/* 1777 */     this.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), effect, false));
/* 1779 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, source);
/*      */   }
/*      */   
/*      */   protected void onEffectsRemoved(Collection<MobEffectInstance> effects) {
/* 1784 */     super.onEffectsRemoved(effects);
/* 1785 */     for (MobEffectInstance effect : effects) {
/* 1786 */       this.connection.send((Packet)new ClientboundRemoveMobEffectPacket(getId(), effect.getEffect()));
/* 1788 */       if (effect.is(MobEffects.LEVITATION))
/* 1789 */         this.levitationStartPos = null; 
/*      */     } 
/* 1793 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this, null);
/*      */   }
/*      */   
/*      */   public void teleportTo(double x, double y, double z) {
/* 1798 */     this.connection.teleport(new PositionMoveRotation(new Vec3(x, y, z), Vec3.ZERO, 0.0F, 0.0F), Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }));
/*      */   }
/*      */   
/*      */   public void teleportRelative(double dx, double dy, double dz) {
/* 1803 */     this.connection.teleport(new PositionMoveRotation(new Vec3(dx, dy, dz), Vec3.ZERO, 0.0F, 0.0F), Relative.ALL);
/*      */   }
/*      */   
/*      */   public boolean teleportTo(ServerLevel level, double x, double y, double z, Set<Relative> relatives, float newYRot, float newXRot, boolean resetCamera) {
/* 1808 */     if (isSleeping())
/* 1809 */       stopSleepInBed(true, true); 
/* 1812 */     if (resetCamera)
/* 1813 */       setCamera((Entity)this); 
/* 1816 */     boolean success = super.teleportTo(level, x, y, z, relatives, newYRot, newXRot, resetCamera);
/* 1817 */     if (success) {
/* 1818 */       setYHeadRot(relatives.contains(Relative.Y_ROT) ? (getYHeadRot() + newYRot) : newYRot);
/* 1819 */       this.connection.resetFlyingTicks();
/*      */     } 
/* 1821 */     return success;
/*      */   }
/*      */   
/*      */   public void snapTo(double x, double y, double z) {
/* 1826 */     super.snapTo(x, y, z);
/* 1827 */     this.connection.resetPosition();
/*      */   }
/*      */   
/*      */   public void crit(Entity entity) {
/* 1832 */     level().getChunkSource().sendToTrackingPlayersAndSelf((Entity)this, (Packet<? super ClientGamePacketListener>)new ClientboundAnimatePacket(entity, 4));
/*      */   }
/*      */   
/*      */   public void magicCrit(Entity entity) {
/* 1837 */     level().getChunkSource().sendToTrackingPlayersAndSelf((Entity)this, (Packet<? super ClientGamePacketListener>)new ClientboundAnimatePacket(entity, 5));
/*      */   }
/*      */   
/*      */   public void onUpdateAbilities() {
/* 1842 */     if (this.connection == null)
/*      */       return; 
/* 1845 */     this.connection.send((Packet)new ClientboundPlayerAbilitiesPacket(getAbilities()));
/* 1846 */     updateInvisibilityStatus();
/*      */   }
/*      */   
/*      */   public ServerLevel level() {
/* 1851 */     return (ServerLevel)super.level();
/*      */   }
/*      */   
/*      */   public boolean setGameMode(GameType mode) {
/* 1855 */     boolean wasSpectator = isSpectator();
/* 1856 */     if (!this.gameMode.changeGameModeForPlayer(mode))
/* 1857 */       return false; 
/* 1859 */     this.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, mode.getId()));
/* 1861 */     if (mode == GameType.SPECTATOR) {
/* 1862 */       removeEntitiesOnShoulder();
/* 1863 */       stopRiding();
/* 1864 */       stopUsingItem();
/* 1865 */       EnchantmentHelper.stopLocationBasedEffects((LivingEntity)this);
/*      */     } else {
/* 1867 */       setCamera((Entity)this);
/* 1868 */       if (wasSpectator)
/* 1869 */         EnchantmentHelper.runLocationChangedEffects(level(), (LivingEntity)this); 
/*      */     } 
/* 1873 */     onUpdateAbilities();
/* 1874 */     updateEffectVisibility();
/* 1875 */     return true;
/*      */   }
/*      */   
/*      */   public GameType gameMode() {
/* 1880 */     return this.gameMode.getGameModeForPlayer();
/*      */   }
/*      */   
/*      */   public CommandSource commandSource() {
/* 1884 */     return this.commandSource;
/*      */   }
/*      */   
/*      */   public CommandSourceStack createCommandSourceStack() {
/* 1888 */     return new CommandSourceStack(commandSource(), position(), getRotationVector(), level(), permissions(), getPlainTextName(), getDisplayName(), this.server, (Entity)this);
/*      */   }
/*      */   
/*      */   public void sendSystemMessage(Component message) {
/* 1893 */     sendSystemMessage(message, false);
/*      */   }
/*      */   
/*      */   public void sendOverlayMessage(Component message) {
/* 1898 */     sendSystemMessage(message, true);
/*      */   }
/*      */   
/*      */   public void sendBuildLimitMessage(boolean isTooHigh, int limit) {
/* 1902 */     sendOverlayMessage((Component)Component.translatable(isTooHigh ? "build.tooHigh" : "build.tooLow", new Object[] { Integer.valueOf(limit) }).withStyle(ChatFormatting.RED));
/*      */   }
/*      */   
/*      */   public void sendSpawnProtectionMessage(BlockPos pos) {
/* 1906 */     sendOverlayMessage((Component)Component.translatable("build.spawn_protection", new Object[] { pos.toShortString() }).withStyle(ChatFormatting.RED));
/*      */   }
/*      */   
/*      */   public void sendSystemMessage(Component message, boolean overlay) {
/* 1910 */     if (!acceptsSystemMessages(overlay))
/*      */       return; 
/* 1914 */     this.connection.send((Packet)new ClientboundSystemChatPacket(message, overlay), PacketSendListener.exceptionallySend(() -> {
/*      */             if (acceptsSystemMessages(false)) {
/*      */               int truncatedMessageSize = 256;
/*      */               String contents = message.getString(256);
/*      */               MutableComponent mutableComponent = Component.literal(contents).withStyle(ChatFormatting.YELLOW);
/*      */               return (Packet)new ClientboundSystemChatPacket((Component)Component.translatable("multiplayer.message_not_delivered", new Object[] { mutableComponent }).withStyle(ChatFormatting.RED), false);
/*      */             } 
/*      */             return null;
/*      */           }));
/*      */   }
/*      */   
/*      */   public void sendChatMessage(OutgoingChatMessage message, boolean filtered, ChatType.Bound chatType) {
/* 1926 */     if (acceptsChatMessages())
/* 1927 */       message.sendToPlayer(this, filtered, chatType); 
/*      */   }
/*      */   
/*      */   public String getIpAddress() {
/* 1932 */     SocketAddress remoteAddress = this.connection.getRemoteAddress();
/* 1933 */     if (remoteAddress instanceof InetSocketAddress) {
/* 1933 */       InetSocketAddress ipSocketAddress = (InetSocketAddress)remoteAddress;
/* 1934 */       return InetAddresses.toAddrString(ipSocketAddress.getAddress());
/*      */     } 
/* 1936 */     return "<unknown>";
/*      */   }
/*      */   
/*      */   public void updateOptions(ClientInformation information) {
/* 1940 */     this.language = information.language();
/* 1941 */     this.requestedViewDistance = information.viewDistance();
/* 1942 */     this.chatVisibility = information.chatVisibility();
/* 1943 */     this.canChatColor = information.chatColors();
/* 1944 */     this.textFilteringEnabled = information.textFilteringEnabled();
/* 1945 */     this.allowsListing = information.allowsListing();
/* 1946 */     this.particleStatus = information.particleStatus();
/* 1948 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)information.modelCustomisation()));
/* 1949 */     getEntityData().set(DATA_PLAYER_MAIN_HAND, information.mainHand());
/*      */   }
/*      */   
/*      */   public ClientInformation clientInformation() {
/* 1953 */     int modelCustomization = ((Byte)getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION)).byteValue();
/* 1955 */     return new ClientInformation(this.language, this.requestedViewDistance, this.chatVisibility, this.canChatColor, modelCustomization, 
/*      */         
/* 1961 */         getMainArm(), this.textFilteringEnabled, this.allowsListing, this.particleStatus);
/*      */   }
/*      */   
/*      */   public boolean canChatInColor() {
/* 1969 */     return this.canChatColor;
/*      */   }
/*      */   
/*      */   public ChatVisiblity getChatVisibility() {
/* 1973 */     return this.chatVisibility;
/*      */   }
/*      */   
/*      */   private boolean acceptsSystemMessages(boolean overlay) {
/* 1977 */     if (this.chatVisibility == ChatVisiblity.HIDDEN)
/* 1978 */       return overlay; 
/* 1980 */     return true;
/*      */   }
/*      */   
/*      */   private boolean acceptsChatMessages() {
/* 1984 */     return (this.chatVisibility == ChatVisiblity.FULL);
/*      */   }
/*      */   
/*      */   public int requestedViewDistance() {
/* 1988 */     return this.requestedViewDistance;
/*      */   }
/*      */   
/*      */   public void sendServerStatus(ServerStatus status) {
/* 1992 */     this.connection.send((Packet)new ClientboundServerDataPacket(status.description(), status.favicon().map(ServerStatus.Favicon::iconBytes)));
/*      */   }
/*      */   
/*      */   public PermissionSet permissions() {
/* 1997 */     return (PermissionSet)this.server.getProfilePermissions(nameAndId());
/*      */   }
/*      */   
/*      */   public void resetLastActionTime() {
/* 2001 */     this.lastActionTime = Util.getMillis();
/*      */   }
/*      */   
/*      */   public ServerStatsCounter getStats() {
/* 2005 */     return this.stats;
/*      */   }
/*      */   
/*      */   public ServerRecipeBook getRecipeBook() {
/* 2009 */     return this.recipeBook;
/*      */   }
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/* 2014 */     if (isSpectator()) {
/* 2015 */       removeEffectParticles();
/* 2016 */       setInvisible(true);
/*      */     } else {
/* 2018 */       super.updateInvisibilityStatus();
/*      */     } 
/*      */   }
/*      */   
/*      */   public Entity getCamera() {
/* 2023 */     return (this.camera == null) ? (Entity)this : this.camera;
/*      */   }
/*      */   
/*      */   public void setCamera(Entity newCamera) {
/* 2027 */     Entity oldCamera = getCamera();
/* 2028 */     this.camera = (newCamera == null) ? (Entity)this : newCamera;
/* 2030 */     if (oldCamera != this.camera) {
/* 2031 */       Level level = this.camera.level();
/* 2031 */       if (level instanceof ServerLevel) {
/* 2031 */         ServerLevel serverLevel = (ServerLevel)level;
/* 2032 */         teleportTo(serverLevel, this.camera.getX(), this.camera.getY(), this.camera.getZ(), Set.of(), getYRot(), getXRot(), false);
/*      */       } 
/* 2034 */       if (newCamera != null)
/* 2036 */         level().getChunkSource().move(this); 
/* 2038 */       this.connection.send((Packet)new ClientboundSetCameraPacket(this.camera));
/* 2039 */       this.connection.resetPosition();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void processPortalCooldown() {
/* 2045 */     if (!this.isChangingDimension)
/* 2046 */       super.processPortalCooldown(); 
/*      */   }
/*      */   
/*      */   public long getLastActionTime() {
/* 2051 */     return this.lastActionTime;
/*      */   }
/*      */   
/*      */   public Component getTabListDisplayName() {
/* 2056 */     return null;
/*      */   }
/*      */   
/*      */   public int getTabListOrder() {
/* 2061 */     return 0;
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand hand) {
/* 2066 */     super.swing(hand);
/* 2067 */     resetAttackStrengthTicker();
/*      */   }
/*      */   
/*      */   public boolean isChangingDimension() {
/* 2071 */     return this.isChangingDimension;
/*      */   }
/*      */   
/*      */   public void hasChangedDimension() {
/* 2075 */     this.isChangingDimension = false;
/*      */   }
/*      */   
/*      */   public PlayerAdvancements getAdvancements() {
/* 2079 */     return this.advancements;
/*      */   }
/*      */   
/*      */   public RespawnConfig getRespawnConfig() {
/* 2083 */     return this.respawnConfig;
/*      */   }
/*      */   
/*      */   public void copyRespawnPosition(ServerPlayer player) {
/* 2087 */     setRespawnPosition(player.respawnConfig, false);
/*      */   }
/*      */   
/*      */   public void setRespawnPosition(RespawnConfig respawnConfig, boolean showMessage) {
/* 2091 */     if (showMessage && respawnConfig != null && !respawnConfig.isSamePosition(this.respawnConfig))
/* 2092 */       sendSystemMessage(SPAWN_SET_MESSAGE); 
/* 2094 */     this.respawnConfig = respawnConfig;
/*      */   }
/*      */   
/*      */   public SectionPos getLastSectionPos() {
/* 2098 */     return this.lastSectionPos;
/*      */   }
/*      */   
/*      */   public void setLastSectionPos(SectionPos lastSectionPos) {
/* 2102 */     this.lastSectionPos = lastSectionPos;
/*      */   }
/*      */   
/*      */   public ChunkTrackingView getChunkTrackingView() {
/* 2106 */     return this.chunkTrackingView;
/*      */   }
/*      */   
/*      */   public void setChunkTrackingView(ChunkTrackingView chunkTrackingView) {
/* 2110 */     this.chunkTrackingView = chunkTrackingView;
/*      */   }
/*      */   
/*      */   public ItemEntity drop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/* 2115 */     ItemEntity entity = super.drop(itemStack, randomly, thrownFromHand);
/* 2117 */     if (thrownFromHand) {
/* 2118 */       ItemStack droppedItemStack = (entity != null) ? entity.getItem() : ItemStack.EMPTY;
/* 2119 */       if (!droppedItemStack.isEmpty()) {
/* 2120 */         awardStat(Stats.ITEM_DROPPED.get(droppedItemStack.getItem()), itemStack.getCount());
/* 2121 */         awardStat(Stats.DROP);
/*      */       } 
/*      */     } 
/* 2125 */     return entity;
/*      */   }
/*      */   
/*      */   public TextFilter getTextFilter() {
/* 2129 */     return this.textFilter;
/*      */   }
/*      */   
/*      */   public void setServerLevel(ServerLevel level) {
/* 2133 */     setLevel(level);
/* 2134 */     this.gameMode.setLevel(level);
/*      */   }
/*      */   
/*      */   private static GameType readPlayerMode(ValueInput playerInput, String modeTag) {
/* 2138 */     return playerInput.read(modeTag, GameType.LEGACY_ID_CODEC).orElse(null);
/*      */   }
/*      */   
/*      */   private GameType calculateGameModeForNewPlayer(GameType loadedGameType) {
/* 2143 */     GameType forcedGameType = this.server.getForcedGameType();
/* 2144 */     if (forcedGameType != null)
/* 2145 */       return forcedGameType; 
/* 2148 */     return (loadedGameType != null) ? loadedGameType : this.server.getDefaultGameType();
/*      */   }
/*      */   
/*      */   private void storeGameTypes(ValueOutput playerOutput) {
/* 2152 */     playerOutput.store("playerGameType", GameType.LEGACY_ID_CODEC, this.gameMode.getGameModeForPlayer());
/* 2153 */     GameType previousGameMode = this.gameMode.getPreviousGameModeForPlayer();
/* 2154 */     playerOutput.storeNullable("previousPlayerGameType", GameType.LEGACY_ID_CODEC, previousGameMode);
/*      */   }
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/* 2159 */     return this.textFilteringEnabled;
/*      */   }
/*      */   
/*      */   public boolean shouldFilterMessageTo(ServerPlayer serverPlayer) {
/* 2164 */     if (serverPlayer == this)
/* 2165 */       return false; 
/* 2167 */     return (this.textFilteringEnabled || serverPlayer.textFilteringEnabled);
/*      */   }
/*      */   
/*      */   public boolean mayInteract(ServerLevel level, BlockPos pos) {
/* 2172 */     return (super.mayInteract(level, pos) && level.mayInteract((Entity)this, pos));
/*      */   }
/*      */   
/*      */   protected void updateUsingItem(ItemStack useItem) {
/* 2177 */     CriteriaTriggers.USING_ITEM.trigger(this, useItem);
/* 2178 */     super.updateUsingItem(useItem);
/*      */   }
/*      */   
/*      */   public void drop(boolean all) {
/* 2182 */     Inventory inventory = getInventory();
/* 2183 */     ItemStack removed = inventory.removeFromSelected(all);
/* 2184 */     this.containerMenu.findSlot((Container)inventory, inventory.getSelectedSlot()).ifPresent(slotIndex -> this.containerMenu.setRemoteSlot(slotIndex, inventory.getSelectedItem()));
/* 2187 */     if (this.useItem.isEmpty())
/* 2188 */       stopUsingItem(); 
/* 2190 */     drop(removed, false, true);
/*      */   }
/*      */   
/*      */   public void handleExtraItemsCreatedOnUse(ItemStack extraItems) {
/* 2195 */     if (!getInventory().add(extraItems))
/* 2196 */       drop(extraItems, false); 
/*      */   }
/*      */   
/*      */   public boolean allowsListing() {
/* 2201 */     return this.allowsListing;
/*      */   }
/*      */   
/*      */   public Optional<WardenSpawnTracker> getWardenSpawnTracker() {
/* 2206 */     return Optional.of(this.wardenSpawnTracker);
/*      */   }
/*      */   
/*      */   public void setSpawnExtraParticlesOnFall(boolean toggle) {
/* 2210 */     this.spawnExtraParticlesOnFall = toggle;
/*      */   }
/*      */   
/*      */   public void onItemPickup(ItemEntity entity) {
/* 2215 */     super.onItemPickup(entity);
/* 2216 */     Entity thrower = entity.getOwner();
/* 2217 */     if (thrower != null)
/* 2218 */       CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.trigger(this, entity.getItem(), thrower); 
/*      */   }
/*      */   
/*      */   public void setChatSession(RemoteChatSession chatSession) {
/* 2223 */     this.chatSession = chatSession;
/*      */   }
/*      */   
/*      */   public RemoteChatSession getChatSession() {
/* 2227 */     if (this.chatSession != null && this.chatSession.hasExpired())
/* 2228 */       return null; 
/* 2230 */     return this.chatSession;
/*      */   }
/*      */   
/*      */   public void indicateDamage(double xd, double zd) {
/* 2235 */     this.hurtDir = (float)(Mth.atan2(zd, xd) * 57.2957763671875D - getYRot());
/* 2236 */     this.connection.send((Packet)new ClientboundHurtAnimationPacket((LivingEntity)this));
/*      */   }
/*      */   
/*      */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 2241 */     if (super.startRiding(entityToRide, force, sendEventAndTriggers)) {
/* 2243 */       entityToRide.positionRider((Entity)this);
/* 2244 */       this.connection.teleport(new PositionMoveRotation(position(), Vec3.ZERO, 0.0F, 0.0F), Relative.ROTATION);
/* 2245 */       if (entityToRide instanceof LivingEntity) {
/* 2245 */         LivingEntity livingEntity = (LivingEntity)entityToRide;
/* 2246 */         this.server.getPlayerList().sendActiveEffects(livingEntity, this.connection);
/*      */       } 
/* 2249 */       this.connection.send((Packet)new ClientboundSetPassengersPacket(entityToRide));
/* 2250 */       return true;
/*      */     } 
/* 2252 */     return false;
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/* 2257 */     Entity oldVehicle = getVehicle();
/* 2258 */     super.removeVehicle();
/* 2259 */     if (oldVehicle instanceof LivingEntity) {
/* 2259 */       LivingEntity livingEntity = (LivingEntity)oldVehicle;
/* 2260 */       for (MobEffectInstance effect : livingEntity.getActiveEffects())
/* 2261 */         this.connection.send((Packet)new ClientboundRemoveMobEffectPacket(oldVehicle.getId(), effect.getEffect())); 
/*      */     } 
/* 2264 */     if (oldVehicle != null)
/* 2266 */       this.connection.send((Packet)new ClientboundSetPassengersPacket(oldVehicle)); 
/*      */   }
/*      */   
/*      */   public CommonPlayerSpawnInfo createCommonSpawnInfo(ServerLevel level) {
/* 2271 */     return new CommonPlayerSpawnInfo(level
/* 2272 */         .dimensionTypeRegistration(), level
/* 2273 */         .dimension(), 
/* 2274 */         BiomeManager.obfuscateSeed(level.getSeed()), this.gameMode
/* 2275 */         .getGameModeForPlayer(), this.gameMode
/* 2276 */         .getPreviousGameModeForPlayer(), level
/* 2277 */         .isDebug(), level
/* 2278 */         .isFlat(), 
/* 2279 */         getLastDeathLocation(), 
/* 2280 */         getPortalCooldown(), level
/* 2281 */         .getSeaLevel());
/*      */   }
/*      */   
/*      */   public void setRaidOmenPosition(BlockPos raidOmenPosition) {
/* 2286 */     this.raidOmenPosition = raidOmenPosition;
/*      */   }
/*      */   
/*      */   public void clearRaidOmenPosition() {
/* 2290 */     this.raidOmenPosition = null;
/*      */   }
/*      */   
/*      */   public BlockPos getRaidOmenPosition() {
/* 2294 */     return this.raidOmenPosition;
/*      */   }
/*      */   
/*      */   public Vec3 getKnownMovement() {
/* 2299 */     Entity vehicle = getVehicle();
/* 2300 */     if (vehicle != null && vehicle.getControllingPassenger() != this)
/* 2302 */       return vehicle.getKnownMovement(); 
/* 2304 */     return this.lastKnownClientMovement;
/*      */   }
/*      */   
/*      */   public Vec3 getKnownSpeed() {
/* 2309 */     Entity vehicle = getVehicle();
/* 2310 */     if (vehicle != null && vehicle.getControllingPassenger() != this)
/* 2312 */       return vehicle.getKnownSpeed(); 
/* 2314 */     return this.lastKnownClientMovement;
/*      */   }
/*      */   
/*      */   public void setKnownMovement(Vec3 lastKnownClientMovement) {
/* 2318 */     this.lastKnownClientMovement = lastKnownClientMovement;
/*      */   }
/*      */   
/*      */   protected float getEnchantedDamage(Entity entity, float dmg, DamageSource damageSource) {
/* 2323 */     return EnchantmentHelper.modifyDamage(level(), getWeaponItem(), entity, damageSource, dmg);
/*      */   }
/*      */   
/*      */   public void onEquippedItemBroken(Item brokenItem, EquipmentSlot inSlot) {
/* 2328 */     super.onEquippedItemBroken(brokenItem, inSlot);
/* 2329 */     awardStat(Stats.ITEM_BROKEN.get(brokenItem));
/*      */   }
/*      */   
/*      */   public Input getLastClientInput() {
/* 2333 */     return this.lastClientInput;
/*      */   }
/*      */   
/*      */   public void setLastClientInput(Input lastClientInput) {
/* 2337 */     this.lastClientInput = lastClientInput;
/*      */   }
/*      */   
/*      */   public Vec3 getLastClientMoveIntent() {
/* 2341 */     float leftIntent = (this.lastClientInput.left() == this.lastClientInput.right()) ? 0.0F : (this.lastClientInput.left() ? 1.0F : -1.0F);
/* 2342 */     float forwardIntent = (this.lastClientInput.forward() == this.lastClientInput.backward()) ? 0.0F : (this.lastClientInput.forward() ? 1.0F : -1.0F);
/* 2343 */     return getInputVector(new Vec3(leftIntent, 0.0D, forwardIntent), 1.0F, getYRot());
/*      */   }
/*      */   
/*      */   public void registerEnderPearl(ThrownEnderpearl enderPearl) {
/* 2347 */     this.enderPearls.add(enderPearl);
/*      */   }
/*      */   
/*      */   public void deregisterEnderPearl(ThrownEnderpearl enderPearl) {
/* 2351 */     this.enderPearls.remove(enderPearl);
/*      */   }
/*      */   
/*      */   public Set<ThrownEnderpearl> getEnderPearls() {
/* 2355 */     return this.enderPearls;
/*      */   }
/*      */   
/*      */   public CompoundTag getShoulderEntityLeft() {
/* 2359 */     return this.shoulderEntityLeft;
/*      */   }
/*      */   
/*      */   protected void setShoulderEntityLeft(CompoundTag tag) {
/* 2363 */     this.shoulderEntityLeft = tag;
/* 2364 */     setShoulderParrotLeft(extractParrotVariant(tag));
/*      */   }
/*      */   
/*      */   public CompoundTag getShoulderEntityRight() {
/* 2368 */     return this.shoulderEntityRight;
/*      */   }
/*      */   
/*      */   protected void setShoulderEntityRight(CompoundTag tag) {
/* 2372 */     this.shoulderEntityRight = tag;
/* 2373 */     setShoulderParrotRight(extractParrotVariant(tag));
/*      */   }
/*      */   
/*      */   public long registerAndUpdateEnderPearlTicket(ThrownEnderpearl enderpearl) {
/* 2377 */     Level level = enderpearl.level();
/* 2377 */     if (level instanceof ServerLevel) {
/* 2377 */       ServerLevel enderPearlLevel = (ServerLevel)level;
/* 2378 */       ChunkPos chunkPos = enderpearl.chunkPosition();
/* 2379 */       registerEnderPearl(enderpearl);
/* 2380 */       enderPearlLevel.resetEmptyTime();
/* 2381 */       return placeEnderPearlTicket(enderPearlLevel, chunkPos) - 1L;
/*      */     } 
/* 2383 */     return 0L;
/*      */   }
/*      */   
/*      */   public static long placeEnderPearlTicket(ServerLevel level, ChunkPos chunk) {
/* 2387 */     level.getChunkSource().addTicketWithRadius(TicketType.ENDER_PEARL, chunk, 2);
/* 2388 */     return TicketType.ENDER_PEARL.timeout();
/*      */   }
/*      */   
/*      */   public void requestDebugSubscriptions(Set<DebugSubscription<?>> subscriptions) {
/* 2392 */     this.requestedDebugSubscriptions = Set.copyOf(subscriptions);
/*      */   }
/*      */   
/*      */   public Set<DebugSubscription<?>> debugSubscriptions() {
/* 2396 */     if (!this.server.debugSubscribers().hasRequiredPermissions(this))
/* 2397 */       return Set.of(); 
/* 2399 */     return this.requestedDebugSubscriptions;
/*      */   }
/*      */   
/*      */   public static final class RespawnConfig extends Record {
/*      */     private final LevelData.RespawnData respawnData;
/*      */     
/*      */     private final boolean forced;
/*      */     
/*      */     public static final Codec<RespawnConfig> CODEC;
/*      */     
/*      */     public RespawnConfig(LevelData.RespawnData respawnData, boolean forced) {
/* 2402 */       this.respawnData = respawnData;
/* 2402 */       this.forced = forced;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2402	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2402	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2402	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public LevelData.RespawnData respawnData() {
/* 2402 */       return this.respawnData;
/*      */     }
/*      */     
/*      */     public boolean forced() {
/* 2402 */       return this.forced;
/*      */     }
/*      */     
/*      */     static {
/* 2406 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)LevelData.RespawnData.MAP_CODEC.forGetter(RespawnConfig::respawnData), (App)Codec.BOOL.optionalFieldOf("forced", Boolean.valueOf(false)).forGetter(RespawnConfig::forced)).apply((Applicative)i, RespawnConfig::new));
/*      */     }
/*      */     
/*      */     private static ResourceKey<Level> getDimensionOrDefault(RespawnConfig respawnConfig) {
/* 2412 */       return (respawnConfig != null) ? respawnConfig.respawnData().dimension() : Level.OVERWORLD;
/*      */     }
/*      */     
/*      */     public boolean isSamePosition(RespawnConfig other) {
/* 2416 */       return (other != null && this.respawnData.globalPos().equals(other.respawnData.globalPos()));
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class SavedPosition extends Record {
/*      */     private final Optional<ResourceKey<Level>> dimension;
/*      */     
/*      */     private final Optional<Vec3> position;
/*      */     
/*      */     private final Optional<Vec2> rotation;
/*      */     
/*      */     public static final MapCodec<SavedPosition> MAP_CODEC;
/*      */     
/*      */     public SavedPosition(Optional<ResourceKey<Level>> dimension, Optional<Vec3> position, Optional<Vec2> rotation) {
/* 2420 */       this.dimension = dimension;
/* 2420 */       this.position = position;
/* 2420 */       this.rotation = rotation;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2420	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2420	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2420	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Optional<ResourceKey<Level>> dimension() {
/* 2420 */       return this.dimension;
/*      */     }
/*      */     
/*      */     public Optional<Vec3> position() {
/* 2420 */       return this.position;
/*      */     }
/*      */     
/*      */     public Optional<Vec2> rotation() {
/* 2420 */       return this.rotation;
/*      */     }
/*      */     
/*      */     static {
/* 2425 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Level.RESOURCE_KEY_CODEC.optionalFieldOf("Dimension").forGetter(SavedPosition::dimension), (App)Vec3.CODEC.optionalFieldOf("Pos").forGetter(SavedPosition::position), (App)Vec2.CODEC.optionalFieldOf("Rotation").forGetter(SavedPosition::rotation)).apply((Applicative)i, SavedPosition::new));
/*      */     }
/*      */     
/* 2431 */     public static final SavedPosition EMPTY = new SavedPosition(Optional.empty(), Optional.empty(), Optional.empty());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */