/*      */ package net.minecraft.client.gui.screens.inventory;
/*      */ 
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ 
/*      */ class CustomCreativeSlot extends Slot {
/*      */   public CustomCreativeSlot(Container container, int slot, int x, int y) {
/* 1036 */     super(container, slot, x, y);
/*      */   }
/*      */   
/*      */   public boolean mayPickup(Player player) {
/* 1041 */     ItemStack item = getItem();
/* 1042 */     if (super.mayPickup(player) && !item.isEmpty())
/* 1043 */       return (item.isItemEnabled(player.level().enabledFeatures()) && 
/* 1044 */         !item.has(DataComponents.CREATIVE_SLOT_LOCK)); 
/* 1046 */     return item.isEmpty();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CreativeModeInventoryScreen$CustomCreativeSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */