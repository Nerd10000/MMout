/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class GameTestInfo {
/*     */   private final Holder.Reference<GameTestInstance> test;
/*     */   
/*     */   private BlockPos testBlockPos;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*  40 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */   
/*     */   private final int timeoutTicks;
/*     */   
/*  44 */   private final Collection<GameTestSequence> sequences = Lists.newCopyOnWriteArrayList();
/*     */   
/*  45 */   private final Object2LongMap<Runnable> runAtTickTimeMap = (Object2LongMap<Runnable>)new Object2LongOpenHashMap();
/*     */   
/*     */   private boolean placedStructure;
/*     */   
/*     */   private boolean chunksLoaded;
/*     */   
/*     */   private int tickCount;
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   private final RetryOptions retryOptions;
/*     */   
/*  52 */   private final Stopwatch timer = Stopwatch.createUnstarted();
/*     */   
/*     */   private boolean done;
/*     */   
/*     */   private final Rotation extraRotation;
/*     */   
/*     */   private GameTestException error;
/*     */   
/*     */   private TestInstanceBlockEntity testInstanceBlockEntity;
/*     */   
/*     */   public GameTestInfo(Holder.Reference<GameTestInstance> test, Rotation extraRotation, ServerLevel level, RetryOptions retryOptions) {
/*  61 */     this.test = test;
/*  62 */     this.level = level;
/*  63 */     this.retryOptions = retryOptions;
/*  64 */     this.timeoutTicks = ((GameTestInstance)test.value()).maxTicks();
/*  65 */     this.extraRotation = extraRotation;
/*     */   }
/*     */   
/*     */   public void setTestBlockPos(BlockPos testBlockPos) {
/*  69 */     this.testBlockPos = testBlockPos;
/*     */   }
/*     */   
/*     */   public GameTestInfo startExecution(int tickDelay) {
/*  73 */     this.tickCount = -(((GameTestInstance)this.test.value()).setupTicks() + tickDelay + 1);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public void placeStructure() {
/*  78 */     if (this.placedStructure)
/*     */       return; 
/*  82 */     TestInstanceBlockEntity test = getTestInstanceBlockEntity();
/*  83 */     if (!test.placeStructure())
/*  84 */       fail((Component)Component.translatable("test.error.structure.failure", new Object[] { test.getTestName().getString() })); 
/*  87 */     this.placedStructure = true;
/*  89 */     test.encaseStructure();
/*  90 */     BoundingBox boundingBox = test.getTestBoundingBox();
/*  91 */     this.level.getBlockTicks().clearArea(boundingBox);
/*  92 */     this.level.clearBlockEvents(boundingBox);
/*  93 */     this.listeners.forEach(listener -> listener.testStructureLoaded(this));
/*     */   }
/*     */   
/*     */   public void tick(GameTestRunner runner) {
/*  97 */     if (isDone())
/*     */       return; 
/* 101 */     if (!this.placedStructure)
/* 102 */       fail((Component)Component.translatable("test.error.ticking_without_structure")); 
/* 105 */     if (this.testInstanceBlockEntity == null)
/* 106 */       fail((Component)Component.translatable("test.error.missing_block_entity")); 
/* 109 */     if (this.error != null) {
/* 110 */       finish();
/*     */     } else {
/* 113 */       Objects.requireNonNull(this.level);
/* 113 */       if (!this.chunksLoaded && !this.testInstanceBlockEntity.getStructureBoundingBox().intersectingChunks().allMatch(this.level::areEntitiesActuallyLoadedAndTicking))
/*     */         return; 
/* 116 */       this.chunksLoaded = true;
/*     */     } 
/* 120 */     tickInternal();
/* 122 */     if (isDone())
/* 123 */       if (this.error != null) {
/* 124 */         this.listeners.forEach(listener -> listener.testFailed(this, runner));
/*     */       } else {
/* 126 */         this.listeners.forEach(listener -> listener.testPassed(this, runner));
/*     */       }  
/*     */   }
/*     */   
/*     */   private void tickInternal() {
/* 132 */     this.tickCount++;
/* 133 */     if (this.tickCount < 0)
/*     */       return; 
/* 136 */     if (!this.started)
/* 137 */       startTest(); 
/* 139 */     ObjectIterator<Object2LongMap.Entry<Runnable>> it = this.runAtTickTimeMap.object2LongEntrySet().iterator();
/* 140 */     while (it.hasNext()) {
/* 141 */       Object2LongMap.Entry<Runnable> entry = (Object2LongMap.Entry<Runnable>)it.next();
/* 142 */       if (entry.getLongValue() <= this.tickCount) {
/*     */         try {
/* 144 */           ((Runnable)entry.getKey()).run();
/* 145 */         } catch (GameTestException error) {
/* 146 */           fail(error);
/* 147 */         } catch (Exception exception) {
/* 148 */           fail(new UnknownGameTestException(exception));
/*     */         } 
/* 150 */         it.remove();
/*     */       } 
/*     */     } 
/* 153 */     if (this.tickCount > this.timeoutTicks) {
/* 155 */       if (this.sequences.isEmpty()) {
/* 156 */         fail(new GameTestTimeoutException((Component)Component.translatable("test.error.timeout.no_result", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) })));
/*     */       } else {
/* 158 */         this.sequences.forEach(ticker -> ticker.tickAndFailIfNotComplete(this.tickCount));
/* 159 */         if (this.error == null)
/* 160 */           fail(new GameTestTimeoutException((Component)Component.translatable("test.error.timeout.no_sequences_finished", new Object[] { Integer.valueOf(((GameTestInstance)this.test.value()).maxTicks()) }))); 
/*     */       } 
/*     */     } else {
/* 165 */       this.sequences.forEach(ticker -> ticker.tickAndContinue(this.tickCount));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startTest() {
/* 170 */     if (this.started)
/*     */       return; 
/* 173 */     this.started = true;
/* 174 */     this.timer.start();
/* 175 */     getTestInstanceBlockEntity().setRunning();
/*     */     try {
/* 177 */       ((GameTestInstance)this.test.value()).run(new GameTestHelper(this));
/* 178 */     } catch (GameTestException e) {
/* 179 */       fail(e);
/* 180 */     } catch (Exception e) {
/* 181 */       fail(new UnknownGameTestException(e));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRunAtTickTime(long time, Runnable assertAtTickTime) {
/* 186 */     this.runAtTickTimeMap.put(assertAtTickTime, time);
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 190 */     return this.test.key().identifier();
/*     */   }
/*     */   
/*     */   public BlockPos getTestBlockPos() {
/* 194 */     return this.testBlockPos;
/*     */   }
/*     */   
/*     */   public BlockPos getTestOrigin() {
/* 198 */     return this.testInstanceBlockEntity.getStartCorner();
/*     */   }
/*     */   
/*     */   public AABB getStructureBounds() {
/* 202 */     TestInstanceBlockEntity blockEntity = getTestInstanceBlockEntity();
/* 203 */     return blockEntity.getStructureBounds();
/*     */   }
/*     */   
/*     */   public TestInstanceBlockEntity getTestInstanceBlockEntity() {
/* 207 */     if (this.testInstanceBlockEntity == null) {
/* 208 */       if (this.testBlockPos == null)
/* 209 */         throw new IllegalStateException("This GameTestInfo has no position"); 
/* 212 */       BlockEntity blockEntity = this.level.getBlockEntity(this.testBlockPos);
/* 212 */       if (blockEntity instanceof TestInstanceBlockEntity) {
/* 212 */         TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 213 */         this.testInstanceBlockEntity = testInstanceBlockEntity;
/*     */       } 
/* 215 */       if (this.testInstanceBlockEntity == null)
/* 216 */         throw new IllegalStateException("Could not find a test instance block entity at the given coordinate " + String.valueOf(this.testBlockPos)); 
/*     */     } 
/* 220 */     return this.testInstanceBlockEntity;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 224 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean hasSucceeded() {
/* 228 */     return (this.done && this.error == null);
/*     */   }
/*     */   
/*     */   public boolean hasFailed() {
/* 232 */     return (this.error != null);
/*     */   }
/*     */   
/*     */   public boolean hasStarted() {
/* 236 */     return this.started;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 240 */     return this.done;
/*     */   }
/*     */   
/*     */   public long getRunTime() {
/* 244 */     return this.timer.elapsed(TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   private void finish() {
/* 248 */     if (!this.done) {
/* 249 */       this.done = true;
/* 250 */       if (this.timer.isRunning())
/* 251 */         this.timer.stop(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void succeed() {
/* 258 */     if (this.error == null) {
/* 259 */       finish();
/* 260 */       AABB bounds = getStructureBounds();
/* 261 */       List<Entity> entities = getLevel().getEntitiesOfClass(Entity.class, bounds.inflate(1.0D), mob -> !(mob instanceof net.minecraft.world.entity.player.Player));
/* 262 */       entities.forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fail(Component message) {
/* 267 */     fail(new GameTestAssertException(message, this.tickCount));
/*     */   }
/*     */   
/*     */   public void fail(GameTestException error) {
/* 271 */     this.error = error;
/*     */   }
/*     */   
/*     */   public GameTestException getError() {
/* 276 */     return this.error;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 281 */     return id().toString();
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener listener) {
/* 285 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public GameTestInfo prepareTestStructure() {
/* 289 */     TestInstanceBlockEntity testInstanceBlock = createTestInstanceBlock(Objects.<BlockPos>requireNonNull(this.testBlockPos), this.extraRotation, this.level);
/* 290 */     if (testInstanceBlock != null) {
/* 291 */       this.testInstanceBlockEntity = testInstanceBlock;
/* 292 */       placeStructure();
/* 293 */       return this;
/*     */     } 
/* 295 */     return null;
/*     */   }
/*     */   
/*     */   private TestInstanceBlockEntity createTestInstanceBlock(BlockPos testPos, Rotation rotation, ServerLevel level) {
/* 299 */     level.setBlockAndUpdate(testPos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/* 301 */     BlockEntity blockEntity = level.getBlockEntity(testPos);
/* 301 */     if (blockEntity instanceof TestInstanceBlockEntity) {
/* 301 */       TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 302 */       ResourceKey<GameTestInstance> test = getTestHolder().key();
/* 303 */       Vec3i size = TestInstanceBlockEntity.getStructureSize(level, test).orElse(new Vec3i(1, 1, 1));
/* 304 */       testInstanceBlockEntity.set(new TestInstanceBlockEntity.Data(Optional.of(test), size, rotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/* 305 */       return testInstanceBlockEntity;
/*     */     } 
/* 307 */     return null;
/*     */   }
/*     */   
/*     */   public int getTick() {
/* 311 */     return this.tickCount;
/*     */   }
/*     */   
/*     */   public GameTestSequence createSequence() {
/* 315 */     GameTestSequence sequence = new GameTestSequence(this);
/* 316 */     this.sequences.add(sequence);
/* 317 */     return sequence;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 321 */     return ((GameTestInstance)this.test.value()).required();
/*     */   }
/*     */   
/*     */   public boolean isOptional() {
/* 325 */     return !((GameTestInstance)this.test.value()).required();
/*     */   }
/*     */   
/*     */   public Identifier getStructure() {
/* 329 */     return ((GameTestInstance)this.test.value()).structure();
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 333 */     return ((GameTestInstance)this.test.value()).info().rotation().getRotated(this.extraRotation);
/*     */   }
/*     */   
/*     */   public GameTestInstance getTest() {
/* 337 */     return (GameTestInstance)this.test.value();
/*     */   }
/*     */   
/*     */   public Holder.Reference<GameTestInstance> getTestHolder() {
/* 341 */     return this.test;
/*     */   }
/*     */   
/*     */   public int getTimeoutTicks() {
/* 345 */     return this.timeoutTicks;
/*     */   }
/*     */   
/*     */   public boolean isFlaky() {
/* 349 */     return (((GameTestInstance)this.test.value()).maxAttempts() > 1);
/*     */   }
/*     */   
/*     */   public int maxAttempts() {
/* 353 */     return ((GameTestInstance)this.test.value()).maxAttempts();
/*     */   }
/*     */   
/*     */   public int requiredSuccesses() {
/* 357 */     return ((GameTestInstance)this.test.value()).requiredSuccesses();
/*     */   }
/*     */   
/*     */   public RetryOptions retryOptions() {
/* 361 */     return this.retryOptions;
/*     */   }
/*     */   
/*     */   public Stream<GameTestListener> getListeners() {
/* 365 */     return this.listeners.stream();
/*     */   }
/*     */   
/*     */   public GameTestInfo copyReset() {
/* 369 */     GameTestInfo i = new GameTestInfo(this.test, this.extraRotation, this.level, retryOptions());
/* 370 */     if (this.testBlockPos != null)
/* 371 */       i.setTestBlockPos(this.testBlockPos); 
/* 373 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */