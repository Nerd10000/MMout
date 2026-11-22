/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface HolderGetter<T> {
/*    */   default Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 14 */     return get(id).<Throwable>orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
/*    */   }
/*    */   
/*    */   default HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 20 */     return get(id).<Throwable>orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(id)));
/*    */   }
/*    */   
/*    */   default Optional<Holder<T>> getRandomElementOf(TagKey<T> tag, RandomSource random) {
/* 24 */     return get(tag).flatMap(holderSet -> holderSet.getRandomElement(random));
/*    */   }
/*    */   
/*    */   Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey);
/*    */   
/*    */   Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey);
/*    */   
/*    */   public static interface Provider {
/*    */     default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 31 */       return (HolderGetter<T>)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*    */     }
/*    */     
/*    */     default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 35 */       return lookup(id.registryKey()).flatMap(l -> l.get(id));
/*    */     }
/*    */     
/*    */     default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 39 */       return (Holder.Reference<T>)lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
/*    */     }
/*    */     
/*    */     default <T> Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 43 */       return lookup(id.registry()).flatMap(l -> l.get(id));
/*    */     }
/*    */     
/*    */     default <T> HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 47 */       return (HolderSet.Named<T>)lookup(id.registry()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(id)));
/*    */     }
/*    */     
/*    */     <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */