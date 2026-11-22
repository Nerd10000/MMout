/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Witch extends Raider implements RangedAttackMob {
/*  49 */   private static final Identifier SPEED_MODIFIER_DRINKING_ID = Identifier.withDefaultNamespace("drinking");
/*     */   
/*  50 */   private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_ID, -0.25D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  52 */   private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int usingTime;
/*     */   
/*     */   private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;
/*     */   
/*     */   private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
/*     */   
/*     */   public Witch(EntityType<? extends Witch> type, Level level) {
/*  60 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  65 */     super.registerGoals();
/*  68 */     this.healRaidersGoal = new NearestHealableRaiderTargetGoal(this, Raider.class, true, (target, level) -> (hasActiveRaid() && !target.is(EntityTypes.WITCH)));
/*  69 */     this.attackPlayersGoal = new NearestAttackableWitchTargetGoal(this, Player.class, 10, true, false, null);
/*  71 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  72 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 60, 10.0F));
/*  73 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  74 */     this.goalSelector.addGoal(3, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  75 */     this.goalSelector.addGoal(3, (Goal)new RandomLookAroundGoal((Mob)this));
/*  77 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class }));
/*  78 */     this.targetSelector.addGoal(2, (Goal)this.healRaidersGoal);
/*  79 */     this.targetSelector.addGoal(3, (Goal)this.attackPlayersGoal);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  84 */     super.defineSynchedData(entityData);
/*  86 */     entityData.define(DATA_USING_ITEM, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  91 */     return SoundEvents.WITCH_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  96 */     return SoundEvents.WITCH_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 101 */     return SoundEvents.WITCH_DEATH;
/*     */   }
/*     */   
/*     */   public void setUsingItem(boolean using) {
/* 105 */     getEntityData().set(DATA_USING_ITEM, Boolean.valueOf(using));
/*     */   }
/*     */   
/*     */   public boolean isDrinkingPotion() {
/* 109 */     return ((Boolean)getEntityData().get(DATA_USING_ITEM)).booleanValue();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 113 */     return Monster.createMonsterAttributes()
/* 114 */       .add(Attributes.MAX_HEALTH, 26.0D)
/* 115 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 120 */     if (!level().isClientSide() && isAlive()) {
/* 121 */       this.healRaidersGoal.decrementCooldown();
/* 123 */       if (this.healRaidersGoal.getCooldown() <= 0) {
/* 124 */         this.attackPlayersGoal.setCanAttack(true);
/*     */       } else {
/* 126 */         this.attackPlayersGoal.setCanAttack(false);
/*     */       } 
/* 129 */       if (isDrinkingPotion()) {
/* 130 */         if (this.usingTime-- <= 0) {
/* 131 */           setUsingItem(false);
/* 132 */           ItemStack itemStack = getMainHandItem();
/* 133 */           setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 135 */           PotionContents potion = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 136 */           if (itemStack.is(Items.POTION) && potion != null)
/* 137 */             potion.forEachEffect(this::addEffect, ((Float)itemStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue()); 
/* 139 */           gameEvent((Holder)GameEvent.DRINK);
/* 140 */           getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING.id());
/*     */         } 
/*     */       } else {
/* 143 */         Holder<Potion> potion = null;
/* 145 */         if (this.random.nextFloat() < 0.15F && isEyeInFluid(FluidTags.WATER) && !hasEffect(MobEffects.WATER_BREATHING)) {
/* 146 */           potion = Potions.WATER_BREATHING;
/* 147 */         } else if (this.random.nextFloat() < 0.15F && (isOnFire() || (getLastDamageSource() != null && getLastDamageSource().is(DamageTypeTags.IS_FIRE))) && !hasEffect(MobEffects.FIRE_RESISTANCE)) {
/* 148 */           potion = Potions.FIRE_RESISTANCE;
/* 149 */         } else if (this.random.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
/* 150 */           potion = Potions.HEALING;
/* 151 */         } else if (this.random.nextFloat() < 0.5F && getTarget() != null && !hasEffect(MobEffects.SPEED) && getTarget().distanceToSqr((Entity)this) > 121.0D) {
/* 152 */           potion = Potions.SWIFTNESS;
/*     */         } 
/* 155 */         if (potion != null) {
/* 156 */           setItemSlot(EquipmentSlot.MAINHAND, PotionContents.createItemStack(Items.POTION, potion));
/* 157 */           this.usingTime = getMainHandItem().getUseDuration((LivingEntity)this);
/* 158 */           setUsingItem(true);
/* 159 */           if (!isSilent())
/* 160 */             level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_DRINK, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F); 
/* 162 */           AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 163 */           speed.removeModifier(SPEED_MODIFIER_DRINKING_ID);
/* 164 */           speed.addTransientModifier(SPEED_MODIFIER_DRINKING);
/*     */         } 
/*     */       } 
/* 168 */       if (this.random.nextFloat() < 7.5E-4F)
/* 169 */         level().broadcastEntityEvent((Entity)this, (byte)15); 
/*     */     } 
/* 173 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 178 */     return SoundEvents.WITCH_CELEBRATE;
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 183 */     if (id == 15) {
/* 184 */       for (int i = 0; i < this.random.nextInt(35) + 10; i++)
/* 185 */         level().addParticle((ParticleOptions)ParticleTypes.WITCH, getX() + this.random.nextGaussian() * 0.12999999523162842D, (getBoundingBox()).maxY + 0.5D + this.random.nextGaussian() * 0.12999999523162842D, getZ() + this.random.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D); 
/*     */     } else {
/* 188 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damage) {
/* 194 */     damage = super.getDamageAfterMagicAbsorb(damageSource, damage);
/* 196 */     if (damageSource.getEntity() == this)
/* 197 */       damage = 0.0F; 
/* 199 */     if (damageSource.is(DamageTypeTags.WITCH_RESISTANT_TO))
/* 200 */       damage *= 0.15F; 
/* 203 */     return damage;
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 208 */     if (isDrinkingPotion())
/*     */       return; 
/* 212 */     Vec3 targetMovement = target.getDeltaMovement();
/* 213 */     double xd = target.getX() + targetMovement.x - getX();
/* 214 */     double yd = target.getEyeY() - 1.100000023841858D - getY();
/* 215 */     double zd = target.getZ() + targetMovement.z - getZ();
/* 216 */     double dist = Math.sqrt(xd * xd + zd * zd);
/* 217 */     Holder<Potion> potion = Potions.HARMING;
/* 220 */     if (target instanceof Raider) {
/* 221 */       if (target.getHealth() <= 4.0F) {
/* 222 */         potion = Potions.HEALING;
/*     */       } else {
/* 224 */         potion = Potions.REGENERATION;
/*     */       } 
/* 226 */       setTarget(null);
/* 228 */     } else if (dist >= 8.0D && !target.hasEffect(MobEffects.SLOWNESS)) {
/* 229 */       potion = Potions.SLOWNESS;
/* 230 */     } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
/* 231 */       potion = Potions.POISON;
/* 232 */     } else if (dist <= 3.0D && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
/* 233 */       potion = Potions.WEAKNESS;
/*     */     } 
/* 237 */     Level level = level();
/* 237 */     if (level instanceof ServerLevel) {
/* 237 */       ServerLevel serverLevel = (ServerLevel)level;
/* 238 */       ItemStack itemStack = PotionContents.createItemStack(Items.SPLASH_POTION, potion);
/* 239 */       Projectile.spawnProjectileUsingShoot(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion::new, serverLevel, itemStack, (LivingEntity)this, xd, yd + dist * 0.2D, zd, 
/*     */           
/* 243 */           (dist <= 2.0D) ? 0.45F : 0.75F, 8.0F);
/*     */     } 
/* 246 */     if (!isSilent())
/* 247 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_THROW, getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F); 
/*     */   }
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */   
/*     */   public boolean canBeLeader() {
/* 257 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Witch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */