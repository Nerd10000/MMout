/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.math.Transformation;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*    */   private final List<ItemModel.Unbaked> models;
/*    */   
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(List<ItemModel.Unbaked> models, Optional<Transformation> transformation) {
/* 33 */     this.models = models;
/* 33 */     this.transformation = transformation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/CompositeModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ItemModel.Unbaked> models() {
/* 33 */     return this.models;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 33 */     return this.transformation;
/*    */   }
/*    */   
/*    */   static {
/* 37 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemModels.CODEC.listOf().fieldOf("models").forGetter(Unbaked::models), (App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 44 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 49 */     for (ItemModel.Unbaked model : this.models)
/* 50 */       model.resolveDependencies(resolver); 
/*    */   }
/*    */   
/*    */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 56 */     if (this.models.isEmpty())
/* 57 */       return EmptyModel.INSTANCE; 
/* 59 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 60 */     if (this.models.size() == 1)
/* 61 */       return ((ItemModel.Unbaked)this.models.getFirst()).bake(context, childTransform); 
/* 63 */     return new CompositeModel(this.models.stream().map(m -> m.bake(context, childTransform)).toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\CompositeModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */