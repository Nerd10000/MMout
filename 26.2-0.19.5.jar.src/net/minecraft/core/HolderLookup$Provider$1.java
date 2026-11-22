/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements HolderLookup.Provider {
/*     */   public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 124 */     return map.keySet().stream();
/*     */   }
/*     */   
/*     */   public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 130 */     return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup$Provider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */