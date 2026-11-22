/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ class null implements RegistryOps.RegistryInfoLookup {
/*     */   public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 253 */     return Optional.ofNullable((RegistryOps.RegistryInfo<T>)result.get(key));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */