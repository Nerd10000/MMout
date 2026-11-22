/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class EmptyBlockModel implements BlockModel {
/*  8 */   public static final BlockModel INSTANCE = new EmptyBlockModel();
/*    */   
/*    */   public void update(BlockModelRenderState output, BlockState blockState, BlockDisplayContext displayContext, long seed) {}
/*    */   
/*    */   public static final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/EmptyBlockModel$Unbaked;
/*    */     }
/*    */     
/*    */     public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 17 */       return EmptyBlockModel.INSTANCE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\EmptyBlockModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */