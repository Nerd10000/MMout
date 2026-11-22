/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.Backup;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class RealmsBackupInfoScreen extends RealmsScreen {
/*  19 */   private static final Component TITLE = (Component)Component.translatable("mco.backup.info.title");
/*     */   
/*  20 */   private static final Component UNKNOWN = (Component)Component.translatable("mco.backup.unknown");
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final Backup backup;
/*     */   
/*  25 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private BackupInfoList backupInfoList;
/*     */   
/*     */   public RealmsBackupInfoScreen(Screen lastScreen, Backup backup) {
/*  30 */     super(TITLE);
/*  31 */     this.lastScreen = lastScreen;
/*  32 */     this.backup = backup;
/*     */   }
/*     */   
/*     */   public void init() {
/*  37 */     this.layout.addTitleHeader(TITLE, this.font);
/*  38 */     this.backupInfoList = (BackupInfoList)this.layout.addToContents((LayoutElement)new BackupInfoList(this, this.minecraft));
/*  39 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*  41 */     repositionElements();
/*  42 */     RealmsBackupInfoScreen realmsBackupInfoScreen = this;
/*  42 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  47 */     this.backupInfoList.updateSize(this.width, this.layout);
/*  48 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/*  53 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private Component checkForSpecificMetadata(String key, String value) {
/*  57 */     String k = key.toLowerCase(Locale.ROOT);
/*  58 */     if (k.contains("game") && k.contains("mode"))
/*  59 */       return gameModeMetadata(value); 
/*  60 */     if (k.contains("game") && k.contains("difficulty"))
/*  61 */       return gameDifficultyMetadata(value); 
/*  62 */     if (key.equals("world_type"))
/*  63 */       return parseWorldType(value); 
/*  65 */     return (Component)Component.literal(value);
/*     */   }
/*     */   
/*     */   private Component gameDifficultyMetadata(String value) {
/*     */     try {
/*  70 */       return ((Difficulty)RealmsSlotOptionsScreen.DIFFICULTIES.get(Integer.parseInt(value))).getDisplayName();
/*  71 */     } catch (Exception ignored) {
/*  72 */       return UNKNOWN;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component gameModeMetadata(String value) {
/*     */     try {
/*  78 */       return ((GameType)RealmsSlotOptionsScreen.GAME_MODES.get(Integer.parseInt(value))).getShortDisplayName();
/*  79 */     } catch (Exception ignored) {
/*  80 */       return UNKNOWN;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component parseWorldType(String value) {
/*     */     try {
/*  86 */       return RealmsServer.WorldType.valueOf(value.toUpperCase(Locale.ROOT)).getDisplayName();
/*  87 */     } catch (Exception ignored) {
/*  88 */       return RealmsServer.WorldType.UNKNOWN.getDisplayName();
/*     */     } 
/*     */   }
/*     */   
/*     */   private class BackupInfoListEntry extends ObjectSelectionList.Entry<BackupInfoListEntry> {
/*  93 */     private static final Component TEMPLATE_NAME = (Component)Component.translatable("mco.backup.entry.templateName");
/*     */     
/*  94 */     private static final Component GAME_DIFFICULTY = (Component)Component.translatable("mco.backup.entry.gameDifficulty");
/*     */     
/*  95 */     private static final Component NAME = (Component)Component.translatable("mco.backup.entry.name");
/*     */     
/*  96 */     private static final Component GAME_SERVER_VERSION = (Component)Component.translatable("mco.backup.entry.gameServerVersion");
/*     */     
/*  97 */     private static final Component UPLOADED = (Component)Component.translatable("mco.backup.entry.uploaded");
/*     */     
/*  98 */     private static final Component ENABLED_PACK = (Component)Component.translatable("mco.backup.entry.enabledPack");
/*     */     
/*  99 */     private static final Component DESCRIPTION = (Component)Component.translatable("mco.backup.entry.description");
/*     */     
/* 100 */     private static final Component GAME_MODE = (Component)Component.translatable("mco.backup.entry.gameMode");
/*     */     
/* 101 */     private static final Component SEED = (Component)Component.translatable("mco.backup.entry.seed");
/*     */     
/* 102 */     private static final Component WORLD_TYPE = (Component)Component.translatable("mco.backup.entry.worldType");
/*     */     
/* 103 */     private static final Component UNDEFINED = (Component)Component.translatable("mco.backup.entry.undefined");
/*     */     
/*     */     private final String key;
/*     */     
/*     */     private final String value;
/*     */     
/*     */     private final Component keyComponent;
/*     */     
/*     */     private final Component valueComponent;
/*     */     
/*     */     public BackupInfoListEntry(String key, String value) {
/* 111 */       this.key = key;
/* 112 */       this.value = value;
/* 113 */       this.keyComponent = translateKey(key);
/* 114 */       this.valueComponent = RealmsBackupInfoScreen.this.checkForSpecificMetadata(key, value);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 119 */       graphics.text(RealmsBackupInfoScreen.this.font, this.keyComponent, getContentX(), getContentY(), -6250336);
/* 120 */       graphics.text(RealmsBackupInfoScreen.this.font, this.valueComponent, getContentX(), getContentY() + 12, -1);
/*     */     }
/*     */     
/*     */     private Component translateKey(String key) {
/* 124 */       switch (key) {
/*     */         case "template_name":
/*     */         
/*     */         case "game_difficulty":
/*     */         
/*     */         case "name":
/*     */         
/*     */         case "game_server_version":
/*     */         
/*     */         case "uploaded":
/*     */         
/*     */         case "enabled_packs":
/*     */         
/*     */         case "description":
/*     */         
/*     */         case "game_mode":
/*     */         
/*     */         case "seed":
/*     */         
/*     */         case "world_type":
/*     */         
/*     */       } 
/* 124 */       return 
/*     */         
/* 135 */         UNDEFINED;
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 141 */       return (Component)Component.translatable("narrator.select", new Object[] { this.key + " " + this.key });
/*     */     }
/*     */   }
/*     */   
/*     */   private class BackupInfoList extends ObjectSelectionList<BackupInfoListEntry> {
/*     */     public BackupInfoList(RealmsBackupInfoScreen this$0, Minecraft minecraft) {
/* 147 */       super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
/* 148 */       if (this$0.backup.changeList != null)
/* 149 */         this$0.backup.changeList.forEach((key, value) -> addEntry((AbstractSelectionList.Entry)new RealmsBackupInfoScreen.BackupInfoListEntry(key, value))); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupInfoScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */