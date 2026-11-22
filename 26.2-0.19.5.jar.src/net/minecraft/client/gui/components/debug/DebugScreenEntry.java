/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public interface DebugScreenEntry {
/*    */   void display(DebugScreenDisplayer paramDebugScreenDisplayer, Level paramLevel, LevelChunk paramLevelChunk1, LevelChunk paramLevelChunk2);
/*    */   
/*    */   default boolean isAllowed(boolean reducedDebugInfo) {
/* 11 */     return !reducedDebugInfo;
/*    */   }
/*    */   
/*    */   default DebugEntryCategory category() {
/* 15 */     return DebugEntryCategory.SCREEN_TEXT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */