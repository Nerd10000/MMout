/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class EnchantedItemInUse extends Record {
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   private final EquipmentSlot inSlot;
/*    */   
/*    */   private final LivingEntity owner;
/*    */   
/*    */   private final Consumer<Item> onBreak;
/*    */   
/*    */   public EnchantedItemInUse(ItemStack itemStack, EquipmentSlot inSlot, LivingEntity owner, Consumer<Item> onBreak) {
/* 11 */     this.itemStack = itemStack;
/* 11 */     this.inSlot = inSlot;
/* 11 */     this.owner = owner;
/* 11 */     this.onBreak = onBreak;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/EnchantedItemInUse;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStack itemStack() {
/* 11 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public EquipmentSlot inSlot() {
/* 11 */     return this.inSlot;
/*    */   }
/*    */   
/*    */   public LivingEntity owner() {
/* 11 */     return this.owner;
/*    */   }
/*    */   
/*    */   public Consumer<Item> onBreak() {
/* 11 */     return this.onBreak;
/*    */   }
/*    */   
/*    */   public EnchantedItemInUse(ItemStack itemStack, EquipmentSlot inSlot, LivingEntity owner) {
/* 13 */     this(itemStack, inSlot, owner, item -> owner.onEquippedItemBroken(item, inSlot));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\EnchantedItemInUse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */