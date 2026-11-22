/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*     */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*     */ import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ public class PreparedFrame implements AutoCloseable {
/*     */   private FeatureFrameContext context;
/*     */   
/*     */   private SubmitNodeStorage submitNodeStorage;
/*     */   
/* 125 */   private final List<SubmitNode> allSubmits = new ArrayList<>();
/*     */   
/* 126 */   private final Map<FeatureRenderPhase<?>, List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsByPhase = new IdentityHashMap<>();
/*     */   
/* 127 */   private final Map<FeatureRendererType<?>, List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsByFeature = new IdentityHashMap<>();
/*     */   
/*     */   private PreparedFrame begin(FeatureFrameContext context, SubmitNodeStorage submitNodeStorage) {
/* 130 */     if (this.context != null)
/* 131 */       throw new IllegalStateException("PreparedFrame already in use"); 
/* 133 */     this.context = context;
/* 134 */     this.submitNodeStorage = submitNodeStorage;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public void executeSolid() {
/* 139 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 140 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 141 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 141 */       SubmitNodeCollection collection = objectIterator.next();
/* 142 */       executePhase((FeatureRenderPhase<?>)collection.solid, context);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeTranslucent() {
/* 147 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 148 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/*     */     ObjectIterator<SubmitNodeCollection> objectIterator;
/* 149 */     for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 149 */       SubmitNodeCollection collection = objectIterator.next();
/* 150 */       executePhase((FeatureRenderPhase<?>)collection.shadows, context);
/* 151 */       executePhase((FeatureRenderPhase<?>)collection.translucentModels, context);
/* 152 */       executePhase((FeatureRenderPhase<?>)collection.seeThroughNameTags, context);
/* 153 */       executePhase((FeatureRenderPhase<?>)collection.nameTags, context);
/* 154 */       executePhase((FeatureRenderPhase<?>)collection.texts, context);
/* 155 */       executePhase((FeatureRenderPhase<?>)collection.translucentCustomGeometry, context);
/*     */     } 
/* 157 */     for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 157 */       SubmitNodeCollection collection = objectIterator.next();
/* 158 */       executePhase((FeatureRenderPhase<?>)collection.shapeOutlines, context);
/* 159 */       executePhase((FeatureRenderPhase<?>)collection.gizmos, context);
/*     */     } 
/* 161 */     for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 161 */       SubmitNodeCollection collection = objectIterator.next();
/* 162 */       executePhase((FeatureRenderPhase<?>)collection.translucentBlocksAndItems, context);
/* 163 */       executePhase((FeatureRenderPhase<?>)collection.breakingOverlay, context);
/* 164 */       executePhase((FeatureRenderPhase<?>)collection.waterMask, context);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeOutline() {
/* 169 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 170 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 171 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 171 */       SubmitNodeCollection collection = objectIterator.next();
/* 172 */       executePhase((FeatureRenderPhase<?>)collection.outline, context);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeTranslucentAfterTerrain() {
/* 177 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 178 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 179 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 179 */       SubmitNodeCollection collection = objectIterator.next();
/* 180 */       executePhase((FeatureRenderPhase<?>)collection.afterTerrain, context);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeAlwaysOnTop() {
/* 185 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 186 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 187 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 187 */       SubmitNodeCollection collection = objectIterator.next();
/* 188 */       executePhase((FeatureRenderPhase<?>)collection.alwaysOnTop, context);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executePhase(FeatureRenderPhase<?> phase, FeatureFrameContext context) {
/* 193 */     ProfilerFiller profiler = Profiler.get();
/* 194 */     for (FeatureRenderDispatcher.PreparedGroup<?> group : this.groupsByPhase.getOrDefault(phase, List.of())) {
/* 195 */       profiler.push(group.featureType.toString());
/* 196 */       group.execute(context, FeatureRenderDispatcher.this.featureRenderers, this.allSubmits);
/* 197 */       profiler.pop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasAnyAlwaysOnTop() {
/* 202 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 203 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 203 */       SubmitNodeCollection collection = objectIterator.next();
/* 204 */       if (!((List)this.groupsByPhase.getOrDefault(collection.alwaysOnTop, List.of())).isEmpty())
/* 205 */         return true; 
/*     */     } 
/* 208 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasAnyOutline() {
/* 212 */     SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 213 */     for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 213 */       SubmitNodeCollection collection = objectIterator.next();
/* 214 */       if (!((List)this.groupsByPhase.getOrDefault(collection.outline, List.of())).isEmpty())
/* 215 */         return true; 
/*     */     } 
/* 218 */     return false;
/*     */   }
/*     */   
/*     */   public void close() {
/* 223 */     FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context, "Frame not in use");
/* 224 */     this.context = null;
/* 225 */     this.submitNodeStorage = null;
/* 227 */     for (FeatureRenderer<?> featureRenderer : FeatureRenderDispatcher.this.featureRenderers.values())
/* 228 */       featureRenderer.finishExecute(context); 
/* 230 */     FeatureRenderDispatcher.this.stagedVertexBuffer.endDraw();
/* 232 */     this.allSubmits.clear();
/* 233 */     clearGroups(this.groupsByPhase.values());
/* 234 */     clearGroups(this.groupsByFeature.values());
/*     */   }
/*     */   
/*     */   private static void clearGroups(Collection<List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsSet) {
/* 238 */     groupsSet.removeIf(groups -> {
/*     */           if (groups.isEmpty())
/*     */             return true; 
/*     */           groups.clear();
/*     */           return false;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRenderDispatcher$PreparedFrame.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */