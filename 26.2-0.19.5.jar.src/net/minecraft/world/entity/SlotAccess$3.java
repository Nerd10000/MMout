/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements SlotAccess {
/*    */   public ItemStack get() {
/* 57 */     return stacks.get(index);
/*    */   }
/*    */   
/*    */   public boolean set(ItemStack itemStack) {
/* 62 */     stacks.set(index, itemStack);
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SlotAccess$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */