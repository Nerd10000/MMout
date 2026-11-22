/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Initializer<T> {
/*     */   default Initializer<T> andThen(Initializer<T> other) {
/* 118 */     return (components, context, key) -> {
/*     */         run(components, context, key);
/*     */         other.run(components, context, key);
/*     */       };
/*     */   }
/*     */   
/*     */   default <C> Initializer<T> add(DataComponentType<C> type, C value) {
/* 125 */     return andThen((components, context, key) -> components.set(type, value));
/*     */   }
/*     */   
/*     */   void run(DataComponentMap.Builder paramBuilder, HolderLookup.Provider paramProvider, ResourceKey<T> paramResourceKey);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$Initializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */