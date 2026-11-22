/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.SignBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SignItem extends StandingAndWallBlockItem {
/*    */   public SignItem(Block sign, Block wallSign, Item.Properties properties) {
/* 15 */     super(sign, wallSign, Direction.DOWN, properties);
/*    */   }
/*    */   
/*    */   public SignItem(Item.Properties properties, Block sign, Block wallSign, Direction direction) {
/* 19 */     super(sign, wallSign, direction, properties);
/*    */   }
/*    */   
/*    */   protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState placedState) {
/* 24 */     boolean success = super.updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
/* 26 */     if (!level.isClientSide() && !success && player != null) {
/* 26 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 26 */       if (blockEntity instanceof SignBlockEntity) {
/* 26 */         SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/* 27 */         Block block = level.getBlockState(pos).getBlock();
/* 27 */         if (block instanceof SignBlock) {
/* 27 */           SignBlock sign = (SignBlock)block;
/* 28 */           sign.openTextEdit(player, signEntity, true);
/*    */         } 
/*    */       } 
/*    */     } 
/* 31 */     return success;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\SignItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */