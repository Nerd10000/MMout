/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import net.minecraft.client.renderer.block.model.properties.select.SelectBlockModelProperty;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class SelectBlockModel<T> implements BlockModel {
/*    */   private final SelectBlockModelProperty<T> property;
/*    */   
/*    */   private final ModelSelector<T> models;
/*    */   
/*    */   public SelectBlockModel(SelectBlockModelProperty<T> property, ModelSelector<T> models) {
/* 21 */     this.property = property;
/* 22 */     this.models = models;
/*    */   }
/*    */   
/*    */   public void update(BlockModelRenderState output, BlockState blockState, BlockDisplayContext displayContext, long seed) {
/* 27 */     T value = (T)this.property.get(blockState, displayContext);
/* 28 */     BlockModel model = this.models.get(value);
/* 29 */     if (model != null)
/* 30 */       model.update(output, blockState, displayContext, seed); 
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */     private final Optional<Transformation> transformation;
/*    */     
/*    */     private final SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch;
/*    */     
/*    */     private final Optional<BlockModel.Unbaked> fallback;
/*    */     
/*    */     public Unbaked(Optional<Transformation> transformation, SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch, Optional<BlockModel.Unbaked> fallback) {
/* 34 */       this.transformation = transformation;
/* 34 */       this.unbakedSwitch = unbakedSwitch;
/* 34 */       this.fallback = fallback;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #34	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<Transformation> transformation() {
/* 34 */       return this.transformation;
/*    */     }
/*    */     
/*    */     public SelectBlockModel.UnbakedSwitch<?, ?> unbakedSwitch() {
/* 34 */       return this.unbakedSwitch;
/*    */     }
/*    */     
/*    */     public Optional<BlockModel.Unbaked> fallback() {
/* 34 */       return this.fallback;
/*    */     }
/*    */     
/*    */     public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 41 */       Matrix4fc childTransform = Transformation.compose(transformation, this.transformation);
/* 42 */       BlockModel bakedFallback = this.fallback.<BlockModel>map(m -> m.bake(context, childTransform)).orElse(context.missingBlockModel());
/* 43 */       return this.unbakedSwitch.bake(context, childTransform, bakedFallback);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class UnbakedSwitch<P extends SelectBlockModelProperty<T>, T> extends Record {
/*    */     private final P property;
/*    */     
/*    */     private final List<SelectBlockModel.SwitchCase<T>> cases;
/*    */     
/*    */     public UnbakedSwitch(P property, List<SelectBlockModel.SwitchCase<T>> cases) {
/* 47 */       this.property = property;
/* 47 */       this.cases = cases;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */     }
/*    */     
/*    */     public P property() {
/* 47 */       return this.property;
/*    */     }
/*    */     
/*    */     public List<SelectBlockModel.SwitchCase<T>> cases() {
/* 47 */       return this.cases;
/*    */     }
/*    */     
/*    */     public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation, BlockModel fallback) {
/* 52 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/* 53 */       for (SelectBlockModel.SwitchCase<T> c : this.cases) {
/* 54 */         BlockModel.Unbaked caseModel = c.model;
/* 56 */         BlockModel bakedCaseModel = caseModel.bake(context, transformation);
/* 57 */         for (T value : c.values)
/* 58 */           object2ObjectOpenHashMap.put(value, bakedCaseModel); 
/*    */       } 
/* 62 */       object2ObjectOpenHashMap.defaultReturnValue(fallback);
/* 63 */       Objects.requireNonNull(object2ObjectOpenHashMap);
/* 63 */       return new SelectBlockModel((SelectBlockModelProperty<?>)this.property, object2ObjectOpenHashMap::get);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class SwitchCase<T> extends Record {
/*    */     private final List<T> values;
/*    */     
/*    */     private final BlockModel.Unbaked model;
/*    */     
/*    */     public SwitchCase(List<T> values, BlockModel.Unbaked model) {
/* 67 */       this.values = values;
/* 67 */       this.model = model;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #67	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$SwitchCase<TT;>;
/*    */     }
/*    */     
/*    */     public List<T> values() {
/* 67 */       return this.values;
/*    */     }
/*    */     
/*    */     public BlockModel.Unbaked model() {
/* 67 */       return this.model;
/*    */     }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ModelSelector<T> {
/*    */     BlockModel get(T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\SelectBlockModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */