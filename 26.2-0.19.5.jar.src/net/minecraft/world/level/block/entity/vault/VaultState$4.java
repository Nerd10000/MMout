/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ enum null {
/*    */   protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/* 41 */     serverLevel.playSound(null, pos, SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS);
/*    */   }
/*    */   
/*    */   protected void onExit(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData) {
/* 46 */     serverLevel.playSound(null, pos, SoundEvents.VAULT_CLOSE_SHUTTER, SoundSource.BLOCKS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultState$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */