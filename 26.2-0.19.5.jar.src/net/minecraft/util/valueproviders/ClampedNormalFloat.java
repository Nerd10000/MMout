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
/*    */ public final class ClampedNormalFloat extends Record implements FloatProvider {
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   public static final MapCodec<ClampedNormalFloat> MAP_CODEC;
/*    */   
/*    */   public ClampedNormalFloat(float mean, float deviation, float min, float max) {
/* 10 */     this.mean = mean;
/* 10 */     this.deviation = deviation;
/* 10 */     this.min = min;
/* 10 */     this.max = max;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/ClampedNormalFloat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/ClampedNormalFloat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/ClampedNormalFloat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/ClampedNormalFloat;
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
/*    */   public float min() {
/* 10 */     return this.min;
/*    */   }
/*    */   
/*    */   public float max() {
/* 10 */     return this.max;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mean").forGetter(ClampedNormalFloat::mean), (App)Codec.FLOAT.fieldOf("deviation").forGetter(ClampedNormalFloat::deviation), (App)Codec.FLOAT.fieldOf("min").forGetter(ClampedNormalFloat::min), (App)Codec.FLOAT.fieldOf("max").forGetter(ClampedNormalFloat::max)).apply((Applicative)i, ClampedNormalFloat::new)).validate(c -> (c.max < c.min) ? DataResult.error(()) : DataResult.success(c));
/*    */   }
/*    */   
/*    */   public static ClampedNormalFloat of(float mean, float deviation, float min, float max) {
/* 24 */     return new ClampedNormalFloat(mean, deviation, min, max);
/*    */   }
/*    */   
/*    */   public float sample(RandomSource random) {
/* 29 */     return sample(random, this.mean, this.deviation, this.min, this.max);
/*    */   }
/*    */   
/*    */   public static float sample(RandomSource random, float mean, float deviation, float min, float max) {
/* 33 */     return Mth.clamp(Mth.normal(random, mean, deviation), min, max);
/*    */   }
/*    */   
/*    */   public MapCodec<ClampedNormalFloat> codec() {
/* 38 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\ClampedNormalFloat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */