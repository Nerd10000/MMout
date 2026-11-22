/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ 
/*    */ public class DerivedLevelData implements ServerLevelData {
/*    */   private final WorldData worldData;
/*    */   
/*    */   private final ServerLevelData wrapped;
/*    */   
/*    */   public DerivedLevelData(WorldData worldData, ServerLevelData wrapped) {
/* 17 */     this.worldData = worldData;
/* 18 */     this.wrapped = wrapped;
/*    */   }
/*    */   
/*    */   public LevelData.RespawnData getRespawnData() {
/* 23 */     return this.wrapped.getRespawnData();
/*    */   }
/*    */   
/*    */   public long getGameTime() {
/* 28 */     return this.wrapped.getGameTime();
/*    */   }
/*    */   
/*    */   public String getLevelName() {
/* 33 */     return this.worldData.getLevelName();
/*    */   }
/*    */   
/*    */   public GameType getGameType() {
/* 38 */     return this.worldData.getGameType();
/*    */   }
/*    */   
/*    */   public void setGameTime(long time) {}
/*    */   
/*    */   public void setSpawn(LevelData.RespawnData respawnData) {
/* 47 */     this.wrapped.setSpawn(respawnData);
/*    */   }
/*    */   
/*    */   public void setGameType(GameType gameType) {}
/*    */   
/*    */   public boolean isHardcore() {
/* 56 */     return this.worldData.isHardcore();
/*    */   }
/*    */   
/*    */   public boolean isAllowCommands() {
/* 61 */     return this.worldData.isAllowCommands();
/*    */   }
/*    */   
/*    */   public void setAllowCommands(boolean allowCommands) {}
/*    */   
/*    */   public boolean isInitialized() {
/* 70 */     return this.wrapped.isInitialized();
/*    */   }
/*    */   
/*    */   public void setInitialized(boolean initialized) {}
/*    */   
/*    */   public Difficulty getDifficulty() {
/* 79 */     return this.worldData.getDifficulty();
/*    */   }
/*    */   
/*    */   public boolean isDifficultyLocked() {
/* 84 */     return this.worldData.isDifficultyLocked();
/*    */   }
/*    */   
/*    */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 89 */     category.setDetail("Derived", Boolean.valueOf(true));
/* 90 */     this.wrapped.fillCrashReportCategory(category, levelHeightAccessor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\DerivedLevelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */