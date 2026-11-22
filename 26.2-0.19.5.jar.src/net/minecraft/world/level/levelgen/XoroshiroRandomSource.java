/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class XoroshiroRandomSource implements RandomSource {
/*     */   private static final float FLOAT_UNIT = 5.9604645E-8F;
/*     */   
/*     */   private static final double DOUBLE_UNIT = 1.1102230246251565E-16D;
/*     */   
/*     */   public static final Codec<XoroshiroRandomSource> CODEC;
/*     */   
/*     */   private Xoroshiro128PlusPlus randomNumberGenerator;
/*     */   
/*     */   static {
/*  13 */     CODEC = Xoroshiro128PlusPlus.CODEC.xmap(generator -> new XoroshiroRandomSource(generator), source -> source.randomNumberGenerator);
/*     */   }
/*     */   
/*  19 */   private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);
/*     */   
/*     */   public XoroshiroRandomSource(long seed) {
/*  22 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(RandomSupport.Seed128bit seed) {
/*  26 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seed);
/*     */   }
/*     */   
/*     */   public XoroshiroRandomSource(long seedLo, long seedHi) {
/*  30 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(seedLo, seedHi);
/*     */   }
/*     */   
/*     */   private XoroshiroRandomSource(Xoroshiro128PlusPlus randomNumberGenerator) {
/*  34 */     this.randomNumberGenerator = randomNumberGenerator;
/*     */   }
/*     */   
/*     */   public RandomSource fork() {
/*  39 */     return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */   
/*     */   public PositionalRandomFactory forkPositional() {
/*  44 */     return new XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
/*     */   }
/*     */   
/*     */   public void setSeed(long seed) {
/*  49 */     this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
/*  50 */     this.gaussianSource.reset();
/*     */   }
/*     */   
/*     */   public int nextInt() {
/*  55 */     return (int)this.randomNumberGenerator.nextLong();
/*     */   }
/*     */   
/*     */   public int nextInt(int bound) {
/*  60 */     if (bound <= 0)
/*  61 */       throw new IllegalArgumentException("Bound must be positive"); 
/*  67 */     long randomBits = Integer.toUnsignedLong(nextInt());
/*  70 */     long multipliedRandomBits = randomBits * bound;
/*  72 */     long fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*  75 */     if (fractionalPart < bound) {
/*  80 */       int unbiasedBucketsStartIndex = Integer.remainderUnsigned((bound ^ 0xFFFFFFFF) + 1, bound);
/*  81 */       while (fractionalPart < unbiasedBucketsStartIndex) {
/*  83 */         randomBits = Integer.toUnsignedLong(nextInt());
/*  84 */         multipliedRandomBits = randomBits * bound;
/*  85 */         fractionalPart = multipliedRandomBits & 0xFFFFFFFFL;
/*     */       } 
/*     */     } 
/*  89 */     long integerPart = multipliedRandomBits >> 32L;
/*  91 */     return (int)integerPart;
/*     */   }
/*     */   
/*     */   public long nextLong() {
/*  96 */     return this.randomNumberGenerator.nextLong();
/*     */   }
/*     */   
/*     */   public boolean nextBoolean() {
/* 101 */     return ((this.randomNumberGenerator.nextLong() & 0x1L) != 0L);
/*     */   }
/*     */   
/*     */   public float nextFloat() {
/* 106 */     return (float)nextBits(24) * 5.9604645E-8F;
/*     */   }
/*     */   
/*     */   public double nextDouble() {
/* 111 */     return nextBits(53) * 1.1102230246251565E-16D;
/*     */   }
/*     */   
/*     */   public double nextGaussian() {
/* 116 */     return this.gaussianSource.nextGaussian();
/*     */   }
/*     */   
/*     */   public void consumeCount(int rounds) {
/* 121 */     for (int i = 0; i < rounds; i++)
/* 122 */       this.randomNumberGenerator.nextLong(); 
/*     */   }
/*     */   
/*     */   private long nextBits(int bits) {
/* 127 */     return this.randomNumberGenerator.nextLong() >>> 64 - bits;
/*     */   }
/*     */   
/*     */   public static class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {
/*     */     private final long seedLo;
/*     */     
/*     */     private final long seedHi;
/*     */     
/*     */     public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
/* 135 */       this.seedLo = seedLo;
/* 136 */       this.seedHi = seedHi;
/*     */     }
/*     */     
/*     */     public RandomSource at(int x, int y, int z) {
/* 141 */       long positionalSeed = Mth.getSeed(x, y, z);
/* 142 */       long randomSeed = positionalSeed ^ this.seedLo;
/* 143 */       return new XoroshiroRandomSource(randomSeed, this.seedHi);
/*     */     }
/*     */     
/*     */     public RandomSource fromHashOf(String name) {
/* 148 */       RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
/* 149 */       return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
/*     */     }
/*     */     
/*     */     public RandomSource fromSeed(long seed) {
/* 154 */       return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     public void parityConfigString(StringBuilder sb) {
/* 160 */       sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */