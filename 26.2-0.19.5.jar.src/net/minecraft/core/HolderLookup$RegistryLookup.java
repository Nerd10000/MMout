/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.flag.FeatureElement;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public interface RegistryLookup<T> extends HolderLookup<T>, HolderOwner<T> {
/*    */   default RegistryLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
/* 39 */     if (FeatureElement.FILTERED_REGISTRIES.contains(key()))
/* 40 */       return filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures)); 
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   default RegistryLookup<T> filterElements(final Predicate<T> filter) {
/* 47 */     return new Delegate<T>() {
/*    */         public HolderLookup.RegistryLookup<T> parent() {
/* 50 */           return HolderLookup.RegistryLookup.this;
/*    */         }
/*    */         
/*    */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 55 */           return parent().get(id).filter(holder -> filter.test(holder.value()));
/*    */         }
/*    */         
/*    */         public Stream<Holder.Reference<T>> listElements() {
/* 60 */           return parent().listElements().filter(e -> filter.test(e.value()));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   ResourceKey<? extends Registry<? extends T>> key();
/*    */   
/*    */   Lifecycle registryLifecycle();
/*    */   
/*    */   public static interface Delegate<T> extends RegistryLookup<T> {
/*    */     HolderLookup.RegistryLookup<T> parent();
/*    */     
/*    */     default ResourceKey<? extends Registry<? extends T>> key() {
/* 70 */       return parent().key();
/*    */     }
/*    */     
/*    */     default Lifecycle registryLifecycle() {
/* 75 */       return parent().registryLifecycle();
/*    */     }
/*    */     
/*    */     default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 80 */       return parent().get(id);
/*    */     }
/*    */     
/*    */     default Stream<Holder.Reference<T>> listElements() {
/* 85 */       return parent().listElements();
/*    */     }
/*    */     
/*    */     default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 90 */       return parent().get(id);
/*    */     }
/*    */     
/*    */     default Stream<HolderSet.Named<T>> listTags() {
/* 95 */       return parent().listTags();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup$RegistryLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */