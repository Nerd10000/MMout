/*     */ package net.minecraft.client.renderer.feature.phase;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.feature.FeatureRendererType;
/*     */ import net.minecraft.client.renderer.feature.submit.BatchableSubmit;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ public class SimpleFeatureRenderPhase implements FeatureRenderPhase<SubmitNode> {
/*  21 */   private FeatureSubmits<?>[] submitsByFeature = (FeatureSubmits<?>[])new FeatureSubmits[0];
/*     */   
/*     */   public void submit(SubmitNode submit) {
/*  25 */     FeatureRendererType<? extends SubmitNode> type = submit.featureType();
/*  26 */     if (this.submitsByFeature.length <= type.id())
/*  27 */       this.submitsByFeature = (FeatureSubmits<?>[])Arrays.<FeatureSubmits>copyOf((FeatureSubmits[])this.submitsByFeature, Mth.roundToward(type.id() + 1, 16)); 
/*  29 */     FeatureSubmits<?> submits = this.submitsByFeature[type.id()];
/*  30 */     if (submits == null) {
/*  31 */       submits = new FeatureSubmits(type);
/*  32 */       this.submitsByFeature[type.id()] = submits;
/*     */     } 
/*  34 */     submits.addUnchecked(submit);
/*     */   }
/*     */   
/*     */   public void sortInto(FeatureRenderPhase.Output output) {
/*  39 */     for (FeatureSubmits<?> submits : (FeatureSubmits[])maybeShuffle((FeatureSubmits[])this.submitsByFeature)) {
/*  40 */       if (submits != null)
/*  41 */         sortFeatureInto(output, submits); 
/*     */     } 
/*  44 */     clear();
/*     */   }
/*     */   
/*     */   private static <Submit extends SubmitNode> void sortFeatureInto(FeatureRenderPhase.Output output, FeatureSubmits<Submit> submits) {
/*  49 */     output.acceptFeatureGroup(submits.featureType, maybeShuffle(submits.unbatched), false);
/*  50 */     for (List<Submit> batch : maybeShuffle(submits.batches.values()))
/*  51 */       output.acceptFeatureGroup(submits.featureType, maybeShuffle(batch), false); 
/*     */   }
/*     */   
/*     */   private static <V> Collection<V> maybeShuffle(Collection<V> collection) {
/*  56 */     if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
/*  57 */       List<V> shuffled = new ArrayList<>(collection);
/*  58 */       Collections.shuffle(shuffled);
/*  59 */       return shuffled;
/*     */     } 
/*  61 */     return collection;
/*     */   }
/*     */   
/*     */   private static <V> V[] maybeShuffle(V[] array) {
/*  66 */     if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
/*  67 */       V[] shuffled = Arrays.copyOf(array, array.length);
/*  68 */       ArrayUtils.shuffle((Object[])shuffled);
/*  69 */       return shuffled;
/*     */     } 
/*  71 */     return array;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  76 */     for (int i = 0; i < this.submitsByFeature.length; i++) {
/*  77 */       FeatureSubmits<?> submits = this.submitsByFeature[i];
/*  78 */       if (submits != null)
/*  79 */         if (submits.isEmpty()) {
/*  80 */           this.submitsByFeature[i] = null;
/*     */         } else {
/*  82 */           submits.clear();
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  90 */     for (FeatureSubmits<?> submits : this.submitsByFeature) {
/*  91 */       if (submits != null && !submits.isEmpty())
/*  92 */         return false; 
/*     */     } 
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   private static class FeatureSubmits<Submit extends SubmitNode> {
/*     */     private final FeatureRendererType<Submit> featureType;
/*     */     
/* 100 */     private final List<Submit> unbatched = new ArrayList<>();
/*     */     
/* 101 */     private final Map<Object, List<Submit>> batches = new HashMap<>();
/*     */     
/*     */     private FeatureSubmits(FeatureRendererType<Submit> featureType) {
/* 104 */       this.featureType = featureType;
/*     */     }
/*     */     
/*     */     public void addUnchecked(SubmitNode submit) {
/* 109 */       add((Submit)submit);
/*     */     }
/*     */     
/*     */     public void add(Submit submit) {
/* 113 */       Object key = batchKey((SubmitNode)submit);
/* 114 */       if (key == null) {
/* 115 */         this.unbatched.add(submit);
/*     */       } else {
/* 117 */         ((List<Submit>)this.batches.computeIfAbsent(key, param1Object -> new ArrayList())).add(submit);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Object batchKey(SubmitNode submit) {
/* 122 */       if (submit instanceof BatchableSubmit) {
/* 122 */         BatchableSubmit batchable = (BatchableSubmit)submit;
/* 123 */         return batchable.batchKey();
/*     */       } 
/* 125 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 129 */       if (!this.unbatched.isEmpty())
/* 130 */         return false; 
/* 132 */       for (List<Submit> submits : this.batches.values()) {
/* 133 */         if (!submits.isEmpty())
/* 134 */           return false; 
/*     */       } 
/* 137 */       return true;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 141 */       this.unbatched.clear();
/* 142 */       this.batches.values().removeIf(submits -> {
/*     */             if (submits.isEmpty())
/*     */               return true; 
/*     */             submits.clear();
/*     */             return false;
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\phase\SimpleFeatureRenderPhase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */