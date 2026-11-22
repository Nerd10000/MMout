/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Builder {
/*  95 */   private final List<TypedDataComponent<?>> expectedComponents = new ArrayList<>();
/*     */   
/*     */   public <T> Builder expect(TypedDataComponent<T> value) {
/* 101 */     return expect(value.type(), value.value());
/*     */   }
/*     */   
/*     */   public <T> Builder expect(DataComponentType<? super T> type, T value) {
/* 105 */     for (TypedDataComponent<?> component : this.expectedComponents) {
/* 106 */       if (component.type() == type)
/* 107 */         throw new IllegalArgumentException("Predicate already has component of type: '" + String.valueOf(type) + "'"); 
/*     */     } 
/* 110 */     this.expectedComponents.add(new TypedDataComponent(type, value));
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentExactPredicate build() {
/* 115 */     return new DataComponentExactPredicate(List.copyOf(this.expectedComponents));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentExactPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */