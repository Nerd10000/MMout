/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ 
/*     */ class PhaseSubmitGrouper implements FeatureRenderPhase.Output {
/*     */   private final FeatureRenderDispatcher.PreparedFrame frame;
/*     */   
/*     */   private final List<SubmitNode> allSubmits;
/*     */   
/*     */   private final List<FeatureRenderDispatcher.PreparedGroup<?>> phaseGroups;
/*     */   
/*     */   private FeatureRenderDispatcher.PreparedGroup<?> lastGroup;
/*     */   
/*     */   public PhaseSubmitGrouper(FeatureRenderDispatcher.PreparedFrame frame, FeatureRenderPhase<?> phase) {
/* 256 */     this.frame = frame;
/* 257 */     this.allSubmits = frame.allSubmits;
/* 258 */     this.phaseGroups = frame.groupsByPhase.computeIfAbsent(phase, paramFeatureRenderPhase -> new ArrayList());
/*     */   }
/*     */   
/*     */   public void accept(SubmitNode submit, boolean strictlyOrdered) {
/* 263 */     int index = this.allSubmits.size();
/* 264 */     this.allSubmits.add(submit);
/* 265 */     addOrExtendGroup(submit.featureType(), strictlyOrdered, index, index);
/*     */   }
/*     */   
/*     */   public <Submit extends SubmitNode> void acceptFeatureGroup(FeatureRendererType<Submit> featureType, Collection<Submit> submits, boolean strictlyOrdered) {
/* 270 */     if (!submits.isEmpty()) {
/* 271 */       for (SubmitNode submitNode : submits) {
/* 272 */         if (submitNode.featureType() != featureType)
/* 273 */           throw new IllegalArgumentException(String.valueOf(submitNode) + " was not of feature type " + String.valueOf(submitNode)); 
/*     */       } 
/* 276 */       int fromInclusive = this.allSubmits.size();
/* 277 */       this.allSubmits.addAll(submits);
/* 278 */       int toInclusive = this.allSubmits.size() - 1;
/* 279 */       addOrExtendGroup(featureType, strictlyOrdered, fromInclusive, toInclusive);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <Submit extends SubmitNode> void addOrExtendGroup(FeatureRendererType<Submit> featureType, boolean strictlyOrdered, int fromInclusive, int toInclusive) {
/* 284 */     if (this.lastGroup != null && this.lastGroup.featureType == featureType && this.lastGroup.strictlyOrdered == strictlyOrdered) {
/* 285 */       this.lastGroup.toInclusive = toInclusive;
/*     */       return;
/*     */     } 
/* 288 */     List<FeatureRenderDispatcher.PreparedGroup<?>> featureGroups = this.frame.groupsByFeature.computeIfAbsent(featureType, paramFeatureRendererType -> new ArrayList());
/* 289 */     FeatureRenderDispatcher.PreparedGroup<Submit> group = new FeatureRenderDispatcher.PreparedGroup<>(featureGroups.size(), featureType, strictlyOrdered, fromInclusive, toInclusive);
/* 290 */     this.phaseGroups.add(group);
/* 291 */     featureGroups.add(group);
/* 292 */     this.lastGroup = group;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRenderDispatcher$PhaseSubmitGrouper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */