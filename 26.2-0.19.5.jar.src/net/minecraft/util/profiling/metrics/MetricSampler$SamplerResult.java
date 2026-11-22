/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
/*     */ 
/*     */ public class SamplerResult {
/*     */   private final Int2DoubleMap recording;
/*     */   
/*     */   private final int firstTick;
/*     */   
/*     */   private final int lastTick;
/*     */   
/*     */   public SamplerResult(int firstTick, int lastTick, Int2DoubleMap recording) {
/* 154 */     this.firstTick = firstTick;
/* 155 */     this.lastTick = lastTick;
/* 156 */     this.recording = recording;
/*     */   }
/*     */   
/*     */   public double valueAtTick(int tick) {
/* 160 */     return this.recording.get(tick);
/*     */   }
/*     */   
/*     */   public int getFirstTick() {
/* 164 */     return this.firstTick;
/*     */   }
/*     */   
/*     */   public int getLastTick() {
/* 168 */     return this.lastTick;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricSampler$SamplerResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */