/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class ThrowablePotionItem extends PotionItem implements ProjectileItem {
/*    */   public static final float PROJECTILE_SHOOT_POWER = 0.5F;
/*    */   
/*    */   public ThrowablePotionItem(Item.Properties properties) {
/* 20 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 25 */     ItemStack itemStack = player.getItemInHand(hand);
/* 26 */     if (level instanceof ServerLevel) {
/* 26 */       ServerLevel serverLevel = (ServerLevel)level;
/* 27 */       Projectile.spawnProjectileFromRotation(this::createPotion, serverLevel, itemStack, (LivingEntity)player, -20.0F, 0.5F, 1.0F);
/*    */     } 
/* 29 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 30 */     itemStack.consume(1, (LivingEntity)player);
/* 31 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */   
/*    */   protected abstract AbstractThrownPotion createPotion(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack);
/*    */   
/*    */   protected abstract AbstractThrownPotion createPotion(Level paramLevel, Position paramPosition, ItemStack paramItemStack);
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 40 */     return (Projectile)createPotion(level, position, itemStack);
/*    */   }
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 45 */     return ProjectileItem.DispenseConfig.builder()
/* 46 */       .uncertainty(ProjectileItem.DispenseConfig.DEFAULT.uncertainty() * 0.5F)
/* 47 */       .power(ProjectileItem.DispenseConfig.DEFAULT.power() * 1.25F)
/* 48 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ThrowablePotionItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */