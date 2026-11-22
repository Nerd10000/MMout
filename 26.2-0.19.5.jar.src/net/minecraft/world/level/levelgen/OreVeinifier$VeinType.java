/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public enum VeinType {
/*  87 */   COPPER(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50),
/*  88 */   IRON(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);
/*     */   
/*     */   private final BlockState ore;
/*     */   
/*     */   private final BlockState rawOreBlock;
/*     */   
/*     */   private final BlockState filler;
/*     */   
/*     */   protected final int minY;
/*     */   
/*     */   protected final int maxY;
/*     */   
/*     */   VeinType(BlockState ore, BlockState rawOreBlock, BlockState filler, int minY, int maxY) {
/*  98 */     this.ore = ore;
/*  99 */     this.rawOreBlock = rawOreBlock;
/* 100 */     this.filler = filler;
/* 101 */     this.minY = minY;
/* 102 */     this.maxY = maxY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\OreVeinifier$VeinType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */