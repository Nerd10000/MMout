/*   */ package net.minecraft.world.level;
/*   */ 
/*   */ import net.minecraft.core.BlockPos;
/*   */ import net.minecraft.world.level.block.Block;
/*   */ 
/*   */ public final class BlockEventData extends Record {
/*   */   private final BlockPos pos;
/*   */   
/*   */   private final Block block;
/*   */   
/*   */   private final int paramA;
/*   */   
/*   */   private final int paramB;
/*   */   
/*   */   public BlockEventData(BlockPos pos, Block block, int paramA, int paramB) {
/* 6 */     this.pos = pos;
/* 6 */     this.block = block;
/* 6 */     this.paramA = paramA;
/* 6 */     this.paramB = paramB;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/BlockEventData;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/BlockEventData;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/BlockEventData;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/BlockEventData;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/BlockEventData;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/BlockEventData;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public BlockPos pos() {
/* 6 */     return this.pos;
/*   */   }
/*   */   
/*   */   public Block block() {
/* 6 */     return this.block;
/*   */   }
/*   */   
/*   */   public int paramA() {
/* 6 */     return this.paramA;
/*   */   }
/*   */   
/*   */   public int paramB() {
/* 6 */     return this.paramB;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\BlockEventData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */