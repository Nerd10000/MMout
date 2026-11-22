/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ public class GlobalTestReporter {
/*  4 */   private static TestReporter DELEGATE = new LogTestReporter();
/*    */   
/*    */   public static void replaceWith(TestReporter testReporter) {
/*  7 */     DELEGATE = testReporter;
/*    */   }
/*    */   
/*    */   public static void onTestFailed(GameTestInfo testInfo) {
/* 11 */     DELEGATE.onTestFailed(testInfo);
/*    */   }
/*    */   
/*    */   public static void onTestSuccess(GameTestInfo testInfo) {
/* 15 */     DELEGATE.onTestSuccess(testInfo);
/*    */   }
/*    */   
/*    */   public static void finish() {
/* 19 */     DELEGATE.finish();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GlobalTestReporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */