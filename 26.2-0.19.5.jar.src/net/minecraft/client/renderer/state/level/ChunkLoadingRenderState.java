/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ 
/*    */ public class ChunkLoadingRenderState {
/*  6 */   public LongOpenHashSet addedEmptySections = new LongOpenHashSet();
/*    */   
/*  7 */   public LongOpenHashSet removedEmptySections = new LongOpenHashSet();
/*    */   
/*  8 */   public LongOpenHashSet addedLoadedChunks = new LongOpenHashSet();
/*    */   
/*  9 */   public LongOpenHashSet removedLoadedChunks = new LongOpenHashSet();
/*    */   
/* 10 */   public LongOpenHashSet loadedExpectedChunks = new LongOpenHashSet();
/*    */   
/*    */   public void reset() {
/* 13 */     this.loadedExpectedChunks.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\ChunkLoadingRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */