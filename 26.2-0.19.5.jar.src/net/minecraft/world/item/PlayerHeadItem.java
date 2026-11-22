/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class PlayerHeadItem extends StandingAndWallBlockItem {
/*    */   public PlayerHeadItem(Block block, Block wallBlock, Item.Properties properties) {
/* 11 */     super(block, wallBlock, Direction.DOWN, properties);
/*    */   }
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 16 */     ResolvableProfile profile = (ResolvableProfile)itemStack.get(DataComponents.PROFILE);
/* 17 */     if (profile != null && profile.name().isPresent())
/* 18 */       return (Component)Component.translatable(this.descriptionId + ".named", new Object[] { profile.name().get() }); 
/* 20 */     return super.getName(itemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\PlayerHeadItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */