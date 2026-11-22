/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SingleVariant implements BlockStateModel {
/*    */   private final BlockStateModelPart model;
/*    */   
/*    */   public SingleVariant(BlockStateModelPart model) {
/* 15 */     this.model = model;
/*    */   }
/*    */   
/*    */   public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
/* 20 */     output.add(this.model);
/*    */   }
/*    */   
/*    */   public Material.Baked particleMaterial() {
/* 25 */     return this.model.particleMaterial();
/*    */   }
/*    */   
/*    */   public int materialFlags() {
/* 30 */     return this.model.materialFlags();
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements BlockStateModel.Unbaked {
/*    */     private final Variant variant;
/*    */     
/*    */     public Unbaked(Variant variant) {
/* 33 */       this.variant = variant;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/SingleVariant$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Variant variant() {
/* 33 */       return this.variant;
/*    */     }
/*    */     
/* 34 */     public static final Codec<Unbaked> CODEC = Variant.CODEC.xmap(Unbaked::new, Unbaked::variant);
/*    */     
/*    */     public BlockStateModel bake(ModelBaker modelBakery) {
/* 38 */       return new SingleVariant(this.variant.bake(modelBakery));
/*    */     }
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 43 */       this.variant.resolveDependencies(resolver);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\SingleVariant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */