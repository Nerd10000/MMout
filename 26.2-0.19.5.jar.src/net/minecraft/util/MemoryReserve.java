/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class MemoryReserve {
/*    */   private static byte[] reserve;
/*    */   
/*    */   public static void allocate() {
/* 10 */     reserve = new byte[10485760];
/*    */   }
/*    */   
/*    */   public static void release() {
/* 14 */     if (reserve != null) {
/* 15 */       reserve = null;
/*    */       try {
/* 17 */         System.gc();
/* 18 */         System.gc();
/* 19 */         System.gc();
/* 20 */       } catch (Throwable throwable) {}
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\MemoryReserve.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */