/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class PaymentSlot extends Slot {
/*     */   public PaymentSlot(Container container, int slot, int x, int y) {
/* 175 */     super(container, slot, x, y);
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/* 180 */     return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/* 185 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\BeaconMenu$PaymentSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */