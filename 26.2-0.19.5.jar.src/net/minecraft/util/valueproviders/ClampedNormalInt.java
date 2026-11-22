/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class ClampedNormalInt extends Record implements IntProvider {
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static final MapCodec<ClampedNormalInt> MAP_CODEC;
/*    */   
/*    */   public ClampedNormalInt(float mean, float deviation, int minInclusive, int maxInclusive) {
/* 10 */     this.mean = mean;
/* 10 */     this.deviation = deviation;
/* 10 */     this.minInclusive = minInclusive;
/* 10 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/ClampedNormalInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/ClampedNormalInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/ClampedNormalInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/ClampedNormalInt;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float mean() {
/* 10 */     return this.mean;
/*    */   }
/*    */   
/*    */   public float deviation() {
/* 10 */     return this.deviation;
/*    */   }
/*    */   
/*    */   public int minInclusive() {
/* 10 */     return this.minInclusive;
/*    */   }
/*    */   
/*    */   public int maxInclusive() {
/* 10 */     return this.maxInclusive;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mean").forGetter(ClampedNormalInt::mean), (App)Codec.FLOAT.fieldOf("deviation").forGetter(ClampedNormalInt::deviation), (App)Codec.INT.fieldOf("min_inclusive").forGetter(ClampedNormalInt::minInclusive), (App)Codec.INT.fieldOf("max_inclusive").forGetter(ClampedNormalInt::maxInclusive)).apply((Applicative)i, ClampedNormalInt::new)).validate(c -> (c.maxInclusive < c.minInclusive) ? DataResult.error(()) : DataResult.success(c));
/*    */   }
/*    */   
/*    */   public static ClampedNormalInt of(float mean, float deviation, int minInclusive, int maxInclusive) {
/* 24 */     return new ClampedNormalInt(mean, deviation, minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 29 */     return sample(random, this.mean, this.deviation, this.minInclusive, this.maxInclusive);
/*    */   }
/*    */   
/*    */   public static int sample(RandomSource random, float mean, float deviation, float minInclusive, float maxInclusive) {
/* 33 */     return (int)Mth.clamp(Mth.normal(random, mean, deviation), minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public MapCodec<ClampedNormalInt> codec() {
/* 38 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\ClampedNormalInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */