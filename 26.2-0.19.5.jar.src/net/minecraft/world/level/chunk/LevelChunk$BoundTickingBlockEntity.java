/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ class BoundTickingBlockEntity<T extends BlockEntity> implements TickingBlockEntity {
/*     */   private final T blockEntity;
/*     */   
/*     */   private final BlockEntityTicker<T> ticker;
/*     */   
/*     */   private boolean loggedInvalidBlockState;
/*     */   
/*     */   private BoundTickingBlockEntity(T blockEntity, BlockEntityTicker<T> ticker) {
/* 756 */     this.blockEntity = blockEntity;
/* 757 */     this.ticker = ticker;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 762 */     if (!this.blockEntity.isRemoved() && this.blockEntity.hasLevel()) {
/* 763 */       BlockPos pos = this.blockEntity.getBlockPos();
/* 764 */       if (LevelChunk.this.isTicking(pos))
/*     */         try {
/* 766 */           ProfilerFiller profiler = Profiler.get();
/* 767 */           profiler.push(this::getType);
/* 768 */           BlockState blockState = LevelChunk.this.getBlockState(pos);
/* 769 */           if (this.blockEntity.getType().isValid(blockState)) {
/* 770 */             this.ticker.tick(LevelChunk.this.level, this.blockEntity.getBlockPos(), blockState, (BlockEntity)this.blockEntity);
/* 771 */             this.loggedInvalidBlockState = false;
/* 773 */           } else if (!this.loggedInvalidBlockState) {
/* 774 */             this.loggedInvalidBlockState = true;
/* 775 */             LevelChunk.LOGGER.warn("Block entity {} @ {} state {} invalid for ticking:", new Object[] { LogUtils.defer(this::getType), LogUtils.defer(this::getPos), blockState });
/*     */           } 
/* 778 */           profiler.pop();
/* 779 */         } catch (Throwable t) {
/* 780 */           CrashReport report = CrashReport.forThrowable(t, "Ticking block entity");
/* 781 */           CrashReportCategory category = report.addCategory("Block entity being ticked");
/* 782 */           this.blockEntity.fillCrashReportCategory(category);
/* 784 */           throw new ReportedException(report);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 792 */     return this.blockEntity.isRemoved();
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 797 */     return this.blockEntity.getBlockPos();
/*     */   }
/*     */   
/*     */   public String getType() {
/* 802 */     return this.blockEntity.typeHolder().getRegisteredName();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 807 */     return "Level ticker for " + getType() + "@" + String.valueOf(getPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunk$BoundTickingBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */