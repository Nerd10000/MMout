/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedItem extends BlockItem {
/*    */   public BedItem(Block block, Item.Properties properties) {
/*  9 */     super(block, properties);
/*    */   }
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
/* 14 */     return context.getLevel().setBlock(context.getClickedPos(), placementState, 26);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BedItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */