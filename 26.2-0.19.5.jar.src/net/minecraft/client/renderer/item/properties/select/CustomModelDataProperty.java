/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataProperty extends Record implements SelectItemModelProperty<String> {
/*    */   private final int index;
/*    */   
/*    */   public CustomModelDataProperty(int index) {
/* 15 */     this.index = index;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int index() {
/* 15 */     return this.index;
/*    */   }
/*    */   
/* 16 */   public static final PrimitiveCodec<String> VALUE_CODEC = Codec.STRING;
/*    */   
/*    */   public static final SelectItemModelProperty.Type<CustomModelDataProperty, String> TYPE;
/*    */   
/*    */   static {
/* 18 */     TYPE = SelectItemModelProperty.Type.create(
/* 19 */         RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", Integer.valueOf(0)).forGetter(CustomModelDataProperty::index)).apply((Applicative)i, CustomModelDataProperty::new)), (Codec<String>)VALUE_CODEC);
/*    */   }
/*    */   
/*    */   public String get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 27 */     CustomModelData customModelData = (CustomModelData)itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
/* 28 */     if (customModelData != null)
/* 29 */       return customModelData.getString(this.index); 
/* 31 */     return null;
/*    */   }
/*    */   
/*    */   public SelectItemModelProperty.Type<CustomModelDataProperty, String> type() {
/* 36 */     return TYPE;
/*    */   }
/*    */   
/*    */   public Codec<String> valueCodec() {
/* 41 */     return (Codec<String>)VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\select\CustomModelDataProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */