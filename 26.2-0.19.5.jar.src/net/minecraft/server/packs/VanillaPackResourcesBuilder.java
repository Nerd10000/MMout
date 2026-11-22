/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.server.packs.resources.ResourceMetadata;
/*     */ import net.minecraft.util.FileSystemUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResourcesBuilder {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static Consumer<VanillaPackResourcesBuilder> developmentConfig = builder -> {
/*     */     
/*     */     };
/*     */   
/*     */   private static final Map<PackType, Path> ROOT_DIR_BY_TYPE;
/*     */   
/*     */   static {
/*  34 */     ROOT_DIR_BY_TYPE = (Map<PackType, Path>)Util.make(() -> {
/*     */           synchronized (VanillaPackResources.class) {
/*     */             ImmutableMap.Builder<PackType, Path> result = ImmutableMap.builder();
/*     */             for (PackType type : PackType.values()) {
/*     */               String probeName = "/" + type.getDirectory() + "/.mcassetsroot";
/*     */               URL probeUrl = VanillaPackResources.class.getResource(probeName);
/*     */               if (probeUrl == null) {
/*     */                 LOGGER.error("File {} does not exist in classpath", probeName);
/*     */               } else {
/*     */                 try {
/*     */                   URI probeUri = probeUrl.toURI();
/*     */                   String scheme = probeUri.getScheme();
/*     */                   if (!"jar".equals(scheme) && !"file".equals(scheme))
/*     */                     LOGGER.warn("Assets URL '{}' uses unexpected schema", probeUri); 
/*     */                   Path probePath = FileSystemUtil.safeGetPath(probeUri);
/*     */                   result.put(type, probePath.getParent());
/*  53 */                 } catch (Exception e) {
/*     */                   LOGGER.error("Couldn't resolve path to vanilla assets", e);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             return result.build();
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*  61 */   private final Set<Path> rootPaths = new LinkedHashSet<>();
/*     */   
/*  62 */   private final Map<PackType, Set<Path>> pathsForType = new EnumMap<>(PackType.class);
/*     */   
/*  64 */   private ResourceMetadata metadata = ResourceMetadata.EMPTY;
/*     */   
/*  65 */   private final Set<String> namespaces = new HashSet<>();
/*     */   
/*     */   private boolean validateDirPath(Path path) {
/*  68 */     if (!Files.exists(path, new java.nio.file.LinkOption[0]))
/*  69 */       return false; 
/*  71 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0]))
/*  72 */       throw new IllegalArgumentException("Path " + String.valueOf(path.toAbsolutePath()) + " is not directory"); 
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   private void pushRootPath(Path path) {
/*  78 */     if (validateDirPath(path))
/*  79 */       this.rootPaths.add(path); 
/*     */   }
/*     */   
/*     */   private void pushPathForType(PackType packType, Path path) {
/*  84 */     if (validateDirPath(path))
/*  85 */       ((Set<Path>)this.pathsForType.computeIfAbsent(packType, k -> new LinkedHashSet())).add(path); 
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushJarResources() {
/*  90 */     ROOT_DIR_BY_TYPE.forEach((packType, path) -> {
/*     */           pushRootPath(path.getParent());
/*     */           pushPathForType(packType, path);
/*     */         });
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushClasspathResources(PackType packType, Class<?> source) {
/* 100 */     Enumeration<URL> resources = null;
/*     */     try {
/* 102 */       resources = source.getClassLoader().getResources(packType.getDirectory() + "/");
/* 103 */     } catch (IOException iOException) {}
/* 106 */     while (resources != null && resources.hasMoreElements()) {
/* 107 */       URL url = resources.nextElement();
/*     */       try {
/* 109 */         URI uri = url.toURI();
/* 110 */         if ("file".equals(uri.getScheme())) {
/* 111 */           Path assetsPath = Paths.get(uri);
/* 112 */           pushRootPath(assetsPath.getParent());
/* 113 */           pushPathForType(packType, assetsPath);
/*     */         } 
/* 115 */       } catch (Exception e) {
/* 116 */         LOGGER.error("Failed to extract path from {}", url, e);
/*     */       } 
/*     */     } 
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder applyDevelopmentConfig() {
/* 123 */     developmentConfig.accept(this);
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushUniversalPath(Path path) {
/* 129 */     pushRootPath(path);
/* 130 */     for (PackType packType : PackType.values())
/* 131 */       pushPathForType(packType, path.resolve(packType.getDirectory())); 
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushAssetPath(PackType packType, Path path) {
/* 138 */     pushRootPath(path);
/* 139 */     pushPathForType(packType, path);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder setMetadata(ResourceMetadata metadata) {
/* 144 */     this.metadata = metadata;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder exposeNamespace(String... namespaces) {
/* 149 */     this.namespaces.addAll(Arrays.asList(namespaces));
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResources build(PackLocationInfo location) {
/* 154 */     return new VanillaPackResources(location, this.metadata, 
/*     */         
/* 157 */         Set.copyOf(this.namespaces), 
/* 158 */         copyAndReverse(this.rootPaths), 
/* 159 */         Util.makeEnumMap(PackType.class, packType -> copyAndReverse(this.pathsForType.getOrDefault(packType, Set.of()))));
/*     */   }
/*     */   
/*     */   private static List<Path> copyAndReverse(Collection<Path> input) {
/* 164 */     List<Path> paths = new ArrayList<>(input);
/* 165 */     Collections.reverse(paths);
/* 166 */     return List.copyOf(paths);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\VanillaPackResourcesBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */