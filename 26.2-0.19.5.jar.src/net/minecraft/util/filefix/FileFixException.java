/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ 
/*    */ public class FileFixException extends RuntimeException {
/*    */   protected final FileSystemCapabilities fileSystemCapabilities;
/*    */   
/*    */   public FileFixException(Exception cause, FileSystemCapabilities fileSystemCapabilities) {
/* 12 */     super(cause);
/* 13 */     this.fileSystemCapabilities = fileSystemCapabilities;
/*    */   }
/*    */   
/*    */   protected CrashReport createCrashReport() {
/* 17 */     CrashReport crashReport = CrashReport.forThrowable(this, "Upgrading world failed with errors");
/* 18 */     CrashReportCategory fsCapabilities = crashReport.addCategory("File system capabilities");
/* 19 */     fsCapabilities.setDetail("Hard Links", (this.fileSystemCapabilities == null) ? "null" : Boolean.valueOf(this.fileSystemCapabilities.hardLinks()));
/* 20 */     fsCapabilities.setDetail("Atomic Move", (this.fileSystemCapabilities == null) ? "null" : Boolean.valueOf(this.fileSystemCapabilities.atomicMove()));
/* 21 */     return crashReport;
/*    */   }
/*    */   
/*    */   public ReportedException makeReportedException() {
/* 25 */     return new ReportedException(createCrashReport());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */