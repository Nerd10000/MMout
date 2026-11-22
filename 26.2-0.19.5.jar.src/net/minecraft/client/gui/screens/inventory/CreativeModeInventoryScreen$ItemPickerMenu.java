/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ItemPickerMenu extends AbstractContainerMenu {
/* 125 */   public final NonNullList<ItemStack> items = NonNullList.create();
/*     */   
/*     */   private final AbstractContainerMenu inventoryMenu;
/*     */   
/*     */   public ItemPickerMenu(Player player) {
/* 130 */     super(null, 0);
/* 131 */     this.inventoryMenu = (AbstractContainerMenu)player.inventoryMenu;
/* 133 */     Inventory inventory = player.getInventory();
/* 134 */     for (int y = 0; y < 5; y++) {
/* 135 */       for (int x = 0; x < 9; x++)
/* 136 */         addSlot(new CreativeModeInventoryScreen.CustomCreativeSlot((Container)CreativeModeInventoryScreen.CONTAINER, y * 9 + x, 9 + x * 18, 18 + y * 18)); 
/*     */     } 
/* 140 */     addInventoryHotbarSlots((Container)inventory, 9, 112);
/* 142 */     scrollTo(0.0F);
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   protected int calculateRowCount() {
/* 151 */     return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
/*     */   }
/*     */   
/*     */   protected int getRowIndexForScroll(float scrollOffs) {
/* 155 */     return Math.max((int)((scrollOffs * calculateRowCount()) + 0.5D), 0);
/*     */   }
/*     */   
/*     */   protected float getScrollForRowIndex(int rowIndex) {
/* 159 */     return Mth.clamp(rowIndex / calculateRowCount(), 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   protected float subtractInputFromScroll(float scrollOffs, double input) {
/* 163 */     return Mth.clamp(scrollOffs - (float)(input / calculateRowCount()), 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void scrollTo(float scrollOffs) {
/* 167 */     int rowToScrollTo = getRowIndexForScroll(scrollOffs);
/* 168 */     for (int y = 0; y < 5; y++) {
/* 169 */       for (int x = 0; x < 9; x++) {
/* 170 */         int slot = x + (y + rowToScrollTo) * 9;
/* 171 */         if (slot >= 0 && slot < this.items.size()) {
/* 172 */           CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, (ItemStack)this.items.get(slot));
/*     */         } else {
/* 174 */           CreativeModeInventoryScreen.CONTAINER.setItem(x + y * 9, ItemStack.EMPTY);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canScroll() {
/* 181 */     return (this.items.size() > 45);
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 186 */     if (slotIndex >= this.slots.size() - 9 && slotIndex < this.slots.size()) {
/* 187 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 189 */       if (slot != null && slot.hasItem())
/* 190 */         slot.setByPlayer(ItemStack.EMPTY); 
/*     */     } 
/* 194 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
/* 199 */     return (target.container != CreativeModeInventoryScreen.CONTAINER);
/*     */   }
/*     */   
/*     */   public boolean canDragTo(Slot slot) {
/* 204 */     return (slot.container != CreativeModeInventoryScreen.CONTAINER);
/*     */   }
/*     */   
/*     */   public ItemStack getCarried() {
/* 209 */     return this.inventoryMenu.getCarried();
/*     */   }
/*     */   
/*     */   public void setCarried(ItemStack carried) {
/* 214 */     this.inventoryMenu.setCarried(carried);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CreativeModeInventoryScreen$ItemPickerMenu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */