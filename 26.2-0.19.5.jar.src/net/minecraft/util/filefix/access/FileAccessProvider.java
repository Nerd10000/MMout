/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class FileAccessProvider implements AutoCloseable {
/*  8 */   private final List<FileAccess<?>> accessedFiles = new ArrayList<>();
/*    */   
/*  9 */   private final ScopedValue<Path> baseDirectory = ScopedValue.newInstance();
/*    */   
/*    */   private final int dataVersion;
/*    */   
/*    */   private boolean frozen = false;
/*    */   
/*    */   public FileAccessProvider(int dataVersion) {
/* 14 */     this.dataVersion = dataVersion;
/*    */   }
/*    */   
/*    */   public <T extends AutoCloseable> FileAccess<T> getFileAccess(FileResourceType<T> type, FileRelation fileRelation) {
/* 18 */     if (this.frozen)
/* 19 */       throw new IllegalStateException("Cannot request new file access here."); 
/* 21 */     FileAccess<T> fileAccess = new FileAccess<>(this, type, fileRelation);
/* 22 */     this.accessedFiles.add(fileAccess);
/* 23 */     return fileAccess;
/*    */   }
/*    */   
/*    */   public void freeze() {
/* 27 */     this.frozen = true;
/*    */   }
/*    */   
/*    */   public ScopedValue<Path> baseDirectory() {
/* 31 */     return this.baseDirectory;
/*    */   }
/*    */   
/*    */   public int dataVersion() {
/* 35 */     return this.dataVersion;
/*    */   }
/*    */   
/*    */   public void close() {
/* 40 */     for (FileAccess<?> accessedFile : this.accessedFiles)
/* 41 */       accessedFile.close(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\FileAccessProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */