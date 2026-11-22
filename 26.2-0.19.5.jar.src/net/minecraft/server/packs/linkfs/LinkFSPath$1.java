/*     */ package net.minecraft.server.packs.linkfs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.ReadOnlyFileSystemException;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ 
/*     */ class null implements BasicFileAttributeView {
/*     */   public String name() {
/* 360 */     return "basic";
/*     */   }
/*     */   
/*     */   public BasicFileAttributes readAttributes() throws IOException {
/* 365 */     return LinkFSPath.this.getBasicAttributes();
/*     */   }
/*     */   
/*     */   public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) {
/* 370 */     throw new ReadOnlyFileSystemException();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFSPath$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */