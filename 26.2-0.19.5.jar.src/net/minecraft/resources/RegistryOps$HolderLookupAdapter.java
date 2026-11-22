/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ final class HolderLookupAdapter implements RegistryOps.RegistryInfoLookup {
/*     */   private final HolderLookup.Provider lookupProvider;
/*     */   
/* 111 */   private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups = new ConcurrentHashMap<>();
/*     */   
/*     */   public HolderLookupAdapter(HolderLookup.Provider lookupProvider) {
/* 114 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */   
/*     */   public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) {
/* 120 */     return (Optional<RegistryOps.RegistryInfo<E>>)this.lookups.computeIfAbsent(registryKey, this::createLookup);
/*     */   }
/*     */   
/*     */   private Optional<RegistryOps.RegistryInfo<Object>> createLookup(ResourceKey<? extends Registry<?>> key) {
/* 124 */     return this.lookupProvider.lookup(key).map(RegistryOps.RegistryInfo::fromRegistryLookup);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 129 */     if (this == obj)
/* 130 */       return true; 
/* 132 */     if (obj instanceof HolderLookupAdapter) {
/* 132 */       HolderLookupAdapter adapter = (HolderLookupAdapter)obj;
/* 132 */       if (this.lookupProvider.equals(adapter.lookupProvider));
/*     */     } 
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 137 */     return this.lookupProvider.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryOps$HolderLookupAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */