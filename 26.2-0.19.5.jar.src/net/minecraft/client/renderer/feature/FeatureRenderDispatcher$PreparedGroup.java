/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ 
/*     */ class PreparedGroup<Submit extends SubmitNode> {
/*     */   private final int featureGroupIndex;
/*     */   
/*     */   private final FeatureRendererType<Submit> featureType;
/*     */   
/*     */   private final boolean strictlyOrdered;
/*     */   
/*     */   private final int fromInclusive;
/*     */   
/*     */   private int toInclusive;
/*     */   
/*     */   public PreparedGroup(int featureGroupIndex, FeatureRendererType<Submit> featureType, boolean strictlyOrdered, int fromInclusive, int toInclusive) {
/* 304 */     this.featureGroupIndex = featureGroupIndex;
/* 305 */     this.featureType = featureType;
/* 306 */     this.strictlyOrdered = strictlyOrdered;
/* 307 */     this.fromInclusive = fromInclusive;
/* 308 */     this.toInclusive = toInclusive;
/*     */   }
/*     */   
/*     */   public void prepare(FeatureFrameContext context, FeatureRendererMap featureRenderers, List<SubmitNode> submits) {
/* 312 */     FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
/* 313 */     featureRenderer.prepareGroup(context, sliceUnchecked(submits), this.strictlyOrdered);
/*     */   }
/*     */   
/*     */   public void execute(FeatureFrameContext context, FeatureRendererMap featureRenderers, List<SubmitNode> submits) {
/* 317 */     FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
/* 318 */     featureRenderer.executeGroup(context, this.featureGroupIndex, sliceUnchecked(submits), this.strictlyOrdered);
/*     */   }
/*     */   
/*     */   private List<Submit> sliceUnchecked(List<SubmitNode> submits) {
/* 323 */     return (List)submits.subList(this.fromInclusive, this.toInclusive + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRenderDispatcher$PreparedGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */