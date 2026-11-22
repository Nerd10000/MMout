/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ public class CompiledSectionMesh implements SectionMesh {
/*  15 */   public static final SectionMesh UNCOMPILED = new SectionMesh() {
/*     */       public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  18 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  21 */   public static final SectionMesh EMPTY = new SectionMesh() {
/*     */       public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  24 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final List<BlockEntity> renderableBlockEntities;
/*     */   
/*     */   private final VisibilitySet visibilitySet;
/*     */   
/*     */   private final MeshData.SortState transparencyState;
/*     */   
/*     */   private TranslucencyPointOfView translucencyPointOfView;
/*     */   
/*  32 */   private final Map<ChunkSectionLayer, SectionMesh.SectionDraw> draws = new EnumMap<>(ChunkSectionLayer.class);
/*     */   
/*  33 */   private final Map<ChunkSectionLayer, AtomicBoolean> vertexBufferUploaded = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new AtomicBoolean());
/*     */   
/*  34 */   private final Map<ChunkSectionLayer, AtomicBoolean> indexBufferUploaded = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new AtomicBoolean());
/*     */   
/*     */   public CompiledSectionMesh(TranslucencyPointOfView translucencyPointOfView, SectionCompiler.Results results) {
/*  37 */     this.translucencyPointOfView = translucencyPointOfView;
/*  38 */     this.visibilitySet = results.visibilitySet;
/*  39 */     this.renderableBlockEntities = results.blockEntities;
/*  40 */     this.transparencyState = results.transparencyState;
/*  41 */     results.renderedLayers.forEach((layer, mesh) -> this.draws.put(layer, new SectionMesh.SectionDraw(mesh.drawState().indexCount(), mesh.drawState().indexType(), (mesh.indexBuffer() != null))));
/*     */   }
/*     */   
/*     */   public void setTranslucencyPointOfView(TranslucencyPointOfView translucencyPointOfView) {
/*  47 */     this.translucencyPointOfView = translucencyPointOfView;
/*     */   }
/*     */   
/*     */   public boolean isDifferentPointOfView(TranslucencyPointOfView pointOfView) {
/*  52 */     return !pointOfView.equals(this.translucencyPointOfView);
/*     */   }
/*     */   
/*     */   public boolean hasRenderableLayers() {
/*  57 */     return !this.draws.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isEmpty(ChunkSectionLayer layer) {
/*  62 */     return !this.draws.containsKey(layer);
/*     */   }
/*     */   
/*     */   public List<BlockEntity> getRenderableBlockEntities() {
/*  67 */     return this.renderableBlockEntities;
/*     */   }
/*     */   
/*     */   public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  72 */     return this.visibilitySet.visibilityBetween(direction1, direction2);
/*     */   }
/*     */   
/*     */   public SectionMesh.SectionDraw getSectionDraw(ChunkSectionLayer layer) {
/*  77 */     return this.draws.get(layer);
/*     */   }
/*     */   
/*     */   public boolean isVertexBufferUploaded(ChunkSectionLayer layer) {
/*  81 */     return ((AtomicBoolean)this.vertexBufferUploaded.get(layer)).get();
/*     */   }
/*     */   
/*     */   public boolean isIndexBufferUploaded(ChunkSectionLayer layer) {
/*  85 */     return ((AtomicBoolean)this.indexBufferUploaded.get(layer)).get();
/*     */   }
/*     */   
/*     */   public void setVertexBufferUploaded(ChunkSectionLayer layer) {
/*  89 */     ((AtomicBoolean)this.vertexBufferUploaded.get(layer)).set(true);
/*     */   }
/*     */   
/*     */   public void setIndexBufferUploaded(ChunkSectionLayer layer) {
/*  93 */     ((AtomicBoolean)this.indexBufferUploaded.get(layer)).set(true);
/*     */   }
/*     */   
/*     */   public boolean hasTranslucentGeometry() {
/*  98 */     return this.draws.containsKey(ChunkSectionLayer.TRANSLUCENT);
/*     */   }
/*     */   
/*     */   public MeshData.SortState getTransparencyState() {
/* 102 */     return this.transparencyState;
/*     */   }
/*     */   
/*     */   public void close() {
/* 107 */     this.draws.clear();
/* 108 */     this.vertexBufferUploaded.clear();
/* 109 */     this.indexBufferUploaded.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\CompiledSectionMesh.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */