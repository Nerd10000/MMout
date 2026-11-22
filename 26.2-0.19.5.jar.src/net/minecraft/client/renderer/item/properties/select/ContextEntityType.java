/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ContextEntityType extends Record implements SelectItemModelProperty<ResourceKey<EntityType<?>>> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ContextEntityType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/* 15 */   public static final Codec<ResourceKey<EntityType<?>>> VALUE_CODEC = ResourceKey.codec(Registries.ENTITY_TYPE);
/*    */   
/* 17 */   public static final SelectItemModelProperty.Type<ContextEntityType, ResourceKey<EntityType<?>>> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit(new ContextEntityType()), VALUE_CODEC);
/*    */   
/*    */   public ResourceKey<EntityType<?>> get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 21 */     return (owner == null) ? null : owner.typeHolder().unwrapKey().orElse(null);
/*    */   }
/*    */   
/*    */   public SelectItemModelProperty.Type<ContextEntityType, ResourceKey<EntityType<?>>> type() {
/* 26 */     return TYPE;
/*    */   }
/*    */   
/*    */   public Codec<ResourceKey<EntityType<?>>> valueCodec() {
/* 31 */     return VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\select\ContextEntityType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */