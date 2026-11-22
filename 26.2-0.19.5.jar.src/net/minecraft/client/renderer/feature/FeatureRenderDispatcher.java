/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.RenderBuffers;
/*     */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*     */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*     */ import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.state.GameRenderState;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ public class FeatureRenderDispatcher implements AutoCloseable {
/*     */   private final ModelManager modelManager;
/*     */   
/*     */   private final AtlasManager atlasManager;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final GameRenderState gameRenderState;
/*     */   
/*     */   private final StagedVertexBuffer stagedVertexBuffer;
/*     */   
/*  32 */   private final FeatureRendererMap featureRenderers = new FeatureRendererMap();
/*     */   
/*  34 */   private final PreparedFrame preparedFrame = new PreparedFrame();
/*     */   
/*     */   public FeatureRenderDispatcher(RenderBuffers renderBuffers, ModelManager modelManager, AtlasManager atlasManager, Font font, GameRenderState gameRenderState) {
/*  37 */     this.modelManager = modelManager;
/*  38 */     this.atlasManager = atlasManager;
/*  39 */     this.font = font;
/*  40 */     this.gameRenderState = gameRenderState;
/*  41 */     this.stagedVertexBuffer = renderBuffers.stagedVertexBuffer();
/*  43 */     this.featureRenderers.put(ShadowFeatureRenderer.TYPE, new ShadowFeatureRenderer());
/*  44 */     this.featureRenderers.put(FlameFeatureRenderer.TYPE, new FlameFeatureRenderer());
/*  45 */     this.featureRenderers.put(ModelFeatureRenderer.TYPE, new ModelFeatureRenderer());
/*  46 */     this.featureRenderers.put(NameTagFeatureRenderer.TYPE, new NameTagFeatureRenderer());
/*  47 */     this.featureRenderers.put(TextFeatureRenderer.TYPE, new TextFeatureRenderer());
/*  48 */     this.featureRenderers.put(LeashFeatureRenderer.TYPE, new LeashFeatureRenderer());
/*  49 */     this.featureRenderers.put(ItemFeatureRenderer.TYPE, new ItemFeatureRenderer());
/*  50 */     this.featureRenderers.put(CustomFeatureRenderer.TYPE, new CustomFeatureRenderer());
/*  51 */     this.featureRenderers.put(BlockModelFeatureRenderer.TYPE, new BlockModelFeatureRenderer());
/*  52 */     this.featureRenderers.put(MovingBlockFeatureRenderer.TYPE, new MovingBlockFeatureRenderer());
/*  53 */     this.featureRenderers.put(QuadParticleFeatureRenderer.TYPE, new QuadParticleFeatureRenderer());
/*  54 */     this.featureRenderers.put(ShapeOutlineFeatureRenderer.TYPE, new ShapeOutlineFeatureRenderer());
/*  55 */     this.featureRenderers.put(GizmoFeatureRenderer.TYPE, new GizmoFeatureRenderer());
/*     */   }
/*     */   
/*     */   public PreparedFrame prepareFrame(SubmitNodeStorage submitNodeStorage) {
/*  59 */     Minecraft minecraft = Minecraft.getInstance();
/*  60 */     return prepareFrameWithContext(new FeatureFrameContext(this.gameRenderState.optionsRenderState, this.font, this.modelManager
/*     */           
/*  63 */           .getBlockStateModelSet(), minecraft
/*  64 */           .getBlockColors(), minecraft
/*  65 */           .getTextureManager(), this.atlasManager, minecraft.gameRenderer
/*     */           
/*  67 */           .lightmap(), this.stagedVertexBuffer), submitNodeStorage);
/*     */   }
/*     */   
/*     */   private PreparedFrame prepareFrameWithContext(FeatureFrameContext context, SubmitNodeStorage submitNodeStorage) {
/*  73 */     PreparedFrame frame = this.preparedFrame.begin(context, submitNodeStorage);
/*  75 */     ProfilerFiller profiler = Profiler.get();
/*  76 */     profiler.push("sort");
/*  77 */     submitNodeStorage.drainPhases(phase -> phase.sortInto(new PhaseSubmitGrouper(frame, phase)));
/*  81 */     profiler.popPush("beginPrepare");
/*  82 */     for (FeatureRenderer<?> renderer : this.featureRenderers.values())
/*  83 */       renderer.beginPrepare(context); 
/*  86 */     profiler.popPush("prepare");
/*  88 */     for (Map.Entry<FeatureRendererType<?>, List<PreparedGroup<?>>> entry : frame.groupsByFeature.entrySet()) {
/*  89 */       profiler.push(((FeatureRendererType)entry.getKey()).toString());
/*  90 */       for (PreparedGroup<?> group : entry.getValue())
/*  91 */         group.prepare(context, this.featureRenderers, frame.allSubmits); 
/*  93 */       profiler.pop();
/*     */     } 
/*  96 */     profiler.popPush("finishPrepare");
/*  97 */     for (FeatureRenderer<?> renderer : this.featureRenderers.values())
/*  98 */       renderer.finishPrepare(context); 
/* 101 */     profiler.popPush("uploadSharedVertexBuffer");
/* 102 */     this.stagedVertexBuffer.upload();
/* 103 */     profiler.pop();
/* 105 */     return frame;
/*     */   }
/*     */   
/*     */   public void renderAllFeatures(SubmitNodeStorage submitNodeStorage) {
/* 109 */     PreparedFrame frame = prepareFrame(submitNodeStorage);
/*     */     try {
/* 110 */       frame.executeSolid();
/* 111 */       frame.executeTranslucent();
/* 112 */       frame.executeTranslucentAfterTerrain();
/* 113 */       frame.executeAlwaysOnTop();
/* 114 */       if (frame != null)
/* 114 */         frame.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (frame != null)
/*     */         try {
/*     */           frame.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 119 */     this.featureRenderers.close();
/*     */   }
/*     */   
/*     */   public class PreparedFrame implements AutoCloseable {
/*     */     private FeatureFrameContext context;
/*     */     
/*     */     private SubmitNodeStorage submitNodeStorage;
/*     */     
/* 125 */     private final List<SubmitNode> allSubmits = new ArrayList<>();
/*     */     
/* 126 */     private final Map<FeatureRenderPhase<?>, List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsByPhase = new IdentityHashMap<>();
/*     */     
/* 127 */     private final Map<FeatureRendererType<?>, List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsByFeature = new IdentityHashMap<>();
/*     */     
/*     */     private PreparedFrame begin(FeatureFrameContext context, SubmitNodeStorage submitNodeStorage) {
/* 130 */       if (this.context != null)
/* 131 */         throw new IllegalStateException("PreparedFrame already in use"); 
/* 133 */       this.context = context;
/* 134 */       this.submitNodeStorage = submitNodeStorage;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public void executeSolid() {
/* 139 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 140 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 141 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 141 */         SubmitNodeCollection collection = objectIterator.next();
/* 142 */         executePhase((FeatureRenderPhase<?>)collection.solid, context);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void executeTranslucent() {
/* 147 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 148 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/*     */       ObjectIterator<SubmitNodeCollection> objectIterator;
/* 149 */       for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 149 */         SubmitNodeCollection collection = objectIterator.next();
/* 150 */         executePhase((FeatureRenderPhase<?>)collection.shadows, context);
/* 151 */         executePhase((FeatureRenderPhase<?>)collection.translucentModels, context);
/* 152 */         executePhase((FeatureRenderPhase<?>)collection.seeThroughNameTags, context);
/* 153 */         executePhase((FeatureRenderPhase<?>)collection.nameTags, context);
/* 154 */         executePhase((FeatureRenderPhase<?>)collection.texts, context);
/* 155 */         executePhase((FeatureRenderPhase<?>)collection.translucentCustomGeometry, context);
/*     */       } 
/* 157 */       for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 157 */         SubmitNodeCollection collection = objectIterator.next();
/* 158 */         executePhase((FeatureRenderPhase<?>)collection.shapeOutlines, context);
/* 159 */         executePhase((FeatureRenderPhase<?>)collection.gizmos, context);
/*     */       } 
/* 161 */       for (objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 161 */         SubmitNodeCollection collection = objectIterator.next();
/* 162 */         executePhase((FeatureRenderPhase<?>)collection.translucentBlocksAndItems, context);
/* 163 */         executePhase((FeatureRenderPhase<?>)collection.breakingOverlay, context);
/* 164 */         executePhase((FeatureRenderPhase<?>)collection.waterMask, context);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void executeOutline() {
/* 169 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 170 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 171 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 171 */         SubmitNodeCollection collection = objectIterator.next();
/* 172 */         executePhase((FeatureRenderPhase<?>)collection.outline, context);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void executeTranslucentAfterTerrain() {
/* 177 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 178 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 179 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 179 */         SubmitNodeCollection collection = objectIterator.next();
/* 180 */         executePhase((FeatureRenderPhase<?>)collection.afterTerrain, context);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void executeAlwaysOnTop() {
/* 185 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context);
/* 186 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 187 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 187 */         SubmitNodeCollection collection = objectIterator.next();
/* 188 */         executePhase((FeatureRenderPhase<?>)collection.alwaysOnTop, context);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void executePhase(FeatureRenderPhase<?> phase, FeatureFrameContext context) {
/* 193 */       ProfilerFiller profiler = Profiler.get();
/* 194 */       for (FeatureRenderDispatcher.PreparedGroup<?> group : this.groupsByPhase.getOrDefault(phase, List.of())) {
/* 195 */         profiler.push(group.featureType.toString());
/* 196 */         group.execute(context, FeatureRenderDispatcher.this.featureRenderers, this.allSubmits);
/* 197 */         profiler.pop();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasAnyAlwaysOnTop() {
/* 202 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 203 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 203 */         SubmitNodeCollection collection = objectIterator.next();
/* 204 */         if (!((List)this.groupsByPhase.getOrDefault(collection.alwaysOnTop, List.of())).isEmpty())
/* 205 */           return true; 
/*     */       } 
/* 208 */       return false;
/*     */     }
/*     */     
/*     */     public boolean hasAnyOutline() {
/* 212 */       SubmitNodeStorage submitNodeStorage = Objects.<SubmitNodeStorage>requireNonNull(this.submitNodeStorage);
/* 213 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 213 */         SubmitNodeCollection collection = objectIterator.next();
/* 214 */         if (!((List)this.groupsByPhase.getOrDefault(collection.outline, List.of())).isEmpty())
/* 215 */           return true; 
/*     */       } 
/* 218 */       return false;
/*     */     }
/*     */     
/*     */     public void close() {
/* 223 */       FeatureFrameContext context = Objects.<FeatureFrameContext>requireNonNull(this.context, "Frame not in use");
/* 224 */       this.context = null;
/* 225 */       this.submitNodeStorage = null;
/* 227 */       for (FeatureRenderer<?> featureRenderer : FeatureRenderDispatcher.this.featureRenderers.values())
/* 228 */         featureRenderer.finishExecute(context); 
/* 230 */       FeatureRenderDispatcher.this.stagedVertexBuffer.endDraw();
/* 232 */       this.allSubmits.clear();
/* 233 */       clearGroups(this.groupsByPhase.values());
/* 234 */       clearGroups(this.groupsByFeature.values());
/*     */     }
/*     */     
/*     */     private static void clearGroups(Collection<List<FeatureRenderDispatcher.PreparedGroup<?>>> groupsSet) {
/* 238 */       groupsSet.removeIf(groups -> {
/*     */             if (groups.isEmpty())
/*     */               return true; 
/*     */             groups.clear();
/*     */             return false;
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PhaseSubmitGrouper implements FeatureRenderPhase.Output {
/*     */     private final FeatureRenderDispatcher.PreparedFrame frame;
/*     */     
/*     */     private final List<SubmitNode> allSubmits;
/*     */     
/*     */     private final List<FeatureRenderDispatcher.PreparedGroup<?>> phaseGroups;
/*     */     
/*     */     private FeatureRenderDispatcher.PreparedGroup<?> lastGroup;
/*     */     
/*     */     public PhaseSubmitGrouper(FeatureRenderDispatcher.PreparedFrame frame, FeatureRenderPhase<?> phase) {
/* 256 */       this.frame = frame;
/* 257 */       this.allSubmits = frame.allSubmits;
/* 258 */       this.phaseGroups = frame.groupsByPhase.computeIfAbsent(phase, param1FeatureRenderPhase -> new ArrayList());
/*     */     }
/*     */     
/*     */     public void accept(SubmitNode submit, boolean strictlyOrdered) {
/* 263 */       int index = this.allSubmits.size();
/* 264 */       this.allSubmits.add(submit);
/* 265 */       addOrExtendGroup(submit.featureType(), strictlyOrdered, index, index);
/*     */     }
/*     */     
/*     */     public <Submit extends SubmitNode> void acceptFeatureGroup(FeatureRendererType<Submit> featureType, Collection<Submit> submits, boolean strictlyOrdered) {
/* 270 */       if (!submits.isEmpty()) {
/* 271 */         for (SubmitNode submitNode : submits) {
/* 272 */           if (submitNode.featureType() != featureType)
/* 273 */             throw new IllegalArgumentException(String.valueOf(submitNode) + " was not of feature type " + String.valueOf(submitNode)); 
/*     */         } 
/* 276 */         int fromInclusive = this.allSubmits.size();
/* 277 */         this.allSubmits.addAll(submits);
/* 278 */         int toInclusive = this.allSubmits.size() - 1;
/* 279 */         addOrExtendGroup(featureType, strictlyOrdered, fromInclusive, toInclusive);
/*     */       } 
/*     */     }
/*     */     
/*     */     private <Submit extends SubmitNode> void addOrExtendGroup(FeatureRendererType<Submit> featureType, boolean strictlyOrdered, int fromInclusive, int toInclusive) {
/* 284 */       if (this.lastGroup != null && this.lastGroup.featureType == featureType && this.lastGroup.strictlyOrdered == strictlyOrdered) {
/* 285 */         this.lastGroup.toInclusive = toInclusive;
/*     */         return;
/*     */       } 
/* 288 */       List<FeatureRenderDispatcher.PreparedGroup<?>> featureGroups = this.frame.groupsByFeature.computeIfAbsent(featureType, param1FeatureRendererType -> new ArrayList());
/* 289 */       FeatureRenderDispatcher.PreparedGroup<Submit> group = new FeatureRenderDispatcher.PreparedGroup<>(featureGroups.size(), featureType, strictlyOrdered, fromInclusive, toInclusive);
/* 290 */       this.phaseGroups.add(group);
/* 291 */       featureGroups.add(group);
/* 292 */       this.lastGroup = group;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PreparedGroup<Submit extends SubmitNode> {
/*     */     private final int featureGroupIndex;
/*     */     
/*     */     private final FeatureRendererType<Submit> featureType;
/*     */     
/*     */     private final boolean strictlyOrdered;
/*     */     
/*     */     private final int fromInclusive;
/*     */     
/*     */     private int toInclusive;
/*     */     
/*     */     public PreparedGroup(int featureGroupIndex, FeatureRendererType<Submit> featureType, boolean strictlyOrdered, int fromInclusive, int toInclusive) {
/* 304 */       this.featureGroupIndex = featureGroupIndex;
/* 305 */       this.featureType = featureType;
/* 306 */       this.strictlyOrdered = strictlyOrdered;
/* 307 */       this.fromInclusive = fromInclusive;
/* 308 */       this.toInclusive = toInclusive;
/*     */     }
/*     */     
/*     */     public void prepare(FeatureFrameContext context, FeatureRendererMap featureRenderers, List<SubmitNode> submits) {
/* 312 */       FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
/* 313 */       featureRenderer.prepareGroup(context, sliceUnchecked(submits), this.strictlyOrdered);
/*     */     }
/*     */     
/*     */     public void execute(FeatureFrameContext context, FeatureRendererMap featureRenderers, List<SubmitNode> submits) {
/* 317 */       FeatureRenderer<Submit> featureRenderer = featureRenderers.getOrThrow(this.featureType);
/* 318 */       featureRenderer.executeGroup(context, this.featureGroupIndex, sliceUnchecked(submits), this.strictlyOrdered);
/*     */     }
/*     */     
/*     */     private List<Submit> sliceUnchecked(List<SubmitNode> submits) {
/* 323 */       return (List)submits.subList(this.fromInclusive, this.toInclusive + 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRenderDispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */