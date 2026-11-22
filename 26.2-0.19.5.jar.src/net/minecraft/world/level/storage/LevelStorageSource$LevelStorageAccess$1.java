/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ 
/*     */ class null extends SimpleFileVisitor<Path> {
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 576 */     if (!file.equals(lockPath)) {
/* 577 */       LevelStorageSource.LOGGER.debug("Deleting {}", file);
/* 581 */       Files.deleteIfExists(file);
/*     */     } 
/* 583 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */   
/*     */   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 588 */     if (exc != null)
/* 589 */       throw exc; 
/* 592 */     if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/* 594 */       LevelStorageSource.LevelStorageAccess.this.lock.close();
/* 595 */       Files.deleteIfExists(lockPath);
/*     */     } 
/* 597 */     Files.deleteIfExists(dir);
/* 598 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelStorageAccess$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */