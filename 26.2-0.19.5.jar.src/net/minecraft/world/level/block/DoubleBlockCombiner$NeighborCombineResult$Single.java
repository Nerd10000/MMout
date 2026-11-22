/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ public final class Single<S> implements DoubleBlockCombiner.NeighborCombineResult<S> {
/*    */   private final S single;
/*    */   
/*    */   public Single(S single) {
/* 91 */     this.single = single;
/*    */   }
/*    */   
/*    */   public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) {
/* 96 */     return callback.acceptSingle(this.single);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$NeighborCombineResult$Single.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */