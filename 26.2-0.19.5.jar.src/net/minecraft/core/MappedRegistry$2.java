/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements HolderLookup.RegistryLookup.Delegate<T> {
/*     */   public HolderLookup.RegistryLookup<T> parent() {
/* 431 */     return MappedRegistry.this;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 436 */     return Optional.ofNullable((HolderSet.Named<T>)pendingTags.get(id));
/*     */   }
/*     */   
/*     */   public Stream<HolderSet.Named<T>> listTags() {
/* 441 */     return pendingTags.values().stream();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */