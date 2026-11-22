/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistrationInfo;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistrySynchronization;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.server.packs.resources.ResourceProvider;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.tags.TagLoader;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class NetworkRegistryLoadTask<T> extends RegistryLoadTask<T> {
/* 25 */   private static final RegistrationInfo NETWORK_REGISTRATION_INFO = new RegistrationInfo(Optional.empty(), Lifecycle.experimental());
/*    */   
/*    */   private final Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entries;
/*    */   
/*    */   private final ResourceProvider knownDataSource;
/*    */   
/*    */   public NetworkRegistryLoadTask(RegistryDataLoader.RegistryData<T> data, Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors, Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entries, ResourceProvider knownDataSource) {
/* 32 */     super(data, lifecycle, loadingErrors);
/* 33 */     this.entries = entries;
/* 34 */     this.knownDataSource = knownDataSource;
/*    */   }
/*    */   
/*    */   public CompletableFuture<?> load(RegistryOps.RegistryInfoLookup context, Executor executor) {
/* 39 */     RegistryDataLoader.NetworkedRegistryData registryEntries = this.entries.get(registryKey());
/* 40 */     if (registryEntries == null)
/* 42 */       return CompletableFuture.completedFuture(null); 
/* 45 */     RegistryOps<Tag> nbtOps = RegistryOps.create((DynamicOps<Tag>)NbtOps.INSTANCE, context);
/* 46 */     RegistryOps<JsonElement> jsonOps = RegistryOps.create((DynamicOps<JsonElement>)JsonOps.INSTANCE, context);
/* 47 */     FileToIdConverter knownDataPathConverter = FileToIdConverter.registry(registryKey());
/* 49 */     List<CompletableFuture<RegistryLoadTask.PendingRegistration<T>>> elements = new ArrayList<>(registryEntries.elements().size());
/* 52 */     for (RegistrySynchronization.PackedRegistryEntry entry : registryEntries.elements()) {
/* 53 */       ResourceKey<T> elementKey = ResourceKey.create(registryKey(), entry.id());
/* 54 */       Optional<Tag> networkContents = entry.data();
/* 55 */       if (networkContents.isPresent()) {
/* 56 */         elements.add(CompletableFuture.supplyAsync(() -> new RegistryLoadTask.PendingRegistration(elementKey, RegistryLoadTask.PendingRegistration.loadFromNetwork((Decoder<?>)this.data.elementCodec(), nbtOps, elementKey, networkContents.get()), NETWORK_REGISTRATION_INFO), executor));
/*    */         continue;
/*    */       } 
/* 59 */       elements.add(CompletableFuture.supplyAsync(() -> new RegistryLoadTask.PendingRegistration(elementKey, RegistryLoadTask.PendingRegistration.findAndLoadFromResource((Decoder<?>)this.data.elementCodec(), jsonOps, elementKey, knownDataPathConverter, this.knownDataSource), NETWORK_REGISTRATION_INFO), executor));
/*    */     } 
/* 63 */     return Util.sequence(elements).thenAcceptAsync(pendingRegistrations -> {
/*    */           registerElements(pendingRegistrations.stream());
/*    */           Map<TagKey<T>, List<Holder<T>>> pendingTags = TagLoader.loadTagsFromNetwork(registryEntries.tags(), readOnlyRegistry());
/*    */           registerTags(pendingTags);
/* 63 */         }executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\NetworkRegistryLoadTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */