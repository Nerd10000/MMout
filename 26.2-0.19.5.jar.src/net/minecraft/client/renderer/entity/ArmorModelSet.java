/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public final class ArmorModelSet<T> extends Record {
/*    */   private final T head;
/*    */   
/*    */   private final T chest;
/*    */   
/*    */   private final T legs;
/*    */   
/*    */   private final T feet;
/*    */   
/*    */   public ArmorModelSet(T head, T chest, T legs, T feet) {
/* 13 */     this.head = head;
/* 13 */     this.chest = chest;
/* 13 */     this.legs = legs;
/* 13 */     this.feet = feet;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>;
/*    */   }
/*    */   
/*    */   public T head() {
/* 13 */     return this.head;
/*    */   }
/*    */   
/*    */   public T chest() {
/* 13 */     return this.chest;
/*    */   }
/*    */   
/*    */   public T legs() {
/* 13 */     return this.legs;
/*    */   }
/*    */   
/*    */   public T feet() {
/* 13 */     return this.feet;
/*    */   }
/*    */   
/*    */   public T get(EquipmentSlot slot) {
/* 20 */     switch (slot) {
/*    */       case HEAD:
/*    */       
/*    */       case CHEST:
/*    */       
/*    */       case LEGS:
/*    */       
/*    */       case FEET:
/*    */       
/*    */     } 
/* 25 */     throw new IllegalStateException("No model for slot: " + String.valueOf(slot));
/*    */   }
/*    */   
/*    */   public <U> ArmorModelSet<U> map(Function<? super T, ? extends U> mapper) {
/* 30 */     return new ArmorModelSet((T)mapper.apply(this.head), (T)mapper.apply(this.chest), (T)mapper.apply(this.legs), (T)mapper.apply(this.feet));
/*    */   }
/*    */   
/*    */   public void putFrom(ArmorModelSet<LayerDefinition> values, ImmutableMap.Builder<T, LayerDefinition> output) {
/* 34 */     output.put(this.head, (LayerDefinition)values.head);
/* 35 */     output.put(this.chest, (LayerDefinition)values.chest);
/* 36 */     output.put(this.legs, (LayerDefinition)values.legs);
/* 37 */     output.put(this.feet, (LayerDefinition)values.feet);
/*    */   }
/*    */   
/*    */   public static <M extends HumanoidModel<?>> ArmorModelSet<M> bake(ArmorModelSet<ModelLayerLocation> locations, EntityModelSet modelSet, Function<ModelPart, M> factory) {
/* 45 */     return locations.map(id -> (HumanoidModel)factory.apply(modelSet.bakeLayer(id)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ArmorModelSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */