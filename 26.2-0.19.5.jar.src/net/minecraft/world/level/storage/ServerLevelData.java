/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ 
/*    */ public interface ServerLevelData extends WritableLevelData {
/*    */   String getLevelName();
/*    */   
/*    */   default void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 14 */     super.fillCrashReportCategory(category, levelHeightAccessor);
/* 15 */     category.setDetail("Level name", this::getLevelName);
/* 16 */     category.setDetail("Level game mode", () -> String.format(Locale.ROOT, "Game mode: %s (ID %d). Hardcore: %b. Commands: %b", new Object[] { getGameType().getName(), Integer.valueOf(getGameType().getId()), Boolean.valueOf(isHardcore()), Boolean.valueOf(isAllowCommands()) }));
/*    */   }
/*    */   
/*    */   GameType getGameType();
/*    */   
/*    */   boolean isInitialized();
/*    */   
/*    */   void setInitialized(boolean paramBoolean);
/*    */   
/*    */   boolean isAllowCommands();
/*    */   
/*    */   void setAllowCommands(boolean paramBoolean);
/*    */   
/*    */   void setGameType(GameType paramGameType);
/*    */   
/*    */   void setGameTime(long paramLong);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ServerLevelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */