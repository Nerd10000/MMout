/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class WritableBookItem extends Item {
/*    */   public WritableBookItem(Item.Properties properties) {
/* 11 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 16 */     ItemStack itemStack = player.getItemInHand(hand);
/* 17 */     player.openItemGui(itemStack, hand);
/* 18 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 19 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\WritableBookItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */