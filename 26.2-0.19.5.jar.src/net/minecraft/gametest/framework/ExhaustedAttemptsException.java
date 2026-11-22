/*   */ package net.minecraft.gametest.framework;
/*   */ 
/*   */ public class ExhaustedAttemptsException extends Throwable {
/*   */   public ExhaustedAttemptsException(int attempts, int successes, GameTestInfo testInfo) {
/* 6 */     super("Not enough successes: " + successes + " out of " + attempts + " attempts. Required successes: " + testInfo
/*   */         
/* 8 */         .requiredSuccesses() + ". max attempts: " + testInfo.maxAttempts() + ".", testInfo.getError());
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\ExhaustedAttemptsException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */