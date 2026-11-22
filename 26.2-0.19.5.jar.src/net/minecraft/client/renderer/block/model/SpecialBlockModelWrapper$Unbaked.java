/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked<T> extends Record implements BlockModel.Unbaked {
/*    */   private final SpecialModelRenderer.Unbaked<T> model;
/*    */   
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   public Unbaked(SpecialModelRenderer.Unbaked<T> model, Optional<Transformation> transformation) {
/* 25 */     this.model = model;
/* 25 */     this.transformation = transformation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/SpecialBlockModelWrapper$Unbaked<TT;>;
/*    */   }
/*    */   
/*    */   public SpecialModelRenderer.Unbaked<T> model() {
/* 25 */     return this.model;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 25 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 31 */     SpecialModelRenderer<T> baked = this.model.bake(context);
/* 32 */     if (baked == null)
/* 34 */       return EmptyBlockModel.INSTANCE; 
/* 36 */     Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
/* 37 */     return new SpecialBlockModelWrapper<>(baked, modelTransform);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\SpecialBlockModelWrapper$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */