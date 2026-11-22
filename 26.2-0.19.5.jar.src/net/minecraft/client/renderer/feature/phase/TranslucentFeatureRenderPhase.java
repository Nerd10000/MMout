/*    */ package net.minecraft.client.renderer.feature.phase;
/*    */ 
/*    */ import com.google.common.primitives.Floats;
/*    */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*    */ import it.unimi.dsi.fastutil.floats.FloatList;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ 
/*    */ public class TranslucentFeatureRenderPhase implements FeatureRenderPhase<TranslucentSubmit> {
/* 13 */   private final List<TranslucentSubmit> submits = new ArrayList<>();
/*    */   
/* 14 */   private final FloatList distances = (FloatList)new FloatArrayList();
/*    */   
/*    */   public void submit(TranslucentSubmit submit) {
/* 18 */     this.submits.add(submit);
/* 19 */     this.distances.add(submit.distanceToCameraSq());
/*    */   }
/*    */   
/*    */   public void sortInto(FeatureRenderPhase.Output output) {
/* 24 */     if (this.submits.isEmpty())
/*    */       return; 
/* 27 */     for (int index : sortIndices())
/* 28 */       output.accept((SubmitNode)this.submits.get(index), true); 
/* 30 */     this.submits.clear();
/* 31 */     this.distances.clear();
/*    */   }
/*    */   
/*    */   private int[] sortIndices() {
/* 35 */     int[] indices = new int[this.submits.size()];
/* 36 */     for (int i = 0; i < this.submits.size(); i++)
/* 37 */       indices[i] = i; 
/* 39 */     IntArrays.unstableSort(indices, (i1, i2) -> Floats.compare(this.distances.getFloat(i2), this.distances.getFloat(i1)));
/* 40 */     return indices;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 45 */     return this.submits.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\phase\TranslucentFeatureRenderPhase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */