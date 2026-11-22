/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.ConversionType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public abstract class AbstractCubeMob extends AgeableMob {
/*  45 */   protected static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(AbstractCubeMob.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int MIN_SIZE = 1;
/*     */   
/*     */   public static final int MAX_SIZE = 127;
/*     */   
/*     */   public static final int MAX_NATURAL_SIZE = 4;
/*     */   
/*     */   private static final boolean DEFAULT_WAS_ON_GROUND = false;
/*     */   
/*     */   public float targetSquish;
/*     */   
/*     */   public float squish;
/*     */   
/*     */   public float oSquish;
/*     */   
/*     */   private boolean wasOnGround = false;
/*     */   
/*     */   protected AbstractCubeMob(EntityType<? extends AbstractCubeMob> type, Level level) {
/*  57 */     super(type, level);
/*  59 */     fixupDimensions();
/*  60 */     this.moveControl = new CubeMobMoveControl<>(this);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  65 */     this.goalSelector.addGoal(1, new CubeMobFloatGoal(this));
/*  66 */     this.goalSelector.addGoal(4, new CubeMobRandomDirectionGoal(this));
/*  67 */     this.goalSelector.addGoal(5, new CubeMobKeepOnJumpingGoal(this));
/*  68 */     addBehaviourGoals();
/*  69 */     addTargetingGoals();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  78 */     super.defineSynchedData(entityData);
/*  80 */     entityData.define(ID_SIZE, Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSize(int size, boolean updateHealth) {
/*  85 */     int actualSize = Mth.clamp(size, 1, 127);
/*  86 */     this.entityData.set(ID_SIZE, Integer.valueOf(actualSize));
/*  87 */     reapplyPosition();
/*  89 */     refreshDimensions();
/*  91 */     setcubeMobHealth(actualSize);
/*  92 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((0.2F + 0.1F * actualSize));
/*  93 */     if (updateHealth)
/*  94 */       setHealth(getMaxHealth()); 
/*     */   }
/*     */   
/*     */   protected void setcubeMobHealth(int actualSize) {
/*  99 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue((actualSize * actualSize));
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 103 */     return ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 108 */     super.addAdditionalSaveData(output);
/* 109 */     output.putInt("Size", getSize() - 1);
/* 110 */     output.putBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 115 */     setSize(input.getIntOr("Size", 0) + 1, false);
/* 116 */     super.readAdditionalSaveData(input);
/* 117 */     this.wasOnGround = input.getBooleanOr("wasOnGround", false);
/*     */   }
/*     */   
/*     */   public boolean isTiny() {
/* 121 */     return (getSize() <= 1);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 128 */     this.oSquish = this.squish;
/* 129 */     this.squish += (this.targetSquish - this.squish) * 0.5F;
/* 130 */     super.tick();
/* 132 */     if (onGround() && !this.wasOnGround) {
/* 133 */       float size = getDimensions(getPose()).width() * 2.0F;
/* 134 */       float radius = size / 2.0F;
/* 135 */       for (int i = 0; i < size * 16.0F; i++) {
/* 136 */         float dir = this.random.nextFloat() * 6.2831855F;
/* 137 */         float d = this.random.nextFloat() * 0.5F + 0.5F;
/* 138 */         float xd = Mth.sin(dir) * radius * d;
/* 139 */         float zd = Mth.cos(dir) * radius * d;
/* 140 */         ParticleOptions particleType = getParticleType();
/* 141 */         if (particleType != null)
/* 142 */           level().addParticle(particleType, getX() + xd, getY(), getZ() + zd, 0.0D, 0.0D, 0.0D); 
/*     */       } 
/* 146 */       playSound(getSquishSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/* 147 */       this.targetSquish = -0.5F;
/* 148 */     } else if (!onGround() && this.wasOnGround) {
/* 149 */       this.targetSquish = 1.0F;
/*     */     } 
/* 151 */     this.wasOnGround = onGround();
/* 152 */     decreaseSquish();
/*     */   }
/*     */   
/*     */   protected void decreaseSquish() {
/* 156 */     this.targetSquish *= 0.6F;
/*     */   }
/*     */   
/*     */   protected int getJumpDelay() {
/* 160 */     return this.random.nextInt(20) + 10;
/*     */   }
/*     */   
/*     */   public void refreshDimensions() {
/* 165 */     double oldX = getX();
/* 166 */     double oldY = getY();
/* 167 */     double oldZ = getZ();
/* 168 */     super.refreshDimensions();
/* 169 */     setPos(oldX, oldY, oldZ);
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 174 */     if (ID_SIZE.equals(accessor)) {
/* 175 */       refreshDimensions();
/* 176 */       setYRot(this.yHeadRot);
/* 177 */       this.yBodyRot = this.yHeadRot;
/* 179 */       if (isInWater() && 
/* 180 */         this.random.nextInt(20) == 0)
/* 181 */         doWaterSplashEffect(); 
/*     */     } 
/* 186 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public EntityType<? extends AbstractCubeMob> getType() {
/* 192 */     return super.getType();
/*     */   }
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/* 197 */     int size = getSize();
/* 198 */     if (!level().isClientSide() && size > 1 && isDeadOrDying()) {
/* 199 */       float width = getDimensions(getPose()).width();
/* 200 */       float xzCubeSpawnOffset = width / 2.0F;
/* 201 */       int halfSize = size / 2;
/* 203 */       int count = getSplitCount();
/* 204 */       PlayerTeam team = getTeam();
/* 205 */       for (int i = 0; i < count; i++) {
/* 206 */         float xd = ((i % 2) - 0.5F) * xzCubeSpawnOffset;
/* 207 */         float zd = ((i / 2) - 0.5F) * xzCubeSpawnOffset;
/* 208 */         convertTo(getType(), new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, team), EntitySpawnReason.TRIGGERED, cubeMob -> setUpSplitCube(cubeMob, halfSize, xd, zd));
/*     */       } 
/*     */     } 
/* 213 */     super.remove(reason);
/*     */   }
/*     */   
/*     */   protected void setUpSplitCube(AbstractCubeMob cubeMob, int halfSize, float xd, float zd) {
/* 217 */     cubeMob.setSize(halfSize, true);
/* 218 */     cubeMob.snapTo(getX() + xd, getY() + 0.5D, getZ() + zd, this.random.nextFloat() * 360.0F, 0.0F);
/*     */   }
/*     */   
/*     */   protected int getSplitCount() {
/* 222 */     return 2 + this.random.nextInt(3);
/*     */   }
/*     */   
/*     */   public void push(Entity entity) {
/* 227 */     super.push(entity);
/* 228 */     if (entity instanceof net.minecraft.world.entity.animal.golem.IronGolem && isDealsDamage())
/* 229 */       dealDamage((LivingEntity)entity); 
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/* 235 */     if (isDealsDamage())
/* 236 */       dealDamage((LivingEntity)player); 
/*     */   }
/*     */   
/*     */   protected void dealDamage(LivingEntity target) {
/* 241 */     Level level = level();
/* 241 */     if (level instanceof ServerLevel) {
/* 241 */       ServerLevel serverLevel = (ServerLevel)level;
/* 242 */       if (isAlive() && 
/* 243 */         isWithinMeleeAttackRange(target) && hasLineOfSight((Entity)target)) {
/* 244 */         DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 245 */         if (target.hurtServer(serverLevel, damageSource, getAttackDamage())) {
/* 246 */           playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 247 */           EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)target, damageSource);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 256 */     return new Vec3(0.0D, dimensions.height() - 0.015625D * getSize() * scale, 0.0D);
/*     */   }
/*     */   
/*     */   protected boolean isDealsDamage() {
/* 260 */     return (!isTiny() && isEffectiveAi());
/*     */   }
/*     */   
/*     */   protected float getAttackDamage() {
/* 264 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */   
/*     */   protected static class CubeMobMoveControl<T extends AbstractCubeMob> extends MoveControl<T> {
/*     */     private float yRot;
/*     */     
/*     */     private int jumpDelay;
/*     */     
/*     */     private boolean isAggressive;
/*     */     
/*     */     public CubeMobMoveControl(T cubeMob) {
/* 273 */       super((Mob)cubeMob);
/* 274 */       this.yRot = 180.0F * cubeMob.getYRot() / 3.1415927F;
/*     */     }
/*     */     
/*     */     public void setDirection(float yRot, boolean isAggressive) {
/* 278 */       this.yRot = yRot;
/* 279 */       this.isAggressive = isAggressive;
/*     */     }
/*     */     
/*     */     public void setWantedMovement(double speedModifier) {
/* 283 */       this.speedModifier = speedModifier;
/* 284 */       this.operation = MoveControl.Operation.MOVE_TO;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 289 */       ((AbstractCubeMob)this.mob).setYRot(rotlerp(((AbstractCubeMob)this.mob).getYRot(), this.yRot, 90.0F));
/* 290 */       ((AbstractCubeMob)this.mob).yHeadRot = ((AbstractCubeMob)this.mob).getYRot();
/* 291 */       ((AbstractCubeMob)this.mob).yBodyRot = ((AbstractCubeMob)this.mob).getYRot();
/* 293 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/* 294 */         ((AbstractCubeMob)this.mob).setZza(0.0F);
/*     */         return;
/*     */       } 
/* 297 */       this.operation = MoveControl.Operation.WAIT;
/* 299 */       if (((AbstractCubeMob)this.mob).onGround()) {
/* 300 */         ((AbstractCubeMob)this.mob).setSpeed((float)(this.speedModifier * ((AbstractCubeMob)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 301 */         if (this.jumpDelay-- <= 0) {
/* 302 */           this.jumpDelay = ((AbstractCubeMob)this.mob).getJumpDelay();
/* 303 */           if (this.isAggressive)
/* 304 */             this.jumpDelay /= 3; 
/* 306 */           ((AbstractCubeMob)this.mob).getJumpControl().jump();
/* 307 */           if (((AbstractCubeMob)this.mob).doPlayJumpSound())
/* 308 */             ((AbstractCubeMob)this.mob).playSound(((AbstractCubeMob)this.mob).getJumpSound(), ((AbstractCubeMob)this.mob).getSoundVolume(), ((AbstractCubeMob)this.mob).getSoundPitch()); 
/*     */         } else {
/* 311 */           ((AbstractCubeMob)this.mob).xxa = 0.0F;
/* 312 */           ((AbstractCubeMob)this.mob).zza = 0.0F;
/* 313 */           ((AbstractCubeMob)this.mob).setSpeed(0.0F);
/*     */         } 
/*     */       } else {
/* 316 */         ((AbstractCubeMob)this.mob).setSpeed((float)(this.speedModifier * ((AbstractCubeMob)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 323 */     return getType().getDimensions().scale(getSize());
/*     */   }
/*     */   
/*     */   protected static class CubeMobAttackGoal extends Goal {
/*     */     private final AbstractCubeMob cubeMob;
/*     */     
/*     */     private int growTiredTimer;
/*     */     
/*     */     public CubeMobAttackGoal(AbstractCubeMob cubeMob) {
/* 331 */       this.cubeMob = cubeMob;
/* 332 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 337 */       LivingEntity target = this.cubeMob.getTarget();
/* 339 */       if (target == null)
/* 340 */         return false; 
/* 343 */       if (!this.cubeMob.canAttack(target))
/* 344 */         return false; 
/* 347 */       return this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl;
/*     */     }
/*     */     
/*     */     public void start() {
/* 352 */       this.growTiredTimer = reducedTickDelay(300);
/* 353 */       super.start();
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 358 */       LivingEntity target = this.cubeMob.getTarget();
/* 360 */       if (target == null)
/* 361 */         return false; 
/* 364 */       if (!this.cubeMob.canAttack(target))
/* 365 */         return false; 
/* 368 */       if (--this.growTiredTimer <= 0)
/* 369 */         return false; 
/* 372 */       return true;
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 377 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 382 */       LivingEntity target = this.cubeMob.getTarget();
/* 383 */       if (target != null)
/* 384 */         this.cubeMob.lookAt((Entity)target, 10.0F, 10.0F); 
/* 386 */       MoveControl moveControl = this.cubeMob.getMoveControl();
/* 386 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 386 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 387 */         cubeMobMoveControl.setDirection(this.cubeMob.getYRot(), this.cubeMob.isDealsDamage());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CubeMobRandomDirectionGoal extends Goal {
/*     */     private final AbstractCubeMob cubeMob;
/*     */     
/*     */     private float chosenDegrees;
/*     */     
/*     */     private int nextRandomizeTime;
/*     */     
/*     */     public CubeMobRandomDirectionGoal(AbstractCubeMob cubeMob) {
/* 399 */       this.cubeMob = cubeMob;
/* 400 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 405 */       return (this.cubeMob.getTarget() == null && (this.cubeMob.onGround() || this.cubeMob.isInWater() || this.cubeMob.isInLava() || this.cubeMob.hasEffect(MobEffects.LEVITATION)) && this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 410 */       if (--this.nextRandomizeTime <= 0) {
/* 411 */         this.nextRandomizeTime = adjustedTickDelay(40 + this.cubeMob.getRandom().nextInt(60));
/* 412 */         this.chosenDegrees = this.cubeMob.getRandom().nextInt(360);
/*     */       } 
/* 414 */       MoveControl moveControl = this.cubeMob.getMoveControl();
/* 414 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 414 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 415 */         cubeMobMoveControl.setDirection(this.chosenDegrees, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CubeMobFloatGoal extends Goal {
/*     */     private final AbstractCubeMob cubeMob;
/*     */     
/*     */     public CubeMobFloatGoal(AbstractCubeMob mob) {
/* 424 */       this.cubeMob = mob;
/* 425 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/* 426 */       mob.getNavigation().setCanFloat(true);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 431 */       return ((this.cubeMob.isInWater() || this.cubeMob.isInLava()) && this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl);
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 436 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 441 */       if (this.cubeMob.getRandom().nextFloat() < 0.8F)
/* 442 */         this.cubeMob.getJumpControl().jump(); 
/* 444 */       MoveControl moveControl = this.cubeMob.getMoveControl();
/* 444 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 444 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 445 */         cubeMobMoveControl.setWantedMovement(1.2D);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CubeMobKeepOnJumpingGoal extends Goal {
/*     */     private final AbstractCubeMob cubeMob;
/*     */     
/*     */     public CubeMobKeepOnJumpingGoal(AbstractCubeMob mob) {
/* 454 */       this.cubeMob = mob;
/* 455 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 460 */       return !this.cubeMob.isPassenger();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 465 */       MoveControl moveControl = this.cubeMob.getMoveControl();
/* 465 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 465 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 466 */         cubeMobMoveControl.setWantedMovement(1.0D);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void jumpFromGround() {
/* 473 */     Vec3 movement = getDeltaMovement();
/* 474 */     setDeltaMovement(movement.x, getJumpPower(), movement.z);
/* 475 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 480 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 485 */     return 0.4F * getSize();
/*     */   }
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 490 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean doPlayJumpSound() {
/* 494 */     return (getSize() > 0);
/*     */   }
/*     */   
/*     */   public float getSoundPitch() {
/* 498 */     float pitchAdjuster = isTiny() ? 1.4F : 0.8F;
/* 499 */     return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * pitchAdjuster;
/*     */   }
/*     */   
/*     */   public AbstractCubeMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 514 */     return null;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 519 */     SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 520 */     setSpawnSize(level, difficulty);
/* 521 */     return data;
/*     */   }
/*     */   
/*     */   protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
/* 525 */     RandomSource random = level.getRandom();
/* 526 */     int sizeScale = random.nextInt(3);
/* 527 */     if (sizeScale < 2 && random.nextFloat() < 0.5F * difficulty.getSpecialMultiplier())
/* 528 */       sizeScale++; 
/* 530 */     int size = 1 << sizeScale;
/* 531 */     setSize(size, true);
/*     */   }
/*     */   
/*     */   protected abstract void addBehaviourGoals();
/*     */   
/*     */   protected abstract void addTargetingGoals();
/*     */   
/*     */   protected abstract ParticleOptions getParticleType();
/*     */   
/*     */   protected abstract SoundEvent getJumpSound();
/*     */   
/*     */   protected abstract SoundEvent getHurtSound(DamageSource paramDamageSource);
/*     */   
/*     */   protected abstract SoundEvent getDeathSound();
/*     */   
/*     */   protected abstract SoundEvent getSquishSound();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */