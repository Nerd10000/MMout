/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntrySectionPosition implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 16 */     Minecraft minecraft = Minecraft.getInstance();
/* 17 */     Entity entity = minecraft.getCameraEntity();
/* 19 */     if (entity == null)
/*    */       return; 
/* 23 */     BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
/* 25 */     displayer.addToGroup(DebugEntryPosition.GROUP, String.format(Locale.ROOT, "Section-relative: %02d %02d %02d", new Object[] { Integer.valueOf(feetPos.getX() & 0xF), Integer.valueOf(feetPos.getY() & 0xF), Integer.valueOf(feetPos.getZ() & 0xF) }));
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntrySectionPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */