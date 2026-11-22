/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.LightningRodBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LightningBolt extends Entity {
/*     */   private static final int START_LIFE = 2;
/*     */   
/*     */   private static final double DAMAGE_RADIUS = 3.0D;
/*     */   
/*     */   private static final double DETECTION_RADIUS = 15.0D;
/*     */   
/*     */   private int life;
/*     */   
/*     */   public long seed;
/*     */   
/*     */   private int flashes;
/*     */   
/*     */   private boolean visualOnly;
/*     */   
/*     */   private ServerPlayer cause;
/*     */   
/*  46 */   private final Set<Entity> hitEntities = Sets.newHashSet();
/*     */   
/*     */   private int blocksSetOnFire;
/*     */   
/*     */   public LightningBolt(EntityType<? extends LightningBolt> type, Level level) {
/*  50 */     super(type, level);
/*  52 */     this.life = 2;
/*  53 */     this.seed = this.random.nextLong();
/*  54 */     this.flashes = this.random.nextInt(3) + 1;
/*     */   }
/*     */   
/*     */   public void setVisualOnly(boolean visualOnly) {
/*  58 */     this.visualOnly = visualOnly;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  63 */     return SoundSource.WEATHER;
/*     */   }
/*     */   
/*     */   public ServerPlayer getCause() {
/*  67 */     return this.cause;
/*     */   }
/*     */   
/*     */   public void setCause(ServerPlayer cause) {
/*  71 */     this.cause = cause;
/*     */   }
/*     */   
/*     */   private void powerLightningRod() {
/*  75 */     BlockPos strikePosition = getStrikePosition();
/*  76 */     BlockState stateBelow = level().getBlockState(strikePosition);
/*  77 */     Block block = stateBelow.getBlock();
/*  77 */     if (block instanceof LightningRodBlock) {
/*  77 */       LightningRodBlock lightningRodBlock = (LightningRodBlock)block;
/*  78 */       lightningRodBlock.onLightningStrike(stateBelow, level(), strikePosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/*  84 */     super.tick();
/*  86 */     if (this.life == 2)
/*  87 */       if (level().isClientSide()) {
/*  88 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
/*  89 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
/*     */       } else {
/*  91 */         Difficulty difficulty = level().getDifficulty();
/*  92 */         if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)
/*  93 */           spawnFire(4); 
/*  96 */         powerLightningRod();
/*  97 */         clearCopperOnLightningStrike(level(), getStrikePosition());
/*  99 */         gameEvent((Holder<GameEvent>)GameEvent.LIGHTNING_STRIKE);
/*     */       }  
/* 103 */     this.life--;
/* 104 */     if (this.life < 0)
/* 105 */       if (this.flashes == 0) {
/* 106 */         if (level() instanceof ServerLevel) {
/* 107 */           List<Entity> viewers = level().getEntities(this, new AABB(getX() - 15.0D, getY() - 15.0D, getZ() - 15.0D, getX() + 15.0D, getY() + 6.0D + 15.0D, getZ() + 15.0D), entity -> 
/* 108 */               (entity.isAlive() && !this.hitEntities.contains(entity)));
/* 111 */           for (ServerPlayer player : ((ServerLevel)level()).getPlayers(player -> (player.distanceTo(this) < 256.0F)))
/* 112 */             CriteriaTriggers.LIGHTNING_STRIKE.trigger(player, this, viewers); 
/*     */         } 
/* 116 */         discard();
/* 117 */       } else if (this.life < -this.random.nextInt(10)) {
/* 118 */         this.flashes--;
/* 119 */         this.life = 1;
/* 120 */         this.seed = this.random.nextLong();
/* 121 */         spawnFire(0);
/*     */       }  
/* 125 */     if (this.life >= 0)
/* 126 */       if (!(level() instanceof ServerLevel)) {
/* 127 */         level().setSkyFlashTime(2);
/* 128 */       } else if (!this.visualOnly) {
/* 129 */         List<Entity> entities = level().getEntities(this, new AABB(getX() - 3.0D, getY() - 3.0D, getZ() - 3.0D, getX() + 3.0D, getY() + 6.0D + 3.0D, getZ() + 3.0D), Entity::isAlive);
/* 130 */         for (Entity entity : entities)
/* 131 */           entity.thunderHit((ServerLevel)level(), this); 
/* 133 */         this.hitEntities.addAll(entities);
/* 134 */         if (this.cause != null)
/* 135 */           CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, entities); 
/*     */       }  
/*     */   }
/*     */   
/*     */   private BlockPos getStrikePosition() {
/* 142 */     Vec3 position = position();
/* 143 */     return BlockPos.containing(position.x, position.y - 1.0E-6D, position.z);
/*     */   }
/*     */   
/*     */   private void spawnFire(int additionalSources) {
/*     */     ServerLevel level;
/* 147 */     if (!this.visualOnly) {
/* 147 */       Level level1 = level();
/* 147 */       if (level1 instanceof ServerLevel) {
/* 147 */         level = (ServerLevel)level1;
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } else {
/*     */       return;
/*     */     } 
/* 151 */     BlockPos pos = blockPosition();
/* 152 */     if (!level.canSpreadFireAround(pos))
/*     */       return; 
/* 156 */     BlockState fire = BaseFireBlock.getState((BlockGetter)level, pos);
/* 158 */     if (level.getBlockState(pos).isAir() && fire.canSurvive((LevelReader)level, pos)) {
/* 159 */       level.setBlockAndUpdate(pos, fire);
/* 160 */       this.blocksSetOnFire++;
/*     */     } 
/* 163 */     for (int i = 0; i < additionalSources; i++) {
/* 164 */       BlockPos nearbyPos = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
/* 165 */       fire = BaseFireBlock.getState((BlockGetter)level, nearbyPos);
/* 166 */       if (level.getBlockState(nearbyPos).isAir() && fire.canSurvive((LevelReader)level, nearbyPos)) {
/* 167 */         level.setBlockAndUpdate(nearbyPos, fire);
/* 168 */         this.blocksSetOnFire++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void clearCopperOnLightningStrike(Level level, BlockPos struckPos) {
/* 174 */     BlockState struckState = level.getBlockState(struckPos);
/* 176 */     boolean isWaxed = (((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(struckState.getBlock()) != null);
/* 177 */     boolean isWeatheringCopper = struckState.getBlock() instanceof WeatheringCopper;
/* 179 */     if (!isWeatheringCopper && !isWaxed)
/*     */       return; 
/* 183 */     if (isWeatheringCopper)
/* 184 */       level.setBlockAndUpdate(struckPos, WeatheringCopper.getFirst(level.getBlockState(struckPos))); 
/* 187 */     BlockPos.MutableBlockPos workPos = struckPos.mutable();
/* 188 */     RandomSource random = level.getRandom();
/* 189 */     int strikesCount = random.nextInt(3) + 3;
/* 190 */     for (int strike = 0; strike < strikesCount; strike++) {
/* 191 */       int stepCount = random.nextInt(8) + 1;
/* 192 */       randomWalkCleaningCopper(level, struckPos, workPos, stepCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void randomWalkCleaningCopper(Level level, BlockPos originalStrikePos, BlockPos.MutableBlockPos workPos, int stepCount) {
/* 197 */     workPos.set((Vec3i)originalStrikePos);
/* 198 */     for (int step = 0; step < stepCount; step++) {
/* 199 */       Optional<BlockPos> stepPos = randomStepCleaningCopper(level, (BlockPos)workPos);
/* 200 */       if (stepPos.isEmpty())
/*     */         break; 
/* 203 */       workPos.set((Vec3i)stepPos.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> randomStepCleaningCopper(Level level, BlockPos pos) {
/* 209 */     for (Iterator<BlockPos> iterator = BlockPos.randomInCube(level.getRandom(), 10, pos, 1).iterator(); iterator.hasNext(); ) {
/* 209 */       BlockPos candidate = iterator.next();
/* 210 */       BlockState state = level.getBlockState(candidate);
/* 211 */       if (state.getBlock() instanceof WeatheringCopper) {
/* 212 */         WeatheringCopper.getPrevious(state).ifPresent(s -> level.setBlockAndUpdate(candidate, s));
/* 213 */         level.levelEvent(3002, candidate, -1);
/* 215 */         return Optional.of(candidate);
/*     */       } 
/*     */     } 
/* 219 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 225 */     double size = 64.0D * getViewScale();
/* 226 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */   
/*     */   public int getBlocksSetOnFire() {
/* 242 */     return this.blocksSetOnFire;
/*     */   }
/*     */   
/*     */   public Stream<Entity> getHitEntities() {
/* 246 */     return this.hitEntities.stream().filter(Entity::isAlive);
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 251 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\LightningBolt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */