/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ public interface LevelHeightAccessor {
/*    */   int getHeight();
/*    */   
/*    */   int getMinY();
/*    */   
/*    */   default int getMaxY() {
/* 16 */     return getMinY() + getHeight() - 1;
/*    */   }
/*    */   
/*    */   default int getSectionsCount() {
/* 21 */     return getMaxSectionY() - getMinSectionY() + 1;
/*    */   }
/*    */   
/*    */   default int getMinSectionY() {
/* 26 */     return SectionPos.blockToSectionCoord(getMinY());
/*    */   }
/*    */   
/*    */   default int getMaxSectionY() {
/* 31 */     return SectionPos.blockToSectionCoord(getMaxY());
/*    */   }
/*    */   
/*    */   default boolean isInsideBuildHeight(BlockPos pos) {
/* 35 */     return isInsideBuildHeight(pos.getY());
/*    */   }
/*    */   
/*    */   default boolean isInsideBuildHeight(int blockY) {
/* 39 */     return (blockY >= getMinY() && blockY <= getMaxY());
/*    */   }
/*    */   
/*    */   default boolean isOutsideBuildHeight(BlockPos pos) {
/* 44 */     return isOutsideBuildHeight(pos.getY());
/*    */   }
/*    */   
/*    */   default boolean isOutsideBuildHeight(int blockY) {
/* 49 */     return (blockY < getMinY() || blockY > getMaxY());
/*    */   }
/*    */   
/*    */   default int getSectionIndex(int blockY) {
/* 54 */     return getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(blockY));
/*    */   }
/*    */   
/*    */   default int getSectionIndexFromSectionY(int sectionY) {
/* 59 */     return sectionY - getMinSectionY();
/*    */   }
/*    */   
/*    */   default int getSectionYFromSectionIndex(int sectionIndex) {
/* 64 */     return sectionIndex + getMinSectionY();
/*    */   }
/*    */   
/*    */   static LevelHeightAccessor create(final int minY, final int height) {
/* 68 */     return new LevelHeightAccessor() {
/*    */         public int getHeight() {
/* 71 */           return height;
/*    */         }
/*    */         
/*    */         public int getMinY() {
/* 76 */           return minY;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LevelHeightAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */