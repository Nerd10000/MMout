/*    */ package net.minecraft.util.filefix.access;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FileAccess<T extends AutoCloseable> implements AutoCloseable {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final FileAccessProvider fileAccessProvider;
/*    */   
/*    */   private final FileResourceType<T> type;
/*    */   
/*    */   private final FileRelation fileRelation;
/*    */   
/*    */   private List<T> files;
/*    */   
/*    */   public FileAccess(FileAccessProvider fileAccessProvider, FileResourceType<T> type, FileRelation fileRelation) {
/* 18 */     this.fileAccessProvider = fileAccessProvider;
/* 19 */     this.type = type;
/* 20 */     this.fileRelation = fileRelation;
/*    */   }
/*    */   
/*    */   public List<T> get() {
/* 24 */     if (this.files == null) {
/* 25 */       Path baseDirectory = this.fileAccessProvider.baseDirectory().get();
/* 26 */       if (baseDirectory == null)
/* 27 */         throw new IllegalStateException("Cannot access world files"); 
/* 29 */       this.files = this.fileRelation.getPaths(baseDirectory).stream().map(path -> this.type.create(path, this.fileAccessProvider.dataVersion())).toList();
/*    */     } 
/* 31 */     return this.files;
/*    */   }
/*    */   
/*    */   public T getOnlyFile() {
/* 35 */     List<T> files = get();
/* 36 */     if (files.size() != 1)
/* 37 */       throw new IllegalStateException("Trying to get only file, but there are " + files.size() + " files"); 
/* 39 */     return files.getFirst();
/*    */   }
/*    */   
/*    */   public void close() {
/* 44 */     if (this.files != null) {
/* 45 */       for (AutoCloseable autoCloseable : this.files) {
/*    */         try {
/* 47 */           autoCloseable.close();
/* 48 */         } catch (Exception e) {
/* 49 */           LOGGER.error("Failed to close file: ", e);
/*    */         } 
/*    */       } 
/* 52 */       this.files = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\access\FileAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */