/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ @VisibleForTesting
/*    */ abstract class Node<T> {
/*    */   protected final Climate.Parameter[] parameterSpace;
/*    */   
/*    */   protected Node(List<Climate.Parameter> parameterSpace) {
/* 80 */     this.parameterSpace = parameterSpace.<Climate.Parameter>toArray(new Climate.Parameter[0]);
/*    */   }
/*    */   
/*    */   protected abstract Climate.RTree.Leaf<T> search(long[] paramArrayOflong, Climate.RTree.Leaf<T> paramLeaf, Climate.DistanceMetric<T> paramDistanceMetric);
/*    */   
/*    */   protected long distance(long[] target) {
/* 86 */     long distance = 0L;
/* 87 */     for (int i = 0; i < 7; i++)
/* 88 */       distance += Mth.square(this.parameterSpace[i].distance(target[i])); 
/* 90 */     return distance;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 95 */     return Arrays.toString((Object[])this.parameterSpace);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$RTree$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */