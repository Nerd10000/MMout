/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public class DependencySorter<K, V extends DependencySorter.Entry<K>> {
/* 17 */   private final Map<K, V> contents = new HashMap<>();
/*    */   
/*    */   public DependencySorter<K, V> addEntry(K id, V value) {
/* 20 */     this.contents.put(id, value);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   private void visitDependenciesAndElement(Multimap<K, K> dependencies, Set<K> alreadyVisited, K id, BiConsumer<K, V> output) {
/* 25 */     if (!alreadyVisited.add(id))
/*    */       return; 
/* 29 */     dependencies.get(id).forEach(dependency -> visitDependenciesAndElement(dependencies, alreadyVisited, (K)dependency, output));
/* 31 */     Entry entry = (Entry)this.contents.get(id);
/* 32 */     if (entry != null)
/* 33 */       output.accept(id, (V)entry); 
/*    */   }
/*    */   
/*    */   private static <K> boolean isCyclic(Multimap<K, K> directDependencies, K from, K to) {
/* 38 */     Collection<K> dependencies = directDependencies.get(to);
/* 39 */     if (dependencies.contains(from))
/* 40 */       return true; 
/* 42 */     return dependencies.stream().anyMatch(dep -> isCyclic(directDependencies, from, dep));
/*    */   }
/*    */   
/*    */   private static <K> void addDependencyIfNotCyclic(Multimap<K, K> directDependencies, K from, K to) {
/* 46 */     if (!isCyclic(directDependencies, from, to))
/* 47 */       directDependencies.put(from, to); 
/*    */   }
/*    */   
/*    */   public void orderByDependencies(BiConsumer<K, V> output) {
/* 55 */     HashMultimap hashMultimap = HashMultimap.create();
/* 59 */     this.contents.forEach((id, value) -> value.visitRequiredDependencies(()));
/* 66 */     this.contents.forEach((id, value) -> value.visitOptionalDependencies(()));
/* 73 */     Set<K> alreadyVisited = new HashSet<>();
/* 74 */     this.contents.keySet().forEach(topId -> visitDependenciesAndElement(directDependencies, alreadyVisited, (K)topId, output));
/*    */   }
/*    */   
/*    */   public static interface Entry<K> {
/*    */     void visitRequiredDependencies(Consumer<K> param1Consumer);
/*    */     
/*    */     void visitOptionalDependencies(Consumer<K> param1Consumer);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\DependencySorter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */