/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.ChargedProjectiles;
/*    */ 
/*    */ public final class Charge extends Record implements SelectItemModelProperty<CrossbowItem.ChargeType> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/Charge;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/Charge;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/Charge;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/Charge;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/Charge;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/Charge;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/* 16 */   public static final Codec<CrossbowItem.ChargeType> VALUE_CODEC = CrossbowItem.ChargeType.CODEC;
/*    */   
/* 17 */   public static final SelectItemModelProperty.Type<Charge, CrossbowItem.ChargeType> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit(new Charge()), VALUE_CODEC);
/*    */   
/*    */   public CrossbowItem.ChargeType get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 21 */     ChargedProjectiles projectiles = (ChargedProjectiles)itemStack.get(DataComponents.CHARGED_PROJECTILES);
/* 22 */     if (projectiles == null || projectiles.isEmpty())
/* 23 */       return CrossbowItem.ChargeType.NONE; 
/* 26 */     if (projectiles.contains(Items.FIREWORK_ROCKET))
/* 27 */       return CrossbowItem.ChargeType.ROCKET; 
/* 31 */     return CrossbowItem.ChargeType.ARROW;
/*    */   }
/*    */   
/*    */   public SelectItemModelProperty.Type<Charge, CrossbowItem.ChargeType> type() {
/* 36 */     return TYPE;
/*    */   }
/*    */   
/*    */   public Codec<CrossbowItem.ChargeType> valueCodec() {
/* 41 */     return VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\select\Charge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */