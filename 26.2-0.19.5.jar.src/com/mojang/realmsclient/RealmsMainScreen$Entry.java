/*     */ package com.mojang.realmsclient;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ abstract class Entry extends ObjectSelectionList.Entry<RealmsMainScreen.Entry> {
/*     */   protected static final int STATUS_LIGHT_WIDTH = 10;
/*     */   
/*     */   private static final int STATUS_LIGHT_HEIGHT = 28;
/*     */   
/*     */   protected static final int PADDING_X = 7;
/*     */   
/*     */   protected static final int PADDING_Y = 2;
/*     */   
/*     */   protected void extractStatusLights(RealmsServer serverData, GuiGraphicsExtractor graphics, int rowRight, int rowTop, int mouseX, int mouseY) {
/* 778 */     int x = rowRight - 10 - 7;
/* 779 */     int y = rowTop + 2;
/* 780 */     if (serverData.expired) {
/* 781 */       extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRED_SPRITE, () -> RealmsMainScreen.SERVER_EXPIRED_TOOLTIP);
/* 782 */     } else if (serverData.state == RealmsServer.State.CLOSED) {
/* 783 */       extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.CLOSED_SPRITE, () -> RealmsMainScreen.SERVER_CLOSED_TOOLTIP);
/* 784 */     } else if (RealmsMainScreen.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
/* 785 */       extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRES_SOON_SPRITE, () -> (serverData.daysLeft <= 0) ? RealmsMainScreen.SERVER_EXPIRES_SOON_TOOLTIP : ((serverData.daysLeft == 1) ? RealmsMainScreen.SERVER_EXPIRES_IN_DAY_TOOLTIP : Component.translatable("mco.selectServer.expires.days", new Object[] { Integer.valueOf(serverData.daysLeft) })));
/* 794 */     } else if (serverData.state == RealmsServer.State.OPEN) {
/* 795 */       extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.OPEN_SPRITE, () -> RealmsMainScreen.SERVER_OPEN_TOOLTIP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractRealmStatus(GuiGraphicsExtractor graphics, int x, int y, int xm, int ym, Identifier sprite, Supplier<Component> tooltip) {
/* 800 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 10, 28);
/* 801 */     if (RealmsMainScreen.this.realmSelectionList.isMouseOver(xm, ym) && xm >= x && xm <= x + 10 && ym >= y && ym <= y + 28)
/* 802 */       graphics.setTooltipForNextFrame(tooltip.get(), xm, ym); 
/*     */   }
/*     */   
/*     */   protected void extractFirstLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, int serverNameColor, RealmsServer serverData) {
/* 807 */     int textX = textX(rowLeft);
/* 808 */     int firstLineY = firstLineY(rowTop);
/* 810 */     Component versionComponent = RealmsMainScreen.getVersionComponent(serverData.activeVersion, serverData.isCompatible());
/* 811 */     int versionTextX = versionTextX(rowLeft, rowWidth, versionComponent);
/* 812 */     extractClampedString(graphics, serverData.getName(), textX, firstLineY, versionTextX, serverNameColor);
/* 813 */     if (versionComponent != CommonComponents.EMPTY && !serverData.isMinigameActive())
/* 814 */       graphics.text(RealmsMainScreen.access$400(RealmsMainScreen.this), versionComponent, versionTextX, firstLineY, -8355712); 
/*     */   }
/*     */   
/*     */   protected void extractSecondLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, RealmsServer serverData) {
/* 819 */     int textX = textX(rowLeft);
/* 820 */     int firstLineY = firstLineY(rowTop);
/* 821 */     int secondLineY = secondLineY(firstLineY);
/* 823 */     String minigameName = serverData.getMinigameName();
/* 824 */     boolean minigameActive = serverData.isMinigameActive();
/* 825 */     if (minigameActive && minigameName != null) {
/* 826 */       MutableComponent mutableComponent = Component.literal(minigameName).withStyle(ChatFormatting.GRAY);
/* 827 */       graphics.text(RealmsMainScreen.access$500(RealmsMainScreen.this), (Component)Component.translatable("mco.selectServer.minigameName", new Object[] { mutableComponent }).withColor(-171), textX, secondLineY, -1);
/*     */     } else {
/* 829 */       int maxX = extractGameMode(serverData, graphics, rowLeft, rowWidth, firstLineY);
/* 830 */       extractClampedString(graphics, serverData.getDescription(), textX, secondLineY(firstLineY), maxX, -8355712);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractThirdLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, RealmsServer server) {
/* 835 */     int textX = textX(rowLeft);
/* 836 */     int firstLineY = firstLineY(rowTop);
/* 837 */     int thirdLineY = thirdLineY(firstLineY);
/* 839 */     if (!RealmsMainScreen.isSelfOwnedServer(server)) {
/* 840 */       graphics.text(RealmsMainScreen.access$600(RealmsMainScreen.this), server.owner, textX, thirdLineY(firstLineY), -8355712);
/* 841 */     } else if (server.expired) {
/* 842 */       Component expirationText = server.expiredTrial ? RealmsMainScreen.TRIAL_EXPIRED_TEXT : RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
/* 843 */       graphics.text(RealmsMainScreen.access$700(RealmsMainScreen.this), expirationText, textX, thirdLineY, -2142128);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractClampedString(GuiGraphicsExtractor graphics, String string, int x, int y, int maxX, int color) {
/* 848 */     if (string == null)
/*     */       return; 
/* 851 */     int availableSpace = maxX - x;
/* 852 */     if (RealmsMainScreen.access$800(RealmsMainScreen.this).width(string) > availableSpace) {
/* 853 */       String clampedName = RealmsMainScreen.access$1000(RealmsMainScreen.this).plainSubstrByWidth(string, availableSpace - RealmsMainScreen.access$900(RealmsMainScreen.this).width("... "));
/* 854 */       graphics.text(RealmsMainScreen.access$1100(RealmsMainScreen.this), clampedName + "...", x, y, color);
/*     */     } else {
/* 856 */       graphics.text(RealmsMainScreen.access$1200(RealmsMainScreen.this), string, x, y, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int versionTextX(int rowLeft, int rowWidth, Component versionComponent) {
/* 861 */     return rowLeft + rowWidth - RealmsMainScreen.access$1300(RealmsMainScreen.this).width((FormattedText)versionComponent) - 20;
/*     */   }
/*     */   
/*     */   protected int gameModeTextX(int rowLeft, int rowWidth, Component versionComponent) {
/* 865 */     return rowLeft + rowWidth - RealmsMainScreen.access$1400(RealmsMainScreen.this).width((FormattedText)versionComponent) - 20;
/*     */   }
/*     */   
/*     */   protected int extractGameMode(RealmsServer server, GuiGraphicsExtractor graphics, int rowLeft, int rowWidth, int firstLineY) {
/* 869 */     boolean hardcore = server.isHardcore;
/* 870 */     int gameMode = server.gameMode;
/* 871 */     int x = rowLeft;
/* 872 */     if (GameType.isValidId(gameMode)) {
/* 873 */       Component gameModeComponent = RealmsMainScreen.getGameModeComponent(gameMode, hardcore);
/* 874 */       x = gameModeTextX(rowLeft, rowWidth, gameModeComponent);
/* 875 */       graphics.text(RealmsMainScreen.access$1500(RealmsMainScreen.this), gameModeComponent, x, secondLineY(firstLineY), -8355712);
/*     */     } 
/* 877 */     if (hardcore) {
/* 878 */       x -= 10;
/* 879 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x, secondLineY(firstLineY), 8, 8);
/*     */     } 
/* 881 */     return x;
/*     */   }
/*     */   
/*     */   protected int firstLineY(int rowTop) {
/* 885 */     return rowTop + 1;
/*     */   }
/*     */   
/*     */   protected int lineHeight() {
/* 889 */     Objects.requireNonNull(RealmsMainScreen.access$1600(RealmsMainScreen.this));
/* 889 */     return 2 + 9;
/*     */   }
/*     */   
/*     */   protected int textX(int rowLeft) {
/* 893 */     return rowLeft + 36 + 2;
/*     */   }
/*     */   
/*     */   protected int secondLineY(int firstLineY) {
/* 897 */     return firstLineY + lineHeight();
/*     */   }
/*     */   
/*     */   protected int thirdLineY(int firstLineY) {
/* 901 */     return firstLineY + lineHeight() * 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */