/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CarrotBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Rabbit extends Animal {
/*  86 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.24F, 0.4F).withEyeHeight(0.39F);
/*     */   
/*     */   public static final double STROLL_SPEED_MOD = 0.6D;
/*     */   
/*     */   public static final double BREED_SPEED_MOD = 0.8D;
/*     */   
/*     */   public static final double FOLLOW_SPEED_MOD = 1.0D;
/*     */   
/*     */   public static final double FLEE_SPEED_MOD = 2.2D;
/*     */   
/*     */   public static final double ATTACK_SPEED_MOD = 1.4D;
/*     */   
/*     */   private static final double BABY_JUMP_HEIGHT = 0.5D;
/*     */   
/*     */   private static final double ADULT_JUMP_HEIGHT = 1.5D;
/*     */   
/*     */   private static final int JUMP_DELAY_TICKS = 10;
/*     */   
/*     */   private static final int PANIC_JUMP_DELAY_TICKS = 3;
/*     */   
/*     */   private static final int JUMP_DURATION_IN_TICKS = 15;
/*     */   
/* 101 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Rabbit.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int DEFAULT_MORE_CARROT_TICKS = 0;
/*     */   
/* 104 */   public final AnimationState hopAnimationState = new AnimationState();
/*     */   
/* 105 */   public final AnimationState idleHeadTiltAnimationState = new AnimationState();
/*     */   
/*     */   private static final int IDLE_MINIMAL_DURATION_TICKS = 180;
/*     */   
/* 109 */   private int idleAnimationTimeout = this.random.nextInt(40) + 180;
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 112 */     BROWN(0, "brown"),
/* 113 */     WHITE(1, "white"),
/* 114 */     BLACK(2, "black"),
/* 115 */     WHITE_SPLOTCHED(3, "white_splotched"),
/* 116 */     GOLD(4, "gold"),
/* 117 */     SALT(5, "salt"),
/* 118 */     EVIL(99, "evil");
/*     */     
/* 121 */     public static final Variant DEFAULT = BROWN;
/*     */     
/* 123 */     private static final IntFunction<Variant> BY_ID = ByIdMap.sparse(Variant::id, (Object[])values(), DEFAULT);
/*     */     
/* 125 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/*     */     @Deprecated
/* 127 */     public static final Codec<Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::id);
/*     */     
/* 129 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Variant(int id, String name) {
/* 135 */       this.id = id;
/* 136 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 141 */       return this.name;
/*     */     }
/*     */     
/*     */     public int id() {
/* 145 */       return this.id;
/*     */     }
/*     */     
/*     */     public static Variant byId(int id) {
/* 149 */       return BY_ID.apply(id);
/*     */     }
/*     */   }
/*     */   
/* 153 */   private static final Identifier KILLER_BUNNY = Identifier.withDefaultNamespace("killer_bunny");
/*     */   
/*     */   private static final int DEFAULT_ATTACK_POWER = 3;
/*     */   
/*     */   private static final int EVIL_ATTACK_POWER_INCREMENT = 5;
/*     */   
/* 157 */   private static final Identifier EVIL_ATTACK_POWER_MODIFIER = Identifier.withDefaultNamespace("evil");
/*     */   
/*     */   private static final int EVIL_ARMOR_VALUE = 8;
/*     */   
/*     */   private static final int MORE_CARROTS_DELAY = 40;
/*     */   
/*     */   private int jumpTicks;
/*     */   
/*     */   private int jumpDuration;
/*     */   
/*     */   private boolean wasOnGround;
/*     */   
/*     */   private int jumpDelayTicks;
/*     */   
/* 168 */   private int moreCarrotTicks = 0;
/*     */   
/*     */   public Rabbit(EntityType<? extends Rabbit> type, Level level) {
/* 171 */     super(type, level);
/* 173 */     this.jumpControl = new RabbitJumpControl(this);
/* 175 */     this.moveControl = new RabbitMoveControl<>(this);
/* 177 */     setSpeedModifier(0.0D);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 182 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 183 */     this.goalSelector.addGoal(1, (Goal)new ClimbOnTopOfPowderSnowGoal((Mob)this, level()));
/* 184 */     this.goalSelector.addGoal(1, (Goal)new RabbitPanicGoal(this, 2.2D));
/* 185 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 0.8D));
/* 186 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, i -> i.is(ItemTags.RABBIT_FOOD), false));
/* 187 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
/* 188 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2D, 2.2D));
/* 189 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Monster.class, 4.0F, 2.2D, 2.2D));
/* 190 */     this.goalSelector.addGoal(5, (Goal)new RaidGardenGoal(this));
/* 191 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.6D));
/* 192 */     this.goalSelector.addGoal(11, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/*     */   }
/*     */   
/*     */   protected EntityDimensions getDefaultDimensions(Pose pose) {
/* 197 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected float getJumpPower() {
/* 202 */     float baseJumpPower = 0.3F;
/* 204 */     if (this.moveControl.getSpeedModifier() <= 0.6D)
/* 205 */       baseJumpPower = 0.2F; 
/* 211 */     Path path = this.navigation.getPath();
/* 212 */     if (path != null && !path.isDone()) {
/* 213 */       Vec3 currentPos = path.getNextEntityPos((Entity)this);
/* 214 */       if (currentPos.y > getY() + 0.5D)
/* 215 */         baseJumpPower = 0.5F; 
/*     */     } 
/* 224 */     if (this.horizontalCollision || (this.jumping && this.moveControl.getWantedY() > getY() + 0.5D))
/* 225 */       baseJumpPower = 0.5F; 
/* 228 */     return getJumpPower(baseJumpPower / 0.42F);
/*     */   }
/*     */   
/*     */   public void jumpFromGround() {
/* 233 */     super.jumpFromGround();
/* 234 */     double speedModifier = this.moveControl.getSpeedModifier();
/* 235 */     if (speedModifier > 0.0D) {
/* 236 */       double current = getDeltaMovement().horizontalDistanceSqr();
/* 237 */       if (current < 0.01D)
/* 239 */         moveRelative(0.1F, new Vec3(0.0D, isBaby() ? 0.5D : 1.5D, 1.0D)); 
/*     */     } 
/* 242 */     if (!level().isClientSide())
/* 243 */       level().broadcastEntityEvent((Entity)this, (byte)1); 
/*     */   }
/*     */   
/*     */   public float getJumpCompletion(float a) {
/* 248 */     if (this.jumpDuration == 0)
/* 249 */       return 0.0F; 
/* 251 */     return (this.jumpTicks + a) / this.jumpDuration;
/*     */   }
/*     */   
/*     */   public void setSpeedModifier(double speed) {
/* 255 */     getNavigation().setSpeedModifier(speed);
/* 256 */     this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speed);
/*     */   }
/*     */   
/*     */   public void setJumping(boolean jump) {
/* 261 */     super.setJumping(jump);
/* 262 */     if (jump)
/* 263 */       playSound(getJumpSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F); 
/*     */   }
/*     */   
/*     */   public void startJumping() {
/* 268 */     setJumping(true);
/* 269 */     this.jumpDuration = 15;
/* 270 */     this.jumpTicks = 0;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 275 */     super.defineSynchedData(entityData);
/* 277 */     entityData.define(DATA_TYPE_ID, Integer.valueOf(Variant.DEFAULT.id));
/*     */   }
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 282 */     if (this.jumpDelayTicks > 0)
/* 283 */       this.jumpDelayTicks--; 
/* 286 */     if (this.moreCarrotTicks > 0) {
/* 287 */       this.moreCarrotTicks -= this.random.nextInt(3);
/* 288 */       if (this.moreCarrotTicks < 0)
/* 289 */         this.moreCarrotTicks = 0; 
/*     */     } 
/* 293 */     if (onGround()) {
/* 294 */       if (!this.wasOnGround) {
/* 295 */         setJumping(false);
/* 296 */         checkLandingDelay();
/*     */       } 
/* 299 */       if (getVariant() == Variant.EVIL && this.jumpDelayTicks == 0) {
/* 300 */         LivingEntity target = getTarget();
/* 301 */         if (target != null && distanceToSqr((Entity)target) < 16.0D) {
/* 302 */           facePoint(target.getX(), target.getZ());
/* 303 */           this.moveControl.setWantedPosition(target.getX(), target.getY(), target.getZ(), this.moveControl.getSpeedModifier());
/* 304 */           startJumping();
/* 305 */           this.wasOnGround = true;
/*     */         } 
/*     */       } 
/* 309 */       RabbitJumpControl jumpControl = (RabbitJumpControl)this.jumpControl;
/* 310 */       if (!jumpControl.wantJump()) {
/* 311 */         if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
/* 312 */           Path path = this.navigation.getPath();
/* 313 */           Vec3 pos = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
/* 314 */           if (path != null && !path.isDone())
/* 315 */             pos = path.getNextEntityPos((Entity)this); 
/* 317 */           facePoint(pos.x, pos.z);
/* 318 */           startJumping();
/*     */         } 
/* 320 */       } else if (!jumpControl.canJump()) {
/* 321 */         enableJumpControl();
/*     */       } 
/*     */     } 
/* 325 */     this.wasOnGround = onGround();
/*     */   }
/*     */   
/*     */   public boolean canSpawnSprintParticle() {
/* 330 */     return false;
/*     */   }
/*     */   
/*     */   private void facePoint(double faceX, double faceZ) {
/* 334 */     setYRot((float)(Mth.atan2(faceZ - getZ(), faceX - getX()) * 57.2957763671875D) - 90.0F);
/*     */   }
/*     */   
/*     */   private void enableJumpControl() {
/* 338 */     ((RabbitJumpControl)this.jumpControl).setCanJump(true);
/*     */   }
/*     */   
/*     */   private void disableJumpControl() {
/* 342 */     ((RabbitJumpControl)this.jumpControl).setCanJump(false);
/*     */   }
/*     */   
/*     */   private void setLandingDelay() {
/* 346 */     this.jumpDelayTicks = (this.moveControl.getSpeedModifier() < 2.2D) ? 10 : 3;
/*     */   }
/*     */   
/*     */   private void checkLandingDelay() {
/* 350 */     setLandingDelay();
/* 351 */     disableJumpControl();
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 356 */     super.aiStep();
/* 357 */     if (this.jumpTicks != this.jumpDuration) {
/* 358 */       this.jumpTicks++;
/* 359 */     } else if (this.jumpDuration != 0) {
/* 360 */       this.jumpTicks = 0;
/* 361 */       this.jumpDuration = 0;
/* 362 */       setJumping(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 367 */     return Animal.createAnimalAttributes()
/* 368 */       .add(Attributes.MAX_HEALTH, 3.0D)
/* 369 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 370 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 375 */     super.addAdditionalSaveData(output);
/* 376 */     output.store("RabbitType", Variant.LEGACY_CODEC, getVariant());
/* 377 */     output.putInt("MoreCarrotTicks", this.moreCarrotTicks);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 382 */     super.readAdditionalSaveData(input);
/* 383 */     setVariant(input.read("RabbitType", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/* 384 */     this.moreCarrotTicks = input.getIntOr("MoreCarrotTicks", 0);
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 388 */     return SoundEvents.RABBIT_JUMP;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 393 */     return SoundEvents.RABBIT_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 398 */     return SoundEvents.RABBIT_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 403 */     return SoundEvents.RABBIT_DEATH;
/*     */   }
/*     */   
/*     */   public void playAttackSound() {
/* 408 */     if (getVariant() == Variant.EVIL)
/* 409 */       playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); 
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 415 */     return (getVariant() == Variant.EVIL) ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
/*     */   }
/*     */   
/*     */   public Rabbit getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*     */     // Byte code:
/*     */     //   0: getstatic net/minecraft/world/entity/EntityTypes.RABBIT : Lnet/minecraft/world/entity/EntityType;
/*     */     //   3: aload_1
/*     */     //   4: getstatic net/minecraft/world/entity/EntitySpawnReason.BREEDING : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   7: invokevirtual create : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;
/*     */     //   10: checkcast net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   13: astore_3
/*     */     //   14: aload_3
/*     */     //   15: ifnull -> 89
/*     */     //   18: aload_1
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   23: invokestatic getRandomRabbitVariant : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   26: astore #4
/*     */     //   28: aload_0
/*     */     //   29: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   32: bipush #20
/*     */     //   34: invokeinterface nextInt : (I)I
/*     */     //   39: ifeq -> 83
/*     */     //   42: aload_2
/*     */     //   43: instanceof net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   46: ifeq -> 77
/*     */     //   49: aload_2
/*     */     //   50: checkcast net/minecraft/world/entity/animal/rabbit/Rabbit
/*     */     //   53: astore #5
/*     */     //   55: aload_0
/*     */     //   56: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   59: invokeinterface nextBoolean : ()Z
/*     */     //   64: ifeq -> 77
/*     */     //   67: aload #5
/*     */     //   69: invokevirtual getVariant : ()Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   72: astore #4
/*     */     //   74: goto -> 83
/*     */     //   77: aload_0
/*     */     //   78: invokevirtual getVariant : ()Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   81: astore #4
/*     */     //   83: aload_3
/*     */     //   84: aload #4
/*     */     //   86: invokevirtual setVariant : (Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;)V
/*     */     //   89: aload_3
/*     */     //   90: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #420	-> 0
/*     */     //   #421	-> 14
/*     */     //   #422	-> 18
/*     */     //   #423	-> 28
/*     */     //   #424	-> 42
/*     */     //   #425	-> 67
/*     */     //   #427	-> 77
/*     */     //   #430	-> 83
/*     */     //   #432	-> 89
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   55	22	5	rabbitPartner	Lnet/minecraft/world/entity/animal/rabbit/Rabbit;
/*     */     //   28	61	4	variant	Lnet/minecraft/world/entity/animal/rabbit/Rabbit$Variant;
/*     */     //   0	91	0	this	Lnet/minecraft/world/entity/animal/rabbit/Rabbit;
/*     */     //   0	91	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	91	2	partner	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   14	77	3	offspring	Lnet/minecraft/world/entity/animal/rabbit/Rabbit;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 437 */     return itemStack.is(ItemTags.RABBIT_FOOD);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 441 */     return Variant.byId(((Integer)this.entityData.get(DATA_TYPE_ID)).intValue());
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 445 */     if (variant == Variant.EVIL) {
/* 446 */       getAttribute(Attributes.ARMOR).setBaseValue(8.0D);
/* 447 */       this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.4D, true));
/* 448 */       this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/* 449 */       this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/* 450 */       this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Wolf.class, true));
/* 451 */       getAttribute(Attributes.ATTACK_DAMAGE).addOrUpdateTransientModifier(new AttributeModifier(EVIL_ATTACK_POWER_MODIFIER, 5.0D, AttributeModifier.Operation.ADD_VALUE));
/* 453 */       if (!hasCustomName())
/* 454 */         setCustomName((Component)Component.translatable(Util.makeDescriptionId("entity", KILLER_BUNNY))); 
/*     */     } else {
/* 457 */       getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(EVIL_ATTACK_POWER_MODIFIER);
/*     */     } 
/* 460 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(variant.id));
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 465 */     if (type == DataComponents.RABBIT_VARIANT)
/* 466 */       return (T)castComponentValue(type, getVariant()); 
/* 469 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 474 */     applyImplicitComponentIfPresent(components, DataComponents.RABBIT_VARIANT);
/* 475 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 480 */     if (type == DataComponents.RABBIT_VARIANT) {
/* 481 */       setVariant((Variant)castComponentValue(DataComponents.RABBIT_VARIANT, value));
/* 482 */       return true;
/*     */     } 
/* 485 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     RabbitGroupData rabbitGroupData;
/* 490 */     Variant variant = getRandomRabbitVariant((LevelAccessor)level, blockPosition());
/* 491 */     if (groupData instanceof RabbitGroupData) {
/* 493 */       variant = ((RabbitGroupData)groupData).variant;
/*     */     } else {
/* 495 */       rabbitGroupData = new RabbitGroupData(variant);
/*     */     } 
/* 498 */     setVariant(variant);
/* 500 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)rabbitGroupData);
/*     */   }
/*     */   
/*     */   private static Variant getRandomRabbitVariant(LevelAccessor level, BlockPos pos) {
/* 504 */     Holder<Biome> biome = level.getBiome(pos);
/* 506 */     int randomVal = level.getRandom().nextInt(100);
/* 507 */     if (biome.is(BiomeTags.SPAWNS_WHITE_RABBITS))
/* 508 */       return (randomVal < 80) ? Variant.WHITE : Variant.WHITE_SPLOTCHED; 
/* 511 */     if (biome.is(BiomeTags.SPAWNS_GOLD_RABBITS))
/* 512 */       return Variant.GOLD; 
/* 515 */     return (randomVal < 50) ? Variant.BROWN : ((randomVal < 90) ? Variant.SALT : Variant.BLACK);
/*     */   }
/*     */   
/*     */   public static boolean checkRabbitSpawnRules(EntityType<Rabbit> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 519 */     return (level.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON) && 
/* 520 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   public static class RabbitGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Rabbit.Variant variant;
/*     */     
/*     */     public RabbitGroupData(Rabbit.Variant variant) {
/* 527 */       super(1.0F);
/* 528 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean wantsMoreFood() {
/* 533 */     return (this.moreCarrotTicks <= 0);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 538 */     if (id == 1) {
/* 539 */       spawnSprintParticle();
/* 540 */       this.jumpDuration = 15;
/* 541 */       this.jumpTicks = 0;
/*     */     } else {
/* 543 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 549 */     return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 553 */     if (shouldPlayIdleAnimation()) {
/* 554 */       this.idleAnimationTimeout = this.random.nextInt(40) + 180;
/* 555 */       this.idleHeadTiltAnimationState.start(this.tickCount);
/* 556 */     } else if (this.jumpTicks > 0) {
/* 557 */       this.hopAnimationState.startIfStopped(this.tickCount);
/* 558 */       this.idleHeadTiltAnimationState.stop();
/*     */     } else {
/* 560 */       this.idleAnimationTimeout--;
/* 561 */       this.hopAnimationState.stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldPlayIdleAnimation() {
/* 566 */     return (this.idleAnimationTimeout <= 0 && (getLeashData() == null || (getLeashData()).leashHolder == null) && !isNoAi());
/*     */   }
/*     */   
/*     */   public void setLeashData(Leashable.LeashData leashData) {
/* 571 */     super.setLeashData(leashData);
/* 572 */     this.idleHeadTiltAnimationState.stop();
/*     */   }
/*     */   
/*     */   public void baseTick() {
/* 577 */     super.baseTick();
/* 578 */     if (level().isClientSide())
/* 579 */       setupAnimationStates(); 
/*     */   }
/*     */   
/*     */   public static class RabbitJumpControl extends JumpControl {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     private boolean canJump;
/*     */     
/*     */     public RabbitJumpControl(Rabbit rabbit) {
/* 588 */       super((Mob)rabbit);
/* 589 */       this.rabbit = rabbit;
/*     */     }
/*     */     
/*     */     public boolean wantJump() {
/* 593 */       return this.jump;
/*     */     }
/*     */     
/*     */     public boolean canJump() {
/* 597 */       return this.canJump;
/*     */     }
/*     */     
/*     */     public void setCanJump(boolean canJump) {
/* 601 */       this.canJump = canJump;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 606 */       if (this.jump) {
/* 607 */         this.rabbit.startJumping();
/* 608 */         this.jump = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitMoveControl<T extends Rabbit> extends MoveControl<T> {
/*     */     private double nextJumpSpeed;
/*     */     
/*     */     public RabbitMoveControl(T rabbit) {
/* 617 */       super((Mob)rabbit);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 622 */       if (((Rabbit)this.mob).onGround() && !((Rabbit)this.mob).jumping && !((Rabbit.RabbitJumpControl)((Rabbit)this.mob).jumpControl).wantJump()) {
/* 623 */         ((Rabbit)this.mob).setSpeedModifier(0.0D);
/* 624 */       } else if (hasWanted() || this.operation == MoveControl.Operation.JUMPING) {
/* 625 */         ((Rabbit)this.mob).setSpeedModifier(this.nextJumpSpeed);
/*     */       } 
/* 627 */       super.tick();
/*     */     }
/*     */     
/*     */     public void setWantedPosition(double x, double y, double z, double speedModifier) {
/* 632 */       if (((Rabbit)this.mob).isInWater())
/* 633 */         speedModifier = 1.5D; 
/* 636 */       super.setWantedPosition(x, y, z, speedModifier);
/* 637 */       if (speedModifier > 0.0D)
/* 638 */         this.nextJumpSpeed = speedModifier; 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitAvoidEntityGoal(Rabbit rabbit, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 647 */       super((PathfinderMob)rabbit, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 648 */       this.rabbit = rabbit;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 653 */       return (this.rabbit.getVariant() != Rabbit.Variant.EVIL && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RaidGardenGoal extends MoveToBlockGoal {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     private boolean wantsToRaid;
/*     */     
/*     */     private boolean canRaid;
/*     */     
/*     */     public RaidGardenGoal(Rabbit rabbit) {
/* 664 */       super((PathfinderMob)rabbit, 0.699999988079071D, 16);
/* 665 */       this.rabbit = rabbit;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 670 */       if (this.nextStartTick <= 0) {
/* 671 */         if (!((Boolean)getServerLevel((Entity)this.rabbit).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 672 */           return false; 
/* 676 */         this.canRaid = false;
/* 677 */         this.wantsToRaid = this.rabbit.wantsMoreFood();
/*     */       } 
/* 680 */       return super.canUse();
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 685 */       return (this.canRaid && super.canContinueToUse());
/*     */     }
/*     */     
/*     */     public void tick() {
/* 690 */       super.tick();
/* 692 */       this.rabbit.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, 10.0F, this.rabbit.getMaxHeadXRot());
/* 694 */       if (isReachedTarget()) {
/* 695 */         Level level = this.rabbit.level();
/* 696 */         BlockPos cropsPos = this.blockPos.above();
/* 698 */         BlockState blockState = level.getBlockState(cropsPos);
/* 699 */         Block block = blockState.getBlock();
/* 701 */         if (this.canRaid && block instanceof CarrotBlock) {
/* 702 */           int carrotAge = ((Integer)blockState.getValue((Property)CarrotBlock.AGE)).intValue();
/* 703 */           if (carrotAge == 0) {
/* 704 */             level.setBlock(cropsPos, Blocks.AIR.defaultBlockState(), 2);
/* 705 */             level.destroyBlock(cropsPos, true, (Entity)this.rabbit);
/*     */           } else {
/* 707 */             level.setBlock(cropsPos, (BlockState)blockState.setValue((Property)CarrotBlock.AGE, Integer.valueOf(carrotAge - 1)), 2);
/* 708 */             level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, cropsPos, GameEvent.Context.of((Entity)this.rabbit));
/* 709 */             level.levelEvent(2001, cropsPos, Block.getId(blockState));
/*     */           } 
/* 711 */           this.rabbit.moreCarrotTicks = 40;
/*     */         } 
/* 714 */         this.canRaid = false;
/* 717 */         this.nextStartTick = 10;
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 723 */       BlockState state = level.getBlockState(pos);
/* 725 */       if (state.is(BlockTags.SUPPORTS_CROPS) && this.wantsToRaid && !this.canRaid) {
/* 726 */         state = level.getBlockState(pos.above());
/* 728 */         Block block = state.getBlock();
/* 728 */         if (block instanceof CarrotBlock) {
/* 728 */           CarrotBlock carrotBlock = (CarrotBlock)block;
/* 728 */           if (carrotBlock.isMaxAge(state)) {
/* 729 */             this.canRaid = true;
/* 730 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 733 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RabbitPanicGoal extends PanicGoal {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitPanicGoal(Rabbit rabbit, double speedModifier) {
/* 741 */       super((PathfinderMob)rabbit, speedModifier);
/* 742 */       this.rabbit = rabbit;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 747 */       super.tick();
/* 749 */       this.rabbit.setSpeedModifier(this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */