/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.WallHangingSignBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HangingSignItem extends SignItem {
/*    */   public HangingSignItem(Block hangingSign, Block wallHangingSign, Item.Properties properties) {
/* 12 */     super(properties, hangingSign, wallHangingSign, Direction.UP);
/*    */   }
/*    */   
/*    */   protected boolean canPlace(LevelReader level, BlockState possibleState, BlockPos pos) {
/* 17 */     Block block = possibleState.getBlock();
/* 17 */     if (block instanceof WallHangingSignBlock) {
/* 17 */       WallHangingSignBlock hangingSign = (WallHangingSignBlock)block;
/* 18 */       if (!hangingSign.canPlace(possibleState, level, pos))
/* 19 */         return false; 
/*    */     } 
/* 22 */     return super.canPlace(level, possibleState, pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\HangingSignItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */