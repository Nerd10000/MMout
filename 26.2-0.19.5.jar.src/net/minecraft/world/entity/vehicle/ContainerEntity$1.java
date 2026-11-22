/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class null implements SlotAccess {
/*     */   public ItemStack get() {
/* 166 */     return ContainerEntity.this.getChestVehicleItem(slot);
/*     */   }
/*     */   
/*     */   public boolean set(ItemStack itemStack) {
/* 171 */     ContainerEntity.this.setChestVehicleItem(slot, itemStack);
/* 172 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\ContainerEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */