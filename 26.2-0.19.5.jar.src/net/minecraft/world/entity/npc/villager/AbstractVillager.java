/*     */ package net.minecraft.world.entity.npc.villager;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.npc.Npc;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.item.trading.TradeSet;
/*     */ import net.minecraft.world.item.trading.VillagerTrade;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractVillager extends AgeableMob implements Npc, Merchant, InventoryCarrier {
/*  61 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  63 */   private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);
/*     */   
/*     */   private Player tradingPlayer;
/*     */   
/*     */   protected MerchantOffers offers;
/*     */   
/*  68 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   public AbstractVillager(EntityType<? extends AbstractVillager> type, Level level) {
/*  71 */     super(type, level);
/*  72 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 16.0F);
/*  73 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/*  78 */     if (groupData == null)
/*  79 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false); 
/*  82 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   public int getUnhappyCounter() {
/*  86 */     return ((Integer)this.entityData.get(DATA_UNHAPPY_COUNTER)).intValue();
/*     */   }
/*     */   
/*     */   public void setUnhappyCounter(int value) {
/*  90 */     this.entityData.set(DATA_UNHAPPY_COUNTER, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getVillagerXp() {
/*  95 */     return 0;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 100 */     super.defineSynchedData(entityData);
/* 101 */     entityData.define(DATA_UNHAPPY_COUNTER, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public void setTradingPlayer(Player player) {
/* 106 */     this.tradingPlayer = player;
/*     */   }
/*     */   
/*     */   public Player getTradingPlayer() {
/* 111 */     return this.tradingPlayer;
/*     */   }
/*     */   
/*     */   public boolean isTrading() {
/* 115 */     return (this.tradingPlayer != null);
/*     */   }
/*     */   
/*     */   public MerchantOffers getOffers() {
/*     */     ServerLevel serverLevel;
/* 120 */     Level level = level();
/* 120 */     if (level instanceof ServerLevel) {
/* 120 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/* 121 */       throw new IllegalStateException("Cannot load Villager offers on the client");
/*     */     } 
/* 123 */     if (this.offers == null) {
/* 124 */       this.offers = new MerchantOffers();
/* 125 */       updateTrades(serverLevel);
/*     */     } 
/* 127 */     return this.offers;
/*     */   }
/*     */   
/*     */   public void overrideOffers(MerchantOffers offers) {}
/*     */   
/*     */   public void overrideXp(int xp) {}
/*     */   
/*     */   public void notifyTrade(MerchantOffer offer) {
/* 140 */     offer.increaseUses();
/* 141 */     this.ambientSoundTime = -getAmbientSoundInterval();
/* 143 */     rewardTradeXp(offer);
/* 145 */     if (this.tradingPlayer instanceof ServerPlayer)
/* 146 */       CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, offer.getResult()); 
/*     */   }
/*     */   
/*     */   protected abstract void rewardTradeXp(MerchantOffer paramMerchantOffer);
/*     */   
/*     */   public boolean showProgressBar() {
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   public void notifyTradeUpdated(ItemStack itemStack) {
/* 159 */     if (!level().isClientSide() && this.ambientSoundTime > -getAmbientSoundInterval() + 20) {
/* 160 */       this.ambientSoundTime = -getAmbientSoundInterval();
/* 161 */       makeSound(getTradeUpdatedSound(!itemStack.isEmpty()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public SoundEvent getNotifyTradeSound() {
/* 167 */     return SoundEvents.VILLAGER_YES;
/*     */   }
/*     */   
/*     */   protected SoundEvent getTradeUpdatedSound(boolean validTrade) {
/* 171 */     return validTrade ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
/*     */   }
/*     */   
/*     */   public void playCelebrateSound() {
/* 175 */     makeSound(SoundEvents.VILLAGER_CELEBRATE);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 180 */     super.addAdditionalSaveData(output);
/* 181 */     if (!level().isClientSide() && 
/* 182 */       this.offers != null)
/* 183 */       output.store("Offers", MerchantOffers.CODEC, this.offers); 
/* 186 */     writeInventoryToTag(output);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 191 */     super.readAdditionalSaveData(input);
/* 193 */     this.offers = input.read("Offers", MerchantOffers.CODEC).orElse(null);
/* 195 */     readInventoryFromTag(input);
/*     */   }
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 200 */     stopTrading();
/* 201 */     return super.teleport(transition);
/*     */   }
/*     */   
/*     */   protected void stopTrading() {
/* 205 */     setTradingPlayer((Player)null);
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 210 */     super.die(source);
/* 211 */     stopTrading();
/*     */   }
/*     */   
/*     */   protected void addParticlesAroundSelf(ParticleOptions particle) {
/* 215 */     for (int i = 0; i < 5; i++) {
/* 216 */       double xa = this.random.nextGaussian() * 0.02D;
/* 217 */       double ya = this.random.nextGaussian() * 0.02D;
/* 218 */       double za = this.random.nextGaussian() * 0.02D;
/* 219 */       level().addParticle(particle, getRandomX(1.0D), getRandomY() + 1.0D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 225 */     return false;
/*     */   }
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 230 */     return this.inventory;
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 235 */     int inventorySlot = slot - 300;
/* 236 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize())
/* 237 */       return this.inventory.getSlot(inventorySlot); 
/* 239 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   protected abstract void updateTrades(ServerLevel paramServerLevel);
/*     */   
/*     */   protected void addOffersFromTradeSet(ServerLevel level, MerchantOffers offers, ResourceKey<TradeSet> resourceKey) {
/* 245 */     Optional<TradeSet> tradeSetOpt = registryAccess().lookupOrThrow(Registries.TRADE_SET).getOptional(resourceKey);
/* 246 */     if (tradeSetOpt.isEmpty()) {
/* 247 */       LOGGER.debug("Missing expected trade set {}", resourceKey);
/*     */       return;
/*     */     } 
/* 251 */     TradeSet tradeSet = tradeSetOpt.get();
/* 258 */     LootContext lootContext = (new LootContext.Builder((new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, Unit.INSTANCE).create(LootContextParamSets.VILLAGER_TRADE))).create(tradeSet.randomSequence());
/* 260 */     int numberOfOffers = tradeSet.calculateNumberOfTrades(lootContext);
/* 262 */     if (tradeSet.allowDuplicates()) {
/* 263 */       addOffersFromItemListings(lootContext, offers, tradeSet.getTrades(), numberOfOffers);
/*     */     } else {
/* 265 */       addOffersFromItemListingsWithoutDuplicates(lootContext, offers, tradeSet.getTrades(), numberOfOffers);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void addOffersFromItemListings(LootContext lootContext, MerchantOffers merchantOffers, HolderSet<VillagerTrade> potentialOffers, int numberOfOffers) {
/* 272 */     List<Holder<VillagerTrade>> potentialOffersList = Lists.newArrayList((Iterable)potentialOffers);
/* 273 */     int offersFound = 0;
/* 274 */     while (offersFound < numberOfOffers && !potentialOffersList.isEmpty()) {
/* 275 */       int roll = lootContext.getRandom().nextInt(potentialOffersList.size());
/* 276 */       Holder<VillagerTrade> villagerTrade = potentialOffersList.get(roll);
/* 277 */       MerchantOffer offer = ((VillagerTrade)villagerTrade.value()).getOffer(lootContext);
/* 278 */       if (offer == null) {
/* 279 */         potentialOffersList.remove(roll);
/*     */         continue;
/*     */       } 
/* 282 */       merchantOffers.add(offer);
/* 283 */       offersFound++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addOffersFromItemListingsWithoutDuplicates(LootContext lootContext, MerchantOffers merchantOffers, HolderSet<VillagerTrade> potentialOffers, int numberOfOffers) {
/* 288 */     List<Holder<VillagerTrade>> leftoverOffers = Lists.newArrayList((Iterable)potentialOffers);
/* 289 */     int offersFound = 0;
/* 290 */     while (offersFound < numberOfOffers && !leftoverOffers.isEmpty()) {
/* 291 */       Holder<VillagerTrade> villagerTrade = leftoverOffers.remove(lootContext.getRandom().nextInt(leftoverOffers.size()));
/* 292 */       MerchantOffer offer = ((VillagerTrade)villagerTrade.value()).getOffer(lootContext);
/* 293 */       if (offer != null) {
/* 294 */         merchantOffers.add(offer);
/* 295 */         offersFound++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 302 */     float yRot = Mth.lerp(partialTickTime, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
/* 303 */     Vec3 offset = new Vec3(0.0D, getBoundingBox().getYsize() - 1.0D, 0.2D);
/* 304 */     return getPosition(partialTickTime).add(offset.yRot(-yRot));
/*     */   }
/*     */   
/*     */   public boolean isClientSide() {
/* 309 */     return level().isClientSide();
/*     */   }
/*     */   
/*     */   public boolean stillValid(Player player) {
/* 314 */     return (getTradingPlayer() == player && isAlive() && player.isWithinEntityInteractionRange((Entity)this, 4.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\villager\AbstractVillager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */