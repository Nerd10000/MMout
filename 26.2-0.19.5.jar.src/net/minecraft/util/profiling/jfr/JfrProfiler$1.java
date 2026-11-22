/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import jdk.jfr.FlightRecorderListener;
/*     */ import jdk.jfr.Recording;
/*     */ 
/*     */ class null implements FlightRecorderListener {
/*     */   public void recordingStateChanged(Recording rec) {
/* 108 */     switch (JfrProfiler.null.$SwitchMap$jdk$jfr$RecordingState[rec.getState().ordinal()]) {
/*     */       case 1:
/* 109 */         JfrProfiler.this.registerPeriodicEvents();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\JfrProfiler$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */