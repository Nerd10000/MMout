/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
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
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ItemBasedSteering;
/*     */ import net.minecraft.world.entity.ItemSteerable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class Strider extends Animal implements ItemSteerable {
/*  81 */   private static final Identifier SUFFOCATING_MODIFIER_ID = Identifier.withDefaultNamespace("suffocating");
/*     */   
/*  82 */   private static final AttributeModifier SUFFOCATING_MODIFIER = new AttributeModifier(SUFFOCATING_MODIFIER_ID, -0.3400000035762787D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*     */   private static final float SUFFOCATE_STEERING_MODIFIER = 0.35F;
/*     */   
/*     */   private static final float STEERING_MODIFIER = 0.55F;
/*     */   
/*  86 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.INT);
/*     */   
/*  87 */   private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  88 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.85F)
/*  89 */     .withEyeHeight(0.4375F)
/*  90 */     .withAttachments(EntityAttachments.builder()
/*  91 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.65625F, 0.0F));
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   
/*     */   public Strider(EntityType<? extends Strider> strider, Level level) {
/*  98 */     super(strider, level);
/*  99 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME);
/* 101 */     this.blocksBuilding = true;
/* 103 */     setPathfindingMalus(PathType.WATER, -1.0F);
/* 104 */     setPathfindingMalus(PathType.LAVA, 0.0F);
/* 105 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 0.0F);
/* 106 */     setPathfindingMalus(PathType.FIRE, 0.0F);
/*     */   }
/*     */   
/*     */   public static boolean checkStriderSpawnRules(EntityType<Strider> ignoredType, LevelAccessor level, EntitySpawnReason ignoredSpawnType, BlockPos pos, RandomSource ignoredRandom) {
/* 110 */     BlockPos.MutableBlockPos checkPos = pos.mutable();
/*     */     do {
/* 112 */       checkPos.move(Direction.UP);
/* 113 */     } while (level.getFluidState((BlockPos)checkPos).is(FluidTags.LAVA));
/* 115 */     return level.getBlockState((BlockPos)checkPos).isAir();
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 120 */     if (DATA_BOOST_TIME.equals(accessor) && level().isClientSide())
/* 121 */       this.steering.onSynced(); 
/* 123 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 128 */     super.defineSynchedData(entityData);
/* 129 */     entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/* 130 */     entityData.define(DATA_SUFFOCATING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 135 */     if (slot == EquipmentSlot.SADDLE)
/* 136 */       return (isAlive() && !isBaby()); 
/* 138 */     return super.canUseSlot(slot);
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 143 */     return (slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot));
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 148 */     if (slot == EquipmentSlot.SADDLE)
/* 149 */       return (Holder<SoundEvent>)SoundEvents.STRIDER_SADDLE; 
/* 151 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 156 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.65D));
/* 157 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/* 158 */     this.temptGoal = new TemptGoal((PathfinderMob)this, 1.4D, i -> i.is(ItemTags.STRIDER_TEMPT_ITEMS), false);
/* 159 */     this.goalSelector.addGoal(3, (Goal)this.temptGoal);
/* 160 */     this.goalSelector.addGoal(4, (Goal)new StriderGoToLavaGoal(this, 1.0D));
/* 161 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.0D));
/* 162 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D, 60));
/* 163 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 164 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/* 165 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Strider.class, 8.0F));
/*     */   }
/*     */   
/*     */   public void setSuffocating(boolean flag) {
/* 169 */     this.entityData.set(DATA_SUFFOCATING, Boolean.valueOf(flag));
/* 171 */     AttributeInstance attribute = getAttribute(Attributes.MOVEMENT_SPEED);
/* 172 */     if (attribute != null)
/* 173 */       if (flag) {
/* 174 */         attribute.addOrUpdateTransientModifier(SUFFOCATING_MODIFIER);
/*     */       } else {
/* 176 */         attribute.removeModifier(SUFFOCATING_MODIFIER_ID);
/*     */       }  
/*     */   }
/*     */   
/*     */   public boolean isSuffocating() {
/* 182 */     return ((Boolean)this.entityData.get(DATA_SUFFOCATING)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean canStandOnFluid(FluidState fluid) {
/* 187 */     return fluid.is(FluidTags.LAVA);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 192 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 197 */     if (!level().isClientSide())
/* 198 */       return super.getPassengerAttachmentPoint(passenger, dimensions, scale); 
/* 201 */     float animSpeed = Math.min(0.25F, this.walkAnimation.speed());
/* 202 */     float animPos = this.walkAnimation.position();
/* 204 */     float offset = 0.12F * Mth.cos((animPos * 1.5F)) * 2.0F * animSpeed;
/* 205 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale).add(0.0D, (offset * scale), 0.0D);
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 210 */     return level.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 215 */     if (isSaddled()) {
/* 215 */       Entity entity = getFirstPassenger();
/* 215 */       if (entity instanceof Player) {
/* 215 */         Player player = (Player)entity;
/* 215 */         if (player.isHolding(Items.WARPED_FUNGUS_ON_A_STICK))
/* 216 */           return (LivingEntity)player; 
/*     */       } 
/*     */     } 
/* 218 */     return super.getControllingPassenger();
/*     */   }
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 228 */     Vec3[] directions = { getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot()), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() - 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() + 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() - 45.0F), getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), passenger.getYRot() + 45.0F) };
/* 231 */     Set<BlockPos> targetBlockPositions = Sets.newLinkedHashSet();
/* 232 */     double colliderTop = (getBoundingBox()).maxY;
/* 233 */     double colliderBottom = (getBoundingBox()).minY - 0.5D;
/* 235 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 236 */     for (Vec3 direction : directions) {
/* 237 */       blockPos.set(getX() + direction.x, colliderTop, getZ() + direction.z);
/*     */       double y;
/* 240 */       for (y = colliderTop; y > colliderBottom; y--) {
/* 241 */         targetBlockPositions.add(blockPos.immutable());
/* 242 */         blockPos.move(Direction.DOWN);
/*     */       } 
/*     */     } 
/* 246 */     for (BlockPos targetBlockPos : targetBlockPositions) {
/* 247 */       if (level().getFluidState(targetBlockPos).is(FluidTags.LAVA))
/*     */         continue; 
/* 251 */       double blockFloorHeight = level().getBlockFloorHeight(targetBlockPos);
/* 252 */       if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/* 253 */         Vec3 location = Vec3.upFromBottomCenterOf((Vec3i)targetBlockPos, blockFloorHeight);
/* 255 */         for (UnmodifiableIterator<Pose> unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) {
/* 255 */           Pose dismountPose = unmodifiableIterator.next();
/* 256 */           AABB poseCollisionBox = passenger.getLocalBoundsForPose(dismountPose);
/* 258 */           if (DismountHelper.canDismountTo((CollisionGetter)level(), passenger, poseCollisionBox.move(location))) {
/* 259 */             passenger.setPose(dismountPose);
/* 260 */             return location;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 266 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*     */   }
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 271 */     setRot(controller.getYRot(), controller.getXRot() * 0.5F);
/* 272 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/* 273 */     this.steering.tickBoost();
/* 274 */     super.tickRidden(controller, riddenInput);
/*     */   }
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 279 */     return new Vec3(0.0D, 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */   protected float getRiddenSpeed(Player controller) {
/* 284 */     return (float)(getAttributeValue(Attributes.MOVEMENT_SPEED) * (isSuffocating() ? 0.35F : 0.55F) * this.steering.boostFactor());
/*     */   }
/*     */   
/*     */   protected float nextStep() {
/* 289 */     return this.moveDist + 0.6F;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 294 */     playSound(isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean boost() {
/* 299 */     return this.steering.boost(getRandom());
/*     */   }
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 304 */     if (isInLava()) {
/* 305 */       resetFallDistance();
/*     */       return;
/*     */     } 
/* 309 */     super.checkFallDamage(ya, onGround, onState, pos);
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isBeingTempted : ()Z
/*     */     //   4: ifeq -> 32
/*     */     //   7: aload_0
/*     */     //   8: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   11: sipush #140
/*     */     //   14: invokeinterface nextInt : (I)I
/*     */     //   19: ifne -> 32
/*     */     //   22: aload_0
/*     */     //   23: getstatic net/minecraft/sounds/SoundEvents.STRIDER_HAPPY : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   26: invokevirtual makeSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*     */     //   29: goto -> 60
/*     */     //   32: aload_0
/*     */     //   33: invokevirtual isPanicking : ()Z
/*     */     //   36: ifeq -> 60
/*     */     //   39: aload_0
/*     */     //   40: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   43: bipush #60
/*     */     //   45: invokeinterface nextInt : (I)I
/*     */     //   50: ifne -> 60
/*     */     //   53: aload_0
/*     */     //   54: getstatic net/minecraft/sounds/SoundEvents.STRIDER_RETREAT : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   57: invokevirtual makeSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*     */     //   60: aload_0
/*     */     //   61: invokevirtual isNoAi : ()Z
/*     */     //   64: ifne -> 176
/*     */     //   67: aload_0
/*     */     //   68: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   71: aload_0
/*     */     //   72: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   75: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   78: astore_1
/*     */     //   79: aload_0
/*     */     //   80: invokevirtual getBlockStateOnLegacy : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   83: astore_2
/*     */     //   84: aload_1
/*     */     //   85: getstatic net/minecraft/tags/BlockTags.STRIDER_WARM_BLOCKS : Lnet/minecraft/tags/TagKey;
/*     */     //   88: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   91: ifne -> 116
/*     */     //   94: aload_2
/*     */     //   95: getstatic net/minecraft/tags/BlockTags.STRIDER_WARM_BLOCKS : Lnet/minecraft/tags/TagKey;
/*     */     //   98: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */     //   101: ifne -> 116
/*     */     //   104: aload_0
/*     */     //   105: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*     */     //   108: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*     */     //   111: dconst_0
/*     */     //   112: dcmpl
/*     */     //   113: ifle -> 120
/*     */     //   116: iconst_1
/*     */     //   117: goto -> 121
/*     */     //   120: iconst_0
/*     */     //   121: istore_3
/*     */     //   122: aload_0
/*     */     //   123: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   126: astore #6
/*     */     //   128: aload #6
/*     */     //   130: instanceof net/minecraft/world/entity/monster/Strider
/*     */     //   133: ifeq -> 155
/*     */     //   136: aload #6
/*     */     //   138: checkcast net/minecraft/world/entity/monster/Strider
/*     */     //   141: astore #5
/*     */     //   143: aload #5
/*     */     //   145: invokevirtual isSuffocating : ()Z
/*     */     //   148: ifne -> 155
/*     */     //   151: iconst_1
/*     */     //   152: goto -> 156
/*     */     //   155: iconst_0
/*     */     //   156: istore #4
/*     */     //   158: aload_0
/*     */     //   159: iload_3
/*     */     //   160: ifne -> 172
/*     */     //   163: iload #4
/*     */     //   165: ifne -> 172
/*     */     //   168: iconst_1
/*     */     //   169: goto -> 173
/*     */     //   172: iconst_0
/*     */     //   173: invokevirtual setSuffocating : (Z)V
/*     */     //   176: aload_0
/*     */     //   177: invokespecial tick : ()V
/*     */     //   180: aload_0
/*     */     //   181: invokevirtual floatStrider : ()V
/*     */     //   184: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #314	-> 0
/*     */     //   #315	-> 22
/*     */     //   #316	-> 32
/*     */     //   #317	-> 53
/*     */     //   #320	-> 60
/*     */     //   #321	-> 67
/*     */     //   #322	-> 79
/*     */     //   #324	-> 84
/*     */     //   #325	-> 122
/*     */     //   #328	-> 158
/*     */     //   #331	-> 176
/*     */     //   #332	-> 180
/*     */     //   #333	-> 184
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   143	12	5	strider	Lnet/minecraft/world/entity/monster/Strider;
/*     */     //   79	97	1	stateInside	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   84	92	2	stateOn	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   122	54	3	inWarmBlocks	Z
/*     */     //   158	18	4	onWarmStrider	Z
/*     */     //   0	185	0	this	Lnet/minecraft/world/entity/monster/Strider;
/*     */   }
/*     */   
/*     */   private boolean isBeingTempted() {
/* 336 */     return (this.temptGoal != null && this.temptGoal.isRunning());
/*     */   }
/*     */   
/*     */   protected boolean shouldPassengersInheritMalus() {
/* 341 */     return true;
/*     */   }
/*     */   
/*     */   private void floatStrider() {
/* 345 */     if (isInLava()) {
/* 346 */       CollisionContext context = CollisionContext.of((Entity)this);
/* 347 */       if (!context.isAbove(getLiquidCollisionShape(), blockPosition(), true) || level().getFluidState(blockPosition().above()).is(FluidTags.LAVA)) {
/* 348 */         setDeltaMovement(getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
/*     */       } else {
/* 350 */         setOnGround(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public VoxelShape getLiquidCollisionShape() {
/* 357 */     return Block.column(16.0D, 0.0D, 8.0D);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 361 */     return Animal.createAnimalAttributes()
/* 362 */       .add(Attributes.MOVEMENT_SPEED, 0.17499999701976776D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 367 */     if (isPanicking() || isBeingTempted())
/* 368 */       return null; 
/* 370 */     return SoundEvents.STRIDER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 375 */     return SoundEvents.STRIDER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 380 */     return SoundEvents.STRIDER_DEATH;
/*     */   }
/*     */   
/*     */   protected boolean canAddPassenger(Entity passenger) {
/* 385 */     return (!isVehicle() && !isEyeInFluid(FluidTags.LAVA));
/*     */   }
/*     */   
/*     */   public boolean isSensitiveToWater() {
/* 390 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isOnFire() {
/* 395 */     return false;
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 400 */     return (PathNavigation)new StriderPathNavigation(this, level);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 405 */     if (level.getBlockState(pos).getFluidState().is(FluidTags.LAVA))
/* 406 */       return 10.0F; 
/* 410 */     return isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
/*     */   }
/*     */   
/*     */   public Strider getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 415 */     return (Strider)EntityTypes.STRIDER.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 420 */     return itemStack.is(ItemTags.STRIDER_FOOD);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 425 */     boolean hasFood = isFood(player.getItemInHand(hand));
/* 427 */     if (!hasFood && isSaddled() && !isVehicle() && !player.isSecondaryUseActive()) {
/* 428 */       if (!level().isClientSide())
/* 429 */         player.startRiding((Entity)this); 
/* 431 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 434 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 435 */     if (!interactionResult.consumesAction()) {
/* 436 */       ItemStack itemStack = player.getItemInHand(hand);
/* 437 */       if (isEquippableInSlot(itemStack, EquipmentSlot.SADDLE))
/* 438 */         return itemStack.interactLivingEntity(player, (LivingEntity)this, hand); 
/* 440 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 441 */     if (hasFood && !isSilent())
/* 442 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.STRIDER_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F); 
/* 445 */     return interactionResult;
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 450 */     return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 456 */     if (isBaby())
/* 457 */       return super.finalizeSpawn(level, difficulty, spawnReason, groupData); 
/* 460 */     RandomSource random = level.getRandom();
/* 461 */     if (random.nextInt(30) == 0) {
/* 462 */       Mob jockey = (Mob)EntityTypes.ZOMBIFIED_PIGLIN.create((Level)level.getLevel(), EntitySpawnReason.JOCKEY);
/* 463 */       if (jockey != null) {
/* 464 */         groupData = spawnJockey(level, difficulty, jockey, (SpawnGroupData)new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(random), false));
/* 466 */         jockey.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK));
/* 467 */         setItemSlot(EquipmentSlot.SADDLE, new ItemStack((ItemLike)Items.SADDLE));
/* 468 */         setGuaranteedDrop(EquipmentSlot.SADDLE);
/*     */       } 
/* 470 */     } else if (random.nextInt(10) == 0) {
/* 471 */       AgeableMob jockey = (AgeableMob)EntityTypes.STRIDER.create((Level)level.getLevel(), EntitySpawnReason.JOCKEY);
/* 472 */       if (jockey != null) {
/* 473 */         jockey.setAge(-24000);
/* 475 */         groupData = spawnJockey(level, difficulty, (Mob)jockey, (SpawnGroupData)null);
/*     */       } 
/*     */     } else {
/* 478 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.5F);
/*     */     } 
/* 481 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   private SpawnGroupData spawnJockey(ServerLevelAccessor level, DifficultyInstance difficulty, Mob jockey, SpawnGroupData jockeyGroupData) {
/* 485 */     jockey.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 486 */     jockey.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, jockeyGroupData);
/* 487 */     jockey.startRiding((Entity)this, true, false);
/* 489 */     return (SpawnGroupData)new AgeableMob.AgeableMobGroupData(0.0F);
/*     */   }
/*     */   
/*     */   private static class StriderPathNavigation extends GroundPathNavigation {
/*     */     public StriderPathNavigation(Strider mob, Level level) {
/* 494 */       super((Mob)mob, level);
/*     */     }
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 500 */       this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/* 501 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */     
/*     */     public boolean isStableDestination(BlockPos pos) {
/* 506 */       return (this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StriderGoToLavaGoal extends MoveToBlockGoal {
/*     */     private final Strider strider;
/*     */     
/*     */     private StriderGoToLavaGoal(Strider strider, double speedModifier) {
/* 514 */       super((PathfinderMob)strider, speedModifier, 8, 2);
/* 515 */       this.strider = strider;
/*     */     }
/*     */     
/*     */     public BlockPos getMoveToTarget() {
/* 520 */       return this.blockPos;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 525 */       return (!this.strider.isInLava() && isValidTarget((LevelReader)this.strider.level(), this.blockPos));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 530 */       return (!this.strider.isInLava() && super.canUse());
/*     */     }
/*     */     
/*     */     public boolean shouldRecalculatePath() {
/* 535 */       return (this.tryTicks % 20 == 0);
/*     */     }
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 540 */       return (level.getBlockState(pos).is(Blocks.LAVA) && level.getBlockState(pos.above()).isPathfindable(PathComputationType.LAND));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Strider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */