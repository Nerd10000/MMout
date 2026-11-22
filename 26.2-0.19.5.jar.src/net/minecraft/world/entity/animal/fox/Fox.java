/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.codec.ByteBufCodecs;
/*      */ import net.minecraft.network.codec.StreamCodec;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BiomeTags;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.ByIdMap;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityAttachment;
/*      */ import net.minecraft.world.entity.EntityAttachments;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.ExperienceOrb;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.JumpGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*      */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*      */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*      */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.ItemStackTemplate;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.BlockAndLightGetter;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CaveVines;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Fox extends Animal {
/*  114 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.INT);
/*      */   
/*  115 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   private static final float BABY_SCALE = 0.6F;
/*      */   
/*      */   private static final int FLAG_SITTING = 1;
/*      */   
/*      */   public static final int FLAG_CROUCHING = 4;
/*      */   
/*      */   public static final int FLAG_INTERESTED = 8;
/*      */   
/*      */   public static final int FLAG_POUNCING = 16;
/*      */   
/*      */   private static final int FLAG_SLEEPING = 32;
/*      */   
/*      */   private static final int FLAG_FACEPLANTED = 64;
/*      */   
/*      */   private static final int FLAG_DEFENDING = 128;
/*      */   
/*  127 */   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*      */   
/*  128 */   private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*      */   
/*      */   private static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*      */   
/*      */   private static final Predicate<Entity> TRUSTED_TARGET_SELECTOR;
/*      */   
/*      */   private static final Predicate<Entity> STALKABLE_PREY;
/*      */   
/*      */   private static final Predicate<Entity> AVOID_PLAYERS;
/*      */   
/*      */   private static final int MIN_TICKS_BEFORE_EAT = 600;
/*      */   
/*      */   static {
/*  130 */     ALLOWED_ITEMS = (e -> (!e.hasPickUpDelay() && e.isAlive()));
/*  132 */     TRUSTED_TARGET_SELECTOR = (entity -> {
/*      */         if (entity instanceof LivingEntity) {
/*      */           LivingEntity livingEntity = (LivingEntity)entity;
/*  134 */           return (livingEntity.getLastHurtMob() != null && livingEntity.getLastHurtMobTimestamp() < livingEntity.tickCount + 600);
/*      */         } 
/*      */         return false;
/*      */       });
/*  139 */     STALKABLE_PREY = (entity -> (entity instanceof net.minecraft.world.entity.animal.chicken.Chicken || entity instanceof net.minecraft.world.entity.animal.rabbit.Rabbit));
/*  141 */     AVOID_PLAYERS = (entity -> (!entity.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)));
/*      */   }
/*      */   
/*  145 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.FOX.getDimensions().scale(0.6F)
/*  146 */     .withEyeHeight(0.34375F)
/*  147 */     .withAttachments(EntityAttachments.builder()
/*  148 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.375F, 0.0F));
/*      */   
/*  150 */   private static final Codec<List<EntityReference<LivingEntity>>> TRUSTED_LIST_CODEC = EntityReference.codec().listOf();
/*      */   
/*      */   private static final boolean DEFAULT_SLEEPING = false;
/*      */   
/*      */   private static final boolean DEFAULT_SITTING = false;
/*      */   
/*      */   private static final boolean DEFAULT_CROUCHING = false;
/*      */   
/*      */   private Goal landTargetGoal;
/*      */   
/*      */   private Goal turtleEggTargetGoal;
/*      */   
/*      */   private Goal fishTargetGoal;
/*      */   
/*      */   private float interestedAngle;
/*      */   
/*      */   private float interestedAngleO;
/*      */   
/*      */   private float crouchAmount;
/*      */   
/*      */   private float crouchAmountO;
/*      */   
/*      */   private static final float MAX_CROUCH_AMOUNT = 5.0F;
/*      */   
/*      */   private int ticksSinceEaten;
/*      */   
/*      */   public enum Variant implements StringRepresentable {
/*  171 */     RED(0, "red"),
/*  172 */     SNOW(1, "snow");
/*      */     
/*  174 */     public static final Variant DEFAULT = RED;
/*      */     
/*  176 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*      */     
/*  178 */     private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*      */     
/*  180 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*      */     
/*      */     private final int id;
/*      */     
/*      */     private final String name;
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */     
/*      */     Variant(int id, String name) {
/*  186 */       this.id = id;
/*  187 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/*  192 */       return this.name;
/*      */     }
/*      */     
/*      */     public int getId() {
/*  196 */       return this.id;
/*      */     }
/*      */     
/*      */     public static Variant byId(int id) {
/*  200 */       return BY_ID.apply(id);
/*      */     }
/*      */     
/*      */     public static Variant byBiome(Holder<Biome> biome) {
/*  204 */       return biome.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED;
/*      */     }
/*      */   }
/*      */   
/*      */   public Fox(EntityType<? extends Fox> type, Level level) {
/*  209 */     super(type, level);
/*  211 */     this.lookControl = new FoxLookControl();
/*  212 */     this.moveControl = new FoxMoveControl<>(this);
/*  214 */     setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 0.0F);
/*  215 */     setPathfindingMalus(PathType.DAMAGING, 0.0F);
/*  217 */     setCanPickUpLoot(true);
/*  219 */     getNavigation().setRequiredPathLength(32.0F);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  224 */     super.defineSynchedData(entityData);
/*  225 */     entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
/*  226 */     entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
/*  227 */     entityData.define(DATA_TYPE_ID, Integer.valueOf(Variant.DEFAULT.getId()));
/*  228 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */   protected void registerGoals() {
/*  233 */     this.landTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, Animal.class, 10, false, false, (target, level) -> (target instanceof net.minecraft.world.entity.animal.chicken.Chicken || target instanceof net.minecraft.world.entity.animal.rabbit.Rabbit));
/*  234 */     this.turtleEggTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR);
/*  235 */     this.fishTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractFish.class, 20, false, false, (target, level) -> target instanceof net.minecraft.world.entity.animal.fish.AbstractSchoolingFish);
/*  237 */     this.goalSelector.addGoal(0, (Goal)new FoxFloatGoal());
/*  238 */     this.goalSelector.addGoal(0, (Goal)new ClimbOnTopOfPowderSnowGoal((Mob)this, level()));
/*  239 */     this.goalSelector.addGoal(1, new FaceplantGoal());
/*  240 */     this.goalSelector.addGoal(2, (Goal)new FoxPanicGoal(2.2D));
/*  241 */     this.goalSelector.addGoal(3, (Goal)new FoxBreedGoal(this, 1.0D));
/*  242 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 16.0F, 1.6D, 1.4D, entity -> (AVOID_PLAYERS.test(entity) && !trusts(entity) && !isDefending())));
/*  243 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Wolf.class, 8.0F, 1.6D, 1.4D, entity -> (!((Wolf)entity).isTame() && !isDefending())));
/*  244 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, PolarBear.class, 8.0F, 1.6D, 1.4D, entity -> !isDefending()));
/*  245 */     this.goalSelector.addGoal(5, new StalkPreyGoal());
/*  246 */     this.goalSelector.addGoal(6, (Goal)new FoxPounceGoal());
/*  247 */     this.goalSelector.addGoal(6, (Goal)new SeekShelterGoal(1.25D));
/*  248 */     this.goalSelector.addGoal(7, (Goal)new FoxMeleeAttackGoal(1.2000000476837158D, true));
/*  249 */     this.goalSelector.addGoal(7, new SleepGoal());
/*  250 */     this.goalSelector.addGoal(8, (Goal)new FoxFollowParentGoal(this, 1.25D));
/*  251 */     this.goalSelector.addGoal(9, (Goal)new FoxStrollThroughVillageGoal(32, 200));
/*  252 */     this.goalSelector.addGoal(10, (Goal)new FoxEatBerriesGoal(1.2000000476837158D, 12, 1));
/*  253 */     this.goalSelector.addGoal(10, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  254 */     this.goalSelector.addGoal(11, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  255 */     this.goalSelector.addGoal(11, new FoxSearchForItemsGoal());
/*  256 */     this.goalSelector.addGoal(12, (Goal)new FoxLookAtPlayerGoal((Mob)this, (Class)Player.class, 24.0F));
/*  257 */     this.goalSelector.addGoal(13, new PerchAndSearchGoal());
/*  259 */     this.targetSelector.addGoal(3, (Goal)new DefendTrustedTargetGoal(LivingEntity.class, false, false, (target, level) -> (TRUSTED_TARGET_SELECTOR.test(target) && !trusts(target))));
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  264 */     if (!level().isClientSide() && isAlive() && isEffectiveAi()) {
/*  266 */       this.ticksSinceEaten++;
/*  267 */       ItemStack itemInMouth = getItemBySlot(EquipmentSlot.MAINHAND);
/*  268 */       if (canEat(itemInMouth))
/*  269 */         if (this.ticksSinceEaten > 600) {
/*  270 */           ItemStack remainingFood = itemInMouth.finishUsingItem(level(), (LivingEntity)this);
/*  271 */           if (!remainingFood.isEmpty())
/*  272 */             setItemSlot(EquipmentSlot.MAINHAND, remainingFood); 
/*  274 */           this.ticksSinceEaten = 0;
/*  275 */         } else if (this.ticksSinceEaten > 560 && 
/*  276 */           this.random.nextFloat() < 0.1F) {
/*  277 */           playEatingSound();
/*  278 */           level().broadcastEntityEvent((Entity)this, (byte)45);
/*      */         }  
/*  284 */       LivingEntity target = getTarget();
/*  285 */       if (target == null || !target.isAlive()) {
/*  286 */         setIsCrouching(false);
/*  287 */         setIsInterested(false);
/*      */       } 
/*      */     } 
/*  291 */     if (isSleeping() || isImmobile()) {
/*  292 */       this.jumping = false;
/*  293 */       this.xxa = 0.0F;
/*  294 */       this.zza = 0.0F;
/*      */     } 
/*  297 */     super.aiStep();
/*  299 */     if (isDefending() && this.random.nextFloat() < 0.05F)
/*  300 */       playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F); 
/*      */   }
/*      */   
/*      */   protected boolean isImmobile() {
/*  306 */     return isDeadOrDying();
/*      */   }
/*      */   
/*      */   private boolean canEat(ItemStack itemInMouth) {
/*  310 */     return (isConsumableFood(itemInMouth) && getTarget() == null && onGround() && !isSleeping());
/*      */   }
/*      */   
/*      */   private boolean isConsumableFood(ItemStack itemStack) {
/*  314 */     return (itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE));
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/*  319 */     if (random.nextFloat() < 0.2F) {
/*      */       ItemStack heldInMouth;
/*  320 */       float odds = random.nextFloat();
/*  322 */       if (odds < 0.05F) {
/*  323 */         heldInMouth = new ItemStack((ItemLike)Items.EMERALD);
/*  324 */       } else if (odds < 0.2F) {
/*  325 */         heldInMouth = new ItemStack((ItemLike)Items.EGG);
/*  326 */       } else if (odds < 0.4F) {
/*  327 */         heldInMouth = random.nextBoolean() ? new ItemStack((ItemLike)Items.RABBIT_FOOT) : new ItemStack((ItemLike)Items.RABBIT_HIDE);
/*  328 */       } else if (odds < 0.6F) {
/*  329 */         heldInMouth = new ItemStack((ItemLike)Items.WHEAT);
/*  330 */       } else if (odds < 0.8F) {
/*  331 */         heldInMouth = new ItemStack((ItemLike)Items.LEATHER);
/*      */       } else {
/*  333 */         heldInMouth = new ItemStack((ItemLike)Items.FEATHER);
/*      */       } 
/*  335 */       setItemSlot(EquipmentSlot.MAINHAND, heldInMouth);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  341 */     if (id == 45) {
/*  342 */       ItemStack mouthItem = getItemBySlot(EquipmentSlot.MAINHAND);
/*  343 */       if (!mouthItem.isEmpty()) {
/*  344 */         ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(mouthItem));
/*  345 */         for (int i = 0; i < 8; i++) {
/*  348 */           Vec3 direction = (new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, 0.0D)).xRot(-getXRot() * 0.017453292F).yRot(-getYRot() * 0.017453292F);
/*  350 */           level().addParticle((ParticleOptions)breakParticle, getX() + (getLookAngle()).x / 2.0D, getY(), getZ() + (getLookAngle()).z / 2.0D, direction.x, direction.y + 0.05D, direction.z);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  354 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  359 */     return Animal.createAnimalAttributes()
/*  360 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  361 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  362 */       .add(Attributes.ATTACK_DAMAGE, 2.0D)
/*  363 */       .add(Attributes.SAFE_FALL_DISTANCE, 5.0D)
/*  364 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*      */   }
/*      */   
/*      */   public Fox getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  369 */     Fox baby = (Fox)EntityTypes.FOX.create((Level)level, EntitySpawnReason.BREEDING);
/*  370 */     if (baby != null)
/*  371 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : ((Fox)partner).getVariant()); 
/*  373 */     return baby;
/*      */   }
/*      */   
/*      */   public static boolean checkFoxSpawnRules(EntityType<Fox> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  377 */     return (level.getBlockState(pos.below()).is(BlockTags.FOXES_SPAWNABLE_ON) && 
/*  378 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     FoxGroupData foxGroupData;
/*  383 */     Holder<Biome> biome = level.getBiome(blockPosition());
/*  384 */     Variant variant = Variant.byBiome(biome);
/*  385 */     boolean isBaby = false;
/*  386 */     if (groupData instanceof FoxGroupData) {
/*  386 */       FoxGroupData foxGroupData1 = (FoxGroupData)groupData;
/*  388 */       variant = foxGroupData1.variant;
/*  389 */       if (foxGroupData1.getGroupSize() >= 2)
/*  390 */         isBaby = true; 
/*      */     } else {
/*  393 */       foxGroupData = new FoxGroupData(variant);
/*      */     } 
/*  396 */     setVariant(variant);
/*  397 */     if (isBaby)
/*  398 */       setAge(-24000); 
/*  401 */     if (level instanceof ServerLevel)
/*  402 */       setTargetGoals(); 
/*  405 */     populateDefaultEquipmentSlots(level.getRandom(), difficulty);
/*  407 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)foxGroupData);
/*      */   }
/*      */   
/*      */   private void setTargetGoals() {
/*  412 */     if (getVariant() == Variant.RED) {
/*  413 */       this.targetSelector.addGoal(4, this.landTargetGoal);
/*  414 */       this.targetSelector.addGoal(4, this.turtleEggTargetGoal);
/*  415 */       this.targetSelector.addGoal(6, this.fishTargetGoal);
/*      */     } else {
/*  417 */       this.targetSelector.addGoal(4, this.fishTargetGoal);
/*  418 */       this.targetSelector.addGoal(6, this.landTargetGoal);
/*  419 */       this.targetSelector.addGoal(6, this.turtleEggTargetGoal);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void playEatingSound() {
/*  425 */     playSound(SoundEvents.FOX_EAT, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public EntityDimensions getDefaultDimensions(Pose pose) {
/*  430 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*      */   }
/*      */   
/*      */   public Variant getVariant() {
/*  434 */     return Variant.byId(((Integer)this.entityData.get(DATA_TYPE_ID)).intValue());
/*      */   }
/*      */   
/*      */   private void setVariant(Variant variant) {
/*  438 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(variant.getId()));
/*      */   }
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/*  443 */     if (type == DataComponents.FOX_VARIANT)
/*  444 */       return (T)castComponentValue(type, getVariant()); 
/*  447 */     return (T)super.get(type);
/*      */   }
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  452 */     applyImplicitComponentIfPresent(components, DataComponents.FOX_VARIANT);
/*  453 */     super.applyImplicitComponents(components);
/*      */   }
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/*  458 */     if (type == DataComponents.FOX_VARIANT) {
/*  459 */       setVariant((Variant)castComponentValue(DataComponents.FOX_VARIANT, value));
/*  460 */       return true;
/*      */     } 
/*  463 */     return super.applyImplicitComponent(type, value);
/*      */   }
/*      */   
/*      */   private Stream<EntityReference<LivingEntity>> getTrustedEntities() {
/*  467 */     return Stream.concat(((Optional<? extends EntityReference<LivingEntity>>)this.entityData.get(DATA_TRUSTED_ID_0)).stream(), ((Optional<? extends EntityReference<LivingEntity>>)this.entityData.get(DATA_TRUSTED_ID_1)).stream());
/*      */   }
/*      */   
/*      */   private void addTrustedEntity(LivingEntity entity) {
/*  471 */     addTrustedEntity(EntityReference.of((UniquelyIdentifyable)entity));
/*      */   }
/*      */   
/*      */   private void addTrustedEntity(EntityReference<LivingEntity> reference) {
/*  475 */     if (((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).isPresent()) {
/*  477 */       this.entityData.set(DATA_TRUSTED_ID_1, Optional.of(reference));
/*      */     } else {
/*  479 */       this.entityData.set(DATA_TRUSTED_ID_0, Optional.of(reference));
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  485 */     super.addAdditionalSaveData(output);
/*  486 */     output.store("Trusted", TRUSTED_LIST_CODEC, getTrustedEntities().toList());
/*  487 */     output.putBoolean("Sleeping", isSleeping());
/*  488 */     output.store("Type", Variant.CODEC, getVariant());
/*  489 */     output.putBoolean("Sitting", isSitting());
/*  490 */     output.putBoolean("Crouching", isCrouching());
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  495 */     super.readAdditionalSaveData(input);
/*  497 */     clearTrusted();
/*  498 */     ((List)input.read("Trusted", TRUSTED_LIST_CODEC).orElse(List.of()))
/*  499 */       .forEach(this::addTrustedEntity);
/*  501 */     setSleeping(input.getBooleanOr("Sleeping", false));
/*  502 */     setVariant(input.read("Type", Variant.CODEC).orElse(Variant.DEFAULT));
/*  503 */     setSitting(input.getBooleanOr("Sitting", false));
/*  504 */     setIsCrouching(input.getBooleanOr("Crouching", false));
/*  507 */     if (level() instanceof ServerLevel)
/*  508 */       setTargetGoals(); 
/*      */   }
/*      */   
/*      */   private void clearTrusted() {
/*  513 */     this.entityData.set(DATA_TRUSTED_ID_0, Optional.empty());
/*  514 */     this.entityData.set(DATA_TRUSTED_ID_1, Optional.empty());
/*      */   }
/*      */   
/*      */   public boolean isSitting() {
/*  518 */     return getFlag(1);
/*      */   }
/*      */   
/*      */   public void setSitting(boolean value) {
/*  522 */     setFlag(1, value);
/*      */   }
/*      */   
/*      */   public boolean isFaceplanted() {
/*  526 */     return getFlag(64);
/*      */   }
/*      */   
/*      */   private void setFaceplanted(boolean faceplanted) {
/*  530 */     setFlag(64, faceplanted);
/*      */   }
/*      */   
/*      */   private boolean isDefending() {
/*  534 */     return getFlag(128);
/*      */   }
/*      */   
/*      */   private void setDefending(boolean defending) {
/*  538 */     setFlag(128, defending);
/*      */   }
/*      */   
/*      */   public boolean isSleeping() {
/*  543 */     return getFlag(32);
/*      */   }
/*      */   
/*      */   private void setSleeping(boolean sleeping) {
/*  547 */     setFlag(32, sleeping);
/*      */   }
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  551 */     if (value) {
/*  552 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | flag)));
/*      */     } else {
/*  554 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getFlag(int flag) {
/*  559 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & flag) != 0);
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/*  564 */     return (slot == EquipmentSlot.MAINHAND && canPickUpLoot());
/*      */   }
/*      */   
/*      */   public boolean canHoldItem(ItemStack itemStack) {
/*  569 */     ItemStack heldItemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/*  571 */     return (heldItemStack.isEmpty() || (this.ticksSinceEaten > 0 && isConsumableFood(itemStack) && !isConsumableFood(heldItemStack)));
/*      */   }
/*      */   
/*      */   private void spitOutItem(ItemStack itemStack) {
/*  575 */     if (itemStack.isEmpty() || level().isClientSide())
/*      */       return; 
/*  579 */     ItemEntity thrownItem = new ItemEntity(level(), getX() + (getLookAngle()).x, getY() + 1.0D, getZ() + (getLookAngle()).z, itemStack);
/*  580 */     thrownItem.setPickUpDelay(40);
/*  581 */     thrownItem.setThrower((Entity)this);
/*  583 */     playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
/*  584 */     level().addFreshEntity((Entity)thrownItem);
/*      */   }
/*      */   
/*      */   private void dropItemStack(ItemStack itemStack) {
/*  588 */     ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), itemStack);
/*  589 */     level().addFreshEntity((Entity)itemEntity);
/*      */   }
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  594 */     ItemStack itemStack = entity.getItem();
/*  595 */     if (canHoldItem(itemStack)) {
/*  596 */       int count = itemStack.getCount();
/*  597 */       if (count > 1)
/*  598 */         dropItemStack(itemStack.split(count - 1)); 
/*  601 */       spitOutItem(getItemBySlot(EquipmentSlot.MAINHAND));
/*  603 */       onItemPickup(entity);
/*  605 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1));
/*  606 */       setGuaranteedDrop(EquipmentSlot.MAINHAND);
/*  607 */       take((Entity)entity, itemStack.getCount());
/*  608 */       entity.discard();
/*  609 */       this.ticksSinceEaten = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void tick() {
/*  615 */     super.tick();
/*  617 */     if (isEffectiveAi()) {
/*  618 */       boolean inWater = isInWater();
/*  619 */       if (inWater || getTarget() != null || level().isThundering())
/*  620 */         wakeUp(); 
/*  623 */       if (inWater || isSleeping())
/*  624 */         setSitting(false); 
/*  627 */       if (isFaceplanted() && level().getRandom().nextFloat() < 0.2F) {
/*  628 */         BlockPos pos = blockPosition();
/*  629 */         BlockState state = level().getBlockState(pos);
/*  630 */         level().levelEvent(2001, pos, Block.getId(state));
/*      */       } 
/*      */     } 
/*  634 */     this.interestedAngleO = this.interestedAngle;
/*  635 */     if (isInterested()) {
/*  636 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*      */     } else {
/*  638 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*      */     } 
/*  641 */     this.crouchAmountO = this.crouchAmount;
/*  642 */     if (isCrouching()) {
/*  643 */       this.crouchAmount += 0.2F;
/*  644 */       if (this.crouchAmount > 5.0F)
/*  645 */         this.crouchAmount = 5.0F; 
/*      */     } else {
/*  648 */       this.crouchAmount = 0.0F;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  654 */     return itemStack.is(ItemTags.FOX_FOOD);
/*      */   }
/*      */   
/*      */   protected void onOffspringSpawnedFromEgg(Player spawner, Mob offspring) {
/*  659 */     ((Fox)offspring).addTrustedEntity((LivingEntity)spawner);
/*      */   }
/*      */   
/*      */   public boolean isPouncing() {
/*  663 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public void setIsPouncing(boolean pouncing) {
/*  667 */     setFlag(16, pouncing);
/*      */   }
/*      */   
/*      */   public boolean isFullyCrouched() {
/*  671 */     return (this.crouchAmount == 5.0F);
/*      */   }
/*      */   
/*      */   public void setIsCrouching(boolean isCrouching) {
/*  675 */     setFlag(4, isCrouching);
/*      */   }
/*      */   
/*      */   public boolean isCrouching() {
/*  680 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void setIsInterested(boolean value) {
/*  684 */     setFlag(8, value);
/*      */   }
/*      */   
/*      */   public boolean isInterested() {
/*  688 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public float getHeadRollAngle(float a) {
/*  692 */     return Mth.lerp(a, this.interestedAngleO, this.interestedAngle) * 0.11F * 3.1415927F;
/*      */   }
/*      */   
/*      */   public float getCrouchAmount(float a) {
/*  696 */     return Mth.lerp(a, this.crouchAmountO, this.crouchAmount);
/*      */   }
/*      */   
/*      */   public void setTarget(LivingEntity target) {
/*  701 */     if (isDefending() && target == null)
/*  702 */       setDefending(false); 
/*  704 */     super.setTarget(target);
/*      */   }
/*      */   
/*      */   private void wakeUp() {
/*  708 */     setSleeping(false);
/*      */   }
/*      */   
/*      */   private void clearStates() {
/*  712 */     setIsInterested(false);
/*  713 */     setIsCrouching(false);
/*  714 */     setSitting(false);
/*  715 */     setSleeping(false);
/*  716 */     setDefending(false);
/*  717 */     setFaceplanted(false);
/*      */   }
/*      */   
/*      */   public boolean canMove() {
/*  721 */     return (!isSleeping() && !isSitting() && !isFaceplanted());
/*      */   }
/*      */   
/*      */   public void playAmbientSound() {
/*  726 */     SoundEvent ambient = getAmbientSound();
/*  728 */     if (ambient == SoundEvents.FOX_SCREECH) {
/*  729 */       playSound(ambient, 2.0F, getVoicePitch());
/*      */     } else {
/*  731 */       super.playAmbientSound();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  737 */     if (isSleeping())
/*  738 */       return SoundEvents.FOX_SLEEP; 
/*  740 */     if (!level().isBrightOutside() && this.random.nextFloat() < 0.1F) {
/*  741 */       List<Player> nearbyEntities = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
/*  742 */       if (nearbyEntities.isEmpty())
/*  743 */         return SoundEvents.FOX_SCREECH; 
/*      */     } 
/*  746 */     return SoundEvents.FOX_AMBIENT;
/*      */   }
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  751 */     return SoundEvents.FOX_HURT;
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  756 */     return SoundEvents.FOX_DEATH;
/*      */   }
/*      */   
/*      */   private boolean trusts(LivingEntity entity) {
/*  760 */     return getTrustedEntities().anyMatch(trusted -> trusted.matches((UniquelyIdentifyable)entity));
/*      */   }
/*      */   
/*      */   protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
/*  765 */     ItemStack itemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/*  767 */     if (!itemStack.isEmpty()) {
/*  768 */       spawnAtLocation(level, itemStack);
/*  769 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*      */     } 
/*  772 */     super.dropAllDeathLoot(level, source);
/*      */   }
/*      */   
/*      */   public static boolean isPathClear(Fox fox, LivingEntity target) {
/*  776 */     double zdiff = target.getZ() - fox.getZ();
/*  777 */     double xdiff = target.getX() - fox.getX();
/*  778 */     double slope = zdiff / xdiff;
/*  780 */     int increments = 6;
/*  781 */     for (int i = 0; i < 6; i++) {
/*  782 */       double z = (slope == 0.0D) ? 0.0D : (zdiff * (i / 6.0F));
/*  783 */       double x = (slope == 0.0D) ? (xdiff * (i / 6.0F)) : (z / slope);
/*  784 */       for (int j = 1; j < 4; j++) {
/*  785 */         if (!fox.level().getBlockState(BlockPos.containing(fox.getX() + x, fox.getY() + j, fox.getZ() + z)).canBeReplaced())
/*  786 */           return false; 
/*      */       } 
/*      */     } 
/*  791 */     return true;
/*      */   }
/*      */   
/*      */   private class FoxSearchForItemsGoal extends Goal {
/*      */     public FoxSearchForItemsGoal() {
/*  796 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  801 */       if (!Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/*  802 */         return false; 
/*  805 */       if (Fox.this.getTarget() != null || Fox.this.getLastHurtByMob() != null)
/*  806 */         return false; 
/*  809 */       if (!Fox.this.canMove())
/*  810 */         return false; 
/*  813 */       if (Fox.this.getRandom().nextInt(reducedTickDelay(10)) != 0)
/*  814 */         return false; 
/*  816 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  817 */       return (!items.isEmpty() && Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*      */     }
/*      */     
/*      */     public void tick() {
/*  822 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  823 */       ItemStack itemStack = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/*  825 */       if (itemStack.isEmpty() && !items.isEmpty())
/*  826 */         Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D); 
/*      */     }
/*      */     
/*      */     public void start() {
/*  832 */       List<ItemEntity> items = Fox.this.level().getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  833 */       if (!items.isEmpty())
/*  834 */         Fox.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FoxMoveControl<T extends Fox> extends MoveControl<T> {
/*      */     public FoxMoveControl(T fox) {
/*  841 */       super((Mob)fox);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  846 */       if (((Fox)this.mob).canMove())
/*  847 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class StalkPreyGoal extends Goal {
/*      */     public StalkPreyGoal() {
/*  854 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  859 */       if (Fox.this.isSleeping())
/*  860 */         return false; 
/*  863 */       LivingEntity target = Fox.this.getTarget();
/*  864 */       return (target != null && target.isAlive() && Fox.STALKABLE_PREY.test(target) && Fox.this.distanceToSqr((Entity)target) > 36.0D && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.jumping);
/*      */     }
/*      */     
/*      */     public void start() {
/*  869 */       Fox.this.setSitting(false);
/*  870 */       Fox.this.setFaceplanted(false);
/*      */     }
/*      */     
/*      */     public void stop() {
/*  876 */       LivingEntity target = Fox.this.getTarget();
/*  877 */       if (target != null && Fox.isPathClear(Fox.this, target)) {
/*  878 */         Fox.this.setIsInterested(true);
/*  879 */         Fox.this.setIsCrouching(true);
/*  880 */         Fox.this.getNavigation().stop();
/*  881 */         Fox.this.getLookControl().setLookAt((Entity)target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */       } else {
/*  883 */         Fox.this.setIsInterested(false);
/*  884 */         Fox.this.setIsCrouching(false);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void tick() {
/*  890 */       LivingEntity target = Fox.this.getTarget();
/*  891 */       if (target == null)
/*      */         return; 
/*  894 */       Fox.this.getLookControl().setLookAt((Entity)target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*  895 */       if (Fox.this.distanceToSqr((Entity)target) <= 36.0D) {
/*  896 */         Fox.this.setIsInterested(true);
/*  897 */         Fox.this.setIsCrouching(true);
/*  898 */         Fox.this.getNavigation().stop();
/*      */       } else {
/*  900 */         Fox.this.getNavigation().moveTo((Entity)target, 1.5D);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxMeleeAttackGoal extends MeleeAttackGoal {
/*      */     public FoxMeleeAttackGoal(double speedModifier, boolean trackTarget) {
/*  907 */       super((PathfinderMob)Fox.this, speedModifier, trackTarget);
/*      */     }
/*      */     
/*      */     protected void checkAndPerformAttack(LivingEntity target) {
/*  912 */       if (canPerformAttack(target)) {
/*  913 */         resetAttackCooldown();
/*  914 */         this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)target);
/*  915 */         Fox.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void start() {
/*  921 */       Fox.this.setIsInterested(false);
/*  922 */       super.start();
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  927 */       return (!Fox.this.isSitting() && !Fox.this.isSleeping() && !Fox.this.isCrouching() && !Fox.this.isFaceplanted() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxBreedGoal extends BreedGoal {
/*      */     public FoxBreedGoal(Fox this$0, double speedModifier) {
/*  933 */       super(this$0, speedModifier);
/*      */     }
/*      */     
/*      */     public void start() {
/*  938 */       ((Fox)this.animal).clearStates();
/*  939 */       ((Fox)this.partner).clearStates();
/*  940 */       super.start();
/*      */     }
/*      */     
/*      */     protected void breed() {
/*  945 */       Fox offspring = (Fox)this.animal.getBreedOffspring(this.level, (AgeableMob)this.partner);
/*  946 */       if (offspring == null)
/*      */         return; 
/*  950 */       ServerPlayer animalLoveCause = this.animal.getLoveCause();
/*  951 */       ServerPlayer partnerLoveCause = this.partner.getLoveCause();
/*  952 */       ServerPlayer loveCause = animalLoveCause;
/*  954 */       if (animalLoveCause != null) {
/*  955 */         offspring.addTrustedEntity((LivingEntity)animalLoveCause);
/*      */       } else {
/*  957 */         loveCause = partnerLoveCause;
/*      */       } 
/*  960 */       if (partnerLoveCause != null && animalLoveCause != partnerLoveCause)
/*  961 */         offspring.addTrustedEntity((LivingEntity)partnerLoveCause); 
/*  964 */       if (loveCause != null) {
/*  965 */         loveCause.awardStat(Stats.ANIMALS_BRED);
/*  966 */         CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, (AgeableMob)offspring);
/*      */       } 
/*  969 */       this.animal.setAge(6000);
/*  970 */       this.partner.setAge(6000);
/*  971 */       this.animal.resetLove();
/*  972 */       this.partner.resetLove();
/*  973 */       offspring.setAge(-24000);
/*  974 */       offspring.snapTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
/*  975 */       this.level.addFreshEntityWithPassengers((Entity)offspring);
/*  977 */       this.level.broadcastEntityEvent((Entity)this.animal, (byte)18);
/*  979 */       if (((Boolean)this.level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/*  980 */         this.level.addFreshEntity((Entity)new ExperienceOrb((Level)this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1)); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class DefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*      */     private LivingEntity trustedLastHurtBy;
/*      */     
/*      */     private LivingEntity trustedLastHurt;
/*      */     
/*      */     private int timestamp;
/*      */     
/*      */     public DefendTrustedTargetGoal(Class<LivingEntity> targetType, boolean mustSee, boolean mustReach, TargetingConditions.Selector subselector) {
/*  991 */       super((Mob)Fox.this, targetType, 10, mustSee, mustReach, subselector);
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  996 */       if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0)
/*  997 */         return false; 
/* 1000 */       ServerLevel level = getServerLevel(Fox.this.level());
/* 1001 */       for (EntityReference<LivingEntity> trustedReference : Fox.this.getTrustedEntities().toList()) {
/* 1002 */         LivingEntity trustedEntity = (LivingEntity)trustedReference.getEntity((Level)level, LivingEntity.class);
/* 1003 */         if (trustedEntity != null) {
/* 1004 */           this.trustedLastHurt = trustedEntity;
/* 1005 */           this.trustedLastHurtBy = trustedEntity.getLastHurtByMob();
/* 1006 */           int timestamp = trustedEntity.getLastHurtByMobTimestamp();
/* 1007 */           return (timestamp != this.timestamp && canAttack(this.trustedLastHurtBy, this.targetConditions));
/*      */         } 
/*      */       } 
/* 1010 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1015 */       setTarget(this.trustedLastHurtBy);
/* 1016 */       this.target = this.trustedLastHurtBy;
/* 1018 */       if (this.trustedLastHurt != null)
/* 1019 */         this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp(); 
/* 1022 */       Fox.this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/* 1024 */       Fox.this.setDefending(true);
/* 1027 */       Fox.this.wakeUp();
/* 1029 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SeekShelterGoal extends FleeSunGoal {
/*      */     private int interval;
/*      */     
/*      */     public SeekShelterGoal(double speedModifier) {
/* 1037 */       super((PathfinderMob)Fox.this, speedModifier);
/* 1038 */       this.interval = reducedTickDelay(100);
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1043 */       if (Fox.this.isSleeping() || this.mob.getTarget() != null)
/* 1044 */         return false; 
/* 1046 */       if (Fox.this.level().isThundering() && Fox.this.level().canSeeSky(this.mob.blockPosition()))
/* 1047 */         return setWantedPos(); 
/* 1049 */       if (this.interval > 0) {
/* 1050 */         this.interval--;
/* 1051 */         return false;
/*      */       } 
/* 1053 */       this.interval = 100;
/* 1055 */       BlockPos pos = this.mob.blockPosition();
/* 1057 */       return (Fox.this.level().isBrightOutside() && Fox.this
/* 1058 */         .level().canSeeSky(pos) && 
/* 1059 */         !((ServerLevel)Fox.this.level()).isVillage(pos) && 
/* 1060 */         setWantedPos());
/*      */     }
/*      */     
/*      */     public void start() {
/* 1065 */       Fox.this.clearStates();
/* 1066 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxAlertableEntitiesSelector implements TargetingConditions.Selector {
/*      */     public boolean test(LivingEntity target, ServerLevel level) {
/* 1073 */       if (target instanceof Fox)
/* 1074 */         return false; 
/* 1078 */       if (target instanceof net.minecraft.world.entity.animal.chicken.Chicken || target instanceof net.minecraft.world.entity.animal.rabbit.Rabbit || target instanceof net.minecraft.world.entity.monster.Monster)
/* 1079 */         return true; 
/* 1083 */       if (target instanceof TamableAnimal) {
/* 1083 */         TamableAnimal tamableAnimal = (TamableAnimal)target;
/* 1084 */         return !tamableAnimal.isTame();
/*      */       } 
/* 1088 */       if (target instanceof Player) {
/* 1088 */         Player player = (Player)target;
/* 1088 */         if (player.isSpectator() || player.isCreative())
/* 1089 */           return false; 
/*      */       } 
/* 1093 */       if (Fox.this.trusts(target))
/* 1094 */         return false; 
/* 1098 */       return (!target.isSleeping() && !target.isDiscrete());
/*      */     }
/*      */   }
/*      */   
/*      */   private abstract class FoxBehaviorGoal extends Goal {
/*      */     private final TargetingConditions alertableTargeting;
/*      */     
/*      */     private FoxBehaviorGoal() {
/* 1103 */       this.alertableTargeting = TargetingConditions.forCombat().range(12.0D).ignoreLineOfSight().selector(new Fox.FoxAlertableEntitiesSelector());
/*      */     }
/*      */     
/*      */     protected boolean hasShelter() {
/* 1106 */       BlockPos foxPos = BlockPos.containing(Fox.this.getX(), (Fox.this.getBoundingBox()).maxY, Fox.this.getZ());
/* 1107 */       return (!Fox.this.level().canSeeSky(foxPos) && Fox.this.getWalkTargetValue(foxPos) >= 0.0F);
/*      */     }
/*      */     
/*      */     protected boolean alertable() {
/* 1111 */       return !getServerLevel(Fox.this.level()).getNearbyEntities(LivingEntity.class, this.alertableTargeting, (LivingEntity)Fox.this, Fox.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SleepGoal extends FoxBehaviorGoal {
/* 1116 */     private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
/*      */     
/*      */     private int countdown;
/*      */     
/*      */     public SleepGoal() {
/* 1120 */       this.countdown = Fox.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1121 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1126 */       if (Fox.this.xxa != 0.0F || Fox.this.yya != 0.0F || Fox.this.zza != 0.0F)
/* 1127 */         return false; 
/* 1129 */       return (canSleep() || Fox.this.isSleeping());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1134 */       return canSleep();
/*      */     }
/*      */     
/*      */     private boolean canSleep() {
/* 1138 */       if (this.countdown > 0) {
/* 1139 */         this.countdown--;
/* 1140 */         return false;
/*      */       } 
/* 1142 */       return (Fox.this.level().isBrightOutside() && hasShelter() && !alertable() && !Fox.this.isInPowderSnow);
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1147 */       this.countdown = Fox.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1148 */       Fox.this.clearStates();
/*      */     }
/*      */     
/*      */     public void start() {
/* 1153 */       Fox.this.setSitting(false);
/* 1154 */       Fox.this.setIsCrouching(false);
/* 1155 */       Fox.this.setIsInterested(false);
/* 1156 */       Fox.this.setJumping(false);
/* 1157 */       Fox.this.setSleeping(true);
/* 1158 */       Fox.this.getNavigation().stop();
/* 1159 */       Fox.this.getMoveControl().setWantedPosition(Fox.this.getX(), Fox.this.getY(), Fox.this.getZ(), 0.0D);
/*      */     }
/*      */   }
/*      */   
/*      */   private class PerchAndSearchGoal extends FoxBehaviorGoal {
/*      */     private double relX;
/*      */     
/*      */     private double relZ;
/*      */     
/*      */     private int lookTime;
/*      */     
/*      */     private int looksRemaining;
/*      */     
/*      */     public PerchAndSearchGoal() {
/* 1170 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1175 */       return (Fox.this.getLastHurtByMob() == null && Fox.this.getRandom().nextFloat() < 0.02F && !Fox.this.isSleeping() && Fox.this.getTarget() == null && Fox.this.getNavigation().isDone() && !alertable() && !Fox.this.isPouncing() && !Fox.this.isCrouching());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1180 */       return (this.looksRemaining > 0);
/*      */     }
/*      */     
/*      */     public void start() {
/* 1185 */       resetLook();
/* 1186 */       this.looksRemaining = 2 + Fox.this.getRandom().nextInt(3);
/* 1187 */       Fox.this.setSitting(true);
/* 1188 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1193 */       Fox.this.setSitting(false);
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1198 */       this.lookTime--;
/* 1199 */       if (this.lookTime <= 0) {
/* 1200 */         this.looksRemaining--;
/* 1201 */         resetLook();
/*      */       } 
/* 1203 */       Fox.this.getLookControl().setLookAt(Fox.this.getX() + this.relX, Fox.this.getEyeY(), Fox.this.getZ() + this.relZ, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */     }
/*      */     
/*      */     private void resetLook() {
/* 1207 */       double rnd = 6.283185307179586D * Fox.this.getRandom().nextDouble();
/* 1208 */       this.relX = Math.cos(rnd);
/* 1209 */       this.relZ = Math.sin(rnd);
/* 1210 */       this.lookTime = adjustedTickDelay(80 + Fox.this.getRandom().nextInt(20));
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxEatBerriesGoal extends MoveToBlockGoal {
/*      */     private static final int WAIT_TICKS = 40;
/*      */     
/*      */     protected int ticksWaited;
/*      */     
/*      */     public FoxEatBerriesGoal(double speedModifier, int searchRange, int verticalSearchRange) {
/* 1220 */       super((PathfinderMob)Fox.this, speedModifier, searchRange, verticalSearchRange);
/*      */     }
/*      */     
/*      */     public double acceptedDistance() {
/* 1225 */       return 2.0D;
/*      */     }
/*      */     
/*      */     public boolean shouldRecalculatePath() {
/* 1230 */       return (this.tryTicks % 100 == 0);
/*      */     }
/*      */     
/*      */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 1235 */       BlockState blockState = level.getBlockState(pos);
/* 1236 */       return ((blockState.is(Blocks.SWEET_BERRY_BUSH) && ((Integer)blockState.getValue((Property)SweetBerryBushBlock.AGE)).intValue() >= 2) || 
/* 1237 */         CaveVines.hasGlowBerries(blockState));
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1242 */       if (isReachedTarget()) {
/* 1243 */         if (this.ticksWaited >= 40) {
/* 1244 */           onReachedTarget();
/*      */         } else {
/* 1246 */           this.ticksWaited++;
/*      */         } 
/* 1248 */       } else if (!isReachedTarget() && Fox.this.random.nextFloat() < 0.05F) {
/* 1249 */         Fox.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
/*      */       } 
/* 1252 */       super.tick();
/*      */     }
/*      */     
/*      */     protected void onReachedTarget() {
/* 1256 */       if (!((Boolean)getServerLevel(Fox.this.level()).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/*      */         return; 
/* 1260 */       BlockState state = Fox.this.level().getBlockState(this.blockPos);
/* 1263 */       if (state.is(Blocks.SWEET_BERRY_BUSH)) {
/* 1264 */         pickSweetBerries(state);
/* 1265 */       } else if (CaveVines.hasGlowBerries(state)) {
/* 1266 */         pickGlowBerry(state);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void pickGlowBerry(BlockState state) {
/* 1271 */       CaveVines.use((Entity)Fox.this, state, Fox.this.level(), this.blockPos);
/*      */     }
/*      */     
/*      */     private void pickSweetBerries(BlockState state) {
/* 1275 */       int age = ((Integer)state.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1276 */       state.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1));
/* 1277 */       int count = 1 + Fox.this.level().getRandom().nextInt(2) + ((age == 3) ? 1 : 0);
/* 1278 */       ItemStack heldItem = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1279 */       if (heldItem.isEmpty()) {
/* 1280 */         Fox.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.SWEET_BERRIES));
/* 1281 */         count--;
/*      */       } 
/* 1283 */       if (count > 0)
/* 1284 */         Block.popResource(Fox.this.level(), this.blockPos, new ItemStack((ItemLike)Items.SWEET_BERRIES, count)); 
/* 1286 */       Fox.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
/* 1287 */       Fox.this.level().setBlock(this.blockPos, (BlockState)state.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
/* 1288 */       Fox.this.level().gameEvent((Holder)GameEvent.BLOCK_CHANGE, this.blockPos, GameEvent.Context.of((Entity)Fox.this));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1293 */       return (!Fox.this.isSleeping() && super.canUse());
/*      */     }
/*      */     
/*      */     public void start() {
/* 1298 */       this.ticksWaited = 0;
/* 1299 */       Fox.this.setSitting(false);
/* 1300 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FoxGroupData extends AgeableMob.AgeableMobGroupData {
/*      */     public final Fox.Variant variant;
/*      */     
/*      */     public FoxGroupData(Fox.Variant variant) {
/* 1308 */       super(false);
/* 1309 */       this.variant = variant;
/*      */     }
/*      */   }
/*      */   
/*      */   private class FaceplantGoal extends Goal {
/*      */     private int countdown;
/*      */     
/*      */     public FaceplantGoal() {
/* 1317 */       setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1322 */       return Fox.this.isFaceplanted();
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1327 */       return (canUse() && this.countdown > 0);
/*      */     }
/*      */     
/*      */     public void start() {
/* 1332 */       this.countdown = adjustedTickDelay(40);
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1337 */       Fox.this.setFaceplanted(false);
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1342 */       this.countdown--;
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxPanicGoal extends PanicGoal {
/*      */     public FoxPanicGoal(double speedModifier) {
/* 1348 */       super((PathfinderMob)Fox.this, speedModifier);
/*      */     }
/*      */     
/*      */     public boolean shouldPanic() {
/* 1353 */       return (!Fox.this.isDefending() && super.shouldPanic());
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxStrollThroughVillageGoal extends StrollThroughVillageGoal {
/*      */     public FoxStrollThroughVillageGoal(int searchRadius, int interval) {
/* 1359 */       super((PathfinderMob)Fox.this, interval);
/*      */     }
/*      */     
/*      */     public void start() {
/* 1364 */       Fox.this.clearStates();
/* 1365 */       super.start();
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1370 */       return (super.canUse() && canFoxMove());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1375 */       return (super.canContinueToUse() && canFoxMove());
/*      */     }
/*      */     
/*      */     private boolean canFoxMove() {
/* 1379 */       return (!Fox.this.isSleeping() && !Fox.this.isSitting() && !Fox.this.isDefending() && Fox.this.getTarget() == null);
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxFloatGoal extends FloatGoal {
/*      */     public FoxFloatGoal() {
/* 1385 */       super((Mob)Fox.this);
/*      */     }
/*      */     
/*      */     public void start() {
/* 1390 */       super.start();
/* 1391 */       Fox.this.clearStates();
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1396 */       return ((Fox.this.isInWater() && Fox.this.getFluidHeight(FluidTags.WATER) > 0.25D) || Fox.this.isInLava());
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxPounceGoal extends JumpGoal {
/*      */     public boolean canUse() {
/* 1403 */       if (!Fox.this.isFullyCrouched())
/* 1404 */         return false; 
/* 1407 */       LivingEntity target = Fox.this.getTarget();
/* 1409 */       if (target == null || !target.isAlive())
/* 1410 */         return false; 
/* 1413 */       if (target.getMotionDirection() != target.getDirection())
/* 1414 */         return false; 
/* 1417 */       boolean hasClearPath = Fox.isPathClear(Fox.this, target);
/* 1418 */       if (!hasClearPath) {
/* 1419 */         Fox.this.getNavigation().createPath((Entity)target, 0);
/* 1420 */         Fox.this.setIsCrouching(false);
/* 1421 */         Fox.this.setIsInterested(false);
/*      */       } 
/* 1424 */       return hasClearPath;
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1429 */       LivingEntity target = Fox.this.getTarget();
/* 1431 */       if (target == null || !target.isAlive())
/* 1432 */         return false; 
/* 1435 */       double yd = (Fox.this.getDeltaMovement()).y;
/* 1436 */       return ((yd * yd >= 0.05000000074505806D || Math.abs(Fox.this.getXRot()) >= 15.0F || !Fox.this.onGround()) && !Fox.this.isFaceplanted());
/*      */     }
/*      */     
/*      */     public boolean isInterruptable() {
/* 1441 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1446 */       Fox.this.setJumping(true);
/* 1447 */       Fox.this.setIsPouncing(true);
/* 1448 */       Fox.this.setIsInterested(false);
/* 1450 */       LivingEntity target = Fox.this.getTarget();
/* 1451 */       if (target != null) {
/* 1452 */         Fox.this.getLookControl().setLookAt((Entity)target, 60.0F, 30.0F);
/* 1454 */         Vec3 uv = (new Vec3(target.getX() - Fox.this.getX(), target.getY() - Fox.this.getY(), target.getZ() - Fox.this.getZ())).normalize();
/* 1455 */         Fox.this.setDeltaMovement(Fox.this.getDeltaMovement().add(uv.x * 0.8D, 0.9D, uv.z * 0.8D));
/*      */       } 
/* 1462 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1467 */       Fox.this.setIsCrouching(false);
/* 1468 */       Fox.this.crouchAmount = 0.0F;
/* 1469 */       Fox.this.crouchAmountO = 0.0F;
/* 1470 */       Fox.this.setIsInterested(false);
/* 1471 */       Fox.this.setIsPouncing(false);
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1476 */       LivingEntity target = Fox.this.getTarget();
/* 1478 */       if (target != null)
/* 1479 */         Fox.this.getLookControl().setLookAt((Entity)target, 60.0F, 30.0F); 
/* 1482 */       if (!Fox.this.isFaceplanted()) {
/* 1483 */         Vec3 movement = Fox.this.getDeltaMovement();
/* 1484 */         if (movement.y * movement.y < 0.029999999329447746D && Fox.this.getXRot() != 0.0F) {
/* 1485 */           Fox.this.setXRot(Mth.rotLerp(0.2F, Fox.this.getXRot(), 0.0F));
/*      */         } else {
/* 1487 */           double direction = movement.horizontalDistance();
/* 1488 */           float upwardsBias = (Fox.this.jumping && movement.y > 0.0D) ? 6.5F : 1.0F;
/* 1489 */           double biasedY = movement.y * upwardsBias;
/* 1490 */           double len = Math.sqrt(direction * direction + biasedY * biasedY);
/* 1491 */           if (len > 9.999999747378752E-6D) {
/* 1492 */             double rotation = Math.signum(-biasedY) * Math.acos(direction / len) * 57.2957763671875D;
/* 1493 */             Fox.this.setXRot((float)rotation);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1499 */       if (target != null && Fox.this.distanceTo((Entity)target) <= 2.0F) {
/* 1500 */         Fox.this.doHurtTarget(getServerLevel(Fox.this.level()), (Entity)target);
/* 1502 */       } else if (Fox.this.getXRot() > 0.0F && Fox.this.onGround() && (float)(Fox.this.getDeltaMovement()).y != 0.0F && 
/* 1503 */         Fox.this.level().getBlockState(Fox.this.blockPosition()).is(Blocks.SNOW)) {
/* 1504 */         Fox.this.setXRot(60.0F);
/* 1505 */         Fox.this.setTarget((LivingEntity)null);
/* 1506 */         Fox.this.setFaceplanted(true);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public Vec3 getLeashOffset() {
/* 1515 */     return new Vec3(0.0D, (0.55F * getEyeHeight()), (getBbWidth() * 0.4F));
/*      */   }
/*      */   
/*      */   public class FoxLookControl extends LookControl {
/*      */     public FoxLookControl() {
/* 1520 */       super((Mob)Fox.this);
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1525 */       if (!Fox.this.isSleeping())
/* 1526 */         super.tick(); 
/*      */     }
/*      */     
/*      */     protected boolean resetXRotOnTick() {
/* 1532 */       return (!Fox.this.isPouncing() && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.isFaceplanted());
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FoxFollowParentGoal extends FollowParentGoal {
/*      */     private final Fox fox;
/*      */     
/*      */     public FoxFollowParentGoal(Fox fox, double speedModifier) {
/* 1540 */       super(fox, speedModifier);
/* 1541 */       this.fox = fox;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1546 */       return (!this.fox.isDefending() && super.canUse());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1551 */       return (!this.fox.isDefending() && super.canContinueToUse());
/*      */     }
/*      */     
/*      */     public void start() {
/* 1556 */       this.fox.clearStates();
/* 1557 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   private class FoxLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */     public FoxLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/* 1563 */       super(mob, lookAtType, lookDistance);
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1568 */       return (super.canUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1573 */       return (super.canContinueToUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */