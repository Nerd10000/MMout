/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import net.minecraft.client.renderer.block.model.properties.select.SelectBlockModelProperty;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class UnbakedSwitch<P extends SelectBlockModelProperty<T>, T> extends Record {
/*    */   private final P property;
/*    */   
/*    */   private final List<SelectBlockModel.SwitchCase<T>> cases;
/*    */   
/*    */   public UnbakedSwitch(P property, List<SelectBlockModel.SwitchCase<T>> cases) {
/* 47 */     this.property = property;
/* 47 */     this.cases = cases;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/SelectBlockModel$UnbakedSwitch<TP;TT;>;
/*    */   }
/*    */   
/*    */   public P property() {
/* 47 */     return this.property;
/*    */   }
/*    */   
/*    */   public List<SelectBlockModel.SwitchCase<T>> cases() {
/* 47 */     return this.cases;
/*    */   }
/*    */   
/*    */   public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation, BlockModel fallback) {
/* 52 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
/* 53 */     for (SelectBlockModel.SwitchCase<T> c : this.cases) {
/* 54 */       BlockModel.Unbaked caseModel = c.model;
/* 56 */       BlockModel bakedCaseModel = caseModel.bake(context, transformation);
/* 57 */       for (T value : c.values)
/* 58 */         object2ObjectOpenHashMap.put(value, bakedCaseModel); 
/*    */     } 
/* 62 */     object2ObjectOpenHashMap.defaultReturnValue(fallback);
/* 63 */     Objects.requireNonNull(object2ObjectOpenHashMap);
/* 63 */     return new SelectBlockModel((SelectBlockModelProperty<?>)this.property, object2ObjectOpenHashMap::get);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\SelectBlockModel$UnbakedSwitch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */