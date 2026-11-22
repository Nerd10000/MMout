/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ItemUtils {
/*    */   public static InteractionResult startUsingInstantly(Level level, Player player, InteractionHand hand) {
/* 15 */     player.startUsingItem(hand);
/* 16 */     return (InteractionResult)InteractionResult.CONSUME;
/*    */   }
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack itemStack, Player player, ItemStack newItemStack, boolean limitCreativeStackSize) {
/* 23 */     boolean isCreative = player.hasInfiniteMaterials();
/* 24 */     if (limitCreativeStackSize && isCreative) {
/* 25 */       if (!player.getInventory().contains(newItemStack))
/* 26 */         player.getInventory().add(newItemStack); 
/* 28 */       return itemStack;
/*    */     } 
/* 31 */     itemStack.consume(1, (LivingEntity)player);
/* 32 */     if (itemStack.isEmpty())
/* 33 */       return newItemStack; 
/* 35 */     if (!player.getInventory().add(newItemStack))
/* 36 */       player.drop(newItemStack, false); 
/* 38 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack itemStack, Player player, ItemStack newItemStack) {
/* 42 */     return createFilledResult(itemStack, player, newItemStack, true);
/*    */   }
/*    */   
/*    */   public static void onContainerDestroyed(ItemEntity container, Stream<ItemStack> contents) {
/* 46 */     Level level = container.level();
/* 47 */     if (level.isClientSide())
/*    */       return; 
/* 51 */     contents.forEach(stack -> level.addFreshEntity((Entity)new ItemEntity(level, container.getX(), container.getY(), container.getZ(), stack)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */