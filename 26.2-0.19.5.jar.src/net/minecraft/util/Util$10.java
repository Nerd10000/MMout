/*      */ package net.minecraft.util;
/*      */ 
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.function.Function;
/*      */ 
/*      */ class null implements Function<T, R> {
/* 1076 */   private final Map<T, R> cache = new ConcurrentHashMap<>();
/*      */   
/*      */   public R apply(T arg) {
/* 1080 */     return this.cache.computeIfAbsent(arg, function);
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1085 */     return "memoize/1[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]";
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$10.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */