/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ 
/*    */ final class FileNode extends Node {
/*    */   private Path storagePath;
/*    */   
/*    */   boolean isCopy;
/*    */   
/*    */   FileNode(CopyOnWriteFSPath path, Path storagePath, boolean isCopy) {
/* 18 */     super(path);
/* 19 */     this.storagePath = storagePath;
/* 20 */     this.isCopy = isCopy;
/*    */   }
/*    */   
/*    */   public void ensureCopy() throws IOException {
/* 24 */     if (!this.isCopy) {
/* 25 */       Path tempFile = this.path.getFileSystem().createTemporaryFilePath();
/* 26 */       Files.copy(this.storagePath, tempFile, new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES });
/* 27 */       this.storagePath = tempFile;
/* 28 */       this.isCopy = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void deleteCopy() throws IOException {
/* 33 */     if (this.isCopy)
/* 34 */       Files.deleteIfExists(this.storagePath); 
/*    */   }
/*    */   
/*    */   public Path storagePath() {
/* 39 */     return this.storagePath;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\FileNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */