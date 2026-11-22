/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ public class GameTestEvent {
/*    */   public final Long expectedDelay;
/*    */   
/*    */   public final Long minimumDelay;
/*    */   
/*    */   public final Runnable assertion;
/*    */   
/*    */   private GameTestEvent(Long expectedDelay, Long minimumDelay, Runnable assertion) {
/* 11 */     this.expectedDelay = expectedDelay;
/* 12 */     this.minimumDelay = minimumDelay;
/* 13 */     this.assertion = assertion;
/*    */   }
/*    */   
/*    */   public static GameTestEvent create(Runnable runnable) {
/* 17 */     return new GameTestEvent(null, null, runnable);
/*    */   }
/*    */   
/*    */   public static GameTestEvent create(long expectedTick, Runnable runnable) {
/* 21 */     return new GameTestEvent(Long.valueOf(expectedTick), null, runnable);
/*    */   }
/*    */   
/*    */   public static GameTestEvent createWithMinimumDelay(long minimumDelay, Runnable runnable) {
/* 25 */     return new GameTestEvent(null, Long.valueOf(minimumDelay), runnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */