/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ final class BlockEntityPathElement extends Record implements ProblemReporter.PathElement {
/*     */   private final BlockEntity blockEntity;
/*     */   
/*     */   private BlockEntityPathElement(BlockEntity blockEntity) {
/* 363 */     this.blockEntity = blockEntity;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BlockEntity$BlockEntityPathElement;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockEntity blockEntity() {
/* 363 */     return this.blockEntity;
/*     */   }
/*     */   
/*     */   public String get() {
/* 366 */     return this.blockEntity.getNameForReporting() + "@" + this.blockEntity.getNameForReporting();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntity$BlockEntityPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */