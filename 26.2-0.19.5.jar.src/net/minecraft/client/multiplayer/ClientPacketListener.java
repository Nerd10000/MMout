/*      */ package net.minecraft.client.multiplayer;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.hash.HashCode;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.ParseResults;
/*      */ import com.mojang.brigadier.arguments.ArgumentType;
/*      */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*      */ import com.mojang.brigadier.context.CommandContext;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.AdvancementHolder;
/*      */ import net.minecraft.client.ClientClockManager;
/*      */ import net.minecraft.client.ClientRecipeBook;
/*      */ import net.minecraft.client.DebugQueryHandler;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.Options;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.PopupScreen;
/*      */ import net.minecraft.client.gui.components.toasts.RecipeToast;
/*      */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*      */ import net.minecraft.client.gui.components.toasts.Toast;
/*      */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*      */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*      */ import net.minecraft.client.gui.screens.DeathScreen;
/*      */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*      */ import net.minecraft.client.gui.screens.MenuScreens;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.WinScreen;
/*      */ import net.minecraft.client.gui.screens.achievement.StatsScreen;
/*      */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*      */ import net.minecraft.client.gui.screens.inventory.BookViewScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.NautilusInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestInstanceBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
/*      */ import net.minecraft.client.gui.screens.options.HasDifficultyReaction;
/*      */ import net.minecraft.client.gui.screens.options.InWorldGameRulesScreen;
/*      */ import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
/*      */ import net.minecraft.client.particle.ItemPickupParticle;
/*      */ import net.minecraft.client.particle.Particle;
/*      */ import net.minecraft.client.player.ClientInput;
/*      */ import net.minecraft.client.player.KeyboardInput;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.RemotePlayer;
/*      */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*      */ import net.minecraft.client.resources.sounds.BeeAggressiveSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.BeeFlyingSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.GuardianAttackSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.MinecartSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SnifferSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.resources.sounds.TickableSoundInstance;
/*      */ import net.minecraft.client.waypoints.ClientWaypointManager;
/*      */ import net.minecraft.commands.CommandBuildContext;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*      */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.HashedPatchMap;
/*      */ import net.minecraft.network.PacketListener;
/*      */ import net.minecraft.network.TickablePacketListener;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.LastSeenMessagesTracker;
/*      */ import net.minecraft.network.chat.LocalChatSession;
/*      */ import net.minecraft.network.chat.MessageSignature;
/*      */ import net.minecraft.network.chat.MessageSignatureCache;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.PlayerChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.SignableCommand;
/*      */ import net.minecraft.network.chat.SignedMessageBody;
/*      */ import net.minecraft.network.chat.SignedMessageChain;
/*      */ import net.minecraft.network.chat.SignedMessageLink;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketUtils;
/*      */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*      */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*      */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*      */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBundlePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDeleteChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameRuleValuesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameTestHighlightPosPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
/*      */ import net.minecraft.network.protocol.game.ClientboundLoginPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLowDiskSpaceWarningPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMountScreenOpenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveMinecartPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundProjectilePowerPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookRemovePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTabListPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTestInstanceBlockStatus;
/*      */ import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTickingStepPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
/*      */ import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
/*      */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
/*      */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*      */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.permissions.Permission;
/*      */ import net.minecraft.server.permissions.PermissionCheck;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.StatsCounter;
/*      */ import net.minecraft.tags.TagNetworkSerialization;
/*      */ import net.minecraft.util.CommonLinks;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.HashOps;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.SignatureValidator;
/*      */ import net.minecraft.util.debug.DebugValueAccess;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.animal.bee.Bee;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Guardian;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*      */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*      */ import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
/*      */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
/*      */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*      */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.MerchantMenu;
/*      */ import net.minecraft.world.inventory.NautilusInventoryMenu;
/*      */ import net.minecraft.world.item.CreativeModeTabs;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*      */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LightLayer;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.DataLayer;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*      */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ClientPacketListener extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {
/*  350 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  352 */   private static final Component UNSECURE_SERVER_TOAST_TITLE = (Component)Component.translatable("multiplayer.unsecureserver.toast.title");
/*      */   
/*  353 */   private static final Component UNSERURE_SERVER_TOAST = (Component)Component.translatable("multiplayer.unsecureserver.toast");
/*      */   
/*  354 */   private static final Component INVALID_PACKET = (Component)Component.translatable("multiplayer.disconnect.invalid_packet");
/*      */   
/*  355 */   private static final Component RECONFIGURE_SCREEN_MESSAGE = (Component)Component.translatable("connect.reconfiguring");
/*      */   
/*  356 */   private static final Component BAD_CHAT_INDEX = (Component)Component.translatable("multiplayer.disconnect.bad_chat_index");
/*      */   
/*  357 */   private static final Component COMMAND_SEND_CONFIRM_TITLE = (Component)Component.translatable("multiplayer.confirm_command.title");
/*      */   
/*  358 */   private static final Component BUTTON_RUN_COMMAND = (Component)Component.translatable("multiplayer.confirm_command.run_command");
/*      */   
/*  359 */   private static final Component BUTTON_SUGGEST_COMMAND = (Component)Component.translatable("multiplayer.confirm_command.suggest_command");
/*      */   
/*      */   private static final int PENDING_OFFSET_THRESHOLD = 64;
/*      */   
/*      */   public static final int TELEPORT_INTERPOLATION_THRESHOLD = 64;
/*      */   
/*  368 */   private static final Permission RESTRICTED_COMMAND = (Permission)Permission.Atom.create("client/commands/restricted");
/*      */   
/*  370 */   private static final PermissionCheck RESTRICTED_COMMAND_CHECK = (PermissionCheck)new PermissionCheck.Require(RESTRICTED_COMMAND);
/*      */   
/*      */   private static final PermissionSet ALLOW_RESTRICTED_COMMANDS;
/*      */   
/*      */   static {
/*  372 */     ALLOW_RESTRICTED_COMMANDS = (permission -> permission.equals(RESTRICTED_COMMAND));
/*      */   }
/*      */   
/*  374 */   private static final ClientboundCommandsPacket.NodeBuilder<ClientSuggestionProvider> COMMAND_NODE_BUILDER = new ClientboundCommandsPacket.NodeBuilder<ClientSuggestionProvider>() {
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> createLiteral(String id) {
/*  378 */         return (ArgumentBuilder<ClientSuggestionProvider, ?>)LiteralArgumentBuilder.literal(id);
/*      */       }
/*      */       
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> createArgument(String id, ArgumentType<?> argumentType, Identifier suggestionId) {
/*  383 */         RequiredArgumentBuilder<ClientSuggestionProvider, ?> builder = RequiredArgumentBuilder.argument(id, argumentType);
/*  384 */         if (suggestionId != null)
/*  385 */           builder.suggests(SuggestionProviders.getProvider(suggestionId)); 
/*  387 */         return (ArgumentBuilder<ClientSuggestionProvider, ?>)builder;
/*      */       }
/*      */       
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> configure(ArgumentBuilder<ClientSuggestionProvider, ?> builder, boolean executable, boolean restricted) {
/*  392 */         if (executable)
/*  393 */           builder.executes(c -> 0); 
/*  395 */         if (restricted)
/*  396 */           builder.requires((Predicate)Commands.hasPermission(ClientPacketListener.RESTRICTED_COMMAND_CHECK)); 
/*  398 */         return builder;
/*      */       }
/*      */     };
/*      */   
/*      */   private final GameProfile localGameProfile;
/*      */   
/*      */   private ClientLevel level;
/*      */   
/*      */   private ClientLevel.ClientLevelData levelData;
/*      */   
/*  405 */   private final Map<UUID, PlayerInfo> playerInfoMap = Maps.newHashMap();
/*      */   
/*  406 */   private final Set<PlayerInfo> listedPlayers = (Set<PlayerInfo>)new ReferenceOpenHashSet();
/*      */   
/*      */   private final ClientAdvancements advancements;
/*      */   
/*      */   private final ClientSuggestionProvider suggestionsProvider;
/*      */   
/*      */   private final ClientSuggestionProvider restrictedSuggestionsProvider;
/*      */   
/*  410 */   private final DebugQueryHandler debugQueryHandler = new DebugQueryHandler(this);
/*      */   
/*  411 */   private int serverChunkRadius = 3;
/*      */   
/*  412 */   private int serverSimulationDistance = 3;
/*      */   
/*  415 */   private final RandomSource random = RandomSource.createThreadSafe();
/*      */   
/*  416 */   private CommandDispatcher<ClientSuggestionProvider> commands = new CommandDispatcher();
/*      */   
/*  417 */   private ClientRecipeContainer recipes = new ClientRecipeContainer(Map.of(), SelectableRecipe.SingleInputSet.empty());
/*      */   
/*      */   private Set<ResourceKey<Level>> levels;
/*      */   
/*      */   private final RegistryAccess.Frozen registryAccess;
/*      */   
/*      */   private final FeatureFlagSet enabledFeatures;
/*      */   
/*      */   private final PotionBrewing potionBrewing;
/*      */   
/*      */   private FuelValues fuelValues;
/*      */   
/*      */   private final HashedPatchMap.HashGenerator decoratedHashOpsGenerator;
/*      */   
/*  427 */   private OptionalInt removedPlayerVehicleId = OptionalInt.empty();
/*      */   
/*      */   private LocalChatSession chatSession;
/*      */   
/*  430 */   private SignedMessageChain.Encoder signedMessageEncoder = SignedMessageChain.Encoder.UNSIGNED;
/*      */   
/*      */   private int nextChatIndex;
/*      */   
/*  433 */   private LastSeenMessagesTracker lastSeenMessages = new LastSeenMessagesTracker(20);
/*      */   
/*  434 */   private MessageSignatureCache messageSignatureCache = MessageSignatureCache.createDefault();
/*      */   
/*      */   private CompletableFuture<Optional<ProfileKeyPair>> keyPairFuture;
/*      */   
/*      */   private ClientInformation remoteClientInformation;
/*      */   
/*  440 */   private final ChunkBatchSizeCalculator chunkBatchSizeCalculator = new ChunkBatchSizeCalculator();
/*      */   
/*      */   private final PingDebugMonitor pingDebugMonitor;
/*      */   
/*      */   private final ClientDebugSubscriber debugSubscriber;
/*      */   
/*      */   private LevelLoadTracker levelLoadTracker;
/*      */   
/*      */   private boolean serverEnforcesSecureChat;
/*      */   
/*      */   private boolean onlineMode;
/*      */   
/*      */   private volatile boolean closed;
/*      */   
/*  450 */   private final Scoreboard scoreboard = new Scoreboard();
/*      */   
/*  451 */   private final ClientWaypointManager waypointManager = new ClientWaypointManager();
/*      */   
/*      */   private final ClientClockManager clockManager;
/*      */   
/*  454 */   private final SessionSearchTrees searchTrees = new SessionSearchTrees();
/*      */   
/*  459 */   private final List<WeakReference<CacheSlot<?, ?>>> cacheSlots = new ArrayList<>();
/*      */   
/*      */   private boolean clientLoaded;
/*      */   
/*      */   public ClientPacketListener(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
/*  464 */     super(minecraft, connection, cookie);
/*  465 */     this.localGameProfile = cookie.localGameProfile();
/*  466 */     this.registryAccess = cookie.receivedRegistries();
/*  467 */     RegistryOps<HashCode> hashOps = this.registryAccess.createSerializationContext((DynamicOps)HashOps.CRC32C_INSTANCE);
/*  468 */     this.decoratedHashOpsGenerator = (component -> Integer.valueOf(((HashCode)component.encodeValue((DynamicOps)hashOps).getOrThrow(())).asInt()));
/*  469 */     this.enabledFeatures = cookie.enabledFeatures();
/*  470 */     this.advancements = new ClientAdvancements(minecraft, this.telemetryManager);
/*  472 */     PermissionSet playerPermissions = permission -> {
/*      */         LocalPlayer player = minecraft.player;
/*  474 */         return (player != null && player.permissions().hasPermission(permission));
/*      */       };
/*  477 */     this.suggestionsProvider = new ClientSuggestionProvider(this, minecraft, playerPermissions.union(ALLOW_RESTRICTED_COMMANDS));
/*  479 */     this.restrictedSuggestionsProvider = new ClientSuggestionProvider(this, minecraft, PermissionSet.NO_PERMISSIONS);
/*  480 */     this.pingDebugMonitor = new PingDebugMonitor(this, minecraft.getDebugOverlay().getPingLogger());
/*  481 */     this.debugSubscriber = new ClientDebugSubscriber(this, minecraft.getDebugOverlay());
/*  482 */     if (cookie.chatState() != null)
/*  483 */       minecraft.gui.hud.getChat().restoreState(cookie.chatState()); 
/*  485 */     this.potionBrewing = PotionBrewing.bootstrap(this.enabledFeatures);
/*  486 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)cookie.receivedRegistries(), this.enabledFeatures);
/*  487 */     this.levelLoadTracker = cookie.levelLoadTracker();
/*  488 */     this.clockManager = new ClientClockManager();
/*      */   }
/*      */   
/*      */   public ClientSuggestionProvider getSuggestionsProvider() {
/*  492 */     return this.suggestionsProvider;
/*      */   }
/*      */   
/*      */   public void close() {
/*  496 */     this.closed = true;
/*  497 */     clearLevel();
/*  498 */     this.telemetryManager.onDisconnect();
/*      */   }
/*      */   
/*      */   public void clearLevel() {
/*  502 */     clearCacheSlots();
/*  503 */     this.level = null;
/*  504 */     this.levelLoadTracker = null;
/*      */   }
/*      */   
/*      */   private void clearCacheSlots() {
/*  508 */     for (WeakReference<CacheSlot<?, ?>> cacheSlot : this.cacheSlots) {
/*  509 */       CacheSlot<?, ?> slot = cacheSlot.get();
/*  510 */       if (slot != null)
/*  511 */         slot.clear(); 
/*      */     } 
/*  514 */     this.cacheSlots.clear();
/*      */   }
/*      */   
/*      */   public RecipeAccess recipes() {
/*  518 */     return this.recipes;
/*      */   }
/*      */   
/*      */   public void handleLogin(ClientboundLoginPacket packet) {
/*  525 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  527 */     this.minecraft.gameMode = new MultiPlayerGameMode(this.minecraft, this);
/*  528 */     CommonPlayerSpawnInfo spawnInfo = packet.commonPlayerSpawnInfo();
/*  530 */     List<ResourceKey<Level>> levels = Lists.newArrayList(packet.levels());
/*  531 */     Collections.shuffle(levels);
/*  533 */     this.levels = Sets.newLinkedHashSet(levels);
/*  534 */     ResourceKey<Level> dimension = spawnInfo.dimension();
/*  535 */     Holder<DimensionType> dimensionType = spawnInfo.dimensionType();
/*  537 */     this.serverChunkRadius = packet.chunkRadius();
/*  538 */     this.serverSimulationDistance = packet.simulationDistance();
/*  540 */     boolean isDebug = spawnInfo.isDebug();
/*  541 */     boolean isFlat = spawnInfo.isFlat();
/*  542 */     int seaLevel = spawnInfo.seaLevel();
/*  543 */     ClientLevel.ClientLevelData levelData = new ClientLevel.ClientLevelData(Difficulty.NORMAL, packet.hardcore(), isFlat);
/*  544 */     this.levelData = levelData;
/*  545 */     this.level = new ClientLevel(this, levelData, dimension, dimensionType, this.serverChunkRadius, this.serverSimulationDistance, this.minecraft.levelExtractor, isDebug, spawnInfo.seed(), seaLevel);
/*  546 */     this.minecraft.setLevel(this.level);
/*  549 */     if (this.minecraft.player == null) {
/*  550 */       this.minecraft.player = this.minecraft.gameMode.createPlayer(this.level, new StatsCounter(), new ClientRecipeBook());
/*  551 */       this.minecraft.player.setYRot(-180.0F);
/*  552 */       if (this.minecraft.getSingleplayerServer() != null)
/*  553 */         this.minecraft.getSingleplayerServer().setUUID(this.minecraft.player.getUUID()); 
/*      */     } 
/*  557 */     setClientLoaded(false);
/*  558 */     this.debugSubscriber.clear();
/*  559 */     this.minecraft.levelExtractor.debugRenderer.refreshRendererList();
/*  561 */     this.minecraft.player.resetPos();
/*  562 */     this.minecraft.player.setId(packet.playerId());
/*  563 */     this.level.addEntity((Entity)this.minecraft.player);
/*  564 */     this.minecraft.player.input = (ClientInput)new KeyboardInput(this.minecraft.options);
/*  565 */     this.minecraft.gameMode.adjustPlayer((Player)this.minecraft.player);
/*  566 */     this.minecraft.setCameraEntity((Entity)this.minecraft.player);
/*  567 */     startWaitingForNewLevel(this.minecraft.player, this.level, LevelLoadingScreen.Reason.OTHER);
/*  568 */     this.minecraft.player.setReducedDebugInfo(packet.reducedDebugInfo());
/*  569 */     this.minecraft.player.setShowDeathScreen(packet.showDeathScreen());
/*  570 */     this.minecraft.player.setDoLimitedCrafting(packet.doLimitedCrafting());
/*  571 */     this.minecraft.player.setLastDeathLocation(spawnInfo.lastDeathLocation());
/*  572 */     this.minecraft.player.setPortalCooldown(spawnInfo.portalCooldown());
/*  573 */     this.minecraft.gameMode.setLocalMode(spawnInfo.gameType(), spawnInfo.previousGameType());
/*  574 */     this.minecraft.options.setServerRenderDistance(packet.chunkRadius());
/*  578 */     this.chatSession = null;
/*  579 */     this.signedMessageEncoder = SignedMessageChain.Encoder.UNSIGNED;
/*  580 */     this.nextChatIndex = 0;
/*  581 */     this.lastSeenMessages = new LastSeenMessagesTracker(20);
/*  582 */     this.messageSignatureCache = MessageSignatureCache.createDefault();
/*  583 */     this.onlineMode = packet.onlineMode();
/*  584 */     if (packet.onlineMode())
/*  585 */       prepareKeyPair(); 
/*  588 */     this.telemetryManager.onPlayerInfoReceived(spawnInfo.gameType(), packet.hardcore());
/*  589 */     this.minecraft.quickPlayLog().log(this.minecraft);
/*  591 */     this.serverEnforcesSecureChat = packet.enforcesSecureChat();
/*  593 */     if (this.serverData != null && !this.seenInsecureChatWarning && !enforcesSecureChat()) {
/*  594 */       SystemToast toast = new SystemToast(SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, UNSERURE_SERVER_TOAST);
/*  595 */       this.minecraft.gui.toastManager().addToast((Toast)toast);
/*  596 */       this.seenInsecureChatWarning = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleAddEntity(ClientboundAddEntityPacket packet) {
/*  602 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  604 */     if (this.removedPlayerVehicleId.isPresent() && this.removedPlayerVehicleId.getAsInt() == packet.getId())
/*  605 */       this.removedPlayerVehicleId = OptionalInt.empty(); 
/*  608 */     Entity entity = createEntityFromPacket(packet);
/*  609 */     if (entity != null) {
/*  610 */       entity.recreateFromPacket(packet);
/*  611 */       this.level.addEntity(entity);
/*  613 */       postAddEntitySoundInstance(entity);
/*      */     } else {
/*  615 */       LOGGER.warn("Skipping Entity with id {}", packet.getType());
/*      */     } 
/*  618 */     if (entity instanceof Player) {
/*  618 */       Player player = (Player)entity;
/*  619 */       UUID uuid = player.getUUID();
/*  620 */       PlayerInfo playerInfo = this.playerInfoMap.get(uuid);
/*  621 */       if (playerInfo != null)
/*  622 */         this.seenPlayers.put(uuid, playerInfo); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Entity createEntityFromPacket(ClientboundAddEntityPacket packet) {
/*  628 */     EntityType<?> type = packet.getType();
/*  629 */     if (type == EntityTypes.PLAYER) {
/*  630 */       PlayerInfo playerInfo = getPlayerInfo(packet.getUUID());
/*  631 */       if (playerInfo == null) {
/*  632 */         LOGGER.warn("Server attempted to add player prior to sending player info (Player id {})", packet.getUUID());
/*  633 */         return null;
/*      */       } 
/*  635 */       return (Entity)new RemotePlayer(this.level, playerInfo.getProfile());
/*      */     } 
/*  637 */     return type.create(this.level, EntitySpawnReason.LOAD);
/*      */   }
/*      */   
/*      */   private void postAddEntitySoundInstance(Entity entity) {
/*  641 */     if (entity instanceof AbstractMinecart) {
/*  641 */       AbstractMinecart minecart = (AbstractMinecart)entity;
/*  642 */       this.minecraft.getSoundManager().play((SoundInstance)new MinecartSoundInstance(minecart));
/*  643 */     } else if (entity instanceof Bee) {
/*      */       BeeFlyingSoundInstance beeFlyingSoundInstance;
/*  643 */       Bee bee = (Bee)entity;
/*  644 */       boolean angry = bee.isAngry();
/*  646 */       if (angry) {
/*  647 */         BeeAggressiveSoundInstance beeAggressiveSoundInstance = new BeeAggressiveSoundInstance(bee);
/*      */       } else {
/*  649 */         beeFlyingSoundInstance = new BeeFlyingSoundInstance(bee);
/*      */       } 
/*  651 */       this.minecraft.getSoundManager().queueTickingSound((TickableSoundInstance)beeFlyingSoundInstance);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetEntityMotion(ClientboundSetEntityMotionPacket packet) {
/*  657 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  658 */     Entity entity = this.level.getEntity(packet.id());
/*  659 */     if (entity == null)
/*      */       return; 
/*  662 */     entity.lerpMotion(packet.movement());
/*      */   }
/*      */   
/*      */   public void handleSetEntityData(ClientboundSetEntityDataPacket packet) {
/*  667 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  668 */     Entity entity = this.level.getEntity(packet.id());
/*  669 */     if (entity != null)
/*  670 */       entity.getEntityData().assignValues(packet.packedItems()); 
/*      */   }
/*      */   
/*      */   public void handleEntityPositionSync(ClientboundEntityPositionSyncPacket packet) {
/*  676 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  677 */     Entity entity = this.level.getEntity(packet.id());
/*  678 */     if (entity == null)
/*      */       return; 
/*  682 */     Vec3 pos = packet.values().position();
/*  683 */     entity.getPositionCodec().setBase(pos);
/*  684 */     if (entity.isLocalInstanceAuthoritative())
/*      */       return; 
/*  689 */     float yRot = packet.values().yRot();
/*  690 */     float xRot = packet.values().xRot();
/*  691 */     boolean tooBigToInterpolate = (entity.position().distanceToSqr(pos) > 4096.0D);
/*  692 */     if (this.level.isTickingEntity(entity) && !tooBigToInterpolate) {
/*  693 */       entity.moveOrInterpolateTo(pos, yRot, xRot);
/*      */     } else {
/*  695 */       entity.snapTo(pos, yRot, xRot);
/*      */     } 
/*  698 */     if (!entity.isInterpolating() && entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*  699 */       entity.positionRider((Entity)this.minecraft.player);
/*  700 */       this.minecraft.player.setOldPosAndRot();
/*      */     } 
/*  703 */     entity.setOnGround(packet.onGround());
/*      */   }
/*      */   
/*      */   public void handleTeleportEntity(ClientboundTeleportEntityPacket packet) {
/*  708 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  709 */     Entity entity = this.level.getEntity(packet.id());
/*  710 */     if (entity == null) {
/*  711 */       if (this.removedPlayerVehicleId.isPresent() && this.removedPlayerVehicleId.getAsInt() == packet.id()) {
/*  712 */         LOGGER.debug("Trying to teleport entity with id {}, that was formerly player vehicle, applying teleport to player instead", Integer.valueOf(packet.id()));
/*  713 */         setValuesFromPositionPacket(packet.change(), packet.relatives(), (Entity)this.minecraft.player, false);
/*  714 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), this.minecraft.player.getYRot(), this.minecraft.player.getXRot(), false, false));
/*      */       } 
/*      */       return;
/*      */     } 
/*  719 */     boolean hasRelative = (packet.relatives().contains(Relative.X) || packet.relatives().contains(Relative.Y) || packet.relatives().contains(Relative.Z));
/*  720 */     boolean interpolate = (this.level.isTickingEntity(entity) || !entity.isLocalInstanceAuthoritative() || hasRelative);
/*  721 */     boolean wasInterpolated = setValuesFromPositionPacket(packet.change(), packet.relatives(), entity, interpolate);
/*  722 */     entity.setOnGround(packet.onGround());
/*  724 */     if (!wasInterpolated && entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*  725 */       entity.positionRider((Entity)this.minecraft.player);
/*  726 */       this.minecraft.player.setOldPosAndRot();
/*  727 */       if (entity.isLocalInstanceAuthoritative())
/*  728 */         this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(entity)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleTickingState(ClientboundTickingStatePacket packet) {
/*  735 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  736 */     if (this.minecraft.level == null)
/*      */       return; 
/*  739 */     TickRateManager manager = this.minecraft.level.tickRateManager();
/*  740 */     manager.setTickRate(packet.tickRate());
/*  741 */     manager.setFrozen(packet.isFrozen());
/*      */   }
/*      */   
/*      */   public void handleTickingStep(ClientboundTickingStepPacket packet) {
/*  746 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  747 */     if (this.minecraft.level == null)
/*      */       return; 
/*  750 */     TickRateManager manager = this.minecraft.level.tickRateManager();
/*  751 */     manager.setFrozenTicksToRun(packet.tickSteps());
/*      */   }
/*      */   
/*      */   public void handleSetHeldSlot(ClientboundSetHeldSlotPacket packet) {
/*  756 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  757 */     if (Inventory.isHotbarSlot(packet.slot()))
/*  758 */       this.minecraft.player.getInventory().setSelectedSlot(packet.slot()); 
/*      */   }
/*      */   
/*      */   public void handleMoveEntity(ClientboundMoveEntityPacket packet) {
/*  764 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  765 */     Entity entity = packet.getEntity(this.level);
/*  766 */     if (entity == null)
/*      */       return; 
/*  770 */     if (entity.isLocalInstanceAuthoritative()) {
/*  771 */       VecDeltaCodec positionCodec = entity.getPositionCodec();
/*  772 */       Vec3 pos = positionCodec.decode(packet.getXa(), packet.getYa(), packet.getZa());
/*  773 */       positionCodec.setBase(pos);
/*      */       return;
/*      */     } 
/*  777 */     if (packet.hasPosition()) {
/*  778 */       VecDeltaCodec positionCodec = entity.getPositionCodec();
/*  779 */       Vec3 pos = positionCodec.decode(packet.getXa(), packet.getYa(), packet.getZa());
/*  780 */       positionCodec.setBase(pos);
/*  781 */       if (packet.hasRotation()) {
/*  782 */         entity.moveOrInterpolateTo(pos, packet.getYRot(), packet.getXRot());
/*      */       } else {
/*  784 */         entity.moveOrInterpolateTo(pos);
/*      */       } 
/*  786 */     } else if (packet.hasRotation()) {
/*  787 */       entity.moveOrInterpolateTo(packet.getYRot(), packet.getXRot());
/*      */     } 
/*  789 */     entity.setOnGround(packet.isOnGround());
/*      */   }
/*      */   
/*      */   public void handleMinecartAlongTrack(ClientboundMoveMinecartPacket packet) {
/*      */     AbstractMinecart minecart;
/*  794 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  795 */     Entity entity = packet.getEntity(this.level);
/*  796 */     if (entity instanceof AbstractMinecart) {
/*  796 */       minecart = (AbstractMinecart)entity;
/*      */     } else {
/*      */       return;
/*      */     } 
/*  799 */     MinecartBehavior minecartBehavior = minecart.getBehavior();
/*  799 */     if (minecartBehavior instanceof NewMinecartBehavior) {
/*  799 */       NewMinecartBehavior newMinecartBehavior = (NewMinecartBehavior)minecartBehavior;
/*  800 */       newMinecartBehavior.lerpSteps.addAll(packet.lerpSteps());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleRotateMob(ClientboundRotateHeadPacket packet) {
/*  806 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  807 */     Entity entity = packet.getEntity(this.level);
/*  808 */     if (entity == null)
/*      */       return; 
/*  811 */     entity.lerpHeadTo(packet.getYHeadRot(), 3);
/*      */   }
/*      */   
/*      */   public void handleRemoveEntities(ClientboundRemoveEntitiesPacket packet) {
/*  816 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  817 */     packet.getEntityIds().forEach(entityId -> {
/*      */           Entity entity = this.level.getEntity(entityId);
/*      */           if (entity == null)
/*      */             return; 
/*      */           if (entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*      */             LOGGER.debug("Remove entity {}:{} that has player as passenger", entity.typeHolder().getRegisteredName(), Integer.valueOf(entityId));
/*      */             this.removedPlayerVehicleId = OptionalInt.of(entityId);
/*      */           } 
/*      */           this.level.removeEntity(entityId, Entity.RemovalReason.DISCARDED);
/*      */           this.debugSubscriber.dropEntity(entity);
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleMovePlayer(ClientboundPlayerPositionPacket packet) {
/*  833 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  834 */     LocalPlayer localPlayer = this.minecraft.player;
/*  835 */     if (!localPlayer.isPassenger())
/*  837 */       setValuesFromPositionPacket(packet.change(), packet.relatives(), (Entity)localPlayer, false); 
/*  839 */     this.connection.send((Packet)new ServerboundAcceptTeleportationPacket(packet.id()));
/*  840 */     this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), localPlayer.getYRot(), localPlayer.getXRot(), false, false));
/*  842 */     this.minecraft.level.getBlockStatePredictionHandler().onTeleport();
/*      */   }
/*      */   
/*      */   private static boolean setValuesFromPositionPacket(PositionMoveRotation change, Set<Relative> relatives, Entity entity, boolean interpolate) {
/*  846 */     PositionMoveRotation currentValues = PositionMoveRotation.of(entity);
/*  848 */     PositionMoveRotation newValues = PositionMoveRotation.calculateAbsolute(currentValues, change, relatives);
/*  850 */     boolean tooBigToInterpolate = (currentValues.position().distanceToSqr(newValues.position()) > 4096.0D);
/*  851 */     if (interpolate && !tooBigToInterpolate) {
/*  852 */       entity.moveOrInterpolateTo(newValues.position(), newValues.yRot(), newValues.xRot());
/*  853 */       entity.setDeltaMovement(newValues.deltaMovement());
/*  854 */       return true;
/*      */     } 
/*  857 */     entity.setPos(newValues.position());
/*  858 */     entity.setDeltaMovement(newValues.deltaMovement());
/*  859 */     entity.setYRot(newValues.yRot());
/*  860 */     entity.setXRot(newValues.xRot());
/*  863 */     PositionMoveRotation currentInterpolationValues = new PositionMoveRotation(entity.oldPosition(), Vec3.ZERO, entity.yRotO, entity.xRotO);
/*  864 */     PositionMoveRotation interpolationValues = PositionMoveRotation.calculateAbsolute(currentInterpolationValues, change, relatives);
/*  865 */     entity.setOldPosAndRot(interpolationValues.position(), interpolationValues.yRot(), interpolationValues.xRot());
/*  866 */     return false;
/*      */   }
/*      */   
/*      */   public void handleRotatePlayer(ClientboundPlayerRotationPacket packet) {
/*  871 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  872 */     LocalPlayer localPlayer = this.minecraft.player;
/*  874 */     Set<Relative> relatives = Relative.rotation(packet.relativeY(), packet.relativeX());
/*  875 */     PositionMoveRotation currentValues = PositionMoveRotation.of((Entity)localPlayer);
/*  876 */     PositionMoveRotation newValues = PositionMoveRotation.calculateAbsolute(currentValues, currentValues.withRotation(packet.yRot(), packet.xRot()), relatives);
/*  878 */     localPlayer.setYRot(newValues.yRot());
/*  879 */     localPlayer.setXRot(newValues.xRot());
/*  880 */     localPlayer.setOldRot();
/*  881 */     this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(localPlayer.getYRot(), localPlayer.getXRot(), false, false));
/*      */   }
/*      */   
/*      */   public void handleChunkBlocksUpdate(ClientboundSectionBlocksUpdatePacket packet) {
/*  886 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  888 */     packet.runUpdates((pos, state) -> this.level.setServerVerifiedBlockState(pos, state, 19));
/*      */   }
/*      */   
/*      */   public void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet) {
/*  893 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  894 */     int x = packet.getX();
/*  895 */     int z = packet.getZ();
/*  896 */     updateLevelChunk(x, z, packet.getChunkData());
/*  897 */     ClientboundLightUpdatePacketData lightData = packet.getLightData();
/*  898 */     this.level.queueLightUpdate(() -> {
/*      */           applyLightData(x, z, lightData, false);
/*      */           LevelChunk chunk = this.level.getChunkSource().getChunk(x, z, false);
/*      */           if (chunk != null)
/*      */             enableChunkLight(chunk, x, z); 
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleChunksBiomes(ClientboundChunksBiomesPacket packet) {
/*  909 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  911 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : packet.chunkBiomeData())
/*  912 */       this.level.getChunkSource().replaceBiomes(data.pos().x(), data.pos().z(), data.getReadBuffer()); 
/*  915 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : packet.chunkBiomeData())
/*  916 */       this.level.onChunkLoaded(new ChunkPos(data.pos().x(), data.pos().z())); 
/*  919 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : packet.chunkBiomeData()) {
/*  920 */       for (int xOffset = -1; xOffset <= 1; xOffset++) {
/*  921 */         for (int zOffset = -1; zOffset <= 1; zOffset++) {
/*  922 */           for (int y = this.level.getMinSectionY(); y <= this.level.getMaxSectionY(); y++)
/*  923 */             this.minecraft.levelExtractor.setSectionDirty(data.pos().x() + xOffset, y, data.pos().z() + zOffset); 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateLevelChunk(int x, int z, ClientboundLevelChunkPacketData chunkData) {
/*  931 */     this.level.getChunkSource().replaceWithPacketData(x, z, chunkData.getReadBuffer(), chunkData
/*  932 */         .getHeightmaps(), chunkData.getBlockEntitiesTagsConsumer(x, z));
/*      */   }
/*      */   
/*      */   private void enableChunkLight(LevelChunk chunk, int x, int z) {
/*  936 */     LevelLightEngine lightEngine = this.level.getChunkSource().getLightEngine();
/*  937 */     LevelChunkSection[] sections = chunk.getSections();
/*  938 */     ChunkPos chunkPos = chunk.getPos();
/*  939 */     for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/*  940 */       LevelChunkSection section = sections[sectionIndex];
/*  941 */       int sectionY = this.level.getSectionYFromSectionIndex(sectionIndex);
/*  942 */       lightEngine.updateSectionStatus(SectionPos.of(chunkPos, sectionY), section.hasOnlyAir());
/*      */     } 
/*  944 */     this.level.setSectionRangeDirty(x - 1, this.level.getMinSectionY(), z - 1, x + 1, this.level.getMaxSectionY(), z + 1);
/*      */   }
/*      */   
/*      */   public void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket packet) {
/*  949 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  950 */     this.level.getChunkSource().drop(packet.pos());
/*  951 */     this.debugSubscriber.dropChunk(packet.pos());
/*  952 */     queueLightRemoval(packet);
/*      */   }
/*      */   
/*      */   private void queueLightRemoval(ClientboundForgetLevelChunkPacket packet) {
/*  956 */     ChunkPos chunkPos = packet.pos();
/*  957 */     this.level.queueLightUpdate(() -> {
/*      */           LevelLightEngine lightEngine = this.level.getLightEngine();
/*      */           lightEngine.setLightEnabled(chunkPos, false);
/*      */           int sectionY;
/*      */           for (sectionY = lightEngine.getMinLightSection(); sectionY < lightEngine.getMaxLightSection(); sectionY++) {
/*      */             SectionPos sectionPos = SectionPos.of(chunkPos, sectionY);
/*      */             lightEngine.queueSectionData(LightLayer.BLOCK, sectionPos, null);
/*      */             lightEngine.queueSectionData(LightLayer.SKY, sectionPos, null);
/*      */           } 
/*      */           for (sectionY = this.level.getMinSectionY(); sectionY <= this.level.getMaxSectionY(); sectionY++)
/*      */             lightEngine.updateSectionStatus(SectionPos.of(chunkPos, sectionY), true); 
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleBlockUpdate(ClientboundBlockUpdatePacket packet) {
/*  973 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  974 */     this.level.setServerVerifiedBlockState(packet.getPos(), packet.getBlockState(), 19);
/*      */   }
/*      */   
/*      */   public void handleConfigurationStart(ClientboundStartConfigurationPacket packet) {
/*  979 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  983 */     this.minecraft.gui.chatListener().flushQueue();
/*  985 */     sendChatAcknowledgement();
/*  987 */     ChatComponent.State chatState = this.minecraft.gui.hud.getChat().storeState();
/*  988 */     this.minecraft.clearClientLevel((Screen)new ServerReconfigScreen(RECONFIGURE_SCREEN_MESSAGE, this.connection));
/*  990 */     this.connection.setupInboundProtocol(ConfigurationProtocols.CLIENTBOUND, (PacketListener)new ClientConfigurationPacketListenerImpl(this.minecraft, this.connection, new CommonListenerCookie(new LevelLoadTracker(), this.localGameProfile, this.telemetryManager, this.registryAccess, this.enabledFeatures, this.serverBrand, this.serverData, this.postDisconnectScreen, this.serverCookies, chatState, this.customReportDetails, 
/*      */             
/* 1005 */             serverLinks(), this.seenPlayers, this.seenInsecureChatWarning)));
/* 1010 */     send((Packet<?>)ServerboundConfigurationAcknowledgedPacket.INSTANCE);
/* 1011 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.SERVERBOUND);
/*      */   }
/*      */   
/*      */   public void handleTakeItemEntity(ClientboundTakeItemEntityPacket packet) {
/*      */     LocalPlayer localPlayer;
/* 1016 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1017 */     Entity from = this.level.getEntity(packet.getItemId());
/* 1018 */     LivingEntity to = (LivingEntity)this.level.getEntity(packet.getPlayerId());
/* 1019 */     if (to == null)
/* 1020 */       localPlayer = this.minecraft.player; 
/* 1022 */     if (from != null) {
/* 1023 */       if (from instanceof net.minecraft.world.entity.ExperienceOrb) {
/* 1024 */         this.level.playLocalSound(from.getX(), from.getY(), from.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.35F + 0.9F, false);
/*      */       } else {
/* 1026 */         this.level.playLocalSound(from.getX(), from.getY(), from.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F, false);
/*      */       } 
/* 1029 */       EntityRenderState itemState = this.minecraft.getEntityRenderDispatcher().extractEntity(from, 1.0F);
/* 1030 */       this.minecraft.particleEngine.add((Particle)new ItemPickupParticle(this.level, itemState, (Entity)localPlayer, from.getDeltaMovement()));
/* 1031 */       if (from instanceof ItemEntity) {
/* 1031 */         ItemEntity itemEntity = (ItemEntity)from;
/* 1033 */         ItemStack itemStack = itemEntity.getItem();
/* 1034 */         if (!itemStack.isEmpty())
/* 1035 */           itemStack.shrink(packet.getAmount()); 
/* 1037 */         if (itemStack.isEmpty())
/* 1038 */           this.level.removeEntity(packet.getItemId(), Entity.RemovalReason.DISCARDED); 
/* 1040 */       } else if (!(from instanceof net.minecraft.world.entity.ExperienceOrb)) {
/* 1041 */         this.level.removeEntity(packet.getItemId(), Entity.RemovalReason.DISCARDED);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSystemChat(ClientboundSystemChatPacket packet) {
/* 1048 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1049 */     if (packet.overlay()) {
/* 1050 */       this.minecraft.gui.chatListener().handleOverlay(packet.content());
/*      */     } else {
/* 1052 */       this.minecraft.gui.chatListener().handleSystemMessage(packet.content(), true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlayerChat(ClientboundPlayerChatPacket packet) {
/*      */     SignedMessageLink link;
/* 1058 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1060 */     int expectedChatIndex = this.nextChatIndex++;
/* 1061 */     if (packet.globalIndex() != expectedChatIndex) {
/* 1062 */       LOGGER.error("Missing or out-of-order chat message from server, expected index {} but got {}", Integer.valueOf(expectedChatIndex), Integer.valueOf(packet.globalIndex()));
/* 1063 */       this.connection.disconnect(BAD_CHAT_INDEX);
/*      */       return;
/*      */     } 
/* 1067 */     Optional<SignedMessageBody> body = packet.body().unpack(this.messageSignatureCache);
/* 1068 */     if (body.isEmpty()) {
/* 1069 */       LOGGER.error("Message from player with ID {} referenced unrecognized signature id", packet.sender());
/* 1071 */       this.connection.disconnect(INVALID_PACKET);
/*      */       return;
/*      */     } 
/* 1075 */     this.messageSignatureCache.push(body.get(), packet.signature());
/* 1078 */     UUID senderId = packet.sender();
/* 1079 */     PlayerInfo sender = getPlayerInfo(senderId);
/* 1080 */     if (sender == null) {
/* 1081 */       LOGGER.error("Received player chat packet for unknown player with ID: {}", senderId);
/* 1082 */       this.minecraft.gui.chatListener().handleChatMessageError(senderId, packet.signature(), packet.chatType());
/*      */       return;
/*      */     } 
/* 1086 */     RemoteChatSession chatSession = sender.getChatSession();
/* 1088 */     if (chatSession != null) {
/* 1089 */       link = new SignedMessageLink(packet.index(), senderId, chatSession.sessionId());
/*      */     } else {
/* 1091 */       link = SignedMessageLink.unsigned(senderId);
/*      */     } 
/* 1094 */     PlayerChatMessage message = new PlayerChatMessage(link, packet.signature(), body.get(), packet.unsignedContent(), packet.filterMask());
/* 1095 */     message = sender.getMessageValidator().updateAndValidate(message);
/* 1096 */     if (message != null) {
/* 1097 */       this.minecraft.gui.chatListener().handlePlayerChatMessage(message, sender.getProfile(), packet.chatType());
/*      */     } else {
/* 1099 */       this.minecraft.gui.chatListener().handleChatMessageError(senderId, packet.signature(), packet.chatType());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleDisguisedChat(ClientboundDisguisedChatPacket packet) {
/* 1105 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1106 */     this.minecraft.gui.chatListener().handleDisguisedChatMessage(packet.message(), packet.chatType());
/*      */   }
/*      */   
/*      */   public void handleDeleteChat(ClientboundDeleteChatPacket packet) {
/* 1111 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1113 */     Optional<MessageSignature> signature = packet.messageSignature().unpack(this.messageSignatureCache);
/* 1114 */     if (signature.isEmpty()) {
/* 1115 */       this.connection.disconnect(INVALID_PACKET);
/*      */       return;
/*      */     } 
/* 1119 */     this.lastSeenMessages.ignorePending(signature.get());
/* 1121 */     if (!this.minecraft.gui.chatListener().removeFromDelayedMessageQueue(signature.get()))
/* 1122 */       this.minecraft.gui.hud.getChat().deleteMessage(signature.get()); 
/*      */   }
/*      */   
/*      */   public void handleAnimate(ClientboundAnimatePacket packet) {
/* 1128 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1129 */     Entity entity = this.level.getEntity(packet.getId());
/* 1130 */     if (entity == null)
/*      */       return; 
/* 1133 */     if (packet.getAction() == 0) {
/* 1134 */       LivingEntity mob = (LivingEntity)entity;
/* 1135 */       mob.swing(InteractionHand.MAIN_HAND);
/* 1136 */     } else if (packet.getAction() == 3) {
/* 1137 */       LivingEntity mob = (LivingEntity)entity;
/* 1138 */       mob.swing(InteractionHand.OFF_HAND);
/* 1139 */     } else if (packet.getAction() == 2) {
/* 1140 */       Player player = (Player)entity;
/* 1141 */       player.stopSleepInBed(false, false);
/* 1142 */     } else if (packet.getAction() == 4) {
/* 1143 */       this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.CRIT);
/* 1144 */     } else if (packet.getAction() == 5) {
/* 1145 */       this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.ENCHANTED_HIT);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleHurtAnimation(ClientboundHurtAnimationPacket packet) {
/* 1151 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1152 */     Entity entity = this.level.getEntity(packet.id());
/* 1153 */     if (entity == null)
/*      */       return; 
/* 1156 */     entity.animateHurt(packet.yaw());
/*      */   }
/*      */   
/*      */   public void handleSetTime(ClientboundSetTimePacket packet) {
/* 1161 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1162 */     long gameTime = packet.gameTime();
/* 1163 */     this.level.setTimeFromServer(gameTime);
/* 1164 */     this.telemetryManager.setTime(gameTime);
/* 1165 */     this.clockManager.handleUpdates(gameTime, packet.clockUpdates());
/* 1166 */     this.level.environmentAttributes().invalidateTickCache();
/*      */   }
/*      */   
/*      */   public void handleSetSpawn(ClientboundSetDefaultSpawnPositionPacket packet) {
/* 1171 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1172 */     this.minecraft.level.setRespawnData(packet.respawnData());
/*      */   }
/*      */   
/*      */   public void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket packet) {
/* 1177 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1178 */     Entity vehicle = this.level.getEntity(packet.getVehicle());
/* 1179 */     if (vehicle == null) {
/* 1180 */       LOGGER.warn("Received passengers for unknown entity");
/*      */       return;
/*      */     } 
/* 1184 */     boolean wasPlayerMounted = vehicle.hasIndirectPassenger((Entity)this.minecraft.player);
/* 1185 */     vehicle.ejectPassengers();
/* 1187 */     for (int id : packet.getPassengers()) {
/* 1188 */       Entity passenger = this.level.getEntity(id);
/* 1189 */       if (passenger != null) {
/* 1190 */         passenger.startRiding(vehicle, true, false);
/* 1191 */         if (passenger == this.minecraft.player) {
/* 1192 */           this.removedPlayerVehicleId = OptionalInt.empty();
/* 1193 */           if (!wasPlayerMounted) {
/* 1194 */             if (vehicle instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 1195 */               this.minecraft.player.yRotO = vehicle.getYRot();
/* 1196 */               this.minecraft.player.setYRot(vehicle.getYRot());
/* 1197 */               this.minecraft.player.setYHeadRot(vehicle.getYRot());
/*      */             } 
/* 1199 */             MutableComponent mutableComponent = Component.translatable("mount.onboard", new Object[] { this.minecraft.options.keyShift.getTranslatedKeyMessage() });
/* 1200 */             this.minecraft.gui.hud.setOverlayMessage((Component)mutableComponent, false);
/* 1201 */             this.minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEntityLinkPacket(ClientboundSetEntityLinkPacket packet) {
/* 1210 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1211 */     Entity sourceEntity = this.level.getEntity(packet.getSourceId());
/* 1212 */     if (sourceEntity instanceof Leashable) {
/* 1212 */       Leashable leashable = (Leashable)sourceEntity;
/* 1213 */       leashable.setDelayedLeashHolderId(packet.getDestId());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static ItemStack findTotem(Player player) {
/* 1218 */     for (InteractionHand hand : InteractionHand.values()) {
/* 1219 */       ItemStack itemStack = player.getItemInHand(hand);
/* 1220 */       if (itemStack.has(DataComponents.DEATH_PROTECTION))
/* 1221 */         return itemStack; 
/*      */     } 
/* 1224 */     return new ItemStack((ItemLike)Items.TOTEM_OF_UNDYING);
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(ClientboundEntityEventPacket packet) {
/* 1229 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1230 */     Entity entity = packet.getEntity(this.level);
/* 1231 */     if (entity != null) {
/*      */       int tickLength;
/* 1233 */       switch (packet.getEventId()) {
/*      */         case 63:
/* 1235 */           this.minecraft.getSoundManager().play((SoundInstance)new SnifferSoundInstance((Sniffer)entity));
/*      */           return;
/*      */         case 21:
/* 1237 */           this.minecraft.getSoundManager().play((SoundInstance)new GuardianAttackSoundInstance((Guardian)entity));
/*      */           return;
/*      */         case 35:
/* 1239 */           tickLength = 40;
/* 1240 */           this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.TOTEM_OF_UNDYING, 30);
/* 1241 */           this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
/* 1242 */           if (entity == this.minecraft.player)
/* 1243 */             this.minecraft.gameRenderer.displayItemActivation(findTotem((Player)this.minecraft.player)); 
/*      */           return;
/*      */       } 
/* 1246 */       entity.handleEntityEvent(packet.getEventId());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleDamageEvent(ClientboundDamageEventPacket packet) {
/* 1253 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1254 */     Entity entity = this.level.getEntity(packet.entityId());
/* 1255 */     if (entity == null)
/*      */       return; 
/* 1259 */     entity.handleDamageEvent(packet.getSource(this.level));
/*      */   }
/*      */   
/*      */   public void handleSetHealth(ClientboundSetHealthPacket packet) {
/* 1264 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1265 */     this.minecraft.player.hurtTo(packet.getHealth());
/* 1266 */     this.minecraft.player.getFoodData().setFoodLevel(packet.getFood());
/* 1267 */     this.minecraft.player.getFoodData().setSaturation(packet.getSaturation());
/*      */   }
/*      */   
/*      */   public void handleSetExperience(ClientboundSetExperiencePacket packet) {
/* 1272 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1273 */     this.minecraft.player.setExperienceValues(packet.getExperienceProgress(), packet.getTotalExperience(), packet.getExperienceLevel());
/*      */   }
/*      */   
/*      */   public void handleRespawn(ClientboundRespawnPacket packet) {
/*      */     LocalPlayer newPlayer;
/* 1278 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1279 */     CommonPlayerSpawnInfo spawnInfo = packet.commonPlayerSpawnInfo();
/* 1280 */     ResourceKey<Level> dimensionKey = spawnInfo.dimension();
/* 1281 */     Holder<DimensionType> dimensionType = spawnInfo.dimensionType();
/* 1282 */     LocalPlayer oldPlayer = this.minecraft.player;
/* 1283 */     ResourceKey<Level> oldDimensionKey = oldPlayer.level().dimension();
/* 1284 */     boolean dimensionChanged = (dimensionKey != oldDimensionKey);
/* 1285 */     LevelLoadingScreen.Reason levelLoadingReason = determineLevelLoadingReason(oldPlayer.isDeadOrDying(), dimensionKey, oldDimensionKey);
/* 1286 */     if (dimensionChanged) {
/* 1287 */       Map<MapId, MapItemSavedData> mapData = this.level.getAllMapData();
/* 1288 */       boolean isDebug = spawnInfo.isDebug();
/* 1289 */       boolean isFlat = spawnInfo.isFlat();
/* 1290 */       int seaLevel = spawnInfo.seaLevel();
/* 1291 */       ClientLevel.ClientLevelData levelData = new ClientLevel.ClientLevelData(this.levelData.getDifficulty(), this.levelData.isHardcore(), isFlat);
/* 1292 */       this.levelData = levelData;
/* 1293 */       this.level = new ClientLevel(this, levelData, dimensionKey, dimensionType, this.serverChunkRadius, this.serverSimulationDistance, this.minecraft.levelExtractor, isDebug, spawnInfo.seed(), seaLevel);
/* 1294 */       this.level.addMapData(mapData);
/* 1295 */       this.minecraft.setLevel(this.level);
/* 1296 */       this.debugSubscriber.dropLevel();
/*      */     } 
/* 1299 */     this.minecraft.setCameraEntity(null);
/* 1300 */     if (oldPlayer.hasContainerOpen())
/* 1301 */       oldPlayer.closeContainer(); 
/* 1305 */     if (packet.shouldKeep((byte)2)) {
/* 1306 */       newPlayer = this.minecraft.gameMode.createPlayer(this.level, oldPlayer.getStats(), oldPlayer.getRecipeBook(), oldPlayer.getLastSentInput(), oldPlayer.isSprinting());
/*      */     } else {
/* 1308 */       newPlayer = this.minecraft.gameMode.createPlayer(this.level, oldPlayer.getStats(), oldPlayer.getRecipeBook());
/*      */     } 
/* 1311 */     setClientLoaded(false);
/* 1312 */     startWaitingForNewLevel(newPlayer, this.level, levelLoadingReason);
/* 1314 */     newPlayer.setId(oldPlayer.getId());
/* 1315 */     this.minecraft.player = newPlayer;
/* 1316 */     if (dimensionChanged)
/* 1317 */       this.minecraft.getMusicManager().stopPlaying(); 
/* 1319 */     this.minecraft.setCameraEntity((Entity)newPlayer);
/* 1321 */     if (packet.shouldKeep((byte)2)) {
/* 1322 */       List<SynchedEntityData.DataValue<?>> data = oldPlayer.getEntityData().getNonDefaultValues();
/* 1323 */       if (data != null)
/* 1324 */         newPlayer.getEntityData().assignValues(data); 
/* 1326 */       newPlayer.setDeltaMovement(oldPlayer.getDeltaMovement());
/* 1327 */       newPlayer.setYRot(oldPlayer.getYRot());
/* 1328 */       newPlayer.setXRot(oldPlayer.getXRot());
/*      */     } else {
/* 1330 */       newPlayer.resetPos();
/* 1331 */       newPlayer.setYRot(-180.0F);
/*      */     } 
/* 1334 */     if (packet.shouldKeep((byte)1)) {
/* 1335 */       newPlayer.getAttributes().assignAllValues(oldPlayer.getAttributes());
/*      */     } else {
/* 1337 */       newPlayer.getAttributes().assignBaseValues(oldPlayer.getAttributes());
/*      */     } 
/* 1340 */     this.level.addEntity((Entity)newPlayer);
/* 1341 */     newPlayer.input = (ClientInput)new KeyboardInput(this.minecraft.options);
/* 1342 */     this.minecraft.gameMode.adjustPlayer((Player)newPlayer);
/* 1343 */     newPlayer.setReducedDebugInfo(oldPlayer.isReducedDebugInfo());
/* 1344 */     newPlayer.setShowDeathScreen(oldPlayer.shouldShowDeathScreen());
/* 1345 */     newPlayer.setLastDeathLocation(spawnInfo.lastDeathLocation());
/* 1346 */     newPlayer.setPortalCooldown(spawnInfo.portalCooldown());
/* 1347 */     newPlayer.portalEffectIntensity = oldPlayer.portalEffectIntensity;
/* 1348 */     newPlayer.oPortalEffectIntensity = oldPlayer.oPortalEffectIntensity;
/* 1349 */     if (this.minecraft.gui.screen() instanceof DeathScreen || this.minecraft.gui.screen() instanceof DeathScreen.TitleConfirmScreen)
/* 1350 */       this.minecraft.gui.setScreen(null); 
/* 1352 */     this.minecraft.gameMode.setLocalMode(spawnInfo.gameType(), spawnInfo.previousGameType());
/*      */   }
/*      */   
/*      */   private LevelLoadingScreen.Reason determineLevelLoadingReason(boolean playerDied, ResourceKey<Level> dimensionKey, ResourceKey<Level> oldDimensionKey) {
/* 1356 */     LevelLoadingScreen.Reason levelLoadingReason = LevelLoadingScreen.Reason.OTHER;
/* 1357 */     if (!playerDied)
/* 1358 */       if (dimensionKey == Level.NETHER || oldDimensionKey == Level.NETHER) {
/* 1359 */         levelLoadingReason = LevelLoadingScreen.Reason.NETHER_PORTAL;
/* 1360 */       } else if (dimensionKey == Level.END || oldDimensionKey == Level.END) {
/* 1361 */         levelLoadingReason = LevelLoadingScreen.Reason.END_PORTAL;
/*      */       }  
/* 1364 */     return levelLoadingReason;
/*      */   }
/*      */   
/*      */   public void handleExplosion(ClientboundExplodePacket packet) {
/* 1369 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1370 */     Vec3 center = packet.center();
/* 1371 */     this.minecraft.level.playLocalSound(center.x(), center.y(), center.z(), (SoundEvent)packet.explosionSound().value(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.minecraft.level.getRandom().nextFloat() - this.minecraft.level.getRandom().nextFloat()) * 0.2F) * 0.7F, false);
/* 1372 */     this.minecraft.level.addParticle(packet.explosionParticle(), center.x(), center.y(), center.z(), 1.0D, 0.0D, 0.0D);
/* 1373 */     this.minecraft.level.trackExplosionEffects(center, packet.radius(), packet.blockCount(), packet.blockParticles());
/* 1374 */     Objects.requireNonNull(this.minecraft.player);
/* 1374 */     packet.playerKnockback().ifPresent(this.minecraft.player::addDeltaMovement);
/*      */   }
/*      */   
/*      */   public void handleMountScreenOpen(ClientboundMountScreenOpenPacket packet) {
/* 1379 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1380 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 1381 */     LocalPlayer player = this.minecraft.player;
/* 1382 */     int inventoryColumns = packet.getInventoryColumns();
/* 1383 */     SimpleContainer container = new SimpleContainer(AbstractMountInventoryMenu.getInventorySize(inventoryColumns));
/* 1384 */     if (entity instanceof AbstractHorse) {
/* 1384 */       AbstractHorse horse = (AbstractHorse)entity;
/* 1385 */       HorseInventoryMenu menu = new HorseInventoryMenu(packet.getContainerId(), player.getInventory(), (Container)container, horse, inventoryColumns);
/* 1386 */       player.containerMenu = (AbstractContainerMenu)menu;
/* 1387 */       this.minecraft.gui.setScreen((Screen)new HorseInventoryScreen(menu, player.getInventory(), horse, inventoryColumns));
/* 1388 */     } else if (entity instanceof AbstractNautilus) {
/* 1388 */       AbstractNautilus nautilus = (AbstractNautilus)entity;
/* 1389 */       NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.getContainerId(), player.getInventory(), (Container)container, nautilus, inventoryColumns);
/* 1390 */       player.containerMenu = (AbstractContainerMenu)menu;
/* 1391 */       this.minecraft.gui.setScreen((Screen)new NautilusInventoryScreen(menu, player.getInventory(), nautilus, inventoryColumns));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleOpenScreen(ClientboundOpenScreenPacket packet) {
/* 1397 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1398 */     MenuScreens.create(packet.getType(), this.minecraft, packet.getContainerId(), packet.getTitle());
/*      */   }
/*      */   
/*      */   public void handleContainerSetSlot(ClientboundContainerSetSlotPacket packet) {
/*      */     boolean creative;
/* 1403 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1404 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1405 */     ItemStack itemStack = packet.getItem();
/* 1406 */     int slot = packet.getSlot();
/* 1408 */     this.minecraft.getTutorial().onGetItem(itemStack);
/* 1412 */     Screen screen = this.minecraft.gui.screen();
/* 1412 */     if (screen instanceof CreativeModeInventoryScreen) {
/* 1412 */       CreativeModeInventoryScreen creativeModeInventoryScreen = (CreativeModeInventoryScreen)screen;
/* 1413 */       creative = !creativeModeInventoryScreen.isInventoryOpen();
/*      */     } else {
/* 1415 */       creative = false;
/*      */     } 
/* 1418 */     if (packet.getContainerId() == 0) {
/* 1419 */       if (InventoryMenu.isHotbarSlot(slot) && 
/* 1420 */         !itemStack.isEmpty()) {
/* 1421 */         ItemStack lastItemStack = ((Player)localPlayer).inventoryMenu.getSlot(slot).getItem();
/* 1422 */         if (lastItemStack.isEmpty() || lastItemStack.getCount() < itemStack.getCount())
/* 1423 */           itemStack.setPopTime(5); 
/*      */       } 
/* 1427 */       ((Player)localPlayer).inventoryMenu.setItem(slot, packet.getStateId(), itemStack);
/* 1428 */     } else if (packet.getContainerId() == ((Player)localPlayer).containerMenu.containerId && (packet.getContainerId() != 0 || !creative)) {
/* 1429 */       ((Player)localPlayer).containerMenu.setItem(slot, packet.getStateId(), itemStack);
/*      */     } 
/* 1432 */     if (this.minecraft.gui.screen() instanceof CreativeModeInventoryScreen) {
/* 1436 */       ((Player)localPlayer).inventoryMenu.setRemoteSlot(slot, itemStack);
/* 1437 */       ((Player)localPlayer).inventoryMenu.broadcastChanges();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetCursorItem(ClientboundSetCursorItemPacket packet) {
/* 1443 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1445 */     this.minecraft.getTutorial().onGetItem(packet.contents());
/* 1448 */     if (!(this.minecraft.gui.screen() instanceof CreativeModeInventoryScreen))
/* 1449 */       this.minecraft.player.containerMenu.setCarried(packet.contents()); 
/*      */   }
/*      */   
/*      */   public void handleSetPlayerInventory(ClientboundSetPlayerInventoryPacket packet) {
/* 1455 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1457 */     this.minecraft.getTutorial().onGetItem(packet.contents());
/* 1459 */     this.minecraft.player.getInventory().setItem(packet.slot(), packet.contents());
/*      */   }
/*      */   
/*      */   public void handleContainerContent(ClientboundContainerSetContentPacket packet) {
/* 1464 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1465 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1466 */     if (packet.containerId() == 0) {
/* 1467 */       ((Player)localPlayer).inventoryMenu.initializeContents(packet.stateId(), packet.items(), packet.carriedItem());
/* 1468 */     } else if (packet.containerId() == ((Player)localPlayer).containerMenu.containerId) {
/* 1469 */       ((Player)localPlayer).containerMenu.initializeContents(packet.stateId(), packet.items(), packet.carriedItem());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleOpenSignEditor(ClientboundOpenSignEditorPacket packet) {
/* 1475 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1476 */     BlockPos pos = packet.getPos();
/* 1478 */     BlockEntity blockEntity = this.level.getBlockEntity(pos);
/* 1478 */     if (blockEntity instanceof SignBlockEntity) {
/* 1478 */       SignBlockEntity sign = (SignBlockEntity)blockEntity;
/* 1479 */       this.minecraft.player.openTextEdit(sign, packet.isFrontText());
/*      */     } else {
/* 1481 */       LOGGER.warn("Ignoring openTextEdit on an invalid entity: {} at pos {}", this.level.getBlockEntity(pos), pos);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleBlockEntityData(ClientboundBlockEntityDataPacket packet) {
/* 1487 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1488 */     BlockPos pos = packet.getPos();
/* 1489 */     this.minecraft.level.getBlockEntity(pos, packet.getType()).ifPresent(blockEntity -> {
/*      */           ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER);
/*      */           try {
/*      */             blockEntity.loadWithComponents(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.registryAccess, packet.getTag()));
/*      */             reporter.close();
/* 1490 */           } catch (Throwable t$) {
/*      */             try {
/*      */               reporter.close();
/* 1490 */             } catch (Throwable x2) {
/*      */               t$.addSuppressed(x2);
/*      */             } 
/*      */             throw t$;
/*      */           } 
/*      */           if (blockEntity instanceof net.minecraft.world.level.block.entity.CommandBlockEntity && this.minecraft.gui.screen() instanceof CommandBlockEditScreen)
/*      */             ((CommandBlockEditScreen)this.minecraft.gui.screen()).updateGui(); 
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleContainerSetData(ClientboundContainerSetDataPacket packet) {
/* 1502 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1503 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1504 */     if (((Player)localPlayer).containerMenu.containerId == packet.getContainerId())
/* 1505 */       ((Player)localPlayer).containerMenu.setData(packet.getId(), packet.getValue()); 
/*      */   }
/*      */   
/*      */   public void handleSetEquipment(ClientboundSetEquipmentPacket packet) {
/* 1511 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1512 */     Entity entity = this.level.getEntity(packet.getEntity());
/* 1513 */     if (entity instanceof LivingEntity) {
/* 1513 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 1514 */       packet.getSlots().forEach(e -> livingEntity.setItemSlot((EquipmentSlot)e.getFirst(), (ItemStack)e.getSecond()));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleContainerClose(ClientboundContainerClosePacket packet) {
/* 1520 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1521 */     this.minecraft.player.clientSideCloseContainer();
/*      */   }
/*      */   
/*      */   public void handleBlockEvent(ClientboundBlockEventPacket packet) {
/* 1526 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1527 */     this.minecraft.level.blockEvent(packet.getPos(), packet.getBlock(), packet.getB0(), packet.getB1());
/*      */   }
/*      */   
/*      */   public void handleBlockDestruction(ClientboundBlockDestructionPacket packet) {
/* 1532 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1533 */     this.minecraft.level.destroyBlockProgress(packet.getId(), packet.getPos(), packet.getProgress());
/*      */   }
/*      */   
/*      */   public void handleGameEvent(ClientboundGameEventPacket packet) {
/* 1538 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1539 */     LocalPlayer player = Objects.<LocalPlayer>requireNonNull(this.minecraft.player);
/* 1540 */     ClientboundGameEventPacket.Type event = packet.getEvent();
/* 1541 */     float paramFloat = packet.getParam();
/* 1542 */     int param = Mth.floor(paramFloat + 0.5F);
/* 1543 */     if (event == ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE) {
/* 1545 */       player.sendSystemMessage((Component)Component.translatable("block.minecraft.spawn.not_valid"));
/* 1546 */     } else if (event == ClientboundGameEventPacket.START_RAINING) {
/* 1547 */       this.level.setRainLevel(0.0F);
/* 1548 */     } else if (event == ClientboundGameEventPacket.STOP_RAINING) {
/* 1549 */       this.level.setRainLevel(1.0F);
/* 1550 */     } else if (event == ClientboundGameEventPacket.CHANGE_GAME_MODE) {
/* 1551 */       this.minecraft.gameMode.setLocalMode(GameType.byId(param));
/* 1552 */     } else if (event == ClientboundGameEventPacket.WIN_GAME) {
/* 1553 */       this.minecraft.gui.setScreen((Screen)new WinScreen(true, () -> {
/*      */               player.connection.send((Packet<?>)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
/*      */               this.minecraft.gui.setScreen(null);
/*      */             }));
/* 1557 */     } else if (event == ClientboundGameEventPacket.DEMO_EVENT) {
/*      */       MutableComponent mutableComponent;
/* 1558 */       Options options = this.minecraft.options;
/* 1559 */       Component message = null;
/* 1560 */       if (paramFloat == 0.0F) {
/* 1561 */         openDemoIntroScreen(options);
/* 1562 */       } else if (paramFloat == 101.0F) {
/* 1563 */         mutableComponent = Component.translatable("demo.help.movement", new Object[] { options.keyUp.getTranslatedKeyMessage(), options.keyLeft.getTranslatedKeyMessage(), options.keyDown.getTranslatedKeyMessage(), options.keyRight.getTranslatedKeyMessage() });
/* 1564 */       } else if (paramFloat == 102.0F) {
/* 1565 */         mutableComponent = Component.translatable("demo.help.jump", new Object[] { options.keyJump.getTranslatedKeyMessage() });
/* 1566 */       } else if (paramFloat == 103.0F) {
/* 1567 */         mutableComponent = Component.translatable("demo.help.inventory", new Object[] { options.keyInventory.getTranslatedKeyMessage() });
/* 1568 */       } else if (paramFloat == 104.0F) {
/* 1569 */         mutableComponent = Component.translatable("demo.day.6", new Object[] { options.keyScreenshot.getTranslatedKeyMessage() });
/*      */       } 
/* 1572 */       if (mutableComponent != null) {
/* 1573 */         this.minecraft.gui.hud.getChat().addClientSystemMessage((Component)mutableComponent);
/* 1574 */         this.minecraft.getNarrator().saySystemQueued((Component)mutableComponent);
/*      */       } 
/* 1576 */     } else if (event == ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND) {
/* 1577 */       this.level.playSound((Entity)player, player.getX(), player.getEyeY(), player.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.18F, 0.45F);
/* 1578 */     } else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
/* 1579 */       this.level.setRainLevel(paramFloat);
/* 1580 */     } else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
/* 1581 */       this.level.setThunderLevel(paramFloat);
/* 1582 */     } else if (event == ClientboundGameEventPacket.PUFFER_FISH_STING) {
/* 1583 */       this.level.playSound((Entity)player, player.getX(), player.getY(), player.getZ(), SoundEvents.PUFFER_FISH_STING, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 1584 */     } else if (event == ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT) {
/* 1585 */       this.level.addParticle((ParticleOptions)ParticleTypes.ELDER_GUARDIAN, player.getX(), player.getY(), player.getZ(), 0.0D, 0.0D, 0.0D);
/* 1587 */       if (param == 1)
/* 1588 */         this.level.playSound((Entity)player, player.getX(), player.getY(), player.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0F, 1.0F); 
/* 1590 */     } else if (event == ClientboundGameEventPacket.IMMEDIATE_RESPAWN) {
/* 1591 */       player.setShowDeathScreen((paramFloat == 0.0F));
/* 1592 */     } else if (event == ClientboundGameEventPacket.LIMITED_CRAFTING) {
/* 1593 */       player.setDoLimitedCrafting((paramFloat == 1.0F));
/* 1594 */     } else if (event == ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START && 
/* 1595 */       this.levelLoadTracker != null) {
/* 1596 */       this.levelLoadTracker.loadingPacketsReceived();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void openDemoIntroScreen(Options options) {
/* 1602 */     this.minecraft.gui.setScreen((Screen)(new PopupScreen.Builder(null, (Component)Component.translatable("demo.help.title")))
/* 1603 */         .addMessage(CommonComponents.joinLines(new Component[] { (Component)Component.translatable("demo.help.movementShort", new Object[] { options.keyUp.getTranslatedKeyMessage(), options.keyLeft.getTranslatedKeyMessage(), options.keyDown.getTranslatedKeyMessage(), options.keyRight.getTranslatedKeyMessage() }), (Component)Component.translatable("demo.help.movementMouse"), 
/* 1606 */               (Component)Component.translatable("demo.help.jump", new Object[] { options.keyJump.getTranslatedKeyMessage() }), (Component)Component.translatable("demo.help.inventory", new Object[] { options.keyInventory.getTranslatedKeyMessage() }) })).addMessage((Component)Component.translatable("demo.help.fullWrapped"))
/* 1610 */         .addButton((Component)Component.translatable("demo.help.buy"), popupScreen -> ConfirmLinkScreen.confirmLinkNow(null, CommonLinks.BUY_MINECRAFT_JAVA))
/* 1611 */         .addButton(
/* 1612 */           (Component)Component.translatable("demo.help.later"), popupScreen -> {
/*      */             this.minecraft.mouseHandler.grabMouse();
/*      */             popupScreen.onClose();
/* 1616 */           }).build());
/*      */   }
/*      */   
/*      */   private void startWaitingForNewLevel(LocalPlayer player, ClientLevel level, LevelLoadingScreen.Reason reason) {
/* 1621 */     if (this.levelLoadTracker == null)
/* 1623 */       this.levelLoadTracker = new LevelLoadTracker(); 
/* 1625 */     this.levelLoadTracker.startClientLoad(player, level);
/* 1626 */     Screen screen = this.minecraft.gui.screen();
/* 1626 */     if (screen instanceof LevelLoadingScreen) {
/* 1626 */       LevelLoadingScreen loadingScreen = (LevelLoadingScreen)screen;
/* 1627 */       loadingScreen.update(this.levelLoadTracker, reason);
/*      */     } else {
/* 1629 */       this.minecraft.gui.hud.getChat().preserveCurrentChatScreen();
/* 1630 */       this.minecraft.setScreenAndShow((Screen)new LevelLoadingScreen(this.levelLoadTracker, reason));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleMapItemData(ClientboundMapItemDataPacket packet) {
/* 1636 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1637 */     MapId id = packet.mapId();
/* 1638 */     MapItemSavedData data = this.minecraft.level.getMapData(id);
/* 1640 */     if (data == null) {
/* 1641 */       data = MapItemSavedData.createForClient(packet.scale(), packet.locked(), this.minecraft.level.dimension());
/* 1642 */       this.minecraft.level.overrideMapData(id, data);
/*      */     } 
/* 1645 */     packet.applyToMap(data);
/* 1646 */     this.minecraft.getMapTextureManager().update(id, data);
/*      */   }
/*      */   
/*      */   public void handleLevelEvent(ClientboundLevelEventPacket packet) {
/* 1651 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1652 */     if (packet.isGlobalEvent()) {
/* 1653 */       this.minecraft.level.globalLevelEvent(packet.getType(), packet.getPos(), packet.getData());
/*      */     } else {
/* 1655 */       this.minecraft.level.levelEvent(packet.getType(), packet.getPos(), packet.getData());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleUpdateAdvancementsPacket(ClientboundUpdateAdvancementsPacket packet) {
/* 1661 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1662 */     this.advancements.update(packet);
/*      */   }
/*      */   
/*      */   public void handleSelectAdvancementsTab(ClientboundSelectAdvancementsTabPacket packet) {
/* 1667 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1668 */     Identifier id = packet.getTab();
/* 1669 */     if (id == null) {
/* 1670 */       this.advancements.setSelectedTab(null, false);
/*      */     } else {
/* 1672 */       AdvancementHolder advancement = this.advancements.get(id);
/* 1673 */       this.advancements.setSelectedTab(advancement, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleCommands(ClientboundCommandsPacket packet) {
/* 1679 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1680 */     this.commands = new CommandDispatcher(packet.getRoot(CommandBuildContext.simple((HolderLookup.Provider)this.registryAccess, this.enabledFeatures), COMMAND_NODE_BUILDER));
/*      */   }
/*      */   
/*      */   public void handleStopSoundEvent(ClientboundStopSoundPacket packet) {
/* 1685 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1686 */     this.minecraft.getSoundManager().stop(packet.getName(), packet.getSource());
/*      */   }
/*      */   
/*      */   public void handleCommandSuggestions(ClientboundCommandSuggestionsPacket packet) {
/* 1691 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1692 */     this.suggestionsProvider.completeCustomSuggestions(packet.id(), packet.toSuggestions());
/*      */   }
/*      */   
/*      */   public void handleUpdateRecipes(ClientboundUpdateRecipesPacket packet) {
/* 1697 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1698 */     this.recipes = new ClientRecipeContainer(packet.itemSets(), packet.stonecutterRecipes());
/*      */   }
/*      */   
/*      */   public void handleLookAt(ClientboundPlayerLookAtPacket packet) {
/* 1703 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1704 */     Vec3 pos = packet.getPosition(this.level);
/* 1705 */     if (pos != null)
/* 1706 */       this.minecraft.player.lookAt(packet.getFromAnchor(), pos); 
/*      */   }
/*      */   
/*      */   public void handleTagQueryPacket(ClientboundTagQueryPacket packet) {
/* 1712 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1714 */     if (!this.debugQueryHandler.handleResponse(packet.getTransactionId(), packet.getTag()))
/* 1715 */       LOGGER.debug("Got unhandled response to tag query {}", Integer.valueOf(packet.getTransactionId())); 
/*      */   }
/*      */   
/*      */   public void handleAwardStats(ClientboundAwardStatsPacket packet) {
/* 1721 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1723 */     for (ObjectIterator<Object2IntMap.Entry<Stat<?>>> objectIterator = packet.stats().object2IntEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 1723 */       Object2IntMap.Entry<Stat<?>> entry = objectIterator.next();
/* 1724 */       Stat<?> stat = (Stat)entry.getKey();
/* 1725 */       int amount = entry.getIntValue();
/* 1727 */       this.minecraft.player.getStats().setValue((Player)this.minecraft.player, stat, amount);
/*      */     } 
/* 1730 */     Screen screen = this.minecraft.gui.screen();
/* 1730 */     if (screen instanceof StatsScreen) {
/* 1730 */       StatsScreen statsScreen = (StatsScreen)screen;
/* 1731 */       statsScreen.onStatsUpdated();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleRecipeBookAdd(ClientboundRecipeBookAddPacket packet) {
/* 1737 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1739 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/* 1741 */     if (packet.replace())
/* 1742 */       recipeBook.clear(); 
/* 1745 */     for (ClientboundRecipeBookAddPacket.Entry entry : packet.entries()) {
/* 1746 */       recipeBook.add(entry.contents());
/* 1747 */       if (entry.highlight())
/* 1748 */         recipeBook.addHighlight(entry.contents().id()); 
/* 1751 */       if (entry.notification())
/* 1752 */         RecipeToast.addOrUpdate(this.minecraft.gui.toastManager(), entry.contents().display()); 
/*      */     } 
/* 1755 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */   
/*      */   public void handleRecipeBookRemove(ClientboundRecipeBookRemovePacket packet) {
/* 1760 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1761 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/* 1762 */     for (RecipeDisplayId id : packet.recipes())
/* 1763 */       recipeBook.remove(id); 
/* 1766 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */   
/*      */   public void handleRecipeBookSettings(ClientboundRecipeBookSettingsPacket packet) {
/* 1771 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1772 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/* 1773 */     recipeBook.setBookSettings(packet.bookSettings());
/* 1774 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */   
/*      */   private void refreshRecipeBook(ClientRecipeBook recipeBook) {
/* 1778 */     recipeBook.rebuildCollections();
/* 1779 */     this.searchTrees.updateRecipes(recipeBook, this.level);
/* 1781 */     Screen screen = this.minecraft.gui.screen();
/* 1781 */     if (screen instanceof RecipeUpdateListener) {
/* 1781 */       RecipeUpdateListener updateListener = (RecipeUpdateListener)screen;
/* 1782 */       updateListener.recipesUpdated();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleUpdateMobEffect(ClientboundUpdateMobEffectPacket packet) {
/*      */     LivingEntity livingEntity;
/* 1788 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1789 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 1790 */     if (entity instanceof LivingEntity) {
/* 1790 */       livingEntity = (LivingEntity)entity;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 1794 */     Holder<MobEffect> effect = packet.getEffect();
/* 1795 */     MobEffectInstance mobEffectInstance = new MobEffectInstance(effect, packet.getEffectDurationTicks(), packet.getEffectAmplifier(), packet.isEffectAmbient(), packet.isEffectVisible(), packet.effectShowsIcon(), null);
/* 1796 */     if (!packet.shouldBlend())
/* 1797 */       mobEffectInstance.skipBlending(); 
/* 1799 */     livingEntity.forceAddEffect(mobEffectInstance, null);
/*      */   }
/*      */   
/*      */   private <T> Registry.PendingTags<T> updateTags(ResourceKey<? extends Registry<? extends T>> registryKey, TagNetworkSerialization.NetworkPayload payload) {
/* 1803 */     Registry<T> registry = this.registryAccess.lookupOrThrow(registryKey);
/* 1804 */     return registry.prepareTagReload(payload.resolve(registry));
/*      */   }
/*      */   
/*      */   public void handleUpdateTags(ClientboundUpdateTagsPacket packet) {
/* 1809 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1810 */     List<Registry.PendingTags<?>> pendingTags = new ArrayList<>(packet.getTags().size());
/* 1812 */     packet.getTags().forEach((key, networkPayload) -> pendingTags.add(updateTags(key, networkPayload)));
/* 1815 */     if (!this.connection.isMemoryConnection())
/* 1816 */       pendingTags.forEach(Registry.PendingTags::apply); 
/* 1819 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registryAccess, this.enabledFeatures);
/* 1820 */     List<ItemStack> searchItems = List.copyOf(CreativeModeTabs.searchTab().getDisplayItems());
/* 1821 */     this.searchTrees.updateCreativeTags(searchItems);
/*      */   }
/*      */   
/*      */   public void handlePlayerCombatEnd(ClientboundPlayerCombatEndPacket packet) {}
/*      */   
/*      */   public void handlePlayerCombatEnter(ClientboundPlayerCombatEnterPacket packet) {}
/*      */   
/*      */   public void handlePlayerCombatKill(ClientboundPlayerCombatKillPacket packet) {
/* 1834 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1836 */     Entity player = this.level.getEntity(packet.playerId());
/* 1837 */     if (player == this.minecraft.player)
/* 1838 */       if (this.minecraft.player.shouldShowDeathScreen()) {
/* 1839 */         this.minecraft.gui.setScreen((Screen)new DeathScreen(packet.message(), this.level.getLevelData().isHardcore(), this.minecraft.player));
/*      */       } else {
/* 1841 */         this.minecraft.player.respawn();
/*      */       }  
/*      */   }
/*      */   
/*      */   public void handleChangeDifficulty(ClientboundChangeDifficultyPacket packet) {
/* 1848 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1849 */     this.levelData.setDifficulty(packet.difficulty());
/* 1850 */     this.levelData.setDifficultyLocked(packet.locked());
/* 1851 */     Screen screen = this.minecraft.gui.screen();
/* 1851 */     if (screen instanceof HasDifficultyReaction) {
/* 1851 */       HasDifficultyReaction hasDifficultyReaction = (HasDifficultyReaction)screen;
/* 1852 */       hasDifficultyReaction.onDifficultyChanged();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetCamera(ClientboundSetCameraPacket packet) {
/* 1858 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1859 */     Entity entity = packet.getEntity(this.level);
/* 1860 */     if (entity != null)
/* 1861 */       this.minecraft.setCameraEntity(entity); 
/*      */   }
/*      */   
/*      */   public void handleInitializeBorder(ClientboundInitializeBorderPacket packet) {
/* 1867 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1868 */     WorldBorder border = this.level.getWorldBorder();
/* 1870 */     border.setCenter(packet.getNewCenterX(), packet.getNewCenterZ());
/* 1872 */     long lerpTime = packet.getLerpTime();
/* 1873 */     if (lerpTime > 0L) {
/* 1874 */       border.lerpSizeBetween(packet.getOldSize(), packet.getNewSize(), lerpTime, this.level.getGameTime());
/*      */     } else {
/* 1876 */       border.setSize(packet.getNewSize());
/*      */     } 
/* 1879 */     border.setAbsoluteMaxSize(packet.getNewAbsoluteMaxSize());
/* 1880 */     border.setWarningBlocks(packet.getWarningBlocks());
/* 1881 */     border.setWarningTime(packet.getWarningTime());
/*      */   }
/*      */   
/*      */   public void handleSetBorderCenter(ClientboundSetBorderCenterPacket packet) {
/* 1886 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1887 */     this.level.getWorldBorder().setCenter(packet.getNewCenterX(), packet.getNewCenterZ());
/*      */   }
/*      */   
/*      */   public void handleSetBorderLerpSize(ClientboundSetBorderLerpSizePacket packet) {
/* 1892 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1893 */     this.level.getWorldBorder().lerpSizeBetween(packet.getOldSize(), packet.getNewSize(), packet.getLerpTime(), this.level.getGameTime());
/*      */   }
/*      */   
/*      */   public void handleSetBorderSize(ClientboundSetBorderSizePacket packet) {
/* 1898 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1899 */     this.level.getWorldBorder().setSize(packet.getSize());
/*      */   }
/*      */   
/*      */   public void handleSetBorderWarningDistance(ClientboundSetBorderWarningDistancePacket packet) {
/* 1904 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1905 */     this.level.getWorldBorder().setWarningBlocks(packet.getWarningBlocks());
/*      */   }
/*      */   
/*      */   public void handleSetBorderWarningDelay(ClientboundSetBorderWarningDelayPacket packet) {
/* 1910 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1911 */     this.level.getWorldBorder().setWarningTime(packet.getWarningDelay());
/*      */   }
/*      */   
/*      */   public void handleTitlesClear(ClientboundClearTitlesPacket packet) {
/* 1916 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1917 */     this.minecraft.gui.hud.clearTitles();
/* 1918 */     if (packet.shouldResetTimes())
/* 1919 */       this.minecraft.gui.hud.resetTitleTimes(); 
/*      */   }
/*      */   
/*      */   public void handleServerData(ClientboundServerDataPacket packet) {
/* 1925 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1926 */     if (this.serverData == null)
/*      */       return; 
/* 1930 */     this.serverData.motd = packet.motd();
/* 1931 */     Objects.requireNonNull(this.serverData);
/* 1931 */     packet.iconBytes().map(ServerData::validateIcon).ifPresent(this.serverData::setIconBytes);
/* 1933 */     ServerList.saveSingleServer(this.serverData);
/*      */   }
/*      */   
/*      */   public void handleCustomChatCompletions(ClientboundCustomChatCompletionsPacket packet) {
/* 1938 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1939 */     this.suggestionsProvider.modifyCustomCompletions(packet.action(), packet.entries());
/*      */   }
/*      */   
/*      */   public void setActionBarText(ClientboundSetActionBarTextPacket packet) {
/* 1944 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1945 */     this.minecraft.gui.hud.setOverlayMessage(packet.text(), false);
/*      */   }
/*      */   
/*      */   public void setTitleText(ClientboundSetTitleTextPacket packet) {
/* 1950 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1951 */     this.minecraft.gui.hud.setTitle(packet.text());
/*      */   }
/*      */   
/*      */   public void setSubtitleText(ClientboundSetSubtitleTextPacket packet) {
/* 1956 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1957 */     this.minecraft.gui.hud.setSubtitle(packet.text());
/*      */   }
/*      */   
/*      */   public void setTitlesAnimation(ClientboundSetTitlesAnimationPacket packet) {
/* 1962 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1963 */     this.minecraft.gui.hud.setTimes(packet.getFadeIn(), packet.getStay(), packet.getFadeOut());
/*      */   }
/*      */   
/*      */   public void handleTabListCustomisation(ClientboundTabListPacket packet) {
/* 1968 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1969 */     this.minecraft.gui.hud.getTabList().setHeader(packet.header().getString().isEmpty() ? null : packet.header());
/* 1970 */     this.minecraft.gui.hud.getTabList().setFooter(packet.footer().getString().isEmpty() ? null : packet.footer());
/*      */   }
/*      */   
/*      */   public void handleRemoveMobEffect(ClientboundRemoveMobEffectPacket packet) {
/* 1975 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1976 */     Entity entity = packet.getEntity(this.level);
/* 1976 */     if (entity instanceof LivingEntity) {
/* 1976 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 1977 */       livingEntity.removeEffectNoUpdate(packet.effect());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket packet) {
/* 1983 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1984 */     for (UUID profileId : packet.profileIds()) {
/* 1985 */       this.minecraft.getPlayerSocialManager().removePlayer(profileId);
/* 1986 */       PlayerInfo info = this.playerInfoMap.remove(profileId);
/* 1987 */       if (info != null)
/* 1988 */         this.listedPlayers.remove(info); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket packet) {
/* 1995 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1997 */     for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.newEntries()) {
/* 1998 */       PlayerInfo playerInfo = new PlayerInfo(Objects.<GameProfile>requireNonNull(entry.profile()), enforcesSecureChat());
/* 1999 */       if (this.playerInfoMap.putIfAbsent(entry.profileId(), playerInfo) == null)
/* 2000 */         this.minecraft.getPlayerSocialManager().addPlayer(playerInfo); 
/*      */     } 
/* 2004 */     for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
/* 2005 */       PlayerInfo info = this.playerInfoMap.get(entry.profileId());
/* 2006 */       if (info == null) {
/* 2007 */         LOGGER.warn("Ignoring player info update for unknown player {} ({})", entry.profileId(), packet.actions());
/*      */         continue;
/*      */       } 
/* 2011 */       for (ClientboundPlayerInfoUpdatePacket.Action action : packet.actions())
/* 2012 */         applyPlayerInfoUpdate(action, entry, info); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void applyPlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket.Action action, ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo info) {
/* 2018 */     switch (action) {
/*      */       case INITIALIZE_CHAT:
/* 2019 */         initializeChatSession(entry, info);
/*      */         break;
/*      */       case UPDATE_GAME_MODE:
/* 2021 */         if (info.getGameMode() != entry.gameMode() && this.minecraft.player != null && this.minecraft.player
/*      */           
/* 2023 */           .getUUID().equals(entry.profileId()))
/* 2025 */           this.minecraft.player.onGameModeChanged(entry.gameMode()); 
/* 2027 */         info.setGameMode(entry.gameMode());
/*      */         break;
/*      */       case UPDATE_LISTED:
/* 2030 */         if (entry.listed()) {
/* 2031 */           this.listedPlayers.add(info);
/*      */           break;
/*      */         } 
/* 2033 */         this.listedPlayers.remove(info);
/*      */         break;
/*      */       case UPDATE_LATENCY:
/* 2036 */         info.setLatency(entry.latency());
/*      */         break;
/*      */       case UPDATE_DISPLAY_NAME:
/* 2037 */         info.setTabListDisplayName(entry.displayName());
/*      */         break;
/*      */       case UPDATE_HAT:
/* 2038 */         info.setShowHat(entry.showHat());
/*      */         break;
/*      */       case UPDATE_LIST_ORDER:
/* 2039 */         info.setTabListOrder(entry.listOrder());
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initializeChatSession(ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo info) {
/* 2044 */     GameProfile profile = info.getProfile();
/* 2045 */     SignatureValidator signatureValidator = this.minecraft.services().profileKeySignatureValidator();
/* 2046 */     if (signatureValidator == null) {
/* 2047 */       LOGGER.warn("Ignoring chat session from {} due to missing Services public key", profile.name());
/* 2048 */       info.clearChatSession(enforcesSecureChat());
/*      */       return;
/*      */     } 
/* 2051 */     RemoteChatSession.Data chatSessionData = entry.chatSession();
/* 2052 */     if (chatSessionData != null) {
/*      */       try {
/* 2054 */         RemoteChatSession chatSession = chatSessionData.validate(profile, signatureValidator);
/* 2055 */         info.setChatSession(chatSession);
/* 2056 */       } catch (net.minecraft.world.entity.player.ProfilePublicKey.ValidationException e) {
/* 2057 */         LOGGER.error("Failed to validate profile key for player: '{}'", profile.name(), e);
/* 2058 */         info.clearChatSession(enforcesSecureChat());
/*      */       } 
/*      */     } else {
/* 2061 */       info.clearChatSession(enforcesSecureChat());
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean enforcesSecureChat() {
/* 2066 */     return (this.minecraft.services().canValidateProfileKeys() && this.serverEnforcesSecureChat);
/*      */   }
/*      */   
/*      */   public boolean onlineMode() {
/* 2070 */     return this.onlineMode;
/*      */   }
/*      */   
/*      */   public void handlePlayerAbilities(ClientboundPlayerAbilitiesPacket packet) {
/* 2075 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2076 */     LocalPlayer localPlayer = this.minecraft.player;
/* 2077 */     (localPlayer.getAbilities()).flying = packet.isFlying();
/* 2078 */     (localPlayer.getAbilities()).instabuild = packet.canInstabuild();
/* 2079 */     (localPlayer.getAbilities()).invulnerable = packet.isInvulnerable();
/* 2080 */     (localPlayer.getAbilities()).mayfly = packet.canFly();
/* 2081 */     localPlayer.getAbilities().setFlyingSpeed(packet.getFlyingSpeed());
/* 2082 */     localPlayer.getAbilities().setWalkingSpeed(packet.getWalkingSpeed());
/*      */   }
/*      */   
/*      */   public void handleGameRuleValues(ClientboundGameRuleValuesPacket packet) {
/* 2087 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2088 */     Screen screen = this.minecraft.gui.screen();
/* 2088 */     if (screen instanceof InWorldGameRulesScreen) {
/* 2088 */       InWorldGameRulesScreen inWorldGameRulesScreen = (InWorldGameRulesScreen)screen;
/* 2089 */       inWorldGameRulesScreen.onGameRuleValuesUpdated(packet.values());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSoundEvent(ClientboundSoundPacket packet) {
/* 2095 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2096 */     this.minecraft.level.playSeededSound((Entity)this.minecraft.player, packet.getX(), packet.getY(), packet.getZ(), packet.getSound(), packet.getSource(), packet.getVolume(), packet.getPitch(), packet.getSeed());
/*      */   }
/*      */   
/*      */   public void handleSoundEntityEvent(ClientboundSoundEntityPacket packet) {
/* 2101 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2102 */     Entity entity = this.level.getEntity(packet.getId());
/* 2103 */     if (entity == null)
/*      */       return; 
/* 2106 */     this.minecraft.level.playSeededSound((Entity)this.minecraft.player, entity, packet.getSound(), packet.getSource(), packet.getVolume(), packet.getPitch(), packet.getSeed());
/*      */   }
/*      */   
/*      */   public void handleBossUpdate(ClientboundBossEventPacket packet) {
/* 2111 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2112 */     this.minecraft.gui.hud.getBossOverlay().update(packet);
/*      */   }
/*      */   
/*      */   public void handleItemCooldown(ClientboundCooldownPacket packet) {
/* 2117 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2118 */     if (packet.duration() == 0) {
/* 2119 */       this.minecraft.player.getCooldowns().removeCooldown(packet.cooldownGroup());
/*      */     } else {
/* 2121 */       this.minecraft.player.getCooldowns().addCooldown(packet.cooldownGroup(), packet.duration());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleMoveVehicle(ClientboundMoveVehiclePacket packet) {
/* 2127 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2128 */     Entity vehicle = this.minecraft.player.getRootVehicle();
/* 2129 */     if (vehicle != this.minecraft.player && vehicle.isLocalInstanceAuthoritative()) {
/* 2130 */       Vec3 currentTarget, target = packet.position();
/* 2132 */       if (vehicle.isInterpolating()) {
/* 2133 */         currentTarget = vehicle.getInterpolation().position();
/*      */       } else {
/* 2135 */         currentTarget = vehicle.position();
/*      */       } 
/* 2137 */       if (target.distanceTo(currentTarget) > 9.999999747378752E-6D) {
/* 2138 */         if (vehicle.isInterpolating())
/* 2139 */           vehicle.getInterpolation().cancel(); 
/* 2141 */         vehicle.absSnapTo(target.x(), target.y(), target.z(), packet.yRot(), packet.xRot());
/*      */       } 
/* 2143 */       this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(vehicle));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleOpenBook(ClientboundOpenBookPacket packet) {
/* 2149 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2150 */     ItemStack held = this.minecraft.player.getItemInHand(packet.getHand());
/* 2151 */     BookViewScreen.BookAccess bookAccess = BookViewScreen.BookAccess.fromItem(held);
/* 2152 */     if (bookAccess != null)
/* 2153 */       this.minecraft.gui.setScreen((Screen)new BookViewScreen(bookAccess)); 
/*      */   }
/*      */   
/*      */   public void handleCustomPayload(CustomPacketPayload payload) {
/* 2162 */     handleUnknownCustomPayload(payload);
/*      */   }
/*      */   
/*      */   private void handleUnknownCustomPayload(CustomPacketPayload payload) {
/* 2168 */     LOGGER.warn("Unknown custom packet payload: {}", payload.type().id());
/*      */   }
/*      */   
/*      */   public void handleAddObjective(ClientboundSetObjectivePacket packet) {
/* 2173 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2175 */     String objectiveName = packet.getObjectiveName();
/* 2176 */     if (packet.getMethod() == 0) {
/* 2177 */       this.scoreboard.addObjective(objectiveName, ObjectiveCriteria.DUMMY, packet.getDisplayName(), packet.getRenderType(), false, packet.getNumberFormat().orElse(null));
/*      */     } else {
/* 2179 */       Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2180 */       if (objective != null)
/* 2181 */         if (packet.getMethod() == 1) {
/* 2182 */           this.scoreboard.removeObjective(objective);
/* 2183 */         } else if (packet.getMethod() == 2) {
/* 2184 */           objective.setRenderType(packet.getRenderType());
/* 2185 */           objective.setDisplayName(packet.getDisplayName());
/* 2186 */           objective.setNumberFormat(packet.getNumberFormat().orElse(null));
/*      */         }  
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetScore(ClientboundSetScorePacket packet) {
/* 2194 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2195 */     String objectiveName = packet.objectiveName();
/* 2197 */     ScoreHolder scoreHolder = ScoreHolder.forNameOnly(packet.owner());
/* 2198 */     Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2199 */     if (objective != null) {
/* 2200 */       ScoreAccess score = this.scoreboard.getOrCreatePlayerScore(scoreHolder, objective, true);
/* 2201 */       score.set(packet.score());
/* 2202 */       score.display(packet.display().orElse(null));
/* 2203 */       score.numberFormatOverride(packet.numberFormat().orElse(null));
/*      */     } else {
/* 2205 */       LOGGER.warn("Received packet for unknown scoreboard objective: {}", objectiveName);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleResetScore(ClientboundResetScorePacket packet) {
/* 2211 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2212 */     String objectiveName = packet.objectiveName();
/* 2214 */     ScoreHolder scoreHolder = ScoreHolder.forNameOnly(packet.owner());
/* 2215 */     if (objectiveName == null) {
/* 2216 */       this.scoreboard.resetAllPlayerScores(scoreHolder);
/*      */     } else {
/* 2218 */       Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2219 */       if (objective != null) {
/* 2220 */         this.scoreboard.resetSinglePlayerScore(scoreHolder, objective);
/*      */       } else {
/* 2222 */         LOGGER.warn("Received packet for unknown scoreboard objective: {}", objectiveName);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetDisplayObjective(ClientboundSetDisplayObjectivePacket packet) {
/* 2229 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2231 */     String objectiveName = packet.getObjectiveName();
/* 2232 */     Objective objective = (objectiveName == null) ? null : this.scoreboard.getObjective(objectiveName);
/* 2233 */     this.scoreboard.setDisplayObjective(packet.getSlot(), objective);
/*      */   }
/*      */   
/*      */   public void handleSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket packet) {
/*      */     PlayerTeam team;
/* 2238 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2241 */     ClientboundSetPlayerTeamPacket.Action teamAction = packet.getTeamAction();
/* 2243 */     if (teamAction == ClientboundSetPlayerTeamPacket.Action.ADD) {
/* 2244 */       team = this.scoreboard.addPlayerTeam(packet.getName());
/*      */     } else {
/* 2246 */       team = this.scoreboard.getPlayerTeam(packet.getName());
/* 2247 */       if (team == null) {
/* 2248 */         LOGGER.warn("Received packet for unknown team {}: team action: {}, player action: {}", new Object[] { packet.getName(), packet.getTeamAction(), packet.getPlayerAction() });
/*      */         return;
/*      */       } 
/*      */     } 
/* 2253 */     Optional<ClientboundSetPlayerTeamPacket.Parameters> parameters = packet.getParameters();
/* 2255 */     parameters.ifPresent(p -> {
/*      */           team.setDisplayName(p.displayName());
/*      */           team.setColor(p.color());
/*      */           team.unpackOptions(p.options());
/*      */           team.setNameTagVisibility(p.nameTagVisibility());
/*      */           team.setCollisionRule(p.collisionRule());
/*      */           team.setPlayerPrefix(p.playerPrefix());
/*      */           team.setPlayerSuffix(p.playerSuffix());
/*      */         });
/* 2265 */     ClientboundSetPlayerTeamPacket.Action playerAction = packet.getPlayerAction();
/* 2266 */     if (playerAction == ClientboundSetPlayerTeamPacket.Action.ADD) {
/* 2267 */       for (String player : packet.getPlayers())
/* 2268 */         this.scoreboard.addPlayerToTeam(player, team); 
/* 2270 */     } else if (playerAction == ClientboundSetPlayerTeamPacket.Action.REMOVE) {
/* 2271 */       for (String player : packet.getPlayers())
/* 2272 */         this.scoreboard.removePlayerFromTeam(player, team); 
/*      */     } 
/* 2276 */     if (teamAction == ClientboundSetPlayerTeamPacket.Action.REMOVE)
/* 2277 */       this.scoreboard.removePlayerTeam(team); 
/*      */   }
/*      */   
/*      */   public void handleParticleEvent(ClientboundLevelParticlesPacket packet) {
/* 2283 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2284 */     if (packet.getCount() == 0) {
/* 2285 */       double xa = (packet.getMaxSpeed() * packet.getXDist());
/* 2286 */       double ya = (packet.getMaxSpeed() * packet.getYDist());
/* 2287 */       double za = (packet.getMaxSpeed() * packet.getZDist());
/*      */       try {
/* 2289 */         this.level.addParticle(packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX(), packet.getY(), packet.getZ(), xa, ya, za);
/* 2290 */       } catch (Throwable ignored) {
/* 2291 */         LOGGER.warn("Could not spawn particle effect {}", packet.getParticle());
/*      */       } 
/*      */     } else {
/* 2294 */       for (int i = 0; i < packet.getCount(); i++) {
/* 2295 */         double xVarience = this.random.nextGaussian() * packet.getXDist();
/* 2296 */         double yVarience = this.random.nextGaussian() * packet.getYDist();
/* 2297 */         double zVarience = this.random.nextGaussian() * packet.getZDist();
/* 2298 */         double xa = this.random.nextGaussian() * packet.getMaxSpeed();
/* 2299 */         double ya = this.random.nextGaussian() * packet.getMaxSpeed();
/* 2300 */         double za = this.random.nextGaussian() * packet.getMaxSpeed();
/*      */         try {
/* 2302 */           this.level.addParticle(packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX() + xVarience, packet.getY() + yVarience, packet.getZ() + zVarience, xa, ya, za);
/* 2303 */         } catch (Throwable ignored) {
/* 2304 */           LOGGER.warn("Could not spawn particle effect {}", packet.getParticle());
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleUpdateAttributes(ClientboundUpdateAttributesPacket packet) {
/*      */     LivingEntity livingEntity;
/* 2313 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2314 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 2315 */     if (entity == null)
/*      */       return; 
/* 2318 */     if (entity instanceof LivingEntity) {
/* 2318 */       livingEntity = (LivingEntity)entity;
/*      */     } else {
/* 2319 */       throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + String.valueOf(entity) + ")");
/*      */     } 
/* 2322 */     AttributeMap attributes = livingEntity.getAttributes();
/* 2323 */     for (ClientboundUpdateAttributesPacket.AttributeSnapshot attribute : packet.getValues()) {
/* 2324 */       AttributeInstance instance = attributes.getInstance(attribute.attribute());
/* 2326 */       if (instance == null) {
/* 2327 */         LOGGER.warn("Entity {} does not have attribute {}", entity, attribute.attribute().getRegisteredName());
/*      */         continue;
/*      */       } 
/* 2330 */       instance.setBaseValue(attribute.base());
/* 2331 */       instance.removeModifiers();
/* 2333 */       for (AttributeModifier modifier : attribute.modifiers())
/* 2334 */         instance.addTransientModifier(modifier); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handlePlaceRecipe(ClientboundPlaceGhostRecipePacket packet) {
/* 2341 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2343 */     AbstractContainerMenu containerMenu = this.minecraft.player.containerMenu;
/* 2344 */     if (containerMenu.containerId != packet.containerId())
/*      */       return; 
/* 2348 */     Screen screen = this.minecraft.gui.screen();
/* 2348 */     if (screen instanceof RecipeUpdateListener) {
/* 2348 */       RecipeUpdateListener listener = (RecipeUpdateListener)screen;
/* 2349 */       listener.fillGhostRecipe(packet.recipeDisplay());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleLightUpdatePacket(ClientboundLightUpdatePacket packet) {
/* 2355 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2356 */     int x = packet.getX();
/* 2357 */     int z = packet.getZ();
/* 2358 */     ClientboundLightUpdatePacketData lightData = packet.getLightData();
/* 2359 */     this.level.queueLightUpdate(() -> applyLightData(x, z, lightData, true));
/*      */   }
/*      */   
/*      */   private void applyLightData(int x, int z, ClientboundLightUpdatePacketData lightData, boolean scheduleRebuild) {
/* 2363 */     LevelLightEngine lightEngine = this.level.getChunkSource().getLightEngine();
/* 2364 */     BitSet skyYMask = lightData.getSkyYMask();
/* 2365 */     BitSet emptySkyYMask = lightData.getEmptySkyYMask();
/* 2366 */     Iterator<byte[]> skyUpdates = (Iterator)lightData.getSkyUpdates().iterator();
/* 2368 */     readSectionList(x, z, lightEngine, LightLayer.SKY, skyYMask, emptySkyYMask, skyUpdates, scheduleRebuild);
/* 2370 */     BitSet blockYMask = lightData.getBlockYMask();
/* 2371 */     BitSet emptyBlockYMask = lightData.getEmptyBlockYMask();
/* 2372 */     Iterator<byte[]> blockUpdates = (Iterator)lightData.getBlockUpdates().iterator();
/* 2373 */     readSectionList(x, z, lightEngine, LightLayer.BLOCK, blockYMask, emptyBlockYMask, blockUpdates, scheduleRebuild);
/* 2375 */     lightEngine.setLightEnabled(new ChunkPos(x, z), true);
/*      */   }
/*      */   
/*      */   public void handleMerchantOffers(ClientboundMerchantOffersPacket packet) {
/* 2380 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2381 */     AbstractContainerMenu menu = this.minecraft.player.containerMenu;
/* 2382 */     if (packet.getContainerId() == menu.containerId && menu instanceof MerchantMenu) {
/* 2382 */       MerchantMenu merchantMenu = (MerchantMenu)menu;
/* 2383 */       merchantMenu.setOffers(packet.getOffers());
/* 2384 */       merchantMenu.setXp(packet.getVillagerXp());
/* 2385 */       merchantMenu.setMerchantLevel(packet.getVillagerLevel());
/* 2386 */       merchantMenu.setShowProgressBar(packet.showProgress());
/* 2387 */       merchantMenu.setCanRestock(packet.canRestock());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleSetChunkCacheRadius(ClientboundSetChunkCacheRadiusPacket packet) {
/* 2393 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2394 */     this.serverChunkRadius = packet.getRadius();
/* 2395 */     this.minecraft.options.setServerRenderDistance(this.serverChunkRadius);
/* 2396 */     this.level.getChunkSource().updateViewRadius(packet.getRadius());
/*      */   }
/*      */   
/*      */   public void handleSetSimulationDistance(ClientboundSetSimulationDistancePacket packet) {
/* 2401 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2402 */     this.serverSimulationDistance = packet.simulationDistance();
/* 2403 */     this.level.setServerSimulationDistance(this.serverSimulationDistance);
/*      */   }
/*      */   
/*      */   public void handleSetChunkCacheCenter(ClientboundSetChunkCacheCenterPacket packet) {
/* 2408 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2409 */     this.level.getChunkSource().updateViewCenter(packet.getX(), packet.getZ());
/*      */   }
/*      */   
/*      */   public void handleBlockChangedAck(ClientboundBlockChangedAckPacket packet) {
/* 2414 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2415 */     this.level.handleBlockChangedAck(packet.sequence());
/*      */   }
/*      */   
/*      */   public void handleBundlePacket(ClientboundBundlePacket packet) {
/* 2420 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2421 */     for (Packet<? super ClientGamePacketListener> subPacket : (Iterable<Packet<? super ClientGamePacketListener>>)packet.subPackets())
/* 2422 */       subPacket.handle((PacketListener)this); 
/*      */   }
/*      */   
/*      */   public void handleProjectilePowerPacket(ClientboundProjectilePowerPacket packet) {
/* 2428 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2429 */     Entity entity = this.level.getEntity(packet.getId());
/* 2430 */     if (entity instanceof AbstractHurtingProjectile) {
/* 2430 */       AbstractHurtingProjectile projectile = (AbstractHurtingProjectile)entity;
/* 2431 */       projectile.accelerationPower = packet.getAccelerationPower();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleChunkBatchStart(ClientboundChunkBatchStartPacket packet) {
/* 2437 */     this.chunkBatchSizeCalculator.onBatchStart();
/*      */   }
/*      */   
/*      */   public void handleChunkBatchFinished(ClientboundChunkBatchFinishedPacket packet) {
/* 2442 */     this.chunkBatchSizeCalculator.onBatchFinished(packet.batchSize());
/* 2443 */     send((Packet<?>)new ServerboundChunkBatchReceivedPacket(this.chunkBatchSizeCalculator.getDesiredChunksPerTick()));
/*      */   }
/*      */   
/*      */   public void handleDebugSample(ClientboundDebugSamplePacket packet) {
/* 2448 */     this.minecraft.getDebugOverlay().logRemoteSample(packet.sample(), packet.debugSampleType());
/*      */   }
/*      */   
/*      */   public void handlePongResponse(ClientboundPongResponsePacket packet) {
/* 2453 */     this.pingDebugMonitor.onPongReceived(packet);
/*      */   }
/*      */   
/*      */   public void handleTestInstanceBlockStatus(ClientboundTestInstanceBlockStatus packet) {
/* 2458 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2459 */     Screen screen = this.minecraft.gui.screen();
/* 2459 */     if (screen instanceof TestInstanceBlockEditScreen) {
/* 2459 */       TestInstanceBlockEditScreen editScreen = (TestInstanceBlockEditScreen)screen;
/* 2460 */       editScreen.setStatus(packet.status(), packet.size());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleWaypoint(ClientboundTrackedWaypointPacket packet) {
/* 2466 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2467 */     packet.apply((TrackedWaypointManager)this.waypointManager);
/*      */   }
/*      */   
/*      */   public void handleDebugChunkValue(ClientboundDebugChunkValuePacket packet) {
/* 2472 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2473 */     this.debugSubscriber.updateChunk(this.level.getGameTime(), packet.chunkPos(), packet.update());
/*      */   }
/*      */   
/*      */   public void handleDebugBlockValue(ClientboundDebugBlockValuePacket packet) {
/* 2478 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2479 */     this.debugSubscriber.updateBlock(this.level.getGameTime(), packet.blockPos(), packet.update());
/*      */   }
/*      */   
/*      */   public void handleDebugEntityValue(ClientboundDebugEntityValuePacket packet) {
/* 2484 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2485 */     Entity entity = this.level.getEntity(packet.entityId());
/* 2486 */     if (entity != null)
/* 2487 */       this.debugSubscriber.updateEntity(this.level.getGameTime(), entity, packet.update()); 
/*      */   }
/*      */   
/*      */   public void handleDebugEvent(ClientboundDebugEventPacket packet) {
/* 2493 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2494 */     this.debugSubscriber.pushEvent(this.level.getGameTime(), packet.event());
/*      */   }
/*      */   
/*      */   public void handleGameTestHighlightPos(ClientboundGameTestHighlightPosPacket packet) {
/* 2499 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2500 */     this.minecraft.levelExtractor.gameTestBlockHighlightRenderer.highlightPos(packet.absolutePos(), packet.relativePos());
/*      */   }
/*      */   
/*      */   public void handleLowDiskSpaceWarning(ClientboundLowDiskSpaceWarningPacket packet) {
/* 2505 */     this.minecraft.sendLowDiskSpaceWarning();
/*      */   }
/*      */   
/*      */   private void readSectionList(int chunkX, int chunkZ, LevelLightEngine lightEngine, LightLayer layer, BitSet yMask, BitSet emptyYMask, Iterator<byte[]> updates, boolean scheduleRebuild) {
/* 2509 */     for (int sectionIndex = 0; sectionIndex < lightEngine.getLightSectionCount(); sectionIndex++) {
/* 2510 */       int sectionY = lightEngine.getMinLightSection() + sectionIndex;
/* 2511 */       boolean haveData = yMask.get(sectionIndex);
/* 2512 */       boolean haveEmpty = emptyYMask.get(sectionIndex);
/* 2513 */       if (haveData || haveEmpty) {
/* 2514 */         lightEngine.queueSectionData(layer, SectionPos.of(chunkX, sectionY, chunkZ), haveData ? new DataLayer((byte[])((byte[])updates.next()).clone()) : new DataLayer());
/* 2515 */         if (scheduleRebuild)
/* 2516 */           this.level.setSectionDirtyWithNeighbors(chunkX, sectionY, chunkZ); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Connection getConnection() {
/* 2523 */     return this.connection;
/*      */   }
/*      */   
/*      */   public boolean isAcceptingMessages() {
/* 2528 */     return (this.connection.isConnected() && !this.closed);
/*      */   }
/*      */   
/*      */   public Collection<PlayerInfo> getListedOnlinePlayers() {
/* 2532 */     return this.listedPlayers;
/*      */   }
/*      */   
/*      */   public Collection<PlayerInfo> getOnlinePlayers() {
/* 2536 */     return this.playerInfoMap.values();
/*      */   }
/*      */   
/*      */   public Collection<UUID> getOnlinePlayerIds() {
/* 2540 */     return this.playerInfoMap.keySet();
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfo(UUID player) {
/* 2544 */     return this.playerInfoMap.get(player);
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfo(String player) {
/* 2548 */     for (PlayerInfo playerInfo : this.playerInfoMap.values()) {
/* 2549 */       if (playerInfo.getProfile().name().equals(player))
/* 2550 */         return playerInfo; 
/*      */     } 
/* 2554 */     return null;
/*      */   }
/*      */   
/*      */   public Map<UUID, PlayerInfo> getSeenPlayers() {
/* 2558 */     return this.seenPlayers;
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfoIgnoreCase(String player) {
/* 2562 */     for (PlayerInfo playerInfo : this.playerInfoMap.values()) {
/* 2563 */       if (playerInfo.getProfile().name().equalsIgnoreCase(player))
/* 2564 */         return playerInfo; 
/*      */     } 
/* 2568 */     return null;
/*      */   }
/*      */   
/*      */   public GameProfile getLocalGameProfile() {
/* 2572 */     return this.localGameProfile;
/*      */   }
/*      */   
/*      */   public ClientAdvancements getAdvancements() {
/* 2576 */     return this.advancements;
/*      */   }
/*      */   
/*      */   public CommandDispatcher<ClientSuggestionProvider> getCommands() {
/* 2580 */     return this.commands;
/*      */   }
/*      */   
/*      */   public ClientLevel getLevel() {
/* 2584 */     return this.level;
/*      */   }
/*      */   
/*      */   public DebugQueryHandler getDebugQueryHandler() {
/* 2588 */     return this.debugQueryHandler;
/*      */   }
/*      */   
/*      */   public Set<ResourceKey<Level>> levels() {
/* 2592 */     return this.levels;
/*      */   }
/*      */   
/*      */   public RegistryAccess.Frozen registryAccess() {
/* 2596 */     return this.registryAccess;
/*      */   }
/*      */   
/*      */   public void markMessageAsProcessed(MessageSignature signature, boolean wasShown) {
/* 2600 */     if (this.lastSeenMessages.addPending(signature, wasShown) && 
/* 2601 */       this.lastSeenMessages.offset() > 64)
/* 2602 */       sendChatAcknowledgement(); 
/*      */   }
/*      */   
/*      */   private void sendChatAcknowledgement() {
/* 2608 */     int offset = this.lastSeenMessages.getAndClearOffset();
/* 2609 */     if (offset > 0)
/* 2610 */       send((Packet<?>)new ServerboundChatAckPacket(offset)); 
/*      */   }
/*      */   
/*      */   public void sendChat(String content) {
/* 2615 */     Instant timeStamp = Instant.now();
/* 2616 */     long salt = Crypt.SaltSupplier.getLong();
/* 2617 */     LastSeenMessagesTracker.Update lastSeenUpdate = this.lastSeenMessages.generateAndApplyUpdate();
/* 2618 */     MessageSignature signature = this.signedMessageEncoder.pack(new SignedMessageBody(content, timeStamp, salt, lastSeenUpdate.lastSeen()));
/* 2619 */     send((Packet<?>)new ServerboundChatPacket(content, timeStamp, salt, signature, lastSeenUpdate.update()));
/*      */   }
/*      */   
/*      */   public void sendCommand(String command) {
/* 2623 */     SignableCommand<ClientSuggestionProvider> signableCommand = SignableCommand.of(this.commands.parse(command, this.suggestionsProvider));
/* 2624 */     if (signableCommand.arguments().isEmpty()) {
/* 2625 */       send((Packet<?>)new ServerboundChatCommandPacket(command));
/*      */       return;
/*      */     } 
/* 2629 */     Instant timeStamp = Instant.now();
/* 2630 */     long salt = Crypt.SaltSupplier.getLong();
/* 2631 */     LastSeenMessagesTracker.Update lastSeenUpdate = this.lastSeenMessages.generateAndApplyUpdate();
/* 2632 */     ArgumentSignatures argumentSignatures = ArgumentSignatures.signCommand(signableCommand, argument -> {
/*      */           SignedMessageBody signedBody = new SignedMessageBody(argument, timeStamp, salt, lastSeenUpdate.lastSeen());
/*      */           return this.signedMessageEncoder.pack(signedBody);
/*      */         });
/* 2636 */     send((Packet<?>)new ServerboundChatCommandSignedPacket(command, timeStamp, salt, argumentSignatures, lastSeenUpdate.update()));
/*      */   }
/*      */   
/*      */   public void sendUnattendedCommand(String command, Screen screenAfterCommand) {
/* 2642 */     switch (verifyCommand(command).ordinal()) {
/*      */       case 0:
/* 2644 */         send((Packet<?>)new ServerboundChatCommandPacket(command));
/* 2645 */         this.minecraft.gui.setScreen(screenAfterCommand);
/*      */         break;
/*      */       case 1:
/* 2648 */         openCommandSendConfirmationWindow(command, "multiplayer.confirm_command.parse_errors", screenAfterCommand);
/*      */         break;
/*      */       case 3:
/* 2650 */         openCommandSendConfirmationWindow(command, "multiplayer.confirm_command.permissions_required", screenAfterCommand);
/*      */         break;
/*      */       case 2:
/* 2652 */         openSignedCommandSendConfirmationWindow(command, "multiplayer.confirm_command.signature_required", screenAfterCommand);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private enum CommandCheckResult {
/* 2657 */     NO_ISSUES, PARSE_ERRORS, SIGNATURE_REQUIRED, PERMISSIONS_REQUIRED;
/*      */   }
/*      */   
/*      */   private CommandCheckResult verifyCommand(String command) {
/* 2664 */     ParseResults<ClientSuggestionProvider> parseWithCurrentPermissions = this.commands.parse(command, this.suggestionsProvider);
/* 2665 */     if (!isValidCommand(parseWithCurrentPermissions))
/* 2666 */       return CommandCheckResult.PARSE_ERRORS; 
/* 2669 */     if (SignableCommand.hasSignableArguments(parseWithCurrentPermissions))
/* 2670 */       return CommandCheckResult.SIGNATURE_REQUIRED; 
/* 2673 */     ParseResults<ClientSuggestionProvider> parseWithoutPermissions = this.commands.parse(command, this.restrictedSuggestionsProvider);
/* 2674 */     if (!isValidCommand(parseWithoutPermissions))
/* 2675 */       return CommandCheckResult.PERMISSIONS_REQUIRED; 
/* 2678 */     return CommandCheckResult.NO_ISSUES;
/*      */   }
/*      */   
/*      */   private static boolean isValidCommand(ParseResults<?> parseResults) {
/* 2682 */     return (!parseResults.getReader().canRead() && parseResults
/* 2683 */       .getExceptions().isEmpty() && parseResults
/* 2684 */       .getContext().getLastChild().getCommand() != null);
/*      */   }
/*      */   
/*      */   private void openSendConfirmationWindow(String command, String messageKey, Component acceptButton, Runnable onAccept) {
/* 2688 */     Screen currentScreen = this.minecraft.gui.screen();
/* 2689 */     this.minecraft.gui.setScreen((Screen)new ConfirmScreen(result -> {
/*      */             if (result) {
/*      */               onAccept.run();
/*      */             } else {
/*      */               this.minecraft.gui.setScreen(currentScreen);
/*      */             } 
/* 2689 */           }COMMAND_SEND_CONFIRM_TITLE, 
/*      */           
/* 2698 */           (Component)Component.translatable(messageKey, new Object[] { Component.literal(command).withStyle(ChatFormatting.YELLOW) }), acceptButton, 
/*      */           
/* 2700 */           (currentScreen != null) ? CommonComponents.GUI_BACK : CommonComponents.GUI_CANCEL));
/*      */   }
/*      */   
/*      */   private void openCommandSendConfirmationWindow(String command, String messageKey, Screen screenAfterCommand) {
/* 2705 */     openSendConfirmationWindow(command, messageKey, BUTTON_RUN_COMMAND, () -> {
/*      */           send((Packet<?>)new ServerboundChatCommandPacket(command));
/*      */           this.minecraft.gui.setScreen(screenAfterCommand);
/*      */         });
/*      */   }
/*      */   
/*      */   private void openSignedCommandSendConfirmationWindow(String command, String messageKey, Screen screenAfterCommand) {
/* 2717 */     boolean canOpenChatScreen = (screenAfterCommand == null && this.minecraft.player != null && this.minecraft.player.chatAbilities().canSendCommands());
/* 2718 */     if (canOpenChatScreen) {
/* 2719 */       openSendConfirmationWindow(command, messageKey, BUTTON_SUGGEST_COMMAND, () -> this.minecraft.gui.openChatAndAddText(ChatComponent.ChatMethod.COMMAND, command));
/*      */     } else {
/* 2728 */       openSendConfirmationWindow(command, messageKey, CommonComponents.GUI_COPY_TO_CLIPBOARD, () -> {
/*      */             this.minecraft.keyboardHandler.setClipboard("/" + command);
/*      */             this.minecraft.gui.setScreen(screenAfterCommand);
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   public void broadcastClientInformation(ClientInformation information) {
/* 2741 */     if (!information.equals(this.remoteClientInformation)) {
/* 2742 */       send((Packet<?>)new ServerboundClientInformationPacket(information));
/* 2743 */       this.remoteClientInformation = information;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void tick() {
/* 2749 */     if (this.chatSession != null && this.minecraft.getProfileKeyPairManager().shouldRefreshKeyPair())
/* 2750 */       prepareKeyPair(); 
/* 2753 */     if (this.keyPairFuture != null && this.keyPairFuture.isDone()) {
/* 2754 */       ((Optional)this.keyPairFuture.join()).ifPresent(this::setKeyPair);
/* 2755 */       this.keyPairFuture = null;
/*      */     } 
/* 2758 */     sendDeferredPackets();
/* 2760 */     if (this.minecraft.getDebugOverlay().showNetworkCharts())
/* 2761 */       this.pingDebugMonitor.tick(); 
/* 2764 */     if (this.level != null)
/* 2765 */       this.debugSubscriber.tick(this.level.getGameTime()); 
/* 2768 */     this.telemetryManager.tick();
/* 2770 */     if (this.levelLoadTracker != null) {
/* 2771 */       this.levelLoadTracker.tickClientLoad();
/* 2772 */       if (this.levelLoadTracker.isLevelReady()) {
/* 2773 */         notifyPlayerLoaded();
/* 2774 */         this.levelLoadTracker = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void notifyPlayerLoaded() {
/* 2780 */     if (!hasClientLoaded()) {
/* 2782 */       this.connection.send((Packet)new ServerboundPlayerLoadedPacket());
/* 2783 */       setClientLoaded(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Runnable getPlayerCompiledSectionCallback() {
/* 2788 */     return (this.levelLoadTracker != null) ? this.levelLoadTracker.getPlayerCompiledSectionCallback() : null;
/*      */   }
/*      */   
/*      */   public void prepareKeyPair() {
/* 2792 */     this.keyPairFuture = this.minecraft.getProfileKeyPairManager().prepareKeyPair();
/*      */   }
/*      */   
/*      */   private void setKeyPair(ProfileKeyPair keyPair) {
/* 2797 */     if (!this.minecraft.isLocalPlayer(this.localGameProfile.id()))
/*      */       return; 
/* 2800 */     if (this.chatSession != null && this.chatSession.keyPair().equals(keyPair))
/*      */       return; 
/* 2804 */     this.chatSession = LocalChatSession.create(keyPair);
/* 2805 */     this.signedMessageEncoder = this.chatSession.createMessageEncoder(this.localGameProfile.id());
/* 2806 */     send((Packet<?>)new ServerboundChatSessionUpdatePacket(this.chatSession.asRemote().asData()));
/*      */   }
/*      */   
/*      */   protected DialogConnectionAccess createDialogAccess() {
/* 2811 */     return new ClientCommonPacketListenerImpl.CommonDialogAccess() {
/*      */         public void runCommand(String command, Screen activeScreen) {
/* 2814 */           ClientPacketListener.this.sendUnattendedCommand(command, activeScreen);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public ServerData getServerData() {
/* 2820 */     return this.serverData;
/*      */   }
/*      */   
/*      */   public FeatureFlagSet enabledFeatures() {
/* 2824 */     return this.enabledFeatures;
/*      */   }
/*      */   
/*      */   public boolean isFeatureEnabled(FeatureFlagSet requiredFlags) {
/* 2828 */     return requiredFlags.isSubsetOf(enabledFeatures());
/*      */   }
/*      */   
/*      */   public Scoreboard scoreboard() {
/* 2832 */     return this.scoreboard;
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 2836 */     return this.potionBrewing;
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 2840 */     return this.fuelValues;
/*      */   }
/*      */   
/*      */   public void updateSearchTrees() {
/* 2844 */     this.searchTrees.rebuildAfterLanguageChange();
/*      */   }
/*      */   
/*      */   public SessionSearchTrees searchTrees() {
/* 2848 */     return this.searchTrees;
/*      */   }
/*      */   
/*      */   public void registerForCleaning(CacheSlot<?, ?> slot) {
/* 2852 */     this.cacheSlots.add(new WeakReference<>(slot));
/*      */   }
/*      */   
/*      */   public HashedPatchMap.HashGenerator decoratedHashOpsGenenerator() {
/* 2856 */     return this.decoratedHashOpsGenerator;
/*      */   }
/*      */   
/*      */   public ClientWaypointManager getWaypointManager() {
/* 2860 */     return this.waypointManager;
/*      */   }
/*      */   
/*      */   public DebugValueAccess createDebugValueAccess() {
/* 2864 */     return this.debugSubscriber.createDebugValueAccess(this.level);
/*      */   }
/*      */   
/*      */   public boolean hasClientLoaded() {
/* 2868 */     return this.clientLoaded;
/*      */   }
/*      */   
/*      */   private void setClientLoaded(boolean loaded) {
/* 2872 */     this.clientLoaded = loaded;
/*      */   }
/*      */   
/*      */   public ClientClockManager clockManager() {
/* 2876 */     return this.clockManager;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */