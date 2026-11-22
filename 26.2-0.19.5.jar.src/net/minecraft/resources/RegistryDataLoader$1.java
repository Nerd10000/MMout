/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ 
/*     */ class null implements RegistryDataLoader.LoaderFactory {
/*     */   public <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> data, Map<ResourceKey<?>, Exception> loadingErrors) {
/* 189 */     return new ResourceManagerRegistryLoadTask<>(data, Lifecycle.stable(), loadingErrors, resourceManager);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */