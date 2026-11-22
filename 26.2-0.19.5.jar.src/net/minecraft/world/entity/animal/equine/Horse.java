/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Horse extends AbstractHorse {
/*  41 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);
/*     */   
/*  43 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.HORSE.getDimensions()
/*  44 */     .withAttachments(EntityAttachments.builder()
/*  45 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityTypes.HORSE.getHeight() - 0.125F, 0.0F))
/*  46 */     .scale(0.7F);
/*     */   
/*     */   private static final int DEFAULT_VARIANT = 0;
/*     */   
/*     */   public Horse(EntityType<? extends Horse> type, Level level) {
/*  51 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void randomizeAttributes(RandomSource random) {
/*  56 */     Objects.requireNonNull(random);
/*  56 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateMaxHealth(random::nextInt));
/*  57 */     Objects.requireNonNull(random);
/*  57 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(generateSpeed(random::nextDouble));
/*  58 */     Objects.requireNonNull(random);
/*  58 */     getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(random::nextDouble));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  63 */     super.defineSynchedData(entityData);
/*  65 */     entityData.define(DATA_ID_TYPE_VARIANT, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  70 */     super.addAdditionalSaveData(output);
/*  72 */     output.putInt("Variant", getTypeVariant());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  77 */     super.readAdditionalSaveData(input);
/*  79 */     setTypeVariant(input.getIntOr("Variant", 0));
/*     */   }
/*     */   
/*     */   private void setTypeVariant(int i) {
/*  83 */     this.entityData.set(DATA_ID_TYPE_VARIANT, Integer.valueOf(i));
/*     */   }
/*     */   
/*     */   private int getTypeVariant() {
/*  87 */     return ((Integer)this.entityData.get(DATA_ID_TYPE_VARIANT)).intValue();
/*     */   }
/*     */   
/*     */   private void setVariantAndMarkings(Variant variant, Markings markings) {
/*  91 */     setTypeVariant(variant.getId() & 0xFF | markings.getId() << 8 & 0xFF00);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/*  95 */     return Variant.byId(getTypeVariant() & 0xFF);
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/*  99 */     setTypeVariant(variant.getId() & 0xFF | getTypeVariant() & 0xFFFFFF00);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 104 */     if (type == DataComponents.HORSE_VARIANT)
/* 105 */       return (T)castComponentValue(type, getVariant()); 
/* 108 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 113 */     applyImplicitComponentIfPresent(components, DataComponents.HORSE_VARIANT);
/* 114 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 119 */     if (type == DataComponents.HORSE_VARIANT) {
/* 120 */       setVariant((Variant)castComponentValue(DataComponents.HORSE_VARIANT, value));
/* 121 */       return true;
/*     */     } 
/* 124 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public Markings getMarkings() {
/* 128 */     return Markings.byId((getTypeVariant() & 0xFF00) >> 8);
/*     */   }
/*     */   
/*     */   protected void playGallopSound(SoundType soundType) {
/* 133 */     super.playGallopSound(soundType);
/* 134 */     if (this.random.nextInt(10) == 0)
/* 135 */       playSound(isBaby() ? SoundEvents.HORSE_BREATHE_BABY : SoundEvents.HORSE_BREATHE, soundType.getVolume() * 0.6F, soundType.getPitch()); 
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 141 */     return isBaby() ? SoundEvents.HORSE_AMBIENT_BABY : SoundEvents.HORSE_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 146 */     return isBaby() ? SoundEvents.HORSE_DEATH_BABY : SoundEvents.HORSE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 151 */     return isBaby() ? SoundEvents.HORSE_EAT_BABY : SoundEvents.HORSE_EAT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 156 */     return isBaby() ? SoundEvents.HORSE_HURT_BABY : SoundEvents.HORSE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 161 */     return isBaby() ? SoundEvents.HORSE_ANGRY_BABY : SoundEvents.HORSE_ANGRY;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 166 */     boolean shouldOpenInventory = (!isBaby() && isTamed() && player.isSecondaryUseActive());
/* 167 */     if (isVehicle() || shouldOpenInventory || (isBaby() && player.isHolding(Items.GOLDEN_DANDELION)))
/* 168 */       return super.mobInteract(player, hand); 
/* 171 */     ItemStack itemStack = player.getItemInHand(hand);
/* 173 */     if (!itemStack.isEmpty()) {
/* 174 */       if (isFood(itemStack))
/* 175 */         return fedFood(player, itemStack); 
/* 178 */       if (!isTamed()) {
/* 179 */         makeMad();
/* 180 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 183 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 188 */     if (partner == this)
/* 189 */       return false; 
/* 192 */     if (partner instanceof Donkey || partner instanceof Horse)
/* 193 */       return (canParent() && ((AbstractHorse)partner).canParent()); 
/* 196 */     return false;
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 201 */     if (partner instanceof Donkey) {
/* 202 */       Mule mule = (Mule)EntityTypes.MULE.create((Level)level, EntitySpawnReason.BREEDING);
/* 203 */       if (mule != null)
/* 204 */         setOffspringAttributes(partner, mule); 
/* 206 */       return (AgeableMob)mule;
/*     */     } 
/* 208 */     Horse horsePartner = (Horse)partner;
/* 210 */     Horse baby = (Horse)EntityTypes.HORSE.create((Level)level, EntitySpawnReason.BREEDING);
/* 211 */     if (baby != null) {
/*     */       Variant variant;
/*     */       Markings markings;
/* 213 */       int selectSkin = this.random.nextInt(9);
/* 214 */       if (selectSkin < 4) {
/* 215 */         variant = getVariant();
/* 216 */       } else if (selectSkin < 8) {
/* 217 */         variant = horsePartner.getVariant();
/*     */       } else {
/* 219 */         variant = (Variant)Util.getRandom((Object[])Variant.values(), this.random);
/*     */       } 
/* 223 */       int selectMarking = this.random.nextInt(5);
/* 224 */       if (selectMarking < 2) {
/* 225 */         markings = getMarkings();
/* 226 */       } else if (selectMarking < 4) {
/* 227 */         markings = horsePartner.getMarkings();
/*     */       } else {
/* 229 */         markings = (Markings)Util.getRandom((Object[])Markings.values(), this.random);
/*     */       } 
/* 232 */       baby.setVariantAndMarkings(variant, markings);
/* 233 */       setOffspringAttributes(partner, baby);
/*     */     } 
/* 235 */     return (AgeableMob)baby;
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 240 */     return true;
/*     */   }
/*     */   
/*     */   protected void hurtArmor(DamageSource damageSource, float damage) {
/* 245 */     doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.BODY });
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     HorseGroupData horseGroupData;
/*     */     Variant variant;
/* 250 */     RandomSource random = level.getRandom();
/* 252 */     if (groupData instanceof HorseGroupData) {
/* 253 */       variant = ((HorseGroupData)groupData).variant;
/*     */     } else {
/* 255 */       variant = (Variant)Util.getRandom((Object[])Variant.values(), random);
/* 256 */       horseGroupData = new HorseGroupData(variant);
/*     */     } 
/* 258 */     setVariantAndMarkings(variant, (Markings)Util.getRandom((Object[])Markings.values(), random));
/* 260 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)horseGroupData);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 265 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public static class HorseGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Variant variant;
/*     */     
/*     */     public HorseGroupData(Variant variant) {
/* 272 */       super(true);
/* 273 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Horse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */