/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ class Compact<E> implements WeightedList.Selector<E> {
/*     */   private final Weighted<?>[] entries;
/*     */   
/*     */   private Compact(List<Weighted<E>> entries) {
/* 202 */     this.entries = (Weighted<?>[])entries.toArray(x$0 -> new Weighted[x$0]);
/*     */   }
/*     */   
/*     */   public E get(int selection) {
/* 208 */     for (Weighted<?> entry : this.entries) {
/* 209 */       selection -= entry.weight();
/* 210 */       if (selection < 0)
/* 211 */         return (E)entry.value(); 
/*     */     } 
/* 214 */     throw new IllegalStateException("" + selection + " exceeded total weight");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\random\WeightedList$Compact.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */