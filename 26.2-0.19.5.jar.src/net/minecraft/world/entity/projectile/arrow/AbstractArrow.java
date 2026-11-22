/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractArrow extends Projectile {
/*     */   private static final double ARROW_BASE_DAMAGE = 2.0D;
/*     */   
/*     */   private static final int SHAKE_TIME = 7;
/*     */   
/*     */   private static final float WATER_INERTIA = 0.6F;
/*     */   
/*     */   private static final float INERTIA = 0.99F;
/*     */   
/*     */   private static final short DEFAULT_LIFE = 0;
/*     */   
/*     */   private static final byte DEFAULT_SHAKE = 0;
/*     */   
/*     */   private static final boolean DEFAULT_IN_GROUND = false;
/*     */   
/*     */   private static final boolean DEFAULT_CRIT = false;
/*     */   
/*     */   private static final byte DEFAULT_PIERCE_LEVEL = 0;
/*     */   
/*     */   public enum Pickup {
/*  74 */     DISALLOWED, ALLOWED, CREATIVE_ONLY;
/*     */     
/*     */     public static final Codec<Pickup> LEGACY_CODEC;
/*     */     
/*     */     static {
/*  76 */       LEGACY_CODEC = Codec.BYTE.xmap(Pickup::byOrdinal, p -> Byte.valueOf((byte)p.ordinal()));
/*     */     }
/*     */     
/*     */     public static Pickup byOrdinal(int ordinal) {
/*  79 */       if (ordinal < 0 || ordinal > (values()).length)
/*  80 */         ordinal = 0; 
/*  83 */       return values()[ordinal];
/*     */     }
/*     */   }
/*     */   
/*  87 */   private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*     */   
/*  88 */   private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*     */   
/*  89 */   private static final EntityDataAccessor<Boolean> IN_GROUND = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int FLAG_CRIT = 1;
/*     */   
/*     */   private static final int FLAG_NOPHYSICS = 2;
/*     */   
/*     */   private BlockState lastState;
/*     */   
/*     */   protected int inGroundTime;
/*     */   
/*  95 */   public Pickup pickup = Pickup.DISALLOWED;
/*     */   
/*  96 */   public int shakeTime = 0;
/*     */   
/*  97 */   private int life = 0;
/*     */   
/*  98 */   private double baseDamage = 2.0D;
/*     */   
/*     */   private SoundEvent soundEvent;
/*     */   
/*     */   private IntOpenHashSet piercingIgnoreEntityIds;
/*     */   
/*     */   private List<Entity> piercedAndKilledEntities;
/*     */   
/*     */   private ItemStack pickupItemStack;
/*     */   
/*     */   private ItemStack firedFromWeapon;
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, Level level) {
/* 108 */     super(type, level);
/* 109 */     this.soundEvent = getDefaultHitGroundSoundEvent();
/* 110 */     this.pickupItemStack = getDefaultPickupItem();
/* 111 */     this.firedFromWeapon = null;
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 115 */     this(type, level);
/* 116 */     this.pickupItemStack = pickupItemStack.copy();
/* 117 */     applyComponentsFromItemStack(pickupItemStack);
/* 119 */     Unit intangible = (Unit)pickupItemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
/* 120 */     if (intangible != null)
/* 121 */       this.pickup = Pickup.CREATIVE_ONLY; 
/* 124 */     setPos(x, y, z);
/* 125 */     if (firedFromWeapon != null && level instanceof ServerLevel) {
/* 125 */       ServerLevel serverLevel = (ServerLevel)level;
/* 126 */       if (firedFromWeapon.isEmpty())
/* 127 */         throw new IllegalArgumentException("Invalid weapon firing an arrow"); 
/* 129 */       this.firedFromWeapon = firedFromWeapon.copy();
/* 130 */       int pierceLevel = EnchantmentHelper.getPiercingCount(serverLevel, firedFromWeapon, this.pickupItemStack);
/* 131 */       if (pierceLevel > 0)
/* 132 */         setPierceLevel((byte)pierceLevel); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, LivingEntity mob, Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 138 */     this(type, mob.getX(), mob.getEyeY() - 0.10000000149011612D, mob.getZ(), level, pickupItemStack, firedFromWeapon);
/* 140 */     setOwner((Entity)mob);
/*     */   }
/*     */   
/*     */   public void setSoundEvent(SoundEvent soundEvent) {
/* 144 */     this.soundEvent = soundEvent;
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 149 */     double size = getBoundingBox().getSize() * 10.0D;
/* 150 */     if (Double.isNaN(size))
/* 151 */       size = 1.0D; 
/* 153 */     size *= 64.0D * getViewScale();
/* 154 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 159 */     entityData.define(ID_FLAGS, Byte.valueOf((byte)0));
/* 160 */     entityData.define(PIERCE_LEVEL, Byte.valueOf((byte)0));
/* 161 */     entityData.define(IN_GROUND, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public void shoot(double xd, double yd, double zd, float pow, float uncertainty) {
/* 166 */     super.shoot(xd, yd, zd, pow, uncertainty);
/* 167 */     this.life = 0;
/*     */   }
/*     */   
/*     */   public void lerpMotion(Vec3 movement) {
/* 172 */     super.lerpMotion(movement);
/* 173 */     this.life = 0;
/* 174 */     if (isInGround() && movement.lengthSqr() > 0.0D)
/* 176 */       setInGround(false); 
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 182 */     super.onSyncedDataUpdated(accessor);
/* 183 */     if (!this.firstTick && this.shakeTime <= 0 && accessor.equals(IN_GROUND) && isInGround())
/* 184 */       this.shakeTime = 7; 
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     float yRot;
/* 190 */     boolean physicsEnabled = !isNoPhysics();
/* 192 */     Vec3 movement = getDeltaMovement();
/* 194 */     BlockPos blockPos = blockPosition();
/* 195 */     BlockState blockState = level().getBlockState(blockPos);
/* 196 */     if (!blockState.isAir() && physicsEnabled) {
/* 197 */       VoxelShape shape = blockState.getCollisionShape((BlockGetter)level(), blockPos);
/* 198 */       if (!shape.isEmpty()) {
/* 199 */         Vec3 position = position();
/* 200 */         for (AABB aabb : shape.toAabbs()) {
/* 201 */           if (aabb.move(blockPos).contains(position)) {
/* 202 */             setDeltaMovement(Vec3.ZERO);
/* 203 */             setInGround(true);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 210 */     if (this.shakeTime > 0)
/* 211 */       this.shakeTime--; 
/* 214 */     if (isInWaterOrRain())
/* 215 */       clearFire(); 
/* 218 */     if (isInGround() && physicsEnabled) {
/* 219 */       if (!level().isClientSide())
/* 220 */         if (this.lastState != blockState && shouldFall()) {
/* 221 */           startFalling();
/*     */         } else {
/* 223 */           tickDespawn();
/*     */         }  
/* 227 */       this.inGroundTime++;
/* 228 */       if (isAlive())
/* 230 */         applyEffectsFromBlocks(); 
/* 233 */       if (!level().isClientSide())
/* 234 */         setSharedFlagOnFire((getRemainingFireTicks() > 0)); 
/*     */       return;
/*     */     } 
/* 238 */     this.inGroundTime = 0;
/* 240 */     Vec3 originalPosition = position();
/* 242 */     if (isInWater()) {
/* 243 */       applyInertia(getWaterInertia());
/* 244 */       addBubbleParticles(originalPosition);
/*     */     } 
/* 247 */     if (isCritArrow())
/* 248 */       for (int i = 0; i < 4; i++)
/* 249 */         level().addParticle((ParticleOptions)ParticleTypes.CRIT, originalPosition.x + movement.x * i / 4.0D, originalPosition.y + movement.y * i / 4.0D, originalPosition.z + movement.z * i / 4.0D, -movement.x, -movement.y + 0.2D, -movement.z);  
/* 254 */     if (!physicsEnabled) {
/* 255 */       yRot = (float)(Mth.atan2(-movement.x, -movement.z) * 57.2957763671875D);
/*     */     } else {
/* 257 */       yRot = (float)(Mth.atan2(movement.x, movement.z) * 57.2957763671875D);
/*     */     } 
/* 259 */     float xRot = (float)(Mth.atan2(movement.y, movement.horizontalDistance()) * 57.2957763671875D);
/* 260 */     setXRot(lerpRotation(getXRot(), xRot));
/* 261 */     setYRot(lerpRotation(getYRot(), yRot));
/* 264 */     checkLeftOwner();
/* 266 */     if (physicsEnabled) {
/* 267 */       BlockHitResult blockHitResult = level().clipIncludingBorder(new ClipContext(originalPosition, originalPosition.add(movement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
/* 268 */       stepMoveAndHit(blockHitResult);
/*     */     } else {
/* 270 */       setPos(originalPosition.add(movement));
/* 271 */       applyEffectsFromBlocks();
/*     */     } 
/* 274 */     if (!isInWater())
/* 275 */       applyInertia(getAirDrag()); 
/* 277 */     if (physicsEnabled && !isInGround())
/* 278 */       applyGravity(); 
/* 280 */     super.tick();
/*     */   }
/*     */   
/*     */   protected float getAirDrag() {
/* 285 */     return 0.99F;
/*     */   }
/*     */   
/*     */   private void stepMoveAndHit(BlockHitResult blockHitResult) {
/* 289 */     while (isAlive()) {
/* 290 */       Vec3 initialPosition = position();
/* 291 */       ArrayList<EntityHitResult> entitiesHit = new ArrayList<>(findHitEntities(initialPosition, blockHitResult.getLocation()));
/* 292 */       entitiesHit.sort(Comparator.comparingDouble(c -> initialPosition.distanceToSqr(c.getEntity().position())));
/* 294 */       EntityHitResult firstEntityHit = entitiesHit.isEmpty() ? null : entitiesHit.getFirst();
/* 295 */       Vec3 nextLocation = ((HitResult)Objects.requireNonNullElse(firstEntityHit, blockHitResult)).getLocation();
/* 297 */       setPos(nextLocation);
/* 298 */       applyEffectsFromBlocks(initialPosition, nextLocation);
/* 299 */       if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick())
/* 300 */         handlePortal(); 
/* 305 */       if (entitiesHit.isEmpty()) {
/* 306 */         if (isAlive() && blockHitResult.getType() != HitResult.Type.MISS) {
/* 307 */           hitTargetOrDeflectSelf((HitResult)blockHitResult);
/* 308 */           this.needsSync = true;
/*     */         } 
/*     */         break;
/*     */       } 
/* 311 */       if (isAlive() && !this.noPhysics) {
/* 312 */         ProjectileDeflection deflection = hitTargetsOrDeflectSelf(entitiesHit);
/* 313 */         this.needsSync = true;
/* 314 */         if (getPierceLevel() <= 0 || deflection != ProjectileDeflection.NONE)
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ProjectileDeflection hitTargetsOrDeflectSelf(Collection<EntityHitResult> entityHitResults) {
/* 322 */     for (EntityHitResult e : entityHitResults) {
/* 323 */       ProjectileDeflection deflection = hitTargetOrDeflectSelf((HitResult)e);
/* 324 */       if (!isAlive() || deflection != ProjectileDeflection.NONE)
/* 325 */         return deflection; 
/*     */     } 
/* 328 */     return ProjectileDeflection.NONE;
/*     */   }
/*     */   
/*     */   private void applyInertia(float inertia) {
/* 332 */     Vec3 movement = getDeltaMovement();
/* 333 */     setDeltaMovement(movement.scale(inertia));
/*     */   }
/*     */   
/*     */   private void addBubbleParticles(Vec3 position) {
/* 337 */     Vec3 movement = getDeltaMovement();
/* 338 */     for (int i = 0; i < 4; i++) {
/* 339 */       float s = 0.25F;
/* 340 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, position.x - movement.x * 0.25D, position.y - movement.y * 0.25D, position.z - movement.z * 0.25D, movement.x, movement.y, movement.z);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 346 */     return 0.05D;
/*     */   }
/*     */   
/*     */   private boolean shouldFall() {
/* 350 */     return (isInGround() && level().noCollision((new AABB(position(), position())).inflate(0.06D)));
/*     */   }
/*     */   
/*     */   private void startFalling() {
/* 354 */     setInGround(false);
/* 355 */     Vec3 deltaMovement = getDeltaMovement();
/* 356 */     setDeltaMovement(deltaMovement.multiply((this.random
/* 357 */           .nextFloat() * 0.2F), (this.random
/* 358 */           .nextFloat() * 0.2F), (this.random
/* 359 */           .nextFloat() * 0.2F)));
/* 361 */     this.life = 0;
/*     */   }
/*     */   
/*     */   protected boolean isInGround() {
/* 365 */     return ((Boolean)this.entityData.get(IN_GROUND)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void setInGround(boolean inGround) {
/* 369 */     this.entityData.set(IN_GROUND, Boolean.valueOf(inGround));
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 374 */     return !isInGround();
/*     */   }
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 379 */     super.move(moverType, delta);
/* 380 */     if (moverType != MoverType.SELF && shouldFall())
/* 381 */       startFalling(); 
/*     */   }
/*     */   
/*     */   protected void tickDespawn() {
/* 386 */     this.life++;
/* 387 */     if (this.life >= 1200)
/* 388 */       discard(); 
/*     */   }
/*     */   
/*     */   private void resetPiercedEntities() {
/* 393 */     if (this.piercedAndKilledEntities != null)
/* 394 */       this.piercedAndKilledEntities.clear(); 
/* 396 */     if (this.piercingIgnoreEntityIds != null)
/* 397 */       this.piercingIgnoreEntityIds.clear(); 
/*     */   }
/*     */   
/*     */   public void onItemBreak(Item item) {
/* 403 */     this.firedFromWeapon = null;
/*     */   }
/*     */   
/*     */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 408 */     if (isInGround())
/*     */       return; 
/* 411 */     super.onAboveBubbleColumn(dragDown, pos);
/*     */   }
/*     */   
/*     */   public void onInsideBubbleColumn(boolean dragDown) {
/* 416 */     if (isInGround())
/*     */       return; 
/* 419 */     super.onInsideBubbleColumn(dragDown);
/*     */   }
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 424 */     if (isInGround())
/*     */       return; 
/* 427 */     super.push(xa, ya, za);
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 432 */     super.onHitEntity(hitResult);
/* 433 */     Entity entity = hitResult.getEntity();
/* 434 */     float pow = (float)getDeltaMovement().length();
/* 435 */     double arrowDamage = this.baseDamage;
/* 436 */     Entity currentOwner = getOwner();
/* 437 */     DamageSource damageSource = damageSources().arrow(this, (currentOwner != null) ? currentOwner : (Entity)this);
/* 439 */     if (getWeaponItem() != null) {
/* 439 */       Level level = level();
/* 439 */       if (level instanceof ServerLevel) {
/* 439 */         ServerLevel serverLevel = (ServerLevel)level;
/* 440 */         arrowDamage = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity, damageSource, (float)arrowDamage);
/*     */       } 
/*     */     } 
/* 442 */     int damage = Mth.ceil(Mth.clamp(pow * arrowDamage, 0.0D, 2.147483647E9D));
/* 444 */     if (getPierceLevel() > 0) {
/* 445 */       if (this.piercingIgnoreEntityIds == null)
/* 446 */         this.piercingIgnoreEntityIds = new IntOpenHashSet(5); 
/* 449 */       if (this.piercedAndKilledEntities == null)
/* 450 */         this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5); 
/* 454 */       if (this.piercingIgnoreEntityIds.size() < getPierceLevel() + 1) {
/* 455 */         this.piercingIgnoreEntityIds.add(entity.getId());
/*     */       } else {
/* 457 */         discard();
/*     */         return;
/*     */       } 
/*     */     } 
/* 462 */     if (isCritArrow()) {
/* 463 */       long dmgIncrease = this.random.nextInt(damage / 2 + 2);
/* 464 */       damage = (int)Math.min(dmgIncrease + damage, 2147483647L);
/*     */     } 
/* 467 */     if (currentOwner instanceof LivingEntity) {
/* 467 */       LivingEntity livingOwner = (LivingEntity)currentOwner;
/* 468 */       livingOwner.setLastHurtMob(entity);
/*     */     } 
/* 471 */     boolean isEnderman = entity.is(EntityTypes.ENDERMAN);
/* 472 */     int remainingFireTicks = entity.getRemainingFireTicks();
/* 475 */     if (isOnFire() && !isEnderman)
/* 476 */       entity.igniteForSeconds(5.0F); 
/* 479 */     if (entity.hurtOrSimulate(damageSource, damage)) {
/* 481 */       if (isEnderman)
/*     */         return; 
/* 484 */       if (entity instanceof LivingEntity) {
/* 484 */         LivingEntity mob = (LivingEntity)entity;
/* 485 */         if (!level().isClientSide() && getPierceLevel() <= 0)
/* 486 */           mob.setArrowCount(mob.getArrowCount() + 1); 
/* 489 */         doKnockback(mob, damageSource);
/* 491 */         Level level = level();
/* 491 */         if (level instanceof ServerLevel) {
/* 491 */           ServerLevel serverLevel = (ServerLevel)level;
/* 492 */           EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, (Entity)mob, damageSource, getWeaponItem());
/*     */         } 
/* 495 */         doPostHurtEffects(mob);
/* 497 */         if (mob instanceof Player && currentOwner instanceof ServerPlayer) {
/* 497 */           ServerPlayer ownerPlayer = (ServerPlayer)currentOwner;
/* 497 */           if (!isSilent() && mob != ownerPlayer)
/* 498 */             ownerPlayer.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, 0.0F)); 
/*     */         } 
/* 501 */         if (!entity.isAlive() && this.piercedAndKilledEntities != null)
/* 502 */           this.piercedAndKilledEntities.add(mob); 
/* 505 */         if (!level().isClientSide() && currentOwner instanceof ServerPlayer) {
/* 505 */           ServerPlayer player = (ServerPlayer)currentOwner;
/* 506 */           if (this.piercedAndKilledEntities != null) {
/* 507 */             CriteriaTriggers.KILLED_BY_ARROW.trigger(player, this.piercedAndKilledEntities, this.firedFromWeapon);
/* 508 */           } else if (!entity.isAlive()) {
/* 509 */             CriteriaTriggers.KILLED_BY_ARROW.trigger(player, List.of(entity), this.firedFromWeapon);
/*     */           } 
/*     */         } 
/*     */       } 
/* 513 */       playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 514 */       if (getPierceLevel() <= 0)
/* 515 */         discard(); 
/*     */     } else {
/* 519 */       entity.setRemainingFireTicks(remainingFireTicks);
/* 520 */       deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
/* 521 */       setDeltaMovement(getDeltaMovement().scale(0.2D));
/* 523 */       Level level = level();
/* 523 */       if (level instanceof ServerLevel) {
/* 523 */         ServerLevel serverLevel = (ServerLevel)level;
/* 523 */         if (getDeltaMovement().lengthSqr() < 1.0E-7D) {
/* 524 */           if (this.pickup == Pickup.ALLOWED)
/* 525 */             spawnAtLocation(serverLevel, getPickupItem(), 0.1F); 
/* 527 */           discard();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void doKnockback(LivingEntity mob, DamageSource damageSource) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield firedFromWeapon : Lnet/minecraft/world/item/ItemStack;
/*     */     //   4: ifnull -> 43
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   11: astore #6
/*     */     //   13: aload #6
/*     */     //   15: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   18: ifeq -> 43
/*     */     //   21: aload #6
/*     */     //   23: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   26: astore #5
/*     */     //   28: aload #5
/*     */     //   30: aload_0
/*     */     //   31: getfield firedFromWeapon : Lnet/minecraft/world/item/ItemStack;
/*     */     //   34: aload_1
/*     */     //   35: aload_2
/*     */     //   36: fconst_0
/*     */     //   37: invokestatic modifyKnockback : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;F)F
/*     */     //   40: goto -> 44
/*     */     //   43: fconst_0
/*     */     //   44: f2d
/*     */     //   45: dstore_3
/*     */     //   46: dload_3
/*     */     //   47: dconst_0
/*     */     //   48: dcmpl
/*     */     //   49: ifle -> 120
/*     */     //   52: dconst_0
/*     */     //   53: dconst_1
/*     */     //   54: aload_1
/*     */     //   55: getstatic net/minecraft/world/entity/ai/attributes/Attributes.KNOCKBACK_RESISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   58: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   61: dsub
/*     */     //   62: invokestatic max : (DD)D
/*     */     //   65: dstore #5
/*     */     //   67: aload_0
/*     */     //   68: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   71: dconst_1
/*     */     //   72: dconst_0
/*     */     //   73: dconst_1
/*     */     //   74: invokevirtual multiply : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   77: invokevirtual normalize : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   80: dload_3
/*     */     //   81: ldc2_w 0.6
/*     */     //   84: dmul
/*     */     //   85: dload #5
/*     */     //   87: dmul
/*     */     //   88: invokevirtual scale : (D)Lnet/minecraft/world/phys/Vec3;
/*     */     //   91: astore #7
/*     */     //   93: aload #7
/*     */     //   95: invokevirtual lengthSqr : ()D
/*     */     //   98: dconst_0
/*     */     //   99: dcmpl
/*     */     //   100: ifle -> 120
/*     */     //   103: aload_1
/*     */     //   104: aload #7
/*     */     //   106: getfield x : D
/*     */     //   109: ldc2_w 0.1
/*     */     //   112: aload #7
/*     */     //   114: getfield z : D
/*     */     //   117: invokevirtual push : (DDD)V
/*     */     //   120: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #533	-> 0
/*     */     //   #534	-> 46
/*     */     //   #535	-> 52
/*     */     //   #536	-> 67
/*     */     //   #537	-> 93
/*     */     //   #538	-> 103
/*     */     //   #541	-> 120
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   28	15	5	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   67	53	5	knockbackResistance	D
/*     */     //   93	27	7	movement	Lnet/minecraft/world/phys/Vec3;
/*     */     //   0	121	0	this	Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;
/*     */     //   0	121	1	mob	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	121	2	damageSource	Lnet/minecraft/world/damagesource/DamageSource;
/*     */     //   46	75	3	knockback	D
/*     */   }
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 545 */     this.lastState = level().getBlockState(hitResult.getBlockPos());
/* 546 */     super.onHitBlock(hitResult);
/* 548 */     ItemStack weaponItem = getWeaponItem();
/* 549 */     Level level = level();
/* 549 */     if (level instanceof ServerLevel) {
/* 549 */       ServerLevel serverLevel = (ServerLevel)level;
/* 549 */       if (weaponItem != null)
/* 550 */         hitBlockEnchantmentEffects(serverLevel, hitResult, weaponItem); 
/*     */     } 
/* 553 */     Vec3 movement = getDeltaMovement();
/* 554 */     Vec3 offsetDirection = new Vec3(Math.signum(movement.x), Math.signum(movement.y), Math.signum(movement.z));
/* 555 */     Vec3 scaledMovement = offsetDirection.scale(0.05000000074505806D);
/* 556 */     setPos(position().subtract(scaledMovement));
/* 557 */     setDeltaMovement(Vec3.ZERO);
/* 559 */     playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 560 */     setInGround(true);
/* 561 */     this.shakeTime = 7;
/* 562 */     setCritArrow(false);
/* 563 */     setPierceLevel((byte)0);
/* 564 */     setSoundEvent(SoundEvents.ARROW_HIT);
/* 565 */     resetPiercedEntities();
/*     */   }
/*     */   
/*     */   protected void hitBlockEnchantmentEffects(ServerLevel serverLevel, BlockHitResult hitResult, ItemStack weapon) {
/* 569 */     Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
/* 570 */     Entity entity = getOwner();
/* 570 */     LivingEntity livingOwner = (LivingEntity)entity;
/* 570 */     EnchantmentHelper.onHitBlock(serverLevel, weapon, (entity instanceof LivingEntity) ? livingOwner : null, (Entity)this, null, compensatedHitPosition, serverLevel.getBlockState(hitResult.getBlockPos()), item -> this.firedFromWeapon = null);
/*     */   }
/*     */   
/*     */   public ItemStack getWeaponItem() {
/* 575 */     return this.firedFromWeapon;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 579 */     return SoundEvents.ARROW_HIT;
/*     */   }
/*     */   
/*     */   protected final SoundEvent getHitGroundSoundEvent() {
/* 583 */     return this.soundEvent;
/*     */   }
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity mob) {}
/*     */   
/*     */   protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
/* 590 */     return ProjectileUtil.getEntityHitResult(level(), this, from, to, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity);
/*     */   }
/*     */   
/*     */   protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
/* 594 */     return ProjectileUtil.getManyEntityHitResult(level(), (Entity)this, from, to, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity, false);
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 599 */     if (entity instanceof Player) {
/* 599 */       Player playerEntity = (Player)entity;
/* 599 */       Entity entity1 = getOwner();
/* 599 */       if (entity1 instanceof Player) {
/* 599 */         Player player = (Player)entity1;
/* 599 */         if (!player.canHarmPlayer(playerEntity))
/* 600 */           return false; 
/*     */       } 
/*     */     } 
/* 602 */     return (super.canHitEntity(entity) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(entity.getId())));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 607 */     super.addAdditionalSaveData(output);
/* 609 */     output.putShort("life", (short)this.life);
/* 610 */     output.storeNullable("inBlockState", BlockState.CODEC, this.lastState);
/* 611 */     output.putByte("shake", (byte)this.shakeTime);
/* 612 */     output.putBoolean("inGround", isInGround());
/* 613 */     output.store("pickup", Pickup.LEGACY_CODEC, this.pickup);
/* 614 */     output.putDouble("damage", this.baseDamage);
/* 615 */     output.putBoolean("crit", isCritArrow());
/* 616 */     output.putByte("PierceLevel", getPierceLevel());
/* 617 */     output.store("SoundEvent", BuiltInRegistries.SOUND_EVENT.byNameCodec(), this.soundEvent);
/* 618 */     output.store("item", ItemStack.CODEC, this.pickupItemStack);
/* 619 */     output.storeNullable("weapon", ItemStack.CODEC, this.firedFromWeapon);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 624 */     super.readAdditionalSaveData(input);
/* 626 */     this.life = input.getShortOr("life", (short)0);
/* 627 */     this.lastState = input.read("inBlockState", BlockState.CODEC).orElse(null);
/* 628 */     this.shakeTime = input.getByteOr("shake", (byte)0) & 0xFF;
/* 629 */     setInGround(input.getBooleanOr("inGround", false));
/* 630 */     this.baseDamage = input.getDoubleOr("damage", 2.0D);
/* 631 */     this.pickup = input.read("pickup", Pickup.LEGACY_CODEC).orElse(Pickup.DISALLOWED);
/* 633 */     setCritArrow(input.getBooleanOr("crit", false));
/* 634 */     setPierceLevel(input.getByteOr("PierceLevel", (byte)0));
/* 636 */     this.soundEvent = input.read("SoundEvent", BuiltInRegistries.SOUND_EVENT.byNameCodec()).orElse(getDefaultHitGroundSoundEvent());
/* 638 */     setPickupItemStack(input.read("item", ItemStack.CODEC).orElse(getDefaultPickupItem()));
/* 639 */     this.firedFromWeapon = input.read("weapon", ItemStack.CODEC).orElse(null);
/*     */   }
/*     */   
/*     */   public void setOwner(Entity owner) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial setOwner : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   5: aload_0
/*     */     //   6: aload_1
/*     */     //   7: astore_2
/*     */     //   8: iconst_0
/*     */     //   9: istore_3
/*     */     //   10: aload_2
/*     */     //   11: iload_3
/*     */     //   12: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*     */     //   17: tableswitch default -> 83, -1 -> 83, 0 -> 44, 1 -> 71
/*     */     //   44: aload_2
/*     */     //   45: checkcast net/minecraft/world/entity/player/Player
/*     */     //   48: astore #4
/*     */     //   50: aload_0
/*     */     //   51: getfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   54: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.DISALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   57: if_acmpeq -> 65
/*     */     //   60: iconst_1
/*     */     //   61: istore_3
/*     */     //   62: goto -> 10
/*     */     //   65: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.ALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   68: goto -> 90
/*     */     //   71: aload_2
/*     */     //   72: checkcast net/minecraft/world/entity/OminousItemSpawner
/*     */     //   75: astore #5
/*     */     //   77: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.DISALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   80: goto -> 90
/*     */     //   83: aload_0
/*     */     //   84: getfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   87: goto -> 90
/*     */     //   90: putfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   93: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #644	-> 0
/*     */     //   #646	-> 5
/*     */     //   #647	-> 44
/*     */     //   #648	-> 71
/*     */     //   #649	-> 83
/*     */     //   #650	-> 90
/*     */     //   #651	-> 93
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   50	21	4	ignored	Lnet/minecraft/world/entity/player/Player;
/*     */     //   77	6	5	ignored	Lnet/minecraft/world/entity/OminousItemSpawner;
/*     */     //   0	94	0	this	Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;
/*     */     //   0	94	1	owner	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/* 655 */     if (level().isClientSide() || (!isInGround() && !isNoPhysics()) || this.shakeTime > 0)
/*     */       return; 
/* 659 */     if (tryPickup(player)) {
/* 660 */       player.take((Entity)this, 1);
/* 661 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean tryPickup(Player player) {
/* 667 */     switch (this.pickup.ordinal()) {
/*     */       default:
/* 667 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 667 */     return 
/*     */       
/* 670 */       player.hasInfiniteMaterials();
/*     */   }
/*     */   
/*     */   protected ItemStack getPickupItem() {
/* 675 */     return this.pickupItemStack.copy();
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 682 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   public ItemStack getPickupItemStackOrigin() {
/* 686 */     return this.pickupItemStack;
/*     */   }
/*     */   
/*     */   public void setBaseDamage(double baseDamage) {
/* 690 */     this.baseDamage = baseDamage;
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 695 */     return is(EntityTypeTags.REDIRECTABLE_PROJECTILE);
/*     */   }
/*     */   
/*     */   public void setCritArrow(boolean critArrow) {
/* 699 */     setFlag(1, critArrow);
/*     */   }
/*     */   
/*     */   private void setPierceLevel(byte pieceLevel) {
/* 703 */     this.entityData.set(PIERCE_LEVEL, Byte.valueOf(pieceLevel));
/*     */   }
/*     */   
/*     */   private void setFlag(int flag, boolean value) {
/* 707 */     byte flags = ((Byte)this.entityData.get(ID_FLAGS)).byteValue();
/* 708 */     if (value) {
/* 709 */       this.entityData.set(ID_FLAGS, Byte.valueOf((byte)(flags | flag)));
/*     */     } else {
/* 711 */       this.entityData.set(ID_FLAGS, Byte.valueOf((byte)(flags & (flag ^ 0xFFFFFFFF))));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setPickupItemStack(ItemStack itemStack) {
/* 716 */     if (!itemStack.isEmpty()) {
/* 717 */       this.pickupItemStack = itemStack;
/*     */     } else {
/* 719 */       this.pickupItemStack = getDefaultPickupItem();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCritArrow() {
/* 724 */     byte flags = ((Byte)this.entityData.get(ID_FLAGS)).byteValue();
/* 725 */     return ((flags & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public byte getPierceLevel() {
/* 729 */     return ((Byte)this.entityData.get(PIERCE_LEVEL)).byteValue();
/*     */   }
/*     */   
/*     */   public void setBaseDamageFromMob(float power) {
/* 733 */     setBaseDamage((power * 2.0F) + this.random.triangle(level().getDifficulty().getId() * 0.11D, 0.57425D));
/*     */   }
/*     */   
/*     */   protected float getWaterInertia() {
/* 737 */     return 0.6F;
/*     */   }
/*     */   
/*     */   public void setNoPhysics(boolean noPhysics) {
/* 741 */     this.noPhysics = noPhysics;
/* 742 */     setFlag(2, noPhysics);
/*     */   }
/*     */   
/*     */   public boolean isNoPhysics() {
/* 746 */     if (!level().isClientSide())
/* 747 */       return this.noPhysics; 
/* 749 */     return ((((Byte)this.entityData.get(ID_FLAGS)).byteValue() & 0x2) != 0);
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 754 */     return (super.isPickable() && !isInGround());
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 759 */     if (slot == 0)
/* 760 */       return SlotAccess.of(this::getPickupItemStackOrigin, this::setPickupItemStack); 
/* 762 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   protected boolean shouldBounceOnWorldBorder() {
/* 767 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract ItemStack getDefaultPickupItem();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\arrow\AbstractArrow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */