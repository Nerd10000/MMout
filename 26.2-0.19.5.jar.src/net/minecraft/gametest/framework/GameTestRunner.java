/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongArraySet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestRunner {
/*     */   public static final int DEFAULT_TESTS_PER_ROW = 8;
/*     */   
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final GameTestTicker testTicker;
/*     */   
/*     */   private final List<GameTestInfo> allTestInfos;
/*     */   
/*     */   private ImmutableList<GameTestBatch> batches;
/*     */   
/*  27 */   private final List<GameTestBatchListener> batchListeners = Lists.newArrayList();
/*     */   
/*  28 */   private final List<GameTestInfo> scheduledForRerun = Lists.newArrayList();
/*     */   
/*     */   private final GameTestBatcher testBatcher;
/*     */   
/*     */   private boolean stopped = true;
/*     */   
/*     */   private TestEnvironmentDefinition.Activation<?> currentEnvironment;
/*     */   
/*     */   private final StructureSpawner existingStructureSpawner;
/*     */   
/*     */   private final StructureSpawner newStructureSpawner;
/*     */   
/*     */   private final boolean haltOnError;
/*     */   
/*     */   private final boolean clearBetweenBatches;
/*     */   
/*     */   public static interface StructureSpawner {
/*     */     public static final StructureSpawner IN_PLACE;
/*     */     
/*     */     default void onBatchStart(ServerLevel level) {}
/*     */     
/*     */     static {
/*  46 */       IN_PLACE = (testInfo -> Optional.<GameTestInfo>ofNullable(testInfo.prepareTestStructure()).map(()));
/*     */     }
/*     */     
/*     */     public static final StructureSpawner NOT_SET = testInfo -> Optional.empty();
/*     */     
/*     */     Optional<GameTestInfo> spawnStructure(GameTestInfo param1GameTestInfo);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final ServerLevel level;
/*     */     
/*  53 */     private final GameTestTicker testTicker = GameTestTicker.SINGLETON;
/*     */     
/*  54 */     private GameTestRunner.GameTestBatcher batcher = GameTestBatchFactory.fromGameTestInfo();
/*     */     
/*  55 */     private GameTestRunner.StructureSpawner existingStructureSpawner = GameTestRunner.StructureSpawner.IN_PLACE;
/*     */     
/*  56 */     private GameTestRunner.StructureSpawner newStructureSpawner = GameTestRunner.StructureSpawner.NOT_SET;
/*     */     
/*     */     private final Collection<GameTestBatch> batches;
/*     */     
/*     */     private boolean haltOnError = false;
/*     */     
/*     */     private boolean clearBetweenBatches = false;
/*     */     
/*     */     private Builder(Collection<GameTestBatch> batches, ServerLevel level) {
/*  62 */       this.batches = batches;
/*  63 */       this.level = level;
/*     */     }
/*     */     
/*     */     public static Builder fromBatches(Collection<GameTestBatch> batches, ServerLevel level) {
/*  67 */       return new Builder(batches, level);
/*     */     }
/*     */     
/*     */     public static Builder fromInfo(Collection<GameTestInfo> tests, ServerLevel level) {
/*  71 */       return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(tests), level);
/*     */     }
/*     */     
/*     */     public Builder haltOnError() {
/*  75 */       this.haltOnError = true;
/*  76 */       return this;
/*     */     }
/*     */     
/*     */     public Builder clearBetweenBatches() {
/*  80 */       this.clearBetweenBatches = true;
/*  81 */       return this;
/*     */     }
/*     */     
/*     */     public Builder newStructureSpawner(GameTestRunner.StructureSpawner structureSpawner) {
/*  85 */       this.newStructureSpawner = structureSpawner;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     public Builder existingStructureSpawner(StructureGridSpawner spawner) {
/*  90 */       this.existingStructureSpawner = spawner;
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public Builder batcher(GameTestRunner.GameTestBatcher batcher) {
/*  95 */       this.batcher = batcher;
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     public GameTestRunner build() {
/* 100 */       return new GameTestRunner(this.batcher, this.batches, this.level, this.testTicker, this.existingStructureSpawner, this.newStructureSpawner, this.haltOnError, this.clearBetweenBatches);
/*     */     }
/*     */   }
/*     */   
/*     */   protected GameTestRunner(GameTestBatcher batcher, Collection<GameTestBatch> batches, ServerLevel level, GameTestTicker testTicker, StructureSpawner existingStructureSpawner, StructureSpawner newStructureSpawner, boolean haltOnError, boolean clearBetweenBatches) {
/* 105 */     this.level = level;
/* 106 */     this.testTicker = testTicker;
/* 107 */     this.testBatcher = batcher;
/* 108 */     this.existingStructureSpawner = existingStructureSpawner;
/* 109 */     this.newStructureSpawner = newStructureSpawner;
/* 110 */     this.batches = ImmutableList.copyOf(batches);
/* 111 */     this.haltOnError = haltOnError;
/* 112 */     this.clearBetweenBatches = clearBetweenBatches;
/* 114 */     this.allTestInfos = (List<GameTestInfo>)this.batches.stream().flatMap(batch -> batch.gameTestInfos().stream()).collect(Util.toMutableList());
/* 115 */     testTicker.setRunner(this);
/* 116 */     this.allTestInfos.forEach(info -> info.addListener(new ReportGameListener()));
/*     */   }
/*     */   
/*     */   public List<GameTestInfo> getTestInfos() {
/* 120 */     return this.allTestInfos;
/*     */   }
/*     */   
/*     */   public void start() {
/* 124 */     this.stopped = false;
/* 125 */     runBatch(0);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 129 */     this.stopped = true;
/* 130 */     if (this.currentEnvironment != null)
/* 131 */       endCurrentEnvironment(); 
/*     */   }
/*     */   
/*     */   public void rerunTest(GameTestInfo info) {
/* 136 */     GameTestInfo copy = info.copyReset();
/* 137 */     info.getListeners().forEach(listener -> listener.testAddedForRerun(info, copy, this));
/* 139 */     this.allTestInfos.add(copy);
/* 140 */     this.scheduledForRerun.add(copy);
/* 142 */     if (this.stopped)
/* 143 */       runScheduledRerunTests(); 
/*     */   }
/*     */   
/*     */   private void runBatch(final int batchIndex) {
/* 148 */     if (batchIndex >= this.batches.size()) {
/* 150 */       endCurrentEnvironment();
/* 151 */       runScheduledRerunTests();
/*     */       return;
/*     */     } 
/* 153 */     if (batchIndex > 0 && this.clearBetweenBatches) {
/* 154 */       GameTestBatch lastBatch = (GameTestBatch)this.batches.get(batchIndex - 1);
/* 155 */       lastBatch.gameTestInfos().forEach(gameTestInfo -> {
/*     */             TestInstanceBlockEntity testInstanceBlockEntity = gameTestInfo.getTestInstanceBlockEntity();
/*     */             StructureUtils.clearSpaceForStructure(testInstanceBlockEntity.getTestBoundingBox(), this.level);
/*     */             this.level.destroyBlock(testInstanceBlockEntity.getBlockPos(), false);
/*     */           });
/*     */     } 
/* 162 */     final GameTestBatch currentBatch = (GameTestBatch)this.batches.get(batchIndex);
/* 163 */     this.existingStructureSpawner.onBatchStart(this.level);
/* 164 */     this.newStructureSpawner.onBatchStart(this.level);
/* 165 */     Collection<GameTestInfo> testInfosForThisBatch = createStructuresForBatch(currentBatch.gameTestInfos());
/* 167 */     LOGGER.info("Running test environment '{}' batch {} ({} tests)...", new Object[] { currentBatch.environment().getRegisteredName(), Integer.valueOf(currentBatch.index()), Integer.valueOf(testInfosForThisBatch.size()) });
/* 168 */     endCurrentEnvironment();
/* 169 */     this.currentEnvironment = TestEnvironmentDefinition.activate((TestEnvironmentDefinition)currentBatch.environment().value(), this.level);
/* 171 */     this.batchListeners.forEach(listener -> listener.testBatchStarting(currentBatch));
/* 173 */     final MultipleTestTracker currentBatchTracker = new MultipleTestTracker();
/* 174 */     Objects.requireNonNull(currentBatchTracker);
/* 174 */     testInfosForThisBatch.forEach(currentBatchTracker::addTestToTrack);
/* 175 */     currentBatchTracker.addListener(new GameTestListener() {
/*     */           private void testCompleted(GameTestInfo testInfo) {
/* 177 */             if (currentBatchTracker.isDone()) {
/* 178 */               GameTestRunner.this.batchListeners.forEach(listener -> listener.testBatchFinished(currentBatch));
/* 179 */               LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 180 */               longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 181 */               GameTestRunner.this.runBatch(batchIndex + 1);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */           
/*     */           public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {
/* 191 */             testInfo.getTestInstanceBlockEntity().removeBarriers();
/* 192 */             testCompleted(testInfo);
/*     */           }
/*     */           
/*     */           public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 197 */             if (GameTestRunner.this.haltOnError) {
/* 198 */               GameTestRunner.this.endCurrentEnvironment();
/* 199 */               LongArraySet longArraySet = new LongArraySet(GameTestRunner.this.level.getForceLoadedChunks());
/* 200 */               longArraySet.forEach(pos -> GameTestRunner.this.level.setChunkForced(ChunkPos.getX(pos), ChunkPos.getZ(pos), false));
/* 201 */               GameTestTicker.SINGLETON.clear();
/*     */             } else {
/* 203 */               testCompleted(testInfo);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */         });
/* 212 */     Objects.requireNonNull(this.testTicker);
/* 212 */     testInfosForThisBatch.forEach(this.testTicker::add);
/*     */   }
/*     */   
/*     */   private void endCurrentEnvironment() {
/* 216 */     if (this.currentEnvironment != null) {
/* 217 */       this.currentEnvironment.teardown();
/* 218 */       this.currentEnvironment = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runScheduledRerunTests() {
/* 223 */     if (!this.scheduledForRerun.isEmpty()) {
/* 224 */       LOGGER.info("Starting re-run of tests: {}", this.scheduledForRerun.stream().map(info -> info.id().toString()).collect(Collectors.joining(", ")));
/* 225 */       this.batches = ImmutableList.copyOf(this.testBatcher.batch(this.scheduledForRerun));
/* 226 */       this.scheduledForRerun.clear();
/* 227 */       this.stopped = false;
/* 228 */       runBatch(0);
/*     */     } else {
/* 230 */       this.batches = ImmutableList.of();
/* 231 */       this.stopped = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addListener(GameTestBatchListener listener) {
/* 236 */     this.batchListeners.add(listener);
/*     */   }
/*     */   
/*     */   private Collection<GameTestInfo> createStructuresForBatch(Collection<GameTestInfo> batch) {
/* 240 */     return batch.stream().map(this::spawn).flatMap(Optional::stream).toList();
/*     */   }
/*     */   
/*     */   private Optional<GameTestInfo> spawn(GameTestInfo testInfo) {
/* 244 */     if (testInfo.getTestBlockPos() == null)
/* 245 */       return this.newStructureSpawner.spawnStructure(testInfo); 
/* 247 */     return this.existingStructureSpawner.spawnStructure(testInfo);
/*     */   }
/*     */   
/*     */   public static interface GameTestBatcher {
/*     */     Collection<GameTestBatch> batch(Collection<GameTestInfo> param1Collection);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestRunner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */