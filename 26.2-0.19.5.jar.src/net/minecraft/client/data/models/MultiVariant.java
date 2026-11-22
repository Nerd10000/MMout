/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.block.dispatch.SingleVariant;
/*    */ import net.minecraft.client.renderer.block.dispatch.Variant;
/*    */ import net.minecraft.client.renderer.block.dispatch.VariantMutator;
/*    */ import net.minecraft.client.renderer.block.dispatch.WeightedVariants;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public final class MultiVariant extends Record {
/*    */   private final WeightedList<Variant> variants;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/MultiVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/MultiVariant;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/MultiVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/MultiVariant;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/MultiVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/MultiVariant;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WeightedList<Variant> variants() {
/* 13 */     return this.variants;
/*    */   }
/*    */   
/*    */   public MultiVariant(WeightedList<Variant> variants) {
/* 15 */     if (variants.isEmpty())
/* 16 */       throw new IllegalArgumentException("Variant list must contain at least one element"); 
/*    */     this.variants = variants;
/*    */   }
/*    */   
/*    */   public MultiVariant with(VariantMutator mutator) {
/* 21 */     return new MultiVariant(this.variants.map((Function)mutator));
/*    */   }
/*    */   
/*    */   public BlockStateModel.Unbaked toUnbaked() {
/* 25 */     List<Weighted<Variant>> entries = this.variants.unwrap();
/* 26 */     return (entries.size() == 1) ? 
/* 27 */       (BlockStateModel.Unbaked)new SingleVariant.Unbaked((Variant)((Weighted)entries.getFirst()).value()) : 
/* 28 */       (BlockStateModel.Unbaked)new WeightedVariants.Unbaked(this.variants.map(net.minecraft.client.renderer.block.dispatch.SingleVariant.Unbaked::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\MultiVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */