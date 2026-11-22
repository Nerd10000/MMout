/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ @Deprecated
/*    */ public class ThreadSafeLegacyRandomSource implements BitRandomSource {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   
/*    */   private static final long INCREMENT = 11L;
/*    */   
/* 17 */   private final AtomicLong seed = new AtomicLong();
/*    */   
/* 18 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*    */   
/*    */   public ThreadSafeLegacyRandomSource(long seed) {
/* 21 */     setSeed(seed);
/*    */   }
/*    */   
/*    */   public RandomSource fork() {
/* 26 */     return new ThreadSafeLegacyRandomSource(nextLong());
/*    */   }
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 31 */     return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */   
/*    */   public void setSeed(long seed) {
/* 36 */     this.seed.set((seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL);
/*    */   }
/*    */   
/*    */   public int next(int bits) {
/*    */     while (true) {
/* 44 */       long oldSeed = this.seed.get();
/* 45 */       long nextSeed = oldSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 46 */       if (this.seed.compareAndSet(oldSeed, nextSeed))
/* 47 */         return (int)(nextSeed >>> 48 - bits); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/* 52 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\ThreadSafeLegacyRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */