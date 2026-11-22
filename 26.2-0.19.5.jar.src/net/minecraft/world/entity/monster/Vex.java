/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.OwnableEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.Targeting;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Vex extends Monster implements TraceableEntity, OwnableEntity {
/*     */   public static final float FLAP_DEGREES_PER_TICK = 45.836624F;
/*     */   
/*  51 */   public static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
/*     */   
/*  53 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Vex.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int FLAG_IS_CHARGING = 1;
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   private BlockPos boundOrigin;
/*     */   
/*     */   private boolean hasLimitedLife;
/*     */   
/*     */   private int limitedLifeTicks;
/*     */   
/*     */   public Vex(EntityType<? extends Vex> type, Level level) {
/*  63 */     super((EntityType)type, level);
/*  65 */     this.moveControl = new VexMoveControl<>(this);
/*  67 */     this.xpReward = 3;
/*     */   }
/*     */   
/*     */   public boolean isFlapping() {
/*  74 */     return (this.tickCount % TICKS_PER_FLAP == 0);
/*     */   }
/*     */   
/*     */   protected boolean isAffectedByBlocks() {
/*  80 */     return !isRemoved();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  85 */     this.noPhysics = true;
/*  86 */     super.tick();
/*  87 */     this.noPhysics = false;
/*  89 */     setNoGravity(true);
/*  91 */     if (this.hasLimitedLife && 
/*  92 */       --this.limitedLifeTicks <= 0) {
/*  93 */       this.limitedLifeTicks = 20;
/*  94 */       hurt(damageSources().starve(), 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 101 */     super.registerGoals();
/* 103 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 104 */     this.goalSelector.addGoal(4, new VexChargeAttackGoal());
/* 105 */     this.goalSelector.addGoal(8, new VexRandomMoveGoal());
/* 106 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/* 107 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/* 109 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/* 110 */     this.targetSelector.addGoal(2, (Goal)new VexCopyOwnerTargetGoal(this));
/* 111 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 115 */     return Monster.createMonsterAttributes()
/* 116 */       .add(Attributes.MAX_HEALTH, 14.0D)
/* 117 */       .add(Attributes.ATTACK_DAMAGE, 4.0D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 122 */     super.defineSynchedData(entityData);
/* 124 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 129 */     super.readAdditionalSaveData(input);
/* 131 */     this.boundOrigin = input.read("bound_pos", BlockPos.CODEC).orElse(null);
/* 133 */     input.getInt("life_ticks").ifPresentOrElse(this::setLimitedLife, () -> this.hasLimitedLife = false);
/* 138 */     this.owner = EntityReference.read(input, "owner");
/*     */   }
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/* 143 */     super.restoreFrom(oldEntity);
/* 144 */     if (oldEntity instanceof Vex) {
/* 144 */       Vex vex = (Vex)oldEntity;
/* 145 */       this.owner = vex.owner;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 151 */     super.addAdditionalSaveData(output);
/* 153 */     output.storeNullable("bound_pos", BlockPos.CODEC, this.boundOrigin);
/* 154 */     if (this.hasLimitedLife)
/* 155 */       output.putInt("life_ticks", this.limitedLifeTicks); 
/* 157 */     EntityReference.store(this.owner, output, "owner");
/*     */   }
/*     */   
/*     */   public LivingEntity getOwner() {
/* 162 */     return super.getOwner();
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getOwnerReference() {
/* 167 */     return this.owner;
/*     */   }
/*     */   
/*     */   public BlockPos getBoundOrigin() {
/* 171 */     return this.boundOrigin;
/*     */   }
/*     */   
/*     */   public void setBoundOrigin(BlockPos boundOrigin) {
/* 175 */     this.boundOrigin = boundOrigin;
/*     */   }
/*     */   
/*     */   private boolean getVexFlag(int flag) {
/* 179 */     int flags = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 180 */     return ((flags & flag) != 0);
/*     */   }
/*     */   
/*     */   private void setVexFlag(int flag, boolean value) {
/* 184 */     int flags = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 185 */     if (value) {
/* 186 */       flags |= flag;
/*     */     } else {
/* 188 */       flags &= flag ^ 0xFFFFFFFF;
/*     */     } 
/* 190 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(flags & 0xFF)));
/*     */   }
/*     */   
/*     */   public boolean isCharging() {
/* 194 */     return getVexFlag(1);
/*     */   }
/*     */   
/*     */   public void setIsCharging(boolean value) {
/* 198 */     setVexFlag(1, value);
/*     */   }
/*     */   
/*     */   public void setOwner(Mob owner) {
/* 202 */     this.owner = EntityReference.of((UniquelyIdentifyable)owner);
/*     */   }
/*     */   
/*     */   public void setLimitedLife(int lifeTicks) {
/* 206 */     this.hasLimitedLife = true;
/* 207 */     this.limitedLifeTicks = lifeTicks;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 212 */     return SoundEvents.VEX_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 217 */     return SoundEvents.VEX_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 222 */     return SoundEvents.VEX_HURT;
/*     */   }
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 227 */     return 1.0F;
/*     */   }
/*     */   
/*     */   private class VexMoveControl<T extends Mob> extends MoveControl<T> {
/*     */     public VexMoveControl(T vex) {
/* 232 */       super((Mob)vex);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 237 */       if (this.operation != MoveControl.Operation.MOVE_TO)
/*     */         return; 
/* 245 */       Vec3 delta = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
/* 248 */       double deltaLength = delta.length();
/* 249 */       if (deltaLength < Vex.this.getBoundingBox().getSize()) {
/* 250 */         this.operation = MoveControl.Operation.WAIT;
/* 251 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5D));
/*     */       } else {
/* 253 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(delta.scale(this.speedModifier * 0.05D / deltaLength)));
/* 255 */         if (Vex.this.getTarget() == null) {
/* 256 */           Vec3 movement = Vex.this.getDeltaMovement();
/* 257 */           Vex.this.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 258 */           Vex.this.yBodyRot = Vex.this.getYRot();
/*     */         } else {
/* 261 */           double tx = Vex.this.getTarget().getX() - Vex.this.getX();
/* 262 */           double tz = Vex.this.getTarget().getZ() - Vex.this.getZ();
/* 263 */           Vex.this.setYRot(-((float)Mth.atan2(tx, tz)) * 57.295776F);
/* 264 */           Vex.this.yBodyRot = Vex.this.getYRot();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class VexChargeAttackGoal extends Goal {
/*     */     public VexChargeAttackGoal() {
/* 272 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 277 */       LivingEntity target = Vex.this.getTarget();
/* 278 */       if (target != null && target.isAlive() && !Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(reducedTickDelay(7)) == 0)
/* 279 */         return (Vex.this.distanceToSqr((Entity)target) > 4.0D); 
/* 281 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 286 */       return (Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive());
/*     */     }
/*     */     
/*     */     public void start() {
/* 291 */       LivingEntity attackTarget = Vex.this.getTarget();
/* 292 */       if (attackTarget != null) {
/* 293 */         Vec3 eyePosition = attackTarget.getEyePosition();
/* 294 */         Vex.this.moveControl.setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */       } 
/* 296 */       Vex.this.setIsCharging(true);
/* 297 */       Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 302 */       Vex.this.setIsCharging(false);
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 307 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 312 */       LivingEntity attackTarget = Vex.this.getTarget();
/* 313 */       if (attackTarget == null)
/*     */         return; 
/* 316 */       if (Vex.this.getBoundingBox().intersects(attackTarget.getBoundingBox())) {
/* 317 */         Vex.this.doHurtTarget(getServerLevel(Vex.this.level()), (Entity)attackTarget);
/* 318 */         Vex.this.setIsCharging(false);
/*     */       } else {
/* 320 */         double distance = Vex.this.distanceToSqr((Entity)attackTarget);
/* 321 */         if (distance < 9.0D) {
/* 322 */           Vec3 eyePosition = attackTarget.getEyePosition();
/* 323 */           Vex.this.moveControl.setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class VexRandomMoveGoal extends Goal {
/*     */     public VexRandomMoveGoal() {
/* 331 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 336 */       return (!Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(reducedTickDelay(7)) == 0);
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 341 */       return false;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 346 */       BlockPos boundOrigin = Vex.this.getBoundOrigin();
/* 347 */       if (boundOrigin == null)
/* 349 */         boundOrigin = Vex.this.blockPosition(); 
/* 352 */       for (int attempts = 0; attempts < 3; attempts++) {
/* 353 */         BlockPos testPos = boundOrigin.offset(Vex.this.random.nextInt(15) - 7, Vex.this.random.nextInt(11) - 5, Vex.this.random.nextInt(15) - 7);
/* 354 */         if (Vex.this.level().isEmptyBlock(testPos)) {
/* 355 */           Vex.this.moveControl.setWantedPosition(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 0.25D);
/* 356 */           if (Vex.this.getTarget() == null)
/* 357 */             Vex.this.getLookControl().setLookAt(testPos.getX() + 0.5D, testPos.getY() + 0.5D, testPos.getZ() + 0.5D, 180.0F, 20.0F); 
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 367 */     RandomSource random = level.getRandom();
/* 368 */     populateDefaultEquipmentSlots(random, difficulty);
/* 369 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 371 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 376 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
/* 377 */     setDropChance(EquipmentSlot.MAINHAND, 0.0F);
/*     */   }
/*     */   
/*     */   private class VexCopyOwnerTargetGoal extends TargetGoal {
/* 381 */     private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */     
/*     */     public VexCopyOwnerTargetGoal(PathfinderMob mob) {
/* 384 */       super((Mob)mob, false);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 389 */       LivingEntity livingEntity = Vex.this.getOwner();
/* 389 */       if (livingEntity instanceof Targeting) {
/* 389 */         Targeting owner = (Targeting)livingEntity;
/* 389 */         if (owner.getTarget() != null && canAttack(owner.getTarget(), this.copyOwnerTargeting));
/*     */       } 
/* 389 */       return false;
/*     */     }
/*     */     
/*     */     public void start() {
/* 394 */       LivingEntity livingEntity = Vex.this.getOwner();
/* 394 */       Targeting owner = (Targeting)livingEntity;
/* 394 */       Vex.this.setTarget((livingEntity instanceof Targeting) ? owner.getTarget() : null);
/* 395 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Vex.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */