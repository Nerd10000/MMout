/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryChunkSourceStats implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 11 */     Minecraft minecraft = Minecraft.getInstance();
/* 12 */     if (minecraft.level != null)
/* 13 */       displayer.addLine(minecraft.level.gatherChunkSourceStats()); 
/* 15 */     if (serverOrClientLevel != null && serverOrClientLevel != minecraft.level)
/* 16 */       displayer.addLine(serverOrClientLevel.gatherChunkSourceStats()); 
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 22 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryChunkSourceStats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */