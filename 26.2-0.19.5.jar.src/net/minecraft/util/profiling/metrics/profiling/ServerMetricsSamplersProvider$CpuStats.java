/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ public class CpuStats {
/*  92 */   private final SystemInfo systemInfo = new SystemInfo();
/*     */   
/*  93 */   private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*     */   
/*  94 */   public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */   
/*  96 */   private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/*     */   
/*  97 */   private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */   
/*     */   private long lastPollMs;
/*     */   
/*     */   public double loadForCpu(int i) {
/* 101 */     long now = System.currentTimeMillis();
/* 102 */     if (this.lastPollMs == 0L || this.lastPollMs + 501L < now) {
/* 103 */       this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 104 */       this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 105 */       this.lastPollMs = now;
/*     */     } 
/* 108 */     return this.currentLoad[i] * 100.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider$CpuStats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */