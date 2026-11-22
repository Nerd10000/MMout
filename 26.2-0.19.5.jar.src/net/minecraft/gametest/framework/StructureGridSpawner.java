/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class StructureGridSpawner implements GameTestRunner.StructureSpawner {
/*    */   private static final int SPACE_BETWEEN_COLUMNS = 5;
/*    */   
/*    */   private static final int SPACE_BETWEEN_ROWS = 6;
/*    */   
/*    */   private final int testsPerRow;
/*    */   
/*    */   private int currentRowCount;
/*    */   
/*    */   private AABB rowBounds;
/*    */   
/*    */   private final BlockPos.MutableBlockPos nextTestNorthWestCorner;
/*    */   
/*    */   private final BlockPos firstTestNorthWestCorner;
/*    */   
/*    */   private final boolean clearOnBatch;
/*    */   
/* 25 */   private float maxX = -1.0F;
/*    */   
/* 26 */   private final Collection<GameTestInfo> testInLastBatch = new ArrayList<>();
/*    */   
/*    */   public StructureGridSpawner(BlockPos firstTestNorthWestCorner, int testsPerRow, boolean clearOnBatch) {
/* 30 */     this.testsPerRow = testsPerRow;
/* 31 */     this.nextTestNorthWestCorner = firstTestNorthWestCorner.mutable();
/* 32 */     this.rowBounds = new AABB((BlockPos)this.nextTestNorthWestCorner);
/* 33 */     this.firstTestNorthWestCorner = firstTestNorthWestCorner;
/* 34 */     this.clearOnBatch = clearOnBatch;
/*    */   }
/*    */   
/*    */   public void onBatchStart(ServerLevel level) {
/* 39 */     if (this.clearOnBatch) {
/* 40 */       this.testInLastBatch.forEach(info -> {
/*    */             BoundingBox boundingBox = info.getTestInstanceBlockEntity().getTestBoundingBox();
/*    */             StructureUtils.clearSpaceForStructure(boundingBox, level);
/*    */           });
/* 44 */       this.testInLastBatch.clear();
/* 45 */       this.rowBounds = new AABB(this.firstTestNorthWestCorner);
/* 46 */       this.nextTestNorthWestCorner.set((Vec3i)this.firstTestNorthWestCorner);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Optional<GameTestInfo> spawnStructure(GameTestInfo testInfo) {
/* 52 */     BlockPos northWestCorner = new BlockPos((Vec3i)this.nextTestNorthWestCorner);
/* 53 */     testInfo.setTestBlockPos(northWestCorner);
/* 54 */     GameTestInfo infoWithStructure = testInfo.prepareTestStructure();
/* 55 */     if (infoWithStructure == null)
/* 56 */       return Optional.empty(); 
/* 58 */     infoWithStructure.startExecution(1);
/* 60 */     AABB structureBounds = testInfo.getTestInstanceBlockEntity().getTestBounds();
/* 61 */     this.rowBounds = this.rowBounds.minmax(structureBounds);
/* 63 */     this.nextTestNorthWestCorner.move((int)structureBounds.getXsize() + 5, 0, 0);
/* 64 */     if (this.nextTestNorthWestCorner.getX() > this.maxX)
/* 65 */       this.maxX = this.nextTestNorthWestCorner.getX(); 
/* 68 */     if (++this.currentRowCount >= this.testsPerRow) {
/* 70 */       this.currentRowCount = 0;
/* 71 */       this.nextTestNorthWestCorner.move(0, 0, (int)this.rowBounds.getZsize() + 6);
/* 72 */       this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.getX());
/* 73 */       this.rowBounds = new AABB((BlockPos)this.nextTestNorthWestCorner);
/*    */     } 
/* 76 */     this.testInLastBatch.add(testInfo);
/* 77 */     return Optional.of(testInfo);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\StructureGridSpawner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */