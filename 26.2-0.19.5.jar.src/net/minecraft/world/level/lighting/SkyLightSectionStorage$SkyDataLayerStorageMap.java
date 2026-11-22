/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ 
/*     */ public final class SkyDataLayerStorageMap extends DataLayerStorageMap<SkyLightSectionStorage.SkyDataLayerStorageMap> {
/*     */   private int currentLowestY;
/*     */   
/*     */   private final Long2IntOpenHashMap topSections;
/*     */   
/*     */   public SkyDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map, Long2IntOpenHashMap topSections, int currentLowestY) {
/* 152 */     super(map);
/* 153 */     this.topSections = topSections;
/* 154 */     topSections.defaultReturnValue(currentLowestY);
/* 155 */     this.currentLowestY = currentLowestY;
/*     */   }
/*     */   
/*     */   public SkyDataLayerStorageMap copy() {
/* 160 */     return new SkyDataLayerStorageMap(this.map.clone(), this.topSections.clone(), this.currentLowestY);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\SkyLightSectionStorage$SkyDataLayerStorageMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */