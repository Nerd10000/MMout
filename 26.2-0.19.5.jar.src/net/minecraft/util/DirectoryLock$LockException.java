/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ public class LockException extends IOException {
/*    */   private LockException(Path path, String message) {
/* 89 */     super(String.valueOf(path.toAbsolutePath()) + ": " + String.valueOf(path.toAbsolutePath()));
/*    */   }
/*    */   
/*    */   public static LockException alreadyLocked(Path path) {
/* 93 */     return new LockException(path, "already locked (possibly by other Minecraft instance?)");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\DirectoryLock$LockException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */