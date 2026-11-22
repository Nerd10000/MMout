/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySynchronization;
/*     */ import net.minecraft.core.component.DataComponentInitializers;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryDataLoader;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RegistryDataCollector {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private ContentsCollector contentsCollector;
/*     */   
/*     */   private TagCollector tagCollector;
/*     */   
/*     */   public void appendContents(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> elementData) {
/*  38 */     if (this.contentsCollector == null)
/*  39 */       this.contentsCollector = new ContentsCollector(); 
/*  42 */     this.contentsCollector.append(registry, elementData);
/*     */   }
/*     */   
/*     */   public void appendTags(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> data) {
/*  46 */     if (this.tagCollector == null)
/*  47 */       this.tagCollector = new TagCollector(); 
/*  50 */     Objects.requireNonNull(this.tagCollector);
/*  50 */     data.forEach(this.tagCollector::append);
/*     */   }
/*     */   
/*     */   private static class ContentsCollector {
/*  54 */     private final Map<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> elements = new HashMap<>();
/*     */     
/*     */     public void append(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> elementData) {
/*  58 */       ((List<RegistrySynchronization.PackedRegistryEntry>)this.elements.computeIfAbsent(registry, ignore -> new ArrayList())).addAll(elementData);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Registry.PendingTags<T> resolveRegistryTags(RegistryAccess.Frozen context, ResourceKey<? extends Registry<? extends T>> registryKey, TagNetworkSerialization.NetworkPayload tags) {
/*  63 */     Registry<T> staticRegistry = context.lookupOrThrow(registryKey);
/*  64 */     return staticRegistry.prepareTagReload(tags.resolve(staticRegistry));
/*     */   }
/*     */   
/*     */   private RegistryAccess loadNewElementsAndTags(ResourceProvider knownDataSource, ContentsCollector contentsCollector, boolean tagsForSynchronizedRegistriesOnly) {
/*     */     RegistryAccess.Frozen receivedRegistries;
/*  69 */     LayeredRegistryAccess<ClientRegistryLayer> base = ClientRegistryLayer.createRegistryAccess();
/*  70 */     RegistryAccess.Frozen loadingContext = base.getAccessForLoading(ClientRegistryLayer.REMOTE);
/*  72 */     Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entriesToLoad = new HashMap<>();
/*  73 */     contentsCollector.elements.forEach((registryKey, elements) -> entriesToLoad.put(registryKey, new RegistryDataLoader.NetworkedRegistryData(elements, TagNetworkSerialization.NetworkPayload.EMPTY)));
/*  75 */     List<Registry.PendingTags<?>> pendingStaticTags = new ArrayList<>();
/*  76 */     if (this.tagCollector != null)
/*  77 */       this.tagCollector.forEach((registryKey, tags) -> {
/*     */             if (tags.isEmpty())
/*     */               return; 
/*     */             if (RegistrySynchronization.isNetworkable(registryKey)) {
/*     */               entriesToLoad.compute(registryKey, ());
/*     */             } else if (!tagsForSynchronizedRegistriesOnly) {
/*     */               pendingStaticTags.add(resolveRegistryTags(loadingContext, registryKey, tags));
/*     */             } 
/*     */           }); 
/*  96 */     List<HolderLookup.RegistryLookup<?>> contextRegistriesWithTags = TagLoader.buildUpdatedLookups(loadingContext, pendingStaticTags);
/*     */     try {
/*  99 */       long start = Util.getMillis();
/* 100 */       receivedRegistries = RegistryDataLoader.load(entriesToLoad, knownDataSource, contextRegistriesWithTags, RegistryDataLoader.SYNCHRONIZED_REGISTRIES, (Executor)Util.backgroundExecutor()).join();
/* 101 */       long end = Util.getMillis();
/* 102 */       LOGGER.debug("Loading network data took {} ms", Long.valueOf(end - start));
/* 103 */     } catch (Exception e) {
/* 104 */       CrashReport report = CrashReport.forThrowable(e, "Network Registry Load");
/* 105 */       addCrashDetails(report, entriesToLoad, pendingStaticTags);
/* 106 */       throw new ReportedException(report);
/*     */     } 
/* 111 */     RegistryAccess.Frozen frozen1 = base.replaceFrom(ClientRegistryLayer.REMOTE, new RegistryAccess.Frozen[] { receivedRegistries }).compositeAccess();
/* 114 */     pendingStaticTags.forEach(Registry.PendingTags::apply);
/* 115 */     return (RegistryAccess)frozen1;
/*     */   }
/*     */   
/*     */   private static void addCrashDetails(CrashReport report, Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> dynamicRegistries, List<Registry.PendingTags<?>> staticRegistries) {
/* 119 */     CrashReportCategory details = report.addCategory("Received Elements and Tags");
/* 120 */     details.setDetail("Dynamic Registries", () -> (String)dynamicRegistries.entrySet().stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/* 130 */     details.setDetail("Static Registries", () -> (String)staticRegistries.stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */   }
/*     */   
/*     */   private static void loadOnlyTags(TagCollector tagCollector, RegistryAccess.Frozen originalRegistries, boolean includeSharedRegistries) {
/* 141 */     tagCollector.forEach((registryKey, tags) -> {
/*     */           if (includeSharedRegistries || RegistrySynchronization.isNetworkable(registryKey))
/*     */             resolveRegistryTags(originalRegistries, registryKey, tags).apply(); 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void updateComponents(RegistryAccess.Frozen frozenRegistries, boolean includeSharedRegistries) {
/* 149 */     BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build((HolderLookup.Provider)frozenRegistries).forEach(pendingComponents -> {
/*     */           if (includeSharedRegistries || RegistrySynchronization.isNetworkable(pendingComponents.key()))
/*     */             pendingComponents.apply(); 
/*     */         });
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen collectGameRegistries(ResourceProvider knownDataSource, RegistryAccess.Frozen originalRegistries, boolean tagsAndComponentsForSynchronizedRegistriesOnly) {
/*     */     RegistryAccess.Frozen frozen1;
/* 159 */     if (this.contentsCollector != null) {
/* 161 */       RegistryAccess registries = loadNewElementsAndTags(knownDataSource, this.contentsCollector, tagsAndComponentsForSynchronizedRegistriesOnly);
/*     */     } else {
/* 164 */       if (this.tagCollector != null)
/* 165 */         loadOnlyTags(this.tagCollector, originalRegistries, !tagsAndComponentsForSynchronizedRegistriesOnly); 
/* 167 */       frozen1 = originalRegistries;
/*     */     } 
/* 170 */     RegistryAccess.Frozen frozenRegistries = frozen1.freeze();
/* 171 */     updateComponents(frozenRegistries, !tagsAndComponentsForSynchronizedRegistriesOnly);
/* 172 */     return frozenRegistries;
/*     */   }
/*     */   
/*     */   private static class TagCollector {
/*     */     private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;
/*     */     
/*     */     private TagCollector() {
/* 176 */       this.tags = new HashMap<>();
/*     */     }
/*     */     
/*     */     public void append(ResourceKey<? extends Registry<?>> registry, TagNetworkSerialization.NetworkPayload tagData) {
/* 179 */       this.tags.put(registry, tagData);
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<? super ResourceKey<? extends Registry<?>>, ? super TagNetworkSerialization.NetworkPayload> action) {
/* 183 */       this.tags.forEach(action);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\RegistryDataCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */