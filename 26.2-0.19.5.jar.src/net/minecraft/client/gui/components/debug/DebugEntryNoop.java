/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryNoop implements DebugScreenEntry {
/*    */   private final boolean isAllowedWithReducedDebugInfo;
/*    */   
/*    */   public DebugEntryNoop() {
/* 11 */     this(false);
/*    */   }
/*    */   
/*    */   public DebugEntryNoop(boolean isAllowedWithReducedDebugInfo) {
/* 15 */     this.isAllowedWithReducedDebugInfo = isAllowedWithReducedDebugInfo;
/*    */   }
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {}
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 25 */     return (this.isAllowedWithReducedDebugInfo || !reducedDebugInfo);
/*    */   }
/*    */   
/*    */   public DebugEntryCategory category() {
/* 30 */     return DebugEntryCategory.RENDERER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryNoop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */