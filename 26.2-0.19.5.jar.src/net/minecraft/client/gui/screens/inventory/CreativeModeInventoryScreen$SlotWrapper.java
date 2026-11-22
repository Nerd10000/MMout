/*      */ package net.minecraft.client.gui.screens.inventory;
/*      */ 
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ 
/*      */ class SlotWrapper extends Slot {
/*      */   private final Slot target;
/*      */   
/*      */   public SlotWrapper(Slot target, int index, int x, int y) {
/*  964 */     super(target.container, index, x, y);
/*  965 */     this.target = target;
/*      */   }
/*      */   
/*      */   public void onTake(Player player, ItemStack carried) {
/*  970 */     this.target.onTake(player, carried);
/*      */   }
/*      */   
/*      */   public boolean mayPlace(ItemStack itemStack) {
/*  975 */     return this.target.mayPlace(itemStack);
/*      */   }
/*      */   
/*      */   public ItemStack getItem() {
/*  980 */     return this.target.getItem();
/*      */   }
/*      */   
/*      */   public boolean hasItem() {
/*  985 */     return this.target.hasItem();
/*      */   }
/*      */   
/*      */   public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/*  990 */     this.target.setByPlayer(itemStack, previous);
/*      */   }
/*      */   
/*      */   public void set(ItemStack itemStack) {
/*  995 */     this.target.set(itemStack);
/*      */   }
/*      */   
/*      */   public void setChanged() {
/* 1000 */     this.target.setChanged();
/*      */   }
/*      */   
/*      */   public int getMaxStackSize() {
/* 1005 */     return this.target.getMaxStackSize();
/*      */   }
/*      */   
/*      */   public int getMaxStackSize(ItemStack itemStack) {
/* 1010 */     return this.target.getMaxStackSize(itemStack);
/*      */   }
/*      */   
/*      */   public Identifier getNoItemIcon() {
/* 1015 */     return this.target.getNoItemIcon();
/*      */   }
/*      */   
/*      */   public ItemStack remove(int amount) {
/* 1020 */     return this.target.remove(amount);
/*      */   }
/*      */   
/*      */   public boolean isActive() {
/* 1025 */     return this.target.isActive();
/*      */   }
/*      */   
/*      */   public boolean mayPickup(Player player) {
/* 1030 */     return this.target.mayPickup(player);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CreativeModeInventoryScreen$SlotWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */