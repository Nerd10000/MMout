/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.screens.PrivacyConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.friends.FriendsListConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ public class OnlineOptionsScreen extends OptionsSubScreen {
/*  24 */   private static final Component TITLE = (Component)Component.translatable("options.online.title");
/*     */   
/*  25 */   private static final Component SERVERS_HEADER = (Component)Component.translatable("options.online.servers.header");
/*     */   
/*  26 */   private static final Component REALMS_HEADER = (Component)Component.translatable("options.online.realms.header");
/*     */   
/*  27 */   private static final Component FRIENDS_HEADER = (Component)Component.translatable("options.online.friends.header");
/*     */   
/*  28 */   private static final Component XBOX_SETTINGS = (Component)Component.translatable("options.online.xboxSettings");
/*     */   
/*  29 */   private static final Component FRIENDS_CONFIRM_TITLE = (Component)Component.translatable("options.friendsList.confirm.title").withStyle(ChatFormatting.UNDERLINE);
/*     */   
/*     */   private static final Component MICROSOFT_ACCOUNT_LINK;
/*     */   
/*     */   static {
/*  31 */     MICROSOFT_ACCOUNT_LINK = (Component)Component.translatable("options.friendsList.confirm.message.link").withStyle(style -> style.withUnderlined(Boolean.valueOf(true)).withColor(ChatFormatting.BLUE).withClickEvent((ClickEvent)new ClickEvent.OpenUrl(CommonLinks.PRIVACY_AND_ONLINE_SETTINGS)));
/*     */   }
/*     */   
/*  35 */   private static final Component FRIENDS_CONFIRM_MESSAGE = (Component)Component.translatable("options.friendsList.confirm.message", new Object[] { MICROSOFT_ACCOUNT_LINK });
/*     */   
/*  36 */   private static final Component FRIENDS_CONFIRM_TURN_ON = (Component)Component.translatable("options.friendsList.confirm.turnOn");
/*     */   
/*  37 */   private static final Component FRIENDS_CONFIRM_TURN_OFF = (Component)Component.translatable("options.friendsList.confirm.turnOff");
/*     */   
/*  38 */   private static final Component FRIENDS_LIST_LABEL = (Component)Component.translatable("options.friendsList");
/*     */   
/*  39 */   private static final Component ALLOW_FRIEND_REQUESTS_LABEL = (Component)Component.translatable("options.allowFriendRequests");
/*     */   
/*  40 */   private static final Tooltip ALLOW_FRIEND_REQUESTS_TOOLTIP = Tooltip.create((Component)Component.translatable("options.allowFriendRequests.tooltip"));
/*     */   
/*  41 */   private static final Component IN_GAME_NOTIFICATIONS_LABEL = (Component)Component.translatable("options.inGameNotification");
/*     */   
/*  42 */   private static final Tooltip IN_GAME_NOTIFICATIONS_TOOLTIP = Tooltip.create((Component)Component.translatable("options.inGameNotification.tooltip"));
/*     */   
/*     */   private CycleButton<Boolean> friendsListButton;
/*     */   
/*     */   private CycleButton<Boolean> allowFriendRequestsButton;
/*     */   
/*     */   private CycleButton<Boolean> inGameNotificationButton;
/*     */   
/*     */   private AbstractWidget presenceWidget;
/*     */   
/*     */   public OnlineOptionsScreen(Screen lastScreen, Options options) {
/*  50 */     super(lastScreen, options, TITLE);
/*     */   }
/*     */   
/*     */   public static void confirmFriendsListEnabled(Minecraft minecraft, Runnable onEnabled, Screen lastScreen) {
/*  54 */     PlayerSocialManager playerSocialManager = minecraft.getPlayerSocialManager();
/*  55 */     if (playerSocialManager.isFriendListEnabled()) {
/*  56 */       onEnabled.run();
/*     */       return;
/*     */     } 
/*  60 */     minecraft.gui.setScreen((Screen)new FriendsListConfirmScreen(accepted -> {
/*     */             if (accepted) {
/*     */               applyFriendSettings(minecraft, true, true, ());
/*     */             } else {
/*     */               minecraft.gui.setScreen(lastScreen);
/*     */             } 
/*  60 */           }FRIENDS_CONFIRM_TITLE, FRIENDS_CONFIRM_MESSAGE, FRIENDS_CONFIRM_TURN_ON, FRIENDS_CONFIRM_TURN_OFF));
/*     */   }
/*     */   
/*     */   private static void applyFriendSettings(Minecraft minecraft, boolean friendsListEnabled, boolean allowFriendRequests, Consumer<Boolean> onResult) {
/*  80 */     PlayerSocialManager playerSocialManager = minecraft.getPlayerSocialManager();
/*  81 */     playerSocialManager.updateFriendSettings(friendsListEnabled, allowFriendRequests)
/*  82 */       .whenCompleteAsync((result, paramThrowable) -> {
/*     */           boolean success = (result == FriendsService.ResultCode.SUCCESS);
/*     */           if (success) {
/*     */             playerSocialManager.setFriendListEnabled(friendsListEnabled);
/*     */             playerSocialManager.setAllowFriendRequests(allowFriendRequests);
/*     */           } 
/*     */           onResult.accept(Boolean.valueOf(success));
/*     */         }(Executor)minecraft);
/*     */   }
/*     */   
/*     */   protected void addOptions() {
/*  94 */     this.list.addHeader(FRIENDS_HEADER);
/*  95 */     PlayerSocialManager playerSocialManager = this.minecraft.getPlayerSocialManager();
/*  96 */     OptionInstance<Boolean> inGameNotificationOpt = this.options.inGameNotification();
/*  97 */     this
/*  98 */       .friendsListButton = CycleButton.onOffBuilder(playerSocialManager.isFriendListEnabled()).create(0, 0, 150, 20, FRIENDS_LIST_LABEL, (paramCycleButton, newValue) -> onFriendsListToggled(newValue, playerSocialManager, inGameNotificationOpt));
/* 100 */     this.friendsListButton.active = !this.minecraft.isDemo();
/* 102 */     this
/*     */       
/* 104 */       .allowFriendRequestsButton = CycleButton.onOffBuilder(playerSocialManager.isAllowFriendRequests()).withTooltip(paramBoolean -> ALLOW_FRIEND_REQUESTS_TOOLTIP).create(0, 0, 150, 20, ALLOW_FRIEND_REQUESTS_LABEL, (paramCycleButton, enabled) -> applyFriendSettings(this.minecraft, playerSocialManager.isFriendListEnabled(), enabled.booleanValue(), ()));
/* 106 */     this.list.addSmall((AbstractWidget)this.friendsListButton, (AbstractWidget)this.allowFriendRequestsButton);
/* 108 */     this
/*     */       
/* 110 */       .inGameNotificationButton = CycleButton.onOffBuilder(((Boolean)inGameNotificationOpt.get()).booleanValue()).withTooltip(paramBoolean -> IN_GAME_NOTIFICATIONS_TOOLTIP).create(0, 0, 150, 20, IN_GAME_NOTIFICATIONS_LABEL, (paramCycleButton, enabled) -> inGameNotificationOpt.set(enabled));
/* 113 */     this.presenceWidget = this.options.sharePresence().createButton(this.options);
/* 115 */     this.list.addSmall((AbstractWidget)this.inGameNotificationButton, this.presenceWidget);
/* 116 */     updateFriendListDependentButtons();
/* 118 */     this.list.addBig((AbstractWidget)Button.builder(XBOX_SETTINGS, paramButton -> PrivacyConfirmLinkScreen.confirmLinkNow(this, CommonLinks.PRIVACY_AND_ONLINE_SETTINGS)).build());
/* 120 */     this.list.addHeader(SERVERS_HEADER);
/* 121 */     this.list.addBig(this.options.allowServerListing());
/* 123 */     this.list.addHeader(REALMS_HEADER);
/* 124 */     this.list.addBig(this.options.realmsNotifications());
/*     */   }
/*     */   
/*     */   private void onFriendsListToggled(Boolean newValue, PlayerSocialManager playerSocialManager, OptionInstance<Boolean> inGameNotificationOpt) {
/* 128 */     if (newValue.booleanValue()) {
/* 129 */       this.minecraft.gui.setScreen((Screen)new FriendsListConfirmScreen(accepted -> {
/*     */               this.minecraft.gui.setScreen(this);
/*     */               if (accepted) {
/*     */                 playerSocialManager.setFriendListEnabled(true);
/*     */                 playerSocialManager.setAllowFriendRequests(true);
/*     */                 applyFriendSettings(this.minecraft, true, true, ());
/*     */               } 
/*     */               updateFriendListDependentButtons();
/* 129 */             }FRIENDS_CONFIRM_TITLE, FRIENDS_CONFIRM_MESSAGE, FRIENDS_CONFIRM_TURN_ON, FRIENDS_CONFIRM_TURN_OFF));
/*     */     } else {
/* 144 */       boolean friendListEnabled = playerSocialManager.isFriendListEnabled();
/* 145 */       boolean allowFriendRequests = playerSocialManager.isAllowFriendRequests();
/* 146 */       playerSocialManager.setFriendListEnabled(false);
/* 147 */       playerSocialManager.setAllowFriendRequests(false);
/* 148 */       inGameNotificationOpt.set(Boolean.valueOf(false));
/* 149 */       updateFriendListDependentButtons();
/* 150 */       applyFriendSettings(this.minecraft, false, false, result -> {
/*     */             if (!result.booleanValue()) {
/*     */               playerSocialManager.setFriendListEnabled(friendListEnabled);
/*     */               playerSocialManager.setAllowFriendRequests(allowFriendRequests);
/*     */             } 
/*     */             updateFriendListDependentButtons();
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateFriendListDependentButtons() {
/* 161 */     PlayerSocialManager playerSocialManager = this.minecraft.getPlayerSocialManager();
/* 162 */     boolean enabled = playerSocialManager.isFriendListEnabled();
/* 163 */     if (this.friendsListButton != null)
/* 164 */       this.friendsListButton.setValue(Boolean.valueOf(enabled)); 
/* 166 */     if (this.allowFriendRequestsButton != null) {
/* 167 */       this.allowFriendRequestsButton.setValue(Boolean.valueOf(playerSocialManager.isAllowFriendRequests()));
/* 168 */       this.allowFriendRequestsButton.active = enabled;
/*     */     } 
/* 170 */     if (this.inGameNotificationButton != null) {
/* 171 */       this.inGameNotificationButton.setValue(this.options.inGameNotification().get());
/* 172 */       this.inGameNotificationButton.active = enabled;
/*     */     } 
/* 174 */     if (this.presenceWidget != null)
/* 175 */       this.presenceWidget.active = enabled; 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\OnlineOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */