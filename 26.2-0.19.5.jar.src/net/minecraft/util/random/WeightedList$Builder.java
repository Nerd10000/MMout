/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Builder<E> {
/* 162 */   private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();
/*     */   
/*     */   public Builder<E> add(E item) {
/* 165 */     return add(item, 1);
/*     */   }
/*     */   
/*     */   public Builder<E> add(E item, int weight) {
/* 169 */     this.result.add(new Weighted<>(item, weight));
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public WeightedList<E> build() {
/* 174 */     return new WeightedList<>((List<? extends Weighted<E>>)this.result.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\random\WeightedList$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */