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
/*    */ public final class UniformFloat extends Record implements FloatProvider {
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   public static final MapCodec<UniformFloat> MAP_CODEC;
/*    */   
/*    */   public UniformFloat(float min, float max) {
/* 11 */     this.min = min;
/* 11 */     this.max = max;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/valueproviders/UniformFloat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/valueproviders/UniformFloat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/valueproviders/UniformFloat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/valueproviders/UniformFloat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float min() {
/* 11 */     return this.min;
/*    */   }
/*    */   
/*    */   public float max() {
/* 11 */     return this.max;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min_inclusive").forGetter(UniformFloat::min), (App)Codec.FLOAT.fieldOf("max_exclusive").forGetter(UniformFloat::max)).apply((Applicative)i, UniformFloat::new)).validate(u -> (u.max <= u.min) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */   
/*    */   public static UniformFloat of(float min, float max) {
/* 24 */     if (max <= min)
/* 25 */       throw new IllegalArgumentException("Max must exceed min"); 
/* 27 */     return new UniformFloat(min, max);
/*    */   }
/*    */   
/*    */   public float sample(RandomSource random) {
/* 32 */     return Mth.randomBetween(random, this.min, this.max);
/*    */   }
/*    */   
/*    */   public MapCodec<UniformFloat> codec() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 42 */     return "[" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\UniformFloat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */