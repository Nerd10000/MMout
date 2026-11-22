/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MagmaCube extends AbstractCubeMob implements Enemy {
/*     */   public MagmaCube(EntityType<? extends MagmaCube> type, Level level) {
/*  33 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  38 */     this.goalSelector.addGoal(2, new AbstractCubeMob.CubeMobAttackGoal(this));
/*     */   }
/*     */   
/*     */   protected void addTargetingGoals() {
/*  44 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, (target, level) -> (Math.abs(target.getY() - getY()) <= 4.0D)));
/*  45 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  49 */     return Monster.createMonsterAttributes()
/*  50 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkMagmaCubeSpawnRules(EntityType<MagmaCube> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  54 */     return (level.getDifficulty() != Difficulty.PEACEFUL);
/*     */   }
/*     */   
/*     */   public void setSize(int size, boolean updateHealth) {
/*  59 */     super.setSize(size, updateHealth);
/*  60 */     int actualSize = ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*  61 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(actualSize);
/*  62 */     getAttribute(Attributes.ARMOR).setBaseValue((size * 3));
/*  63 */     this.xpReward = actualSize;
/*     */   }
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/*  68 */     return 1.0F;
/*     */   }
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/*  73 */     return (ParticleOptions)ParticleTypes.FLAME;
/*     */   }
/*     */   
/*     */   public boolean isOnFire() {
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   protected int getJumpDelay() {
/*  83 */     return super.getJumpDelay() * 4;
/*     */   }
/*     */   
/*     */   protected void decreaseSquish() {
/*  88 */     this.targetSquish *= 0.9F;
/*     */   }
/*     */   
/*     */   public void jumpFromGround() {
/*  93 */     Vec3 movement = getDeltaMovement();
/*  94 */     float sizeJumpBoostPower = getSize() * 0.1F;
/*  95 */     setDeltaMovement(movement.x, (getJumpPower() + sizeJumpBoostPower), movement.z);
/*  96 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   protected void jumpInLiquid(TagKey<Fluid> type) {
/* 101 */     if (type == FluidTags.LAVA) {
/* 102 */       Vec3 movement = getDeltaMovement();
/* 103 */       setDeltaMovement(movement.x, (0.22F + getSize() * 0.05F), movement.z);
/* 104 */       this.needsSync = true;
/*     */     } else {
/* 106 */       super.jumpInLiquid(type);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isDealsDamage() {
/* 112 */     return isEffectiveAi();
/*     */   }
/*     */   
/*     */   protected float getAttackDamage() {
/* 117 */     return super.getAttackDamage() + 2.0F;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 122 */     if (isTiny())
/* 123 */       return SoundEvents.MAGMA_CUBE_HURT_SMALL; 
/* 125 */     return SoundEvents.MAGMA_CUBE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 131 */     if (isTiny())
/* 132 */       return SoundEvents.MAGMA_CUBE_DEATH_SMALL; 
/* 134 */     return SoundEvents.MAGMA_CUBE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 140 */     if (isTiny())
/* 141 */       return SoundEvents.MAGMA_CUBE_SQUISH_SMALL; 
/* 143 */     return SoundEvents.MAGMA_CUBE_SQUISH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 149 */     return SoundEvents.MAGMA_CUBE_JUMP;
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 159 */     if (groupData == null)
/* 160 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false); 
/* 163 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\MagmaCube.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */