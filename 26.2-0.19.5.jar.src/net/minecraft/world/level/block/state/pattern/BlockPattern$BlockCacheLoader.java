/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ class BlockCacheLoader extends CacheLoader<BlockPos, BlockInWorld> {
/*     */   private final LevelReader level;
/*     */   
/*     */   private final boolean loadChunks;
/*     */   
/*     */   public BlockCacheLoader(LevelReader level, boolean loadChunks) {
/* 126 */     this.level = level;
/* 127 */     this.loadChunks = loadChunks;
/*     */   }
/*     */   
/*     */   public BlockInWorld load(BlockPos key) {
/* 132 */     return new BlockInWorld(this.level, key, this.loadChunks);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern$BlockCacheLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */