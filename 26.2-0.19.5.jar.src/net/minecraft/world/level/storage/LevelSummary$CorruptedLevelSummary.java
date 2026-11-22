/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class CorruptedLevelSummary extends LevelSummary {
/*     */   private static final Component INFO;
/*     */   
/*     */   static {
/* 300 */     INFO = (Component)Component.translatable("recover_world.warning").withStyle(style -> style.withColor(-65536));
/*     */   }
/*     */   
/* 301 */   private static final Component RECOVER = (Component)Component.translatable("recover_world.button");
/*     */   
/*     */   private final long lastPlayed;
/*     */   
/*     */   public CorruptedLevelSummary(String levelId, Path icon, long lastPlayed) {
/* 305 */     super(null, null, levelId, false, false, false, false, icon);
/* 306 */     this.lastPlayed = lastPlayed;
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/* 311 */     return getLevelId();
/*     */   }
/*     */   
/*     */   public Component getInfo() {
/* 316 */     return INFO;
/*     */   }
/*     */   
/*     */   public long getLastPlayed() {
/* 321 */     return this.lastPlayed;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 326 */     return false;
/*     */   }
/*     */   
/*     */   public Component primaryActionMessage() {
/* 331 */     return RECOVER;
/*     */   }
/*     */   
/*     */   public boolean primaryActionActive() {
/* 336 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canUpload() {
/* 341 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canEdit() {
/* 346 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canRecreate() {
/* 351 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelSummary$CorruptedLevelSummary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */