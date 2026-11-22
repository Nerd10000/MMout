/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SpreadContext {
/*     */   private final BlockGetter level;
/*     */   
/*     */   private final BlockPos origin;
/*     */   
/* 387 */   private final Short2ObjectMap<BlockState> stateCache = (Short2ObjectMap<BlockState>)new Short2ObjectOpenHashMap();
/*     */   
/* 388 */   private final Short2BooleanMap holeCache = (Short2BooleanMap)new Short2BooleanOpenHashMap();
/*     */   
/*     */   private SpreadContext(BlockGetter level, BlockPos origin) {
/* 391 */     this.level = level;
/* 392 */     this.origin = origin;
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 396 */     return getBlockState(pos, getCacheKey(pos));
/*     */   }
/*     */   
/*     */   private BlockState getBlockState(BlockPos pos, short key) {
/* 400 */     return (BlockState)this.stateCache.computeIfAbsent(key, k -> this.level.getBlockState(pos));
/*     */   }
/*     */   
/*     */   public boolean isHole(BlockPos pos) {
/* 404 */     return this.holeCache.computeIfAbsent(getCacheKey(pos), key -> {
/*     */           BlockState state = getBlockState(pos, key);
/*     */           BlockPos below = pos.below();
/*     */           BlockState belowState = this.level.getBlockState(below);
/*     */           return FlowingFluid.this.isWaterHole(this.level, pos, state, below, belowState);
/*     */         });
/*     */   }
/*     */   
/*     */   private short getCacheKey(BlockPos pos) {
/* 413 */     int relativeX = pos.getX() - this.origin.getX();
/* 414 */     int relativeZ = pos.getZ() - this.origin.getZ();
/* 415 */     return (short)((relativeX + 128 & 0xFF) << 8 | relativeZ + 128 & 0xFF);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\material\FlowingFluid$SpreadContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */