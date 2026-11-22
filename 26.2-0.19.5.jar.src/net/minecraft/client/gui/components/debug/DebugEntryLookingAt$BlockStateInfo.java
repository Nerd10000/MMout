/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class BlockStateInfo extends DebugEntryLookingAt.DebugEntryLookingAtState<Block, BlockState> {
/*     */   protected BlockStateInfo() {
/* 104 */     super("Targeted Block");
/*     */   }
/*     */   
/*     */   public HitResult getHitResult(Entity cameraEntity) {
/* 109 */     return cameraEntity.pick(20.0D, 0.0F, false);
/*     */   }
/*     */   
/*     */   public BlockState getInstance(Level level, BlockPos pos) {
/* 114 */     return level.getBlockState(pos);
/*     */   }
/*     */   
/*     */   public Identifier group() {
/* 119 */     return DebugEntryLookingAt.BLOCK_GROUP;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt$BlockStateInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */