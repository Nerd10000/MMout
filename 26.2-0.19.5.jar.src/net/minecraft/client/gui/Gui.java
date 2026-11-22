/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.BanDetails;
/*     */ import com.mojang.authlib.yggdrasil.ProfileActionType;
/*     */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.GameLoadCookie;
/*     */ import net.minecraft.client.InputType;
/*     */ import net.minecraft.client.KeyMapping;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.components.ChatComponent;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.components.toasts.Toast;
/*     */ import net.minecraft.client.gui.components.toasts.ToastManager;
/*     */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*     */ import net.minecraft.client.gui.font.ActiveArea;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
/*     */ import net.minecraft.client.gui.screens.BanNoticeScreens;
/*     */ import net.minecraft.client.gui.screens.ChatScreen;
/*     */ import net.minecraft.client.gui.screens.DeathScreen;
/*     */ import net.minecraft.client.gui.screens.InBedChatScreen;
/*     */ import net.minecraft.client.gui.screens.Overlay;
/*     */ import net.minecraft.client.gui.screens.PauseScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
/*     */ import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
/*     */ import net.minecraft.client.multiplayer.chat.ChatListener;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.quickplay.QuickPlay;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.client.resources.SplashManager;
/*     */ import net.minecraft.client.tutorial.Tutorial;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.ReloadableResourceManager;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Gui {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  68 */   private static final Component SOCIAL_INTERACTIONS_NOT_AVAILABLE = (Component)Component.translatable("multiplayer.socialInteractions.not_available");
/*     */   
/*  69 */   public static final Component SAVING_LEVEL = (Component)Component.translatable("menu.savingLevel");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   public final Hud hud;
/*     */   
/*     */   private final GuiRenderState guiRenderState;
/*     */   
/*     */   private Screen screen;
/*     */   
/*     */   private Overlay overlay;
/*     */   
/*     */   private boolean clientLevelTeardownInProgress;
/*     */   
/*     */   private final SplashManager splashManager;
/*     */   
/*     */   private final ToastManager toastManager;
/*     */   
/*     */   private final ChatListener chatListener;
/*     */   
/*     */   private TutorialToast socialInteractionsToast;
/*     */   
/*     */   public Gui(Minecraft minecraft, Hud hud, GuiRenderState guiRenderState) {
/*  85 */     this.minecraft = minecraft;
/*  86 */     this.hud = hud;
/*  87 */     this.splashManager = new SplashManager(minecraft.getUser());
/*  88 */     this.toastManager = new ToastManager(minecraft, minecraft.options);
/*  89 */     this.chatListener = new ChatListener(minecraft);
/*  90 */     this.chatListener.setMessageDelay(((Double)minecraft.options.chatDelay().get()).doubleValue());
/*  91 */     this.guiRenderState = guiRenderState;
/*     */   }
/*     */   
/*     */   public void registerReloadListeners(ReloadableResourceManager resourceManager) {
/*  95 */     resourceManager.registerReloadListener((PreparableReloadListener)this.splashManager);
/*  96 */     this.hud.registerReloadListeners(resourceManager);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 100 */     ProfilerFiller profiler = Profiler.get();
/* 101 */     this.chatListener.tick();
/* 103 */     this.hud.tick(this.minecraft.isPaused());
/* 105 */     profiler.push("screen");
/* 107 */     LocalPlayer player = this.minecraft.player;
/* 108 */     if (this.screen == null && player != null) {
/* 109 */       if (player.isDeadOrDying() && !(this.screen instanceof DeathScreen)) {
/* 110 */         setScreen(null);
/* 111 */       } else if (player.isSleeping() && this.minecraft.level != null) {
/* 112 */         this.hud.getChat().openScreen(ChatComponent.ChatMethod.MESSAGE, InBedChatScreen::new);
/*     */       } 
/*     */     } else {
/* 114 */       Screen screen = this.screen;
/* 114 */       if (screen instanceof InBedChatScreen) {
/* 114 */         InBedChatScreen inBedScreen = (InBedChatScreen)screen;
/* 114 */         if (!player.isSleeping())
/* 115 */           inBedScreen.onPlayerWokeUp(); 
/*     */       } 
/*     */     } 
/* 118 */     if (this.screen != null)
/*     */       try {
/* 120 */         this.screen.tick();
/* 121 */       } catch (Throwable t) {
/* 122 */         CrashReport report = CrashReport.forThrowable(t, "Ticking screen");
/* 123 */         this.screen.fillCrashDetails(report);
/* 124 */         throw new ReportedException(report);
/*     */       }  
/* 127 */     profiler.pop();
/* 129 */     if (this.overlay != null)
/* 130 */       this.overlay.tick(); 
/* 133 */     if (!this.minecraft.getDebugOverlay().showDebugScreen())
/* 134 */       this.hud.clearCache(); 
/*     */   }
/*     */   
/*     */   public void update() {
/* 139 */     this.toastManager.update();
/*     */     try {
/* 141 */       if (this.screen != null)
/* 142 */         this.screen.handleDelayedNarration(); 
/* 144 */     } catch (Throwable t) {
/* 145 */       CrashReport report = CrashReport.forThrowable(t, "Narrating screen");
/* 146 */       CrashReportCategory category = report.addCategory("Screen details");
/* 147 */       category.setDetail("Screen name", () -> this.screen.getClass().getCanonicalName());
/* 149 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded) {
/* 154 */     ProfilerFiller profiler = Profiler.get();
/* 156 */     int xMouse = (int)this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
/* 157 */     int yMouse = (int)this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
/* 159 */     profiler.push("gui");
/* 160 */     this.guiRenderState.reset();
/* 161 */     GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(this.minecraft, this.guiRenderState, xMouse, yMouse);
/* 162 */     if (shouldRenderLevel) {
/* 163 */       profiler.push("hud");
/* 164 */       this.hud.extractRenderState(graphics, deltaTracker);
/* 165 */       profiler.pop();
/*     */     } 
/* 167 */     if (this.overlay != null) {
/* 168 */       profiler.push("overlay");
/*     */       try {
/* 170 */         this.overlay.extractRenderState(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
/* 171 */       } catch (Throwable t) {
/* 172 */         CrashReport report = CrashReport.forThrowable(t, "Extracting overlay render state");
/* 173 */         CrashReportCategory category = report.addCategory("Overlay details");
/* 175 */         category.setDetail("Overlay name", () -> this.overlay.getClass().getCanonicalName());
/* 177 */         throw new ReportedException(report);
/*     */       } 
/* 179 */       profiler.pop();
/* 180 */     } else if (resourcesLoaded && this.screen != null) {
/* 181 */       profiler.push("screen");
/*     */       try {
/* 183 */         this.screen.extractRenderStateWithTooltipAndSubtitles(graphics, xMouse, yMouse, deltaTracker.getGameTimeDeltaTicks());
/* 184 */       } catch (Throwable t) {
/* 185 */         CrashReport report = CrashReport.forThrowable(t, "Rendering screen");
/* 186 */         CrashReportCategory category = report.addCategory("Screen render details");
/* 188 */         category.setDetail("Screen name", () -> this.screen.getClass().getCanonicalName());
/* 189 */         this.minecraft.mouseHandler.fillMousePositionDetails(category, this.minecraft.getWindow());
/* 191 */         throw new ReportedException(report);
/*     */       } 
/* 193 */       if (SharedConstants.DEBUG_CURSOR_POS)
/* 194 */         this.minecraft.mouseHandler.drawDebugMouseInfo(this.minecraft.font, graphics); 
/* 197 */       profiler.pop();
/*     */     } 
/* 200 */     if (shouldRenderLevel)
/* 201 */       this.hud.extractSavingIndicator(graphics, deltaTracker); 
/* 204 */     if (resourcesLoaded) {
/* 205 */       Zone ignored = profiler.zone("toasts");
/*     */       try {
/* 206 */         toastManager().extractRenderState(graphics);
/* 207 */         if (ignored != null)
/* 207 */           ignored.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/* 209 */     if (!(this.screen instanceof net.minecraft.client.gui.screens.debug.DebugOptionsScreen))
/* 210 */       this.hud.extractDebugOverlay(graphics); 
/* 213 */     this.hud.extractDeferredSubtitles();
/* 215 */     if (SharedConstants.DEBUG_ACTIVE_TEXT_AREAS)
/* 216 */       renderActiveTextDebug(); 
/* 219 */     profiler.pop();
/* 221 */     graphics.applyCursor(this.minecraft.getWindow());
/*     */   }
/*     */   
/*     */   @Contract(pure = true)
/*     */   public Screen screen() {
/* 226 */     return this.screen;
/*     */   }
/*     */   
/*     */   public void setScreen(Screen screen) {
/*     */     ChatScreen chatScreen;
/* 230 */     if (SharedConstants.IS_RUNNING_IN_IDE && Thread.currentThread() != this.minecraft.getRunningThread())
/* 231 */       LOGGER.error("setScreen called from non-game thread"); 
/* 234 */     if (this.screen != null) {
/* 235 */       this.screen.removed();
/*     */     } else {
/* 237 */       this.minecraft.setLastInputType(InputType.NONE);
/*     */     } 
/* 240 */     if (screen == null) {
/* 241 */       if (this.clientLevelTeardownInProgress)
/* 242 */         throw new IllegalStateException("Trying to return to in-game GUI during disconnection"); 
/* 244 */       if (this.minecraft.level == null) {
/* 245 */         TitleScreen titleScreen = new TitleScreen();
/* 246 */       } else if (this.minecraft.player.isDeadOrDying()) {
/* 247 */         if (this.minecraft.player.shouldShowDeathScreen()) {
/* 248 */           DeathScreen deathScreen = new DeathScreen(null, this.minecraft.level.getLevelData().isHardcore(), this.minecraft.player);
/*     */         } else {
/* 250 */           this.minecraft.player.respawn();
/*     */         } 
/*     */       } else {
/* 253 */         chatScreen = this.hud.getChat().restoreChatScreen();
/*     */       } 
/*     */     } 
/* 257 */     this.screen = (Screen)chatScreen;
/* 258 */     if (this.screen != null)
/* 259 */       this.screen.added(); 
/* 262 */     if (chatScreen != null) {
/* 263 */       this.minecraft.mouseHandler.releaseMouse();
/* 264 */       KeyMapping.releaseAll();
/* 269 */       chatScreen.init(this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
/*     */     } else {
/* 271 */       this.minecraft.textInputManager().stopTextInput();
/* 272 */       if (this.minecraft.level != null)
/* 273 */         KeyMapping.restoreToggleStatesOnScreenClosed(); 
/* 275 */       this.minecraft.getSoundManager().resume();
/* 276 */       this.minecraft.mouseHandler.grabMouse();
/*     */     } 
/* 279 */     this.minecraft.updateTitle();
/*     */   }
/*     */   
/*     */   @Contract(pure = true)
/*     */   public Overlay overlay() {
/* 284 */     return this.overlay;
/*     */   }
/*     */   
/*     */   public void setOverlay(Overlay overlay) {
/* 288 */     this.overlay = overlay;
/*     */   }
/*     */   
/*     */   public boolean isPausing() {
/* 292 */     return ((this.screen != null && this.screen.isPauseScreen()) || (this.overlay != null && this.overlay.isPausing()));
/*     */   }
/*     */   
/*     */   public SplashManager splashManager() {
/* 296 */     return this.splashManager;
/*     */   }
/*     */   
/*     */   public ToastManager toastManager() {
/* 300 */     return this.toastManager;
/*     */   }
/*     */   
/*     */   public ChatListener chatListener() {
/* 304 */     return this.chatListener;
/*     */   }
/*     */   
/*     */   public void addSocialInteractionsToast() {
/* 308 */     MutableComponent mutableComponent1 = Component.translatable("tutorial.socialInteractions.title");
/* 309 */     MutableComponent mutableComponent2 = Component.translatable("tutorial.socialInteractions.description", new Object[] { Tutorial.key("socialInteractions") });
/* 310 */     this.socialInteractionsToast = new TutorialToast(this.minecraft.font, TutorialToast.Icons.SOCIAL_INTERACTIONS, (Component)mutableComponent1, (Component)mutableComponent2, true, 8000);
/* 312 */     this.toastManager.addToast((Toast)this.socialInteractionsToast);
/*     */   }
/*     */   
/*     */   public void setPauseScreen(boolean suppressPauseMenuIfWeReallyArePausing, boolean canGameReallyBePaused) {
/* 316 */     if (this.screen != null)
/*     */       return; 
/* 319 */     if (canGameReallyBePaused) {
/* 320 */       setScreen((Screen)new PauseScreen(!suppressPauseMenuIfWeReallyArePausing));
/*     */     } else {
/* 322 */       setScreen((Screen)new PauseScreen(true));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleKeybinds() {
/* 327 */     Options options = this.minecraft.options;
/* 328 */     while (options.keyToggleGui.consumeClick())
/* 329 */       this.hud.toggle(); 
/* 332 */     while (options.keyAdvancements.consumeClick())
/* 333 */       setScreen((Screen)new AdvancementsScreen(this.minecraft.player.connection.getAdvancements())); 
/* 336 */     while (options.keySocialInteractions.consumeClick()) {
/* 337 */       if (!this.minecraft.isMultiplayerServer() && !SharedConstants.DEBUG_SOCIAL_INTERACTIONS) {
/* 338 */         this.chatListener.handleOverlay(SOCIAL_INTERACTIONS_NOT_AVAILABLE);
/* 339 */         this.minecraft.getNarrator().saySystemNow(SOCIAL_INTERACTIONS_NOT_AVAILABLE);
/*     */         continue;
/*     */       } 
/* 341 */       if (this.socialInteractionsToast != null) {
/* 342 */         this.socialInteractionsToast.hide();
/* 343 */         this.socialInteractionsToast = null;
/*     */       } 
/* 345 */       setScreen((Screen)new SocialInteractionsScreen());
/*     */     } 
/* 349 */     while (options.keyChat.consumeClick())
/* 350 */       openChatScreen(ChatComponent.ChatMethod.MESSAGE); 
/* 352 */     if (this.screen == null && this.overlay == null && options.keyCommand.consumeClick())
/* 353 */       openChatScreen(ChatComponent.ChatMethod.COMMAND); 
/*     */   }
/*     */   
/*     */   public void openChatScreen(ChatComponent.ChatMethod chatMethod) {
/* 358 */     if (this.minecraft.player != null)
/* 359 */       this.hud.getChat().openScreen(chatMethod, ChatScreen::new); 
/*     */   }
/*     */   
/*     */   public void openChatAndAddText(ChatComponent.ChatMethod chatMethod, String text) {
/* 364 */     openChatScreen(ChatComponent.ChatMethod.COMMAND);
/* 365 */     Screen screen = this.screen;
/* 365 */     if (screen instanceof ChatScreen) {
/* 365 */       ChatScreen chatScreen = (ChatScreen)screen;
/* 366 */       chatScreen.insertText(text, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Runnable buildInitialScreens(GameLoadCookie cookie) {
/* 371 */     List<Function<Runnable, Screen>> screens = new ArrayList<>();
/* 372 */     boolean onboardingScreenAdded = addInitialScreens(screens);
/* 374 */     Runnable nextStep = () -> {
/*     */         if (cookie != null && cookie.quickPlayData().isEnabled()) {
/*     */           QuickPlay.connect(this.minecraft, cookie.quickPlayData().variant(), cookie.realmsClient());
/*     */         } else {
/*     */           setScreen((Screen)new TitleScreen(true, new LogoRenderer(onboardingScreenAdded)));
/*     */         } 
/*     */       };
/* 382 */     for (Function<Runnable, Screen> function : (Iterable<Function<Runnable, Screen>>)Lists.reverse(screens)) {
/* 383 */       Screen screen = function.apply(nextStep);
/* 384 */       nextStep = (() -> setScreen(screen));
/*     */     } 
/* 387 */     return nextStep;
/*     */   }
/*     */   
/*     */   private boolean addInitialScreens(List<Function<Runnable, Screen>> screens) {
/* 391 */     boolean onboardingScreenAdded = false;
/* 392 */     if (this.minecraft.options.onboardAccessibility || SharedConstants.DEBUG_FORCE_ONBOARDING_SCREEN) {
/* 393 */       screens.add(next -> new AccessibilityOnboardingScreen(this.minecraft.options, next));
/* 394 */       onboardingScreenAdded = true;
/*     */     } 
/* 397 */     BanDetails multiplayerBan = this.minecraft.multiplayerBan();
/* 398 */     if (multiplayerBan != null)
/* 399 */       screens.add(next -> BanNoticeScreens.create((), multiplayerBan)); 
/* 407 */     ProfileResult profileResult = this.minecraft.getProfileResult();
/* 408 */     if (profileResult != null) {
/* 409 */       GameProfile profile = profileResult.profile();
/* 410 */       Set<ProfileActionType> actions = profileResult.actions();
/* 411 */       if (actions.contains(ProfileActionType.FORCED_NAME_CHANGE))
/* 412 */         screens.add(onClose -> BanNoticeScreens.createNameBan(profile.name(), onClose)); 
/* 414 */       if (actions.contains(ProfileActionType.USING_BANNED_SKIN))
/* 415 */         screens.add(BanNoticeScreens::createSkinBan); 
/*     */     } 
/* 418 */     return onboardingScreenAdded;
/*     */   }
/*     */   
/*     */   public boolean canInterruptScreen() {
/* 422 */     return ((this.screen == null || this.screen.canInterruptWithAnotherScreen()) && !this.clientLevelTeardownInProgress);
/*     */   }
/*     */   
/*     */   public void setClientLevelTeardownInProgress(boolean clientLevelTeardownInProgress) {
/* 426 */     this.clientLevelTeardownInProgress = clientLevelTeardownInProgress;
/*     */   }
/*     */   
/*     */   private void renderActiveTextDebug() {
/* 430 */     this.guiRenderState.nextStratum();
/* 431 */     this.guiRenderState.forEachText(text -> text.ensurePrepared().visit(new Font.GlyphVisitor() {
/*     */             private int index;
/*     */             
/*     */             public void acceptGlyph(TextRenderable.Styled glyph) {
/* 437 */               renderDebugMarkers((ActiveArea)glyph, false);
/*     */             }
/*     */             
/*     */             public void acceptEmptyArea(EmptyArea empty) {
/* 442 */               renderDebugMarkers((ActiveArea)empty, true);
/*     */             }
/*     */             
/*     */             private void renderDebugMarkers(ActiveArea glyph, boolean isEmpty) {
/* 446 */               int intensity = (isEmpty ? 128 : 255) - (this.index++ & 0x1) * 64;
/* 448 */               Style style = glyph.style();
/* 449 */               int red = (style.getClickEvent() != null) ? intensity : 0;
/* 450 */               int green = (style.getHoverEvent() != null) ? intensity : 0;
/* 451 */               int blue = (red == 0 || green == 0) ? intensity : 0;
/* 452 */               int color = ARGB.color(128, red, green, blue);
/* 454 */               Gui.this.guiRenderState.addGuiElement((GuiElementRenderState)new ColoredRectangleRenderState(RenderPipelines.GUI, 
/*     */                     
/* 456 */                     TextureSetup.noTexture(), text.pose, 
/*     */                     
/* 458 */                     (int)glyph.activeLeft(), 
/* 459 */                     (int)glyph.activeTop(), 
/* 460 */                     (int)glyph.activeRight(), 
/* 461 */                     (int)glyph.activeBottom(), color, color, text.scissor));
/*     */             }
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Gui.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */