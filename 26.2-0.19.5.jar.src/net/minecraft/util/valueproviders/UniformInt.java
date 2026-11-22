/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class UniformInt extends Record implements IntProvider {
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static final MapCodec<UniformInt> MAP_CODEC;
/*    */   
/*    */   public UniformInt(int minInclusive, int maxInclusive) {
/* 10 */     this.minInclusive = minInclusive;
/* 10 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/UniformInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/UniformInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/UniformInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/UniformInt;
/*    */     //   0	8	1	o	Ljava/lang/Object;
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
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("min_inclusive").forGetter(UniformInt::minInclusive), (App)Codec.INT.fieldOf("max_inclusive").forGetter(UniformInt::maxInclusive)).apply((Applicative)i, UniformInt::new)).validate(u -> (u.maxInclusive < u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */   
/*    */   public static UniformInt of(int minInclusive, int maxInclusive) {
/* 22 */     return new UniformInt(minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 27 */     return Mth.randomBetweenInclusive(random, this.minInclusive, this.maxInclusive);
/*    */   }
/*    */   
/*    */   public MapCodec<UniformInt> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 37 */     return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\UniformInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */