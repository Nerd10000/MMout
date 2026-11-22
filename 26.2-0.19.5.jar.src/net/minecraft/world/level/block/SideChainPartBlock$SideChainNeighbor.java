/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ 
/*     */ public final class SideChainNeighbor extends Record implements SideChainPartBlock.Neighbor {
/*     */   private final LevelAccessor level;
/*     */   
/*     */   private final SideChainPartBlock block;
/*     */   
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final SideChainPart part;
/*     */   
/*     */   public SideChainNeighbor(LevelAccessor level, SideChainPartBlock block, BlockPos pos, SideChainPart part) {
/* 182 */     this.level = level;
/* 182 */     this.block = block;
/* 182 */     this.pos = pos;
/* 182 */     this.part = part;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LevelAccessor level() {
/* 182 */     return this.level;
/*     */   }
/*     */   
/*     */   public SideChainPartBlock block() {
/* 182 */     return this.block;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 182 */     return this.pos;
/*     */   }
/*     */   
/*     */   public SideChainPart part() {
/* 182 */     return this.part;
/*     */   }
/*     */   
/*     */   public boolean isConnectable() {
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isUnconnectableOrChainEnd() {
/* 190 */     return this.part.isChainEnd();
/*     */   }
/*     */   
/*     */   public boolean connectsTowards(SideChainPart endPart) {
/* 195 */     return this.part.isConnectionTowards(endPart);
/*     */   }
/*     */   
/*     */   public void connectToTheRight() {
/* 200 */     this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheRight());
/*     */   }
/*     */   
/*     */   public void connectToTheLeft() {
/* 205 */     this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheLeft());
/*     */   }
/*     */   
/*     */   public void disconnectFromRight() {
/* 210 */     this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheRight());
/*     */   }
/*     */   
/*     */   public void disconnectFromLeft() {
/* 215 */     this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheLeft());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SideChainPartBlock$SideChainNeighbor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */