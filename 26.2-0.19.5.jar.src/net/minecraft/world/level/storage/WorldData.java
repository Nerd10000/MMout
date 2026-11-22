/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.LevelSettings;
/*    */ import net.minecraft.world.level.WorldDataConfiguration;
/*    */ 
/*    */ public interface WorldData {
/*    */   public static final int ANVIL_VERSION_ID = 19133;
/*    */   
/*    */   public static final int MCREGION_VERSION_ID = 19132;
/*    */   
/*    */   default void fillCrashReportCategory(CrashReportCategory category) {
/* 34 */     category.setDetail("Known server brands", () -> String.join(", ", (Iterable)getKnownServerBrands()));
/* 35 */     category.setDetail("Removed feature flags", () -> String.join(", ", (Iterable)getRemovedFeatureFlags()));
/* 36 */     category.setDetail("Level was modded", () -> Boolean.toString(wasModded()));
/* 37 */     category.setDetail("Level storage version", () -> {
/*    */           int version = getVersion();
/*    */           return String.format(Locale.ROOT, "0x%05X - %s", new Object[] { Integer.valueOf(version), getStorageVersionName(version) });
/*    */         });
/*    */   }
/*    */   
/*    */   default String getStorageVersionName(int version) {
/* 44 */     switch (version) {
/*    */       case 19133:
/*    */       
/*    */       case 19132:
/*    */       
/*    */     } 
/* 44 */     return 
/*    */       
/* 47 */       "Unknown?";
/*    */   }
/*    */   
/*    */   default FeatureFlagSet enabledFeatures() {
/* 89 */     return getDataConfiguration().enabledFeatures();
/*    */   }
/*    */   
/*    */   WorldDataConfiguration getDataConfiguration();
/*    */   
/*    */   void setDataConfiguration(WorldDataConfiguration paramWorldDataConfiguration);
/*    */   
/*    */   boolean wasModded();
/*    */   
/*    */   Set<String> getKnownServerBrands();
/*    */   
/*    */   Set<String> getRemovedFeatureFlags();
/*    */   
/*    */   void setModdedInfo(String paramString, boolean paramBoolean);
/*    */   
/*    */   ServerLevelData overworldData();
/*    */   
/*    */   LevelSettings getLevelSettings();
/*    */   
/*    */   CompoundTag createTag(UUID paramUUID);
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   int getVersion();
/*    */   
/*    */   String getLevelName();
/*    */   
/*    */   GameType getGameType();
/*    */   
/*    */   void setGameType(GameType paramGameType);
/*    */   
/*    */   boolean isAllowCommands();
/*    */   
/*    */   void setAllowCommands(boolean paramBoolean);
/*    */   
/*    */   Difficulty getDifficulty();
/*    */   
/*    */   void setDifficulty(Difficulty paramDifficulty);
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */   
/*    */   void setDifficultyLocked(boolean paramBoolean);
/*    */   
/*    */   UUID getSinglePlayerUUID();
/*    */   
/*    */   boolean isFlatWorld();
/*    */   
/*    */   boolean isDebugWorld();
/*    */   
/*    */   Lifecycle worldGenSettingsLifecycle();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\WorldData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */