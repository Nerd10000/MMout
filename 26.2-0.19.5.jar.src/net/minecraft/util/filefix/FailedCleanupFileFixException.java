/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ 
/*    */ public final class FailedCleanupFileFixException extends FileFixException {
/*    */   private final String newWorldFolderName;
/*    */   
/*    */   public FailedCleanupFileFixException(Exception cause, String newWorldFolderName, FileSystemCapabilities fileSystemCapabilities) {
/* 10 */     super(cause, fileSystemCapabilities);
/* 11 */     this.newWorldFolderName = newWorldFolderName;
/*    */   }
/*    */   
/*    */   protected CrashReport createCrashReport() {
/* 16 */     CrashReport crashReport = super.createCrashReport();
/* 17 */     CrashReportCategory worldUpgrade = crashReport.addCategory("World upgrade");
/* 18 */     worldUpgrade.setDetail("New Name", this.newWorldFolderName);
/* 19 */     return crashReport;
/*    */   }
/*    */   
/*    */   public String newWorldFolderName() {
/* 23 */     return this.newWorldFolderName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FailedCleanupFileFixException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */