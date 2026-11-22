/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class LinearCongruentialGenerator {
/*    */   private static final long MULTIPLIER = 6364136223846793005L;
/*    */   
/*    */   private static final long INCREMENT = 1442695040888963407L;
/*    */   
/*    */   public static long next(long rval, long c) {
/*  8 */     rval *= rval * 6364136223846793005L + 1442695040888963407L;
/*  9 */     rval += c;
/* 10 */     return rval;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\LinearCongruentialGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */