/*      */ package net.minecraft.world.entity.monster.cubemob;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import java.util.ArrayList;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Bucketable;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityAttachment;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.Shearable;
/*      */ import net.minecraft.world.entity.SulfurCubeArchetype;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.item.PrimedTnt;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.CustomData;
/*      */ import net.minecraft.world.item.component.SulfurCubeContent;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class SulfurCube extends AbstractCubeMob implements Bucketable, Shearable {
/*      */   public static final int SPLIT_COUNT = 2;
/*      */   
/*      */   public static final int MAX_SIZE = 2;
/*      */   
/*      */   public static final int MIN_SIZE = 1;
/*      */   
/*      */   public static final int PICKUP_TIMER_DURATION = 100;
/*      */   
/*      */   public static final double PUSH_DISTANCE_THRESHOLD = 1.2999999523162842D;
/*      */   
/*   90 */   private int pickupTimer = 0;
/*      */   
/*   91 */   private int pushSoundCooldown = 0;
/*      */   
/*      */   private boolean floatsInLiquids = false;
/*      */   
/*      */   private static final double MAX_PLAYER_PUSH_SPEED = 0.5D;
/*      */   
/*      */   private static final float PLAYER_PUSH_SPEED_SCALE_MULTIPLIER = 0.3F;
/*      */   
/*      */   private static final float VEHICLE_PUSH_SPEED_SCALE_MULTIPLIER = 0.16F;
/*      */   
/*      */   private static final float VERTICAL_PUSH_MULTIPLIER = 0.3F;
/*      */   
/*  100 */   private Optional<SulfurCubeArchetype.ExplosionData> explosionData = Optional.empty();
/*      */   
/*  101 */   private SulfurCubeArchetype.KnockbackModifiers knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
/*      */   
/*  102 */   private SulfurCubeArchetype.SoundSettings soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;
/*      */   
/*  105 */   private int fuse = -1;
/*      */   
/*  106 */   private List<SulfurCubeArchetype.ContactDamage> contactDamages = new ArrayList<>();
/*      */   
/*  108 */   private static final EntityDataAccessor<Integer> MAX_FUSE = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.INT);
/*      */   
/*  110 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.BOOLEAN);
/*      */   
/*      */   private static final boolean DEFAULT_FROM_BUCKET = false;
/*      */   
/*      */   private static final float HORIZONTAL_HIT_ANGLE_SCALE = 1.6F;
/*      */   
/*      */   private static final float VERTICAL_HIT_ANGLE_SCALE = 0.5F;
/*      */   
/*      */   private static final float VERTICAL_POSITION_ANGLE_SCALE = 0.8F;
/*      */   
/*      */   private static final float EXTRA_KNOCKBACK_DAMPENING = 0.25F;
/*      */   
/*      */   private static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*      */   
/*      */   public SulfurCube(EntityType<? extends SulfurCube> type, Level level) {
/*  118 */     super((EntityType)type, level);
/*  119 */     this.lookControl = new SulfurCubeLookControl();
/*  120 */     this.moveControl = new SulfurCubeMobMoveControl<>(this);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  125 */     super.defineSynchedData(entityData);
/*  126 */     entityData.define(FROM_BUCKET, Boolean.valueOf(false));
/*  127 */     entityData.define(MAX_FUSE, Integer.valueOf(-1));
/*      */   }
/*      */   
/*      */   protected void addBehaviourGoals() {
/*  132 */     this.goalSelector.addGoal(2, (Goal)new SulfurCubeTemptGoal((Mob)this, 1.0D, itemStack -> isBaby() ? itemStack.is(ItemTags.SULFUR_CUBE_FOOD) : isSwallowableItem(itemStack), false, 1.0D));
/*  133 */     this.goalSelector.addGoal(3, new SulfurCubeSearchForItemsGoal(this));
/*      */   }
/*      */   
/*      */   public boolean fromBucket() {
/*  138 */     return ((Boolean)this.entityData.get(FROM_BUCKET)).booleanValue();
/*      */   }
/*      */   
/*      */   public int getFuse() {
/*  142 */     return this.fuse;
/*      */   }
/*      */   
/*      */   public boolean isPrimed() {
/*  146 */     return (getFuse() >= 0);
/*      */   }
/*      */   
/*      */   private void setFuse(int fuse) {
/*  150 */     this.fuse = fuse;
/*      */   }
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  155 */     if (MAX_FUSE.equals(accessor))
/*  156 */       setFuse(((Integer)this.entityData.get(MAX_FUSE)).intValue()); 
/*  158 */     super.onSyncedDataUpdated(accessor);
/*      */   }
/*      */   
/*      */   public void setFromBucket(boolean fromBucket) {
/*  163 */     this.entityData.set(FROM_BUCKET, Boolean.valueOf(fromBucket));
/*      */   }
/*      */   
/*      */   public SoundEvent getPickupSound() {
/*  168 */     return SoundEvents.BUCKET_FILL_SULFUR_CUBE;
/*      */   }
/*      */   
/*      */   public void saveToBucketTag(ItemStack bucket) {
/*  173 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/*  174 */     bucket.copyFrom(DataComponents.SULFUR_CUBE_CONTENT, (DataComponentGetter)this);
/*  175 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/*      */           tag.putInt("age", getAge());
/*      */           tag.putBoolean("age_locked", isAgeLocked());
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean canBreatheUnderwater() {
/*  183 */     return (hasBodyItem() || super.canBreatheUnderwater());
/*      */   }
/*      */   
/*      */   protected void travelInFluid(Vec3 input) {
/*  188 */     super.travelInFluid(input);
/*  189 */     if (!hasBodyItem() || !this.floatsInLiquids)
/*      */       return; 
/*  193 */     float vibeAmount = 0.2F * Mth.sin((this.tickCount * 0.4F));
/*  194 */     double immersion = getFluidHeight(isInWater() ? FluidTags.WATER : FluidTags.LAVA) - getFluidJumpThreshold() + vibeAmount;
/*  195 */     if (immersion > 0.0D)
/*  196 */       setDeltaMovement(getDeltaMovement().add(0.0D, Math.min(1.0D, immersion) * 0.03999999910593033D, 0.0D)); 
/*      */   }
/*      */   
/*      */   public double getFluidJumpThreshold() {
/*  202 */     return getBbHeight() * 0.2D;
/*      */   }
/*      */   
/*      */   public void loadFromBucketTag(CompoundTag tag) {
/*  207 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, tag);
/*  208 */     setAge(tag.getIntOr("age", 0));
/*  209 */     setAgeLocked(tag.getBooleanOr("age_locked", false));
/*      */   }
/*      */   
/*      */   public ItemStack getBucketItemStack() {
/*  214 */     return new ItemStack((ItemLike)Items.SULFUR_CUBE_BUCKET);
/*      */   }
/*      */   
/*      */   protected void addTargetingGoals() {}
/*      */   
/*      */   public float getLightLevelDependentMagicValue() {
/*  223 */     return 1.0F;
/*      */   }
/*      */   
/*      */   protected boolean isDealsDamage() {
/*  228 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean checkSulfurCubeSpawnRules(EntityType<SulfurCube> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  232 */     return true;
/*      */   }
/*      */   
/*      */   public boolean requiresCustomPersistence() {
/*  237 */     return (super.requiresCustomPersistence() || hasBodyItem() || fromBucket());
/*      */   }
/*      */   
/*      */   public boolean canBeLeashed() {
/*  242 */     return hasBodyItem();
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual hasBodyItem : ()Z
/*      */     //   4: ifeq -> 114
/*      */     //   7: aload_0
/*      */     //   8: invokevirtual canExplode : ()Z
/*      */     //   11: ifeq -> 85
/*      */     //   14: aload_0
/*      */     //   15: invokevirtual isPrimed : ()Z
/*      */     //   18: ifne -> 85
/*      */     //   21: aload_2
/*      */     //   22: invokevirtual getDirectEntity : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   25: astore #4
/*      */     //   27: aload_2
/*      */     //   28: getstatic net/minecraft/tags/DamageTypeTags.IS_FIRE : Lnet/minecraft/tags/TagKey;
/*      */     //   31: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*      */     //   34: ifne -> 60
/*      */     //   37: aload #4
/*      */     //   39: instanceof net/minecraft/world/entity/projectile/arrow/AbstractArrow
/*      */     //   42: ifeq -> 69
/*      */     //   45: aload #4
/*      */     //   47: checkcast net/minecraft/world/entity/projectile/arrow/AbstractArrow
/*      */     //   50: astore #5
/*      */     //   52: aload #5
/*      */     //   54: invokevirtual isOnFire : ()Z
/*      */     //   57: ifeq -> 69
/*      */     //   60: aload_0
/*      */     //   61: iconst_0
/*      */     //   62: invokevirtual primeTime : (Z)Z
/*      */     //   65: pop
/*      */     //   66: goto -> 85
/*      */     //   69: aload_2
/*      */     //   70: getstatic net/minecraft/tags/DamageTypeTags.IS_EXPLOSION : Lnet/minecraft/tags/TagKey;
/*      */     //   73: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*      */     //   76: ifeq -> 85
/*      */     //   79: aload_0
/*      */     //   80: iconst_1
/*      */     //   81: invokevirtual primeTime : (Z)Z
/*      */     //   84: pop
/*      */     //   85: aload_2
/*      */     //   86: getstatic net/minecraft/tags/DamageTypeTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO : Lnet/minecraft/tags/TagKey;
/*      */     //   89: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*      */     //   92: ifeq -> 114
/*      */     //   95: aload_2
/*      */     //   96: getstatic net/minecraft/tags/DamageTypeTags.NO_KNOCKBACK : Lnet/minecraft/tags/TagKey;
/*      */     //   99: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*      */     //   102: ifne -> 112
/*      */     //   105: aload_0
/*      */     //   106: aload_2
/*      */     //   107: fload_3
/*      */     //   108: iconst_1
/*      */     //   109: invokevirtual dealDefaultKnockback : (Lnet/minecraft/world/damagesource/DamageSource;FZ)V
/*      */     //   112: iconst_1
/*      */     //   113: ireturn
/*      */     //   114: aload_0
/*      */     //   115: aload_1
/*      */     //   116: aload_2
/*      */     //   117: fload_3
/*      */     //   118: invokespecial hurtServer : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z
/*      */     //   121: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #247	-> 0
/*      */     //   #248	-> 7
/*      */     //   #249	-> 21
/*      */     //   #250	-> 27
/*      */     //   #251	-> 60
/*      */     //   #252	-> 69
/*      */     //   #253	-> 79
/*      */     //   #256	-> 85
/*      */     //   #258	-> 95
/*      */     //   #259	-> 105
/*      */     //   #261	-> 112
/*      */     //   #264	-> 114
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   52	8	5	projectile	Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;
/*      */     //   27	58	4	sourceEntity	Lnet/minecraft/world/entity/Entity;
/*      */     //   0	122	0	this	Lnet/minecraft/world/entity/monster/cubemob/SulfurCube;
/*      */     //   0	122	1	level	Lnet/minecraft/server/level/ServerLevel;
/*      */     //   0	122	2	source	Lnet/minecraft/world/damagesource/DamageSource;
/*      */     //   0	122	3	damage	F
/*      */   }
/*      */   
/*      */   public boolean hasBodyItem() {
/*  268 */     return !getItemBySlot(EquipmentSlot.BODY).isEmpty();
/*      */   }
/*      */   
/*      */   public boolean canExplode() {
/*  272 */     return (this.explosionData.isPresent() && isAlive() && !isPrimed());
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public List<SulfurCubeArchetype> matchingArchetypes(ItemStack stack) {
/*  277 */     return (List<SulfurCubeArchetype>)level().registryAccess().lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE)
/*  278 */       .stream()
/*  279 */       .filter(arch -> stack.is(arch.items()))
/*  280 */       .collect(Collectors.toCollection(ArrayList::new));
/*      */   }
/*      */   
/*      */   public void tick() {
/*  285 */     tickFuse();
/*  286 */     primeWhenOnPoweredPosition();
/*  287 */     super.tick();
/*      */   }
/*      */   
/*      */   private void tickFuse() {
/*  291 */     if (this.fuse > 0)
/*  292 */       this.fuse--; 
/*  295 */     if (this.explosionData.isEmpty())
/*      */       return; 
/*  298 */     if (this.fuse == 0) {
/*  299 */       dropLeash();
/*  300 */       this.dead = true;
/*  301 */       Level level = level();
/*  301 */       if (level instanceof ServerLevel) {
/*  301 */         ServerLevel serverLevel = (ServerLevel)level;
/*  302 */         if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*  303 */           Level.ExplosionInteraction explosionInteraction = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
/*  304 */           serverLevel.explode((Entity)this, Explosion.getDefaultDamageSource(level(), (Entity)this), (getPortalCooldown() > 0) ? PrimedTnt.USED_PORTAL_DAMAGE_CALCULATOR : null, getX(), getY(0.0625D), getZ(), ((SulfurCubeArchetype.ExplosionData)this.explosionData.get()).power(), ((SulfurCubeArchetype.ExplosionData)this.explosionData.get()).causesFire(), explosionInteraction);
/*      */         } 
/*  306 */         triggerOnDeathMobEffects(serverLevel, Entity.RemovalReason.KILLED);
/*      */       } 
/*  308 */       discard();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void primeWhenOnPoweredPosition() {
/*  313 */     Level level = level();
/*  313 */     if (level instanceof ServerLevel) {
/*  313 */       ServerLevel serverLevel = (ServerLevel)level;
/*  314 */       if (canExplode()) {
/*  315 */         BlockPos here = BlockPos.containing((Position)position());
/*  316 */         if (serverLevel.getBestOwnOrNeighbourSignal(here) != 0)
/*  317 */           primeTime(false); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean primeTime(boolean imminent) {
/*  324 */     if (!this.explosionData.isEmpty() && isAlive()) {
/*  324 */       Level level = level();
/*  324 */       if (level instanceof ServerLevel) {
/*  324 */         ServerLevel serverLevel = (ServerLevel)level;
/*  324 */         if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue() && !isPrimed()) {
/*  327 */           int fuse = ((SulfurCubeArchetype.ExplosionData)this.explosionData.get()).fuse();
/*  328 */           int fuseTime = imminent ? PrimedTnt.getRandomShortFuse(fuse, getRandom()) : fuse;
/*  329 */           setInvulnerable(true);
/*  330 */           setFuse(fuseTime);
/*  331 */           this.entityData.set(MAX_FUSE, Integer.valueOf(fuseTime));
/*  332 */           makeSound(SoundEvents.TNT_PRIMED);
/*  333 */           gameEvent((Holder)GameEvent.PRIME_FUSE);
/*  334 */           return true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     return false;
/*      */   }
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {
/*  339 */     super.customServerAiStep(level);
/*  340 */     if (this.pickupTimer > 0)
/*  341 */       this.pickupTimer--; 
/*  343 */     if (this.pushSoundCooldown > 0)
/*  344 */       this.pushSoundCooldown--; 
/*      */   }
/*      */   
/*      */   protected Map<EquipmentSlot, ItemStack> collectEquipmentChanges(Map<EquipmentSlot, ItemStack> lastEquipmentItems) {
/*  350 */     ItemStack previous = lastEquipmentItems.get(EquipmentSlot.BODY);
/*  351 */     ItemStack current = getItemBySlot(EquipmentSlot.BODY);
/*  353 */     if (equipmentHasChanged(previous, current)) {
/*  354 */       if (!current.isEmpty()) {
/*  355 */         removeAllGoals(g -> true);
/*  356 */         setSpeed(0.0F);
/*      */       } else {
/*  358 */         registerGoals();
/*      */       } 
/*  361 */       for (SulfurCubeArchetype archetype : matchingArchetypes(previous)) {
/*  362 */         for (SulfurCubeArchetype.AttributeEntry mod : archetype.attributeModifiers()) {
/*  363 */           AttributeInstance attr = getAttribute(mod.attribute());
/*  364 */           if (attr != null)
/*  365 */             attr.removeModifier(mod.modifier()); 
/*      */         } 
/*      */       } 
/*  369 */       this.floatsInLiquids = false;
/*  370 */       this.explosionData = Optional.empty();
/*  371 */       this.contactDamages.clear();
/*  372 */       this.knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
/*  373 */       this.soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;
/*  374 */       for (SulfurCubeArchetype archetype : matchingArchetypes(current)) {
/*  375 */         if (archetype.buoyant())
/*  376 */           this.floatsInLiquids = true; 
/*  378 */         if (archetype.explosion().isPresent())
/*  379 */           this.explosionData = archetype.explosion(); 
/*  381 */         if (archetype.contactDamage().isPresent())
/*  382 */           this.contactDamages.add(archetype.contactDamage().get()); 
/*  384 */         this.knockbackModifier = archetype.knockbackModifiers();
/*  385 */         this.soundSettings = archetype.soundSettings();
/*  386 */         for (SulfurCubeArchetype.AttributeEntry mod : archetype.attributeModifiers()) {
/*  387 */           AttributeInstance attr = getAttribute(mod.attribute());
/*  388 */           if (attr != null)
/*  389 */             attr.addOrUpdateTransientModifier(mod.modifier()); 
/*      */         } 
/*      */       } 
/*      */     } 
/*  395 */     return super.collectEquipmentChanges(lastEquipmentItems);
/*      */   }
/*      */   
/*      */   public float maxUpStep() {
/*  400 */     if (hasBodyItem())
/*  401 */       return 0.0F; 
/*  403 */     return super.maxUpStep();
/*      */   }
/*      */   
/*      */   protected boolean omnidirectionalAirMover() {
/*  408 */     return hasBodyItem();
/*      */   }
/*      */   
/*      */   public boolean canFreeze() {
/*  413 */     if (hasBodyItem())
/*  414 */       return false; 
/*  416 */     return super.canFreeze();
/*      */   }
/*      */   
/*      */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  421 */     ItemStack heldItem = player.getItemInHand(hand);
/*  422 */     if (isBaby()) {
/*  423 */       if (isFood(heldItem) && canAgeUp()) {
/*  424 */         int age = getAge();
/*  425 */         usePlayerItem(player, hand, heldItem);
/*  426 */         ageUp(getSpeedUpSecondsWhenFeeding(-age), true);
/*  427 */         playEatingSound();
/*  428 */         return (InteractionResult)InteractionResult.SUCCESS;
/*      */       } 
/*  430 */       return super.mobInteract(player, hand);
/*      */     } 
/*  433 */     if (isPrimed())
/*  435 */       return (InteractionResult)InteractionResult.PASS; 
/*  437 */     if (canExplode() && (
/*  438 */       heldItem.is(Items.FLINT_AND_STEEL) || heldItem.is(Items.FIRE_CHARGE))) {
/*  439 */       Level level = level();
/*  439 */       if (level instanceof ServerLevel) {
/*  439 */         ServerLevel serverLevel = (ServerLevel)level;
/*  439 */         if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*  440 */           player.sendOverlayMessage((Component)Component.translatable("block.minecraft.tnt.disabled"));
/*  441 */           return (InteractionResult)InteractionResult.PASS;
/*      */         } 
/*      */       } 
/*  443 */       primeTime(false);
/*  444 */       if (heldItem.is(Items.FLINT_AND_STEEL)) {
/*  445 */         heldItem.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*      */       } else {
/*  447 */         heldItem.consume(1, (LivingEntity)player);
/*      */       } 
/*  449 */       player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
/*  450 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*      */     } 
/*  453 */     if (heldItem.is(Items.SHEARS) && readyForShearing()) {
/*  454 */       Level level = level();
/*  454 */       if (level instanceof ServerLevel) {
/*  454 */         ServerLevel serverLevel = (ServerLevel)level;
/*  455 */         ItemStack itemStackToShear = getItemBySlot(EquipmentSlot.BODY);
/*  456 */         shear(serverLevel, SoundSource.PLAYERS, heldItem);
/*  457 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/*  458 */         heldItem.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*  459 */         CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer)player, itemStackToShear, (Entity)this);
/*      */       } 
/*  461 */       return (InteractionResult)InteractionResult.SUCCESS;
/*      */     } 
/*  462 */     if (isSwallowableItem(heldItem)) {
/*  463 */       boolean itWorked = equipItem(heldItem);
/*  464 */       if (itWorked) {
/*  465 */         heldItem.consume(1, (LivingEntity)player);
/*  466 */         gameEvent((Holder)GameEvent.ENTITY_INTERACT);
/*      */       } 
/*  468 */       return itWorked ? (InteractionResult)InteractionResult.SUCCESS_SERVER : (InteractionResult)InteractionResult.PASS;
/*      */     } 
/*  470 */     return Bucketable.bucketMobPickup(player, hand, (LivingEntity)this).orElse(super.mobInteract(player, hand));
/*      */   }
/*      */   
/*      */   public boolean equipItem(ItemStack heldItem) {
/*  474 */     if (isBaby())
/*  475 */       return false; 
/*  477 */     if (hasBodyItem()) {
/*  478 */       Item swallowedItem = getItemBySlot(EquipmentSlot.BODY).getItem();
/*  479 */       if (heldItem.is(swallowedItem))
/*  480 */         return false; 
/*  482 */       Vec3 equipmentSpawnOffset = getAttachments().getAverage(EntityAttachment.PASSENGER);
/*  483 */       Level level = level();
/*  483 */       if (level instanceof ServerLevel) {
/*  483 */         ServerLevel serverLevel = (ServerLevel)level;
/*  484 */         spawnAtLocation(serverLevel, getItemBySlot(EquipmentSlot.BODY), equipmentSpawnOffset);
/*      */       } 
/*      */     } 
/*  487 */     if (!level().isClientSide()) {
/*  488 */       ItemStack swallowedItem = getItemBySlot(EquipmentSlot.BODY);
/*  489 */       setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, heldItem.copyWithCount(1));
/*  491 */       if (!swallowedItem.isEmpty()) {
/*  492 */         Map<EquipmentSlot, ItemStack> lastEquipmentItems = Util.makeEnumMap(EquipmentSlot.class, slot -> ItemStack.EMPTY);
/*  493 */         lastEquipmentItems.put(EquipmentSlot.BODY, swallowedItem);
/*  494 */         collectEquipmentChanges(lastEquipmentItems);
/*      */       } 
/*      */     } 
/*  497 */     playSound(getAbsorbSound());
/*  498 */     return true;
/*      */   }
/*      */   
/*      */   private void applyContactDamage(Entity entity) {
/*  502 */     Level level = level();
/*  502 */     if (level instanceof ServerLevel) {
/*  502 */       ServerLevel serverLevel = (ServerLevel)level;
/*  503 */       for (SulfurCubeArchetype.ContactDamage damage : this.contactDamages)
/*  504 */         entity.hurtServer(serverLevel, new DamageSource(damage.damageType(), damage.attributeToSource() ? (Entity)this : null), damage.amount().sample(getRandom())); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void playEatingSound() {
/*  510 */     makeSound(SoundEvents.SULFUR_CUBE_SMALL_EAT);
/*      */   }
/*      */   
/*      */   public boolean canBePickedUpWithBucket(ItemStack itemStack) {
/*  515 */     return (itemStack.getItem() == Items.BUCKET);
/*      */   }
/*      */   
/*      */   public EquipmentSlot getEquipmentSlotForItem(ItemStack itemStack) {
/*  520 */     if (isSwallowableItem(itemStack))
/*  521 */       return EquipmentSlot.BODY; 
/*  523 */     return super.getEquipmentSlotForItem(itemStack);
/*      */   }
/*      */   
/*      */   public boolean isEquippableInSlot(ItemStack itemStack, EquipmentSlot slot) {
/*  528 */     if (slot == EquipmentSlot.BODY)
/*  529 */       return isSwallowableItem(itemStack); 
/*  531 */     return super.isEquippableInSlot(itemStack, slot);
/*      */   }
/*      */   
/*      */   public SoundSource getSoundSource() {
/*  536 */     return SoundSource.NEUTRAL;
/*      */   }
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  541 */     if (isTiny())
/*  542 */       return SoundEvents.SULFUR_CUBE_SMALL_HURT; 
/*  544 */     return SoundEvents.SULFUR_CUBE_HURT;
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  550 */     if (isTiny())
/*  551 */       return SoundEvents.SULFUR_CUBE_SMALL_DEATH; 
/*  553 */     return SoundEvents.SULFUR_CUBE_DEATH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSquishSound() {
/*  559 */     if (isTiny())
/*  560 */       return SoundEvents.SULFUR_CUBE_SMALL_SQUISH; 
/*  561 */     if (hasBodyItem())
/*  562 */       return SoundEvents.SULFUR_CUBE_BOUNCE; 
/*  564 */     return SoundEvents.SULFUR_CUBE_SQUISH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getJumpSound() {
/*  570 */     return isTiny() ? SoundEvents.SULFUR_CUBE_SMALL_JUMP : SoundEvents.SULFUR_CUBE_JUMP;
/*      */   }
/*      */   
/*      */   private SoundEvent getAbsorbSound() {
/*  574 */     return SoundEvents.SULFUR_CUBE_ABSORB;
/*      */   }
/*      */   
/*      */   private SoundEvent getEjectSound() {
/*  578 */     return SoundEvents.SULFUR_CUBE_EJECT;
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  583 */     if (!hasBodyItem())
/*  584 */       super.playStepSound(pos, blockState); 
/*      */   }
/*      */   
/*      */   protected ParticleOptions getParticleType() {
/*  590 */     return (ParticleOptions)ParticleTypes.SULFUR_CUBE_GOO;
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createSulfurCubeAttributes() {
/*  594 */     return Mob.createMobAttributes()
/*  595 */       .add(Attributes.TEMPT_RANGE, 8.0D);
/*      */   }
/*      */   
/*      */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/*  600 */     Vec3 equipmentSpawnOffset = getAttachments().getAverage(EntityAttachment.PASSENGER);
/*  601 */     ItemStack itemStackToShear = getItemBySlot(EquipmentSlot.BODY);
/*  602 */     setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
/*  603 */     spawnAtLocation(level, itemStackToShear, equipmentSpawnOffset);
/*  604 */     playSound(getEjectSound());
/*  605 */     this.pickupTimer = 100;
/*      */   }
/*      */   
/*      */   public boolean readyForShearing() {
/*  610 */     return hasBodyItem();
/*      */   }
/*      */   
/*      */   private static class SulfurCubeTemptGoal extends TemptGoal.ForNonPathfinders {
/*      */     public SulfurCubeTemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
/*  616 */       super(mob, speedModifier, items, canScare, stopDistance);
/*  617 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*      */     }
/*      */     
/*      */     protected void stopNavigation() {
/*  622 */       MoveControl moveControl = this.mob.getMoveControl();
/*  622 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/*  622 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/*  623 */         cubeMobMoveControl.setWantedMovement(0.0D);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void navigateTowards(Player player) {
/*  629 */       this.mob.lookAt((Entity)player, 10.0F, 10.0F);
/*  630 */       MoveControl moveControl = this.mob.getMoveControl();
/*  630 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/*  630 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/*  631 */         cubeMobMoveControl.setDirection(this.mob.getYRot(), true);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static {
/*  636 */     ALLOWED_ITEMS = (e -> (!e.hasPickUpDelay() && e.isAlive() && isSwallowableItem(e.getItem())));
/*      */   }
/*      */   
/*      */   private class SulfurCubeSearchForItemsGoal extends Goal {
/*      */     private final SulfurCube sulfurCube;
/*      */     
/*      */     private ItemEntity targetItem;
/*      */     
/*      */     public SulfurCubeSearchForItemsGoal(SulfurCube sulfurCube) {
/*  643 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*  644 */       this.sulfurCube = sulfurCube;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  649 */       if (this.sulfurCube.isBaby() || this.sulfurCube.pickupTimer > 0)
/*  650 */         return false; 
/*  653 */       this.targetItem = (ItemEntity)getServerLevel((Entity)this.sulfurCube).getNearestEntity(this.sulfurCube.level().getEntitiesOfClass(ItemEntity.class, this.sulfurCube.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), SulfurCube.ALLOWED_ITEMS), this.sulfurCube.getX(), this.sulfurCube.getY(), this.sulfurCube.getZ());
/*  654 */       return (this.targetItem != null);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  659 */       SulfurCube.this.lookAt((Entity)this.targetItem, 10.0F, 10.0F);
/*  660 */       MoveControl moveControl = SulfurCube.this.getMoveControl();
/*  660 */       if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/*  660 */         AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/*  661 */         cubeMobMoveControl.setDirection(SulfurCube.this.getYRot(), true);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canPickUpLoot() {
/*  668 */     return !hasBodyItem();
/*      */   }
/*      */   
/*      */   private static boolean isSwallowableItem(ItemStack itemStack) {
/*  672 */     return itemStack.is(ItemTags.SULFUR_CUBE_SWALLOWABLE);
/*      */   }
/*      */   
/*      */   public boolean canUseSlot(EquipmentSlot slot) {
/*  677 */     if (slot == EquipmentSlot.BODY)
/*  678 */       return (isAlive() && !isBaby()); 
/*  680 */     return super.canUseSlot(slot);
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/*  685 */     return (slot == EquipmentSlot.BODY);
/*      */   }
/*      */   
/*      */   public boolean canHoldItem(ItemStack itemStack) {
/*  690 */     ItemStack heldItemStack = getItemBySlot(EquipmentSlot.BODY);
/*  691 */     return (heldItemStack.isEmpty() && isSwallowableItem(itemStack) && !isBaby());
/*      */   }
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  696 */     ItemStack itemStack = entity.getItem();
/*  697 */     if (canHoldItem(itemStack) && this.pickupTimer <= 0) {
/*  698 */       onItemPickup(entity);
/*  699 */       setItemSlot(EquipmentSlot.BODY, itemStack.split(1));
/*  700 */       playSound(getAbsorbSound());
/*  701 */       setGuaranteedDrop(EquipmentSlot.BODY);
/*  702 */       take((Entity)entity, 1);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/*  708 */     return isBaby() ? 0 : (1 + this.random.nextInt(2));
/*      */   }
/*      */   
/*      */   protected int getSplitCount() {
/*  713 */     if (isPrimed())
/*  714 */       return 0; 
/*  716 */     return 2;
/*      */   }
/*      */   
/*      */   protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
/*  721 */     if (isBaby()) {
/*  722 */       setSize(1, true);
/*      */     } else {
/*  724 */       setSize(2, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setSize(int size, boolean updateHealth) {
/*  730 */     super.setSize(size, updateHealth);
/*  732 */     if (updateHealth && size == 1 && !isBaby())
/*  733 */       setBaby(true); 
/*      */   }
/*      */   
/*      */   protected void setUpSplitCube(AbstractCubeMob cubeMob, int halfSize, float xd, float zd) {
/*  739 */     super.setUpSplitCube(cubeMob, halfSize, xd, zd);
/*  740 */     cubeMob.setBaby(true);
/*      */   }
/*      */   
/*      */   public AbstractCubeMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  745 */     SulfurCube sulfurCube = (SulfurCube)EntityTypes.SULFUR_CUBE.create((Level)level, EntitySpawnReason.BREEDING);
/*  746 */     if (sulfurCube != null)
/*  748 */       sulfurCube.setSize(1, true); 
/*  750 */     return sulfurCube;
/*      */   }
/*      */   
/*      */   private boolean isFood(ItemStack itemStack) {
/*  754 */     return itemStack.is(ItemTags.SULFUR_CUBE_FOOD);
/*      */   }
/*      */   
/*      */   protected void ageBoundaryReached() {
/*  759 */     super.ageBoundaryReached();
/*  760 */     if (!isBaby())
/*  761 */       setSize(2, true); 
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  767 */     super.addAdditionalSaveData(output);
/*  768 */     output.putInt("pickup_timer", this.pickupTimer);
/*  769 */     output.putBoolean("from_bucket", fromBucket());
/*  770 */     output.putInt("fuse", getFuse());
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  775 */     this.pickupTimer = input.getIntOr("pickup_timer", 0);
/*  776 */     setFromBucket(input.getBooleanOr("from_bucket", false));
/*  777 */     setFuse(input.getIntOr("fuse", -1));
/*  778 */     this.entityData.set(MAX_FUSE, Integer.valueOf(getFuse()));
/*  779 */     super.readAdditionalSaveData(input);
/*      */   }
/*      */   
/*      */   private class SulfurCubeLookControl extends LookControl {
/*      */     private SulfurCubeLookControl() {
/*  784 */       super((Mob)SulfurCube.this);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  789 */       if (!SulfurCube.this.hasBodyItem()) {
/*  790 */         super.tick();
/*      */         return;
/*      */       } 
/*  793 */       float closeAngle = Mth.wrapDegrees90(SulfurCube.this.getYRot());
/*  794 */       SulfurCube.this.setYRot(SulfurCube.this.getYRot() - closeAngle);
/*  795 */       SulfurCube.this.setYHeadRot(SulfurCube.this.getYRot());
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class SulfurCubeMobMoveControl<T extends SulfurCube> extends AbstractCubeMob.CubeMobMoveControl<T> {
/*      */     public SulfurCubeMobMoveControl(T cubeMob) {
/*  801 */       super(cubeMob);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  806 */       if (!((SulfurCube)this.mob).hasBodyItem())
/*  807 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doPush(Entity entity) {
/*  814 */     super.doPush(entity);
/*  815 */     applyContactDamage(entity);
/*      */   }
/*      */   
/*      */   public void playerTouch(Player player) {
/*  820 */     super.playerTouch(player);
/*  821 */     playerPush(player);
/*      */   }
/*      */   
/*      */   private void playerPush(Player player) {
/*  825 */     if (!hasBodyItem())
/*      */       return; 
/*  829 */     Entity pusher = player.isPassenger() ? player.getRootVehicle() : (Entity)player;
/*  831 */     Vec3 cubeToPusher = position().subtract(pusher.position());
/*  832 */     double pusherFeetPosition = pusher.getY();
/*  833 */     double sulfurCubeBottomPosition = getY();
/*  834 */     double sulfurCubeTopPosition = sulfurCubeBottomPosition + getBbHeight();
/*  835 */     double pusherTopPosition = pusherFeetPosition + pusher.getBbHeight();
/*  836 */     if (cubeToPusher.horizontalDistance() < 1.2999999523162842D && pusherFeetPosition <= sulfurCubeTopPosition && pusherTopPosition > sulfurCubeBottomPosition) {
/*  838 */       double knockback = Math.max(0.0D, 1.0D - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
/*  839 */       Vec3 pushDirection = cubeToPusher.horizontal().normalize().scale(knockback);
/*  840 */       float pushSpeedScale = player.isPassenger() ? 0.16F : 0.3F;
/*  842 */       double playerSpeed = player.getKnownSpeed().length() * 2.0D * pushSpeedScale;
/*  843 */       playerSpeed = Mth.clamp(playerSpeed, 0.0D, 0.5D);
/*  845 */       Vec3 pushVelocity = (new Vec3(pushDirection.x, onGround() ? (knockback * 0.30000001192092896D) : 0.0D, pushDirection.z)).scale(playerSpeed);
/*  847 */       this.needsSync = true;
/*  848 */       float push_sound_threshold = this.soundSettings.pushSoundImpulseThreshold();
/*  849 */       if (pushVelocity.lengthSqr() > (push_sound_threshold * push_sound_threshold) && 
/*  850 */         this.pushSoundCooldown <= 0) {
/*  851 */         this.pushSoundCooldown = (int)(this.soundSettings.pushSoundCooldown() * 20.0F);
/*  852 */         playSound((SoundEvent)this.soundSettings.pushSound().value());
/*      */       } 
/*  855 */       addDeltaMovement(pushVelocity);
/*  856 */       applyContactDamage((Entity)player);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Vec2 applyHorizontalHitAngleScale(float horizontalAngleScale, Vec2 originalAngle, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenter) {
/*  871 */     Vec3 attackerToTarget = targetCenter.subtract(attackerPosition).normalize();
/*  872 */     float angleDiff = (float)Math.atan2(attackerAimDirection.x * attackerToTarget.z - attackerAimDirection.z * attackerToTarget.x, attackerAimDirection.x * attackerToTarget.x + attackerAimDirection.z * attackerToTarget.z);
/*  876 */     return originalAngle.rotate((angleDiff * horizontalAngleScale));
/*      */   }
/*      */   
/*      */   private Vec2 applyVerticalHitAnglePowerTransfer(float verticalHitAngleScale, float horizontalPower, float verticalPower, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenteredPosition, float targetHeight) {
/*  891 */     float targetHalfHeight = 0.5F * targetHeight;
/*  892 */     Vec3 targetTopPos = targetCenteredPosition.add(0.0D, targetHalfHeight, 0.0D);
/*  893 */     Vec3 tagetBottomPos = targetCenteredPosition.add(0.0D, -targetHalfHeight, 0.0D);
/*  894 */     Vec3 attackerToTargetTop = targetTopPos.subtract(attackerPosition).normalize();
/*  895 */     Vec3 attackerToTargetBottom = tagetBottomPos.subtract(attackerPosition).normalize();
/*  898 */     float verticalHitAngleFactor = (float)Mth.clampedMap(attackerAimDirection.y, attackerToTargetTop.y, attackerToTargetBottom.y, -1.0D, 1.0D);
/*  900 */     float transferredPowerRatio = Math.abs(verticalHitAngleFactor * verticalHitAngleScale);
/*  901 */     if (verticalHitAngleFactor < 0.0F)
/*  902 */       transferredPowerRatio = -transferredPowerRatio; 
/*  905 */     float px = horizontalPower * (1.0F - transferredPowerRatio);
/*  906 */     float py = verticalPower * (1.0F + transferredPowerRatio);
/*  907 */     return new Vec2(px, py);
/*      */   }
/*      */   
/*      */   private Vec2 applyVerticalPositionAnglePowerRotation(float verticalPositionAngleScale, float horizontalPower, float verticalPower, float originalHorizontalPower, float originalVerticalPower, Vec3 attackerFeetPosition, Vec3 targetFeetPosition) {
/*  923 */     Vec3 attackerFeetToTargetFeet = targetFeetPosition.subtract(attackerFeetPosition);
/*  924 */     float verticalPositionAngle = (float)Math.atan2(-attackerFeetToTargetFeet.y, attackerFeetToTargetFeet.horizontalDistance());
/*  925 */     Vec2 powerBeforeRotation = new Vec2(horizontalPower, verticalPower);
/*  926 */     Vec2 rotatedPower = powerBeforeRotation.rotate((-verticalPositionAngle * verticalPositionAngleScale));
/*  928 */     float horizontalRatio = (originalHorizontalPower > 0.0F) ? (Mth.abs(rotatedPower.x) / originalHorizontalPower) : 0.0F;
/*  929 */     float verticalRatio = (originalVerticalPower > 0.0F) ? (Mth.abs(rotatedPower.y) / originalVerticalPower) : 0.0F;
/*  930 */     float maxRatio = Math.max(horizontalRatio, verticalRatio);
/*  931 */     if (maxRatio > 1.0F)
/*  932 */       rotatedPower = rotatedPower.scale(1.0F / maxRatio); 
/*  934 */     return rotatedPower;
/*      */   }
/*      */   
/*      */   public void knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {
/*  939 */     if (source.getEntity() == null || !hasBodyItem()) {
/*  940 */       super.knockback(power, xd, zd, source, damage, comesFromEffect);
/*      */       return;
/*      */     } 
/*  944 */     float horizontalHitAngleScale = 1.6F;
/*  945 */     float verticalHitAngleScale = 0.5F;
/*  946 */     float verticalPositionAngleScale = 0.8F;
/*  947 */     float horizontalPower = this.knockbackModifier.horizontalPower();
/*  948 */     float verticalPower = this.knockbackModifier.verticalPower();
/*  949 */     float originalHorizontalPower = horizontalPower;
/*  950 */     float originalVerticalPower = verticalPower;
/*  951 */     Holder<SoundEvent> hitSound = this.soundSettings.hitSound();
/*  954 */     Vec2 originalAngle = new Vec2((float)xd, (float)zd);
/*  957 */     Vec2 newAngle = applyHorizontalHitAngleScale(1.6F, originalAngle, source
/*      */         
/*  960 */         .getEntity().getEyePosition(), source
/*  961 */         .getEntity().getLookAngle().normalize(), 
/*  962 */         getBoundingBox().getCenter());
/*  967 */     Vec2 newPower = applyVerticalHitAnglePowerTransfer(0.5F, horizontalPower, verticalPower, source
/*      */         
/*  971 */         .getEntity().getEyePosition(), source
/*  972 */         .getEntity().getLookAngle().normalize(), 
/*  973 */         getBoundingBox().getCenter(), 
/*  974 */         getBbHeight());
/*  977 */     horizontalPower = newPower.x;
/*  978 */     verticalPower = newPower.y;
/*  983 */     newPower = applyVerticalPositionAnglePowerRotation(0.8F, horizontalPower, verticalPower, originalHorizontalPower, originalVerticalPower, source
/*      */         
/*  989 */         .getEntity().position(), 
/*  990 */         position());
/*  993 */     horizontalPower = newPower.x;
/*  994 */     verticalPower = newPower.y;
/*  998 */     xd = newAngle.x;
/*  999 */     zd = newAngle.y;
/* 1003 */     float powerMultiplier = Mth.sqrt(damage) * (comesFromEffect ? ((float)power * 0.25F) : 1.0F);
/* 1004 */     horizontalPower *= powerMultiplier;
/* 1005 */     verticalPower *= powerMultiplier;
/* 1007 */     double knockBackResistance = getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 1008 */     horizontalPower *= (float)(1.0D - knockBackResistance);
/* 1009 */     verticalPower *= (float)(1.0D - knockBackResistance);
/* 1011 */     this.needsSync = true;
/* 1013 */     Vec3 deltaMovement = getDeltaMovement();
/* 1014 */     horizontalPower *= 0.4F;
/* 1016 */     horizontalPower = Mth.clamp(horizontalPower, -128.0F, 128.0F);
/* 1017 */     verticalPower = Mth.clamp(verticalPower, -128.0F, 128.0F);
/* 1018 */     Vec3 horizontalKnockback = (new Vec3(xd, 0.0D, zd)).normalize().scale(horizontalPower);
/* 1020 */     setDeltaMovement(deltaMovement.x - horizontalKnockback.x, deltaMovement.y + verticalPower * 1.2D, deltaMovement.z - horizontalKnockback.z);
/* 1025 */     playSound((SoundEvent)hitSound.value());
/*      */   }
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/* 1030 */     if (type == DataComponents.SULFUR_CUBE_CONTENT)
/* 1031 */       return (T)castComponentValue(type, getSulfurCubeContent(getBodyArmorItem())); 
/* 1033 */     return (T)super.get(type);
/*      */   }
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 1038 */     applyImplicitComponentIfPresent(components, DataComponents.SULFUR_CUBE_CONTENT);
/* 1039 */     super.applyImplicitComponents(components);
/*      */   }
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 1044 */     if (type == DataComponents.SULFUR_CUBE_CONTENT) {
/* 1045 */       setSulfurCubeContent((SulfurCubeContent)castComponentValue(DataComponents.SULFUR_CUBE_CONTENT, value));
/* 1046 */       return true;
/*      */     } 
/* 1048 */     return super.applyImplicitComponent(type, value);
/*      */   }
/*      */   
/*      */   private static SulfurCubeContent getSulfurCubeContent(ItemStack itemStack) {
/* 1052 */     return itemStack.isEmpty() ? null : SulfurCubeContent.ofNonEmpty(itemStack);
/*      */   }
/*      */   
/*      */   private void setSulfurCubeContent(SulfurCubeContent sulfurCubeContent) {
/* 1056 */     setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, sulfurCubeContent.absorbedBlockItemStack().create());
/*      */   }
/*      */   
/*      */   public Vec3 getLeashOffset() {
/* 1061 */     return new Vec3(0.0D, (getBbHeight() / 2.0F), 0.0D);
/*      */   }
/*      */   
/*      */   protected void setcubeMobHealth(int actualSize) {
/* 1066 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue((4 * actualSize));
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableToPiercingWeapon() {
/* 1071 */     return (isInvulnerable() && !isPrimed());
/*      */   }
/*      */   
/*      */   public boolean canBePickedFromInside() {
/* 1076 */     return !hasBodyItem();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\SulfurCube.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */