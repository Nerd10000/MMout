/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class FuelSlot extends Slot {
/*     */   public FuelSlot(Container container, int slot, int x, int y) {
/* 182 */     super(container, slot, x, y);
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/* 187 */     return mayPlaceItem(itemStack);
/*     */   }
/*     */   
/*     */   public static boolean mayPlaceItem(ItemStack itemStack) {
/* 191 */     return itemStack.is(ItemTags.BREWING_FUEL);
/*     */   }
/*     */   
/*     */   public Identifier getNoItemIcon() {
/* 196 */     return BrewingStandMenu.EMPTY_SLOT_FUEL;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\BrewingStandMenu$FuelSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */