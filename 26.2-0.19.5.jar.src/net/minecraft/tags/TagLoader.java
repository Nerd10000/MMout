/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.SequencedSet;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.DependencySorter;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TagLoader<T> {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ElementLookup<T> elementLookup;
/*     */   
/*     */   private final String directory;
/*     */   
/*     */   public TagLoader(ElementLookup<T> elementLookup, String directory) {
/*  45 */     this.elementLookup = elementLookup;
/*  46 */     this.directory = directory;
/*     */   }
/*     */   
/*     */   public Map<Identifier, List<EntryWithSource>> load(ResourceManager resourceManager) {
/*  50 */     Map<Identifier, List<EntryWithSource>> builders = new HashMap<>();
/*  52 */     FileToIdConverter lister = FileToIdConverter.json(this.directory);
/*  54 */     for (Map.Entry<Identifier, List<Resource>> entry : (Iterable<Map.Entry<Identifier, List<Resource>>>)lister.listMatchingResourceStacks(resourceManager).entrySet()) {
/*  55 */       Identifier location = entry.getKey();
/*  56 */       Identifier id = lister.fileToId(location);
/*  57 */       for (Resource resource : entry.getValue()) {
/*     */         try {
/*  58 */           Reader reader = resource.openAsReader();
/*     */           try {
/*  59 */             JsonElement element = StrictJsonParser.parse(reader);
/*  60 */             List<EntryWithSource> tagContents = builders.computeIfAbsent(id, key -> new ArrayList());
/*  61 */             TagFile parsedContents = (TagFile)TagFile.CODEC.parse(new Dynamic((DynamicOps)JsonOps.INSTANCE, element)).getOrThrow();
/*  62 */             if (parsedContents.replace())
/*  63 */               tagContents.clear(); 
/*  65 */             String sourceId = resource.sourcePackId();
/*  66 */             parsedContents.entries().forEach(e -> tagContents.add(new EntryWithSource(e, sourceId)));
/*  67 */             if (reader != null)
/*  67 */               reader.close(); 
/*     */           } catch (Throwable throwable) {
/*     */             if (reader != null)
/*     */               try {
/*     */                 reader.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               }  
/*     */             throw throwable;
/*     */           } 
/*  67 */         } catch (Exception e) {
/*  68 */           LOGGER.error("Couldn't read tag list {} from {} in data pack {}", new Object[] { id, location, resource.sourcePackId(), e });
/*     */         } 
/*     */       } 
/*     */     } 
/*  73 */     return builders;
/*     */   }
/*     */   
/*     */   public static final class EntryWithSource extends Record {
/*     */     private final TagEntry entry;
/*     */     
/*     */     private final String source;
/*     */     
/*     */     public EntryWithSource(TagEntry entry, String source) {
/*  76 */       this.entry = entry;
/*  76 */       this.source = source;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$EntryWithSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$EntryWithSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public TagEntry entry() {
/*  76 */       return this.entry;
/*     */     }
/*     */     
/*     */     public String source() {
/*  76 */       return this.source;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  79 */       return String.valueOf(this.entry) + " (from " + String.valueOf(this.entry) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SortingEntry extends Record implements DependencySorter.Entry<Identifier> {
/*     */     private final List<TagLoader.EntryWithSource> entries;
/*     */     
/*     */     private SortingEntry(List<TagLoader.EntryWithSource> entries) {
/*  83 */       this.entries = entries;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$SortingEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$SortingEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$SortingEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<TagLoader.EntryWithSource> entries() {
/*  83 */       return this.entries;
/*     */     }
/*     */     
/*     */     public void visitRequiredDependencies(Consumer<Identifier> output) {
/*  86 */       this.entries.forEach(e -> e.entry.visitRequiredDependencies(output));
/*     */     }
/*     */     
/*     */     public void visitOptionalDependencies(Consumer<Identifier> output) {
/*  91 */       this.entries.forEach(e -> e.entry.visitOptionalDependencies(output));
/*     */     }
/*     */   }
/*     */   
/*     */   private Either<List<EntryWithSource>, List<T>> tryBuildTag(TagEntry.Lookup<T> lookup, List<EntryWithSource> entries) {
/*  96 */     SequencedSet<T> values = new LinkedHashSet<>();
/*  97 */     List<EntryWithSource> missingElements = new ArrayList<>();
/*  98 */     for (EntryWithSource entry : entries) {
/*  99 */       Objects.requireNonNull(values);
/*  99 */       if (!entry.entry().build(lookup, values::add))
/* 100 */         missingElements.add(entry); 
/*     */     } 
/* 103 */     return missingElements.isEmpty() ? Either.right(List.copyOf(values)) : Either.left(missingElements);
/*     */   }
/*     */   
/*     */   public Map<Identifier, List<T>> build(Map<Identifier, List<EntryWithSource>> builders) {
/* 107 */     final Map<Identifier, List<T>> newTags = new HashMap<>();
/* 109 */     TagEntry.Lookup<T> lookup = new TagEntry.Lookup<T>() {
/*     */         public T element(Identifier key, boolean required) {
/* 112 */           return TagLoader.this.elementLookup.get(key, required).orElse(null);
/*     */         }
/*     */         
/*     */         public Collection<T> tag(Identifier key) {
/* 117 */           return (Collection<T>)newTags.get(key);
/*     */         }
/*     */       };
/* 121 */     DependencySorter<Identifier, SortingEntry> sorter = new DependencySorter();
/* 123 */     builders.forEach((id, entry) -> sorter.addEntry(id, new SortingEntry(entry)));
/* 125 */     sorter.orderByDependencies((id, contents) -> tryBuildTag(lookup, contents.entries).ifLeft(()).ifRight(()));
/* 130 */     return newTags;
/*     */   }
/*     */   
/*     */   public static <T> Map<TagKey<T>, List<Holder<T>>> loadTagsFromNetwork(TagNetworkSerialization.NetworkPayload tags, Registry<T> registry) {
/* 134 */     return (tags.resolve(registry)).tags;
/*     */   }
/*     */   
/*     */   public static List<Registry.PendingTags<?>> loadTagsForExistingRegistries(ResourceManager manager, RegistryAccess layer) {
/* 138 */     return (List<Registry.PendingTags<?>>)layer.registries()
/* 139 */       .map(entry -> loadPendingTags(manager, entry.value()))
/* 140 */       .flatMap(Optional::stream)
/* 141 */       .collect(Collectors.toUnmodifiableList());
/*     */   }
/*     */   
/*     */   public static <T> void loadTagsForRegistry(ResourceManager manager, WritableRegistry<T> registry) {
/* 145 */     loadTagsForRegistry(manager, registry.key(), ElementLookup.fromWritableRegistry(registry));
/*     */   }
/*     */   
/*     */   public static <T> Map<TagKey<T>, List<Holder<T>>> loadTagsForRegistry(ResourceManager manager, ResourceKey<? extends Registry<T>> registryKey, ElementLookup<Holder<T>> lookup) {
/* 149 */     TagLoader<Holder<T>> loader = new TagLoader<>(lookup, Registries.tagsDirPath(registryKey));
/* 150 */     return wrapTags(registryKey, loader.build(loader.load(manager)));
/*     */   }
/*     */   
/*     */   private static <T> Map<TagKey<T>, List<Holder<T>>> wrapTags(ResourceKey<? extends Registry<T>> registryKey, Map<Identifier, List<Holder<T>>> tags) {
/* 154 */     return (Map<TagKey<T>, List<Holder<T>>>)tags.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> TagKey.create(registryKey, (Identifier)e.getKey()), Map.Entry::getValue));
/*     */   }
/*     */   
/*     */   private static <T> Optional<Registry.PendingTags<T>> loadPendingTags(ResourceManager manager, Registry<T> registry) {
/* 161 */     ResourceKey<? extends Registry<T>> key = registry.key();
/* 162 */     TagLoader<Holder<T>> loader = new TagLoader<>((ElementLookup)ElementLookup.fromFrozenRegistry(registry), Registries.tagsDirPath(key));
/* 163 */     LoadResult<T> tags = new LoadResult<>(key, wrapTags(registry.key(), loader.build(loader.load(manager))));
/* 164 */     return tags.tags().isEmpty() ? Optional.<Registry.PendingTags<T>>empty() : Optional.<Registry.PendingTags<T>>of(registry.prepareTagReload(tags));
/*     */   }
/*     */   
/*     */   public static List<HolderLookup.RegistryLookup<?>> buildUpdatedLookups(RegistryAccess.Frozen registries, List<Registry.PendingTags<?>> tags) {
/* 168 */     List<HolderLookup.RegistryLookup<?>> result = new ArrayList<>();
/* 169 */     registries.registries().forEach(lookup -> {
/*     */           Registry.PendingTags<?> foundTags = findTagsForRegistry(tags, lookup.key());
/*     */           result.add((foundTags != null) ? foundTags.lookup() : lookup.value());
/*     */         });
/* 173 */     return result;
/*     */   }
/*     */   
/*     */   private static Registry.PendingTags<?> findTagsForRegistry(List<Registry.PendingTags<?>> tags, ResourceKey<? extends Registry<?>> registryKey) {
/* 178 */     for (Registry.PendingTags<?> tag : tags) {
/* 179 */       if (tag.key() == registryKey)
/* 180 */         return tag; 
/*     */     } 
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   public static final class LoadResult<T> extends Record {
/*     */     private final ResourceKey<? extends Registry<T>> key;
/*     */     
/*     */     private final Map<TagKey<T>, List<Holder<T>>> tags;
/*     */     
/*     */     public LoadResult(ResourceKey<? extends Registry<T>> key, Map<TagKey<T>, List<Holder<T>>> tags) {
/* 186 */       this.key = key;
/* 186 */       this.tags = tags;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$LoadResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$LoadResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$LoadResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<? extends Registry<T>> key() {
/* 186 */       return this.key;
/*     */     }
/*     */     
/*     */     public Map<TagKey<T>, List<Holder<T>>> tags() {
/* 186 */       return this.tags;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ElementLookup<T> {
/*     */     static <T> ElementLookup<? extends Holder<T>> fromFrozenRegistry(Registry<T> registry) {
/* 193 */       return (id, required) -> registry.get(id);
/*     */     }
/*     */     
/*     */     static <T> ElementLookup<Holder<T>> fromWritableRegistry(WritableRegistry<T> registry) {
/* 197 */       return fromGetters(registry.key(), registry.createRegistrationLookup(), (HolderGetter<T>)registry);
/*     */     }
/*     */     
/*     */     static <T> ElementLookup<Holder<T>> fromGetters(ResourceKey<? extends Registry<T>> registryKey, HolderGetter<T> writable, HolderGetter<T> immutable) {
/* 202 */       return (id, required) -> (required ? writable : immutable).get(ResourceKey.create(registryKey, id));
/*     */     }
/*     */     
/*     */     Optional<? extends T> get(Identifier param1Identifier, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */