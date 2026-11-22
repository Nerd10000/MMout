/*     */ package net.minecraft.world.entity.boss.wither;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WitherBoss extends Monster implements RangedAttackMob {
/*  65 */   private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*  66 */   private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*  67 */   private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*  68 */   private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = (List<EntityDataAccessor<Integer>>)ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
/*     */   
/*  69 */   private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int INVULNERABLE_TICKS = 220;
/*     */   
/*     */   private static final int DEFAULT_INVULNERABLE_TICKS = 0;
/*     */   
/*  74 */   private final float[] xRotHeads = new float[2];
/*     */   
/*  75 */   private final float[] yRotHeads = new float[2];
/*     */   
/*  76 */   private final float[] xRotOHeads = new float[2];
/*     */   
/*  77 */   private final float[] yRotOHeads = new float[2];
/*     */   
/*  78 */   private final int[] nextHeadUpdate = new int[2];
/*     */   
/*  79 */   private final int[] idleHeadUpdates = new int[2];
/*     */   
/*     */   private int destroyBlocksTick;
/*     */   
/*     */   private final ServerBossEvent bossEvent;
/*     */   
/*     */   private static final TargetingConditions.Selector LIVING_ENTITY_SELECTOR;
/*     */   
/*     */   static {
/*  85 */     LIVING_ENTITY_SELECTOR = ((target, level) -> (!target.is(EntityTypeTags.WITHER_FRIENDS) && target.attackable()));
/*     */   }
/*     */   
/*  86 */   private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0D).selector(LIVING_ENTITY_SELECTOR);
/*     */   
/*     */   public WitherBoss(EntityType<? extends WitherBoss> type, Level level) {
/*  89 */     super(type, level);
/*     */     this.bossEvent = (ServerBossEvent)Util.make(new ServerBossEvent(Mth.createInsecureUUID(this.random), getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS), e -> e.setDarkenScreen(true));
/*  91 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 10, false);
/*  93 */     setHealth(getMaxHealth());
/*  95 */     this.xpReward = 50;
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 100 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level);
/* 101 */     flyingPathNavigation.setCanOpenDoors(false);
/* 102 */     flyingPathNavigation.setCanFloat(true);
/* 103 */     return (PathNavigation)flyingPathNavigation;
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 108 */     this.goalSelector.addGoal(0, new WitherDoNothingGoal());
/* 109 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 40, 20.0F));
/* 111 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomFlyingGoal((PathfinderMob)this, 1.0D));
/* 112 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 113 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/* 115 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
/* 116 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 121 */     super.defineSynchedData(entityData);
/* 123 */     entityData.define(DATA_TARGET_A, Integer.valueOf(0));
/* 124 */     entityData.define(DATA_TARGET_B, Integer.valueOf(0));
/* 125 */     entityData.define(DATA_TARGET_C, Integer.valueOf(0));
/* 126 */     entityData.define(DATA_ID_INV, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 131 */     super.addAdditionalSaveData(output);
/* 133 */     output.putInt("Invul", getInvulnerableTicks());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 138 */     super.readAdditionalSaveData(input);
/* 140 */     setInvulnerableTicks(input.getIntOr("Invul", 0));
/* 141 */     if (hasCustomName())
/* 142 */       this.bossEvent.setName(getDisplayName()); 
/*     */   }
/*     */   
/*     */   public void setCustomName(Component name) {
/* 148 */     super.setCustomName(name);
/* 149 */     this.bossEvent.setName(getDisplayName());
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 154 */     return SoundEvents.WITHER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 159 */     return SoundEvents.WITHER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 164 */     return SoundEvents.WITHER_DEATH;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 169 */     Vec3 deltaMovement = getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
/* 171 */     if (!level().isClientSide() && getAlternativeTarget(0) > 0) {
/* 172 */       Entity entity = level().getEntity(getAlternativeTarget(0));
/* 173 */       if (entity != null) {
/* 174 */         double yd = deltaMovement.y;
/* 175 */         if (getY() < entity.getY() || (!isPowered() && getY() < entity.getY() + 5.0D)) {
/* 176 */           yd = Math.max(0.0D, yd);
/* 178 */           yd += 0.3D - yd * 0.6000000238418579D;
/*     */         } 
/* 180 */         deltaMovement = new Vec3(deltaMovement.x, yd, deltaMovement.z);
/* 182 */         Vec3 delta = new Vec3(entity.getX() - getX(), 0.0D, entity.getZ() - getZ());
/* 183 */         if (delta.horizontalDistanceSqr() > 9.0D) {
/* 184 */           Vec3 scale = delta.normalize();
/* 185 */           deltaMovement = deltaMovement.add(scale.x * 0.3D - deltaMovement.x * 0.6D, 0.0D, scale.z * 0.3D - deltaMovement.z * 0.6D);
/*     */         } 
/*     */       } 
/*     */     } 
/* 194 */     setDeltaMovement(deltaMovement);
/* 195 */     if (deltaMovement.horizontalDistanceSqr() > 0.05D)
/* 196 */       setYRot((float)Mth.atan2(deltaMovement.z, deltaMovement.x) * 57.295776F - 90.0F); 
/* 198 */     super.aiStep();
/*     */     int i;
/* 200 */     for (i = 0; i < 2; i++) {
/* 201 */       this.yRotOHeads[i] = this.yRotHeads[i];
/* 202 */       this.xRotOHeads[i] = this.xRotHeads[i];
/*     */     } 
/* 205 */     for (i = 0; i < 2; i++) {
/* 206 */       int entityId = getAlternativeTarget(i + 1);
/* 207 */       Entity entity = null;
/* 208 */       if (entityId > 0)
/* 209 */         entity = level().getEntity(entityId); 
/* 211 */       if (entity != null) {
/* 212 */         double hx = getHeadX(i + 1);
/* 213 */         double hy = getHeadY(i + 1);
/* 214 */         double hz = getHeadZ(i + 1);
/* 216 */         double xd = entity.getX() - hx;
/* 217 */         double yd = entity.getEyeY() - hy;
/* 218 */         double zd = entity.getZ() - hz;
/* 219 */         double sd = Math.sqrt(xd * xd + zd * zd);
/* 221 */         float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 222 */         float xRotD = (float)-(Mth.atan2(yd, sd) * 57.2957763671875D);
/* 223 */         this.xRotHeads[i] = rotlerp(this.xRotHeads[i], xRotD, 40.0F);
/* 224 */         this.yRotHeads[i] = rotlerp(this.yRotHeads[i], yRotD, 10.0F);
/*     */       } else {
/* 226 */         this.yRotHeads[i] = rotlerp(this.yRotHeads[i], this.yBodyRot, 10.0F);
/*     */       } 
/*     */     } 
/* 229 */     boolean isPowered = isPowered();
/* 230 */     for (int j = 0; j < 3; j++) {
/* 231 */       double hx = getHeadX(j);
/* 232 */       double hy = getHeadY(j);
/* 233 */       double hz = getHeadZ(j);
/* 235 */       float radius = 0.3F * getScale();
/* 236 */       level().addParticle((ParticleOptions)ParticleTypes.SMOKE, hx + this.random.nextGaussian() * radius, hy + this.random.nextGaussian() * radius, hz + this.random.nextGaussian() * radius, 0.0D, 0.0D, 0.0D);
/* 237 */       if (isPowered && level().getRandom().nextInt(4) == 0)
/* 238 */         level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.5F), hx + this.random.nextGaussian() * radius, hy + this.random.nextGaussian() * radius, hz + this.random.nextGaussian() * radius, 0.0D, 0.0D, 0.0D); 
/*     */     } 
/* 241 */     if (getInvulnerableTicks() > 0) {
/* 242 */       float height = 3.3F * getScale();
/* 243 */       for (int k = 0; k < 3; k++)
/* 244 */         level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.9F), getX() + this.random.nextGaussian(), getY() + (this.random.nextFloat() * height), getZ() + this.random.nextGaussian(), 0.0D, 0.0D, 0.0D); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 251 */     if (getInvulnerableTicks() > 0) {
/* 252 */       int newCount = getInvulnerableTicks() - 1;
/* 253 */       this.bossEvent.setProgress(1.0F - newCount / 220.0F);
/* 255 */       if (newCount <= 0) {
/* 256 */         level.explode((Entity)this, getX(), getEyeY(), getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
/* 257 */         if (!isSilent())
/* 258 */           level.globalLevelEvent(1023, blockPosition(), 0); 
/*     */       } 
/* 262 */       setInvulnerableTicks(newCount);
/* 263 */       if (this.tickCount % 10 == 0)
/* 264 */         heal(10.0F); 
/*     */       return;
/*     */     } 
/* 270 */     super.customServerAiStep(level);
/* 272 */     for (int i = 1; i < 3; i++) {
/* 273 */       if (this.tickCount >= this.nextHeadUpdate[i - 1]) {
/* 274 */         this.nextHeadUpdate[i - 1] = this.tickCount + 10 + this.random.nextInt(10);
/* 276 */         this.idleHeadUpdates[i - 1] = this.idleHeadUpdates[i - 1] + 1;
/* 276 */         if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && this.idleHeadUpdates[i - 1] > 15) {
/* 277 */           float hrange = 10.0F;
/* 278 */           float vrange = 5.0F;
/* 279 */           double xt = Mth.nextDouble(this.random, getX() - 10.0D, getX() + 10.0D);
/* 280 */           double yt = Mth.nextDouble(this.random, getY() - 5.0D, getY() + 5.0D);
/* 281 */           double zt = Mth.nextDouble(this.random, getZ() - 10.0D, getZ() + 10.0D);
/* 282 */           performRangedAttack(i + 1, xt, yt, zt, true);
/* 283 */           this.idleHeadUpdates[i - 1] = 0;
/*     */         } 
/* 286 */         int headTarget = getAlternativeTarget(i);
/* 287 */         if (headTarget > 0) {
/* 288 */           LivingEntity current = (LivingEntity)level.getEntity(headTarget);
/* 289 */           if (current == null || !canAttack(current) || distanceToSqr((Entity)current) > 900.0D || !hasLineOfSight((Entity)current)) {
/* 290 */             setAlternativeTarget(i, 0);
/*     */           } else {
/* 292 */             performRangedAttack(i + 1, current);
/* 293 */             this.nextHeadUpdate[i - 1] = this.tickCount + 40 + this.random.nextInt(20);
/* 294 */             this.idleHeadUpdates[i - 1] = 0;
/*     */           } 
/*     */         } else {
/* 297 */           List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, (LivingEntity)this, getBoundingBox().inflate(20.0D, 8.0D, 20.0D));
/* 299 */           if (!entities.isEmpty()) {
/* 300 */             LivingEntity selected = entities.get(this.random.nextInt(entities.size()));
/* 301 */             setAlternativeTarget(i, selected.getId());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 306 */     if (getTarget() != null) {
/* 307 */       setAlternativeTarget(0, getTarget().getId());
/*     */     } else {
/* 309 */       setAlternativeTarget(0, 0);
/*     */     } 
/* 312 */     if (this.destroyBlocksTick > 0) {
/* 313 */       this.destroyBlocksTick--;
/* 315 */       if (this.destroyBlocksTick == 0 && ((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 316 */         boolean destroyed = false;
/* 318 */         int width = Mth.floor(getBbWidth() / 2.0F + 1.0F);
/* 319 */         int height = Mth.floor(getBbHeight());
/* 321 */         for (BlockPos blockPos : BlockPos.betweenClosed(
/* 322 */             getBlockX() - width, getBlockY(), getBlockZ() - width, 
/* 323 */             getBlockX() + width, getBlockY() + height, getBlockZ() + width)) {
/* 325 */           BlockState state = level.getBlockState(blockPos);
/* 326 */           if (canDestroy(state))
/* 327 */             destroyed = (level.destroyBlock(blockPos, true, (Entity)this) || destroyed); 
/*     */         } 
/* 330 */         if (destroyed)
/* 331 */           level.levelEvent(null, 1022, blockPosition(), 0); 
/*     */       } 
/*     */     } 
/* 336 */     if (this.tickCount % 20 == 0)
/* 337 */       heal(1.0F); 
/* 340 */     this.bossEvent.setProgress(getHealth() / getMaxHealth());
/*     */   }
/*     */   
/*     */   public static boolean canDestroy(BlockState state) {
/* 344 */     return (!state.isAir() && !state.is(BlockTags.WITHER_IMMUNE));
/*     */   }
/*     */   
/*     */   public void makeInvulnerable() {
/* 348 */     setInvulnerableTicks(220);
/* 349 */     this.bossEvent.setProgress(0.0F);
/* 350 */     setHealth(getMaxHealth() / 3.0F);
/*     */   }
/*     */   
/*     */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {}
/*     */   
/*     */   public void startSeenByPlayer(ServerPlayer player) {
/* 359 */     super.startSeenByPlayer(player);
/* 360 */     this.bossEvent.addPlayer(player);
/*     */   }
/*     */   
/*     */   public void stopSeenByPlayer(ServerPlayer player) {
/* 365 */     super.stopSeenByPlayer(player);
/* 366 */     this.bossEvent.removePlayer(player);
/*     */   }
/*     */   
/*     */   private double getHeadX(int index) {
/* 370 */     if (index <= 0)
/* 371 */       return getX(); 
/* 373 */     float headAngle = (this.yBodyRot + (180 * (index - 1))) * 0.017453292F;
/* 374 */     float cos = Mth.cos(headAngle);
/* 375 */     return getX() + cos * 1.3D * getScale();
/*     */   }
/*     */   
/*     */   private double getHeadY(int index) {
/* 379 */     float height = (index <= 0) ? 3.0F : 2.2F;
/* 380 */     return getY() + (height * getScale());
/*     */   }
/*     */   
/*     */   private double getHeadZ(int index) {
/* 384 */     if (index <= 0)
/* 385 */       return getZ(); 
/* 387 */     float headAngle = (this.yBodyRot + (180 * (index - 1))) * 0.017453292F;
/* 388 */     float sin = Mth.sin(headAngle);
/* 389 */     return getZ() + sin * 1.3D * getScale();
/*     */   }
/*     */   
/*     */   private float rotlerp(float a, float b, float max) {
/* 393 */     float diff = Mth.wrapDegrees(b - a);
/* 394 */     if (diff > max)
/* 395 */       diff = max; 
/* 397 */     if (diff < -max)
/* 398 */       diff = -max; 
/* 400 */     return a + diff;
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int head, LivingEntity target) {
/* 404 */     performRangedAttack(head, target.getX(), target.getY() + target.getEyeHeight() * 0.5D, target.getZ(), (head == 0 && this.random.nextFloat() < 0.001F));
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int head, double tx, double ty, double tz, boolean dangerous) {
/* 408 */     if (!isSilent())
/* 409 */       level().levelEvent(null, 1024, blockPosition(), 0); 
/* 412 */     double hx = getHeadX(head);
/* 413 */     double hy = getHeadY(head);
/* 414 */     double hz = getHeadZ(head);
/* 416 */     double xd = tx - hx;
/* 417 */     double yd = ty - hy;
/* 418 */     double zd = tz - hz;
/* 419 */     Vec3 direction = new Vec3(xd, yd, zd);
/* 421 */     WitherSkull entity = new WitherSkull(level(), (LivingEntity)this, direction.normalize());
/* 422 */     entity.setOwner((Entity)this);
/* 423 */     if (dangerous)
/* 424 */       entity.setDangerous(true); 
/* 427 */     entity.setPos(hx, hy, hz);
/* 428 */     level().addFreshEntity((Entity)entity);
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 433 */     performRangedAttack(0, target);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 438 */     if (isInvulnerableTo(level, source))
/* 439 */       return false; 
/* 441 */     if (source.is(DamageTypeTags.WITHER_IMMUNE_TO) || source.getEntity() instanceof WitherBoss)
/* 442 */       return false; 
/* 444 */     if (getInvulnerableTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
/* 445 */       return false; 
/* 448 */     if (isPowered()) {
/* 449 */       Entity directEntity = source.getDirectEntity();
/* 450 */       if (directEntity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow || directEntity instanceof net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge)
/* 451 */         return false; 
/*     */     } 
/* 455 */     Entity sourceEntity = source.getEntity();
/* 456 */     if (sourceEntity != null && 
/* 457 */       sourceEntity.is(EntityTypeTags.WITHER_FRIENDS))
/* 459 */       return false; 
/* 462 */     if (this.destroyBlocksTick <= 0)
/* 463 */       this.destroyBlocksTick = 20; 
/* 466 */     for (int i = 0; i < this.idleHeadUpdates.length; i++)
/* 467 */       this.idleHeadUpdates[i] = this.idleHeadUpdates[i] + 3; 
/* 469 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/* 474 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/* 475 */     ItemEntity netherStar = spawnAtLocation(level, (ItemLike)Items.NETHER_STAR);
/* 476 */     if (netherStar != null)
/* 477 */       netherStar.setExtendedLifetime(); 
/*     */   }
/*     */   
/*     */   public void checkDespawn() {
/* 483 */     if (level().getDifficulty() == Difficulty.PEACEFUL && !getType().isAllowedInPeaceful()) {
/* 484 */       discard();
/*     */       return;
/*     */     } 
/* 488 */     this.noActionTime = 0;
/*     */   }
/*     */   
/*     */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 493 */     return false;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 497 */     return Monster.createMonsterAttributes()
/* 498 */       .add(Attributes.MAX_HEALTH, 300.0D)
/* 499 */       .add(Attributes.MOVEMENT_SPEED, 0.6000000238418579D)
/* 500 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/* 501 */       .add(Attributes.FOLLOW_RANGE, 40.0D)
/* 502 */       .add(Attributes.ARMOR, 4.0D);
/*     */   }
/*     */   
/*     */   public float[] getHeadYRots() {
/* 506 */     return this.yRotHeads;
/*     */   }
/*     */   
/*     */   public float[] getHeadXRots() {
/* 510 */     return this.xRotHeads;
/*     */   }
/*     */   
/*     */   public int getInvulnerableTicks() {
/* 514 */     return ((Integer)this.entityData.get(DATA_ID_INV)).intValue();
/*     */   }
/*     */   
/*     */   public void setInvulnerableTicks(int invulnerableTicks) {
/* 518 */     this.entityData.set(DATA_ID_INV, Integer.valueOf(invulnerableTicks));
/*     */   }
/*     */   
/*     */   public int getAlternativeTarget(int headIndex) {
/* 522 */     return ((Integer)this.entityData.get(DATA_TARGETS.get(headIndex))).intValue();
/*     */   }
/*     */   
/*     */   public void setAlternativeTarget(int headIndex, int entityId) {
/* 526 */     this.entityData.set(DATA_TARGETS.get(headIndex), Integer.valueOf(entityId));
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/* 530 */     return (getHealth() <= getMaxHealth() / 2.0F);
/*     */   }
/*     */   
/*     */   protected boolean canRide(Entity vehicle) {
/* 535 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 540 */     return false;
/*     */   }
/*     */   
/*     */   private class WitherDoNothingGoal extends Goal {
/*     */     public WitherDoNothingGoal() {
/* 545 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 550 */       return (WitherBoss.this.getInvulnerableTicks() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 556 */     if (newEffect.is(MobEffects.WITHER))
/* 557 */       return false; 
/* 559 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\boss\wither\WitherBoss.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */