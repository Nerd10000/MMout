/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
/*    */ 
/*    */ final class LoadedBlockStateModelDispatcher extends Record {
/*    */   private final String source;
/*    */   
/*    */   private final BlockStateModelDispatcher contents;
/*    */   
/*    */   private LoadedBlockStateModelDispatcher(String source, BlockStateModelDispatcher contents) {
/* 92 */     this.source = source;
/* 92 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #92	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedBlockStateModelDispatcher;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String source() {
/* 92 */     return this.source;
/*    */   }
/*    */   
/*    */   public BlockStateModelDispatcher contents() {
/* 92 */     return this.contents;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\BlockStateModelLoader$LoadedBlockStateModelDispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */