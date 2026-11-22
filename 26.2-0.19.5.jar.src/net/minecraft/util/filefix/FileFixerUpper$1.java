/*     */ package net.minecraft.util.filefix;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ 
/*     */ class null extends SimpleFileVisitor<Path> {
/*     */   public FileVisitResult visitFile(Path realPath, BasicFileAttributes attrs) {
/*     */     try {
/* 147 */       Files.deleteIfExists(realPath);
/* 148 */     } catch (IOException iOException) {}
/* 150 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */   
/*     */   public FileVisitResult postVisitDirectory(Path realPath, IOException e) {
/*     */     try {
/* 156 */       Files.deleteIfExists(realPath);
/* 157 */     } catch (IOException iOException) {}
/* 159 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixerUpper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */