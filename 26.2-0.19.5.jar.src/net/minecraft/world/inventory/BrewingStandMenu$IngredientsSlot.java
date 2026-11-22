/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ 
/*     */ class IngredientsSlot extends Slot {
/*     */   private final PotionBrewing potionBrewing;
/*     */   
/*     */   public IngredientsSlot(PotionBrewing potionBrewing, Container container, int slot, int x, int y) {
/* 170 */     super(container, slot, x, y);
/* 171 */     this.potionBrewing = potionBrewing;
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/* 176 */     return this.potionBrewing.isIngredient(itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\BrewingStandMenu$IngredientsSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */