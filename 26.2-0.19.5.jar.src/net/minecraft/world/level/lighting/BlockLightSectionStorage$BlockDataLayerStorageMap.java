/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public final class BlockDataLayerStorageMap extends DataLayerStorageMap<BlockLightSectionStorage.BlockDataLayerStorageMap> {
/*    */   public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map) {
/* 31 */     super(map);
/*    */   }
/*    */   
/*    */   public BlockDataLayerStorageMap copy() {
/* 36 */     return new BlockDataLayerStorageMap(this.map.clone());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage$BlockDataLayerStorageMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */