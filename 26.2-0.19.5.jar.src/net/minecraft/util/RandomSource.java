/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
/*    */ import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;
/*    */ 
/*    */ public interface RandomSource {
/*    */   @Deprecated
/*    */   public static final double GAUSSIAN_SPREAD_FACTOR = 2.297D;
/*    */   
/*    */   static RandomSource create() {
/* 12 */     return create(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   static RandomSource createThreadSafe() {
/* 18 */     return (RandomSource)new ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed());
/*    */   }
/*    */   
/*    */   static RandomSource create(long seed) {
/* 22 */     return (RandomSource)new LegacyRandomSource(seed);
/*    */   }
/*    */   
/*    */   static RandomSource createThreadLocalInstance() {
/* 29 */     return (RandomSource)new SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong());
/*    */   }
/*    */   
/*    */   static RandomSource createThreadLocalInstance(long seed) {
/* 33 */     return (RandomSource)new SingleThreadedRandomSource(seed);
/*    */   }
/*    */   
/*    */   RandomSource fork();
/*    */   
/*    */   PositionalRandomFactory forkPositional();
/*    */   
/*    */   void setSeed(long paramLong);
/*    */   
/*    */   int nextInt();
/*    */   
/*    */   int nextInt(int paramInt);
/*    */   
/*    */   default int nextIntBetweenInclusive(int min, int maxInclusive) {
/* 49 */     return nextInt(maxInclusive - min + 1) + min;
/*    */   }
/*    */   
/*    */   long nextLong();
/*    */   
/*    */   boolean nextBoolean();
/*    */   
/*    */   float nextFloat();
/*    */   
/*    */   double nextDouble();
/*    */   
/*    */   double nextGaussian();
/*    */   
/*    */   default double triangle(double mean, double spread) {
/* 70 */     return mean + spread * (nextDouble() - nextDouble());
/*    */   }
/*    */   
/*    */   default float triangle(float mean, float spread) {
/* 74 */     return mean + spread * (nextFloat() - nextFloat());
/*    */   }
/*    */   
/*    */   default void consumeCount(int rounds) {
/* 78 */     for (int i = 0; i < rounds; i++)
/* 79 */       nextInt(); 
/*    */   }
/*    */   
/*    */   default int nextInt(int origin, int bound) {
/* 84 */     if (origin >= bound)
/* 85 */       throw new IllegalArgumentException("bound - origin is non positive"); 
/* 87 */     return origin + nextInt(bound - origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\RandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */