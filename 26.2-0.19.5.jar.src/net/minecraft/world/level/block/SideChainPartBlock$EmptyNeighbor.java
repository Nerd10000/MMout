/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ 
/*     */ public final class EmptyNeighbor extends Record implements SideChainPartBlock.Neighbor {
/*     */   private final BlockPos pos;
/*     */   
/*     */   public EmptyNeighbor(BlockPos pos) {
/* 165 */     this.pos = pos;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #165	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 165 */     return this.pos;
/*     */   }
/*     */   
/*     */   public boolean isConnectable() {
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isUnconnectableOrChainEnd() {
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   public boolean connectsTowards(SideChainPart endPart) {
/* 178 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SideChainPartBlock$EmptyNeighbor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */