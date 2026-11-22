/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public interface Provider extends HolderGetter.Provider {
/*     */   default Stream<HolderLookup.RegistryLookup<?>> listRegistries() {
/* 104 */     return listRegistryKeys().map(this::lookupOrThrow);
/*     */   }
/*     */   
/*     */   default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 112 */     return (HolderLookup.RegistryLookup<T>)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*     */   }
/*     */   
/*     */   default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/* 116 */     return RegistryOps.create(parent, this);
/*     */   }
/*     */   
/*     */   static Provider create(Stream<HolderLookup.RegistryLookup<?>> lookups) {
/* 120 */     final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> map = lookups.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::key, e -> e));
/* 121 */     return new Provider() {
/*     */         public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 124 */           return map.keySet().stream();
/*     */         }
/*     */         
/*     */         public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 130 */           return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Lifecycle allRegistriesLifecycle() {
/* 140 */     return listRegistries().<Lifecycle>map(HolderLookup.RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add);
/*     */   }
/*     */   
/*     */   Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();
/*     */   
/*     */   <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup$Provider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */