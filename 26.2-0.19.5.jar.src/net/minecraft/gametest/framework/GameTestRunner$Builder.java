/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ public class Builder {
/*     */   private final ServerLevel level;
/*     */   
/*  53 */   private final GameTestTicker testTicker = GameTestTicker.SINGLETON;
/*     */   
/*  54 */   private GameTestRunner.GameTestBatcher batcher = GameTestBatchFactory.fromGameTestInfo();
/*     */   
/*  55 */   private GameTestRunner.StructureSpawner existingStructureSpawner = GameTestRunner.StructureSpawner.IN_PLACE;
/*     */   
/*  56 */   private GameTestRunner.StructureSpawner newStructureSpawner = GameTestRunner.StructureSpawner.NOT_SET;
/*     */   
/*     */   private final Collection<GameTestBatch> batches;
/*     */   
/*     */   private boolean haltOnError = false;
/*     */   
/*     */   private boolean clearBetweenBatches = false;
/*     */   
/*     */   private Builder(Collection<GameTestBatch> batches, ServerLevel level) {
/*  62 */     this.batches = batches;
/*  63 */     this.level = level;
/*     */   }
/*     */   
/*     */   public static Builder fromBatches(Collection<GameTestBatch> batches, ServerLevel level) {
/*  67 */     return new Builder(batches, level);
/*     */   }
/*     */   
/*     */   public static Builder fromInfo(Collection<GameTestInfo> tests, ServerLevel level) {
/*  71 */     return fromBatches(GameTestBatchFactory.fromGameTestInfo().batch(tests), level);
/*     */   }
/*     */   
/*     */   public Builder haltOnError() {
/*  75 */     this.haltOnError = true;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public Builder clearBetweenBatches() {
/*  80 */     this.clearBetweenBatches = true;
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public Builder newStructureSpawner(GameTestRunner.StructureSpawner structureSpawner) {
/*  85 */     this.newStructureSpawner = structureSpawner;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public Builder existingStructureSpawner(StructureGridSpawner spawner) {
/*  90 */     this.existingStructureSpawner = spawner;
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Builder batcher(GameTestRunner.GameTestBatcher batcher) {
/*  95 */     this.batcher = batcher;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public GameTestRunner build() {
/* 100 */     return new GameTestRunner(this.batcher, this.batches, this.level, this.testTicker, this.existingStructureSpawner, this.newStructureSpawner, this.haltOnError, this.clearBetweenBatches);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestRunner$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */