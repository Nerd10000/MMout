/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ 
/*     */ class MossStoneSelector extends StructurePiece.BlockSelector {
/*     */   public void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge) {
/* 258 */     if (random.nextFloat() < 0.4F) {
/* 259 */       this.next = Blocks.COBBLESTONE.defaultBlockState();
/*     */     } else {
/* 261 */       this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTemplePiece$MossStoneSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */