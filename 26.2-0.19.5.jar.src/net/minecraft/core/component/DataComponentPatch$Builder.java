/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public class Builder {
/* 305 */   private final Reference2ObjectMap<DataComponentType<?>, Optional<?>> map = (Reference2ObjectMap<DataComponentType<?>, Optional<?>>)new Reference2ObjectArrayMap();
/*     */   
/*     */   public <T> Builder set(DataComponentType<T> type, T value) {
/* 311 */     this.map.put(type, Optional.of(value));
/* 312 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder remove(DataComponentType<T> type) {
/* 316 */     this.map.put(type, Optional.empty());
/* 317 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder set(TypedDataComponent<T> component) {
/* 321 */     return set(component.type(), component.value());
/*     */   }
/*     */   
/*     */   public <T> Builder set(Iterable<TypedDataComponent<?>> components) {
/* 325 */     for (TypedDataComponent<?> component : components)
/* 326 */       set(component); 
/* 328 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentPatch build() {
/* 332 */     if (this.map.isEmpty())
/* 333 */       return DataComponentPatch.EMPTY; 
/* 335 */     return new DataComponentPatch(this.map);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */