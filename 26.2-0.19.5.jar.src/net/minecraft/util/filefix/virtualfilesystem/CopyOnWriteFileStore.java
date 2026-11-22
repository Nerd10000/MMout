/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.attribute.BasicFileAttributeView;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ 
/*    */ public class CopyOnWriteFileStore extends FileStore {
/*    */   private final String name;
/*    */   
/*    */   private final CopyOnWriteFileSystem fs;
/*    */   
/*    */   public CopyOnWriteFileStore(String name, CopyOnWriteFileSystem fs) {
/* 17 */     this.name = name;
/* 18 */     this.fs = fs;
/*    */   }
/*    */   
/*    */   public String name() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public String type() {
/* 28 */     return "copy-on-write";
/*    */   }
/*    */   
/*    */   public boolean isReadOnly() {
/* 33 */     return false;
/*    */   }
/*    */   
/*    */   public long getTotalSpace() throws IOException {
/* 38 */     return Files.getFileStore(this.fs.tmpDirectory()).getTotalSpace();
/*    */   }
/*    */   
/*    */   public long getUsableSpace() throws IOException {
/* 43 */     return Files.getFileStore(this.fs.tmpDirectory()).getUsableSpace();
/*    */   }
/*    */   
/*    */   public long getUnallocatedSpace() throws IOException {
/* 48 */     return Files.getFileStore(this.fs.tmpDirectory()).getUnallocatedSpace();
/*    */   }
/*    */   
/*    */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/* 53 */     return (type == BasicFileAttributeView.class);
/*    */   }
/*    */   
/*    */   public boolean supportsFileAttributeView(String name) {
/* 58 */     return "basic".equals(name);
/*    */   }
/*    */   
/*    */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
/* 63 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Object getAttribute(String attribute) throws IOException {
/* 68 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFileStore.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */