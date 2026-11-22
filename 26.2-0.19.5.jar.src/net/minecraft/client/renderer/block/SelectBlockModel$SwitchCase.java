/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ 
/*    */ public final class SwitchCase<T> extends Record {
/*    */   private final List<T> values;
/*    */   
/*    */   private final BlockModel.Unbaked model;
/*    */   
/*    */   public SwitchCase(List<T> values, BlockModel.Unbaked model) {
/* 67 */     this.values = values;
/* 67 */     this.model = model;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */   }
/*    */   
/*    */   public List<T> values() {
/* 67 */     return this.values;
/*    */   }
/*    */   
/*    */   public BlockModel.Unbaked model() {
/* 67 */     return this.model;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\SelectBlockModel$SwitchCase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */