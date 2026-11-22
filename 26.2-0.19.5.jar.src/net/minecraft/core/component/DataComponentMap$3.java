/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ class null implements DataComponentMap {
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 133 */     return predicate.test(type) ? DataComponentMap.this.<T>get(type) : null;
/*     */   }
/*     */   
/*     */   public Set<DataComponentType<?>> keySet() {
/* 138 */     Objects.requireNonNull(predicate);
/* 138 */     return Sets.filter(DataComponentMap.this.keySet(), predicate::test);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentMap$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */