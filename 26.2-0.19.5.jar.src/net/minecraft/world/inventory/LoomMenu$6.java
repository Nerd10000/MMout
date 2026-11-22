/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null extends Slot {
/*     */   null(Container container, int slot, int x, int y) {
/*  94 */     super(container, slot, x, y);
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack itemStack) {
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   public void onTake(Player player, ItemStack carried) {
/* 102 */     LoomMenu.this.bannerSlot.remove(1);
/* 103 */     LoomMenu.this.dyeSlot.remove(1);
/* 104 */     if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem())
/* 105 */       LoomMenu.this.selectedBannerPatternIndex.set(-1); 
/* 107 */     access.execute((level, pos) -> {
/*     */           long gameTime = level.getGameTime();
/*     */           if (LoomMenu.this.lastSoundTime != gameTime) {
/*     */             level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */             LoomMenu.this.lastSoundTime = gameTime;
/*     */           } 
/*     */         });
/* 117 */     super.onTake(player, carried);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\LoomMenu$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */