/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class BlockDisplay extends Display {
/*     */   public static final String TAG_BLOCK_STATE = "block_state";
/*     */   
/* 599 */   private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(BlockDisplay.class, EntityDataSerializers.BLOCK_STATE);
/*     */   
/*     */   private BlockRenderState blockRenderState;
/*     */   
/*     */   public BlockDisplay(EntityType<?> type, Level level) {
/* 604 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 609 */     super.defineSynchedData(entityData);
/* 610 */     entityData.define(DATA_BLOCK_STATE_ID, Blocks.AIR.defaultBlockState());
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 615 */     super.onSyncedDataUpdated(accessor);
/* 617 */     if (accessor.equals(DATA_BLOCK_STATE_ID))
/* 618 */       this.updateRenderState = true; 
/*     */   }
/*     */   
/*     */   private BlockState getBlockState() {
/* 623 */     return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
/*     */   }
/*     */   
/*     */   private void setBlockState(BlockState blockState) {
/* 627 */     this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 632 */     super.readAdditionalSaveData(input);
/* 633 */     setBlockState(input.read("block_state", BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState()));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 638 */     super.addAdditionalSaveData(output);
/* 639 */     output.store("block_state", BlockState.CODEC, getBlockState());
/*     */   }
/*     */   
/*     */   public BlockRenderState blockRenderState() {
/* 643 */     return this.blockRenderState;
/*     */   }
/*     */   
/*     */   protected void updateRenderSubState(boolean shouldInterpolate, float progress) {
/* 648 */     this.blockRenderState = new BlockRenderState(getBlockState());
/*     */   }
/*     */   
/*     */   public static final class BlockRenderState extends Record {
/*     */     private final BlockState blockState;
/*     */     
/*     */     public BlockRenderState(BlockState blockState) {
/* 651 */       this.blockState = blockState;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #651	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockState blockState() {
/* 651 */       return this.blockState;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$BlockDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */