/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MultiPackResourceManager implements CloseableResourceManager {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Map<String, FallbackResourceManager> namespacedManagers;
/*     */   
/*     */   private final List<PackResources> packs;
/*     */   
/*     */   public MultiPackResourceManager(PackType type, List<PackResources> packs) {
/*  26 */     this.packs = List.copyOf(packs);
/*  28 */     Map<String, FallbackResourceManager> namespacedManagers = new HashMap<>();
/*  30 */     List<String> namespaces = packs.stream().flatMap(p -> p.getNamespaces(type).stream()).distinct().toList();
/*  32 */     for (PackResources pack : packs) {
/*  33 */       ResourceFilterSection filterSection = getPackFilterSection(pack);
/*  34 */       Set<String> providedNamespaces = pack.getNamespaces(type);
/*  36 */       Predicate<Identifier> pathFilter = (filterSection != null) ? (location -> filterSection.isPathFiltered(location.getPath())) : null;
/*  38 */       for (String namespace : namespaces) {
/*  39 */         boolean packContainsNamespace = providedNamespaces.contains(namespace);
/*  40 */         boolean filterMatchesNamespace = (filterSection != null && filterSection.isNamespaceFiltered(namespace));
/*  41 */         if (packContainsNamespace || filterMatchesNamespace) {
/*  42 */           FallbackResourceManager fallbackResourceManager = namespacedManagers.get(namespace);
/*  43 */           if (fallbackResourceManager == null) {
/*  44 */             fallbackResourceManager = new FallbackResourceManager(type, namespace);
/*  45 */             namespacedManagers.put(namespace, fallbackResourceManager);
/*     */           } 
/*  48 */           if (packContainsNamespace && filterMatchesNamespace) {
/*  49 */             fallbackResourceManager.push(pack, pathFilter);
/*     */             continue;
/*     */           } 
/*  50 */           if (packContainsNamespace) {
/*  51 */             fallbackResourceManager.push(pack);
/*     */             continue;
/*     */           } 
/*  53 */           fallbackResourceManager.pushFilterOnly(pack.packId(), pathFilter);
/*     */         } 
/*     */       } 
/*     */     } 
/*  58 */     this.namespacedManagers = namespacedManagers;
/*     */   }
/*     */   
/*     */   private ResourceFilterSection getPackFilterSection(PackResources pack) {
/*     */     try {
/*  63 */       return (ResourceFilterSection)pack.getMetadataSection(ResourceFilterSection.TYPE);
/*  64 */     } catch (Exception e) {
/*  65 */       LOGGER.error("Failed to get filter section from pack {}", pack.packId());
/*  67 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<String> getNamespaces() {
/*  72 */     return this.namespacedManagers.keySet();
/*     */   }
/*     */   
/*     */   public Optional<Resource> getResource(Identifier location) {
/*  77 */     ResourceManager pack = this.namespacedManagers.get(location.getNamespace());
/*  79 */     if (pack != null)
/*  80 */       return pack.getResource(location); 
/*  83 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public List<Resource> getResourceStack(Identifier location) {
/*  88 */     ResourceManager pack = this.namespacedManagers.get(location.getNamespace());
/*  90 */     if (pack != null)
/*  91 */       return pack.getResourceStack(location); 
/*  93 */     return List.of();
/*     */   }
/*     */   
/*     */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) {
/*  99 */     checkTrailingDirectoryPath(directory);
/* 101 */     Map<Identifier, Resource> result = new TreeMap<>();
/* 104 */     for (FallbackResourceManager manager : this.namespacedManagers.values())
/* 105 */       result.putAll(manager.listResources(directory, filter)); 
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 113 */     checkTrailingDirectoryPath(directory);
/* 115 */     Map<Identifier, List<Resource>> result = new TreeMap<>();
/* 118 */     for (FallbackResourceManager manager : this.namespacedManagers.values())
/* 119 */       result.putAll(manager.listResourceStacks(directory, filter)); 
/* 122 */     return result;
/*     */   }
/*     */   
/*     */   private static void checkTrailingDirectoryPath(String directory) {
/* 127 */     if (directory.endsWith("/"))
/* 128 */       throw new IllegalArgumentException("Trailing slash in path " + directory); 
/*     */   }
/*     */   
/*     */   public Stream<PackResources> listPacks() {
/* 134 */     return this.packs.stream();
/*     */   }
/*     */   
/*     */   public void close() {
/* 139 */     this.packs.forEach(PackResources::close);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\MultiPackResourceManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */