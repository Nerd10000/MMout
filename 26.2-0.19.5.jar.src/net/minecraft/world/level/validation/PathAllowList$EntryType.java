/*    */ package net.minecraft.world.level.validation;
/*    */ 
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface EntryType {
/* 23 */   public static final EntryType FILESYSTEM = FileSystem::getPathMatcher;
/*    */   
/*    */   public static final EntryType PREFIX = (fileSystem, pattern) -> ();
/*    */   
/*    */   PathMatcher compile(FileSystem paramFileSystem, String paramString);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\validation\PathAllowList$EntryType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */