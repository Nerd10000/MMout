/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ 
/*      */ class null implements SlotAccess {
/*      */   public ItemStack get() {
/* 1829 */     return Player.this.containerMenu.getCarried();
/*      */   }
/*      */   
/*      */   public boolean set(ItemStack itemStack) {
/* 1834 */     Player.this.containerMenu.setCarried(itemStack);
/* 1835 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Player$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */