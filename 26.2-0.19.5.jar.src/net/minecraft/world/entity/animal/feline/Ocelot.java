/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ocelot extends Animal {
/*     */   public static final double CROUCH_SPEED_MOD = 0.6D;
/*     */   
/*     */   public static final double WALK_SPEED_MOD = 0.8D;
/*     */   
/*     */   public static final double SPRINT_SPEED_MOD = 1.33D;
/*     */   
/*  66 */   private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(Ocelot.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_TRUSTING = false;
/*     */   
/*  68 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.3F, 0.35F)
/*  69 */     .withEyeHeight(0.34375F)
/*  70 */     .withAttachments(EntityAttachments.builder()
/*  71 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.3125F, 0.0F));
/*     */   
/*     */   private OcelotAvoidEntityGoal<Player> ocelotAvoidPlayersGoal;
/*     */   
/*     */   private OcelotTemptGoal temptGoal;
/*     */   
/*     */   public Ocelot(EntityType<? extends Ocelot> type, Level level) {
/*  77 */     super(type, level);
/*  79 */     reassessTrustingGoals();
/*     */   }
/*     */   
/*     */   private boolean isTrusting() {
/*  83 */     return ((Boolean)this.entityData.get(DATA_TRUSTING)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setTrusting(boolean trusting) {
/*  87 */     this.entityData.set(DATA_TRUSTING, Boolean.valueOf(trusting));
/*  89 */     reassessTrustingGoals();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  94 */     super.addAdditionalSaveData(output);
/*  96 */     output.putBoolean("Trusting", isTrusting());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 101 */     super.readAdditionalSaveData(input);
/* 103 */     setTrusting(input.getBooleanOr("Trusting", false));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 108 */     super.defineSynchedData(entityData);
/* 110 */     entityData.define(DATA_TRUSTING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 115 */     this.temptGoal = new OcelotTemptGoal(this, 0.6D, i -> i.is(ItemTags.OCELOT_FOOD), true);
/* 116 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 117 */     this.goalSelector.addGoal(3, (Goal)this.temptGoal);
/* 118 */     this.goalSelector.addGoal(7, (Goal)new LeapAtTargetGoal((Mob)this, 0.3F));
/* 119 */     this.goalSelector.addGoal(8, (Goal)new OcelotAttackGoal((Mob)this));
/* 120 */     this.goalSelector.addGoal(9, (Goal)new BreedGoal(this, 0.8D));
/* 121 */     this.goalSelector.addGoal(10, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D, 1.0000001E-5F));
/* 122 */     this.goalSelector.addGoal(11, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/* 124 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Chicken.class, false));
/* 125 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 130 */     if (getMoveControl().hasWanted()) {
/* 131 */       double speed = getMoveControl().getSpeedModifier();
/* 132 */       if (speed == 0.6D) {
/* 133 */         setPose(Pose.CROUCHING);
/* 134 */         setSprinting(false);
/* 135 */       } else if (speed == 1.33D) {
/* 136 */         setPose(Pose.STANDING);
/* 137 */         setSprinting(true);
/*     */       } else {
/* 139 */         setPose(Pose.STANDING);
/* 140 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 143 */       setPose(Pose.STANDING);
/* 144 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 150 */     return (!isTrusting() && this.tickCount > 2400);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 154 */     return Animal.createAnimalAttributes()
/* 155 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 156 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 157 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 162 */     return SoundEvents.OCELOT_AMBIENT;
/*     */   }
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 167 */     return 900;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 172 */     return SoundEvents.OCELOT_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 177 */     return SoundEvents.OCELOT_DEATH;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 182 */     ItemStack itemStack = player.getItemInHand(hand);
/* 183 */     if ((this.temptGoal == null || this.temptGoal.isRunning()) && !isTrusting() && isFood(itemStack) && player.distanceToSqr((Entity)this) < 9.0D) {
/* 184 */       usePlayerItem(player, hand, itemStack);
/* 186 */       if (!level().isClientSide())
/* 187 */         if (this.random.nextInt(3) == 0) {
/* 188 */           setTrusting(true);
/* 189 */           spawnTrustingParticles(true);
/* 190 */           level().broadcastEntityEvent((Entity)this, (byte)41);
/*     */         } else {
/* 192 */           spawnTrustingParticles(false);
/* 193 */           level().broadcastEntityEvent((Entity)this, (byte)40);
/*     */         }  
/* 197 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 200 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 205 */     if (id == 41) {
/* 206 */       spawnTrustingParticles(true);
/* 207 */     } else if (id == 40) {
/* 208 */       spawnTrustingParticles(false);
/*     */     } else {
/* 210 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnTrustingParticles(boolean success) {
/* 215 */     SimpleParticleType simpleParticleType = ParticleTypes.HEART;
/* 216 */     if (!success)
/* 217 */       simpleParticleType = ParticleTypes.SMOKE; 
/* 219 */     for (int i = 0; i < 7; i++) {
/* 220 */       double xa = this.random.nextGaussian() * 0.02D;
/* 221 */       double ya = this.random.nextGaussian() * 0.02D;
/* 222 */       double za = this.random.nextGaussian() * 0.02D;
/* 223 */       level().addParticle((ParticleOptions)simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void reassessTrustingGoals() {
/* 228 */     if (this.ocelotAvoidPlayersGoal == null)
/* 229 */       this.ocelotAvoidPlayersGoal = new OcelotAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D); 
/* 232 */     this.goalSelector.removeGoal((Goal)this.ocelotAvoidPlayersGoal);
/* 234 */     if (!isTrusting())
/* 235 */       this.goalSelector.addGoal(4, (Goal)this.ocelotAvoidPlayersGoal); 
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 241 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public Ocelot getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 246 */     return (Ocelot)EntityTypes.OCELOT.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 251 */     return itemStack.is(ItemTags.OCELOT_FOOD);
/*     */   }
/*     */   
/*     */   public static boolean checkOcelotSpawnRules(EntityType<Ocelot> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 257 */     return (random.nextInt(3) != 0);
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 262 */     if (level.isUnobstructed((Entity)this) && !level.containsAnyLiquid(getBoundingBox())) {
/* 263 */       BlockPos pos = blockPosition();
/* 264 */       if (pos.getY() < level.getSeaLevel())
/* 265 */         return false; 
/* 268 */       BlockState state = level.getBlockState(pos.below());
/* 269 */       if (state.is(Blocks.GRASS_BLOCK) || state.is(BlockTags.LEAVES))
/* 270 */         return true; 
/*     */     } 
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 278 */     if (groupData == null)
/* 280 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(1.0F); 
/* 283 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 288 */     return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   public boolean isSteppingCarefully() {
/* 293 */     return (isCrouching() || super.isSteppingCarefully());
/*     */   }
/*     */   
/*     */   private static class OcelotAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */     private final Ocelot ocelot;
/*     */     
/*     */     public OcelotAvoidEntityGoal(Ocelot ocelot, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 300 */       super((PathfinderMob)ocelot, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 301 */       this.ocelot = ocelot;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 306 */       return (!this.ocelot.isTrusting() && super.canUse());
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 311 */       return (!this.ocelot.isTrusting() && super.canContinueToUse());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OcelotTemptGoal extends TemptGoal {
/*     */     private final Ocelot ocelot;
/*     */     
/*     */     public OcelotTemptGoal(Ocelot ocelot, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 319 */       super((PathfinderMob)ocelot, speedModifier, items, canScare);
/* 320 */       this.ocelot = ocelot;
/*     */     }
/*     */     
/*     */     protected boolean canScare() {
/* 325 */       return (super.canScare() && !this.ocelot.isTrusting());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Ocelot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */