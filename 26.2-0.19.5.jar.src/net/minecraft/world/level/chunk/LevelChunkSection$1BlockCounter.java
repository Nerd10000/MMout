/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ class BlockCounter implements PalettedContainer.CountConsumer<BlockState> {
/*     */   public int nonEmptyBlockCount;
/*     */   
/*     */   public int fluidCount;
/*     */   
/*     */   public int tickingBlockCount;
/*     */   
/*     */   public int tickingFluidCount;
/*     */   
/*     */   BlockCounter(LevelChunkSection this$0) {}
/*     */   
/*     */   public void accept(BlockState state, int count) {
/* 133 */     if (state.isAir())
/*     */       return; 
/* 136 */     this.nonEmptyBlockCount += count;
/* 137 */     if (state.isRandomlyTicking())
/* 138 */       this.tickingBlockCount += count; 
/* 140 */     FluidState fluid = state.getFluidState();
/* 141 */     if (!fluid.isEmpty()) {
/* 142 */       this.fluidCount += count;
/* 143 */       if (fluid.isRandomlyTicking())
/* 144 */         this.tickingFluidCount += count; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunkSection$1BlockCounter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */