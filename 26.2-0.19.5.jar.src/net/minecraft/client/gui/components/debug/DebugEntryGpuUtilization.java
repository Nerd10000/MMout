/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryGpuUtilization implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 12 */     Minecraft minecraft = Minecraft.getInstance();
/* 14 */     String gpuUtilizationString = "GPU: " + ((minecraft.getGpuUtilization() > 100.0D) ? (String.valueOf(ChatFormatting.RED) + "100%") : ("" + Math.round(minecraft.getGpuUtilization()) + "%"));
/* 16 */     displayer.addLine(gpuUtilizationString);
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 21 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryGpuUtilization.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */