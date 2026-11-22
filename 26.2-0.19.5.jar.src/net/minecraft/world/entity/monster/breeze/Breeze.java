/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.debug.DebugBreezeInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Breeze extends Monster {
/*     */   private static final int SLIDE_PARTICLES_AMOUNT = 20;
/*     */   
/*     */   private static final int IDLE_PARTICLES_AMOUNT = 1;
/*     */   
/*     */   private static final int JUMP_TRAIL_PARTICLES_AMOUNT = 3;
/*     */   
/*     */   private static final int JUMP_TRAIL_DURATION_TICKS = 5;
/*     */   
/*     */   private static final int JUMP_CIRCLE_DISTANCE_Y = 10;
/*     */   
/*     */   private static final float FALL_DISTANCE_SOUND_TRIGGER_THRESHOLD = 3.0F;
/*     */   
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MIN = 1;
/*     */   
/*     */   private static final int WHIRL_SOUND_FREQUENCY_MAX = 80;
/*     */   
/*  54 */   private static final Brain.Provider<Breeze> BRAIN_PROVIDER = Brain.provider(
/*  55 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorType.BREEZE_ATTACK_ENTITY_SENSOR), BreezeAi::getActivities);
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  65 */     return Mob.createMobAttributes()
/*  66 */       .add(Attributes.MOVEMENT_SPEED, 0.6299999952316284D)
/*  67 */       .add(Attributes.MAX_HEALTH, 30.0D)
/*  68 */       .add(Attributes.FOLLOW_RANGE, 24.0D)
/*  69 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*  73 */   public final AnimationState idle = new AnimationState();
/*     */   
/*  74 */   public final AnimationState slide = new AnimationState();
/*     */   
/*  75 */   public final AnimationState slideBack = new AnimationState();
/*     */   
/*  76 */   public final AnimationState longJump = new AnimationState();
/*     */   
/*  77 */   public final AnimationState shoot = new AnimationState();
/*     */   
/*  78 */   public final AnimationState inhale = new AnimationState();
/*     */   
/*  80 */   private int jumpTrailStartedTick = 0;
/*     */   
/*  81 */   private int soundTick = 0;
/*     */   
/*     */   private static final ProjectileDeflection PROJECTILE_DEFLECTION;
/*     */   
/*     */   static {
/*  83 */     PROJECTILE_DEFLECTION = ((projectile, entity, random) -> {
/*     */         entity.level().playSound(null, entity, SoundEvents.BREEZE_DEFLECT, entity.getSoundSource(), 1.0F, 1.0F);
/*     */         ProjectileDeflection.REVERSE.deflect(projectile, entity, random);
/*     */       });
/*     */   }
/*     */   
/*     */   public Breeze(EntityType<? extends Monster> type, Level level) {
/*  89 */     super(type, level);
/*  90 */     setPathfindingMalus(PathType.ON_TOP_OF_TRAPDOOR, -1.0F);
/*  91 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/*  92 */     this.xpReward = 10;
/*     */   }
/*     */   
/*     */   public Brain<Breeze> getBrain() {
/*  98 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected Brain<Breeze> makeBrain(Brain.Packed input) {
/* 103 */     Brain<Breeze> brain = BRAIN_PROVIDER.makeBrain((LivingEntity)this, input);
/* 104 */     brain.setDefaultActivity(Activity.FIGHT);
/* 105 */     brain.useDefaultActivity();
/* 106 */     return brain;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 111 */     if (level().isClientSide() && DATA_POSE.equals(accessor)) {
/* 113 */       resetAnimations();
/* 115 */       Pose pose = getPose();
/* 116 */       switch (pose) {
/*     */         case SHOOTING:
/* 117 */           this.shoot.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case INHALING:
/* 118 */           this.inhale.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case SLIDING:
/* 119 */           this.slide.startIfStopped(this.tickCount);
/*     */           break;
/*     */       } 
/*     */     } 
/* 123 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   private void resetAnimations() {
/* 127 */     this.shoot.stop();
/* 128 */     this.idle.stop();
/* 129 */     this.inhale.stop();
/* 130 */     this.longJump.stop();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 135 */     Pose pose = getPose();
/* 136 */     switch (pose) {
/*     */       case SLIDING:
/* 137 */         emitGroundParticles(20);
/*     */         break;
/*     */       case SHOOTING:
/*     */       case INHALING:
/*     */       case STANDING:
/* 139 */         resetJumpTrail().emitGroundParticles(1 + getRandom().nextInt(1));
/*     */         break;
/*     */       case LONG_JUMPING:
/* 141 */         this.longJump.startIfStopped(this.tickCount);
/* 142 */         emitJumpTrailParticles();
/*     */         break;
/*     */     } 
/* 146 */     this.idle.startIfStopped(this.tickCount);
/* 148 */     if (pose != Pose.SLIDING && this.slide.isStarted()) {
/* 149 */       this.slideBack.start(this.tickCount);
/* 150 */       this.slide.stop();
/*     */     } 
/* 153 */     this.soundTick = (this.soundTick == 0) ? this.random.nextIntBetweenInclusive(1, 80) : (this.soundTick - 1);
/* 154 */     if (this.soundTick == 0)
/* 155 */       playWhirlSound(); 
/* 158 */     super.tick();
/*     */   }
/*     */   
/*     */   public Breeze resetJumpTrail() {
/* 162 */     this.jumpTrailStartedTick = 0;
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public void emitJumpTrailParticles() {
/* 167 */     if (++this.jumpTrailStartedTick > 5)
/*     */       return; 
/* 171 */     BlockState ground = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/* 172 */     Vec3 movement = getDeltaMovement();
/* 173 */     Vec3 centered = position().add(movement).add(0.0D, 0.10000000149011612D, 0.0D);
/* 175 */     for (int i = 0; i < 3; i++)
/* 176 */       level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, ground), centered.x, centered.y, centered.z, 0.0D, 0.0D, 0.0D); 
/*     */   }
/*     */   
/*     */   public void emitGroundParticles(int amount) {
/* 181 */     if (isPassenger())
/*     */       return; 
/* 185 */     Vec3 boundingBoxCenter = getBoundingBox().getCenter();
/* 186 */     Vec3 position = new Vec3(boundingBoxCenter.x, (position()).y, boundingBoxCenter.z);
/* 188 */     BlockState ground = !getInBlockState().isAir() ? getInBlockState() : getBlockStateOn();
/* 190 */     if (ground.getRenderShape() == RenderShape.INVISIBLE)
/*     */       return; 
/* 194 */     for (int i = 0; i < amount; i++)
/* 195 */       level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, ground), position.x, position.y, position.z, 0.0D, 0.0D, 0.0D); 
/*     */   }
/*     */   
/*     */   public void playAmbientSound() {
/* 202 */     if (getTarget() != null && onGround())
/*     */       return; 
/* 207 */     level().playLocalSound((Entity)this, getAmbientSound(), getSoundSource(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void playWhirlSound() {
/* 211 */     float pitch = 0.7F + 0.4F * this.random.nextFloat();
/* 212 */     float volume = 0.8F + 0.2F * this.random.nextFloat();
/* 214 */     level().playLocalSound((Entity)this, SoundEvents.BREEZE_WHIRL, getSoundSource(), volume, pitch);
/*     */   }
/*     */   
/*     */   public ProjectileDeflection deflection(Projectile projectile) {
/* 219 */     if (projectile.is(EntityTypes.BREEZE_WIND_CHARGE) || projectile.is(EntityTypes.WIND_CHARGE))
/* 220 */       return ProjectileDeflection.NONE; 
/* 223 */     return is(EntityTypeTags.DEFLECTS_PROJECTILES) ? PROJECTILE_DEFLECTION : ProjectileDeflection.NONE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 228 */     return SoundEvents.BREEZE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 233 */     return SoundEvents.BREEZE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 238 */     return onGround() ? SoundEvents.BREEZE_IDLE_GROUND : SoundEvents.BREEZE_IDLE_AIR;
/*     */   }
/*     */   
/*     */   public Optional<LivingEntity> getHurtBy() {
/* 242 */     return getBrain().getMemory(MemoryModuleType.HURT_BY)
/* 243 */       .map(DamageSource::getEntity)
/* 244 */       .filter(entity -> entity instanceof LivingEntity)
/* 245 */       .map(entity -> (LivingEntity)entity);
/*     */   }
/*     */   
/*     */   public boolean withinInnerCircleRange(Vec3 target) {
/* 249 */     Vec3 ourPosition = Vec3.atCenterOf((Vec3i)blockPosition());
/* 250 */     return target.closerThan(ourPosition, 4.0D, 10.0D);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 255 */     ProfilerFiller profiler = Profiler.get();
/* 256 */     profiler.push("breezeBrain");
/* 257 */     getBrain().tick(level, (LivingEntity)this);
/* 259 */     profiler.popPush("breezeActivityUpdate");
/* 260 */     BreezeAi.updateActivity(this);
/* 261 */     profiler.pop();
/* 263 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 268 */     return ((target.is(EntityTypes.PLAYER) || target.is(EntityTypes.IRON_GOLEM)) && super.canAttack(target));
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 273 */     return 30;
/*     */   }
/*     */   
/*     */   public int getHeadRotSpeed() {
/* 278 */     return 25;
/*     */   }
/*     */   
/*     */   public double getFiringYPosition() {
/* 283 */     return getY() + (getBbHeight() / 2.0F) + 0.30000001192092896D;
/*     */   }
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 288 */     return (source.getEntity() instanceof Breeze || super.isInvulnerableTo(level, source));
/*     */   }
/*     */   
/*     */   public double getFluidJumpThreshold() {
/* 294 */     return getEyeHeight();
/*     */   }
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 300 */     if (fallDistance > 3.0D)
/* 301 */       playSound(SoundEvents.BREEZE_LAND, 1.0F, 1.0F); 
/* 303 */     return super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 308 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 313 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 318 */     super.registerDebugValues(level, registration);
/* 319 */     registration.register(DebugSubscriptions.BREEZES, () -> new DebugBreezeInfo(getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(Entity::getId), getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_TARGET)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\breeze\Breeze.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */