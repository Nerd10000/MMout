/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ 
/*      */ class SmoothStoneSelector extends StructurePiece.BlockSelector {
/*      */   public void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge) {
/* 1511 */     if (isEdge) {
/* 1512 */       float selection = random.nextFloat();
/* 1513 */       if (selection < 0.2F) {
/* 1514 */         this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
/* 1515 */       } else if (selection < 0.5F) {
/* 1516 */         this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
/* 1517 */       } else if (selection < 0.55F) {
/* 1518 */         this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
/*      */       } else {
/* 1520 */         this.next = Blocks.STONE_BRICKS.defaultBlockState();
/*      */       } 
/*      */     } else {
/* 1523 */       this.next = Blocks.CAVE_AIR.defaultBlockState();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$SmoothStoneSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */