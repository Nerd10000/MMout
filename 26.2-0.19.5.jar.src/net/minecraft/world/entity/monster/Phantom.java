/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
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
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Phantom extends Mob implements Enemy {
/*     */   public static final float FLAP_DEGREES_PER_TICK = 7.448451F;
/*     */   
/*  49 */   public static final int TICKS_PER_FLAP = Mth.ceil(24.166098F);
/*     */   
/*  51 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.INT);
/*     */   
/*  53 */   private Vec3 moveTargetPoint = Vec3.ZERO;
/*     */   
/*     */   private BlockPos anchorPoint;
/*     */   
/*     */   private enum AttackPhase {
/*  57 */     CIRCLE, SWOOP;
/*     */   }
/*     */   
/*  61 */   private AttackPhase attackPhase = AttackPhase.CIRCLE;
/*     */   
/*     */   public Phantom(EntityType<? extends Phantom> type, Level level) {
/*  64 */     super(type, level);
/*  65 */     this.xpReward = 5;
/*  67 */     this.moveControl = new PhantomMoveControl<>(this);
/*  68 */     this.lookControl = new PhantomLookControl(this);
/*     */   }
/*     */   
/*     */   public boolean isFlapping() {
/*  75 */     return ((getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0);
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/*  80 */     return new PhantomBodyRotationControl(this);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  85 */     this.goalSelector.addGoal(1, new PhantomAttackStrategyGoal());
/*  86 */     this.goalSelector.addGoal(2, new PhantomSweepAttackGoal());
/*  87 */     this.goalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
/*  89 */     this.targetSelector.addGoal(1, new PhantomAttackPlayerTargetGoal());
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  94 */     super.defineSynchedData(entityData);
/*  96 */     entityData.define(ID_SIZE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public void setPhantomSize(int size) {
/* 100 */     this.entityData.set(ID_SIZE, Integer.valueOf(Mth.clamp(size, 0, 64)));
/*     */   }
/*     */   
/*     */   private void updatePhantomSizeInfo() {
/* 104 */     refreshDimensions();
/* 105 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((6 + getPhantomSize()));
/*     */   }
/*     */   
/*     */   public int getPhantomSize() {
/* 109 */     return ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 114 */     if (ID_SIZE.equals(accessor))
/* 115 */       updatePhantomSizeInfo(); 
/* 118 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public int getUniqueFlapTickOffset() {
/* 122 */     return getId() * 3;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 127 */     super.tick();
/* 129 */     if (level().isClientSide()) {
/* 130 */       float anim = Mth.cos(((getUniqueFlapTickOffset() + this.tickCount) * 7.448451F * 0.017453292F + 3.1415927F));
/* 131 */       float nextAnim = Mth.cos(((getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451F * 0.017453292F + 3.1415927F));
/* 132 */       if (anim > 0.0F && nextAnim <= 0.0F)
/* 133 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.PHANTOM_FLAP, getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false); 
/* 136 */       float width = getBbWidth() * 1.48F;
/* 137 */       float c = Mth.cos((getYRot() * 0.017453292F)) * width;
/* 138 */       float s = Mth.sin((getYRot() * 0.017453292F)) * width;
/* 140 */       float h = (0.3F + anim * 0.45F) * getBbHeight() * 2.5F;
/* 141 */       level().addParticle((ParticleOptions)ParticleTypes.MYCELIUM, getX() + c, getY() + h, getZ() + s, 0.0D, 0.0D, 0.0D);
/* 142 */       level().addParticle((ParticleOptions)ParticleTypes.MYCELIUM, getX() - c, getY() + h, getZ() - s, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */   
/*     */   public boolean onClimbable() {
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 159 */     travelFlying(input, 0.2F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 164 */     this.anchorPoint = blockPosition().above(5);
/* 165 */     setPhantomSize(0);
/* 166 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 171 */     super.readAdditionalSaveData(input);
/* 173 */     this.anchorPoint = input.read("anchor_pos", BlockPos.CODEC).orElse(null);
/* 174 */     setPhantomSize(input.getIntOr("size", 0));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 179 */     super.addAdditionalSaveData(output);
/* 181 */     output.storeNullable("anchor_pos", BlockPos.CODEC, this.anchorPoint);
/* 182 */     output.putInt("size", getPhantomSize());
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 192 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 197 */     return SoundEvents.PHANTOM_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 202 */     return SoundEvents.PHANTOM_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 207 */     return SoundEvents.PHANTOM_DEATH;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 212 */     int size = getPhantomSize();
/* 214 */     EntityDimensions originalDimensions = super.getDefaultDimensions(pose);
/* 215 */     return originalDimensions.scale(1.0F + 0.15F * size);
/*     */   }
/*     */   
/*     */   private boolean canAttack(ServerLevel level, LivingEntity target, TargetingConditions targetConditions) {
/* 219 */     return targetConditions.test(level, (LivingEntity)this, target);
/*     */   }
/*     */   
/*     */   private class PhantomMoveControl<T extends Mob> extends MoveControl<T> {
/* 223 */     private float speed = 0.1F;
/*     */     
/*     */     public PhantomMoveControl(T mob) {
/* 226 */       super((Mob)mob);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 231 */       if (Phantom.this.horizontalCollision) {
/* 233 */         Phantom.this.setYRot(Phantom.this.getYRot() + 180.0F);
/* 234 */         this.speed = 0.1F;
/*     */       } 
/* 238 */       double tdx = Phantom.this.moveTargetPoint.x - Phantom.this.getX();
/* 239 */       double tdy = Phantom.this.moveTargetPoint.y - Phantom.this.getY();
/* 240 */       double tdz = Phantom.this.moveTargetPoint.z - Phantom.this.getZ();
/* 241 */       double sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 244 */       if (Math.abs(sd) > 9.999999747378752E-6D) {
/* 245 */         double yRelativeScale = 1.0D - Math.abs(tdy * 0.699999988079071D) / sd;
/* 246 */         tdx *= yRelativeScale;
/* 247 */         tdz *= yRelativeScale;
/* 248 */         sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 249 */         double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
/* 252 */         float prev = Phantom.this.getYRot();
/* 253 */         float angle = (float)Mth.atan2(tdz, tdx);
/* 254 */         float a = Mth.wrapDegrees(Phantom.this.getYRot() + 90.0F);
/* 255 */         float b = Mth.wrapDegrees(angle * 57.295776F);
/* 256 */         Phantom.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
/* 257 */         Phantom.this.yBodyRot = Phantom.this.getYRot();
/* 259 */         if (Mth.degreesDifferenceAbs(prev, Phantom.this.getYRot()) < 3.0F) {
/* 260 */           this.speed = Mth.approach(this.speed, 1.8F, 0.005F * 1.8F / this.speed);
/*     */         } else {
/* 262 */           this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
/*     */         } 
/* 265 */         float xRotD = (float)-(Mth.atan2(-tdy, sd) * 57.2957763671875D);
/* 266 */         Phantom.this.setXRot(xRotD);
/* 268 */         float moveAngle = Phantom.this.getYRot() + 90.0F;
/* 269 */         double txd = (this.speed * Mth.cos((moveAngle * 0.017453292F))) * Math.abs(tdx / sd2);
/* 270 */         double tzd = (this.speed * Mth.sin((moveAngle * 0.017453292F))) * Math.abs(tdz / sd2);
/* 271 */         double tyd = (this.speed * Mth.sin((xRotD * 0.017453292F))) * Math.abs(tdy / sd2);
/* 273 */         Vec3 movement = Phantom.this.getDeltaMovement();
/* 274 */         Phantom.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd)).subtract(movement).scale(0.2D)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomBodyRotationControl extends BodyRotationControl {
/*     */     public PhantomBodyRotationControl(Mob mob) {
/* 281 */       super(mob);
/*     */     }
/*     */     
/*     */     public void clientTick() {
/* 286 */       Phantom.this.yHeadRot = Phantom.this.yBodyRot;
/* 287 */       Phantom.this.yBodyRot = Phantom.this.getYRot();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PhantomLookControl extends LookControl {
/*     */     public PhantomLookControl(Mob mob) {
/* 293 */       super(mob);
/*     */     }
/*     */     
/*     */     public void tick() {}
/*     */   }
/*     */   
/*     */   private abstract class PhantomMoveTargetGoal extends Goal {
/*     */     public PhantomMoveTargetGoal() {
/* 303 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     protected boolean touchingTarget() {
/* 307 */       return (Phantom.this.moveTargetPoint.distanceToSqr(Phantom.this.getX(), Phantom.this.getY(), Phantom.this.getZ()) < 4.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal {
/*     */     private float angle;
/*     */     
/*     */     private float distance;
/*     */     
/*     */     private float height;
/*     */     
/*     */     private float clockwise;
/*     */     
/*     */     public boolean canUse() {
/* 319 */       return (Phantom.this.getTarget() == null || Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE);
/*     */     }
/*     */     
/*     */     public void start() {
/* 324 */       this.distance = 5.0F + Phantom.this.random.nextFloat() * 10.0F;
/* 325 */       this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F;
/* 326 */       this.clockwise = Phantom.this.random.nextBoolean() ? 1.0F : -1.0F;
/* 327 */       selectNext();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 332 */       if (Phantom.this.random.nextInt(adjustedTickDelay(350)) == 0)
/* 333 */         this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F; 
/* 335 */       if (Phantom.this.random.nextInt(adjustedTickDelay(250)) == 0) {
/* 336 */         this.distance++;
/* 337 */         if (this.distance > 15.0F) {
/* 338 */           this.distance = 5.0F;
/* 339 */           this.clockwise = -this.clockwise;
/*     */         } 
/*     */       } 
/* 342 */       if (Phantom.this.random.nextInt(adjustedTickDelay(450)) == 0) {
/* 343 */         this.angle = Phantom.this.random.nextFloat() * 2.0F * 3.1415927F;
/* 344 */         selectNext();
/*     */       } 
/* 346 */       if (touchingTarget())
/* 347 */         selectNext(); 
/* 350 */       if (Phantom.this.moveTargetPoint.y < Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().below(1))) {
/* 351 */         this.height = Math.max(1.0F, this.height);
/* 352 */         selectNext();
/*     */       } 
/* 355 */       if (Phantom.this.moveTargetPoint.y > Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().above(1))) {
/* 356 */         this.height = Math.min(-1.0F, this.height);
/* 357 */         selectNext();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void selectNext() {
/* 362 */       if (Phantom.this.anchorPoint == null)
/* 363 */         Phantom.this.anchorPoint = Phantom.this.blockPosition(); 
/* 365 */       this.angle += this.clockwise * 15.0F * 0.017453292F;
/* 366 */       Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf((Vec3i)Phantom.this.anchorPoint).add((this.distance * Mth.cos(this.angle)), (-4.0F + this.height), (this.distance * Mth.sin(this.angle)));
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomSweepAttackGoal extends PhantomMoveTargetGoal {
/*     */     private static final int CAT_SEARCH_TICK_DELAY = 20;
/*     */     
/*     */     private boolean isScaredOfCat;
/*     */     
/*     */     private int catSearchTick;
/*     */     
/*     */     public boolean canUse() {
/* 378 */       return (Phantom.this.getTarget() != null && Phantom.this.attackPhase == Phantom.AttackPhase.SWOOP);
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 383 */       LivingEntity target = Phantom.this.getTarget();
/* 384 */       if (target == null)
/* 385 */         return false; 
/* 387 */       if (!target.isAlive())
/* 388 */         return false; 
/* 390 */       if (target instanceof Player) {
/* 390 */         Player player = (Player)target;
/* 390 */         if (target.isSpectator() || player.isCreative())
/* 391 */           return false; 
/*     */       } 
/* 394 */       if (!canUse())
/* 395 */         return false; 
/* 398 */       if (Phantom.this.tickCount > this.catSearchTick) {
/* 399 */         this.catSearchTick = Phantom.this.tickCount + 20;
/* 400 */         List<Cat> cats = Phantom.this.level().getEntitiesOfClass(Cat.class, Phantom.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);
/* 401 */         for (Cat cat : cats)
/* 402 */           cat.hiss(); 
/* 404 */         this.isScaredOfCat = !cats.isEmpty();
/*     */       } 
/* 407 */       return !this.isScaredOfCat;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 412 */       Phantom.this.setTarget(null);
/* 413 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 418 */       LivingEntity target = Phantom.this.getTarget();
/* 419 */       if (target == null)
/*     */         return; 
/* 422 */       Phantom.this.moveTargetPoint = new Vec3(target.getX(), target.getY(0.5D), target.getZ());
/* 424 */       if (Phantom.this.getBoundingBox().inflate(0.20000000298023224D).intersects(target.getBoundingBox())) {
/* 425 */         Phantom.this.doHurtTarget(getServerLevel(Phantom.this.level()), (Entity)target);
/* 426 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 427 */         if (!Phantom.this.isSilent())
/* 428 */           Phantom.this.level().levelEvent(1039, Phantom.this.blockPosition(), 0); 
/* 430 */       } else if (Phantom.this.horizontalCollision || Phantom.this.hurtTime > 0) {
/* 431 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomAttackStrategyGoal extends Goal {
/*     */     private int nextSweepTick;
/*     */     
/*     */     public boolean canUse() {
/* 441 */       LivingEntity target = Phantom.this.getTarget();
/* 442 */       if (target != null)
/* 443 */         return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT); 
/* 445 */       return false;
/*     */     }
/*     */     
/*     */     public void start() {
/* 450 */       this.nextSweepTick = adjustedTickDelay(10);
/* 451 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 452 */       setAnchorAboveTarget();
/*     */     }
/*     */     
/*     */     public void stop() {
/* 457 */       if (Phantom.this.anchorPoint != null)
/* 458 */         Phantom.this.anchorPoint = Phantom.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Phantom.this.anchorPoint).above(10 + Phantom.this.random.nextInt(20)); 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 464 */       if (Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE) {
/* 465 */         this.nextSweepTick--;
/* 466 */         if (this.nextSweepTick <= 0) {
/* 467 */           Phantom.this.attackPhase = Phantom.AttackPhase.SWOOP;
/* 468 */           setAnchorAboveTarget();
/* 469 */           this.nextSweepTick = adjustedTickDelay((8 + Phantom.this.random.nextInt(4)) * 20);
/* 471 */           Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.this.random.nextFloat() * 0.1F);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void setAnchorAboveTarget() {
/* 477 */       if (Phantom.this.anchorPoint == null)
/*     */         return; 
/* 480 */       Phantom.this.anchorPoint = Phantom.this.getTarget().blockPosition().above(20 + Phantom.this.random.nextInt(20));
/* 481 */       if (Phantom.this.anchorPoint.getY() < Phantom.this.level().getSeaLevel())
/* 482 */         Phantom.this.anchorPoint = new BlockPos(Phantom.this.anchorPoint.getX(), Phantom.this.level().getSeaLevel() + 1, Phantom.this.anchorPoint.getZ()); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PhantomAttackPlayerTargetGoal extends Goal {
/*     */     private final TargetingConditions attackTargeting;
/*     */     
/*     */     private int nextScanTick;
/*     */     
/*     */     private PhantomAttackPlayerTargetGoal() {
/* 489 */       this.attackTargeting = TargetingConditions.forCombat().range(64.0D);
/* 491 */       this.nextScanTick = reducedTickDelay(20);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 495 */       if (this.nextScanTick > 0) {
/* 496 */         this.nextScanTick--;
/* 497 */         return false;
/*     */       } 
/* 499 */       this.nextScanTick = reducedTickDelay(60);
/* 501 */       ServerLevel level = getServerLevel(Phantom.this.level());
/* 502 */       List<Player> players = level.getNearbyPlayers(this.attackTargeting, (LivingEntity)Phantom.this, Phantom.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
/* 503 */       if (!players.isEmpty()) {
/* 505 */         players.sort(Comparator.<Player, Comparable>comparing(Entity::getY).reversed());
/* 506 */         for (Player player : players) {
/* 507 */           if (Phantom.this.canAttack(level, (LivingEntity)player, TargetingConditions.DEFAULT)) {
/* 508 */             Phantom.this.setTarget((LivingEntity)player);
/* 509 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 513 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 518 */       LivingEntity target = Phantom.this.getTarget();
/* 519 */       if (target != null)
/* 520 */         return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT); 
/* 523 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */