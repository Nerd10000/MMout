/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.BanDetails;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommonButtons;
/*     */ import net.minecraft.client.gui.components.FriendsButton;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.components.PlainTextButton;
/*     */ import net.minecraft.client.gui.components.SplashRenderer;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
/*     */ import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
/*     */ import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.Panorama;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TitleScreen extends Screen {
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  50 */   private static final Component TITLE = (Component)Component.translatable("narrator.screen.title");
/*     */   
/*  51 */   private static final Component COPYRIGHT_TEXT = (Component)Component.translatable("title.credits");
/*     */   
/*     */   private static final String DEMO_LEVEL_ID = "Demo_World";
/*     */   
/*     */   private SplashRenderer splash;
/*     */   
/*     */   private RealmsNotificationsScreen realmsNotificationsScreen;
/*     */   
/*     */   private FriendsButton friends;
/*     */   
/*     */   private boolean fading;
/*     */   
/*     */   private long fadeInStart;
/*     */   
/*     */   private final LogoRenderer logoRenderer;
/*     */   
/*     */   public TitleScreen() {
/*  65 */     this(false);
/*     */   }
/*     */   
/*     */   public TitleScreen(boolean fading) {
/*  69 */     this(fading, (LogoRenderer)null);
/*     */   }
/*     */   
/*     */   public TitleScreen(boolean fading, LogoRenderer logoRenderer) {
/*  73 */     super(TITLE);
/*  74 */     this.fading = fading;
/*  75 */     this.logoRenderer = Objects.<LogoRenderer>requireNonNullElseGet(logoRenderer, () -> new LogoRenderer(false));
/*  76 */     this.minecraft.gameRenderer.panorama().startSpin();
/*     */   }
/*     */   
/*     */   private boolean realmsNotificationsEnabled() {
/*  80 */     return (this.realmsNotificationsScreen != null);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  85 */     if (realmsNotificationsEnabled())
/*  86 */       this.realmsNotificationsScreen.tick(); 
/*  90 */     if (this.minecraft.getPlayerSocialManager().isFriendListEnabled() && this.friends != null)
/*  91 */       this.friends.refreshIncomingRequestCount(); 
/*     */   }
/*     */   
/*     */   public static void registerTextures(TextureManager textureManager) {
/*  96 */     textureManager.registerForNextReload(LogoRenderer.MINECRAFT_LOGO);
/*  97 */     textureManager.registerForNextReload(LogoRenderer.MINECRAFT_EDITION);
/*  98 */     textureManager.registerForNextReload(Panorama.PANORAMA_OVERLAY);
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   protected void init() {
/* 113 */     if (this.splash == null)
/* 114 */       this.splash = this.minecraft.gui.splashManager().getSplash(); 
/* 116 */     int copyrightWidth = this.font.width((FormattedText)COPYRIGHT_TEXT);
/* 117 */     int copyrightX = this.width - copyrightWidth - 2;
/* 119 */     int spacing = 24;
/* 120 */     int topPos = this.height / 4 + 48;
/* 122 */     if (this.minecraft.isDemo()) {
/* 123 */       topPos = createDemoMenuOptions(topPos, 24);
/*     */     } else {
/* 125 */       topPos = createNormalMenuOptions(topPos, 24);
/*     */     } 
/* 128 */     int numberOfButtons = 3;
/* 129 */     int currentButton = 0;
/* 130 */     topPos += 24;
/* 132 */     this.friends = addRenderableWidget(CommonButtons.friends(20, paramButton -> OnlineOptionsScreen.confirmFriendsListEnabled(this.minecraft, (), this), 
/*     */           
/* 134 */           !this.minecraft.isDemo()));
/* 135 */     this.friends.setPosition(getHorizontalPosition(++currentButton, 3, 20), topPos);
/* 137 */     SpriteIconButton language = addRenderableWidget(CommonButtons.language(20, paramButton -> this.minecraft.gui.setScreen((Screen)new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), true));
/* 138 */     language.setPosition(getHorizontalPosition(++currentButton, 3, 20), topPos);
/* 140 */     SpriteIconButton accessibility = addRenderableWidget(CommonButtons.accessibility(20, paramButton -> this.minecraft.gui.setScreen((Screen)new AccessibilityOptionsScreen(this, this.minecraft.options)), true));
/* 141 */     accessibility.setPosition(getHorizontalPosition(++currentButton, 3, 20), topPos);
/* 143 */     topPos += 24;
/* 143 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.options"), paramButton -> this.minecraft.gui.setScreen((Screen)new OptionsScreen(this, this.minecraft.options, false))).bounds(this.width / 2 - 100, topPos, 98, 20).build());
/* 144 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.quit"), paramButton -> this.minecraft.stop()).bounds(this.width / 2 + 2, topPos, 98, 20).build());
/* 146 */     addRenderableWidget(new PlainTextButton(copyrightX, this.height - 10, copyrightWidth, 10, COPYRIGHT_TEXT, paramButton -> this.minecraft.gui.setScreen(new CreditsAndAttributionScreen(this)), this.font));
/* 148 */     if (this.realmsNotificationsScreen == null)
/* 149 */       this.realmsNotificationsScreen = new RealmsNotificationsScreen(); 
/* 152 */     if (realmsNotificationsEnabled())
/* 153 */       this.realmsNotificationsScreen.init(this.width, this.height); 
/*     */   }
/*     */   
/*     */   private int getHorizontalPosition(int currentButton, int numberOfButtons, int buttonWidth) {
/* 158 */     int totalWidth = numberOfButtons * buttonWidth + (numberOfButtons - 1) * 4;
/* 159 */     return this.width / 2 - totalWidth / 2 + (currentButton - 1) * (buttonWidth + 4);
/*     */   }
/*     */   
/*     */   private int createNormalMenuOptions(int topPos, int spacing) {
/* 163 */     Button singleplayerButton = addRenderableWidget(Button.builder((Component)Component.translatable("menu.singleplayer"), paramButton -> this.minecraft.gui.setScreen((Screen)new SelectWorldScreen(this))).bounds(this.width / 2 - 100, topPos, 200, 20).build());
/* 164 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/* 165 */       addRenderableWidget(Button.builder(
/* 166 */             (Component)Component.literal("TW"), paramButton -> CreateWorldScreen.testWorld(this.minecraft, ()))
/*     */           
/* 168 */           .bounds(singleplayerButton.getX() + singleplayerButton.getWidth() + 2, topPos, 20, 20).build()); 
/* 171 */     Component multiplayerDisabledReason = getMultiplayerDisabledReason();
/* 172 */     boolean multiplayerAllowed = (multiplayerDisabledReason == null);
/* 174 */     Tooltip tooltip = (multiplayerDisabledReason != null) ? Tooltip.create(multiplayerDisabledReason) : null;
/* 175 */     ((Button)addRenderableWidget((T)Button.builder((Component)Component.translatable("menu.multiplayer"), button -> {
/*     */             Screen screen = this.minecraft.options.skipMultiplayerWarning ? (Screen)new JoinMultiplayerScreen(this) : (Screen)new SafetyScreen(this);
/*     */             this.minecraft.gui.setScreen(screen);
/* 179 */           }).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).tooltip(tooltip).build())).active = multiplayerAllowed;
/* 180 */     ((Button)addRenderableWidget((T)Button.builder((Component)Component.translatable("menu.online"), paramButton -> this.minecraft.gui.setScreen((Screen)new RealmsMainScreen(this))).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).tooltip(tooltip).build())).active = multiplayerAllowed;
/* 181 */     return topPos;
/*     */   }
/*     */   
/*     */   private Component getMultiplayerDisabledReason() {
/* 185 */     if (this.minecraft.allowsMultiplayer())
/* 186 */       return null; 
/* 189 */     if (this.minecraft.isNameBanned())
/* 190 */       return (Component)Component.translatable("title.multiplayer.disabled.banned.name"); 
/* 193 */     BanDetails multiplayerBan = this.minecraft.multiplayerBan();
/* 194 */     if (multiplayerBan != null) {
/* 195 */       if (multiplayerBan.expires() != null)
/* 196 */         return (Component)Component.translatable("title.multiplayer.disabled.banned.temporary"); 
/* 198 */       return (Component)Component.translatable("title.multiplayer.disabled.banned.permanent");
/*     */     } 
/* 202 */     return (Component)Component.translatable("title.multiplayer.disabled");
/*     */   }
/*     */   
/*     */   private int createDemoMenuOptions(int topPos, int spacing) {
/* 206 */     boolean demoWorldPresent = checkDemoWorldPresence();
/* 208 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.playdemo"), button -> {
/*     */             if (demoWorldPresent) {
/*     */               this.minecraft.createWorldOpenFlows().openWorld("Demo_World", ());
/*     */             } else {
/*     */               this.minecraft.createWorldOpenFlows().createFreshLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, WorldOptions.DEMO_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
/*     */             } 
/* 214 */           }).bounds(this.width / 2 - 100, topPos, 200, 20).build());
/* 215 */     Button resetDemoButton = addRenderableWidget(Button.builder((Component)Component.translatable("menu.resetdemo"), button -> {
/*     */             LevelStorageSource levelSource = this.minecraft.getLevelSource();
/*     */             try {
/*     */               LevelStorageSource.LevelStorageAccess levelAccess = levelSource.createAccess("Demo_World");
/*     */               try {
/*     */                 if (levelAccess.hasWorldData())
/*     */                   this.minecraft.gui.setScreen(new ConfirmScreen(this::confirmDemo, (Component)Component.translatable("selectWorld.deleteQuestion"), (Component)Component.translatable("selectWorld.deleteWarning", new Object[] { MinecraftServer.DEMO_SETTINGS.levelName() }), (Component)Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL)); 
/*     */                 if (levelAccess != null)
/*     */                   levelAccess.close(); 
/* 218 */               } catch (Throwable t$) {
/*     */                 if (levelAccess != null)
/*     */                   try {
/*     */                     levelAccess.close();
/* 218 */                   } catch (Throwable x2) {
/*     */                     t$.addSuppressed(x2);
/*     */                   }  
/*     */                 throw t$;
/*     */               } 
/* 227 */             } catch (IOException e) {
/*     */               SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
/*     */               LOGGER.warn("Failed to access demo world", e);
/*     */             } 
/* 231 */           }).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).build());
/* 232 */     resetDemoButton.active = demoWorldPresent;
/* 233 */     return topPos;
/*     */   }
/*     */   
/*     */   private boolean checkDemoWorldPresence() {
/*     */     try {
/* 238 */       LevelStorageSource.LevelStorageAccess levelSource = this.minecraft.getLevelSource().createAccess("Demo_World");
/*     */       try {
/* 239 */         boolean bool = levelSource.hasWorldData();
/* 240 */         if (levelSource != null)
/* 240 */           levelSource.close(); 
/*     */         return bool;
/*     */       } catch (Throwable throwable) {
/*     */         if (levelSource != null)
/*     */           try {
/*     */             levelSource.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 240 */     } catch (IOException e) {
/* 241 */       SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
/* 242 */       LOGGER.warn("Failed to read demo world data", e);
/* 244 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 249 */     if (this.fadeInStart == 0L && this.fading)
/* 250 */       this.fadeInStart = Util.getMillis(); 
/* 253 */     float widgetFade = 1.0F;
/* 254 */     if (this.fading) {
/* 255 */       float fade = (float)(Util.getMillis() - this.fadeInStart) / 2000.0F;
/* 256 */       if (fade > 1.0F) {
/* 257 */         this.fading = false;
/*     */       } else {
/* 259 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 260 */         widgetFade = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 262 */       fadeWidgets(widgetFade);
/*     */     } 
/* 264 */     extractPanorama(graphics, a);
/* 266 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 267 */     this.logoRenderer.extractRenderState(graphics, this.width, this.logoRenderer.keepLogoThroughFade() ? 1.0F : widgetFade);
/* 269 */     if (this.splash != null && !((Boolean)this.minecraft.options.hideSplashTexts().get()).booleanValue())
/* 270 */       this.splash.extractRenderState(graphics, this.width, this.font, widgetFade); 
/* 273 */     String versionString = "Minecraft " + SharedConstants.getCurrentVersion().name();
/* 275 */     if (this.minecraft.isDemo())
/* 276 */       versionString = versionString + " Demo"; 
/* 279 */     if (Minecraft.checkModStatus().shouldReportAsModified())
/* 280 */       versionString = versionString + versionString; 
/* 283 */     graphics.text(this.font, versionString, 2, this.height - 10, ARGB.white(widgetFade));
/* 285 */     if (realmsNotificationsEnabled() && widgetFade >= 1.0F)
/* 286 */       this.realmsNotificationsScreen.extractRenderState(graphics, mouseX, mouseY, a); 
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {}
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 297 */     if (super.mouseClicked(event, doubleClick))
/* 298 */       return true; 
/* 301 */     if (realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(event, doubleClick))
/* 302 */       return true; 
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   public void removed() {
/* 310 */     if (this.realmsNotificationsScreen != null)
/* 311 */       this.realmsNotificationsScreen.removed(); 
/*     */   }
/*     */   
/*     */   public void added() {
/* 317 */     super.added();
/* 318 */     if (this.realmsNotificationsScreen != null)
/* 319 */       this.realmsNotificationsScreen.added(); 
/*     */   }
/*     */   
/*     */   private void confirmDemo(boolean result) {
/* 324 */     if (result)
/*     */       try {
/* 326 */         LevelStorageSource.LevelStorageAccess levelSource = this.minecraft.getLevelSource().createAccess("Demo_World");
/*     */         try {
/* 327 */           levelSource.deleteLevel();
/* 328 */           if (levelSource != null)
/* 328 */             levelSource.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (levelSource != null)
/*     */             try {
/*     */               levelSource.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 328 */       } catch (IOException e) {
/* 329 */         SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
/* 330 */         LOGGER.warn("Failed to delete demo world", e);
/*     */       }  
/* 333 */     this.minecraft.gui.setScreen(this);
/*     */   }
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/* 338 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\TitleScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */