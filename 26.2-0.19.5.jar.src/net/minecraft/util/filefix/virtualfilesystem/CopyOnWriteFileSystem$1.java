/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSCreationException;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSSymlinkException;
/*     */ 
/*     */ class null extends SimpleFileVisitor<Path> {
/*     */   public FileVisitResult visitFile(Path realPath, BasicFileAttributes attrs) throws IOException {
/*  72 */     checkAttributes(realPath, attrs);
/*  73 */     if (CopyOnWriteFileSystem.this.skippedPaths.matches(realPath))
/*  74 */       return FileVisitResult.CONTINUE; 
/*  76 */     CopyOnWriteFSPath cowPath = toCowPath(realPath);
/*  77 */     DirectoryNode parentNode = fileTree.directoryByPath(Objects.<CopyOnWriteFSPath>requireNonNull(cowPath.getParent()));
/*  78 */     parentNode.addChild(new FileNode(cowPath, realPath, false));
/*  79 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */   
/*     */   public FileVisitResult preVisitDirectory(Path realPath, BasicFileAttributes attrs) throws IOException {
/*  84 */     checkAttributes(realPath, attrs);
/*  85 */     if (CopyOnWriteFileSystem.this.skippedPaths.matches(realPath))
/*  86 */       return FileVisitResult.SKIP_SUBTREE; 
/*  87 */     if (realPath.equals(baseDirectory))
/*  88 */       return FileVisitResult.CONTINUE; 
/*  90 */     CopyOnWriteFSPath cowPath = toCowPath(realPath);
/*  91 */     DirectoryNode parentNode = fileTree.directoryByPath(Objects.<CopyOnWriteFSPath>requireNonNull(cowPath.getParent()));
/*  92 */     parentNode.addChild(new DirectoryNode(cowPath));
/*  93 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */   
/*     */   private static void checkAttributes(Path realPath, BasicFileAttributes attrs) throws CowFSCreationException {
/*  97 */     if (!attrs.isRegularFile() && !attrs.isDirectory())
/*  98 */       throw new CowFSSymlinkException("Cannot build copy-on-write file system when symlink is present: " + String.valueOf(realPath)); 
/* 100 */     if (!Files.isWritable(realPath))
/* 102 */       throw new CowFSCreationException("Cannot build copy-on-write file system, missing write access for file: " + String.valueOf(realPath)); 
/*     */   }
/*     */   
/*     */   private CopyOnWriteFSPath toCowPath(Path realPath) {
/* 107 */     return fileTree.path().resolve(baseDirectory.relativize(realPath).toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFileSystem$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */