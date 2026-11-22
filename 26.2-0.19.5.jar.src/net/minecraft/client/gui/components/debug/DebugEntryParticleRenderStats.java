/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryParticleRenderStats implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 11 */     displayer.addLine("P: " + (Minecraft.getInstance()).particleEngine.countParticles());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryParticleRenderStats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */