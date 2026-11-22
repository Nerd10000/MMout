/*      */ package net.minecraft.util;
/*      */ 
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.function.BiFunction;
/*      */ 
/*      */ class null implements BiFunction<T, U, R> {
/* 1092 */   private final Map<Pair<T, U>, R> cache = new ConcurrentHashMap<>();
/*      */   
/*      */   public R apply(T a, U b) {
/* 1096 */     return this.cache.computeIfAbsent(Pair.of(a, b), args -> function.apply(args.getFirst(), args.getSecond()));
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1101 */     return "memoize/2[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]";
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$11.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */