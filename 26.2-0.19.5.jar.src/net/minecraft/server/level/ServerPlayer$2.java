/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerListener;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class null implements ContainerListener {
/*     */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack changedItem) {
/* 352 */     Slot slot = container.getSlot(slotIndex);
/* 353 */     if (slot instanceof net.minecraft.world.inventory.ResultSlot)
/*     */       return; 
/* 357 */     if (slot.container == ServerPlayer.this.getInventory())
/* 359 */       CriteriaTriggers.INVENTORY_CHANGED.trigger(ServerPlayer.this, ServerPlayer.this.getInventory(), changedItem); 
/*     */   }
/*     */   
/*     */   public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */