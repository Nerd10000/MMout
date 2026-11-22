/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataSource extends Record implements ItemTintSource {
/*    */   private final int index;
/*    */   
/*    */   private final int defaultColor;
/*    */   
/*    */   public static final MapCodec<CustomModelDataSource> MAP_CODEC;
/*    */   
/*    */   public CustomModelDataSource(int index, int defaultColor) {
/* 14 */     this.index = index;
/* 14 */     this.defaultColor = defaultColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/CustomModelDataSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/CustomModelDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/CustomModelDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/CustomModelDataSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int index() {
/* 14 */     return this.index;
/*    */   }
/*    */   
/*    */   public int defaultColor() {
/* 14 */     return this.defaultColor;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", Integer.valueOf(0)).forGetter(CustomModelDataSource::index), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(CustomModelDataSource::defaultColor)).apply((Applicative)i, CustomModelDataSource::new));
/*    */   }
/*    */   
/*    */   public int calculate(ItemStack itemStack, ClientLevel level, LivingEntity owner) {
/* 25 */     CustomModelData customModelData = (CustomModelData)itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
/* 26 */     if (customModelData != null) {
/* 27 */       Integer value = customModelData.getColor(this.index);
/* 28 */       if (value != null)
/* 29 */         return ARGB.opaque(value.intValue()); 
/*    */     } 
/* 32 */     return ARGB.opaque(this.defaultColor);
/*    */   }
/*    */   
/*    */   public MapCodec<CustomModelDataSource> type() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\item\CustomModelDataSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */