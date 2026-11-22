/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public class BlockPatternMatch {
/*     */   private final BlockPos frontTopLeft;
/*     */   
/*     */   private final Direction forwards;
/*     */   
/*     */   private final Direction up;
/*     */   
/*     */   private final LoadingCache<BlockPos, BlockInWorld> cache;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int depth;
/*     */   
/*     */   public BlockPatternMatch(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, BlockInWorld> cache, int width, int height, int depth) {
/* 146 */     this.frontTopLeft = frontTopLeft;
/* 147 */     this.forwards = forwards;
/* 148 */     this.up = up;
/* 149 */     this.cache = cache;
/* 150 */     this.width = width;
/* 151 */     this.height = height;
/* 152 */     this.depth = depth;
/*     */   }
/*     */   
/*     */   public BlockPos getFrontTopLeft() {
/* 156 */     return this.frontTopLeft;
/*     */   }
/*     */   
/*     */   public Direction getForwards() {
/* 160 */     return this.forwards;
/*     */   }
/*     */   
/*     */   public Direction getUp() {
/* 164 */     return this.up;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 168 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 172 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 176 */     return this.depth;
/*     */   }
/*     */   
/*     */   public BlockInWorld getBlock(int right, int down, int forwards) {
/* 180 */     return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, getForwards(), getUp(), right, down, forwards));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 185 */     return MoreObjects.toStringHelper(this)
/* 186 */       .add("up", this.up)
/* 187 */       .add("forwards", this.forwards)
/* 188 */       .add("frontTopLeft", this.frontTopLeft)
/* 189 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern$BlockPatternMatch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */