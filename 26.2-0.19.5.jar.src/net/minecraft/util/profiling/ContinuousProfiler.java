/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.function.IntSupplier;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ public class ContinuousProfiler {
/*    */   private final LongSupplier realTime;
/*    */   
/*    */   private final IntSupplier tickCount;
/*    */   
/*    */   private final BooleanSupplier suppressWarnings;
/*    */   
/* 11 */   private ProfileCollector profiler = InactiveProfiler.INSTANCE;
/*    */   
/*    */   public ContinuousProfiler(LongSupplier realTime, IntSupplier tickCount, BooleanSupplier suppressWarnings) {
/* 14 */     this.realTime = realTime;
/* 15 */     this.tickCount = tickCount;
/* 16 */     this.suppressWarnings = suppressWarnings;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 20 */     return (this.profiler != InactiveProfiler.INSTANCE);
/*    */   }
/*    */   
/*    */   public void disable() {
/* 24 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public void enable() {
/* 28 */     this.profiler = new ActiveProfiler(this.realTime, this.tickCount, this.suppressWarnings);
/*    */   }
/*    */   
/*    */   public ProfilerFiller getFiller() {
/* 32 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public ProfileResults getResults() {
/* 36 */     return this.profiler.getResults();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ContinuousProfiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */