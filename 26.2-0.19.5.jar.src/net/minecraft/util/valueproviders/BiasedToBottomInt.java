/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class BiasedToBottomInt extends Record implements IntProvider {
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static final MapCodec<BiasedToBottomInt> MAP_CODEC;
/*    */   
/*    */   public BiasedToBottomInt(int minInclusive, int maxInclusive) {
/*  9 */     this.minInclusive = minInclusive;
/*  9 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/BiasedToBottomInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/BiasedToBottomInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/BiasedToBottomInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/BiasedToBottomInt;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int minInclusive() {
/*  9 */     return this.minInclusive;
/*    */   }
/*    */   
/*    */   public int maxInclusive() {
/*  9 */     return this.maxInclusive;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("min_inclusive").forGetter(BiasedToBottomInt::minInclusive), (App)Codec.INT.fieldOf("max_inclusive").forGetter(BiasedToBottomInt::maxInclusive)).apply((Applicative)i, BiasedToBottomInt::new)).validate(u -> (u.maxInclusive < u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */   
/*    */   public static BiasedToBottomInt of(int minInclusive, int maxInclusive) {
/* 21 */     return new BiasedToBottomInt(minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 26 */     return this.minInclusive + random.nextInt(random.nextInt(this.maxInclusive - this.minInclusive + 1) + 1);
/*    */   }
/*    */   
/*    */   public MapCodec<BiasedToBottomInt> codec() {
/* 31 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\BiasedToBottomInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */