/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PathPackResources extends AbstractPackResources {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  33 */   private static final Joiner PATH_JOINER = Joiner.on("/");
/*     */   
/*     */   private final Path root;
/*     */   
/*     */   public PathPackResources(PackLocationInfo location, Path root) {
/*  38 */     super(location);
/*  39 */     this.root = root;
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... path) {
/*  44 */     FileUtil.validatePath(path);
/*  46 */     Path pathInRoot = FileUtil.resolvePath(this.root, List.of(path));
/*  47 */     if (Files.exists(pathInRoot, new java.nio.file.LinkOption[0]))
/*  48 */       return IoSupplier.create(pathInRoot); 
/*  50 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean validatePath(Path path) {
/*  57 */     if (!SharedConstants.DEBUG_VALIDATE_RESOURCE_PATH_CASE)
/*  58 */       return true; 
/*  60 */     if (path.getFileSystem() != FileSystems.getDefault())
/*  61 */       return true; 
/*     */     try {
/*  65 */       return path.toRealPath(new java.nio.file.LinkOption[0]).endsWith(path);
/*  66 */     } catch (IOException e) {
/*  67 */       LOGGER.warn("Failed to resolve real path for {}", path, e);
/*  68 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path topPackDir(PackType type) {
/*  73 */     return this.root.resolve(type.getDirectory());
/*     */   }
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/*  78 */     Path topDir = topPackDir(type);
/*  79 */     return getResource(topDir, location);
/*     */   }
/*     */   
/*     */   public static IoSupplier<InputStream> getResource(Path topDir, Identifier location) {
/*  83 */     Path namespaceDir = topDir.resolve(location.getNamespace());
/*  84 */     return (IoSupplier<InputStream>)FileUtil.decomposePath(location.getPath()).mapOrElse(decomposedPath -> {
/*     */           Path resolvedPath = FileUtil.resolvePath(namespaceDir, decomposedPath);
/*     */           return returnFileIfExists(resolvedPath);
/*     */         }error -> {
/*     */           LOGGER.error("Invalid path {}: {}", location, error.message());
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private static IoSupplier<InputStream> returnFileIfExists(Path resolvedPath) {
/*  97 */     if (Files.exists(resolvedPath, new java.nio.file.LinkOption[0]) && validatePath(resolvedPath))
/*  98 */       return IoSupplier.create(resolvedPath); 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/* 105 */     Path topDir = topPackDir(type);
/* 106 */     listResources(topDir, namespace, directory, output);
/*     */   }
/*     */   
/*     */   public static void listResources(Path topPath, String namespace, String directory, PackResources.ResourceOutput output) {
/* 110 */     FileUtil.decomposePath(directory)
/* 111 */       .ifSuccess(decomposedPath -> {
/*     */           Path namespaceDir = topPath.resolve(namespace);
/*     */           listPath(namespace, namespaceDir, decomposedPath, output);
/* 115 */         }).ifError(error -> LOGGER.error("Invalid path {}: {}", directory, error.message()));
/*     */   }
/*     */   
/*     */   public static void listPath(String namespace, Path topDir, List<String> decomposedPrefixPath, PackResources.ResourceOutput output) {
/* 121 */     Path targetPath = FileUtil.resolvePath(topDir, decomposedPrefixPath);
/*     */     try {
/* 122 */       Stream<Path> files = Files.find(targetPath, 2147483647, PathPackResources::isRegularFile, new java.nio.file.FileVisitOption[0]);
/*     */       try {
/* 123 */         files.forEach(file -> {
/*     */               String resourcePath = PATH_JOINER.join(topDir.relativize(file));
/*     */               Identifier identifier = Identifier.tryBuild(namespace, resourcePath);
/*     */               if (identifier == null) {
/*     */                 Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", new Object[] { namespace, resourcePath }));
/*     */               } else {
/*     */                 output.accept(identifier, IoSupplier.create(file));
/*     */               } 
/*     */             });
/* 132 */         if (files != null)
/* 132 */           files.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (files != null)
/*     */           try {
/*     */             files.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 132 */     } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException) {
/*     */     
/* 134 */     } catch (IOException e) {
/* 135 */       LOGGER.error("Failed to list path {}", targetPath, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isRegularFile(Path file, BasicFileAttributes attributes) {
/* 140 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/* 141 */       return (attributes.isRegularFile() && !StringUtils.equalsIgnoreCase(file.getFileName().toString(), ".ds_store")); 
/* 143 */     return attributes.isRegularFile();
/*     */   }
/*     */   
/*     */   public Set<String> getNamespaces(PackType type) {
/* 148 */     Path assetRoot = topPackDir(type);
/* 149 */     return getNamespaces(assetRoot);
/*     */   }
/*     */   
/*     */   public static Set<String> getNamespaces(Path rootDir) {
/* 153 */     Set<String> namespaces = new HashSet<>();
/*     */     try {
/* 155 */       DirectoryStream<Path> directDirs = Files.newDirectoryStream(rootDir);
/*     */       try {
/* 156 */         for (Path directDir : directDirs) {
/* 157 */           if (!Files.isDirectory(directDir, new java.nio.file.LinkOption[0])) {
/* 158 */             LOGGER.warn("Non-directory entry {} found in namespace directory, rejecting", directDir);
/*     */             continue;
/*     */           } 
/* 161 */           String namespace = directDir.getFileName().toString();
/* 163 */           if (Identifier.isValidNamespace(namespace)) {
/* 164 */             namespaces.add(namespace);
/*     */             continue;
/*     */           } 
/* 166 */           LOGGER.warn("Non {} character in namespace {} in pack directory {}, ignoring", new Object[] { "[a-z0-9_.-]", namespace, rootDir });
/*     */         } 
/* 169 */         if (directDirs != null)
/* 169 */           directDirs.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (directDirs != null)
/*     */           try {
/*     */             directDirs.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 169 */     } catch (NoSuchFileException|java.nio.file.NotDirectoryException noSuchFileException) {
/*     */     
/* 171 */     } catch (IOException e) {
/* 172 */       LOGGER.error("Failed to list path {}", rootDir, e);
/*     */     } 
/* 174 */     return namespaces;
/*     */   }
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public static class PathResourcesSupplier implements Pack.ResourcesSupplier {
/*     */     private final Path content;
/*     */     
/*     */     public PathResourcesSupplier(Path content) {
/* 185 */       this.content = content;
/*     */     }
/*     */     
/*     */     public PackResources openPrimary(PackLocationInfo location) {
/* 190 */       return new PathPackResources(location, this.content);
/*     */     }
/*     */     
/*     */     public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
/* 195 */       PackResources primary = openPrimary(location);
/* 197 */       List<String> overlays = metadata.overlays();
/* 198 */       if (overlays.isEmpty())
/* 199 */         return primary; 
/* 202 */       List<PackResources> overlayResources = new ArrayList<>(overlays.size());
/* 203 */       for (String overlay : overlays) {
/* 204 */         Path overlayRoot = this.content.resolve(overlay);
/* 205 */         overlayResources.add(new PathPackResources(location, overlayRoot));
/*     */       } 
/* 208 */       return new CompositePackResources(primary, overlayResources);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PathPackResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */