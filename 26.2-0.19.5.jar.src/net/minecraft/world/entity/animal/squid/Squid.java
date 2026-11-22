/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.animal.AgeableWaterCreature;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Squid extends AgeableWaterCreature {
/*     */   public float xBodyRot;
/*     */   
/*     */   public float xBodyRotO;
/*     */   
/*     */   public float zBodyRot;
/*     */   
/*     */   public float zBodyRotO;
/*     */   
/*     */   public float tentacleMovement;
/*     */   
/*     */   public float oldTentacleMovement;
/*     */   
/*     */   public float tentacleAngle;
/*     */   
/*     */   public float oldTentacleAngle;
/*     */   
/*     */   private float speed;
/*     */   
/*     */   private float tentacleSpeed;
/*     */   
/*     */   private float rotateSpeed;
/*     */   
/*  54 */   private Vec3 movementVector = Vec3.ZERO;
/*     */   
/*  56 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.5F, 0.5F).withEyeHeight(0.37F);
/*     */   
/*     */   public Squid(EntityType<? extends Squid> type, Level level) {
/*  59 */     super(type, level);
/*  61 */     this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  66 */     this.goalSelector.addGoal(0, new SquidRandomMovementGoal(this));
/*  67 */     this.goalSelector.addGoal(1, new SquidFleeGoal());
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  71 */     return Mob.createMobAttributes()
/*  72 */       .add(Attributes.MAX_HEALTH, 10.0D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  77 */     return SoundEvents.SQUID_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  82 */     return SoundEvents.SQUID_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  87 */     return SoundEvents.SQUID_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSquirtSound() {
/*  91 */     return SoundEvents.SQUID_SQUIRT;
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 101 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 106 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 111 */     return (AgeableMob)EntityTypes.SQUID.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 116 */     return 0.08D;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 121 */     super.aiStep();
/* 123 */     this.xBodyRotO = this.xBodyRot;
/* 124 */     this.zBodyRotO = this.zBodyRot;
/* 126 */     this.oldTentacleMovement = this.tentacleMovement;
/* 127 */     this.oldTentacleAngle = this.tentacleAngle;
/* 129 */     this.tentacleMovement += this.tentacleSpeed;
/* 130 */     if (this.tentacleMovement > 6.283185307179586D)
/* 131 */       if (level().isClientSide()) {
/* 132 */         this.tentacleMovement = 6.2831855F;
/*     */       } else {
/* 134 */         this.tentacleMovement -= 6.2831855F;
/* 135 */         if (this.random.nextInt(10) == 0)
/* 136 */           this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F; 
/* 138 */         level().broadcastEntityEvent((Entity)this, (byte)19);
/*     */       }  
/* 142 */     if (isInWater()) {
/* 143 */       if (this.tentacleMovement < 3.1415927F) {
/* 144 */         float tentacleScale = this.tentacleMovement / 3.1415927F;
/* 145 */         this.tentacleAngle = Mth.sin((tentacleScale * tentacleScale * 3.1415927F)) * 3.1415927F * 0.25F;
/* 147 */         if (tentacleScale > 0.75D) {
/* 148 */           if (isLocalInstanceAuthoritative())
/* 149 */             setDeltaMovement(this.movementVector); 
/* 151 */           this.rotateSpeed = 1.0F;
/*     */         } else {
/* 153 */           this.rotateSpeed *= 0.8F;
/*     */         } 
/*     */       } else {
/* 156 */         this.tentacleAngle = 0.0F;
/* 157 */         if (isLocalInstanceAuthoritative())
/* 158 */           setDeltaMovement(getDeltaMovement().scale(0.9D)); 
/* 160 */         this.rotateSpeed *= 0.99F;
/*     */       } 
/* 163 */       Vec3 movement = getDeltaMovement();
/* 164 */       double horizontalMovement = movement.horizontalDistance();
/* 166 */       this.yBodyRot += (-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F - this.yBodyRot) * 0.1F;
/* 167 */       setYRot(this.yBodyRot);
/* 168 */       this.zBodyRot += 3.1415927F * this.rotateSpeed * 1.5F;
/* 169 */       this.xBodyRot += (-((float)Mth.atan2(horizontalMovement, movement.y)) * 57.295776F - this.xBodyRot) * 0.1F;
/*     */     } else {
/* 171 */       this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * 3.1415927F * 0.25F;
/* 173 */       if (!level().isClientSide()) {
/* 174 */         double yd = (getDeltaMovement()).y;
/* 176 */         if (hasEffect(MobEffects.LEVITATION)) {
/* 177 */           yd = 0.05D * (getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
/*     */         } else {
/* 179 */           yd -= getGravity();
/*     */         } 
/* 182 */         setDeltaMovement(0.0D, yd * getAirDrag(), 0.0D);
/*     */       } 
/* 186 */       this.xBodyRot += (-90.0F - this.xBodyRot) * 0.02F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 192 */     if (super.hurtServer(level, source, damage) && getLastHurtByMob() != null) {
/* 193 */       spawnInk();
/* 194 */       return true;
/*     */     } 
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   private Vec3 rotateVector(Vec3 vec) {
/* 201 */     Vec3 v = vec.xRot(this.xBodyRotO * 0.017453292F);
/* 202 */     v = v.yRot(-this.yBodyRotO * 0.017453292F);
/* 203 */     return v;
/*     */   }
/*     */   
/*     */   private void spawnInk() {
/* 207 */     makeSound(getSquirtSound());
/* 208 */     Vec3 pos = rotateVector(new Vec3(0.0D, -1.0D, 0.0D)).add(getX(), getY(), getZ());
/* 209 */     for (int i = 0; i < 30; i++) {
/* 210 */       Vec3 dir = rotateVector(new Vec3(this.random.nextFloat() * 0.6D - 0.3D, -1.0D, this.random.nextFloat() * 0.6D - 0.3D));
/* 211 */       float inkPosOffsetScale = isBaby() ? 0.1F : 0.3F;
/* 212 */       Vec3 dirOffset = dir.scale((inkPosOffsetScale + this.random.nextFloat() * 2.0F));
/* 213 */       ((ServerLevel)level()).sendParticles(getInkParticle(), pos.x, pos.y + 0.5D, pos.z, 0, dirOffset.x, dirOffset.y, dirOffset.z, 0.10000000149011612D);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ParticleOptions getInkParticle() {
/* 218 */     return (ParticleOptions)ParticleTypes.SQUID_INK;
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 223 */     move(MoverType.SELF, getDeltaMovement());
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 228 */     if (id == 19) {
/* 229 */       this.tentacleMovement = 0.0F;
/*     */     } else {
/* 231 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasMovementVector() {
/* 236 */     return (this.movementVector.lengthSqr() > 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 241 */     SpawnGroupData spawnGroupData = Objects.<SpawnGroupData>requireNonNullElseGet(groupData, () -> new AgeableMob.AgeableMobGroupData(0.05F));
/* 242 */     return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 247 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   private static class SquidRandomMovementGoal extends Goal {
/*     */     private final Squid squid;
/*     */     
/*     */     public SquidRandomMovementGoal(Squid squid) {
/* 254 */       this.squid = squid;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 259 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 264 */       int noActionTime = this.squid.getNoActionTime();
/* 266 */       if (noActionTime > 100) {
/* 267 */         this.squid.movementVector = Vec3.ZERO;
/* 268 */       } else if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || !this.squid.wasTouchingWater || !this.squid.hasMovementVector()) {
/* 269 */         float angle = this.squid.getRandom().nextFloat() * 6.2831855F;
/* 270 */         this.squid
/*     */           
/* 273 */           .movementVector = new Vec3((Mth.cos(angle) * 0.2F), (-0.1F + this.squid.getRandom().nextFloat() * 0.2F), (Mth.sin(angle) * 0.2F));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class SquidFleeGoal extends Goal {
/*     */     private static final float SQUID_FLEE_SPEED = 3.0F;
/*     */     
/*     */     private static final float SQUID_FLEE_MIN_DISTANCE = 5.0F;
/*     */     
/*     */     private static final float SQUID_FLEE_MAX_DISTANCE = 10.0F;
/*     */     
/*     */     private int fleeTicks;
/*     */     
/*     */     public boolean canUse() {
/* 288 */       LivingEntity entity = Squid.this.getLastHurtByMob();
/* 289 */       if (Squid.this.isInWater() && entity != null)
/* 290 */         return (Squid.this.distanceToSqr((Entity)entity) < 100.0D); 
/* 293 */       return false;
/*     */     }
/*     */     
/*     */     public void start() {
/* 298 */       this.fleeTicks = 0;
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 303 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 308 */       this.fleeTicks++;
/* 310 */       LivingEntity lastHurtByMob = Squid.this.getLastHurtByMob();
/* 311 */       if (lastHurtByMob == null)
/*     */         return; 
/* 315 */       Vec3 fleeTo = new Vec3(Squid.this.getX() - lastHurtByMob.getX(), Squid.this.getY() - lastHurtByMob.getY(), Squid.this.getZ() - lastHurtByMob.getZ());
/* 317 */       BlockState blockState = Squid.this.level().getBlockState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 318 */       FluidState fluidState = Squid.this.level().getFluidState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 319 */       if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
/* 320 */         double length = fleeTo.length();
/* 321 */         if (length > 0.0D) {
/* 322 */           fleeTo.normalize();
/* 324 */           double avoidSpeed = 3.0D;
/* 325 */           if (length > 5.0D)
/* 326 */             avoidSpeed -= (length - 5.0D) / 5.0D; 
/* 329 */           if (avoidSpeed > 0.0D)
/* 330 */             fleeTo = fleeTo.scale(avoidSpeed); 
/*     */         } 
/* 334 */         if (blockState.isAir())
/* 335 */           fleeTo = fleeTo.subtract(0.0D, fleeTo.y, 0.0D); 
/* 338 */         Squid.this.movementVector = new Vec3(fleeTo.x / 20.0D, fleeTo.y / 20.0D, fleeTo.z / 20.0D);
/*     */       } 
/* 345 */       if (this.fleeTicks % 10 == 5)
/* 346 */         Squid.this.level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, Squid.this.getX(), Squid.this.getY(), Squid.this.getZ(), 0.0D, 0.0D, 0.0D); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\squid\Squid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */