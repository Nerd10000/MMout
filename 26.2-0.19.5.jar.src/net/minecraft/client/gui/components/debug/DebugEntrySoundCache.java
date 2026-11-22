/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.sounds.SoundBufferLibrary;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntrySoundCache implements DebugScreenEntry {
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 15 */     return true;
/*    */   }
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     SoundBufferLibrary.DebugOutput.Counter counter = new SoundBufferLibrary.DebugOutput.Counter();
/* 21 */     Minecraft.getInstance().getSoundManager().getSoundCacheDebugStats((SoundBufferLibrary.DebugOutput)counter);
/* 22 */     displayer.addLine(String.format(Locale.ROOT, "Sound cache: %d buffers, %d MiB", new Object[] { Integer.valueOf(counter.totalCount()), Long.valueOf(bytesToMegabytes(counter.totalSize())) }));
/*    */   }
/*    */   
/*    */   private static long bytesToMegabytes(long used) {
/* 26 */     return Mth.ceilLong(used / 1024.0D / 1024.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntrySoundCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */