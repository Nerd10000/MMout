/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ReputationEventHandler;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*     */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class ZombieVillager extends Zombie implements VillagerDataHolder {
/*  67 */   private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  68 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.VILLAGER_DATA);
/*     */   
/*  69 */   private static final EntityDataAccessor<Boolean> DATA_VILLAGER_DATA_FINALIZED = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MIN = 3600;
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MAX = 6000;
/*     */   
/*     */   private static final int MAX_SPECIAL_BLOCKS_COUNT = 14;
/*     */   
/*     */   private static final int SPECIAL_BLOCK_RADIUS = 4;
/*     */   
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   
/*     */   private static final int DEFAULT_XP = 0;
/*     */   
/*     */   private int villagerConversionTime;
/*     */   
/*     */   private UUID conversionStarter;
/*     */   
/*     */   private GossipContainer gossips;
/*     */   
/*     */   private MerchantOffers tradeOffers;
/*     */   
/*  83 */   private int villagerXp = 0;
/*     */   
/*  85 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  86 */     .withEyeHeight(0.67F)
/*  87 */     .withAttachments(EntityAttachments.builder()
/*  88 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.125F, 0.0F));
/*     */   
/*     */   public ZombieVillager(EntityType<? extends ZombieVillager> type, Level level) {
/*  91 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  96 */     super.defineSynchedData(entityData);
/*  98 */     entityData.define(DATA_CONVERTING_ID, Boolean.valueOf(false));
/*  99 */     entityData.define(DATA_VILLAGER_DATA, initializeZombieVillagerData(this.random));
/* 100 */     entityData.define(DATA_VILLAGER_DATA_FINALIZED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 105 */     super.addAdditionalSaveData(output);
/* 107 */     output.store("VillagerData", VillagerData.CODEC, getVillagerData());
/* 108 */     output.putBoolean("VillagerDataFinalized", getVillagerDataFinalized());
/* 110 */     output.storeNullable("Offers", MerchantOffers.CODEC, this.tradeOffers);
/* 112 */     output.storeNullable("Gossips", GossipContainer.CODEC, this.gossips);
/* 114 */     output.putInt("ConversionTime", isConverting() ? this.villagerConversionTime : -1);
/* 116 */     output.storeNullable("ConversionPlayer", UUIDUtil.CODEC, this.conversionStarter);
/* 118 */     output.putInt("Xp", this.villagerXp);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 123 */     super.readAdditionalSaveData(input);
/* 124 */     Optional<VillagerData> villagerDataOptional = input.read("VillagerData", VillagerData.CODEC);
/* 125 */     if (input.getBooleanOr("VillagerDataFinalized", false) || villagerDataOptional.isPresent()) {
/* 126 */       setVillagerDataFinalized(true);
/* 127 */       VillagerData villagerData = villagerDataOptional.orElseGet(() -> initializeZombieVillagerData(this.random));
/* 128 */       this.entityData.set(DATA_VILLAGER_DATA, villagerData);
/*     */     } 
/* 131 */     this.tradeOffers = input.read("Offers", MerchantOffers.CODEC).orElse(null);
/* 133 */     this.gossips = input.read("Gossips", GossipContainer.CODEC).orElse(null);
/* 135 */     int conversionTime = input.getIntOr("ConversionTime", -1);
/* 136 */     if (conversionTime != -1) {
/* 137 */       UUID conversionStarter = input.read("ConversionPlayer", UUIDUtil.CODEC).orElse(null);
/* 138 */       startConverting(conversionStarter, conversionTime);
/*     */     } else {
/* 140 */       getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(false));
/* 141 */       this.villagerConversionTime = -1;
/*     */     } 
/* 144 */     this.villagerXp = input.getIntOr("Xp", 0);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 149 */     finalizeVillagerType(level, blockPosition());
/* 150 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   private static VillagerData initializeZombieVillagerData(RandomSource random) {
/* 154 */     VillagerData villagerData = Villager.createDefaultVillagerData();
/* 158 */     Optional<Holder.Reference<VillagerProfession>> profession = BuiltInRegistries.VILLAGER_PROFESSION.getRandom(random);
/* 159 */     if (profession.isPresent())
/* 160 */       villagerData = villagerData.withProfession((Holder)profession.get()); 
/* 162 */     return villagerData;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 167 */     if (!level().isClientSide() && isAlive() && isConverting()) {
/* 168 */       int amount = getConversionProgress();
/* 170 */       this.villagerConversionTime -= amount;
/* 172 */       if (this.villagerConversionTime <= 0)
/* 173 */         finishConversion((ServerLevel)level()); 
/*     */     } 
/* 177 */     super.tick();
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 182 */     ItemStack itemStack = player.getItemInHand(hand);
/* 183 */     if (itemStack.is(Items.GOLDEN_APPLE)) {
/* 184 */       if (hasEffect(MobEffects.WEAKNESS)) {
/* 185 */         itemStack.consume(1, (LivingEntity)player);
/* 187 */         if (!level().isClientSide())
/* 188 */           startConverting(player.getUUID(), this.random.nextInt(2401) + 3600); 
/* 192 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */       } 
/* 194 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/* 197 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/* 202 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 207 */     return (!isConverting() && this.villagerXp == 0);
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 211 */     return ((Boolean)getEntityData().get(DATA_CONVERTING_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void startConverting(UUID player, int time) {
/* 215 */     this.conversionStarter = player;
/* 216 */     this.villagerConversionTime = time;
/* 217 */     getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(true));
/* 219 */     removeEffect(MobEffects.WEAKNESS);
/* 220 */     addEffect(new MobEffectInstance(MobEffects.STRENGTH, time, Math.min(level().getDifficulty().getId() - 1, 0)));
/* 222 */     level().broadcastEntityEvent((Entity)this, (byte)16);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 227 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 232 */     if (id == 16) {
/* 233 */       if (!isSilent())
/* 234 */         level().playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false); 
/*     */       return;
/*     */     } 
/* 238 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   private void finishConversion(ServerLevel level) {
/* 242 */     convertTo(EntityTypes.VILLAGER, ConversionParams.single((Mob)this, false, false), villager -> {
/*     */           for (EquipmentSlot undroppedSlot : dropPreservedEquipment(level, ())) {
/*     */             SlotAccess offsetSlot = villager.getSlot(undroppedSlot.getIndex() + 300);
/*     */             if (offsetSlot != null)
/*     */               offsetSlot.set(getItemBySlot(undroppedSlot)); 
/*     */           } 
/*     */           villager.setVillagerDataFinalized(getVillagerDataFinalized());
/*     */           villager.setVillagerData(getVillagerData());
/*     */           if (this.gossips != null)
/*     */             villager.setGossips(this.gossips); 
/*     */           if (this.tradeOffers != null)
/*     */             villager.setOffers(this.tradeOffers.copy()); 
/*     */           villager.setVillagerXp(this.villagerXp);
/*     */           villager.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(villager.blockPosition()), EntitySpawnReason.CONVERSION, null);
/*     */           villager.refreshBrain(level);
/*     */           if (this.conversionStarter != null) {
/*     */             Player player = level.getPlayerByUUID(this.conversionStarter);
/*     */             if (player instanceof ServerPlayer) {
/*     */               ServerPlayer serverPlayer = (ServerPlayer)player;
/*     */               CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger(serverPlayer, this, villager);
/*     */               level.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, (Entity)player, (ReputationEventHandler)villager);
/*     */             } 
/*     */           } 
/*     */           villager.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));
/*     */           if (!isSilent())
/*     */             level.levelEvent(null, 1027, blockPosition(), 0); 
/*     */         });
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setVillagerConversionTime(int conversionTime) {
/* 279 */     this.villagerConversionTime = conversionTime;
/*     */   }
/*     */   
/*     */   private int getConversionProgress() {
/* 283 */     int amount = 1;
/* 285 */     if (this.random.nextFloat() < 0.01F) {
/* 286 */       int specialBlocksCount = 0;
/* 288 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 290 */       for (int xx = (int)getX() - 4; xx < (int)getX() + 4 && specialBlocksCount < 14; xx++) {
/* 291 */         for (int yy = (int)getY() - 4; yy < (int)getY() + 4 && specialBlocksCount < 14; yy++) {
/* 292 */           for (int zz = (int)getZ() - 4; zz < (int)getZ() + 4 && specialBlocksCount < 14; zz++) {
/* 293 */             BlockState state = level().getBlockState((BlockPos)blockPos.set(xx, yy, zz));
/* 294 */             if (state.is(Blocks.IRON_BARS) || state.getBlock() instanceof net.minecraft.world.level.block.BedBlock) {
/* 295 */               if (this.random.nextFloat() < 0.3F)
/* 296 */                 amount++; 
/* 298 */               specialBlocksCount++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 304 */     return amount;
/*     */   }
/*     */   
/*     */   public float getVoicePitch() {
/* 309 */     if (isBaby())
/* 310 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F; 
/* 312 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/* 317 */     return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
/*     */   }
/*     */   
/*     */   public SoundEvent getHurtSound(DamageSource source) {
/* 322 */     return SoundEvents.ZOMBIE_VILLAGER_HURT;
/*     */   }
/*     */   
/*     */   public SoundEvent getDeathSound() {
/* 327 */     return SoundEvents.ZOMBIE_VILLAGER_DEATH;
/*     */   }
/*     */   
/*     */   public SoundEvent getStepSound() {
/* 332 */     return SoundEvents.ZOMBIE_VILLAGER_STEP;
/*     */   }
/*     */   
/*     */   public void setTradeOffers(MerchantOffers tradeOffers) {
/* 336 */     this.tradeOffers = tradeOffers;
/*     */   }
/*     */   
/*     */   public void setGossips(GossipContainer gossips) {
/* 340 */     this.gossips = gossips;
/*     */   }
/*     */   
/*     */   public void setVillagerData(VillagerData villagerData) {
/* 345 */     VillagerData currentData = getVillagerData();
/* 346 */     if (!currentData.profession().equals(villagerData.profession()))
/* 347 */       this.tradeOffers = null; 
/* 350 */     this.entityData.set(DATA_VILLAGER_DATA, villagerData);
/*     */   }
/*     */   
/*     */   public boolean getVillagerDataFinalized() {
/* 355 */     return ((Boolean)this.entityData.get(DATA_VILLAGER_DATA_FINALIZED)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setVillagerDataFinalized(boolean villagerDataFinalized) {
/* 360 */     this.entityData.set(DATA_VILLAGER_DATA_FINALIZED, Boolean.valueOf(villagerDataFinalized));
/*     */   }
/*     */   
/*     */   public VillagerData getVillagerData() {
/* 365 */     return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
/*     */   }
/*     */   
/*     */   public int getVillagerXp() {
/* 369 */     return this.villagerXp;
/*     */   }
/*     */   
/*     */   public void setVillagerXp(int villagerXp) {
/* 373 */     this.villagerXp = villagerXp;
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 378 */     if (type == DataComponents.VILLAGER_VARIANT)
/* 379 */       return (T)castComponentValue(type, getVillagerData().type()); 
/* 382 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 387 */     applyImplicitComponentIfPresent(components, DataComponents.VILLAGER_VARIANT);
/* 388 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 393 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 394 */       Holder<VillagerType> variant = (Holder<VillagerType>)castComponentValue(DataComponents.VILLAGER_VARIANT, value);
/* 395 */       setVillagerData(getVillagerData().withType(variant));
/* 396 */       setVillagerDataFinalized(true);
/* 397 */       return true;
/*     */     } 
/* 400 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\ZombieVillager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */