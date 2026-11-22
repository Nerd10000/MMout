/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class TransportItemTarget extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final Container container;
/*     */   
/*     */   private final BlockEntity blockEntity;
/*     */   
/*     */   private final BlockState state;
/*     */   
/*     */   public TransportItemTarget(BlockPos pos, Container container, BlockEntity blockEntity, BlockState state) {
/* 556 */     this.pos = pos;
/* 556 */     this.container = container;
/* 556 */     this.blockEntity = blockEntity;
/* 556 */     this.state = state;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #556	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #556	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #556	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 556 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Container container() {
/* 556 */     return this.container;
/*     */   }
/*     */   
/*     */   public BlockEntity blockEntity() {
/* 556 */     return this.blockEntity;
/*     */   }
/*     */   
/*     */   public BlockState state() {
/* 556 */     return this.state;
/*     */   }
/*     */   
/*     */   public static TransportItemTarget tryCreatePossibleTarget(BlockEntity blockEntity, Level level) {
/* 558 */     BlockPos blockPos = blockEntity.getBlockPos();
/* 559 */     BlockState blockState = blockEntity.getBlockState();
/* 560 */     Container container = getBlockEntityContainer(blockEntity, blockState, level, blockPos);
/* 561 */     if (container != null)
/* 562 */       return new TransportItemTarget(blockPos, container, blockEntity, blockState); 
/* 564 */     return null;
/*     */   }
/*     */   
/*     */   public static TransportItemTarget tryCreatePossibleTarget(BlockPos blockPos, Level level) {
/* 568 */     BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 569 */     return (blockEntity == null) ? null : tryCreatePossibleTarget(blockEntity, level);
/*     */   }
/*     */   
/*     */   private static Container getBlockEntityContainer(BlockEntity blockEntity, BlockState blockState, Level level, BlockPos blockPos) {
/* 573 */     Block block = blockState.getBlock();
/* 573 */     if (block instanceof ChestBlock) {
/* 573 */       ChestBlock chestBlock = (ChestBlock)block;
/* 574 */       return ChestBlock.getContainer(chestBlock, blockState, level, blockPos, false);
/*     */     } 
/* 575 */     if (blockEntity instanceof Container) {
/* 575 */       Container container = (Container)blockEntity;
/* 576 */       return container;
/*     */     } 
/* 578 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\TransportItemsBetweenContainers$TransportItemTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */