/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.PowerParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonFireball extends AbstractHurtingProjectile {
/*    */   public static final float SPLASH_RANGE = 4.0F;
/*    */   
/*    */   public DragonFireball(EntityType<? extends DragonFireball> type, Level level) {
/* 25 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public DragonFireball(Level level, LivingEntity mob, Vec3 direction) {
/* 29 */     super(EntityTypes.DRAGON_FIREBALL, mob, direction, level);
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 34 */     super.onHit(hitResult);
/* 35 */     if (hitResult.getType() == HitResult.Type.ENTITY && ownedBy(((EntityHitResult)hitResult).getEntity()))
/*    */       return; 
/* 38 */     if (!level().isClientSide()) {
/* 39 */       List<LivingEntity> entitiesOfClass = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
/* 41 */       AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 42 */       Entity owner = getOwner();
/* 43 */       if (owner instanceof LivingEntity) {
/* 43 */         LivingEntity livingEntity = (LivingEntity)owner;
/* 44 */         cloud.setOwner(livingEntity);
/*    */       } 
/* 46 */       cloud.setCustomParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
/* 47 */       cloud.setRadius(3.0F);
/* 48 */       cloud.setDuration(600);
/* 49 */       cloud.setRadiusPerTick((7.0F - cloud.getRadius()) / cloud.getDuration());
/* 50 */       cloud.setPotionDurationScale(0.25F);
/* 51 */       cloud.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
/* 53 */       if (!entitiesOfClass.isEmpty())
/* 54 */         for (LivingEntity entity : entitiesOfClass) {
/* 55 */           double dist = distanceToSqr((Entity)entity);
/* 56 */           if (dist < 16.0D) {
/* 57 */             cloud.setPos(entity.getX(), entity.getY(), entity.getZ());
/*    */             break;
/*    */           } 
/*    */         }  
/* 63 */       level().levelEvent(2006, blockPosition(), isSilent() ? -1 : 1);
/* 64 */       level().addFreshEntity((Entity)cloud);
/* 66 */       discard();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected ParticleOptions getTrailParticle() {
/* 72 */     return (ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F);
/*    */   }
/*    */   
/*    */   protected boolean shouldBurn() {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\DragonFireball.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */