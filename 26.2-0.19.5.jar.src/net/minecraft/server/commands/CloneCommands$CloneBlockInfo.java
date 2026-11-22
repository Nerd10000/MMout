/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ final class CloneBlockInfo extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final BlockState state;
/*     */   
/*     */   private final CloneCommands.CloneBlockEntityInfo blockEntityInfo;
/*     */   
/*     */   private final BlockState previousStateAtDestination;
/*     */   
/*     */   private CloneBlockInfo(BlockPos pos, BlockState state, CloneCommands.CloneBlockEntityInfo blockEntityInfo, BlockState previousStateAtDestination) {
/* 304 */     this.pos = pos;
/* 304 */     this.state = state;
/* 304 */     this.blockEntityInfo = blockEntityInfo;
/* 304 */     this.previousStateAtDestination = previousStateAtDestination;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #304	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #304	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #304	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 304 */     return this.pos;
/*     */   }
/*     */   
/*     */   public BlockState state() {
/* 304 */     return this.state;
/*     */   }
/*     */   
/*     */   public CloneCommands.CloneBlockEntityInfo blockEntityInfo() {
/* 304 */     return this.blockEntityInfo;
/*     */   }
/*     */   
/*     */   public BlockState previousStateAtDestination() {
/* 304 */     return this.previousStateAtDestination;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\CloneCommands$CloneBlockInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */