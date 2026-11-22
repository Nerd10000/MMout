/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.Monitor;
/*     */ import com.mojang.blaze3d.platform.VideoMode;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class VideoSettingsScreen extends OptionsSubScreen {
/*  30 */   private static final Component TITLE = (Component)Component.translatable("options.videoTitle");
/*     */   
/*  32 */   private static final Component IMPROVED_TRANSPARENCY = (Component)Component.translatable("options.improvedTransparency").withStyle(ChatFormatting.ITALIC);
/*     */   
/*  33 */   private static final Component WARNING_MESSAGE = (Component)Component.translatable("options.graphics.warning.message", new Object[] { IMPROVED_TRANSPARENCY, IMPROVED_TRANSPARENCY });
/*     */   
/*  34 */   private static final Component WARNING_TITLE = (Component)Component.translatable("options.graphics.warning.title").withStyle(ChatFormatting.RED);
/*     */   
/*  35 */   private static final Component BUTTON_ACCEPT = (Component)Component.translatable("options.graphics.warning.accept");
/*     */   
/*  36 */   private static final Component BUTTON_CANCEL = (Component)Component.translatable("options.graphics.warning.cancel");
/*     */   
/*  37 */   private static final Component DISPLAY_HEADER = (Component)Component.translatable("options.video.display.header");
/*     */   
/*  38 */   private static final Component QUALITY_HEADER = (Component)Component.translatable("options.video.quality.header");
/*     */   
/*  39 */   private static final Component PREFERENCES_HEADER = (Component)Component.translatable("options.video.preferences.header");
/*     */   
/*  40 */   private static final Component RESTART_REQUIRED = (Component)Component.translatable("options.restartRequired").withColor(-2142128);
/*     */   
/*     */   private final GpuWarnlistManager gpuWarnlistManager;
/*     */   
/*     */   private final int oldMipmaps;
/*     */   
/*     */   private final int oldAnisotropyBit;
/*     */   
/*     */   private final TextureFilteringMethod oldTextureFiltering;
/*     */   
/*     */   private static OptionInstance<?>[] qualityOptions(Options options) {
/*  43 */     return (OptionInstance<?>[])new OptionInstance[] { 
/*  43 */         options
/*  44 */         .biomeBlendRadius(), options
/*  45 */         .renderDistance(), options
/*     */         
/*  47 */         .prioritizeChunkUpdates(), options
/*  48 */         .simulationDistance(), options
/*     */         
/*  50 */         .ambientOcclusion(), options
/*  51 */         .cloudStatus(), options
/*     */         
/*  53 */         .particles(), options
/*  54 */         .mipmapLevels(), options
/*     */         
/*  56 */         .entityShadows(), options
/*  57 */         .entityDistanceScaling(), 
/*  57 */         options
/*     */         
/*  59 */         .menuBackgroundBlurriness(), options
/*  60 */         .cloudRange(), options
/*     */         
/*  62 */         .cutoutLeaves(), options
/*  63 */         .improvedTransparency(), options
/*     */         
/*  65 */         .textureFiltering(), options
/*  66 */         .maxAnisotropyBit(), options
/*     */         
/*  68 */         .weatherRadius() };
/*     */   }
/*     */   
/*     */   private static OptionInstance<?>[] displayOptions(Options options) {
/*  73 */     return (OptionInstance<?>[])new OptionInstance[] { options
/*  74 */         .framerateLimit(), options
/*  75 */         .enableVsync(), options
/*     */         
/*  77 */         .inactivityFpsLimit(), options
/*  78 */         .guiScale(), options
/*     */         
/*  80 */         .fullscreen(), options
/*  81 */         .exclusiveFullscreen(), options
/*  82 */         .gamma(), options
/*     */         
/*  84 */         .preferredGraphicsBackend() };
/*     */   }
/*     */   
/*     */   private static OptionInstance<?>[] preferenceOptions(Options options) {
/*  89 */     return (OptionInstance<?>[])new OptionInstance[] { options
/*  90 */         .showAutosaveIndicator(), options
/*  91 */         .vignette(), options
/*     */         
/*  93 */         .attackIndicator(), options
/*  94 */         .chunkSectionFadeInTime() };
/*     */   }
/*     */   
/* 102 */   private final LinearLayout header = LinearLayout.vertical().spacing(2);
/*     */   
/*     */   private StringWidget restartWarning;
/*     */   
/*     */   public VideoSettingsScreen(Screen lastScreen, Minecraft minecraft, Options options) {
/* 106 */     super(lastScreen, options, TITLE);
/* 107 */     this.gpuWarnlistManager = minecraft.getGpuWarnlistManager();
/* 109 */     this.gpuWarnlistManager.resetWarnings();
/* 110 */     if (((Boolean)options.improvedTransparency().get()).booleanValue())
/* 112 */       this.gpuWarnlistManager.dismissWarning(); 
/* 115 */     this.oldMipmaps = ((Integer)options.mipmapLevels().get()).intValue();
/* 116 */     this.oldAnisotropyBit = ((Integer)options.maxAnisotropyBit().get()).intValue();
/* 117 */     this.oldTextureFiltering = (TextureFilteringMethod)options.textureFiltering().get();
/*     */   }
/*     */   
/*     */   protected void addOptions() {
/* 122 */     int initialValue, CURRENT_MODE = -1;
/* 124 */     Window window = this.minecraft.getWindow();
/* 125 */     Monitor monitor = window.findBestMonitor();
/* 127 */     if (monitor == null) {
/* 128 */       initialValue = -1;
/*     */     } else {
/* 130 */       Optional<VideoMode> preferredFullscreenVideoMode = window.getPreferredFullscreenVideoMode();
/* 131 */       Objects.requireNonNull(monitor);
/* 131 */       initialValue = ((Integer)preferredFullscreenVideoMode.<Integer>map(monitor::indexOfMode).orElse(Integer.valueOf(-1))).intValue();
/*     */     } 
/* 149 */     OptionInstance<Integer> fullscreenOption = new OptionInstance("options.fullscreen.resolution", OptionInstance.noTooltip(), (caption, value) -> {
/*     */           if (monitor == null)
/*     */             return (Component)Component.translatable("options.fullscreen.unavailable"); 
/*     */           if (value.intValue() == -1)
/*     */             return Options.genericValueLabel(caption, (Component)Component.translatable("options.fullscreen.current")); 
/*     */           VideoMode mode = monitor.mode(value.intValue());
/*     */           return Options.genericValueLabel(caption, (Component)Component.translatable("options.fullscreen.entry", new Object[] { Integer.valueOf(mode.getWidth()), Integer.valueOf(mode.getHeight()), Integer.valueOf(mode.getRefreshRate()), Integer.valueOf(mode.getRedBits() + mode.getGreenBits() + mode.getBlueBits()) }));
/* 149 */         }(OptionInstance.ValueSet)new OptionInstance.IntRange(-1, (monitor != null) ? (monitor.modeCount() - 1) : -1), Integer.valueOf(initialValue), value -> {
/*     */           if (monitor == null)
/*     */             return; 
/*     */           window.setPreferredFullscreenVideoMode((value.intValue() == -1) ? Optional.empty() : Optional.<VideoMode>of(monitor.mode(value.intValue())));
/*     */         });
/* 158 */     this.list.addHeader(DISPLAY_HEADER);
/* 159 */     this.list.addBig(fullscreenOption);
/* 160 */     this.list.addSmall((OptionInstance[])displayOptions(this.options));
/* 162 */     this.list.addHeader(QUALITY_HEADER);
/* 163 */     this.list.addBig(this.options.graphicsPreset());
/* 164 */     this.list.addSmall((OptionInstance[])qualityOptions(this.options));
/* 166 */     this.list.addHeader(PREFERENCES_HEADER);
/* 167 */     this.list.addSmall((OptionInstance[])preferenceOptions(this.options));
/*     */   }
/*     */   
/*     */   protected void addTitle() {
/* 172 */     this.header.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/* 173 */     this.header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/* 174 */     if (this.options.isRestartRequiredToApplyVideoSettings()) {
/* 177 */       this.restartWarning = new StringWidget(RESTART_REQUIRED, this.font);
/* 178 */       this.header.addChild((LayoutElement)this.restartWarning);
/*     */     } 
/* 180 */     this.layout.addToHeader((LayoutElement)this.header);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 185 */     if (this.list != null) {
/* 185 */       AbstractWidget abstractWidget = this.list.findOption(this.options.maxAnisotropyBit());
/* 185 */       if (abstractWidget instanceof AbstractSliderButton) {
/* 185 */         AbstractSliderButton maxAnisotropy = (AbstractSliderButton)abstractWidget;
/* 186 */         maxAnisotropy.active = (this.options.textureFiltering().get() == TextureFilteringMethod.ANISOTROPIC);
/*     */       } 
/*     */     } 
/* 188 */     boolean restartRequired = this.options.isRestartRequiredToApplyVideoSettings();
/* 189 */     if (restartRequired && (this.restartWarning == null || !this.restartWarning.visible)) {
/* 190 */       if (this.restartWarning == null) {
/* 191 */         this.restartWarning = new StringWidget(RESTART_REQUIRED, this.font);
/* 192 */         this.header.addChild((LayoutElement)this.restartWarning);
/* 193 */         addRenderableWidget((GuiEventListener)this.restartWarning);
/*     */       } 
/* 195 */       this.restartWarning.visible = true;
/* 196 */       repositionElements();
/* 197 */     } else if (!restartRequired && this.restartWarning != null && this.restartWarning.visible) {
/* 198 */       this.restartWarning.visible = false;
/* 199 */       repositionElements();
/*     */     } 
/* 201 */     super.tick();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 206 */     this.minecraft.getWindow().changeFullscreenVideoMode();
/* 207 */     super.onClose();
/*     */   }
/*     */   
/*     */   public void removed() {
/* 212 */     if (((Integer)this.options.mipmapLevels().get()).intValue() != this.oldMipmaps || ((Integer)this.options.maxAnisotropyBit().get()).intValue() != this.oldAnisotropyBit || this.options.textureFiltering().get() != this.oldTextureFiltering) {
/* 213 */       this.minecraft.updateMaxMipLevel(((Integer)this.options.mipmapLevels().get()).intValue());
/* 214 */       this.minecraft.delayTextureReload();
/*     */     } 
/* 216 */     super.removed();
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 221 */     if (super.mouseClicked(event, doubleClick)) {
/* 222 */       if (this.gpuWarnlistManager.isShowingWarning()) {
/* 224 */         List<Component> warningMessage = Lists.newArrayList((Object[])new Component[] { WARNING_MESSAGE, CommonComponents.NEW_LINE });
/* 226 */         String rendererWarnings = this.gpuWarnlistManager.getRendererWarnings();
/* 227 */         if (rendererWarnings != null) {
/* 228 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 229 */           warningMessage.add(Component.translatable("options.graphics.warning.renderer", new Object[] { rendererWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/* 232 */         String vendorWarnings = this.gpuWarnlistManager.getVendorWarnings();
/* 233 */         if (vendorWarnings != null) {
/* 234 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 235 */           warningMessage.add(Component.translatable("options.graphics.warning.vendor", new Object[] { vendorWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/* 238 */         String versionWarnings = this.gpuWarnlistManager.getVersionWarnings();
/* 239 */         if (versionWarnings != null) {
/* 240 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 241 */           warningMessage.add(Component.translatable("options.graphics.warning.version", new Object[] { versionWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/* 244 */         this.minecraft.gui.setScreen(new UnsupportedGraphicsWarningScreen(WARNING_TITLE, warningMessage, ImmutableList.of(new UnsupportedGraphicsWarningScreen.ButtonOption(BUTTON_ACCEPT, btn -> {
/*     */                     this.options.improvedTransparency().set(Boolean.valueOf(true));
/*     */                     (Minecraft.getInstance()).levelExtractor.allChanged();
/*     */                     this.gpuWarnlistManager.dismissWarning();
/*     */                     this.minecraft.gui.setScreen(this);
/* 244 */                   }), new UnsupportedGraphicsWarningScreen.ButtonOption(BUTTON_CANCEL, btn -> {
/*     */                     this.gpuWarnlistManager.dismissWarning();
/*     */                     this.options.improvedTransparency().set(Boolean.valueOf(false));
/*     */                     updateTransparencyButton();
/*     */                     this.minecraft.gui.setScreen(this);
/*     */                   }))));
/*     */       } 
/* 261 */       return true;
/*     */     } 
/* 263 */     return false;
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 268 */     if (this.minecraft.hasControlDown()) {
/* 269 */       OptionInstance<Integer> guiScale = this.options.guiScale();
/* 270 */       OptionInstance.ValueSet valueSet = guiScale.values();
/* 270 */       if (valueSet instanceof OptionInstance.ClampingLazyMaxIntRange) {
/* 270 */         OptionInstance.ClampingLazyMaxIntRange clampingLazyMaxIntRange = (OptionInstance.ClampingLazyMaxIntRange)valueSet;
/* 271 */         int oldValue = ((Integer)guiScale.get()).intValue();
/* 272 */         int adjustedOldValue = (oldValue == 0) ? (clampingLazyMaxIntRange.maxInclusive() + 1) : oldValue;
/* 273 */         int newValue = adjustedOldValue + (int)Math.signum(scrollY);
/* 274 */         if (newValue != 0 && newValue <= clampingLazyMaxIntRange.maxInclusive() && newValue >= clampingLazyMaxIntRange.minInclusive()) {
/* 275 */           CycleButton<Integer> cycleButton = (CycleButton<Integer>)this.list.findOption(guiScale);
/* 276 */           if (cycleButton != null) {
/* 277 */             guiScale.set(Integer.valueOf(newValue));
/* 278 */             cycleButton.setValue(Integer.valueOf(newValue));
/* 279 */             this.list.setScrollAmount(0.0D);
/* 280 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 284 */       return false;
/*     */     } 
/* 286 */     return super.mouseScrolled(x, y, scrollX, scrollY);
/*     */   }
/*     */   
/*     */   public void updateFullscreenButton(boolean fullscreen) {
/* 291 */     if (this.list != null) {
/* 292 */       AbstractWidget fullscreenWidget = this.list.findOption(this.options.fullscreen());
/* 293 */       if (fullscreenWidget != null) {
/* 294 */         CycleButton<Boolean> fullscreenButton = (CycleButton<Boolean>)fullscreenWidget;
/* 295 */         fullscreenButton.setValue(Boolean.valueOf(fullscreen));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateTransparencyButton() {
/* 301 */     if (this.list != null) {
/* 302 */       OptionInstance<Boolean> option = this.options.improvedTransparency();
/* 303 */       AbstractWidget widget = this.list.findOption(option);
/* 304 */       if (widget != null) {
/* 305 */         CycleButton<Boolean> button = (CycleButton<Boolean>)widget;
/* 306 */         button.setValue(option.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\VideoSettingsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */