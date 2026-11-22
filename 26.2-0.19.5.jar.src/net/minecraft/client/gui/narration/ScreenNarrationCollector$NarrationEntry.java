/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ class NarrationEntry {
/* 65 */   private NarrationThunk<?> contents = NarrationThunk.EMPTY;
/*    */   
/* 66 */   private int generation = -1;
/*    */   
/*    */   private boolean alreadyNarrated;
/*    */   
/*    */   public NarrationEntry update(int generation, NarrationThunk<?> contents) {
/* 70 */     if (!this.contents.equals(contents)) {
/* 71 */       this.contents = contents;
/* 72 */       this.alreadyNarrated = false;
/* 73 */     } else if (this.generation + 1 != generation) {
/* 74 */       this.alreadyNarrated = false;
/*    */     } 
/* 77 */     this.generation = generation;
/* 78 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\ScreenNarrationCollector$NarrationEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */