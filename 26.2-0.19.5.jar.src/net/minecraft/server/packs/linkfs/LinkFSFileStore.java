/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileStore;
/*    */ import java.nio.file.attribute.BasicFileAttributeView;
/*    */ import java.nio.file.attribute.FileAttributeView;
/*    */ 
/*    */ class LinkFSFileStore extends FileStore {
/*    */   private final String name;
/*    */   
/*    */   public LinkFSFileStore(String name) {
/* 17 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String name() {
/* 22 */     return this.name;
/*    */   }
/*    */   
/*    */   public String type() {
/* 27 */     return "index";
/*    */   }
/*    */   
/*    */   public boolean isReadOnly() {
/* 32 */     return true;
/*    */   }
/*    */   
/*    */   public long getTotalSpace() {
/* 37 */     return 0L;
/*    */   }
/*    */   
/*    */   public long getUsableSpace() {
/* 42 */     return 0L;
/*    */   }
/*    */   
/*    */   public long getUnallocatedSpace() {
/* 47 */     return 0L;
/*    */   }
/*    */   
/*    */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
/* 52 */     return (type == BasicFileAttributeView.class);
/*    */   }
/*    */   
/*    */   public boolean supportsFileAttributeView(String name) {
/* 57 */     return "basic".equals(name);
/*    */   }
/*    */   
/*    */   public <V extends java.nio.file.attribute.FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
/* 62 */     return null;
/*    */   }
/*    */   
/*    */   public Object getAttribute(String attribute) throws IOException {
/* 67 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFSFileStore.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */