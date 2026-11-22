/*   */ package net.minecraft.client.renderer.block.model.properties.select;
/*   */ 
/*   */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*   */ import net.minecraft.world.level.block.state.BlockState;
/*   */ 
/*   */ public final class DisplayContext extends Record implements SelectBlockModelProperty<BlockDisplayContext> {
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/properties/select/DisplayContext;
/*   */   }
/*   */   
/*   */   public BlockDisplayContext get(BlockState blockState, BlockDisplayContext displayContext) {
/* 9 */     return displayContext;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\properties\select\DisplayContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */