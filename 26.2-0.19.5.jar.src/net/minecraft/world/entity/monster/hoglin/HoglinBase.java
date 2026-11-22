/*    */ package net.minecraft.world.entity.monster.hoglin;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface HoglinBase {
/*    */   public static final int ATTACK_ANIMATION_DURATION = 10;
/*    */   
/*    */   public static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*    */   
/*    */   int getAttackAnimationRemainingTicks();
/*    */   
/*    */   static boolean hurtAndThrowTarget(ServerLevel level, LivingEntity body, LivingEntity target) {
/* 20 */     float actualDamage, attackDamage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 21 */     if (!body.isBaby() && (int)attackDamage > 0) {
/* 22 */       actualDamage = attackDamage / 2.0F + level.getRandom().nextInt((int)attackDamage);
/*    */     } else {
/* 24 */       actualDamage = attackDamage;
/*    */     } 
/* 27 */     DamageSource damageSource = body.damageSources().mobAttack(body);
/* 28 */     boolean wasHurt = target.hurtServer(level, damageSource, actualDamage);
/* 29 */     if (wasHurt) {
/* 30 */       EnchantmentHelper.doPostAttackEffects(level, (Entity)target, damageSource);
/* 31 */       if (!body.isBaby())
/* 32 */         throwTarget(body, target); 
/*    */     } 
/* 35 */     return wasHurt;
/*    */   }
/*    */   
/*    */   static void throwTarget(LivingEntity body, LivingEntity target) {
/* 39 */     double knockbackPower = body.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 40 */     double knockbackResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 41 */     double effectiveKnockbackPower = knockbackPower - knockbackResistance;
/* 42 */     if (effectiveKnockbackPower <= 0.0D)
/*    */       return; 
/* 46 */     double xd = target.getX() - body.getX();
/* 47 */     double zd = target.getZ() - body.getZ();
/* 48 */     RandomSource random = body.level().getRandom();
/* 49 */     float horizontalPushAngle = (random.nextInt(21) - 10);
/* 50 */     double horizontalScale = effectiveKnockbackPower * (random.nextFloat() * 0.5F + 0.2F);
/* 51 */     Vec3 horizontalPushVector = (new Vec3(xd, 0.0D, zd)).normalize().scale(horizontalScale).yRot(horizontalPushAngle);
/* 53 */     double verticalScale = effectiveKnockbackPower * random.nextFloat() * 0.5D;
/* 54 */     target.push(horizontalPushVector.x, verticalScale, horizontalPushVector.z);
/* 55 */     target.hurtMarked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\hoglin\HoglinBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */