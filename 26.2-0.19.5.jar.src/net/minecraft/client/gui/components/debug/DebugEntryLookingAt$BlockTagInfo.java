/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.TypedInstance;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class BlockTagInfo extends DebugEntryLookingAt.DebugEntryLookingAtTags<BlockState> {
/*     */   public HitResult getHitResult(Entity cameraEntity) {
/* 126 */     return cameraEntity.pick(20.0D, 0.0F, false);
/*     */   }
/*     */   
/*     */   public BlockState getInstance(Level level, BlockPos pos) {
/* 131 */     return level.getBlockState(pos);
/*     */   }
/*     */   
/*     */   public Identifier group() {
/* 136 */     return DebugEntryLookingAt.BLOCK_GROUP;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryLookingAt$BlockTagInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */