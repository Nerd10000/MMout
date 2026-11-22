/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class null implements MenuProvider {
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 255 */     if (first.canOpen(player) && second.canOpen(player)) {
/* 256 */       first.unpackLootTable(inventory.player);
/* 257 */       second.unpackLootTable(inventory.player);
/* 259 */       return (AbstractContainerMenu)ChestMenu.sixRows(containerId, inventory, container);
/*     */     } 
/* 261 */     Direction connectedDirection = ChestBlock.getConnectedDirection(first.getBlockState());
/* 262 */     Vec3 firstCenter = Vec3.atCenterOf((Vec3i)first.getBlockPos());
/* 263 */     Vec3 centerBetweenChests = firstCenter.add(connectedDirection.getStepX() / 2.0D, 0.0D, connectedDirection.getStepZ() / 2.0D);
/* 264 */     BaseContainerBlockEntity.sendChestLockedNotifications(centerBetweenChests, player, getDisplayName());
/* 266 */     return null;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 271 */     if (first.hasCustomName())
/* 272 */       return first.getDisplayName(); 
/* 274 */     if (second.hasCustomName())
/* 275 */       return second.getDisplayName(); 
/* 277 */     return (Component)Component.translatable("container.chestDouble");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChestBlock$2$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */