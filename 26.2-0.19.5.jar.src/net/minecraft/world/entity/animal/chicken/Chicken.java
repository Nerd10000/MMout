/*     */ package net.minecraft.world.entity.animal.chicken;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
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
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Chicken extends Animal {
/*  61 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.3F, 0.4F)
/*  62 */     .withEyeHeight(0.28125F)
/*  63 */     .withAttachments(EntityAttachments.builder()
/*  64 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.375F, 0.0F));
/*     */   
/*  65 */   private static final EntityDataAccessor<Holder<ChickenVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.CHICKEN_VARIANT);
/*     */   
/*  66 */   private static final EntityDataAccessor<Holder<ChickenSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.CHICKEN_SOUND_VARIANT);
/*     */   
/*     */   private static final boolean DEFAULT_CHICKEN_JOCKEY = false;
/*     */   
/*     */   public float flap;
/*     */   
/*     */   public float flapSpeed;
/*     */   
/*     */   public float oFlapSpeed;
/*     */   
/*     */   public float oFlap;
/*     */   
/*  73 */   public float flapping = 1.0F;
/*     */   
/*  74 */   private float nextFlap = 1.0F;
/*     */   
/*     */   public int eggTime;
/*     */   
/*     */   public boolean isChickenJockey = false;
/*     */   
/*     */   public Chicken(EntityType<? extends Chicken> type, Level level) {
/*  79 */     super(type, level);
/*  81 */     this.eggTime = this.random.nextInt(6000) + 6000;
/*  83 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  88 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  89 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.4D));
/*  90 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  91 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, i -> i.is(ItemTags.CHICKEN_FOOD), false));
/*  92 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.1D));
/*  93 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  94 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  95 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   private Holder<ChickenSoundVariant> getSoundVariant() {
/*  99 */     return (Holder<ChickenSoundVariant>)this.entityData.get(DATA_SOUND_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setSoundVariant(Holder<ChickenSoundVariant> soundVariant) {
/* 103 */     this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
/*     */   }
/*     */   
/*     */   private ChickenSoundVariant.ChickenSoundSet getSoundSet() {
/* 107 */     return isBaby() ? ((ChickenSoundVariant)getSoundVariant().value()).babySounds() : ((ChickenSoundVariant)getSoundVariant().value()).adultSounds();
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 112 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 116 */     return Animal.createAnimalAttributes()
/* 117 */       .add(Attributes.MAX_HEALTH, 4.0D)
/* 118 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 123 */     super.aiStep();
/* 125 */     this.oFlap = this.flap;
/* 126 */     this.oFlapSpeed = this.flapSpeed;
/* 128 */     this.flapSpeed += (onGround() ? -1.0F : 4.0F) * 0.3F;
/* 129 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/* 131 */     if (!onGround() && this.flapping < 1.0F)
/* 132 */       this.flapping = 1.0F; 
/* 134 */     this.flapping *= 0.9F;
/* 136 */     Vec3 movement = getDeltaMovement();
/* 137 */     if (!onGround() && movement.y < 0.0D)
/* 138 */       setDeltaMovement(movement.multiply(1.0D, 0.6D, 1.0D)); 
/* 141 */     this.flap += this.flapping * 2.0F;
/* 143 */     Level level = level();
/* 143 */     if (level instanceof ServerLevel) {
/* 143 */       ServerLevel serverLevel = (ServerLevel)level;
/* 143 */       if (isAlive() && !isBaby() && !isChickenJockey() && --this.eggTime <= 0) {
/* 144 */         if (dropFromGiftLootTable(serverLevel, BuiltInLootTables.CHICKEN_LAY, this::spawnAtLocation)) {
/* 145 */           playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 146 */           gameEvent((Holder)GameEvent.ENTITY_PLACE);
/*     */         } 
/* 148 */         this.eggTime = this.random.nextInt(6000) + 6000;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isFlapping() {
/* 154 */     return (this.flyDist > this.nextFlap);
/*     */   }
/*     */   
/*     */   protected void onFlap() {
/* 159 */     this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 164 */     return (SoundEvent)getSoundSet().ambientSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 169 */     return (SoundEvent)getSoundSet().hurtSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 174 */     return (SoundEvent)getSoundSet().deathSound().value();
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 179 */     playSound((SoundEvent)getSoundSet().stepSound().value(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public Chicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 184 */     Chicken baby = (Chicken)EntityTypes.CHICKEN.create((Level)level, EntitySpawnReason.BREEDING);
/* 185 */     if (baby != null && partner instanceof Chicken) {
/* 185 */       Chicken partnerChicken = (Chicken)partner;
/* 186 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerChicken.getVariant());
/*     */     } 
/* 188 */     return baby;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 193 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
/* 194 */     setSoundVariant(ChickenSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/* 195 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 200 */     return itemStack.is(ItemTags.CHICKEN_FOOD);
/*     */   }
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 205 */     if (isChickenJockey())
/* 206 */       return 10; 
/* 208 */     return super.getBaseExperienceReward(level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 213 */     super.defineSynchedData(entityData);
/* 214 */     Registry<ChickenSoundVariant> chickenSoundVariants = registryAccess().lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT);
/* 215 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), ChickenVariants.TEMPERATE));
/* 216 */     Objects.requireNonNull(chickenSoundVariants);
/* 216 */     entityData.define(DATA_SOUND_VARIANT_ID, chickenSoundVariants.get(ChickenSoundVariants.CLASSIC).or(chickenSoundVariants::getAny).orElseThrow());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 221 */     super.readAdditionalSaveData(input);
/* 222 */     this.isChickenJockey = input.getBooleanOr("IsChickenJockey", false);
/* 224 */     input.getInt("EggLayTime").ifPresent(time -> this.eggTime = time.intValue());
/* 225 */     VariantUtils.readVariant(input, Registries.CHICKEN_VARIANT).ifPresent(this::setVariant);
/* 226 */     input.read("sound_variant", ResourceKey.codec(Registries.CHICKEN_SOUND_VARIANT))
/* 227 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 232 */     super.addAdditionalSaveData(output);
/* 233 */     output.putBoolean("IsChickenJockey", this.isChickenJockey);
/* 234 */     output.putInt("EggLayTime", this.eggTime);
/* 235 */     VariantUtils.writeVariant(output, getVariant());
/* 236 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.CHICKEN_SOUND_VARIANT), soundVariant));
/*     */   }
/*     */   
/*     */   public void setVariant(Holder<ChickenVariant> variant) {
/* 240 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<ChickenVariant> getVariant() {
/* 244 */     return (Holder<ChickenVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 249 */     if (type == DataComponents.CHICKEN_VARIANT)
/* 250 */       return (T)castComponentValue(type, getVariant()); 
/* 252 */     if (type == DataComponents.CHICKEN_SOUND_VARIANT)
/* 253 */       return (T)castComponentValue(type, getSoundVariant()); 
/* 255 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 260 */     applyImplicitComponentIfPresent(components, DataComponents.CHICKEN_VARIANT);
/* 261 */     applyImplicitComponentIfPresent(components, DataComponents.CHICKEN_SOUND_VARIANT);
/* 262 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 267 */     if (type == DataComponents.CHICKEN_VARIANT) {
/* 268 */       setVariant((Holder<ChickenVariant>)castComponentValue(DataComponents.CHICKEN_VARIANT, value));
/* 269 */       return true;
/*     */     } 
/* 271 */     if (type == DataComponents.CHICKEN_SOUND_VARIANT) {
/* 272 */       setSoundVariant((Holder<ChickenSoundVariant>)castComponentValue(DataComponents.CHICKEN_SOUND_VARIANT, value));
/* 273 */       return true;
/*     */     } 
/* 276 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 281 */     return isChickenJockey();
/*     */   }
/*     */   
/*     */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/* 286 */     super.positionRider(passenger, moveFunction);
/* 287 */     if (passenger instanceof LivingEntity) {
/* 287 */       LivingEntity livingEntity = (LivingEntity)passenger;
/* 288 */       livingEntity.yBodyRot = this.yBodyRot;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isChickenJockey() {
/* 293 */     return this.isChickenJockey;
/*     */   }
/*     */   
/*     */   public void setChickenJockey(boolean isChickenJockey) {
/* 297 */     this.isChickenJockey = isChickenJockey;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\Chicken.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */