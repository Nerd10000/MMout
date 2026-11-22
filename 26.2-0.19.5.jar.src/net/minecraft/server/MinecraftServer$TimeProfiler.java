/*      */ package net.minecraft.server;
/*      */ 
/*      */ import java.nio.file.Path;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.ResultField;
/*      */ 
/*      */ class TimeProfiler {
/*      */   private final long startNanos;
/*      */   
/*      */   private final int startTick;
/*      */   
/*      */   private TimeProfiler(long startNanos, int startTick) {
/* 2183 */     this.startNanos = startNanos;
/* 2184 */     this.startTick = startTick;
/*      */   }
/*      */   
/*      */   private ProfileResults stop(final long stopNanos, final int stopTick) {
/* 2188 */     return new ProfileResults() {
/*      */         public List<ResultField> getTimes(String path) {
/* 2191 */           return Collections.emptyList();
/*      */         }
/*      */         
/*      */         public boolean saveResults(Path file) {
/* 2196 */           return false;
/*      */         }
/*      */         
/*      */         public long getStartTimeNano() {
/* 2201 */           return MinecraftServer.TimeProfiler.this.startNanos;
/*      */         }
/*      */         
/*      */         public int getStartTimeTicks() {
/* 2206 */           return MinecraftServer.TimeProfiler.this.startTick;
/*      */         }
/*      */         
/*      */         public long getEndTimeNano() {
/* 2211 */           return stopNanos;
/*      */         }
/*      */         
/*      */         public int getEndTimeTicks() {
/* 2216 */           return stopTick;
/*      */         }
/*      */         
/*      */         public String getProfilerResults() {
/* 2221 */           return "";
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer$TimeProfiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */