/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryPostEffect implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 12 */     Minecraft minecraft = Minecraft.getInstance();
/* 13 */     Identifier effectId = minecraft.gameRenderer.currentPostEffect();
/* 14 */     if (effectId != null)
/* 15 */       displayer.addLine("Post: " + String.valueOf(effectId)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryPostEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */