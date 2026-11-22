/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ class null implements GameTestListener {
/*    */   null(MultipleTestTracker this$0) {}
/*    */   
/*    */   public void testStructureLoaded(GameTestInfo testInfo) {}
/*    */   
/*    */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {}
/*    */   
/*    */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 48 */     listener.accept(testInfo);
/*    */   }
/*    */   
/*    */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\MultipleTestTracker$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */