/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ final class DimensionAndPosition extends Record {
/*    */   private final ServerLevel dimension;
/*    */   
/*    */   private final BlockPos position;
/*    */   
/*    */   private DimensionAndPosition(ServerLevel dimension, BlockPos position) {
/* 92 */     this.dimension = dimension;
/* 92 */     this.position = position;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$DimensionAndPosition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ServerLevel dimension() {
/* 92 */     return this.dimension;
/*    */   }
/*    */   
/*    */   public BlockPos position() {
/* 92 */     return this.position;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\CloneCommands$DimensionAndPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */