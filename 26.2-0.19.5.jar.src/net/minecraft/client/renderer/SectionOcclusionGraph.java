/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.state.level.ChunkLoadingRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ChunkTrackingView;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3d;
/*     */ import org.joml.Vector3dc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SectionOcclusionGraph {
/*     */   private static final int HALF_SECTION_SIZE = 8;
/*     */   
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  49 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private static final int MINIMUM_ADVANCED_CULLING_DISTANCE = 60;
/*     */   
/*  52 */   private static final int MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE = SectionPos.blockToSectionCoord(60);
/*     */   
/*  54 */   private static final double CEILED_SECTION_DIAGONAL = Math.ceil(Math.sqrt(3.0D) * 16.0D);
/*     */   
/*     */   private boolean needsFullUpdate = true;
/*     */   
/*     */   private Future<?> fullUpdateTask;
/*     */   
/*     */   private ViewArea viewArea;
/*     */   
/*  60 */   private final AtomicReference<GraphState> currentGraph = new AtomicReference<>();
/*     */   
/*  62 */   private final AtomicBoolean needsFrustumUpdate = new AtomicBoolean(false);
/*     */   
/*  64 */   private final LongOpenHashSet emptySections = new LongOpenHashSet();
/*     */   
/*  65 */   private final LongOpenHashSet loadedChunks = new LongOpenHashSet();
/*     */   
/*     */   private volatile BlockingQueue<SectionRenderDispatcher.RenderSection> nextSectionsToPropagateFrom;
/*     */   
/*  68 */   private double prevCamX = Double.MIN_VALUE;
/*     */   
/*  69 */   private double prevCamY = Double.MIN_VALUE;
/*     */   
/*  70 */   private double prevCamZ = Double.MIN_VALUE;
/*     */   
/*  71 */   private int prevFov = Integer.MAX_VALUE;
/*     */   
/*     */   private boolean lastSmartCull = true;
/*     */   
/*     */   public void waitAndReset(ViewArea viewArea) {
/*  75 */     if (this.fullUpdateTask != null)
/*     */       try {
/*  77 */         this.fullUpdateTask.get();
/*  78 */         this.fullUpdateTask = null;
/*  79 */       } catch (Exception e) {
/*  80 */         LOGGER.warn("Full update failed", e);
/*     */       }  
/*  83 */     this.viewArea = viewArea;
/*  84 */     if (viewArea != null) {
/*  85 */       this.currentGraph.set(new GraphState(viewArea));
/*  86 */       invalidate();
/*     */     } else {
/*  88 */       this.currentGraph.set(null);
/*  89 */       this.emptySections.clear();
/*  90 */       this.loadedChunks.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public LongCollection expectedChunks() {
/*  95 */     GraphState graphState = this.currentGraph.get();
/*  96 */     return (graphState != null) ? (LongCollection)graphState.storage.sectionsWaitingForChunkLoads.keySet() : (LongCollection)LongSets.EMPTY_SET;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 100 */     this.needsFullUpdate = true;
/*     */   }
/*     */   
/*     */   public void invalidateIfNeeded(CameraRenderState camera, int fov) {
/* 104 */     Vec3 cameraPos = camera.pos;
/* 105 */     double camX = Math.floor(cameraPos.x / 8.0D);
/* 106 */     double camY = Math.floor(cameraPos.y / 8.0D);
/* 107 */     double camZ = Math.floor(cameraPos.z / 8.0D);
/* 109 */     if (camX != this.prevCamX || camY != this.prevCamY || camZ != this.prevCamZ || this.prevFov != fov || this.lastSmartCull != camera.smartCull)
/* 110 */       invalidate(); 
/* 113 */     this.prevCamX = camX;
/* 114 */     this.prevCamY = camY;
/* 115 */     this.prevCamZ = camZ;
/* 116 */     this.prevFov = fov;
/* 117 */     this.lastSmartCull = camera.smartCull;
/*     */   }
/*     */   
/*     */   public void addSectionsInFrustum(Frustum frustum, List<SectionRenderDispatcher.RenderSection> visibleSections, List<SectionRenderDispatcher.RenderSection> nearbyVisibleSection) {
/* 121 */     Frustum offsetFrustum = offsetFrustum(frustum);
/* 122 */     (((GraphState)this.currentGraph.get()).storage()).sectionTree.visitNodes((node, fullyVisible, depth, isClose) -> {
/*     */           SectionRenderDispatcher.RenderSection renderSection = node.getSection();
/*     */           if (renderSection != null) {
/*     */             visibleSections.add(renderSection);
/*     */             if (isClose)
/*     */               nearbyVisibleSection.add(renderSection); 
/*     */           } 
/* 122 */         }offsetFrustum, 32);
/*     */   }
/*     */   
/*     */   public boolean consumeFrustumUpdate() {
/* 134 */     return this.needsFrustumUpdate.compareAndSet(true, false);
/*     */   }
/*     */   
/*     */   public void schedulePropagationFrom(SectionRenderDispatcher.RenderSection section) {
/* 138 */     BlockingQueue<SectionRenderDispatcher.RenderSection> nextSectionsToPropagateFrom = this.nextSectionsToPropagateFrom;
/* 139 */     if (nextSectionsToPropagateFrom != null)
/* 140 */       nextSectionsToPropagateFrom.add(section); 
/* 142 */     BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom = ((GraphState)this.currentGraph.get()).sectionsToPropagateFrom;
/* 144 */     if (sectionsToPropagateFrom != nextSectionsToPropagateFrom)
/* 145 */       sectionsToPropagateFrom.add(section); 
/*     */   }
/*     */   
/*     */   public void update(CameraRenderState camera, int fov, ChunkLoadingRenderState chunkLoadingRenderState) {
/* 150 */     updateLoadedChunks(chunkLoadingRenderState.addedLoadedChunks, chunkLoadingRenderState.removedLoadedChunks);
/* 151 */     updateEmptySections(chunkLoadingRenderState.addedEmptySections, chunkLoadingRenderState.removedEmptySections);
/* 153 */     if (!camera.isFrustumCaptured) {
/* 154 */       invalidateIfNeeded(camera, fov);
/* 156 */       if (this.needsFullUpdate && (this.fullUpdateTask == null || this.fullUpdateTask.isDone()))
/* 157 */         scheduleFullUpdate(camera); 
/* 159 */       runPartialUpdate(camera, (LongSet)chunkLoadingRenderState.loadedExpectedChunks);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleFullUpdate(CameraRenderState camera) {
/* 164 */     this.needsFullUpdate = false;
/* 165 */     LongOpenHashSet clonedEmptySections = this.emptySections.clone();
/* 166 */     LongOpenHashSet clonedLoadedChunks = this.loadedChunks.clone();
/* 167 */     this.fullUpdateTask = CompletableFuture.runAsync(() -> {
/*     */           GraphState newState = new GraphState(this.viewArea);
/*     */           this.nextSectionsToPropagateFrom = newState.sectionsToPropagateFrom;
/*     */           Queue<Node> queue = Queues.newArrayDeque();
/*     */           initializeQueueForFullUpdate(camera.blockPos, queue);
/*     */           queue.forEach(());
/*     */           runUpdates(newState.storage, camera.pos, queue, camera.smartCull, (), clonedEmptySections, clonedLoadedChunks);
/*     */           this.currentGraph.set(newState);
/*     */           this.nextSectionsToPropagateFrom = null;
/*     */           this.needsFrustumUpdate.set(true);
/* 177 */         }(Executor)Util.backgroundExecutor());
/*     */   }
/*     */   
/*     */   private void runPartialUpdate(CameraRenderState camera, LongSet loadedExpectedChunks) {
/* 181 */     GraphState state = this.currentGraph.get();
/* 182 */     loadedExpectedChunks.forEach(chunkNode -> {
/*     */           LongList waitingSections = (LongList)state.storage.sectionsWaitingForChunkLoads.remove(chunkNode);
/*     */           if (waitingSections != null)
/*     */             waitingSections.forEach(()); 
/*     */         });
/* 193 */     if (!state.sectionsToPropagateFrom.isEmpty()) {
/* 194 */       Queue<Node> queue = Queues.newArrayDeque();
/* 195 */       while (!state.sectionsToPropagateFrom.isEmpty()) {
/* 196 */         SectionRenderDispatcher.RenderSection renderSection = state.sectionsToPropagateFrom.poll();
/* 197 */         Node node = state.storage.sectionToNodeMap.get(renderSection);
/* 199 */         if (node != null && node.section == renderSection)
/* 200 */           queue.add(node); 
/*     */       } 
/* 203 */       Frustum offsetFrustum = offsetFrustum(camera.cullFrustum);
/* 204 */       Consumer<SectionRenderDispatcher.RenderSection> onSectionAdded = section -> {
/*     */           if (offsetFrustum.isVisible(section.getBoundingBox()))
/*     */             this.needsFrustumUpdate.set(true); 
/*     */         };
/* 209 */       runUpdates(state.storage, camera.pos, queue, camera.smartCull, onSectionAdded, this.emptySections, this.loadedChunks);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initializeQueueForFullUpdate(BlockPos cameraPosition, Queue<Node> queue) {
/* 214 */     long cameraSectionNode = SectionPos.asLong(cameraPosition);
/* 215 */     int cameraSectionY = SectionPos.y(cameraSectionNode);
/* 216 */     SectionRenderDispatcher.RenderSection cameraSection = this.viewArea.getRenderSection(cameraSectionNode);
/* 218 */     if (cameraSection == null) {
/* 220 */       boolean isBelowTheWorld = (cameraSectionY < this.viewArea.minSectionY());
/* 221 */       int sectionY = isBelowTheWorld ? this.viewArea.minSectionY() : this.viewArea.maxSectionY();
/* 223 */       int viewDistance = this.viewArea.getViewDistance();
/* 224 */       List<Node> toAdd = Lists.newArrayList();
/* 225 */       int cameraSectionX = SectionPos.x(cameraSectionNode);
/* 226 */       int cameraSectionZ = SectionPos.z(cameraSectionNode);
/* 227 */       for (int sectionX = -viewDistance; sectionX <= viewDistance; sectionX++) {
/* 228 */         for (int sectionZ = -viewDistance; sectionZ <= viewDistance; sectionZ++) {
/* 229 */           SectionRenderDispatcher.RenderSection renderSectionAt = this.viewArea.getRenderSection(SectionPos.asLong(sectionX + cameraSectionX, sectionY, sectionZ + cameraSectionZ));
/* 230 */           if (renderSectionAt != null && isInViewDistance(cameraSectionNode, renderSectionAt.getSectionNode())) {
/* 231 */             Direction sourceDirection = isBelowTheWorld ? Direction.UP : Direction.DOWN;
/* 232 */             Node node = new Node(renderSectionAt, sourceDirection, 0);
/* 233 */             node.setDirections(node.directions, sourceDirection);
/* 234 */             if (sectionX > 0) {
/* 235 */               node.setDirections(node.directions, Direction.EAST);
/* 236 */             } else if (sectionX < 0) {
/* 237 */               node.setDirections(node.directions, Direction.WEST);
/*     */             } 
/* 239 */             if (sectionZ > 0) {
/* 240 */               node.setDirections(node.directions, Direction.SOUTH);
/* 241 */             } else if (sectionZ < 0) {
/* 242 */               node.setDirections(node.directions, Direction.NORTH);
/*     */             } 
/* 244 */             toAdd.add(node);
/*     */           } 
/*     */         } 
/*     */       } 
/* 248 */       toAdd.sort(Comparator.comparingDouble(c -> cameraPosition.distSqr((Vec3i)SectionPos.of(c.section.getSectionNode()).center())));
/* 249 */       queue.addAll(toAdd);
/*     */     } else {
/* 251 */       queue.add(new Node(cameraSection, null, 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runUpdates(GraphStorage storage, Vec3 cameraPos, Queue<Node> queue, boolean smartCull, Consumer<SectionRenderDispatcher.RenderSection> onSectionAdded, LongOpenHashSet emptySections, LongOpenHashSet loadedChunks) {
/* 256 */     SectionPos cameraSectionPos = SectionPos.of((Position)cameraPos);
/* 257 */     long cameraSectionNode = cameraSectionPos.asLong();
/* 258 */     BlockPos cameraSectionCenter = cameraSectionPos.center();
/* 260 */     while (!queue.isEmpty()) {
/* 261 */       Node node = queue.poll();
/* 262 */       SectionRenderDispatcher.RenderSection currentSection = node.section;
/* 264 */       long sectionNode = currentSection.getSectionNode();
/* 265 */       long chunkNode = ChunkPos.fromSectionNode(sectionNode);
/* 267 */       if (!loadedChunks.contains(chunkNode)) {
/* 268 */         ((LongList)storage.sectionsWaitingForChunkLoads.computeIfAbsent(chunkNode, paramLong -> new LongArrayList())).add(sectionNode);
/*     */         continue;
/*     */       } 
/* 272 */       if (!emptySections.contains(node.section.getSectionNode())) {
/* 273 */         if (storage.sectionTree.add(node.section))
/* 274 */           onSectionAdded.accept(node.section); 
/*     */       } else {
/* 278 */         node.section.sectionMesh.compareAndSet(CompiledSectionMesh.UNCOMPILED, CompiledSectionMesh.EMPTY);
/*     */       } 
/* 284 */       boolean distantFromCamera = (Math.abs(SectionPos.x(sectionNode) - cameraSectionPos.x()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE || Math.abs(SectionPos.y(sectionNode) - cameraSectionPos.y()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE || Math.abs(SectionPos.z(sectionNode) - cameraSectionPos.z()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE);
/* 286 */       for (Direction direction : DIRECTIONS) {
/* 287 */         SectionRenderDispatcher.RenderSection renderSectionAt = getRelativeFrom(cameraSectionNode, currentSection, direction);
/* 289 */         if (renderSectionAt == null)
/*     */           continue; 
/* 293 */         if (smartCull && node.hasDirection(direction.getOpposite()))
/*     */           continue; 
/* 297 */         if (smartCull && node.hasSourceDirections()) {
/* 298 */           SectionMesh sectionMesh = currentSection.getSectionMesh();
/* 299 */           boolean visible = false;
/* 300 */           for (int i = 0; i < DIRECTIONS.length; i++) {
/* 301 */             if (node.hasSourceDirection(i) && sectionMesh.facesCanSeeEachother(DIRECTIONS[i].getOpposite(), direction)) {
/* 302 */               visible = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 307 */           if (!visible)
/*     */             continue; 
/*     */         } 
/* 312 */         if (smartCull && distantFromCamera) {
/* 313 */           int renderSectionOriginX = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
/* 314 */           int renderSectionOriginY = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 315 */           int renderSectionOriginZ = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
/* 316 */           boolean maxX = (direction.getAxis() == Direction.Axis.X) ? ((cameraSectionCenter.getX() > renderSectionOriginX)) : ((cameraSectionCenter.getX() < renderSectionOriginX));
/* 317 */           boolean maxY = (direction.getAxis() == Direction.Axis.Y) ? ((cameraSectionCenter.getY() > renderSectionOriginY)) : ((cameraSectionCenter.getY() < renderSectionOriginY));
/* 318 */           boolean maxZ = (direction.getAxis() == Direction.Axis.Z) ? ((cameraSectionCenter.getZ() > renderSectionOriginZ)) : ((cameraSectionCenter.getZ() < renderSectionOriginZ));
/* 322 */           Vector3d checkPos = new Vector3d((renderSectionOriginX + (maxX ? 16 : 0)), (renderSectionOriginY + (maxY ? 16 : 0)), (renderSectionOriginZ + (maxZ ? 16 : 0)));
/* 324 */           Vector3d step = (new Vector3d(cameraPos.x, cameraPos.y, cameraPos.z)).sub((Vector3dc)checkPos).normalize().mul(CEILED_SECTION_DIAGONAL);
/* 325 */           boolean visible = true;
/* 326 */           while (checkPos.distanceSquared(cameraPos.x, cameraPos.y, cameraPos.z) > 3600.0D) {
/* 327 */             checkPos.add((Vector3dc)step);
/* 328 */             if (checkPos.y > this.viewArea.maxY() || checkPos.y < this.viewArea.minY())
/*     */               break; 
/* 331 */             SectionRenderDispatcher.RenderSection checkSection = this.viewArea.getRenderSectionAt(BlockPos.containing(checkPos.x, checkPos.y, checkPos.z));
/* 332 */             if (checkSection == null || storage.sectionToNodeMap.get(checkSection) == null) {
/* 333 */               visible = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 337 */           if (!visible)
/*     */             continue; 
/*     */         } 
/* 342 */         Node existingNode = storage.sectionToNodeMap.get(renderSectionAt);
/* 343 */         if (existingNode != null) {
/* 345 */           existingNode.addSourceDirection(direction);
/*     */         } else {
/* 349 */           Node newNode = new Node(renderSectionAt, direction, node.step + 1);
/* 350 */           newNode.setDirections(node.directions, direction);
/* 352 */           queue.add(newNode);
/* 353 */           storage.sectionToNodeMap.put(renderSectionAt, newNode);
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Frustum offsetFrustum(Frustum frustum) {
/* 359 */     return (new Frustum(frustum)).offsetToFullyIncludeCameraCube(8);
/*     */   }
/*     */   
/*     */   private boolean isInViewDistance(long cameraSectionNode, long sectionNode) {
/* 363 */     return ChunkTrackingView.isInViewDistance(SectionPos.x(cameraSectionNode), SectionPos.z(cameraSectionNode), this.viewArea.getViewDistance(), SectionPos.x(sectionNode), SectionPos.z(sectionNode));
/*     */   }
/*     */   
/*     */   private SectionRenderDispatcher.RenderSection getRelativeFrom(long cameraSectionNode, SectionRenderDispatcher.RenderSection renderSection, Direction direction) {
/* 367 */     long relative = renderSection.getNeighborSectionNode(direction);
/* 369 */     if (!isInViewDistance(cameraSectionNode, relative))
/* 370 */       return null; 
/* 372 */     if (Mth.abs(SectionPos.y(cameraSectionNode) - SectionPos.y(relative)) > this.viewArea.getViewDistance())
/* 373 */       return null; 
/* 377 */     return this.viewArea.getRenderSection(relative);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Node getNode(SectionRenderDispatcher.RenderSection section) {
/* 382 */     return ((GraphState)this.currentGraph.get()).storage.sectionToNodeMap.get(section);
/*     */   }
/*     */   
/*     */   public void updateEmptySections(LongOpenHashSet added, LongOpenHashSet removed) {
/* 386 */     this.emptySections.addAll((LongCollection)added);
/* 387 */     LongIterator iter = removed.longIterator();
/* 388 */     while (iter.hasNext()) {
/* 389 */       long sectionNode = iter.nextLong();
/* 390 */       if (this.emptySections.remove(sectionNode)) {
/* 391 */         SectionRenderDispatcher.RenderSection section = this.viewArea.getRenderSection(sectionNode);
/* 392 */         if (section != null) {
/* 393 */           schedulePropagationFrom(section);
/* 394 */           section.setWasPreviouslyEmpty(true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateLoadedChunks(LongOpenHashSet added, LongOpenHashSet removed) {
/* 401 */     this.loadedChunks.addAll((LongCollection)added);
/* 402 */     this.loadedChunks.removeAll((LongCollection)removed);
/*     */   }
/*     */   
/*     */   private static final class GraphState extends Record {
/*     */     private final SectionOcclusionGraph.GraphStorage storage;
/*     */     
/*     */     private final BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom;
/*     */     
/*     */     public BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom() {
/* 405 */       return this.sectionsToPropagateFrom;
/*     */     }
/*     */     
/*     */     public SectionOcclusionGraph.GraphStorage storage() {
/* 405 */       return this.storage;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */     }
/*     */     
/*     */     private GraphState(SectionOcclusionGraph.GraphStorage storage, BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom) {
/* 405 */       this.storage = storage;
/* 405 */       this.sectionsToPropagateFrom = sectionsToPropagateFrom;
/*     */     }
/*     */     
/*     */     private GraphState(ViewArea viewArea) {
/* 410 */       this(new SectionOcclusionGraph.GraphStorage(viewArea), new LinkedBlockingQueue<>());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GraphStorage {
/*     */     public final SectionOcclusionGraph.SectionToNodeMap sectionToNodeMap;
/*     */     
/*     */     public final Octree sectionTree;
/*     */     
/*     */     public final Long2ObjectMap<LongList> sectionsWaitingForChunkLoads;
/*     */     
/*     */     public GraphStorage(ViewArea viewArea) {
/* 420 */       this.sectionToNodeMap = new SectionOcclusionGraph.SectionToNodeMap(viewArea.size());
/* 421 */       this.sectionTree = new Octree(viewArea.getCameraSectionPos(), viewArea.getViewDistance(), viewArea.sectionCount(), viewArea.minY());
/* 422 */       this.sectionsWaitingForChunkLoads = (Long2ObjectMap<LongList>)new Long2ObjectOpenHashMap();
/*     */     }
/*     */   }
/*     */   
/*     */   public Octree getOctree() {
/* 427 */     return ((GraphState)this.currentGraph.get()).storage.sectionTree;
/*     */   }
/*     */   
/*     */   private static class SectionToNodeMap {
/*     */     private final SectionOcclusionGraph.Node[] nodes;
/*     */     
/*     */     private SectionToNodeMap(int sectionCount) {
/* 434 */       this.nodes = new SectionOcclusionGraph.Node[sectionCount];
/*     */     }
/*     */     
/*     */     public void put(SectionRenderDispatcher.RenderSection renderSection, SectionOcclusionGraph.Node node) {
/* 438 */       this.nodes[renderSection.index] = node;
/*     */     }
/*     */     
/*     */     public SectionOcclusionGraph.Node get(SectionRenderDispatcher.RenderSection renderSection) {
/* 442 */       int index = renderSection.index;
/* 443 */       if (index < 0 || index >= this.nodes.length)
/* 444 */         return null; 
/* 446 */       return this.nodes[index];
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static class Node {
/*     */     @VisibleForDebug
/*     */     protected final SectionRenderDispatcher.RenderSection section;
/*     */     
/*     */     private byte sourceDirections;
/*     */     
/*     */     private byte directions;
/*     */     
/*     */     @VisibleForDebug
/*     */     public final int step;
/*     */     
/*     */     private Node(SectionRenderDispatcher.RenderSection section, Direction sourceDirection, int step) {
/* 460 */       this.section = section;
/* 461 */       if (sourceDirection != null)
/* 462 */         addSourceDirection(sourceDirection); 
/* 464 */       this.step = step;
/*     */     }
/*     */     
/*     */     private void setDirections(byte oldDirections, Direction direction) {
/* 468 */       this.directions = (byte)(this.directions | oldDirections | 1 << direction.ordinal());
/*     */     }
/*     */     
/*     */     private boolean hasDirection(Direction direction) {
/* 472 */       return ((this.directions & 1 << direction.ordinal()) > 0);
/*     */     }
/*     */     
/*     */     private void addSourceDirection(Direction direction) {
/* 476 */       this.sourceDirections = (byte)(this.sourceDirections | this.sourceDirections | 1 << direction.ordinal());
/*     */     }
/*     */     
/*     */     @VisibleForDebug
/*     */     public boolean hasSourceDirection(int directionOrdinal) {
/* 481 */       return ((this.sourceDirections & 1 << directionOrdinal) > 0);
/*     */     }
/*     */     
/*     */     private boolean hasSourceDirections() {
/* 485 */       return (this.sourceDirections != 0);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 490 */       return Long.hashCode(this.section.getSectionNode());
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/*     */       Node other;
/* 495 */       if (obj instanceof Node) {
/* 495 */         other = (Node)obj;
/*     */       } else {
/* 496 */         return false;
/*     */       } 
/* 499 */       return (this.section.getSectionNode() == other.section.getSectionNode());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionOcclusionGraph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */