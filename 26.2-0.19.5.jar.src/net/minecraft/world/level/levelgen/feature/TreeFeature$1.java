/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ class null implements FoliagePlacer.FoliageSetter {
/*     */   null(TreeFeature this$0) {}
/*     */   
/*     */   public void set(BlockPos pos, BlockState state) {
/* 138 */     foliage.add(pos.immutable());
/* 139 */     level.setBlock(pos, state, 19);
/*     */   }
/*     */   
/*     */   public boolean isSet(BlockPos pos) {
/* 144 */     return foliage.contains(pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\TreeFeature$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */