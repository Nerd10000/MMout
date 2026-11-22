/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import jdk.jfr.FlightRecorder;
/*     */ import jdk.jfr.FlightRecorderListener;
/*     */ import jdk.jfr.Recording;
/*     */ 
/*     */ class null implements FlightRecorderListener {
/* 209 */   private final SummaryReporter summaryReporter = new SummaryReporter(() -> JfrProfiler.this.recording = null);
/*     */   
/*     */   public void recordingStateChanged(Recording rec) {
/* 213 */     if (rec != JfrProfiler.this.recording)
/*     */       return; 
/* 216 */     switch (JfrProfiler.null.$SwitchMap$jdk$jfr$RecordingState[rec.getState().ordinal()]) {
/*     */       case 1:
/* 218 */         this.summaryReporter.recordingStopped(rec.getDestination());
/* 219 */         FlightRecorder.removeListener(this);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\JfrProfiler$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */