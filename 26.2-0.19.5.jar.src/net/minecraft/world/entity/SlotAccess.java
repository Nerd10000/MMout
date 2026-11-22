/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface SlotAccess {
/*    */   static SlotAccess of(final Supplier<ItemStack> getter, final Consumer<ItemStack> setter) {
/* 16 */     return new SlotAccess() {
/*    */         public ItemStack get() {
/* 19 */           return getter.get();
/*    */         }
/*    */         
/*    */         public boolean set(ItemStack itemStack) {
/* 24 */           setter.accept(itemStack);
/* 25 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static SlotAccess forEquipmentSlot(final LivingEntity entity, final EquipmentSlot slot, final Predicate<ItemStack> validator) {
/* 31 */     return new SlotAccess() {
/*    */         public ItemStack get() {
/* 34 */           return entity.getItemBySlot(slot);
/*    */         }
/*    */         
/*    */         public boolean set(ItemStack itemStack) {
/* 39 */           if (!validator.test(itemStack))
/* 40 */             return false; 
/* 43 */           entity.setItemSlot(slot, itemStack);
/* 44 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static SlotAccess forEquipmentSlot(LivingEntity entity, EquipmentSlot slot) {
/* 50 */     return forEquipmentSlot(entity, slot, stack -> true);
/*    */   }
/*    */   
/*    */   static SlotAccess forListElement(final List<ItemStack> stacks, final int index) {
/* 54 */     return new SlotAccess() {
/*    */         public ItemStack get() {
/* 57 */           return stacks.get(index);
/*    */         }
/*    */         
/*    */         public boolean set(ItemStack itemStack) {
/* 62 */           stacks.set(index, itemStack);
/* 63 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   ItemStack get();
/*    */   
/*    */   boolean set(ItemStack paramItemStack);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SlotAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */