/*      */ package net.minecraft.client.multiplayer;
/*      */ 
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ 
/*      */ public class ClientLevelData implements WritableLevelData {
/*      */   private final boolean hardcore;
/*      */   
/*      */   private final boolean isFlat;
/*      */   
/*      */   private LevelData.RespawnData respawnData;
/*      */   
/*      */   private long gameTime;
/*      */   
/*      */   private Difficulty difficulty;
/*      */   
/*      */   private boolean difficultyLocked;
/*      */   
/*      */   public ClientLevelData(Difficulty difficulty, boolean hardcore, boolean isFlat) {
/* 1035 */     this.difficulty = difficulty;
/* 1036 */     this.hardcore = hardcore;
/* 1037 */     this.isFlat = isFlat;
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/* 1042 */     return this.respawnData;
/*      */   }
/*      */   
/*      */   public long getGameTime() {
/* 1047 */     return this.gameTime;
/*      */   }
/*      */   
/*      */   public void setGameTime(long time) {
/* 1051 */     this.gameTime = time;
/*      */   }
/*      */   
/*      */   public void setSpawn(LevelData.RespawnData respawnData) {
/* 1056 */     this.respawnData = respawnData;
/*      */   }
/*      */   
/*      */   public boolean isHardcore() {
/* 1061 */     return this.hardcore;
/*      */   }
/*      */   
/*      */   public Difficulty getDifficulty() {
/* 1066 */     return this.difficulty;
/*      */   }
/*      */   
/*      */   public boolean isDifficultyLocked() {
/* 1071 */     return this.difficultyLocked;
/*      */   }
/*      */   
/*      */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 1076 */     super.fillCrashReportCategory(category, levelHeightAccessor);
/*      */   }
/*      */   
/*      */   public void setDifficulty(Difficulty difficulty) {
/* 1080 */     this.difficulty = difficulty;
/*      */   }
/*      */   
/*      */   public void setDifficultyLocked(boolean locked) {
/* 1084 */     this.difficultyLocked = locked;
/*      */   }
/*      */   
/*      */   public double getHorizonHeight(LevelHeightAccessor level) {
/* 1088 */     if (this.isFlat)
/* 1089 */       return level.getMinY(); 
/* 1092 */     return 63.0D;
/*      */   }
/*      */   
/*      */   public float voidDarknessOnsetRange() {
/* 1096 */     if (this.isFlat)
/* 1097 */       return 1.0F; 
/* 1099 */     return 32.0F;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientLevel$ClientLevelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */