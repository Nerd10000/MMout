/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.FileUtil;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FileFixUtil {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static final String NAMESPACE_PATTERN = "([a-z0-9_.-]+)";
/*    */   
/*    */   public static void moveFile(Path baseDirectory, String from, String to) throws IOException {
/* 20 */     Path fromAbsolute = baseDirectory.resolve(from);
/* 21 */     if (!Files.exists(fromAbsolute, new java.nio.file.LinkOption[0]))
/*    */       return; 
/* 24 */     Path toAbsolute = baseDirectory.resolve(to);
/* 25 */     if (Files.exists(toAbsolute, new java.nio.file.LinkOption[0])) {
/* 26 */       LOGGER.warn("Target already exists, skipping move from {} to {}", from, to);
/*    */       return;
/*    */     } 
/* 30 */     FileUtil.createDirectoriesSafe(toAbsolute.getParent());
/* 31 */     Files.move(fromAbsolute, toAbsolute, new CopyOption[] { StandardCopyOption.COPY_ATTRIBUTES });
/*    */   }
/*    */   
/*    */   public static void deleteFileOrEmptyDirectory(Path baseDirectory, String file) throws IOException {
/* 35 */     Path toDelete = baseDirectory.resolve(file);
/* 37 */     if (!Files.exists(toDelete, new java.nio.file.LinkOption[0]))
/*    */       return; 
/* 41 */     if (Files.isDirectory(toDelete, new java.nio.file.LinkOption[0]))
/*    */       try {
/*    */         List<Path> files;
/* 45 */         Stream<Path> paths = Files.list(toDelete);
/*    */         try {
/* 46 */           files = paths.toList();
/* 48 */           if (files.size() == 1 && ((Path)files.getFirst()).getFileName().toString().equals(".DS_Store")) {
/* 49 */             LOGGER.debug("Attempting to delete DS_Store at '{}'", toDelete);
/* 50 */             if (!Files.deleteIfExists(files.getFirst()))
/* 51 */               LOGGER.warn("Failed to delete file '{}' at '{}'", files.getFirst(), toDelete); 
/*    */           } 
/* 54 */           if (paths != null)
/* 54 */             paths.close(); 
/*    */         } catch (Throwable throwable) {
/*    */           if (paths != null)
/*    */             try {
/*    */               paths.close();
/*    */             } catch (Throwable throwable1) {
/*    */               throwable.addSuppressed(throwable1);
/*    */             }  
/*    */           throw throwable;
/*    */         } 
/* 56 */         paths = Files.list(toDelete);
/*    */         try {
/* 57 */           files = paths.toList();
/* 58 */           if (paths != null)
/* 58 */             paths.close(); 
/*    */         } catch (Throwable throwable) {
/*    */           if (paths != null)
/*    */             try {
/*    */               paths.close();
/*    */             } catch (Throwable throwable1) {
/*    */               throwable.addSuppressed(throwable1);
/*    */             }  
/*    */           throw throwable;
/*    */         } 
/* 60 */         if (!files.isEmpty()) {
/* 61 */           LOGGER.warn("Failed to delete directory '{}', as it's not empty. Content: {}", toDelete, files);
/*    */           return;
/*    */         } 
/* 64 */       } catch (IOException e) {
/* 65 */         LOGGER.warn("Failed to delete directory '{}' because {}", toDelete, e.toString());
/*    */         return;
/*    */       }  
/* 69 */     Files.deleteIfExists(toDelete);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */