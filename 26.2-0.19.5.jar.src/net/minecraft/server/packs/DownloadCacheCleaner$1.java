/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ import java.util.List;
/*    */ 
/*    */ class null extends SimpleFileVisitor<Path> {
/*    */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
/* 93 */     if (attrs.isRegularFile() && !file.getParent().equals(cacheDir)) {
/* 96 */       FileTime fileTime = attrs.lastModifiedTime();
/* 97 */       unsortedFiles.add(new DownloadCacheCleaner.PathAndTime(file, fileTime));
/*    */     } 
/* 99 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadCacheCleaner$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */