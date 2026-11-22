/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.item.SelectItemModel;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ComponentContents<T> extends Record implements SelectItemModelProperty<T> {
/*    */   private final DataComponentType<T> componentType;
/*    */   
/*    */   public ComponentContents(DataComponentType<T> componentType) {
/* 15 */     this.componentType = componentType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/*    */   }
/*    */   
/*    */   public DataComponentType<T> componentType() {
/* 15 */     return this.componentType;
/*    */   }
/*    */   
/* 20 */   private static final SelectItemModelProperty.Type<? extends ComponentContents<?>, ?> TYPE = createType();
/*    */   
/*    */   private static <T> SelectItemModelProperty.Type<ComponentContents<T>, T> createType() {
/* 25 */     Codec<? extends DataComponentType<?>> rawComponentCodec = BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().validate(t -> t.isTransient() ? DataResult.error(()) : DataResult.success(t));
/* 31 */     Codec<? extends DataComponentType<?>> codec1 = rawComponentCodec;
/* 33 */     MapCodec<SelectItemModel.UnbakedSwitch<ComponentContents<T>, T>> switchCodec = codec1.dispatchMap("component", switchObject -> ((ComponentContents)switchObject.property()).componentType, componentType -> SelectItemModelProperty.Type.<T>createCasesFieldCodec(componentType.codecOrThrow()).xmap((), SelectItemModel.UnbakedSwitch::cases));
/* 43 */     return new SelectItemModelProperty.Type<>(switchCodec);
/*    */   }
/*    */   
/*    */   public static <T> SelectItemModelProperty.Type<ComponentContents<T>, T> castType() {
/* 49 */     return (SelectItemModelProperty.Type)TYPE;
/*    */   }
/*    */   
/*    */   public T get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 54 */     return (T)itemStack.get(this.componentType);
/*    */   }
/*    */   
/*    */   public SelectItemModelProperty.Type<ComponentContents<T>, T> type() {
/* 59 */     return castType();
/*    */   }
/*    */   
/*    */   public Codec<T> valueCodec() {
/* 64 */     return this.componentType.codecOrThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\select\ComponentContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */