/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class ZombieNautilus extends AbstractNautilus {
/*  40 */   private static final Brain.Provider<ZombieNautilus> BRAIN_PROVIDER = Brain.provider(
/*  41 */       List.of(MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN), 
/*     */       
/*  45 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS), paramZombieNautilus -> ZombieNautilusAi.getActivities());
/*     */   
/*  55 */   private static final EntityDataAccessor<Holder<ZombieNautilusVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(ZombieNautilus.class, EntityDataSerializers.ZOMBIE_NAUTILUS_VARIANT);
/*     */   
/*     */   public ZombieNautilus(EntityType<? extends ZombieNautilus> type, Level level) {
/*  58 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  63 */     return AbstractNautilus.createAttributes()
/*  64 */       .add(Attributes.MOVEMENT_SPEED, 1.100000023841858D);
/*     */   }
/*     */   
/*     */   public ZombieNautilus getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  69 */     return null;
/*     */   }
/*     */   
/*     */   protected EquipmentSlot sunProtectionSlot() {
/*  74 */     return EquipmentSlot.BODY;
/*     */   }
/*     */   
/*     */   protected Brain<ZombieNautilus> makeBrain(Brain.Packed packedBrain) {
/*  79 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<ZombieNautilus> getBrain() {
/*  85 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  90 */     ProfilerFiller profiler = Profiler.get();
/*  91 */     profiler.push("zombieNautilusBrain");
/*  92 */     getBrain().tick(level, (LivingEntity)this);
/*  93 */     profiler.pop();
/*  95 */     profiler.push("zombieNautilusActivityUpdate");
/*  96 */     ZombieNautilusAi.updateActivity(this);
/*  97 */     profiler.pop();
/*  98 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 103 */     return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_AMBIENT : SoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 108 */     return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_HURT : SoundEvents.ZOMBIE_NAUTILUS_HURT_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 113 */     return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DEATH : SoundEvents.ZOMBIE_NAUTILUS_DEATH_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashSound() {
/* 118 */     return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH : SoundEvents.ZOMBIE_NAUTILUS_DASH_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 123 */     return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH_READY : SoundEvents.ZOMBIE_NAUTILUS_DASH_READY_ON_LAND;
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 128 */     makeSound(SoundEvents.ZOMBIE_NAUTILUS_EAT);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 133 */     return SoundEvents.ZOMBIE_NAUTILUS_SWIM;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 138 */     super.defineSynchedData(entityData);
/* 139 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), ZombieNautilusVariants.TEMPERATE));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 144 */     super.readAdditionalSaveData(input);
/* 145 */     VariantUtils.readVariant(input, Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 150 */     super.addAdditionalSaveData(output);
/* 151 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */   
/*     */   public void setVariant(Holder<ZombieNautilusVariant> variant) {
/* 155 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<ZombieNautilusVariant> getVariant() {
/* 159 */     return (Holder<ZombieNautilusVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 164 */     if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT)
/* 165 */       return (T)castComponentValue(type, getVariant()); 
/* 168 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 173 */     applyImplicitComponentIfPresent(components, DataComponents.ZOMBIE_NAUTILUS_VARIANT);
/* 174 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 179 */     if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT) {
/* 180 */       Holder<ZombieNautilusVariant> variant = (Holder<ZombieNautilusVariant>)castComponentValue(DataComponents.ZOMBIE_NAUTILUS_VARIANT, value);
/* 181 */       setVariant(variant);
/* 182 */       return true;
/*     */     } 
/* 185 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 190 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
/* 191 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 196 */     return (!isAggravated() && !isMobControlled());
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 201 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */