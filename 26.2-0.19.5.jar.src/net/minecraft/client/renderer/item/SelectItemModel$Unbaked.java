/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.math.Transformation;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Unbaked extends Record implements ItemModel.Unbaked {
/*    */   private final Optional<Transformation> transformation;
/*    */   
/*    */   private final SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch;
/*    */   
/*    */   private final Optional<ItemModel.Unbaked> fallback;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(Optional<Transformation> transformation, SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch, Optional<ItemModel.Unbaked> fallback) {
/* 41 */     this.transformation = transformation;
/* 41 */     this.unbakedSwitch = unbakedSwitch;
/* 41 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/SelectItemModel$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Transformation> transformation() {
/* 41 */     return this.transformation;
/*    */   }
/*    */   
/*    */   public SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch() {
/* 41 */     return this.unbakedSwitch;
/*    */   }
/*    */   
/*    */   public Optional<ItemModel.Unbaked> fallback() {
/* 41 */     return this.fallback;
/*    */   }
/*    */   
/*    */   static {
/* 46 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)SelectItemModel.UnbakedSwitch.MAP_CODEC.forGetter(Unbaked::unbakedSwitch), (App)ItemModels.CODEC.optionalFieldOf("fallback").forGetter(Unbaked::fallback)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 54 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 59 */     Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 60 */     ItemModel bakedFallback = this.fallback.<ItemModel>map(m -> m.bake(context, childTransform)).orElseGet(() -> context.missingItemModel(childTransform));
/* 61 */     return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
/*    */   }
/*    */   
/*    */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 66 */     this.unbakedSwitch.resolveDependencies(resolver);
/* 67 */     this.fallback.ifPresent(m -> m.resolveDependencies(resolver));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SelectItemModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */