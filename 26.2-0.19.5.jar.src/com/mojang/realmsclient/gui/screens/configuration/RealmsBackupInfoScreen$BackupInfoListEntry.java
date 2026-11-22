/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class BackupInfoListEntry extends ObjectSelectionList.Entry<RealmsBackupInfoScreen.BackupInfoListEntry> {
/*  93 */   private static final Component TEMPLATE_NAME = (Component)Component.translatable("mco.backup.entry.templateName");
/*     */   
/*  94 */   private static final Component GAME_DIFFICULTY = (Component)Component.translatable("mco.backup.entry.gameDifficulty");
/*     */   
/*  95 */   private static final Component NAME = (Component)Component.translatable("mco.backup.entry.name");
/*     */   
/*  96 */   private static final Component GAME_SERVER_VERSION = (Component)Component.translatable("mco.backup.entry.gameServerVersion");
/*     */   
/*  97 */   private static final Component UPLOADED = (Component)Component.translatable("mco.backup.entry.uploaded");
/*     */   
/*  98 */   private static final Component ENABLED_PACK = (Component)Component.translatable("mco.backup.entry.enabledPack");
/*     */   
/*  99 */   private static final Component DESCRIPTION = (Component)Component.translatable("mco.backup.entry.description");
/*     */   
/* 100 */   private static final Component GAME_MODE = (Component)Component.translatable("mco.backup.entry.gameMode");
/*     */   
/* 101 */   private static final Component SEED = (Component)Component.translatable("mco.backup.entry.seed");
/*     */   
/* 102 */   private static final Component WORLD_TYPE = (Component)Component.translatable("mco.backup.entry.worldType");
/*     */   
/* 103 */   private static final Component UNDEFINED = (Component)Component.translatable("mco.backup.entry.undefined");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final String value;
/*     */   
/*     */   private final Component keyComponent;
/*     */   
/*     */   private final Component valueComponent;
/*     */   
/*     */   public BackupInfoListEntry(String key, String value) {
/* 111 */     this.key = key;
/* 112 */     this.value = value;
/* 113 */     this.keyComponent = translateKey(key);
/* 114 */     this.valueComponent = paramRealmsBackupInfoScreen.checkForSpecificMetadata(key, value);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 119 */     graphics.text(RealmsBackupInfoScreen.access$000(RealmsBackupInfoScreen.this), this.keyComponent, getContentX(), getContentY(), -6250336);
/* 120 */     graphics.text(RealmsBackupInfoScreen.access$100(RealmsBackupInfoScreen.this), this.valueComponent, getContentX(), getContentY() + 12, -1);
/*     */   }
/*     */   
/*     */   private Component translateKey(String key) {
/* 124 */     switch (key) {
/*     */       case "template_name":
/*     */       
/*     */       case "game_difficulty":
/*     */       
/*     */       case "name":
/*     */       
/*     */       case "game_server_version":
/*     */       
/*     */       case "uploaded":
/*     */       
/*     */       case "enabled_packs":
/*     */       
/*     */       case "description":
/*     */       
/*     */       case "game_mode":
/*     */       
/*     */       case "seed":
/*     */       
/*     */       case "world_type":
/*     */       
/*     */     } 
/* 124 */     return 
/*     */       
/* 135 */       UNDEFINED;
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 141 */     return (Component)Component.translatable("narrator.select", new Object[] { this.key + " " + this.key });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupInfoScreen$BackupInfoListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */