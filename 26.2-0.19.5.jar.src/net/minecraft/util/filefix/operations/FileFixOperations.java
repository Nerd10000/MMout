/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.filefix.access.FileRelation;
/*    */ 
/*    */ public class FileFixOperations {
/*    */   public static Move moveSimple(String file) {
/* 10 */     return new Move(file, file);
/*    */   }
/*    */   
/*    */   public static Move move(String from, String to) {
/* 14 */     return new Move(from, to);
/*    */   }
/*    */   
/*    */   public static RegexMove moveRegex(String filePattern, String replacePattern) {
/* 18 */     return new RegexMove(filePattern, replacePattern);
/*    */   }
/*    */   
/*    */   public static DeleteFileOrEmptyDirectory delete(String target) {
/* 22 */     return new DeleteFileOrEmptyDirectory(target);
/*    */   }
/*    */   
/*    */   public static ApplyInFolders applyInFolders(FileRelation applicableFolders, List<FileFixOperation> operations) {
/* 26 */     return new ApplyInFolders(applicableFolders, operations);
/*    */   }
/*    */   
/*    */   public static GroupMove groupMove(Map<String, String> data, List<Move> move) {
/* 30 */     return new GroupMove(data, move);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\FileFixOperations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */