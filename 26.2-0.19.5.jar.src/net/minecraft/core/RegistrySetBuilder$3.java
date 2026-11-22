/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements HolderLookup.Provider {
/*     */   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 298 */     return lookups.keySet().stream();
/*     */   }
/*     */   
/*     */   private <T> Optional<RegistrySetBuilder.Entry<T>> getEntry(ResourceKey<? extends Registry<? extends T>> key) {
/* 303 */     return Optional.ofNullable((RegistrySetBuilder.Entry<T>)lookups.get(key));
/*     */   }
/*     */   
/*     */   public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 308 */     return getEntry(key).map(RegistrySetBuilder.Entry::lookup);
/*     */   }
/*     */   
/*     */   public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/* 313 */     return RegistryOps.create(parent, new RegistryOps.RegistryInfoLookup() {
/*     */           public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/* 316 */             return RegistrySetBuilder.null.this.<T>getEntry(registryKey).map(RegistrySetBuilder.Entry::opsInfo);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */