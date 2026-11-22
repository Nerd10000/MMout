/*    */ package net.minecraft.core.registries;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public class ConcurrentHolderGetter<T> implements HolderGetter<T> {
/*    */   private final Object lock;
/*    */   
/*    */   private final HolderGetter<T> original;
/*    */   
/* 17 */   private final Map<ResourceKey<T>, Optional<Holder.Reference<T>>> elementCache = new ConcurrentHashMap<>();
/*    */   
/* 18 */   private final Map<TagKey<T>, Optional<HolderSet.Named<T>>> tagCache = new ConcurrentHashMap<>();
/*    */   
/*    */   public ConcurrentHolderGetter(Object lock, HolderGetter<T> original) {
/* 21 */     this.lock = lock;
/* 22 */     this.original = original;
/*    */   }
/*    */   
/*    */   public Optional<Holder.Reference<T>> get(ResourceKey<T> elementId) {
/* 27 */     return this.elementCache.computeIfAbsent(elementId, id -> {
/*    */           synchronized (this.lock) {
/*    */             return this.original.get(id);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public Optional<HolderSet.Named<T>> get(TagKey<T> tagId) {
/* 36 */     return this.tagCache.computeIfAbsent(tagId, id -> {
/*    */           synchronized (this.lock) {
/*    */             return this.original.get(id);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\registries\ConcurrentHolderGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */