/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleLists;
/*    */ 
/*    */ public class IndirectMerger implements IndexMerger {
/*  8 */   private static final DoubleList EMPTY = DoubleLists.unmodifiable((DoubleList)DoubleArrayList.wrap(new double[] { 0.0D }));
/*    */   
/*    */   private final double[] result;
/*    */   
/*    */   private final int[] firstIndices;
/*    */   
/*    */   private final int[] secondIndices;
/*    */   
/*    */   private final int resultLength;
/*    */   
/*    */   public IndirectMerger(DoubleList first, DoubleList second, boolean firstOnlyMatters, boolean secondOnlyMatters) {
/* 18 */     double lastValue = Double.NaN;
/* 20 */     int firstSize = first.size();
/* 21 */     int secondSize = second.size();
/* 22 */     int capacity = firstSize + secondSize;
/* 23 */     this.result = new double[capacity];
/* 24 */     this.firstIndices = new int[capacity];
/* 25 */     this.secondIndices = new int[capacity];
/* 27 */     boolean canSkipFirst = !firstOnlyMatters;
/* 28 */     boolean canSkipSecond = !secondOnlyMatters;
/* 30 */     int resultIndex = 0;
/* 31 */     int firstIndex = 0;
/* 32 */     int secondIndex = 0;
/*    */     while (true) {
/* 35 */       boolean ranOutOfFirst = (firstIndex >= firstSize);
/* 36 */       boolean ranOutOfSecond = (secondIndex >= secondSize);
/* 38 */       if (ranOutOfFirst && ranOutOfSecond)
/*    */         break; 
/* 41 */       boolean choseFirst = (!ranOutOfFirst && (ranOutOfSecond || first.getDouble(firstIndex) < second.getDouble(secondIndex) + 1.0E-7D));
/* 43 */       if (choseFirst) {
/* 44 */         firstIndex++;
/* 45 */         if (canSkipFirst && (secondIndex == 0 || ranOutOfSecond))
/*    */           continue; 
/*    */       } else {
/* 49 */         secondIndex++;
/* 50 */         if (canSkipSecond && (firstIndex == 0 || ranOutOfFirst))
/*    */           continue; 
/*    */       } 
/* 55 */       int currentFirstIndex = firstIndex - 1;
/* 56 */       int currentSecondIndex = secondIndex - 1;
/* 58 */       double nextValue = choseFirst ? first.getDouble(currentFirstIndex) : second.getDouble(currentSecondIndex);
/* 59 */       if (lastValue < nextValue - 1.0E-7D) {
/* 60 */         this.firstIndices[resultIndex] = currentFirstIndex;
/* 61 */         this.secondIndices[resultIndex] = currentSecondIndex;
/* 62 */         this.result[resultIndex] = nextValue;
/* 63 */         resultIndex++;
/* 64 */         lastValue = nextValue;
/*    */         continue;
/*    */       } 
/* 66 */       this.firstIndices[resultIndex - 1] = currentFirstIndex;
/* 67 */       this.secondIndices[resultIndex - 1] = currentSecondIndex;
/*    */     } 
/* 71 */     this.resultLength = Math.max(1, resultIndex);
/*    */   }
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 76 */     int length = this.resultLength - 1;
/* 77 */     for (int i = 0; i < length; i++) {
/* 78 */       if (!consumer.merge(this.firstIndices[i], this.secondIndices[i], i))
/* 79 */         return false; 
/*    */     } 
/* 82 */     return true;
/*    */   }
/*    */   
/*    */   public int size() {
/* 87 */     return this.resultLength;
/*    */   }
/*    */   
/*    */   public DoubleList getList() {
/* 92 */     return (this.resultLength <= 1) ? EMPTY : (DoubleList)DoubleArrayList.wrap(this.result, this.resultLength);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\IndirectMerger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */