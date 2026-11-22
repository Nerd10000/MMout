/*    */ package net.minecraft.data.registries;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.RegistryDataLoader;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistriesDatapackGenerator implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   private final CompletableFuture<HolderLookup.Provider> registries;
/*    */   
/*    */   public RegistriesDatapackGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
/* 24 */     this.registries = registries;
/* 25 */     this.output = output;
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 30 */     return this.registries.thenCompose(access -> {
/*    */           RegistryOps registryOps = access.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])RegistryDataLoader.WORLDGEN_REGISTRIES.stream().flatMap(()).toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   private <T> Optional<CompletableFuture<?>> dumpRegistryCap(CachedOutput cache, HolderLookup.Provider registries, DynamicOps<JsonElement> writeOps, RegistryDataLoader.RegistryData<T> v) {
/* 39 */     ResourceKey<? extends Registry<T>> registryKey = v.key();
/* 40 */     return registries.lookup(registryKey).map(registry -> {
/*    */           PackOutput.PathProvider pathProvider = this.output.createRegistryElementsPathProvider(registryKey);
/*    */           return CompletableFuture.allOf((CompletableFuture<?>[])registry.listElements().map(()).toArray(()));
/*    */         });
/*    */   }
/*    */   
/*    */   private static <E> CompletableFuture<?> dumpValue(Path path, CachedOutput cache, DynamicOps<JsonElement> ops, Encoder<E> codec, E value) {
/* 50 */     return (CompletableFuture)codec.encodeStart(ops, value).mapOrElse(result -> DataProvider.saveStable(cache, result, path), error -> CompletableFuture.failedFuture(new IllegalStateException("Couldn't generate file '" + String.valueOf(path) + "': " + error.message())));
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 58 */     return "Registries";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\registries\RegistriesDatapackGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */