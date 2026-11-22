/*    */ package net.minecraft.client.renderer.feature.phase;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.client.renderer.feature.FeatureRendererType;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Output {
/*    */   void accept(SubmitNode paramSubmitNode, boolean paramBoolean);
/*    */   
/*    */   default <Submit extends SubmitNode> void acceptFeatureGroup(FeatureRendererType<Submit> featureType, Collection<Submit> submits, boolean strictlyOrdered) {
/* 20 */     for (SubmitNode submitNode : submits) {
/* 21 */       if (submitNode.featureType() != featureType)
/* 22 */         throw new IllegalArgumentException(String.valueOf(submitNode) + " was not of feature type " + String.valueOf(submitNode)); 
/* 24 */       accept(submitNode, strictlyOrdered);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\phase\FeatureRenderPhase$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */