/*     */ package net.minecraft.client.renderer.feature.phase;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.feature.FeatureRendererType;
/*     */ import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ 
/*     */ class FeatureSubmits<Submit extends SubmitNode> {
/*     */   private final FeatureRendererType<Submit> featureType;
/*     */   
/* 100 */   private final List<Submit> unbatched = new ArrayList<>();
/*     */   
/* 101 */   private final Map<Object, List<Submit>> batches = new HashMap<>();
/*     */   
/*     */   private FeatureSubmits(FeatureRendererType<Submit> featureType) {
/* 104 */     this.featureType = featureType;
/*     */   }
/*     */   
/*     */   public void addUnchecked(SubmitNode submit) {
/* 109 */     add((Submit)submit);
/*     */   }
/*     */   
/*     */   public void add(Submit submit) {
/* 113 */     Object key = batchKey((SubmitNode)submit);
/* 114 */     if (key == null) {
/* 115 */       this.unbatched.add(submit);
/*     */     } else {
/* 117 */       ((List<Submit>)this.batches.computeIfAbsent(key, paramObject -> new ArrayList())).add(submit);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object batchKey(SubmitNode submit) {
/* 122 */     if (submit instanceof BatchableSubmit) {
/* 122 */       BatchableSubmit batchable = (BatchableSubmit)submit;
/* 123 */       return batchable.batchKey();
/*     */     } 
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 129 */     if (!this.unbatched.isEmpty())
/* 130 */       return false; 
/* 132 */     for (List<Submit> submits : this.batches.values()) {
/* 133 */       if (!submits.isEmpty())
/* 134 */         return false; 
/*     */     } 
/* 137 */     return true;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 141 */     this.unbatched.clear();
/* 142 */     this.batches.values().removeIf(submits -> {
/*     */           if (submits.isEmpty())
/*     */             return true; 
/*     */           submits.clear();
/*     */           return false;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\phase\SimpleFeatureRenderPhase$FeatureSubmits.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */