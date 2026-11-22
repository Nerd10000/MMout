/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import net.minecraft.util.profiling.InactiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public class InactiveMetricsRecorder implements MetricsRecorder {
/*  7 */   public static final MetricsRecorder INSTANCE = new InactiveMetricsRecorder();
/*    */   
/*    */   public void end() {}
/*    */   
/*    */   public void cancel() {}
/*    */   
/*    */   public void startTick() {}
/*    */   
/*    */   public void sampleDuringExtract() {}
/*    */   
/*    */   public boolean isRecording() {
/* 27 */     return false;
/*    */   }
/*    */   
/*    */   public ProfilerFiller getProfiler() {
/* 32 */     return (ProfilerFiller)InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public void endTick() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\InactiveMetricsRecorder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */