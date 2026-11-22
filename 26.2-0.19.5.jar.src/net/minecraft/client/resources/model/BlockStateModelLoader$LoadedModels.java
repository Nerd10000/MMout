/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class LoadedModels extends Record {
/*    */   private final Map<BlockState, BlockStateModel.UnbakedRoot> models;
/*    */   
/*    */   public LoadedModels(Map<BlockState, BlockStateModel.UnbakedRoot> models) {
/* 94 */     this.models = models;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/BlockStateModelLoader$LoadedModels;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<BlockState, BlockStateModel.UnbakedRoot> models() {
/* 94 */     return this.models;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\BlockStateModelLoader$LoadedModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */