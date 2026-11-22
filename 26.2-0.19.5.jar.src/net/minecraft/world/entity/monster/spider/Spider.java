/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Spider extends Monster {
/*  49 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;
/*     */   
/*     */   public Spider(EntityType<? extends Spider> type, Level level) {
/*  52 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  57 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  59 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal((PathfinderMob)this, Armadillo.class, 6.0F, 1.0D, 1.2D, entity -> !((Armadillo)entity).isScared()));
/*  61 */     this.goalSelector.addGoal(3, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  62 */     this.goalSelector.addGoal(4, (Goal)new SpiderAttackGoal(this));
/*  64 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D));
/*  65 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  66 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*  68 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
/*  69 */     this.targetSelector.addGoal(2, (Goal)new SpiderTargetGoal<>(this, Player.class));
/*  70 */     this.targetSelector.addGoal(3, (Goal)new SpiderTargetGoal<>(this, IronGolem.class));
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  75 */     return (PathNavigation)new WallClimberNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  80 */     super.defineSynchedData(entityData);
/*  82 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  87 */     super.tick();
/*  89 */     if (!level().isClientSide())
/*  92 */       setClimbing(this.horizontalCollision); 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  97 */     return Monster.createMonsterAttributes()
/*  98 */       .add(Attributes.MAX_HEALTH, 16.0D)
/*  99 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 104 */     return SoundEvents.SPIDER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 109 */     return SoundEvents.SPIDER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 114 */     return SoundEvents.SPIDER_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 119 */     playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean onClimbable() {
/* 126 */     return isClimbing();
/*     */   }
/*     */   
/*     */   public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
/* 132 */     if (!state.is(Blocks.COBWEB))
/* 133 */       super.makeStuckInBlock(state, speedMultiplier); 
/*     */   }
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 139 */     if (newEffect.is(MobEffects.POISON))
/* 140 */       return false; 
/* 142 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */   
/*     */   public boolean isClimbing() {
/* 146 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setClimbing(boolean value) {
/* 150 */     byte flags = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 151 */     if (value) {
/* 152 */       flags = (byte)(flags | 0x1);
/*     */     } else {
/* 154 */       flags = (byte)(flags & 0xFFFFFFFE);
/*     */     } 
/* 156 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf(flags));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 161 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 163 */     RandomSource random = level.getRandom();
/* 164 */     if (random.nextInt(100) == 0) {
/* 165 */       Skeleton skeleton = (Skeleton)EntityTypes.SKELETON.create(level(), EntitySpawnReason.JOCKEY);
/* 166 */       if (skeleton != null) {
/* 167 */         skeleton.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 168 */         skeleton.finalizeSpawn(level, difficulty, spawnReason, null);
/* 169 */         skeleton.startRiding((Entity)this, false, false);
/*     */       } 
/*     */     } 
/* 173 */     if (groupData == null) {
/* 174 */       groupData = new SpiderEffectsGroupData();
/* 176 */       if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier())
/* 177 */         ((SpiderEffectsGroupData)groupData).setRandomEffect(random); 
/*     */     } 
/* 180 */     if (groupData instanceof SpiderEffectsGroupData) {
/* 180 */       SpiderEffectsGroupData spiderEffectsGroupData = (SpiderEffectsGroupData)groupData;
/* 181 */       Holder<MobEffect> effect = spiderEffectsGroupData.effect;
/* 182 */       if (effect != null)
/* 183 */         addEffect(new MobEffectInstance(effect, -1)); 
/*     */     } 
/* 187 */     return groupData;
/*     */   }
/*     */   
/*     */   public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
/* 192 */     if (vehicle.getBbWidth() <= getBbWidth())
/* 193 */       return new Vec3(0.0D, 0.3125D * getScale(), 0.0D); 
/* 195 */     return super.getVehicleAttachmentPoint(vehicle);
/*     */   }
/*     */   
/*     */   public static class SpiderEffectsGroupData implements SpawnGroupData {
/*     */     public Holder<MobEffect> effect;
/*     */     
/*     */     public void setRandomEffect(RandomSource random) {
/* 204 */       int selection = random.nextInt(5);
/* 205 */       if (selection <= 1) {
/* 206 */         this.effect = MobEffects.SPEED;
/* 207 */       } else if (selection <= 2) {
/* 208 */         this.effect = MobEffects.STRENGTH;
/* 209 */       } else if (selection <= 3) {
/* 210 */         this.effect = MobEffects.REGENERATION;
/* 211 */       } else if (selection <= 4) {
/* 212 */         this.effect = MobEffects.INVISIBILITY;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderAttackGoal extends MeleeAttackGoal {
/*     */     public SpiderAttackGoal(Spider mob) {
/* 219 */       super((PathfinderMob)mob, 1.0D, true);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 224 */       return (super.canUse() && !this.mob.isVehicle());
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 229 */       float br = this.mob.getLightLevelDependentMagicValue();
/* 230 */       if (br >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
/* 231 */         this.mob.setTarget(null);
/* 232 */         return false;
/*     */       } 
/* 234 */       return super.canContinueToUse();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*     */     public SpiderTargetGoal(Spider mob, Class<T> targetType) {
/* 240 */       super((Mob)mob, targetType, true);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 245 */       float br = this.mob.getLightLevelDependentMagicValue();
/* 246 */       if (br >= 0.5F)
/* 247 */         return false; 
/* 250 */       return super.canUse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\spider\Spider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */