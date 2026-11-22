/*    */ package net.minecraft.client.renderer.feature.phase;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.client.renderer.feature.FeatureRendererType;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ 
/*    */ public interface FeatureRenderPhase<Submit extends SubmitNode> {
/*    */   void submit(Submit paramSubmit);
/*    */   
/*    */   void sortInto(Output paramOutput);
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output {
/*    */     void accept(SubmitNode param1SubmitNode, boolean param1Boolean);
/*    */     
/*    */     default <Submit extends SubmitNode> void acceptFeatureGroup(FeatureRendererType<Submit> featureType, Collection<Submit> submits, boolean strictlyOrdered) {
/* 20 */       for (SubmitNode submitNode : submits) {
/* 21 */         if (submitNode.featureType() != featureType)
/* 22 */           throw new IllegalArgumentException(String.valueOf(submitNode) + " was not of feature type " + String.valueOf(submitNode)); 
/* 24 */         accept(submitNode, strictlyOrdered);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\phase\FeatureRenderPhase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */