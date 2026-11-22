/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements SlotAccess {
/*    */   public ItemStack get() {
/* 34 */     return entity.getItemBySlot(slot);
/*    */   }
/*    */   
/*    */   public boolean set(ItemStack itemStack) {
/* 39 */     if (!validator.test(itemStack))
/* 40 */       return false; 
/* 43 */     entity.setItemSlot(slot, itemStack);
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SlotAccess$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */