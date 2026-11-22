/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.ZipFile;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ class SharedZipFileAccess implements AutoCloseable {
/*     */   private final File file;
/*     */   
/*     */   private ZipFile zipFile;
/*     */   
/*     */   private boolean failedToLoad;
/*     */   
/*     */   private SharedZipFileAccess(File file) {
/* 158 */     this.file = file;
/*     */   }
/*     */   
/*     */   private ZipFile getOrCreateZipFile() {
/* 162 */     if (this.failedToLoad)
/* 163 */       return null; 
/* 166 */     if (this.zipFile == null)
/*     */       try {
/* 168 */         this.zipFile = new ZipFile(this.file);
/* 169 */       } catch (IOException e) {
/* 170 */         FilePackResources.LOGGER.error("Failed to open pack {}", this.file, e);
/* 171 */         this.failedToLoad = true;
/* 172 */         return null;
/*     */       }  
/* 176 */     return this.zipFile;
/*     */   }
/*     */   
/*     */   public void close() {
/* 181 */     if (this.zipFile != null) {
/* 182 */       IOUtils.closeQuietly(this.zipFile);
/* 183 */       this.zipFile = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 190 */     close();
/* 191 */     super.finalize();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\FilePackResources$SharedZipFileAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */