/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.clock.ClockManager;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.timeline.Timeline;
/*    */ import net.minecraft.world.timeline.Timelines;
/*    */ 
/*    */ public class DebugEntryDayCount implements DebugScreenEntry {
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 12 */     if (serverOrClientLevel != null) {
/* 13 */       ClockManager clockManager = serverOrClientLevel.clockManager();
/* 14 */       serverOrClientLevel.registryAccess().get(Timelines.OVERWORLD_DAY).ifPresent(timeline -> displayer.addLine("Day #" + ((Timeline)timeline.value()).getPeriodCount(clockManager)));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryDayCount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */