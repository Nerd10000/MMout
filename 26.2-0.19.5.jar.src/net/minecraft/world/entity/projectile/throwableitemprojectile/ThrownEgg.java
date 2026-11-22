/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.particles.ItemParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownEgg extends ThrowableItemProjectile {
/* 25 */   private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);
/*    */   
/*    */   public ThrownEgg(EntityType<? extends ThrownEgg> type, Level level) {
/* 28 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level level, LivingEntity mob, ItemStack itemStack) {
/* 32 */     super(EntityTypes.EGG, mob, level, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownEgg(Level level, double x, double y, double z, ItemStack itemStack) {
/* 36 */     super(EntityTypes.EGG, x, y, z, level, itemStack);
/*    */   }
/*    */   
/*    */   public void handleEntityEvent(byte id) {
/* 41 */     if (id == 3) {
/* 42 */       ItemStack item = getItem();
/* 43 */       if (!item.isEmpty()) {
/* 44 */         ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(item));
/* 45 */         for (int i = 0; i < 8; i++)
/* 46 */           level().addParticle((ParticleOptions)breakParticle, getX(), getY(), getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult hitResult) {
/* 54 */     super.onHitEntity(hitResult);
/* 55 */     hitResult.getEntity().hurt(damageSources().thrown((Entity)this, getOwner()), 0.0F);
/*    */   }
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 60 */     super.onHit(hitResult);
/* 62 */     if (!level().isClientSide()) {
/* 63 */       if (this.random.nextInt(8) == 0) {
/* 64 */         int count = 1;
/* 65 */         if (this.random.nextInt(32) == 0)
/* 66 */           count = 4; 
/* 68 */         for (int i = 0; i < count; i++) {
/* 69 */           Chicken chicken = (Chicken)EntityTypes.CHICKEN.create(level(), EntitySpawnReason.TRIGGERED);
/* 70 */           if (chicken != null) {
/* 71 */             chicken.setAge(-24000);
/* 72 */             chicken.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 75 */             Objects.requireNonNull(chicken);
/* 75 */             Optional.<Holder>ofNullable((Holder)getItem().get(DataComponents.CHICKEN_VARIANT)).ifPresent(chicken::setVariant);
/* 77 */             if (!chicken.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS))
/*    */               break; 
/* 80 */             level().addFreshEntity((Entity)chicken);
/*    */           } 
/*    */         } 
/*    */       } 
/* 85 */       level().broadcastEntityEvent((Entity)this, (byte)3);
/* 86 */       discard();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Item getDefaultItem() {
/* 92 */     return Items.EGG;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEgg.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */