/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class SymlinkLevelSummary extends LevelSummary {
/* 246 */   private static final Component MORE_INFO_BUTTON = (Component)Component.translatable("symlink_warning.more_info");
/*     */   
/* 247 */   private static final Component INFO = (Component)Component.translatable("symlink_warning.title").withColor(-65536);
/*     */   
/*     */   public SymlinkLevelSummary(String levelId, Path icon) {
/* 250 */     super(null, null, levelId, false, false, false, false, icon);
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/* 255 */     return getLevelId();
/*     */   }
/*     */   
/*     */   public Component getInfo() {
/* 260 */     return INFO;
/*     */   }
/*     */   
/*     */   public long getLastPlayed() {
/* 265 */     return -1L;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 270 */     return false;
/*     */   }
/*     */   
/*     */   public Component primaryActionMessage() {
/* 275 */     return MORE_INFO_BUTTON;
/*     */   }
/*     */   
/*     */   public boolean primaryActionActive() {
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canUpload() {
/* 285 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canEdit() {
/* 290 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canRecreate() {
/* 295 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelSummary$SymlinkLevelSummary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */