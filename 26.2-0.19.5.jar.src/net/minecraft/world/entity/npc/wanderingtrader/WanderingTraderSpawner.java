/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.SpawnPlacementType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.animal.equine.TraderLlama;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.WanderingTraderData;
/*     */ import net.minecraft.world.level.storage.SavedDataStorage;
/*     */ 
/*     */ public class WanderingTraderSpawner implements CustomSpawner {
/*     */   private static final int DEFAULT_TICK_DELAY = 1200;
/*     */   
/*     */   public static final int DEFAULT_SPAWN_DELAY = 24000;
/*     */   
/*     */   public static final int MIN_SPAWN_CHANCE = 25;
/*     */   
/*     */   private static final int MAX_SPAWN_CHANCE = 75;
/*     */   
/*     */   private static final int SPAWN_CHANCE_INCREASE = 25;
/*     */   
/*     */   private static final int SPAWN_ONE_IN_X_CHANCE = 10;
/*     */   
/*     */   private static final int NUMBER_OF_SPAWN_ATTEMPTS = 10;
/*     */   
/*  39 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private final SavedDataStorage savedDataStorage;
/*     */   
/*     */   private int tickDelay;
/*     */   
/*     */   private WanderingTraderData traderData;
/*     */   
/*     */   public WanderingTraderSpawner(SavedDataStorage savedDataStorage) {
/*  45 */     this.savedDataStorage = savedDataStorage;
/*  46 */     this.tickDelay = 1200;
/*  47 */     this.traderData = null;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, boolean spawnEnemies) {
/*  52 */     if (!((Boolean)level.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS)).booleanValue())
/*     */       return; 
/*  56 */     if (--this.tickDelay > 0)
/*     */       return; 
/*  59 */     this.tickDelay = 1200;
/*  61 */     WanderingTraderData data = getTraderData();
/*  62 */     int spawnDelay = data.spawnDelay() - 1200;
/*  63 */     data.setSpawnDelay(spawnDelay);
/*  64 */     if (spawnDelay > 0)
/*     */       return; 
/*  67 */     data.setSpawnDelay(24000);
/*  69 */     int chanceToSpawn = data.spawnChance();
/*  70 */     int newSpawnChance = Mth.clamp(chanceToSpawn + 25, 25, 75);
/*  71 */     data.setSpawnChance(newSpawnChance);
/*  73 */     if (this.random.nextInt(100) > chanceToSpawn)
/*     */       return; 
/*  77 */     if (spawn(level))
/*  78 */       data.setSpawnChance(25); 
/*     */   }
/*     */   
/*     */   private WanderingTraderData getTraderData() {
/*  83 */     if (this.traderData == null)
/*  84 */       this.traderData = (WanderingTraderData)this.savedDataStorage.computeIfAbsent(WanderingTraderData.TYPE); 
/*  86 */     return this.traderData;
/*     */   }
/*     */   
/*     */   private boolean spawn(ServerLevel level) {
/*  90 */     ServerPlayer serverPlayer = level.getRandomPlayer();
/*  91 */     if (serverPlayer == null)
/*  92 */       return true; 
/*  95 */     if (this.random.nextInt(10) != 0)
/*  96 */       return false; 
/*  99 */     BlockPos playerPos = serverPlayer.blockPosition();
/* 100 */     int radius = 48;
/* 102 */     PoiManager poiManager = level.getPoiManager();
/* 103 */     Optional<BlockPos> poiPos = poiManager.find(p -> p.is(PoiTypes.MEETING), p -> true, playerPos, 48, PoiManager.Occupancy.ANY);
/* 105 */     BlockPos referencePos = poiPos.orElse(playerPos);
/* 106 */     BlockPos spawnPosition = findSpawnPositionNear((LevelReader)level, referencePos, 48);
/* 108 */     if (spawnPosition != null && hasEnoughSpace((BlockGetter)level, spawnPosition)) {
/* 109 */       if (level.getBiome(spawnPosition).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS))
/* 110 */         return false; 
/* 113 */       WanderingTrader trader = (WanderingTrader)EntityTypes.WANDERING_TRADER.spawn(level, spawnPosition, EntitySpawnReason.EVENT);
/* 115 */       if (trader != null) {
/* 116 */         for (int i = 0; i < 2; i++)
/* 117 */           tryToSpawnLlamaFor(level, trader, 4); 
/* 119 */         trader.setDespawnDelay(48000);
/* 120 */         trader.setWanderTarget(referencePos);
/* 121 */         trader.setHomeTo(referencePos, 16);
/* 122 */         return true;
/*     */       } 
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   private void tryToSpawnLlamaFor(ServerLevel level, WanderingTrader trader, int radius) {
/* 129 */     BlockPos spawnPosition = findSpawnPositionNear((LevelReader)level, trader.blockPosition(), radius);
/* 130 */     if (spawnPosition == null)
/*     */       return; 
/* 134 */     TraderLlama llama = (TraderLlama)EntityTypes.TRADER_LLAMA.spawn(level, spawnPosition, EntitySpawnReason.EVENT);
/* 135 */     if (llama == null)
/*     */       return; 
/* 139 */     llama.setLeashedTo((Entity)trader, true);
/*     */   }
/*     */   
/*     */   private BlockPos findSpawnPositionNear(LevelReader level, BlockPos referencePosition, int radius) {
/* 143 */     BlockPos spawnPosition = null;
/* 145 */     SpawnPlacementType wanderingTraderSpawnType = SpawnPlacements.getPlacementType(EntityTypes.WANDERING_TRADER);
/* 146 */     for (int i = 0; i < 10; i++) {
/* 147 */       int xPosition = referencePosition.getX() + this.random.nextInt(radius * 2) - radius;
/* 148 */       int zPosition = referencePosition.getZ() + this.random.nextInt(radius * 2) - radius;
/* 149 */       int yPosition = level.getHeight(SpawnPlacements.getHeightmapType(EntityTypes.WANDERING_TRADER), xPosition, zPosition);
/* 150 */       BlockPos spawnPos = new BlockPos(xPosition, yPosition, zPosition);
/* 152 */       if (wanderingTraderSpawnType.isSpawnPositionOk(level, spawnPos, EntityTypes.WANDERING_TRADER)) {
/* 153 */         spawnPosition = spawnPos;
/*     */         break;
/*     */       } 
/*     */     } 
/* 157 */     return spawnPosition;
/*     */   }
/*     */   
/*     */   private boolean hasEnoughSpace(BlockGetter level, BlockPos spawnPos) {
/* 161 */     for (BlockPos pos : BlockPos.betweenClosed(spawnPos, spawnPos.offset(1, 2, 1))) {
/* 162 */       if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())
/* 163 */         return false; 
/*     */     } 
/* 166 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTraderSpawner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */