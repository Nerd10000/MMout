/*    */ package net.minecraft.util.random;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class WeightedRandom {
/*    */   public static <T> int getTotalWeight(List<T> items, ToIntFunction<T> weightGetter) {
/* 15 */     long totalWeight = 0L;
/* 16 */     for (T item : items)
/* 17 */       totalWeight += weightGetter.applyAsInt(item); 
/* 20 */     if (totalWeight > 2147483647L)
/* 21 */       throw new IllegalArgumentException("Sum of weights must be <= 2147483647"); 
/* 23 */     return (int)totalWeight;
/*    */   }
/*    */   
/*    */   public static <T> Optional<T> getRandomItem(RandomSource random, List<T> items, int totalWeight, ToIntFunction<T> weightGetter) {
/* 27 */     if (totalWeight < 0)
/* 28 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Negative total weight in getRandomItem")); 
/* 31 */     if (totalWeight == 0)
/* 32 */       return Optional.empty(); 
/* 35 */     int selection = random.nextInt(totalWeight);
/* 36 */     return getWeightedItem(items, selection, weightGetter);
/*    */   }
/*    */   
/*    */   public static <T> Optional<T> getWeightedItem(List<T> items, int index, ToIntFunction<T> weightGetter) {
/* 40 */     for (T item : items) {
/* 41 */       index -= weightGetter.applyAsInt(item);
/* 42 */       if (index < 0)
/* 43 */         return Optional.of(item); 
/*    */     } 
/* 46 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public static <T> Optional<T> getRandomItem(RandomSource random, List<T> items, ToIntFunction<T> weightGetter) {
/* 50 */     return getRandomItem(random, items, getTotalWeight(items, weightGetter), weightGetter);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\random\WeightedRandom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */