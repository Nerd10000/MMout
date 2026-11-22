/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class BlockSelector {
/* 528 */   protected BlockState next = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   public abstract void next(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
/*     */   
/*     */   public BlockState getNext() {
/* 533 */     return this.next;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructurePiece$BlockSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */