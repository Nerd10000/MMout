/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SmallFireball extends Fireball {
/*    */   public SmallFireball(EntityType<? extends SmallFireball> type, Level level) {
/* 22 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level level, LivingEntity mob, Vec3 direction) {
/* 26 */     super(EntityTypes.SMALL_FIREBALL, mob, direction, level);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level level, double x, double y, double z, Vec3 direction) {
/* 30 */     super(EntityTypes.SMALL_FIREBALL, x, y, z, direction, level);
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 35 */     super.onHitEntity(hitResult);
/* 37 */     Level level = level();
/* 37 */     if (level instanceof ServerLevel) {
/* 37 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 40 */     Entity entity = hitResult.getEntity();
/* 41 */     Entity owner = getOwner();
/* 42 */     int remainingFireTicks = entity.getRemainingFireTicks();
/* 43 */     entity.igniteForSeconds(5.0F);
/* 44 */     DamageSource damageSource = damageSources().fireball(this, owner);
/* 45 */     if (!entity.hurtServer(serverLevel, damageSource, 5.0F)) {
/* 48 */       entity.setRemainingFireTicks(remainingFireTicks);
/*    */     } else {
/* 50 */       EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitBlock(BlockHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 56 */     super.onHitBlock(hitResult);
/* 57 */     Level level = level();
/* 57 */     if (level instanceof ServerLevel) {
/* 57 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 60 */     Entity owner = getOwner();
/* 61 */     if (!(owner instanceof net.minecraft.world.entity.Mob) || ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 62 */       BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
/* 63 */       if (level().isEmptyBlock(pos))
/* 64 */         level().setBlockAndUpdate(pos, BaseFireBlock.getState((BlockGetter)level(), pos)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 71 */     super.onHit(hitResult);
/* 72 */     if (!level().isClientSide())
/* 73 */       discard(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\SmallFireball.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */