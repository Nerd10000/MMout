/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
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
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Cow extends AbstractCow {
/*  33 */   private static final EntityDataAccessor<Holder<CowVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Cow.class, EntityDataSerializers.COW_VARIANT);
/*     */   
/*  34 */   private static final EntityDataAccessor<Holder<CowSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Cow.class, EntityDataSerializers.COW_SOUND_VARIANT);
/*     */   
/*  35 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.7F)
/*  36 */     .withEyeHeight(0.69F)
/*  37 */     .withAttachments(EntityAttachments.builder()
/*  38 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.75F, 0.0F));
/*     */   
/*     */   public Cow(EntityType<? extends Cow> type, Level level) {
/*  41 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  46 */     super.defineSynchedData(entityData);
/*  47 */     Registry<CowSoundVariant> cowSoundVariants = registryAccess().lookupOrThrow(Registries.COW_SOUND_VARIANT);
/*  48 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), CowVariants.TEMPERATE));
/*  49 */     Objects.requireNonNull(cowSoundVariants);
/*  49 */     entityData.define(DATA_SOUND_VARIANT_ID, cowSoundVariants.get(CowSoundVariants.CLASSIC).or(cowSoundVariants::getAny).orElseThrow());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  55 */     super.addAdditionalSaveData(output);
/*  56 */     super.addAdditionalSaveData(output);
/*  57 */     VariantUtils.writeVariant(output, getVariant());
/*  58 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.COW_SOUND_VARIANT), soundVariant));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  63 */     super.readAdditionalSaveData(input);
/*  64 */     VariantUtils.readVariant(input, Registries.COW_VARIANT).ifPresent(this::setVariant);
/*  65 */     input.read("sound_variant", ResourceKey.codec(Registries.COW_SOUND_VARIANT))
/*  66 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.COW_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/*     */   }
/*     */   
/*     */   public Cow getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  71 */     Cow baby = (Cow)EntityTypes.COW.create((Level)level, EntitySpawnReason.BREEDING);
/*  72 */     if (baby != null && partner instanceof Cow) {
/*  72 */       Cow partnerCow = (Cow)partner;
/*  73 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerCow.getVariant());
/*     */     } 
/*  75 */     return baby;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  80 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.COW_VARIANT).ifPresent(this::setVariant);
/*  81 */     setSoundVariant(CowSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/*  82 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public void setVariant(Holder<CowVariant> variant) {
/*  86 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<CowVariant> getVariant() {
/*  90 */     return (Holder<CowVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private Holder<CowSoundVariant> getSoundVariant() {
/*  94 */     return (Holder<CowSoundVariant>)this.entityData.get(DATA_SOUND_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setSoundVariant(Holder<CowSoundVariant> soundVariant) {
/*  98 */     this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
/*     */   }
/*     */   
/*     */   protected CowSoundVariant getSoundSet() {
/* 103 */     return (CowSoundVariant)getSoundVariant().value();
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 108 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 113 */     if (type == DataComponents.COW_VARIANT)
/* 114 */       return (T)castComponentValue(type, getVariant()); 
/* 116 */     if (type == DataComponents.COW_SOUND_VARIANT)
/* 117 */       return (T)castComponentValue(type, getSoundVariant()); 
/* 119 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 124 */     applyImplicitComponentIfPresent(components, DataComponents.COW_VARIANT);
/* 125 */     applyImplicitComponentIfPresent(components, DataComponents.COW_SOUND_VARIANT);
/* 126 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 131 */     if (type == DataComponents.COW_VARIANT) {
/* 132 */       setVariant((Holder<CowVariant>)castComponentValue(DataComponents.COW_VARIANT, value));
/* 133 */       return true;
/*     */     } 
/* 135 */     if (type == DataComponents.COW_SOUND_VARIANT) {
/* 136 */       setSoundVariant((Holder<CowSoundVariant>)castComponentValue(DataComponents.COW_SOUND_VARIANT, value));
/* 137 */       return true;
/*     */     } 
/* 140 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\Cow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */