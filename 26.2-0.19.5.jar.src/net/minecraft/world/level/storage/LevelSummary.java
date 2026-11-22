/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class LevelSummary implements Comparable<LevelSummary> {
/*  20 */   public static final Component PLAY_WORLD = (Component)Component.translatable("selectWorld.select");
/*     */   
/*  21 */   public static final Component UPGRADE_AND_PLAY_WORLD = (Component)Component.translatable("selectWorld.upgrade_and_play");
/*     */   
/*     */   private final LevelSettings settings;
/*     */   
/*     */   private final LevelVersion levelVersion;
/*     */   
/*     */   private final String levelId;
/*     */   
/*     */   private final boolean requiresManualConversion;
/*     */   
/*     */   private final boolean requiresFileFixing;
/*     */   
/*     */   private final boolean locked;
/*     */   
/*     */   private final boolean experimental;
/*     */   
/*     */   private final Path icon;
/*     */   
/*     */   private Component info;
/*     */   
/*     */   public LevelSummary(LevelSettings settings, LevelVersion levelVersion, String levelId, boolean requiresManualConversion, boolean requiresFileFixing, boolean locked, boolean experimental, Path icon) {
/*  34 */     this.settings = settings;
/*  35 */     this.levelVersion = levelVersion;
/*  36 */     this.levelId = levelId;
/*  37 */     this.requiresFileFixing = requiresFileFixing;
/*  38 */     this.locked = locked;
/*  39 */     this.experimental = experimental;
/*  40 */     this.icon = icon;
/*  41 */     this.requiresManualConversion = requiresManualConversion;
/*     */   }
/*     */   
/*     */   public String getLevelId() {
/*  45 */     return this.levelId;
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/*  49 */     return StringUtils.isEmpty(this.settings.levelName()) ? this.levelId : this.settings.levelName();
/*     */   }
/*     */   
/*     */   public Path getIcon() {
/*  53 */     return this.icon;
/*     */   }
/*     */   
/*     */   public boolean requiresManualConversion() {
/*  57 */     return this.requiresManualConversion;
/*     */   }
/*     */   
/*     */   public boolean requiresFileFixing() {
/*  61 */     return this.requiresFileFixing;
/*     */   }
/*     */   
/*     */   public boolean isExperimental() {
/*  65 */     return this.experimental;
/*     */   }
/*     */   
/*     */   public long getLastPlayed() {
/*  69 */     return this.levelVersion.lastPlayed();
/*     */   }
/*     */   
/*     */   public int compareTo(LevelSummary rhs) {
/*  74 */     if (getLastPlayed() < rhs.getLastPlayed())
/*  75 */       return 1; 
/*  77 */     if (getLastPlayed() > rhs.getLastPlayed())
/*  78 */       return -1; 
/*  80 */     return this.levelId.compareTo(rhs.levelId);
/*     */   }
/*     */   
/*     */   public LevelSettings getSettings() {
/*  84 */     return this.settings;
/*     */   }
/*     */   
/*     */   public GameType getGameMode() {
/*  88 */     return this.settings.gameType();
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/*  92 */     return this.settings.difficultySettings().hardcore();
/*     */   }
/*     */   
/*     */   public boolean hasCommands() {
/*  96 */     return this.settings.allowCommands();
/*     */   }
/*     */   
/*     */   public MutableComponent getWorldVersionName() {
/* 100 */     if (StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName()))
/* 101 */       return Component.translatable("selectWorld.versionUnknown"); 
/* 103 */     return Component.literal(this.levelVersion.minecraftVersionName());
/*     */   }
/*     */   
/*     */   public LevelVersion levelVersion() {
/* 107 */     return this.levelVersion;
/*     */   }
/*     */   
/*     */   public boolean shouldBackup() {
/* 111 */     return backupStatus().shouldBackup();
/*     */   }
/*     */   
/*     */   public boolean isDowngrade() {
/* 115 */     return (backupStatus() == BackupStatus.DOWNGRADE);
/*     */   }
/*     */   
/*     */   public BackupStatus backupStatus() {
/* 119 */     WorldVersion currentVersion = SharedConstants.getCurrentVersion();
/* 120 */     int currentVersionNumber = currentVersion.dataVersion().version();
/* 121 */     int levelVersionNumber = this.levelVersion.minecraftVersion().version();
/* 122 */     if (DataFixers.getFileFixer().requiresFileFixing(levelVersionNumber))
/* 123 */       return BackupStatus.FILE_FIXING_REQUIRED; 
/* 124 */     if (!currentVersion.stable() && levelVersionNumber < currentVersionNumber)
/* 125 */       return BackupStatus.UPGRADE_TO_SNAPSHOT; 
/* 126 */     if (levelVersionNumber > currentVersionNumber)
/* 127 */       return BackupStatus.DOWNGRADE; 
/* 129 */     return BackupStatus.NONE;
/*     */   }
/*     */   
/*     */   public boolean isLocked() {
/* 133 */     return this.locked;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 137 */     if (isLocked() || requiresManualConversion())
/* 138 */       return true; 
/* 141 */     return !isCompatible();
/*     */   }
/*     */   
/*     */   public boolean isCompatible() {
/* 145 */     return SharedConstants.getCurrentVersion().dataVersion().isCompatible(this.levelVersion.minecraftVersion());
/*     */   }
/*     */   
/*     */   public Component getInfo() {
/* 149 */     if (this.info == null)
/* 150 */       this.info = createInfo(); 
/* 153 */     return this.info;
/*     */   }
/*     */   
/*     */   private Component createInfo() {
/* 157 */     if (isLocked())
/* 158 */       return (Component)Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED); 
/* 160 */     if (requiresManualConversion())
/* 161 */       return (Component)Component.translatable("selectWorld.conversion").withStyle(ChatFormatting.RED); 
/* 163 */     if (!isCompatible())
/* 164 */       return (Component)Component.translatable("selectWorld.incompatible.info", new Object[] { getWorldVersionName() }).withStyle(ChatFormatting.RED); 
/* 168 */     MutableComponent result = isHardcore() ? Component.empty().append((Component)Component.translatable("gameMode.hardcore").withColor(-65536)) : Component.translatable("gameMode." + getGameMode().getName());
/* 170 */     if (hasCommands())
/* 171 */       result.append(", ").append((Component)Component.translatable("selectWorld.commands")); 
/* 174 */     if (isExperimental())
/* 175 */       result.append(", ").append((Component)Component.translatable("selectWorld.experimental").withStyle(ChatFormatting.YELLOW)); 
/* 178 */     MutableComponent worldVersionName = getWorldVersionName();
/* 179 */     MutableComponent decoratedVersionName = Component.literal(", ").append((Component)Component.translatable("selectWorld.version")).append(CommonComponents.SPACE);
/* 180 */     if (shouldBackup()) {
/* 181 */       decoratedVersionName.append((Component)worldVersionName.withStyle(isDowngrade() ? ChatFormatting.RED : ChatFormatting.ITALIC));
/*     */     } else {
/* 183 */       decoratedVersionName.append((Component)worldVersionName);
/*     */     } 
/* 185 */     result.append((Component)decoratedVersionName);
/* 186 */     return (Component)result;
/*     */   }
/*     */   
/*     */   public Component primaryActionMessage() {
/* 190 */     if (requiresFileFixing())
/* 191 */       return UPGRADE_AND_PLAY_WORLD; 
/* 193 */     return PLAY_WORLD;
/*     */   }
/*     */   
/*     */   public boolean primaryActionActive() {
/* 197 */     return !isDisabled();
/*     */   }
/*     */   
/*     */   public boolean canUpload() {
/* 201 */     return (!requiresManualConversion() && !isLocked());
/*     */   }
/*     */   
/*     */   public boolean canEdit() {
/* 205 */     return (!isDisabled() && !requiresFileFixing());
/*     */   }
/*     */   
/*     */   public boolean canRecreate() {
/* 209 */     return (!isDisabled() && !requiresFileFixing());
/*     */   }
/*     */   
/*     */   public boolean canDelete() {
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   public enum BackupStatus {
/* 217 */     NONE(false, false, ""),
/* 218 */     DOWNGRADE(true, true, "downgrade"),
/* 219 */     UPGRADE_TO_SNAPSHOT(true, false, "snapshot"),
/* 220 */     FILE_FIXING_REQUIRED(true, false, "file_fixing_required");
/*     */     
/*     */     private final boolean shouldBackup;
/*     */     
/*     */     private final boolean severe;
/*     */     
/*     */     private final String translationKey;
/*     */     
/*     */     BackupStatus(boolean shouldBackup, boolean severe, String translationKey) {
/* 227 */       this.shouldBackup = shouldBackup;
/* 228 */       this.severe = severe;
/* 229 */       this.translationKey = translationKey;
/*     */     }
/*     */     
/*     */     public boolean shouldBackup() {
/* 233 */       return this.shouldBackup;
/*     */     }
/*     */     
/*     */     public boolean isSevere() {
/* 237 */       return this.severe;
/*     */     }
/*     */     
/*     */     public String getTranslationKey() {
/* 241 */       return this.translationKey;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SymlinkLevelSummary extends LevelSummary {
/* 246 */     private static final Component MORE_INFO_BUTTON = (Component)Component.translatable("symlink_warning.more_info");
/*     */     
/* 247 */     private static final Component INFO = (Component)Component.translatable("symlink_warning.title").withColor(-65536);
/*     */     
/*     */     public SymlinkLevelSummary(String levelId, Path icon) {
/* 250 */       super(null, null, levelId, false, false, false, false, icon);
/*     */     }
/*     */     
/*     */     public String getLevelName() {
/* 255 */       return getLevelId();
/*     */     }
/*     */     
/*     */     public Component getInfo() {
/* 260 */       return INFO;
/*     */     }
/*     */     
/*     */     public long getLastPlayed() {
/* 265 */       return -1L;
/*     */     }
/*     */     
/*     */     public boolean isDisabled() {
/* 270 */       return false;
/*     */     }
/*     */     
/*     */     public Component primaryActionMessage() {
/* 275 */       return MORE_INFO_BUTTON;
/*     */     }
/*     */     
/*     */     public boolean primaryActionActive() {
/* 280 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canUpload() {
/* 285 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canEdit() {
/* 290 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canRecreate() {
/* 295 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CorruptedLevelSummary extends LevelSummary {
/*     */     private static final Component INFO;
/*     */     
/*     */     static {
/* 300 */       INFO = (Component)Component.translatable("recover_world.warning").withStyle(style -> style.withColor(-65536));
/*     */     }
/*     */     
/* 301 */     private static final Component RECOVER = (Component)Component.translatable("recover_world.button");
/*     */     
/*     */     private final long lastPlayed;
/*     */     
/*     */     public CorruptedLevelSummary(String levelId, Path icon, long lastPlayed) {
/* 305 */       super(null, null, levelId, false, false, false, false, icon);
/* 306 */       this.lastPlayed = lastPlayed;
/*     */     }
/*     */     
/*     */     public String getLevelName() {
/* 311 */       return getLevelId();
/*     */     }
/*     */     
/*     */     public Component getInfo() {
/* 316 */       return INFO;
/*     */     }
/*     */     
/*     */     public long getLastPlayed() {
/* 321 */       return this.lastPlayed;
/*     */     }
/*     */     
/*     */     public boolean isDisabled() {
/* 326 */       return false;
/*     */     }
/*     */     
/*     */     public Component primaryActionMessage() {
/* 331 */       return RECOVER;
/*     */     }
/*     */     
/*     */     public boolean primaryActionActive() {
/* 336 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canUpload() {
/* 341 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canEdit() {
/* 346 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canRecreate() {
/* 351 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelSummary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */