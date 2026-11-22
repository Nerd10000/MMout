/*    */ package net.minecraft.world.entity.projectile.arrow;
/*    */ 
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SpellParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class SpectralArrow extends AbstractArrow {
/*    */   private static final int DEFAULT_DURATION = 200;
/*    */   
/* 22 */   private int duration = 200;
/*    */   
/*    */   public SpectralArrow(EntityType<? extends SpectralArrow> type, Level level) {
/* 25 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 29 */     super(EntityTypes.SPECTRAL_ARROW, owner, level, pickupItemStack, firedFromWeapon);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 33 */     super(EntityTypes.SPECTRAL_ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 38 */     super.tick();
/* 40 */     if (level().isClientSide() && !isInGround())
/* 41 */       level().addParticle((ParticleOptions)SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D); 
/*    */   }
/*    */   
/*    */   protected void doPostHurtEffects(LivingEntity mob) {
/* 47 */     super.doPostHurtEffects(mob);
/* 49 */     MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
/* 50 */     mob.addEffect(effect, getEffectSource());
/*    */   }
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 55 */     super.readAdditionalSaveData(input);
/* 56 */     this.duration = input.getIntOr("Duration", 200);
/*    */   }
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 61 */     super.addAdditionalSaveData(output);
/* 62 */     output.putInt("Duration", this.duration);
/*    */   }
/*    */   
/*    */   protected ItemStack getDefaultPickupItem() {
/* 67 */     return new ItemStack((ItemLike)Items.SPECTRAL_ARROW);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\arrow\SpectralArrow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */