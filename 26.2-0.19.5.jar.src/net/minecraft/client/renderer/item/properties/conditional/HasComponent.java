/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class HasComponent extends Record implements ConditionalItemModelProperty {
/*    */   private final DataComponentType<?> componentType;
/*    */   
/*    */   private final boolean ignoreDefault;
/*    */   
/*    */   public static final MapCodec<HasComponent> MAP_CODEC;
/*    */   
/*    */   public HasComponent(DataComponentType<?> componentType, boolean ignoreDefault) {
/* 14 */     this.componentType = componentType;
/* 14 */     this.ignoreDefault = ignoreDefault;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/HasComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DataComponentType<?> componentType() {
/* 14 */     return this.componentType;
/*    */   }
/*    */   
/*    */   public boolean ignoreDefault() {
/* 14 */     return this.ignoreDefault;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().fieldOf("component").forGetter(HasComponent::componentType), (App)Codec.BOOL.optionalFieldOf("ignore_default", Boolean.valueOf(false)).forGetter(HasComponent::ignoreDefault)).apply((Applicative)i, HasComponent::new));
/*    */   }
/*    */   
/*    */   public boolean get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 22 */     return this.ignoreDefault ? itemStack.hasNonDefault(this.componentType) : itemStack.has(this.componentType);
/*    */   }
/*    */   
/*    */   public MapCodec<HasComponent> type() {
/* 27 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\conditional\HasComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */