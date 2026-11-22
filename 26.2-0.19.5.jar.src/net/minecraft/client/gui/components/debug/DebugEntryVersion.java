/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.ClientBrandRetriever;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryVersion implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level level, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 13 */     displayer.addPriorityLine("Minecraft " + SharedConstants.getCurrentVersion().name() + " (" + Minecraft.getInstance().getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")");
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 18 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryVersion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */