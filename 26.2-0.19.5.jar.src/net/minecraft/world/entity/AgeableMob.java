/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AgeableMob extends PathfinderMob {
/*  31 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  32 */   private static final EntityDataAccessor<Boolean> AGE_LOCKED = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final int BABY_START_AGE = -24000;
/*     */   
/*     */   public static final int AGE_LOCK_COOLDOWN_TICKS = 40;
/*     */   
/*     */   public static final float AGE_LOCK_DOWNWARDS_MOVING_PARTICLE_Y_OFFSET = 0.2F;
/*     */   
/*     */   private static final int FORCED_AGE_PARTICLE_TICKS = 40;
/*     */   
/*     */   protected static final int DEFAULT_AGE = 0;
/*     */   
/*     */   protected static final int DEFAULT_FORCED_AGE = 0;
/*     */   
/*  42 */   protected int age = 0;
/*     */   
/*  43 */   protected int forcedAge = 0;
/*     */   
/*     */   protected int forcedAgeTimer;
/*     */   
/*  45 */   protected int ageLockParticleTimer = 0;
/*     */   
/*     */   protected AgeableMob(EntityType<? extends AgeableMob> type, Level level) {
/*  48 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  53 */     if (groupData == null)
/*  54 */       groupData = new AgeableMobGroupData(true); 
/*  57 */     AgeableMobGroupData ageableMobGroupData = (AgeableMobGroupData)groupData;
/*  59 */     if (ageableMobGroupData.isShouldSpawnBaby() && ageableMobGroupData.getGroupSize() > 0 && level.getRandom().nextFloat() <= ageableMobGroupData.getBabySpawnChance())
/*  60 */       setAge(getBabyStartAge()); 
/*  63 */     ageableMobGroupData.increaseGroupSizeByOne();
/*  65 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  72 */     ItemStack itemInHand = player.getItemInHand(hand);
/*  73 */     if (canUseGoldenDandelion(itemInHand, isBaby(), this.ageLockParticleTimer, this)) {
/*  74 */       setAgeLocked(this, this::isAgeLocked, player, itemInHand, mob -> setAgeLockedData());
/*  75 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  77 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public static boolean canUseGoldenDandelion(ItemStack itemInHand, boolean isBaby, int cooldown, Mob mob) {
/*  81 */     return (itemInHand.getItem() == Items.GOLDEN_DANDELION && isBaby && cooldown == 0 && !mob.is(EntityTypeTags.CANNOT_BE_AGE_LOCKED));
/*     */   }
/*     */   
/*     */   private void setAgeLockedData() {
/*  85 */     setAgeLocked(!isAgeLocked());
/*  86 */     setAge(getBabyStartAge());
/*  87 */     this.ageLockParticleTimer = 40;
/*     */   }
/*     */   
/*     */   public static void setAgeLocked(Mob mob, Supplier<Boolean> isAgedLocked, Player player, ItemStack itemInHand, Consumer<Mob> setAgeLockData) {
/*  91 */     setAgeLockData.accept(mob);
/*  92 */     itemInHand.consume(1, (LivingEntity)player);
/*  93 */     boolean isAgeLocked = ((Boolean)isAgedLocked.get()).booleanValue();
/*  94 */     if (isAgeLocked)
/*  95 */       mob.setPersistenceRequired(); 
/*  97 */     mob.level().playSound(null, mob.blockPosition(), isAgeLocked ? SoundEvents.GOLDEN_DANDELION_USE : SoundEvents.GOLDEN_DANDELION_UNUSE, SoundSource.PLAYERS, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 102 */     super.defineSynchedData(entityData);
/* 103 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 104 */     entityData.define(AGE_LOCKED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canBreed() {
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   public int getAge() {
/* 112 */     if (level().isClientSide())
/* 113 */       return ((Boolean)this.entityData.get(DATA_BABY_ID)).booleanValue() ? -1 : 1; 
/* 115 */     return this.age;
/*     */   }
/*     */   
/*     */   public void ageUp(int seconds, boolean forced) {
/* 120 */     int age = getAge();
/* 121 */     int oldAge = age;
/* 122 */     age += seconds * 20;
/* 123 */     if (age > 0)
/* 124 */       age = 0; 
/* 126 */     int delta = age - oldAge;
/* 127 */     setAge(age);
/* 128 */     if (forced) {
/* 129 */       this.forcedAge += delta;
/* 130 */       if (this.forcedAgeTimer == 0)
/* 131 */         this.forcedAgeTimer = 40; 
/*     */     } 
/* 134 */     if (getAge() == 0)
/* 135 */       setAge(this.forcedAge); 
/*     */   }
/*     */   
/*     */   public void ageUp(int seconds) {
/* 140 */     ageUp(seconds, false);
/*     */   }
/*     */   
/*     */   public void setAge(int newAge) {
/* 144 */     int oldAge = getAge();
/* 145 */     this.age = newAge;
/* 147 */     if ((oldAge < 0 && newAge >= 0) || (oldAge >= 0 && newAge < 0)) {
/* 148 */       this.entityData.set(DATA_BABY_ID, Boolean.valueOf((newAge < 0)));
/* 149 */       ageBoundaryReached();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setAgeLocked(boolean locked) {
/* 154 */     this.entityData.set(AGE_LOCKED, Boolean.valueOf(locked));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 159 */     super.addAdditionalSaveData(output);
/* 160 */     if (canBeABaby()) {
/* 161 */       output.putInt("Age", getAge());
/* 162 */       output.putInt("ForcedAge", this.forcedAge);
/* 163 */       output.putBoolean("AgeLocked", isAgeLocked());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 168 */     return true;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 173 */     super.readAdditionalSaveData(input);
/* 174 */     if (canBeABaby()) {
/* 175 */       setAge(input.getIntOr("Age", 0));
/* 176 */       this.forcedAge = input.getIntOr("ForcedAge", 0);
/* 177 */       setAgeLocked(input.getBooleanOr("AgeLocked", false));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 183 */     if (DATA_BABY_ID.equals(accessor))
/* 184 */       refreshDimensions(); 
/* 186 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 191 */     super.aiStep();
/* 193 */     if (level().isClientSide()) {
/* 194 */       if (this.forcedAgeTimer > 0) {
/* 195 */         if (this.forcedAgeTimer % 4 == 0)
/* 196 */           level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D); 
/* 198 */         this.forcedAgeTimer--;
/*     */       } 
/* 200 */     } else if (isAlive()) {
/* 201 */       int age = getAge();
/* 202 */       if (canAgeUp()) {
/* 203 */         age++;
/* 204 */         setAge(age);
/* 205 */       } else if (age > 0) {
/* 206 */         age--;
/* 207 */         setAge(age);
/*     */       } 
/*     */     } 
/* 211 */     this.ageLockParticleTimer = makeAgeLockedParticle(level(), this, this.ageLockParticleTimer, isAgeLocked());
/*     */   }
/*     */   
/*     */   public boolean canAgeUp() {
/* 215 */     return (isBaby() && !isAgeLocked());
/*     */   }
/*     */   
/*     */   public static int makeAgeLockedParticle(Level level, Mob mob, int ageLockParticleTimer, boolean isAgeLocked) {
/* 219 */     if (ageLockParticleTimer > 0) {
/* 220 */       if (level.isClientSide() && ageLockParticleTimer % 2 == 0) {
/* 221 */         float yParticleOffset = isAgeLocked ? 0.2F : 0.0F;
/* 222 */         Vec3 spawnPosition = new Vec3(mob.getRandomX(1.0D), mob.getRandomY(0.2D) + mob.getBbHeight() + yParticleOffset, mob.getRandomZ(1.0D));
/* 223 */         level.addParticle(isAgeLocked ? (ParticleOptions)ParticleTypes.PAUSE_MOB_GROWTH : (ParticleOptions)ParticleTypes.RESET_MOB_GROWTH, spawnPosition.x, spawnPosition.y, spawnPosition.z, 0.0D, 0.0D, 0.0D);
/*     */       } 
/* 225 */       ageLockParticleTimer--;
/*     */     } 
/* 227 */     return ageLockParticleTimer;
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 231 */     if (!isBaby() && 
/* 232 */       isPassenger()) {
/* 233 */       Entity entity = getVehicle();
/* 233 */       if (entity instanceof AbstractBoat) {
/* 233 */         AbstractBoat boat = (AbstractBoat)entity;
/* 234 */         if (!boat.hasEnoughSpaceFor(this))
/* 235 */           stopRiding(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getBabyStartAge() {
/* 240 */     return -24000;
/*     */   }
/*     */   
/*     */   public final boolean isBaby() {
/* 245 */     return (canBeABaby() && getAge() < 0);
/*     */   }
/*     */   
/*     */   public final void setBaby(boolean baby) {
/* 250 */     if (canBeABaby())
/* 251 */       setAge(baby ? getBabyStartAge() : 0); 
/*     */   }
/*     */   
/*     */   public static int getSpeedUpSecondsWhenFeeding(int ticksUntilAdult) {
/* 256 */     return (int)((ticksUntilAdult / 20) * 0.1F);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAge() {
/* 261 */     return this.forcedAge;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAgeTimer() {
/* 266 */     return this.forcedAgeTimer;
/*     */   }
/*     */   
/*     */   public boolean isAgeLocked() {
/* 270 */     return ((Boolean)this.entityData.get(AGE_LOCKED)).booleanValue();
/*     */   }
/*     */   
/*     */   public abstract AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob);
/*     */   
/*     */   public static class AgeableMobGroupData implements SpawnGroupData {
/*     */     private int groupSize;
/*     */     
/*     */     private final boolean shouldSpawnBaby;
/*     */     
/*     */     private final float babySpawnChance;
/*     */     
/*     */     public AgeableMobGroupData(boolean shouldSpawnBaby, float babySpawnChance) {
/* 279 */       this.shouldSpawnBaby = shouldSpawnBaby;
/* 280 */       this.babySpawnChance = babySpawnChance;
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(boolean shouldSpawnBaby) {
/* 284 */       this(shouldSpawnBaby, 0.05F);
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(float babySpawnChance) {
/* 288 */       this(true, babySpawnChance);
/*     */     }
/*     */     
/*     */     public int getGroupSize() {
/* 292 */       return this.groupSize;
/*     */     }
/*     */     
/*     */     public void increaseGroupSizeByOne() {
/* 296 */       this.groupSize++;
/*     */     }
/*     */     
/*     */     public boolean isShouldSpawnBaby() {
/* 300 */       return this.shouldSpawnBaby;
/*     */     }
/*     */     
/*     */     public float getBabySpawnChance() {
/* 304 */       return this.babySpawnChance;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\AgeableMob.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */