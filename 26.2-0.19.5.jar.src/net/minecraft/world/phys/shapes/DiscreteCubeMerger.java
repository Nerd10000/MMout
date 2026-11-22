/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import com.google.common.math.IntMath;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public final class DiscreteCubeMerger implements IndexMerger {
/*    */   private final CubePointRange result;
/*    */   
/*    */   private final int firstDiv;
/*    */   
/*    */   private final int secondDiv;
/*    */   
/*    */   DiscreteCubeMerger(int firstSize, int secondSize) {
/* 14 */     this.result = new CubePointRange((int)Shapes.lcm(firstSize, secondSize));
/* 16 */     int gcd = IntMath.gcd(firstSize, secondSize);
/* 17 */     this.firstDiv = firstSize / gcd;
/* 18 */     this.secondDiv = secondSize / gcd;
/*    */   }
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 23 */     int size = this.result.size() - 1;
/* 24 */     for (int i = 0; i < size; i++) {
/* 25 */       if (!consumer.merge(i / this.secondDiv, i / this.firstDiv, i))
/* 26 */         return false; 
/*    */     } 
/* 29 */     return true;
/*    */   }
/*    */   
/*    */   public int size() {
/* 34 */     return this.result.size();
/*    */   }
/*    */   
/*    */   public DoubleList getList() {
/* 39 */     return (DoubleList)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\DiscreteCubeMerger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */