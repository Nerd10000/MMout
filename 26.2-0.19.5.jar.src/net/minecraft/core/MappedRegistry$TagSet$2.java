/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements MappedRegistry.TagSet<T> {
/*     */   public boolean isBound() {
/* 502 */     return true;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 507 */     return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 512 */     tags.forEach(action);
/*     */   }
/*     */   
/*     */   public Stream<HolderSet.Named<T>> getTags() {
/* 517 */     return tags.values().stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$TagSet$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */