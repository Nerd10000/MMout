/*     */ package net.minecraft.world.entity.animal.goat;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.InstrumentTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.InstrumentItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Goat extends Animal {
/*     */   public static final float LONG_JUMPING_DIMENSION_SCALE_FACTOR = 0.7F;
/*     */   
/*  60 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.65F)
/*  61 */     .withEyeHeight(0.59375F)
/*  62 */     .withAttachments(EntityAttachments.builder()
/*  63 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.53125F, 0.0F));
/*     */   
/*     */   public static final float BABY_DEFAULT_X_HEAD_ROT = 22.5F;
/*     */   
/*     */   public static final float MAX_ADDED_RAMMING_X_HEAD_ROT = 30.0F;
/*     */   
/*     */   private static final float BABY_SCALE = 0.55F;
/*     */   
/*     */   private static final int ADULT_ATTACK_DAMAGE = 2;
/*     */   
/*     */   private static final int BABY_ATTACK_DAMAGE = 1;
/*     */   
/*  72 */   private static final Brain.Provider<Goat> BRAIN_PROVIDER = Brain.provider(
/*  73 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS), paramGoat -> GoatAi.getActivities());
/*     */   
/*     */   public static final int GOAT_FALL_DAMAGE_REDUCTION = 10;
/*     */   
/*     */   public static final double GOAT_SCREAMING_CHANCE = 0.02D;
/*     */   
/*     */   public static final double UNIHORN_CHANCE = 0.10000000149011612D;
/*     */   
/*  88 */   private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  89 */   private static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  90 */   private static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_IS_SCREAMING = false;
/*     */   
/*     */   private static final boolean DEFAULT_HAS_LEFT_HORN = true;
/*     */   
/*     */   private static final boolean DEFAULT_HAS_RIGHT_HORN = true;
/*     */   
/*     */   private boolean isLoweringHead;
/*     */   
/*     */   private int lowerHeadTick;
/*     */   
/*     */   public Goat(EntityType<? extends Goat> type, Level level) {
/* 100 */     super(type, level);
/* 102 */     getNavigation().setCanFloat(true);
/* 103 */     setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
/* 104 */     setPathfindingMalus(PathType.ON_TOP_OF_POWDER_SNOW, -1.0F);
/*     */   }
/*     */   
/*     */   public ItemStack createHorn() {
/* 108 */     RandomSource random = RandomSource.createThreadLocalInstance(getUUID().hashCode());
/* 109 */     TagKey<Instrument> key = isScreamingGoat() ? InstrumentTags.SCREAMING_GOAT_HORNS : InstrumentTags.REGULAR_GOAT_HORNS;
/* 110 */     return level().registryAccess().lookupOrThrow(Registries.INSTRUMENT)
/* 111 */       .getRandomElementOf(key, random)
/* 112 */       .map(instrument -> InstrumentItem.create(Items.GOAT_HORN, instrument))
/* 113 */       .orElseGet(() -> new ItemStack((ItemLike)Items.GOAT_HORN));
/*     */   }
/*     */   
/*     */   protected Brain<Goat> makeBrain(Brain.Packed packedBrain) {
/* 118 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Animal.createAnimalAttributes()
/* 123 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 125 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 130 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(isBaby() ? 1.0D : 2.0D);
/*     */   }
/*     */   
/*     */   protected int calculateFallDamage(double fallDistance, float damageModifier) {
/* 135 */     return super.calculateFallDamage(fallDistance, damageModifier) - 10;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 140 */     if (isScreamingGoat())
/* 141 */       return SoundEvents.GOAT_SCREAMING_AMBIENT; 
/* 143 */     return SoundEvents.GOAT_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 149 */     if (isScreamingGoat())
/* 150 */       return SoundEvents.GOAT_SCREAMING_HURT; 
/* 152 */     return SoundEvents.GOAT_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 158 */     if (isScreamingGoat())
/* 159 */       return SoundEvents.GOAT_SCREAMING_DEATH; 
/* 161 */     return SoundEvents.GOAT_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 167 */     playSound(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected SoundEvent getMilkingSound() {
/* 171 */     if (isScreamingGoat())
/* 172 */       return SoundEvents.GOAT_SCREAMING_MILK; 
/* 174 */     return SoundEvents.GOAT_MILK;
/*     */   }
/*     */   
/*     */   public Goat getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*     */     // Byte code:
/*     */     //   0: getstatic net/minecraft/world/entity/EntityTypes.GOAT : Lnet/minecraft/world/entity/EntityType;
/*     */     //   3: aload_1
/*     */     //   4: getstatic net/minecraft/world/entity/EntitySpawnReason.BREEDING : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   7: invokevirtual create : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;
/*     */     //   10: checkcast net/minecraft/world/entity/animal/goat/Goat
/*     */     //   13: astore_3
/*     */     //   14: aload_3
/*     */     //   15: ifnull -> 97
/*     */     //   18: aload_3
/*     */     //   19: aload_1
/*     */     //   20: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   23: invokestatic initMemories : (Lnet/minecraft/world/entity/animal/goat/Goat;Lnet/minecraft/util/RandomSource;)V
/*     */     //   26: aload_1
/*     */     //   27: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   30: invokeinterface nextBoolean : ()Z
/*     */     //   35: ifeq -> 42
/*     */     //   38: aload_0
/*     */     //   39: goto -> 43
/*     */     //   42: aload_2
/*     */     //   43: astore #4
/*     */     //   45: aload #4
/*     */     //   47: instanceof net/minecraft/world/entity/animal/goat/Goat
/*     */     //   50: ifeq -> 68
/*     */     //   53: aload #4
/*     */     //   55: checkcast net/minecraft/world/entity/animal/goat/Goat
/*     */     //   58: astore #6
/*     */     //   60: aload #6
/*     */     //   62: invokevirtual isScreamingGoat : ()Z
/*     */     //   65: ifne -> 84
/*     */     //   68: aload_1
/*     */     //   69: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   72: invokeinterface nextDouble : ()D
/*     */     //   77: ldc2_w 0.02
/*     */     //   80: dcmpg
/*     */     //   81: ifge -> 88
/*     */     //   84: iconst_1
/*     */     //   85: goto -> 89
/*     */     //   88: iconst_0
/*     */     //   89: istore #5
/*     */     //   91: aload_3
/*     */     //   92: iload #5
/*     */     //   94: invokevirtual setScreamingGoat : (Z)V
/*     */     //   97: aload_3
/*     */     //   98: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     //   #182	-> 14
/*     */     //   #183	-> 18
/*     */     //   #184	-> 26
/*     */     //   #185	-> 45
/*     */     //   #186	-> 91
/*     */     //   #189	-> 97
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   60	8	6	goat	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */     //   45	52	4	selectedParent	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   91	6	5	babyIsScreaming	Z
/*     */     //   0	99	0	this	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */     //   0	99	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	99	2	partner	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   14	85	3	newGoat	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 194 */     return isBaby() ? 0.55F : 1.0F;
/*     */   }
/*     */   
/*     */   public Brain<Goat> getBrain() {
/* 200 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 205 */     ProfilerFiller profiler = Profiler.get();
/* 206 */     profiler.push("goatBrain");
/* 207 */     getBrain().tick(level, (LivingEntity)this);
/* 208 */     profiler.pop();
/* 210 */     profiler.push("goatActivityUpdate");
/* 211 */     GoatAi.updateActivity(this);
/* 212 */     profiler.pop();
/* 214 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 219 */     return 15;
/*     */   }
/*     */   
/*     */   public void setYHeadRot(float yHeadRot) {
/* 224 */     int maxHeadYRot = getMaxHeadYRot();
/* 225 */     float deltaFromBody = Mth.degreesDifference(this.yBodyRot, yHeadRot);
/* 226 */     float deltaFromBodyClamped = Mth.clamp(deltaFromBody, -maxHeadYRot, maxHeadYRot);
/* 228 */     super.setYHeadRot(this.yBodyRot + deltaFromBodyClamped);
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 233 */     level().playSound(null, (Entity)this, isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_EAT : SoundEvents.GOAT_EAT, SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(level().getRandom(), 0.8F, 1.2F));
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 238 */     return itemStack.is(ItemTags.GOAT_FOOD);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 243 */     ItemStack heldItem = player.getItemInHand(hand);
/* 244 */     if (heldItem.is(Items.BUCKET) && !isBaby()) {
/* 245 */       player.playSound(getMilkingSound(), 1.0F, 1.0F);
/* 246 */       ItemStack bucketOrMilkBucket = ItemUtils.createFilledResult(heldItem, player, Items.MILK_BUCKET.getDefaultInstance());
/* 247 */       player.setItemInHand(hand, bucketOrMilkBucket);
/* 248 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 251 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 252 */     if (interactionResult.consumesAction() && isFood(heldItem))
/* 253 */       playEatingSound(); 
/* 256 */     return interactionResult;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 261 */     RandomSource random = level.getRandom();
/* 262 */     GoatAi.initMemories(this, random);
/* 264 */     setScreamingGoat((random.nextDouble() < 0.02D));
/* 265 */     ageBoundaryReached();
/* 266 */     if (!isBaby() && random.nextFloat() < 0.10000000149011612D) {
/* 267 */       EntityDataAccessor<Boolean> hornToRemove = random.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
/* 268 */       this.entityData.set(hornToRemove, Boolean.valueOf(false));
/*     */     } 
/* 271 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 276 */     EntityDimensions entityDimensions = isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/* 277 */     return (pose == Pose.LONG_JUMPING) ? entityDimensions.scale(0.7F) : entityDimensions;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 282 */     super.addAdditionalSaveData(output);
/* 284 */     output.putBoolean("IsScreamingGoat", isScreamingGoat());
/* 285 */     output.putBoolean("HasLeftHorn", hasLeftHorn());
/* 286 */     output.putBoolean("HasRightHorn", hasRightHorn());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 291 */     super.readAdditionalSaveData(input);
/* 293 */     setScreamingGoat(input.getBooleanOr("IsScreamingGoat", false));
/* 294 */     this.entityData.set(DATA_HAS_LEFT_HORN, Boolean.valueOf(input.getBooleanOr("HasLeftHorn", true)));
/* 295 */     this.entityData.set(DATA_HAS_RIGHT_HORN, Boolean.valueOf(input.getBooleanOr("HasRightHorn", true)));
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 300 */     if (id == 58) {
/* 301 */       this.isLoweringHead = true;
/* 302 */     } else if (id == 59) {
/* 303 */       this.isLoweringHead = false;
/*     */     } else {
/* 305 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 311 */     if (this.isLoweringHead) {
/* 312 */       this.lowerHeadTick++;
/*     */     } else {
/* 314 */       this.lowerHeadTick -= 2;
/*     */     } 
/* 316 */     this.lowerHeadTick = Mth.clamp(this.lowerHeadTick, 0, 20);
/* 318 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 323 */     super.defineSynchedData(entityData);
/* 324 */     entityData.define(DATA_IS_SCREAMING_GOAT, Boolean.valueOf(false));
/* 325 */     entityData.define(DATA_HAS_LEFT_HORN, Boolean.valueOf(true));
/* 326 */     entityData.define(DATA_HAS_RIGHT_HORN, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public boolean hasLeftHorn() {
/* 330 */     return ((Boolean)this.entityData.get(DATA_HAS_LEFT_HORN)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean hasRightHorn() {
/* 334 */     return ((Boolean)this.entityData.get(DATA_HAS_RIGHT_HORN)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean dropHorn() {
/*     */     EntityDataAccessor<Boolean> hornToDrop;
/* 338 */     if (isBaby())
/* 339 */       return false; 
/* 342 */     boolean hasLeft = hasLeftHorn();
/* 343 */     boolean hasRight = hasRightHorn();
/* 345 */     if (!hasLeft && !hasRight)
/* 346 */       return false; 
/* 351 */     if (!hasLeft) {
/* 352 */       hornToDrop = DATA_HAS_RIGHT_HORN;
/* 353 */     } else if (!hasRight) {
/* 354 */       hornToDrop = DATA_HAS_LEFT_HORN;
/*     */     } else {
/* 356 */       hornToDrop = this.random.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
/*     */     } 
/* 358 */     this.entityData.set(hornToDrop, Boolean.valueOf(false));
/* 360 */     Vec3 bodyPosition = position();
/* 361 */     ItemStack item = createHorn();
/* 362 */     double deltaX = Mth.randomBetween(this.random, -0.2F, 0.2F);
/* 363 */     double deltaY = Mth.randomBetween(this.random, 0.3F, 0.7F);
/* 364 */     double deltaZ = Mth.randomBetween(this.random, -0.2F, 0.2F);
/* 365 */     ItemEntity itemEntity = new ItemEntity(level(), bodyPosition.x(), bodyPosition.y(), bodyPosition.z(), item, deltaX, deltaY, deltaZ);
/* 366 */     level().addFreshEntity((Entity)itemEntity);
/* 367 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isScreamingGoat() {
/* 371 */     return ((Boolean)this.entityData.get(DATA_IS_SCREAMING_GOAT)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setScreamingGoat(boolean isScreamingGoat) {
/* 375 */     this.entityData.set(DATA_IS_SCREAMING_GOAT, Boolean.valueOf(isScreamingGoat));
/*     */   }
/*     */   
/*     */   public float getRammingXHeadRot() {
/* 379 */     float maxRammingXHeadRot = isBaby() ? 52.5F : 30.0F;
/* 380 */     return this.lowerHeadTick / 20.0F * maxRammingXHeadRot * 0.017453292F;
/*     */   }
/*     */   
/*     */   public static boolean checkGoatSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 384 */     return (level.getBlockState(pos.below()).is(BlockTags.GOATS_SPAWNABLE_ON) && 
/* 385 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\goat\Goat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */