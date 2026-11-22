/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FireworkRocketEntity extends Projectile implements ItemSupplier {
/*  42 */   private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*  43 */   private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*     */   
/*  44 */   private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DEFAULT_LIFE = 0;
/*     */   
/*     */   private static final int DEFAULT_LIFE_TIME = 0;
/*     */   
/*     */   private static final boolean DEFAULT_SHOT_AT_ANGLE = false;
/*     */   
/*  49 */   private int life = 0;
/*     */   
/*  50 */   private int lifetime = 0;
/*     */   
/*     */   private LivingEntity attachedToEntity;
/*     */   
/*     */   public FireworkRocketEntity(EntityType<? extends FireworkRocketEntity> type, Level level) {
/*  54 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, double x, double y, double z, ItemStack sourceItemStack) {
/*  58 */     super(EntityTypes.FIREWORK_ROCKET, level);
/*  59 */     this.life = 0;
/*  61 */     setPos(x, y, z);
/*  63 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, sourceItemStack.copy());
/*  64 */     int flightCount = 1;
/*  65 */     Fireworks fireworks = (Fireworks)sourceItemStack.get(DataComponents.FIREWORKS);
/*  66 */     if (fireworks != null)
/*  67 */       flightCount += fireworks.flightDuration(); 
/*  69 */     setDeltaMovement(this.random
/*  70 */         .triangle(0.0D, 0.002297D), 0.05D, this.random
/*     */         
/*  72 */         .triangle(0.0D, 0.002297D));
/*  75 */     this.lifetime = 10 * flightCount + this.random.nextInt(6) + this.random.nextInt(7);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, Entity owner, double x, double y, double z, ItemStack sourceItemStack) {
/*  79 */     this(level, x, y, z, sourceItemStack);
/*  80 */     setOwner(owner);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, LivingEntity stuckTo) {
/*  84 */     this(level, (Entity)stuckTo, stuckTo.getX(), stuckTo.getY(), stuckTo.getZ(), sourceItemStack);
/*  85 */     this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(stuckTo.getId()));
/*  86 */     this.attachedToEntity = stuckTo;
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, double x, double y, double z, boolean shotAtAngle) {
/*  90 */     this(level, x, y, z, sourceItemStack);
/*  91 */     this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(shotAtAngle));
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, Entity owner, double x, double y, double z, boolean shotAtAngle) {
/*  95 */     this(level, sourceItemStack, x, y, z, shotAtAngle);
/*  96 */     setOwner(owner);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 101 */     entityData.define(DATA_ID_FIREWORKS_ITEM, getDefaultItem());
/* 102 */     entityData.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
/* 103 */     entityData.define(DATA_SHOT_AT_ANGLE, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 108 */     return (distance < 4096.0D && !isAttachedToEntity());
/*     */   }
/*     */   
/*     */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 113 */     return (super.shouldRender(camX, camY, camZ) && !isAttachedToEntity());
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     HitResult hitResult;
/* 118 */     super.tick();
/* 121 */     if (isAttachedToEntity()) {
/* 122 */       if (this.attachedToEntity == null)
/* 123 */         ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).ifPresent(id -> {
/*     */               Entity ent = level().getEntity(id);
/*     */               if (ent instanceof LivingEntity) {
/*     */                 LivingEntity livingEntity = (LivingEntity)ent;
/*     */                 this.attachedToEntity = livingEntity;
/*     */               } 
/*     */             }); 
/* 130 */       if (this.attachedToEntity != null) {
/*     */         Vec3 handAngle;
/* 132 */         if (this.attachedToEntity.isFallFlying()) {
/* 133 */           Vec3 lookAngle = this.attachedToEntity.getLookAngle();
/* 134 */           double power = 1.5D;
/* 135 */           double powerAdd = 0.1D;
/* 137 */           Vec3 movement = this.attachedToEntity.getDeltaMovement();
/* 138 */           this.attachedToEntity.setDeltaMovement(movement.add(lookAngle.x * 0.1D + (lookAngle.x * 1.5D - movement.x) * 0.5D, lookAngle.y * 0.1D + (lookAngle.y * 1.5D - movement.y) * 0.5D, lookAngle.z * 0.1D + (lookAngle.z * 1.5D - movement.z) * 0.5D));
/* 143 */           handAngle = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
/*     */         } else {
/* 145 */           handAngle = Vec3.ZERO;
/*     */         } 
/* 147 */         setPos(this.attachedToEntity.getX() + handAngle.x, this.attachedToEntity.getY() + handAngle.y, this.attachedToEntity.getZ() + handAngle.z);
/* 149 */         setDeltaMovement(this.attachedToEntity.getDeltaMovement());
/*     */       } 
/* 151 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } else {
/* 153 */       if (!isShotAtAngle()) {
/* 155 */         double horizontalAcceleration = this.horizontalCollision ? 1.0D : 1.15D;
/* 156 */         setDeltaMovement(getDeltaMovement().multiply(horizontalAcceleration, 1.0D, horizontalAcceleration).add(0.0D, 0.04D, 0.0D));
/*     */       } 
/* 158 */       Vec3 movement = getDeltaMovement();
/* 159 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/* 161 */       move(MoverType.SELF, movement);
/* 162 */       applyEffectsFromBlocks();
/* 163 */       setDeltaMovement(movement);
/*     */     } 
/* 166 */     if (!this.noPhysics && isAlive() && hitResult.getType() != HitResult.Type.MISS) {
/* 167 */       hitTargetOrDeflectSelf(hitResult);
/* 168 */       this.needsSync = true;
/*     */     } 
/* 171 */     updateRotation();
/* 173 */     if (this.life == 0 && !isSilent())
/* 174 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F); 
/* 177 */     this.life++;
/* 178 */     if (level().isClientSide() && this.life % 2 < 2)
/* 179 */       level().addParticle((ParticleOptions)ParticleTypes.FIREWORK, getX(), getY(), getZ(), this.random.nextGaussian() * 0.05D, -(getDeltaMovement()).y * 0.5D, this.random.nextGaussian() * 0.05D); 
/* 181 */     if (this.life > this.lifetime) {
/* 181 */       Level level = level();
/* 181 */       if (level instanceof ServerLevel) {
/* 181 */         ServerLevel serverLevel = (ServerLevel)level;
/* 182 */         explode(serverLevel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void explode(ServerLevel level) {
/* 187 */     level.broadcastEntityEvent(this, (byte)17);
/* 188 */     gameEvent((Holder)GameEvent.EXPLODE, getOwner());
/* 189 */     dealExplosionDamage(level);
/* 190 */     discard();
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 195 */     super.onHitEntity(hitResult);
/* 196 */     Level level = level();
/* 196 */     if (level instanceof ServerLevel) {
/* 196 */       ServerLevel serverLevel = (ServerLevel)level;
/* 197 */       explode(serverLevel);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 203 */     BlockPos pos = new BlockPos((Vec3i)hitResult.getBlockPos());
/* 204 */     level().getBlockState(pos).entityInside(level(), pos, this, InsideBlockEffectApplier.NOOP, true);
/* 205 */     Level level = level();
/* 205 */     if (level instanceof ServerLevel) {
/* 205 */       ServerLevel serverLevel = (ServerLevel)level;
/* 205 */       if (hasExplosion())
/* 206 */         explode(serverLevel); 
/*     */     } 
/* 208 */     super.onHitBlock(hitResult);
/*     */   }
/*     */   
/*     */   private boolean hasExplosion() {
/* 212 */     return !getExplosions().isEmpty();
/*     */   }
/*     */   
/*     */   private void dealExplosionDamage(ServerLevel level) {
/* 217 */     float damageAmount = 0.0F;
/* 218 */     List<FireworkExplosion> explosions = getExplosions();
/* 219 */     if (!explosions.isEmpty())
/* 220 */       damageAmount = 5.0F + (explosions.size() * 2); 
/* 222 */     if (damageAmount > 0.0F) {
/* 223 */       if (this.attachedToEntity != null)
/* 224 */         this.attachedToEntity.hurtServer(level, damageSources().fireworks(this, getOwner()), 5.0F + (explosions.size() * 2)); 
/* 227 */       double radius = 5.0D;
/* 228 */       Vec3 rocketPos = position();
/* 229 */       List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(5.0D));
/* 230 */       for (LivingEntity target : targets) {
/* 231 */         if (target == this.attachedToEntity)
/*     */           continue; 
/* 234 */         if (distanceToSqr((Entity)target) > 25.0D)
/*     */           continue; 
/* 238 */         boolean canSee = false;
/* 239 */         for (int testStep = 0; testStep < 2; testStep++) {
/* 240 */           Vec3 to = new Vec3(target.getX(), target.getY(0.5D * testStep), target.getZ());
/* 241 */           BlockHitResult blockHitResult = level().clip(new ClipContext(rocketPos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
/* 242 */           if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 243 */             canSee = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 247 */         if (canSee) {
/* 248 */           float damage = damageAmount * (float)Math.sqrt((5.0D - distanceTo((Entity)target)) / 5.0D);
/* 249 */           target.hurtServer(level, damageSources().fireworks(this, getOwner()), damage);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isAttachedToEntity() {
/* 256 */     return ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).isPresent();
/*     */   }
/*     */   
/*     */   public boolean isShotAtAngle() {
/* 260 */     return ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue();
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 265 */     if (id == 17 && level().isClientSide()) {
/* 266 */       Vec3 movement = getDeltaMovement();
/* 267 */       level().createFireworks(getX(), getY(), getZ(), movement.x, movement.y, movement.z, getExplosions());
/*     */     } 
/* 269 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 274 */     super.addAdditionalSaveData(output);
/* 275 */     output.putInt("Life", this.life);
/* 276 */     output.putInt("LifeTime", this.lifetime);
/* 277 */     output.store("FireworksItem", ItemStack.CODEC, getItem());
/* 278 */     output.putBoolean("ShotAtAngle", ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 283 */     super.readAdditionalSaveData(input);
/* 284 */     this.life = input.getIntOr("Life", 0);
/* 285 */     this.lifetime = input.getIntOr("LifeTime", 0);
/* 287 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, input.read("FireworksItem", ItemStack.CODEC).orElse(getDefaultItem()));
/* 289 */     this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(input.getBooleanOr("ShotAtAngle", false)));
/*     */   }
/*     */   
/*     */   private List<FireworkExplosion> getExplosions() {
/* 293 */     ItemStack sourceItemStack = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 294 */     Fireworks fireworks = (Fireworks)sourceItemStack.get(DataComponents.FIREWORKS);
/* 295 */     return (fireworks != null) ? fireworks.explosions() : List.<FireworkExplosion>of();
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 300 */     return (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   private static ItemStack getDefaultItem() {
/* 309 */     return new ItemStack((ItemLike)Items.FIREWORK_ROCKET);
/*     */   }
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity hurtEntity, DamageSource damageSource) {
/* 314 */     double dx = (hurtEntity.position()).x - (position()).x;
/* 315 */     double dz = (hurtEntity.position()).z - (position()).z;
/* 316 */     return DoubleDoubleImmutablePair.of(dx, dz);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\FireworkRocketEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */