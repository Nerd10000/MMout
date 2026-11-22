/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.tags.TagBuilder;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {
/*     */   static <T> TagLookup<T> empty() {
/* 119 */     return id -> Optional.empty();
/*     */   }
/*     */   
/*     */   default boolean contains(TagKey<T> key) {
/* 123 */     return apply(key).isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\TagsProvider$TagLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */