/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.RegistrationInfo;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.tags.TagLoader;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.thread.ParallelMapTransform;
/*    */ 
/*    */ public class ResourceManagerRegistryLoadTask<T> extends RegistryLoadTask<T> {
/*    */   private static final Function<Optional<KnownPack>, RegistrationInfo> REGISTRATION_INFO_CACHE;
/*    */   
/*    */   private final ResourceManager resourceManager;
/*    */   
/*    */   static {
/* 23 */     REGISTRATION_INFO_CACHE = Util.memoize(knownPack -> {
/*    */           Lifecycle lifecycle = knownPack.map(KnownPack::isVanilla).map(()).orElse(Lifecycle.experimental());
/*    */           return new RegistrationInfo(knownPack, lifecycle);
/*    */         });
/*    */   }
/*    */   
/*    */   public ResourceManagerRegistryLoadTask(RegistryDataLoader.RegistryData<T> data, Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors, ResourceManager resourceManager) {
/* 31 */     super(data, lifecycle, loadingErrors);
/* 32 */     this.resourceManager = resourceManager;
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> load(RegistryOps.RegistryInfoLookup context, Executor executor) {
/* 37 */     FileToIdConverter lister = FileToIdConverter.registry(registryKey());
/* 39 */     return CompletableFuture.supplyAsync(() -> lister.listMatchingResources(this.resourceManager), executor)
/* 40 */       .thenCompose(registryResources -> {
/*    */           RegistryOps<JsonElement> ops = RegistryOps.create((DynamicOps<JsonElement>)JsonOps.INSTANCE, context);
/*    */           return ParallelMapTransform.schedule(registryResources, (), executor);
/* 49 */         }).thenAcceptAsync(loadedEntries -> {
/*    */           registerElements(loadedEntries.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue));
/*    */           TagLoader.ElementLookup<Holder<T>> tagElementLookup = TagLoader.ElementLookup.fromGetters(registryKey(), (HolderGetter)this.concurrentRegistrationGetter, (HolderGetter)readOnlyRegistry());
/*    */           Map<TagKey<T>, List<Holder<T>>> pendingTags = TagLoader.loadTagsForRegistry(this.resourceManager, registryKey(), tagElementLookup);
/*    */           registerTags(pendingTags);
/*    */         }executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\ResourceManagerRegistryLoadTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */