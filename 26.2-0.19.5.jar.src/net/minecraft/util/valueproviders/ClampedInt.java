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
/*    */ public final class ClampedInt extends Record implements IntProvider {
/*    */   private final IntProvider source;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static final MapCodec<ClampedInt> MAP_CODEC;
/*    */   
/*    */   public ClampedInt(IntProvider source, int minInclusive, int maxInclusive) {
/* 10 */     this.source = source;
/* 10 */     this.minInclusive = minInclusive;
/* 10 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/valueproviders/ClampedInt;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/ClampedInt;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/ClampedInt;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/ClampedInt;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/ClampedInt;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/ClampedInt;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public IntProvider source() {
/* 10 */     return this.source;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProviders.CODEC.fieldOf("source").forGetter(ClampedInt::source), (App)Codec.INT.fieldOf("min_inclusive").forGetter(ClampedInt::minInclusive), (App)Codec.INT.fieldOf("max_inclusive").forGetter(ClampedInt::maxInclusive)).apply((Applicative)i, ClampedInt::new)).validate(u -> (u.maxInclusive < u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */   
/*    */   public static ClampedInt of(IntProvider source, int minInclusive, int maxInclusive) {
/* 23 */     return new ClampedInt(source, minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public int sample(RandomSource random) {
/* 28 */     return Mth.clamp(this.source.sample(random), this.minInclusive, this.maxInclusive);
/*    */   }
/*    */   
/*    */   public int minInclusive() {
/* 33 */     return Math.max(this.minInclusive, this.source.minInclusive());
/*    */   }
/*    */   
/*    */   public int maxInclusive() {
/* 38 */     return Math.min(this.maxInclusive, this.source.maxInclusive());
/*    */   }
/*    */   
/*    */   public MapCodec<ClampedInt> codec() {
/* 43 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\ClampedInt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */