/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
/*     */ import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
/*     */ import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class OptionsScreen extends Screen implements HasGamemasterPermissionReaction {
/*  25 */   private static final Component TITLE = (Component)Component.translatable("options.title");
/*     */   
/*  26 */   private static final Component SKIN_CUSTOMIZATION = (Component)Component.translatable("options.skinCustomisation");
/*     */   
/*  27 */   private static final Component SOUNDS = (Component)Component.translatable("options.sounds");
/*     */   
/*  28 */   private static final Component VIDEO = (Component)Component.translatable("options.video");
/*     */   
/*  29 */   public static final Component CONTROLS = (Component)Component.translatable("options.controls");
/*     */   
/*  30 */   private static final Component LANGUAGE = (Component)Component.translatable("options.language");
/*     */   
/*  31 */   private static final Component CHAT = (Component)Component.translatable("options.chat");
/*     */   
/*  32 */   private static final Component RESOURCEPACK = (Component)Component.translatable("options.resourcepack");
/*     */   
/*  33 */   private static final Component ACCESSIBILITY = (Component)Component.translatable("options.accessibility");
/*     */   
/*  34 */   private static final Component TELEMETRY = (Component)Component.translatable("options.telemetry");
/*     */   
/*  35 */   private static final Tooltip TELEMETRY_DISABLED_TOOLTIP = Tooltip.create((Component)Component.translatable("options.telemetry.disabled"));
/*     */   
/*  36 */   private static final Component CREDITS_AND_ATTRIBUTION = (Component)Component.translatable("options.credits_and_attribution");
/*     */   
/*     */   private static final int COLUMNS = 2;
/*     */   
/*  40 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private final boolean inWorld;
/*     */   
/*     */   public OptionsScreen(Screen lastScreen, Options options, boolean inWorld) {
/*  48 */     super(TITLE);
/*  49 */     this.lastScreen = lastScreen;
/*  50 */     this.options = options;
/*  51 */     this.inWorld = inWorld;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  56 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(8));
/*  57 */     header.addChild((LayoutElement)new StringWidget(TITLE, this.font), LayoutSettings::alignHorizontallyCenter);
/*  58 */     LinearLayout subHeader = ((LinearLayout)header.addChild((LayoutElement)LinearLayout.horizontal())).spacing(8);
/*  59 */     subHeader.addChild((LayoutElement)this.options.fov().createButton(this.minecraft.options));
/*  60 */     if (this.inWorld) {
/*  61 */       subHeader.addChild((LayoutElement)Button.builder((Component)Component.translatable("options.worldOptions.button"), paramButton -> this.minecraft.gui.setScreen(new WorldOptionsScreen(this, (Level)Objects.requireNonNull(this.minecraft.level)))).build());
/*     */     } else {
/*  63 */       subHeader.addChild(createOnlineButton());
/*     */     } 
/*  66 */     GridLayout gridLayout = new GridLayout();
/*  67 */     gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
/*  69 */     GridLayout.RowHelper helper = gridLayout.createRowHelper(2);
/*  71 */     helper.addChild((LayoutElement)openScreenButton(SKIN_CUSTOMIZATION, () -> new SkinCustomizationScreen(this, this.options)));
/*  72 */     helper.addChild((LayoutElement)openScreenButton(SOUNDS, () -> new SoundOptionsScreen(this, this.options)));
/*  73 */     helper.addChild((LayoutElement)openScreenButton(VIDEO, () -> new VideoSettingsScreen(this, this.minecraft, this.options)));
/*  74 */     helper.addChild((LayoutElement)openScreenButton(CONTROLS, () -> new ControlsScreen(this, this.options)));
/*  75 */     helper.addChild((LayoutElement)openScreenButton(LANGUAGE, () -> new LanguageSelectScreen(this, this.options, this.minecraft.getLanguageManager())));
/*  76 */     helper.addChild((LayoutElement)openScreenButton(CHAT, () -> new ChatOptionsScreen(this, this.options)));
/*  77 */     helper.addChild((LayoutElement)openScreenButton(RESOURCEPACK, () -> new PackSelectionScreen(this.minecraft.getResourcePackRepository(), this::applyPacks, this.minecraft.getResourcePackDirectory(), (Component)Component.translatable("resourcePack.title"))));
/*  78 */     helper.addChild((LayoutElement)openScreenButton(ACCESSIBILITY, () -> new AccessibilityOptionsScreen(this, this.options)));
/*  79 */     Button telemetryButton = (Button)helper.addChild((LayoutElement)openScreenButton(TELEMETRY, () -> new TelemetryInfoScreen(this, this.options)));
/*  80 */     if (!this.minecraft.allowsTelemetry()) {
/*  81 */       telemetryButton.active = false;
/*  82 */       telemetryButton.setTooltip(TELEMETRY_DISABLED_TOOLTIP);
/*     */     } 
/*  84 */     helper.addChild((LayoutElement)openScreenButton(CREDITS_AND_ATTRIBUTION, () -> new CreditsAndAttributionScreen(this)));
/*  86 */     this.layout.addToContents((LayoutElement)gridLayout);
/*  87 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*  89 */     OptionsScreen optionsScreen = this;
/*  89 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  90 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  95 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 100 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public Screen getLastScreen() {
/* 104 */     return this.lastScreen;
/*     */   }
/*     */   
/*     */   private void applyPacks(PackRepository packRepository) {
/* 108 */     this.options.updateResourcePacks(packRepository);
/* 109 */     this.minecraft.gui.setScreen(this);
/*     */   }
/*     */   
/*     */   private LayoutElement createOnlineButton() {
/* 113 */     return (LayoutElement)Button.builder((Component)Component.translatable("options.online"), paramButton -> this.minecraft.gui.setScreen(new OnlineOptionsScreen(this, this.options))).bounds(this.width / 2 + 5, this.height / 6 - 12 + 24, 150, 20).build();
/*     */   }
/*     */   
/*     */   public void removed() {
/* 118 */     this.options.save();
/*     */   }
/*     */   
/*     */   private Button openScreenButton(Component message, Supplier<Screen> screenToScreen) {
/* 122 */     return Button.builder(message, paramButton -> this.minecraft.gui.setScreen(screenToScreen.get())).build();
/*     */   }
/*     */   
/*     */   public void onGamemasterPermissionChanged(boolean hasGamemasterPermission) {
/* 127 */     this.minecraft.gui.setScreen(new OptionsScreen(this.lastScreen, this.minecraft.options, true));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\OptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */