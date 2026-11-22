/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ 
/*     */ class GraphStorage {
/*     */   public final SectionOcclusionGraph.SectionToNodeMap sectionToNodeMap;
/*     */   
/*     */   public final Octree sectionTree;
/*     */   
/*     */   public final Long2ObjectMap<LongList> sectionsWaitingForChunkLoads;
/*     */   
/*     */   public GraphStorage(ViewArea viewArea) {
/* 420 */     this.sectionToNodeMap = new SectionOcclusionGraph.SectionToNodeMap(viewArea.size());
/* 421 */     this.sectionTree = new Octree(viewArea.getCameraSectionPos(), viewArea.getViewDistance(), viewArea.sectionCount(), viewArea.minY());
/* 422 */     this.sectionsWaitingForChunkLoads = (Long2ObjectMap<LongList>)new Long2ObjectOpenHashMap();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionOcclusionGraph$GraphStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */