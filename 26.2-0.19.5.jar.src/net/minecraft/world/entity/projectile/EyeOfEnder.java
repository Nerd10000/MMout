/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EyeOfEnder extends Entity implements ItemSupplier {
/*     */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/*     */   
/*     */   private static final float TOO_FAR_SIGNAL_HEIGHT = 8.0F;
/*     */   
/*     */   private static final float TOO_FAR_DISTANCE = 12.0F;
/*     */   
/*  29 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private Vec3 target;
/*     */   
/*     */   private int life;
/*     */   
/*     */   private boolean surviveAfterDeath;
/*     */   
/*     */   public EyeOfEnder(EntityType<? extends EyeOfEnder> type, Level level) {
/*  36 */     super(type, level);
/*     */   }
/*     */   
/*     */   public EyeOfEnder(Level level, double x, double y, double z) {
/*  40 */     this(EntityTypes.EYE_OF_ENDER, level);
/*  42 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack source) {
/*  46 */     if (source.isEmpty()) {
/*  47 */       getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
/*     */     } else {
/*  49 */       getEntityData().set(DATA_ITEM_STACK, source.copyWithCount(1));
/*     */     } 
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  55 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  60 */     entityData.define(DATA_ITEM_STACK, getDefaultItem());
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/*  65 */     if (this.tickCount < 2 && distance < 12.25D)
/*  66 */       return false; 
/*  68 */     double size = getBoundingBox().getSize() * 4.0D;
/*  69 */     if (Double.isNaN(size))
/*  70 */       size = 4.0D; 
/*  72 */     size *= 64.0D;
/*  73 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   public void signalTo(Vec3 target) {
/*  77 */     Vec3 delta = target.subtract(position());
/*  79 */     double horizontalDistance = delta.horizontalDistance();
/*  80 */     if (horizontalDistance > 12.0D) {
/*  81 */       this.target = position().add(delta.x / horizontalDistance * 12.0D, 8.0D, delta.z / horizontalDistance * 12.0D);
/*     */     } else {
/*  87 */       this.target = target;
/*     */     } 
/*  90 */     this.life = 0;
/*  91 */     this.surviveAfterDeath = (this.random.nextInt(5) > 0);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  96 */     super.tick();
/*  98 */     Vec3 newPosition = position().add(getDeltaMovement());
/*  99 */     if (!level().isClientSide() && this.target != null)
/* 100 */       setDeltaMovement(updateDeltaMovement(getDeltaMovement(), newPosition, this.target)); 
/* 103 */     if (level().isClientSide()) {
/* 104 */       Vec3 particleOrigin = newPosition.subtract(getDeltaMovement().scale(0.25D));
/* 105 */       spawnParticles(particleOrigin, getDeltaMovement());
/*     */     } 
/* 108 */     setPos(newPosition);
/* 110 */     if (!level().isClientSide()) {
/* 111 */       this.life++;
/* 112 */       if (this.life > 80 && !level().isClientSide()) {
/* 113 */         playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
/* 114 */         discard();
/* 115 */         if (this.surviveAfterDeath) {
/* 116 */           level().addFreshEntity((Entity)new ItemEntity(level(), getX(), getY(), getZ(), getItem()));
/*     */         } else {
/* 118 */           level().levelEvent(2003, blockPosition(), 0);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnParticles(Vec3 origin, Vec3 movement) {
/* 125 */     if (isInWater()) {
/* 126 */       for (int i = 0; i < 4; i++)
/* 127 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, origin.x, origin.y, origin.z, movement.x, movement.y, movement.z); 
/*     */     } else {
/* 130 */       level().addParticle((ParticleOptions)ParticleTypes.PORTAL, origin.x + this.random
/* 131 */           .nextDouble() * 0.6D - 0.3D, origin.y - 0.5D, origin.z + this.random
/*     */           
/* 133 */           .nextDouble() * 0.6D - 0.3D, movement.x, movement.y, movement.z);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Vec3 updateDeltaMovement(Vec3 oldMovement, Vec3 position, Vec3 target) {
/* 140 */     Vec3 horizontalDelta = new Vec3(target.x - position.x, 0.0D, target.z - position.z);
/* 141 */     double horizontalLength = horizontalDelta.length();
/* 142 */     double wantedSpeed = Mth.lerp(0.0025D, oldMovement.horizontalDistance(), horizontalLength);
/* 143 */     double movementY = oldMovement.y;
/* 144 */     if (horizontalLength < 1.0D) {
/* 145 */       wantedSpeed *= 0.8D;
/* 146 */       movementY *= 0.8D;
/*     */     } 
/* 148 */     double wantedMovementY = (position.y - oldMovement.y < target.y) ? 1.0D : -1.0D;
/* 149 */     return horizontalDelta.scale(wantedSpeed / horizontalLength)
/* 150 */       .add(0.0D, movementY + (wantedMovementY - movementY) * 0.015D, 0.0D);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 155 */     output.store("Item", ItemStack.CODEC, getItem());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 160 */     setItem(input.read("Item", ItemStack.CODEC).orElse(getDefaultItem()));
/*     */   }
/*     */   
/*     */   private ItemStack getDefaultItem() {
/* 164 */     return new ItemStack((ItemLike)Items.ENDER_EYE);
/*     */   }
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 169 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 179 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\EyeOfEnder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */