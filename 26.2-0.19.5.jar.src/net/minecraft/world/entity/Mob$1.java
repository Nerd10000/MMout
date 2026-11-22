/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ class null implements ContainerSingleItem {
/*     */   public ItemStack getTheItem() {
/* 950 */     return Mob.this.getItemBySlot(slot);
/*     */   }
/*     */   
/*     */   public void setTheItem(ItemStack itemStack) {
/* 955 */     Mob.this.setItemSlot(slot, itemStack);
/* 956 */     if (!itemStack.isEmpty()) {
/* 957 */       Mob.this.setGuaranteedDrop(slot);
/* 958 */       Mob.this.setPersistenceRequired();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setChanged() {}
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 968 */     return (player.getVehicle() == Mob.this || player.isWithinEntityInteractionRange(Mob.this, 4.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Mob$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */