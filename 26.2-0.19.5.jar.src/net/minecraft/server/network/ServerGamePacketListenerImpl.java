/*      */ package net.minecraft.server.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.primitives.Floats;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.ParseResults;
/*      */ import com.mojang.brigadier.StringReader;
/*      */ import com.mojang.brigadier.suggestion.Suggestions;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import java.net.SocketAddress;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.AdvancementHolder;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSigningContext;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.gametest.framework.GameTestInstance;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.DisconnectionDetails;
/*      */ import net.minecraft.network.HashedStack;
/*      */ import net.minecraft.network.PacketListener;
/*      */ import net.minecraft.network.TickablePacketListener;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.LastSeenMessages;
/*      */ import net.minecraft.network.chat.LastSeenMessagesValidator;
/*      */ import net.minecraft.network.chat.MessageSignature;
/*      */ import net.minecraft.network.chat.MessageSignatureCache;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.PlayerChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.SignableCommand;
/*      */ import net.minecraft.network.chat.SignedMessageBody;
/*      */ import net.minecraft.network.chat.SignedMessageChain;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketUtils;
/*      */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*      */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*      */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameRuleValuesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTestInstanceBlockStatus;
/*      */ import net.minecraft.network.protocol.game.GameProtocols;
/*      */ import net.minecraft.network.protocol.game.ServerGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundAttackPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEntityTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSelectBundleItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetGameRulePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetTestBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSpectatorActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundTestInstanceBlockActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*      */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*      */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.commands.FetchProfileCommand;
/*      */ import net.minecraft.server.commands.GameModeCommand;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.permissions.Permissions;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.util.FutureChain;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.SignatureValidator;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.TaskChainer;
/*      */ import net.minecraft.util.TickThrottler;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Avatar;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.player.PlayerModelPart;
/*      */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.AnvilMenu;
/*      */ import net.minecraft.world.inventory.BeaconMenu;
/*      */ import net.minecraft.world.inventory.CrafterMenu;
/*      */ import net.minecraft.world.inventory.MerchantMenu;
/*      */ import net.minecraft.world.inventory.RecipeBookMenu;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.ItemInstance;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.PiercingWeapon;
/*      */ import net.minecraft.world.item.component.WritableBookContent;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.BaseCommandBlock;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CommandBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CrafterBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ServerGamePacketListenerImpl extends ServerCommonPacketListenerImpl implements ServerGamePacketListener, ServerPlayerConnection, TickablePacketListener, GameProtocols.Context {
/*  228 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int NO_BLOCK_UPDATES_TO_ACK = -1;
/*      */   
/*      */   private static final int TRACKED_MESSAGE_DISCONNECT_THRESHOLD = 4096;
/*      */   
/*      */   private static final int MAXIMUM_FLYING_TICKS = 80;
/*      */   
/*      */   private static final int ATTACK_INDICATOR_TOLERANCE_TICKS = 5;
/*      */   
/*      */   public static final int CLIENT_LOADED_TIMEOUT_TIME = 60;
/*      */   
/*  235 */   private static final Component CHAT_VALIDATION_FAILED = (Component)Component.translatable("multiplayer.disconnect.chat_validation_failed");
/*      */   
/*  236 */   private static final Component INVALID_COMMAND_SIGNATURE = (Component)Component.translatable("chat.disabled.invalid_command_signature").withStyle(ChatFormatting.RED);
/*      */   
/*      */   private static final int MAX_COMMAND_SUGGESTIONS = 1000;
/*      */   
/*      */   public ServerPlayer player;
/*      */   
/*      */   public final PlayerChunkSender chunkSender;
/*      */   
/*      */   private int tickCount;
/*      */   
/*  241 */   private int ackBlockChangesUpTo = -1;
/*      */   
/*  243 */   private final TickThrottler dropSpamThrottler = new TickThrottler(20, 1480);
/*      */   
/*      */   private final TickThrottler chatSpamThrottler;
/*      */   
/*      */   private final TickThrottler commandSpamThrottler;
/*      */   
/*      */   private double firstGoodX;
/*      */   
/*      */   private double firstGoodY;
/*      */   
/*      */   private double firstGoodZ;
/*      */   
/*      */   private double lastGoodX;
/*      */   
/*      */   private double lastGoodY;
/*      */   
/*      */   private double lastGoodZ;
/*      */   
/*      */   private Entity lastVehicle;
/*      */   
/*      */   private double vehicleFirstGoodX;
/*      */   
/*      */   private double vehicleFirstGoodY;
/*      */   
/*      */   private double vehicleFirstGoodZ;
/*      */   
/*      */   private double vehicleLastGoodX;
/*      */   
/*      */   private double vehicleLastGoodY;
/*      */   
/*      */   private double vehicleLastGoodZ;
/*      */   
/*      */   private Vec3 awaitingPositionFromClient;
/*      */   
/*      */   private int awaitingTeleport;
/*      */   
/*      */   private int awaitingTeleportTime;
/*      */   
/*      */   private boolean clientIsFloating;
/*      */   
/*      */   private int aboveGroundTickCount;
/*      */   
/*      */   private boolean clientVehicleIsFloating;
/*      */   
/*      */   private int aboveGroundVehicleTickCount;
/*      */   
/*      */   private int receivedMovePacketCount;
/*      */   
/*      */   private int knownMovePacketCount;
/*      */   
/*      */   private boolean receivedMovementThisTick;
/*      */   
/*      */   private RemoteChatSession chatSession;
/*      */   
/*      */   private SignedMessageChain.Decoder signedMessageDecoder;
/*      */   
/*  275 */   private final LastSeenMessagesValidator lastSeenMessages = new LastSeenMessagesValidator(20);
/*      */   
/*      */   private int nextChatIndex;
/*      */   
/*  278 */   private final MessageSignatureCache messageSignatureCache = MessageSignatureCache.createDefault();
/*      */   
/*      */   private final FutureChain chatMessageChain;
/*      */   
/*      */   private boolean waitingForSwitchToConfig;
/*      */   
/*      */   private boolean waitingForRespawn;
/*      */   
/*      */   private int clientLoadedTimeoutTimer;
/*      */   
/*      */   public ServerGamePacketListenerImpl(MinecraftServer server, Connection connection, ServerPlayer player, CommonListenerCookie cookie) {
/*  287 */     super(server, connection, cookie);
/*  288 */     this.commandSpamThrottler = new TickThrottler(20, 20 * server.getCommandSpamThresholdSeconds());
/*  289 */     this.chatSpamThrottler = new TickThrottler(20, 20 * server.getChatSpamThresholdSeconds());
/*  290 */     restartClientLoadTimerAfterRespawn();
/*  292 */     this.chunkSender = new PlayerChunkSender(connection.isMemoryConnection());
/*  293 */     this.player = player;
/*  294 */     player.connection = this;
/*  296 */     player.getTextFilter().join();
/*  298 */     Objects.requireNonNull(server);
/*  298 */     this.signedMessageDecoder = SignedMessageChain.Decoder.unsigned(player.getUUID(), server::enforceSecureProfile);
/*  300 */     this.chatMessageChain = new FutureChain((Executor)server);
/*      */   }
/*      */   
/*      */   public void tick() {
/*  305 */     if (this.ackBlockChangesUpTo > -1) {
/*  306 */       send((Packet<?>)new ClientboundBlockChangedAckPacket(this.ackBlockChangesUpTo));
/*  307 */       this.ackBlockChangesUpTo = -1;
/*      */     } 
/*  310 */     if (!this.server.isPaused() && tickPlayer())
/*      */       return; 
/*  314 */     keepConnectionAlive();
/*  316 */     this.chatSpamThrottler.tick();
/*  317 */     this.commandSpamThrottler.tick();
/*  318 */     this.dropSpamThrottler.tick();
/*  320 */     if (this.player.getLastActionTime() > 0L && this.server
/*  321 */       .playerIdleTimeout() > 0 && 
/*  322 */       Util.getMillis() - this.player.getLastActionTime() > TimeUnit.MINUTES.toMillis(this.server.playerIdleTimeout()) && !this.player.wonGame)
/*  325 */       disconnect((Component)Component.translatable("multiplayer.disconnect.idling")); 
/*      */   }
/*      */   
/*      */   private boolean tickPlayer() {
/*  330 */     resetPosition();
/*  331 */     this.player.xo = this.player.getX();
/*  332 */     this.player.yo = this.player.getY();
/*  333 */     this.player.zo = this.player.getZ();
/*  334 */     this.player.doTick();
/*  335 */     this.player.absSnapTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.getYRot(), this.player.getXRot());
/*  336 */     this.tickCount++;
/*  337 */     this.knownMovePacketCount = this.receivedMovePacketCount;
/*  339 */     if (this.clientIsFloating && !this.player.isSleeping() && !this.player.isPassenger() && !this.player.isDeadOrDying()) {
/*  340 */       if (++this.aboveGroundTickCount > getMaximumFlyingTicks((Entity)this.player)) {
/*  341 */         LOGGER.warn("{} was kicked for floating too long!", this.player.getPlainTextName());
/*  342 */         disconnect((Component)Component.translatable("multiplayer.disconnect.flying"));
/*  343 */         return true;
/*      */       } 
/*      */     } else {
/*  346 */       this.clientIsFloating = false;
/*  347 */       this.aboveGroundTickCount = 0;
/*      */     } 
/*  350 */     this.lastVehicle = this.player.getRootVehicle();
/*  351 */     if (this.lastVehicle == this.player || this.lastVehicle.getControllingPassenger() != this.player) {
/*  352 */       this.lastVehicle = null;
/*  353 */       this.clientVehicleIsFloating = false;
/*  354 */       this.aboveGroundVehicleTickCount = 0;
/*      */     } else {
/*  356 */       this.vehicleFirstGoodX = this.lastVehicle.getX();
/*  357 */       this.vehicleFirstGoodY = this.lastVehicle.getY();
/*  358 */       this.vehicleFirstGoodZ = this.lastVehicle.getZ();
/*  359 */       this.vehicleLastGoodX = this.lastVehicle.getX();
/*  360 */       this.vehicleLastGoodY = this.lastVehicle.getY();
/*  361 */       this.vehicleLastGoodZ = this.lastVehicle.getZ();
/*  362 */       if (this.clientVehicleIsFloating && this.lastVehicle.getControllingPassenger() == this.player) {
/*  363 */         if (++this.aboveGroundVehicleTickCount > getMaximumFlyingTicks(this.lastVehicle)) {
/*  364 */           LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getPlainTextName());
/*  365 */           disconnect((Component)Component.translatable("multiplayer.disconnect.flying"));
/*  366 */           return true;
/*      */         } 
/*      */       } else {
/*  369 */         this.clientVehicleIsFloating = false;
/*  370 */         this.aboveGroundVehicleTickCount = 0;
/*      */       } 
/*      */     } 
/*  374 */     return false;
/*      */   }
/*      */   
/*      */   private int getMaximumFlyingTicks(Entity entity) {
/*  378 */     double gravity = entity.getGravity();
/*  379 */     if (gravity < 9.999999747378752E-6D)
/*  381 */       return Integer.MAX_VALUE; 
/*  383 */     double gravityModifier = 0.08D / gravity;
/*  384 */     return Mth.ceil(80.0D * Math.max(gravityModifier, 1.0D));
/*      */   }
/*      */   
/*      */   public void resetFlyingTicks() {
/*  388 */     this.aboveGroundTickCount = 0;
/*  389 */     this.aboveGroundVehicleTickCount = 0;
/*      */   }
/*      */   
/*      */   public void resetPosition() {
/*  393 */     this.firstGoodX = this.player.getX();
/*  394 */     this.firstGoodY = this.player.getY();
/*  395 */     this.firstGoodZ = this.player.getZ();
/*  396 */     this.lastGoodX = this.player.getX();
/*  397 */     this.lastGoodY = this.player.getY();
/*  398 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */   
/*      */   public boolean isAcceptingMessages() {
/*  403 */     return (this.connection.isConnected() && !this.waitingForSwitchToConfig);
/*      */   }
/*      */   
/*      */   public boolean shouldHandleMessage(Packet<?> packet) {
/*  408 */     if (super.shouldHandleMessage(packet))
/*  409 */       return true; 
/*  412 */     return (this.waitingForSwitchToConfig && this.connection.isConnected() && packet instanceof ServerboundConfigurationAcknowledgedPacket);
/*      */   }
/*      */   
/*      */   protected GameProfile playerProfile() {
/*  417 */     return this.player.getGameProfile();
/*      */   }
/*      */   
/*      */   private <T, R> CompletableFuture<R> filterTextPacket(T message, BiFunction<TextFilter, T, CompletableFuture<R>> action) {
/*  421 */     return ((CompletableFuture)action.apply(this.player.getTextFilter(), message))
/*  422 */       .thenApply(result -> {
/*      */           if (!isAcceptingMessages()) {
/*      */             LOGGER.debug("Ignoring packet due to disconnection");
/*      */             throw new CancellationException("disconnected");
/*      */           } 
/*      */           return result;
/*      */         });
/*      */   }
/*      */   
/*      */   private CompletableFuture<FilteredText> filterTextPacket(String message) {
/*  433 */     return filterTextPacket(message, TextFilter::processStreamMessage);
/*      */   }
/*      */   
/*      */   private CompletableFuture<List<FilteredText>> filterTextPacket(List<String> message) {
/*  437 */     return filterTextPacket(message, TextFilter::processMessageBundle);
/*      */   }
/*      */   
/*      */   public void handlePlayerInput(ServerboundPlayerInputPacket packet) {
/*  442 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  443 */     this.player.setLastClientInput(packet.input());
/*  444 */     if (hasClientLoaded()) {
/*  445 */       this.player.resetLastActionTime();
/*  446 */       this.player.setShiftKeyDown(packet.input().shift());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean containsInvalidValues(double x, double y, double z, float yRot, float xRot) {
/*  451 */     return (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || !Floats.isFinite(xRot) || !Floats.isFinite(yRot));
/*      */   }
/*      */   
/*      */   private static double clampHorizontal(double value) {
/*  455 */     return Mth.clamp(value, -3.0E7D, 3.0E7D);
/*      */   }
/*      */   
/*      */   private static double clampVertical(double value) {
/*  459 */     return Mth.clamp(value, -2.0E7D, 2.0E7D);
/*      */   }
/*      */   
/*      */   public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {
/*  464 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  465 */     if (containsInvalidValues(packet.position().x(), packet.position().y(), packet.position().z(), packet.yRot(), packet.xRot())) {
/*  466 */       disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_vehicle_movement"));
/*      */       return;
/*      */     } 
/*  470 */     if (updateAwaitingTeleport() || !hasClientLoaded())
/*      */       return; 
/*  474 */     Entity vehicle = this.player.getRootVehicle();
/*  475 */     if (vehicle != this.player && vehicle.getControllingPassenger() == this.player && vehicle == this.lastVehicle) {
/*  476 */       ServerLevel level = this.player.level();
/*  477 */       double oldX = vehicle.getX();
/*  478 */       double oldY = vehicle.getY();
/*  479 */       double oldZ = vehicle.getZ();
/*  481 */       double targetX = clampHorizontal(packet.position().x());
/*  482 */       double targetY = clampVertical(packet.position().y());
/*  483 */       double targetZ = clampHorizontal(packet.position().z());
/*  484 */       float targetYRot = Mth.wrapDegrees(packet.yRot());
/*  485 */       float targetXRot = Mth.wrapDegrees(packet.xRot());
/*  487 */       double xDist = targetX - this.vehicleFirstGoodX;
/*  488 */       double yDist = targetY - this.vehicleFirstGoodY;
/*  489 */       double zDist = targetZ - this.vehicleFirstGoodZ;
/*  491 */       double expectedDist = vehicle.getDeltaMovement().lengthSqr();
/*  492 */       double movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/*  494 */       if (movedDist - expectedDist > 100.0D && !isSingleplayerOwner()) {
/*  495 */         LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", new Object[] { vehicle.getPlainTextName(), this.player.getPlainTextName(), Double.valueOf(xDist), Double.valueOf(yDist), Double.valueOf(zDist) });
/*  496 */         send((Packet<?>)ClientboundMoveVehiclePacket.fromEntity(vehicle));
/*      */         return;
/*      */       } 
/*  500 */       AABB oldAABB = vehicle.getBoundingBox();
/*  502 */       xDist = targetX - this.vehicleLastGoodX;
/*  503 */       yDist = targetY - this.vehicleLastGoodY;
/*  504 */       zDist = targetZ - this.vehicleLastGoodZ;
/*  505 */       boolean vehicleRestsOnSomething = vehicle.verticalCollisionBelow;
/*  506 */       if (vehicle instanceof LivingEntity) {
/*  506 */         LivingEntity livingVehicle = (LivingEntity)vehicle;
/*  506 */         if (livingVehicle.onClimbable())
/*  507 */           livingVehicle.resetFallDistance(); 
/*      */       } 
/*  510 */       vehicle.move(MoverType.PLAYER, new Vec3(xDist, yDist, zDist));
/*  512 */       double oyDist = yDist;
/*  514 */       xDist = targetX - vehicle.getX();
/*  515 */       yDist = targetY - vehicle.getY();
/*  516 */       if (yDist > -0.5D || yDist < 0.5D)
/*  517 */         yDist = 0.0D; 
/*  519 */       zDist = targetZ - vehicle.getZ();
/*  520 */       movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/*  521 */       boolean fail = false;
/*  522 */       if (movedDist > 0.0625D) {
/*  523 */         fail = true;
/*  524 */         LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", new Object[] { vehicle.getPlainTextName(), this.player.getPlainTextName(), Double.valueOf(Math.sqrt(movedDist)) });
/*      */       } 
/*  527 */       if ((fail && level.noCollision(vehicle, oldAABB)) || isEntityCollidingWithAnythingNew((LevelReader)level, vehicle, oldAABB, targetX, targetY, targetZ)) {
/*  528 */         vehicle.absSnapTo(oldX, oldY, oldZ, targetYRot, targetXRot);
/*  529 */         send((Packet<?>)ClientboundMoveVehiclePacket.fromEntity(vehicle));
/*  530 */         vehicle.removeLatestMovementRecording();
/*      */         return;
/*      */       } 
/*  534 */       vehicle.absSnapTo(targetX, targetY, targetZ, targetYRot, targetXRot);
/*  536 */       this.player.level().getChunkSource().move(this.player);
/*  538 */       Vec3 clientDeltaMovement = new Vec3(vehicle.getX() - oldX, vehicle.getY() - oldY, vehicle.getZ() - oldZ);
/*  539 */       handlePlayerKnownMovement(clientDeltaMovement);
/*  540 */       vehicle.setOnGroundWithMovement(packet.onGround(), clientDeltaMovement);
/*  541 */       vehicle.doCheckFallDamage(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z, packet.onGround());
/*  543 */       this.player.checkMovementStatistics(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z);
/*  544 */       this
/*      */         
/*  549 */         .clientVehicleIsFloating = (oyDist >= -0.03125D && !vehicleRestsOnSomething && !this.server.allowFlight() && !vehicle.isFlyingVehicle() && !vehicle.isNoGravity() && noBlocksAround(vehicle));
/*  551 */       this.vehicleLastGoodX = vehicle.getX();
/*  552 */       this.vehicleLastGoodY = vehicle.getY();
/*  553 */       this.vehicleLastGoodZ = vehicle.getZ();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean noBlocksAround(Entity entity) {
/*  558 */     return entity.level().getBlockStates(entity.getBoundingBox().inflate(0.0625D).expandTowards(0.0D, -0.55D, 0.0D)).allMatch(BlockBehaviour.BlockStateBase::isAir);
/*      */   }
/*      */   
/*      */   public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {
/*  563 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  564 */     if (packet.getId() == this.awaitingTeleport) {
/*  565 */       if (this.awaitingPositionFromClient == null) {
/*  566 */         disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_player_movement"));
/*      */         return;
/*      */       } 
/*  569 */       this.player.absSnapTo(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.getYRot(), this.player.getXRot());
/*  571 */       this.lastGoodX = this.awaitingPositionFromClient.x;
/*  572 */       this.lastGoodY = this.awaitingPositionFromClient.y;
/*  573 */       this.lastGoodZ = this.awaitingPositionFromClient.z;
/*  574 */       this.player.hasChangedDimension();
/*  576 */       this.awaitingPositionFromClient = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleAcceptPlayerLoad(ServerboundPlayerLoadedPacket packet) {
/*  582 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  583 */     markClientLoaded();
/*      */   }
/*      */   
/*      */   public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {
/*  588 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  590 */     RecipeManager.ServerDisplayInfo entry = this.server.getRecipeManager().getRecipeFromDisplay(packet.recipe());
/*  591 */     if (entry != null)
/*  592 */       this.player.getRecipeBook().removeHighlight(entry.parent().id()); 
/*      */   }
/*      */   
/*      */   public void handleBundleItemSelectedPacket(ServerboundSelectBundleItemPacket packet) {
/*  598 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  599 */     this.player.containerMenu.setSelectedBundleItemIndex(packet.slotId(), packet.selectedItemIndex());
/*      */   }
/*      */   
/*      */   public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {
/*  604 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  605 */     this.player.getRecipeBook().setBookSetting(packet.getBookType(), packet.isOpen(), packet.isFiltering());
/*      */   }
/*      */   
/*      */   public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) {
/*  610 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  611 */     if (packet.getAction() == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
/*  612 */       Identifier id = Objects.<Identifier>requireNonNull(packet.getTab());
/*  613 */       AdvancementHolder advancement = this.server.getAdvancements().get(id);
/*  614 */       if (advancement != null)
/*  615 */         this.player.getAdvancements().setSelectedTab(advancement); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {
/*  622 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  623 */     StringReader command = new StringReader(packet.getCommand());
/*  624 */     if (command.canRead() && command.peek() == '/')
/*  625 */       command.skip(); 
/*  627 */     ParseResults<CommandSourceStack> parse = this.server.getCommands().getDispatcher().parse(command, this.player.createCommandSourceStack());
/*  628 */     this.server.getCommands().getDispatcher().getCompletionSuggestions(parse).thenAccept(results -> {
/*      */           Suggestions suggestions = (results.getList().size() <= 1000) ? results : new Suggestions(results.getRange(), results.getList().subList(0, 1000));
/*      */           send((Packet<?>)new ClientboundCommandSuggestionsPacket(packet.getId(), suggestions));
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) {
/*  636 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  637 */     if (!this.player.canUseGameMasterBlocks()) {
/*  638 */       this.player.sendSystemMessage((Component)Component.translatable("advMode.notAllowed"));
/*      */       return;
/*      */     } 
/*  641 */     BaseCommandBlock commandBlock = null;
/*  642 */     CommandBlockEntity autoCommandBlock = null;
/*  643 */     BlockPos blockPos = packet.getPos();
/*  644 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  645 */     if (blockEntity instanceof CommandBlockEntity) {
/*  645 */       CommandBlockEntity commandBlockEntity = (CommandBlockEntity)blockEntity;
/*  646 */       autoCommandBlock = commandBlockEntity;
/*  647 */       commandBlock = autoCommandBlock.getCommandBlock();
/*      */     } 
/*  650 */     String command = packet.getCommand();
/*  651 */     boolean trackOutput = packet.isTrackOutput();
/*  653 */     if (commandBlock != null) {
/*  654 */       CommandBlockEntity.Mode oldMode = autoCommandBlock.getMode();
/*  656 */       BlockState currentBlockState = this.player.level().getBlockState(blockPos);
/*  657 */       Direction direction = (Direction)currentBlockState.getValue((Property)CommandBlock.FACING);
/*  658 */       switch (packet.getMode()) {
/*      */         case PERFORM_RESPAWN:
/*      */         
/*      */         case REQUEST_STATS:
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*  658 */       BlockState baseBlockState = 
/*      */         
/*  661 */         Blocks.COMMAND_BLOCK.defaultBlockState();
/*  663 */       BlockState blockState = (BlockState)((BlockState)baseBlockState.setValue((Property)CommandBlock.FACING, (Comparable)direction)).setValue((Property)CommandBlock.CONDITIONAL, Boolean.valueOf(packet.isConditional()));
/*  664 */       if (blockState != currentBlockState) {
/*  665 */         this.player.level().setBlock(blockPos, blockState, 2);
/*  667 */         blockEntity.setBlockState(blockState);
/*  668 */         this.player.level().getChunkAt(blockPos).setBlockEntity(blockEntity);
/*      */       } 
/*  671 */       commandBlock.setCommand(command);
/*  672 */       commandBlock.setTrackOutput(trackOutput);
/*  673 */       if (!trackOutput)
/*  674 */         commandBlock.setLastOutput(null); 
/*  676 */       autoCommandBlock.setAutomatic(packet.isAutomatic());
/*  677 */       if (oldMode != packet.getMode())
/*  678 */         autoCommandBlock.onModeSwitch(); 
/*  680 */       if (this.player.level().isCommandBlockEnabled())
/*  681 */         commandBlock.onUpdated(this.player.level()); 
/*  683 */       if (!StringUtil.isNullOrEmpty(command))
/*  684 */         this.player.sendSystemMessage((Component)Component.translatable(this.player.level().isCommandBlockEnabled() ? "advMode.setCommand.success" : "advMode.setCommand.disabled", new Object[] { command })); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) {
/*  691 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  692 */     if (!this.player.canUseGameMasterBlocks()) {
/*  693 */       this.player.sendSystemMessage((Component)Component.translatable("advMode.notAllowed"));
/*      */       return;
/*      */     } 
/*  696 */     BaseCommandBlock commandBlock = packet.getCommandBlock((Level)this.player.level());
/*  698 */     if (commandBlock != null) {
/*  699 */       String command = packet.getCommand();
/*  700 */       commandBlock.setCommand(command);
/*  701 */       commandBlock.setTrackOutput(packet.isTrackOutput());
/*  702 */       if (!packet.isTrackOutput())
/*  703 */         commandBlock.setLastOutput(null); 
/*  705 */       boolean commandBlockEnabled = this.player.level().isCommandBlockEnabled();
/*  706 */       if (commandBlockEnabled)
/*  707 */         commandBlock.onUpdated(this.player.level()); 
/*  709 */       if (!StringUtil.isNullOrEmpty(command))
/*  710 */         this.player.sendSystemMessage((Component)Component.translatable(commandBlockEnabled ? "advMode.setCommand.success" : "advMode.setCommand.disabled", new Object[] { command })); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePickItemFromBlock(ServerboundPickItemFromBlockPacket packet) {
/*  717 */     ServerLevel level = this.player.level();
/*  718 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, level);
/*  720 */     BlockPos pos = packet.pos();
/*  721 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D))
/*      */       return; 
/*  723 */     if (!level.isLoaded(pos))
/*      */       return; 
/*  727 */     BlockState blockState = level.getBlockState(pos);
/*  729 */     boolean includeData = (this.player.hasInfiniteMaterials() && packet.includeData());
/*  730 */     ItemStack itemStack = blockState.getCloneItemStack((LevelReader)level, pos, includeData);
/*  731 */     if (itemStack.isEmpty())
/*      */       return; 
/*  735 */     if (includeData)
/*  736 */       addBlockDataToItem(blockState, level, pos, itemStack); 
/*  739 */     tryPickItem(itemStack);
/*      */   }
/*      */   
/*      */   private static void addBlockDataToItem(BlockState blockState, ServerLevel level, BlockPos pos, ItemStack itemStack) {
/*  743 */     BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  744 */     if (blockEntity != null) {
/*  745 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER);
/*      */       try {
/*  746 */         TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess());
/*  747 */         blockEntity.saveCustomOnly((ValueOutput)output);
/*  748 */         blockEntity.removeComponentsFromTag((ValueOutput)output);
/*  749 */         BlockItem.setBlockEntityData(itemStack, blockEntity.getType(), output);
/*  750 */         itemStack.applyComponents(blockEntity.collectComponents());
/*  751 */         reporter.close();
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
/*      */   public void handlePickItemFromEntity(ServerboundPickItemFromEntityPacket packet) {
/*  757 */     ServerLevel level = this.player.level();
/*  758 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, level);
/*  760 */     Entity entity = level.getEntityOrPart(packet.id());
/*  761 */     if (entity == null || !this.player.isWithinEntityInteractionRange(entity, 3.0D))
/*      */       return; 
/*  765 */     ItemStack itemStack = entity.getPickResult();
/*  766 */     if (itemStack != null && !itemStack.isEmpty())
/*  767 */       tryPickItem(itemStack); 
/*  770 */     if (packet.includeData() && this.player.canUseGameMasterBlocks() && entity instanceof Avatar) {
/*  770 */       Avatar avatar = (Avatar)entity;
/*  771 */       FetchProfileCommand.printForAvatar(this.player.createCommandSourceStack(), avatar);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void tryPickItem(ItemStack itemStack) {
/*  776 */     if (!itemStack.isItemEnabled(this.player.level().enabledFeatures()))
/*      */       return; 
/*  780 */     Inventory inventory = this.player.getInventory();
/*  781 */     int slotWithExistingItem = inventory.findSlotMatchingItem(itemStack);
/*  782 */     if (slotWithExistingItem != -1) {
/*  783 */       if (Inventory.isHotbarSlot(slotWithExistingItem)) {
/*  784 */         inventory.setSelectedSlot(slotWithExistingItem);
/*      */       } else {
/*  786 */         inventory.pickSlot(slotWithExistingItem);
/*      */       } 
/*  788 */     } else if (this.player.hasInfiniteMaterials()) {
/*  789 */       inventory.addAndPickItem(itemStack);
/*      */     } 
/*  792 */     send((Packet<?>)new ClientboundSetHeldSlotPacket(inventory.getSelectedSlot()));
/*  793 */     this.player.inventoryMenu.broadcastChanges();
/*      */   }
/*      */   
/*      */   public void handleRenameItem(ServerboundRenameItemPacket packet) {
/*  798 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  799 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu;
/*  799 */     if (abstractContainerMenu instanceof AnvilMenu) {
/*  799 */       AnvilMenu menu = (AnvilMenu)abstractContainerMenu;
/*  800 */       if (!menu.stillValid((Player)this.player)) {
/*  801 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, menu);
/*      */         return;
/*      */       } 
/*  804 */       menu.setItemName(packet.getName());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) {
/*  810 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  811 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu;
/*  811 */     if (abstractContainerMenu instanceof BeaconMenu) {
/*  811 */       BeaconMenu menu = (BeaconMenu)abstractContainerMenu;
/*  812 */       if (!this.player.containerMenu.stillValid((Player)this.player)) {
/*  813 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */         return;
/*      */       } 
/*  816 */       if (!menu.updateEffects(packet.primary(), packet.secondary())) {
/*  817 */         disconnect((Component)Component.translatable("multiplayer.disconnect.generic"));
/*  818 */         LOGGER.warn("Player {} tried to set invalid beacon effects", this.player);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetGameRule(ServerboundSetGameRulePacket packet) {
/*  825 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  826 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/*  827 */       LOGGER.warn("Player {} tried to set game rule values without required permissions", this.player.getGameProfile().name());
/*      */       return;
/*      */     } 
/*  830 */     GameRules gameRules = this.player.level().getGameRules();
/*  831 */     for (ServerboundSetGameRulePacket.Entry entry : packet.entries()) {
/*  832 */       GameRule<?> rule = (GameRule)BuiltInRegistries.GAME_RULE.getValue(entry.gameRuleKey());
/*  833 */       if (rule != null) {
/*  834 */         setGameRuleValue(gameRules, rule, entry.value());
/*      */         continue;
/*      */       } 
/*  836 */       LOGGER.warn("Received request to set unknown game rule: {}", entry.gameRuleKey());
/*      */     } 
/*      */   }
/*      */   
/*      */   private <T> void setGameRuleValue(GameRules gameRules, GameRule<T> rule, String value) {
/*  842 */     rule.deserialize(value).result().ifPresent(parsedValue -> {
/*      */           gameRules.set(rule, parsedValue, this.server);
/*      */           broadcastGameRuleChangeToOperators(rule, parsedValue);
/*      */         });
/*      */   }
/*      */   
/*      */   private <T> void broadcastGameRuleChangeToOperators(GameRule<T> rule, T value) {
/*  849 */     MutableComponent mutableComponent = Component.translatable("commands.gamerule.set", new Object[] { rule.id(), rule.serialize(value) });
/*  850 */     PlayerList playerList = this.server.getPlayerList();
/*  851 */     playerList.getPlayers().stream()
/*  852 */       .filter(op -> playerList.isOp(op.nameAndId()))
/*  853 */       .forEach(op -> op.sendSystemMessage(message));
/*      */   }
/*      */   
/*      */   public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) {
/*  858 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  859 */     if (!this.player.canUseGameMasterBlocks())
/*      */       return; 
/*  862 */     BlockPos blockPos = packet.getPos();
/*  863 */     BlockState state = this.player.level().getBlockState(blockPos);
/*  864 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  865 */     if (blockEntity instanceof StructureBlockEntity) {
/*  865 */       StructureBlockEntity structure = (StructureBlockEntity)blockEntity;
/*  866 */       structure.setMode(packet.getMode());
/*  867 */       structure.setStructureName(packet.getName());
/*  868 */       structure.setStructurePos(packet.getOffset());
/*  869 */       structure.setStructureSize(packet.getSize());
/*  870 */       structure.setMirror(packet.getMirror());
/*  871 */       structure.setRotation(packet.getRotation());
/*  872 */       structure.setMetaData(packet.getData());
/*  873 */       structure.setIgnoreEntities(packet.isIgnoreEntities());
/*  874 */       structure.setStrict(packet.isStrict());
/*  875 */       structure.setShowAir(packet.isShowAir());
/*  876 */       structure.setShowBoundingBox(packet.isShowBoundingBox());
/*  877 */       structure.setIntegrity(packet.getIntegrity());
/*  878 */       structure.setSeed(packet.getSeed());
/*  880 */       if (structure.hasStructureName()) {
/*  881 */         String actualStructureName = structure.getStructureName();
/*  882 */         if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
/*  883 */           if (structure.saveStructure()) {
/*  884 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.save_success", new Object[] { actualStructureName }));
/*      */           } else {
/*  886 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.save_failure", new Object[] { actualStructureName }));
/*      */           } 
/*  888 */         } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
/*  889 */           if (!structure.isStructureLoadable()) {
/*  890 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.load_not_found", new Object[] { actualStructureName }));
/*  891 */           } else if (structure.placeStructureIfSameSize(this.player.level())) {
/*  892 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.load_success", new Object[] { actualStructureName }));
/*      */           } else {
/*  894 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.load_prepare", new Object[] { actualStructureName }));
/*      */           } 
/*  896 */         } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
/*  897 */           if (structure.detectSize()) {
/*  898 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.size_success", new Object[] { actualStructureName }));
/*      */           } else {
/*  900 */             this.player.sendSystemMessage((Component)Component.translatable("structure_block.size_failure"));
/*      */           } 
/*      */         } 
/*      */       } else {
/*  904 */         this.player.sendSystemMessage((Component)Component.translatable("structure_block.invalid_structure_name", new Object[] { packet.getName() }));
/*      */       } 
/*  907 */       structure.setChanged();
/*  908 */       this.player.level().sendBlockUpdated(blockPos, state, state, 3);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetTestBlock(ServerboundSetTestBlockPacket packet) {
/*  914 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  915 */     if (!this.player.canUseGameMasterBlocks())
/*      */       return; 
/*  918 */     BlockPos blockPos = packet.position();
/*  919 */     BlockState initialState = this.player.level().getBlockState(blockPos);
/*  920 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  921 */     if (blockEntity instanceof TestBlockEntity) {
/*  921 */       TestBlockEntity testBlock = (TestBlockEntity)blockEntity;
/*  922 */       testBlock.setMode(packet.mode());
/*  923 */       testBlock.setMessage(packet.message());
/*  924 */       testBlock.setChanged();
/*  925 */       this.player.level().sendBlockUpdated(blockPos, initialState, testBlock.getBlockState(), 3);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleTestInstanceBlockAction(ServerboundTestInstanceBlockActionPacket packet) {
/*      */     TestInstanceBlockEntity blockEntity;
/*  931 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  932 */     BlockPos pos = packet.pos();
/*  933 */     if (this.player.canUseGameMasterBlocks()) {
/*  933 */       BlockEntity blockEntity1 = this.player.level().getBlockEntity(pos);
/*  933 */       if (blockEntity1 instanceof TestInstanceBlockEntity) {
/*  933 */         blockEntity = (TestInstanceBlockEntity)blockEntity1;
/*      */       } else {
/*      */         return;
/*      */       } 
/*      */     } else {
/*      */       return;
/*      */     } 
/*  936 */     if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.QUERY || packet
/*  937 */       .action() == ServerboundTestInstanceBlockActionPacket.Action.INIT) {
/*      */       MutableComponent mutableComponent;
/*      */       Optional<Vec3i> size;
/*  938 */       Registry<GameTestInstance> registry = this.player.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE);
/*  939 */       Objects.requireNonNull(registry);
/*  939 */       Optional<Holder.Reference<GameTestInstance>> test = packet.data().test().flatMap(registry::get);
/*  941 */       if (test.isPresent()) {
/*  942 */         Component status = ((GameTestInstance)((Holder.Reference)test.get()).value()).describe();
/*      */       } else {
/*  944 */         mutableComponent = Component.translatable("test_instance.description.no_test").withStyle(ChatFormatting.RED);
/*      */       } 
/*  947 */       if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.QUERY) {
/*  948 */         size = packet.data().test().flatMap(testKey -> TestInstanceBlockEntity.getStructureSize(this.player.level(), testKey));
/*      */       } else {
/*  950 */         size = Optional.empty();
/*      */       } 
/*  952 */       this.connection.send((Packet)new ClientboundTestInstanceBlockStatus((Component)mutableComponent, size));
/*      */     } else {
/*  954 */       blockEntity.set(packet.data());
/*  955 */       if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.RESET) {
/*  956 */         Objects.requireNonNull(this.player);
/*  956 */         blockEntity.resetTest(this.player::sendSystemMessage);
/*  957 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.SAVE) {
/*  958 */         Objects.requireNonNull(this.player);
/*  958 */         blockEntity.saveTest(this.player::sendSystemMessage);
/*  959 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.EXPORT) {
/*  960 */         Objects.requireNonNull(this.player);
/*  960 */         blockEntity.exportTest(this.player::sendSystemMessage);
/*  961 */       } else if (packet.action() == ServerboundTestInstanceBlockActionPacket.Action.RUN) {
/*  962 */         Objects.requireNonNull(this.player);
/*  962 */         blockEntity.runTest(this.player::sendSystemMessage);
/*      */       } 
/*  964 */       BlockState state = this.player.level().getBlockState(pos);
/*  965 */       this.player.level().sendBlockUpdated(pos, Blocks.AIR.defaultBlockState(), state, 3);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) {
/*  971 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  972 */     if (!this.player.canUseGameMasterBlocks())
/*      */       return; 
/*  975 */     BlockPos blockPos = packet.getPos();
/*  976 */     BlockState state = this.player.level().getBlockState(blockPos);
/*  977 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  978 */     if (blockEntity instanceof JigsawBlockEntity) {
/*  978 */       JigsawBlockEntity jigsaw = (JigsawBlockEntity)blockEntity;
/*  979 */       jigsaw.setName(packet.getName());
/*  980 */       jigsaw.setTarget(packet.getTarget());
/*  981 */       jigsaw.setPool(ResourceKey.create(Registries.TEMPLATE_POOL, packet.getPool()));
/*  982 */       jigsaw.setFinalState(packet.getFinalState());
/*  983 */       jigsaw.setJoint(packet.getJoint());
/*  984 */       jigsaw.setPlacementPriority(packet.getPlacementPriority());
/*  985 */       jigsaw.setSelectionPriority(packet.getSelectionPriority());
/*  986 */       jigsaw.setChanged();
/*  987 */       this.player.level().sendBlockUpdated(blockPos, state, state, 3);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) {
/*  993 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/*  994 */     if (!this.player.canUseGameMasterBlocks())
/*      */       return; 
/*  997 */     BlockPos blockPos = packet.getPos();
/*  998 */     BlockEntity blockEntity = this.player.level().getBlockEntity(blockPos);
/*  999 */     if (blockEntity instanceof JigsawBlockEntity) {
/*  999 */       JigsawBlockEntity jigsaw = (JigsawBlockEntity)blockEntity;
/* 1000 */       jigsaw.generate(this.player.level(), packet.levels(), packet.keepJigsaws());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSelectTrade(ServerboundSelectTradePacket packet) {
/* 1006 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1007 */     int selection = packet.getItem();
/* 1008 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu;
/* 1008 */     if (abstractContainerMenu instanceof MerchantMenu) {
/* 1008 */       MerchantMenu menu = (MerchantMenu)abstractContainerMenu;
/* 1009 */       if (!menu.stillValid((Player)this.player)) {
/* 1010 */         LOGGER.debug("Player {} interacted with invalid menu {}", this.player, menu);
/*      */         return;
/*      */       } 
/* 1013 */       menu.setSelectionHint(selection);
/* 1014 */       menu.tryMoveItems(selection);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEditBook(ServerboundEditBookPacket packet) {
/* 1021 */     int slot = packet.slot();
/* 1022 */     if (!Inventory.isHotbarSlot(slot) && slot != 40)
/*      */       return; 
/* 1026 */     List<String> contents = Lists.newArrayList();
/* 1027 */     Optional<String> title = packet.title();
/* 1028 */     Objects.requireNonNull(contents);
/* 1028 */     title.ifPresent(contents::add);
/* 1029 */     contents.addAll(packet.pages());
/* 1033 */     Consumer<List<FilteredText>> handler = title.isPresent() ? (filteredContents -> signBook(filteredContents.get(0), filteredContents.subList(1, filteredContents.size()), slot)) : (filteredContents -> updateBookContents(filteredContents, slot));
/* 1034 */     filterTextPacket(contents).thenAcceptAsync(handler, (Executor)this.server);
/*      */   }
/*      */   
/*      */   private void updateBookContents(List<FilteredText> contents, int slot) {
/* 1038 */     ItemStack carried = this.player.getInventory().getItem(slot);
/* 1039 */     if (!carried.has(DataComponents.WRITABLE_BOOK_CONTENT))
/*      */       return; 
/* 1043 */     List<Filterable<String>> pages = contents.stream().map(this::filterableFromOutgoing).toList();
/* 1044 */     carried.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(pages));
/*      */   }
/*      */   
/*      */   private void signBook(FilteredText title, List<FilteredText> contents, int slot) {
/* 1048 */     ItemStack carried = this.player.getInventory().getItem(slot);
/* 1049 */     if (!carried.has(DataComponents.WRITABLE_BOOK_CONTENT))
/*      */       return; 
/* 1053 */     ItemStack writtenBook = carried.transmuteCopy((ItemLike)Items.WRITTEN_BOOK);
/* 1054 */     writtenBook.remove(DataComponents.WRITABLE_BOOK_CONTENT);
/* 1058 */     List<Filterable<Component>> pages = contents.stream().map(page -> filterableFromOutgoing(page).map(Component::literal)).toList();
/* 1060 */     writtenBook.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
/* 1061 */           filterableFromOutgoing(title), this.player
/* 1062 */           .getPlainTextName(), 0, pages, true));
/* 1068 */     this.player.getInventory().setItem(slot, writtenBook);
/*      */   }
/*      */   
/*      */   private Filterable<String> filterableFromOutgoing(FilteredText text) {
/* 1072 */     if (this.player.isTextFilteringEnabled())
/* 1074 */       return Filterable.passThrough(text.filteredOrEmpty()); 
/* 1076 */     return Filterable.from(text);
/*      */   }
/*      */   
/*      */   public void handleEntityTagQuery(ServerboundEntityTagQueryPacket packet) {
/* 1082 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1084 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
/*      */       return; 
/* 1088 */     Entity entity = this.player.level().getEntity(packet.getEntityId());
/* 1089 */     if (entity != null) {
/* 1090 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER);
/*      */       try {
/* 1091 */         TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 1092 */         entity.saveWithoutId((ValueOutput)output);
/* 1093 */         CompoundTag result = output.buildResult();
/* 1094 */         send((Packet<?>)new ClientboundTagQueryPacket(packet.getTransactionId(), result));
/* 1095 */         reporter.close();
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
/*      */   public void handleContainerSlotStateChanged(ServerboundContainerSlotStateChangedPacket packet) {
/* 1101 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1103 */     if (this.player.isSpectator() || packet.containerId() != this.player.containerMenu.containerId)
/*      */       return; 
/* 1107 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu;
/* 1107 */     if (abstractContainerMenu instanceof CrafterMenu) {
/* 1107 */       CrafterMenu crafterMenu = (CrafterMenu)abstractContainerMenu;
/* 1108 */       Container container = crafterMenu.getContainer();
/* 1108 */       if (container instanceof CrafterBlockEntity) {
/* 1108 */         CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)container;
/* 1109 */         crafterBlockEntity.setSlotState(packet.slotId(), packet.newState());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet) {
/* 1115 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1117 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
/*      */       return; 
/* 1121 */     BlockEntity blockEntity = this.player.level().getBlockEntity(packet.getPos());
/* 1122 */     CompoundTag tag = (blockEntity != null) ? blockEntity.saveWithoutMetadata((HolderLookup.Provider)this.player.registryAccess()) : null;
/* 1123 */     send((Packet<?>)new ClientboundTagQueryPacket(packet.getTransactionId(), tag));
/*      */   }
/*      */   
/*      */   public void handleMovePlayer(ServerboundMovePlayerPacket packet) {
/* 1128 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1129 */     if (containsInvalidValues(packet.getX(0.0D), packet.getY(0.0D), packet.getZ(0.0D), packet.getYRot(0.0F), packet.getXRot(0.0F))) {
/* 1130 */       disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_player_movement"));
/*      */       return;
/*      */     } 
/* 1133 */     ServerLevel level = this.player.level();
/* 1135 */     if (this.player.wonGame)
/*      */       return; 
/* 1139 */     if (this.tickCount == 0)
/* 1140 */       resetPosition(); 
/* 1143 */     if (!hasClientLoaded())
/*      */       return; 
/* 1147 */     float targetYRot = Mth.wrapDegrees(packet.getYRot(this.player.getYRot()));
/* 1148 */     float targetXRot = Mth.wrapDegrees(packet.getXRot(this.player.getXRot()));
/* 1150 */     if (updateAwaitingTeleport()) {
/* 1153 */       this.player.absSnapRotationTo(targetYRot, targetXRot);
/*      */       return;
/*      */     } 
/* 1157 */     double targetX = clampHorizontal(packet.getX(this.player.getX()));
/* 1158 */     double targetY = clampVertical(packet.getY(this.player.getY()));
/* 1159 */     double targetZ = clampHorizontal(packet.getZ(this.player.getZ()));
/* 1161 */     if (this.player.isPassenger()) {
/* 1162 */       this.player.absSnapTo(this.player.getX(), this.player.getY(), this.player.getZ(), targetYRot, targetXRot);
/* 1163 */       this.player.level().getChunkSource().move(this.player);
/*      */       return;
/*      */     } 
/* 1167 */     double startX = this.player.getX();
/* 1168 */     double startY = this.player.getY();
/* 1169 */     double startZ = this.player.getZ();
/* 1171 */     double xDist = targetX - this.firstGoodX;
/* 1172 */     double yDist = targetY - this.firstGoodY;
/* 1173 */     double zDist = targetZ - this.firstGoodZ;
/* 1176 */     double expectedDist = this.player.getDeltaMovement().lengthSqr();
/* 1177 */     double movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/* 1179 */     if (this.player.isSleeping()) {
/* 1180 */       if (movedDist > 1.0D)
/* 1181 */         teleport(this.player.getX(), this.player.getY(), this.player.getZ(), targetYRot, targetXRot); 
/*      */       return;
/*      */     } 
/* 1186 */     boolean isFallFlying = this.player.isFallFlying();
/* 1187 */     if (level.tickRateManager().runsNormally()) {
/* 1188 */       this.receivedMovePacketCount++;
/* 1189 */       int deltaPackets = this.receivedMovePacketCount - this.knownMovePacketCount;
/* 1190 */       if (deltaPackets > 5) {
/* 1191 */         LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getPlainTextName(), Integer.valueOf(deltaPackets));
/* 1192 */         deltaPackets = 1;
/*      */       } 
/* 1195 */       if (shouldCheckPlayerMovement(isFallFlying)) {
/* 1196 */         float metersPerTick = isFallFlying ? 300.0F : 100.0F;
/* 1197 */         if (movedDist - expectedDist > (metersPerTick * deltaPackets)) {
/* 1198 */           LOGGER.warn("{} moved too quickly! {},{},{}", new Object[] { this.player.getPlainTextName(), Double.valueOf(xDist), Double.valueOf(yDist), Double.valueOf(zDist) });
/* 1199 */           teleport(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.getYRot(), this.player.getXRot());
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1205 */     AABB oldAABB = this.player.getBoundingBox();
/* 1207 */     xDist = targetX - this.lastGoodX;
/* 1208 */     yDist = targetY - this.lastGoodY;
/* 1209 */     zDist = targetZ - this.lastGoodZ;
/* 1211 */     boolean movedUpwards = (yDist > 0.0D);
/* 1213 */     if (this.player.onGround() && !packet.isOnGround() && movedUpwards)
/* 1215 */       this.player.jumpFromGround(); 
/* 1217 */     boolean playerStandsOnSomething = this.player.verticalCollisionBelow;
/* 1218 */     this.player.move(MoverType.PLAYER, new Vec3(xDist, yDist, zDist));
/* 1220 */     double oyDist = yDist;
/* 1222 */     xDist = targetX - this.player.getX();
/* 1223 */     yDist = targetY - this.player.getY();
/* 1224 */     if (yDist > -0.5D || yDist < 0.5D)
/* 1225 */       yDist = 0.0D; 
/* 1227 */     zDist = targetZ - this.player.getZ();
/* 1228 */     movedDist = xDist * xDist + yDist * yDist + zDist * zDist;
/* 1229 */     boolean fail = false;
/* 1230 */     if (!this.player.isChangingDimension() && movedDist > 0.0625D && !this.player.isSleeping() && !this.player.isCreative() && !this.player.isSpectator() && !this.player.isInPostImpulseGraceTime()) {
/* 1231 */       fail = true;
/* 1232 */       LOGGER.warn("{} moved wrongly!", this.player.getPlainTextName());
/*      */     } 
/* 1235 */     if (!this.player.noPhysics && !this.player.isSleeping() && ((
/* 1236 */       fail && level.noCollision((Entity)this.player, oldAABB)) || isEntityCollidingWithAnythingNew((LevelReader)level, (Entity)this.player, oldAABB, targetX, targetY, targetZ))) {
/* 1237 */       teleport(startX, startY, startZ, targetYRot, targetXRot);
/* 1238 */       this.player.doCheckFallDamage(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ, packet.isOnGround());
/* 1239 */       this.player.removeLatestMovementRecording();
/*      */       return;
/*      */     } 
/* 1244 */     this.player.absSnapTo(targetX, targetY, targetZ, targetYRot, targetXRot);
/* 1246 */     boolean isAutoSpinAttack = this.player.isAutoSpinAttack();
/* 1247 */     this
/*      */       
/* 1255 */       .clientIsFloating = (oyDist >= -0.03125D && !playerStandsOnSomething && !this.player.isSpectator() && !this.server.allowFlight() && !(this.player.getAbilities()).mayfly && !this.player.hasEffect(MobEffects.LEVITATION) && !isFallFlying && !isAutoSpinAttack && noBlocksAround((Entity)this.player));
/* 1257 */     this.player.level().getChunkSource().move(this.player);
/* 1259 */     Vec3 clientDeltaMovement = new Vec3(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ);
/* 1260 */     this.player.setOnGroundWithMovement(packet.isOnGround(), packet.horizontalCollision(), clientDeltaMovement);
/* 1261 */     this.player.doCheckFallDamage(clientDeltaMovement.x, clientDeltaMovement.y, clientDeltaMovement.z, packet.isOnGround());
/* 1263 */     handlePlayerKnownMovement(clientDeltaMovement);
/* 1266 */     if (movedUpwards)
/* 1267 */       this.player.resetFallDistance(); 
/* 1270 */     if (packet.isOnGround() || this.player.hasLandedInLiquid() || this.player.onClimbable() || this.player.isSpectator() || isFallFlying || isAutoSpinAttack)
/* 1271 */       this.player.tryResetCurrentImpulseContext(); 
/* 1275 */     this.player.checkMovementStatistics(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ);
/* 1277 */     this.lastGoodX = this.player.getX();
/* 1278 */     this.lastGoodY = this.player.getY();
/* 1279 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */   
/*      */   private boolean shouldCheckPlayerMovement(boolean isFallFlying) {
/* 1283 */     if (isSingleplayerOwner())
/* 1284 */       return false; 
/* 1286 */     if (this.player.isChangingDimension())
/* 1287 */       return false; 
/* 1289 */     GameRules gameRules = this.player.level().getGameRules();
/* 1290 */     if (!((Boolean)gameRules.get(GameRules.PLAYER_MOVEMENT_CHECK)).booleanValue())
/* 1291 */       return false; 
/* 1293 */     if (isFallFlying && !((Boolean)gameRules.get(GameRules.ELYTRA_MOVEMENT_CHECK)).booleanValue())
/* 1294 */       return false; 
/* 1296 */     return true;
/*      */   }
/*      */   
/*      */   private boolean updateAwaitingTeleport() {
/* 1300 */     if (this.awaitingPositionFromClient != null) {
/* 1302 */       if (this.tickCount - this.awaitingTeleportTime > 20) {
/* 1303 */         this.awaitingTeleportTime = this.tickCount;
/* 1304 */         teleport(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.getYRot(), this.player.getXRot());
/*      */       } 
/* 1306 */       return true;
/*      */     } 
/* 1309 */     this.awaitingTeleportTime = this.tickCount;
/* 1310 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isEntityCollidingWithAnythingNew(LevelReader level, Entity entity, AABB oldAABB, double newX, double newY, double newZ) {
/* 1314 */     AABB newAABB = entity.getBoundingBox().move(newX - entity.getX(), newY - entity.getY(), newZ - entity.getZ());
/* 1315 */     Iterable<VoxelShape> newCollisions = level.getPreMoveCollisions(entity, newAABB.deflate(9.999999747378752E-6D), oldAABB.getBottomCenter());
/* 1316 */     VoxelShape oldShape = Shapes.create(oldAABB.deflate(9.999999747378752E-6D));
/* 1318 */     for (VoxelShape shape : newCollisions) {
/* 1319 */       if (!Shapes.joinIsNotEmpty(shape, oldShape, BooleanOp.AND))
/* 1320 */         return true; 
/*      */     } 
/* 1323 */     return false;
/*      */   }
/*      */   
/*      */   public void teleport(double x, double y, double z, float yRot, float xRot) {
/* 1327 */     teleport(new PositionMoveRotation(new Vec3(x, y, z), Vec3.ZERO, yRot, xRot), Collections.emptySet());
/*      */   }
/*      */   
/*      */   public void teleport(PositionMoveRotation destination, Set<Relative> relatives) {
/* 1334 */     this.awaitingTeleportTime = this.tickCount;
/* 1335 */     if (++this.awaitingTeleport == Integer.MAX_VALUE)
/* 1336 */       this.awaitingTeleport = 0; 
/* 1339 */     this.player.teleportSetPosition(destination, relatives);
/* 1340 */     this.awaitingPositionFromClient = this.player.position();
/* 1341 */     send((Packet<?>)ClientboundPlayerPositionPacket.of(this.awaitingTeleport, destination, relatives));
/*      */   }
/*      */   
/*      */   public void handlePlayerAction(ServerboundPlayerActionPacket packet) {
/*      */     ItemStack itemInHand;
/*      */     PiercingWeapon piercingWeapon;
/* 1346 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1347 */     if (!hasClientLoaded())
/*      */       return; 
/* 1351 */     BlockPos pos = packet.getPos();
/* 1352 */     this.player.resetLastActionTime();
/* 1354 */     ServerboundPlayerActionPacket.Action action = packet.getAction();
/* 1356 */     switch (action) {
/*      */       case PERFORM_RESPAWN:
/* 1358 */         if (this.player.isSpectator())
/*      */           return; 
/* 1361 */         itemInHand = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1363 */         if (this.player.cannotAttackWithItem(itemInHand, 5))
/*      */           return; 
/* 1366 */         piercingWeapon = (PiercingWeapon)itemInHand.get(DataComponents.PIERCING_WEAPON);
/* 1367 */         if (piercingWeapon != null)
/* 1368 */           piercingWeapon.attack((LivingEntity)this.player, EquipmentSlot.MAINHAND); 
/*      */         return;
/*      */       case REQUEST_STATS:
/* 1373 */         if (!this.player.isSpectator()) {
/* 1374 */           ItemStack swap = this.player.getItemInHand(InteractionHand.OFF_HAND);
/* 1375 */           this.player.setItemInHand(InteractionHand.OFF_HAND, this.player.getItemInHand(InteractionHand.MAIN_HAND));
/* 1376 */           this.player.setItemInHand(InteractionHand.MAIN_HAND, swap);
/* 1377 */           this.player.stopUsingItem();
/*      */         } 
/*      */         return;
/*      */       case REQUEST_GAMERULE_VALUES:
/* 1382 */         if (!this.player.isSpectator())
/* 1383 */           this.player.drop(false); 
/*      */         return;
/*      */       case null:
/* 1388 */         if (!this.player.isSpectator())
/* 1389 */           this.player.drop(true); 
/*      */         return;
/*      */       case null:
/* 1394 */         this.player.releaseUsingItem();
/*      */         return;
/*      */       case null:
/*      */       case null:
/*      */       case null:
/* 1398 */         this.player.gameMode.handleBlockBreakAction(pos, action, packet.getDirection(), this.player.level().getMaxY(), packet.getSequence());
/* 1399 */         ackBlockChangesUpTo(packet.getSequence());
/*      */         return;
/*      */     } 
/* 1402 */     throw new IllegalArgumentException("Invalid player action");
/*      */   }
/*      */   
/*      */   private static boolean wasBlockPlacementAttempt(ServerPlayer player, ItemStack itemStack) {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual isEmpty : ()Z
/*      */     //   4: ifeq -> 9
/*      */     //   7: iconst_0
/*      */     //   8: ireturn
/*      */     //   9: aload_1
/*      */     //   10: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*      */     //   13: astore_2
/*      */     //   14: aload_2
/*      */     //   15: instanceof net/minecraft/world/item/BlockItem
/*      */     //   18: ifne -> 43
/*      */     //   21: aload_2
/*      */     //   22: instanceof net/minecraft/world/item/BucketItem
/*      */     //   25: ifeq -> 58
/*      */     //   28: aload_2
/*      */     //   29: checkcast net/minecraft/world/item/BucketItem
/*      */     //   32: astore_3
/*      */     //   33: aload_3
/*      */     //   34: invokevirtual getContent : ()Lnet/minecraft/world/level/material/Fluid;
/*      */     //   37: getstatic net/minecraft/world/level/material/Fluids.EMPTY : Lnet/minecraft/world/level/material/Fluid;
/*      */     //   40: if_acmpeq -> 58
/*      */     //   43: aload_0
/*      */     //   44: invokevirtual getCooldowns : ()Lnet/minecraft/world/item/ItemCooldowns;
/*      */     //   47: aload_1
/*      */     //   48: invokevirtual isOnCooldown : (Lnet/minecraft/world/item/ItemStack;)Z
/*      */     //   51: ifne -> 58
/*      */     //   54: iconst_1
/*      */     //   55: goto -> 59
/*      */     //   58: iconst_0
/*      */     //   59: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1407	-> 0
/*      */     //   #1408	-> 7
/*      */     //   #1411	-> 9
/*      */     //   #1412	-> 14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   33	10	3	bucket	Lnet/minecraft/world/item/BucketItem;
/*      */     //   0	60	0	player	Lnet/minecraft/server/level/ServerPlayer;
/*      */     //   0	60	1	itemStack	Lnet/minecraft/world/item/ItemStack;
/*      */     //   14	46	2	item	Lnet/minecraft/world/item/Item;
/*      */   }
/*      */   
/*      */   public void handleUseItemOn(ServerboundUseItemOnPacket packet) {
/* 1420 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1421 */     if (!hasClientLoaded())
/*      */       return; 
/* 1424 */     ackBlockChangesUpTo(packet.getSequence());
/* 1426 */     ServerLevel level = this.player.level();
/* 1427 */     InteractionHand hand = packet.getHand();
/* 1428 */     ItemStack itemStack = this.player.getItemInHand(hand);
/* 1430 */     if (!itemStack.isItemEnabled(level.enabledFeatures()))
/*      */       return; 
/* 1434 */     BlockHitResult blockHit = packet.getHitResult();
/* 1435 */     Vec3 location = blockHit.getLocation();
/* 1436 */     BlockPos pos = blockHit.getBlockPos();
/* 1437 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D))
/*      */       return; 
/* 1440 */     Vec3 distance = location.subtract(Vec3.atCenterOf((Vec3i)pos));
/* 1441 */     double limit = 1.0000001D;
/* 1443 */     if (Math.abs(distance.x()) >= 1.0000001D || Math.abs(distance.y()) >= 1.0000001D || Math.abs(distance.z()) >= 1.0000001D) {
/* 1444 */       LOGGER.warn("Rejecting UseItemOnPacket from {}: Location {} too far away from hit block {}.", new Object[] { this.player.getGameProfile().name(), location, pos });
/*      */       return;
/*      */     } 
/* 1448 */     Direction direction = blockHit.getDirection();
/* 1449 */     this.player.resetLastActionTime();
/* 1451 */     int maxY = level.getMaxY();
/* 1452 */     int minY = level.getMinY();
/* 1454 */     if (pos.getY() > maxY) {
/* 1455 */       this.player.sendBuildLimitMessage(true, maxY);
/*      */       return;
/*      */     } 
/* 1459 */     if (pos.getY() < minY) {
/* 1460 */       this.player.sendBuildLimitMessage(false, minY);
/*      */       return;
/*      */     } 
/* 1464 */     if (this.server.isUnderSpawnProtection(level, pos, (Player)this.player)) {
/* 1465 */       this.player.sendSpawnProtectionMessage(pos);
/* 1466 */     } else if (this.awaitingPositionFromClient == null && level.mayInteract((Entity)this.player, pos)) {
/* 1467 */       InteractionResult interactionResult = this.player.gameMode.useItemOn(this.player, (Level)level, itemStack, hand, blockHit);
/* 1469 */       if (interactionResult.consumesAction())
/* 1470 */         CriteriaTriggers.ANY_BLOCK_USE.trigger(this.player, blockHit.getBlockPos(), (ItemInstance)itemStack); 
/* 1473 */       if (direction == Direction.UP && !interactionResult.consumesAction() && pos.getY() >= maxY && wasBlockPlacementAttempt(this.player, itemStack)) {
/* 1475 */         this.player.sendBuildLimitMessage(true, maxY);
/* 1476 */       } else if (interactionResult instanceof InteractionResult.Success) {
/* 1476 */         InteractionResult.Success success = (InteractionResult.Success)interactionResult;
/* 1476 */         if (success.swingSource() == InteractionResult.SwingSource.SERVER)
/* 1477 */           this.player.swing(hand, true); 
/*      */       } 
/* 1480 */       if (!interactionResult.consumesAction() && wasBlockPlacementAttempt(this.player, itemStack)) {
/* 1481 */         if (direction == Direction.UP && pos.getY() >= maxY) {
/* 1482 */           this.player.sendBuildLimitMessage(true, maxY);
/* 1483 */         } else if (direction == Direction.DOWN && pos.getY() <= minY) {
/* 1484 */           this.player.sendBuildLimitMessage(false, minY);
/*      */         } 
/* 1486 */       } else if (interactionResult instanceof InteractionResult.Success) {
/* 1486 */         InteractionResult.Success success = (InteractionResult.Success)interactionResult;
/* 1486 */         if (success.swingSource() == InteractionResult.SwingSource.SERVER)
/* 1487 */           this.player.swing(hand, true); 
/*      */       } 
/*      */     } else {
/* 1490 */       this.player.sendBuildLimitMessage(true, maxY);
/*      */     } 
/* 1496 */     send((Packet<?>)new ClientboundBlockUpdatePacket((BlockGetter)level, pos));
/* 1497 */     send((Packet<?>)new ClientboundBlockUpdatePacket((BlockGetter)level, pos.relative(direction)));
/*      */   }
/*      */   
/*      */   public void handleUseItem(ServerboundUseItemPacket packet) {
/* 1502 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1503 */     if (!hasClientLoaded())
/*      */       return; 
/* 1506 */     ackBlockChangesUpTo(packet.getSequence());
/* 1508 */     ServerLevel level = this.player.level();
/* 1509 */     InteractionHand hand = packet.getHand();
/* 1510 */     ItemStack itemStack = this.player.getItemInHand(hand);
/* 1511 */     this.player.resetLastActionTime();
/* 1513 */     if (itemStack.isEmpty() || !itemStack.isItemEnabled(level.enabledFeatures()))
/*      */       return; 
/* 1517 */     float targetYRot = Mth.wrapDegrees(packet.getYRot());
/* 1518 */     float targetXRot = Mth.wrapDegrees(packet.getXRot());
/* 1519 */     if (targetXRot != this.player.getXRot() || targetYRot != this.player.getYRot())
/* 1521 */       this.player.absSnapRotationTo(targetYRot, targetXRot); 
/* 1524 */     InteractionResult useResult = this.player.gameMode.useItem(this.player, (Level)level, itemStack, hand);
/* 1525 */     if (useResult instanceof InteractionResult.Success) {
/* 1525 */       InteractionResult.Success success = (InteractionResult.Success)useResult;
/* 1525 */       if (success.swingSource() == InteractionResult.SwingSource.SERVER)
/* 1526 */         this.player.swing(hand, true); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) {
/* 1532 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1533 */     if (this.player.isSpectator())
/* 1534 */       for (ServerLevel level : this.server.getAllLevels()) {
/* 1535 */         Entity entity = packet.getEntity(level);
/* 1537 */         if (entity != null) {
/* 1538 */           this.player.teleportTo(level, entity.getX(), entity.getY(), entity.getZ(), Set.of(), entity.getYRot(), entity.getXRot(), true);
/*      */           return;
/*      */         } 
/*      */       }  
/*      */   }
/*      */   
/*      */   public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) {
/* 1547 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1548 */     Entity vehicle = this.player.getControlledVehicle();
/* 1549 */     if (vehicle instanceof AbstractBoat) {
/* 1549 */       AbstractBoat boat = (AbstractBoat)vehicle;
/* 1550 */       boat.setPaddleState(packet.getLeft(), packet.getRight());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onDisconnect(DisconnectionDetails details) {
/* 1556 */     LOGGER.info("{} lost connection: {}", this.player.getPlainTextName(), details.reason().getString());
/* 1557 */     removePlayerFromWorld();
/* 1559 */     super.onDisconnect(details);
/*      */   }
/*      */   
/*      */   private void removePlayerFromWorld() {
/* 1563 */     this.chatMessageChain.close();
/* 1565 */     this.server.invalidateStatus();
/* 1566 */     this.server.getPlayerList().broadcastSystemMessage((Component)Component.translatable("multiplayer.player.left", new Object[] { this.player.getDisplayName() }).withStyle(ChatFormatting.YELLOW), false);
/* 1567 */     this.player.disconnect();
/* 1568 */     this.server.getPlayerList().remove(this.player);
/* 1570 */     this.player.getTextFilter().leave();
/*      */   }
/*      */   
/*      */   public void ackBlockChangesUpTo(int packetSequenceNr) {
/* 1574 */     if (packetSequenceNr < 0)
/* 1575 */       throw new IllegalArgumentException("Expected packet sequence nr >= 0"); 
/* 1578 */     this.ackBlockChangesUpTo = Math.max(packetSequenceNr, this.ackBlockChangesUpTo);
/*      */   }
/*      */   
/*      */   public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {
/* 1583 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1584 */     if (packet.getSlot() < 0 || packet.getSlot() >= Inventory.getSelectionSize()) {
/* 1585 */       LOGGER.warn("{} tried to set an invalid carried item", this.player.getPlainTextName());
/*      */       return;
/*      */     } 
/* 1588 */     if (this.player.getInventory().getSelectedSlot() != packet.getSlot() && this.player.getUsedItemHand() == InteractionHand.MAIN_HAND)
/* 1589 */       this.player.stopUsingItem(); 
/* 1591 */     this.player.getInventory().setSelectedSlot(packet.getSlot());
/* 1592 */     this.player.resetLastActionTime();
/*      */   }
/*      */   
/*      */   public void handleChat(ServerboundChatPacket packet) {
/* 1597 */     Optional<LastSeenMessages> unpackedLastSeen = unpackAndApplyLastSeen(packet.lastSeenMessages());
/* 1598 */     if (unpackedLastSeen.isEmpty())
/*      */       return; 
/* 1601 */     tryHandleChat(packet.message(), false, () -> {
/*      */           PlayerChatMessage signedMessage;
/*      */           try {
/*      */             signedMessage = getSignedMessage(packet, unpackedLastSeen.get());
/* 1605 */           } catch (net.minecraft.network.chat.SignedMessageChain.DecodeException e) {
/*      */             handleMessageDecodeFailure(e);
/*      */             return;
/*      */           } 
/*      */           CompletableFuture<FilteredText> filteredFuture = filterTextPacket(signedMessage.signedContent());
/*      */           Component decorated = this.server.getChatDecorator().decorate(this.player, signedMessage.decoratedContent());
/*      */           this.chatMessageChain.append(filteredFuture, ());
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleChatCommand(ServerboundChatCommandPacket packet) {
/* 1624 */     tryHandleChat(packet.command(), true, () -> {
/*      */           performUnsignedChatCommand(packet.command());
/*      */           detectCommandRateSpam();
/*      */         });
/*      */   }
/*      */   
/*      */   private void performUnsignedChatCommand(String command) {
/* 1631 */     ParseResults<CommandSourceStack> parsed = parseCommand(command);
/* 1632 */     if (this.server.enforceSecureProfile() && SignableCommand.hasSignableArguments(parsed)) {
/* 1633 */       LOGGER.error("Received unsigned command packet from {}, but the command requires signable arguments: {}", this.player.getGameProfile().name(), command);
/* 1634 */       this.player.sendSystemMessage(INVALID_COMMAND_SIGNATURE);
/*      */       return;
/*      */     } 
/* 1637 */     this.server.getCommands().performCommand(parsed, command);
/*      */   }
/*      */   
/*      */   public void handleSignedChatCommand(ServerboundChatCommandSignedPacket packet) {
/* 1642 */     Optional<LastSeenMessages> unpackedLastSeen = unpackAndApplyLastSeen(packet.lastSeenMessages());
/* 1643 */     if (unpackedLastSeen.isEmpty())
/*      */       return; 
/* 1646 */     tryHandleChat(packet.command(), true, () -> {
/*      */           performSignedChatCommand(packet, unpackedLastSeen.get());
/*      */           detectCommandRateSpam();
/*      */         });
/*      */   }
/*      */   
/*      */   private void performSignedChatCommand(ServerboundChatCommandSignedPacket packet, LastSeenMessages lastSeenMessages) {
/*      */     Map<String, PlayerChatMessage> signedArguments;
/* 1653 */     ParseResults<CommandSourceStack> command = parseCommand(packet.command());
/*      */     try {
/* 1657 */       signedArguments = collectSignedArguments(packet, SignableCommand.of(command), lastSeenMessages);
/* 1658 */     } catch (net.minecraft.network.chat.SignedMessageChain.DecodeException e) {
/* 1659 */       handleMessageDecodeFailure(e);
/*      */       return;
/*      */     } 
/* 1663 */     CommandSigningContext.SignedArguments signedArguments1 = new CommandSigningContext.SignedArguments(signedArguments);
/* 1664 */     command = Commands.mapSource(command, source -> source.withSigningContext(signingContext, (TaskChainer)this.chatMessageChain));
/* 1666 */     this.server.getCommands().performCommand(command, packet.command());
/*      */   }
/*      */   
/*      */   private void handleMessageDecodeFailure(SignedMessageChain.DecodeException e) {
/* 1670 */     LOGGER.warn("Failed to update secure chat state for {}: '{}'", this.player.getGameProfile().name(), e.getComponent().getString());
/* 1671 */     this.player.sendSystemMessage((Component)e.getComponent().copy().withStyle(ChatFormatting.RED));
/*      */   }
/*      */   
/*      */   private <S> Map<String, PlayerChatMessage> collectSignedArguments(ServerboundChatCommandSignedPacket packet, SignableCommand<S> command, LastSeenMessages lastSeenMessages) throws SignedMessageChain.DecodeException {
/* 1676 */     List<ArgumentSignatures.Entry> argumentSignatures = packet.argumentSignatures().entries();
/* 1677 */     List<SignableCommand.Argument<S>> parsedArguments = command.arguments();
/* 1680 */     if (argumentSignatures.isEmpty())
/* 1681 */       return collectUnsignedArguments(parsedArguments); 
/* 1684 */     Object2ObjectOpenHashMap<String, PlayerChatMessage> object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/* 1687 */     for (ArgumentSignatures.Entry clientArgument : argumentSignatures) {
/* 1688 */       SignableCommand.Argument<S> expectedArgument = command.getArgument(clientArgument.name());
/* 1689 */       if (expectedArgument == null) {
/* 1691 */         this.signedMessageDecoder.setChainBroken();
/* 1692 */         throw createSignedArgumentMismatchException(packet.command(), argumentSignatures, parsedArguments);
/*      */       } 
/* 1694 */       SignedMessageBody body = new SignedMessageBody(expectedArgument.value(), packet.timeStamp(), packet.salt(), lastSeenMessages);
/* 1695 */       object2ObjectOpenHashMap.put(expectedArgument.name(), this.signedMessageDecoder.unpack(clientArgument.signature(), body));
/*      */     } 
/* 1698 */     for (SignableCommand.Argument<S> expectedArgument : parsedArguments) {
/* 1699 */       if (!object2ObjectOpenHashMap.containsKey(expectedArgument.name()))
/* 1700 */         throw createSignedArgumentMismatchException(packet.command(), argumentSignatures, parsedArguments); 
/*      */     } 
/* 1704 */     return (Map<String, PlayerChatMessage>)object2ObjectOpenHashMap;
/*      */   }
/*      */   
/*      */   private <S> Map<String, PlayerChatMessage> collectUnsignedArguments(List<SignableCommand.Argument<S>> parsedArguments) throws SignedMessageChain.DecodeException {
/* 1708 */     Map<String, PlayerChatMessage> arguments = new HashMap<>();
/* 1709 */     for (SignableCommand.Argument<S> parsedArgument : parsedArguments) {
/* 1710 */       SignedMessageBody body = SignedMessageBody.unsigned(parsedArgument.value());
/* 1711 */       arguments.put(parsedArgument.name(), this.signedMessageDecoder.unpack(null, body));
/*      */     } 
/* 1713 */     return arguments;
/*      */   }
/*      */   
/*      */   private static <S> SignedMessageChain.DecodeException createSignedArgumentMismatchException(String command, List<ArgumentSignatures.Entry> clientArguments, List<SignableCommand.Argument<S>> expectedArguments) {
/* 1717 */     String clientNames = clientArguments.stream().map(ArgumentSignatures.Entry::name).collect(Collectors.joining(", "));
/* 1718 */     String expectedNames = expectedArguments.stream().map(SignableCommand.Argument::name).collect(Collectors.joining(", "));
/* 1719 */     LOGGER.error("Signed command mismatch between server and client ('{}'): got [{}] from client, but expected [{}]", new Object[] { command, clientNames, expectedNames });
/* 1720 */     return new SignedMessageChain.DecodeException(INVALID_COMMAND_SIGNATURE);
/*      */   }
/*      */   
/*      */   private ParseResults<CommandSourceStack> parseCommand(String command) {
/* 1724 */     CommandDispatcher<CommandSourceStack> commands = this.server.getCommands().getDispatcher();
/* 1725 */     return commands.parse(command, this.player.createCommandSourceStack());
/*      */   }
/*      */   
/*      */   private void tryHandleChat(String message, boolean isCommand, Runnable chatHandler) {
/* 1729 */     if (isChatMessageIllegal(message)) {
/* 1730 */       disconnect((Component)Component.translatable("multiplayer.disconnect.illegal_characters"));
/*      */       return;
/*      */     } 
/* 1735 */     if (!isCommand && this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
/* 1738 */       send((Packet<?>)new ClientboundSystemChatPacket((Component)Component.translatable("chat.disabled.options").withStyle(ChatFormatting.RED), false));
/*      */       return;
/*      */     } 
/* 1742 */     this.player.resetLastActionTime();
/* 1745 */     this.server.execute(chatHandler);
/*      */   }
/*      */   
/*      */   private Optional<LastSeenMessages> unpackAndApplyLastSeen(LastSeenMessages.Update update) {
/* 1750 */     synchronized (this.lastSeenMessages) {
/* 1752 */       LastSeenMessages result = this.lastSeenMessages.applyUpdate(update);
/* 1753 */       return Optional.of(result);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isChatMessageIllegal(String message) {
/* 1763 */     for (int i = 0; i < message.length(); i++) {
/* 1764 */       if (!StringUtil.isAllowedChatCharacter(message.charAt(i)))
/* 1765 */         return true; 
/*      */     } 
/* 1768 */     return false;
/*      */   }
/*      */   
/*      */   private PlayerChatMessage getSignedMessage(ServerboundChatPacket packet, LastSeenMessages lastSeenMessages) throws SignedMessageChain.DecodeException {
/* 1772 */     SignedMessageBody body = new SignedMessageBody(packet.message(), packet.timeStamp(), packet.salt(), lastSeenMessages);
/* 1773 */     return this.signedMessageDecoder.unpack(packet.signature(), body);
/*      */   }
/*      */   
/*      */   private void broadcastChatMessage(PlayerChatMessage message) {
/* 1777 */     this.server.getPlayerList().broadcastChatMessage(message, this.player, ChatType.bind(ChatType.CHAT, (Entity)this.player));
/* 1778 */     detectChatRateSpam();
/*      */   }
/*      */   
/*      */   private void detectRateSpam(TickThrottler throttler) {
/* 1782 */     throttler.increment();
/* 1783 */     if (!throttler.isUnderThreshold() && !this.server.getPlayerList().isOp(this.player.nameAndId()) && !this.server.isSingleplayerOwner(this.player.nameAndId()))
/* 1784 */       disconnect((Component)Component.translatable("disconnect.spam")); 
/*      */   }
/*      */   
/*      */   private void detectCommandRateSpam() {
/* 1789 */     detectRateSpam(this.commandSpamThrottler);
/*      */   }
/*      */   
/*      */   private void detectChatRateSpam() {
/* 1793 */     detectRateSpam(this.chatSpamThrottler);
/*      */   }
/*      */   
/*      */   public void handleChatAck(ServerboundChatAckPacket packet) {
/* 1799 */     synchronized (this.lastSeenMessages) {
/*      */       try {
/* 1801 */         this.lastSeenMessages.applyOffset(packet.offset());
/* 1802 */       } catch (net.minecraft.network.chat.LastSeenMessagesValidator.ValidationException e) {
/* 1803 */         LOGGER.error("Failed to validate message acknowledgement offset from {}: {}", this.player.getPlainTextName(), e.getMessage());
/* 1804 */         disconnect(CHAT_VALIDATION_FAILED);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleAnimate(ServerboundSwingPacket packet) {
/* 1811 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1812 */     this.player.resetLastActionTime();
/* 1813 */     this.player.swing(packet.getHand());
/*      */   }
/*      */   
/*      */   public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {
/*      */     Entity entity;
/* 1818 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1819 */     if (!hasClientLoaded())
/*      */       return; 
/* 1822 */     this.player.resetLastActionTime();
/* 1823 */     switch (packet.getAction()) {
/*      */       case PERFORM_RESPAWN:
/* 1824 */         this.player.setSprinting(true);
/*      */         return;
/*      */       case REQUEST_STATS:
/* 1825 */         this.player.setSprinting(false);
/*      */         return;
/*      */       case REQUEST_GAMERULE_VALUES:
/* 1827 */         if (this.player.isSleeping()) {
/* 1828 */           this.player.stopSleepInBed(false, true);
/* 1829 */           this.awaitingPositionFromClient = this.player.position();
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1833 */         entity = this.player.getControlledVehicle();
/* 1833 */         if (entity instanceof PlayerRideableJumping) {
/* 1833 */           PlayerRideableJumping vehicle = (PlayerRideableJumping)entity;
/* 1834 */           int data = packet.getData();
/* 1835 */           if (vehicle.canJump() && data > 0)
/* 1836 */             vehicle.handleStartJump(data); 
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1841 */         entity = this.player.getControlledVehicle();
/* 1841 */         if (entity instanceof PlayerRideableJumping) {
/* 1841 */           PlayerRideableJumping vehicle = (PlayerRideableJumping)entity;
/* 1842 */           vehicle.handleStopJump();
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1846 */         entity = this.player.getVehicle();
/* 1846 */         if (entity instanceof HasCustomInventoryScreen) {
/* 1846 */           HasCustomInventoryScreen vehicleWithInventory = (HasCustomInventoryScreen)entity;
/* 1847 */           vehicleWithInventory.openCustomInventoryScreen((Player)this.player);
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1851 */         if (!this.player.tryToStartFallFlying())
/* 1853 */           this.player.stopFallFlying(); 
/*      */         return;
/*      */     } 
/* 1856 */     throw new IllegalArgumentException("Invalid client command!");
/*      */   }
/*      */   
/*      */   public void sendPlayerChatMessage(PlayerChatMessage message, ChatType.Bound chatType) {
/*      */     int trackedCount;
/* 1861 */     send((Packet<?>)new ClientboundPlayerChatPacket(this.nextChatIndex++, message
/*      */           
/* 1863 */           .link().sender(), message
/* 1864 */           .link().index(), message
/* 1865 */           .signature(), message
/* 1866 */           .signedBody().pack(this.messageSignatureCache), message
/* 1867 */           .unsignedContent(), message
/* 1868 */           .filterMask(), chatType));
/* 1872 */     MessageSignature signature = message.signature();
/* 1873 */     if (signature == null)
/*      */       return; 
/* 1877 */     this.messageSignatureCache.push(message.signedBody(), message.signature());
/* 1880 */     synchronized (this.lastSeenMessages) {
/* 1881 */       this.lastSeenMessages.addPending(signature);
/* 1882 */       trackedCount = this.lastSeenMessages.trackedMessagesCount();
/*      */     } 
/* 1884 */     if (trackedCount > 4096)
/* 1885 */       disconnect((Component)Component.translatable("multiplayer.disconnect.too_many_pending_chats")); 
/*      */   }
/*      */   
/*      */   public void sendDisguisedChatMessage(Component content, ChatType.Bound chatType) {
/* 1890 */     send((Packet<?>)new ClientboundDisguisedChatPacket(content, chatType));
/*      */   }
/*      */   
/*      */   public SocketAddress getRemoteAddress() {
/* 1894 */     return this.connection.getRemoteAddress();
/*      */   }
/*      */   
/*      */   public void switchToConfig() {
/* 1898 */     this.waitingForSwitchToConfig = true;
/* 1899 */     removePlayerFromWorld();
/* 1900 */     send((Packet<?>)ClientboundStartConfigurationPacket.INSTANCE);
/* 1901 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.CLIENTBOUND);
/*      */   }
/*      */   
/*      */   public void handlePingRequest(ServerboundPingRequestPacket packet) {
/* 1906 */     this.connection.send((Packet)new ClientboundPongResponsePacket(packet.getTime()));
/*      */   }
/*      */   
/*      */   public void handleAttack(ServerboundAttackPacket packet) {
/* 1911 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1912 */     if (!hasClientLoaded() || this.player.isSpectator())
/*      */       return; 
/* 1916 */     ServerLevel level = this.player.level();
/* 1917 */     Entity target = level.getEntityOrPart(packet.entityId());
/* 1918 */     this.player.resetLastActionTime();
/* 1920 */     if (target == null || !level.getWorldBorder().isWithinBounds(target.blockPosition()))
/*      */       return; 
/* 1924 */     AABB targetBounds = target.getBoundingBox();
/* 1925 */     ItemStack mainHandItem = this.player.getMainHandItem();
/* 1926 */     if (!this.player.isWithinAttackRange(mainHandItem, targetBounds, 3.0D))
/*      */       return; 
/* 1930 */     if (mainHandItem.has(DataComponents.PIERCING_WEAPON))
/*      */       return; 
/* 1934 */     if (!(target instanceof net.minecraft.world.entity.item.ItemEntity) && !(target instanceof net.minecraft.world.entity.ExperienceOrb) && target != this.player) {
/* 1934 */       if (target instanceof AbstractArrow) {
/* 1935 */         AbstractArrow abstractArrow = (AbstractArrow)target;
/* 1935 */         if (!abstractArrow.isAttackable()) {
/* 1936 */           disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
/* 1937 */           LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getPlainTextName());
/*      */           return;
/*      */         } 
/*      */       } 
/* 1941 */       if (!mainHandItem.isItemEnabled(level.enabledFeatures()))
/*      */         return; 
/* 1946 */       if (this.player.cannotAttackWithItem(mainHandItem, 5))
/*      */         return; 
/* 1950 */       this.player.attack(target);
/*      */       return;
/*      */     } 
/*      */     disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
/*      */     LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getPlainTextName());
/*      */   }
/*      */   
/*      */   public void handleInteract(ServerboundInteractPacket packet) {
/* 1955 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1956 */     if (!hasClientLoaded())
/*      */       return; 
/* 1960 */     ServerLevel level = this.player.level();
/* 1961 */     Entity target = level.getEntityOrPart(packet.entityId());
/* 1962 */     this.player.resetLastActionTime();
/* 1964 */     this.player.setShiftKeyDown(packet.usingSecondaryAction());
/* 1966 */     if (target == null || !level.getWorldBorder().isWithinBounds(target.blockPosition()))
/*      */       return; 
/* 1970 */     AABB targetBounds = target.getBoundingBox();
/* 1971 */     if (!this.player.isWithinEntityInteractionRange(targetBounds, 3.0D))
/*      */       return; 
/* 1975 */     InteractionHand hand = packet.hand();
/* 1976 */     Vec3 location = packet.location();
/* 1978 */     ItemStack tool = this.player.getItemInHand(hand);
/* 1979 */     if (!tool.isItemEnabled(level.enabledFeatures()))
/*      */       return; 
/* 1982 */     ItemStack usedItemStack = tool.copy();
/* 1983 */     InteractionResult result = this.player.interactOn(target, hand, location);
/* 1984 */     if (result instanceof InteractionResult.Success) {
/* 1984 */       InteractionResult.Success success = (InteractionResult.Success)result;
/* 1985 */       ItemStack awardedForStack = success.wasItemInteraction() ? usedItemStack : ItemStack.EMPTY;
/* 1986 */       CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(this.player, awardedForStack, target);
/* 1987 */       if (success.swingSource() == InteractionResult.SwingSource.SERVER)
/* 1988 */         this.player.swing(hand, true); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSpectatorAction(ServerboundSpectatorActionPacket packet) {
/* 1995 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 1996 */     if (!hasClientLoaded() || !this.player.isSpectator())
/*      */       return; 
/* 2000 */     this.player.resetLastActionTime();
/* 2002 */     if (!packet.spectateEntityId().isPresent())
/*      */       return; 
/* 2005 */     ServerLevel level = this.player.level();
/* 2006 */     Entity target = level.getEntityOrPart(packet.spectateEntityId().getAsInt());
/* 2007 */     if (target == null || !level.getWorldBorder().isWithinBounds(target.blockPosition()))
/*      */       return; 
/* 2009 */     if (!this.player.isWithinEntityInteractionRange(target.getBoundingBox(), 3.0D))
/*      */       return; 
/* 2011 */     if (!target.isPickable())
/*      */       return; 
/* 2015 */     this.player.setCamera(target);
/*      */   }
/*      */   
/*      */   public void handleClientCommand(ServerboundClientCommandPacket packet) {
/* 2020 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2021 */     this.player.resetLastActionTime();
/* 2022 */     ServerboundClientCommandPacket.Action action = packet.getAction();
/* 2023 */     switch (action) {
/*      */       case PERFORM_RESPAWN:
/* 2025 */         if (this.player.wonGame) {
/* 2026 */           this.player.wonGame = false;
/* 2027 */           this.player = this.server.getPlayerList().respawn(this.player, true, Entity.RemovalReason.CHANGED_DIMENSION);
/* 2028 */           resetPosition();
/* 2029 */           restartClientLoadTimerAfterRespawn();
/* 2030 */           CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD);
/*      */           break;
/*      */         } 
/* 2032 */         if (this.player.getHealth() > 0.0F)
/*      */           return; 
/* 2035 */         this.player = this.server.getPlayerList().respawn(this.player, false, Entity.RemovalReason.KILLED);
/* 2036 */         resetPosition();
/* 2037 */         restartClientLoadTimerAfterRespawn();
/* 2038 */         if (this.server.isHardcore())
/* 2039 */           this.player.setGameMode(GameType.SPECTATOR); 
/*      */         break;
/*      */       case REQUEST_STATS:
/* 2043 */         this.player.getStats().sendStats(this.player);
/*      */         break;
/*      */       case REQUEST_GAMERULE_VALUES:
/* 2044 */         sendGameRuleValues();
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendGameRuleValues() {
/* 2049 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 2050 */       LOGGER.warn("Player {} tried to request game rule values without required permissions", this.player.getGameProfile().name());
/*      */       return;
/*      */     } 
/* 2054 */     GameRules gameRules = this.player.level().getGameRules();
/* 2055 */     Map<ResourceKey<GameRule<?>>, String> values = new HashMap<>();
/* 2056 */     gameRules.availableRules().forEach(rule -> addGameRuleValue(gameRules, values, rule));
/* 2057 */     send((Packet<?>)new ClientboundGameRuleValuesPacket(values));
/*      */   }
/*      */   
/*      */   private static <T> void addGameRuleValue(GameRules gameRules, Map<ResourceKey<GameRule<?>>, String> values, GameRule<T> rule) {
/* 2061 */     BuiltInRegistries.GAME_RULE.getResourceKey(rule)
/* 2062 */       .ifPresent(key -> values.put(key, rule.serialize(gameRules.get(rule))));
/*      */   }
/*      */   
/*      */   public void handleContainerClose(ServerboundContainerClosePacket packet) {
/* 2067 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2068 */     this.player.doCloseContainer();
/*      */   }
/*      */   
/*      */   public void handleContainerClick(ServerboundContainerClickPacket packet) {
/* 2073 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2074 */     this.player.resetLastActionTime();
/* 2075 */     if (this.player.containerMenu.containerId != packet.containerId())
/*      */       return; 
/* 2079 */     if (this.player.isSpectator() || this.player.isDeadOrDying()) {
/* 2081 */       this.player.containerMenu.sendAllDataToRemote();
/*      */       return;
/*      */     } 
/* 2085 */     if (!this.player.containerMenu.stillValid((Player)this.player)) {
/* 2086 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       return;
/*      */     } 
/* 2090 */     int slotIndex = packet.slotNum();
/* 2091 */     if (!this.player.containerMenu.isValidSlotIndex(slotIndex)) {
/* 2093 */       LOGGER.debug("Player {} clicked invalid slot index: {}, available slots: {}", new Object[] { this.player.getPlainTextName(), Integer.valueOf(slotIndex), Integer.valueOf(this.player.containerMenu.slots.size()) });
/*      */       return;
/*      */     } 
/* 2098 */     boolean fullResyncNeeded = (packet.stateId() != this.player.containerMenu.getStateId());
/* 2101 */     this.player.containerMenu.suppressRemoteUpdates();
/* 2102 */     this.player.containerMenu.clicked(slotIndex, packet.buttonNum(), packet.containerInput(), (Player)this.player);
/* 2105 */     for (ObjectIterator<Int2ObjectMap.Entry<HashedStack>> objectIterator = Int2ObjectMaps.fastIterable(packet.changedSlots()).iterator(); objectIterator.hasNext(); ) {
/* 2105 */       Int2ObjectMap.Entry<HashedStack> e = objectIterator.next();
/* 2106 */       this.player.containerMenu.setRemoteSlotUnsafe(e.getIntKey(), (HashedStack)e.getValue());
/*      */     } 
/* 2108 */     this.player.containerMenu.setRemoteCarried(packet.carriedItem());
/* 2111 */     this.player.containerMenu.resumeRemoteUpdates();
/* 2112 */     if (fullResyncNeeded) {
/* 2113 */       this.player.containerMenu.broadcastFullState();
/*      */     } else {
/* 2115 */       this.player.containerMenu.broadcastChanges();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) {
/* 2121 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2122 */     this.player.resetLastActionTime();
/* 2123 */     if (this.player.isSpectator() || this.player.containerMenu.containerId != packet.containerId())
/*      */       return; 
/* 2127 */     if (!this.player.containerMenu.stillValid((Player)this.player)) {
/* 2128 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       return;
/*      */     } 
/* 2132 */     RecipeManager.ServerDisplayInfo displayInfo = this.server.getRecipeManager().getRecipeFromDisplay(packet.recipe());
/* 2133 */     if (displayInfo == null)
/*      */       return; 
/* 2137 */     RecipeHolder<?> recipe = displayInfo.parent();
/* 2138 */     if (!this.player.getRecipeBook().contains(recipe.id()))
/*      */       return; 
/* 2142 */     AbstractContainerMenu abstractContainerMenu = this.player.containerMenu;
/* 2142 */     if (abstractContainerMenu instanceof RecipeBookMenu) {
/* 2142 */       RecipeBookMenu recipeBookMenu = (RecipeBookMenu)abstractContainerMenu;
/* 2143 */       if (recipe.value().placementInfo().isImpossibleToPlace()) {
/* 2144 */         LOGGER.debug("Player {} tried to place impossible recipe {}", this.player, recipe.id().identifier());
/*      */         return;
/*      */       } 
/* 2149 */       RecipeBookMenu.PostPlaceAction postPlaceAction = recipeBookMenu.handlePlacement(packet.useMaxItems(), this.player.isCreative(), recipe, this.player.level(), this.player.getInventory());
/* 2150 */       if (postPlaceAction == RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE)
/* 2151 */         send((Packet<?>)new ClientboundPlaceGhostRecipePacket(this.player.containerMenu.containerId, displayInfo.display().display())); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {
/* 2158 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2159 */     this.player.resetLastActionTime();
/* 2160 */     if (this.player.containerMenu.containerId != packet.containerId() || this.player.isSpectator())
/*      */       return; 
/* 2164 */     if (!this.player.containerMenu.stillValid((Player)this.player)) {
/* 2165 */       LOGGER.debug("Player {} interacted with invalid menu {}", this.player, this.player.containerMenu);
/*      */       return;
/*      */     } 
/* 2169 */     boolean clickAccepted = this.player.containerMenu.clickMenuButton((Player)this.player, packet.buttonId());
/* 2170 */     if (clickAccepted)
/* 2171 */       this.player.containerMenu.broadcastChanges(); 
/*      */   }
/*      */   
/*      */   public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {
/* 2177 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2178 */     if (this.player.hasInfiniteMaterials()) {
/* 2179 */       boolean drop = (packet.slotNum() < 0);
/* 2180 */       ItemStack itemStack = packet.itemStack();
/* 2181 */       if (!itemStack.isItemEnabled(this.player.level().enabledFeatures()))
/*      */         return; 
/* 2185 */       boolean validSlot = (packet.slotNum() >= 1 && packet.slotNum() <= 45);
/* 2186 */       boolean validData = (itemStack.isEmpty() || itemStack.getCount() <= itemStack.getMaxStackSize());
/* 2188 */       if (validSlot && validData) {
/* 2189 */         this.player.inventoryMenu.getSlot(packet.slotNum()).setByPlayer(itemStack);
/* 2192 */         this.player.inventoryMenu.setRemoteSlot(packet.slotNum(), itemStack);
/* 2193 */         this.player.inventoryMenu.broadcastChanges();
/* 2194 */       } else if (drop && validData) {
/* 2195 */         if (this.dropSpamThrottler.isUnderThreshold()) {
/* 2196 */           this.dropSpamThrottler.increment();
/* 2198 */           this.player.drop(itemStack, true);
/*      */         } else {
/* 2200 */           LOGGER.warn("Player {} was dropping items too fast in creative mode, ignoring.", this.player.getPlainTextName());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSignUpdate(ServerboundSignUpdatePacket packet) {
/* 2208 */     List<String> lines = (List<String>)Stream.<String>of(packet.getLines()).map(ChatFormatting::stripFormatting).collect(Collectors.toList());
/* 2209 */     filterTextPacket(lines).thenAcceptAsync(filteredLines -> updateSignText(packet, filteredLines), (Executor)this.server);
/*      */   }
/*      */   
/*      */   private void updateSignText(ServerboundSignUpdatePacket packet, List<FilteredText> lines) {
/* 2213 */     this.player.resetLastActionTime();
/* 2214 */     ServerLevel level = this.player.level();
/* 2215 */     BlockPos pos = packet.getPos();
/* 2216 */     if (level.hasChunkAt(pos)) {
/*      */       SignBlockEntity sign;
/* 2217 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 2219 */       if (blockEntity instanceof SignBlockEntity) {
/* 2219 */         sign = (SignBlockEntity)blockEntity;
/*      */       } else {
/*      */         return;
/*      */       } 
/* 2223 */       sign.updateSignText((Player)this.player, packet.isFrontText(), lines);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {
/* 2229 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2230 */     (this.player.getAbilities()).flying = (packet.isFlying() && (this.player.getAbilities()).mayfly);
/*      */   }
/*      */   
/*      */   public void handleClientInformation(ServerboundClientInformationPacket packet) {
/* 2235 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2236 */     boolean wasHatShown = this.player.isModelPartShown(PlayerModelPart.HAT);
/* 2237 */     this.player.updateOptions(packet.information());
/* 2238 */     if (this.player.isModelPartShown(PlayerModelPart.HAT) != wasHatShown)
/* 2239 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_HAT, this.player)); 
/*      */   }
/*      */   
/*      */   public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) {
/* 2245 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2247 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) && !isSingleplayerOwner()) {
/* 2248 */       LOGGER.warn("Player {} tried to change difficulty to {} without required permissions", this.player.getGameProfile().name(), packet.difficulty().getDisplayName());
/*      */       return;
/*      */     } 
/* 2252 */     this.server.setDifficulty(packet.difficulty(), false);
/*      */   }
/*      */   
/*      */   public void handleChangeGameMode(ServerboundChangeGameModePacket packet) {
/* 2257 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2259 */     if (!GameModeCommand.PERMISSION_CHECK.check(this.player.permissions())) {
/* 2260 */       LOGGER.warn("Player {} tried to change game mode to {} without required permissions", this.player.getGameProfile().name(), packet.mode().getShortDisplayName().getString());
/*      */       return;
/*      */     } 
/* 2264 */     GameModeCommand.setGameMode(this.player, packet.mode());
/*      */   }
/*      */   
/*      */   public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) {
/* 2269 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2271 */     if (!this.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) && !isSingleplayerOwner())
/*      */       return; 
/* 2275 */     this.server.setDifficultyLocked(packet.isLocked());
/*      */   }
/*      */   
/*      */   public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket packet) {
/* 2280 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2282 */     RemoteChatSession.Data newChatSession = packet.chatSession();
/* 2283 */     ProfilePublicKey.Data oldProfileKey = (this.chatSession != null) ? this.chatSession.profilePublicKey().data() : null;
/* 2284 */     ProfilePublicKey.Data newProfileKey = newChatSession.profilePublicKey();
/* 2285 */     if (Objects.equals(oldProfileKey, newProfileKey))
/*      */       return; 
/* 2289 */     if (oldProfileKey != null && newProfileKey.expiresAt().isBefore(oldProfileKey.expiresAt())) {
/* 2290 */       disconnect(ProfilePublicKey.EXPIRED_PROFILE_PUBLIC_KEY);
/*      */       return;
/*      */     } 
/*      */     try {
/* 2295 */       SignatureValidator profileKeySignatureValidator = this.server.services().profileKeySignatureValidator();
/* 2296 */       if (profileKeySignatureValidator == null) {
/* 2297 */         LOGGER.warn("Ignoring chat session from {} due to missing Services public key", this.player.getGameProfile().name());
/*      */         return;
/*      */       } 
/* 2300 */       resetPlayerChatState(newChatSession.validate(this.player.getGameProfile(), profileKeySignatureValidator));
/* 2301 */     } catch (net.minecraft.world.entity.player.ProfilePublicKey.ValidationException e) {
/* 2302 */       LOGGER.error("Failed to validate profile key: {}", e.getMessage());
/* 2303 */       disconnect(e.getComponent());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleConfigurationAcknowledged(ServerboundConfigurationAcknowledgedPacket packet) {
/* 2309 */     if (!this.waitingForSwitchToConfig)
/* 2310 */       throw new IllegalStateException("Client acknowledged config, but none was requested"); 
/* 2312 */     this.connection.setupInboundProtocol(ConfigurationProtocols.SERVERBOUND, (PacketListener)new ServerConfigurationPacketListenerImpl(this.server, this.connection, createCookie(this.player.clientInformation())));
/*      */   }
/*      */   
/*      */   public void handleChunkBatchReceived(ServerboundChunkBatchReceivedPacket packet) {
/* 2317 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2318 */     this.chunkSender.onChunkBatchReceivedByClient(packet.desiredChunksPerTick());
/*      */   }
/*      */   
/*      */   public void handleDebugSubscriptionRequest(ServerboundDebugSubscriptionRequestPacket packet) {
/* 2323 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2324 */     this.player.requestDebugSubscriptions(packet.subscriptions());
/*      */   }
/*      */   
/*      */   private void resetPlayerChatState(RemoteChatSession chatSession) {
/* 2328 */     this.chatSession = chatSession;
/* 2329 */     this.signedMessageDecoder = chatSession.createMessageDecoder(this.player.getUUID());
/* 2332 */     this.chatMessageChain.append(() -> {
/*      */           this.player.setChatSession(chatSession);
/*      */           this.server.getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT), List.of(this.player)));
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {}
/*      */   
/*      */   public void handleClientTickEnd(ServerboundClientTickEndPacket packet) {
/* 2349 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.player.level());
/* 2350 */     if (!this.receivedMovementThisTick)
/* 2351 */       this.player.setKnownMovement(Vec3.ZERO); 
/* 2353 */     this.receivedMovementThisTick = false;
/*      */   }
/*      */   
/*      */   private void handlePlayerKnownMovement(Vec3 movement) {
/* 2357 */     if (movement.lengthSqr() > 9.999999747378752E-6D)
/* 2358 */       this.player.resetLastActionTime(); 
/* 2360 */     this.player.setKnownMovement(movement);
/* 2361 */     this.receivedMovementThisTick = true;
/*      */   }
/*      */   
/*      */   public boolean hasInfiniteMaterials() {
/* 2366 */     return this.player.hasInfiniteMaterials();
/*      */   }
/*      */   
/*      */   public ServerPlayer getPlayer() {
/* 2371 */     return this.player;
/*      */   }
/*      */   
/*      */   public boolean hasClientLoaded() {
/* 2376 */     return (!this.waitingForRespawn && this.clientLoadedTimeoutTimer <= 0);
/*      */   }
/*      */   
/*      */   public void tickClientLoadTimeout() {
/* 2380 */     if (this.clientLoadedTimeoutTimer > 0)
/* 2381 */       this.clientLoadedTimeoutTimer--; 
/*      */   }
/*      */   
/*      */   private void markClientLoaded() {
/* 2387 */     this.clientLoadedTimeoutTimer = 0;
/*      */   }
/*      */   
/*      */   public void markClientUnloadedAfterDeath() {
/* 2392 */     this.waitingForRespawn = true;
/*      */   }
/*      */   
/*      */   private void restartClientLoadTimerAfterRespawn() {
/* 2396 */     this.waitingForRespawn = false;
/* 2397 */     this.clientLoadedTimeoutTimer = 60;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerGamePacketListenerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */