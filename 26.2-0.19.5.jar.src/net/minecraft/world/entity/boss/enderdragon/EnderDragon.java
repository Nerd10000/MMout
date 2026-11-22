/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.end.EnderDragonFight;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.BinaryHeap;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnderDragon extends Mob implements Enemy {
/*  61 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  63 */   public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);
/*     */   
/*  65 */   private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = TargetingConditions.forCombat().range(64.0D);
/*     */   
/*     */   private static final int GROWL_INTERVAL_MIN = 200;
/*     */   
/*     */   private static final int GROWL_INTERVAL_MAX = 400;
/*     */   
/*     */   private static final float SITTING_ALLOWED_DAMAGE_PERCENTAGE = 0.25F;
/*     */   
/*     */   private static final String DRAGON_DEATH_TIME_KEY = "DragonDeathTime";
/*     */   
/*     */   private static final String DRAGON_PHASE_KEY = "DragonPhase";
/*     */   
/*     */   private static final String SITTING_DAMAGE_RECEIVED_KEY = "sitting_damage_received";
/*     */   
/*     */   private static final int DEFAULT_DEATH_TIME = 0;
/*     */   
/*  75 */   public final DragonFlightHistory flightHistory = new DragonFlightHistory();
/*     */   
/*     */   private final EnderDragonPart[] subEntities;
/*     */   
/*     */   public final EnderDragonPart head;
/*     */   
/*     */   private final EnderDragonPart neck;
/*     */   
/*     */   private final EnderDragonPart body;
/*     */   
/*     */   private final EnderDragonPart tail1;
/*     */   
/*     */   private final EnderDragonPart tail2;
/*     */   
/*     */   private final EnderDragonPart tail3;
/*     */   
/*     */   private final EnderDragonPart wing1;
/*     */   
/*     */   private final EnderDragonPart wing2;
/*     */   
/*     */   public float oFlapTime;
/*     */   
/*     */   public float flapTime;
/*     */   
/*     */   public boolean inWall;
/*     */   
/*  90 */   public int dragonDeathTime = 0;
/*     */   
/*     */   public float yRotA;
/*     */   
/*     */   public EndCrystal nearestCrystal;
/*     */   
/*     */   private EnderDragonFight dragonFight;
/*     */   
/*  96 */   private BlockPos fightOrigin = BlockPos.ZERO;
/*     */   
/*     */   private final EnderDragonPhaseManager phaseManager;
/*     */   
/*  98 */   private int growlTime = 100;
/*     */   
/*     */   private float sittingDamageReceived;
/*     */   
/* 100 */   private final Node[] nodes = new Node[24];
/*     */   
/* 101 */   private final int[] nodeAdjacency = new int[24];
/*     */   
/* 102 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public EnderDragon(EntityType<? extends EnderDragon> type, Level level) {
/* 105 */     super(EntityTypes.ENDER_DRAGON, level);
/* 107 */     this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
/* 108 */     this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
/* 109 */     this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
/* 110 */     this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 111 */     this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 112 */     this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 113 */     this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 114 */     this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 116 */     this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
/* 118 */     setHealth(getMaxHealth());
/* 120 */     this.noPhysics = true;
/* 122 */     this.phaseManager = new EnderDragonPhaseManager(this);
/*     */   }
/*     */   
/*     */   public void setDragonFight(EnderDragonFight fight) {
/* 126 */     this.dragonFight = fight;
/*     */   }
/*     */   
/*     */   public void setFightOrigin(BlockPos fightOrigin) {
/* 130 */     this.fightOrigin = fightOrigin;
/*     */   }
/*     */   
/*     */   public BlockPos getFightOrigin() {
/* 134 */     return this.fightOrigin;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 138 */     return Mob.createMobAttributes()
/* 139 */       .add(Attributes.MAX_HEALTH, 200.0D)
/* 140 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*     */   }
/*     */   
/*     */   public boolean isFlapping() {
/* 145 */     float flap = Mth.cos((this.flapTime * 6.2831855F));
/* 146 */     float oldFlap = Mth.cos((this.oFlapTime * 6.2831855F));
/* 148 */     return (oldFlap <= -0.3F && flap >= -0.3F);
/*     */   }
/*     */   
/*     */   public void onFlap() {
/* 153 */     if (level().isClientSide() && !isSilent())
/* 154 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_FLAP, getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false); 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 160 */     super.defineSynchedData(entityData);
/* 161 */     entityData.define(DATA_PHASE, Integer.valueOf(EnderDragonPhase.HOVERING.getId()));
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*     */     ServerLevel level;
/* 166 */     processFlappingMovement();
/* 168 */     if (level().isClientSide()) {
/* 169 */       setHealth(getHealth());
/* 171 */       if (!isSilent() && 
/* 172 */         !this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
/* 173 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, getSoundSource(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
/* 174 */         this.growlTime = 200 + this.random.nextInt(200);
/*     */       } 
/*     */     } 
/* 179 */     if (this.dragonFight == null) {
/* 179 */       Level level2 = level();
/* 179 */       if (level2 instanceof ServerLevel) {
/* 179 */         ServerLevel serverLevel = (ServerLevel)level2;
/* 180 */         EnderDragonFight maybeOurFight = serverLevel.getDragonFight();
/* 181 */         if (maybeOurFight != null && getUUID().equals(maybeOurFight.dragonUUID()))
/* 182 */           this.dragonFight = maybeOurFight; 
/*     */       } 
/*     */     } 
/* 186 */     this.oFlapTime = this.flapTime;
/* 188 */     if (isDeadOrDying()) {
/* 189 */       float xo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 190 */       float yo = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 191 */       float zo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 192 */       level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION, getX() + xo, getY() + 2.0D + yo, getZ() + zo, 0.0D, 0.0D, 0.0D);
/*     */       return;
/*     */     } 
/* 196 */     checkCrystals();
/* 198 */     Vec3 movement = getDeltaMovement();
/* 199 */     float flapSpeed = 0.2F / ((float)movement.horizontalDistance() * 10.0F + 1.0F);
/* 200 */     flapSpeed *= (float)Math.pow(2.0D, movement.y);
/* 201 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 202 */       this.flapTime += 0.1F;
/* 203 */     } else if (this.inWall) {
/* 204 */       this.flapTime += flapSpeed * 0.5F;
/*     */     } else {
/* 206 */       this.flapTime += flapSpeed;
/*     */     } 
/* 209 */     setYRot(Mth.wrapDegrees(getYRot()));
/* 211 */     if (isNoAi()) {
/* 212 */       this.flapTime = 0.5F;
/*     */       return;
/*     */     } 
/* 216 */     this.flightHistory.record(getY(), getYRot());
/* 218 */     Level level1 = level();
/* 218 */     if (level1 instanceof ServerLevel) {
/* 218 */       level = (ServerLevel)level1;
/*     */     } else {
/* 219 */       this.interpolation.interpolate();
/* 221 */       this.phaseManager.getCurrentPhase().doClientTick();
/* 274 */       if (!level().isClientSide())
/* 275 */         applyEffectsFromBlocks(); 
/*     */     } 
/*     */     DragonPhaseInstance currentPhase = this.phaseManager.getCurrentPhase();
/*     */     currentPhase.doServerTick(level);
/*     */     if (this.phaseManager.getCurrentPhase() != currentPhase) {
/*     */       currentPhase = this.phaseManager.getCurrentPhase();
/*     */       currentPhase.doServerTick(level);
/*     */     } 
/*     */     Vec3 targetLocation = currentPhase.getFlyTargetLocation();
/*     */     if (targetLocation != null) {
/*     */       double xdd = targetLocation.x - getX();
/*     */       double ydd = targetLocation.y - getY();
/*     */       double zdd = targetLocation.z - getZ();
/*     */       double distToTarget = xdd * xdd + ydd * ydd + zdd * zdd;
/*     */       float max = currentPhase.getFlySpeed();
/*     */       double horizontalDist = Math.sqrt(xdd * xdd + zdd * zdd);
/*     */       if (horizontalDist > 0.0D)
/*     */         ydd = Mth.clamp(ydd / horizontalDist, -max, max); 
/*     */       setDeltaMovement(getDeltaMovement().add(0.0D, ydd * 0.01D, 0.0D));
/*     */       setYRot(Mth.wrapDegrees(getYRot()));
/*     */       Vec3 aim = targetLocation.subtract(getX(), getY(), getZ()).normalize();
/*     */       Vec3 dir = (new Vec3(Mth.sin((getYRot() * 0.017453292F)), (getDeltaMovement()).y, -Mth.cos((getYRot() * 0.017453292F)))).normalize();
/*     */       float dot = Math.max(((float)dir.dot(aim) + 0.5F) / 1.5F, 0.0F);
/*     */       if (Math.abs(xdd) > 9.999999747378752E-6D || Math.abs(zdd) > 9.999999747378752E-6D) {
/*     */         float yRotD = Mth.clamp(Mth.wrapDegrees(180.0F - (float)Mth.atan2(xdd, zdd) * 57.295776F - getYRot()), -50.0F, 50.0F);
/*     */         this.yRotA *= 0.8F;
/*     */         this.yRotA += yRotD * currentPhase.getTurnSpeed();
/*     */         setYRot(getYRot() + this.yRotA * 0.1F);
/*     */       } 
/*     */       float span = (float)(2.0D / (distToTarget + 1.0D));
/*     */       float speed = 0.06F;
/*     */       moveRelative(0.06F * (dot * span + 1.0F - span), new Vec3(0.0D, 0.0D, -1.0D));
/*     */       if (this.inWall) {
/*     */         move(MoverType.SELF, getDeltaMovement().scale(0.800000011920929D));
/*     */       } else {
/*     */         move(MoverType.SELF, getDeltaMovement());
/*     */       } 
/*     */       Vec3 actual = getDeltaMovement().normalize();
/*     */       double slide = 0.8D + 0.15D * (actual.dot(dir) + 1.0D) / 2.0D;
/*     */       setDeltaMovement(getDeltaMovement().multiply(slide, 0.9100000262260437D, slide));
/*     */     } 
/*     */     if (!level().isClientSide())
/* 275 */       applyEffectsFromBlocks(); 
/*     */   }
/*     */   
/*     */   private void tickPart(EnderDragonPart part, double x, double y, double z) {
/* 355 */     part.setPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   private float getHeadYOffset() {
/* 359 */     if (this.phaseManager.getCurrentPhase().isSitting())
/* 360 */       return -1.0F; 
/* 362 */     DragonFlightHistory.Sample p0 = this.flightHistory.get(5);
/* 363 */     DragonFlightHistory.Sample p1 = this.flightHistory.get(0);
/* 364 */     return (float)(p0.y() - p1.y());
/*     */   }
/*     */   
/*     */   private void checkCrystals() {
/* 368 */     if (this.nearestCrystal != null)
/* 369 */       if (this.nearestCrystal.isRemoved()) {
/* 370 */         this.nearestCrystal = null;
/* 371 */       } else if (this.tickCount % 10 == 0 && 
/* 372 */         getHealth() < getMaxHealth()) {
/* 373 */         setHealth(getHealth() + 1.0F);
/*     */       }  
/* 378 */     if (this.random.nextInt(10) == 0) {
/* 379 */       List<EndCrystal> crystals = level().getEntitiesOfClass(EndCrystal.class, getBoundingBox().inflate(32.0D));
/* 381 */       EndCrystal nearest = null;
/* 382 */       double distance = Double.MAX_VALUE;
/* 383 */       for (EndCrystal crystal : crystals) {
/* 384 */         double dist = crystal.distanceToSqr((Entity)this);
/* 385 */         if (dist < distance) {
/* 386 */           distance = dist;
/* 387 */           nearest = crystal;
/*     */         } 
/*     */       } 
/* 391 */       this.nearestCrystal = nearest;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void knockBack(ServerLevel serverLevel, List<Entity> entities) {
/* 396 */     double xm = ((this.body.getBoundingBox()).minX + (this.body.getBoundingBox()).maxX) / 2.0D;
/* 397 */     double zm = ((this.body.getBoundingBox()).minZ + (this.body.getBoundingBox()).maxZ) / 2.0D;
/* 399 */     for (Entity entity : entities) {
/* 400 */       if (entity instanceof LivingEntity) {
/* 400 */         LivingEntity livingTarget = (LivingEntity)entity;
/* 401 */         double xd = entity.getX() - xm;
/* 402 */         double zd = entity.getZ() - zm;
/* 403 */         double dd = Math.max(xd * xd + zd * zd, 0.1D);
/* 404 */         entity.push(xd / dd * 4.0D, 0.20000000298023224D, zd / dd * 4.0D);
/* 405 */         if (!this.phaseManager.getCurrentPhase().isSitting() && livingTarget.getLastHurtByMobTimestamp() < entity.tickCount - 2) {
/* 406 */           DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 407 */           entity.hurtServer(serverLevel, damageSource, 5.0F);
/* 408 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hurt(ServerLevel level, List<Entity> entities) {
/* 415 */     for (Entity target : entities) {
/* 416 */       if (target instanceof LivingEntity) {
/* 417 */         DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 418 */         target.hurtServer(level, damageSource, 10.0F);
/* 419 */         EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float rotWrap(double d) {
/* 425 */     return (float)Mth.wrapDegrees(d);
/*     */   }
/*     */   
/*     */   private boolean checkWalls(ServerLevel level, AABB bb) {
/* 429 */     int x0 = Mth.floor(bb.minX);
/* 430 */     int y0 = Mth.floor(bb.minY);
/* 431 */     int z0 = Mth.floor(bb.minZ);
/* 432 */     int x1 = Mth.floor(bb.maxX);
/* 433 */     int y1 = Mth.floor(bb.maxY);
/* 434 */     int z1 = Mth.floor(bb.maxZ);
/* 435 */     boolean hitWall = false;
/* 436 */     boolean destroyedBlock = false;
/* 437 */     for (int x = x0; x <= x1; x++) {
/* 438 */       for (int y = y0; y <= y1; y++) {
/* 439 */         for (int z = z0; z <= z1; z++) {
/* 440 */           BlockPos blockPos = new BlockPos(x, y, z);
/* 441 */           BlockState state = level.getBlockState(blockPos);
/* 442 */           if (!state.isAir() && !state.is(BlockTags.DRAGON_TRANSPARENT))
/* 444 */             if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || state.is(BlockTags.DRAGON_IMMUNE)) {
/* 445 */               hitWall = true;
/*     */             } else {
/* 447 */               destroyedBlock = (level.removeBlock(blockPos, false) || destroyedBlock);
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/* 453 */     if (destroyedBlock) {
/* 457 */       BlockPos randomPos = new BlockPos(x0 + this.random.nextInt(x1 - x0 + 1), y0 + this.random.nextInt(y1 - y0 + 1), z0 + this.random.nextInt(z1 - z0 + 1));
/* 459 */       level.levelEvent(2008, randomPos, 0);
/*     */     } 
/* 462 */     return hitWall;
/*     */   }
/*     */   
/*     */   public boolean hurt(ServerLevel level, EnderDragonPart part, DamageSource source, float damage) {
/* 466 */     if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING)
/* 467 */       return false; 
/* 470 */     damage = this.phaseManager.getCurrentPhase().onHurt(source, damage);
/* 472 */     if (part != this.head)
/* 473 */       damage = damage / 4.0F + Math.min(damage, 1.0F); 
/* 476 */     if (damage < 0.01F)
/* 477 */       return false; 
/* 480 */     if (source.getEntity() instanceof Player || source.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
/* 481 */       float healthBefore = getHealth();
/* 482 */       reallyHurt(level, source, damage);
/* 483 */       if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 484 */         this.sittingDamageReceived = this.sittingDamageReceived + healthBefore - getHealth();
/* 486 */         if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
/* 487 */           this.sittingDamageReceived = 0.0F;
/* 488 */           this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
/*     */         } 
/*     */       } 
/*     */     } 
/* 493 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 498 */     return hurt(level, this.body, source, damage);
/*     */   }
/*     */   
/*     */   protected void reallyHurt(ServerLevel level, DamageSource source, float damage) {
/* 502 */     super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected void handleKillingBlow() {
/* 507 */     if (!this.phaseManager.getCurrentPhase().isSitting()) {
/* 508 */       setHealth(1.0F);
/* 509 */       this.phaseManager.setPhase(EnderDragonPhase.DYING);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void knockback(double power, double xd, double zd, DamageSource source, float damage) {
/* 515 */     if (this.phaseManager.getCurrentPhase().isSitting())
/*     */       return; 
/* 518 */     super.knockback(power, xd, zd, source, damage);
/*     */   }
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 523 */     remove(Entity.RemovalReason.KILLED);
/* 524 */     gameEvent((Holder)GameEvent.ENTITY_DIE);
/* 526 */     if (this.dragonFight != null) {
/* 527 */       this.dragonFight.updateDragon(this);
/* 528 */       this.dragonFight.setDragonKilled(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tickDeath() {
/* 534 */     if (this.dragonFight != null)
/* 535 */       this.dragonFight.updateDragon(this); 
/* 538 */     this.dragonDeathTime++;
/* 539 */     if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
/* 540 */       float xo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 541 */       float yo = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 542 */       float zo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 543 */       level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, getX() + xo, getY() + 2.0D + yo, getZ() + zo, 0.0D, 0.0D, 0.0D);
/*     */     } 
/* 546 */     int xpCount = 500;
/* 547 */     if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon())
/* 548 */       xpCount = 12000; 
/* 551 */     Level level = level();
/* 551 */     if (level instanceof ServerLevel) {
/* 551 */       ServerLevel serverLevel = (ServerLevel)level;
/* 552 */       if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 553 */         ExperienceOrb.award(serverLevel, position(), Mth.floor(xpCount * 0.08F)); 
/* 555 */       if (this.dragonDeathTime == 1 && !isSilent())
/* 556 */         serverLevel.globalLevelEvent(1028, blockPosition(), 0); 
/*     */     } 
/* 560 */     Vec3 deathMove = new Vec3(0.0D, 0.10000000149011612D, 0.0D);
/* 561 */     move(MoverType.SELF, deathMove);
/* 562 */     for (EnderDragonPart dragonPart : this.subEntities) {
/* 563 */       dragonPart.setOldPosAndRot();
/* 564 */       dragonPart.setPos(dragonPart.position().add(deathMove));
/*     */     } 
/* 567 */     if (this.dragonDeathTime >= 200) {
/* 567 */       Level level1 = level();
/* 567 */       if (level1 instanceof ServerLevel) {
/* 567 */         ServerLevel serverLevel = (ServerLevel)level1;
/* 568 */         if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 569 */           ExperienceOrb.award(serverLevel, position(), Mth.floor(xpCount * 0.2F)); 
/* 571 */         if (this.dragonFight != null)
/* 572 */           this.dragonFight.setDragonKilled(this); 
/* 574 */         remove(Entity.RemovalReason.KILLED);
/* 575 */         gameEvent((Holder)GameEvent.ENTITY_DIE);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int findClosestNode() {
/* 581 */     if (this.nodes[0] == null) {
/* 587 */       for (int i = 0; i < 24; i++) {
/* 588 */         int nodeX, nodeZ, yAdjustment = 5;
/* 589 */         int multiplier = i;
/* 593 */         if (i < 12) {
/* 594 */           nodeX = Mth.floor(60.0F * Mth.cos((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 595 */           nodeZ = Mth.floor(60.0F * Mth.sin((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 596 */         } else if (i < 20) {
/* 597 */           multiplier -= 12;
/* 598 */           nodeX = Mth.floor(40.0F * Mth.cos((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 599 */           nodeZ = Mth.floor(40.0F * Mth.sin((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 600 */           yAdjustment += 10;
/*     */         } else {
/* 602 */           multiplier -= 20;
/* 603 */           nodeX = Mth.floor(20.0F * Mth.cos((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/* 604 */           nodeZ = Mth.floor(20.0F * Mth.sin((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/*     */         } 
/* 608 */         int nodeY = Math.max(73, level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(nodeX, 0, nodeZ)).getY() + yAdjustment);
/* 610 */         this.nodes[i] = new Node(nodeX, nodeY, nodeZ);
/*     */       } 
/* 613 */       this.nodeAdjacency[0] = 6146;
/* 614 */       this.nodeAdjacency[1] = 8197;
/* 615 */       this.nodeAdjacency[2] = 8202;
/* 616 */       this.nodeAdjacency[3] = 16404;
/* 617 */       this.nodeAdjacency[4] = 32808;
/* 618 */       this.nodeAdjacency[5] = 32848;
/* 619 */       this.nodeAdjacency[6] = 65696;
/* 620 */       this.nodeAdjacency[7] = 131392;
/* 621 */       this.nodeAdjacency[8] = 131712;
/* 622 */       this.nodeAdjacency[9] = 263424;
/* 623 */       this.nodeAdjacency[10] = 526848;
/* 624 */       this.nodeAdjacency[11] = 525313;
/* 626 */       this.nodeAdjacency[12] = 1581057;
/* 627 */       this.nodeAdjacency[13] = 3166214;
/* 628 */       this.nodeAdjacency[14] = 2138120;
/* 629 */       this.nodeAdjacency[15] = 6373424;
/* 630 */       this.nodeAdjacency[16] = 4358208;
/* 631 */       this.nodeAdjacency[17] = 12910976;
/* 632 */       this.nodeAdjacency[18] = 9044480;
/* 633 */       this.nodeAdjacency[19] = 9706496;
/* 635 */       this.nodeAdjacency[20] = 15216640;
/* 636 */       this.nodeAdjacency[21] = 13688832;
/* 637 */       this.nodeAdjacency[22] = 11763712;
/* 638 */       this.nodeAdjacency[23] = 8257536;
/*     */     } 
/* 641 */     return findClosestNode(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public int findClosestNode(double tX, double tY, double tZ) {
/* 645 */     float closestDist = 10000.0F;
/* 646 */     int closestIndex = 0;
/* 647 */     Node currentPos = new Node(Mth.floor(tX), Mth.floor(tY), Mth.floor(tZ));
/* 648 */     int startIndex = 0;
/* 650 */     if (this.dragonFight == null || this.dragonFight.aliveCrystals() == 0)
/* 652 */       startIndex = 12; 
/* 655 */     for (int i = startIndex; i < 24; i++) {
/* 656 */       if (this.nodes[i] != null) {
/* 657 */         float dist = this.nodes[i].distanceToSqr(currentPos);
/* 658 */         if (dist < closestDist) {
/* 659 */           closestDist = dist;
/* 660 */           closestIndex = i;
/*     */         } 
/*     */       } 
/*     */     } 
/* 664 */     return closestIndex;
/*     */   }
/*     */   
/*     */   public Path findPath(int startIndex, int endIndex, Node finalNode) {
/* 668 */     for (int i = 0; i < 24; i++) {
/* 669 */       Node node = this.nodes[i];
/* 670 */       node.closed = false;
/* 671 */       node.f = 0.0F;
/* 672 */       node.g = 0.0F;
/* 673 */       node.h = 0.0F;
/* 674 */       node.cameFrom = null;
/* 675 */       node.heapIdx = -1;
/*     */     } 
/* 678 */     Node from = this.nodes[startIndex];
/* 679 */     Node to = this.nodes[endIndex];
/* 681 */     from.g = 0.0F;
/* 682 */     from.h = from.distanceTo(to);
/* 683 */     from.f = from.h;
/* 685 */     this.openSet.clear();
/* 686 */     this.openSet.insert(from);
/* 688 */     Node closest = from;
/* 690 */     int minimumNodeIndex = 0;
/* 691 */     if (this.dragonFight == null || this.dragonFight.aliveCrystals() == 0)
/* 693 */       minimumNodeIndex = 12; 
/* 696 */     while (!this.openSet.isEmpty()) {
/* 697 */       Node openNode = this.openSet.pop();
/* 699 */       if (openNode.equals(to)) {
/* 700 */         if (finalNode != null) {
/* 701 */           finalNode.cameFrom = to;
/* 702 */           to = finalNode;
/*     */         } 
/* 704 */         return reconstructPath(from, to);
/*     */       } 
/* 707 */       if (openNode.distanceTo(to) < closest.distanceTo(to))
/* 708 */         closest = openNode; 
/* 710 */       openNode.closed = true;
/* 712 */       int xIndex = 0;
/*     */       int j;
/* 713 */       for (j = 0; j < 24; j++) {
/* 714 */         if (this.nodes[j] == openNode) {
/* 715 */           xIndex = j;
/*     */           break;
/*     */         } 
/*     */       } 
/* 720 */       for (j = minimumNodeIndex; j < 24; j++) {
/* 721 */         if ((this.nodeAdjacency[xIndex] & 1 << j) > 0) {
/* 722 */           Node adjacentNode = this.nodes[j];
/* 724 */           if (!adjacentNode.closed) {
/* 728 */             float tentativeGScore = openNode.g + openNode.distanceTo(adjacentNode);
/* 729 */             if (!adjacentNode.inOpenSet() || tentativeGScore < adjacentNode.g) {
/* 730 */               adjacentNode.cameFrom = openNode;
/* 731 */               adjacentNode.g = tentativeGScore;
/* 732 */               adjacentNode.h = adjacentNode.distanceTo(to);
/* 733 */               if (adjacentNode.inOpenSet()) {
/* 734 */                 this.openSet.changeCost(adjacentNode, adjacentNode.g + adjacentNode.h);
/*     */               } else {
/* 736 */                 adjacentNode.f = adjacentNode.g + adjacentNode.h;
/* 737 */                 this.openSet.insert(adjacentNode);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 744 */     if (closest == from)
/* 745 */       return null; 
/* 747 */     LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(startIndex), Integer.valueOf(endIndex));
/* 748 */     if (finalNode != null) {
/* 749 */       finalNode.cameFrom = closest;
/* 750 */       closest = finalNode;
/*     */     } 
/* 752 */     return reconstructPath(from, closest);
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node from, Node to) {
/* 756 */     List<Node> nodes = Lists.newArrayList();
/* 757 */     Node node = to;
/* 758 */     nodes.add(0, node);
/* 759 */     while (node.cameFrom != null) {
/* 760 */       node = node.cameFrom;
/* 761 */       nodes.add(0, node);
/*     */     } 
/* 763 */     return new Path(nodes, new BlockPos(to.x, to.y, to.z), true);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 768 */     super.addAdditionalSaveData(output);
/* 769 */     output.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
/* 770 */     output.putInt("DragonDeathTime", this.dragonDeathTime);
/* 771 */     output.putFloat("sitting_damage_received", this.sittingDamageReceived);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 776 */     super.readAdditionalSaveData(input);
/* 777 */     input.getInt("DragonPhase").ifPresent(phaseId -> this.phaseManager.setPhase(EnderDragonPhase.getById(phaseId.intValue())));
/* 780 */     this.dragonDeathTime = input.getIntOr("DragonDeathTime", 0);
/* 781 */     this.sittingDamageReceived = input.getFloatOr("sitting_damage_received", 0.0F);
/*     */   }
/*     */   
/*     */   public void checkDespawn() {}
/*     */   
/*     */   public EnderDragonPart[] getSubEntities() {
/* 789 */     return this.subEntities;
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 794 */     return false;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 799 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 804 */     return SoundEvents.ENDER_DRAGON_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 809 */     return SoundEvents.ENDER_DRAGON_HURT;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 814 */     return 5.0F;
/*     */   }
/*     */   
/*     */   public Vec3 getHeadLookVector(float a) {
/*     */     Vec3 result;
/* 818 */     DragonPhaseInstance phaseInstance = this.phaseManager.getCurrentPhase();
/* 819 */     EnderDragonPhase<? extends DragonPhaseInstance> phase = phaseInstance.getPhase();
/* 822 */     if (phase == EnderDragonPhase.LANDING || phase == EnderDragonPhase.TAKEOFF) {
/* 823 */       BlockPos egg = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
/* 824 */       float dist = Math.max((float)Math.sqrt(egg.distToCenterSqr((Position)position())) / 4.0F, 1.0F);
/* 825 */       float yOffset = 6.0F / dist;
/* 827 */       float xRotOld = getXRot();
/* 828 */       float rotScale = 1.5F;
/* 829 */       setXRot(-yOffset * 1.5F * 5.0F);
/* 831 */       result = getViewVector(a);
/* 832 */       setXRot(xRotOld);
/* 833 */     } else if (phaseInstance.isSitting()) {
/* 834 */       float xRotOld = getXRot();
/* 835 */       float rotScale = 1.5F;
/* 836 */       setXRot(-45.0F);
/* 838 */       result = getViewVector(a);
/* 839 */       setXRot(xRotOld);
/*     */     } else {
/* 841 */       result = getViewVector(a);
/*     */     } 
/* 844 */     return result;
/*     */   }
/*     */   
/*     */   public void onCrystalDestroyed(ServerLevel level, EndCrystal crystal, BlockPos pos, DamageSource source) {
/*     */     Player player;
/* 850 */     Entity entity = source.getEntity();
/* 850 */     if (entity instanceof Player) {
/* 850 */       Player playerSource = (Player)entity;
/* 851 */       player = playerSource;
/*     */     } else {
/* 853 */       player = level.getNearestPlayer(CRYSTAL_DESTROY_TARGETING, pos.getX(), pos.getY(), pos.getZ());
/*     */     } 
/* 856 */     if (crystal == this.nearestCrystal)
/* 857 */       hurt(level, this.head, damageSources().explosion(crystal, (Entity)player), 10.0F); 
/* 860 */     this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, source, player);
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 865 */     if (DATA_PHASE.equals(accessor) && level().isClientSide())
/* 866 */       this.phaseManager.setPhase(EnderDragonPhase.getById(((Integer)getEntityData().get(DATA_PHASE)).intValue())); 
/* 869 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public EnderDragonPhaseManager getPhaseManager() {
/* 873 */     return this.phaseManager;
/*     */   }
/*     */   
/*     */   public EnderDragonFight getDragonFight() {
/* 877 */     return this.dragonFight;
/*     */   }
/*     */   
/*     */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 882 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean canRide(Entity vehicle) {
/* 887 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 892 */     return false;
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 897 */     super.recreateFromPacket(packet);
/* 898 */     EnderDragonPart[] subEntities = getSubEntities();
/* 899 */     for (int i = 0; i < subEntities.length; i++)
/* 901 */       subEntities[i].setId(i + packet.getId() + 1); 
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 908 */     return target.canBeSeenAsEnemy();
/*     */   }
/*     */   
/*     */   protected float sanitizeScale(float scale) {
/* 914 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */