/*    */ package net.minecraft;
/*    */ 
/*    */ public class ReportedException extends RuntimeException {
/*    */   private final CrashReport report;
/*    */   
/*    */   public ReportedException(CrashReport report) {
/*  7 */     this.report = report;
/*    */   }
/*    */   
/*    */   public CrashReport getReport() {
/* 11 */     return this.report;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 16 */     return this.report.getException();
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 21 */     return this.report.getTitle();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\ReportedException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */