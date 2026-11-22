/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ 
/*     */ class BackupObjectSelectionList extends ContainerObjectSelectionList<RealmsBackupScreen.Entry> {
/*     */   private static final int ITEM_HEIGHT = 36;
/*     */   
/*     */   public BackupObjectSelectionList(RealmsBackupScreen paramRealmsBackupScreen) {
/* 155 */     super(Minecraft.getInstance(), paramRealmsBackupScreen.width, paramRealmsBackupScreen.layout.getContentHeight(), paramRealmsBackupScreen.layout.getHeaderHeight(), 36);
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 160 */     return 300;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupScreen$BackupObjectSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */