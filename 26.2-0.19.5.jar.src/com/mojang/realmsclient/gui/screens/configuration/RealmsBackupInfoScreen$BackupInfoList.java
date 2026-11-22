/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ 
/*     */ class BackupInfoList extends ObjectSelectionList<RealmsBackupInfoScreen.BackupInfoListEntry> {
/*     */   public BackupInfoList(RealmsBackupInfoScreen paramRealmsBackupInfoScreen, Minecraft minecraft) {
/* 147 */     super(minecraft, paramRealmsBackupInfoScreen.width, paramRealmsBackupInfoScreen.layout.getContentHeight(), paramRealmsBackupInfoScreen.layout.getHeaderHeight(), 36);
/* 148 */     if (paramRealmsBackupInfoScreen.backup.changeList != null)
/* 149 */       paramRealmsBackupInfoScreen.backup.changeList.forEach((key, value) -> addEntry((AbstractSelectionList.Entry)new RealmsBackupInfoScreen.BackupInfoListEntry(this$0, key, value))); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupInfoScreen$BackupInfoList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */