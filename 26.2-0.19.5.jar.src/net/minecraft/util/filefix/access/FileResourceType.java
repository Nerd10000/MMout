/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ public class FileResourceType<T extends AutoCloseable> {
/*    */   private final AccessFactory<T> factory;
/*    */   
/*    */   public FileResourceType(AccessFactory<T> factory) {
/* 14 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public T create(Path path, int dataVersion) {
/* 18 */     return this.factory.create(path, dataVersion);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface AccessFactory<T> {
/*    */     T create(Path param1Path, int param1Int);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\FileResourceType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */