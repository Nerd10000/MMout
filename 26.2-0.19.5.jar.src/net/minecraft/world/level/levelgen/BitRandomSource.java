/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface BitRandomSource extends RandomSource {
/*    */   public static final float FLOAT_MULTIPLIER = 5.9604645E-8F;
/*    */   
/*    */   public static final double DOUBLE_MULTIPLIER = 1.1102230246251565E-16D;
/*    */   
/*    */   int next(int paramInt);
/*    */   
/*    */   default int nextInt() {
/* 13 */     return next(32);
/*    */   }
/*    */   
/*    */   default int nextInt(int bound) {
/* 19 */     if (bound <= 0)
/* 20 */       throw new IllegalArgumentException("Bound must be positive"); 
/* 23 */     if ((bound & bound - 1) == 0)
/* 25 */       return (int)(bound * next(31) >> 31L); 
/*    */     while (true) {
/* 31 */       int sample = next(31);
/* 32 */       int modulo = sample % bound;
/* 33 */       if (sample - modulo + bound - 1 >= 0)
/* 34 */         return modulo; 
/*    */     } 
/*    */   }
/*    */   
/*    */   default long nextLong() {
/* 41 */     int upper = next(32);
/* 42 */     int lower = next(32);
/* 43 */     long shifted = upper << 32L;
/* 44 */     return shifted + lower;
/*    */   }
/*    */   
/*    */   default boolean nextBoolean() {
/* 49 */     return (next(1) != 0);
/*    */   }
/*    */   
/*    */   default float nextFloat() {
/* 54 */     return next(24) * 5.9604645E-8F;
/*    */   }
/*    */   
/*    */   default double nextDouble() {
/* 59 */     int upper = next(26);
/* 60 */     int lower = next(27);
/* 61 */     long combined = (upper << 27L) + lower;
/* 62 */     return combined * 1.1102230246251565E-16D;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\BitRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */