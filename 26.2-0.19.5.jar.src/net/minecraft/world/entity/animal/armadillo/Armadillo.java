/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Armadillo extends Animal {
/*     */   public static final float BABY_SCALE = 0.6F;
/*     */   
/*     */   public static final float MAX_HEAD_ROTATION_EXTENT = 32.5F;
/*     */   
/*     */   public static final int SCARE_CHECK_INTERVAL = 80;
/*     */   
/*     */   private static final double SCARE_DISTANCE_HORIZONTAL = 7.0D;
/*     */   
/*     */   private static final double SCARE_DISTANCE_VERTICAL = 2.0D;
/*     */   
/*  66 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.ARMADILLO.getDimensions()
/*  67 */     .scale(0.6F)
/*  68 */     .withEyeHeight(0.21875F);
/*     */   
/*  70 */   private static final Brain.Provider<Armadillo> BRAIN_PROVIDER = Brain.provider(
/*  71 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT, SensorType.ARMADILLO_SCARE_DETECTED), paramArmadillo -> ArmadilloAi.getActivities());
/*     */   
/*  82 */   private static final EntityDataAccessor<ArmadilloState> ARMADILLO_STATE = SynchedEntityData.defineId(Armadillo.class, EntityDataSerializers.ARMADILLO_STATE);
/*     */   
/*  83 */   private long inStateTicks = 0L;
/*     */   
/*  84 */   public final AnimationState rollOutAnimationState = new AnimationState();
/*     */   
/*  85 */   public final AnimationState rollUpAnimationState = new AnimationState();
/*     */   
/*  86 */   public final AnimationState peekAnimationState = new AnimationState();
/*     */   
/*     */   private int scuteTime;
/*     */   
/*     */   private boolean peekReceivedClient = false;
/*     */   
/*     */   public Armadillo(EntityType<? extends Animal> type, Level level) {
/*  93 */     super(type, level);
/*  94 */     getNavigation().setCanFloat(true);
/*  95 */     this.scuteTime = pickNextScuteDropTime();
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 100 */     return (AgeableMob)EntityTypes.ARMADILLO.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 104 */     return Animal.createAnimalAttributes()
/* 105 */       .add(Attributes.MAX_HEALTH, 12.0D)
/* 106 */       .add(Attributes.MOVEMENT_SPEED, 0.14D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 111 */     super.defineSynchedData(entityData);
/* 112 */     entityData.define(ARMADILLO_STATE, ArmadilloState.IDLE);
/*     */   }
/*     */   
/*     */   public boolean isScared() {
/* 116 */     return (this.entityData.get(ARMADILLO_STATE) != ArmadilloState.IDLE);
/*     */   }
/*     */   
/*     */   public boolean shouldHideInShell() {
/* 120 */     return getState().shouldHideInShell(this.inStateTicks);
/*     */   }
/*     */   
/*     */   public boolean shouldSwitchToScaredState() {
/* 124 */     return (getState() == ArmadilloState.ROLLING && this.inStateTicks > ArmadilloState.ROLLING.animationDuration());
/*     */   }
/*     */   
/*     */   public ArmadilloState getState() {
/* 128 */     return (ArmadilloState)this.entityData.get(ARMADILLO_STATE);
/*     */   }
/*     */   
/*     */   public void switchToState(ArmadilloState state) {
/* 132 */     this.entityData.set(ARMADILLO_STATE, state);
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 137 */     if (ARMADILLO_STATE.equals(accessor))
/* 138 */       this.inStateTicks = 0L; 
/* 140 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected Brain<Armadillo> makeBrain(Brain.Packed packedBrain) {
/* 145 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Armadillo> getBrain() {
/* 151 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 156 */     ProfilerFiller profiler = Profiler.get();
/* 157 */     profiler.push("armadilloBrain");
/* 158 */     getBrain().tick(level, (LivingEntity)this);
/* 159 */     profiler.pop();
/* 161 */     profiler.push("armadilloActivityUpdate");
/* 162 */     ArmadilloAi.updateActivity(this);
/* 163 */     profiler.pop();
/* 165 */     if (isAlive() && --this.scuteTime <= 0 && shouldDropLoot(level)) {
/* 166 */       if (dropFromGiftLootTable(level, BuiltInLootTables.ARMADILLO_SHED, this::spawnAtLocation)) {
/* 167 */         playSound(SoundEvents.ARMADILLO_SCUTE_DROP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 168 */         gameEvent((Holder)GameEvent.ENTITY_PLACE);
/*     */       } 
/* 170 */       this.scuteTime = pickNextScuteDropTime();
/*     */     } 
/* 173 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private int pickNextScuteDropTime() {
/* 177 */     return this.random.nextInt(20 * TimeUtil.SECONDS_PER_MINUTE * 5) + 20 * TimeUtil.SECONDS_PER_MINUTE * 5;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 182 */     super.tick();
/* 183 */     if (level().isClientSide())
/* 184 */       setupAnimationStates(); 
/* 186 */     if (isScared())
/* 187 */       clampHeadRotationToBody(); 
/* 190 */     this.inStateTicks++;
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 195 */     return isBaby() ? 0.6F : 1.0F;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 200 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 204 */     switch (getState().ordinal()) {
/*     */       case 0:
/* 206 */         this.rollOutAnimationState.stop();
/* 207 */         this.rollUpAnimationState.stop();
/* 208 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 3:
/* 211 */         this.rollOutAnimationState.startIfStopped(this.tickCount);
/* 212 */         this.rollUpAnimationState.stop();
/* 213 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 1:
/* 216 */         this.rollOutAnimationState.stop();
/* 217 */         this.rollUpAnimationState.startIfStopped(this.tickCount);
/* 218 */         this.peekAnimationState.stop();
/*     */         break;
/*     */       case 2:
/* 221 */         this.rollOutAnimationState.stop();
/* 222 */         this.rollUpAnimationState.stop();
/* 223 */         if (this.peekReceivedClient) {
/* 224 */           this.peekAnimationState.stop();
/* 225 */           this.peekReceivedClient = false;
/*     */         } 
/* 228 */         if (this.inStateTicks == 0L) {
/* 229 */           this.peekAnimationState.start(this.tickCount);
/* 230 */           this.peekAnimationState.fastForward(ArmadilloState.SCARED.animationDuration(), 1.0F);
/*     */           break;
/*     */         } 
/* 232 */         this.peekAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 240 */     if (id == 64 && level().isClientSide()) {
/* 242 */       this.peekReceivedClient = true;
/* 243 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ARMADILLO_PEEK, getSoundSource(), 1.0F, 1.0F, false);
/*     */     } else {
/* 245 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 251 */     return itemStack.is(ItemTags.ARMADILLO_FOOD);
/*     */   }
/*     */   
/*     */   public static boolean checkArmadilloSpawnRules(EntityType<Armadillo> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 255 */     return (level.getBlockState(pos.below()).is(BlockTags.ARMADILLO_SPAWNABLE_ON) && 
/* 256 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   public boolean isScaredBy(LivingEntity livingEntity) {
/* 260 */     if (!getBoundingBox().inflate(7.0D, 2.0D, 7.0D).intersects(livingEntity.getBoundingBox()))
/* 261 */       return false; 
/* 263 */     if (livingEntity.is(EntityTypeTags.UNDEAD))
/* 264 */       return true; 
/* 266 */     if (getLastHurtByMob() == livingEntity)
/* 267 */       return true; 
/* 269 */     if (livingEntity instanceof Player) {
/* 269 */       Player player = (Player)livingEntity;
/* 270 */       if (player.isSpectator())
/* 271 */         return false; 
/* 273 */       return (player.isSprinting() || player.isPassenger());
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 280 */     super.addAdditionalSaveData(output);
/* 281 */     output.store("state", ArmadilloState.CODEC, getState());
/* 282 */     output.putInt("scute_time", this.scuteTime);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 287 */     super.readAdditionalSaveData(input);
/* 288 */     switchToState(input.read("state", ArmadilloState.CODEC).orElse(ArmadilloState.IDLE));
/* 290 */     input.getInt("scute_time").ifPresent(time -> this.scuteTime = time.intValue());
/*     */   }
/*     */   
/*     */   public void rollUp() {
/* 294 */     if (isScared())
/*     */       return; 
/* 297 */     stopInPlace();
/* 298 */     resetLove();
/* 299 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 300 */     makeSound(SoundEvents.ARMADILLO_ROLL);
/* 301 */     switchToState(ArmadilloState.ROLLING);
/*     */   }
/*     */   
/*     */   public void rollOut() {
/* 305 */     if (!isScared())
/*     */       return; 
/* 308 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 309 */     makeSound(SoundEvents.ARMADILLO_UNROLL_FINISH);
/* 310 */     switchToState(ArmadilloState.IDLE);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 315 */     if (isScared())
/* 316 */       damage = (damage - 1.0F) / 2.0F; 
/* 318 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 323 */     super.actuallyHurt(level, source, dmg);
/* 324 */     if (isNoAi() || isDeadOrDying())
/*     */       return; 
/* 328 */     if (source.getEntity() instanceof LivingEntity) {
/* 329 */       getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, Boolean.valueOf(true), 80L);
/* 330 */       if (canStayRolledUp())
/* 331 */         rollUp(); 
/* 333 */     } else if (source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)) {
/* 334 */       rollOut();
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 340 */     ItemStack itemStack = player.getItemInHand(hand);
/* 342 */     if (itemStack.is(Items.BRUSH) && brushOffScute((Entity)player, itemStack)) {
/* 343 */       itemStack.hurtAndBreak(16, (LivingEntity)player, hand.asEquipmentSlot());
/* 344 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 347 */     if (isScared())
/* 348 */       return (InteractionResult)InteractionResult.FAIL; 
/* 350 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean brushOffScute(Entity interactingEntity, ItemStack tool) {
/* 354 */     if (isBaby())
/* 355 */       return false; 
/* 357 */     Level level = level();
/* 357 */     if (level instanceof ServerLevel) {
/* 357 */       ServerLevel serverLevel = (ServerLevel)level;
/* 358 */       dropFromEntityInteractLootTable(serverLevel, BuiltInLootTables.ARMADILLO_BRUSH, interactingEntity, (ItemInstance)tool, this::spawnAtLocation);
/* 359 */       playSound(SoundEvents.ARMADILLO_BRUSH);
/* 360 */       gameEvent((Holder)GameEvent.ENTITY_INTERACT);
/*     */     } 
/* 362 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canStayRolledUp() {
/* 366 */     return (!isPanicking() && !isInLiquid() && !isLeashed() && !isPassenger() && !isVehicle());
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/* 371 */     return (super.canFallInLove() && !isScared());
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 376 */     if (isScared())
/* 377 */       return null; 
/* 379 */     return SoundEvents.ARMADILLO_AMBIENT;
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 384 */     makeSound(SoundEvents.ARMADILLO_EAT);
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 389 */     return SoundEvents.ARMADILLO_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 394 */     if (isScared())
/* 395 */       return SoundEvents.ARMADILLO_HURT_REDUCED; 
/* 397 */     return SoundEvents.ARMADILLO_HURT;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 402 */     playSound(SoundEvents.ARMADILLO_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 407 */     if (isScared())
/* 408 */       return 0; 
/* 410 */     return 32;
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 415 */     return new BodyRotationControl((Mob)this) {
/*     */         public void clientTick() {
/* 418 */           if (!Armadillo.this.isScared())
/* 419 */             super.clientTick(); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public enum ArmadilloState implements StringRepresentable {
/* 427 */     IDLE("idle", false, 0, 0) {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 430 */         return false;
/*     */       }
/*     */     },
/* 433 */     ROLLING("rolling", true, 10, 1) {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 436 */         return (ticksInState > 5L);
/*     */       }
/*     */     },
/* 439 */     SCARED("scared", true, 50, 2) {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 442 */         return true;
/*     */       }
/*     */     },
/* 445 */     UNROLLING("unrolling", true, 30, 3) {
/*     */       public boolean shouldHideInShell(long ticksInState) {
/* 448 */         return (ticksInState < 26L);
/*     */       }
/*     */     };
/*     */     
/* 453 */     private static final Codec<ArmadilloState> CODEC = (Codec<ArmadilloState>)StringRepresentable.fromEnum(ArmadilloState::values);
/*     */     
/* 454 */     private static final IntFunction<ArmadilloState> BY_ID = ByIdMap.continuous(ArmadilloState::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 456 */     public static final StreamCodec<ByteBuf, ArmadilloState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArmadilloState::id);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final boolean isThreatened;
/*     */     
/*     */     private final int animationDuration;
/*     */     
/*     */     private final int id;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
/* 464 */       this.name = name;
/* 465 */       this.isThreatened = isThreatened;
/* 466 */       this.animationDuration = animationDuration;
/* 467 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 472 */       return this.name;
/*     */     }
/*     */     
/*     */     private int id() {
/* 476 */       return this.id;
/*     */     }
/*     */     
/*     */     public boolean isThreatened() {
/* 482 */       return this.isThreatened;
/*     */     }
/*     */     
/*     */     public int animationDuration() {
/* 486 */       return this.animationDuration;
/*     */     }
/*     */     
/*     */     public abstract boolean shouldHideInShell(long param1Long);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return (ticksInState > 5L);
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public boolean shouldHideInShell(long ticksInState) {
/*     */       return (ticksInState < 26L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\Armadillo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */