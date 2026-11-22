/*      */ package net.minecraft.client.gui;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.Optionull;
/*      */ import net.minecraft.client.AttackIndicatorStatus;
/*      */ import net.minecraft.client.DeltaTracker;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.Options;
/*      */ import net.minecraft.client.gui.components.BossHealthOverlay;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.DebugScreenOverlay;
/*      */ import net.minecraft.client.gui.components.PlayerTabOverlay;
/*      */ import net.minecraft.client.gui.components.SubtitleOverlay;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*      */ import net.minecraft.client.gui.components.spectator.SpectatorGui;
/*      */ import net.minecraft.client.gui.contextualbar.ContextualBar;
/*      */ import net.minecraft.client.gui.contextualbar.ExperienceBar;
/*      */ import net.minecraft.client.gui.contextualbar.JumpableVehicleBar;
/*      */ import net.minecraft.client.gui.contextualbar.LocatorBar;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.renderer.Lightmap;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.resources.WaypointStyleManager;
/*      */ import net.minecraft.client.server.IntegratedServer;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.FormattedText;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.numbers.NumberFormat;
/*      */ import net.minecraft.network.chat.numbers.StyledFormat;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*      */ import net.minecraft.server.packs.resources.ReloadableResourceManager;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.food.FoodData;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.scores.DisplaySlot;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.PlayerScoreEntry;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.scores.TeamColor;
/*      */ 
/*      */ public class Hud {
/*   92 */   private static final Identifier CROSSHAIR_SPRITE = Identifier.withDefaultNamespace("hud/crosshair");
/*      */   
/*   93 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_full");
/*      */   
/*   94 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background");
/*      */   
/*   95 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress");
/*      */   
/*   96 */   private static final Identifier EFFECT_BACKGROUND_AMBIENT_SPRITE = Identifier.withDefaultNamespace("hud/effect_background_ambient");
/*      */   
/*   97 */   private static final Identifier EFFECT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/effect_background");
/*      */   
/*   98 */   private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
/*      */   
/*   99 */   private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
/*      */   
/*  100 */   private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
/*      */   
/*  101 */   private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");
/*      */   
/*  102 */   private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background");
/*      */   
/*  103 */   private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress");
/*      */   
/*  104 */   private static final Identifier ARMOR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/armor_empty");
/*      */   
/*  105 */   private static final Identifier ARMOR_HALF_SPRITE = Identifier.withDefaultNamespace("hud/armor_half");
/*      */   
/*  106 */   private static final Identifier ARMOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/armor_full");
/*      */   
/*  107 */   private static final Identifier FOOD_EMPTY_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_empty_hunger");
/*      */   
/*  108 */   private static final Identifier FOOD_HALF_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_half_hunger");
/*      */   
/*  109 */   private static final Identifier FOOD_FULL_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_full_hunger");
/*      */   
/*  110 */   private static final Identifier FOOD_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/food_empty");
/*      */   
/*  111 */   private static final Identifier FOOD_HALF_SPRITE = Identifier.withDefaultNamespace("hud/food_half");
/*      */   
/*  112 */   private static final Identifier FOOD_FULL_SPRITE = Identifier.withDefaultNamespace("hud/food_full");
/*      */   
/*  113 */   private static final Identifier AIR_SPRITE = Identifier.withDefaultNamespace("hud/air");
/*      */   
/*  114 */   private static final Identifier AIR_POPPING_SPRITE = Identifier.withDefaultNamespace("hud/air_bursting");
/*      */   
/*  115 */   private static final Identifier AIR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/air_empty");
/*      */   
/*  116 */   private static final Identifier HEART_VEHICLE_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_container");
/*      */   
/*  117 */   private static final Identifier HEART_VEHICLE_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_full");
/*      */   
/*  118 */   private static final Identifier HEART_VEHICLE_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_half");
/*      */   
/*  119 */   private static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/vignette.png");
/*      */   
/*  120 */   public static final Identifier NAUSEA_LOCATION = Identifier.withDefaultNamespace("textures/misc/nausea.png");
/*      */   
/*  121 */   private static final Identifier SPYGLASS_SCOPE_LOCATION = Identifier.withDefaultNamespace("textures/misc/spyglass_scope.png");
/*      */   
/*  122 */   private static final Identifier POWDER_SNOW_OUTLINE_LOCATION = Identifier.withDefaultNamespace("textures/misc/powder_snow_outline.png");
/*      */   
/*  124 */   private static final Comparator<PlayerScoreEntry> SCORE_DISPLAY_ORDER = Comparator.<PlayerScoreEntry, Comparable>comparing(PlayerScoreEntry::value).reversed().thenComparing(PlayerScoreEntry::owner, String.CASE_INSENSITIVE_ORDER);
/*      */   
/*  126 */   private static final Component DEMO_EXPIRED_TEXT = (Component)Component.translatable("demo.demoExpired");
/*      */   
/*  127 */   private static final Component SAVING_TEXT = (Component)Component.translatable("menu.savingLevel");
/*      */   
/*      */   private static final float MIN_CROSSHAIR_ATTACK_SPEED = 5.0F;
/*      */   
/*      */   private static final int EXPERIENCE_BAR_DISPLAY_TICKS = 100;
/*      */   
/*      */   private static final int NUM_HEARTS_PER_ROW = 10;
/*      */   
/*      */   private static final int LINE_HEIGHT = 10;
/*      */   
/*      */   private static final String SPACER = ": ";
/*      */   
/*      */   private static final float PORTAL_OVERLAY_ALPHA_MIN = 0.2F;
/*      */   
/*      */   private static final int HEART_SIZE = 9;
/*      */   
/*      */   private static final int HEART_SEPARATION = 8;
/*      */   
/*      */   private static final int NUM_AIR_BUBBLES = 10;
/*      */   
/*      */   private static final int AIR_BUBBLE_SIZE = 9;
/*      */   
/*      */   private static final int AIR_BUBBLE_SEPERATION = 8;
/*      */   
/*      */   private static final int AIR_BUBBLE_POPPING_DURATION = 2;
/*      */   
/*      */   private static final int EMPTY_AIR_BUBBLE_DELAY_DURATION = 1;
/*      */   
/*      */   private static final float AIR_BUBBLE_POP_SOUND_VOLUME_BASE = 0.5F;
/*      */   
/*      */   private static final float AIR_BUBBLE_POP_SOUND_VOLUME_INCREMENT = 0.1F;
/*      */   
/*      */   private static final float AIR_BUBBLE_POP_SOUND_PITCH_BASE = 1.0F;
/*      */   
/*      */   private static final float AIR_BUBBLE_POP_SOUND_PITCH_INCREMENT = 0.1F;
/*      */   
/*      */   private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_VOLUME_INCREASE = 3;
/*      */   
/*      */   private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_PITCH_INCREASE = 5;
/*      */   
/*      */   private static final float AUTOSAVE_FADE_SPEED_FACTOR = 0.2F;
/*      */   
/*      */   private static final int SAVING_INDICATOR_WIDTH_PADDING_RIGHT = 5;
/*      */   
/*      */   private static final int SAVING_INDICATOR_HEIGHT_PADDING_BOTTOM = 5;
/*      */   
/*  155 */   private final RandomSource random = RandomSource.create();
/*      */   
/*      */   private final Minecraft minecraft;
/*      */   
/*      */   private final ChatComponent chat;
/*      */   
/*  158 */   private final WaypointStyleManager waypointStyles = new WaypointStyleManager();
/*      */   
/*      */   private boolean isHidden;
/*      */   
/*      */   private int tickCount;
/*      */   
/*      */   private Component overlayMessageString;
/*      */   
/*      */   private int overlayMessageTime;
/*      */   
/*      */   private boolean animateOverlayMessageColor;
/*      */   
/*  168 */   public float vignetteBrightness = 1.0F;
/*      */   
/*      */   private int toolHighlightTimer;
/*      */   
/*  171 */   private ItemStack lastToolHighlight = ItemStack.EMPTY;
/*      */   
/*      */   private final DebugScreenOverlay debugOverlay;
/*      */   
/*      */   private final SubtitleOverlay subtitleOverlay;
/*      */   
/*      */   private final SpectatorGui spectatorGui;
/*      */   
/*      */   private final PlayerTabOverlay tabList;
/*      */   
/*      */   private final BossHealthOverlay bossOverlay;
/*      */   
/*      */   private int titleTime;
/*      */   
/*      */   private Component title;
/*      */   
/*      */   private Component subtitle;
/*      */   
/*      */   private int titleFadeInTime;
/*      */   
/*      */   private int titleStayTime;
/*      */   
/*      */   private int titleFadeOutTime;
/*      */   
/*      */   private int lastHealth;
/*      */   
/*      */   private int displayHealth;
/*      */   
/*      */   private long lastHealthTime;
/*      */   
/*      */   private long healthBlinkTime;
/*      */   
/*      */   private int lastBubblePopSoundPlayed;
/*      */   
/*      */   private Runnable deferredSubtitles;
/*      */   
/*      */   private float autosaveIndicatorValue;
/*      */   
/*      */   private float lastAutosaveIndicatorValue;
/*      */   
/*  195 */   private Pair<ContextualInfo, ContextualBar> contextualInfoBar = Pair.of(ContextualInfo.EMPTY, ContextualBar.EMPTY);
/*      */   
/*      */   private final Map<ContextualInfo, Supplier<ContextualBar>> contextualInfoBars;
/*      */   
/*      */   private float scopeScale;
/*      */   
/*      */   public Hud(Minecraft minecraft) {
/*  199 */     this.minecraft = minecraft;
/*  200 */     this.debugOverlay = new DebugScreenOverlay(minecraft);
/*  201 */     this.spectatorGui = new SpectatorGui(minecraft);
/*  202 */     this.chat = new ChatComponent(minecraft);
/*  203 */     this.tabList = new PlayerTabOverlay(minecraft, this);
/*  204 */     this.bossOverlay = new BossHealthOverlay(minecraft);
/*  205 */     this.subtitleOverlay = new SubtitleOverlay(minecraft);
/*  207 */     this.contextualInfoBars = (Map<ContextualInfo, Supplier<ContextualBar>>)ImmutableMap.of(ContextualInfo.EMPTY, () -> ContextualBar.EMPTY, ContextualInfo.EXPERIENCE, () -> new ExperienceBar(minecraft), ContextualInfo.LOCATOR, () -> new LocatorBar(minecraft), ContextualInfo.JUMPABLE_VEHICLE, () -> new JumpableVehicleBar(minecraft));
/*  214 */     resetTitleTimes();
/*      */   }
/*      */   
/*      */   public void registerReloadListeners(ReloadableResourceManager resourceManager) {
/*  218 */     resourceManager.registerReloadListener((PreparableReloadListener)this.waypointStyles);
/*      */   }
/*      */   
/*      */   public void toggle() {
/*  222 */     this.isHidden = !this.isHidden;
/*      */   }
/*      */   
/*      */   public boolean isHidden() {
/*  226 */     return this.isHidden;
/*      */   }
/*      */   
/*      */   public void resetTitleTimes() {
/*  230 */     this.titleFadeInTime = 10;
/*  231 */     this.titleStayTime = 70;
/*  232 */     this.titleFadeOutTime = 20;
/*      */   }
/*      */   
/*      */   public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  238 */     (this.minecraft.gameRenderer.gameRenderState()).guiRenderState.isHudHidden = this.isHidden;
/*  239 */     if (this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.LevelLoadingScreen)
/*      */       return; 
/*  242 */     if (!this.isHidden) {
/*  243 */       extractCameraOverlays(graphics, deltaTracker);
/*  244 */       extractCrosshair(graphics, deltaTracker);
/*  245 */       graphics.nextStratum();
/*  246 */       extractHotbarAndDecorations(graphics, deltaTracker);
/*  247 */       extractEffects(graphics, deltaTracker);
/*  248 */       extractBossOverlay(graphics, deltaTracker);
/*      */     } 
/*  250 */     extractSleepOverlay(graphics, deltaTracker);
/*  252 */     if (!this.isHidden) {
/*  253 */       extractDemoOverlay(graphics, deltaTracker);
/*  254 */       extractScoreboardSidebar(graphics, deltaTracker);
/*  255 */       extractOverlayMessage(graphics, deltaTracker);
/*  256 */       extractTitle(graphics, deltaTracker);
/*  257 */       extractChat(graphics, deltaTracker);
/*  258 */       extractTabList(graphics, deltaTracker);
/*  259 */       extractSubtitleOverlay(graphics, (this.minecraft.gui.screen() == null || this.minecraft.gui.screen().isInGameUi()));
/*  260 */     } else if (this.minecraft.gui.screen() != null && this.minecraft.gui.screen().isInGameUi()) {
/*  261 */       extractSubtitleOverlay(graphics, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractBossOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  266 */     this.bossOverlay.extractRenderState(graphics);
/*      */   }
/*      */   
/*      */   public void extractDebugOverlay(GuiGraphicsExtractor graphics) {
/*  270 */     this.debugOverlay.extractRenderState(graphics);
/*      */   }
/*      */   
/*      */   private void extractSubtitleOverlay(GuiGraphicsExtractor graphics, boolean deferRendering) {
/*  274 */     if (deferRendering) {
/*  275 */       this.deferredSubtitles = (() -> this.subtitleOverlay.extractRenderState(graphics));
/*      */     } else {
/*  277 */       this.deferredSubtitles = null;
/*  278 */       this.subtitleOverlay.extractRenderState(graphics);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void extractDeferredSubtitles() {
/*  283 */     if (this.deferredSubtitles != null) {
/*  284 */       this.deferredSubtitles.run();
/*  285 */       this.deferredSubtitles = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractCameraOverlays(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  290 */     if (((Boolean)this.minecraft.options.vignette().get()).booleanValue())
/*  291 */       extractVignette(graphics, this.minecraft.getCameraEntity()); 
/*  294 */     LocalPlayer player = this.minecraft.player;
/*  295 */     float gameTimeDeltaTicks = deltaTracker.getGameTimeDeltaTicks();
/*  297 */     this.scopeScale = Mth.lerp(0.5F * gameTimeDeltaTicks, this.scopeScale, 1.125F);
/*  299 */     if (this.minecraft.options.getCameraType().isFirstPerson())
/*  300 */       if (player.isScoping()) {
/*  301 */         extractSpyglassOverlay(graphics, this.scopeScale);
/*      */       } else {
/*  303 */         this.scopeScale = 0.5F;
/*  305 */         for (EquipmentSlot slot : EquipmentSlot.values()) {
/*  306 */           ItemStack item = player.getItemBySlot(slot);
/*  307 */           Equippable equippable = (Equippable)item.get(DataComponents.EQUIPPABLE);
/*  308 */           if (equippable != null && equippable.slot() == slot && equippable.cameraOverlay().isPresent())
/*  309 */             extractTextureOverlay(graphics, ((Identifier)equippable.cameraOverlay().get()).withPath(p -> "textures/" + p + ".png"), 1.0F); 
/*      */         } 
/*      */       }  
/*  315 */     if (player.getTicksFrozen() > 0)
/*  316 */       extractTextureOverlay(graphics, POWDER_SNOW_OUTLINE_LOCATION, player.getPercentFrozen()); 
/*  319 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/*  320 */     float portalIntensity = Mth.lerp(partialTicks, player.oPortalEffectIntensity, player.portalEffectIntensity);
/*  321 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, partialTicks);
/*  322 */     if (portalIntensity > 0.0F) {
/*  323 */       extractPortalOverlay(graphics, portalIntensity);
/*  324 */     } else if (nauseaIntensity > 0.0F) {
/*  325 */       float screenEffectScale = ((Double)this.minecraft.options.screenEffectScale().get()).floatValue();
/*  326 */       if (screenEffectScale < 1.0F) {
/*  327 */         float overlayStrength = nauseaIntensity * (1.0F - screenEffectScale);
/*  328 */         extractConfusionOverlay(graphics, overlayStrength);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractSleepOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  334 */     if (this.minecraft.player.getSleepTimer() <= 0)
/*      */       return; 
/*  338 */     Profiler.get().push("sleep");
/*  340 */     graphics.nextStratum();
/*  341 */     float sleepTimer = this.minecraft.player.getSleepTimer();
/*  342 */     float amount = sleepTimer / 100.0F;
/*  343 */     if (amount > 1.0F)
/*  345 */       amount = 1.0F - (sleepTimer - 100.0F) / 10.0F; 
/*  348 */     int color = (int)(220.0F * amount) << 24 | 0x101020;
/*  349 */     graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
/*  350 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void extractOverlayMessage(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  354 */     Font font = getFont();
/*  355 */     if (this.overlayMessageString == null || this.overlayMessageTime <= 0)
/*      */       return; 
/*  358 */     Profiler.get().push("overlayMessage");
/*  359 */     float t = this.overlayMessageTime - deltaTracker.getGameTimeDeltaPartialTick(false);
/*  360 */     int alpha = (int)(t * 255.0F / 20.0F);
/*  361 */     if (alpha > 255)
/*  362 */       alpha = 255; 
/*  364 */     if (alpha > 0) {
/*      */       int color;
/*  365 */       graphics.nextStratum();
/*  366 */       graphics.pose().pushMatrix();
/*  367 */       graphics.pose().translate((graphics.guiWidth() / 2), (graphics.guiHeight() - 68));
/*  370 */       if (this.animateOverlayMessageColor) {
/*  371 */         color = Mth.hsvToArgb(t / 50.0F, 0.7F, 0.6F, alpha);
/*      */       } else {
/*  373 */         color = ARGB.white(alpha);
/*      */       } 
/*  375 */       int width = font.width((FormattedText)this.overlayMessageString);
/*  376 */       graphics.textWithBackdrop(font, this.overlayMessageString, -width / 2, -4, width, color);
/*  378 */       graphics.pose().popMatrix();
/*      */     } 
/*  380 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void extractTitle(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  384 */     if (this.title == null || this.titleTime <= 0)
/*      */       return; 
/*  388 */     Font font = getFont();
/*  389 */     Profiler.get().push("titleAndSubtitle");
/*  390 */     float t = this.titleTime - deltaTracker.getGameTimeDeltaPartialTick(false);
/*  391 */     int alpha = 255;
/*  392 */     if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
/*  393 */       float time = (this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime) - t;
/*  394 */       alpha = (int)(time * 255.0F / this.titleFadeInTime);
/*      */     } 
/*  396 */     if (this.titleTime <= this.titleFadeOutTime)
/*  397 */       alpha = (int)(t * 255.0F / this.titleFadeOutTime); 
/*  399 */     alpha = Mth.clamp(alpha, 0, 255);
/*  400 */     if (alpha > 0) {
/*  401 */       graphics.nextStratum();
/*  402 */       graphics.pose().pushMatrix();
/*  403 */       graphics.pose().translate((graphics.guiWidth() / 2), (graphics.guiHeight() / 2));
/*  405 */       graphics.pose().pushMatrix();
/*  406 */       graphics.pose().scale(4.0F, 4.0F);
/*  408 */       int titleWidth = font.width((FormattedText)this.title);
/*  409 */       int textColor = ARGB.white(alpha);
/*  410 */       graphics.textWithBackdrop(font, this.title, -titleWidth / 2, -10, titleWidth, textColor);
/*  412 */       graphics.pose().popMatrix();
/*  414 */       if (this.subtitle != null) {
/*  415 */         graphics.pose().pushMatrix();
/*  416 */         graphics.pose().scale(2.0F, 2.0F);
/*  418 */         int subtitleWidth = font.width((FormattedText)this.subtitle);
/*  419 */         graphics.textWithBackdrop(font, this.subtitle, -subtitleWidth / 2, 5, subtitleWidth, textColor);
/*  420 */         graphics.pose().popMatrix();
/*      */       } 
/*  423 */       graphics.pose().popMatrix();
/*      */     } 
/*  425 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void extractChat(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  429 */     if (this.minecraft.player != null && !this.chat.isChatFocused()) {
/*  430 */       Window window = this.minecraft.getWindow();
/*  431 */       int mouseX = Mth.floor(this.minecraft.mouseHandler.getScaledXPos(window));
/*  432 */       int mouseY = Mth.floor(this.minecraft.mouseHandler.getScaledYPos(window));
/*  433 */       graphics.nextStratum();
/*  434 */       this.chat.extractRenderState(graphics, getFont(), this.tickCount, mouseX, mouseY, ChatComponent.DisplayMode.BACKGROUND, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractScoreboardSidebar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  439 */     Scoreboard scoreboard = this.minecraft.level.getScoreboard();
/*  440 */     Objective teamObjective = null;
/*  441 */     PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
/*  442 */     if (playerTeam != null) {
/*  443 */       Optional<TeamColor> teamColor = playerTeam.getColor();
/*  444 */       if (teamColor.isPresent())
/*  445 */         teamObjective = scoreboard.getDisplayObjective(((TeamColor)teamColor.get()).displaySlot()); 
/*      */     } 
/*  448 */     Objective displayObjective = (teamObjective != null) ? teamObjective : scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
/*  449 */     if (displayObjective != null) {
/*  450 */       graphics.nextStratum();
/*  451 */       displayScoreboardSidebar(graphics, displayObjective);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractTabList(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  456 */     Scoreboard scoreboard = this.minecraft.level.getScoreboard();
/*  457 */     Objective displayObjective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
/*  458 */     if (this.minecraft.options.keyPlayerList.isDown() && (!this.minecraft.isLocalServer() || this.minecraft.player.connection.getListedOnlinePlayers().size() > 1 || displayObjective != null)) {
/*  459 */       this.tabList.setVisible(true);
/*  460 */       graphics.nextStratum();
/*  461 */       this.tabList.extractRenderState(graphics, graphics.guiWidth(), scoreboard, displayObjective);
/*      */     } else {
/*  463 */       this.tabList.setVisible(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  468 */     Options options = this.minecraft.options;
/*  470 */     if (!options.getCameraType().isFirstPerson())
/*      */       return; 
/*  474 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR && 
/*  475 */       !canRenderCrosshairForSpectator(this.minecraft.hitResult))
/*      */       return; 
/*  480 */     if (!this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
/*  481 */       graphics.nextStratum();
/*  482 */       int size = 15;
/*  483 */       graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_SPRITE, (graphics.guiWidth() - 15) / 2, (graphics.guiHeight() - 15) / 2, 15, 15);
/*  485 */       if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
/*      */         int i;
/*  486 */         float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
/*  487 */         boolean renderMaxAttackIndicator = false;
/*  488 */         if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && attackStrengthScale >= 1.0F) {
/*  489 */           renderMaxAttackIndicator = (this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0F);
/*  490 */           renderMaxAttackIndicator &= this.minecraft.crosshairPickEntity.isAlive();
/*  491 */           AttackRange attackRange = (AttackRange)this.minecraft.player.getActiveItem().get(DataComponents.ATTACK_RANGE);
/*  492 */           i = renderMaxAttackIndicator & ((attackRange == null || attackRange.isInRange((LivingEntity)this.minecraft.player, this.minecraft.hitResult.getLocation())) ? 1 : 0);
/*      */         } 
/*  495 */         int y = graphics.guiHeight() / 2 - 7 + 16;
/*  496 */         int x = graphics.guiWidth() / 2 - 8;
/*  498 */         if (i != 0) {
/*  499 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE, x, y, 16, 16);
/*  500 */         } else if (attackStrengthScale < 1.0F) {
/*  501 */           int progress = (int)(attackStrengthScale * 17.0F);
/*  503 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 16, 4);
/*  504 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE, 16, 4, 0, 0, x, y, progress, 4);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean canRenderCrosshairForSpectator(HitResult hitResult) {
/*  511 */     if (hitResult == null)
/*  512 */       return false; 
/*  515 */     if (hitResult.getType() == HitResult.Type.ENTITY)
/*  516 */       return ((EntityHitResult)hitResult).getEntity() instanceof net.minecraft.world.MenuProvider; 
/*  517 */     if (hitResult.getType() == HitResult.Type.BLOCK) {
/*  518 */       BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
/*  519 */       ClientLevel clientLevel = this.minecraft.level;
/*  520 */       return (clientLevel.getBlockState(pos).getMenuProvider((Level)clientLevel, pos) != null);
/*      */     } 
/*  523 */     return false;
/*      */   }
/*      */   
/*      */   private void extractEffects(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  527 */     Collection<MobEffectInstance> activeEffects = this.minecraft.player.getActiveEffects();
/*  529 */     if (activeEffects.isEmpty() || (this.minecraft.gui.screen() != null && this.minecraft.gui.screen().showsActiveEffects()))
/*      */       return; 
/*  533 */     int beneficialCount = 0;
/*  534 */     int harmfulCount = 0;
/*  536 */     for (MobEffectInstance instance : Ordering.natural().reverse().sortedCopy(activeEffects)) {
/*  537 */       Holder<MobEffect> effect = instance.getEffect();
/*  539 */       if (instance.showIcon()) {
/*  540 */         int x = graphics.guiWidth();
/*  541 */         int y = 1;
/*  542 */         if (this.minecraft.isDemo())
/*  543 */           y += 15; 
/*  546 */         if (((MobEffect)effect.value()).isBeneficial()) {
/*  547 */           beneficialCount++;
/*  548 */           x -= 25 * beneficialCount;
/*      */         } else {
/*  550 */           harmfulCount++;
/*  551 */           x -= 25 * harmfulCount;
/*  552 */           y += 26;
/*      */         } 
/*  555 */         float alpha = 1.0F;
/*  556 */         if (instance.isAmbient()) {
/*  557 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_AMBIENT_SPRITE, x, y, 24, 24);
/*      */         } else {
/*  559 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 24, 24);
/*  561 */           if (instance.endsWithin(200)) {
/*  562 */             int remainingDuration = instance.getDuration();
/*  563 */             int usedSeconds = 10 - remainingDuration / 20;
/*  564 */             alpha = Mth.clamp(remainingDuration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((remainingDuration * 3.1415927F / 5.0F)) * Mth.clamp(usedSeconds / 10.0F * 0.25F, 0.0F, 0.25F);
/*  565 */             alpha = Mth.clamp(alpha, 0.0F, 1.0F);
/*      */           } 
/*      */         } 
/*  569 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getMobEffectSprite(effect), x + 3, y + 3, 18, 18, ARGB.white(alpha));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Identifier getMobEffectSprite(Holder<MobEffect> effect) {
/*  575 */     return effect.unwrapKey().map(ResourceKey::identifier)
/*  576 */       .map(id -> id.withPrefix("mob_effect/"))
/*  577 */       .orElseGet(MissingTextureAtlasSprite::getLocation);
/*      */   }
/*      */   
/*      */   private void extractHotbarAndDecorations(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  581 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
/*  582 */       this.spectatorGui.extractHotbar(graphics);
/*      */     } else {
/*  584 */       extractItemHotbar(graphics, deltaTracker);
/*      */     } 
/*  587 */     if (this.minecraft.gameMode.canHurtPlayer())
/*  588 */       extractPlayerHealth(graphics); 
/*  590 */     extractVehicleHealth(graphics);
/*  592 */     ContextualInfo nextContextualInfo = nextContextualInfoState();
/*  593 */     if (nextContextualInfo != this.contextualInfoBar.getFirst())
/*  594 */       this.contextualInfoBar = Pair.of(nextContextualInfo, ((Supplier<ContextualBar>)this.contextualInfoBars.get(nextContextualInfo)).get()); 
/*  597 */     ((ContextualBar)this.contextualInfoBar.getSecond()).extractBackground(graphics, deltaTracker);
/*  599 */     if (this.minecraft.gameMode.hasExperience() && this.minecraft.player.experienceLevel > 0)
/*  600 */       ContextualBar.extractExperienceLevel(graphics, this.minecraft.font, this.minecraft.player.experienceLevel); 
/*  602 */     ((ContextualBar)this.contextualInfoBar.getSecond()).extractRenderState(graphics, deltaTracker);
/*  604 */     if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
/*  605 */       extractSelectedItemName(graphics);
/*  606 */     } else if (this.minecraft.player.isSpectator()) {
/*  607 */       this.spectatorGui.extractAction(graphics);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractItemHotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*  612 */     Player player = getCameraPlayer();
/*  613 */     if (player == null)
/*      */       return; 
/*  617 */     ItemStack offhand = player.getOffhandItem();
/*  618 */     HumanoidArm offhandArm = player.getMainArm().getOpposite();
/*  619 */     int screenCenter = graphics.guiWidth() / 2;
/*  620 */     int hotbarWidth = 182;
/*  621 */     int halfHotbar = 91;
/*  623 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, screenCenter - 91, graphics.guiHeight() - 22, 182, 22);
/*  624 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + player.getInventory().getSelectedSlot() * 20, graphics.guiHeight() - 22 - 1, 24, 23);
/*  626 */     if (!offhand.isEmpty())
/*  627 */       if (offhandArm == HumanoidArm.LEFT) {
/*  628 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, graphics.guiHeight() - 23, 29, 24);
/*      */       } else {
/*  630 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, graphics.guiHeight() - 23, 29, 24);
/*      */       }  
/*  634 */     int seed = 1;
/*  635 */     for (int i = 0; i < 9; i++) {
/*  636 */       int x = screenCenter - 90 + i * 20 + 2;
/*  637 */       int y = graphics.guiHeight() - 16 - 3;
/*  638 */       extractSlot(graphics, x, y, deltaTracker, player, player.getInventory().getItem(i), seed++);
/*      */     } 
/*  641 */     if (!offhand.isEmpty()) {
/*  642 */       int y = graphics.guiHeight() - 16 - 3;
/*  643 */       if (offhandArm == HumanoidArm.LEFT) {
/*  644 */         extractSlot(graphics, screenCenter - 91 - 26, y, deltaTracker, player, offhand, seed++);
/*      */       } else {
/*  646 */         extractSlot(graphics, screenCenter + 91 + 10, y, deltaTracker, player, offhand, seed++);
/*      */       } 
/*      */     } 
/*  650 */     if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
/*  651 */       float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
/*  652 */       if (attackStrengthScale < 1.0F) {
/*  653 */         int y = graphics.guiHeight() - 20;
/*  654 */         int x = screenCenter + 91 + 6;
/*  655 */         if (offhandArm == HumanoidArm.RIGHT)
/*  656 */           x = screenCenter - 91 - 22; 
/*  659 */         int progress = (int)(attackStrengthScale * 19.0F);
/*  661 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
/*  662 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - progress, x, y + 18 - progress, 18, progress);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractSelectedItemName(GuiGraphicsExtractor graphics) {
/*  668 */     if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
/*  669 */       MutableComponent str = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color());
/*  670 */       if (this.lastToolHighlight.has(DataComponents.CUSTOM_NAME))
/*  671 */         str.withStyle(ChatFormatting.ITALIC); 
/*  674 */       int strWidth = getFont().width((FormattedText)str);
/*  675 */       int x = (graphics.guiWidth() - strWidth) / 2;
/*  676 */       int y = graphics.guiHeight() - 59;
/*  677 */       if (!this.minecraft.gameMode.canHurtPlayer())
/*  679 */         y += 14; 
/*  682 */       int alpha = (int)(this.toolHighlightTimer * 256.0F / 10.0F);
/*  683 */       if (alpha > 255)
/*  684 */         alpha = 255; 
/*  686 */       if (alpha > 0)
/*  687 */         graphics.textWithBackdrop(getFont(), (Component)str, x, y, strWidth, ARGB.white(alpha)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractDemoOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/*      */     MutableComponent mutableComponent;
/*  693 */     if (!this.minecraft.isDemo())
/*      */       return; 
/*  697 */     Profiler.get().push("demo");
/*  698 */     graphics.nextStratum();
/*  700 */     if (this.minecraft.level.getGameTime() >= 120500L) {
/*  701 */       Component msg = DEMO_EXPIRED_TEXT;
/*      */     } else {
/*  703 */       mutableComponent = Component.translatable("demo.remainingTime", new Object[] { StringUtil.formatTickDuration((int)(120500L - this.minecraft.level.getGameTime()), this.minecraft.level.tickRateManager().tickrate()) });
/*      */     } 
/*  706 */     int width = getFont().width((FormattedText)mutableComponent);
/*  707 */     int textX = graphics.guiWidth() - width - 10;
/*  708 */     int textY = 5;
/*  709 */     graphics.textWithBackdrop(getFont(), (Component)mutableComponent, textX, 5, width, -1);
/*  710 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void displayScoreboardSidebar(GuiGraphicsExtractor graphics, Objective objective) {
/*  716 */     Scoreboard scoreboard = objective.getScoreboard();
/*  717 */     NumberFormat objectiveScoreFormat = objective.numberFormatOrDefault((NumberFormat)StyledFormat.SIDEBAR_DEFAULT);
/*  730 */     DisplayEntry[] entriesToDisplay = (DisplayEntry[])scoreboard.listPlayerScores(objective).stream().filter(input -> !input.isHidden()).sorted(SCORE_DISPLAY_ORDER).limit(15L).map(score -> {
/*      */           PlayerTeam team = scoreboard.getPlayersTeam(score.owner());
/*      */           Component ownerName = score.ownerName();
/*      */           MutableComponent mutableComponent1 = PlayerTeam.formatNameForTeam((Team)team, ownerName);
/*      */           MutableComponent mutableComponent2 = score.formatValue(objectiveScoreFormat);
/*      */           int scoreWidth = getFont().width((FormattedText)mutableComponent2);
/*      */           static final class DisplayEntry extends Record {
/*      */             private final Component name;
/*      */             
/*      */             private final Component score;
/*      */             
/*      */             private final int scoreWidth;
/*      */             
/*      */             DisplayEntry(Component name, Component score, int scoreWidth) {
/*      */               this.name = name;
/*      */               this.score = score;
/*      */               this.scoreWidth = scoreWidth;
/*      */             }
/*      */             
/*      */             public final String toString() {
/*      */               // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;)Ljava/lang/String;
/*      */               //   6: areturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #714	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	7	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*      */             }
/*      */             
/*      */             public final int hashCode() {
/*      */               // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;)I
/*      */               //   6: ireturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #714	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	7	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*      */             }
/*      */             
/*      */             public final boolean equals(Object o) {
/*      */               // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: aload_1
/*      */               //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;Ljava/lang/Object;)Z
/*      */               //   7: ireturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #714	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	8	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*      */               //   0	8	1	o	Ljava/lang/Object;
/*      */             }
/*      */             
/*      */             public Component name() {
/*      */               return this.name;
/*      */             }
/*      */             
/*      */             public Component score() {
/*      */               return this.score;
/*      */             }
/*      */             
/*      */             public int scoreWidth() {
/*      */               return this.scoreWidth;
/*      */             }
/*      */           };
/*      */           return new DisplayEntry((Component)mutableComponent1, (Component)mutableComponent2, scoreWidth);
/*  730 */         }).toArray(x$0 -> new DisplayEntry[x$0]);
/*  732 */     Component objectiveDisplayName = objective.getDisplayName();
/*  733 */     int objectiveDisplayNameWidth = getFont().width((FormattedText)objectiveDisplayName);
/*  734 */     int biggestWidth = objectiveDisplayNameWidth;
/*  736 */     int spacerWidth = getFont().width(": ");
/*  737 */     for (DisplayEntry entry : entriesToDisplay)
/*  738 */       biggestWidth = Math.max(biggestWidth, getFont().width((FormattedText)entry.name) + ((entry.scoreWidth > 0) ? (spacerWidth + entry.scoreWidth) : 0)); 
/*  741 */     int width = biggestWidth;
/*  742 */     int entriesCount = entriesToDisplay.length;
/*  743 */     Objects.requireNonNull(getFont());
/*  743 */     int height = entriesCount * 9;
/*  744 */     int bottom = graphics.guiHeight() / 2 + height / 3;
/*  745 */     int rightPadding = 3;
/*  746 */     int left = graphics.guiWidth() - width - 3;
/*  747 */     int right = graphics.guiWidth() - 3 + 2;
/*  749 */     int backgroundColor = this.minecraft.options.getBackgroundColor(0.3F);
/*  750 */     int headerBackgroundColor = this.minecraft.options.getBackgroundColor(0.4F);
/*  752 */     Objects.requireNonNull(getFont());
/*  752 */     int headerY = bottom - entriesCount * 9;
/*  754 */     Objects.requireNonNull(getFont());
/*  754 */     graphics.fill(left - 2, headerY - 9 - 1, right, headerY - 1, headerBackgroundColor);
/*  755 */     graphics.fill(left - 2, headerY - 1, right, bottom, backgroundColor);
/*  757 */     Objects.requireNonNull(getFont());
/*  757 */     graphics.text(getFont(), objectiveDisplayName, left + width / 2 - objectiveDisplayNameWidth / 2, headerY - 9, -1, false);
/*  758 */     for (int i = 0; i < entriesCount; i++) {
/*  759 */       DisplayEntry e = entriesToDisplay[i];
/*  760 */       Objects.requireNonNull(getFont());
/*  760 */       int y = bottom - (entriesCount - i) * 9;
/*  762 */       graphics.text(getFont(), e.name, left, y, -1, false);
/*  763 */       graphics.text(getFont(), e.score, right - e.scoreWidth, y, -1, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Player getCameraPlayer() {
/*  768 */     Entity entity = this.minecraft.getCameraEntity();
/*  768 */     Player player = (Player)entity;
/*  768 */     return (entity instanceof Player) ? player : null;
/*      */   }
/*      */   
/*      */   private LivingEntity getPlayerVehicleWithHealth() {
/*  772 */     Player player = getCameraPlayer();
/*  773 */     if (player != null) {
/*  774 */       Entity vehicle = player.getVehicle();
/*  775 */       if (vehicle == null)
/*  776 */         return null; 
/*  778 */       if (vehicle instanceof LivingEntity) {
/*  778 */         LivingEntity livingEntity = (LivingEntity)vehicle;
/*  779 */         return livingEntity;
/*      */       } 
/*      */     } 
/*  782 */     return null;
/*      */   }
/*      */   
/*      */   private int getVehicleMaxHearts(LivingEntity vehicle) {
/*  786 */     if (vehicle == null || !vehicle.showVehicleHealth())
/*  787 */       return 0; 
/*  790 */     float maxVehicleHealth = vehicle.getMaxHealth();
/*  791 */     int hearts = (int)(maxVehicleHealth + 0.5F) / 2;
/*  792 */     if (hearts > 30)
/*  793 */       hearts = 30; 
/*  795 */     return hearts;
/*      */   }
/*      */   
/*      */   private int getVisibleVehicleHeartRows(int hearts) {
/*  799 */     return (int)Math.ceil(hearts / 10.0D);
/*      */   }
/*      */   
/*      */   private void extractPlayerHealth(GuiGraphicsExtractor graphics) {
/*  803 */     Player player = getCameraPlayer();
/*  804 */     if (player == null)
/*      */       return; 
/*  807 */     int currentHealth = Mth.ceil(player.getHealth());
/*  809 */     boolean blink = (this.healthBlinkTime > this.tickCount && (this.healthBlinkTime - this.tickCount) / 3L % 2L == 1L);
/*  810 */     long timeMillis = Util.getMillis();
/*  811 */     if (currentHealth < this.lastHealth && player.invulnerableTime > 0) {
/*  812 */       this.lastHealthTime = timeMillis;
/*  813 */       this.healthBlinkTime = (this.tickCount + 20);
/*  814 */     } else if (currentHealth > this.lastHealth && player.invulnerableTime > 0) {
/*  815 */       this.lastHealthTime = timeMillis;
/*  816 */       this.healthBlinkTime = (this.tickCount + 10);
/*      */     } 
/*  818 */     if (timeMillis - this.lastHealthTime > 1000L) {
/*  819 */       this.displayHealth = currentHealth;
/*  820 */       this.lastHealthTime = timeMillis;
/*      */     } 
/*  822 */     this.lastHealth = currentHealth;
/*  823 */     int oldHealth = this.displayHealth;
/*  824 */     this.random.setSeed((this.tickCount * 312871));
/*  826 */     int xLeft = graphics.guiWidth() / 2 - 91;
/*  827 */     int xRight = graphics.guiWidth() / 2 + 91;
/*  829 */     int yLineBase = graphics.guiHeight() - 39;
/*  830 */     float maxHealth = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), Math.max(oldHealth, currentHealth));
/*  831 */     int totalAbsorption = Mth.ceil(player.getAbsorptionAmount());
/*  832 */     int numHealthRows = Mth.ceil((maxHealth + totalAbsorption) / 2.0F / 10.0F);
/*  833 */     int healthRowHeight = Math.max(10 - numHealthRows - 2, 3);
/*  834 */     int yLineAir = yLineBase - 10;
/*  835 */     int heartOffsetIndex = -1;
/*  836 */     if (player.hasEffect(MobEffects.REGENERATION))
/*  837 */       heartOffsetIndex = this.tickCount % Mth.ceil(maxHealth + 5.0F); 
/*  840 */     Profiler.get().push("armor");
/*  841 */     extractArmor(graphics, player, yLineBase, numHealthRows, healthRowHeight, xLeft);
/*  843 */     Profiler.get().popPush("health");
/*  844 */     extractHearts(graphics, player, xLeft, yLineBase, healthRowHeight, heartOffsetIndex, maxHealth, currentHealth, oldHealth, totalAbsorption, blink);
/*  846 */     LivingEntity vehicleWithHearts = getPlayerVehicleWithHealth();
/*  847 */     int vehicleHearts = getVehicleMaxHearts(vehicleWithHearts);
/*  848 */     if (vehicleHearts == 0) {
/*  849 */       Profiler.get().popPush("food");
/*  850 */       extractFood(graphics, player, yLineBase, xRight);
/*  851 */       yLineAir -= 10;
/*      */     } 
/*  854 */     Profiler.get().popPush("air");
/*  855 */     extractAirBubbles(graphics, player, vehicleHearts, yLineAir, xRight);
/*  857 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private static void extractArmor(GuiGraphicsExtractor graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft) {
/*  861 */     int armor = player.getArmorValue();
/*  862 */     if (armor <= 0)
/*      */       return; 
/*  865 */     int yLineArmor = yLineBase - (numHealthRows - 1) * healthRowHeight - 10;
/*  866 */     for (int i = 0; i < 10; i++) {
/*  867 */       int xo = xLeft + i * 8;
/*  868 */       if (i * 2 + 1 < armor)
/*  869 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_SPRITE, xo, yLineArmor, 9, 9); 
/*  871 */       if (i * 2 + 1 == armor)
/*  872 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF_SPRITE, xo, yLineArmor, 9, 9); 
/*  874 */       if (i * 2 + 1 > armor)
/*  875 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_SPRITE, xo, yLineArmor, 9, 9); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private enum HeartType {
/*  881 */     CONTAINER(
/*  882 */       (String)Identifier.withDefaultNamespace("hud/heart/container"), 
/*  883 */       Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/*  884 */       Identifier.withDefaultNamespace("hud/heart/container"), 
/*  885 */       Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/*  886 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/*  887 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking"), 
/*  888 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/*  889 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking")),
/*  891 */     NORMAL(
/*  892 */       (String)Identifier.withDefaultNamespace("hud/heart/full"), 
/*  893 */       Identifier.withDefaultNamespace("hud/heart/full_blinking"), 
/*  894 */       Identifier.withDefaultNamespace("hud/heart/half"), 
/*  895 */       Identifier.withDefaultNamespace("hud/heart/half_blinking"), 
/*  896 */       Identifier.withDefaultNamespace("hud/heart/hardcore_full"), 
/*  897 */       Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking"), 
/*  898 */       Identifier.withDefaultNamespace("hud/heart/hardcore_half"), 
/*  899 */       Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking")),
/*  901 */     POISIONED(
/*  902 */       (String)Identifier.withDefaultNamespace("hud/heart/poisoned_full"), 
/*  903 */       Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking"), 
/*  904 */       Identifier.withDefaultNamespace("hud/heart/poisoned_half"), 
/*  905 */       Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking"), 
/*  906 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full"), 
/*  907 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"), 
/*  908 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half"), 
/*  909 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")),
/*  911 */     WITHERED(
/*  912 */       (String)Identifier.withDefaultNamespace("hud/heart/withered_full"), 
/*  913 */       Identifier.withDefaultNamespace("hud/heart/withered_full_blinking"), 
/*  914 */       Identifier.withDefaultNamespace("hud/heart/withered_half"), 
/*  915 */       Identifier.withDefaultNamespace("hud/heart/withered_half_blinking"), 
/*  916 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full"), 
/*  917 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"), 
/*  918 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half"), 
/*  919 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")),
/*  921 */     ABSORBING(
/*  922 */       (String)Identifier.withDefaultNamespace("hud/heart/absorbing_full"), 
/*  923 */       Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking"), 
/*  924 */       Identifier.withDefaultNamespace("hud/heart/absorbing_half"), 
/*  925 */       Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking"), 
/*  926 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full"), 
/*  927 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"), 
/*  928 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half"), 
/*  929 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")),
/*  931 */     FROZEN(
/*  932 */       (String)Identifier.withDefaultNamespace("hud/heart/frozen_full"), 
/*  933 */       Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking"), 
/*  934 */       Identifier.withDefaultNamespace("hud/heart/frozen_half"), 
/*  935 */       Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking"), 
/*  936 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full"), 
/*  937 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"), 
/*  938 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half"), 
/*  939 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking"));
/*      */     
/*      */     private final Identifier full;
/*      */     
/*      */     private final Identifier fullBlinking;
/*      */     
/*      */     private final Identifier half;
/*      */     
/*      */     private final Identifier halfBlinking;
/*      */     
/*      */     private final Identifier hardcoreFull;
/*      */     
/*      */     private final Identifier hardcoreFullBlinking;
/*      */     
/*      */     private final Identifier hardcoreHalf;
/*      */     
/*      */     private final Identifier hardcoreHalfBlinking;
/*      */     
/*      */     HeartType(Identifier full, Identifier fullBlinking, Identifier half, Identifier halfBlinking, Identifier hardcoreFull, Identifier hardcoreFullBlinking, Identifier hardcoreHalf, Identifier hardcoreHalfBlinking) {
/*  953 */       this.full = full;
/*  954 */       this.fullBlinking = fullBlinking;
/*  955 */       this.half = half;
/*  956 */       this.halfBlinking = halfBlinking;
/*  957 */       this.hardcoreFull = hardcoreFull;
/*  958 */       this.hardcoreFullBlinking = hardcoreFullBlinking;
/*  959 */       this.hardcoreHalf = hardcoreHalf;
/*  960 */       this.hardcoreHalfBlinking = hardcoreHalfBlinking;
/*      */     }
/*      */     
/*      */     public Identifier getSprite(boolean isHardcore, boolean isHalf, boolean isBlink) {
/*  964 */       if (!isHardcore) {
/*  965 */         if (isHalf)
/*  966 */           return isBlink ? this.halfBlinking : this.half; 
/*  968 */         return isBlink ? this.fullBlinking : this.full;
/*      */       } 
/*  971 */       if (isHalf)
/*  972 */         return isBlink ? this.hardcoreHalfBlinking : this.hardcoreHalf; 
/*  974 */       return isBlink ? this.hardcoreFullBlinking : this.hardcoreFull;
/*      */     }
/*      */     
/*      */     private static HeartType forPlayer(Player player) {
/*      */       HeartType type;
/*  981 */       if (player.hasEffect(MobEffects.POISON)) {
/*  982 */         type = POISIONED;
/*  983 */       } else if (player.hasEffect(MobEffects.WITHER)) {
/*  984 */         type = WITHERED;
/*  985 */       } else if (player.isFullyFrozen()) {
/*  986 */         type = FROZEN;
/*      */       } else {
/*  988 */         type = NORMAL;
/*      */       } 
/*  990 */       return type;
/*      */     }
/*      */   }
/*      */   
/*      */   private void extractHearts(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink) {
/*  995 */     HeartType type = HeartType.forPlayer(player);
/*  996 */     boolean isHardcore = player.level().getLevelData().isHardcore();
/*  998 */     int healthContainerCount = Mth.ceil(maxHealth / 2.0D);
/*  999 */     int absorptionContainerCount = Mth.ceil(absorption / 2.0D);
/* 1000 */     int maxHealthHalvesCount = healthContainerCount * 2;
/* 1002 */     for (int containerIndex = healthContainerCount + absorptionContainerCount - 1; containerIndex >= 0; containerIndex--) {
/* 1003 */       int row = containerIndex / 10;
/* 1004 */       int column = containerIndex % 10;
/* 1006 */       int xo = xLeft + column * 8;
/* 1007 */       int yo = yLineBase - row * healthRowHeight;
/* 1009 */       if (currentHealth + absorption <= 4)
/* 1010 */         yo += this.random.nextInt(2); 
/* 1012 */       if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex)
/* 1013 */         yo -= 2; 
/* 1016 */       extractHeart(graphics, HeartType.CONTAINER, xo, yo, isHardcore, blink, false);
/* 1018 */       int halves = containerIndex * 2;
/* 1020 */       boolean isAbsorptionHeart = (containerIndex >= healthContainerCount);
/* 1021 */       if (isAbsorptionHeart) {
/* 1022 */         int absorptionHalves = halves - maxHealthHalvesCount;
/* 1023 */         if (absorptionHalves < absorption) {
/* 1024 */           boolean halfHeart = (absorptionHalves + 1 == absorption);
/* 1025 */           extractHeart(graphics, (type == HeartType.WITHERED) ? type : HeartType.ABSORBING, xo, yo, isHardcore, false, halfHeart);
/*      */         } 
/*      */       } 
/* 1028 */       if (blink && halves < oldHealth) {
/* 1029 */         boolean halfHeart = (halves + 1 == oldHealth);
/* 1030 */         extractHeart(graphics, type, xo, yo, isHardcore, true, halfHeart);
/*      */       } 
/* 1032 */       if (halves < currentHealth) {
/* 1033 */         boolean halfHeart = (halves + 1 == currentHealth);
/* 1034 */         extractHeart(graphics, type, xo, yo, isHardcore, false, halfHeart);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractHeart(GuiGraphicsExtractor graphics, HeartType type, int xo, int yo, boolean isHardcore, boolean blinks, boolean half) {
/* 1040 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, type.getSprite(isHardcore, half, blinks), xo, yo, 9, 9);
/*      */   }
/*      */   
/*      */   private void extractAirBubbles(GuiGraphicsExtractor graphics, Player player, int vehicleHearts, int yLineAir, int xRight) {
/* 1044 */     int maxAirSupplyTicks = player.getMaxAirSupply();
/* 1045 */     int currentAirSupplyTicks = Math.clamp(player.getAirSupply(), 0, maxAirSupplyTicks);
/* 1046 */     boolean isUnderWater = player.isEyeInFluid(FluidTags.WATER);
/* 1047 */     if (isUnderWater || currentAirSupplyTicks < maxAirSupplyTicks) {
/* 1048 */       yLineAir = getAirBubbleYLine(vehicleHearts, yLineAir);
/* 1049 */       int fullAirBubbles = getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, -2);
/* 1050 */       int poppingAirBubblePosition = getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, 0);
/* 1051 */       int emptyAirBubbles = 10 - getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, getEmptyBubbleDelayDuration(currentAirSupplyTicks, isUnderWater));
/* 1052 */       boolean isPoppingBubble = (fullAirBubbles != poppingAirBubblePosition);
/* 1054 */       if (!isUnderWater)
/* 1055 */         this.lastBubblePopSoundPlayed = 0; 
/* 1058 */       for (int airBubble = 1; airBubble <= 10; airBubble++) {
/* 1059 */         int airBubbleXPos = xRight - (airBubble - 1) * 8 - 9;
/* 1061 */         if (airBubble <= fullAirBubbles) {
/* 1062 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_SPRITE, airBubbleXPos, yLineAir, 9, 9);
/* 1063 */         } else if (isPoppingBubble && airBubble == poppingAirBubblePosition && isUnderWater) {
/* 1064 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_POPPING_SPRITE, airBubbleXPos, yLineAir, 9, 9);
/* 1065 */           playAirBubblePoppedSound(airBubble, player, emptyAirBubbles);
/* 1066 */         } else if (airBubble > 10 - emptyAirBubbles) {
/* 1067 */           int wobbleYOffset = (emptyAirBubbles == 10 && this.tickCount % 2 == 0) ? this.random.nextInt(2) : 0;
/* 1068 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_EMPTY_SPRITE, airBubbleXPos, yLineAir + wobbleYOffset, 9, 9);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getAirBubbleYLine(int vehicleHearts, int yLineAir) {
/* 1076 */     int rowOffset = getVisibleVehicleHeartRows(vehicleHearts) - 1;
/* 1077 */     yLineAir -= rowOffset * 10;
/* 1078 */     return yLineAir;
/*      */   }
/*      */   
/*      */   private static int getCurrentAirSupplyBubble(int currentAirSupplyTicks, int maxAirSupplyTicks, int tickOffset) {
/* 1082 */     return Mth.ceil(((currentAirSupplyTicks + tickOffset) * 10) / maxAirSupplyTicks);
/*      */   }
/*      */   
/*      */   private static int getEmptyBubbleDelayDuration(int currentAirSupplyTicks, boolean isUnderWater) {
/* 1086 */     return (currentAirSupplyTicks == 0 || !isUnderWater) ? 0 : 1;
/*      */   }
/*      */   
/*      */   private void playAirBubblePoppedSound(int bubble, Player player, int emptyAirBubbles) {
/* 1090 */     if (this.lastBubblePopSoundPlayed != bubble) {
/* 1091 */       float soundVolume = 0.5F + 0.1F * Math.max(0, emptyAirBubbles - 3 + 1);
/* 1092 */       float soundPitch = 1.0F + 0.1F * Math.max(0, emptyAirBubbles - 5 + 1);
/* 1093 */       player.playSound(SoundEvents.BUBBLE_POP, soundVolume, soundPitch);
/* 1094 */       this.lastBubblePopSoundPlayed = bubble;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractFood(GuiGraphicsExtractor graphics, Player player, int yLineBase, int xRight) {
/* 1099 */     FoodData foodData = player.getFoodData();
/* 1100 */     int food = foodData.getFoodLevel();
/* 1101 */     for (int i = 0; i < 10; i++) {
/*      */       Identifier empty, half, full;
/* 1102 */       int yo = yLineBase;
/* 1107 */       if (player.hasEffect(MobEffects.HUNGER)) {
/* 1108 */         empty = FOOD_EMPTY_HUNGER_SPRITE;
/* 1109 */         half = FOOD_HALF_HUNGER_SPRITE;
/* 1110 */         full = FOOD_FULL_HUNGER_SPRITE;
/*      */       } else {
/* 1112 */         empty = FOOD_EMPTY_SPRITE;
/* 1113 */         half = FOOD_HALF_SPRITE;
/* 1114 */         full = FOOD_FULL_SPRITE;
/*      */       } 
/* 1117 */       if (player.getFoodData().getSaturationLevel() <= 0.0F && 
/* 1118 */         this.tickCount % (food * 3 + 1) == 0)
/* 1119 */         yo += this.random.nextInt(3) - 1; 
/* 1123 */       int xo = xRight - i * 8 - 9;
/* 1124 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, xo, yo, 9, 9);
/* 1125 */       if (i * 2 + 1 < food)
/* 1126 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xo, yo, 9, 9); 
/* 1128 */       if (i * 2 + 1 == food)
/* 1129 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, half, xo, yo, 9, 9); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractVehicleHealth(GuiGraphicsExtractor graphics) {
/* 1135 */     LivingEntity vehicleWithHearts = getPlayerVehicleWithHealth();
/* 1136 */     if (vehicleWithHearts == null)
/*      */       return; 
/* 1139 */     int hearts = getVehicleMaxHearts(vehicleWithHearts);
/* 1140 */     if (hearts == 0)
/*      */       return; 
/* 1143 */     int currentHealth = (int)Math.ceil(vehicleWithHearts.getHealth());
/* 1145 */     Profiler.get().popPush("mountHealth");
/* 1148 */     int yLine1 = graphics.guiHeight() - 39;
/* 1149 */     int xRight = graphics.guiWidth() / 2 + 91;
/* 1150 */     int yo = yLine1;
/* 1151 */     int baseHealth = 0;
/* 1153 */     while (hearts > 0) {
/* 1154 */       int rowHearts = Math.min(hearts, 10);
/* 1155 */       hearts -= rowHearts;
/* 1157 */       for (int i = 0; i < rowHearts; i++) {
/* 1158 */         int xo = xRight - i * 8 - 9;
/* 1159 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_CONTAINER_SPRITE, xo, yo, 9, 9);
/* 1160 */         if (i * 2 + 1 + baseHealth < currentHealth)
/* 1161 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_FULL_SPRITE, xo, yo, 9, 9); 
/* 1163 */         if (i * 2 + 1 + baseHealth == currentHealth)
/* 1164 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_HALF_SPRITE, xo, yo, 9, 9); 
/*      */       } 
/* 1167 */       yo -= 10;
/* 1168 */       baseHealth += 20;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void extractTextureOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha) {
/* 1173 */     int color = ARGB.white(alpha);
/* 1174 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void extractSpyglassOverlay(GuiGraphicsExtractor graphics, float scale) {
/* 1178 */     float srcWidth = Math.min(graphics.guiWidth(), graphics.guiHeight());
/* 1179 */     float srcHeight = srcWidth;
/* 1180 */     float ratio = Math.min(graphics.guiWidth() / srcWidth, graphics.guiHeight() / srcHeight) * scale;
/* 1182 */     int width = Mth.floor(srcWidth * ratio);
/* 1183 */     int height = Mth.floor(srcHeight * ratio);
/* 1185 */     int left = (graphics.guiWidth() - width) / 2;
/* 1186 */     int top = (graphics.guiHeight() - height) / 2;
/* 1187 */     int right = left + width;
/* 1188 */     int bottom = top + height;
/* 1191 */     graphics.blit(RenderPipelines.GUI_TEXTURED, SPYGLASS_SCOPE_LOCATION, left, top, 0.0F, 0.0F, width, height, width, height);
/* 1193 */     graphics.fill(RenderPipelines.GUI, 0, bottom, graphics.guiWidth(), graphics.guiHeight(), -16777216);
/* 1195 */     graphics.fill(RenderPipelines.GUI, 0, 0, graphics.guiWidth(), top, -16777216);
/* 1197 */     graphics.fill(RenderPipelines.GUI, 0, top, left, bottom, -16777216);
/* 1199 */     graphics.fill(RenderPipelines.GUI, right, top, graphics.guiWidth(), bottom, -16777216);
/*      */   }
/*      */   
/*      */   private void updateVignetteBrightness(Entity camera) {
/* 1203 */     BlockPos blockPos = BlockPos.containing(camera.getX(), camera.getEyeY(), camera.getZ());
/* 1204 */     float levelBrightness = Lightmap.getBrightness(camera.level().dimensionType(), camera.level().getMaxLocalRawBrightness(blockPos));
/* 1205 */     float brightness = Mth.clamp(1.0F - levelBrightness, 0.0F, 1.0F);
/* 1206 */     this.vignetteBrightness += (brightness - this.vignetteBrightness) * 0.01F;
/*      */   }
/*      */   
/*      */   private void extractVignette(GuiGraphicsExtractor graphics, Entity camera) {
/*      */     int color;
/* 1210 */     WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
/* 1212 */     float borderWarningStrength = 0.0F;
/* 1214 */     if (camera != null) {
/* 1215 */       float distToBorder = (float)worldBorder.getDistanceToBorder(camera);
/* 1216 */       double movingBlocksThreshold = Math.min(worldBorder.getLerpSpeed() * worldBorder.getWarningTime(), Math.abs(worldBorder.getLerpTarget() - worldBorder.getSize()));
/* 1217 */       double warningDistance = Math.max(worldBorder.getWarningBlocks(), movingBlocksThreshold);
/* 1218 */       if (distToBorder < warningDistance)
/* 1219 */         borderWarningStrength = 1.0F - (float)(distToBorder / warningDistance); 
/*      */     } 
/* 1225 */     float brightness = Mth.clamp(this.vignetteBrightness, 0.0F, 1.0F);
/* 1226 */     if (borderWarningStrength > 0.0F) {
/* 1227 */       borderWarningStrength = Mth.clamp(borderWarningStrength, 0.0F, 1.0F);
/* 1228 */       float red = brightness * (1.0F - borderWarningStrength);
/* 1229 */       float greenBlue = brightness + (1.0F - brightness) * borderWarningStrength;
/* 1230 */       color = ARGB.colorFromFloat(1.0F, red, greenBlue, greenBlue);
/*      */     } else {
/* 1232 */       color = ARGB.colorFromFloat(1.0F, brightness, brightness, brightness);
/*      */     } 
/* 1235 */     graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void extractPortalOverlay(GuiGraphicsExtractor graphics, float alpha) {
/* 1239 */     if (alpha < 1.0F) {
/* 1240 */       alpha *= alpha;
/* 1241 */       alpha *= alpha;
/* 1242 */       alpha = alpha * 0.8F + 0.2F;
/*      */     } 
/* 1244 */     int color = ARGB.white(alpha);
/* 1246 */     TextureAtlasSprite slot = this.minecraft.getModelManager().getBlockStateModelSet().getParticleMaterial(Blocks.NETHER_PORTAL.defaultBlockState()).sprite();
/* 1247 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slot, 0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void extractConfusionOverlay(GuiGraphicsExtractor graphics, float strength) {
/* 1251 */     int screenWidth = graphics.guiWidth();
/* 1252 */     int screenHeight = graphics.guiHeight();
/* 1254 */     graphics.pose().pushMatrix();
/* 1255 */     float size = Mth.lerp(strength, 2.0F, 1.0F);
/* 1256 */     graphics.pose().translate(screenWidth / 2.0F, screenHeight / 2.0F);
/* 1257 */     graphics.pose().scale(size, size);
/* 1258 */     graphics.pose().translate(-screenWidth / 2.0F, -screenHeight / 2.0F);
/* 1260 */     float red = 0.2F * strength;
/* 1261 */     float green = 0.4F * strength;
/* 1262 */     float blue = 0.2F * strength;
/* 1264 */     graphics.blit(RenderPipelines.GUI_NAUSEA_OVERLAY, NAUSEA_LOCATION, 0, 0, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight, ARGB.colorFromFloat(1.0F, red, green, blue));
/* 1266 */     graphics.pose().popMatrix();
/*      */   }
/*      */   
/*      */   private void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed) {
/* 1270 */     if (itemStack.isEmpty())
/*      */       return; 
/* 1274 */     float pop = itemStack.getPopTime() - deltaTracker.getGameTimeDeltaPartialTick(false);
/* 1275 */     if (pop > 0.0F) {
/* 1276 */       float squeeze = 1.0F + pop / 5.0F;
/* 1278 */       graphics.pose().pushMatrix();
/* 1279 */       graphics.pose().translate((x + 8), (y + 12));
/* 1280 */       graphics.pose().scale(1.0F / squeeze, (squeeze + 1.0F) / 2.0F);
/* 1281 */       graphics.pose().translate(-(x + 8), -(y + 12));
/*      */     } 
/* 1284 */     graphics.item((LivingEntity)player, itemStack, x, y, seed);
/* 1286 */     if (pop > 0.0F)
/* 1287 */       graphics.pose().popMatrix(); 
/* 1289 */     graphics.itemDecorations(this.minecraft.font, itemStack, x, y);
/*      */   }
/*      */   
/*      */   public void tick(boolean pause) {
/* 1293 */     tickAutosaveIndicator();
/* 1294 */     if (!pause)
/* 1295 */       tick(); 
/*      */   }
/*      */   
/*      */   private void tick() {
/* 1300 */     if (this.overlayMessageTime > 0)
/* 1301 */       this.overlayMessageTime--; 
/* 1303 */     if (this.titleTime > 0) {
/* 1304 */       this.titleTime--;
/* 1305 */       if (this.titleTime <= 0) {
/* 1306 */         this.title = null;
/* 1307 */         this.subtitle = null;
/*      */       } 
/*      */     } 
/* 1310 */     this.tickCount++;
/* 1312 */     Entity camera = this.minecraft.getCameraEntity();
/* 1313 */     if (camera != null)
/* 1314 */       updateVignetteBrightness(camera); 
/* 1317 */     if (this.minecraft.player != null) {
/* 1318 */       ItemStack selected = this.minecraft.player.getInventory().getSelectedItem();
/* 1320 */       if (selected.isEmpty()) {
/* 1321 */         this.toolHighlightTimer = 0;
/* 1322 */       } else if (this.lastToolHighlight.isEmpty() || !selected.is(this.lastToolHighlight.getItem()) || !selected.getHoverName().equals(this.lastToolHighlight.getHoverName())) {
/* 1323 */         this.toolHighlightTimer = (int)(40.0D * ((Double)this.minecraft.options.notificationDisplayTime().get()).doubleValue());
/* 1324 */       } else if (this.toolHighlightTimer > 0) {
/* 1325 */         this.toolHighlightTimer--;
/*      */       } 
/* 1327 */       this.lastToolHighlight = selected;
/*      */     } 
/* 1330 */     this.chat.tick();
/*      */   }
/*      */   
/*      */   private void tickAutosaveIndicator() {
/* 1334 */     IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
/* 1335 */     boolean isAutosaving = (integratedServer != null && integratedServer.isCurrentlySaving());
/* 1336 */     this.lastAutosaveIndicatorValue = this.autosaveIndicatorValue;
/* 1337 */     this.autosaveIndicatorValue = Mth.lerp(0.2F, this.autosaveIndicatorValue, isAutosaving ? 1.0F : 0.0F);
/*      */   }
/*      */   
/*      */   public void setNowPlaying(Component string) {
/* 1341 */     MutableComponent mutableComponent = Component.translatable("record.nowPlaying", new Object[] { string });
/* 1342 */     setOverlayMessage((Component)mutableComponent, true);
/* 1343 */     this.minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/*      */   }
/*      */   
/*      */   public void setOverlayMessage(Component string, boolean animate) {
/* 1347 */     this.overlayMessageString = string;
/* 1348 */     this.overlayMessageTime = 60;
/* 1349 */     this.animateOverlayMessageColor = animate;
/*      */   }
/*      */   
/*      */   public void setTimes(int fadeInTime, int stayTime, int fadeOutTime) {
/* 1353 */     if (fadeInTime >= 0)
/* 1354 */       this.titleFadeInTime = fadeInTime; 
/* 1356 */     if (stayTime >= 0)
/* 1357 */       this.titleStayTime = stayTime; 
/* 1359 */     if (fadeOutTime >= 0)
/* 1360 */       this.titleFadeOutTime = fadeOutTime; 
/* 1362 */     if (this.titleTime > 0)
/* 1363 */       this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime; 
/*      */   }
/*      */   
/*      */   public void setSubtitle(Component subtitle) {
/* 1368 */     this.subtitle = subtitle;
/*      */   }
/*      */   
/*      */   public void setTitle(Component title) {
/* 1372 */     this.title = title;
/* 1373 */     this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
/*      */   }
/*      */   
/*      */   public void clearTitles() {
/* 1377 */     this.title = null;
/* 1378 */     this.subtitle = null;
/* 1379 */     this.titleTime = 0;
/*      */   }
/*      */   
/*      */   public ChatComponent getChat() {
/* 1383 */     return this.chat;
/*      */   }
/*      */   
/*      */   public WaypointStyleManager getWaypointStyles() {
/* 1387 */     return this.waypointStyles;
/*      */   }
/*      */   
/*      */   public int getGuiTicks() {
/* 1391 */     return this.tickCount;
/*      */   }
/*      */   
/*      */   public Font getFont() {
/* 1395 */     return this.minecraft.font;
/*      */   }
/*      */   
/*      */   public SpectatorGui getSpectatorGui() {
/* 1399 */     return this.spectatorGui;
/*      */   }
/*      */   
/*      */   public PlayerTabOverlay getTabList() {
/* 1403 */     return this.tabList;
/*      */   }
/*      */   
/*      */   public void onDisconnected() {
/* 1407 */     this.tabList.reset();
/* 1408 */     this.bossOverlay.reset();
/* 1409 */     this.minecraft.gui.toastManager().clear();
/* 1410 */     this.debugOverlay.reset();
/* 1411 */     this.chat.clearMessages(true);
/* 1412 */     clearTitles();
/* 1413 */     resetTitleTimes();
/*      */   }
/*      */   
/*      */   public BossHealthOverlay getBossOverlay() {
/* 1417 */     return this.bossOverlay;
/*      */   }
/*      */   
/*      */   public DebugScreenOverlay getDebugOverlay() {
/* 1421 */     return this.debugOverlay;
/*      */   }
/*      */   
/*      */   public void clearCache() {
/* 1425 */     this.debugOverlay.clearChunkCache();
/*      */   }
/*      */   
/*      */   public void extractSavingIndicator(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/* 1429 */     if (((Boolean)this.minecraft.options.showAutosaveIndicator().get()).booleanValue() && (this.autosaveIndicatorValue > 0.0F || this.lastAutosaveIndicatorValue > 0.0F)) {
/* 1430 */       int alpha = Mth.floor(255.0F * Mth.clamp(Mth.lerp(deltaTracker.getRealtimeDeltaTicks(), this.lastAutosaveIndicatorValue, this.autosaveIndicatorValue), 0.0F, 1.0F));
/* 1431 */       if (alpha > 0) {
/* 1432 */         Font font = getFont();
/* 1433 */         int width = font.width((FormattedText)SAVING_TEXT);
/* 1434 */         int color = ARGB.color(alpha, -1);
/* 1435 */         int textX = graphics.guiWidth() - width - 5;
/* 1436 */         Objects.requireNonNull(font);
/* 1436 */         int textY = graphics.guiHeight() - 9 - 5;
/* 1437 */         graphics.nextStratum();
/* 1438 */         graphics.textWithBackdrop(font, SAVING_TEXT, textX, textY, width, color);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean willPrioritizeExperienceInfo() {
/* 1444 */     return (this.minecraft.player.experienceDisplayStartTick + 100 > this.minecraft.player.tickCount);
/*      */   }
/*      */   
/*      */   private boolean willPrioritizeJumpInfo() {
/* 1448 */     return (this.minecraft.player.getJumpRidingScale() > 0.0F || ((Integer)Optionull.mapOrDefault(this.minecraft.player.jumpableVehicle(), PlayerRideableJumping::getJumpCooldown, Integer.valueOf(0))).intValue() > 0);
/*      */   }
/*      */   
/*      */   private ContextualInfo nextContextualInfoState() {
/* 1452 */     boolean canShowLocatorInfo = this.minecraft.player.connection.getWaypointManager().hasWaypoints();
/* 1453 */     boolean canShowVehicleJumpInfo = (this.minecraft.player.jumpableVehicle() != null);
/* 1454 */     boolean canShowExperienceInfo = this.minecraft.gameMode.hasExperience();
/* 1456 */     if (canShowLocatorInfo) {
/* 1457 */       if (canShowVehicleJumpInfo && willPrioritizeJumpInfo())
/* 1458 */         return ContextualInfo.JUMPABLE_VEHICLE; 
/* 1460 */       if (canShowExperienceInfo && willPrioritizeExperienceInfo())
/* 1461 */         return ContextualInfo.EXPERIENCE; 
/* 1463 */       return ContextualInfo.LOCATOR;
/*      */     } 
/* 1466 */     if (canShowVehicleJumpInfo)
/* 1467 */       return ContextualInfo.JUMPABLE_VEHICLE; 
/* 1469 */     if (canShowExperienceInfo)
/* 1470 */       return ContextualInfo.EXPERIENCE; 
/* 1472 */     return ContextualInfo.EMPTY;
/*      */   }
/*      */   
/*      */   private enum ContextualInfo {
/* 1476 */     EMPTY, EXPERIENCE, LOCATOR, JUMPABLE_VEHICLE;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Hud.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */