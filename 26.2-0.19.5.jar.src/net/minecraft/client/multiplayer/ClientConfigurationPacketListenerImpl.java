/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.ChatComponent;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*     */ import net.minecraft.client.gui.screens.multiplayer.CodeOfConductScreen;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*     */ import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.game.GameProtocols;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientConfigurationPacketListenerImpl extends ClientCommonPacketListenerImpl implements ClientConfigurationPacketListener, TickablePacketListener {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  47 */   public static final Component DISCONNECTED_MESSAGE = (Component)Component.translatable("multiplayer.disconnect.code_of_conduct");
/*     */   
/*     */   private final LevelLoadTracker levelLoadTracker;
/*     */   
/*     */   private final GameProfile localGameProfile;
/*     */   
/*     */   private FeatureFlagSet enabledFeatures;
/*     */   
/*     */   private final RegistryAccess.Frozen receivedRegistries;
/*     */   
/*  53 */   private final RegistryDataCollector registryDataCollector = new RegistryDataCollector();
/*     */   
/*     */   private KnownPacksManager knownPacks;
/*     */   
/*     */   protected ChatComponent.State chatState;
/*     */   
/*     */   private boolean seenCodeOfConduct;
/*     */   
/*     */   public ClientConfigurationPacketListenerImpl(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
/*  59 */     super(minecraft, connection, cookie);
/*  60 */     this.levelLoadTracker = cookie.levelLoadTracker();
/*  61 */     this.localGameProfile = cookie.localGameProfile();
/*  62 */     this.receivedRegistries = cookie.receivedRegistries();
/*  63 */     this.enabledFeatures = cookie.enabledFeatures();
/*  64 */     this.chatState = cookie.chatState();
/*     */   }
/*     */   
/*     */   public boolean isAcceptingMessages() {
/*  69 */     return this.connection.isConnected();
/*     */   }
/*     */   
/*     */   protected void handleCustomPayload(CustomPacketPayload payload) {
/*  74 */     handleUnknownCustomPayload(payload);
/*     */   }
/*     */   
/*     */   private void handleUnknownCustomPayload(CustomPacketPayload payload) {
/*  79 */     LOGGER.warn("Unknown custom packet payload: {}", payload.type().id());
/*     */   }
/*     */   
/*     */   public void handleRegistryData(ClientboundRegistryDataPacket packet) {
/*  84 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  85 */     this.registryDataCollector.appendContents(packet.registry(), packet.entries());
/*     */   }
/*     */   
/*     */   public void handleUpdateTags(ClientboundUpdateTagsPacket packet) {
/*  90 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  91 */     this.registryDataCollector.appendTags(packet.getTags());
/*     */   }
/*     */   
/*     */   public void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket packet) {
/*  96 */     this.enabledFeatures = FeatureFlags.REGISTRY.fromNames(packet.features());
/*     */   }
/*     */   
/*     */   public void handleSelectKnownPacks(ClientboundSelectKnownPacks packet) {
/* 101 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 102 */     if (this.knownPacks == null)
/* 103 */       this.knownPacks = new KnownPacksManager(); 
/* 106 */     List<KnownPack> selected = this.knownPacks.trySelectingPacks(packet.knownPacks());
/* 107 */     send((Packet<?>)new ServerboundSelectKnownPacks(selected));
/*     */   }
/*     */   
/*     */   public void handleResetChat(ClientboundResetChatPacket packet) {
/* 112 */     this.chatState = null;
/*     */   }
/*     */   
/*     */   private <T> T runWithResources(Function<ResourceProvider, T> operation) {
/* 116 */     if (this.knownPacks == null)
/* 117 */       return operation.apply(ResourceProvider.EMPTY); 
/* 120 */     CloseableResourceManager manager = this.knownPacks.createResourceManager();
/*     */     try {
/* 121 */       T t = operation.apply(manager);
/* 122 */       if (manager != null)
/* 122 */         manager.close(); 
/*     */       return t;
/*     */     } catch (Throwable throwable) {
/*     */       if (manager != null)
/*     */         try {
/*     */           manager.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleCodeOfConduct(ClientboundCodeOfConductPacket packet) {
/* 127 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 128 */     if (this.seenCodeOfConduct)
/* 129 */       throw new IllegalStateException("Server sent duplicate Code of Conduct"); 
/* 132 */     this.seenCodeOfConduct = true;
/* 133 */     String codeOfConduct = packet.codeOfConduct();
/* 134 */     if (this.serverData != null && this.serverData.hasAcceptedCodeOfConduct(codeOfConduct)) {
/* 135 */       send((Packet<?>)ServerboundAcceptCodeOfConductPacket.INSTANCE);
/*     */     } else {
/* 137 */       Screen lastScreen = this.minecraft.gui.screen();
/* 138 */       this.minecraft.gui.setScreen((Screen)new CodeOfConductScreen(this.serverData, lastScreen, codeOfConduct, accepted -> {
/*     */               if (accepted) {
/*     */                 send((Packet<?>)ServerboundAcceptCodeOfConductPacket.INSTANCE);
/*     */                 this.minecraft.gui.setScreen(lastScreen);
/*     */               } else {
/*     */                 createDialogAccess().disconnect(DISCONNECTED_MESSAGE);
/*     */               } 
/*     */             }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleConfigurationFinished(ClientboundFinishConfigurationPacket packet) {
/* 152 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 153 */     RegistryAccess.Frozen registries = runWithResources(knownPacksProvider -> this.registryDataCollector.collectGameRegistries(knownPacksProvider, this.receivedRegistries, this.connection.isMemoryConnection()));
/* 155 */     IntegratedServer localServer = this.minecraft.getSingleplayerServer();
/* 159 */     if (localServer != null)
/* 160 */       registries = filterRegistries(localServer.registryAccess(), registries.listRegistryKeys()); 
/* 163 */     this.connection.setupInboundProtocol(GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator((RegistryAccess)registries)), (PacketListener)new ClientPacketListener(this.minecraft, this.connection, new CommonListenerCookie(this.levelLoadTracker, this.localGameProfile, this.telemetryManager, registries, this.enabledFeatures, this.serverBrand, this.serverData, this.postDisconnectScreen, this.serverCookies, this.chatState, this.customReportDetails, 
/*     */             
/* 178 */             serverLinks(), this.seenPlayers, this.seenInsecureChatWarning)));
/* 183 */     this.connection.send((Packet)ServerboundFinishConfigurationPacket.INSTANCE);
/* 184 */     this.connection.setupOutboundProtocol(GameProtocols.SERVERBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator((RegistryAccess)registries), new GameProtocols.Context(this) {
/*     */             public boolean hasInfiniteMaterials() {
/* 188 */               return true;
/*     */             }
/*     */           }));
/*     */   }
/*     */   
/*     */   private static RegistryAccess.Frozen filterRegistries(RegistryAccess.Frozen original, Stream<ResourceKey<? extends Registry<?>>> keysToInclude) {
/* 194 */     Objects.requireNonNull(original);
/* 194 */     List<? extends Registry<?>> filteredRegistries = keysToInclude.<Registry<?>>map(original::lookupOrThrow).toList();
/* 195 */     return (new RegistryAccess.ImmutableRegistryAccess(filteredRegistries)).freeze();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 200 */     sendDeferredPackets();
/*     */   }
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails reason) {
/* 205 */     super.onDisconnect(reason);
/* 207 */     this.minecraft.clearDownloadedResourcePacks();
/*     */   }
/*     */   
/*     */   protected DialogConnectionAccess createDialogAccess() {
/* 212 */     return new ClientCommonPacketListenerImpl.CommonDialogAccess(this) {
/*     */         public void runCommand(String command, Screen activeScreen) {
/* 215 */           ClientConfigurationPacketListenerImpl.LOGGER.warn("Commands are not supported in configuration phase, trying to run '{}'", command);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientConfigurationPacketListenerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */