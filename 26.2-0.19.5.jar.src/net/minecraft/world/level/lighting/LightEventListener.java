/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public interface LightEventListener {
/*    */   void checkBlock(BlockPos paramBlockPos);
/*    */   
/*    */   boolean hasLightWork();
/*    */   
/*    */   int runLightUpdates();
/*    */   
/*    */   default void updateSectionStatus(BlockPos pos, boolean sectionEmpty) {
/* 15 */     updateSectionStatus(SectionPos.of(pos), sectionEmpty);
/*    */   }
/*    */   
/*    */   void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean);
/*    */   
/*    */   void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean);
/*    */   
/*    */   void propagateLightSources(ChunkPos paramChunkPos);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\LightEventListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */