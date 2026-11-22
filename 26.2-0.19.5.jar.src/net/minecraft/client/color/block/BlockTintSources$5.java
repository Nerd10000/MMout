/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import net.minecraft.client.renderer.BiomeColors;
/*     */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ class null implements BlockTintSource {
/*     */   public int color(BlockState state) {
/*  97 */     return -1;
/*     */   }
/*     */   
/*     */   public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 102 */     return BiomeColors.getAverageGrassColor(level, pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSources$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */