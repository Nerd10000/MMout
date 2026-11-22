/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ 
/*     */ class RebindableTickingBlockEntityWrapper implements TickingBlockEntity {
/*     */   private TickingBlockEntity ticker;
/*     */   
/*     */   private RebindableTickingBlockEntityWrapper(TickingBlockEntity ticker) {
/* 815 */     this.ticker = ticker;
/*     */   }
/*     */   
/*     */   private void rebind(TickingBlockEntity ticker) {
/* 819 */     this.ticker = ticker;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 824 */     this.ticker.tick();
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 829 */     return this.ticker.isRemoved();
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 834 */     return this.ticker.getPos();
/*     */   }
/*     */   
/*     */   public String getType() {
/* 839 */     return this.ticker.getType();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 844 */     return String.valueOf(this.ticker) + " <wrapped>";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunk$RebindableTickingBlockEntityWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */