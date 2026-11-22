/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements SlotAccess {
/*    */   public ItemStack get() {
/* 19 */     return getter.get();
/*    */   }
/*    */   
/*    */   public boolean set(ItemStack itemStack) {
/* 24 */     setter.accept(itemStack);
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SlotAccess$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */