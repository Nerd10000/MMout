/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.exceptions.AuthenticationException;
/*      */ import com.mojang.authlib.minecraft.BanDetails;
/*      */ import com.mojang.authlib.minecraft.UserApiService;
/*      */ import com.mojang.authlib.yggdrasil.FriendsService;
/*      */ import com.mojang.authlib.yggdrasil.ProfileActionType;
/*      */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*      */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*      */ import com.mojang.blaze3d.GpuFormat;
/*      */ import com.mojang.blaze3d.TracyFrameCapture;
/*      */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*      */ import com.mojang.blaze3d.platform.ClientShutdownWatchdog;
/*      */ import com.mojang.blaze3d.platform.DisplayData;
/*      */ import com.mojang.blaze3d.platform.FramerateLimitTracker;
/*      */ import com.mojang.blaze3d.platform.GLX;
/*      */ import com.mojang.blaze3d.platform.IconSet;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.MessageBox;
/*      */ import com.mojang.blaze3d.platform.MonitorManager;
/*      */ import com.mojang.blaze3d.platform.TextInputManager;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import com.mojang.blaze3d.platform.WindowEventHandler;
/*      */ import com.mojang.blaze3d.shaders.GpuDebugOptions;
/*      */ import com.mojang.blaze3d.shaders.ShaderType;
/*      */ import com.mojang.blaze3d.systems.BackendCreationException;
/*      */ import com.mojang.blaze3d.systems.DeviceInfo;
/*      */ import com.mojang.blaze3d.systems.GpuBackend;
/*      */ import com.mojang.blaze3d.systems.GpuDevice;
/*      */ import com.mojang.blaze3d.systems.GpuSurface;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import com.mojang.blaze3d.systems.SurfaceException;
/*      */ import com.mojang.blaze3d.systems.TimerQuery;
/*      */ import com.mojang.blaze3d.textures.GpuTextureView;
/*      */ import com.mojang.blaze3d.vulkan.VulkanBackend;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.jtracy.DiscontinuousFrame;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.realmsclient.RealmsMainScreen;
/*      */ import com.mojang.realmsclient.client.RealmsClient;
/*      */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.net.Proxy;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.LongSupplier;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.Optionull;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SystemReport;
/*      */ import net.minecraft.client.color.block.BlockColors;
/*      */ import net.minecraft.client.entity.ClientMannequin;
/*      */ import net.minecraft.client.gui.ComponentPath;
/*      */ import net.minecraft.client.gui.Font;
/*      */ import net.minecraft.client.gui.Gui;
/*      */ import net.minecraft.client.gui.Hud;
/*      */ import net.minecraft.client.gui.components.DebugScreenOverlay;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntryList;
/*      */ import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
/*      */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*      */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*      */ import net.minecraft.client.gui.components.toasts.ToastManager;
/*      */ import net.minecraft.client.gui.font.FontManager;
/*      */ import net.minecraft.client.gui.font.providers.FreeTypeUtil;
/*      */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*      */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*      */ import net.minecraft.client.gui.screens.LoadingOverlay;
/*      */ import net.minecraft.client.gui.screens.MenuScreens;
/*      */ import net.minecraft.client.gui.screens.OutOfMemoryScreen;
/*      */ import net.minecraft.client.gui.screens.Overlay;
/*      */ import net.minecraft.client.gui.screens.ProgressScreen;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.TitleScreen;
/*      */ import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.InventoryScreen;
/*      */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*      */ import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
/*      */ import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
/*      */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*      */ import net.minecraft.client.gui.screens.social.RemoteFriendListUpdateHandler;
/*      */ import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
/*      */ import net.minecraft.client.main.GameConfig;
/*      */ import net.minecraft.client.model.geom.EntityModelSet;
/*      */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*      */ import net.minecraft.client.multiplayer.MultiPlayerGameMode;
/*      */ import net.minecraft.client.multiplayer.ProfileKeyPairManager;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.multiplayer.chat.ChatAbilities;
/*      */ import net.minecraft.client.multiplayer.chat.ChatRestriction;
/*      */ import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
/*      */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*      */ import net.minecraft.client.particle.ParticleEngine;
/*      */ import net.minecraft.client.particle.ParticleResources;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.LocalPlayerResolver;
/*      */ import net.minecraft.client.profiling.ClientMetricsSamplersProvider;
/*      */ import net.minecraft.client.quickplay.QuickPlayLog;
/*      */ import net.minecraft.client.renderer.GameRenderer;
/*      */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*      */ import net.minecraft.client.renderer.LevelRenderer;
/*      */ import net.minecraft.client.renderer.MapRenderer;
/*      */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*      */ import net.minecraft.client.renderer.ShaderManager;
/*      */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*      */ import net.minecraft.client.renderer.block.BlockStateModelSet;
/*      */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*      */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.entity.EntityRenderers;
/*      */ import net.minecraft.client.renderer.extract.LevelExtractor;
/*      */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*      */ import net.minecraft.client.renderer.state.WindowRenderState;
/*      */ import net.minecraft.client.renderer.texture.SkinTextureDownloader;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.resources.ClientPackSource;
/*      */ import net.minecraft.client.resources.DryFoliageColorReloadListener;
/*      */ import net.minecraft.client.resources.FoliageColorReloadListener;
/*      */ import net.minecraft.client.resources.GrassColorReloadListener;
/*      */ import net.minecraft.client.resources.MapTextureManager;
/*      */ import net.minecraft.client.resources.SkinManager;
/*      */ import net.minecraft.client.resources.language.ClientLanguage;
/*      */ import net.minecraft.client.resources.language.I18n;
/*      */ import net.minecraft.client.resources.language.LanguageManager;
/*      */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*      */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*      */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*      */ import net.minecraft.client.server.IntegratedServer;
/*      */ import net.minecraft.client.sounds.MusicManager;
/*      */ import net.minecraft.client.sounds.SoundBufferLibrary;
/*      */ import net.minecraft.client.sounds.SoundManager;
/*      */ import net.minecraft.client.telemetry.ClientTelemetryManager;
/*      */ import net.minecraft.client.telemetry.TelemetryEventType;
/*      */ import net.minecraft.client.telemetry.TelemetryProperty;
/*      */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*      */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*      */ import net.minecraft.client.tutorial.Tutorial;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.gizmos.GizmoCollector;
/*      */ import net.minecraft.gizmos.Gizmos;
/*      */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.PacketProcessor;
/*      */ import net.minecraft.network.chat.ClickEvent;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.contents.KeybindResolver;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*      */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.Bootstrap;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.Services;
/*      */ import net.minecraft.server.WorldStem;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.dialog.Dialogs;
/*      */ import net.minecraft.server.level.ChunkLevel;
/*      */ import net.minecraft.server.level.progress.LevelLoadListener;
/*      */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*      */ import net.minecraft.server.packs.PackResources;
/*      */ import net.minecraft.server.packs.PackType;
/*      */ import net.minecraft.server.packs.VanillaPackResources;
/*      */ import net.minecraft.server.packs.repository.FolderRepositorySource;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.server.packs.repository.PackSource;
/*      */ import net.minecraft.server.packs.repository.RepositorySource;
/*      */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*      */ import net.minecraft.server.packs.resources.ReloadInstance;
/*      */ import net.minecraft.server.packs.resources.ReloadableResourceManager;
/*      */ import net.minecraft.server.packs.resources.ResourceManager;
/*      */ import net.minecraft.server.players.ProfileResolver;
/*      */ import net.minecraft.sounds.Music;
/*      */ import net.minecraft.sounds.Musics;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.DialogTags;
/*      */ import net.minecraft.util.FileUtil;
/*      */ import net.minecraft.util.FileZipper;
/*      */ import net.minecraft.util.MemoryReserve;
/*      */ import net.minecraft.util.ModCheck;
/*      */ import net.minecraft.util.NativeModuleLister;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixers;
/*      */ import net.minecraft.util.profiling.ContinuousProfiler;
/*      */ import net.minecraft.util.profiling.EmptyProfileResults;
/*      */ import net.minecraft.util.profiling.InactiveProfiler;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.SingleTickProfiler;
/*      */ import net.minecraft.util.profiling.Zone;
/*      */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ActiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.InactiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*      */ import net.minecraft.util.thread.ReentrantBlockableEventLoop;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.BackgroundMusic;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.PiercingWeapon;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.validation.DirectoryValidator;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.lwjgl.glfw.GLFW;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class Minecraft extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler {
/*      */   private static Minecraft instance;
/*      */   
/*  265 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int MAX_TICKS_PER_UPDATE = 10;
/*      */   
/*  268 */   public static final Identifier DEFAULT_FONT = Identifier.withDefaultNamespace("default");
/*      */   
/*  269 */   private static final Identifier REGIONAL_COMPLIANCIES = Identifier.withDefaultNamespace("regional_compliancies.json");
/*      */   
/*  270 */   private static final CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*      */   
/*      */   public static final String UPDATE_DRIVERS_ADVICE = "Please make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).";
/*      */   
/*  275 */   private final long canary = Double.doubleToLongBits(Math.PI);
/*      */   
/*      */   private final Path resourcePackDirectory;
/*      */   
/*      */   private final CompletableFuture<ProfileResult> profileFuture;
/*      */   
/*      */   private final TextureManager textureManager;
/*      */   
/*      */   private final ShaderManager shaderManager;
/*      */   
/*      */   private final DataFixer fixerUpper;
/*      */   
/*      */   private final MonitorManager monitorManager;
/*      */   
/*      */   private final Window window;
/*      */   
/*      */   private final GpuSurface windowSurface;
/*      */   
/*      */   private final TextInputManager textInputManager;
/*      */   
/*  288 */   private final DeltaTracker.Timer deltaTracker = new DeltaTracker.Timer(20.0F, 0L, this::getTickTargetMillis);
/*      */   
/*      */   public final LevelExtractor levelExtractor;
/*      */   
/*      */   public final LevelRenderer levelRenderer;
/*      */   
/*      */   private final EntityRenderDispatcher entityRenderDispatcher;
/*      */   
/*      */   private final ItemModelResolver itemModelResolver;
/*      */   
/*      */   private final MapRenderer mapRenderer;
/*      */   
/*      */   public final ParticleEngine particleEngine;
/*      */   
/*      */   private final User user;
/*      */   
/*      */   public final Font font;
/*      */   
/*      */   public final Font fontFilterFishy;
/*      */   
/*      */   public final GameRenderer gameRenderer;
/*      */   
/*      */   public final Gui gui;
/*      */   
/*      */   public final Options options;
/*      */   
/*      */   public final DebugScreenEntryList debugEntries;
/*      */   
/*      */   private final HotbarManager hotbarManager;
/*      */   
/*      */   public final MouseHandler mouseHandler;
/*      */   
/*      */   public final KeyboardHandler keyboardHandler;
/*      */   
/*  307 */   private InputType lastInputType = InputType.NONE;
/*      */   
/*      */   public final File gameDirectory;
/*      */   
/*      */   private final String launchedVersion;
/*      */   
/*      */   private final Proxy proxy;
/*      */   
/*      */   private final boolean offlineDeveloperMode;
/*      */   
/*      */   private final LevelStorageSource levelSource;
/*      */   
/*      */   private final boolean demo;
/*      */   
/*      */   private final boolean allowsMultiplayer;
/*      */   
/*      */   private final boolean allowsChat;
/*      */   
/*      */   private final ReloadableResourceManager resourceManager;
/*      */   
/*      */   private final VanillaPackResources vanillaPackResources;
/*      */   
/*      */   private final DownloadedPackSource downloadedPackSource;
/*      */   
/*      */   private final PackRepository resourcePackRepository;
/*      */   
/*      */   private final LanguageManager languageManager;
/*      */   
/*      */   private final BlockColors blockColors;
/*      */   
/*      */   private final TracyFrameCapture tracyFrameCapture;
/*      */   
/*      */   private final SoundManager soundManager;
/*      */   
/*      */   private final MusicManager musicManager;
/*      */   
/*      */   private final FontManager fontManager;
/*      */   
/*      */   private final GpuWarnlistManager gpuWarnlistManager;
/*      */   
/*  331 */   private final PeriodicNotificationManager regionalCompliancies = new PeriodicNotificationManager(REGIONAL_COMPLIANCIES, Minecraft::countryEqualsISO3);
/*      */   
/*      */   private final UserApiService userApiService;
/*      */   
/*      */   private final CompletableFuture<UserApiService.UserProperties> userPropertiesFuture;
/*      */   
/*      */   private final SkinManager skinManager;
/*      */   
/*      */   private final AtlasManager atlasManager;
/*      */   
/*      */   private final ModelManager modelManager;
/*      */   
/*      */   private final MapTextureManager mapTextureManager;
/*      */   
/*      */   private final Tutorial tutorial;
/*      */   
/*      */   private final PlayerSocialManager playerSocialManager;
/*      */   
/*      */   private final RemoteFriendListUpdateHandler remoteFriendListUpdateHandler;
/*      */   
/*      */   private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*      */   
/*      */   private final ClientTelemetryManager telemetryManager;
/*      */   
/*      */   private final ProfileKeyPairManager profileKeyPairManager;
/*      */   
/*      */   private final RealmsDataFetcher realmsDataFetcher;
/*      */   
/*      */   private final QuickPlayLog quickPlayLog;
/*      */   
/*      */   private final Services services;
/*      */   
/*      */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*      */   
/*      */   private final TimerQuery timerQuery;
/*      */   
/*      */   public MultiPlayerGameMode gameMode;
/*      */   
/*      */   public ClientLevel level;
/*      */   
/*      */   public LocalPlayer player;
/*      */   
/*      */   private IntegratedServer singleplayerServer;
/*      */   
/*      */   private Connection pendingConnection;
/*      */   
/*      */   private boolean isLocalServer;
/*      */   
/*      */   public Entity crosshairPickEntity;
/*      */   
/*      */   public HitResult hitResult;
/*      */   
/*      */   private int rightClickDelay;
/*      */   
/*      */   protected int missTime;
/*      */   
/*      */   private volatile boolean pause;
/*      */   
/*  363 */   private long lastNanoTime = Util.getNanos();
/*      */   
/*      */   private long lastTime;
/*      */   
/*      */   private int frames;
/*      */   
/*      */   private Thread gameThread;
/*      */   
/*      */   private volatile boolean running;
/*      */   
/*      */   private static int fps;
/*      */   
/*      */   private long frameTimeNs;
/*      */   
/*      */   private final FramerateLimitTracker framerateLimitTracker;
/*      */   
/*      */   public boolean wireframe;
/*      */   
/*      */   public boolean smartCull = true;
/*      */   
/*  378 */   private long lastActiveTime = Util.getMillis();
/*      */   
/*      */   private CompletableFuture<Void> pendingReload;
/*      */   
/*      */   private int fpsPieRenderTicks;
/*      */   
/*      */   private final ContinuousProfiler fpsPieProfiler;
/*      */   
/*  384 */   private MetricsRecorder metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*      */   
/*  385 */   private final ResourceLoadStateTracker reloadStateTracker = new ResourceLoadStateTracker();
/*      */   
/*      */   private long savedCpuDuration;
/*      */   
/*      */   private double gpuUtilization;
/*      */   
/*      */   private final GameNarrator narrator;
/*      */   
/*      */   private ReportingContext reportingContext;
/*      */   
/*      */   private final DirectoryValidator directoryValidator;
/*      */   
/*      */   private boolean gameLoadFinished;
/*      */   
/*      */   private final long clientStartTimeMs;
/*      */   
/*      */   private long clientTickCount;
/*      */   
/*      */   private final PacketProcessor packetProcessor;
/*      */   
/*  401 */   private final SimpleGizmoCollector perTickGizmos = new SimpleGizmoCollector();
/*      */   
/*  402 */   private List<SimpleGizmoCollector.GizmoInstance> drainedLatestTickGizmos = new ArrayList<>();
/*      */   
/*      */   private boolean windowSurfaceNeedsReconfiguring = true;
/*      */   
/*      */   private boolean surfaceIsInvalid = true;
/*      */   
/*      */   private BackendCreationException backendCreationException;
/*      */   
/*      */   public Minecraft(final GameConfig gameConfig) {
/*  410 */     super("Client", true);
/*  411 */     instance = this;
/*  413 */     this.clientStartTimeMs = System.currentTimeMillis();
/*  415 */     this.gameDirectory = gameConfig.location.gameDirectory;
/*  416 */     File assetsDirectory = gameConfig.location.assetDirectory;
/*  417 */     this.resourcePackDirectory = gameConfig.location.resourcePackDirectory.toPath();
/*  418 */     this.launchedVersion = gameConfig.game.launchVersion;
/*  420 */     Path gameDirPath = this.gameDirectory.toPath();
/*  421 */     this.directoryValidator = LevelStorageSource.parseValidator(gameDirPath.resolve("allowed_symlinks.txt"));
/*  423 */     ClientPackSource clientPackSource = new ClientPackSource(gameConfig.location.getExternalAssetSource(), this.directoryValidator);
/*  424 */     this.downloadedPackSource = new DownloadedPackSource(this, gameDirPath.resolve("downloads"), gameConfig.user);
/*  425 */     FolderRepositorySource folderRepositorySource = new FolderRepositorySource(this.resourcePackDirectory, PackType.CLIENT_RESOURCES, PackSource.DEFAULT, this.directoryValidator);
/*  426 */     this.resourcePackRepository = new PackRepository(new RepositorySource[] { (RepositorySource)clientPackSource, this.downloadedPackSource.createRepositorySource(), (RepositorySource)folderRepositorySource });
/*  428 */     this.vanillaPackResources = clientPackSource.getVanillaPack();
/*  429 */     this.proxy = gameConfig.user.proxy;
/*  430 */     this.offlineDeveloperMode = gameConfig.game.offlineDeveloperMode;
/*  431 */     YggdrasilAuthenticationService authenticationService = this.offlineDeveloperMode ? YggdrasilAuthenticationService.createOffline(this.proxy) : new YggdrasilAuthenticationService(this.proxy);
/*  432 */     this.services = Services.create(authenticationService, this.gameDirectory);
/*  433 */     this.user = gameConfig.user.user;
/*  434 */     this.profileFuture = this.offlineDeveloperMode ? CompletableFuture.<ProfileResult>completedFuture(null) : CompletableFuture.<ProfileResult>supplyAsync(() -> this.services.sessionService().fetchProfile(this.user.getProfileId(), true), (Executor)Util.nonCriticalIoPool());
/*  435 */     this.userApiService = createUserApiService(authenticationService, gameConfig);
/*  436 */     this.userPropertiesFuture = CompletableFuture.supplyAsync(() -> {
/*      */           try {
/*      */             return this.userApiService.fetchProperties();
/*  439 */           } catch (AuthenticationException e) {
/*      */             LOGGER.error("Failed to fetch user properties", (Throwable)e);
/*      */             return UserApiService.OFFLINE_PROPERTIES;
/*      */           } 
/*  443 */         }(Executor)Util.nonCriticalIoPool());
/*  445 */     LOGGER.info("Setting user: {}", this.user.getName());
/*  446 */     LOGGER.debug("(Session ID is {})", this.user.getSessionId());
/*  448 */     this.demo = gameConfig.game.demo;
/*  449 */     this.allowsMultiplayer = !gameConfig.game.disableMultiplayer;
/*  450 */     this.allowsChat = !gameConfig.game.disableChat;
/*  452 */     this.singleplayerServer = null;
/*  454 */     KeybindResolver.setKeyResolver(KeyMapping::createNameSupplier);
/*  455 */     this.fixerUpper = DataFixers.getDataFixer();
/*  456 */     this.gameThread = Thread.currentThread();
/*  458 */     this.options = new Options(this, this.gameDirectory);
/*  459 */     this.debugEntries = new DebugScreenEntryList(this.gameDirectory, this.fixerUpper);
/*  461 */     boolean lastStartWasClean = this.options.startedCleanly;
/*  462 */     this.options.startedCleanly = false;
/*  463 */     this.options.save();
/*  465 */     this.running = true;
/*  466 */     this.tutorial = new Tutorial(this, this.options);
/*  467 */     this.hotbarManager = new HotbarManager(gameDirPath, this.fixerUpper);
/*  469 */     LOGGER.info("Backend library: {}", RenderSystem.getBackendDescription());
/*  471 */     DisplayData displayData = gameConfig.display;
/*  472 */     if (this.options.overrideHeight > 0 && this.options.overrideWidth > 0)
/*  473 */       displayData = gameConfig.display.withSize(this.options.overrideWidth, this.options.overrideHeight); 
/*  475 */     if (!lastStartWasClean) {
/*  476 */       displayData = displayData.withFullscreen(false);
/*  477 */       this.options.fullscreenVideoModeString = null;
/*  478 */       LOGGER.warn("Detected unexpected shutdown during last game startup: resetting fullscreen mode");
/*      */     } 
/*  481 */     Util.setTimeSource(RenderSystem.initBackendSystem());
/*  483 */     this.monitorManager = new MonitorManager();
/*  485 */     StringBuilder errorMsgBuilder = new StringBuilder("No supported graphics backend was found.");
/*  486 */     Window windowCandidate = null;
/*  487 */     GpuDevice device = null;
/*  488 */     PreferredGraphicsApi forcedGraphicsApi = gameConfig.game.forcedGraphicsApi;
/*  489 */     if (forcedGraphicsApi != null) {
/*  490 */       LOGGER.warn("Graphics backend forced to {} by launch argument, in-game preferred graphics backend setting is ignored", forcedGraphicsApi.getSerializedName());
/*  491 */     } else if (!lastStartWasClean) {
/*  492 */       if (this.options.preferredGraphicsBackend().get() == PreferredGraphicsApi.VULKAN) {
/*  493 */         LOGGER.warn("Detected unexpected shutdown during last game startup: resetting preferred graphics API to Default");
/*  494 */         this.options.preferredGraphicsBackend().set(PreferredGraphicsApi.DEFAULT);
/*  495 */         this.options.save();
/*  496 */       } else if (this.options.preferredGraphicsBackend().get() == PreferredGraphicsApi.DEFAULT) {
/*  497 */         LOGGER.warn("Detected unexpected shutdown during last game startup: forcing preferred graphics API to OpenGL");
/*  498 */         this.options.preferredGraphicsBackend().set(PreferredGraphicsApi.OPENGL);
/*      */       } 
/*      */     } 
/*  503 */     PreferredGraphicsApi preferredGraphicsBackend = (forcedGraphicsApi == null) ? this.options.preferredGraphicsBackend().get() : forcedGraphicsApi;
/*  504 */     if (preferredGraphicsBackend == PreferredGraphicsApi.DEFAULT)
/*  505 */       this.backendCreationException = VulkanBackend.checkBackendAvailable(); 
/*  507 */     String initialWindowTitle = createTitle();
/*  508 */     for (GpuBackend backend : preferredGraphicsBackend.getBackendsToTry()) {
/*      */       try {
/*  510 */         GLFW.glfwDefaultWindowHints();
/*  512 */         GLFW.glfwWindowHint(131088, GLX.glfwBool(!((Boolean)this.options.exclusiveFullscreen().get()).booleanValue()));
/*  513 */         windowCandidate = new Window(this, displayData, this.options.fullscreenVideoModeString, ((Boolean)this.options.exclusiveFullscreen().get()).booleanValue(), initialWindowTitle, this.monitorManager, backend);
/*  515 */         device = windowCandidate.backend().createDevice(windowCandidate.handle(), (id, type) -> getShaderManager().getShader(id, type), new GpuDebugOptions(this.options.glDebugVerbosity, SharedConstants.DEBUG_SYNCHRONOUS_GL_LOGS, gameConfig.game.renderDebugLabels, gameConfig.game.vulkanValidation), this::loadCriticalShaders);
/*  519 */         DeviceInfo deviceInfo = device.getDeviceInfo();
/*  520 */         int maxSize = deviceInfo.limits().maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM);
/*  521 */         GLFW.glfwSetWindowSizeLimits(windowCandidate.handle(), -1, -1, maxSize, maxSize);
/*  523 */         RenderSystem.initRenderer(device);
/*  524 */         LOGGER.info("Using graphics backend {}, using drivers: {}", deviceInfo.backendName(), deviceInfo.driverInfo());
/*  525 */         LOGGER.info("Using graphics device: {} ({})", deviceInfo.name(), deviceInfo.vendorName());
/*  526 */         LOGGER.info("Using graphics device extensions: {}", String.join(", ", deviceInfo.underlyingExtensions()));
/*      */         break;
/*  528 */       } catch (BackendCreationException exception) {
/*  529 */         LOGGER.error("Failed to create backend {}", backend.getName(), exception);
/*  530 */         errorMsgBuilder.append("\n\n- Tried ").append(backend.getName()).append(": \n  ").append(exception.getMessage());
/*  531 */         if (this.backendCreationException == null || exception.getReason() != BackendCreationException.Reason.OPENGL_MISSING)
/*  532 */           this.backendCreationException = exception; 
/*  535 */         if (windowCandidate != null) {
/*  536 */           windowCandidate.close();
/*  537 */           windowCandidate = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*  542 */     if (windowCandidate == null) {
/*  543 */       String errorMsg = errorMsgBuilder.toString();
/*  544 */       MessageBox.error(errorMsg);
/*  545 */       throw new Window.WindowInitFailed(errorMsg);
/*      */     } 
/*  548 */     this.window = windowCandidate;
/*  549 */     this.windowSurface = device.createSurface(this.window.handle());
/*  551 */     this.textInputManager = new TextInputManager(this.window);
/*  553 */     this.window.setWindowCloseCallback(new Runnable() {
/*      */           private boolean threadStarted;
/*      */           
/*      */           public void run() {
/*  558 */             if (!this.threadStarted) {
/*  559 */               this.threadStarted = true;
/*  561 */               ClientShutdownWatchdog.startShutdownWatchdog("window close callback", false, Minecraft.this, gameConfig, Minecraft.this.gameThread.threadId());
/*      */             } 
/*      */           }
/*      */         });
/*  566 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS);
/*      */     try {
/*  569 */       this.window.setIcon((PackResources)this.vanillaPackResources, SharedConstants.getCurrentVersion().stable() ? IconSet.RELEASE : IconSet.SNAPSHOT);
/*  570 */     } catch (IOException e) {
/*  571 */       LOGGER.error("Couldn't set icon", e);
/*      */     } 
/*  574 */     this.mouseHandler = new MouseHandler(this);
/*  575 */     this.mouseHandler.setup(this.window);
/*  577 */     this.keyboardHandler = new KeyboardHandler(this);
/*  578 */     this.keyboardHandler.setup(this.window);
/*  582 */     this.options.applyGraphicsPreset(this.options.graphicsPreset().get());
/*  584 */     this.resourceManager = new ReloadableResourceManager(PackType.CLIENT_RESOURCES);
/*  586 */     this.resourcePackRepository.reload();
/*  587 */     this.options.loadSelectedResourcePacks(this.resourcePackRepository);
/*  589 */     this.languageManager = new LanguageManager(this.options.languageCode, paramClientLanguage -> {
/*      */           if (this.player != null)
/*      */             this.player.connection.updateSearchTrees(); 
/*      */         });
/*  594 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.languageManager);
/*  596 */     this.textureManager = new TextureManager((ResourceManager)this.resourceManager);
/*  597 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.textureManager);
/*  599 */     this.shaderManager = new ShaderManager(this.textureManager, this::triggerResourcePackRecovery);
/*  600 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.shaderManager);
/*  602 */     SkinTextureDownloader skinTextureDownloader = new SkinTextureDownloader(this.proxy, this.textureManager, (Executor)this);
/*  603 */     this.skinManager = new SkinManager(assetsDirectory.toPath().resolve("skins"), this.services, skinTextureDownloader, (Executor)this);
/*  605 */     this.levelSource = new LevelStorageSource(gameDirPath.resolve("saves"), gameDirPath.resolve("backups"), this.directoryValidator, this.fixerUpper);
/*  607 */     this.musicManager = new MusicManager(this);
/*  608 */     this.soundManager = new SoundManager(this.options);
/*  609 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.soundManager);
/*  611 */     this.atlasManager = new AtlasManager(this.textureManager, ((Integer)this.options.mipmapLevels().get()).intValue());
/*  612 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.atlasManager);
/*  614 */     LocalPlayerResolver localPlayerResolver = new LocalPlayerResolver(this, this.services.profileResolver());
/*  615 */     this.playerSkinRenderCache = new PlayerSkinRenderCache(this.textureManager, this.skinManager, (ProfileResolver)localPlayerResolver);
/*  616 */     ClientMannequin.registerOverrides(this.playerSkinRenderCache);
/*  618 */     this.fontManager = new FontManager(this.textureManager, this.atlasManager, this.playerSkinRenderCache);
/*  619 */     this.font = this.fontManager.createFont();
/*  620 */     this.fontFilterFishy = this.fontManager.createFontFilterFishy();
/*  621 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.fontManager);
/*  622 */     updateFontOptions();
/*  624 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new GrassColorReloadListener());
/*  625 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new FoliageColorReloadListener());
/*  626 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new DryFoliageColorReloadListener());
/*  628 */     this.window.setErrorSection("Startup");
/*  630 */     RenderSystem.setupDefaultState();
/*  632 */     this.window.setErrorSection("Post startup");
/*  634 */     this.blockColors = BlockColors.createDefault();
/*  636 */     this.modelManager = new ModelManager(this.blockColors, this.atlasManager, this.playerSkinRenderCache);
/*  637 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.modelManager);
/*  639 */     EquipmentAssetManager equipmentAssets = new EquipmentAssetManager();
/*  640 */     this.resourceManager.registerReloadListener((PreparableReloadListener)equipmentAssets);
/*  642 */     BlockModelResolver blockModelResolver = new BlockModelResolver(this.modelManager);
/*  643 */     this.itemModelResolver = new ItemModelResolver(this.modelManager);
/*  645 */     this.mapTextureManager = new MapTextureManager(this.textureManager);
/*  647 */     this.mapRenderer = new MapRenderer(this.atlasManager, this.mapTextureManager);
/*  649 */     FriendsService friendsService = authenticationService.createFriendsService(this.user.getAccessToken());
/*  650 */     this.remoteFriendListUpdateHandler = new RemoteFriendListUpdateHandler(friendsService, this);
/*  651 */     this.playerSocialManager = new PlayerSocialManager(this, this.userApiService, friendsService, this.remoteFriendListUpdateHandler);
/*  652 */     if (this.playerSocialManager.isFriendListEnabled())
/*  653 */       this.remoteFriendListUpdateHandler.start(); 
/*  656 */     this.entityRenderDispatcher = new EntityRenderDispatcher(this, this.textureManager, blockModelResolver, this.itemModelResolver, this.mapRenderer, this.atlasManager, this.font, this.options, this.modelManager.entityModels(), equipmentAssets, this.playerSkinRenderCache);
/*  657 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.entityRenderDispatcher);
/*  659 */     this.blockEntityRenderDispatcher = new BlockEntityRenderDispatcher(this.font, this.modelManager.entityModels(), blockModelResolver, this.itemModelResolver, this.entityRenderDispatcher, (SpriteGetter)this.atlasManager, this.playerSkinRenderCache);
/*  660 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.blockEntityRenderDispatcher);
/*  662 */     ParticleResources particleResources = new ParticleResources();
/*  663 */     this.resourceManager.registerReloadListener((PreparableReloadListener)particleResources);
/*  664 */     this.particleEngine = new ParticleEngine(this.level, particleResources);
/*  665 */     Objects.requireNonNull(this.particleEngine);
/*  665 */     particleResources.onReload(this.particleEngine::clearParticles);
/*  667 */     this.gameRenderer = new GameRenderer(this, this.entityRenderDispatcher.getItemInHandRenderer(), this.modelManager);
/*  670 */     WindowRenderState windowRenderState = (this.gameRenderer.gameRenderState()).windowRenderState;
/*  671 */     windowRenderState.width = this.window.getWidth();
/*  672 */     windowRenderState.height = this.window.getHeight();
/*  674 */     this.levelRenderer = new LevelRenderer(this.entityRenderDispatcher, this.blockEntityRenderDispatcher, this.modelManager, this.textureManager, this.atlasManager, this.shaderManager, this.gameRenderer, this.window.getWidth(), this.window.getHeight());
/*  675 */     this.levelExtractor = new LevelExtractor(this, (this.gameRenderer.gameRenderState()).levelRenderState, this.levelRenderer);
/*  676 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.levelExtractor);
/*  677 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.levelRenderer.cloudRenderer());
/*  679 */     this.gpuWarnlistManager = new GpuWarnlistManager();
/*  680 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.gpuWarnlistManager);
/*  682 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.regionalCompliancies);
/*  684 */     this.gui = new Gui(this, new Hud(this), (this.gameRenderer.gameRenderState()).guiRenderState);
/*  685 */     this.gui.registerReloadListeners(this.resourceManager);
/*  687 */     RealmsClient realmsClient = RealmsClient.getOrCreate(this);
/*  689 */     this.realmsDataFetcher = new RealmsDataFetcher(realmsClient);
/*  691 */     RenderSystem.setErrorCallback(this::onFullscreenError);
/*  693 */     RenderTarget mainRenderTarget = this.gameRenderer.mainRenderTarget();
/*  694 */     if (mainRenderTarget.width != this.window.getWidth() || mainRenderTarget.height != this.window.getHeight()) {
/*  696 */       StringBuilder message = new StringBuilder("Recovering from unsupported resolution (" + this.window.getWidth() + "x" + this.window.getHeight() + ").\nPlease make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).");
/*      */       try {
/*  699 */         List<String> messages = device.getLastDebugMessages();
/*  700 */         if (!messages.isEmpty())
/*  701 */           message.append("\n\nReported GL debug messages:\n").append(String.join("\n", (Iterable)messages)); 
/*  703 */       } catch (Throwable throwable) {}
/*  706 */       this.window.setWindowed(mainRenderTarget.width, mainRenderTarget.height);
/*  707 */       MessageBox.error(message.toString());
/*  708 */     } else if (((Boolean)this.options.fullscreen().get()).booleanValue() && !this.window.isFullscreen()) {
/*  709 */       if (lastStartWasClean) {
/*  710 */         this.window.toggleFullScreen();
/*  711 */         this.options.fullscreen().set(Boolean.valueOf(this.window.isFullscreen()));
/*      */       } else {
/*  714 */         this.options.fullscreen().set(Boolean.valueOf(false));
/*      */       } 
/*      */     } 
/*  718 */     this.window.updateRawMouseInput(((Boolean)this.options.rawMouseInput().get()).booleanValue());
/*  719 */     this.window.setAllowCursorChanges(((Boolean)this.options.allowCursorChanges().get()).booleanValue());
/*  720 */     this.window.setDefaultErrorCallback();
/*  725 */     GLFW.glfwShowWindow(windowCandidate.handle());
/*  726 */     resizeGui();
/*  728 */     loadCriticalShaders();
/*  730 */     this.telemetryManager = new ClientTelemetryManager(this, this.userApiService, this.user);
/*  731 */     this.profileKeyPairManager = this.offlineDeveloperMode ? ProfileKeyPairManager.EMPTY_KEY_MANAGER : ProfileKeyPairManager.create(this.userApiService, this.user, gameDirPath);
/*  733 */     this.narrator = new GameNarrator(this);
/*  734 */     this.narrator.checkStatus((this.options.narrator().get() != NarratorStatus.OFF));
/*  736 */     this.reportingContext = ReportingContext.create(ReportEnvironment.local(), this.userApiService);
/*  738 */     TitleScreen.registerTextures(this.textureManager);
/*  739 */     LoadingOverlay.registerTextures(this.textureManager);
/*  740 */     this.gameRenderer.registerPanoramaTextures(this.textureManager);
/*  741 */     this.timerQuery = new TimerQuery();
/*  745 */     this.gui.setScreen((Screen)new GenericMessageScreen((Component)Component.translatable("gui.loadingMinecraft")));
/*  747 */     List<PackResources> packs = this.resourcePackRepository.openAllSelected();
/*  748 */     this.reloadStateTracker.startReload(ResourceLoadStateTracker.ReloadReason.INITIAL, packs);
/*  749 */     ReloadInstance reloadInstance = this.resourceManager.createReload(Util.backgroundExecutor().forName("resourceLoad"), (Executor)this, RESOURCE_RELOAD_INITIAL_TASK, packs);
/*  750 */     GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS);
/*  752 */     GameLoadCookie loadCookie = new GameLoadCookie(realmsClient, gameConfig.quickPlay);
/*  753 */     this.gui.setOverlay((Overlay)new LoadingOverlay(this, reloadInstance, maybeT -> Util.ifElse(maybeT, (), ()), false));
/*  762 */     this.quickPlayLog = QuickPlayLog.of(gameConfig.quickPlay.logPath());
/*  763 */     this.framerateLimitTracker = new FramerateLimitTracker(this.options, this);
/*  765 */     Objects.requireNonNull(this.framerateLimitTracker);
/*  765 */     this.fpsPieProfiler = new ContinuousProfiler((LongSupplier)Util.timeSource(), () -> this.fpsPieRenderTicks, this.framerateLimitTracker::isHeavilyThrottled);
/*  767 */     if (TracyClient.isAvailable() && gameConfig.game.captureTracyImages) {
/*  768 */       this.tracyFrameCapture = new TracyFrameCapture();
/*      */     } else {
/*  770 */       this.tracyFrameCapture = null;
/*      */     } 
/*  772 */     this.packetProcessor = new PacketProcessor(this.gameThread);
/*  774 */     this.telemetryManager.getOutsideSessionSender().send(TelemetryEventType.GRAPHICS_CAPABILITIES, properties -> {
/*      */           properties.putIfNotNull(TelemetryProperty.BACKEND_NAME, RenderSystem.getDevice().getDeviceInfo().backendName());
/*      */           if (this.backendCreationException != null) {
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_MESSAGE, this.backendCreationException.getMessage());
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_REASON, this.backendCreationException.getReason().displayName());
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_MISSING_CAPABILITIES, String.join(",", this.backendCreationException.getMissingCapabilities()));
/*      */           } else {
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_MESSAGE, "");
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_REASON, "");
/*      */             properties.put(TelemetryProperty.BACKEND_FAILURE_MISSING_CAPABILITIES, "");
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean hasShiftDown() {
/*  789 */     Window window = getWindow();
/*  790 */     return (InputConstants.isKeyDown(window, 340) || InputConstants.isKeyDown(window, 344));
/*      */   }
/*      */   
/*      */   public boolean hasControlDown() {
/*  794 */     Window window = getWindow();
/*  795 */     return (InputConstants.isKeyDown(window, 341) || InputConstants.isKeyDown(window, 345));
/*      */   }
/*      */   
/*      */   public boolean hasAltDown() {
/*  799 */     Window window = getWindow();
/*  800 */     return (InputConstants.isKeyDown(window, 342) || InputConstants.isKeyDown(window, 346));
/*      */   }
/*      */   
/*      */   private void onResourceLoadFinished(GameLoadCookie loadCookie) {
/*  804 */     if (!this.gameLoadFinished) {
/*  805 */       this.gameLoadFinished = true;
/*  806 */       onGameLoadFinished(loadCookie);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void onGameLoadFinished(GameLoadCookie cookie) {
/*  811 */     Runnable showScreen = this.gui.buildInitialScreens(cookie);
/*  813 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS);
/*  814 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS);
/*  815 */     GameLoadTimesEvent.INSTANCE.send(this.telemetryManager.getOutsideSessionSender());
/*  817 */     showScreen.run();
/*  819 */     this.options.startedCleanly = true;
/*  820 */     this.options.save();
/*      */   }
/*      */   
/*      */   public boolean isGameLoadFinished() {
/*  824 */     return this.gameLoadFinished;
/*      */   }
/*      */   
/*      */   private static boolean countryEqualsISO3(Object iso3Locale) {
/*      */     try {
/*  829 */       return Locale.getDefault().getISO3Country().equals(iso3Locale);
/*  830 */     } catch (MissingResourceException e) {
/*  831 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateTitle() {
/*  836 */     this.window.setTitle(createTitle());
/*      */   }
/*      */   
/*      */   private String createTitle() {
/*  840 */     StringBuilder builder = new StringBuilder("Minecraft");
/*  841 */     if (checkModStatus().shouldReportAsModified())
/*  842 */       builder.append("*"); 
/*  844 */     builder.append(" ");
/*  845 */     builder.append(SharedConstants.getCurrentVersion().name());
/*  847 */     ClientPacketListener connection = getConnection();
/*  848 */     if (connection != null && connection.getConnection().isConnected()) {
/*  849 */       builder.append(" - ");
/*  850 */       ServerData server = getCurrentServer();
/*  851 */       if (this.singleplayerServer != null && !this.singleplayerServer.isPublished()) {
/*  852 */         builder.append(I18n.get("title.singleplayer", new Object[0]));
/*  853 */       } else if (server != null && server.isRealm()) {
/*  854 */         builder.append(I18n.get("title.multiplayer.realms", new Object[0]));
/*  855 */       } else if (this.singleplayerServer != null || (server != null && server.isLan())) {
/*  856 */         builder.append(I18n.get("title.multiplayer.lan", new Object[0]));
/*      */       } else {
/*  858 */         builder.append(I18n.get("title.multiplayer.other", new Object[0]));
/*      */       } 
/*      */     } 
/*  862 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private static UserApiService createUserApiService(YggdrasilAuthenticationService authService, GameConfig config) {
/*  866 */     if (config.game.offlineDeveloperMode)
/*  867 */       return UserApiService.OFFLINE; 
/*  869 */     return authService.createUserApiService(config.user.user.getAccessToken());
/*      */   }
/*      */   
/*      */   public boolean isOfflineDeveloperMode() {
/*  873 */     return this.offlineDeveloperMode;
/*      */   }
/*      */   
/*      */   public static ModCheck checkModStatus() {
/*  877 */     return ModCheck.identify("vanilla", ClientBrandRetriever::getClientModName, "Client", Minecraft.class);
/*      */   }
/*      */   
/*      */   private void loadCriticalShaders() {
/*  881 */     this.gameRenderer.preloadUiShader(this.vanillaPackResources.asProvider());
/*      */   }
/*      */   
/*      */   private void rollbackResourcePacks(Throwable t, GameLoadCookie loadCookie) {
/*  885 */     if (this.resourcePackRepository.getSelectedIds().size() > 1) {
/*  886 */       clearResourcePacksOnError(t, (Component)null, loadCookie);
/*      */     } else {
/*  888 */       Util.throwAsRuntime(t);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearResourcePacksOnError(Throwable t, Component message, GameLoadCookie loadCookie) {
/*  893 */     LOGGER.info("Caught error loading resourcepacks, removing all selected resourcepacks", t);
/*  894 */     this.reloadStateTracker.startRecovery(t);
/*  895 */     this.downloadedPackSource.onRecovery();
/*  897 */     this.resourcePackRepository.setSelected(Collections.emptyList());
/*  899 */     this.options.resourcePacks.clear();
/*  900 */     this.options.incompatibleResourcePacks.clear();
/*  901 */     this.options.save();
/*  903 */     reloadResourcePacks(true, loadCookie)
/*  904 */       .thenRunAsync(() -> addResourcePackLoadFailToast(message), (Executor)this);
/*      */   }
/*      */   
/*      */   private void abortResourcePackRecovery() {
/*  908 */     this.gui.setOverlay(null);
/*  909 */     if (this.level != null) {
/*  910 */       this.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
/*  911 */       disconnectWithProgressScreen();
/*      */     } 
/*  913 */     this.gui.setScreen((Screen)new TitleScreen());
/*  914 */     addResourcePackLoadFailToast((Component)null);
/*      */   }
/*      */   
/*      */   private void addResourcePackLoadFailToast(Component message) {
/*  918 */     ToastManager toastManager = this.gui.toastManager();
/*  919 */     SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.PACK_LOAD_FAILURE, (Component)Component.translatable("resourcePack.load_fail"), message);
/*      */   }
/*      */   
/*      */   public void triggerResourcePackRecovery(Exception exception) {
/*  923 */     if (!this.resourcePackRepository.isAbleToClearAnyPack()) {
/*  927 */       if (this.resourcePackRepository.getSelectedIds().size() <= 1) {
/*  929 */         LOGGER.error(LogUtils.FATAL_MARKER, exception.getMessage(), exception);
/*  930 */         emergencySaveAndCrash(new CrashReport(exception.getMessage(), exception));
/*      */       } else {
/*  932 */         schedule(this::abortResourcePackRecovery);
/*      */       } 
/*      */       return;
/*      */     } 
/*  937 */     clearResourcePacksOnError(exception, (Component)Component.translatable("resourcePack.runtime_failure"), (GameLoadCookie)null);
/*      */   }
/*      */   
/*      */   public void run() {
/*  941 */     this.gameThread = Thread.currentThread();
/*  943 */     if (Runtime.getRuntime().availableProcessors() > 4)
/*  944 */       this.gameThread.setPriority(10); 
/*  947 */     DiscontinuousFrame tickFrame = TracyClient.createDiscontinuousFrame("Client Tick");
/*      */     try {
/*  949 */       boolean oomRecovery = false;
/*  950 */       while (this.running) {
/*      */         try {
/*  952 */           SingleTickProfiler tickProfiler = SingleTickProfiler.createTickProfiler("Renderer");
/*  953 */           boolean shouldCollectFrameProfile = getDebugOverlay().showProfilerChart();
/*  954 */           Profiler.Scope ignored = Profiler.use(constructProfiler(shouldCollectFrameProfile, tickProfiler));
/*      */           try {
/*  955 */             this.metricsRecorder.startTick();
/*  956 */             tickFrame.start();
/*  958 */             RenderSystem.pollEvents();
/*  959 */             runTick(!oomRecovery);
/*  961 */             tickFrame.end();
/*  962 */             this.metricsRecorder.endTick();
/*  963 */             if (ignored != null)
/*  963 */               ignored.close(); 
/*      */           } catch (Throwable throwable) {
/*      */             if (ignored != null)
/*      */               try {
/*      */                 ignored.close();
/*      */               } catch (Throwable throwable1) {
/*      */                 throwable.addSuppressed(throwable1);
/*      */               }  
/*      */             throw throwable;
/*      */           } 
/*  964 */           finishProfilers(shouldCollectFrameProfile, tickProfiler);
/*  965 */         } catch (OutOfMemoryError e) {
/*  966 */           if (oomRecovery)
/*  968 */             throw e; 
/*  970 */           emergencySave();
/*  971 */           this.gui.setScreen((Screen)new OutOfMemoryScreen());
/*  972 */           System.gc();
/*  973 */           LOGGER.error(LogUtils.FATAL_MARKER, "Out of memory", e);
/*  974 */           oomRecovery = true;
/*      */         } 
/*      */       } 
/*  978 */     } catch (ReportedException e) {
/*  979 */       LOGGER.error(LogUtils.FATAL_MARKER, "Reported exception thrown!", (Throwable)e);
/*  980 */       emergencySaveAndCrash(e.getReport());
/*  981 */     } catch (Throwable t) {
/*  982 */       LOGGER.error(LogUtils.FATAL_MARKER, "Unreported exception thrown!", t);
/*  983 */       emergencySaveAndCrash(new CrashReport("Unexpected error", t));
/*      */     } 
/*      */   }
/*      */   
/*      */   void updateFontOptions() {
/*  989 */     this.fontManager.updateOptions(this.options);
/*      */   }
/*      */   
/*      */   private void onFullscreenError(int error, long description) {
/*  993 */     this.options.enableVsync().set(Boolean.valueOf(false));
/*  994 */     this.options.save();
/*      */   }
/*      */   
/*      */   public String getLaunchedVersion() {
/*  998 */     return this.launchedVersion;
/*      */   }
/*      */   
/*      */   public void delayCrash(CrashReport crash) {
/* 1003 */     super.delayCrash(fillReport(crash));
/*      */   }
/*      */   
/*      */   public void emergencySaveAndCrash(CrashReport partialReport) {
/* 1007 */     MemoryReserve.release();
/* 1008 */     CrashReport finalReport = fillReport(partialReport);
/* 1009 */     int exitCode = saveReportAndShutdownSoundManager(this, this.gameDirectory, finalReport, -1);
/* 1010 */     emergencySave();
/* 1011 */     System.exit(exitCode);
/*      */   }
/*      */   
/*      */   public static void saveReport(File gameDirectory, CrashReport crash) {
/* 1015 */     saveReport(gameDirectory, crash, -1);
/*      */   }
/*      */   
/*      */   public static int saveReport(File gameDirectory, CrashReport crash, int reportExitCode) {
/* 1020 */     Path crashDir = gameDirectory.toPath().resolve("crash-reports");
/* 1021 */     Path crashFile = crashDir.resolve("crash-" + Util.getFilenameFormattedDateTime() + "-client.txt");
/*      */     try {
/* 1024 */       Bootstrap.realStdoutPrintln(crash.getFriendlyReport(ReportType.CRASH));
/* 1025 */     } catch (Throwable t) {
/* 1026 */       LOGGER.error("Failed to print exception", t);
/*      */     } 
/* 1030 */     LOGGER.debug("Disabling console - remaining logs will be available only in log file");
/*      */     try {
/* 1034 */       if (crash.getSaveFile() != null) {
/* 1035 */         Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + String.valueOf(crash.getSaveFile().toAbsolutePath()));
/* 1036 */         return reportExitCode;
/*      */       } 
/* 1037 */       if (crash.saveToFile(crashFile, ReportType.CRASH)) {
/* 1038 */         Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + String.valueOf(crashFile.toAbsolutePath()));
/* 1039 */         return reportExitCode;
/*      */       } 
/* 1041 */       Bootstrap.realStdoutPrintln("#@?@# Game crashed! Crash report could not be saved. #@?@#");
/* 1042 */       return -2;
/*      */     } finally {
/* 1045 */       Bootstrap.shutdownStdout();
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void crash(Minecraft minecraft, File gameDirectory, CrashReport crash, int exitCode) {
/* 1050 */     int actualExitCode = saveReportAndShutdownSoundManager(minecraft, gameDirectory, crash, exitCode);
/* 1051 */     System.exit(actualExitCode);
/*      */   }
/*      */   
/*      */   public static int saveReportAndShutdownSoundManager(Minecraft minecraft, File gameDirectory, CrashReport crash, int exitCode) {
/* 1055 */     int actualExitCode = saveReport(gameDirectory, crash, exitCode);
/* 1057 */     if (minecraft != null)
/* 1059 */       minecraft.soundManager.emergencyShutdown(); 
/* 1061 */     return actualExitCode;
/*      */   }
/*      */   
/*      */   public boolean isEnforceUnicode() {
/* 1065 */     return ((Boolean)this.options.forceUnicodeFont().get()).booleanValue();
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> reloadResourcePacks() {
/* 1069 */     return reloadResourcePacks(false, (GameLoadCookie)null);
/*      */   }
/*      */   
/*      */   private CompletableFuture<Void> reloadResourcePacks(boolean isRecovery, GameLoadCookie loadCookie) {
/* 1075 */     if (this.pendingReload != null)
/* 1076 */       return this.pendingReload; 
/* 1078 */     CompletableFuture<Void> result = new CompletableFuture<>();
/* 1079 */     if (!isRecovery && this.gui.overlay() instanceof LoadingOverlay) {
/* 1080 */       this.pendingReload = result;
/* 1081 */       return result;
/*      */     } 
/* 1085 */     this.resourcePackRepository.reload();
/* 1086 */     List<PackResources> packs = this.resourcePackRepository.openAllSelected();
/* 1087 */     if (!isRecovery)
/* 1088 */       this.reloadStateTracker.startReload(ResourceLoadStateTracker.ReloadReason.MANUAL, packs); 
/* 1092 */     this.gui.setOverlay((Overlay)new LoadingOverlay(this, this.resourceManager.createReload(Util.backgroundExecutor().forName("resourceLoad"), (Executor)this, RESOURCE_RELOAD_INITIAL_TASK, packs), maybeT -> Util.ifElse(maybeT, (), ()), !isRecovery));
/* 1109 */     return result;
/*      */   }
/*      */   
/*      */   private void selfTest() {
/* 1113 */     boolean error = false;
/* 1116 */     BlockStateModelSet blockModelSet = getModelManager().getBlockStateModelSet();
/* 1117 */     BlockStateModel missingModel = blockModelSet.missingModel();
/* 1119 */     for (Block block : BuiltInRegistries.BLOCK) {
/* 1120 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) {
/* 1120 */         BlockState state = unmodifiableIterator.next();
/* 1121 */         if (state.getRenderShape() == RenderShape.MODEL) {
/* 1122 */           BlockStateModel model = blockModelSet.get(state);
/* 1123 */           if (model == missingModel) {
/* 1124 */             LOGGER.debug("Missing model for: {}", state);
/* 1125 */             error = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1132 */     TextureAtlasSprite missingIcon = missingModel.particleMaterial().sprite();
/* 1133 */     for (Block block : BuiltInRegistries.BLOCK) {
/* 1134 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) {
/* 1134 */         BlockState state = unmodifiableIterator.next();
/* 1135 */         TextureAtlasSprite particleIcon = blockModelSet.getParticleMaterial(state).sprite();
/* 1136 */         if (!state.isAir() && particleIcon == missingIcon)
/* 1138 */           LOGGER.debug("Missing particle icon for: {}", state); 
/*      */       } 
/*      */     } 
/* 1143 */     error |= TelemetryEventType.selfTest();
/* 1146 */     error |= MenuScreens.selfTest();
/* 1148 */     error |= EntityRenderers.validateRegistrations();
/* 1150 */     if (error)
/* 1151 */       throw new IllegalStateException("Your game data is foobar, fix the errors above!"); 
/*      */   }
/*      */   
/*      */   public LevelStorageSource getLevelSource() {
/* 1156 */     return this.levelSource;
/*      */   }
/*      */   
/*      */   public void exitWorldAndClose() {
/*      */     try {
/* 1161 */       LOGGER.info("Stopping!");
/* 1163 */       if (this.level != null)
/* 1164 */         this.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE); 
/* 1166 */       disconnectWithProgressScreen();
/* 1168 */       if (this.gui.screen() != null)
/* 1169 */         this.gui.screen().removed(); 
/*      */     } finally {
/* 1172 */       close();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void close() {
/* 1178 */     Util.shutdownTimeSource();
/*      */     try {
/* 1180 */       this.remoteFriendListUpdateHandler.close();
/* 1181 */       this.timerQuery.close();
/* 1182 */       this.telemetryManager.close();
/* 1183 */       this.regionalCompliancies.close();
/* 1184 */       this.atlasManager.close();
/* 1185 */       this.fontManager.close();
/* 1186 */       this.gameRenderer.close();
/* 1187 */       this.shaderManager.close();
/* 1188 */       this.levelRenderer.close();
/* 1189 */       this.soundManager.destroy();
/* 1190 */       this.mapTextureManager.close();
/* 1191 */       this.textureManager.close();
/* 1192 */       this.resourceManager.close();
/* 1193 */       if (this.tracyFrameCapture != null)
/* 1194 */         this.tracyFrameCapture.close(); 
/* 1196 */       this.narrator.destroy();
/* 1197 */       FreeTypeUtil.destroy();
/* 1198 */       Util.shutdownExecutors();
/* 1199 */       this.windowSurface.close();
/* 1200 */       RenderSystem.shutdownRenderer();
/* 1201 */     } catch (Throwable t) {
/* 1202 */       LOGGER.error("Shutdown failure!", t);
/* 1203 */       throw t;
/*      */     } finally {
/* 1205 */       this.window.close();
/* 1206 */       this.monitorManager.close();
/* 1207 */       GLFW.glfwTerminate();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runTick(boolean advanceGameTime) {
/* 1212 */     this.window.setErrorSection("Pre render");
/* 1214 */     if (this.window.shouldClose())
/* 1215 */       stop(); 
/* 1219 */     if (this.pendingReload != null && !(this.gui.overlay() instanceof LoadingOverlay)) {
/* 1220 */       CompletableFuture<Void> future = this.pendingReload;
/* 1221 */       this.pendingReload = null;
/* 1222 */       reloadResourcePacks().thenRun(() -> future.complete(null));
/*      */     } 
/* 1224 */     this.playerSocialManager.getPresenceHandler().tick();
/* 1226 */     int ticksToDo = advanceGameTime ? this.deltaTracker.advanceGameTime(Util.getMillis()) : 0;
/* 1228 */     ProfilerFiller profiler = Profiler.get();
/* 1230 */     if (advanceGameTime) {
/* 1231 */       Gizmos.TemporaryCollection temporaryCollection = collectPerTickGizmos();
/*      */       try {
/* 1232 */         profiler.push("scheduledPacketProcessing");
/* 1233 */         this.packetProcessor.processQueuedPackets();
/* 1234 */         profiler.popPush("scheduledExecutables");
/* 1235 */         runAllTasks();
/* 1236 */         profiler.pop();
/* 1237 */         if (temporaryCollection != null)
/* 1237 */           temporaryCollection.close(); 
/*      */       } catch (Throwable throwable) {
/*      */         if (temporaryCollection != null)
/*      */           try {
/*      */             temporaryCollection.close();
/*      */           } catch (Throwable throwable1) {
/*      */             throwable.addSuppressed(throwable1);
/*      */           }  
/*      */         throw throwable;
/*      */       } 
/* 1239 */       profiler.push("tick");
/* 1240 */       if (ticksToDo > 0 && 
/* 1241 */         isLevelRunningNormally()) {
/* 1242 */         profiler.push("textures");
/* 1243 */         this.textureManager.tick();
/* 1244 */         profiler.pop();
/*      */       } 
/* 1247 */       for (int i = 0; i < Math.min(10, ticksToDo); i++) {
/* 1248 */         profiler.incrementCounter("clientTick");
/* 1249 */         Gizmos.TemporaryCollection temporaryCollection1 = collectPerTickGizmos();
/*      */         try {
/* 1250 */           tick();
/* 1251 */           if (temporaryCollection1 != null)
/* 1251 */             temporaryCollection1.close(); 
/*      */         } catch (Throwable throwable) {
/*      */           if (temporaryCollection1 != null)
/*      */             try {
/*      */               temporaryCollection1.close();
/*      */             } catch (Throwable throwable1) {
/*      */               throwable.addSuppressed(throwable1);
/*      */             }  
/*      */           throw throwable;
/*      */         } 
/*      */       } 
/* 1253 */       if (ticksToDo > 0 && (this.level == null || this.level.tickRateManager().runsNormally()))
/* 1254 */         this.drainedLatestTickGizmos = this.perTickGizmos.drainGizmos(); 
/* 1256 */       profiler.pop();
/*      */     } 
/* 1259 */     this.window.setErrorSection("Render");
/* 1260 */     Gizmos.TemporaryCollection ignored = this.levelExtractor.collectPerFrameMainThreadGizmos();
/*      */     try {
/* 1261 */       profiler.push("sound");
/* 1262 */       this.soundManager.updateSource(this.gameRenderer.mainCamera());
/* 1263 */       profiler.popPush("mouse");
/* 1264 */       this.mouseHandler.handleAccumulatedMovement();
/* 1265 */       if (ignored != null)
/* 1265 */         ignored.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (ignored != null)
/*      */         try {
/*      */           ignored.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */     try {
/* 1268 */       profiler.popPush("frame");
/* 1269 */       renderFrame(advanceGameTime);
/* 1270 */     } catch (Exception e) {
/* 1271 */       CrashReport report = CrashReport.forThrowable(e, "Render Frame");
/* 1272 */       CrashReportCategory manualCrashDetails = report.addCategory("Render Frame Details");
/* 1273 */       NativeModuleLister.addCrashSection(manualCrashDetails);
/* 1274 */       throw new ReportedException(report);
/*      */     } finally {
/* 1276 */       profiler.pop();
/*      */     } 
/* 1279 */     this.window.setErrorSection("Post render");
/* 1281 */     boolean previouslyPaused = this.pause;
/* 1282 */     this.pause = (hasSingleplayerServer() && this.gui.isPausing() && !this.singleplayerServer.isPublished());
/* 1283 */     if (!previouslyPaused && this.pause)
/* 1284 */       this.soundManager.pauseAllExcept(new SoundSource[] { SoundSource.MUSIC, SoundSource.UI }); 
/* 1286 */     this.deltaTracker.updatePauseState(this.pause);
/* 1287 */     this.deltaTracker.updateFrozenState(!isLevelRunningNormally());
/*      */   }
/*      */   
/*      */   public void renderFrame(boolean advanceGameTime) {
/*      */     long renderStartTimer;
/*      */     boolean recordGpuUtilization;
/* 1291 */     if (this.windowSurface.isAcquired())
/*      */       return; 
/* 1296 */     ProfilerFiller profiler = Profiler.get();
/* 1300 */     Gizmos.TemporaryCollection ignored = this.levelExtractor.collectPerFrameMainThreadGizmos();
/*      */     try {
/* 1301 */       profiler.push("update window");
/* 1303 */       this.window.updateFullscreenIfChanged();
/* 1308 */       if (this.windowSurfaceNeedsReconfiguring || (this.windowSurface.isSuboptimal() && !this.surfaceIsInvalid)) {
/* 1309 */         int[] width = new int[1];
/* 1310 */         int[] height = new int[1];
/* 1311 */         GLFW.glfwGetFramebufferSize(this.window.handle(), width, height);
/* 1312 */         if (width[0] != 0 || height[0] != 0) {
/* 1313 */           GpuSurface.PresentMode presentMode = GpuSurface.PresentMode.getSupportedVsyncMode(this.windowSurface.supportedPresentModes(), ((Boolean)this.options.enableVsync().get()).booleanValue());
/* 1314 */           GpuSurface.Configuration config = new GpuSurface.Configuration(width[0], height[0], presentMode);
/*      */           try {
/* 1316 */             this.windowSurface.configure(config);
/* 1317 */             this.surfaceIsInvalid = false;
/* 1318 */           } catch (SurfaceException exception) {
/* 1319 */             LOGGER.warn("Couldn't configure surface to {}: {}", config, exception);
/* 1320 */             this.surfaceIsInvalid = true;
/*      */           } 
/*      */         } 
/* 1323 */         this.windowSurfaceNeedsReconfiguring = false;
/*      */       } 
/* 1326 */       if (!this.surfaceIsInvalid && !this.window.isMinimized())
/*      */         try {
/* 1328 */           this.windowSurface.acquireNextTexture();
/* 1329 */         } catch (SurfaceException ex) {
/* 1330 */           LOGGER.warn("Couldn't acquire next surface texture with config {}: {}", this.windowSurface.currentConfiguration(), ex);
/* 1331 */           this.surfaceIsInvalid = true;
/* 1332 */           this.windowSurfaceNeedsReconfiguring = true;
/*      */         }  
/* 1336 */       profiler.popPush("update");
/* 1338 */       this.deltaTracker.advanceRealTime(Util.getMillis());
/* 1339 */       if (this.debugEntries.isCurrentlyEnabled(DebugScreenEntries.GPU_UTILIZATION) || this.metricsRecorder.isRecording()) {
/* 1340 */         recordGpuUtilization = (this.timerQuery.getStatus() == TimerQuery.Status.NOT_RECORDING);
/* 1341 */         if (recordGpuUtilization)
/* 1342 */           this.timerQuery.beginProfile(); 
/*      */       } else {
/* 1345 */         recordGpuUtilization = false;
/* 1346 */         this.gpuUtilization = 0.0D;
/*      */       } 
/* 1348 */       renderStartTimer = Util.getNanos();
/* 1350 */       pauseIfInactive();
/* 1352 */       this.gui.update();
/* 1354 */       if (isGameLoadFinished() && advanceGameTime && this.level != null)
/* 1355 */         this.level.update(); 
/* 1357 */       this.gameRenderer.update(this.deltaTracker);
/* 1359 */       float worldPartialTicks = this.deltaTracker.getGameTimeDeltaPartialTick(false);
/* 1360 */       pick(worldPartialTicks);
/* 1362 */       profiler.popPush("extract");
/* 1364 */       (this.gameRenderer.gameRenderState()).framerateLimit = this.framerateLimitTracker.getFramerateLimit();
/* 1366 */       this.gameRenderer.extract(this.deltaTracker, advanceGameTime);
/* 1367 */       if (ignored != null)
/* 1367 */         ignored.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (ignored != null)
/*      */         try {
/*      */           ignored.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1369 */     ignored = this.levelRenderer.collectPerFrameRenderThreadGizmos();
/*      */     try {
/* 1370 */       profiler.popPush("gpuAsync");
/* 1371 */       RenderSystem.executePendingTasks();
/* 1372 */       profiler.pop();
/* 1374 */       this.gameRenderer.render(this.deltaTracker, advanceGameTime);
/* 1375 */       if (ignored != null)
/* 1375 */         ignored.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (ignored != null)
/*      */         try {
/*      */           ignored.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1377 */     profiler.push("present");
/* 1379 */     if (this.windowSurface.isAcquired()) {
/* 1380 */       GpuTextureView colorTexture = this.gameRenderer.mainRenderTarget().getColorTextureView();
/* 1381 */       if (colorTexture == null)
/* 1382 */         throw new IllegalStateException("Can't blit to screen, color texture doesn't exist yet"); 
/* 1384 */       this.windowSurface.blitFromTexture(RenderSystem.getDevice().createCommandEncoder(), colorTexture);
/*      */     } 
/* 1386 */     this.frameTimeNs = Util.getNanos() - renderStartTimer;
/* 1387 */     if (recordGpuUtilization)
/* 1388 */       this.timerQuery.endProfile(); 
/* 1391 */     profiler.popPush("swapBuffers");
/* 1392 */     if (this.tracyFrameCapture != null) {
/* 1393 */       this.tracyFrameCapture.upload();
/* 1394 */       this.tracyFrameCapture.capture(this.gameRenderer.mainRenderTarget());
/*      */     } 
/* 1397 */     RenderSystem.getDevice().createCommandEncoder().submit();
/* 1398 */     if (this.windowSurface.isAcquired())
/* 1399 */       this.windowSurface.present(); 
/* 1401 */     if (this.tracyFrameCapture != null)
/* 1402 */       this.tracyFrameCapture.endFrame(); 
/* 1404 */     RenderSystem.getDynamicUniforms().reset();
/* 1405 */     this.levelRenderer.endFrame();
/* 1406 */     profiler.popPush("frameLimiter");
/* 1408 */     int framerateLimit = (this.gameRenderer.gameRenderState()).framerateLimit;
/* 1409 */     if (framerateLimit < 260)
/* 1410 */       FramerateLimiter.limitDisplayFPS(framerateLimit); 
/* 1412 */     profiler.popPush("fpsUpdate");
/* 1413 */     this.frames++;
/* 1414 */     long currentTime = Util.getNanos();
/* 1415 */     long frameDuration = currentTime - this.lastNanoTime;
/* 1416 */     if (recordGpuUtilization)
/* 1417 */       this.savedCpuDuration = frameDuration; 
/* 1419 */     getDebugOverlay().logFrameDuration(frameDuration);
/* 1420 */     this.lastNanoTime = currentTime;
/* 1422 */     this.gpuUtilization = this.timerQuery.get() * 100.0D / this.savedCpuDuration;
/* 1424 */     while (Util.getMillis() >= this.lastTime + 1000L) {
/* 1425 */       fps = this.frames;
/* 1426 */       this.lastTime += 1000L;
/* 1427 */       this.frames = 0;
/*      */     } 
/* 1429 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void pauseIfInactive() {
/* 1433 */     if (this.window.isFocused() || !this.options.pauseOnLostFocus) {
/* 1434 */       this.lastActiveTime = Util.getMillis();
/* 1436 */     } else if (Util.getMillis() - this.lastActiveTime > 500L) {
/* 1437 */       pauseGame(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ProfilerFiller constructProfiler(boolean shouldCollectFrameProfile, SingleTickProfiler tickProfiler) {
/*      */     InactiveProfiler inactiveProfiler;
/*      */     ProfilerFiller profilerFiller;
/* 1444 */     if (!shouldCollectFrameProfile) {
/* 1445 */       this.fpsPieProfiler.disable();
/* 1446 */       if (!this.metricsRecorder.isRecording() && tickProfiler == null)
/* 1447 */         return (ProfilerFiller)InactiveProfiler.INSTANCE; 
/*      */     } 
/* 1452 */     if (shouldCollectFrameProfile) {
/* 1453 */       if (!this.fpsPieProfiler.isEnabled()) {
/* 1454 */         this.fpsPieRenderTicks = 0;
/* 1455 */         this.fpsPieProfiler.enable();
/*      */       } 
/* 1457 */       this.fpsPieRenderTicks++;
/* 1458 */       profilerFiller = this.fpsPieProfiler.getFiller();
/*      */     } else {
/* 1460 */       inactiveProfiler = InactiveProfiler.INSTANCE;
/*      */     } 
/* 1463 */     if (this.metricsRecorder.isRecording())
/* 1464 */       profilerFiller = ProfilerFiller.combine((ProfilerFiller)inactiveProfiler, this.metricsRecorder.getProfiler()); 
/* 1467 */     return SingleTickProfiler.decorateFiller(profilerFiller, tickProfiler);
/*      */   }
/*      */   
/*      */   private void finishProfilers(boolean shouldCollectFrameProfile, SingleTickProfiler tickProfiler) {
/* 1471 */     if (tickProfiler != null)
/* 1472 */       tickProfiler.endTick(); 
/* 1474 */     ProfilerPieChart profilerPieChart = getDebugOverlay().getProfilerPieChart();
/* 1475 */     if (shouldCollectFrameProfile) {
/* 1476 */       profilerPieChart.setPieChartResults(this.fpsPieProfiler.getResults());
/*      */     } else {
/* 1478 */       profilerPieChart.setPieChartResults(null);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void framebufferSizeChanged() {
/* 1484 */     invalidateSurfaceConfiguration();
/* 1485 */     resizeGui();
/*      */   }
/*      */   
/*      */   public void resizeGui() {
/* 1490 */     int guiScale = this.window.calculateScale(((Integer)this.options.guiScale().get()).intValue(), isEnforceUnicode());
/* 1491 */     this.window.setGuiScale(guiScale);
/* 1493 */     if (this.gui.screen() != null)
/* 1494 */       this.gui.screen().resize(this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight()); 
/* 1497 */     this.mouseHandler.setIgnoreFirstMove();
/*      */   }
/*      */   
/*      */   public void cursorEntered() {
/* 1502 */     this.mouseHandler.cursorEntered();
/*      */   }
/*      */   
/*      */   public int getFps() {
/* 1506 */     return fps;
/*      */   }
/*      */   
/*      */   public long getFrameTimeNs() {
/* 1510 */     return this.frameTimeNs;
/*      */   }
/*      */   
/*      */   public void sendLowDiskSpaceWarning() {
/* 1514 */     execute(() -> SystemToast.onLowDiskSpace(this));
/*      */   }
/*      */   
/*      */   private void emergencySave() {
/* 1518 */     MemoryReserve.release();
/*      */     try {
/* 1520 */       if (this.isLocalServer && this.singleplayerServer != null)
/* 1521 */         this.singleplayerServer.halt(true); 
/* 1523 */       disconnectWithSavingScreen();
/* 1524 */     } catch (Throwable throwable) {}
/* 1526 */     System.gc();
/*      */   }
/*      */   
/*      */   public boolean debugClientMetricsStart(Consumer<Component> debugFeedback) {
/*      */     Consumer<Path> whenClientMetricsRecordingFinished;
/* 1530 */     if (this.metricsRecorder.isRecording()) {
/* 1531 */       debugClientMetricsStop();
/* 1532 */       return false;
/*      */     } 
/* 1535 */     Consumer<ProfileResults> onStopped = results -> {
/*      */         if (results == EmptyProfileResults.EMPTY)
/*      */           return; 
/*      */         int ticks = results.getTickDuration();
/*      */         double durationInSeconds = results.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
/*      */         execute(());
/*      */       };
/* 1548 */     Consumer<Path> onFinished = profilePath -> {
/*      */         MutableComponent mutableComponent = Component.literal(profilePath.toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(());
/*      */         execute(());
/*      */       };
/* 1558 */     SystemReport systemReport = fillSystemReport(new SystemReport(), this, this.languageManager, this.launchedVersion, this.options);
/* 1559 */     Consumer<List<Path>> profileReports = logs -> {
/*      */         Path profilePath = archiveProfilingReport(systemReport, logs);
/*      */         onFinished.accept(profilePath);
/*      */       };
/* 1564 */     if (this.singleplayerServer == null) {
/* 1565 */       whenClientMetricsRecordingFinished = (path -> profileReports.accept(ImmutableList.of(path)));
/*      */     } else {
/* 1567 */       this.singleplayerServer.fillSystemReport(systemReport);
/* 1569 */       CompletableFuture<Path> clientMetricRecordingResult = new CompletableFuture<>();
/* 1570 */       CompletableFuture<Path> serverMetricRecordingResult = new CompletableFuture<>();
/* 1571 */       CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { clientMetricRecordingResult, serverMetricRecordingResult }).thenRunAsync(() -> profileReports.accept(ImmutableList.of(clientMetricRecordingResult.join(), serverMetricRecordingResult.join())), 
/*      */           
/* 1574 */           (Executor)Util.ioPool());
/* 1577 */       Objects.requireNonNull(serverMetricRecordingResult);
/* 1577 */       this.singleplayerServer.startRecordingMetrics(ignored -> {
/*      */           
/* 1577 */           }serverMetricRecordingResult::complete);
/* 1578 */       Objects.requireNonNull(clientMetricRecordingResult);
/* 1578 */       whenClientMetricsRecordingFinished = clientMetricRecordingResult::complete;
/*      */     } 
/* 1580 */     this.metricsRecorder = (MetricsRecorder)ActiveMetricsRecorder.createStarted((MetricsSamplerProvider)new ClientMetricsSamplersProvider(
/* 1581 */           (LongSupplier)Util.timeSource(), this.levelRenderer, this.levelExtractor), 
/* 1582 */         (LongSupplier)Util.timeSource(), 
/* 1583 */         (Executor)Util.ioPool(), new MetricsPersister("client"), results -> {
/*      */           this.metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*      */           onStopped.accept(results);
/* 1583 */         }whenClientMetricsRecordingFinished);
/* 1592 */     return true;
/*      */   }
/*      */   
/*      */   private void debugClientMetricsStop() {
/* 1596 */     this.metricsRecorder.end();
/* 1597 */     if (this.singleplayerServer != null)
/* 1598 */       this.singleplayerServer.finishRecordingMetrics(); 
/*      */   }
/*      */   
/*      */   private void debugClientMetricsCancel() {
/* 1603 */     this.metricsRecorder.cancel();
/* 1604 */     if (this.singleplayerServer != null)
/* 1605 */       this.singleplayerServer.cancelRecordingMetrics(); 
/*      */   }
/*      */   
/*      */   private Path archiveProfilingReport(SystemReport systemReport, List<Path> profilingResultPaths) {
/*      */     Path archivePath;
/*      */     String levelName;
/* 1613 */     if (isLocalServer()) {
/* 1614 */       levelName = getSingleplayerServer().getWorldData().getLevelName();
/*      */     } else {
/* 1616 */       ServerData server = getCurrentServer();
/* 1617 */       levelName = (server != null) ? server.name : "unknown";
/*      */     } 
/*      */     try {
/* 1621 */       String profilingName = String.format(Locale.ROOT, "%s-%s-%s", new Object[] { Util.getFilenameFormattedDateTime(), levelName, 
/*      */             
/* 1626 */             SharedConstants.getCurrentVersion().id() });
/* 1628 */       String zipFileName = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, profilingName, ".zip");
/* 1629 */       archivePath = MetricsPersister.PROFILING_RESULTS_DIR.resolve(zipFileName);
/* 1630 */     } catch (IOException e) {
/* 1631 */       throw new UncheckedIOException(e);
/*      */     } 
/*      */     try {
/* 1634 */       FileZipper fileZipper = new FileZipper(archivePath);
/*      */       try {
/* 1635 */         fileZipper.add(Paths.get("system.txt", new String[0]), systemReport.toLineSeparatedString());
/* 1636 */         fileZipper.add(Paths.get("client", new String[0]).resolve(this.options.getFile().getName()), this.options.dumpOptionsForReport());
/* 1637 */         Objects.requireNonNull(fileZipper);
/* 1637 */         profilingResultPaths.forEach(fileZipper::add);
/* 1638 */         fileZipper.close();
/*      */       } catch (Throwable throwable) {
/*      */         try {
/*      */           fileZipper.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         } 
/*      */         throw throwable;
/*      */       } 
/*      */     } finally {
/* 1639 */       for (Path path : profilingResultPaths) {
/*      */         try {
/* 1641 */           FileUtils.forceDelete(path.toFile());
/* 1642 */         } catch (IOException e) {
/* 1643 */           LOGGER.warn("Failed to delete temporary profiling result {}", path, e);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1648 */     return archivePath;
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1652 */     this.running = false;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/* 1656 */     return this.running;
/*      */   }
/*      */   
/*      */   public void pauseGame(boolean suppressPauseMenuIfWeReallyArePausing) {
/* 1660 */     boolean canGameReallyBePaused = (hasSingleplayerServer() && !this.singleplayerServer.isPublished());
/* 1661 */     this.gui.setPauseScreen(suppressPauseMenuIfWeReallyArePausing, canGameReallyBePaused);
/*      */   }
/*      */   
/*      */   private void continueAttack(boolean down) {
/* 1665 */     if (!down)
/* 1666 */       this.missTime = 0; 
/* 1668 */     if (this.missTime > 0 || this.player.isUsingItem())
/*      */       return; 
/* 1672 */     ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1673 */     if (heldItem.has(DataComponents.PIERCING_WEAPON))
/*      */       return; 
/* 1676 */     if (down && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
/* 1677 */       BlockHitResult blockHit = (BlockHitResult)this.hitResult;
/* 1678 */       BlockPos pos = blockHit.getBlockPos();
/* 1680 */       if (!this.level.getBlockState(pos).isAir()) {
/* 1681 */         Direction direction = blockHit.getDirection();
/* 1682 */         if (this.gameMode.continueDestroyBlock(pos, direction)) {
/* 1683 */           this.level.addBreakingBlockEffect(pos, direction);
/* 1684 */           this.player.swing(InteractionHand.MAIN_HAND);
/*      */         } 
/*      */       } 
/*      */       return;
/*      */     } 
/* 1690 */     this.gameMode.stopDestroyBlock();
/*      */   }
/*      */   
/*      */   private boolean startAttack() {
/*      */     AttackRange customItemRange;
/*      */     BlockHitResult blockHit;
/*      */     BlockPos pos;
/* 1694 */     if (this.missTime > 0)
/* 1695 */       return false; 
/* 1698 */     if (this.hitResult == null) {
/* 1699 */       LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
/* 1700 */       if (this.gameMode.hasMissTime())
/* 1701 */         this.missTime = 10; 
/* 1703 */       return false;
/*      */     } 
/* 1706 */     if (this.player.isHandsBusy())
/* 1707 */       return false; 
/* 1710 */     if (this.gameMode.isSpectator()) {
/* 1711 */       HitResult hitResult = this.hitResult;
/* 1711 */       if (hitResult instanceof EntityHitResult) {
/* 1711 */         EntityHitResult entityHitResult = (EntityHitResult)hitResult;
/* 1712 */         this.gameMode.spectate(entityHitResult.getEntity());
/*      */       } else {
/* 1714 */         this.gameMode.spectatorNoAction();
/*      */       } 
/* 1716 */       return true;
/*      */     } 
/* 1719 */     ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1720 */     if (!heldItem.isItemEnabled(this.level.enabledFeatures()))
/* 1721 */       return false; 
/* 1724 */     if (this.player.cannotAttackWithItem(heldItem, 0))
/* 1725 */       return false; 
/* 1728 */     boolean endAttack = false;
/* 1729 */     PiercingWeapon piercingWeapon = (PiercingWeapon)heldItem.get(DataComponents.PIERCING_WEAPON);
/* 1730 */     if (piercingWeapon != null) {
/* 1731 */       this.gameMode.piercingAttack(piercingWeapon);
/* 1732 */       this.player.swing(InteractionHand.MAIN_HAND);
/* 1733 */       return true;
/*      */     } 
/* 1736 */     switch (this.hitResult.getType()) {
/*      */       case ENTITY:
/* 1738 */         customItemRange = (AttackRange)heldItem.get(DataComponents.ATTACK_RANGE);
/* 1739 */         if (customItemRange == null || customItemRange.isInRange((LivingEntity)this.player, this.hitResult.getLocation()))
/* 1740 */           this.gameMode.attack((Player)this.player, ((EntityHitResult)this.hitResult).getEntity()); 
/*      */         break;
/*      */       case BLOCK:
/* 1744 */         blockHit = (BlockHitResult)this.hitResult;
/* 1745 */         pos = blockHit.getBlockPos();
/* 1747 */         if (!this.level.getBlockState(pos).isAir()) {
/* 1748 */           this.gameMode.startDestroyBlock(pos, blockHit.getDirection());
/* 1749 */           if (this.level.getBlockState(pos).isAir())
/* 1750 */             endAttack = true; 
/*      */           break;
/*      */         } 
/*      */       case MISS:
/* 1756 */         if (this.gameMode.hasMissTime())
/* 1757 */           this.missTime = 10; 
/* 1759 */         this.player.resetAttackStrengthTicker();
/*      */         break;
/*      */     } 
/* 1764 */     this.player.swing(InteractionHand.MAIN_HAND);
/* 1765 */     return endAttack;
/*      */   }
/*      */   
/*      */   private void startUseItem() {
/* 1769 */     if (this.gameMode.isDestroying())
/*      */       return; 
/* 1773 */     this.rightClickDelay = 4;
/* 1775 */     if (this.player.isHandsBusy())
/*      */       return; 
/* 1779 */     if (this.hitResult == null)
/* 1780 */       LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!"); 
/* 1783 */     for (InteractionHand hand : InteractionHand.values()) {
/* 1788 */       ItemStack heldItem = this.player.getItemInHand(hand);
/* 1789 */       if (!heldItem.isItemEnabled(this.level.enabledFeatures()))
/*      */         return; 
/* 1793 */       if (this.hitResult != null) {
/*      */         EntityHitResult entityHit;
/*      */         BlockHitResult blockHit;
/*      */         Entity entity;
/*      */         int oldCount;
/*      */         InteractionResult result;
/*      */         InteractionResult useResult;
/* 1794 */         switch (this.hitResult.getType()) {
/*      */           case ENTITY:
/* 1796 */             entityHit = (EntityHitResult)this.hitResult;
/* 1797 */             entity = entityHit.getEntity();
/* 1798 */             if (!this.level.getWorldBorder().isWithinBounds(entity.blockPosition()))
/*      */               return; 
/* 1802 */             if (!this.player.isWithinEntityInteractionRange(entity, 0.0D))
/*      */               break; 
/* 1806 */             result = this.gameMode.interact((Player)this.player, entity, entityHit, hand);
/* 1807 */             if (result instanceof InteractionResult.Success) {
/* 1807 */               InteractionResult.Success success = (InteractionResult.Success)result;
/* 1808 */               if (success.swingSource() == InteractionResult.SwingSource.CLIENT)
/* 1809 */                 this.player.swing(hand); 
/*      */               return;
/*      */             } 
/*      */             break;
/*      */           case BLOCK:
/* 1815 */             blockHit = (BlockHitResult)this.hitResult;
/* 1817 */             oldCount = heldItem.getCount();
/* 1818 */             useResult = this.gameMode.useItemOn(this.player, hand, blockHit);
/* 1819 */             if (useResult instanceof InteractionResult.Success) {
/* 1819 */               InteractionResult.Success success = (InteractionResult.Success)useResult;
/* 1820 */               if (success.swingSource() == InteractionResult.SwingSource.CLIENT) {
/* 1821 */                 this.player.swing(hand);
/* 1824 */                 if (!heldItem.isEmpty() && (heldItem.getCount() != oldCount || this.player.hasInfiniteMaterials()))
/* 1825 */                   this.gameRenderer.itemInHandRenderer.itemUsed(hand); 
/*      */               } 
/*      */               return;
/*      */             } 
/* 1830 */             if (useResult instanceof InteractionResult.Fail)
/*      */               return; 
/*      */             break;
/*      */         } 
/*      */       } 
/* 1837 */       if (!heldItem.isEmpty()) {
/* 1838 */         InteractionResult useItemResult = this.gameMode.useItem((Player)this.player, hand);
/* 1839 */         if (useItemResult instanceof InteractionResult.Success) {
/* 1839 */           InteractionResult.Success success = (InteractionResult.Success)useItemResult;
/* 1840 */           if (success.swingSource() == InteractionResult.SwingSource.CLIENT)
/* 1841 */             this.player.swing(hand); 
/* 1844 */           this.gameRenderer.itemInHandRenderer.itemUsed(hand);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public MusicManager getMusicManager() {
/* 1852 */     return this.musicManager;
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1856 */     this.clientTickCount++;
/* 1858 */     if (this.level != null && !this.pause)
/* 1859 */       this.level.tickRateManager().tick(); 
/* 1861 */     if (this.rightClickDelay > 0)
/* 1862 */       this.rightClickDelay--; 
/* 1865 */     ProfilerFiller profiler = Profiler.get();
/* 1867 */     profiler.push("gameMode");
/* 1868 */     if (!this.pause && this.level != null)
/* 1869 */       this.gameMode.tick(); 
/* 1871 */     profiler.pop();
/* 1873 */     pick(1.0F);
/* 1874 */     this.tutorial.onLookAt(this.level, this.hitResult);
/* 1876 */     profiler.push("gui");
/* 1877 */     this.textInputManager.tick();
/* 1878 */     this.gui.tick();
/* 1880 */     if (this.gui.screen() != null)
/* 1881 */       this.missTime = 10000; 
/* 1884 */     if (this.gui.overlay() == null && this.gui.screen() == null) {
/* 1885 */       profiler.popPush("Keybindings");
/* 1886 */       handleKeybinds();
/* 1888 */       if (this.missTime > 0)
/* 1889 */         this.missTime--; 
/*      */     } 
/* 1893 */     if (this.level != null) {
/* 1894 */       if (!this.pause) {
/* 1895 */         profiler.popPush("gameRenderer");
/* 1896 */         this.gameRenderer.tick();
/* 1897 */         profiler.popPush("entities");
/* 1898 */         this.level.tickEntities();
/* 1899 */         profiler.popPush("blockEntities");
/* 1900 */         this.level.tickBlockEntities();
/*      */       } 
/* 1902 */     } else if (this.gameRenderer.currentPostEffect() != null) {
/* 1903 */       this.gameRenderer.clearPostEffect();
/*      */     } 
/* 1906 */     this.musicManager.tick();
/* 1907 */     this.soundManager.tick(this.pause);
/* 1910 */     if (this.level != null) {
/* 1911 */       if (!this.pause) {
/* 1912 */         profiler.popPush("level");
/* 1913 */         if (!this.options.joinedFirstServer && isMultiplayerServer()) {
/* 1914 */           this.gui.addSocialInteractionsToast();
/* 1915 */           this.options.joinedFirstServer = true;
/* 1916 */           this.options.save();
/*      */         } 
/* 1919 */         this.tutorial.tick();
/*      */         try {
/* 1922 */           this.level.tick(() -> true);
/* 1923 */         } catch (Throwable t) {
/* 1924 */           CrashReport report = CrashReport.forThrowable(t, "Exception in world tick");
/* 1925 */           if (this.level == null) {
/* 1926 */             CrashReportCategory levelCategory = report.addCategory("Affected level");
/* 1927 */             levelCategory.setDetail("Problem", "Level is null!");
/*      */           } else {
/* 1929 */             this.level.fillReportDetails(report);
/*      */           } 
/* 1931 */           throw new ReportedException(report);
/*      */         } 
/*      */       } 
/* 1934 */       profiler.popPush("animateTick");
/* 1935 */       if (!this.pause && isLevelRunningNormally())
/* 1936 */         this.level.animateTick(this.player.getBlockX(), this.player.getBlockY(), this.player.getBlockZ()); 
/* 1938 */       profiler.popPush("particles");
/* 1939 */       if (!this.pause && isLevelRunningNormally())
/* 1940 */         this.particleEngine.tick(); 
/* 1943 */       ClientPacketListener connection = getConnection();
/* 1944 */       if (connection != null && !this.pause)
/* 1945 */         connection.send((Packet)ServerboundClientTickEndPacket.INSTANCE); 
/* 1947 */     } else if (this.pendingConnection != null) {
/* 1948 */       profiler.popPush("pendingConnection");
/* 1949 */       this.pendingConnection.tick();
/*      */     } 
/* 1952 */     profiler.popPush("keyboard");
/* 1953 */     this.keyboardHandler.tick();
/* 1955 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private boolean isLevelRunningNormally() {
/* 1959 */     return (this.level == null || this.level.tickRateManager().runsNormally());
/*      */   }
/*      */   
/*      */   public boolean isMultiplayerServer() {
/* 1963 */     return (!this.isLocalServer || (this.singleplayerServer != null && this.singleplayerServer.isPublished()));
/*      */   }
/*      */   
/*      */   private void handleKeybinds() {
/* 1967 */     while (this.options.keyTogglePerspective.consumeClick()) {
/* 1968 */       CameraType previous = this.options.getCameraType();
/* 1969 */       this.options.setCameraType(this.options.getCameraType().cycle());
/* 1970 */       if (previous.isFirstPerson() != this.options.getCameraType().isFirstPerson())
/* 1971 */         this.gameRenderer.checkEntityPostEffect(this.options.getCameraType().isFirstPerson() ? getCameraEntity() : null); 
/*      */     } 
/* 1975 */     while (this.options.keySmoothCamera.consumeClick())
/* 1976 */       this.options.smoothCamera = !this.options.smoothCamera; 
/* 1979 */     this.gui.handleKeybinds();
/* 1981 */     while (this.options.keyToggleSpectatorShaderEffects.consumeClick())
/* 1982 */       this.gameRenderer.togglePostEffect(); 
/* 1985 */     for (int i = 0; i < 9; i++) {
/* 1986 */       boolean savePressed = this.options.keySaveHotbarActivator.isDown();
/* 1987 */       boolean loadPressed = this.options.keyLoadHotbarActivator.isDown();
/* 1988 */       if (this.options.keyHotbarSlots[i].consumeClick())
/* 1989 */         if (this.player.isSpectator()) {
/* 1990 */           this.gui.hud.getSpectatorGui().onHotbarSelected(i);
/* 1991 */         } else if (this.player.hasInfiniteMaterials() && this.gui.screen() == null && (loadPressed || savePressed)) {
/* 1992 */           CreativeModeInventoryScreen.handleHotbarLoadOrSave(this, i, loadPressed, savePressed);
/*      */         } else {
/* 1994 */           this.player.getInventory().setSelectedSlot(i);
/*      */         }  
/*      */     } 
/* 1999 */     while (this.options.keyInventory.consumeClick()) {
/* 2000 */       if (this.gameMode.isServerControlledInventory()) {
/* 2001 */         this.player.sendOpenInventory();
/*      */         continue;
/*      */       } 
/* 2003 */       this.tutorial.onOpenInventory();
/* 2004 */       this.gui.setScreen((Screen)new InventoryScreen((Player)this.player));
/*      */     } 
/* 2007 */     while (this.options.keyQuickActions.consumeClick())
/* 2008 */       getQuickActionsDialog().ifPresent(dialog -> this.player.connection.showDialog(dialog, this.gui.screen())); 
/* 2010 */     while (this.options.keySwapOffhand.consumeClick()) {
/* 2011 */       if (!this.player.isSpectator())
/* 2012 */         getConnection().send((Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN)); 
/*      */     } 
/* 2015 */     while (this.options.keyDrop.consumeClick()) {
/* 2016 */       if (!this.player.isSpectator() && 
/* 2017 */         this.player.drop(hasControlDown()))
/* 2018 */         this.player.swing(InteractionHand.MAIN_HAND); 
/*      */     } 
/* 2023 */     boolean instantAttack = false;
/* 2024 */     if (this.player.isUsingItem()) {
/* 2025 */       if (!this.options.keyUse.isDown())
/* 2026 */         this.gameMode.releaseUsingItem((Player)this.player); 
/* 2029 */       while (this.options.keyAttack.consumeClick());
/* 2031 */       while (this.options.keyUse.consumeClick());
/* 2033 */       while (this.options.keyPickItem.consumeClick());
/*      */     } else {
/* 2036 */       while (this.options.keyAttack.consumeClick())
/* 2037 */         instantAttack |= startAttack(); 
/* 2039 */       while (this.options.keyUse.consumeClick())
/* 2040 */         startUseItem(); 
/* 2042 */       while (this.options.keyPickItem.consumeClick())
/* 2043 */         pickBlockOrEntity(); 
/* 2045 */       if (this.player.isSpectator())
/* 2046 */         while (this.options.keySpectatorHotbar.consumeClick())
/* 2047 */           this.gui.hud.getSpectatorGui().onHotbarActionKeyPressed();  
/*      */     } 
/* 2052 */     if (this.options.keyUse.isDown() && this.rightClickDelay == 0 && !this.player.isUsingItem())
/* 2053 */       startUseItem(); 
/* 2056 */     continueAttack((this.gui.screen() == null && !instantAttack && this.options.keyAttack.isDown() && this.mouseHandler.isMouseGrabbed()));
/*      */   }
/*      */   
/*      */   private Optional<Holder<Dialog>> getQuickActionsDialog() {
/* 2060 */     Registry<Dialog> dialogRegistry = this.player.connection.registryAccess().lookupOrThrow(Registries.DIALOG);
/* 2061 */     return dialogRegistry.get(DialogTags.QUICK_ACTIONS).flatMap(quickActions -> (quickActions.size() == 0) ? Optional.empty() : ((quickActions.size() == 1) ? Optional.of(quickActions.get(0)) : dialogRegistry.get(Dialogs.QUICK_ACTIONS)));
/*      */   }
/*      */   
/*      */   public ClientTelemetryManager getTelemetryManager() {
/* 2075 */     return this.telemetryManager;
/*      */   }
/*      */   
/*      */   public MetricsRecorder getMetricsRecorder() {
/* 2079 */     return this.metricsRecorder;
/*      */   }
/*      */   
/*      */   public double getGpuUtilization() {
/* 2083 */     return this.gpuUtilization;
/*      */   }
/*      */   
/*      */   public ProfileKeyPairManager getProfileKeyPairManager() {
/* 2087 */     return this.profileKeyPairManager;
/*      */   }
/*      */   
/*      */   public WorldOpenFlows createWorldOpenFlows() {
/* 2091 */     return new WorldOpenFlows(this, this.levelSource);
/*      */   }
/*      */   
/*      */   public void doWorldLoad(LevelStorageSource.LevelStorageAccess levelSourceAccess, PackRepository packRepository, WorldStem worldStem, Optional<GameRules> gameRules, boolean newWorld) {
/* 2095 */     disconnectWithProgressScreen();
/* 2097 */     Instant worldLoadStart = Instant.now();
/* 2099 */     LevelLoadTracker loadTracker = new LevelLoadTracker(newWorld ? 500L : 0L);
/* 2100 */     LevelLoadingScreen screen = new LevelLoadingScreen(loadTracker, LevelLoadingScreen.Reason.OTHER);
/* 2101 */     this.gui.setScreen((Screen)screen);
/* 2103 */     int chunkStatusViewRadius = Math.max(5, 3) + ChunkLevel.RADIUS_AROUND_FULL_CHUNK + 1;
/*      */     try {
/* 2106 */       levelSourceAccess.saveDataTag(worldStem.worldDataAndGenSettings().data());
/* 2108 */       LevelLoadListener loadListener = LevelLoadListener.compose((LevelLoadListener)loadTracker, (LevelLoadListener)LoggingLevelLoadListener.forSingleplayer());
/* 2109 */       this.singleplayerServer = (IntegratedServer)MinecraftServer.spin(thread -> new IntegratedServer(thread, this, levelSourceAccess, packRepository, worldStem, gameRules, this.services, loadListener));
/* 2110 */       loadTracker.setServerChunkStatusView(this.singleplayerServer.createChunkLoadStatusView(chunkStatusViewRadius));
/* 2111 */       this.isLocalServer = true;
/* 2112 */       updateReportEnvironment(ReportEnvironment.local());
/* 2113 */       this.quickPlayLog.setWorldData(QuickPlayLog.Type.SINGLEPLAYER, levelSourceAccess.getLevelId(), worldStem.worldDataAndGenSettings().data().getLevelName());
/* 2114 */     } catch (Throwable t) {
/* 2115 */       CrashReport report = CrashReport.forThrowable(t, "Starting integrated server");
/* 2116 */       CrashReportCategory category = report.addCategory("Starting integrated server");
/* 2118 */       category.setDetail("Level ID", levelSourceAccess.getLevelId());
/* 2119 */       category.setDetail("Level Name", () -> worldStem.worldDataAndGenSettings().data().getLevelName());
/* 2121 */       throw new ReportedException(report);
/*      */     } 
/* 2124 */     ProfilerFiller profiler = Profiler.get();
/* 2126 */     profiler.push("waitForServer");
/* 2128 */     long tickLengthNs = TimeUnit.SECONDS.toNanos(1L) / 60L;
/* 2129 */     while (!this.singleplayerServer.isReady() || this.gui.overlay() != null) {
/* 2130 */       long finishTime = Util.getNanos() + tickLengthNs;
/* 2131 */       screen.tick();
/* 2132 */       if (this.gui.overlay() != null)
/* 2133 */         this.gui.overlay().tick(); 
/* 2135 */       renderFrame(false);
/* 2136 */       runAllTasks();
/* 2137 */       managedBlock(() -> (Util.getNanos() > finishTime));
/*      */     } 
/* 2140 */     profiler.pop();
/* 2141 */     Duration worldLoadDuration = Duration.between(worldLoadStart, Instant.now());
/* 2143 */     SocketAddress socketAddress = this.singleplayerServer.getConnection().startMemoryChannel();
/* 2144 */     Connection connection = Connection.connectToLocalServer(socketAddress);
/* 2145 */     connection.initiateServerboundPlayConnection(socketAddress
/* 2146 */         .toString(), 0, (ClientLoginPacketListener)new ClientHandshakePacketListenerImpl(connection, this, null, null, newWorld, worldLoadDuration, paramComponent -> {
/*      */           
/* 2146 */           }loadTracker, null));
/* 2150 */     connection.send((Packet)new ServerboundHelloPacket(getUser().getName(), getUser().getProfileId()));
/* 2151 */     this.pendingConnection = connection;
/* 2152 */     getPlayerSocialManager().getPresenceHandler().tryUpdatePresence();
/*      */   }
/*      */   
/*      */   public void setLevel(ClientLevel level) {
/* 2156 */     this.level = level;
/* 2157 */     updateLevelInEngines(level);
/*      */   }
/*      */   
/*      */   public void disconnectFromWorld(Component message) {
/* 2161 */     boolean localServer = isLocalServer();
/* 2162 */     ServerData currentServer = getCurrentServer();
/* 2164 */     if (this.level != null)
/* 2166 */       this.level.disconnect(message); 
/* 2169 */     if (localServer) {
/* 2170 */       disconnectWithSavingScreen();
/*      */     } else {
/* 2172 */       disconnectWithProgressScreen();
/*      */     } 
/* 2175 */     TitleScreen titleScreen = new TitleScreen();
/* 2176 */     if (localServer) {
/* 2177 */       this.gui.setScreen((Screen)titleScreen);
/* 2178 */     } else if (currentServer != null && currentServer.isRealm()) {
/* 2179 */       this.gui.setScreen((Screen)new RealmsMainScreen((Screen)titleScreen));
/*      */     } else {
/* 2181 */       this.gui.setScreen((Screen)new JoinMultiplayerScreen((Screen)titleScreen));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void disconnectWithSavingScreen() {
/* 2186 */     disconnect((Screen)new GenericMessageScreen(Gui.SAVING_LEVEL), false);
/*      */   }
/*      */   
/*      */   public void disconnectWithProgressScreen() {
/* 2190 */     disconnectWithProgressScreen(true);
/*      */   }
/*      */   
/*      */   public void disconnectWithProgressScreen(boolean stopSound) {
/* 2194 */     disconnect((Screen)new ProgressScreen(true), false, stopSound);
/*      */   }
/*      */   
/*      */   public void disconnect(Screen screen, boolean keepResourcePacks) {
/* 2198 */     disconnect(screen, keepResourcePacks, true);
/*      */   }
/*      */   
/*      */   public void disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound) {
/* 2204 */     ClientPacketListener connection = getConnection();
/* 2205 */     if (connection != null) {
/* 2206 */       dropAllTasks();
/* 2207 */       connection.close();
/* 2208 */       if (!keepResourcePacks)
/* 2209 */         clearDownloadedResourcePacks(); 
/*      */     } 
/* 2212 */     this.playerSocialManager.stopOnlineMode();
/* 2214 */     if (this.metricsRecorder.isRecording())
/* 2215 */       debugClientMetricsCancel(); 
/* 2218 */     IntegratedServer server = this.singleplayerServer;
/* 2219 */     this.singleplayerServer = null;
/* 2221 */     this.gameRenderer.resetData();
/* 2222 */     this.gameMode = null;
/* 2224 */     this.narrator.clear();
/* 2225 */     this.gui.setClientLevelTeardownInProgress(true);
/*      */     try {
/* 2227 */       if (this.level != null)
/* 2228 */         this.gui.hud.onDisconnected(); 
/* 2231 */       this.level = null;
/* 2232 */       if (server != null) {
/* 2234 */         server.halt(false);
/* 2236 */         this.gui.setScreen((Screen)new GenericMessageScreen(Gui.SAVING_LEVEL));
/* 2237 */         ProfilerFiller profiler = Profiler.get();
/* 2238 */         profiler.push("waitForServer");
/* 2239 */         while (!server.isShutdown())
/* 2240 */           renderFrame(false); 
/* 2242 */         profiler.pop();
/*      */       } 
/* 2244 */       setScreenAndShow(screen);
/* 2245 */       this.isLocalServer = false;
/* 2246 */       updateLevelInEngines((ClientLevel)null, stopSound);
/* 2247 */       this.player = null;
/*      */     } finally {
/* 2249 */       this.gui.setClientLevelTeardownInProgress(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearDownloadedResourcePacks() {
/* 2254 */     this.downloadedPackSource.cleanupAfterDisconnect();
/* 2256 */     runAllTasks();
/*      */   }
/*      */   
/*      */   public void clearClientLevel(Screen screen) {
/* 2260 */     ClientPacketListener connection = getConnection();
/* 2261 */     if (connection != null)
/* 2262 */       connection.clearLevel(); 
/* 2265 */     if (this.metricsRecorder.isRecording())
/* 2266 */       debugClientMetricsCancel(); 
/* 2269 */     this.gameRenderer.resetData();
/* 2270 */     this.gameMode = null;
/* 2272 */     this.narrator.clear();
/* 2273 */     this.gui.setClientLevelTeardownInProgress(true);
/*      */     try {
/* 2275 */       setScreenAndShow(screen);
/* 2279 */       this.gui.hud.onDisconnected();
/* 2280 */       this.level = null;
/* 2281 */       updateLevelInEngines((ClientLevel)null);
/* 2282 */       this.player = null;
/*      */     } finally {
/* 2284 */       this.gui.setClientLevelTeardownInProgress(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setScreenAndShow(Screen screen) {
/* 2292 */     Zone ignored = Profiler.get().zone("forcedTick");
/*      */     try {
/* 2293 */       this.gui.setScreen(screen);
/* 2294 */       renderFrame(false);
/* 2295 */       if (ignored != null)
/* 2295 */         ignored.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (ignored != null)
/*      */         try {
/*      */           ignored.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateLevelInEngines(ClientLevel level) {
/* 2299 */     updateLevelInEngines(level, true);
/*      */   }
/*      */   
/*      */   private void updateLevelInEngines(ClientLevel level, boolean stopSound) {
/* 2303 */     if (stopSound)
/* 2304 */       this.soundManager.stop(); 
/* 2306 */     setCameraEntity((Entity)null);
/* 2307 */     this.pendingConnection = null;
/* 2309 */     this.levelExtractor.setLevel(level);
/* 2310 */     this.particleEngine.setLevel(level);
/* 2311 */     this.gameRenderer.setLevel(level);
/* 2312 */     updateTitle();
/*      */   }
/*      */   
/*      */   private UserApiService.UserProperties userProperties() {
/* 2316 */     return this.userPropertiesFuture.join();
/*      */   }
/*      */   
/*      */   public boolean telemetryOptInExtra() {
/* 2320 */     return (extraTelemetryAvailable() && ((Boolean)this.options.telemetryOptInExtra().get()).booleanValue());
/*      */   }
/*      */   
/*      */   public boolean extraTelemetryAvailable() {
/* 2324 */     return (allowsTelemetry() && userProperties().flag(UserApiService.UserFlag.OPTIONAL_TELEMETRY_AVAILABLE));
/*      */   }
/*      */   
/*      */   public boolean allowsTelemetry() {
/* 2328 */     if (SharedConstants.IS_RUNNING_IN_IDE && !SharedConstants.DEBUG_FORCE_TELEMETRY)
/* 2329 */       return false; 
/* 2331 */     return userProperties().flag(UserApiService.UserFlag.TELEMETRY_ENABLED);
/*      */   }
/*      */   
/*      */   public boolean allowsMultiplayer() {
/* 2335 */     return (this.allowsMultiplayer && 
/* 2336 */       userProperties().flag(UserApiService.UserFlag.SERVERS_ALLOWED) && 
/* 2337 */       multiplayerBan() == null && 
/* 2338 */       !isNameBanned());
/*      */   }
/*      */   
/*      */   public boolean allowsRealms() {
/* 2342 */     return (userProperties().flag(UserApiService.UserFlag.REALMS_ALLOWED) && multiplayerBan() == null);
/*      */   }
/*      */   
/*      */   public boolean friendsEnabled() {
/* 2346 */     return userProperties().flag(UserApiService.UserFlag.FRIENDS_ENABLED);
/*      */   }
/*      */   
/*      */   public boolean handleGlobalKeyPress(InputConstants.Key key, boolean controlDown) {
/* 2350 */     if (this.options.keyDebugModifier.isDown())
/* 2351 */       return false; 
/* 2353 */     if (this.options.keyFullscreen.matches(key)) {
/* 2354 */       toggleFullscreen();
/* 2355 */       return true;
/*      */     } 
/* 2357 */     if (this.options.keyScreenshot.matches(key)) {
/* 2358 */       Screenshot.grab(this, controlDown);
/* 2359 */       return true;
/*      */     } 
/* 2361 */     if (this.options.keyFriends.matches(key))
/* 2362 */       return toggleFriendsScreen(); 
/* 2364 */     return false;
/*      */   }
/*      */   
/*      */   private void toggleFullscreen() {
/* 2368 */     Window window = getWindow();
/* 2369 */     window.toggleFullScreen();
/* 2370 */     boolean fullscreen = window.isFullscreen();
/* 2371 */     this.options.fullscreen().set(Boolean.valueOf(fullscreen));
/* 2372 */     this.options.save();
/* 2373 */     Screen screen = this.gui.screen();
/* 2373 */     if (screen instanceof VideoSettingsScreen) {
/* 2373 */       VideoSettingsScreen videoSettingsScreen = (VideoSettingsScreen)screen;
/* 2374 */       videoSettingsScreen.updateFullscreenButton(fullscreen);
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean toggleFriendsScreen() {
/* 2379 */     if (isDemo())
/* 2380 */       return false; 
/* 2382 */     Screen current = this.gui.screen();
/* 2383 */     if (current instanceof FriendsOverlayScreen) {
/* 2383 */       FriendsOverlayScreen friends = (FriendsOverlayScreen)current;
/* 2384 */       ComponentPath focusPath = current.getCurrentFocusPath();
/* 2385 */       if (focusPath == null || !(focusPath.leafComponent() instanceof net.minecraft.client.gui.components.EditBox)) {
/* 2386 */         friends.onClose();
/* 2387 */         return true;
/*      */       } 
/*      */     } 
/* 2390 */     if (current == null || current instanceof TitleScreen || current instanceof net.minecraft.client.gui.screens.PauseScreen) {
/* 2391 */       OnlineOptionsScreen.confirmFriendsListEnabled(this, () -> this.gui.setScreen((Screen)new FriendsOverlayScreen(current)), current);
/* 2395 */       return true;
/*      */     } 
/* 2397 */     return false;
/*      */   }
/*      */   
/*      */   public boolean allowFriendRequests() {
/* 2401 */     return userProperties().flag(UserApiService.UserFlag.ACCEPT_FRIEND_INVITES);
/*      */   }
/*      */   
/*      */   public boolean allowChatOnlyWithFriend() {
/* 2405 */     return (SharedConstants.DEBUG_CHAT_FRIENDS_ONLY || userProperties().flag(UserApiService.UserFlag.CHAT_FRIENDS_ONLY));
/*      */   }
/*      */   
/*      */   public BanDetails multiplayerBan() {
/* 2409 */     return (BanDetails)userProperties().bannedScopes().get("MULTIPLAYER");
/*      */   }
/*      */   
/*      */   public boolean isNameBanned() {
/* 2413 */     ProfileResult result = this.profileFuture.getNow(null);
/* 2414 */     return (result != null && result.actions().contains(ProfileActionType.FORCED_NAME_CHANGE));
/*      */   }
/*      */   
/*      */   public boolean isBlocked(UUID uuid) {
/* 2419 */     return (!isLocalOrUnknownPlayer(uuid) && this.playerSocialManager.shouldHideMessageFrom(uuid));
/*      */   }
/*      */   
/*      */   public boolean isFriendOnlyRestricted(UUID uuid) {
/* 2423 */     if (!allowChatOnlyWithFriend())
/* 2424 */       return false; 
/* 2427 */     ClientPacketListener conn = getConnection();
/* 2428 */     if (conn != null && conn.getLocalGameProfile().id().equals(uuid))
/* 2429 */       return false; 
/* 2432 */     return (!getPlayerSocialManager().isFriendListEnabled() || !getPlayerSocialManager().isFriend(uuid));
/*      */   }
/*      */   
/*      */   private boolean isLocalOrUnknownPlayer(UUID uuid) {
/* 2437 */     if (uuid.equals(Util.NIL_UUID))
/* 2438 */       return true; 
/* 2440 */     return (this.player != null && uuid.equals(this.player.getUUID()));
/*      */   }
/*      */   
/*      */   public ChatAbilities computeChatAbilities() {
/* 2445 */     ChatAbilities.Builder builder = new ChatAbilities.Builder();
/* 2446 */     ChatVisiblity visiblityOption = this.options.chatVisibility().get();
/* 2447 */     if (visiblityOption == ChatVisiblity.HIDDEN) {
/* 2448 */       builder.addRestriction(ChatRestriction.CHAT_AND_COMMANDS_DISABLED_BY_OPTIONS);
/* 2449 */     } else if (visiblityOption == ChatVisiblity.SYSTEM) {
/* 2450 */       builder.addRestriction(ChatRestriction.CHAT_DISABLED_BY_OPTIONS);
/*      */     } 
/* 2453 */     if (isMultiplayerServer()) {
/* 2454 */       if (!this.allowsChat)
/* 2455 */         builder.addRestriction(ChatRestriction.DISABLED_BY_LAUNCHER); 
/* 2457 */       if (SharedConstants.DEBUG_CHAT_DISABLED || !userProperties().flag(UserApiService.UserFlag.CHAT_ALLOWED))
/* 2458 */         builder.addRestriction(ChatRestriction.DISABLED_BY_PROFILE); 
/*      */     } 
/* 2461 */     return builder.build();
/*      */   }
/*      */   
/*      */   public final boolean isDemo() {
/* 2465 */     return this.demo;
/*      */   }
/*      */   
/*      */   public final boolean canSwitchGameMode() {
/* 2469 */     return (this.player != null && this.gameMode != null);
/*      */   }
/*      */   
/*      */   public ClientPacketListener getConnection() {
/* 2473 */     return (this.player == null) ? null : this.player.connection;
/*      */   }
/*      */   
/*      */   private void pickBlockOrEntity() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   4: ifnull -> 20
/*      */     //   7: aload_0
/*      */     //   8: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   11: invokevirtual getType : ()Lnet/minecraft/world/phys/HitResult$Type;
/*      */     //   14: getstatic net/minecraft/world/phys/HitResult$Type.MISS : Lnet/minecraft/world/phys/HitResult$Type;
/*      */     //   17: if_acmpne -> 21
/*      */     //   20: return
/*      */     //   21: aload_0
/*      */     //   22: invokevirtual hasControlDown : ()Z
/*      */     //   25: istore_1
/*      */     //   26: aload_0
/*      */     //   27: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   30: dup
/*      */     //   31: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   34: pop
/*      */     //   35: astore_2
/*      */     //   36: iconst_0
/*      */     //   37: istore_3
/*      */     //   38: aload_2
/*      */     //   39: iload_3
/*      */     //   40: <illegal opcode> typeSwitch : (Lnet/minecraft/world/phys/HitResult;I)I
/*      */     //   45: lookupswitch default -> 116, 0 -> 72, 1 -> 94
/*      */     //   72: aload_2
/*      */     //   73: checkcast net/minecraft/world/phys/BlockHitResult
/*      */     //   76: astore #4
/*      */     //   78: aload_0
/*      */     //   79: getfield gameMode : Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*      */     //   87: iload_1
/*      */     //   88: invokevirtual handlePickItemFromBlock : (Lnet/minecraft/core/BlockPos;Z)V
/*      */     //   91: goto -> 116
/*      */     //   94: aload_2
/*      */     //   95: checkcast net/minecraft/world/phys/EntityHitResult
/*      */     //   98: astore #5
/*      */     //   100: aload_0
/*      */     //   101: getfield gameMode : Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;
/*      */     //   104: aload #5
/*      */     //   106: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   109: iload_1
/*      */     //   110: invokevirtual handlePickItemFromEntity : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   113: goto -> 116
/*      */     //   116: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2477	-> 0
/*      */     //   #2478	-> 20
/*      */     //   #2481	-> 21
/*      */     //   #2482	-> 26
/*      */     //   #2483	-> 72
/*      */     //   #2484	-> 78
/*      */     //   #2485	-> 94
/*      */     //   #2486	-> 100
/*      */     //   #2490	-> 116
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   78	16	4	blockHitResult	Lnet/minecraft/world/phys/BlockHitResult;
/*      */     //   100	16	5	entityHitResult	Lnet/minecraft/world/phys/EntityHitResult;
/*      */     //   0	117	0	this	Lnet/minecraft/client/Minecraft;
/*      */     //   26	91	1	includeData	Z
/*      */   }
/*      */   
/*      */   public CrashReport fillReport(CrashReport report) {
/* 2493 */     SystemReport systemReport = report.getSystemReport();
/*      */     try {
/* 2495 */       fillSystemReport(systemReport, this, this.languageManager, this.launchedVersion, this.options);
/* 2496 */       fillUptime(report.addCategory("Uptime"));
/* 2498 */       if (this.level != null)
/* 2499 */         this.level.fillReportDetails(report); 
/* 2502 */       if (this.singleplayerServer != null)
/* 2503 */         this.singleplayerServer.fillSystemReport(systemReport); 
/* 2506 */       this.reloadStateTracker.fillCrashReport(report);
/* 2507 */     } catch (Throwable t) {
/* 2508 */       LOGGER.error("Failed to collect details", t);
/*      */     } 
/* 2511 */     return report;
/*      */   }
/*      */   
/*      */   public static void fillReport(Minecraft minecraft, LanguageManager languageManager, String launchedVersion, Options options, CrashReport report) {
/* 2515 */     SystemReport system = report.getSystemReport();
/* 2516 */     fillSystemReport(system, minecraft, languageManager, launchedVersion, options);
/*      */   }
/*      */   
/*      */   private static String formatSeconds(double timeInSeconds) {
/* 2520 */     return String.format(Locale.ROOT, "%.3fs", new Object[] { Double.valueOf(timeInSeconds) });
/*      */   }
/*      */   
/*      */   private void fillUptime(CrashReportCategory category) {
/* 2526 */     category.setDetail("JVM uptime", () -> formatSeconds(ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D));
/* 2528 */     category.setDetail("Wall uptime", () -> formatSeconds((System.currentTimeMillis() - this.clientStartTimeMs) / 1000.0D));
/* 2530 */     category.setDetail("High-res time", () -> formatSeconds(Util.getMillis() / 1000.0D));
/* 2532 */     category.setDetail("Client ticks", () -> String.format(Locale.ROOT, "%d ticks / %.3fs", new Object[] { Long.valueOf(this.clientTickCount), Double.valueOf(this.clientTickCount / 20.0D) }));
/*      */   }
/*      */   
/*      */   private static SystemReport fillSystemReport(SystemReport systemReport, Minecraft minecraft, LanguageManager languageManager, String launchedVersion, Options options) {
/* 2536 */     systemReport.setDetail("Launched Version", () -> launchedVersion);
/* 2537 */     String launcherBrand = getLauncherBrand();
/* 2538 */     if (launcherBrand != null)
/* 2539 */       systemReport.setDetail("Launcher name", launcherBrand); 
/* 2541 */     systemReport.setDetail("Backend library", RenderSystem::getBackendDescription);
/* 2542 */     systemReport.setDetail("Window size", () -> (minecraft != null) ? ("" + minecraft.window.getWidth() + "x" + minecraft.window.getWidth()) : "<not initialized>");
/* 2543 */     systemReport.setDetail("Surface Info", () -> {
/*      */           if (minecraft == null || minecraft.windowSurface == null)
/*      */             return "<no surface>"; 
/*      */           StringBuilder builder = new StringBuilder();
/*      */           if (minecraft.windowSurface.isAcquired())
/*      */             builder.append("acquired, "); 
/*      */           if (minecraft.windowSurface.isSuboptimal())
/*      */             builder.append("suboptimal, "); 
/*      */           if (minecraft.windowSurface.currentConfiguration().isEmpty()) {
/*      */             builder.append("unconfigured");
/*      */           } else {
/*      */             GpuSurface.Configuration config = minecraft.windowSurface.currentConfiguration().get();
/*      */             builder.append(config.presentMode()).append(", ");
/*      */             builder.append(config.width()).append(" x ").append(config.height());
/*      */           } 
/*      */           return builder.toString();
/*      */         });
/* 2564 */     systemReport.setDetail("GFLW Platform", Window::getPlatform);
/* 2566 */     GpuDevice device = RenderSystem.tryGetDevice();
/* 2567 */     DeviceInfo deviceInfo = (device == null) ? null : device.getDeviceInfo();
/* 2568 */     if (deviceInfo != null) {
/* 2569 */       systemReport.setDetail("Graphics Device", deviceInfo.name());
/* 2570 */       systemReport.setDetail("Graphics Backend", deviceInfo.backendName());
/* 2571 */       systemReport.setDetail("Graphics Vendor", deviceInfo.vendorName());
/* 2572 */       systemReport.setDetail("Graphics Drivers", deviceInfo.driverInfo());
/* 2573 */       systemReport.setDetail("Graphics Device Extensions", String.join(", ", deviceInfo.underlyingExtensions()));
/* 2574 */       systemReport.setDetail("Graphics Device Type", deviceInfo.type().toString());
/* 2575 */       systemReport.setDetail("Graphics Device Features", deviceInfo.features().toString());
/*      */     } else {
/* 2577 */       systemReport.setDetail("Graphics Device", "<none>");
/*      */     } 
/* 2579 */     if (minecraft != null && minecraft.backendCreationException != null) {
/* 2580 */       BackendCreationException exception = minecraft.backendCreationException;
/* 2581 */       systemReport.setDetail("Backend failure message", exception.getMessage());
/* 2582 */       systemReport.setDetail("Backend failure reason", exception.getReason().displayName());
/* 2583 */       systemReport.setDetail("Backend failure missing capabilities", String.join(", ", exception.getMissingCapabilities()));
/*      */     } 
/* 2586 */     systemReport.setDetail("GL debug messages", () -> (device == null) ? "<no renderer available>" : (device.isDebuggingEnabled() ? String.join("\n", device.getLastDebugMessages()) : "<debugging unavailable>"));
/* 2596 */     systemReport.setDetail("Is Modded", () -> checkModStatus().fullDescription());
/* 2597 */     systemReport.setDetail("Universe", () -> (minecraft != null) ? Long.toHexString(minecraft.canary) : "404");
/* 2599 */     systemReport.setDetail("Type", "Client");
/* 2600 */     if (options != null) {
/* 2601 */       if (minecraft != null) {
/* 2602 */         String gpuWarnings = minecraft.getGpuWarnlistManager().getAllWarnings();
/* 2603 */         if (gpuWarnings != null)
/* 2604 */           systemReport.setDetail("GPU Warnings", gpuWarnings); 
/*      */       } 
/* 2607 */       systemReport.setDetail("Transparency", ((Boolean)options.improvedTransparency().get()).booleanValue() ? "shader" : "regular");
/* 2608 */       systemReport.setDetail("Render Distance", "" + options.getEffectiveRenderDistance() + "/" + options.getEffectiveRenderDistance() + " chunks");
/* 2609 */       systemReport.setDetail("Narrator config", ((NarratorStatus)options.narrator().get()).toString());
/*      */     } 
/* 2611 */     if (minecraft != null) {
/* 2612 */       systemReport.setDetail("Narrator status", () -> Boolean.valueOf(minecraft.narrator.isActive()));
/* 2613 */       systemReport.setDetail("Resource Packs", () -> PackRepository.displayPackList(minecraft.getResourcePackRepository().getSelectedPacks()));
/* 2614 */       systemReport.setDetail("Sound Cache", () -> {
/*      */             SoundBufferLibrary.DebugOutput.Counter counter = new SoundBufferLibrary.DebugOutput.Counter();
/*      */             minecraft.getSoundManager().getSoundCacheDebugStats((SoundBufferLibrary.DebugOutput)counter);
/*      */             return String.format(Locale.ROOT, "%d bytes in %d buffers", new Object[] { Long.valueOf(counter.totalSize()), Integer.valueOf(counter.totalCount()) });
/*      */           });
/*      */     } 
/* 2621 */     if (languageManager != null)
/* 2622 */       systemReport.setDetail("Current Language", () -> languageManager.getSelected()); 
/* 2624 */     systemReport.setDetail("Locale", String.valueOf(Locale.getDefault()));
/* 2625 */     systemReport.setDetail("System encoding", () -> System.getProperty("sun.jnu.encoding", "<not set>"));
/* 2626 */     systemReport.setDetail("File encoding", () -> System.getProperty("file.encoding", "<not set>"));
/* 2627 */     systemReport.setDetail("CPU", GLX::_getCpuInfo);
/* 2628 */     return systemReport;
/*      */   }
/*      */   
/*      */   public static Minecraft getInstance() {
/* 2632 */     return instance;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> delayTextureReload() {
/* 2636 */     return submit(this::reloadResourcePacks).thenCompose(result -> result);
/*      */   }
/*      */   
/*      */   public void updateReportEnvironment(ReportEnvironment environment) {
/* 2640 */     if (!this.reportingContext.matches(environment))
/* 2641 */       this.reportingContext = ReportingContext.create(environment, this.userApiService); 
/*      */   }
/*      */   
/*      */   public ServerData getCurrentServer() {
/* 2646 */     return (ServerData)Optionull.map(getConnection(), ClientPacketListener::getServerData);
/*      */   }
/*      */   
/*      */   public boolean isLocalServer() {
/* 2650 */     return this.isLocalServer;
/*      */   }
/*      */   
/*      */   public boolean hasSingleplayerServer() {
/* 2654 */     return (this.isLocalServer && this.singleplayerServer != null);
/*      */   }
/*      */   
/*      */   public IntegratedServer getSingleplayerServer() {
/* 2658 */     return this.singleplayerServer;
/*      */   }
/*      */   
/*      */   public boolean isLocalPlayer(UUID profileId) {
/* 2662 */     return profileId.equals(getUser().getProfileId());
/*      */   }
/*      */   
/*      */   public User getUser() {
/* 2666 */     return this.user;
/*      */   }
/*      */   
/*      */   public ProfileResult getProfileResult() {
/* 2670 */     return this.profileFuture.join();
/*      */   }
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 2674 */     ProfileResult profileResult = this.profileFuture.join();
/* 2675 */     if (profileResult != null)
/* 2676 */       return profileResult.profile(); 
/* 2678 */     return new GameProfile(this.user.getProfileId(), this.user.getName());
/*      */   }
/*      */   
/*      */   public Proxy getProxy() {
/* 2682 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public TextureManager getTextureManager() {
/* 2686 */     return this.textureManager;
/*      */   }
/*      */   
/*      */   public ShaderManager getShaderManager() {
/* 2690 */     return this.shaderManager;
/*      */   }
/*      */   
/*      */   public ResourceManager getResourceManager() {
/* 2694 */     return (ResourceManager)this.resourceManager;
/*      */   }
/*      */   
/*      */   public PackRepository getResourcePackRepository() {
/* 2698 */     return this.resourcePackRepository;
/*      */   }
/*      */   
/*      */   public VanillaPackResources getVanillaPackResources() {
/* 2702 */     return this.vanillaPackResources;
/*      */   }
/*      */   
/*      */   public DownloadedPackSource getDownloadedPackSource() {
/* 2706 */     return this.downloadedPackSource;
/*      */   }
/*      */   
/*      */   public Path getResourcePackDirectory() {
/* 2710 */     return this.resourcePackDirectory;
/*      */   }
/*      */   
/*      */   public LanguageManager getLanguageManager() {
/* 2714 */     return this.languageManager;
/*      */   }
/*      */   
/*      */   public boolean isPaused() {
/* 2718 */     return this.pause;
/*      */   }
/*      */   
/*      */   public GpuWarnlistManager getGpuWarnlistManager() {
/* 2722 */     return this.gpuWarnlistManager;
/*      */   }
/*      */   
/*      */   public SoundManager getSoundManager() {
/* 2726 */     return this.soundManager;
/*      */   }
/*      */   
/*      */   public Music getSituationalMusic() {
/* 2730 */     Music screenMusic = (Music)Optionull.map(this.gui.screen(), Screen::getBackgroundMusic);
/* 2731 */     if (screenMusic != null)
/* 2732 */       return screenMusic; 
/* 2734 */     Camera camera = this.gameRenderer.mainCamera();
/* 2735 */     if (this.player != null) {
/* 2736 */       Level playerLevel = this.player.level();
/* 2737 */       if (playerLevel.dimension() == Level.END && this.gui.hud.getBossOverlay().shouldPlayMusic())
/* 2738 */         return Musics.END_BOSS; 
/* 2741 */       BackgroundMusic backgroundMusic = (BackgroundMusic)camera.attributeProbe().getValue(EnvironmentAttributes.BACKGROUND_MUSIC, 1.0F);
/* 2742 */       boolean isCreative = ((this.player.getAbilities()).instabuild && (this.player.getAbilities()).mayfly);
/* 2743 */       boolean isUnderwater = this.player.isUnderWater();
/* 2744 */       return backgroundMusic.select(isCreative, isUnderwater).orElse(null);
/*      */     } 
/* 2747 */     return Musics.MENU;
/*      */   }
/*      */   
/*      */   public float getMusicVolume() {
/* 2751 */     if (this.gui.screen() != null && this.gui.screen().getBackgroundMusic() != null)
/* 2752 */       return 1.0F; 
/* 2754 */     return ((Float)this.gameRenderer.mainCamera().attributeProbe().getValue(EnvironmentAttributes.MUSIC_VOLUME, 1.0F)).floatValue();
/*      */   }
/*      */   
/*      */   public Services services() {
/* 2758 */     return this.services;
/*      */   }
/*      */   
/*      */   public SkinManager getSkinManager() {
/* 2762 */     return this.skinManager;
/*      */   }
/*      */   
/*      */   public Entity getCameraEntity() {
/* 2766 */     return this.gameRenderer.mainCamera().entity();
/*      */   }
/*      */   
/*      */   public void setCameraEntity(Entity cameraEntity) {
/* 2770 */     this.gameRenderer.mainCamera().setEntity(cameraEntity);
/* 2771 */     this.gameRenderer.checkEntityPostEffect(cameraEntity);
/*      */   }
/*      */   
/*      */   public boolean shouldEntityAppearGlowing(Entity entity) {
/* 2775 */     return (entity.isCurrentlyGlowing() || (this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isDown() && entity.is(EntityTypes.PLAYER)));
/*      */   }
/*      */   
/*      */   public Thread getRunningThread() {
/* 2780 */     return this.gameThread;
/*      */   }
/*      */   
/*      */   public Runnable wrapRunnable(Runnable runnable) {
/* 2785 */     return runnable;
/*      */   }
/*      */   
/*      */   protected boolean shouldRun(Runnable task) {
/* 2790 */     return true;
/*      */   }
/*      */   
/*      */   public EntityRenderDispatcher getEntityRenderDispatcher() {
/* 2794 */     return this.entityRenderDispatcher;
/*      */   }
/*      */   
/*      */   public BlockEntityRenderDispatcher getBlockEntityRenderDispatcher() {
/* 2798 */     return this.blockEntityRenderDispatcher;
/*      */   }
/*      */   
/*      */   public MapRenderer getMapRenderer() {
/* 2802 */     return this.mapRenderer;
/*      */   }
/*      */   
/*      */   public DataFixer getFixerUpper() {
/* 2806 */     return this.fixerUpper;
/*      */   }
/*      */   
/*      */   public DeltaTracker getDeltaTracker() {
/* 2810 */     return this.deltaTracker;
/*      */   }
/*      */   
/*      */   public BlockColors getBlockColors() {
/* 2814 */     return this.blockColors;
/*      */   }
/*      */   
/*      */   public boolean showOnlyReducedInfo() {
/* 2818 */     return ((this.player != null && this.player.isReducedDebugInfo()) || ((Boolean)this.options.reducedDebugInfo().get()).booleanValue());
/*      */   }
/*      */   
/*      */   public Tutorial getTutorial() {
/* 2822 */     return this.tutorial;
/*      */   }
/*      */   
/*      */   public boolean isWindowActive() {
/* 2826 */     return this.window.isFocused();
/*      */   }
/*      */   
/*      */   public HotbarManager getHotbarManager() {
/* 2830 */     return this.hotbarManager;
/*      */   }
/*      */   
/*      */   public ModelManager getModelManager() {
/* 2834 */     return this.modelManager;
/*      */   }
/*      */   
/*      */   public AtlasManager getAtlasManager() {
/* 2838 */     return this.atlasManager;
/*      */   }
/*      */   
/*      */   public MapTextureManager getMapTextureManager() {
/* 2842 */     return this.mapTextureManager;
/*      */   }
/*      */   
/*      */   public Component grabPanoramixScreenshot(File folder) {
/* 2846 */     int downscaleFactor = 4;
/* 2847 */     int width = 4096;
/* 2848 */     int height = 4096;
/* 2849 */     int ow = this.window.getWidth();
/* 2850 */     int oh = this.window.getHeight();
/* 2851 */     RenderTarget target = this.gameRenderer.mainRenderTarget();
/* 2853 */     float xRot = this.player.getXRot();
/* 2854 */     float yRot = this.player.getYRot();
/* 2855 */     float xRotO = this.player.xRotO;
/* 2856 */     float yRotO = this.player.yRotO;
/* 2857 */     this.gameRenderer.setRenderBlockOutline(false);
/* 2859 */     Camera camera = this.gameRenderer.mainCamera();
/*      */     try {
/* 2861 */       camera.enablePanoramicMode();
/* 2863 */       this.window.setWidth(4096);
/* 2864 */       this.window.setHeight(4096);
/* 2865 */       target.resize(4096, 4096);
/* 2866 */       for (int i = 0; i < 6; i++) {
/* 2867 */         switch (i) {
/*      */           case 0:
/* 2869 */             this.player.setYRot(yRot);
/* 2870 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 1:
/* 2873 */             this.player.setYRot((yRot + 90.0F) % 360.0F);
/* 2874 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 2:
/* 2877 */             this.player.setYRot((yRot + 180.0F) % 360.0F);
/* 2878 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 3:
/* 2881 */             this.player.setYRot((yRot - 90.0F) % 360.0F);
/* 2882 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 4:
/* 2885 */             this.player.setYRot(yRot);
/* 2886 */             this.player.setXRot(-90.0F);
/*      */             break;
/*      */           default:
/* 2890 */             this.player.setYRot(yRot);
/* 2891 */             this.player.setXRot(90.0F);
/*      */             break;
/*      */         } 
/* 2894 */         this.player.yRotO = this.player.getYRot();
/* 2895 */         this.player.xRotO = this.player.getXRot();
/* 2897 */         this.gameRenderer.update(DeltaTracker.ONE);
/* 2898 */         this.gameRenderer.extract(DeltaTracker.ONE, true);
/* 2899 */         this.gameRenderer.renderLevel(DeltaTracker.ONE);
/*      */         try {
/* 2901 */           Thread.sleep(10L);
/* 2902 */         } catch (InterruptedException interruptedException) {}
/* 2904 */         Screenshot.grab(folder, "panorama_" + i + ".png", target, 4, paramComponent -> {
/*      */             
/*      */             });
/*      */       } 
/* 2906 */       MutableComponent mutableComponent = Component.literal(folder.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.OpenFile(folder.getAbsoluteFile())));
/* 2909 */       return (Component)Component.translatable("screenshot.success", new Object[] { mutableComponent });
/* 2910 */     } catch (Exception e) {
/* 2911 */       LOGGER.error("Couldn't save image", e);
/* 2912 */       return (Component)Component.translatable("screenshot.failure", new Object[] { e.getMessage() });
/*      */     } finally {
/* 2914 */       this.player.setXRot(xRot);
/* 2915 */       this.player.setYRot(yRot);
/* 2916 */       this.player.xRotO = xRotO;
/* 2917 */       this.player.yRotO = yRotO;
/* 2919 */       this.gameRenderer.setRenderBlockOutline(true);
/* 2921 */       this.window.setWidth(ow);
/* 2922 */       this.window.setHeight(oh);
/* 2923 */       target.resize(ow, oh);
/* 2925 */       camera.disablePanoramicMode();
/*      */     } 
/*      */   }
/*      */   
/*      */   public PlayerSocialManager getPlayerSocialManager() {
/* 2930 */     return this.playerSocialManager;
/*      */   }
/*      */   
/*      */   public Window getWindow() {
/* 2934 */     return this.window;
/*      */   }
/*      */   
/*      */   public TextInputManager textInputManager() {
/* 2938 */     return this.textInputManager;
/*      */   }
/*      */   
/*      */   public void onTextInputFocusChange(GuiEventListener element, boolean isFocused) {
/* 2942 */     this.textInputManager.onTextInputFocusChange(isFocused);
/* 2943 */     if (this.gui.screen() != null)
/* 2945 */       if (isFocused) {
/* 2946 */         this.keyboardHandler.resubmitLastPreeditEvent(element);
/*      */       } else {
/* 2948 */         KeyboardHandler.submitPreeditEvent(element, null);
/*      */       }  
/*      */   }
/*      */   
/*      */   public GpuSurface windowSurface() {
/* 2954 */     return this.windowSurface;
/*      */   }
/*      */   
/*      */   public FramerateLimitTracker getFramerateLimitTracker() {
/* 2958 */     return this.framerateLimitTracker;
/*      */   }
/*      */   
/*      */   public DebugScreenOverlay getDebugOverlay() {
/* 2962 */     return this.gui.hud.getDebugOverlay();
/*      */   }
/*      */   
/*      */   public void updateMaxMipLevel(int mipmapLevels) {
/* 2966 */     this.atlasManager.updateMaxMipLevel(mipmapLevels);
/*      */   }
/*      */   
/*      */   public EntityModelSet getEntityModels() {
/* 2970 */     return this.modelManager.entityModels().get();
/*      */   }
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/* 2974 */     return userProperties().flag(UserApiService.UserFlag.PROFANITY_FILTER_ENABLED);
/*      */   }
/*      */   
/*      */   public void prepareForMultiplayer() {
/* 2978 */     this.playerSocialManager.startOnlineMode();
/* 2980 */     getProfileKeyPairManager().prepareKeyPair();
/*      */   }
/*      */   
/*      */   public InputType getLastInputType() {
/* 2984 */     return this.lastInputType;
/*      */   }
/*      */   
/*      */   public void setLastInputType(InputType lastInputType) {
/* 2988 */     this.lastInputType = lastInputType;
/*      */   }
/*      */   
/*      */   public GameNarrator getNarrator() {
/* 2992 */     return this.narrator;
/*      */   }
/*      */   
/*      */   public ReportingContext getReportingContext() {
/* 2996 */     return this.reportingContext;
/*      */   }
/*      */   
/*      */   public RealmsDataFetcher realmsDataFetcher() {
/* 3000 */     return this.realmsDataFetcher;
/*      */   }
/*      */   
/*      */   public QuickPlayLog quickPlayLog() {
/* 3004 */     return this.quickPlayLog;
/*      */   }
/*      */   
/*      */   public DirectoryValidator directoryValidator() {
/* 3008 */     return this.directoryValidator;
/*      */   }
/*      */   
/*      */   public PlayerSkinRenderCache playerSkinRenderCache() {
/* 3012 */     return this.playerSkinRenderCache;
/*      */   }
/*      */   
/*      */   private float getTickTargetMillis(float defaultTickTargetMillis) {
/* 3016 */     if (this.level != null) {
/* 3017 */       TickRateManager manager = this.level.tickRateManager();
/* 3018 */       if (manager.runsNormally())
/* 3019 */         return Math.max(defaultTickTargetMillis, manager.millisecondsPerTick()); 
/*      */     } 
/* 3022 */     return defaultTickTargetMillis;
/*      */   }
/*      */   
/*      */   public ItemModelResolver getItemModelResolver() {
/* 3026 */     return this.itemModelResolver;
/*      */   }
/*      */   
/*      */   public boolean canInterruptScreen() {
/* 3030 */     return this.gui.canInterruptScreen();
/*      */   }
/*      */   
/*      */   public void invalidateSurfaceConfiguration() {
/* 3034 */     this.windowSurfaceNeedsReconfiguring = true;
/*      */   }
/*      */   
/*      */   public static String getLauncherBrand() {
/* 3038 */     return System.getProperty("minecraft.launcher.brand");
/*      */   }
/*      */   
/*      */   public PacketProcessor packetProcessor() {
/* 3042 */     return this.packetProcessor;
/*      */   }
/*      */   
/*      */   public Gizmos.TemporaryCollection collectPerTickGizmos() {
/* 3046 */     return Gizmos.withCollector((GizmoCollector)this.perTickGizmos);
/*      */   }
/*      */   
/*      */   public Collection<SimpleGizmoCollector.GizmoInstance> getPerTickGizmos() {
/* 3050 */     return this.drainedLatestTickGizmos;
/*      */   }
/*      */   
/*      */   private void pick(float partialTicks) {
/* 3054 */     Entity cameraEntity = getCameraEntity();
/* 3055 */     if (cameraEntity == null)
/*      */       return; 
/* 3058 */     if (this.level == null || this.player == null)
/*      */       return; 
/* 3062 */     Profiler.get().push("pick");
/* 3063 */     this.hitResult = this.player.raycastHitResult(partialTicks, cameraEntity);
/* 3064 */     HitResult hitResult = this.hitResult;
/* 3064 */     EntityHitResult entityHitResult = (EntityHitResult)hitResult;
/* 3064 */     this.crosshairPickEntity = (hitResult instanceof EntityHitResult) ? entityHitResult.getEntity() : null;
/* 3065 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   public void showDebugChat(Component message) {
/* 3069 */     this.gui.hud.getChat().addClientSystemMessage(message);
/* 3070 */     getNarrator().saySystemQueued(message);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Minecraft.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */