/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ 
/*     */ class InvitedObjectSelectionList extends ContainerObjectSelectionList<RealmsPlayersTab.Entry> {
/*     */   private static final int PLAYER_ENTRY_HEIGHT = 36;
/*     */   
/*     */   public InvitedObjectSelectionList(int width, int height) {
/*  82 */     super(Minecraft.getInstance(), width, height, paramRealmsPlayersTab.configurationScreen.getHeaderHeight(), 36);
/*     */   }
/*     */   
/*     */   private void updateList(RealmsServer serverData) {
/*  86 */     clearEntries();
/*  87 */     populateList(serverData);
/*     */   }
/*     */   
/*     */   private void populateList(RealmsServer serverData) {
/*  91 */     RealmsPlayersTab.HeaderEntry entry = new RealmsPlayersTab.HeaderEntry(RealmsPlayersTab.this);
/*  92 */     Objects.requireNonNull(RealmsPlayersTab.this.font);
/*  92 */     addEntry((AbstractSelectionList.Entry)entry, entry.height(9));
/*  93 */     for (RealmsPlayersTab.PlayerEntry newChild : serverData.players.stream().map(x$0 -> new RealmsPlayersTab.PlayerEntry(RealmsPlayersTab.this, x$0)).toList())
/*  94 */       addEntry((AbstractSelectionList.Entry)newChild); 
/*     */   }
/*     */   
/*     */   protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   public int getRowWidth() {
/* 108 */     return 300;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsPlayersTab$InvitedObjectSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */