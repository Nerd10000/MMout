/*     */ package net.minecraft.world.entity.animal.sheep;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
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
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.EatBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Sheep extends Animal implements Shearable {
/*     */   private static final int EAT_ANIMATION_TICKS = 40;
/*     */   
/*  62 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.65F)
/*  63 */     .withEyeHeight(0.65625F)
/*  64 */     .withAttachments(EntityAttachments.builder()
/*  65 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.5625F, 0.0F));
/*     */   
/*  67 */   private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BYTE);
/*     */   
/*  70 */   private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
/*     */   
/*     */   private static final boolean DEFAULT_SHEARED = false;
/*     */   
/*     */   private int eatAnimationTick;
/*     */   
/*     */   private EatBlockGoal eatBlockGoal;
/*     */   
/*     */   public Sheep(EntityType<? extends Sheep> type, Level level) {
/*  77 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  82 */     this.eatBlockGoal = new EatBlockGoal((Mob)this);
/*  83 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  84 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.25D));
/*  85 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  86 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.1D, i -> i.is(ItemTags.SHEEP_FOOD), false));
/*  87 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.1D));
/*  88 */     this.goalSelector.addGoal(5, (Goal)this.eatBlockGoal);
/*  89 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  90 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  91 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  96 */     return itemStack.is(ItemTags.SHEEP_FOOD);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 101 */     this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
/* 102 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 107 */     if (level().isClientSide())
/* 108 */       this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1); 
/* 110 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 114 */     return Animal.createAnimalAttributes()
/* 115 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 116 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 121 */     super.defineSynchedData(entityData);
/* 124 */     entityData.define(DATA_WOOL_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 129 */     if (id == 10) {
/* 130 */       this.eatAnimationTick = 40;
/*     */     } else {
/* 132 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getHeadEatPositionScale(float a) {
/* 137 */     if (this.eatAnimationTick <= 0)
/* 138 */       return 0.0F; 
/* 140 */     if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36)
/* 141 */       return 1.0F; 
/* 143 */     if (this.eatAnimationTick < 4)
/* 144 */       return (this.eatAnimationTick - a) / 4.0F; 
/* 146 */     return -((this.eatAnimationTick - 40) - a) / 4.0F;
/*     */   }
/*     */   
/*     */   public float getHeadEatAngleScale(float a) {
/* 150 */     if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
/* 151 */       float scale = ((this.eatAnimationTick - 4) - a) / 32.0F;
/* 152 */       return 0.62831855F + 0.21991149F * Mth.sin((scale * 28.7F));
/*     */     } 
/* 154 */     if (this.eatAnimationTick > 0)
/* 155 */       return 0.62831855F; 
/* 157 */     return getXRot(a) * 0.017453292F;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 162 */     ItemStack itemStack = player.getItemInHand(hand);
/* 163 */     if (itemStack.is(Items.SHEARS)) {
/* 164 */       Level level = level();
/* 164 */       if (level instanceof ServerLevel) {
/* 164 */         ServerLevel serverLevel = (ServerLevel)level;
/* 164 */         if (readyForShearing()) {
/* 165 */           shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 166 */           gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/* 167 */           itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/* 168 */           return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */         } 
/*     */       } 
/* 170 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/* 173 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 178 */     level.playSound(null, (Entity)this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
/* 180 */     dropFromShearingLootTable(level, BuiltInLootTables.SHEAR_SHEEP, (ItemInstance)tool, (l, drop) -> {
/*     */           for (int i = 0; i < drop.getCount(); i++) {
/*     */             ItemEntity entity = spawnAtLocation(l, drop.copyWithCount(1), 1.0F);
/*     */             if (entity != null)
/*     */               entity.setDeltaMovement(entity.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F))); 
/*     */           } 
/*     */         });
/* 193 */     setSheared(true);
/*     */   }
/*     */   
/*     */   public boolean readyForShearing() {
/* 198 */     return (!isSheared() && !isBaby());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 203 */     super.addAdditionalSaveData(output);
/* 204 */     output.putBoolean("Sheared", isSheared());
/* 205 */     output.store("Color", DyeColor.LEGACY_ID_CODEC, getColor());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 210 */     super.readAdditionalSaveData(input);
/* 211 */     setSheared(input.getBooleanOr("Sheared", false));
/* 212 */     setColor(input.read("Color", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLOR));
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 217 */     return SoundEvents.SHEEP_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 222 */     return SoundEvents.SHEEP_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 227 */     return SoundEvents.SHEEP_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 232 */     playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 236 */     return DyeColor.byId(((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0xF);
/*     */   }
/*     */   
/*     */   public void setColor(DyeColor color) {
/* 240 */     byte current = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 241 */     this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current & 0xF0 | color.getId() & 0xF)));
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 246 */     if (type == DataComponents.SHEEP_COLOR)
/* 247 */       return (T)castComponentValue(type, getColor()); 
/* 250 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 255 */     applyImplicitComponentIfPresent(components, DataComponents.SHEEP_COLOR);
/* 256 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 261 */     if (type == DataComponents.SHEEP_COLOR) {
/* 262 */       setColor((DyeColor)castComponentValue(DataComponents.SHEEP_COLOR, value));
/* 263 */       return true;
/*     */     } 
/* 266 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public boolean isSheared() {
/* 270 */     return ((((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   public void setSheared(boolean value) {
/* 274 */     byte current = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 275 */     if (value) {
/* 276 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current | 0x10)));
/*     */     } else {
/* 278 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(current & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static DyeColor getRandomSheepColor(ServerLevelAccessor level, BlockPos pos) {
/* 283 */     Holder<Biome> biome = level.getBiome(pos);
/* 284 */     return SheepColorSpawnRules.getSheepColor(biome, level.getRandom());
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 289 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public Sheep getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 294 */     Sheep sheep = (Sheep)EntityTypes.SHEEP.create((Level)level, EntitySpawnReason.BREEDING);
/* 296 */     if (sheep != null) {
/* 297 */       DyeColor parent1DyeColor = getColor();
/* 298 */       DyeColor parent2DyeColor = ((Sheep)partner).getColor();
/* 299 */       sheep.setColor(DyeColor.getMixedColor(level, parent1DyeColor, parent2DyeColor));
/*     */     } 
/* 302 */     return sheep;
/*     */   }
/*     */   
/*     */   public void ate() {
/* 307 */     super.ate();
/* 308 */     setSheared(false);
/* 309 */     if (canAgeUp())
/* 311 */       ageUp(60); 
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 317 */     setColor(getRandomSheepColor(level, blockPosition()));
/* 318 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sheep\Sheep.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */