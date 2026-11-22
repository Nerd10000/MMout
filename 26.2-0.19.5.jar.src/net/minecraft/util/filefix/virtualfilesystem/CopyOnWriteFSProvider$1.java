/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.attribute.BasicFileAttributeView;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ import net.minecraft.util.DummyFileAttributes;
/*    */ 
/*    */ class null implements BasicFileAttributeView {
/*    */   public String name() {
/* 45 */     return "basic";
/*    */   }
/*    */   
/*    */   public BasicFileAttributes readAttributes() {
/* 50 */     return DummyFileAttributes.DIRECTORY;
/*    */   }
/*    */   
/*    */   public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFSProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */