/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public final class Graph {
/*    */   public static <T> boolean depthFirstSearch(Map<T, Set<T>> edges, Set<T> discovered, Set<T> currentlyVisiting, Consumer<T> reverseTopologicalOrder, T current) {
/* 22 */     if (discovered.contains(current))
/* 23 */       return false; 
/* 25 */     if (currentlyVisiting.contains(current))
/* 26 */       return true; 
/* 28 */     currentlyVisiting.add(current);
/* 29 */     for (T next : edges.getOrDefault(current, ImmutableSet.of())) {
/* 30 */       if (depthFirstSearch(edges, discovered, currentlyVisiting, reverseTopologicalOrder, next))
/* 31 */         return true; 
/*    */     } 
/* 34 */     currentlyVisiting.remove(current);
/* 35 */     discovered.add(current);
/* 36 */     reverseTopologicalOrder.accept(current);
/* 37 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Graph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */