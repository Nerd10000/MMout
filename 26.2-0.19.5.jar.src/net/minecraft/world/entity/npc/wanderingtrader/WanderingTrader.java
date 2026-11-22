/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.InteractGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.UseItemGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.illager.Evoker;
/*     */ import net.minecraft.world.entity.monster.illager.Illusioner;
/*     */ import net.minecraft.world.entity.monster.illager.Pillager;
/*     */ import net.minecraft.world.entity.monster.illager.Vindicator;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.item.trading.TradeSets;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WanderingTrader extends AbstractVillager implements Consumable.OverrideConsumeSound {
/*     */   private static final int DEFAULT_DESPAWN_DELAY = 0;
/*     */   
/*     */   private BlockPos wanderTarget;
/*     */   
/*  55 */   private int despawnDelay = 0;
/*     */   
/*     */   public WanderingTrader(EntityType<? extends WanderingTrader> type, Level level) {
/*  58 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  63 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  64 */     this.goalSelector.addGoal(0, (Goal)new UseItemGoal((Mob)this, PotionContents.createItemStack(Items.POTION, Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, e -> (level().isDarkOutside() && !e.isInvisible())));
/*  65 */     this.goalSelector.addGoal(0, (Goal)new UseItemGoal((Mob)this, new ItemStack((ItemLike)Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, e -> (level().isBrightOutside() && e.isInvisible())));
/*  66 */     this.goalSelector.addGoal(1, (Goal)new TradeWithPlayerGoal(this));
/*  67 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Zombie.class, 8.0F, 0.5D, 0.5D));
/*  68 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Evoker.class, 12.0F, 0.5D, 0.5D));
/*  69 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Vindicator.class, 8.0F, 0.5D, 0.5D));
/*  70 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Vex.class, 8.0F, 0.5D, 0.5D));
/*  71 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Pillager.class, 15.0F, 0.5D, 0.5D));
/*  72 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Illusioner.class, 12.0F, 0.5D, 0.5D));
/*  73 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Zoglin.class, 10.0F, 0.5D, 0.5D));
/*  74 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 0.5D));
/*  75 */     this.goalSelector.addGoal(1, (Goal)new LookAtTradingPlayerGoal(this));
/*  76 */     this.goalSelector.addGoal(2, new WanderToPositionGoal(this, 2.0D, 0.35D));
/*  77 */     this.goalSelector.addGoal(4, (Goal)new MoveTowardsRestrictionGoal((PathfinderMob)this, 0.35D));
/*  78 */     this.goalSelector.addGoal(8, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.35D));
/*  79 */     this.goalSelector.addGoal(9, (Goal)new InteractGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  80 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  85 */     return null;
/*     */   }
/*     */   
/*     */   public boolean showProgressBar() {
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  96 */     ItemStack itemStack = player.getItemInHand(hand);
/*  97 */     if (!itemStack.is(Items.VILLAGER_SPAWN_EGG) && isAlive() && !isTrading() && !isBaby()) {
/*  99 */       if (hand == InteractionHand.MAIN_HAND)
/* 100 */         player.awardStat(Stats.TALKED_TO_VILLAGER); 
/* 103 */       if (!level().isClientSide()) {
/* 104 */         if (getOffers().isEmpty())
/* 105 */           return (InteractionResult)InteractionResult.CONSUME; 
/* 108 */         setTradingPlayer(player);
/* 109 */         openTradingScreen(player, getDisplayName(), 1);
/*     */       } 
/* 111 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 113 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   protected void updateTrades(ServerLevel level) {
/* 118 */     MerchantOffers offers = getOffers();
/* 119 */     addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_BUYING);
/* 120 */     addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_UNCOMMON);
/* 121 */     addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_COMMON);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 126 */     super.addAdditionalSaveData(output);
/* 127 */     output.putInt("DespawnDelay", this.despawnDelay);
/* 129 */     output.storeNullable("wander_target", BlockPos.CODEC, this.wanderTarget);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 134 */     super.readAdditionalSaveData(input);
/* 135 */     this.despawnDelay = input.getIntOr("DespawnDelay", 0);
/* 136 */     this.wanderTarget = input.read("wander_target", BlockPos.CODEC).orElse(null);
/* 138 */     setAge(Math.max(0, getAge()));
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   protected void rewardTradeXp(MerchantOffer offer) {
/* 148 */     if (offer.shouldRewardExp()) {
/* 149 */       int popXp = 3 + this.random.nextInt(4);
/* 150 */       level().addFreshEntity((Entity)new ExperienceOrb(level(), getX(), getY() + 0.5D, getZ(), popXp));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 156 */     if (isTrading())
/* 157 */       return SoundEvents.WANDERING_TRADER_TRADE; 
/* 159 */     return SoundEvents.WANDERING_TRADER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 164 */     return SoundEvents.WANDERING_TRADER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 169 */     return SoundEvents.WANDERING_TRADER_DEATH;
/*     */   }
/*     */   
/*     */   public SoundEvent getConsumeSound(ItemStack itemStack) {
/* 174 */     if (itemStack.is(Items.MILK_BUCKET))
/* 175 */       return SoundEvents.WANDERING_TRADER_DRINK_MILK; 
/* 177 */     return SoundEvents.WANDERING_TRADER_DRINK_POTION;
/*     */   }
/*     */   
/*     */   protected SoundEvent getTradeUpdatedSound(boolean validTrade) {
/* 183 */     return validTrade ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
/*     */   }
/*     */   
/*     */   public SoundEvent getNotifyTradeSound() {
/* 188 */     return SoundEvents.WANDERING_TRADER_YES;
/*     */   }
/*     */   
/*     */   public void setDespawnDelay(int despawnDelay) {
/* 192 */     this.despawnDelay = despawnDelay;
/*     */   }
/*     */   
/*     */   public int getDespawnDelay() {
/* 196 */     return this.despawnDelay;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 201 */     super.aiStep();
/* 203 */     if (!level().isClientSide())
/* 204 */       maybeDespawn(); 
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/* 209 */     if (this.despawnDelay > 0 && !isTrading() && --this.despawnDelay == 0)
/* 210 */       discard(); 
/*     */   }
/*     */   
/*     */   public void setWanderTarget(BlockPos pos) {
/* 215 */     this.wanderTarget = pos;
/*     */   }
/*     */   
/*     */   private BlockPos getWanderTarget() {
/* 219 */     return this.wanderTarget;
/*     */   }
/*     */   
/*     */   private class WanderToPositionGoal extends Goal {
/*     */     private final WanderingTrader trader;
/*     */     
/*     */     private final double stopDistance;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     public WanderToPositionGoal(WanderingTrader trader, double stopDistance, double speedModifier) {
/* 228 */       this.trader = trader;
/* 229 */       this.stopDistance = stopDistance;
/* 230 */       this.speedModifier = speedModifier;
/* 231 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public void stop() {
/* 236 */       this.trader.setWanderTarget((BlockPos)null);
/* 237 */       WanderingTrader.this.navigation.stop();
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 242 */       BlockPos wanderPosition = this.trader.getWanderTarget();
/* 243 */       return (wanderPosition != null && isTooFarAway(wanderPosition, this.stopDistance));
/*     */     }
/*     */     
/*     */     public void tick() {
/* 248 */       BlockPos wanderPosition = this.trader.getWanderTarget();
/* 249 */       if (wanderPosition != null && WanderingTrader.this.navigation.isDone())
/* 250 */         if (isTooFarAway(wanderPosition, 10.0D)) {
/* 252 */           Vec3 dir = (new Vec3(wanderPosition.getX() - this.trader.getX(), wanderPosition.getY() - this.trader.getY(), wanderPosition.getZ() - this.trader.getZ())).normalize();
/* 253 */           Vec3 targetPos = dir.scale(10.0D).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
/* 254 */           WanderingTrader.this.navigation.moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
/*     */         } else {
/* 256 */           WanderingTrader.this.navigation.moveTo(wanderPosition.getX(), wanderPosition.getY(), wanderPosition.getZ(), this.speedModifier);
/*     */         }  
/*     */     }
/*     */     
/*     */     private boolean isTooFarAway(BlockPos pos, double distance) {
/* 262 */       return !pos.closerToCenterThan((Position)this.trader.position(), distance);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTrader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */