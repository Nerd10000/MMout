/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.ThreadingDetector;
/*    */ 
/*    */ public class LegacyRandomSource implements BitRandomSource {
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
/*    */   public LegacyRandomSource(long seed) {
/* 21 */     setSeed(seed);
/*    */   }
/*    */   
/*    */   public RandomSource fork() {
/* 26 */     return new LegacyRandomSource(nextLong());
/*    */   }
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 31 */     return new LegacyPositionalRandomFactory(nextLong());
/*    */   }
/*    */   
/*    */   public void setSeed(long seed) {
/* 36 */     if (!this.seed.compareAndSet(this.seed.get(), (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL))
/* 37 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null); 
/* 39 */     this.gaussianSource.reset();
/*    */   }
/*    */   
/*    */   public int next(int bits) {
/* 44 */     long oldSeed = this.seed.get();
/* 45 */     long newSeed = oldSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 46 */     if (!this.seed.compareAndSet(oldSeed, newSeed))
/* 47 */       throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null); 
/* 50 */     return (int)(newSeed >> 48 - bits);
/*    */   }
/*    */   
/*    */   public double nextGaussian() {
/* 55 */     return this.gaussianSource.nextGaussian();
/*    */   }
/*    */   
/*    */   public static class LegacyPositionalRandomFactory implements PositionalRandomFactory {
/*    */     private final long seed;
/*    */     
/*    */     public LegacyPositionalRandomFactory(long seed) {
/* 62 */       this.seed = seed;
/*    */     }
/*    */     
/*    */     public RandomSource at(int x, int y, int z) {
/* 67 */       long positionalSeed = Mth.getSeed(x, y, z);
/* 68 */       long randomSeed = positionalSeed ^ this.seed;
/* 69 */       return new LegacyRandomSource(randomSeed);
/*    */     }
/*    */     
/*    */     public RandomSource fromHashOf(String name) {
/* 74 */       int positionalSeed = name.hashCode();
/* 75 */       return new LegacyRandomSource(positionalSeed ^ this.seed);
/*    */     }
/*    */     
/*    */     public RandomSource fromSeed(long seed) {
/* 80 */       return new LegacyRandomSource(seed);
/*    */     }
/*    */     
/*    */     @VisibleForTesting
/*    */     public void parityConfigString(StringBuilder sb) {
/* 86 */       sb.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */