/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.stream.LongStream;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Xoroshiro128PlusPlus {
/*    */   private long seedLo;
/*    */   
/*    */   private long seedHi;
/*    */   
/*    */   public static final Codec<Xoroshiro128PlusPlus> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = Codec.LONG_STREAM.comapFlatMap(seed -> Util.fixedSize(seed, 2).map(()), r -> LongStream.of(new long[] { r.seedLo, r.seedHi }));
/*    */   }
/*    */   
/*    */   public Xoroshiro128PlusPlus(RandomSupport.Seed128bit seed) {
/* 20 */     this(seed.seedLo(), seed.seedHi());
/*    */   }
/*    */   
/*    */   public Xoroshiro128PlusPlus(long seedLo, long seedHi) {
/* 24 */     this.seedLo = seedLo;
/* 25 */     this.seedHi = seedHi;
/* 26 */     if ((this.seedLo | this.seedHi) == 0L) {
/* 27 */       this.seedLo = -7046029254386353131L;
/* 28 */       this.seedHi = 7640891576956012809L;
/*    */     } 
/*    */   }
/*    */   
/*    */   public long nextLong() {
/* 34 */     long s0 = this.seedLo;
/* 35 */     long s1 = this.seedHi;
/* 36 */     long result = Long.rotateLeft(s0 + s1, 17) + s0;
/* 38 */     s1 ^= s0;
/* 39 */     this.seedLo = Long.rotateLeft(s0, 49) ^ s1 ^ s1 << 21L;
/* 40 */     this.seedHi = Long.rotateLeft(s1, 28);
/* 42 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Xoroshiro128PlusPlus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */