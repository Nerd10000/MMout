/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.io.Files;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.google.gson.reflect.TypeToken;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.VideoMode;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*      */ import net.minecraft.client.input.InputQuirks;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*      */ import net.minecraft.client.renderer.extract.LevelExtractor;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.sounds.MusicManager;
/*      */ import net.minecraft.client.sounds.SoundEngine;
/*      */ import net.minecraft.client.sounds.SoundManager;
/*      */ import net.minecraft.client.sounds.SoundPreviewHandler;
/*      */ import net.minecraft.client.tutorial.TutorialSteps;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.StringTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.level.ParticleStatus;
/*      */ import net.minecraft.server.packs.repository.Pack;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.GsonHelper;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.PlayerModelPart;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class Options {
/*   82 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*   83 */   private static final Gson GSON = new Gson();
/*      */   
/*   84 */   private static final TypeToken<List<String>> LIST_OF_STRINGS_TYPE = new TypeToken<List<String>>() {
/*      */     
/*      */     };
/*      */   
/*      */   public static final int RENDER_DISTANCE_SHORT = 4;
/*      */   
/*      */   public static final int RENDER_DISTANCE_FAR = 12;
/*      */   
/*      */   public static final int RENDER_DISTANCE_REALLY_FAR = 16;
/*      */   
/*      */   public static final int RENDER_DISTANCE_EXTREME = 32;
/*      */   
/*   91 */   private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);
/*      */   
/*      */   private static final String DEFAULT_SOUND_DEVICE = "";
/*      */   
/*   94 */   private static final Component TOOLTIP_NEEDS_RESTART = (Component)Component.translatable("options.needsRestart");
/*      */   
/*      */   public static boolean isSoundDeviceDefault(String deviceName) {
/*   97 */     return deviceName.equals("");
/*      */   }
/*      */   
/*      */   private static void operateOnLevelExtractor(Consumer<LevelExtractor> consumer) {
/*  101 */     LevelExtractor levelExtractor = (Minecraft.getInstance()).levelExtractor;
/*  102 */     if (levelExtractor != null)
/*  103 */       consumer.accept(levelExtractor); 
/*      */   }
/*      */   
/*  107 */   private static final Component ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND = (Component)Component.translatable("options.darkMojangStudiosBackgroundColor.tooltip");
/*      */   
/*  108 */   private final OptionInstance<Boolean> darkMojangStudiosBackground = OptionInstance.createBoolean("options.darkMojangStudiosBackgroundColor", 
/*      */       
/*  110 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND), false);
/*      */   
/*      */   public OptionInstance<Boolean> darkMojangStudiosBackground() {
/*  115 */     return this.darkMojangStudiosBackground;
/*      */   }
/*      */   
/*  118 */   private static final Component ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES = (Component)Component.translatable("options.hideLightningFlashes.tooltip");
/*      */   
/*  119 */   private final OptionInstance<Boolean> hideLightningFlash = OptionInstance.createBoolean("options.hideLightningFlashes", 
/*      */       
/*  121 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES), false);
/*      */   
/*      */   public OptionInstance<Boolean> hideLightningFlash() {
/*  126 */     return this.hideLightningFlash;
/*      */   }
/*      */   
/*  129 */   private static final Component ACCESSIBILITY_TOOLTIP_HIDE_SPLASH_TEXTS = (Component)Component.translatable("options.hideSplashTexts.tooltip");
/*      */   
/*  130 */   private final OptionInstance<Boolean> hideSplashTexts = OptionInstance.createBoolean("options.hideSplashTexts", 
/*      */       
/*  132 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_HIDE_SPLASH_TEXTS), false);
/*      */   
/*      */   private final OptionInstance<Double> sensitivity;
/*      */   
/*      */   private final OptionInstance<Integer> renderDistance;
/*      */   
/*      */   private final OptionInstance<Integer> simulationDistance;
/*      */   
/*      */   private int serverRenderDistance;
/*      */   
/*      */   private final OptionInstance<Double> entityDistanceScaling;
/*      */   
/*      */   public static final int UNLIMITED_FRAMERATE_CUTOFF = 260;
/*      */   
/*      */   private final OptionInstance<Integer> framerateLimit;
/*      */   
/*      */   private PreferredGraphicsApi preferredGraphicsBackendFromStartup;
/*      */   
/*      */   public OptionInstance<Boolean> hideSplashTexts() {
/*  137 */     return this.hideSplashTexts;
/*      */   }
/*      */   
/*      */   public Options(Minecraft minecraft, File workingDirectory) {
/*  140 */     this
/*      */       
/*  153 */       .sensitivity = new OptionInstance("options.sensitivity", OptionInstance.noTooltip(), (caption, value) -> (value.doubleValue() == 0.0D) ? genericValueLabel(caption, (Component)Component.translatable("options.sensitivity.min")) : ((value.doubleValue() == 1.0D) ? genericValueLabel(caption, (Component)Component.translatable("options.sensitivity.max")) : percentValueLabel(caption, 2.0D * value.doubleValue())), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.5D), OptionInstance.NO_ACTION);
/*  173 */     this.serverRenderDistance = 0;
/*  175 */     this
/*      */       
/*  181 */       .entityDistanceScaling = new OptionInstance<>("options.entityDistanceScaling", OptionInstance.noTooltip(), Options::percentValueLabel, (new OptionInstance.IntRange(2, 20)).xmap(value -> Double.valueOf(value / 4.0D), value -> (int)(value.doubleValue() * 4.0D), true), Codec.doubleRange(0.5D, 5.0D), Double.valueOf(1.0D), paramDouble -> setGraphicsPresetToCustom());
/*  191 */     this
/*      */       
/*  203 */       .framerateLimit = new OptionInstance<>("options.framerateLimit", OptionInstance.noTooltip(), (caption, value) -> (value.intValue() == 260) ? genericValueLabel(caption, (Component)Component.translatable("options.framerateLimit.max")) : genericValueLabel(caption, (Component)Component.translatable("options.framerate", new Object[] { value })), (new OptionInstance.IntRange(1, 26)).xmap(value -> Integer.valueOf(value * 10), value -> value.intValue() / 10, true), Codec.intRange(10, 260), Integer.valueOf(120), value -> Minecraft.getInstance().getFramerateLimitTracker().setFramerateLimit(value.intValue()));
/*  211 */     this.preferredGraphicsBackendFromStartup = PreferredGraphicsApi.DEFAULT;
/*  216 */     this
/*      */       
/*  232 */       .preferredGraphicsBackend = new OptionInstance("options.graphicsApi", value -> {
/*      */           List<Component> tooltipLines = new ArrayList<>();
/*      */           if (value != this.preferredGraphicsBackendFromStartup) {
/*      */             tooltipLines.add(TOOLTIP_NEEDS_RESTART);
/*      */             tooltipLines.add(CommonComponents.EMPTY);
/*      */           } 
/*      */           tooltipLines.add(GRAPHICS_API_TOOLTIP);
/*      */           if (value == PreferredGraphicsApi.VULKAN) {
/*      */             tooltipLines.add(CommonComponents.EMPTY);
/*      */             tooltipLines.add(GRAPHICS_API_TOOLTIP_VULKAN);
/*      */           } 
/*      */           return Tooltip.create(CommonComponents.joinLines(tooltipLines));
/*  232 */         }(caption, value) -> value.caption(), new OptionInstance.Enum<>(List.of(PreferredGraphicsApi.values()), PreferredGraphicsApi.CODEC), PreferredGraphicsApi.CODEC, PreferredGraphicsApi.DEFAULT, OptionInstance.NO_ACTION);
/*  248 */     this
/*      */       
/*  252 */       .graphicsPreset = new OptionInstance<>("options.graphics.preset", OptionInstance.cachedConstantTooltip((Component)Component.translatable("options.graphics.preset.tooltip")), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable(value.getKey())), new OptionInstance.SliderableEnum<>(List.of(GraphicsPreset.values()), GraphicsPreset.CODEC), GraphicsPreset.CODEC, GraphicsPreset.FANCY, this::applyGraphicsPreset);
/*  271 */     this
/*      */       
/*  278 */       .inactivityFpsLimit = new OptionInstance("options.inactivityFpsLimit", value -> {
/*      */           switch (value) {
/*      */             default:
/*      */               throw new MatchException(null, null);
/*      */             case NONE:
/*      */             
/*      */             case RGSS:
/*      */               break;
/*      */           } 
/*      */           return Tooltip.create(INACTIVITY_FPS_LIMIT_TOOLTIP_AFK);
/*  278 */         }(caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(InactivityFpsLimit.values()), InactivityFpsLimit.CODEC), InactivityFpsLimit.AFK, OptionInstance.NO_ACTION);
/*  289 */     this
/*      */       
/*  294 */       .cloudStatus = new OptionInstance<>("options.renderClouds", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(CloudStatus.values()), Codec.withAlternative(CloudStatus.CODEC, (Codec)Codec.BOOL, b -> b.booleanValue() ? CloudStatus.FANCY : CloudStatus.OFF)), CloudStatus.FANCY, paramCloudStatus -> setGraphicsPresetToCustom());
/*  308 */     this
/*      */       
/*  317 */       .cloudRange = new OptionInstance<>("options.renderCloudsDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(2, 128, true), Integer.valueOf(128), paramInteger -> {
/*      */           operateOnLevelExtractor(LevelExtractor::allChanged);
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*  330 */     this
/*      */       
/*  339 */       .weatherRadius = new OptionInstance<>("options.weatherRadius", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_WEATHER_RADIUS), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.blocks", new Object[] { value })), new OptionInstance.IntRange(3, 10, true), Integer.valueOf(10), paramInteger -> setGraphicsPresetToCustom());
/*  349 */     this.cutoutLeaves = OptionInstance.createBoolean("options.cutoutLeaves", 
/*      */         
/*  351 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_CUTOUT_LEAVES), true, paramBoolean -> {
/*      */           operateOnLevelExtractor(LevelExtractor::allChanged);
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*  365 */     this.vignette = OptionInstance.createBoolean("options.vignette", 
/*      */         
/*  367 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_VIGNETTE), true);
/*  377 */     this.improvedTransparency = OptionInstance.createBoolean("options.improvedTransparency", 
/*      */         
/*  379 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_IMPROVED_TRANSPARENCY), false, value -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */           GpuWarnlistManager gpuWarnlistManager = minecraft.getGpuWarnlistManager();
/*      */           if (!this.isApplyingGraphicsPreset && value.booleanValue() && gpuWarnlistManager.willShowWarning()) {
/*      */             gpuWarnlistManager.showWarning();
/*      */             return;
/*      */           } 
/*      */           operateOnLevelExtractor(LevelExtractor::allChanged);
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*  401 */     this.ambientOcclusion = OptionInstance.createBoolean("options.ao", true, paramBoolean -> {
/*      */           operateOnLevelExtractor(LevelExtractor::allChanged);
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*  416 */     this
/*      */       
/*  427 */       .chunkSectionFadeInTime = new OptionInstance("options.chunkFade", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_CHUNK_FADE), (caption, value) -> (value.doubleValue() <= 0.0D) ? Component.translatable("options.chunkFade.none") : Component.translatable("options.chunkFade.seconds", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { value }) }), (new OptionInstance.IntRange(0, 40)).xmap(value -> Double.valueOf(value / 20.0D), value -> (int)(value.doubleValue() * 20.0D), true), Codec.doubleRange(0.0D, 2.0D), Double.valueOf(0.75D), OptionInstance.NO_ACTION);
/*  439 */     this
/*      */       
/*  447 */       .prioritizeChunkUpdates = new OptionInstance<>("options.prioritizeChunkUpdates", value -> {
/*      */           switch (value) {
/*      */             default:
/*      */               throw new MatchException(null, null);
/*      */             case NONE:
/*      */             
/*      */             case RGSS:
/*      */             
/*      */             case ANISOTROPIC:
/*      */               break;
/*      */           } 
/*      */           return Tooltip.create(PRIORITIZE_CHUNK_TOOLTIP_NEARBY);
/*  447 */         }(caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(PrioritizeChunkUpdates.values()), PrioritizeChunkUpdates.LEGACY_CODEC), PrioritizeChunkUpdates.NONE, paramPrioritizeChunkUpdates -> setGraphicsPresetToCustom());
/*  456 */     this.resourcePacks = Lists.newArrayList();
/*  457 */     this.incompatibleResourcePacks = Lists.newArrayList();
/*  478 */     this
/*      */       
/*  482 */       .chatVisibility = new OptionInstance<>("options.chat.visibility", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(ChatVisiblity.values()), ChatVisiblity.LEGACY_CODEC), ChatVisiblity.FULL, paramChatVisiblity -> {
/*      */           LocalPlayer player = (Minecraft.getInstance()).player;
/*      */           if (player != null)
/*      */             player.refreshChatAbilities(); 
/*      */         });
/*  496 */     this
/*      */       
/*  501 */       .chatOpacity = new OptionInstance<>("options.chat.opacity", OptionInstance.noTooltip(), (caption, value) -> percentValueLabel(caption, value.doubleValue() * 0.9D + 0.1D), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  509 */     this
/*      */       
/*  514 */       .chatLineSpacing = new OptionInstance("options.chat.line_spacing", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.0D), OptionInstance.NO_ACTION);
/*  526 */     this
/*      */       
/*  534 */       .menuBackgroundBlurriness = new OptionInstance<>("options.accessibility.menu_background_blurriness", OptionInstance.cachedConstantTooltip(MENU_BACKGROUND_BLURRINESS_TOOLTIP), Options::genericValueOrOffLabel, new OptionInstance.IntRange(0, 10), Integer.valueOf(5), paramInteger -> setGraphicsPresetToCustom());
/*  547 */     this
/*      */       
/*  552 */       .textBackgroundOpacity = new OptionInstance<>("options.accessibility.text_background_opacity", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.5D), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  560 */     this
/*      */       
/*  565 */       .panoramaSpeed = new OptionInstance("options.accessibility.panorama_speed", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/*  574 */     this.highContrast = OptionInstance.createBoolean("options.accessibility.high_contrast", 
/*      */         
/*  576 */         OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_CONTRAST_MODE), false, value -> {
/*      */           PackRepository packRepo = Minecraft.getInstance().getResourcePackRepository();
/*      */           boolean isSelected = packRepo.getSelectedIds().contains("high_contrast");
/*      */           if (!isSelected && value.booleanValue()) {
/*      */             if (packRepo.addPack("high_contrast"))
/*      */               updateResourcePacks(packRepo); 
/*      */           } else if (isSelected && !value.booleanValue() && packRepo.removePack("high_contrast")) {
/*      */             updateResourcePacks(packRepo);
/*      */           } 
/*      */         });
/*  598 */     this.highContrastBlockOutline = OptionInstance.createBoolean("options.accessibility.high_contrast_block_outline", 
/*      */         
/*  600 */         OptionInstance.cachedConstantTooltip(HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP), false);
/*  607 */     this.narratorHotkey = OptionInstance.createBoolean("options.accessibility.narrator_hotkey", 
/*      */         
/*  609 */         OptionInstance.cachedConstantTooltip(
/*  610 */           InputQuirks.REPLACE_CTRL_KEY_WITH_CMD_KEY ? 
/*  611 */           (Component)Component.translatable("options.accessibility.narrator_hotkey.mac.tooltip") : 
/*  612 */           (Component)Component.translatable("options.accessibility.narrator_hotkey.tooltip")), true);
/*  624 */     this.pauseOnLostFocus = true;
/*  625 */     this.modelParts = EnumSet.allOf(PlayerModelPart.class);
/*  626 */     this
/*      */       
/*  630 */       .mainHand = new OptionInstance("options.mainHand", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum(Arrays.asList((Object[])HumanoidArm.values()), HumanoidArm.CODEC), HumanoidArm.RIGHT, OptionInstance.NO_ACTION);
/*  642 */     this
/*      */       
/*  652 */       .chatScale = new OptionInstance<>("options.chat.scale", OptionInstance.noTooltip(), (caption, value) -> (value.doubleValue() == 0.0D) ? CommonComponents.optionStatus(caption, false) : percentValueLabel(caption, value.doubleValue()), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  660 */     this
/*      */       
/*  665 */       .chatWidth = new OptionInstance<>("options.chat.width", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getWidth(value.doubleValue())), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  673 */     this
/*      */       
/*  678 */       .chatHeightUnfocused = new OptionInstance<>("options.chat.height.unfocused", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getHeight(value.doubleValue())), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(ChatComponent.defaultUnfocusedPct()), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  686 */     this
/*      */       
/*  691 */       .chatHeightFocused = new OptionInstance<>("options.chat.height.focused", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getHeight(value.doubleValue())), OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), paramDouble -> (Minecraft.getInstance()).gui.hud.getChat().rescaleChat());
/*  699 */     this
/*      */       
/*  710 */       .chatDelay = new OptionInstance<>("options.chat.delay_instant", OptionInstance.noTooltip(), (caption, value) -> (value.doubleValue() <= 0.0D) ? Component.translatable("options.chat.delay_none") : Component.translatable("options.chat.delay", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { value }) }), (new OptionInstance.IntRange(0, 60)).xmap(value -> Double.valueOf(value / 10.0D), value -> (int)(value.doubleValue() * 10.0D), true), Codec.doubleRange(0.0D, 6.0D), Double.valueOf(0.0D), value -> (Minecraft.getInstance()).gui.chatListener().setMessageDelay(value.doubleValue()));
/*  719 */     this
/*      */       
/*  725 */       .notificationDisplayTime = new OptionInstance("options.notifications.display_time", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_NOTIFICATION_DISPLAY_TIME), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.multiplier", new Object[] { value })), (new OptionInstance.IntRange(5, 100)).xmap(value -> Double.valueOf(value / 10.0D), value -> (int)(value.doubleValue() * 10.0D), true), Codec.doubleRange(0.5D, 10.0D), Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/*  733 */     this
/*      */       
/*  743 */       .mipmapLevels = new OptionInstance<>("options.mipmapLevels", OptionInstance.noTooltip(), (caption, value) -> (value.intValue() == 0) ? CommonComponents.optionStatus(caption, false) : genericValueLabel(caption, value.intValue()), new OptionInstance.IntRange(0, 4), Integer.valueOf(4), paramInteger -> setGraphicsPresetToCustom());
/*  753 */     this
/*      */       
/*  763 */       .maxAnisotropyBit = new OptionInstance<>("options.maxAnisotropy", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_ANISOTROPIC_FILTERING), (caption, value) -> (value.intValue() == 0) ? CommonComponents.optionStatus(caption, false) : genericValueLabel(caption, (Component)Component.translatable("options.multiplier", new Object[] { Integer.toString(1 << value.intValue()) })), new OptionInstance.IntRange(1, 3), Integer.valueOf(2), paramInteger -> {
/*      */           setGraphicsPresetToCustom();
/*      */           operateOnLevelExtractor(LevelExtractor::resetSampler);
/*      */         });
/*  782 */     this
/*      */       
/*  790 */       .textureFiltering = new OptionInstance<>("options.textureFiltering", value -> {
/*      */           switch (value) {
/*      */             default:
/*      */               throw new MatchException(null, null);
/*      */             case NONE:
/*      */             
/*      */             case RGSS:
/*      */             
/*      */             case ANISOTROPIC:
/*      */               break;
/*      */           } 
/*      */           return Tooltip.create(FILTERING_ANISOTROPIC_TOOLTIP);
/*  790 */         }(caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(TextureFilteringMethod.values()), TextureFilteringMethod.LEGACY_CODEC), TextureFilteringMethod.NONE, paramTextureFilteringMethod -> {
/*      */           setGraphicsPresetToCustom();
/*      */           operateOnLevelExtractor(LevelExtractor::resetSampler);
/*      */         });
/*  802 */     this.useNativeTransport = true;
/*  804 */     this
/*      */       
/*  808 */       .attackIndicator = new OptionInstance("options.attackIndicator", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(AttackIndicatorStatus.values()), AttackIndicatorStatus.LEGACY_CODEC), AttackIndicatorStatus.CROSSHAIR, OptionInstance.NO_ACTION);
/*  817 */     this.tutorialStep = TutorialSteps.MOVEMENT;
/*  818 */     this.joinedFirstServer = false;
/*  820 */     this
/*      */       
/*  828 */       .biomeBlendRadius = new OptionInstance<>("options.biomeBlendRadius", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           int dist = value.intValue() * 2 + 1;
/*      */           return genericValueLabel(caption, (Component)Component.translatable("options.biomeBlendRadius." + dist));
/*  828 */         }new OptionInstance.IntRange(0, 7, false), Integer.valueOf(2), paramInteger -> {
/*      */           operateOnLevelExtractor(LevelExtractor::allChanged);
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*  847 */     this
/*      */       
/*  853 */       .mouseWheelSensitivity = new OptionInstance("options.mouseWheelSensitivity", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.literal(String.format(Locale.ROOT, "%.2f", new Object[] { value }))), (new OptionInstance.IntRange(-200, 100)).xmap(Options::logMouse, Options::unlogMouse, false), Codec.doubleRange(logMouse(-200), logMouse(100)), Double.valueOf(logMouse(0)), OptionInstance.NO_ACTION);
/*  861 */     this.rawMouseInput = OptionInstance.createBoolean("options.rawMouseInput", true, value -> {
/*      */           Window window = Minecraft.getInstance().getWindow();
/*      */           if (window != null)
/*      */             window.updateRawMouseInput(value.booleanValue()); 
/*      */         });
/*  877 */     this.allowCursorChanges = OptionInstance.createBoolean("options.allowCursorChanges", 
/*      */         
/*  879 */         OptionInstance.cachedConstantTooltip(ALLOW_CURSOR_CHANGES_TOOLTIP), true, value -> {
/*      */           Window window = Minecraft.getInstance().getWindow();
/*      */           if (window != null)
/*      */             window.setAllowCursorChanges(value.booleanValue()); 
/*      */         });
/*  893 */     this.glDebugVerbosity = 1;
/*  895 */     this.autoJump = OptionInstance.createBoolean("options.autoJump", false);
/*  902 */     this.rotateWithMinecart = OptionInstance.createBoolean("options.rotateWithMinecart", 
/*      */         
/*  904 */         OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_ROTATE_WITH_MINECART), false);
/*  912 */     this.operatorItemsTab = OptionInstance.createBoolean("options.operatorItemsTab", false);
/*  918 */     this.autoSuggestions = OptionInstance.createBoolean("options.autoSuggestCommands", true);
/*  924 */     this.chatColors = OptionInstance.createBoolean("options.chat.color", true);
/*  930 */     this.chatLinks = OptionInstance.createBoolean("options.chat.links", true);
/*  936 */     this.chatLinksPrompt = OptionInstance.createBoolean("options.chat.links.prompt", true);
/*  942 */     this.enableVsync = OptionInstance.createBoolean("options.vsync", true, paramBoolean -> Minecraft.getInstance().invalidateSurfaceConfiguration());
/*  950 */     this.entityShadows = OptionInstance.createBoolean("options.entityShadows", 
/*      */         
/*  952 */         OptionInstance.noTooltip(), true, paramBoolean -> setGraphicsPresetToCustom());
/*  971 */     this.forceUnicodeFont = OptionInstance.createBoolean("options.forceUnicodeFont", false, paramBoolean -> updateFontOptions());
/*  982 */     this.japaneseGlyphVariants = OptionInstance.createBoolean("options.japaneseGlyphVariants", 
/*  983 */         OptionInstance.cachedConstantTooltip((Component)Component.translatable("options.japaneseGlyphVariants.tooltip")), 
/*  984 */         japaneseGlyphVariantsDefault(), paramBoolean -> updateFontOptions());
/*  992 */     this.invertXMouse = OptionInstance.createBoolean("options.invertMouseX", false);
/*  998 */     this.invertYMouse = OptionInstance.createBoolean("options.invertMouseY", false);
/* 1004 */     this.discreteMouseScroll = OptionInstance.createBoolean("options.discrete_mouse_scroll", false);
/* 1012 */     this.realmsNotifications = OptionInstance.createBoolean("options.realmsNotifications.button", 
/* 1013 */         OptionInstance.cachedConstantTooltip(REALMS_NOTIFICATIONS_TOOLTIP), true);
/* 1022 */     this.allowServerListing = OptionInstance.createBoolean("options.allowServerListing", 
/*      */         
/* 1024 */         OptionInstance.cachedConstantTooltip(ALLOW_SERVER_LISTING_TOOLTIP), true, (OptionInstance.ValueUpdateListener)OptionInstance.NO_ACTION);
/* 1033 */     this.reducedDebugInfo = OptionInstance.createBoolean("options.reducedDebugInfo", OptionInstance.noTooltip(), false, paramBoolean -> (Minecraft.getInstance()).debugEntries.rebuildCurrentList());
/* 1040 */     this.inGameNotification = OptionInstance.createBoolean("options.inGameNotification", 
/*      */         
/* 1042 */         OptionInstance.cachedConstantTooltip(IN_GAME_NOTIFICATION_TOOLTIP), false);
/* 1050 */     this
/*      */       
/* 1054 */       .sharePresence = new OptionInstance<>("options.sharePresence", value -> Tooltip.create(value.getTooltip()), (paramComponent, value) -> value.getTranslation(), new OptionInstance.Enum<>(List.of(PresenceSharing.values()), PresenceSharing.CODEC), PresenceSharing.CODEC, PresenceSharing.ALL, paramPresenceSharing -> {
/*      */         
/*      */         });
/* 1064 */     this.soundSourceVolumes = Util.makeEnumMap(SoundSource.class, source -> createSoundSliderOptionInstance("soundCategory." + source.getName(), source));
/* 1106 */     this.showSubtitles = OptionInstance.createBoolean("options.showSubtitles", 
/* 1107 */         OptionInstance.cachedConstantTooltip(CLOSED_CAPTIONS_TOOLTIP), false);
/* 1117 */     this.directionalAudio = OptionInstance.createBoolean("options.directionalAudio", value -> value.booleanValue() ? Tooltip.create(DIRECTIONAL_AUDIO_TOOLTIP_ON) : Tooltip.create(DIRECTIONAL_AUDIO_TOOLTIP_OFF), false, paramBoolean -> {
/*      */           SoundManager soundManager = Minecraft.getInstance().getSoundManager();
/*      */           soundManager.reload();
/*      */           soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*      */         });
/* 1132 */     this
/*      */       
/* 1137 */       .backgroundForChatOnly = new OptionInstance("options.accessibility.text_background", OptionInstance.noTooltip(), (caption, value) -> value.booleanValue() ? (Component)Component.translatable("options.accessibility.text_background.chat") : (Component)Component.translatable("options.accessibility.text_background.everywhere"), OptionInstance.BOOLEAN_VALUES, Boolean.valueOf(true), OptionInstance.NO_ACTION);
/* 1145 */     this.fullscreen = OptionInstance.createBoolean("options.fullscreen", false, value -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */           if (minecraft.getWindow() != null && minecraft.getWindow().isFullscreen() != value.booleanValue()) {
/*      */             minecraft.getWindow().toggleFullScreen();
/*      */             fullscreen().set(Boolean.valueOf(minecraft.getWindow().isFullscreen()));
/*      */           } 
/*      */         });
/* 1162 */     this.exclusiveFullscreen = OptionInstance.createBoolean("options.exclusiveFullscreen", value -> {
/*      */           List<Component> tooltipLines = new ArrayList<>();
/*      */           if (value.booleanValue() != this.exclusiveFullscreenFromStartup) {
/*      */             tooltipLines.add(TOOLTIP_NEEDS_RESTART);
/*      */             tooltipLines.add(CommonComponents.EMPTY);
/*      */           } 
/*      */           tooltipLines.add(value.booleanValue() ? TOOLTIP_EXCLUSIVE_FULLSCREEN_ON : TOOLTIP_EXCLUSIVE_FULLSCREEN_OFF);
/*      */           return Tooltip.create(CommonComponents.joinLines(tooltipLines));
/* 1162 */         }false);
/* 1178 */     this.bobView = OptionInstance.createBoolean("options.viewBobbing", true);
/* 1187 */     this
/*      */       
/* 1192 */       .toggleCrouch = new OptionInstance("key.sneak", OptionInstance.noTooltip(), (caption, value) -> value.booleanValue() ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, Boolean.valueOf(false), OptionInstance.NO_ACTION);
/* 1200 */     this
/*      */       
/* 1205 */       .toggleSprint = new OptionInstance("key.sprint", OptionInstance.noTooltip(), (caption, value) -> value.booleanValue() ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, Boolean.valueOf(false), OptionInstance.NO_ACTION);
/* 1213 */     this
/*      */       
/* 1218 */       .toggleAttack = new OptionInstance("key.attack", OptionInstance.noTooltip(), (caption, value) -> value.booleanValue() ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, Boolean.valueOf(false), OptionInstance.NO_ACTION);
/* 1226 */     this
/*      */       
/* 1231 */       .toggleUse = new OptionInstance("key.use", OptionInstance.noTooltip(), (caption, value) -> value.booleanValue() ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, Boolean.valueOf(false), OptionInstance.NO_ACTION);
/* 1241 */     this
/*      */       
/* 1251 */       .sprintWindow = new OptionInstance("options.sprintWindow", OptionInstance.cachedConstantTooltip(SPRINT_WINDOW_TOOLTIP), (caption, value) -> (value.intValue() == 0) ? genericValueLabel(caption, (Component)Component.translatable("options.off")) : genericValueLabel(caption, (Component)Component.translatable("options.value", new Object[] { value })), new OptionInstance.IntRange(0, 10), Integer.valueOf(7), OptionInstance.NO_ACTION);
/* 1263 */     this.hideMatchedNames = OptionInstance.createBoolean("options.hideMatchedNames", 
/*      */         
/* 1265 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_HIDE_MATCHED_NAMES), true);
/* 1273 */     this.showAutosaveIndicator = OptionInstance.createBoolean("options.autosaveIndicator", true);
/* 1281 */     this.onlyShowSecureChat = OptionInstance.createBoolean("options.onlyShowSecureChat", 
/*      */         
/* 1283 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_ONLY_SHOW_SECURE), false);
/* 1293 */     this.saveChatDrafts = OptionInstance.createBoolean("options.chat.drafts", 
/*      */         
/* 1295 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_SAVE_DRAFTS), false);
/* 1313 */     this.keyUp = new KeyMapping("key.forward", 87, KeyMapping.Category.MOVEMENT);
/* 1314 */     this.keyLeft = new KeyMapping("key.left", 65, KeyMapping.Category.MOVEMENT);
/* 1315 */     this.keyDown = new KeyMapping("key.back", 83, KeyMapping.Category.MOVEMENT);
/* 1316 */     this.keyRight = new KeyMapping("key.right", 68, KeyMapping.Category.MOVEMENT);
/* 1317 */     this.keyJump = new KeyMapping("key.jump", 32, KeyMapping.Category.MOVEMENT);
/* 1320 */     Objects.requireNonNull(this.toggleCrouch);
/* 1320 */     this.keyShift = new ToggleKeyMapping("key.sneak", 340, KeyMapping.Category.MOVEMENT, this.toggleCrouch::get, true);
/* 1321 */     Objects.requireNonNull(this.toggleSprint);
/* 1321 */     this.keySprint = new ToggleKeyMapping("key.sprint", 341, KeyMapping.Category.MOVEMENT, this.toggleSprint::get, true);
/* 1323 */     this.keyInventory = new KeyMapping("key.inventory", 69, KeyMapping.Category.INVENTORY);
/* 1324 */     this.keySwapOffhand = new KeyMapping("key.swapOffhand", 70, KeyMapping.Category.INVENTORY);
/* 1325 */     this.keyDrop = new KeyMapping("key.drop", 81, KeyMapping.Category.INVENTORY);
/* 1327 */     Objects.requireNonNull(this.toggleUse);
/* 1327 */     this.keyUse = new ToggleKeyMapping("key.use", InputConstants.Type.MOUSE, 1, KeyMapping.Category.GAMEPLAY, this.toggleUse::get, false);
/* 1328 */     Objects.requireNonNull(this.toggleAttack);
/* 1328 */     this.keyAttack = new ToggleKeyMapping("key.attack", InputConstants.Type.MOUSE, 0, KeyMapping.Category.GAMEPLAY, this.toggleAttack::get, true);
/* 1329 */     this.keyPickItem = new KeyMapping("key.pickItem", InputConstants.Type.MOUSE, 2, KeyMapping.Category.GAMEPLAY);
/* 1331 */     this.keyChat = new KeyMapping("key.chat", 84, KeyMapping.Category.MULTIPLAYER);
/* 1332 */     this.keyPlayerList = new KeyMapping("key.playerlist", 258, KeyMapping.Category.MULTIPLAYER);
/* 1333 */     this.keyCommand = new KeyMapping("key.command", 47, KeyMapping.Category.MULTIPLAYER);
/* 1334 */     this.keyFriends = new KeyMapping("key.friends", 79, KeyMapping.Category.MULTIPLAYER);
/* 1335 */     this.keySocialInteractions = new KeyMapping("key.socialInteractions", 80, KeyMapping.Category.MULTIPLAYER);
/* 1337 */     this.keyScreenshot = new KeyMapping("key.screenshot", 291, KeyMapping.Category.MISC);
/* 1338 */     this.keyTogglePerspective = new KeyMapping("key.togglePerspective", 294, KeyMapping.Category.MISC);
/* 1339 */     this.keySmoothCamera = new KeyMapping("key.smoothCamera", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.MISC);
/* 1340 */     this.keyFullscreen = new KeyMapping("key.fullscreen", 300, KeyMapping.Category.MISC);
/* 1341 */     this.keyAdvancements = new KeyMapping("key.advancements", 76, KeyMapping.Category.MISC);
/* 1342 */     this.keyQuickActions = new KeyMapping("key.quickActions", 71, KeyMapping.Category.MISC);
/* 1343 */     this.keyToggleGui = new KeyMapping("key.toggleGui", 290, KeyMapping.Category.MISC);
/* 1344 */     this.keyToggleSpectatorShaderEffects = new KeyMapping("key.toggleSpectatorShaderEffects", 293, KeyMapping.Category.MISC);
/* 1346 */     this.keyHotbarSlots = new KeyMapping[] { new KeyMapping("key.hotbar.1", 49, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.2", 50, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.3", 51, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.4", 52, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.5", 53, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.6", 54, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.7", 55, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.8", 56, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.9", 57, KeyMapping.Category.INVENTORY) };
/* 1357 */     this.keySaveHotbarActivator = new KeyMapping("key.saveToolbarActivator", 67, KeyMapping.Category.CREATIVE);
/* 1358 */     this.keyLoadHotbarActivator = new KeyMapping("key.loadToolbarActivator", 88, KeyMapping.Category.CREATIVE);
/* 1360 */     this.keySpectatorOutlines = new KeyMapping("key.spectatorOutlines", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.SPECTATOR);
/* 1361 */     this.keySpectatorHotbar = new KeyMapping("key.spectatorHotbar", InputConstants.Type.MOUSE, 2, KeyMapping.Category.SPECTATOR);
/* 1363 */     this.keyDebugOverlay = new KeyMapping("key.debug.overlay", InputConstants.Type.KEYSYM, 292, KeyMapping.Category.DEBUG, -2);
/* 1364 */     this.keyDebugModifier = new KeyMapping("key.debug.modifier", InputConstants.Type.KEYSYM, 292, KeyMapping.Category.DEBUG, -1);
/* 1365 */     this.keyDebugCrash = new KeyMapping("key.debug.crash", InputConstants.Type.KEYSYM, 67, KeyMapping.Category.DEBUG);
/* 1366 */     this.keyDebugReloadChunk = new KeyMapping("key.debug.reloadChunk", InputConstants.Type.KEYSYM, 65, KeyMapping.Category.DEBUG);
/* 1367 */     this.keyDebugShowHitboxes = new KeyMapping("key.debug.showHitboxes", InputConstants.Type.KEYSYM, 66, KeyMapping.Category.DEBUG);
/* 1368 */     this.keyDebugClearChat = new KeyMapping("key.debug.clearChat", InputConstants.Type.KEYSYM, 68, KeyMapping.Category.DEBUG);
/* 1369 */     this.keyDebugShowChunkBorders = new KeyMapping("key.debug.showChunkBorders", InputConstants.Type.KEYSYM, 71, KeyMapping.Category.DEBUG);
/* 1370 */     this.keyDebugShowAdvancedTooltips = new KeyMapping("key.debug.showAdvancedTooltips", InputConstants.Type.KEYSYM, 72, KeyMapping.Category.DEBUG);
/* 1371 */     this.keyDebugCopyRecreateCommand = new KeyMapping("key.debug.copyRecreateCommand", InputConstants.Type.KEYSYM, 73, KeyMapping.Category.DEBUG);
/* 1372 */     this.keyDebugSpectate = new KeyMapping("key.debug.spectate", InputConstants.Type.KEYSYM, 78, KeyMapping.Category.DEBUG);
/* 1373 */     this.keyDebugSwitchGameMode = new KeyMapping("key.debug.switchGameMode", InputConstants.Type.KEYSYM, 293, KeyMapping.Category.DEBUG);
/* 1374 */     this.keyDebugDebugOptions = new KeyMapping("key.debug.debugOptions", InputConstants.Type.KEYSYM, 295, KeyMapping.Category.DEBUG);
/* 1375 */     this.keyDebugFocusPause = new KeyMapping("key.debug.focusPause", InputConstants.Type.KEYSYM, 80, KeyMapping.Category.DEBUG);
/* 1376 */     this.keyDebugDumpDynamicTextures = new KeyMapping("key.debug.dumpDynamicTextures", InputConstants.Type.KEYSYM, 83, KeyMapping.Category.DEBUG);
/* 1377 */     this.keyDebugReloadResourcePacks = new KeyMapping("key.debug.reloadResourcePacks", InputConstants.Type.KEYSYM, 84, KeyMapping.Category.DEBUG);
/* 1378 */     this.keyDebugProfiling = new KeyMapping("key.debug.profiling", InputConstants.Type.KEYSYM, 76, KeyMapping.Category.DEBUG);
/* 1379 */     this.keyDebugCopyLocation = new KeyMapping("key.debug.copyLocation", InputConstants.Type.KEYSYM, 67, KeyMapping.Category.DEBUG);
/* 1380 */     this.keyDebugDumpVersion = new KeyMapping("key.debug.dumpVersion", InputConstants.Type.KEYSYM, 86, KeyMapping.Category.DEBUG);
/* 1381 */     this.keyDebugPofilingChart = new KeyMapping("key.debug.profilingChart", InputConstants.Type.KEYSYM, 49, KeyMapping.Category.DEBUG, 1);
/* 1382 */     this.keyDebugFpsCharts = new KeyMapping("key.debug.fpsCharts", InputConstants.Type.KEYSYM, 50, KeyMapping.Category.DEBUG, 2);
/* 1383 */     this.keyDebugNetworkCharts = new KeyMapping("key.debug.networkCharts", InputConstants.Type.KEYSYM, 51, KeyMapping.Category.DEBUG, 3);
/* 1384 */     this.keyDebugLightmapTexture = new KeyMapping("key.debug.lightmapTexture", InputConstants.Type.KEYSYM, 52, KeyMapping.Category.DEBUG, 4);
/* 1386 */     this.debugKeys = new KeyMapping[] { 
/* 1386 */         this.keyDebugReloadChunk, this.keyDebugShowHitboxes, this.keyDebugClearChat, this.keyDebugCrash, this.keyDebugShowChunkBorders, this.keyDebugShowAdvancedTooltips, this.keyDebugCopyRecreateCommand, this.keyDebugSpectate, this.keyDebugSwitchGameMode, this.keyDebugDebugOptions, 
/* 1386 */         this.keyDebugFocusPause, this.keyDebugDumpDynamicTextures, this.keyDebugReloadResourcePacks, this.keyDebugProfiling, this.keyDebugCopyLocation, this.keyDebugDumpVersion, this.keyDebugPofilingChart, this.keyDebugFpsCharts, this.keyDebugNetworkCharts, this.keyDebugLightmapTexture };
/* 1395 */     this
/*      */       
/* 1404 */       .keyMappings = (KeyMapping[])Stream.<KeyMapping[]>of(new KeyMapping[][] { { 
/*      */             this.keyAttack, this.keyUse, this.keyUp, this.keyLeft, this.keyDown, this.keyRight, this.keyJump, this.keyShift, this.keySprint, this.keyDrop, 
/*      */             this.keyInventory, this.keyChat, this.keyPlayerList, this.keyPickItem, this.keyCommand, this.keyFriends, this.keySocialInteractions, this.keyToggleGui, this.keyToggleSpectatorShaderEffects, this.keyScreenshot, 
/*      */             this.keyTogglePerspective, this.keySmoothCamera, this.keyFullscreen, this.keySpectatorOutlines, this.keySpectatorHotbar, this.keySwapOffhand, this.keySaveHotbarActivator, this.keyLoadHotbarActivator, this.keyAdvancements, this.keyQuickActions, 
/* 1404 */             this.keyDebugOverlay, this.keyDebugModifier }, this.keyHotbarSlots, this.debugKeys }).flatMap(Stream::of).toArray(x$0 -> new KeyMapping[x$0]);
/* 1409 */     this.cameraType = CameraType.FIRST_PERSON;
/* 1410 */     this.lastMpIp = "";
/* 1414 */     this
/*      */       
/* 1424 */       .fov = new OptionInstance("options.fov", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           switch (value.intValue()) {
/*      */             case 70:
/*      */             
/*      */             case 110:
/*      */             
/*      */           } 
/*      */           return genericValueLabel(caption, value.intValue());
/* 1424 */         }new OptionInstance.IntRange(30, 110), Codec.DOUBLE.xmap(value -> Integer.valueOf((int)(value.doubleValue() * 40.0D + 70.0D)), value -> Double.valueOf((value.intValue() - 70.0D) / 40.0D)), Integer.valueOf(70), OptionInstance.NO_ACTION);
/* 1437 */     this.telemetryOptInExtra = OptionInstance.createBoolean("options.telemetry.button", 
/*      */         
/* 1439 */         OptionInstance.cachedConstantTooltip(TELEMETRY_TOOLTIP), (caption, value) -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/* 1445 */           return (Component)(!minecraft.allowsTelemetry() ? Component.translatable("options.telemetry.state.none") : ((value.booleanValue() && minecraft.extraTelemetryAvailable()) ? Component.translatable("options.telemetry.state.all") : Component.translatable("options.telemetry.state.minimal")));
/*      */         }false, (OptionInstance.ValueUpdateListener)OptionInstance.NO_ACTION);
/* 1460 */     this
/*      */       
/* 1465 */       .screenEffectScale = new OptionInstance("options.screenEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/* 1475 */     this
/*      */       
/* 1484 */       .fovEffectScale = new OptionInstance("options.fovEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_FOV_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt), Codec.doubleRange(0.0D, 1.0D), Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/* 1494 */     this
/*      */       
/* 1502 */       .darknessEffectScale = new OptionInstance("options.darknessEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DARKNESS_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt), Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/* 1512 */     this
/*      */       
/* 1517 */       .glintSpeed = new OptionInstance("options.glintSpeed", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_GLINT_SPEED), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.5D), OptionInstance.NO_ACTION);
/* 1527 */     this
/*      */       
/* 1532 */       .glintStrength = new OptionInstance("options.glintStrength", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_GLINT_STRENGTH), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.75D), OptionInstance.NO_ACTION);
/* 1542 */     this
/*      */       
/* 1547 */       .damageTiltStrength = new OptionInstance("options.damageTiltStrength", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DAMAGE_TILT_STRENGTH), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), OptionInstance.NO_ACTION);
/* 1555 */     this
/*      */       
/* 1572 */       .gamma = new OptionInstance("options.gamma", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           int progressValueToDisplay = (int)(value.doubleValue() * 100.0D);
/*      */           return (progressValueToDisplay == 0) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.min")) : ((progressValueToDisplay == 50) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.default")) : ((progressValueToDisplay == 100) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.max")) : genericValueLabel(caption, progressValueToDisplay)));
/* 1572 */         }OptionInstance.UnitDouble.INSTANCE, Double.valueOf(0.5D), OptionInstance.NO_ACTION);
/* 1582 */     this
/*      */       
/* 1593 */       .guiScale = new OptionInstance<>("options.guiScale", OptionInstance.noTooltip(), (caption, value) -> (value.intValue() == 0) ? (Component)Component.translatable("options.guiScale.auto") : (Component)Component.literal(Integer.toString(value.intValue())), new OptionInstance.ClampingLazyMaxIntRange(0, () -> {
/*      */             Minecraft minecraft = Minecraft.getInstance();
/*      */             return !minecraft.isRunning() ? 2147483646 : minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
/* 1593 */           }2147483646), Integer.valueOf(0), paramInteger -> this.minecraft.resizeGui());
/* 1601 */     this
/*      */       
/* 1605 */       .particles = new OptionInstance<>("options.particles", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(ParticleStatus.values()), ParticleStatus.LEGACY_CODEC), ParticleStatus.ALL, paramParticleStatus -> setGraphicsPresetToCustom());
/* 1614 */     this
/*      */       
/* 1624 */       .narrator = new OptionInstance<>("options.narrator", OptionInstance.noTooltip(), (caption, value) -> this.minecraft.getNarrator().isActive() ? value.getName() : Component.translatable("options.narrator.notavailable"), new OptionInstance.Enum<>(Arrays.asList(NarratorStatus.values()), NarratorStatus.LEGACY_CODEC), NarratorStatus.OFF, value -> this.minecraft.getNarrator().updateNarratorStatus(value));
/* 1633 */     this.languageCode = "en_us";
/* 1634 */     this
/*      */       
/* 1636 */       .soundDevice = new OptionInstance<>("options.audioDevice", OptionInstance.noTooltip(), (caption, value) -> "".equals(value) ? Component.translatable("options.audioDevice.default") : (value.startsWith("OpenAL Soft on ") ? Component.literal(value.substring(SoundEngine.OPEN_AL_SOFT_PREFIX_LENGTH)) : Component.literal(value)), new OptionInstance.LazyEnum<>(() -> Stream.concat(Stream.of(""), Minecraft.getInstance().getSoundManager().getAvailableSoundDevices().stream()).toList(), device -> 
/*      */           
/* 1649 */           (!Minecraft.getInstance().isRunning() || isSoundDeviceDefault(device) || Minecraft.getInstance().getSoundManager().getAvailableSoundDevices().contains(device)) ? Optional.of(device) : Optional.empty(), (Codec<String>)Codec.STRING), "", paramString -> {
/*      */           SoundManager soundManager = Minecraft.getInstance().getSoundManager();
/*      */           soundManager.reload();
/*      */           soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*      */         });
/* 1668 */     this.onboardAccessibility = true;
/* 1677 */     this
/*      */       
/* 1681 */       .musicFrequency = new OptionInstance<>("options.music_frequency", OptionInstance.cachedConstantTooltip(MUSIC_FREQUENCY_TOOLTIP), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(MusicManager.MusicFrequency.values()), MusicManager.MusicFrequency.CODEC), MusicManager.MusicFrequency.DEFAULT, value -> Minecraft.getInstance().getMusicManager().setMinutesBetweenSongs(value));
/* 1690 */     this
/*      */       
/* 1694 */       .musicToast = new OptionInstance<>("options.musicToast", value -> Tooltip.create(value.tooltip()), (caption, value) -> value.text(), new OptionInstance.Enum<>(Arrays.asList(MusicToastDisplayState.values()), MusicToastDisplayState.CODEC), MusicToastDisplayState.NEVER, value -> this.minecraft.gui.toastManager().setMusicToastDisplayState(value));
/* 1704 */     this.startedCleanly = true;
/* 1707 */     this.minecraft = minecraft;
/* 1708 */     this.optionsFile = new File(workingDirectory, "options.txt");
/* 1710 */     boolean largeDistances = (Runtime.getRuntime().maxMemory() >= 1000000000L);
/* 1712 */     this
/*      */       
/* 1721 */       .renderDistance = new OptionInstance<>("options.renderDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(2, largeDistances ? 32 : 16, false), Integer.valueOf(12), paramInteger -> setGraphicsPresetToCustom());
/* 1727 */     this
/*      */       
/* 1736 */       .simulationDistance = new OptionInstance<>("options.simulationDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(SharedConstants.DEBUG_ALLOW_LOW_SIM_DISTANCE ? 2 : 5, largeDistances ? 32 : 16, false), Integer.valueOf(12), paramInteger -> setGraphicsPresetToCustom());
/* 1742 */     this.syncWrites = (Util.getPlatform() == Util.OS.WINDOWS);
/* 1744 */     load();
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> sensitivity() {
/*      */     return this.sensitivity;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> renderDistance() {
/*      */     return this.renderDistance;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> simulationDistance() {
/*      */     return this.simulationDistance;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> entityDistanceScaling() {
/*      */     return this.entityDistanceScaling;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> framerateLimit() {
/*      */     return this.framerateLimit;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_API_TOOLTIP = (Component)Component.translatable("options.graphicsApi.tooltip");
/*      */   
/*      */   private static final Component GRAPHICS_API_TOOLTIP_VULKAN = (Component)Component.translatable("options.graphicsApi.tooltip.vulkan");
/*      */   
/*      */   private final OptionInstance<PreferredGraphicsApi> preferredGraphicsBackend;
/*      */   
/*      */   private boolean isApplyingGraphicsPreset;
/*      */   
/*      */   private final OptionInstance<GraphicsPreset> graphicsPreset;
/*      */   
/*      */   public OptionInstance<PreferredGraphicsApi> preferredGraphicsBackend() {
/*      */     return this.preferredGraphicsBackend;
/*      */   }
/*      */   
/*      */   public boolean isRestartRequiredToApplyVideoSettings() {
/*      */     return (this.preferredGraphicsBackend.get() != this.preferredGraphicsBackendFromStartup || ((Boolean)this.exclusiveFullscreen.get()).booleanValue() != this.exclusiveFullscreenFromStartup);
/*      */   }
/*      */   
/*      */   public void applyGraphicsPreset(GraphicsPreset value) {
/*      */     this.isApplyingGraphicsPreset = true;
/*      */     value.apply(this.minecraft);
/*      */     this.isApplyingGraphicsPreset = false;
/*      */   }
/*      */   
/*      */   public OptionInstance<GraphicsPreset> graphicsPreset() {
/*      */     return this.graphicsPreset;
/*      */   }
/*      */   
/*      */   private static final Component INACTIVITY_FPS_LIMIT_TOOLTIP_MINIMIZED = (Component)Component.translatable("options.inactivityFpsLimit.minimized.tooltip");
/*      */   
/*      */   private static final Component INACTIVITY_FPS_LIMIT_TOOLTIP_AFK = (Component)Component.translatable("options.inactivityFpsLimit.afk.tooltip");
/*      */   
/*      */   private final OptionInstance<InactivityFpsLimit> inactivityFpsLimit;
/*      */   
/*      */   private final OptionInstance<CloudStatus> cloudStatus;
/*      */   
/*      */   private final OptionInstance<Integer> cloudRange;
/*      */   
/*      */   public OptionInstance<InactivityFpsLimit> inactivityFpsLimit() {
/*      */     return this.inactivityFpsLimit;
/*      */   }
/*      */   
/*      */   public OptionInstance<CloudStatus> cloudStatus() {
/*      */     return this.cloudStatus;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> cloudRange() {
/*      */     return this.cloudRange;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_WEATHER_RADIUS = (Component)Component.translatable("options.weatherRadius.tooltip");
/*      */   
/*      */   private final OptionInstance<Integer> weatherRadius;
/*      */   
/*      */   public OptionInstance<Integer> weatherRadius() {
/*      */     return this.weatherRadius;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_CUTOUT_LEAVES = (Component)Component.translatable("options.cutoutLeaves.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> cutoutLeaves;
/*      */   
/*      */   public OptionInstance<Boolean> cutoutLeaves() {
/*      */     return this.cutoutLeaves;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_VIGNETTE = (Component)Component.translatable("options.vignette.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> vignette;
/*      */   
/*      */   public OptionInstance<Boolean> vignette() {
/*      */     return this.vignette;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_IMPROVED_TRANSPARENCY = (Component)Component.translatable("options.improvedTransparency.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> improvedTransparency;
/*      */   
/*      */   private final OptionInstance<Boolean> ambientOcclusion;
/*      */   
/*      */   public OptionInstance<Boolean> improvedTransparency() {
/*      */     return this.improvedTransparency;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> ambientOcclusion() {
/*      */     return this.ambientOcclusion;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_CHUNK_FADE = (Component)Component.translatable("options.chunkFade.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> chunkSectionFadeInTime;
/*      */   
/*      */   public OptionInstance<Double> chunkSectionFadeInTime() {
/*      */     return this.chunkSectionFadeInTime;
/*      */   }
/*      */   
/*      */   private static final Component PRIORITIZE_CHUNK_TOOLTIP_NONE = (Component)Component.translatable("options.prioritizeChunkUpdates.none.tooltip");
/*      */   
/*      */   private static final Component PRIORITIZE_CHUNK_TOOLTIP_PLAYER_AFFECTED = (Component)Component.translatable("options.prioritizeChunkUpdates.byPlayer.tooltip");
/*      */   
/*      */   private static final Component PRIORITIZE_CHUNK_TOOLTIP_NEARBY = (Component)Component.translatable("options.prioritizeChunkUpdates.nearby.tooltip");
/*      */   
/*      */   private final OptionInstance<PrioritizeChunkUpdates> prioritizeChunkUpdates;
/*      */   
/*      */   public List<String> resourcePacks;
/*      */   
/*      */   public List<String> incompatibleResourcePacks;
/*      */   
/*      */   private final OptionInstance<ChatVisiblity> chatVisibility;
/*      */   
/*      */   private final OptionInstance<Double> chatOpacity;
/*      */   
/*      */   private final OptionInstance<Double> chatLineSpacing;
/*      */   
/*      */   public OptionInstance<PrioritizeChunkUpdates> prioritizeChunkUpdates() {
/*      */     return this.prioritizeChunkUpdates;
/*      */   }
/*      */   
/*      */   public void updateResourcePacks(PackRepository packRepository) {
/*      */     ImmutableList immutableList1 = ImmutableList.copyOf(this.resourcePacks);
/*      */     this.resourcePacks.clear();
/*      */     this.incompatibleResourcePacks.clear();
/*      */     for (Pack entry : packRepository.getSelectedPacks()) {
/*      */       if (!entry.isFixedPosition()) {
/*      */         this.resourcePacks.add(entry.getId());
/*      */         if (!entry.getCompatibility().isCompatible())
/*      */           this.incompatibleResourcePacks.add(entry.getId()); 
/*      */       } 
/*      */     } 
/*      */     save();
/*      */     ImmutableList immutableList2 = ImmutableList.copyOf(this.resourcePacks);
/*      */     if (!immutableList2.equals(immutableList1))
/*      */       this.minecraft.reloadResourcePacks(); 
/*      */   }
/*      */   
/*      */   public OptionInstance<ChatVisiblity> chatVisibility() {
/*      */     return this.chatVisibility;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatOpacity() {
/*      */     return this.chatOpacity;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatLineSpacing() {
/*      */     return this.chatLineSpacing;
/*      */   }
/*      */   
/*      */   private static final Component MENU_BACKGROUND_BLURRINESS_TOOLTIP = (Component)Component.translatable("options.accessibility.menu_background_blurriness.tooltip");
/*      */   
/*      */   private static final int BLURRINESS_DEFAULT_VALUE = 5;
/*      */   
/*      */   private final OptionInstance<Integer> menuBackgroundBlurriness;
/*      */   
/*      */   private final OptionInstance<Double> textBackgroundOpacity;
/*      */   
/*      */   private final OptionInstance<Double> panoramaSpeed;
/*      */   
/*      */   public OptionInstance<Integer> menuBackgroundBlurriness() {
/*      */     return this.menuBackgroundBlurriness;
/*      */   }
/*      */   
/*      */   public int getMenuBackgroundBlurriness() {
/*      */     return ((Integer)menuBackgroundBlurriness().get()).intValue();
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> textBackgroundOpacity() {
/*      */     return this.textBackgroundOpacity;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> panoramaSpeed() {
/*      */     return this.panoramaSpeed;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_CONTRAST_MODE = (Component)Component.translatable("options.accessibility.high_contrast.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> highContrast;
/*      */   
/*      */   public OptionInstance<Boolean> highContrast() {
/*      */     return this.highContrast;
/*      */   }
/*      */   
/*      */   private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP = (Component)Component.translatable("options.accessibility.high_contrast_block_outline.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> highContrastBlockOutline;
/*      */   
/*      */   private final OptionInstance<Boolean> narratorHotkey;
/*      */   
/*      */   public String fullscreenVideoModeString;
/*      */   
/*      */   public boolean hideServerAddress;
/*      */   
/*      */   public boolean advancedItemTooltips;
/*      */   
/*      */   public boolean pauseOnLostFocus;
/*      */   
/*      */   private final Set<PlayerModelPart> modelParts;
/*      */   
/*      */   private final OptionInstance<HumanoidArm> mainHand;
/*      */   
/*      */   public int overrideWidth;
/*      */   
/*      */   public int overrideHeight;
/*      */   
/*      */   private final OptionInstance<Double> chatScale;
/*      */   
/*      */   private final OptionInstance<Double> chatWidth;
/*      */   
/*      */   private final OptionInstance<Double> chatHeightUnfocused;
/*      */   
/*      */   private final OptionInstance<Double> chatHeightFocused;
/*      */   
/*      */   private final OptionInstance<Double> chatDelay;
/*      */   
/*      */   public OptionInstance<Boolean> highContrastBlockOutline() {
/*      */     return this.highContrastBlockOutline;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> narratorHotkey() {
/*      */     return this.narratorHotkey;
/*      */   }
/*      */   
/*      */   public OptionInstance<HumanoidArm> mainHand() {
/*      */     return this.mainHand;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatScale() {
/*      */     return this.chatScale;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatWidth() {
/*      */     return this.chatWidth;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatHeightUnfocused() {
/*      */     return this.chatHeightUnfocused;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatHeightFocused() {
/*      */     return this.chatHeightFocused;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> chatDelay() {
/*      */     return this.chatDelay;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_NOTIFICATION_DISPLAY_TIME = (Component)Component.translatable("options.notifications.display_time.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> notificationDisplayTime;
/*      */   
/*      */   private final OptionInstance<Integer> mipmapLevels;
/*      */   
/*      */   public OptionInstance<Double> notificationDisplayTime() {
/*      */     return this.notificationDisplayTime;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> mipmapLevels() {
/*      */     return this.mipmapLevels;
/*      */   }
/*      */   
/*      */   private static final Component GRAPHICS_TOOLTIP_ANISOTROPIC_FILTERING = (Component)Component.translatable("options.maxAnisotropy.tooltip");
/*      */   
/*      */   private final OptionInstance<Integer> maxAnisotropyBit;
/*      */   
/*      */   public OptionInstance<Integer> maxAnisotropyBit() {
/*      */     return this.maxAnisotropyBit;
/*      */   }
/*      */   
/*      */   public int maxAnisotropyValue() {
/*      */     return Math.min(1 << ((Integer)this.maxAnisotropyBit.get()).intValue(), RenderSystem.getDevice().getDeviceInfo().limits().maxAnisotropy());
/*      */   }
/*      */   
/*      */   private static final Component FILTERING_NONE_TOOLTIP = (Component)Component.translatable("options.textureFiltering.none.tooltip");
/*      */   
/*      */   private static final Component FILTERING_RGSS_TOOLTIP = (Component)Component.translatable("options.textureFiltering.rgss.tooltip");
/*      */   
/*      */   private static final Component FILTERING_ANISOTROPIC_TOOLTIP = (Component)Component.translatable("options.textureFiltering.anisotropic.tooltip");
/*      */   
/*      */   private final OptionInstance<TextureFilteringMethod> textureFiltering;
/*      */   
/*      */   private boolean useNativeTransport;
/*      */   
/*      */   private final OptionInstance<AttackIndicatorStatus> attackIndicator;
/*      */   
/*      */   public TutorialSteps tutorialStep;
/*      */   
/*      */   public boolean joinedFirstServer;
/*      */   
/*      */   private final OptionInstance<Integer> biomeBlendRadius;
/*      */   
/*      */   private final OptionInstance<Double> mouseWheelSensitivity;
/*      */   
/*      */   private final OptionInstance<Boolean> rawMouseInput;
/*      */   
/*      */   public OptionInstance<TextureFilteringMethod> textureFiltering() {
/*      */     return this.textureFiltering;
/*      */   }
/*      */   
/*      */   public OptionInstance<AttackIndicatorStatus> attackIndicator() {
/*      */     return this.attackIndicator;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> biomeBlendRadius() {
/*      */     return this.biomeBlendRadius;
/*      */   }
/*      */   
/*      */   private static double logMouse(int value) {
/*      */     return Math.pow(10.0D, value / 100.0D);
/*      */   }
/*      */   
/*      */   private static int unlogMouse(double value) {
/*      */     return Mth.floor(Math.log10(value) * 100.0D);
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> mouseWheelSensitivity() {
/*      */     return this.mouseWheelSensitivity;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> rawMouseInput() {
/*      */     return this.rawMouseInput;
/*      */   }
/*      */   
/*      */   private static final Component ALLOW_CURSOR_CHANGES_TOOLTIP = (Component)Component.translatable("options.allowCursorChanges.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> allowCursorChanges;
/*      */   
/*      */   public int glDebugVerbosity;
/*      */   
/*      */   private final OptionInstance<Boolean> autoJump;
/*      */   
/*      */   public OptionInstance<Boolean> allowCursorChanges() {
/*      */     return this.allowCursorChanges;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> autoJump() {
/*      */     return this.autoJump;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_ROTATE_WITH_MINECART = (Component)Component.translatable("options.rotateWithMinecart.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> rotateWithMinecart;
/*      */   
/*      */   private final OptionInstance<Boolean> operatorItemsTab;
/*      */   
/*      */   private final OptionInstance<Boolean> autoSuggestions;
/*      */   
/*      */   private final OptionInstance<Boolean> chatColors;
/*      */   
/*      */   private final OptionInstance<Boolean> chatLinks;
/*      */   
/*      */   private final OptionInstance<Boolean> chatLinksPrompt;
/*      */   
/*      */   private final OptionInstance<Boolean> enableVsync;
/*      */   
/*      */   private final OptionInstance<Boolean> entityShadows;
/*      */   
/*      */   private final OptionInstance<Boolean> forceUnicodeFont;
/*      */   
/*      */   private final OptionInstance<Boolean> japaneseGlyphVariants;
/*      */   
/*      */   private final OptionInstance<Boolean> invertXMouse;
/*      */   
/*      */   private final OptionInstance<Boolean> invertYMouse;
/*      */   
/*      */   private final OptionInstance<Boolean> discreteMouseScroll;
/*      */   
/*      */   public OptionInstance<Boolean> rotateWithMinecart() {
/*      */     return this.rotateWithMinecart;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> operatorItemsTab() {
/*      */     return this.operatorItemsTab;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> autoSuggestions() {
/*      */     return this.autoSuggestions;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> chatColors() {
/*      */     return this.chatColors;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> chatLinks() {
/*      */     return this.chatLinks;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> chatLinksPrompt() {
/*      */     return this.chatLinksPrompt;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> enableVsync() {
/*      */     return this.enableVsync;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> entityShadows() {
/*      */     return this.entityShadows;
/*      */   }
/*      */   
/*      */   private static void updateFontOptions() {
/*      */     Minecraft instance = Minecraft.getInstance();
/*      */     if (instance.getWindow() != null) {
/*      */       instance.updateFontOptions();
/*      */       instance.resizeGui();
/*      */     } 
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> forceUnicodeFont() {
/*      */     return this.forceUnicodeFont;
/*      */   }
/*      */   
/*      */   private static boolean japaneseGlyphVariantsDefault() {
/*      */     return Locale.getDefault().getLanguage().equalsIgnoreCase("ja");
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> japaneseGlyphVariants() {
/*      */     return this.japaneseGlyphVariants;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> invertMouseX() {
/*      */     return this.invertXMouse;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> invertMouseY() {
/*      */     return this.invertYMouse;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> discreteMouseScroll() {
/*      */     return this.discreteMouseScroll;
/*      */   }
/*      */   
/*      */   private static final Component REALMS_NOTIFICATIONS_TOOLTIP = (Component)Component.translatable("options.realmsNotifications.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> realmsNotifications;
/*      */   
/*      */   public OptionInstance<Boolean> realmsNotifications() {
/*      */     return this.realmsNotifications;
/*      */   }
/*      */   
/*      */   private static final Component ALLOW_SERVER_LISTING_TOOLTIP = (Component)Component.translatable("options.allowServerListing.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> allowServerListing;
/*      */   
/*      */   private final OptionInstance<Boolean> reducedDebugInfo;
/*      */   
/*      */   public OptionInstance<Boolean> allowServerListing() {
/*      */     return this.allowServerListing;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> reducedDebugInfo() {
/*      */     return this.reducedDebugInfo;
/*      */   }
/*      */   
/*      */   private static final Component IN_GAME_NOTIFICATION_TOOLTIP = (Component)Component.translatable("options.inGameNotification.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> inGameNotification;
/*      */   
/*      */   private final OptionInstance<PresenceSharing> sharePresence;
/*      */   
/*      */   private final Map<SoundSource, OptionInstance<Double>> soundSourceVolumes;
/*      */   
/*      */   public OptionInstance<Boolean> inGameNotification() {
/*      */     return this.inGameNotification;
/*      */   }
/*      */   
/*      */   public OptionInstance<PresenceSharing> sharePresence() {
/*      */     return this.sharePresence;
/*      */   }
/*      */   
/*      */   public final float getFinalSoundSourceVolume(SoundSource source) {
/*      */     if (source == SoundSource.MASTER)
/*      */       return getSoundSourceVolume(source); 
/*      */     return getSoundSourceVolume(source) * getSoundSourceVolume(SoundSource.MASTER);
/*      */   }
/*      */   
/*      */   public final float getSoundSourceVolume(SoundSource source) {
/*      */     return ((Double)getSoundSourceOptionInstance(source).get()).floatValue();
/*      */   }
/*      */   
/*      */   public final OptionInstance<Double> getSoundSourceOptionInstance(SoundSource source) {
/*      */     return Objects.<OptionInstance<Double>>requireNonNull(this.soundSourceVolumes.get(source));
/*      */   }
/*      */   
/*      */   private OptionInstance<Double> createSoundSliderOptionInstance(String captionId, SoundSource category) {
/*      */     return new OptionInstance<>(captionId, OptionInstance.noTooltip(), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, Double.valueOf(1.0D), value -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */           SoundManager soundManager = minecraft.getSoundManager();
/*      */           if ((category == SoundSource.MASTER || category == SoundSource.MUSIC) && getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F)
/*      */             minecraft.getMusicManager().showNowPlayingToastIfNeeded(); 
/*      */           soundManager.refreshCategoryVolume(category);
/*      */           if (minecraft.level == null)
/*      */             SoundPreviewHandler.preview(soundManager, category, value.floatValue()); 
/*      */         });
/*      */   }
/*      */   
/*      */   private static final Component CLOSED_CAPTIONS_TOOLTIP = (Component)Component.translatable("options.showSubtitles.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> showSubtitles;
/*      */   
/*      */   public OptionInstance<Boolean> showSubtitles() {
/*      */     return this.showSubtitles;
/*      */   }
/*      */   
/*      */   private static final Component DIRECTIONAL_AUDIO_TOOLTIP_ON = (Component)Component.translatable("options.directionalAudio.on.tooltip");
/*      */   
/*      */   private static final Component DIRECTIONAL_AUDIO_TOOLTIP_OFF = (Component)Component.translatable("options.directionalAudio.off.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> directionalAudio;
/*      */   
/*      */   private final OptionInstance<Boolean> backgroundForChatOnly;
/*      */   
/*      */   private final OptionInstance<Boolean> fullscreen;
/*      */   
/*      */   private boolean exclusiveFullscreenFromStartup;
/*      */   
/*      */   public OptionInstance<Boolean> directionalAudio() {
/*      */     return this.directionalAudio;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> backgroundForChatOnly() {
/*      */     return this.backgroundForChatOnly;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> fullscreen() {
/*      */     return this.fullscreen;
/*      */   }
/*      */   
/*      */   private static final Component TOOLTIP_EXCLUSIVE_FULLSCREEN_ON = (Component)Component.translatable("options.exclusiveFullscreen.on.tooltip");
/*      */   
/*      */   private static final Component TOOLTIP_EXCLUSIVE_FULLSCREEN_OFF = (Component)Component.translatable("options.exclusiveFullscreen.off.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> exclusiveFullscreen;
/*      */   
/*      */   private final OptionInstance<Boolean> bobView;
/*      */   
/*      */   public OptionInstance<Boolean> exclusiveFullscreen() {
/*      */     return this.exclusiveFullscreen;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> bobView() {
/*      */     return this.bobView;
/*      */   }
/*      */   
/*      */   private static final Component KEY_TOGGLE = (Component)Component.translatable("options.key.toggle");
/*      */   
/*      */   private static final Component KEY_HOLD = (Component)Component.translatable("options.key.hold");
/*      */   
/*      */   private final OptionInstance<Boolean> toggleCrouch;
/*      */   
/*      */   private final OptionInstance<Boolean> toggleSprint;
/*      */   
/*      */   private final OptionInstance<Boolean> toggleAttack;
/*      */   
/*      */   private final OptionInstance<Boolean> toggleUse;
/*      */   
/*      */   public OptionInstance<Boolean> toggleCrouch() {
/*      */     return this.toggleCrouch;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> toggleSprint() {
/*      */     return this.toggleSprint;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> toggleAttack() {
/*      */     return this.toggleAttack;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> toggleUse() {
/*      */     return this.toggleUse;
/*      */   }
/*      */   
/*      */   private static final Component SPRINT_WINDOW_TOOLTIP = (Component)Component.translatable("options.sprintWindow.tooltip");
/*      */   
/*      */   private final OptionInstance<Integer> sprintWindow;
/*      */   
/*      */   public boolean skipMultiplayerWarning;
/*      */   
/*      */   public OptionInstance<Integer> sprintWindow() {
/*      */     return this.sprintWindow;
/*      */   }
/*      */   
/*      */   private static final Component CHAT_TOOLTIP_HIDE_MATCHED_NAMES = (Component)Component.translatable("options.hideMatchedNames.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> hideMatchedNames;
/*      */   
/*      */   private final OptionInstance<Boolean> showAutosaveIndicator;
/*      */   
/*      */   public OptionInstance<Boolean> hideMatchedNames() {
/*      */     return this.hideMatchedNames;
/*      */   }
/*      */   
/*      */   public OptionInstance<Boolean> showAutosaveIndicator() {
/*      */     return this.showAutosaveIndicator;
/*      */   }
/*      */   
/*      */   private static final Component CHAT_TOOLTIP_ONLY_SHOW_SECURE = (Component)Component.translatable("options.onlyShowSecureChat.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> onlyShowSecureChat;
/*      */   
/*      */   public OptionInstance<Boolean> onlyShowSecureChat() {
/*      */     return this.onlyShowSecureChat;
/*      */   }
/*      */   
/*      */   private static final Component CHAT_TOOLTIP_SAVE_DRAFTS = (Component)Component.translatable("options.chat.drafts.tooltip");
/*      */   
/*      */   private final OptionInstance<Boolean> saveChatDrafts;
/*      */   
/*      */   public final KeyMapping keyUp;
/*      */   
/*      */   public final KeyMapping keyLeft;
/*      */   
/*      */   public final KeyMapping keyDown;
/*      */   
/*      */   public final KeyMapping keyRight;
/*      */   
/*      */   public final KeyMapping keyJump;
/*      */   
/*      */   public final KeyMapping keyShift;
/*      */   
/*      */   public final KeyMapping keySprint;
/*      */   
/*      */   public final KeyMapping keyInventory;
/*      */   
/*      */   public final KeyMapping keySwapOffhand;
/*      */   
/*      */   public final KeyMapping keyDrop;
/*      */   
/*      */   public final KeyMapping keyUse;
/*      */   
/*      */   public final KeyMapping keyAttack;
/*      */   
/*      */   public final KeyMapping keyPickItem;
/*      */   
/*      */   public final KeyMapping keyChat;
/*      */   
/*      */   public final KeyMapping keyPlayerList;
/*      */   
/*      */   public final KeyMapping keyCommand;
/*      */   
/*      */   public final KeyMapping keyFriends;
/*      */   
/*      */   public final KeyMapping keySocialInteractions;
/*      */   
/*      */   public final KeyMapping keyScreenshot;
/*      */   
/*      */   public final KeyMapping keyTogglePerspective;
/*      */   
/*      */   public final KeyMapping keySmoothCamera;
/*      */   
/*      */   public final KeyMapping keyFullscreen;
/*      */   
/*      */   public final KeyMapping keyAdvancements;
/*      */   
/*      */   public final KeyMapping keyQuickActions;
/*      */   
/*      */   public final KeyMapping keyToggleGui;
/*      */   
/*      */   public final KeyMapping keyToggleSpectatorShaderEffects;
/*      */   
/*      */   public final KeyMapping[] keyHotbarSlots;
/*      */   
/*      */   public final KeyMapping keySaveHotbarActivator;
/*      */   
/*      */   public final KeyMapping keyLoadHotbarActivator;
/*      */   
/*      */   public final KeyMapping keySpectatorOutlines;
/*      */   
/*      */   public final KeyMapping keySpectatorHotbar;
/*      */   
/*      */   public final KeyMapping keyDebugOverlay;
/*      */   
/*      */   public final KeyMapping keyDebugModifier;
/*      */   
/*      */   public final KeyMapping keyDebugCrash;
/*      */   
/*      */   public final KeyMapping keyDebugReloadChunk;
/*      */   
/*      */   public final KeyMapping keyDebugShowHitboxes;
/*      */   
/*      */   public final KeyMapping keyDebugClearChat;
/*      */   
/*      */   public final KeyMapping keyDebugShowChunkBorders;
/*      */   
/*      */   public final KeyMapping keyDebugShowAdvancedTooltips;
/*      */   
/*      */   public final KeyMapping keyDebugCopyRecreateCommand;
/*      */   
/*      */   public final KeyMapping keyDebugSpectate;
/*      */   
/*      */   public final KeyMapping keyDebugSwitchGameMode;
/*      */   
/*      */   public final KeyMapping keyDebugDebugOptions;
/*      */   
/*      */   public final KeyMapping keyDebugFocusPause;
/*      */   
/*      */   public final KeyMapping keyDebugDumpDynamicTextures;
/*      */   
/*      */   public final KeyMapping keyDebugReloadResourcePacks;
/*      */   
/*      */   public final KeyMapping keyDebugProfiling;
/*      */   
/*      */   public final KeyMapping keyDebugCopyLocation;
/*      */   
/*      */   public final KeyMapping keyDebugDumpVersion;
/*      */   
/*      */   public final KeyMapping keyDebugPofilingChart;
/*      */   
/*      */   public final KeyMapping keyDebugFpsCharts;
/*      */   
/*      */   public final KeyMapping keyDebugNetworkCharts;
/*      */   
/*      */   public final KeyMapping keyDebugLightmapTexture;
/*      */   
/*      */   public final KeyMapping[] debugKeys;
/*      */   
/*      */   public final KeyMapping[] keyMappings;
/*      */   
/*      */   protected Minecraft minecraft;
/*      */   
/*      */   private final File optionsFile;
/*      */   
/*      */   private CameraType cameraType;
/*      */   
/*      */   public String lastMpIp;
/*      */   
/*      */   public boolean smoothCamera;
/*      */   
/*      */   private final OptionInstance<Integer> fov;
/*      */   
/*      */   public OptionInstance<Boolean> saveChatDrafts() {
/*      */     return this.saveChatDrafts;
/*      */   }
/*      */   
/*      */   private void setGraphicsPresetToCustom() {
/*      */     if (this.isApplyingGraphicsPreset)
/*      */       return; 
/*      */     this.graphicsPreset.set(GraphicsPreset.CUSTOM);
/*      */     Screen screen = this.minecraft.gui.screen();
/*      */     if (screen instanceof OptionsSubScreen) {
/*      */       OptionsSubScreen optionsSubScreen = (OptionsSubScreen)screen;
/*      */       optionsSubScreen.resetOption(this.graphicsPreset);
/*      */     } 
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> fov() {
/*      */     return this.fov;
/*      */   }
/*      */   
/*      */   private static final Component TELEMETRY_TOOLTIP = (Component)Component.translatable("options.telemetry.button.tooltip", new Object[] { Component.translatable("options.telemetry.state.minimal"), Component.translatable("options.telemetry.state.all") });
/*      */   
/*      */   private final OptionInstance<Boolean> telemetryOptInExtra;
/*      */   
/*      */   public OptionInstance<Boolean> telemetryOptInExtra() {
/*      */     return this.telemetryOptInExtra;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT = (Component)Component.translatable("options.screenEffectScale.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> screenEffectScale;
/*      */   
/*      */   public OptionInstance<Double> screenEffectScale() {
/*      */     return this.screenEffectScale;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_FOV_EFFECT = (Component)Component.translatable("options.fovEffectScale.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> fovEffectScale;
/*      */   
/*      */   public OptionInstance<Double> fovEffectScale() {
/*      */     return this.fovEffectScale;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_DARKNESS_EFFECT = (Component)Component.translatable("options.darknessEffectScale.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> darknessEffectScale;
/*      */   
/*      */   public OptionInstance<Double> darknessEffectScale() {
/*      */     return this.darknessEffectScale;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_GLINT_SPEED = (Component)Component.translatable("options.glintSpeed.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> glintSpeed;
/*      */   
/*      */   public OptionInstance<Double> glintSpeed() {
/*      */     return this.glintSpeed;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_GLINT_STRENGTH = (Component)Component.translatable("options.glintStrength.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> glintStrength;
/*      */   
/*      */   public OptionInstance<Double> glintStrength() {
/*      */     return this.glintStrength;
/*      */   }
/*      */   
/*      */   private static final Component ACCESSIBILITY_TOOLTIP_DAMAGE_TILT_STRENGTH = (Component)Component.translatable("options.damageTiltStrength.tooltip");
/*      */   
/*      */   private final OptionInstance<Double> damageTiltStrength;
/*      */   
/*      */   private final OptionInstance<Double> gamma;
/*      */   
/*      */   public static final int AUTO_GUI_SCALE = 0;
/*      */   
/*      */   private static final int MAX_GUI_SCALE_INCLUSIVE = 2147483646;
/*      */   
/*      */   private final OptionInstance<Integer> guiScale;
/*      */   
/*      */   private final OptionInstance<ParticleStatus> particles;
/*      */   
/*      */   private final OptionInstance<NarratorStatus> narrator;
/*      */   
/*      */   public String languageCode;
/*      */   
/*      */   private final OptionInstance<String> soundDevice;
/*      */   
/*      */   public boolean onboardAccessibility;
/*      */   
/*      */   public OptionInstance<Double> damageTiltStrength() {
/*      */     return this.damageTiltStrength;
/*      */   }
/*      */   
/*      */   public OptionInstance<Double> gamma() {
/*      */     return this.gamma;
/*      */   }
/*      */   
/*      */   public OptionInstance<Integer> guiScale() {
/*      */     return this.guiScale;
/*      */   }
/*      */   
/*      */   public OptionInstance<ParticleStatus> particles() {
/*      */     return this.particles;
/*      */   }
/*      */   
/*      */   public OptionInstance<NarratorStatus> narrator() {
/*      */     return this.narrator;
/*      */   }
/*      */   
/*      */   public OptionInstance<String> soundDevice() {
/*      */     return this.soundDevice;
/*      */   }
/*      */   
/*      */   public void onboardingAccessibilityFinished() {
/*      */     this.onboardAccessibility = false;
/*      */     save();
/*      */   }
/*      */   
/*      */   private static final Component MUSIC_FREQUENCY_TOOLTIP = (Component)Component.translatable("options.music_frequency.tooltip");
/*      */   
/*      */   private final OptionInstance<MusicManager.MusicFrequency> musicFrequency;
/*      */   
/*      */   private final OptionInstance<MusicToastDisplayState> musicToast;
/*      */   
/*      */   public boolean syncWrites;
/*      */   
/*      */   public boolean startedCleanly;
/*      */   
/*      */   public OptionInstance<MusicManager.MusicFrequency> musicFrequency() {
/*      */     return this.musicFrequency;
/*      */   }
/*      */   
/*      */   public OptionInstance<MusicToastDisplayState> musicToast() {
/*      */     return this.musicToast;
/*      */   }
/*      */   
/*      */   public float getBackgroundOpacity(float defaultOpacity) {
/* 1748 */     return ((Boolean)this.backgroundForChatOnly.get()).booleanValue() ? defaultOpacity : ((Double)textBackgroundOpacity().get()).floatValue();
/*      */   }
/*      */   
/*      */   public int getBackgroundColor(float defaultOpacity) {
/* 1752 */     return ARGB.colorFromFloat(getBackgroundOpacity(defaultOpacity), 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */   
/*      */   public int getBackgroundColor(int defaultColor) {
/* 1756 */     return ((Boolean)this.backgroundForChatOnly.get()).booleanValue() ? defaultColor : ARGB.colorFromFloat(((Double)this.textBackgroundOpacity.get()).floatValue(), 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */   
/*      */   private void processDumpedOptions(OptionAccess access) {
/* 1763 */     access.process("ao", this.ambientOcclusion);
/* 1764 */     access.process("biomeBlendRadius", this.biomeBlendRadius);
/* 1765 */     access.process("chunkSectionFadeInTime", this.chunkSectionFadeInTime);
/* 1766 */     access.process("cutoutLeaves", this.cutoutLeaves);
/* 1767 */     access.process("enableVsync", this.enableVsync);
/* 1768 */     access.process("entityDistanceScaling", this.entityDistanceScaling);
/* 1769 */     access.process("entityShadows", this.entityShadows);
/* 1770 */     access.process("forceUnicodeFont", this.forceUnicodeFont);
/* 1771 */     access.process("japaneseGlyphVariants", this.japaneseGlyphVariants);
/* 1772 */     access.process("fov", this.fov);
/* 1773 */     access.process("fovEffectScale", this.fovEffectScale);
/* 1774 */     access.process("darknessEffectScale", this.darknessEffectScale);
/* 1775 */     access.process("glintSpeed", this.glintSpeed);
/* 1776 */     access.process("glintStrength", this.glintStrength);
/* 1777 */     access.process("preferredGraphicsBackend", this.preferredGraphicsBackend);
/* 1778 */     access.process("graphicsPreset", this.graphicsPreset);
/* 1779 */     access.process("prioritizeChunkUpdates", this.prioritizeChunkUpdates);
/* 1780 */     access.process("fullscreen", this.fullscreen);
/* 1781 */     access.process("exclusiveFullscreen", this.exclusiveFullscreen);
/* 1782 */     access.process("gamma", this.gamma);
/* 1783 */     access.process("guiScale", this.guiScale);
/* 1784 */     access.process("maxAnisotropyBit", this.maxAnisotropyBit);
/* 1785 */     access.process("textureFiltering", this.textureFiltering);
/* 1786 */     access.process("maxFps", this.framerateLimit);
/* 1787 */     access.process("improvedTransparency", this.improvedTransparency);
/* 1788 */     access.process("inactivityFpsLimit", this.inactivityFpsLimit);
/* 1789 */     access.process("mipmapLevels", this.mipmapLevels);
/* 1790 */     access.process("narrator", this.narrator);
/* 1791 */     access.process("particles", this.particles);
/* 1792 */     access.process("reducedDebugInfo", this.reducedDebugInfo);
/* 1793 */     access.process("renderClouds", this.cloudStatus);
/* 1794 */     access.process("cloudRange", this.cloudRange);
/* 1796 */     access.process("renderDistance", this.renderDistance);
/* 1797 */     access.process("simulationDistance", this.simulationDistance);
/* 1798 */     access.process("screenEffectScale", this.screenEffectScale);
/* 1799 */     access.process("soundDevice", this.soundDevice);
/* 1800 */     access.process("vignette", this.vignette);
/* 1801 */     access.process("weatherRadius", this.weatherRadius);
/*      */   }
/*      */   
/*      */   private void processOptions(FieldAccess access) {
/* 1805 */     processDumpedOptions(access);
/* 1807 */     access.process("autoJump", this.autoJump);
/* 1808 */     access.process("rotateWithMinecart", this.rotateWithMinecart);
/* 1809 */     access.process("operatorItemsTab", this.operatorItemsTab);
/* 1810 */     access.process("autoSuggestions", this.autoSuggestions);
/* 1811 */     access.process("chatColors", this.chatColors);
/* 1812 */     access.process("chatLinks", this.chatLinks);
/* 1813 */     access.process("chatLinksPrompt", this.chatLinksPrompt);
/* 1814 */     access.process("discrete_mouse_scroll", this.discreteMouseScroll);
/* 1815 */     access.process("invertXMouse", this.invertXMouse);
/* 1816 */     access.process("invertYMouse", this.invertYMouse);
/* 1817 */     access.process("realmsNotifications", this.realmsNotifications);
/* 1818 */     access.process("showSubtitles", this.showSubtitles);
/* 1819 */     access.process("directionalAudio", this.directionalAudio);
/* 1820 */     access.process("bobView", this.bobView);
/* 1821 */     access.process("toggleCrouch", this.toggleCrouch);
/* 1822 */     access.process("toggleSprint", this.toggleSprint);
/* 1823 */     access.process("toggleAttack", this.toggleAttack);
/* 1824 */     access.process("toggleUse", this.toggleUse);
/* 1825 */     access.process("sprintWindow", this.sprintWindow);
/* 1826 */     access.process("darkMojangStudiosBackground", this.darkMojangStudiosBackground);
/* 1827 */     access.process("hideLightningFlashes", this.hideLightningFlash);
/* 1828 */     access.process("hideSplashTexts", this.hideSplashTexts);
/* 1829 */     access.process("mouseSensitivity", this.sensitivity);
/* 1830 */     access.process("damageTiltStrength", this.damageTiltStrength);
/* 1831 */     access.process("highContrast", this.highContrast);
/* 1832 */     access.process("highContrastBlockOutline", this.highContrastBlockOutline);
/* 1833 */     access.process("narratorHotkey", this.narratorHotkey);
/* 1835 */     Objects.requireNonNull(GSON);
/* 1835 */     this.resourcePacks = access.<List<String>>process("resourcePacks", this.resourcePacks, Options::readListOfStrings, GSON::toJson);
/* 1836 */     Objects.requireNonNull(GSON);
/* 1836 */     this.incompatibleResourcePacks = access.<List<String>>process("incompatibleResourcePacks", this.incompatibleResourcePacks, Options::readListOfStrings, GSON::toJson);
/* 1837 */     this.lastMpIp = access.process("lastServer", this.lastMpIp);
/* 1838 */     this.languageCode = access.process("lang", this.languageCode);
/* 1839 */     access.process("chatVisibility", this.chatVisibility);
/* 1840 */     access.process("chatOpacity", this.chatOpacity);
/* 1841 */     access.process("chatLineSpacing", this.chatLineSpacing);
/* 1842 */     access.process("textBackgroundOpacity", this.textBackgroundOpacity);
/* 1843 */     access.process("backgroundForChatOnly", this.backgroundForChatOnly);
/* 1845 */     this.hideServerAddress = access.process("hideServerAddress", this.hideServerAddress);
/* 1846 */     this.advancedItemTooltips = access.process("advancedItemTooltips", this.advancedItemTooltips);
/* 1847 */     this.pauseOnLostFocus = access.process("pauseOnLostFocus", this.pauseOnLostFocus);
/* 1848 */     this.overrideWidth = access.process("overrideWidth", this.overrideWidth);
/* 1849 */     this.overrideHeight = access.process("overrideHeight", this.overrideHeight);
/* 1850 */     access.process("chatHeightFocused", this.chatHeightFocused);
/* 1851 */     access.process("chatDelay", this.chatDelay);
/* 1852 */     access.process("chatHeightUnfocused", this.chatHeightUnfocused);
/* 1853 */     access.process("chatScale", this.chatScale);
/* 1854 */     access.process("chatWidth", this.chatWidth);
/* 1855 */     access.process("notificationDisplayTime", this.notificationDisplayTime);
/* 1856 */     this.useNativeTransport = access.process("useNativeTransport", this.useNativeTransport);
/* 1857 */     access.process("mainHand", this.mainHand);
/* 1858 */     access.process("attackIndicator", this.attackIndicator);
/* 1859 */     this.tutorialStep = access.<TutorialSteps>process("tutorialStep", this.tutorialStep, TutorialSteps::getByName, TutorialSteps::getName);
/* 1860 */     access.process("mouseWheelSensitivity", this.mouseWheelSensitivity);
/* 1861 */     access.process("rawMouseInput", this.rawMouseInput);
/* 1862 */     access.process("allowCursorChanges", this.allowCursorChanges);
/* 1863 */     this.glDebugVerbosity = access.process("glDebugVerbosity", this.glDebugVerbosity);
/* 1864 */     this.skipMultiplayerWarning = access.process("skipMultiplayerWarning", this.skipMultiplayerWarning);
/* 1865 */     access.process("hideMatchedNames", this.hideMatchedNames);
/* 1866 */     this.joinedFirstServer = access.process("joinedFirstServer", this.joinedFirstServer);
/* 1867 */     this.syncWrites = access.process("syncChunkWrites", this.syncWrites);
/* 1868 */     access.process("showAutosaveIndicator", this.showAutosaveIndicator);
/* 1869 */     access.process("allowServerListing", this.allowServerListing);
/* 1870 */     access.process("inGameNotification", this.inGameNotification);
/* 1871 */     access.process("sharePresence", this.sharePresence);
/* 1872 */     access.process("onlyShowSecureChat", this.onlyShowSecureChat);
/* 1873 */     access.process("saveChatDrafts", this.saveChatDrafts);
/* 1874 */     access.process("panoramaScrollSpeed", this.panoramaSpeed);
/* 1875 */     access.process("telemetryOptInExtra", this.telemetryOptInExtra);
/* 1876 */     this.onboardAccessibility = access.process("onboardAccessibility", this.onboardAccessibility);
/* 1877 */     access.process("menuBackgroundBlurriness", this.menuBackgroundBlurriness);
/* 1878 */     this.startedCleanly = access.process("startedCleanly", this.startedCleanly);
/* 1879 */     access.process("musicToast", this.musicToast);
/* 1880 */     access.process("musicFrequency", this.musicFrequency);
/* 1882 */     for (KeyMapping keyMapping : this.keyMappings) {
/* 1883 */       String currentValue = keyMapping.saveString();
/* 1884 */       String newValue = access.process("key_" + keyMapping.getName(), currentValue);
/* 1885 */       if (!currentValue.equals(newValue))
/* 1886 */         keyMapping.setKey(InputConstants.getKey(newValue)); 
/*      */     } 
/* 1890 */     for (SoundSource source : SoundSource.values())
/* 1891 */       access.process("soundCategory_" + source.getName(), this.soundSourceVolumes.get(source)); 
/* 1894 */     for (PlayerModelPart part : PlayerModelPart.values()) {
/* 1895 */       boolean wasEnabled = this.modelParts.contains(part);
/* 1896 */       boolean isEnabled = access.process("modelPart_" + part.getId(), wasEnabled);
/* 1897 */       if (isEnabled != wasEnabled)
/* 1898 */         setModelPart(part, isEnabled); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void load() {
/*      */     try {
/* 1905 */       if (!this.optionsFile.exists())
/*      */         return; 
/* 1909 */       CompoundTag rawOptions = new CompoundTag();
/* 1910 */       BufferedReader reader = Files.newReader(this.optionsFile, StandardCharsets.UTF_8);
/*      */       try {
/* 1911 */         reader.lines().forEach(line -> {
/*      */               try {
/*      */                 Iterator<String> iterator = OPTION_SPLITTER.split(line).iterator();
/*      */                 rawOptions.putString(iterator.next(), iterator.next());
/* 1915 */               } catch (Exception ignored) {
/*      */                 LOGGER.warn("Skipping bad option: {}", line);
/*      */               } 
/*      */             });
/* 1919 */         if (reader != null)
/* 1919 */           reader.close(); 
/*      */       } catch (Throwable throwable) {
/*      */         if (reader != null)
/*      */           try {
/*      */             reader.close();
/*      */           } catch (Throwable throwable1) {
/*      */             throwable.addSuppressed(throwable1);
/*      */           }  
/*      */         throw throwable;
/*      */       } 
/* 1921 */       final CompoundTag options = dataFix(rawOptions);
/* 1923 */       processOptions(new FieldAccess(this) {
/*      */             private String getValue(String name) {
/* 1925 */               Tag tag = options.get(name);
/* 1926 */               if (tag == null)
/* 1927 */                 return null; 
/* 1929 */               if (tag instanceof StringTag) {
/* 1929 */                 StringTag stringTag = (StringTag)tag;
/*      */                 try {
/* 1929 */                   String str1 = stringTag.value(), value = str1;
/* 1930 */                   return value;
/*      */                 } catch (Throwable throwable) {
/*      */                   throw new MatchException(throwable.toString(), throwable);
/*      */                 } 
/*      */               } 
/* 1932 */               throw new IllegalStateException("Cannot read field of wrong type, expected string: " + String.valueOf(tag));
/*      */             }
/*      */             
/*      */             public <T> void process(String name, OptionInstance<T> option) {
/* 1937 */               String result = getValue(name);
/* 1938 */               if (result != null) {
/* 1941 */                 JsonElement element = LenientJsonParser.parse(result.isEmpty() ? "\"\"" : result);
/* 1944 */                 Objects.requireNonNull(option);
/* 1944 */                 option.codec().parse((DynamicOps)JsonOps.INSTANCE, element).ifError(error -> Options.LOGGER.error("Error parsing option value {} for option {}: {}", new Object[] { result, option, error.message() })).ifSuccess(option::set);
/*      */               } 
/*      */             }
/*      */             
/*      */             public int process(String name, int current) {
/* 1950 */               String result = getValue(name);
/* 1951 */               if (result != null)
/*      */                 try {
/* 1953 */                   return Integer.parseInt(result);
/* 1954 */                 } catch (NumberFormatException e) {
/* 1955 */                   Options.LOGGER.warn("Invalid integer value for option {} = {}", new Object[] { name, result, e });
/*      */                 }  
/* 1958 */               return current;
/*      */             }
/*      */             
/*      */             public boolean process(String name, boolean current) {
/* 1963 */               String result = getValue(name);
/* 1964 */               return (result != null) ? Options.isTrue(result) : current;
/*      */             }
/*      */             
/*      */             public String process(String name, String current) {
/* 1969 */               return (String)MoreObjects.firstNonNull(getValue(name), current);
/*      */             }
/*      */             
/*      */             public float process(String name, float current) {
/* 1974 */               String result = getValue(name);
/* 1975 */               if (result != null) {
/* 1977 */                 if (Options.isTrue(result))
/* 1978 */                   return 1.0F; 
/* 1980 */                 if (Options.isFalse(result))
/* 1981 */                   return 0.0F; 
/*      */                 try {
/* 1984 */                   return Float.parseFloat(result);
/* 1985 */                 } catch (NumberFormatException e) {
/* 1986 */                   Options.LOGGER.warn("Invalid floating point value for option {} = {}", new Object[] { name, result, e });
/*      */                 } 
/*      */               } 
/* 1989 */               return current;
/*      */             }
/*      */             
/*      */             public <T> T process(String name, T current, Function<String, T> reader, Function<T, String> writer) {
/* 1994 */               String rawResult = getValue(name);
/* 1995 */               return (rawResult == null) ? current : reader.apply(rawResult);
/*      */             }
/*      */           });
/* 1999 */       options.getString("fullscreenResolution").ifPresent(fullscreenResolution -> this.fullscreenVideoModeString = fullscreenResolution);
/* 2001 */       KeyMapping.resetMapping();
/* 2002 */     } catch (Exception e) {
/* 2003 */       LOGGER.error("Failed to load options", e);
/*      */     } 
/* 2006 */     this.preferredGraphicsBackendFromStartup = this.preferredGraphicsBackend.get();
/* 2007 */     this.exclusiveFullscreenFromStartup = ((Boolean)this.exclusiveFullscreen.get()).booleanValue();
/*      */   }
/*      */   
/*      */   private static boolean isTrue(String value) {
/* 2011 */     return "true".equals(value);
/*      */   }
/*      */   
/*      */   private static boolean isFalse(String value) {
/* 2015 */     return "false".equals(value);
/*      */   }
/*      */   
/*      */   private CompoundTag dataFix(CompoundTag tag) {
/* 2019 */     int version = 0;
/*      */     try {
/* 2021 */       version = ((Integer)tag.getString("version").map(Integer::parseInt).orElse(Integer.valueOf(0))).intValue();
/* 2022 */     } catch (RuntimeException runtimeException) {}
/* 2025 */     return DataFixTypes.OPTIONS.updateToCurrentVersion(this.minecraft.getFixerUpper(), tag, version);
/*      */   }
/*      */   
/*      */   public void save() {
/*      */     try {
/* 2029 */       final PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
/*      */       try {
/* 2030 */         writer.println("version:" + SharedConstants.getCurrentVersion().dataVersion().version());
/* 2032 */         processOptions(new FieldAccess(this) {
/*      */               public void writePrefix(String name) {
/* 2034 */                 writer.print(name);
/* 2035 */                 writer.print(':');
/*      */               }
/*      */               
/*      */               public <T> void process(String name, OptionInstance<T> option) {
/* 2040 */                 option.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, option.get())
/* 2041 */                   .ifError(error -> Options.LOGGER.error("Error saving option {}: {}", option, error.message()))
/* 2042 */                   .ifSuccess(element -> {
/*      */                       writePrefix(name);
/*      */                       writer.println(Options.GSON.toJson(element));
/*      */                     });
/*      */               }
/*      */               
/*      */               public int process(String name, int value) {
/* 2050 */                 writePrefix(name);
/* 2051 */                 writer.println(value);
/* 2052 */                 return value;
/*      */               }
/*      */               
/*      */               public boolean process(String name, boolean value) {
/* 2057 */                 writePrefix(name);
/* 2058 */                 writer.println(value);
/* 2059 */                 return value;
/*      */               }
/*      */               
/*      */               public String process(String name, String value) {
/* 2064 */                 writePrefix(name);
/* 2065 */                 writer.println(value);
/* 2066 */                 return value;
/*      */               }
/*      */               
/*      */               public float process(String name, float value) {
/* 2071 */                 writePrefix(name);
/* 2072 */                 writer.println(value);
/* 2073 */                 return value;
/*      */               }
/*      */               
/*      */               public <T> T process(String name, T value, Function<String, T> reader, Function<T, String> converter) {
/* 2078 */                 writePrefix(name);
/* 2079 */                 writer.println(converter.apply(value));
/* 2080 */                 return value;
/*      */               }
/*      */             });
/* 2084 */         String fullscreenVideoModeString = getFullscreenVideoModeString();
/* 2085 */         if (fullscreenVideoModeString != null)
/* 2086 */           writer.println("fullscreenResolution:" + fullscreenVideoModeString); 
/* 2088 */         writer.close();
/*      */       } catch (Throwable throwable) {
/*      */         try {
/*      */           writer.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         } 
/*      */         throw throwable;
/*      */       } 
/* 2088 */     } catch (Exception e) {
/* 2089 */       LOGGER.error("Failed to save options", e);
/*      */     } 
/* 2092 */     broadcastOptions();
/*      */   }
/*      */   
/*      */   private String getFullscreenVideoModeString() {
/* 2096 */     Window window = this.minecraft.getWindow();
/* 2097 */     if (window == null)
/* 2099 */       return this.fullscreenVideoModeString; 
/* 2101 */     if (window.getPreferredFullscreenVideoMode().isPresent())
/* 2102 */       return ((VideoMode)window.getPreferredFullscreenVideoMode().get()).write(); 
/* 2104 */     return null;
/*      */   }
/*      */   
/*      */   public ClientInformation buildPlayerInformation() {
/* 2108 */     int parts = 0;
/* 2109 */     for (PlayerModelPart part : this.modelParts)
/* 2110 */       parts |= part.getMask(); 
/* 2113 */     return new ClientInformation(this.languageCode, ((Integer)this.renderDistance.get()).intValue(), this.chatVisibility.get(), ((Boolean)this.chatColors.get()).booleanValue(), parts, this.mainHand.get(), this.minecraft.isTextFilteringEnabled(), ((Boolean)this.allowServerListing.get()).booleanValue(), this.particles.get());
/*      */   }
/*      */   
/*      */   public void broadcastOptions() {
/* 2117 */     if (this.minecraft.player != null)
/* 2118 */       this.minecraft.player.connection.broadcastClientInformation(buildPlayerInformation()); 
/*      */   }
/*      */   
/*      */   public void setModelPart(PlayerModelPart part, boolean visible) {
/* 2123 */     if (visible) {
/* 2124 */       this.modelParts.add(part);
/*      */     } else {
/* 2126 */       this.modelParts.remove(part);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isModelPartEnabled(PlayerModelPart part) {
/* 2131 */     return this.modelParts.contains(part);
/*      */   }
/*      */   
/*      */   public CloudStatus getCloudStatus() {
/* 2135 */     return this.cloudStatus.get();
/*      */   }
/*      */   
/*      */   public boolean useNativeTransport() {
/* 2139 */     return this.useNativeTransport;
/*      */   }
/*      */   
/*      */   public void loadSelectedResourcePacks(PackRepository repository) {
/* 2143 */     Set<String> selected = Sets.newLinkedHashSet();
/* 2144 */     for (Iterator<String> iterator = this.resourcePacks.iterator(); iterator.hasNext(); ) {
/* 2145 */       String id = iterator.next();
/* 2146 */       Pack pack = repository.getPack(id);
/* 2148 */       if (pack == null && !id.startsWith("file/"))
/* 2150 */         pack = repository.getPack("file/" + id); 
/* 2153 */       if (pack == null) {
/* 2154 */         LOGGER.warn("Removed resource pack {} from options because it doesn't seem to exist anymore", id);
/* 2155 */         iterator.remove();
/*      */         continue;
/*      */       } 
/* 2156 */       if (!pack.getCompatibility().isCompatible() && !this.incompatibleResourcePacks.contains(id)) {
/* 2157 */         LOGGER.warn("Removed resource pack {} from options because it is no longer compatible", id);
/* 2158 */         iterator.remove();
/*      */         continue;
/*      */       } 
/* 2159 */       if (pack.getCompatibility().isCompatible() && this.incompatibleResourcePacks.contains(id)) {
/* 2160 */         LOGGER.info("Removed resource pack {} from incompatibility list because it's now compatible", id);
/* 2161 */         this.incompatibleResourcePacks.remove(id);
/*      */         continue;
/*      */       } 
/* 2163 */       selected.add(pack.getId());
/*      */     } 
/* 2166 */     repository.setSelected(selected);
/*      */   }
/*      */   
/*      */   public CameraType getCameraType() {
/* 2170 */     return this.cameraType;
/*      */   }
/*      */   
/*      */   public void setCameraType(CameraType cameraType) {
/* 2174 */     this.cameraType = cameraType;
/*      */   }
/*      */   
/*      */   private static List<String> readListOfStrings(String value) {
/* 2178 */     List<String> result = (List<String>)GsonHelper.fromNullableJson(GSON, value, LIST_OF_STRINGS_TYPE);
/* 2179 */     return (result != null) ? result : Lists.newArrayList();
/*      */   }
/*      */   
/*      */   public File getFile() {
/* 2183 */     return this.optionsFile;
/*      */   }
/*      */   
/*      */   public String dumpOptionsForReport() {
/* 2187 */     final List<Pair<String, Object>> optionsForReport = new ArrayList<>();
/* 2189 */     processDumpedOptions(new OptionAccess(this) {
/*      */           public <T> void process(String name, OptionInstance<T> option) {
/* 2192 */             optionsForReport.add(Pair.of(name, option.get()));
/*      */           }
/*      */         });
/* 2197 */     optionsForReport.add(Pair.of("fullscreenResolution", String.valueOf(this.fullscreenVideoModeString)));
/* 2198 */     optionsForReport.add(Pair.of("glDebugVerbosity", Integer.valueOf(this.glDebugVerbosity)));
/* 2199 */     optionsForReport.add(Pair.of("overrideHeight", Integer.valueOf(this.overrideHeight)));
/* 2200 */     optionsForReport.add(Pair.of("overrideWidth", Integer.valueOf(this.overrideWidth)));
/* 2201 */     optionsForReport.add(Pair.of("syncChunkWrites", Boolean.valueOf(this.syncWrites)));
/* 2202 */     optionsForReport.add(Pair.of("useNativeTransport", Boolean.valueOf(this.useNativeTransport)));
/* 2203 */     optionsForReport.add(Pair.of("resourcePacks", this.resourcePacks));
/* 2205 */     return optionsForReport.stream()
/* 2206 */       .sorted(Comparator.comparing(Pair::getFirst))
/* 2207 */       .map(e -> (String)e.getFirst() + ": " + (String)e.getFirst())
/* 2208 */       .collect(Collectors.joining(System.lineSeparator()));
/*      */   }
/*      */   
/*      */   public void setServerRenderDistance(int serverRenderDistance) {
/* 2212 */     this.serverRenderDistance = serverRenderDistance;
/*      */   }
/*      */   
/*      */   public int getEffectiveRenderDistance() {
/* 2216 */     return (this.serverRenderDistance > 0) ? Math.min(((Integer)this.renderDistance.get()).intValue(), this.serverRenderDistance) : ((Integer)this.renderDistance.get()).intValue();
/*      */   }
/*      */   
/*      */   private static Component pixelValueLabel(Component caption, int value) {
/* 2236 */     return (Component)Component.translatable("options.pixel_value", new Object[] { caption, Integer.valueOf(value) });
/*      */   }
/*      */   
/*      */   private static Component percentValueLabel(Component caption, double value) {
/* 2240 */     return (Component)Component.translatable("options.percent_value", new Object[] { caption, Integer.valueOf((int)(value * 100.0D)) });
/*      */   }
/*      */   
/*      */   public static Component genericValueLabel(Component caption, Component value) {
/* 2244 */     return (Component)Component.translatable("options.generic_value", new Object[] { caption, value });
/*      */   }
/*      */   
/*      */   public static Component genericValueLabel(Component caption, int value) {
/* 2248 */     return genericValueLabel(caption, (Component)Component.literal(Integer.toString(value)));
/*      */   }
/*      */   
/*      */   public static Component genericValueOrOffLabel(Component caption, int value) {
/* 2252 */     if (value == 0)
/* 2253 */       return genericValueLabel(caption, CommonComponents.OPTION_OFF); 
/* 2255 */     return genericValueLabel(caption, value);
/*      */   }
/*      */   
/*      */   private static Component percentValueOrOffLabel(Component caption, double value) {
/* 2259 */     if (value == 0.0D)
/* 2260 */       return genericValueLabel(caption, CommonComponents.OPTION_OFF); 
/* 2262 */     return percentValueLabel(caption, value);
/*      */   }
/*      */   
/*      */   private static interface OptionAccess {
/*      */     <T> void process(String param1String, OptionInstance<T> param1OptionInstance);
/*      */   }
/*      */   
/*      */   private static interface FieldAccess extends OptionAccess {
/*      */     int process(String param1String, int param1Int);
/*      */     
/*      */     boolean process(String param1String, boolean param1Boolean);
/*      */     
/*      */     String process(String param1String1, String param1String2);
/*      */     
/*      */     float process(String param1String, float param1Float);
/*      */     
/*      */     <T> T process(String param1String, T param1T, Function<String, T> param1Function, Function<T, String> param1Function1);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Options.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */