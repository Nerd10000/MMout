/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.CompoundContainer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.ChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class null extends ContainerOpenersCounter {
/*    */   protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
/* 37 */     Block block = blockState.getBlock();
/* 37 */     if (block instanceof ChestBlock) {
/* 37 */       ChestBlock chestBlock = (ChestBlock)block;
/* 38 */       ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getOpenChestSound());
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onClose(Level level, BlockPos pos, BlockState blockState) {
/* 44 */     Block block = blockState.getBlock();
/* 44 */     if (block instanceof ChestBlock) {
/* 44 */       ChestBlock chestBlock = (ChestBlock)block;
/* 45 */       ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getCloseChestSound());
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
/* 51 */     ChestBlockEntity.this.signalOpenCount(level, pos, blockState, previous, current);
/*    */   }
/*    */   
/*    */   public boolean isOwnContainer(Player player) {
/* 56 */     if (player.containerMenu instanceof ChestMenu) {
/* 57 */       Container container = ((ChestMenu)player.containerMenu).getContainer();
/* 58 */       if (container != ChestBlockEntity.this) {
/* 58 */         if (container instanceof CompoundContainer) {
/* 58 */           CompoundContainer compoundContainer = (CompoundContainer)container;
/* 58 */           if (compoundContainer.contains(ChestBlockEntity.this));
/*    */         } 
/* 58 */         return false;
/*    */       } 
/*    */     } else {
/* 60 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\ChestBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */