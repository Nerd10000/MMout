/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.io.Files;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ class null extends SimpleFileVisitor<Path> {
/*     */   public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 653 */     if (path.endsWith("session.lock"))
/* 654 */       return FileVisitResult.CONTINUE; 
/* 656 */     String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/* 657 */     ZipEntry entry = new ZipEntry(entryPath);
/* 658 */     stream.putNextEntry(entry);
/* 659 */     Files.asByteSource(path.toFile()).copyTo(stream);
/* 660 */     stream.closeEntry();
/* 661 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelStorageAccess$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */