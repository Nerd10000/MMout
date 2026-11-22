/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SingleThreadedRandomSource implements BitRandomSource {
/*    */   private static final int MODULUS_BITS = 48;
/*    */   
/*    */   private static final long MODULUS_MASK = 281474976710655L;
/*    */   
/*    */   private static final long MULTIPLIER = 25214903917L;
/*    */   
/*    */   private static final long INCREMENT = 11L;
/*    */   
/*    */   private long seed;
/*    */   
/*    */   private MarsagliaPolarGaussian gaussianSource;
/*    */   
/*    */   public SingleThreadedRandomSource(long seed) {
/* 17 */     setSeed(seed);
/*    */   }
/*    */   
/*    */   public RandomSource fork() {
/* 22 */     return new SingleThreadedRandomSource(nextLong());
/*    */   }
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 27 */     return new LegacyRandomSource.LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */   
/*    */   public void setSeed(long seed) {
/* 32 */     this.seed = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
/* 33 */     if (this.gaussianSource != null)
/* 34 */       this.gaussianSource.reset(); 
/*    */   }
/*    */   
/*    */   public int next(int bits) {
/* 40 */     long newSeed = this.seed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 41 */     this.seed = newSeed;
/* 42 */     return (int)(newSeed >> 48 - bits);
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/* 47 */     if (this.gaussianSource == null)
/* 48 */       this.gaussianSource = new MarsagliaPolarGaussian(this); 
/* 50 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SingleThreadedRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */