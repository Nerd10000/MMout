/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ 
/*     */ public class PathEntry implements ProfilerPathEntry {
/* 183 */   private long maxDuration = Long.MIN_VALUE;
/*     */   
/* 184 */   private long minDuration = Long.MAX_VALUE;
/*     */   
/*     */   private long accumulatedDuration;
/*     */   
/*     */   private long count;
/*     */   
/* 187 */   private final Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();
/*     */   
/*     */   public long getDuration() {
/* 191 */     return this.accumulatedDuration;
/*     */   }
/*     */   
/*     */   public long getMaxDuration() {
/* 196 */     return this.maxDuration;
/*     */   }
/*     */   
/*     */   public long getCount() {
/* 201 */     return this.count;
/*     */   }
/*     */   
/*     */   public Object2LongMap<String> getCounters() {
/* 206 */     return Object2LongMaps.unmodifiable((Object2LongMap)this.counters);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ActiveProfiler$PathEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */