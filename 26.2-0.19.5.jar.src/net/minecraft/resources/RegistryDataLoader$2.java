/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ 
/*     */ class null implements RegistryDataLoader.LoaderFactory {
/*     */   public <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 205 */     return new NetworkRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, entries, knownDataSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */