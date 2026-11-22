/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.client.RealmsError;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.PreferredRegionsDto;
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RegionDataDto;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
/*     */ import com.mojang.realmsclient.dto.ServiceQuality;
/*     */ import com.mojang.realmsclient.dto.Subscription;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import com.mojang.realmsclient.util.task.CloseServerTask;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.OpenServerTask;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.LoadingTab;
/*     */ import net.minecraft.client.gui.components.tabs.MenuTabBar;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsConfigureWorldScreen extends RealmsScreen {
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  52 */   private static final Component PLAY_TEXT = (Component)Component.translatable("mco.selectServer.play");
/*     */   
/*     */   private final RealmsMainScreen lastScreen;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   private PreferredRegionsDto regions;
/*     */   
/*     */   private Subscription subscription;
/*     */   
/*  62 */   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality = new LinkedHashMap<>();
/*     */   
/*     */   private final long serverId;
/*     */   
/*     */   private boolean stateChanged;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   private Button playButton;
/*     */   
/*     */   private MenuTabBar tabNavigationBar;
/*     */   
/*     */   final HeaderAndFooterLayout layout;
/*     */   
/*     */   public RealmsConfigureWorldScreen(RealmsMainScreen lastScreen, long serverId, RealmsServer serverData, PreferredRegionsDto regions) {
/*  77 */     super((Component)Component.empty());
/*     */     RealmsConfigureWorldScreen realmsConfigureWorldScreen = this;
/*     */     realmsConfigureWorldScreen = this;
/*     */     this.tabManager = new TabManager(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0), x$0 -> rec$.removeWidget((GuiEventListener)x$0), this::onTabSelected, this::onTabDeselected);
/*     */     this.layout = new HeaderAndFooterLayout((Screen)this);
/*  78 */     this.lastScreen = lastScreen;
/*  79 */     this.serverId = serverId;
/*  80 */     this.serverData = serverData;
/*  81 */     this.regions = regions;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen(RealmsMainScreen lastScreen, long serverId) {
/*  85 */     this(lastScreen, serverId, (RealmsServer)null, (PreferredRegionsDto)null);
/*     */   }
/*     */   
/*     */   public void init() {
/*  90 */     if (this.serverData == null)
/*  91 */       fetchServerData(this.serverId); 
/*  94 */     if (this.regions == null)
/*  95 */       fetchRegionData(); 
/*  98 */     if (this.subscription == null)
/*  99 */       fetchSubscription(this.serverId); 
/* 102 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.loading");
/* 103 */     this
/*     */       
/* 109 */       .tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new LoadingTab(getFont(), RealmsWorldsTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsPlayersTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsSubscriptionTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsSettingsTab.TITLE, (Component)mutableComponent) }).build();
/* 110 */     this.tabNavigationBar.setTabActiveState(3, false);
/* 111 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/* 113 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 115 */     this.playButton = (Button)footer.addChild((LayoutElement)Button.builder(PLAY_TEXT, button -> {
/*     */             onClose();
/*     */             RealmsMainScreen.play(this.serverData, (Screen)this);
/* 118 */           }).width(150).build());
/* 119 */     this.playButton.active = false;
/* 121 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/* 123 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 128 */     this.tabNavigationBar.selectTab(0, false);
/* 129 */     repositionElements();
/* 131 */     if (this.serverData != null && this.regions != null && this.subscription != null)
/* 132 */       onRealmsDataFetched(); 
/*     */   }
/*     */   
/*     */   private void onTabSelected(Tab tab) {
/* 137 */     if (this.serverData != null && tab instanceof RealmsConfigurationTab) {
/* 137 */       RealmsConfigurationTab configurationTab = (RealmsConfigurationTab)tab;
/* 138 */       configurationTab.onSelected(this.serverData);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onTabDeselected(Tab tab) {
/* 143 */     if (this.serverData != null && tab instanceof RealmsConfigurationTab) {
/* 143 */       RealmsConfigurationTab configurationTab = (RealmsConfigurationTab)tab;
/* 144 */       configurationTab.onDeselected(this.serverData);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getContentHeight() {
/* 149 */     return this.layout.getContentHeight();
/*     */   }
/*     */   
/*     */   public int getHeaderHeight() {
/* 153 */     return this.layout.getHeaderHeight();
/*     */   }
/*     */   
/*     */   public Screen getLastScreen() {
/* 157 */     return (Screen)this.lastScreen;
/*     */   }
/*     */   
/*     */   public Screen createErrorScreen(RealmsServiceException exception) {
/* 161 */     return (Screen)new RealmsGenericErrorScreen(exception, (Screen)this.lastScreen);
/*     */   }
/*     */   
/*     */   public void repositionElements() {
/* 166 */     if (this.tabNavigationBar == null)
/*     */       return; 
/* 169 */     this.tabNavigationBar.arrangeElements(this.width);
/* 171 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 172 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 173 */     this.tabManager.setTabArea(tabArea);
/* 174 */     this.layout.setHeaderHeight(tabAreaTop);
/* 175 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void updateButtonStates() {
/* 179 */     if (this.serverData != null && this.playButton != null) {
/* 180 */       this.playButton.active = this.serverData.shouldPlayButtonBeActive();
/* 181 */       if (!this.playButton.active && this.serverData.state == RealmsServer.State.CLOSED)
/* 182 */         this.playButton.setTooltip(Tooltip.create(RealmsServer.WORLD_CLOSED_COMPONENT)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/* 189 */     super.extractRenderState(graphics, xm, ym, a);
/* 190 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 195 */     if (this.tabNavigationBar.keyPressed(event))
/* 196 */       return true; 
/* 198 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   protected void extractMenuBackground(GuiGraphicsExtractor graphics) {
/* 203 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 204 */     extractMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 209 */     if (this.serverData != null) {
/* 209 */       Tab tab = this.tabManager.getCurrentTab();
/* 209 */       if (tab instanceof RealmsConfigurationTab) {
/* 209 */         RealmsConfigurationTab realmsConfigurationTab = (RealmsConfigurationTab)tab;
/* 210 */         realmsConfigurationTab.onDeselected(this.serverData);
/*     */       } 
/*     */     } 
/* 212 */     this.minecraft.gui.setScreen((Screen)this.lastScreen);
/* 213 */     if (this.stateChanged)
/* 214 */       this.lastScreen.resetScreen(); 
/*     */   }
/*     */   
/*     */   public void fetchRegionData() {
/* 219 */     RealmsUtil.supplyAsync(RealmsClient::getPreferredRegionSelections, 
/*     */         
/* 221 */         RealmsUtil.openScreenAndLogOnFailure(this::createErrorScreen, "Couldn't get realms region data"))
/* 222 */       .thenAcceptAsync(regions -> {
/*     */           this.regions = regions;
/*     */           onRealmsDataFetched();
/*     */         }(Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   public void fetchServerData(long realmId) {
/* 229 */     RealmsUtil.supplyAsync(client -> client.getOwnRealm(realmId), 
/*     */         
/* 231 */         RealmsUtil.openScreenAndLogOnFailure(this::createErrorScreen, "Couldn't get own world"))
/* 232 */       .thenAcceptAsync(serverData -> {
/*     */           this.serverData = serverData;
/*     */           onRealmsDataFetched();
/*     */         }(Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   public void fetchSubscription(long realmId) {
/* 239 */     RealmsUtil.supplyAsync(client -> client.subscriptionFor(realmId), e -> {
/*     */           LOGGER.error("Couldn't get subscription", (Throwable)e);
/*     */           this.minecraft.execute(());
/* 249 */         }).thenAcceptAsync(subscription -> {
/*     */           this.subscription = subscription;
/*     */           onRealmsDataFetched();
/*     */         }(Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   private void onRealmsDataFetched() {
/* 256 */     if (this.serverData == null || this.regions == null || this.subscription == null)
/*     */       return; 
/* 260 */     this.regionServiceQuality.clear();
/* 261 */     for (RegionDataDto region : this.regions.regionData()) {
/* 262 */       if (region.region() != RealmsRegion.INVALID_REGION)
/* 263 */         this.regionServiceQuality.put(region.region(), region.serviceQuality()); 
/*     */     } 
/* 267 */     int focusedTabIndex = -1;
/* 268 */     if (this.tabNavigationBar != null)
/* 269 */       focusedTabIndex = this.tabNavigationBar.getTabs().indexOf(this.tabManager.getCurrentTab()); 
/* 272 */     if (this.tabNavigationBar != null)
/* 273 */       removeWidget((GuiEventListener)this.tabNavigationBar); 
/* 276 */     this.tabNavigationBar = (MenuTabBar)addRenderableWidget((GuiEventListener)MenuTabBar.builder(this.tabManager, this.width)
/* 277 */         .addTabs(new Tab[] { (Tab)new RealmsWorldsTab(this, Objects.<Minecraft>requireNonNull(this.minecraft), this.serverData), (Tab)new RealmsPlayersTab(this, this.minecraft, this.serverData), (Tab)new RealmsSubscriptionTab(this, this.minecraft, this.serverData, this.subscription), (Tab)new RealmsSettingsTab(this, this.minecraft, this.serverData, this.regionServiceQuality) }).build());
/* 284 */     setFocused((GuiEventListener)this.tabNavigationBar);
/* 285 */     if (focusedTabIndex != -1)
/* 286 */       this.tabNavigationBar.selectTab(focusedTabIndex, false); 
/* 289 */     this.tabNavigationBar.setTabActiveState(3, !this.serverData.expired);
/* 290 */     if (this.serverData.expired) {
/* 291 */       this.tabNavigationBar.setTabTooltip(3, Tooltip.create((Component)Component.translatable("mco.configure.world.settings.expired")));
/*     */     } else {
/* 293 */       this.tabNavigationBar.setTabTooltip(3, null);
/*     */     } 
/* 296 */     updateButtonStates();
/* 297 */     repositionElements();
/*     */   }
/*     */   
/*     */   public void saveSlotSettings(RealmsSlot slot) {
/* 301 */     RealmsSlot oldSlot = (RealmsSlot)this.serverData.slots.get(Integer.valueOf(this.serverData.activeSlot));
/* 302 */     slot.options.templateId = oldSlot.options.templateId;
/* 303 */     slot.options.templateImage = oldSlot.options.templateImage;
/* 305 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 307 */       if (this.serverData.activeSlot != slot.slotId)
/* 308 */         throw new RealmsServiceException(RealmsError.CustomError.configurationError()); 
/* 311 */       client.updateSlot(this.serverData.id, slot.slotId, slot.options, slot.settings);
/* 312 */       this.serverData.slots.put(Integer.valueOf(this.serverData.activeSlot), slot);
/* 313 */       if (slot.options.gameMode != oldSlot.options.gameMode || slot.isHardcore() != oldSlot.isHardcore())
/* 314 */         RealmsMainScreen.refreshServerList(); 
/* 316 */       stateChanged();
/* 317 */     } catch (RealmsServiceException e) {
/* 318 */       LOGGER.error("Couldn't save slot settings", (Throwable)e);
/* 319 */       this.minecraft.gui.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this));
/*     */       return;
/*     */     } 
/* 323 */     this.minecraft.gui.setScreen((Screen)this);
/*     */   }
/*     */   
/*     */   public void saveSettings(String name, String desc, RegionSelectionPreference preference, RealmsRegion region) {
/* 327 */     String description = StringUtil.isBlank(desc) ? "" : desc;
/* 328 */     String finalName = StringUtil.isBlank(name) ? "" : name;
/* 330 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 332 */       RealmsSlot realmsSlot = (RealmsSlot)this.serverData.slots.get(Integer.valueOf(this.serverData.activeSlot));
/* 333 */       RealmsRegion regionSelection = (preference == RegionSelectionPreference.MANUAL) ? region : null;
/* 334 */       RegionSelectionPreferenceDto regionSelectionPreference = new RegionSelectionPreferenceDto(preference, regionSelection);
/* 335 */       client.updateConfiguration(this.serverData.id, finalName, description, regionSelectionPreference, realmsSlot.slotId, realmsSlot.options, realmsSlot.settings);
/* 336 */       this.serverData.regionSelectionPreference = regionSelectionPreference;
/* 337 */       this.serverData.name = name;
/* 338 */       this.serverData.motd = description;
/* 339 */       stateChanged();
/* 340 */     } catch (RealmsServiceException e) {
/* 341 */       LOGGER.error("Couldn't save settings", (Throwable)e);
/* 342 */       this.minecraft.gui.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this));
/*     */       return;
/*     */     } 
/* 346 */     this.minecraft.gui.setScreen((Screen)this);
/*     */   }
/*     */   
/*     */   public void openTheWorld(boolean join) {
/* 350 */     RealmsConfigureWorldScreen screenWithKnownData = getNewScreenWithKnownData(this.serverData);
/* 351 */     this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)getNewScreen(), new LongRunningTask[] { (LongRunningTask)new OpenServerTask(this.serverData, (Screen)screenWithKnownData, join, this.minecraft) }));
/*     */   }
/*     */   
/*     */   public void closeTheWorld() {
/* 355 */     RealmsConfigureWorldScreen screenWithKnownData = getNewScreenWithKnownData(this.serverData);
/* 356 */     this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)getNewScreen(), new LongRunningTask[] { (LongRunningTask)new CloseServerTask(this.serverData, screenWithKnownData) }));
/*     */   }
/*     */   
/*     */   public void stateChanged() {
/* 360 */     this.stateChanged = true;
/* 361 */     if (this.tabNavigationBar != null)
/* 362 */       for (Tab child : this.tabNavigationBar.getTabs()) {
/* 363 */         if (child instanceof RealmsConfigurationTab) {
/* 363 */           RealmsConfigurationTab tab = (RealmsConfigurationTab)child;
/* 364 */           tab.updateData(this.serverData);
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public boolean invitePlayer(long serverId, String name) {
/* 371 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 373 */       List<PlayerInfo> players = client.invite(serverId, name);
/* 374 */       if (this.serverData != null) {
/* 375 */         this.serverData.players = players;
/*     */       } else {
/* 377 */         this.serverData = client.getOwnRealm(serverId);
/*     */       } 
/* 379 */       stateChanged();
/* 380 */     } catch (RealmsServiceException e) {
/* 381 */       LOGGER.error("Couldn't invite user", (Throwable)e);
/* 382 */       return false;
/*     */     } 
/* 384 */     return true;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen getNewScreen() {
/* 388 */     RealmsConfigureWorldScreen realmsConfigureWorldScreen = new RealmsConfigureWorldScreen(this.lastScreen, this.serverId);
/* 389 */     realmsConfigureWorldScreen.stateChanged = this.stateChanged;
/* 390 */     return realmsConfigureWorldScreen;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen getNewScreenWithKnownData(RealmsServer serverData) {
/* 394 */     RealmsConfigureWorldScreen realmsConfigureWorldScreen = new RealmsConfigureWorldScreen(this.lastScreen, this.serverId, serverData, this.regions);
/* 395 */     realmsConfigureWorldScreen.stateChanged = this.stateChanged;
/* 396 */     return realmsConfigureWorldScreen;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsConfigureWorldScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */