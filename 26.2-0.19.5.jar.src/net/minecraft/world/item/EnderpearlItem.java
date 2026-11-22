/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EnderpearlItem extends Item {
/*    */   public static final float PROJECTILE_SHOOT_POWER = 1.5F;
/*    */   
/*    */   public EnderpearlItem(Item.Properties properties) {
/* 19 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 24 */     ItemStack itemStack = player.getItemInHand(hand);
/* 26 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/* 27 */     if (level instanceof ServerLevel) {
/* 27 */       ServerLevel serverLevel = (ServerLevel)level;
/* 28 */       Projectile.spawnProjectileFromRotation(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl::new, serverLevel, itemStack, (LivingEntity)player, 0.0F, 1.5F, 1.0F);
/*    */     } 
/* 30 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 31 */     itemStack.consume(1, (LivingEntity)player);
/* 32 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\EnderpearlItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */