/*     */ package net.minecraft.world.entity.animal.pig;
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
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
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
/*     */ import net.minecraft.world.entity.LightningBolt;
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
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Pig extends Animal implements ItemSteerable {
/*  65 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.INT);
/*     */   
/*  66 */   private static final EntityDataAccessor<Holder<PigVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.PIG_VARIANT);
/*     */   
/*  67 */   private static final EntityDataAccessor<Holder<PigSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.PIG_SOUND_VARIANT);
/*     */   
/*  68 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.45F)
/*  69 */     .withEyeHeight(0.40625F)
/*  70 */     .withAttachments(EntityAttachments.builder()
/*  71 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.5F, 0.0F));
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   public Pig(EntityType<? extends Pig> type, Level level) {
/*  76 */     super(type, level);
/*  77 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  82 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  83 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.25D));
/*  84 */     this.goalSelector.addGoal(3, (Goal)new BreedGoal(this, 1.0D));
/*  85 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.2D, i -> i.is(Items.CARROT_ON_A_STICK), false));
/*  86 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.2D, i -> i.is(ItemTags.PIG_FOOD), false));
/*  87 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.1D));
/*  88 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  89 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  90 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  94 */     return Animal.createAnimalAttributes()
/*  95 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  96 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 101 */     if (isSaddled()) {
/* 101 */       Entity entity = getFirstPassenger();
/* 101 */       if (entity instanceof Player) {
/* 101 */         Player player = (Player)entity;
/* 101 */         if (player.isHolding(Items.CARROT_ON_A_STICK))
/* 102 */           return (LivingEntity)player; 
/*     */       } 
/*     */     } 
/* 104 */     return super.getControllingPassenger();
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 109 */     if (DATA_BOOST_TIME.equals(accessor) && level().isClientSide())
/* 110 */       this.steering.onSynced(); 
/* 112 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 117 */     super.defineSynchedData(entityData);
/* 118 */     Registry<PigSoundVariant> pigSoundVariants = registryAccess().lookupOrThrow(Registries.PIG_SOUND_VARIANT);
/* 119 */     entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/* 120 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), PigVariants.DEFAULT));
/* 121 */     Objects.requireNonNull(pigSoundVariants);
/* 121 */     entityData.define(DATA_SOUND_VARIANT_ID, pigSoundVariants.get(PigSoundVariants.CLASSIC).or(pigSoundVariants::getAny).orElseThrow());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 126 */     super.addAdditionalSaveData(output);
/* 127 */     VariantUtils.writeVariant(output, getVariant());
/* 128 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.PIG_SOUND_VARIANT), soundVariant));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 133 */     super.readAdditionalSaveData(input);
/* 134 */     VariantUtils.readVariant(input, Registries.PIG_VARIANT).ifPresent(this::setVariant);
/* 135 */     input.read("sound_variant", ResourceKey.codec(Registries.PIG_SOUND_VARIANT))
/* 136 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.PIG_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 141 */     return (SoundEvent)getSoundSet().ambientSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 146 */     return (SoundEvent)getSoundSet().hurtSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 151 */     return (SoundEvent)getSoundSet().deathSound().value();
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 156 */     makeSound((SoundEvent)getSoundSet().eatSound().value());
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 161 */     playSound((SoundEvent)getSoundSet().stepSound().value(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 166 */     boolean hasFood = isFood(player.getItemInHand(hand));
/* 168 */     if (!hasFood && isSaddled() && !isVehicle() && !player.isSecondaryUseActive()) {
/* 169 */       if (!level().isClientSide())
/* 170 */         player.startRiding((Entity)this); 
/* 172 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 175 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 176 */     if (!interactionResult.consumesAction()) {
/* 177 */       ItemStack itemStack = player.getItemInHand(hand);
/* 178 */       if (isEquippableInSlot(itemStack, EquipmentSlot.SADDLE))
/* 179 */         return itemStack.interactLivingEntity(player, (LivingEntity)this, hand); 
/* 181 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 183 */     return interactionResult;
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 188 */     if (slot == EquipmentSlot.SADDLE)
/* 189 */       return (isAlive() && !isBaby()); 
/* 191 */     return super.canUseSlot(slot);
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 196 */     return (slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot));
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 201 */     if (slot == EquipmentSlot.SADDLE)
/* 202 */       return (Holder<SoundEvent>)SoundEvents.PIG_SADDLE; 
/* 204 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 209 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/* 210 */       ZombifiedPiglin zombifiedPiglin = (ZombifiedPiglin)convertTo(EntityTypes.ZOMBIFIED_PIGLIN, ConversionParams.single((Mob)this, false, true), zp -> {
/*     */             zp.populateDefaultEquipmentSlots(getRandom(), level.getCurrentDifficultyAt(blockPosition()));
/*     */             zp.setPersistenceRequired();
/*     */           });
/* 215 */       if (zombifiedPiglin == null)
/* 216 */         super.thunderHit(level, lightningBolt); 
/*     */     } else {
/* 219 */       super.thunderHit(level, lightningBolt);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 225 */     super.tickRidden(controller, riddenInput);
/* 226 */     setRot(controller.getYRot(), controller.getXRot() * 0.5F);
/* 227 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/* 228 */     this.steering.tickBoost();
/*     */   }
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 233 */     return new Vec3(0.0D, 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */   protected float getRiddenSpeed(Player controller) {
/* 238 */     return (float)(getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225D * this.steering.boostFactor());
/*     */   }
/*     */   
/*     */   public boolean boost() {
/* 243 */     return this.steering.boost(getRandom());
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 248 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public Pig getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 253 */     Pig baby = (Pig)EntityTypes.PIG.create((Level)level, EntitySpawnReason.BREEDING);
/* 254 */     if (baby != null && partner instanceof Pig) {
/* 254 */       Pig partnerPig = (Pig)partner;
/* 255 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerPig.getVariant());
/*     */     } 
/* 257 */     return baby;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 262 */     return itemStack.is(ItemTags.PIG_FOOD);
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 267 */     return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<PigVariant> variant) {
/* 271 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<PigVariant> getVariant() {
/* 275 */     return (Holder<PigVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private Holder<PigSoundVariant> getSoundVariant() {
/* 279 */     return (Holder<PigSoundVariant>)this.entityData.get(DATA_SOUND_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setSoundVariant(Holder<PigSoundVariant> soundVariant) {
/* 283 */     this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
/*     */   }
/*     */   
/*     */   private PigSoundVariant.PigSoundSet getSoundSet() {
/* 287 */     return isBaby() ? ((PigSoundVariant)getSoundVariant().value()).babySounds() : ((PigSoundVariant)getSoundVariant().value()).adultSounds();
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 292 */     if (type == DataComponents.PIG_VARIANT)
/* 293 */       return (T)castComponentValue(type, getVariant()); 
/* 296 */     if (type == DataComponents.PIG_SOUND_VARIANT)
/* 297 */       return (T)castComponentValue(type, getSoundVariant()); 
/* 300 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 305 */     applyImplicitComponentIfPresent(components, DataComponents.PIG_VARIANT);
/* 306 */     applyImplicitComponentIfPresent(components, DataComponents.PIG_SOUND_VARIANT);
/* 307 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 312 */     if (type == DataComponents.PIG_VARIANT) {
/* 313 */       setVariant((Holder<PigVariant>)castComponentValue(DataComponents.PIG_VARIANT, value));
/* 314 */       return true;
/*     */     } 
/* 317 */     if (type == DataComponents.PIG_SOUND_VARIANT) {
/* 318 */       setSoundVariant((Holder<PigSoundVariant>)castComponentValue(DataComponents.PIG_SOUND_VARIANT, value));
/* 319 */       return true;
/*     */     } 
/* 322 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 327 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.PIG_VARIANT).ifPresent(this::setVariant);
/* 328 */     setSoundVariant(PigSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/* 329 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\Pig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */