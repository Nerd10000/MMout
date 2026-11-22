/*      */ package net.minecraft.util;
/*      */ 
/*      */ class null extends Thread {
/*      */   null(String name) {
/* 1042 */     super(name);
/*      */   }
/*      */   
/*      */   public void run() {
/*      */     try {
/*      */       while (true)
/* 1047 */         Thread.sleep(2147483647L); 
/* 1048 */     } catch (InterruptedException e) {
/* 1049 */       Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$9.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */