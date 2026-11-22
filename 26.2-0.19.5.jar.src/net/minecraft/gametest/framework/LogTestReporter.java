/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LogTestReporter implements TestReporter {
/*  8 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public void onTestFailed(GameTestInfo testInfo) {
/* 12 */     String testPosition = testInfo.getTestBlockPos().toShortString();
/* 13 */     if (testInfo.isRequired()) {
/* 14 */       LOGGER.error("{} failed at {}! {}", new Object[] { testInfo.id(), testPosition, Util.describeError(testInfo.getError()) });
/*    */     } else {
/* 16 */       LOGGER.warn("(optional) {} failed at {}. {}", new Object[] { testInfo.id(), testPosition, Util.describeError(testInfo.getError()) });
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onTestSuccess(GameTestInfo testInfo) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\LogTestReporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */