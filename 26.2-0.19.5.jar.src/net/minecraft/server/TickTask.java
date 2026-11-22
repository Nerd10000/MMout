/*    */ package net.minecraft.server;
/*    */ 
/*    */ public class TickTask implements Runnable {
/*    */   private final int tick;
/*    */   
/*    */   private final Runnable runnable;
/*    */   
/*    */   public TickTask(int tick, Runnable runnable) {
/*  8 */     this.tick = tick;
/*  9 */     this.runnable = runnable;
/*    */   }
/*    */   
/*    */   public int getTick() {
/* 13 */     return this.tick;
/*    */   }
/*    */   
/*    */   public void run() {
/* 18 */     this.runnable.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\TickTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */