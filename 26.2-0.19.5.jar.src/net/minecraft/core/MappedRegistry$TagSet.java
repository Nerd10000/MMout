/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ interface TagSet<T> {
/*     */   static <T> TagSet<T> unbound() {
/* 475 */     return new TagSet<T>() {
/*     */         public boolean isBound() {
/* 478 */           return false;
/*     */         }
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 483 */           throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id));
/*     */         }
/*     */         
/*     */         public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 488 */           throw new IllegalStateException("Tags not bound");
/*     */         }
/*     */         
/*     */         public Stream<HolderSet.Named<T>> getTags() {
/* 493 */           throw new IllegalStateException("Tags not bound");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <T> TagSet<T> fromMap(final Map<TagKey<T>, HolderSet.Named<T>> tags) {
/* 499 */     return new TagSet<T>() {
/*     */         public boolean isBound() {
/* 502 */           return true;
/*     */         }
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 507 */           return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */         }
/*     */         
/*     */         public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 512 */           tags.forEach(action);
/*     */         }
/*     */         
/*     */         public Stream<HolderSet.Named<T>> getTags() {
/* 517 */           return tags.values().stream();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey);
/*     */   
/*     */   void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> paramBiConsumer);
/*     */   
/*     */   Stream<HolderSet.Named<T>> getTags();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$TagSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */