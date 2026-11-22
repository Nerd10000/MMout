/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ class JitteredLinearRetry {
/*     */   private static final int MIN_INTERVAL_INCREASE = 40;
/*     */   
/*     */   private static final int MAX_INTERVAL_INCREASE = 80;
/*     */   
/*     */   private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */   
/*     */   private final RandomSource random;
/*     */   
/*     */   private long previousAttemptTimestamp;
/*     */   
/*     */   private long nextScheduledAttemptTimestamp;
/*     */   
/*     */   private int currentDelay;
/*     */   
/*     */   public JitteredLinearRetry(RandomSource random, long firstAttemptTimestamp) {
/* 150 */     this.random = random;
/* 151 */     markAttempt(firstAttemptTimestamp);
/*     */   }
/*     */   
/*     */   public void markAttempt(long timestamp) {
/* 155 */     this.previousAttemptTimestamp = timestamp;
/* 156 */     int suggestedDelay = this.currentDelay + this.random.nextInt(40) + 40;
/* 157 */     this.currentDelay = Math.min(suggestedDelay, 400);
/* 158 */     this.nextScheduledAttemptTimestamp = timestamp + this.currentDelay;
/*     */   }
/*     */   
/*     */   public boolean isStillValid(long timestamp) {
/* 162 */     return (timestamp - this.previousAttemptTimestamp < 400L);
/*     */   }
/*     */   
/*     */   public boolean shouldRetry(long timestamp) {
/* 166 */     return (timestamp >= this.nextScheduledAttemptTimestamp);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 171 */     return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi$JitteredLinearRetry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */