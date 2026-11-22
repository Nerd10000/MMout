/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class null implements SlotAccess {
/*     */   public ItemStack get() {
/* 114 */     return Container.this.getItem(slot);
/*     */   }
/*     */   
/*     */   public boolean set(ItemStack itemStack) {
/* 119 */     Container.this.setItem(slot, itemStack);
/* 120 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\Container$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */