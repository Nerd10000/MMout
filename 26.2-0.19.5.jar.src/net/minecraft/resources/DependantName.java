/*   */ package net.minecraft.resources;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface DependantName<T, V> {
/*   */   V get(ResourceKey<T> paramResourceKey);
/*   */   
/*   */   static <T, V> DependantName<T, V> fixed(V value) {
/* 8 */     return id -> value;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\DependantName.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */