/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements MappedRegistry.TagSet<T> {
/*     */   public boolean isBound() {
/* 478 */     return false;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 483 */     throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id));
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 488 */     throw new IllegalStateException("Tags not bound");
/*     */   }
/*     */   
/*     */   public Stream<HolderSet.Named<T>> getTags() {
/* 493 */     throw new IllegalStateException("Tags not bound");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$TagSet$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */