/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ 
/*      */ class null implements SlotAccess {
/*      */   public ItemStack get() {
/* 1844 */     return Player.this.inventoryMenu.getCraftSlots().getItem(craftSlot);
/*      */   }
/*      */   
/*      */   public boolean set(ItemStack itemStack) {
/* 1849 */     Player.this.inventoryMenu.getCraftSlots().setItem(craftSlot, itemStack);
/* 1850 */     Player.this.inventoryMenu.slotsChanged(Player.this.inventory);
/* 1851 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Player$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */