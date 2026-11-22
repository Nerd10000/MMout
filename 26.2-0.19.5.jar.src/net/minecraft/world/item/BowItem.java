/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BowItem extends ProjectileWeaponItem {
/*    */   public static final int MAX_DRAW_DURATION = 20;
/*    */   
/*    */   public static final int DEFAULT_RANGE = 15;
/*    */   
/*    */   public BowItem(Item.Properties properties) {
/* 24 */     super(properties);
/*    */   }
/*    */   
/*    */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/*    */     Player player;
/* 29 */     if (entity instanceof Player) {
/* 29 */       player = (Player)entity;
/*    */     } else {
/* 30 */       return false;
/*    */     } 
/* 33 */     ItemStack projectile = player.getProjectile(itemStack);
/* 35 */     if (projectile.isEmpty())
/* 36 */       return false; 
/* 39 */     int timeHeld = getUseDuration(itemStack, entity) - remainingTime;
/* 40 */     float pow = getPowerForTime(timeHeld);
/* 41 */     if (pow < 0.1D)
/* 42 */       return false; 
/* 45 */     List<ItemStack> firedProjectiles = draw(itemStack, projectile, (LivingEntity)player);
/* 46 */     if (level instanceof ServerLevel) {
/* 46 */       ServerLevel serverLevel = (ServerLevel)level;
/* 46 */       if (!firedProjectiles.isEmpty())
/* 47 */         shoot(serverLevel, (LivingEntity)player, player.getUsedItemHand(), itemStack, firedProjectiles, pow * 3.0F, 1.0F, (pow == 1.0F), null); 
/*    */     } 
/* 50 */     level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
/* 51 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 52 */     return true;
/*    */   }
/*    */   
/*    */   protected void shootProjectile(LivingEntity shooter, Projectile projectileEntity, int index, float power, float uncertainty, float angle, LivingEntity targetOverrride) {
/* 57 */     projectileEntity.shootFromRotation((Entity)shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, power, uncertainty);
/*    */   }
/*    */   
/*    */   public static float getPowerForTime(int timeHeld) {
/* 61 */     float pow = timeHeld / 20.0F;
/* 62 */     pow = (pow * pow + pow * 2.0F) / 3.0F;
/* 63 */     if (pow > 1.0F)
/* 64 */       pow = 1.0F; 
/* 66 */     return pow;
/*    */   }
/*    */   
/*    */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 71 */     return 72000;
/*    */   }
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 76 */     return ItemUseAnimation.BOW;
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 81 */     ItemStack itemStack = player.getItemInHand(hand);
/* 82 */     boolean foundProjectile = !player.getProjectile(itemStack).isEmpty();
/* 83 */     if (player.hasInfiniteMaterials() || foundProjectile) {
/* 84 */       player.startUsingItem(hand);
/* 85 */       return (InteractionResult)InteractionResult.CONSUME;
/*    */     } 
/* 87 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */   
/*    */   public Predicate<ItemStack> getAllSupportedProjectiles() {
/* 92 */     return ARROW_ONLY;
/*    */   }
/*    */   
/*    */   public int getDefaultProjectileRange() {
/* 97 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BowItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */