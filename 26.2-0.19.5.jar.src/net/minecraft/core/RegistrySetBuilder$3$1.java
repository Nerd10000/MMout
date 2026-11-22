/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements RegistryOps.RegistryInfoLookup {
/*     */   public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/* 316 */     return RegistrySetBuilder.null.this.<T>getEntry(registryKey).map(RegistrySetBuilder.Entry::opsInfo);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$3$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */