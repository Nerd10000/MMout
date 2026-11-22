/*     */ package net.minecraft.world.entity.animal.parrot;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowMobGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Parrot extends ShoulderRidingEntity {
/*  78 */   private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Parrot.class, EntityDataSerializers.INT);
/*     */   
/*  79 */   private static final Predicate<Mob> NOT_PARROT_PREDICATE = new Predicate<Mob>() {
/*     */       public boolean test(Mob input) {
/*  82 */         return (input != null && Parrot.MOB_SOUND_MAP.containsKey(input.getType()));
/*     */       }
/*     */     };
/*     */   
/*     */   private static final Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP;
/*     */   
/*     */   public float flap;
/*     */   
/*     */   public float flapSpeed;
/*     */   
/*     */   public float oFlapSpeed;
/*     */   
/*     */   public float oFlap;
/*     */   
/*     */   static {
/*  86 */     MOB_SOUND_MAP = (Map<EntityType<?>, SoundEvent>)Util.make(Maps.newHashMap(), map -> {
/*     */           map.put(EntityTypes.BLAZE, SoundEvents.PARROT_IMITATE_BLAZE);
/*     */           map.put(EntityTypes.BOGGED, SoundEvents.PARROT_IMITATE_BOGGED);
/*     */           map.put(EntityTypes.BREEZE, SoundEvents.PARROT_IMITATE_BREEZE);
/*     */           map.put(EntityTypes.CAMEL_HUSK, SoundEvents.PARROT_IMITATE_CAMEL_HUSK);
/*     */           map.put(EntityTypes.CAVE_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/*     */           map.put(EntityTypes.CREAKING, SoundEvents.PARROT_IMITATE_CREAKING);
/*     */           map.put(EntityTypes.CREEPER, SoundEvents.PARROT_IMITATE_CREEPER);
/*     */           map.put(EntityTypes.DROWNED, SoundEvents.PARROT_IMITATE_DROWNED);
/*     */           map.put(EntityTypes.ELDER_GUARDIAN, SoundEvents.PARROT_IMITATE_ELDER_GUARDIAN);
/*     */           map.put(EntityTypes.ENDER_DRAGON, SoundEvents.PARROT_IMITATE_ENDER_DRAGON);
/*     */           map.put(EntityTypes.ENDERMITE, SoundEvents.PARROT_IMITATE_ENDERMITE);
/*     */           map.put(EntityTypes.EVOKER, SoundEvents.PARROT_IMITATE_EVOKER);
/*     */           map.put(EntityTypes.GHAST, SoundEvents.PARROT_IMITATE_GHAST);
/*     */           map.put(EntityTypes.HAPPY_GHAST, SoundEvents.EMPTY);
/*     */           map.put(EntityTypes.GUARDIAN, SoundEvents.PARROT_IMITATE_GUARDIAN);
/*     */           map.put(EntityTypes.HOGLIN, SoundEvents.PARROT_IMITATE_HOGLIN);
/*     */           map.put(EntityTypes.HUSK, SoundEvents.PARROT_IMITATE_HUSK);
/*     */           map.put(EntityTypes.ILLUSIONER, SoundEvents.PARROT_IMITATE_ILLUSIONER);
/*     */           map.put(EntityTypes.MAGMA_CUBE, SoundEvents.PARROT_IMITATE_MAGMA_CUBE);
/*     */           map.put(EntityTypes.PARCHED, SoundEvents.PARROT_IMITATE_PARCHED);
/*     */           map.put(EntityTypes.PHANTOM, SoundEvents.PARROT_IMITATE_PHANTOM);
/*     */           map.put(EntityTypes.PIGLIN, SoundEvents.PARROT_IMITATE_PIGLIN);
/*     */           map.put(EntityTypes.PIGLIN_BRUTE, SoundEvents.PARROT_IMITATE_PIGLIN_BRUTE);
/*     */           map.put(EntityTypes.PILLAGER, SoundEvents.PARROT_IMITATE_PILLAGER);
/*     */           map.put(EntityTypes.RAVAGER, SoundEvents.PARROT_IMITATE_RAVAGER);
/*     */           map.put(EntityTypes.SHULKER, SoundEvents.PARROT_IMITATE_SHULKER);
/*     */           map.put(EntityTypes.SILVERFISH, SoundEvents.PARROT_IMITATE_SILVERFISH);
/*     */           map.put(EntityTypes.SKELETON, SoundEvents.PARROT_IMITATE_SKELETON);
/*     */           map.put(EntityTypes.SLIME, SoundEvents.PARROT_IMITATE_SLIME);
/*     */           map.put(EntityTypes.SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/*     */           map.put(EntityTypes.STRAY, SoundEvents.PARROT_IMITATE_STRAY);
/*     */           map.put(EntityTypes.VEX, SoundEvents.PARROT_IMITATE_VEX);
/*     */           map.put(EntityTypes.VINDICATOR, SoundEvents.PARROT_IMITATE_VINDICATOR);
/*     */           map.put(EntityTypes.WARDEN, SoundEvents.PARROT_IMITATE_WARDEN);
/*     */           map.put(EntityTypes.WITCH, SoundEvents.PARROT_IMITATE_WITCH);
/*     */           map.put(EntityTypes.WITHER, SoundEvents.PARROT_IMITATE_WITHER);
/*     */           map.put(EntityTypes.WITHER_SKELETON, SoundEvents.PARROT_IMITATE_WITHER_SKELETON);
/*     */           map.put(EntityTypes.ZOGLIN, SoundEvents.PARROT_IMITATE_ZOGLIN);
/*     */           map.put(EntityTypes.ZOMBIE, SoundEvents.PARROT_IMITATE_ZOMBIE);
/*     */           map.put(EntityTypes.ZOMBIE_HORSE, SoundEvents.PARROT_IMITATE_ZOMBIE_HORSE);
/*     */           map.put(EntityTypes.ZOMBIE_NAUTILUS, SoundEvents.PARROT_IMITATE_ZOMBIE_NAUTILUS);
/*     */           map.put(EntityTypes.ZOMBIE_VILLAGER, SoundEvents.PARROT_IMITATE_ZOMBIE_VILLAGER);
/*     */         });
/*     */   }
/*     */   
/* 135 */   private float flapping = 1.0F;
/*     */   
/* 136 */   private float nextFlap = 1.0F;
/*     */   
/*     */   private boolean partyParrot;
/*     */   
/*     */   private BlockPos jukebox;
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 142 */     RED_BLUE(0, "red_blue"),
/* 143 */     BLUE(1, "blue"),
/* 144 */     GREEN(2, "green"),
/* 145 */     YELLOW_BLUE(3, "yellow_blue"),
/* 146 */     GRAY(4, "gray");
/*     */     
/* 149 */     public static final Variant DEFAULT = RED_BLUE;
/*     */     
/* 151 */     private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/* 152 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/*     */     @Deprecated
/* 154 */     public static final Codec<Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */     
/* 156 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
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
/* 162 */       this.id = id;
/* 163 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getId() {
/* 167 */       return this.id;
/*     */     }
/*     */     
/*     */     public static Variant byId(int id) {
/* 171 */       return BY_ID.apply(id);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 176 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public Parrot(EntityType<? extends Parrot> type, Level level) {
/* 181 */     super((EntityType)type, level);
/* 182 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 10, false);
/* 186 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
/* 187 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/* 188 */     setPathfindingMalus(PathType.COCOA, -1.0F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 193 */     setVariant((Variant)Util.getRandom((Object[])Variant.values(), level.getRandom()));
/* 195 */     if (groupData == null)
/* 196 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false); 
/* 199 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 204 */     return false;
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 209 */     this.goalSelector.addGoal(0, (Goal)new TamableAnimal.TamableAnimalPanicGoal(this, 1.25D));
/* 210 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 211 */     this.goalSelector.addGoal(1, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 212 */     this.goalSelector.addGoal(2, (Goal)new SitWhenOrderedToGoal(this));
/* 213 */     this.goalSelector.addGoal(2, (Goal)new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F));
/* 214 */     this.goalSelector.addGoal(2, (Goal)new ParrotWanderGoal((PathfinderMob)this, 1.0D));
/* 215 */     this.goalSelector.addGoal(3, (Goal)new LandOnOwnersShoulderGoal(this));
/* 216 */     this.goalSelector.addGoal(3, (Goal)new FollowMobGoal((Mob)this, 1.0D, 3.0F, 7.0F));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 220 */     return Animal.createAnimalAttributes()
/* 221 */       .add(Attributes.MAX_HEALTH, 6.0D)
/* 222 */       .add(Attributes.FLYING_SPEED, 0.4000000059604645D)
/* 223 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 224 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 229 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level);
/* 230 */     flyingPathNavigation.setCanOpenDoors(false);
/* 231 */     flyingPathNavigation.setCanFloat(true);
/* 232 */     return (PathNavigation)flyingPathNavigation;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 237 */     if (this.jukebox == null || !this.jukebox.closerToCenterThan((Position)position(), 3.46D) || !level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
/* 238 */       this.partyParrot = false;
/* 239 */       this.jukebox = null;
/*     */     } 
/* 242 */     if (level().getRandom().nextInt(400) == 0)
/* 243 */       imitateNearbyMobs(level(), (Entity)this); 
/* 246 */     super.aiStep();
/* 248 */     calculateFlapping();
/*     */   }
/*     */   
/*     */   public void setRecordPlayingNearby(BlockPos jukebox, boolean isPlaying) {
/* 253 */     this.jukebox = jukebox;
/* 254 */     this.partyParrot = isPlaying;
/*     */   }
/*     */   
/*     */   public boolean isPartyParrot() {
/* 258 */     return this.partyParrot;
/*     */   }
/*     */   
/*     */   private void calculateFlapping() {
/* 262 */     this.oFlap = this.flap;
/* 263 */     this.oFlapSpeed = this.flapSpeed;
/* 265 */     this.flapSpeed += ((onGround() || isPassenger()) ? -1 : 4) * 0.3F;
/* 266 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/* 268 */     if (!onGround() && this.flapping < 1.0F)
/* 269 */       this.flapping = 1.0F; 
/* 271 */     this.flapping *= 0.9F;
/* 273 */     Vec3 movement = getDeltaMovement();
/* 274 */     if (!onGround() && movement.y < 0.0D)
/* 275 */       setDeltaMovement(movement.multiply(1.0D, 0.6D, 1.0D)); 
/* 278 */     this.flap += this.flapping * 2.0F;
/*     */   }
/*     */   
/*     */   public static boolean imitateNearbyMobs(Level level, Entity entity) {
/* 282 */     RandomSource random = level.getRandom();
/* 283 */     if (!entity.isAlive() || entity.isSilent() || random.nextInt(2) != 0)
/* 284 */       return false; 
/* 287 */     List<Mob> mobs = level.getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(20.0D), NOT_PARROT_PREDICATE);
/* 288 */     if (!mobs.isEmpty()) {
/* 289 */       Mob mob = mobs.get(random.nextInt(mobs.size()));
/* 290 */       if (!mob.isSilent()) {
/* 291 */         SoundEvent soundEvent = getImitatedSound(mob.getType());
/* 292 */         level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(), 0.7F, getPitch(random));
/* 294 */         return true;
/*     */       } 
/*     */     } 
/* 298 */     return false;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 303 */     ItemStack itemStack = player.getItemInHand(hand);
/* 305 */     if (!isTame() && itemStack.is(ItemTags.PARROT_FOOD)) {
/* 306 */       usePlayerItem(player, hand, itemStack);
/* 308 */       if (!isSilent())
/* 309 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.PARROT_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F); 
/* 312 */       if (!level().isClientSide())
/* 313 */         if (this.random.nextInt(10) == 0) {
/* 314 */           tame(player);
/* 315 */           level().broadcastEntityEvent((Entity)this, (byte)7);
/*     */         } else {
/* 317 */           level().broadcastEntityEvent((Entity)this, (byte)6);
/*     */         }  
/* 321 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 322 */     if (itemStack.is(ItemTags.PARROT_POISONOUS_FOOD)) {
/* 323 */       usePlayerItem(player, hand, itemStack);
/* 325 */       addEffect(new MobEffectInstance(MobEffects.POISON, 900));
/* 326 */       if (player.isCreative() || !isInvulnerable())
/* 327 */         hurt(damageSources().playerAttack(player), Float.MAX_VALUE); 
/* 330 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 331 */     if (!isFlying() && isTame() && isOwnedBy((LivingEntity)player)) {
/* 332 */       if (!level().isClientSide())
/* 333 */         setOrderedToSit(!isOrderedToSit()); 
/* 335 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 338 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 343 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean checkParrotSpawnRules(EntityType<Parrot> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 347 */     return (level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && 
/* 348 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 358 */     return false;
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 363 */     return null;
/*     */   }
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/* 368 */     return getAmbient(level(), level().getRandom());
/*     */   }
/*     */   
/*     */   public static SoundEvent getAmbient(Level level, RandomSource random) {
/* 372 */     if (level.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(1000) == 0) {
/* 374 */       List<EntityType<?>> keys = Lists.newArrayList(MOB_SOUND_MAP.keySet());
/* 375 */       return getImitatedSound(keys.get(random.nextInt(keys.size())));
/*     */     } 
/* 377 */     return SoundEvents.PARROT_AMBIENT;
/*     */   }
/*     */   
/*     */   private static SoundEvent getImitatedSound(EntityType<?> id) {
/* 381 */     return MOB_SOUND_MAP.getOrDefault(id, SoundEvents.PARROT_AMBIENT);
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 386 */     return SoundEvents.PARROT_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 391 */     return SoundEvents.PARROT_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 396 */     playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected boolean isFlapping() {
/* 401 */     return (this.flyDist > this.nextFlap);
/*     */   }
/*     */   
/*     */   protected void onFlap() {
/* 406 */     playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
/* 407 */     this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
/*     */   }
/*     */   
/*     */   public float getVoicePitch() {
/* 412 */     return getPitch(this.random);
/*     */   }
/*     */   
/*     */   public static float getPitch(RandomSource random) {
/* 416 */     return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 421 */     return SoundSource.NEUTRAL;
/*     */   }
/*     */   
/*     */   public boolean isPushable() {
/* 426 */     return true;
/*     */   }
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 431 */     if (entity instanceof Player)
/*     */       return; 
/* 434 */     super.doPush(entity);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 439 */     if (isInvulnerableTo(level, source))
/* 440 */       return false; 
/* 443 */     setOrderedToSit(false);
/* 445 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 449 */     return Variant.byId(((Integer)this.entityData.get(DATA_VARIANT_ID)).intValue());
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 453 */     this.entityData.set(DATA_VARIANT_ID, Integer.valueOf(variant.id));
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 458 */     if (type == DataComponents.PARROT_VARIANT)
/* 459 */       return (T)castComponentValue(type, getVariant()); 
/* 462 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 467 */     applyImplicitComponentIfPresent(components, DataComponents.PARROT_VARIANT);
/* 468 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 473 */     if (type == DataComponents.PARROT_VARIANT) {
/* 474 */       setVariant((Variant)castComponentValue(DataComponents.PARROT_VARIANT, value));
/* 475 */       return true;
/*     */     } 
/* 478 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 483 */     super.defineSynchedData(entityData);
/* 484 */     entityData.define(DATA_VARIANT_ID, Integer.valueOf(Variant.DEFAULT.id));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 489 */     super.addAdditionalSaveData(output);
/* 490 */     output.store("Variant", Variant.LEGACY_CODEC, getVariant());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 495 */     super.readAdditionalSaveData(input);
/* 496 */     setVariant(input.read("Variant", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/*     */   }
/*     */   
/*     */   public boolean isFlying() {
/* 500 */     return !onGround();
/*     */   }
/*     */   
/*     */   protected boolean omnidirectionalAirMover() {
/* 505 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean canFlyToOwner() {
/* 510 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 515 */     return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   private static class ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
/*     */     public ParrotWanderGoal(PathfinderMob mob, double speedModifier) {
/* 520 */       super(mob, speedModifier);
/*     */     }
/*     */     
/*     */     protected Vec3 getPosition() {
/* 525 */       Vec3 pos = null;
/* 526 */       if (this.mob.isInWater())
/* 527 */         pos = LandRandomPos.getPos(this.mob, 15, 15); 
/* 529 */       if (this.mob.getRandom().nextFloat() >= this.probability)
/* 530 */         pos = getTreePos(); 
/* 532 */       return (pos == null) ? super.getPosition() : pos;
/*     */     }
/*     */     
/*     */     private Vec3 getTreePos() {
/* 536 */       BlockPos mobPos = this.mob.blockPosition();
/* 538 */       BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
/* 539 */       BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/* 540 */       Iterable<BlockPos> between = BlockPos.betweenClosed(
/* 541 */           Mth.floor(this.mob.getX() - 3.0D), 
/* 542 */           Mth.floor(this.mob.getY() - 6.0D), 
/* 543 */           Mth.floor(this.mob.getZ() - 3.0D), 
/* 544 */           Mth.floor(this.mob.getX() + 3.0D), 
/* 545 */           Mth.floor(this.mob.getY() + 6.0D), 
/* 546 */           Mth.floor(this.mob.getZ() + 3.0D));
/* 549 */       for (BlockPos pos : between) {
/* 550 */         if (mobPos.equals(pos))
/*     */           continue; 
/* 554 */         BlockState state = this.mob.level().getBlockState((BlockPos)belowPos.setWithOffset((Vec3i)pos, Direction.DOWN));
/* 555 */         boolean canSitOn = (state.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || state.is(BlockTags.LOGS));
/* 556 */         if (canSitOn && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock((BlockPos)abovePos.setWithOffset((Vec3i)pos, Direction.UP)))
/* 557 */           return Vec3.atBottomCenterOf((Vec3i)pos); 
/*     */       } 
/* 561 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\parrot\Parrot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */