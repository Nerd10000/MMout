/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class FeatureRendererMap implements AutoCloseable {
/* 12 */   private FeatureRenderer<?>[] renderers = (FeatureRenderer<?>[])new FeatureRenderer[0];
/*    */   
/*    */   public <Submit extends net.minecraft.client.renderer.feature.submit.SubmitNode> void put(FeatureRendererType<Submit> type, FeatureRenderer<Submit> renderer) {
/* 15 */     if (this.renderers.length <= type.id())
/* 16 */       this.renderers = (FeatureRenderer<?>[])Arrays.<FeatureRenderer>copyOf((FeatureRenderer[])this.renderers, Mth.roundToward(type.id() + 1, 16)); 
/* 18 */     this.renderers[type.id()] = renderer;
/*    */   }
/*    */   
/*    */   public <Submit extends net.minecraft.client.renderer.feature.submit.SubmitNode> FeatureRenderer<Submit> get(FeatureRendererType<Submit> type) {
/* 23 */     if (type.id() >= this.renderers.length)
/* 24 */       return null; 
/* 26 */     return (FeatureRenderer)this.renderers[type.id()];
/*    */   }
/*    */   
/*    */   public <Submit extends net.minecraft.client.renderer.feature.submit.SubmitNode> FeatureRenderer<Submit> getOrThrow(FeatureRendererType<Submit> type) {
/* 30 */     FeatureRenderer<Submit> renderer = get(type);
/* 31 */     if (renderer == null)
/* 32 */       throw new IllegalArgumentException("No FeatureRenderer for type " + String.valueOf(type)); 
/* 34 */     return renderer;
/*    */   }
/*    */   
/*    */   public Iterable<FeatureRenderer<?>> values() {
/* 38 */     return Iterables.filter(Arrays.asList(this.renderers), Objects::nonNull);
/*    */   }
/*    */   
/*    */   public void close() {
/* 43 */     for (FeatureRenderer<?> renderer : this.renderers) {
/* 44 */       if (renderer != null)
/* 45 */         renderer.close(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRendererMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */