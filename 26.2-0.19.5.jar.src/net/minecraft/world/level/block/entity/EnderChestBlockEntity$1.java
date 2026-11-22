/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class null extends ContainerOpenersCounter {
/*    */   protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
/* 19 */     level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/*    */   }
/*    */   
/*    */   protected void onClose(Level level, BlockPos pos, BlockState blockState) {
/* 24 */     level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/*    */   }
/*    */   
/*    */   protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
/* 29 */     level.blockEvent(EnderChestBlockEntity.this.worldPosition, Blocks.ENDER_CHEST, 1, current);
/*    */   }
/*    */   
/*    */   public boolean isOwnContainer(Player player) {
/* 34 */     return player.getEnderChestInventory().isActiveChest(EnderChestBlockEntity.this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\EnderChestBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */