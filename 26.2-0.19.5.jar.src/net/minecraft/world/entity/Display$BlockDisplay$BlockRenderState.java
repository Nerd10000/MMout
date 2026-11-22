/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class BlockRenderState extends Record {
/*     */   private final BlockState blockState;
/*     */   
/*     */   public BlockRenderState(BlockState blockState) {
/* 651 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #651	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #651	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #651	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$BlockDisplay$BlockRenderState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockState blockState() {
/* 651 */     return this.blockState;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$BlockDisplay$BlockRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */