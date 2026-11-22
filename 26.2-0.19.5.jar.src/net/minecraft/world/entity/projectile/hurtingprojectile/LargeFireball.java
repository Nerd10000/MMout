/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LargeFireball extends Fireball {
/*    */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/*    */   
/* 21 */   private int explosionPower = 1;
/*    */   
/*    */   public LargeFireball(EntityType<? extends LargeFireball> type, Level level) {
/* 24 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public LargeFireball(Level level, LivingEntity mob, Vec3 direction, int explosionPower) {
/* 28 */     super(EntityTypes.FIREBALL, mob, direction, level);
/* 29 */     this.explosionPower = explosionPower;
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 34 */     super.onHit(hitResult);
/* 35 */     Level level = level();
/* 35 */     if (level instanceof ServerLevel) {
/* 35 */       ServerLevel serverLevel = (ServerLevel)level;
/* 36 */       boolean grief = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/* 37 */       level().explode((Entity)this, getX(), getY(), getZ(), this.explosionPower, grief, Level.ExplosionInteraction.MOB);
/* 38 */       discard();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 44 */     super.onHitEntity(hitResult);
/* 46 */     Level level = level();
/* 46 */     if (level instanceof ServerLevel) {
/* 46 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/*    */       return;
/*    */     } 
/* 49 */     Entity entity = hitResult.getEntity();
/* 50 */     Entity owner = getOwner();
/* 51 */     DamageSource damageSource = damageSources().fireball(this, owner);
/* 52 */     entity.hurtServer(serverLevel, damageSource, 6.0F);
/* 53 */     EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*    */   }
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 58 */     super.addAdditionalSaveData(output);
/* 59 */     output.putByte("ExplosionPower", (byte)this.explosionPower);
/*    */   }
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 64 */     super.readAdditionalSaveData(input);
/* 65 */     this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\LargeFireball.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */