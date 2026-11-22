/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.function.LongFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WorldgenRandom extends LegacyRandomSource {
/*    */   private final RandomSource randomSource;
/*    */   
/*    */   private int count;
/*    */   
/*    */   public WorldgenRandom(RandomSource randomSource) {
/* 15 */     super(0L);
/* 16 */     this.randomSource = randomSource;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 20 */     return this.count;
/*    */   }
/*    */   
/*    */   public RandomSource fork() {
/* 25 */     return this.randomSource.fork();
/*    */   }
/*    */   
/*    */   public PositionalRandomFactory forkPositional() {
/* 30 */     return this.randomSource.forkPositional();
/*    */   }
/*    */   
/*    */   public int next(int bits) {
/* 35 */     this.count++;
/* 36 */     RandomSource randomSource = this.randomSource;
/* 36 */     if (randomSource instanceof LegacyRandomSource) {
/* 36 */       LegacyRandomSource legacyRandomSource = (LegacyRandomSource)randomSource;
/* 37 */       return legacyRandomSource.next(bits);
/*    */     } 
/* 39 */     return (int)(this.randomSource.nextLong() >>> 64 - bits);
/*    */   }
/*    */   
/*    */   public synchronized void setSeed(long seed) {
/* 45 */     if (this.randomSource == null)
/*    */       return; 
/* 48 */     this.randomSource.setSeed(seed);
/*    */   }
/*    */   
/*    */   public long setDecorationSeed(long seed, int chunkX, int chunkZ) {
/* 52 */     setSeed(seed);
/* 54 */     long xScale = nextLong() | 0x1L;
/* 55 */     long zScale = nextLong() | 0x1L;
/* 56 */     long result = chunkX * xScale + chunkZ * zScale ^ seed;
/* 57 */     setSeed(result);
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   public void setFeatureSeed(long seed, int index, int step) {
/* 62 */     long result = seed + index + (10000 * step);
/* 63 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public void setLargeFeatureSeed(long seed, int chunkX, int chunkZ) {
/* 67 */     setSeed(seed);
/* 68 */     long xScale = nextLong();
/* 69 */     long zScale = nextLong();
/* 70 */     long result = chunkX * xScale ^ chunkZ * zScale ^ seed;
/* 71 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public void setLargeFeatureWithSalt(long seed, int x, int z, int blend) {
/* 75 */     long result = x * 341873128712L + z * 132897987541L + seed + blend;
/* 76 */     setSeed(result);
/*    */   }
/*    */   
/*    */   public static RandomSource seedSlimeChunk(int x, int z, long seed, long salt) {
/* 80 */     return RandomSource.createThreadLocalInstance(seed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ salt);
/*    */   }
/*    */   
/*    */   public enum Algorithm {
/* 84 */     LEGACY((String)LegacyRandomSource::new),
/* 85 */     XOROSHIRO((String)XoroshiroRandomSource::new);
/*    */     
/*    */     private final LongFunction<RandomSource> constructor;
/*    */     
/*    */     Algorithm(LongFunction<RandomSource> constructor) {
/* 91 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     public RandomSource newInstance(long seed) {
/* 95 */       return this.constructor.apply(seed);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldgenRandom.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */