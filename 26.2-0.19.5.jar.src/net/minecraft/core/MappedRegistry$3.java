/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements Registry.PendingTags<T> {
/*     */   public ResourceKey<? extends Registry<? extends T>> key() {
/* 448 */     return MappedRegistry.this.key();
/*     */   }
/*     */   
/*     */   public int size() {
/* 453 */     return pendingContents.size();
/*     */   }
/*     */   
/*     */   public HolderLookup.RegistryLookup<T> lookup() {
/* 458 */     return patchedHolder;
/*     */   }
/*     */   
/*     */   public void apply() {
/* 463 */     pendingTags.forEach((id, tag) -> {
/*     */           List<Holder<T>> values = (List<Holder<T>>)pendingContents.getOrDefault(id, List.of());
/*     */           tag.bind(values);
/*     */         });
/* 467 */     MappedRegistry.this.allTags = MappedRegistry.TagSet.fromMap((Map<TagKey<?>, HolderSet.Named<?>>)pendingTags);
/* 468 */     MappedRegistry.this.refreshTagsInHolders();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */