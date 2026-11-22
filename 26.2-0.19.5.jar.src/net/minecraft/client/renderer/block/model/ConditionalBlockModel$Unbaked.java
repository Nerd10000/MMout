/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.block.model.properties.conditional.ConditionalBlockModelProperty;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   private final ConditionalBlockModelProperty property;
/*    */   
/*    */   private final BlockModel.Unbaked onTrue;
/*    */   
/*    */   private final BlockModel.Unbaked onFalse;
/*    */   
/*    */   public Unbaked(Optional<Transformation> transformation, ConditionalBlockModelProperty property, BlockModel.Unbaked onTrue, BlockModel.Unbaked onFalse) {
/* 27 */     this.transformation = transformation;
/* 27 */     this.property = property;
/* 27 */     this.onTrue = onTrue;
/* 27 */     this.onFalse = onFalse;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/ConditionalBlockModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 27 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public ConditionalBlockModelProperty property() {
/* 27 */     return this.property;
/*    */   }
/*    */   
/*    */   public BlockModel.Unbaked onTrue() {
/* 27 */     return this.onTrue;
/*    */   }
/*    */   
/*    */   public BlockModel.Unbaked onFalse() {
/* 27 */     return this.onFalse;
/*    */   }
/*    */   
/*    */   public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 36 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 37 */     return new ConditionalBlockModel(this.property, this.onTrue
/*    */         
/* 39 */         .bake(context, childTransform), this.onFalse
/* 40 */         .bake(context, childTransform));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\ConditionalBlockModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */