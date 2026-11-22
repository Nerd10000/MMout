/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.common.primitives.Longs;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ public final class RandomSupport {
/*    */   public static final long GOLDEN_RATIO_64 = -7046029254386353131L;
/*    */   
/*    */   public static final long SILVER_RATIO_64 = 7640891576956012809L;
/*    */   
/* 16 */   private static final HashFunction MD5_128 = Hashing.md5();
/*    */   
/* 17 */   private static final AtomicLong SEED_UNIQUIFIER = new AtomicLong(8682522807148012L);
/*    */   
/*    */   @VisibleForTesting
/*    */   public static long mixStafford13(long z) {
/* 21 */     z = (z ^ z >>> 30L) * -4658895280553007687L;
/* 22 */     z = (z ^ z >>> 27L) * -7723592293110705685L;
/* 23 */     return z ^ z >>> 31L;
/*    */   }
/*    */   
/*    */   public static Seed128bit upgradeSeedTo128bitUnmixed(long legacySeed) {
/* 27 */     long lowBits = legacySeed ^ 0x6A09E667F3BCC909L;
/* 28 */     long highBits = lowBits + -7046029254386353131L;
/* 29 */     return new Seed128bit(lowBits, highBits);
/*    */   }
/*    */   
/*    */   public static Seed128bit upgradeSeedTo128bit(long legacySeed) {
/* 33 */     return upgradeSeedTo128bitUnmixed(legacySeed).mixed();
/*    */   }
/*    */   
/*    */   public static Seed128bit seedFromHashOf(String input) {
/* 37 */     byte[] hashCode = MD5_128.hashString(input, StandardCharsets.UTF_8).asBytes();
/* 39 */     long hashLo = Longs.fromBytes(hashCode[0], hashCode[1], hashCode[2], hashCode[3], hashCode[4], hashCode[5], hashCode[6], hashCode[7]);
/* 40 */     long hashHi = Longs.fromBytes(hashCode[8], hashCode[9], hashCode[10], hashCode[11], hashCode[12], hashCode[13], hashCode[14], hashCode[15]);
/* 41 */     return new Seed128bit(hashLo, hashHi);
/*    */   }
/*    */   
/*    */   public static long generateUniqueSeed() {
/* 48 */     return SEED_UNIQUIFIER.updateAndGet(current -> current * 1181783497276652981L) ^ System.nanoTime();
/*    */   }
/*    */   
/*    */   public static final class Seed128bit extends Record {
/*    */     private final long seedLo;
/*    */     
/*    */     private final long seedHi;
/*    */     
/*    */     public Seed128bit(long seedLo, long seedHi) {
/* 51 */       this.seedLo = seedLo;
/* 51 */       this.seedHi = seedHi;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/RandomSupport$Seed128bit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public long seedLo() {
/* 51 */       return this.seedLo;
/*    */     }
/*    */     
/*    */     public long seedHi() {
/* 51 */       return this.seedHi;
/*    */     }
/*    */     
/*    */     public Seed128bit xor(long lo, long hi) {
/* 53 */       return new Seed128bit(this.seedLo ^ lo, this.seedHi ^ hi);
/*    */     }
/*    */     
/*    */     public Seed128bit xor(Seed128bit other) {
/* 57 */       return xor(other.seedLo, other.seedHi);
/*    */     }
/*    */     
/*    */     public Seed128bit mixed() {
/* 61 */       return new Seed128bit(RandomSupport.mixStafford13(this.seedLo), RandomSupport.mixStafford13(this.seedHi));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\RandomSupport.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */