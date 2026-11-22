/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ class CounterCollector {
/*     */   private long selfValue;
/*     */   
/*     */   private long totalValue;
/*     */   
/* 294 */   private final Map<String, CounterCollector> children = Maps.newHashMap();
/*     */   
/*     */   public void addValue(Iterator<String> path, long value) {
/* 297 */     this.totalValue += value;
/* 298 */     if (!path.hasNext()) {
/* 299 */       this.selfValue += value;
/*     */     } else {
/* 301 */       ((CounterCollector)this.children.computeIfAbsent(path.next(), k -> new CounterCollector())).addValue(path, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\FilledProfileResults$CounterCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */