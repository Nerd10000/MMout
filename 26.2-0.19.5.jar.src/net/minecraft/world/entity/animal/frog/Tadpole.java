/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Bucketable;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Tadpole extends AbstractFish {
/*     */   private static final int DEFAULT_AGE = 0;
/*     */   
/*  49 */   private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(Tadpole.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   @VisibleForTesting
/*  52 */   public static int ticksToBeFrog = Math.abs(-24000);
/*     */   
/*     */   public static final float HITBOX_WIDTH = 0.4F;
/*     */   
/*     */   public static final float HITBOX_HEIGHT = 0.3F;
/*     */   
/*  55 */   private int age = 0;
/*     */   
/*  56 */   protected int ageLockParticleTimer = 0;
/*     */   
/*  58 */   private static final Brain.Provider<Tadpole> BRAIN_PROVIDER = Brain.provider(
/*  59 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS), paramTadpole -> TadpoleAi.getActivities());
/*     */   
/*     */   public Tadpole(EntityType<? extends AbstractFish> type, Level level) {
/*  69 */     super(type, level);
/*  71 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.02F, 0.1F, true);
/*  72 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  77 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   protected Brain<Tadpole> makeBrain(Brain.Packed packedBrain) {
/*  82 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Tadpole> getBrain() {
/*  88 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/*  93 */     return SoundEvents.TADPOLE_FLOP;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  98 */     ProfilerFiller profiler = Profiler.get();
/*  99 */     profiler.push("tadpoleBrain");
/* 100 */     getBrain().tick(level, (LivingEntity)this);
/* 101 */     profiler.pop();
/* 103 */     profiler.push("tadpoleActivityUpdate");
/* 104 */     TadpoleAi.updateActivity(this);
/* 105 */     profiler.pop();
/* 107 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 111 */     return Animal.createAnimalAttributes()
/* 112 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 113 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 118 */     super.aiStep();
/* 120 */     if (!level().isClientSide() && !isAgeLocked())
/* 121 */       setAge(this.age + 1); 
/* 124 */     this.ageLockParticleTimer = AgeableMob.makeAgeLockedParticle(level(), (Mob)this, this.ageLockParticleTimer, isAgeLocked());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 129 */     super.addAdditionalSaveData(output);
/* 130 */     output.putInt("Age", this.age);
/* 131 */     output.putBoolean("AgeLocked", isAgeLocked());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 136 */     super.readAdditionalSaveData(input);
/* 137 */     setAge(input.getIntOr("Age", 0));
/* 138 */     setAgeLocked(input.getBooleanOr("AgeLocked", false));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 143 */     super.defineSynchedData(entityData);
/* 144 */     entityData.define(AGE_LOCKED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void setAgeLocked(boolean locked) {
/* 148 */     this.entityData.set(AGE_LOCKED, Boolean.valueOf(locked));
/*     */   }
/*     */   
/*     */   public boolean isAgeLocked() {
/* 152 */     return ((Boolean)this.entityData.get(AGE_LOCKED)).booleanValue();
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 157 */     return null;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 162 */     return SoundEvents.TADPOLE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 167 */     return SoundEvents.TADPOLE_DEATH;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 172 */     ItemStack itemStack = player.getItemInHand(hand);
/* 173 */     if (isFood(itemStack) && !isAgeLocked()) {
/* 174 */       feed(player, itemStack);
/* 175 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 178 */     if (AgeableMob.canUseGoldenDandelion(itemStack, true, this.ageLockParticleTimer, (Mob)this)) {
/* 179 */       AgeableMob.setAgeLocked((Mob)this, this::isAgeLocked, player, itemStack, mob -> setAgeLockedData());
/* 180 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 182 */     return Bucketable.bucketMobPickup(player, hand, (LivingEntity)this).orElse(super.mobInteract(player, hand));
/*     */   }
/*     */   
/*     */   private void setAgeLockedData() {
/* 186 */     setAgeLocked(!isAgeLocked());
/* 187 */     setAge(0);
/* 188 */     this.ageLockParticleTimer = 40;
/*     */   }
/*     */   
/*     */   public boolean fromBucket() {
/* 194 */     return true;
/*     */   }
/*     */   
/*     */   public void setFromBucket(boolean fromBucket) {}
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 204 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/* 206 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/*     */           tag.putInt("Age", getAge());
/*     */           tag.putBoolean("AgeLocked", isAgeLocked());
/*     */         });
/*     */   }
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag tag) {
/* 215 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, tag);
/* 216 */     tag.getInt("Age").ifPresent(this::setAge);
/* 217 */     setAgeLocked(tag.getBooleanOr("AgeLocked", false));
/*     */   }
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/* 222 */     return new ItemStack((ItemLike)Items.TADPOLE_BUCKET);
/*     */   }
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 227 */     return SoundEvents.BUCKET_FILL_TADPOLE;
/*     */   }
/*     */   
/*     */   private boolean isFood(ItemStack itemStack) {
/* 231 */     return itemStack.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */   
/*     */   private void feed(Player player, ItemStack itemStack) {
/* 235 */     usePlayerItem(player, itemStack);
/* 236 */     ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(getTicksLeftUntilAdult()));
/* 237 */     level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private void usePlayerItem(Player player, ItemStack itemStack) {
/* 241 */     itemStack.consume(1, (LivingEntity)player);
/*     */   }
/*     */   
/*     */   private int getAge() {
/* 245 */     return this.age;
/*     */   }
/*     */   
/*     */   private void ageUp(int ticksToAgeUp) {
/* 249 */     setAge(this.age + ticksToAgeUp * 20);
/*     */   }
/*     */   
/*     */   private void setAge(int newAge) {
/* 253 */     this.age = newAge;
/* 255 */     if (this.age >= ticksToBeFrog)
/* 256 */       ageUp(); 
/*     */   }
/*     */   
/*     */   private void ageUp() {
/* 261 */     Level level = level();
/* 261 */     if (level instanceof ServerLevel) {
/* 261 */       ServerLevel serverLevel = (ServerLevel)level;
/* 262 */       convertTo(EntityTypes.FROG, ConversionParams.single((Mob)this, false, false), frog -> {
/*     */             frog.finalizeSpawn((ServerLevelAccessor)serverLevel, serverLevel.getCurrentDifficultyAt(frog.blockPosition()), EntitySpawnReason.CONVERSION, (SpawnGroupData)null);
/*     */             frog.setPersistenceRequired();
/*     */             frog.fudgePositionAfterSizeChange(getDimensions(getPose()));
/*     */             playSound(SoundEvents.TADPOLE_GROW_UP, 0.15F, 1.0F);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getTicksLeftUntilAdult() {
/* 272 */     return Math.max(0, ticksToBeFrog - this.age);
/*     */   }
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 277 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\Tadpole.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */