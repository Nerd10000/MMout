/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.nio.file.attribute.FileTime;
/*    */ 
/*    */ public abstract class DummyFileAttributes implements BasicFileAttributes {
/*  9 */   public static final BasicFileAttributes DIRECTORY = new DummyFileAttributes() {
/*    */       public boolean isRegularFile() {
/* 12 */         return false;
/*    */       }
/*    */       
/*    */       public boolean isDirectory() {
/* 17 */         return true;
/*    */       }
/*    */     };
/*    */   
/* 21 */   public static final BasicFileAttributes FILE = new DummyFileAttributes() {
/*    */       public boolean isRegularFile() {
/* 24 */         return true;
/*    */       }
/*    */       
/*    */       public boolean isDirectory() {
/* 29 */         return false;
/*    */       }
/*    */     };
/*    */   
/* 33 */   private static final FileTime EPOCH = FileTime.fromMillis(0L);
/*    */   
/*    */   public FileTime lastModifiedTime() {
/* 37 */     return EPOCH;
/*    */   }
/*    */   
/*    */   public FileTime lastAccessTime() {
/* 42 */     return EPOCH;
/*    */   }
/*    */   
/*    */   public FileTime creationTime() {
/* 47 */     return EPOCH;
/*    */   }
/*    */   
/*    */   public boolean isSymbolicLink() {
/* 52 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isOther() {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public long size() {
/* 62 */     return 0L;
/*    */   }
/*    */   
/*    */   public Object fileKey() {
/* 67 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\DummyFileAttributes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */