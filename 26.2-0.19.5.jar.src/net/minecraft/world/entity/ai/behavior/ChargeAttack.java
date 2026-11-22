/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ChargeAttack extends Behavior<Animal> {
/*     */   private final int timeBetweenAttacks;
/*     */   
/*     */   private final TargetingConditions chargeTargeting;
/*     */   
/*     */   private final float speed;
/*     */   
/*     */   private final float knockbackForce;
/*     */   
/*     */   private final double maxTargetDetectionDistance;
/*     */   
/*     */   private final double maxChargeDistance;
/*     */   
/*     */   private final SoundEvent chargeSound;
/*     */   
/*     */   private Vec3 chargeVelocityVector;
/*     */   
/*     */   private Vec3 startPosition;
/*     */   
/*     */   public ChargeAttack(int timeBetweenAttacks, TargetingConditions chargeTargeting, float speed, float knockbackForce, double maxChargeDistance, double maxTargetDetectionDistance, SoundEvent chargeSound) {
/*  50 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
/*  54 */     this.timeBetweenAttacks = timeBetweenAttacks;
/*  55 */     this.chargeTargeting = chargeTargeting;
/*  56 */     this.speed = speed;
/*  57 */     this.knockbackForce = knockbackForce;
/*  58 */     this.maxChargeDistance = maxChargeDistance;
/*  59 */     this.maxTargetDetectionDistance = maxTargetDetectionDistance;
/*  60 */     this.chargeSound = chargeSound;
/*  61 */     this.chargeVelocityVector = Vec3.ZERO;
/*  62 */     this.startPosition = Vec3.ZERO;
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Animal body) {
/*  67 */     return body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Animal body, long timestamp) {
/*  72 */     Brain<?> brain = body.getBrain();
/*  73 */     Optional<LivingEntity> attackCandidate = brain.getMemory(MemoryModuleType.ATTACK_TARGET);
/*  74 */     if (attackCandidate.isEmpty())
/*  75 */       return false; 
/*  77 */     LivingEntity attackTarget = attackCandidate.get();
/*  80 */     if (body instanceof TamableAnimal) {
/*  80 */       TamableAnimal tamedAnimal = (TamableAnimal)body;
/*  80 */       if (tamedAnimal.isTame())
/*  81 */         return false; 
/*     */     } 
/*  84 */     if (body.position().subtract(this.startPosition).lengthSqr() >= this.maxChargeDistance * this.maxChargeDistance)
/*  85 */       return false; 
/*  87 */     if (attackTarget.position().subtract(body.position()).lengthSqr() >= this.maxTargetDetectionDistance * this.maxTargetDetectionDistance)
/*  88 */       return false; 
/*  91 */     if (!body.hasLineOfSight((Entity)attackTarget))
/*  92 */       return false; 
/*  94 */     return !brain.hasMemoryValue(MemoryModuleType.CHARGE_COOLDOWN_TICKS);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Animal body, long timestamp) {
/*  99 */     Brain<?> brain = body.getBrain();
/* 100 */     this.startPosition = body.position();
/* 101 */     LivingEntity attackCandidate = brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
/* 102 */     Vec3 direction = attackCandidate.position().subtract(body.position()).normalize();
/* 103 */     this.chargeVelocityVector = direction.scale(this.speed);
/* 104 */     if (canStillUse(level, body, timestamp))
/* 105 */       body.playSound(this.chargeSound); 
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Animal body, long timestamp) {
/* 111 */     Brain<?> brain = body.getBrain();
/* 112 */     LivingEntity attackTarget = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
/* 113 */     body.lookAt((Entity)attackTarget, 360.0F, 360.0F);
/* 114 */     body.setDeltaMovement(this.chargeVelocityVector);
/* 116 */     List<LivingEntity> collidingEntities = new ArrayList<>(1);
/* 117 */     level.getEntities(EntityTypeTest.forClass(LivingEntity.class), body.getBoundingBox(), e -> this.chargeTargeting.test(level, (LivingEntity)body, e), collidingEntities, 1);
/* 118 */     if (!collidingEntities.isEmpty()) {
/* 119 */       LivingEntity closestAttackTarget = collidingEntities.get(0);
/* 120 */       if (body.hasPassenger((Entity)closestAttackTarget))
/*     */         return; 
/* 123 */       dealDamageToTarget(level, body, closestAttackTarget);
/* 124 */       dealKnockBack(level, body, closestAttackTarget);
/* 125 */       stop(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dealDamageToTarget(ServerLevel level, Animal body, LivingEntity target) {
/* 130 */     DamageSource damageSource = level.damageSources().mobAttack((LivingEntity)body);
/* 131 */     float damage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 132 */     if (target.hurtServer(level, damageSource, damage))
/* 133 */       EnchantmentHelper.doPostAttackEffects(level, (Entity)target, damageSource); 
/*     */   }
/*     */   
/*     */   private void dealKnockBack(ServerLevel level, Animal body, LivingEntity target) {
/* 139 */     int movementSpeedLevel = body.hasEffect(MobEffects.SPEED) ? (body.getEffect(MobEffects.SPEED).getAmplifier() + 1) : 0;
/* 140 */     int movementSlowdownLevel = body.hasEffect(MobEffects.SLOWNESS) ? (body.getEffect(MobEffects.SLOWNESS).getAmplifier() + 1) : 0;
/* 141 */     float speedBoostPower = 0.25F * (movementSpeedLevel - movementSlowdownLevel);
/* 142 */     float speedFactor = Mth.clamp(this.speed * (float)body.getAttributeValue(Attributes.MOVEMENT_SPEED), 0.2F, 2.0F) + speedBoostPower;
/* 143 */     DamageSource damageSource = level.damageSources().mobAttack((LivingEntity)body);
/* 144 */     float damage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 145 */     body.causeExtraKnockback((Entity)target, speedFactor * this.knockbackForce, body.getDeltaMovement(), damageSource, damage, false);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Animal body, long timestamp) {
/* 150 */     body.getBrain().setMemory(MemoryModuleType.CHARGE_COOLDOWN_TICKS, Integer.valueOf(this.timeBetweenAttacks));
/* 151 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ChargeAttack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */