/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class RenderRegionCache {
/* 11 */   private final Long2ObjectMap<SectionCopy> sectionCopyCache = (Long2ObjectMap<SectionCopy>)new Long2ObjectOpenHashMap();
/*    */   
/*    */   public RenderSectionRegion createRegion(ClientLevel level, long sectionNode) {
/* 14 */     int sectionX = SectionPos.x(sectionNode);
/* 15 */     int sectionY = SectionPos.y(sectionNode);
/* 16 */     int sectionZ = SectionPos.z(sectionNode);
/* 18 */     int minSectionX = sectionX - 1;
/* 19 */     int minSectionY = sectionY - 1;
/* 20 */     int minSectionZ = sectionZ - 1;
/* 21 */     int maxSectionX = sectionX + 1;
/* 22 */     int maxSectionY = sectionY + 1;
/* 23 */     int maxSectionZ = sectionZ + 1;
/* 25 */     SectionCopy[] regionSections = new SectionCopy[27];
/* 26 */     for (int regionSectionZ = minSectionZ; regionSectionZ <= maxSectionZ; regionSectionZ++) {
/* 27 */       for (int regionSectionY = minSectionY; regionSectionY <= maxSectionY; regionSectionY++) {
/* 28 */         for (int regionSectionX = minSectionX; regionSectionX <= maxSectionX; regionSectionX++) {
/* 29 */           int index = RenderSectionRegion.index(minSectionX, minSectionY, minSectionZ, regionSectionX, regionSectionY, regionSectionZ);
/* 30 */           regionSections[index] = getSectionDataCopy((Level)level, regionSectionX, regionSectionY, regionSectionZ);
/*    */         } 
/*    */       } 
/*    */     } 
/* 35 */     return new RenderSectionRegion(level, minSectionX, minSectionY, minSectionZ, regionSections);
/*    */   }
/*    */   
/*    */   private SectionCopy getSectionDataCopy(Level level, int sectionX, int sectionY, int sectionZ) {
/* 39 */     return (SectionCopy)this.sectionCopyCache.computeIfAbsent(SectionPos.asLong(sectionX, sectionY, sectionZ), k -> {
/*    */           LevelChunk chunk = level.getChunk(sectionX, sectionZ);
/*    */           return new SectionCopy(chunk, chunk.getSectionIndexFromSectionY(sectionY));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\RenderRegionCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */