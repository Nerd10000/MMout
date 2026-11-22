/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   private final SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch;
/*    */   
/*    */   private final Optional<BlockModel.Unbaked> fallback;
/*    */   
/*    */   public Unbaked(Optional<Transformation> transformation, SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch, Optional<BlockModel.Unbaked> fallback) {
/* 34 */     this.transformation = transformation;
/* 34 */     this.unbakedSwitch = unbakedSwitch;
/* 34 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #34	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #34	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #34	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 34 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch() {
/* 34 */     return this.unbakedSwitch;
/*    */   }
/*    */   
/*    */   public Optional<BlockModel.Unbaked> fallback() {
/* 34 */     return this.fallback;
/*    */   }
/*    */   
/*    */   public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 41 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 42 */     BlockModel bakedFallback = this.fallback.<BlockModel>map(m -> m.bake(context, childTransform)).orElse(context.missingBlockModel());
/* 43 */     return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\SelectBlockModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */