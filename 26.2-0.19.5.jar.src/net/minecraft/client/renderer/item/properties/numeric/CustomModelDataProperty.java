/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataProperty extends Record implements RangeSelectItemModelProperty {
/*    */   private final int index;
/*    */   
/*    */   public static final MapCodec<CustomModelDataProperty> MAP_CODEC;
/*    */   
/*    */   public CustomModelDataProperty(int index) {
/* 13 */     this.index = index;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int index() {
/* 13 */     return this.index;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", Integer.valueOf(0)).forGetter(CustomModelDataProperty::index)).apply((Applicative)i, CustomModelDataProperty::new));
/*    */   }
/*    */   
/*    */   public float get(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed) {
/* 20 */     CustomModelData customModelData = (CustomModelData)itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
/* 21 */     if (customModelData != null) {
/* 22 */       Float value = customModelData.getFloat(this.index);
/* 23 */       if (value != null)
/* 24 */         return value.floatValue(); 
/*    */     } 
/* 27 */     return 0.0F;
/*    */   }
/*    */   
/*    */   public MapCodec<CustomModelDataProperty> type() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\CustomModelDataProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */