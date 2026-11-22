/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
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
/* 64 */     super(container, slot, x, y);
/*    */   }
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 72 */     carried.onCraftedBy(player, carried.getCount());
/* 73 */     StonecutterMenu.this.resultContainer.awardUsedRecipes(player, getRelevantItems());
/* 76 */     ItemStack remaining = StonecutterMenu.this.inputSlot.remove(1);
/* 77 */     if (!remaining.isEmpty())
/* 78 */       StonecutterMenu.this.setupResultSlot(StonecutterMenu.this.selectedRecipeIndex.get()); 
/* 81 */     access.execute((level, pos) -> {
/*    */           long gameTime = level.getGameTime();
/*    */           if (StonecutterMenu.this.lastSoundTime != gameTime) {
/*    */             level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */             StonecutterMenu.this.lastSoundTime = gameTime;
/*    */           } 
/*    */         });
/* 90 */     super.onTake(player, carried);
/*    */   }
/*    */   
/*    */   private List<ItemStack> getRelevantItems() {
/* 94 */     return List.of(StonecutterMenu.this.inputSlot
/* 95 */         .getItem());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\StonecutterMenu$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */