/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import com.mojang.realmsclient.dto.RealmsServer;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
/*      */ import com.mojang.realmsclient.util.RealmsUtil;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetTooltipHolder;
/*      */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientActivePlayersTooltip;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.world.item.component.ResolvableProfile;
/*      */ 
/*      */ class ServerEntry extends RealmsMainScreen.Entry {
/* 1079 */   private static final Component ONLINE_PLAYERS_TOOLTIP_HEADER = (Component)Component.translatable("mco.onlinePlayers");
/*      */   
/*      */   private static final int PLAYERS_ONLINE_SPRITE_SIZE = 9;
/*      */   
/*      */   private static final int PLAYERS_ONLINE_SPRITE_SEPARATION = 3;
/*      */   
/*      */   private static final int SKIN_HEAD_LARGE_WIDTH = 36;
/*      */   
/*      */   private final RealmsServer serverData;
/*      */   
/* 1086 */   private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */   
/*      */   public ServerEntry(RealmsServer serverData) {
/* 1089 */     this.serverData = serverData;
/* 1090 */     boolean selfOwnedServer = RealmsMainScreen.isSelfOwnedServer(serverData);
/* 1091 */     if (RealmsMainScreen.isSnapshot() && selfOwnedServer && serverData.isSnapshotRealm()) {
/* 1092 */       this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.paired", new Object[] { serverData.parentWorldName })));
/* 1093 */     } else if (!selfOwnedServer && serverData.needsDowngrade()) {
/* 1094 */       this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.friendsRealm.downgrade", new Object[] { serverData.activeVersion })));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1100 */     if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1101 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/* 1103 */       Objects.requireNonNull(RealmsMainScreen.access$2500(RealmsMainScreen.this));
/* 1103 */       int textYPos = getContentYMiddle() - 9 / 2;
/* 1104 */       graphics.text(RealmsMainScreen.access$2600(RealmsMainScreen.this), RealmsMainScreen.SERVER_UNITIALIZED_TEXT, getContentX() + 40 - 2, textYPos, -8388737);
/*      */       return;
/*      */     } 
/* 1109 */     RealmsUtil.extractPlayerFace(graphics, getContentX(), getContentY(), 32, this.serverData.ownerUUID);
/* 1111 */     extractFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -1, this.serverData);
/* 1112 */     extractSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.serverData);
/* 1113 */     extractThirdLine(graphics, getContentY(), getContentX(), this.serverData);
/* 1115 */     extractStatusLights(this.serverData, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1116 */     boolean hasTooltip = extractOnlinePlayers(graphics, getContentY(), getContentX(), getContentWidth(), getContentHeight(), mouseX, mouseY, a);
/* 1117 */     if (!hasTooltip)
/* 1118 */       this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight())); 
/*      */   }
/*      */   
/*      */   private boolean extractOnlinePlayers(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, float a) {
/* 1123 */     List<ResolvableProfile> profileResults = RealmsMainScreen.this.onlinePlayersPerRealm.getProfileResultsFor(this.serverData.id);
/* 1124 */     int playerCount = profileResults.size();
/* 1125 */     if (playerCount > 0) {
/*      */       List<PlayerSkinRenderCache.RenderInfo> tooltipEntries;
/* 1126 */       int playersOnlineXEnd = rowLeft + rowWidth - 21;
/* 1127 */       int playersOnlineY = rowTop + rowHeight - 9 - 2;
/* 1128 */       int playerOnlineWidth = 9 * playerCount + 3 * (playerCount - 1);
/* 1129 */       int playersOnlineXStart = playersOnlineXEnd - playerOnlineWidth;
/* 1131 */       if (mouseX >= playersOnlineXStart && mouseX <= playersOnlineXEnd && mouseY >= playersOnlineY && mouseY <= playersOnlineY + 9) {
/* 1132 */         tooltipEntries = new ArrayList<>(playerCount);
/*      */       } else {
/* 1134 */         tooltipEntries = null;
/*      */       } 
/* 1137 */       PlayerSkinRenderCache skinCache = RealmsMainScreen.access$2700(RealmsMainScreen.this).playerSkinRenderCache();
/* 1138 */       for (int i = 0; i < profileResults.size(); i++) {
/* 1139 */         ResolvableProfile profile = profileResults.get(i);
/* 1140 */         PlayerSkinRenderCache.RenderInfo profileRenderInfo = skinCache.getOrDefault(profile);
/* 1141 */         int xPos = playersOnlineXStart + 12 * i;
/* 1142 */         PlayerFaceExtractor.extractRenderState(graphics, profileRenderInfo.playerSkin(), xPos, playersOnlineY, 9);
/* 1144 */         if (tooltipEntries != null)
/* 1145 */           tooltipEntries.add(profileRenderInfo); 
/*      */       } 
/* 1149 */       if (tooltipEntries != null) {
/* 1150 */         graphics.setTooltipForNextFrame(RealmsMainScreen.access$2800(RealmsMainScreen.this), List.of(ONLINE_PLAYERS_TOOLTIP_HEADER), Optional.of(new ClientActivePlayersTooltip.ActivePlayersTooltip(tooltipEntries)), mouseX, mouseY);
/* 1151 */         return true;
/*      */       } 
/*      */     } 
/* 1154 */     return false;
/*      */   }
/*      */   
/*      */   private void playRealm() {
/* 1158 */     RealmsMainScreen.access$2900(RealmsMainScreen.this).getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1159 */     RealmsMainScreen.play(this.serverData, (Screen)RealmsMainScreen.this);
/*      */   }
/*      */   
/*      */   private void createUnitializedRealm() {
/* 1163 */     RealmsMainScreen.access$3000(RealmsMainScreen.this).getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1164 */     RealmsCreateRealmScreen createScreen = new RealmsCreateRealmScreen(RealmsMainScreen.this, this.serverData, this.serverData.isSnapshotRealm());
/* 1165 */     (RealmsMainScreen.access$3100(RealmsMainScreen.this)).gui.setScreen((Screen)createScreen);
/*      */   }
/*      */   
/*      */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1170 */     if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1171 */       createUnitializedRealm();
/* 1172 */     } else if (this.serverData.shouldPlayButtonBeActive() && 
/* 1173 */       doubleClick && isFocused()) {
/* 1174 */       playRealm();
/*      */     } 
/* 1177 */     return true;
/*      */   }
/*      */   
/*      */   public boolean keyPressed(KeyEvent event) {
/* 1182 */     if (event.isSelection()) {
/* 1183 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1184 */         createUnitializedRealm();
/* 1185 */         return true;
/*      */       } 
/* 1186 */       if (this.serverData.shouldPlayButtonBeActive()) {
/* 1187 */         playRealm();
/* 1188 */         return true;
/*      */       } 
/*      */     } 
/* 1191 */     return super.keyPressed(event);
/*      */   }
/*      */   
/*      */   public Component getNarration() {
/* 1196 */     if (this.serverData.state == RealmsServer.State.UNINITIALIZED)
/* 1197 */       return RealmsMainScreen.UNITIALIZED_WORLD_NARRATION; 
/* 1199 */     return (Component)Component.translatable("narrator.select", new Object[] { Objects.requireNonNullElse(this.serverData.name, "unknown server") });
/*      */   }
/*      */   
/*      */   public RealmsServer getServer() {
/* 1204 */     return this.serverData;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$ServerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */