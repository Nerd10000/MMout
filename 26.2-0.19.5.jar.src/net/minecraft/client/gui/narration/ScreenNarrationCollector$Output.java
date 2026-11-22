/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ class Output implements NarrationElementOutput {
/*    */   private final int depth;
/*    */   
/*    */   private Output(int depth) {
/* 17 */     this.depth = depth;
/*    */   }
/*    */   
/*    */   public void add(NarratedElementType type, NarrationThunk<?> contents) {
/* 22 */     ((ScreenNarrationCollector.NarrationEntry)ScreenNarrationCollector.this.entries.computeIfAbsent(new ScreenNarrationCollector.EntryKey(type, this.depth), k -> new ScreenNarrationCollector.NarrationEntry())).update(ScreenNarrationCollector.this.generation, contents);
/*    */   }
/*    */   
/*    */   public NarrationElementOutput nest() {
/* 27 */     return new Output(this.depth + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\ScreenNarrationCollector$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */