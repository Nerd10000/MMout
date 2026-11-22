/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EmptyMapItem extends Item {
/*    */   public EmptyMapItem(Item.Properties properties) {
/* 13 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*    */     ServerLevel serverLevel;
/* 18 */     ItemStack itemStack = player.getItemInHand(hand);
/* 20 */     if (level instanceof ServerLevel) {
/* 20 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/* 21 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 24 */     itemStack.consume(1, (LivingEntity)player);
/* 26 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 27 */     serverLevel.playSound(null, (Entity)player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
/* 29 */     ItemStack map = MapItem.create(serverLevel, player.getBlockX(), player.getBlockZ(), (byte)0, true, false);
/* 30 */     if (itemStack.isEmpty())
/* 31 */       return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(map); 
/* 33 */     if (!player.getInventory().add(map.copy()))
/* 34 */       player.drop(map, false); 
/* 37 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\EmptyMapItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */