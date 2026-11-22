/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ 
/*    */ class RecoveryInfo {
/*    */   private final Throwable error;
/*    */   
/*    */   private RecoveryInfo(Throwable error) {
/* 59 */     this.error = error;
/*    */   }
/*    */   
/*    */   public void fillCrashInfo(CrashReportCategory category) {
/* 63 */     category.setDetail("Recovery", "Yes");
/* 65 */     category.setDetail("Recovery reason", () -> {
/*    */           StringWriter writer = new StringWriter();
/*    */           this.error.printStackTrace(new PrintWriter(writer));
/*    */           return writer.toString();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\ResourceLoadStateTracker$RecoveryInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */