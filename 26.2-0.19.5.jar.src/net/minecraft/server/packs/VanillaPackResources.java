/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceMetadata;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResources implements PackResources {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackLocationInfo location;
/*     */   
/*     */   private final ResourceMetadata builtInMetadata;
/*     */   
/*     */   private ResourceMetadata resourceMetadata;
/*     */   
/*     */   private final Set<String> namespaces;
/*     */   
/*     */   private final List<Path> rootPaths;
/*     */   
/*     */   private final Map<PackType, List<Path>> pathsForType;
/*     */   
/*     */   VanillaPackResources(PackLocationInfo location, ResourceMetadata metadata, Set<String> namespaces, List<Path> rootPaths, Map<PackType, List<Path>> pathsForType) {
/*  39 */     this.location = location;
/*  40 */     this.builtInMetadata = metadata;
/*  41 */     this.namespaces = namespaces;
/*  42 */     this.rootPaths = rootPaths;
/*  43 */     this.pathsForType = pathsForType;
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... path) {
/*  48 */     FileUtil.validatePath(path);
/*  50 */     List<String> pathList = List.of(path);
/*  51 */     for (Path rootPath : this.rootPaths) {
/*  52 */       Path pathInRoot = FileUtil.resolvePath(rootPath, pathList);
/*  53 */       if (Files.exists(pathInRoot, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(pathInRoot))
/*  54 */         return IoSupplier.create(pathInRoot); 
/*     */     } 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   public void listRawPaths(PackType type, Identifier resource, Consumer<Path> output) {
/*  61 */     FileUtil.decomposePath(resource.getPath())
/*  62 */       .ifSuccess(decomposedPath -> {
/*     */           String namespace = resource.getNamespace();
/*     */           for (Path typePath : this.pathsForType.get(type)) {
/*     */             Path namespacedPath = typePath.resolve(namespace);
/*     */             output.accept(FileUtil.resolvePath(namespacedPath, decomposedPath));
/*     */           } 
/*  71 */         }).ifError(error -> LOGGER.error("Invalid path {}: {}", resource, error.message()));
/*     */   }
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/*  78 */     FileUtil.decomposePath(directory)
/*  79 */       .ifSuccess(decomposedPath -> {
/*     */           List<Path> paths = this.pathsForType.get(type);
/*     */           int pathsSize = paths.size();
/*     */           if (pathsSize == 1) {
/*     */             getResources(output, namespace, paths.get(0), decomposedPath);
/*     */           } else if (pathsSize > 1) {
/*     */             Map<Identifier, IoSupplier<InputStream>> resources = new HashMap<>();
/*     */             for (int i = 0; i < pathsSize - 1; i++) {
/*     */               Objects.requireNonNull(resources);
/*     */               getResources(resources::putIfAbsent, namespace, paths.get(i), decomposedPath);
/*     */             } 
/*     */             Path lastPath = paths.get(pathsSize - 1);
/*     */             if (resources.isEmpty()) {
/*     */               getResources(output, namespace, lastPath, decomposedPath);
/*     */             } else {
/*     */               Objects.requireNonNull(resources);
/*     */               getResources(resources::putIfAbsent, namespace, lastPath, decomposedPath);
/*     */               resources.forEach(output);
/*     */             } 
/*     */           } 
/* 102 */         }).ifError(error -> LOGGER.error("Invalid path {}: {}", directory, error.message()));
/*     */   }
/*     */   
/*     */   private static void getResources(PackResources.ResourceOutput result, String namespace, Path root, List<String> directory) {
/* 108 */     Path namespaceDir = root.resolve(namespace);
/* 109 */     PathPackResources.listPath(namespace, namespaceDir, directory, result);
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/* 114 */     return (IoSupplier<InputStream>)FileUtil.decomposePath(location.getPath()).mapOrElse(decomposedPath -> {
/*     */           String namespace = location.getNamespace();
/*     */           for (Path typePath : this.pathsForType.get(type)) {
/*     */             Path resource = FileUtil.resolvePath(typePath.resolve(namespace), decomposedPath);
/*     */             if (Files.exists(resource, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(resource))
/*     */               return IoSupplier.create(resource); 
/*     */           } 
/*     */           return null;
/*     */         }error -> {
/*     */           LOGGER.error("Invalid path {}: {}", location, error.message());
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   public Set<String> getNamespaces(PackType type) {
/* 134 */     return this.namespaces;
/*     */   }
/*     */   
/*     */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) {
/*     */     try {
/* 141 */       if (this.resourceMetadata == null)
/* 142 */         this.resourceMetadata = AbstractPackResources.loadMetadata(this); 
/* 145 */       Optional<T> section = this.resourceMetadata.getSection(metadataSerializer);
/* 146 */       if (section.isPresent())
/* 147 */         return section.get(); 
/* 149 */     } catch (Exception e) {
/* 150 */       LOGGER.warn("Failed to parse vanilla pack metadata", e);
/*     */     } 
/* 154 */     return this.builtInMetadata.getSection(metadataSerializer).orElse(null);
/*     */   }
/*     */   
/*     */   public PackLocationInfo location() {
/* 159 */     return this.location;
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public ResourceProvider asProvider() {
/* 169 */     return location -> Optional.<IoSupplier<InputStream>>ofNullable(getResource(PackType.CLIENT_RESOURCES, location)).map(());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\VanillaPackResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */