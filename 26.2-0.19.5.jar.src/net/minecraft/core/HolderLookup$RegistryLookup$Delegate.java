/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public interface Delegate<T> extends HolderLookup.RegistryLookup<T> {
/*    */   HolderLookup.RegistryLookup<T> parent();
/*    */   
/*    */   default ResourceKey<? extends Registry<? extends T>> key() {
/* 70 */     return parent().key();
/*    */   }
/*    */   
/*    */   default Lifecycle registryLifecycle() {
/* 75 */     return parent().registryLifecycle();
/*    */   }
/*    */   
/*    */   default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 80 */     return parent().get(id);
/*    */   }
/*    */   
/*    */   default Stream<Holder.Reference<T>> listElements() {
/* 85 */     return parent().listElements();
/*    */   }
/*    */   
/*    */   default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 90 */     return parent().get(id);
/*    */   }
/*    */   
/*    */   default Stream<HolderSet.Named<T>> listTags() {
/* 95 */     return parent().listTags();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup$RegistryLookup$Delegate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */