/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public interface HolderLookup<T> extends HolderGetter<T> {
/*     */   Stream<Holder.Reference<T>> listElements();
/*     */   
/*     */   default Stream<ResourceKey<T>> listElementIds() {
/*  23 */     return listElements().map(Holder.Reference::key);
/*     */   }
/*     */   
/*     */   Stream<HolderSet.Named<T>> listTags();
/*     */   
/*     */   default Stream<TagKey<T>> listTagIds() {
/*  29 */     return listTags().map(HolderSet.Named::key);
/*     */   }
/*     */   
/*     */   public static interface RegistryLookup<T> extends HolderLookup<T>, HolderOwner<T> {
/*     */     default RegistryLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
/*  39 */       if (FeatureElement.FILTERED_REGISTRIES.contains(key()))
/*  40 */         return filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures)); 
/*  43 */       return this;
/*     */     }
/*     */     
/*     */     default RegistryLookup<T> filterElements(final Predicate<T> filter) {
/*  47 */       return new Delegate<T>() {
/*     */           public HolderLookup.RegistryLookup<T> parent() {
/*  50 */             return HolderLookup.RegistryLookup.this;
/*     */           }
/*     */           
/*     */           public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*  55 */             return parent().get(id).filter(holder -> filter.test(holder.value()));
/*     */           }
/*     */           
/*     */           public Stream<Holder.Reference<T>> listElements() {
/*  60 */             return parent().listElements().filter(e -> filter.test(e.value()));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     
/*     */     Lifecycle registryLifecycle();
/*     */     
/*     */     public static interface Delegate<T> extends RegistryLookup<T> {
/*     */       HolderLookup.RegistryLookup<T> parent();
/*     */       
/*     */       default ResourceKey<? extends Registry<? extends T>> key() {
/*  70 */         return parent().key();
/*     */       }
/*     */       
/*     */       default Lifecycle registryLifecycle() {
/*  75 */         return parent().registryLifecycle();
/*     */       }
/*     */       
/*     */       default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*  80 */         return parent().get(id);
/*     */       }
/*     */       
/*     */       default Stream<Holder.Reference<T>> listElements() {
/*  85 */         return parent().listElements();
/*     */       }
/*     */       
/*     */       default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*  90 */         return parent().get(id);
/*     */       }
/*     */       
/*     */       default Stream<HolderSet.Named<T>> listTags() {
/*  95 */         return parent().listTags();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements RegistryLookup.Delegate<T> {
/*     */     public HolderLookup.RegistryLookup<T> parent() {
/*     */       return HolderLookup.RegistryLookup.this;
/*     */     }
/*     */     
/*     */     public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*     */       return parent().get(id).filter(holder -> filter.test(holder.value()));
/*     */     }
/*     */     
/*     */     public Stream<Holder.Reference<T>> listElements() {
/*     */       return parent().listElements().filter(e -> filter.test(e.value()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Delegate<T> extends RegistryLookup<T> {
/*     */     HolderLookup.RegistryLookup<T> parent();
/*     */     
/*     */     default ResourceKey<? extends Registry<? extends T>> key() {
/*     */       return parent().key();
/*     */     }
/*     */     
/*     */     default Lifecycle registryLifecycle() {
/*     */       return parent().registryLifecycle();
/*     */     }
/*     */     
/*     */     default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*     */       return parent().get(id);
/*     */     }
/*     */     
/*     */     default Stream<Holder.Reference<T>> listElements() {
/*     */       return parent().listElements();
/*     */     }
/*     */     
/*     */     default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*     */       return parent().get(id);
/*     */     }
/*     */     
/*     */     default Stream<HolderSet.Named<T>> listTags() {
/*  95 */       return parent().listTags();
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Provider extends HolderGetter.Provider {
/*     */     default Stream<HolderLookup.RegistryLookup<?>> listRegistries() {
/* 104 */       return listRegistryKeys().map(this::lookupOrThrow);
/*     */     }
/*     */     
/*     */     default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 112 */       return (HolderLookup.RegistryLookup<T>)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*     */     }
/*     */     
/*     */     default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/* 116 */       return RegistryOps.create(parent, this);
/*     */     }
/*     */     
/*     */     static Provider create(Stream<HolderLookup.RegistryLookup<?>> lookups) {
/* 120 */       final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> map = lookups.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::key, e -> e));
/* 121 */       return new Provider() {
/*     */           public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 124 */             return map.keySet().stream();
/*     */           }
/*     */           
/*     */           public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 130 */             return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     default Lifecycle allRegistriesLifecycle() {
/* 140 */       return listRegistries().<Lifecycle>map(HolderLookup.RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add);
/*     */     }
/*     */     
/*     */     Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();
/*     */     
/*     */     <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*     */   }
/*     */   
/*     */   class null implements Provider {
/*     */     public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/*     */       return map.keySet().stream();
/*     */     }
/*     */     
/*     */     public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/*     */       return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */