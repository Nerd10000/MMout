/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.MemoryMXBean;
/*    */ import java.lang.management.MemoryUsage;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryDetailedMemory implements DebugScreenEntry {
/* 15 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("memory");
/*    */   
/* 16 */   private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 20 */     displayer.addToGroup(GROUP, List.of(
/* 21 */           printMemoryUsage(this.memoryBean.getHeapMemoryUsage(), "heap"), 
/* 22 */           printMemoryUsage(this.memoryBean.getNonHeapMemoryUsage(), "non-heap")));
/*    */   }
/*    */   
/*    */   private static long bytesToMebibytes(long used) {
/* 27 */     return used / 1024L / 1024L;
/*    */   }
/*    */   
/*    */   private static String printMemoryUsage(MemoryUsage memoryUsage, String type) {
/* 31 */     return String.format(Locale.ROOT, "Memory (%s): i=%03dMiB u=%03dMiB c=%03dMiB m=%03dMiB", new Object[] { type, 
/*    */           
/* 33 */           Long.valueOf(bytesToMebibytes(memoryUsage.getInit())), 
/* 34 */           Long.valueOf(bytesToMebibytes(memoryUsage.getUsed())), 
/* 35 */           Long.valueOf(bytesToMebibytes(memoryUsage.getCommitted())), 
/* 36 */           Long.valueOf(bytesToMebibytes(memoryUsage.getMax())) });
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryDetailedMemory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */