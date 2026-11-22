/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class null extends Slot {
/*    */   null(Container container, int slot, int x, int y) {
/* 68 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 71 */     return false;
/*    */   }
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 76 */     ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
/* 77 */     ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
/* 79 */     carried.getItem().onCraftedBy(carried, player);
/* 81 */     access.execute((level, pos) -> {
/*    */           long gameTime = level.getGameTime();
/*    */           if (CartographyTableMenu.this.lastSoundTime != gameTime) {
/*    */             level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */             CartographyTableMenu.this.lastSoundTime = gameTime;
/*    */           } 
/*    */         });
/* 90 */     super.onTake(player, carried);
/*    */   }
/*    */   
/*    */   public ItemStack safeClone(Player player) {
/* 95 */     ItemStack result = super.safeClone(player);
/* 96 */     result.getItem().onCraftedBy(result, player);
/* 97 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\CartographyTableMenu$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */