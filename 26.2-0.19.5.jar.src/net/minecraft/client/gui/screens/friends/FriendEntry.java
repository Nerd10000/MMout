/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceStatus;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceStatusDto;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class FriendEntry extends AbstractFriendsEntryContainerWidget {
/*  24 */   private static final WidgetSprites REMOVE_SPRITE = new WidgetSprites(Identifier.withDefaultNamespace("friends/remove"));
/*     */   
/*  26 */   private static final Component UNFRIEND = (Component)Component.translatable("gui.friends.unfriend");
/*     */   
/*  27 */   private static final Component CONFIRM_TITLE = (Component)Component.translatable("gui.friends.confirm_title");
/*     */   
/*  28 */   private static final Component CONFIRM_UNFRIEND = (Component)Component.translatable("gui.friends.confirm_unfriend");
/*     */   
/*  29 */   private static final Component PRESENCE_OFFLINE = (Component)Component.translatable("gui.friends.presence.status.offline").withColor(-6250336);
/*     */   
/*     */   private final SpriteIconButton removeButton;
/*     */   
/*     */   private final StringWidget statusWidget;
/*     */   
/*     */   private PresenceStatusDto presence;
/*     */   
/*     */   public FriendEntry(Minecraft minecraft, FriendsOverlayScreen screen, PlayerSocialManager.PlayerData playerData, PresenceStatusDto presence, boolean initiallyLoading, Runnable onAction) {
/*  37 */     super(minecraft, screen, 0, 0, screen.getOverlayWidth() - 16, 28, playerData, true);
/*  38 */     this.presence = presence;
/*  39 */     this.statusWidget = new StringWidget(presenceStatusComponent(presence), minecraft.font);
/*  40 */     addChild((AbstractWidget)this.statusWidget);
/*  41 */     Button.CreateNarration narration = getSpriteIconNarration(
/*  42 */         (Component)Component.translatable("gui.friends.narration.button.unfriend", new Object[] { playerData.name() }));
/*  44 */     this
/*     */       
/*  49 */       .removeButton = SpriteIconButton.builder(UNFRIEND, paramButton -> confirmRemoveFriend(onAction), true).size(20, 20).sprite(REMOVE_SPRITE, 13, 11).tooltip(UNFRIEND).narration(narration).build();
/*  50 */     if (initiallyLoading)
/*  51 */       this.removeButton.setLoading(true); 
/*  53 */     addChild((AbstractWidget)this.removeButton);
/*     */   }
/*     */   
/*     */   private static Component presenceStatusComponent(PresenceStatusDto presence) {
/*  57 */     if (presence == null || presence.status() == PresenceStatus.OFFLINE)
/*  58 */       return PRESENCE_OFFLINE; 
/*  61 */     String key = "gui.friends.presence.status." + presence.status().toString().toLowerCase(Locale.ROOT);
/*  62 */     return (Component)Component.translatable(key).withColor(-16711936);
/*     */   }
/*     */   
/*     */   void applyPresence(PresenceStatusDto newPresence) {
/*  67 */     this.presence = newPresence;
/*  68 */     this.statusWidget.setMessage(presenceStatusComponent(newPresence));
/*     */   }
/*     */   
/*     */   public int presenceStatusSortOrder() {
/*  72 */     PresenceStatus status = (this.presence == null) ? PresenceStatus.OFFLINE : this.presence.status();
/*  73 */     switch (status) {
/*     */       default:
/*  73 */         throw new MatchException(null, null);
/*     */       case PLAYING_HOSTED_SERVER:
/*     */       
/*     */       case PLAYING_SERVER:
/*     */       
/*     */       case PLAYING_REALMS:
/*     */       
/*     */       case PLAYING_OFFLINE:
/*     */       
/*     */       case ONLINE:
/*     */       
/*     */       case OFFLINE:
/*     */         break;
/*     */     } 
/*  73 */     return 
/*     */       
/*  79 */       5;
/*     */   }
/*     */   
/*     */   void disable() {
/*  86 */     this.removeButton.active = false;
/*     */   }
/*     */   
/*     */   protected Component getEntryNarration() {
/*  91 */     return (Component)Component.translatable("gui.friends.narration.entry.friend", new Object[] { this.playerName });
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  96 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*  97 */     int verticalCenter = getY() + (getHeight() - 20) / 2;
/*  98 */     int removeX = getX() + getWidth() - 20;
/*  99 */     this.removeButton.setPosition(removeX, verticalCenter);
/* 100 */     this.removeButton.extractRenderState(graphics, mouseX, mouseY, a);
/* 101 */     int statusWidgetX = this.playerFaceWidget.getRight() + 4;
/* 102 */     int statusWidth = removeX - statusWidgetX - 2;
/* 103 */     this.statusWidget.setMaxWidth(statusWidth, StringWidget.TextOverflow.SCROLLING);
/* 104 */     this.statusWidget.setPosition(statusWidgetX, this.nameWidget.getBottom() + 2);
/* 105 */     this.statusWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   private void confirmRemoveFriend(Runnable action) {
/* 109 */     this.minecraft.gui.setScreen((Screen)(new PopupScreen.Builder(this.screen, CONFIRM_TITLE))
/* 110 */         .addMessage(CONFIRM_UNFRIEND)
/* 111 */         .addButton(CommonComponents.GUI_REMOVE, paramPopupScreen -> {
/*     */             this.removeButton.setLoading(true);
/*     */             this.screen.startFriendAction();
/*     */             action.run();
/*     */             this.minecraft.gui.setScreen(this.screen);
/* 117 */           }).addButton(CommonComponents.GUI_CANCEL, paramPopupScreen -> this.minecraft.gui.setScreen(this.screen))
/* 118 */         .build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\FriendEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */