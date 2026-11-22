/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedVariants implements BlockStateModel {
/*    */   private final WeightedList<BlockStateModel> list;
/*    */   
/*    */   private final Material.Baked particleMaterial;
/*    */   
/*    */   private final int materialFlags;
/*    */   
/*    */   public WeightedVariants(WeightedList<BlockStateModel> list) {
/* 19 */     this.list = list;
/* 21 */     BlockStateModel firstModel = (BlockStateModel)((Weighted)list.unwrap().getFirst()).value();
/* 22 */     this.particleMaterial = firstModel.particleMaterial();
/* 23 */     this.materialFlags = computeMaterialFlags(list);
/*    */   }
/*    */   
/*    */   private static int computeMaterialFlags(WeightedList<BlockStateModel> list) {
/* 27 */     int flags = 0;
/* 28 */     for (Weighted<BlockStateModel> entry : (Iterable<Weighted<BlockStateModel>>)list.unwrap())
/* 29 */       flags |= ((BlockStateModel)entry.value()).materialFlags(); 
/* 31 */     return flags;
/*    */   }
/*    */   
/*    */   public Material.Baked particleMaterial() {
/* 36 */     return this.particleMaterial;
/*    */   }
/*    */   
/*    */   public int materialFlags() {
/* 41 */     return this.materialFlags;
/*    */   }
/*    */   
/*    */   public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
/* 46 */     ((BlockStateModel)this.list.getRandomOrThrow(random)).collectParts(random, output);
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements BlockStateModel.Unbaked {
/*    */     private final WeightedList<BlockStateModel.Unbaked> entries;
/*    */     
/*    */     public Unbaked(WeightedList<BlockStateModel.Unbaked> entries) {
/* 49 */       this.entries = entries;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #49	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/WeightedVariants$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public WeightedList<BlockStateModel.Unbaked> entries() {
/* 49 */       return this.entries;
/*    */     }
/*    */     
/*    */     public BlockStateModel bake(ModelBaker modelBakery) {
/* 52 */       return new WeightedVariants(this.entries.map(m -> m.bake(modelBakery)));
/*    */     }
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 57 */       this.entries.unwrap().forEach(v -> ((BlockStateModel.Unbaked)v.value()).resolveDependencies(resolver));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\WeightedVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */