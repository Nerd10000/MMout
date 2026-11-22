/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class TrapezoidInt extends Record implements IntProvider {
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   private final int plateau;
/*    */   
/*    */   public static final MapCodec<TrapezoidInt> MAP_CODEC;
/*    */   
/*    */   public TrapezoidInt(int minInclusive, int maxInclusive, int plateau) {
/* 10 */     this.minInclusive = minInclusive;
/* 10 */     this.maxInclusive = maxInclusive;
/* 10 */     this.plateau = plateau;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/TrapezoidInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/TrapezoidInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/TrapezoidInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/TrapezoidInt;
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
/*    */   public int plateau() {
/* 10 */     return this.plateau;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("min").forGetter(TrapezoidInt::minInclusive), (App)Codec.INT.fieldOf("max").forGetter(TrapezoidInt::maxInclusive), (App)Codec.INT.fieldOf("plateau").forGetter(TrapezoidInt::plateau)).apply((Applicative)i, TrapezoidInt::new)).validate(c -> (c.maxInclusive < c.minInclusive) ? DataResult.error(()) : ((c.plateau > c.maxInclusive - c.minInclusive) ? DataResult.error(()) : DataResult.success(c)));
/*    */   }
/*    */   
/*    */   public static TrapezoidInt of(int min, int max, int plateau) {
/* 26 */     return new TrapezoidInt(min, max, plateau);
/*    */   }
/*    */   
/*    */   public static IntProvider triangle(int range) {
/* 30 */     return of(-range, range, 0);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 35 */     if (this.plateau == 0 && this.maxInclusive == -this.minInclusive)
/* 37 */       return random.nextInt(this.maxInclusive + 1) - random.nextInt(this.maxInclusive + 1); 
/* 40 */     int range = this.maxInclusive - this.minInclusive;
/* 41 */     if (this.plateau == range)
/* 42 */       return Mth.randomBetweenInclusive(random, this.minInclusive, this.maxInclusive); 
/* 45 */     int plateauStart = (range - this.plateau) / 2;
/* 46 */     int plateauEnd = range - plateauStart;
/* 47 */     return this.minInclusive + Mth.randomBetweenInclusive(random, 0, plateauEnd) + Mth.randomBetweenInclusive(random, 0, plateauStart);
/*    */   }
/*    */   
/*    */   public MapCodec<TrapezoidInt> codec() {
/* 52 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 57 */     return "trapezoid(" + this.plateau + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\TrapezoidInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */