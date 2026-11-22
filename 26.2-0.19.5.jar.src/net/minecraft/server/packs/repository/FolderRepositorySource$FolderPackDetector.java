/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.server.packs.FilePackResources;
/*     */ import net.minecraft.server.packs.PathPackResources;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ 
/*     */ class FolderPackDetector extends PackDetector<Pack.ResourcesSupplier> {
/*     */   protected FolderPackDetector(DirectoryValidator validator) {
/* 107 */     super(validator);
/*     */   }
/*     */   
/*     */   protected Pack.ResourcesSupplier createZipPack(Path content) {
/* 112 */     FileSystem fileSystem = content.getFileSystem();
/* 113 */     if (fileSystem == FileSystems.getDefault() || fileSystem instanceof net.minecraft.server.packs.linkfs.LinkFileSystem)
/* 114 */       return (Pack.ResourcesSupplier)new FilePackResources.FileResourcesSupplier(content); 
/* 116 */     FolderRepositorySource.LOGGER.info("Can't open pack archive at {}", content);
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   protected Pack.ResourcesSupplier createDirectoryPack(Path content) {
/* 122 */     return (Pack.ResourcesSupplier)new PathPackResources.PathResourcesSupplier(content);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\FolderRepositorySource$FolderPackDetector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */