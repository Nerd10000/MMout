/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GameMasterBlockItem extends BlockItem {
/*    */   public GameMasterBlockItem(Block block, Item.Properties properties) {
/* 11 */     super(block, properties);
/*    */   }
/*    */   
/*    */   protected BlockState getPlacementState(BlockPlaceContext context) {
/* 16 */     Player player = context.getPlayer();
/* 17 */     return (player == null || player.canUseGameMasterBlocks()) ? super.getPlacementState(context) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\GameMasterBlockItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */