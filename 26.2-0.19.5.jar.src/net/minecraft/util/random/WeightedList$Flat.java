/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ class Flat<E> implements WeightedList.Selector<E> {
/*     */   private final Object[] entries;
/*     */   
/*     */   private Flat(List<Weighted<E>> entries, int totalWeight) {
/* 182 */     this.entries = new Object[totalWeight];
/* 183 */     int i = 0;
/* 184 */     for (Weighted<E> entry : entries) {
/* 185 */       int weight = entry.weight();
/* 186 */       Arrays.fill(this.entries, i, i + weight, entry.value());
/* 187 */       i += weight;
/*     */     } 
/*     */   }
/*     */   
/*     */   public E get(int selection) {
/* 194 */     return (E)this.entries[selection];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\random\WeightedList$Flat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */