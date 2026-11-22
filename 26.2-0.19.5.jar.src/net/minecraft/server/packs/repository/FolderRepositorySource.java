/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.FilePackResources;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.PathPackResources;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FolderRepositorySource implements RepositorySource {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  33 */   private static final PackSelectionConfig DISCOVERED_PACK_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
/*     */   
/*     */   private final Path folder;
/*     */   
/*     */   private final PackType packType;
/*     */   
/*     */   private final PackSource packSource;
/*     */   
/*     */   private final DirectoryValidator validator;
/*     */   
/*     */   public FolderRepositorySource(Path folder, PackType packType, PackSource packSource, DirectoryValidator validator) {
/*  45 */     this.folder = folder;
/*  46 */     this.packType = packType;
/*  47 */     this.packSource = packSource;
/*  48 */     this.validator = validator;
/*     */   }
/*     */   
/*     */   private static String nameFromPath(Path content) {
/*  52 */     return content.getFileName().toString();
/*     */   }
/*     */   
/*     */   public void loadPacks(Consumer<Pack> result) {
/*     */     try {
/*  58 */       FileUtil.createDirectoriesSafe(this.folder);
/*  59 */       discoverPacks(this.folder, this.validator, (content, resources) -> {
/*     */             PackLocationInfo locationInfo = createDiscoveredFilePackInfo(content);
/*     */             Pack pack = Pack.readMetaAndCreate(locationInfo, resources, this.packType, DISCOVERED_PACK_SELECTION_CONFIG);
/*     */             if (pack != null)
/*     */               result.accept(pack); 
/*     */           });
/*  68 */     } catch (IOException e) {
/*  69 */       LOGGER.warn("Failed to list packs in {}", this.folder, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private PackLocationInfo createDiscoveredFilePackInfo(Path content) {
/*  74 */     String name = nameFromPath(content);
/*  75 */     return new PackLocationInfo("file/" + name, 
/*     */         
/*  77 */         (Component)Component.literal(name), this.packSource, 
/*     */         
/*  79 */         Optional.empty());
/*     */   }
/*     */   
/*     */   public static void discoverPacks(Path folder, DirectoryValidator validator, BiConsumer<Path, Pack.ResourcesSupplier> result) throws IOException {
/*  84 */     FolderPackDetector detector = new FolderPackDetector(validator);
/*  86 */     DirectoryStream<Path> contents = Files.newDirectoryStream(folder);
/*     */     try {
/*  87 */       for (Path content : contents) {
/*     */         try {
/*  89 */           List<ForbiddenSymlinkInfo> validationIssues = new ArrayList<>();
/*  90 */           Pack.ResourcesSupplier resources = detector.detectPackResources(content, validationIssues);
/*  91 */           if (!validationIssues.isEmpty()) {
/*  92 */             LOGGER.warn("Ignoring potential pack entry: {}", ContentValidationException.getMessage(content, validationIssues));
/*     */             continue;
/*     */           } 
/*  93 */           if (resources != null) {
/*  94 */             result.accept(content, resources);
/*     */             continue;
/*     */           } 
/*  96 */           LOGGER.info("Found non-pack entry '{}', ignoring", content);
/*  98 */         } catch (IOException e) {
/*  99 */           LOGGER.warn("Failed to read properties of '{}', ignoring", content, e);
/*     */         } 
/*     */       } 
/* 102 */       if (contents != null)
/* 102 */         contents.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (contents != null)
/*     */         try {
/*     */           contents.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class FolderPackDetector extends PackDetector<Pack.ResourcesSupplier> {
/*     */     protected FolderPackDetector(DirectoryValidator validator) {
/* 107 */       super(validator);
/*     */     }
/*     */     
/*     */     protected Pack.ResourcesSupplier createZipPack(Path content) {
/* 112 */       FileSystem fileSystem = content.getFileSystem();
/* 113 */       if (fileSystem == FileSystems.getDefault() || fileSystem instanceof net.minecraft.server.packs.linkfs.LinkFileSystem)
/* 114 */         return (Pack.ResourcesSupplier)new FilePackResources.FileResourcesSupplier(content); 
/* 116 */       FolderRepositorySource.LOGGER.info("Can't open pack archive at {}", content);
/* 117 */       return null;
/*     */     }
/*     */     
/*     */     protected Pack.ResourcesSupplier createDirectoryPack(Path content) {
/* 122 */       return (Pack.ResourcesSupplier)new PathPackResources.PathResourcesSupplier(content);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\FolderRepositorySource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */