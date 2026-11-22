/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public interface LayerLightEventListener extends LightEventListener {
/*    */   DataLayer getDataLayerData(SectionPos paramSectionPos);
/*    */   
/*    */   int getLightValue(BlockPos paramBlockPos);
/*    */   
/*    */   public enum DummyLightLayerEventListener implements LayerLightEventListener {
/* 16 */     INSTANCE;
/*    */     
/*    */     public DataLayer getDataLayerData(SectionPos pos) {
/* 20 */       return null;
/*    */     }
/*    */     
/*    */     public int getLightValue(BlockPos pos) {
/* 25 */       return 0;
/*    */     }
/*    */     
/*    */     public void checkBlock(BlockPos pos) {}
/*    */     
/*    */     public boolean hasLightWork() {
/* 34 */       return false;
/*    */     }
/*    */     
/*    */     public int runLightUpdates() {
/* 39 */       return 0;
/*    */     }
/*    */     
/*    */     public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {}
/*    */     
/*    */     public void setLightEnabled(ChunkPos pos, boolean enable) {}
/*    */     
/*    */     public void propagateLightSources(ChunkPos pos) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\LayerLightEventListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */