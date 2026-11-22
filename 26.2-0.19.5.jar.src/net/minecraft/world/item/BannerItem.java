/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class BannerItem extends StandingAndWallBlockItem {
/*    */   public BannerItem(Block block, Block wallBlock, Item.Properties properties) {
/* 10 */     super(block, wallBlock, Direction.DOWN, properties);
/* 12 */     Validate.isInstanceOf(AbstractBannerBlock.class, block);
/* 13 */     Validate.isInstanceOf(AbstractBannerBlock.class, wallBlock);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 17 */     return ((AbstractBannerBlock)getBlock()).getColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BannerItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */