/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.util.filefix.virtualfilesystem.FileMove;
/*    */ 
/*    */ public final class AbortedFileFixException extends FileFixException {
/*    */   private final List<FileMove> notRevertedMoves;
/*    */   
/*    */   public AbortedFileFixException(Exception cause, List<FileMove> notRevertedMoves, FileSystemCapabilities fileSystemCapabilities) {
/* 14 */     super(cause, fileSystemCapabilities);
/* 15 */     this.notRevertedMoves = notRevertedMoves;
/*    */   }
/*    */   
/*    */   public AbortedFileFixException(Exception cause) {
/* 19 */     this(cause, List.of(), null);
/*    */   }
/*    */   
/*    */   public List<FileMove> notRevertedMoves() {
/* 23 */     return this.notRevertedMoves;
/*    */   }
/*    */   
/*    */   protected CrashReport createCrashReport() {
/* 28 */     CrashReport crashReport = super.createCrashReport();
/* 29 */     CrashReportCategory failedReverts = crashReport.addCategory("Moves that failed to revert");
/* 30 */     for (int i = 0; i < this.notRevertedMoves.size(); i++) {
/* 31 */       FileMove notRevertedMove = this.notRevertedMoves.get(i);
/* 32 */       failedReverts.setDetail(String.valueOf(i), String.valueOf(notRevertedMove.from()) + " -> " + String.valueOf(notRevertedMove.from()));
/*    */     } 
/* 34 */     return crashReport;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\AbortedFileFixException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */