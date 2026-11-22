/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.RealmsAvailability;
/*     */ import com.mojang.realmsclient.dto.RealmsNews;
/*     */ import com.mojang.realmsclient.dto.RealmsNotification;
/*     */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*     */ import com.mojang.realmsclient.gui.task.DataFetcher;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RealmsNotificationsScreen extends RealmsScreen {
/*  19 */   private static final Identifier UNSEEN_NOTIFICATION_SPRITE = Identifier.withDefaultNamespace("icon/unseen_notification");
/*     */   
/*  20 */   private static final Identifier NEWS_SPRITE = Identifier.withDefaultNamespace("icon/news");
/*     */   
/*  21 */   private static final Identifier INVITE_SPRITE = Identifier.withDefaultNamespace("icon/invite");
/*     */   
/*  22 */   private static final Identifier TRIAL_AVAILABLE_SPRITE = Identifier.withDefaultNamespace("icon/trial_available");
/*     */   
/*     */   private final CompletableFuture<Boolean> validClient;
/*     */   
/*     */   private DataFetcher.Subscription realmsDataSubscription;
/*     */   
/*     */   private DataFetcherConfiguration currentConfiguration;
/*     */   
/*     */   private volatile int numberOfPendingInvites;
/*     */   
/*     */   private static boolean trialAvailable;
/*     */   
/*     */   private static boolean hasUnreadNews;
/*     */   
/*     */   private static boolean hasUnseenNotifications;
/*     */   
/*     */   private final DataFetcherConfiguration showAll;
/*     */   
/*     */   private final DataFetcherConfiguration onlyNotifications;
/*     */   
/*     */   public RealmsNotificationsScreen() {
/*  35 */     super(GameNarrator.NO_TITLE);
/*     */     this.validClient = RealmsAvailability.get().thenApply(result -> Boolean.valueOf((result.type() == RealmsAvailability.Type.SUCCESS)));
/* 161 */     this.showAll = new DataFetcherConfiguration() {
/*     */         public DataFetcher.Subscription initDataFetcher(RealmsDataFetcher dataSource) {
/* 164 */           DataFetcher.Subscription result = dataSource.dataFetcher.createSubscription();
/* 166 */           RealmsNotificationsScreen.this.addNewsAndInvitesSubscriptions(dataSource, result);
/* 167 */           RealmsNotificationsScreen.this.addNotificationsSubscriptions(dataSource, result);
/* 169 */           return result;
/*     */         }
/*     */         
/*     */         public boolean showOldNotifications() {
/* 174 */           return true;
/*     */         }
/*     */       };
/* 178 */     this.onlyNotifications = new DataFetcherConfiguration() {
/*     */         public DataFetcher.Subscription initDataFetcher(RealmsDataFetcher dataSource) {
/* 181 */           DataFetcher.Subscription result = dataSource.dataFetcher.createSubscription();
/* 183 */           RealmsNotificationsScreen.this.addNotificationsSubscriptions(dataSource, result);
/* 185 */           return result;
/*     */         }
/*     */         
/*     */         public boolean showOldNotifications() {
/* 190 */           return false;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void init() {
/*     */     if (this.realmsDataSubscription != null)
/*     */       this.realmsDataSubscription.forceUpdate(); 
/*     */   }
/*     */   
/*     */   public void added() {
/*     */     super.added();
/*     */     (this.minecraft.realmsDataFetcher()).notificationsTask.reset();
/*     */   }
/*     */   
/*     */   private DataFetcherConfiguration getConfiguration() {
/*     */     boolean realmsEnabled = (inTitleScreen() && ((Boolean)this.validClient.getNow(Boolean.valueOf(false))).booleanValue());
/*     */     if (!realmsEnabled)
/*     */       return null; 
/*     */     return getRealmsNotificationsEnabled() ? this.showAll : this.onlyNotifications;
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     DataFetcherConfiguration dataFetcherConfiguration = getConfiguration();
/*     */     if (!Objects.equals(this.currentConfiguration, dataFetcherConfiguration)) {
/*     */       this.currentConfiguration = dataFetcherConfiguration;
/*     */       if (this.currentConfiguration != null) {
/*     */         this.realmsDataSubscription = this.currentConfiguration.initDataFetcher(this.minecraft.realmsDataFetcher());
/*     */       } else {
/*     */         this.realmsDataSubscription = null;
/*     */       } 
/*     */     } 
/*     */     if (this.realmsDataSubscription != null)
/*     */       this.realmsDataSubscription.tick(); 
/*     */   }
/*     */   
/*     */   private boolean getRealmsNotificationsEnabled() {
/*     */     return ((Boolean)this.minecraft.options.realmsNotifications().get()).booleanValue();
/*     */   }
/*     */   
/*     */   private boolean inTitleScreen() {
/*     */     return this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.TitleScreen;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/*     */     super.extractRenderState(graphics, xm, ym, a);
/*     */     if (((Boolean)this.validClient.getNow(Boolean.valueOf(false))).booleanValue())
/*     */       extractIcons(graphics); 
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {}
/*     */   
/*     */   private void extractIcons(GuiGraphicsExtractor graphics) {
/*     */     int pendingInvitesCount = this.numberOfPendingInvites;
/*     */     int spacing = 24;
/*     */     int topPos = this.height / 4 + 48;
/*     */     int buttonRight = this.width / 2 + 100;
/*     */     int baseY = topPos + 48 + 2;
/*     */     int iconRight = buttonRight - 3;
/*     */     if (hasUnseenNotifications) {
/*     */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UNSEEN_NOTIFICATION_SPRITE, iconRight - 12, baseY + 3, 10, 10);
/*     */       iconRight -= 16;
/*     */     } 
/*     */     if (this.currentConfiguration != null && this.currentConfiguration.showOldNotifications()) {
/*     */       if (hasUnreadNews) {
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NEWS_SPRITE, iconRight - 14, baseY + 1, 14, 14);
/*     */         iconRight -= 16;
/*     */       } 
/*     */       if (pendingInvitesCount != 0) {
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, INVITE_SPRITE, iconRight - 14, baseY + 1, 14, 14);
/*     */         iconRight -= 16;
/*     */       } 
/*     */       if (trialAvailable)
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRIAL_AVAILABLE_SPRITE, iconRight - 10, baseY + 4, 8, 8); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNewsAndInvitesSubscriptions(RealmsDataFetcher dataSource, DataFetcher.Subscription result) {
/*     */     result.subscribe(dataSource.pendingInvitesTask, value -> this.numberOfPendingInvites = value.intValue());
/*     */     result.subscribe(dataSource.trialAvailabilityTask, value -> trialAvailable = value.booleanValue());
/*     */     result.subscribe(dataSource.newsTask, value -> {
/*     */           dataSource.newsManager.updateUnreadNews(value);
/*     */           hasUnreadNews = dataSource.newsManager.hasUnreadNews();
/*     */         });
/*     */   }
/*     */   
/*     */   private void addNotificationsSubscriptions(RealmsDataFetcher dataSource, DataFetcher.Subscription result) {
/*     */     result.subscribe(dataSource.notificationsTask, notifications -> {
/*     */           hasUnseenNotifications = false;
/*     */           for (RealmsNotification notification : notifications) {
/*     */             if (!notification.seen()) {
/*     */               hasUnseenNotifications = true;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static interface DataFetcherConfiguration {
/*     */     DataFetcher.Subscription initDataFetcher(RealmsDataFetcher param1RealmsDataFetcher);
/*     */     
/*     */     boolean showOldNotifications();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsNotificationsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */