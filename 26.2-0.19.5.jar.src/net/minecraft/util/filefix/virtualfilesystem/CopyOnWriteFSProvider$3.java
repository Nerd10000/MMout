/*     */ package net.minecraft.util.filefix.virtualfilesystem;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNoSuchFileException;
/*     */ 
/*     */ class null implements BasicFileAttributeView {
/*     */   null(CopyOnWriteFSProvider this$0) {}
/*     */   
/*     */   public String name() {
/* 283 */     return "basic";
/*     */   }
/*     */   
/*     */   public BasicFileAttributes readAttributes() throws IOException {
/* 288 */     throw new CowFSNoSuchFileException(cowPath.toString());
/*     */   }
/*     */   
/*     */   public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
/* 293 */     throw new CowFSNoSuchFileException(cowPath.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFSProvider$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */