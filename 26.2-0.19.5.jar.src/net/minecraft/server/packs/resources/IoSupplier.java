/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface IoSupplier<T> {
/*    */   static IoSupplier<InputStream> create(Path path) {
/* 13 */     return () -> Files.newInputStream(path, new java.nio.file.OpenOption[0]);
/*    */   }
/*    */   
/*    */   static IoSupplier<InputStream> create(ZipFile zipFile, ZipEntry entry) {
/* 17 */     return () -> zipFile.getInputStream(entry);
/*    */   }
/*    */   
/*    */   T get() throws IOException;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\IoSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */