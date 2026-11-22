/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Pillager extends AbstractIllager implements CrossbowAttackMob, InventoryCarrier {
/*  61 */   private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int INVENTORY_SIZE = 5;
/*     */   
/*     */   private static final int SLOT_OFFSET = 300;
/*     */   
/*  66 */   private final SimpleContainer inventory = new SimpleContainer(5);
/*     */   
/*     */   public Pillager(EntityType<? extends Pillager> type, Level level) {
/*  69 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  74 */     super.registerGoals();
/*  76 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  77 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Creaking.class, 8.0F, 1.0D, 1.2D));
/*  78 */     this.goalSelector.addGoal(2, (Goal)new Raider.HoldGroundAttackGoal(this, 10.0F));
/*  79 */     this.goalSelector.addGoal(3, (Goal)new RangedCrossbowAttackGoal((Monster)this, 1.0D, 8.0F));
/*  80 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  81 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0F, 1.0F));
/*  82 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 15.0F));
/*  84 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  85 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  86 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  87 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  91 */     return Monster.createMonsterAttributes()
/*  92 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  93 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  94 */       .add(Attributes.ATTACK_DAMAGE, 5.0D)
/*  95 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 100 */     super.defineSynchedData(entityData);
/* 102 */     entityData.define(IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canUseNonMeleeWeapon(ItemStack item) {
/* 107 */     return (item.getItem() == Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   public boolean isChargingCrossbow() {
/* 111 */     return ((Boolean)this.entityData.get(IS_CHARGING_CROSSBOW)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setChargingCrossbow(boolean isCharging) {
/* 116 */     this.entityData.set(IS_CHARGING_CROSSBOW, Boolean.valueOf(isCharging));
/*     */   }
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 121 */     this.noActionTime = 0;
/*     */   }
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 126 */     return ItemTags.PILLAGER_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 131 */     super.addAdditionalSaveData(output);
/* 132 */     writeInventoryToTag(output);
/*     */   }
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 137 */     if (isChargingCrossbow())
/* 138 */       return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE; 
/* 139 */     if (isHolding(Items.CROSSBOW))
/* 140 */       return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD; 
/* 141 */     if (isAggressive())
/* 142 */       return AbstractIllager.IllagerArmPose.ATTACKING; 
/* 145 */     return AbstractIllager.IllagerArmPose.NEUTRAL;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 150 */     super.readAdditionalSaveData(input);
/* 152 */     readInventoryFromTag(input);
/* 154 */     setCanPickUpLoot(true);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 160 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 165 */     return 1;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 170 */     RandomSource random = level.getRandom();
/* 171 */     populateDefaultEquipmentSlots(random, difficulty);
/* 172 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 174 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 179 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.CROSSBOW));
/*     */   }
/*     */   
/*     */   protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
/* 184 */     super.enchantSpawnedWeapon(level, random, difficulty);
/* 186 */     if (random.nextInt(300) == 0) {
/* 187 */       ItemStack weapon = getMainHandItem();
/* 188 */       if (weapon.is(Items.CROSSBOW))
/* 189 */         EnchantmentHelper.enchantItemFromProvider(weapon, level.registryAccess(), VanillaEnchantmentProviders.PILLAGER_SPAWN_CROSSBOW, difficulty, random); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 196 */     return SoundEvents.PILLAGER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 201 */     return SoundEvents.PILLAGER_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 206 */     return SoundEvents.PILLAGER_HURT;
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 211 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 216 */     return this.inventory;
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 221 */     ItemStack itemStack = entity.getItem();
/* 222 */     if (itemStack.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 223 */       super.pickUpItem(level, entity);
/* 225 */     } else if (wantsItem(itemStack)) {
/* 226 */       onItemPickup(entity);
/* 227 */       ItemStack remainder = this.inventory.addItem(itemStack);
/* 228 */       if (remainder.isEmpty()) {
/* 229 */         entity.discard();
/*     */       } else {
/* 231 */         itemStack.setCount(remainder.getCount());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean wantsItem(ItemStack itemStack) {
/* 238 */     return (hasActiveRaid() && itemStack.is(Items.BANNER.white()));
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 243 */     int inventorySlot = slot - 300;
/* 244 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize())
/* 245 */       return this.inventory.getSlot(inventorySlot); 
/* 247 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {
/* 252 */     Raid raid = getCurrentRaid();
/* 253 */     boolean shouldEnchant = (this.random.nextFloat() <= raid.getEnchantOdds());
/* 255 */     if (shouldEnchant) {
/*     */       ResourceKey<EnchantmentProvider> provider;
/* 256 */       ItemStack crossbow = new ItemStack((ItemLike)Items.CROSSBOW);
/* 258 */       if (wave > raid.getNumGroups(Difficulty.NORMAL)) {
/* 259 */         provider = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_5;
/* 260 */       } else if (wave > raid.getNumGroups(Difficulty.EASY)) {
/* 261 */         provider = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_3;
/*     */       } else {
/* 263 */         provider = null;
/*     */       } 
/* 266 */       if (provider != null) {
/* 267 */         EnchantmentHelper.enchantItemFromProvider(crossbow, level.registryAccess(), provider, level.getCurrentDifficultyAt(blockPosition()), getRandom());
/* 268 */         setItemSlot(EquipmentSlot.MAINHAND, crossbow);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 275 */     return SoundEvents.PILLAGER_CELEBRATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Pillager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */