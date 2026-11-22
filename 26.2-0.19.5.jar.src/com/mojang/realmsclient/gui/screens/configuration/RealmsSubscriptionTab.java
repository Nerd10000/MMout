/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.Subscription;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class RealmsSubscriptionTab extends GridLayoutTab implements RealmsConfigurationTab {
/*     */   private static final int DEFAULT_COMPONENT_WIDTH = 200;
/*     */   
/*     */   private static final int EXTRA_SPACING = 2;
/*     */   
/*     */   private static final int DEFAULT_SPACING = 6;
/*     */   
/*  33 */   public static final Component TITLE = (Component)Component.translatable("mco.configure.world.subscription.tab");
/*     */   
/*  34 */   private static final Component SUBSCRIPTION_START_LABEL = (Component)Component.translatable("mco.configure.world.subscription.start");
/*     */   
/*  35 */   private static final Component TIME_LEFT_LABEL = (Component)Component.translatable("mco.configure.world.subscription.timeleft");
/*     */   
/*  36 */   private static final Component DAYS_LEFT_LABEL = (Component)Component.translatable("mco.configure.world.subscription.recurring.daysleft");
/*     */   
/*  37 */   private static final Component SUBSCRIPTION_EXPIRED_TEXT = (Component)Component.translatable("mco.configure.world.subscription.expired").withStyle(ChatFormatting.GRAY);
/*     */   
/*  38 */   private static final Component SUBSCRIPTION_LESS_THAN_A_DAY_TEXT = (Component)Component.translatable("mco.configure.world.subscription.less_than_a_day").withStyle(ChatFormatting.GRAY);
/*     */   
/*  39 */   private static final Component RECURRING_INFO = (Component)Component.translatable("mco.configure.world.subscription.recurring.info");
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Button deleteButton;
/*     */   
/*     */   private final FocusableTextWidget subscriptionInfo;
/*     */   
/*     */   private final Component startDate;
/*     */   
/*     */   private final Component daysLeft;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   public RealmsSubscriptionTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData, Subscription subscription) {
/*  49 */     super(TITLE);
/*  50 */     this.configurationScreen = configurationScreen;
/*  51 */     this.minecraft = minecraft;
/*  52 */     this.serverData = serverData;
/*  53 */     this.startDate = localPresentation(subscription.startDate());
/*  54 */     this.daysLeft = daysLeftPresentation(subscription.daysLeft());
/*  56 */     GridLayout.RowHelper helper = this.layout.rowSpacing(6).createRowHelper(1);
/*  58 */     Font font = configurationScreen.getFont();
/*  59 */     Objects.requireNonNull(font);
/*  59 */     helper.addChild((LayoutElement)new StringWidget(200, 9, SUBSCRIPTION_START_LABEL, font));
/*  60 */     Objects.requireNonNull(font);
/*  60 */     helper.addChild((LayoutElement)new StringWidget(200, 9, this.startDate, font));
/*  62 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  63 */     Component daysLeftLabel = (subscription.type() == Subscription.SubscriptionType.RECURRING) ? DAYS_LEFT_LABEL : TIME_LEFT_LABEL;
/*  64 */     Objects.requireNonNull(font);
/*  64 */     helper.addChild((LayoutElement)new StringWidget(200, 9, daysLeftLabel, font));
/*  65 */     Objects.requireNonNull(font);
/*  65 */     helper.addChild((LayoutElement)new StringWidget(200, 9, this.daysLeft, font));
/*  67 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  68 */     helper.addChild((LayoutElement)Button.builder(
/*  69 */           (Component)Component.translatable("mco.configure.world.subscription.extend"), button -> ConfirmLinkScreen.confirmLinkNow((Screen)configurationScreen, CommonLinks.extendRealms(serverData.remoteSubscriptionId, minecraft.getUser().getProfileId())))
/*     */         
/*  71 */         .bounds(0, 0, 200, 20).build());
/*  73 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  74 */     this.deleteButton = (Button)helper.addChild((LayoutElement)Button.builder(
/*  75 */           (Component)Component.translatable("mco.configure.world.delete.button"), paramButton -> minecraft.gui.setScreen((Screen)RealmsPopups.warningPopupScreen((Screen)configurationScreen, (Component)Component.translatable("mco.configure.world.delete.question.line1"), ())))
/*     */         
/*  78 */         .bounds(0, 0, 200, 20).build());
/*  81 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  82 */     this.subscriptionInfo = (FocusableTextWidget)helper.addChild((LayoutElement)FocusableTextWidget.builder((Component)Component.empty(), font).maxWidth(200).build(), LayoutSettings.defaults().alignHorizontallyCenter());
/*  83 */     this.subscriptionInfo.setCentered(false);
/*  85 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   private void deleteRealm() {
/*  91 */     Objects.requireNonNull(this.configurationScreen);
/*  91 */     RealmsUtil.runAsync(client -> client.deleteRealm(this.serverData.id), RealmsUtil.openScreenAndLogOnFailure(this.configurationScreen::createErrorScreen, "Couldn't delete world"))
/*  92 */       .thenRunAsync(() -> this.minecraft.gui.setScreen(this.configurationScreen.getLastScreen()), (Executor)this.minecraft);
/*  94 */     this.minecraft.gui.setScreen((Screen)this.configurationScreen);
/*     */   }
/*     */   
/*     */   private static Component localPresentation(Instant time) {
/*  98 */     String formattedDate = ZonedDateTime.ofInstant(time, ZoneId.systemDefault()).format(Util.localizedDateFormatter(FormatStyle.MEDIUM));
/*  99 */     return (Component)Component.literal(formattedDate).withStyle(ChatFormatting.GRAY);
/*     */   }
/*     */   
/*     */   private Component daysLeftPresentation(int daysLeft) {
/* 103 */     if (daysLeft < 0 && this.serverData.expired)
/* 104 */       return SUBSCRIPTION_EXPIRED_TEXT; 
/* 105 */     if (daysLeft <= 1)
/* 106 */       return SUBSCRIPTION_LESS_THAN_A_DAY_TEXT; 
/* 109 */     int months = daysLeft / 30;
/* 110 */     int days = daysLeft % 30;
/* 111 */     boolean showMonths = (months > 0);
/* 112 */     boolean showDays = (days > 0);
/* 114 */     if (showMonths && showDays)
/* 115 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.months.days", new Object[] { Integer.valueOf(months), Integer.valueOf(days) }).withStyle(ChatFormatting.GRAY); 
/* 116 */     if (showMonths)
/* 117 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.months", new Object[] { Integer.valueOf(months) }).withStyle(ChatFormatting.GRAY); 
/* 118 */     if (showDays)
/* 119 */       return (Component)Component.translatable("mco.configure.world.subscription.remaining.days", new Object[] { Integer.valueOf(days) }).withStyle(ChatFormatting.GRAY); 
/* 121 */     return (Component)Component.empty();
/*     */   }
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/* 127 */     this.serverData = serverData;
/* 129 */     boolean snapshotWorld = (RealmsMainScreen.isSnapshot() && serverData.parentWorldName != null);
/* 130 */     this.deleteButton.active = serverData.expired;
/* 132 */     if (snapshotWorld) {
/* 133 */       this.subscriptionInfo.setMessage((Component)Component.translatable("mco.snapshot.subscription.info", new Object[] { serverData.parentWorldName }));
/*     */     } else {
/* 135 */       this.subscriptionInfo.setMessage(RECURRING_INFO);
/*     */     } 
/* 137 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public Component getTabExtraNarration() {
/* 142 */     return CommonComponents.joinLines(new Component[] { TITLE, SUBSCRIPTION_START_LABEL, this.startDate, TIME_LEFT_LABEL, this.daysLeft });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsSubscriptionTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */