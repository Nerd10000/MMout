/*     */ package net.minecraft.data;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ class null extends SimpleFileVisitor<Path> {
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
/* 215 */     if (HashCache.this.cachePaths.contains(file))
/* 216 */       return FileVisitResult.CONTINUE; 
/* 218 */     found.increment();
/* 219 */     if (allowedFiles.contains(file))
/* 220 */       return FileVisitResult.CONTINUE; 
/*     */     try {
/* 223 */       Files.delete(file);
/* 224 */     } catch (IOException e) {
/* 225 */       HashCache.LOGGER.warn("Failed to delete file {}", file, e);
/*     */     } 
/* 227 */     removed.increment();
/* 228 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\HashCache$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */