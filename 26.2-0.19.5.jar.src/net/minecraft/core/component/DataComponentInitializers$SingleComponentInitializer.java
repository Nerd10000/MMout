/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface SingleComponentInitializer<C> {
/*     */   C create(HolderLookup.Provider paramProvider);
/*     */   
/*     */   default <T> DataComponentInitializers.Initializer<T> asInitializer(DataComponentType<C> type) {
/* 134 */     return (components, context, key) -> components.set(type, create(context));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$SingleComponentInitializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */