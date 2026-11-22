/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ class SculkVeinSpreaderConfig extends MultifaceSpreader.DefaultSpreaderConfig {
/*     */   private final MultifaceSpreader.SpreadType[] spreadTypes;
/*     */   
/*     */   public SculkVeinSpreaderConfig(SculkVeinBlock paramSculkVeinBlock, MultifaceSpreader.SpreadType... spreadTypes) {
/* 156 */     super(paramSculkVeinBlock);
/* 157 */     this.spreadTypes = spreadTypes;
/*     */   }
/*     */   
/*     */   public boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) {
/* 162 */     BlockState againstState = level.getBlockState(placementPos.relative(placementDirection));
/* 166 */     if (againstState.is(Blocks.SCULK) || againstState.is(Blocks.SCULK_CATALYST) || againstState.is(Blocks.MOVING_PISTON))
/* 167 */       return false; 
/* 172 */     if (sourcePos.distManhattan((Vec3i)placementPos) == 2) {
/* 173 */       BlockPos neighourPos = sourcePos.relative(placementDirection.getOpposite());
/* 174 */       if (level.getBlockState(neighourPos).isFaceSturdy(level, neighourPos, placementDirection))
/* 175 */         return false; 
/*     */     } 
/* 179 */     FluidState fluidState = existingState.getFluidState();
/* 180 */     if (!fluidState.isEmpty() && !fluidState.is(Fluids.WATER))
/* 181 */       return false; 
/* 185 */     if (existingState.is(BlockTags.FIRE))
/* 186 */       return false; 
/* 189 */     return (existingState.canBeReplaced() || super.stateCanBeReplaced(level, sourcePos, placementPos, placementDirection, existingState));
/*     */   }
/*     */   
/*     */   public MultifaceSpreader.SpreadType[] getSpreadTypes() {
/* 194 */     return this.spreadTypes;
/*     */   }
/*     */   
/*     */   public boolean isOtherBlockValidAsSource(BlockState state) {
/* 199 */     return !state.is(Blocks.SCULK_VEIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SculkVeinBlock$SculkVeinSpreaderConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */