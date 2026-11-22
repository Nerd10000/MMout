/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Optional;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */   private final BlockModel.Unbaked normal;
/*    */   
/*    */   private final BlockModel.Unbaked custom;
/*    */   
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   public Unbaked(BlockModel.Unbaked normal, BlockModel.Unbaked custom, Optional<Transformation> transformation) {
/* 26 */     this.normal = normal;
/* 26 */     this.custom = custom;
/* 26 */     this.transformation = transformation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/CompositeBlockModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockModel.Unbaked normal() {
/* 26 */     return this.normal;
/*    */   }
/*    */   
/*    */   public BlockModel.Unbaked custom() {
/* 26 */     return this.custom;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 26 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 34 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 35 */     return new CompositeBlockModel(this.normal
/* 36 */         .bake(context, childTransform), this.custom
/* 37 */         .bake(context, childTransform));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\CompositeBlockModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */