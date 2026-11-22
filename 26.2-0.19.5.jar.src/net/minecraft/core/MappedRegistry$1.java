/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements HolderGetter<T> {
/*     */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 383 */     return Optional.of(getOrThrow(id));
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 388 */     return MappedRegistry.this.getOrCreateHolderOrThrow(id);
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 393 */     return Optional.of(getOrThrow(id));
/*     */   }
/*     */   
/*     */   public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 398 */     return MappedRegistry.this.getOrCreateTagForRegistration(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */