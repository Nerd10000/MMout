/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ public interface NeighborCombineResult<S> {
/*    */   <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> paramCombiner);
/*    */   
/*    */   public static final class Double<S> implements NeighborCombineResult<S> {
/*    */     private final S first;
/*    */     
/*    */     private final S second;
/*    */     
/*    */     public Double(S first, S second) {
/* 77 */       this.first = first;
/* 78 */       this.second = second;
/*    */     }
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) {
/* 83 */       return callback.acceptDouble(this.first, this.second);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Single<S> implements NeighborCombineResult<S> {
/*    */     private final S single;
/*    */     
/*    */     public Single(S single) {
/* 91 */       this.single = single;
/*    */     }
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> callback) {
/* 96 */       return callback.acceptSingle(this.single);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$NeighborCombineResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */